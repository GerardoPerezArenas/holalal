package es.altia.flexia.integracion.moduloexterno.melanbide83;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InformeAtaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InformeAtase_No_AceptadasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.dao.MeLanbide83DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.i18n.MeLanbide83I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide83.manager.MeLanbide83Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide83.util.ConstantesMeLanbide83;
import es.altia.flexia.integracion.moduloexterno.melanbide83.util.MeLanbide83Util;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InfoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;


public class MELANBIDE83 extends ModuloIntegracionExterno {
    private static Logger log = LogManager.getLogger(MELANBIDE83.class);
    private static final ResourceBundle properties = ResourceBundle.getBundle(ConstantesMeLanbide83.getFICHERO_PROPIEDADES());
    private static final MeLanbide83Util m83Util = new MeLanbide83Util();
    
     public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }     

   
    public String cargarPantallaListaFacturas(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        log.debug("Entramos en cargarPantallaListaFacturas de " + this.getClass().getName());
        InfoDesplegableVO desplEstado = new InfoDesplegableVO();
        InfoDesplegableVO desplConcepto = new InfoDesplegableVO();
        InfoDesplegableVO desplSN = new InfoDesplegableVO();
        ArrayList<FacturaVO> relacionFacturas = null;
        MeLanbide83Manager meLanbide83Manager = null;
        AdaptadorSQLBD adapt = null;
        int ejercicio;
        String codProcedimiento; 
        String nombreModulo;
        
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String[] partes = numExpediente.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            meLanbide83Manager = MeLanbide83Manager.getInstance();
            nombreModulo = ConstantesMeLanbide83.getNOMBRE_MODULO();
            String codOrg = String.valueOf(codOrganizacion);
            int idioma = m83Util.getIdioma(request);
            // recuperamos la informaci�n de los desplegables necesarios
            desplEstado = meLanbide83Manager.recuperarCampoDesplegable(codOrg,idioma,ConstantesMeLanbide83.getPropVal_COD_DESPL_EST(codOrg,codProcedimiento));
            desplConcepto = meLanbide83Manager.recuperarCampoDesplegable(codOrg, idioma,ConstantesMeLanbide83.getPropVal_COD_DESPL_CTAT(codOrg,codProcedimiento,ejercicio));
            desplSN = meLanbide83Manager.recuperarCampoDesplegable(codOrg,idioma,ConstantesMeLanbide83.getPropVal_COD_DESPL_SN(codOrg,codProcedimiento));
            relacionFacturas = meLanbide83Manager.recuperarListaFacturas(numExpediente,ConstantesMeLanbide83.getNOMBRE_TABLA_FACTURAS(), codOrg,idioma,codProcedimiento, adapt);
            
        } catch (NumberFormatException nfe){
            log.error("Hay un error de formato en los datos de entrada", nfe);
        } catch(Exception ex) {
            log.error("Ha ocurrido un error al cargar los datos de la pantalla lista de facturas para el expediente "+numExpediente, ex);
        } finally {
            request.setAttribute("comboEstados", desplEstado);
            request.setAttribute("comboConceptos", desplConcepto);
            request.setAttribute("comboSiNo", desplSN);
            request.setAttribute("relacionFacturas", relacionFacturas);
        }
        return "/jsp/extension/melanbide83/listaFacturasJustificacion.jsp";
    }
    
    /**
     * Posibles c�digos:
     * 0 -> sin fallos
     * 1 -> error al crear la conexi�n a la BBDD
     * 2 -> error al dar de alta una factura
     * 3 -> erro al formatear los datos recibidos
     * 4 -> fallo en la operaci�n de alta
     * 5 -> fallo en la operaci�n de modificar
     * 6 -> error al modificar una factura
     * 9 -> error al recuperar el listado de facturas
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return 
     */
    public String guardarFactura(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,int idioma, HttpServletRequest request,HttpServletResponse response)
    {
        log.debug("Entramos en guardarFactura de " + this.getClass().getName());
        MeLanbide83Manager meLanbide83Manager;
        FacturaVO factura = new FacturaVO();
        AdaptadorSQLBD adapt;
        String codOrg;
        String codProcedimiento; 
        String numExp = numExpediente; 
        boolean exito = false;
        ArrayList<FacturaVO> lista = null;
        int identificador = -1;
        String error = "0";
        
        
        try
        {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");
            if(request.getParameter("id")!=null)
                identificador = Integer.parseInt(request.getParameter("id"));
            String codEstado = request.getParameter("estado");
            String fechaFactura = request.getParameter("fecha");
            String codConcepto = request.getParameter("codConcepto");
            String justificada = request.getParameter("justif");
            String observaciones = request.getParameter("observ");
            String importe = request.getParameter("importe");
            String imputada = request.getParameter("imputada");
            
            adapt = this.getAdaptSQLBD(String.valueOf(codOrg));
            String[] partes = numExp.split("/");
            codProcedimiento = partes[1];
            meLanbide83Manager = MeLanbide83Manager.getInstance();
            
            // construimos el objeto FacturaVO y guardamos los datos en la base de datos
            factura.setCodIdent(identificador);
            factura.setNumExpediente(numExp);
            factura.setCodEstado(codEstado);
            factura.setFecha(MeLanbide83Util.formattedStringToDate(fechaFactura));
            factura.setCodConcepto(codConcepto);
            factura.setImporte(Double.parseDouble(importe.replace(".", "").replace(",", ".")));
            factura.setCodEntregaJustif(justificada);
            factura.setObservaciones(observaciones);
            factura.setCodImputada(imputada);
            exito = meLanbide83Manager.guardarFactura(factura, ConstantesMeLanbide83.getNOMBRE_TABLA_FACTURAS(),ConstantesMeLanbide83.getNOMBRE_SEQ_FACTURAS(), adapt);
            if(!exito) {
                if(identificador==-1) error= "4";
                else error = "5";
            }
            // Recuperamos el listado de facturas en base de datos y devolvemos la respuesta a la petici�n
            lista = meLanbide83Manager.recuperarListaFacturas(numExp, ConstantesMeLanbide83.getNOMBRE_TABLA_FACTURAS(), codOrg, idioma, codProcedimiento, adapt);
            if(exito && lista==null) error = "9";
        } catch(SQLException ex) {
            if(identificador==-1) {
                error = "2";
                log.error("Ha ocurrido un error al a�adir una nueva factura para el expediente "+numExp, ex);
            } else {
                error = "6";
                log.error("Ha ocurrido un error al actualizar la factura de id " + identificador+ " para el expediente "+numExp, ex);
            }
        } catch(BDException ex) {
            error = "1";
        } catch (Exception nfe){
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            if(error.equals("0")){
                resultado.setAtributo("lista", lista);
                resultado.setAtributo("error", "0");
            } else {
                resultado.setAtributo("error", error);
            }
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
    }
    
    /**
     * Posibles c�digos:
     * 0 -> sin fallos
     * 1 -> error al crear la conexi�n a la BBDD
     * 3 -> error al formatear los datos recibidos
     * 7 -> fallo en la operaci�n de borrado
     * 8 -> error al borrar una factura
     * 9 -> error al recuperar el listado de facturas
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return 
     */
    public String eliminarFactura(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,int idioma,HttpServletRequest request,HttpServletResponse response)
    {
        log.debug("Entramos en eliminarFactura de " + this.getClass().getName());
        MeLanbide83Manager meLanbide83Manager;
        FacturaVO factura = new FacturaVO();
        AdaptadorSQLBD adapt;
        int ejercicio;
        String codOrg;
        String codProcedimiento; 
        String numExp = numExpediente; 
        boolean exito = false;
        ArrayList<FacturaVO> lista = null;
        int identificador = -1;
        String error = "0";        
        
        try
        {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");
            if(request.getParameter("id")!=null)
                identificador = Integer.parseInt(request.getParameter("id"));
            
            adapt = this.getAdaptSQLBD(String.valueOf(codOrg));
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            meLanbide83Manager = MeLanbide83Manager.getInstance();
            
            // construimos el objeto FacturaVO y guardamos los datos en la base de datos
            factura.setCodIdent(identificador);
            factura.setNumExpediente(numExp);
            exito = meLanbide83Manager.borrarFactura(factura, ConstantesMeLanbide83.getNOMBRE_TABLA_FACTURAS(), adapt);
            if(!exito) error = "7";
            // Recuperamos el listado de facturas en base de datos y devolvemos la respuesta a la petici�n
            lista = meLanbide83Manager.recuperarListaFacturas(numExp, ConstantesMeLanbide83.getNOMBRE_TABLA_FACTURAS(), codOrg,idioma, codProcedimiento, adapt);
            if(exito && lista==null) error = "9";
        } catch (NumberFormatException nfe){
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } catch(BDException ex) {
            error = "1";
        } catch(SQLException ex) {
            log.error("Ha ocurrido un error al eliminar la factura de id " + identificador + " para el expediente "+numExp, ex);
            error = "8";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            if(error.equals("0")){
                resultado.setAtributo("lista", lista);
                resultado.setAtributo("error", "0");
            } else {
                resultado.setAtributo("error", error);
            }
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
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
    
    /**
     * M�todo llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petici�n a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a trav�s del cual se
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void getListaFacturas(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,int idioma,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            try
            {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            }
            catch(Exception ex)
            {
                
            }
        
            int codIdioma = 1;    
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            String[] partes = numExpediente.split("/");
            
            String codProcedimiento = partes[1];
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide83Manager meLanbide44Manager = MeLanbide83Manager.getInstance();
            ArrayList<FacturaVO> facturas = null;
            
            facturas = meLanbide44Manager.recuperarListaFacturas(numExpediente, ConstantesMeLanbide83.getNOMBRE_TABLA_FACTURAS(), String.valueOf(codOrganizacion),idioma, codProcedimiento, adapt);
            
            String codigoOperacion = "0";
            
            //this.escribirListaFacturasRequest(codigoOperacion, facturas, null, codIdioma, response);
            GeneralValueObject resultado = new GeneralValueObject();
            resultado.setAtributo("lista", facturas);
            resultado.setAtributo("error", "0");
            
             this.retornarJSON(new Gson().toJson(resultado), response); 
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void descargarInformeInterno(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("descargarInformeInterno() : BEGIN");
        }
        String NombreInteresado = null;
        String documentoInteresado = "";
        int idiomaUsuario = 1;
        NumberFormat formatNumber = NumberFormat.getCurrencyInstance(Locale.ITALY);
        int numFila = 0;
        int numFila2 = 0;
        final int numColumnasMerge = 3;
        double subeTotal = 0;
        try {
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute(numExpediente);
                idiomaUsuario = usuario.getIdioma();
            } catch (Exception e) {
                log.error("Error al recuperar los datos del usuario desde la session : " + e.getMessage(), e);
            }

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1");
            }

            log.info("Leemos template con el classloader");
            InputStream template = getClass().getClassLoader().getResourceAsStream("/es/altia/flexia/integracion/moduloexterno/melanbide83/reports/templates/informeInternoATASE_Template.xls");
            log.info("Lectura InputStream template correcta ");

            HSSFWorkbook libro = new HSSFWorkbook(template, true);//new HSSFWorkbook();

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.1");
            }

            HSSFPalette palette = libro.getCustomPalette();
            short colorIndex = 60;
            //Definimos el color que queremos de fondo para las celdas de cabecera.
            palette.setColorAtIndex(colorIndex, (byte) 142, (byte) 180, (byte) 227);
            HSSFCellStyle my_style = libro.createCellStyle();
            /* Create HSSFFont object from the workbook */
            HSSFFont my_font = libro.createFont();
            /* set the weight of the font */
            my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            /* attach the font to the style created earlier */
            my_style.setFont(my_font);
            //Color de fondo Azul
            //Adaptamos el color de finido pro nosotros a las celdas.
            my_style.setFillForegroundColor(colorIndex);
            my_style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            //Bordes para las celdas de cabecera.
            my_style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            my_style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            my_style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            my_style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            HSSFCellStyle estiloImportes = libro.createCellStyle();
            estiloImportes.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            HSSFCellStyle estiloConceptos = libro.createCellStyle();
            estiloConceptos.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
            estiloConceptos.setWrapText(true);
            estiloConceptos.setShrinkToFit(true);

            HSSFCellStyle estiloObservaciones = libro.createCellStyle();
            estiloObservaciones.setWrapText(true);

            if (log.isDebugEnabled()) {
                log.info("descargarInformeInterno() : 1.2");
            }

            log.info("Renombramos hojas del template");
            libro.setSheetName(0, MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.hoja1"));
            libro.setSheetName(1, MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.hoja2"));

            log.info("cogemos las hojas del template");
            HSSFSheet hoja = libro.getSheet(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.hoja1"));
            HSSFSheet hoja2 = libro.getSheet(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.hoja2"));

            log.info("Iniciamos tratamiento de las Hojas");
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.3");
            }
            /**
             * *******
             * CABECERAS
            ********
             */

            //recupero los datos a mostrar
            AdaptadorSQLBD adapt = null;
            try {
                //adapt = this.getAdaptSQLBD_GestionErores();
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            Connection con = null;
            con = adapt.getConnection();
            TerceroVO terceroVO = MeLanbide83Manager.getInstance().getDatosTerceroXNroExpediente(numExpediente, con);
            NombreInteresado = (terceroVO.getNombre()
                    + (terceroVO.getApellido1() != null && !terceroVO.getApellido1().isEmpty() && !terceroVO.getApellido1().equalsIgnoreCase("null") ? (" " + terceroVO.getApellido1()) : "")
                    + (terceroVO.getApellido2() != null && !terceroVO.getApellido2().isEmpty() && !terceroVO.getApellido2().equalsIgnoreCase("null") ? (" " + terceroVO.getApellido2()) : ""));
            log.info(" Preparar Informe ATASE : NombreInteresado : " + NombreInteresado);
            documentoInteresado = terceroVO.getDoc();
            log.info(" Preparar Informe ATASE : documentoInteresado : " + documentoInteresado);

            //MESES (fila 1)
            int contCelda = 0;
            HSSFRow fila;
            HSSFCell celda;


            fila = hoja.createRow(numFila++);

            //celda en blanco
            hoja.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.nro.expediente"));
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(numExpediente);

            contCelda += 2;
       
            celda = fila.createCell(contCelda);

            fila = hoja.createRow(numFila++);

            contCelda = 0;
            fila = hoja.createRow(numFila++);
            hoja.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.beneficiario"));
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(NombreInteresado);

            fila = hoja.createRow(numFila++);
            contCelda = 0;
            hoja.setColumnWidth(contCelda, (short) 4000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.dni"));
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, (short) 4000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(documentoInteresado);

            //celda en blanco
            //Tabla de expediente y beneficiario
            fila = hoja.createRow(numFila++);
            contCelda = 0;
            //celda en blanco

            fila = hoja.createRow(numFila++);hoja.setColumnWidth(contCelda, (short) 6500);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.justificante.pago"));
            celda.setCellStyle(my_style);
            
            hoja.setColumnWidth(contCelda, (short) 5000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.imputada"));
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, (short) 5000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("CONCEPTO");
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("IMPORTE");
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("OBSERVACIONES");
            celda.setCellStyle(my_style);
            
            hoja.getPrintSetup().setLandscape(true);
            hoja.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

            ///Cabecera Hoja2
            fila = hoja2.createRow(numFila2++);

            //fila = hoja2.createRow(numFila2++);
            contCelda = 0;


            
            //fila = hoja2.createRow(numFila2++);
            //fila = hoja2.createRow(numFila2++);
            //fila = hoja2.createRow(numFila2++);

           
            //celda en blanco
            hoja2.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.nro.expediente"));
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(numExpediente);

            contCelda += 2;
       
            celda = fila.createCell(contCelda);

            fila = hoja2.createRow(numFila2++);

            contCelda = 0;
            fila = hoja2.createRow(numFila2++);
            hoja2.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.beneficiario"));
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(NombreInteresado);

            fila = hoja2.createRow(numFila2++);
            contCelda = 0;
            hoja2.setColumnWidth(contCelda, (short) 4000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.dni"));
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, (short) 4000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(documentoInteresado);
            
            fila = hoja2.createRow(numFila2++);
            
            contCelda = 0;
            fila = hoja2.createRow(numFila2++);
            hoja2.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("ENTREGA JUSTIFICACI�N");
            celda.setCellStyle(my_style);
            
            hoja2.setColumnWidth(contCelda, (short) 5000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("IMPUTADA");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("CONCEPTO");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, (short) 4000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("IMPORTE");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, (short) 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("OBSERVACIONES");
            celda.setCellStyle(my_style);

            hoja2.getPrintSetup().setLandscape(true);
            hoja2.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

            // hoja.autoSizeColumn(0);
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (Exception e) {

            }

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 2");
            }

            /**
             * ****
             * DATOS
            *****
             */
            //recupero los datos a mostrar
            adapt = null;
            
            try {
                //adapt = this.getAdaptSQLBD_GestionErores();
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                con = null;
                try {
                    con = adapt.getConnection();

                    List<InformeAtaseVO> listaDatosInformeInternoResueltas = MeLanbide83DAO.getInstance().getListaAtase(numExpediente, con);
                    //Facturas No Pagadas
                    List<InformeAtase_No_AceptadasVO> listaDatosFacturasNoPagadas = MeLanbide83DAO.getInstance().getListaAtaseNoPagadas(numExpediente, con);
                    //Nombre del Interesado Segun el expediente Proporcionado.
                    NombreInteresado = MeLanbide83DAO.getInstance().getNombreInteresado(numExpediente, con);
                    double totalAyuda = MeLanbide83DAO.getInstance().getTotalAyuda(numExpediente, con);
                    log.info("totalAyuda " + totalAyuda);

                    if (log.isDebugEnabled()) {
                        log.debug("descargarInformeInterno() : 4");
                    }
                    //la lista de datosInformeGeneral est� ordenada por unidades pero no por meses y a�os
                    double importe;
                    double sumatorio = 0.00;
                    double importe2;
                    double sumatorio2 = 0.00;

                    //numFila=5;//7
                    for (InformeAtaseVO datosInformeInterno : listaDatosInformeInternoResueltas) {
                        fila = hoja.createRow(numFila++);
                        //muestro la unidad en la primera celda

                        celda = fila.createCell(0);
                        celda.setCellValue(datosInformeInterno.getENTREGADO_JUSTIF());

                        celda = fila.createCell(1);
                        celda.setCellValue(datosInformeInterno.getIMPUTADA());

                        celda = fila.createCell(2);
                        celda.setCellValue(datosInformeInterno.getCODIGO_CONCEPTO());

                        celda = fila.createCell(3);
                        importe = datosInformeInterno.getIMPORTE();
                        celda.setCellValue(formatNumber.format(importe));
                        celda.setCellStyle(estiloImportes);
                        sumatorio = sumatorio + importe;

                        celda = fila.createCell(4);
                        celda.setCellValue(datosInformeInterno.getOBSERVACIONES());
                      
                    }

                    for (InformeAtase_No_AceptadasVO datosInformeInterno : listaDatosFacturasNoPagadas) {
                        fila = hoja2.createRow(numFila2++);
                        //muestro la unidad en la primera celda

                        //muestro la unidad en la primera celda
                        celda = fila.createCell(0);
                        celda.setCellValue(datosInformeInterno.getENTREGADO_JUSTIF());

                        celda = fila.createCell(1);
                        celda.setCellValue(datosInformeInterno.getIMPUTADA());

                        celda = fila.createCell(2);
                        celda.setCellValue(datosInformeInterno.getCODIGO_CONCEPTO());

                        celda = fila.createCell(3);
                        importe2 = datosInformeInterno.getIMPORTE();
                        celda.setCellValue(formatNumber.format(importe2));
                        celda.setCellStyle(estiloImportes);
                        sumatorio2 = sumatorio2 + importe2;

                        celda = fila.createCell(4);
                        celda.setCellValue(datosInformeInterno.getOBSERVACIONES());
                        celda.setCellStyle(estiloObservaciones);

                    }

                    ///Total Sumatorio de la columna Importe 
                    fila = hoja.createRow(numFila++);
                    hoja.setColumnWidth(contCelda, (short) 6000);
                    celda = fila.createCell(3);
                    celda.setCellValue("TOTAL JUSTIFICADO");
                    celda.setCellStyle(my_style);

                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell(3);
                    celda.setCellValue(formatNumber.format(sumatorio));
                    celda.setCellStyle(estiloImportes);

                    fila = hoja.createRow(numFila++);
                    hoja.setColumnWidth(contCelda, (short) 6000);
                    celda = fila.createCell(3);
                    celda.setCellValue("TOTAL AYUDA");
                    celda.setCellStyle(my_style);

                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell(3);
                    //double doble = Double.parseDouble(totalAyuda);
                    celda.setCellValue(formatNumber.format(totalAyuda));
                    celda.setCellStyle(estiloImportes);                
                    
                    fila = hoja.createRow(numFila++);
                    hoja.setColumnWidth(contCelda, (short) 6000);
                    celda = fila.createCell(3);
                    celda.setCellValue("PENDIENTE JUSTIFICAR");
                    celda.setCellStyle(my_style);

                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell(3);
                    celda.setCellValue(formatNumber.format(totalAyuda-sumatorio));
                    celda.setCellStyle(estiloImportes); 
                    
                    ///Total Sumatorio de la columna Importe (hoja 2) 
                    fila = hoja2.createRow(numFila2++);
                    hoja.setColumnWidth(contCelda, (short) 6000);
                    celda = fila.createCell(3);
                    celda.setCellValue("TOTAL");
                    celda.setCellStyle(my_style);

                    fila = hoja2.createRow(numFila2++);
                    celda = fila.createCell(3);
                    celda.setCellValue(formatNumber.format(sumatorio2));
                    celda.setCellStyle(estiloImportes);

                    //conjunto de celdas despues del total
                    fila = hoja.createRow(numFila++);

                    //Ponemos el salto de pagina en la linea de conceptos.
                    hoja.setRowBreak(fila.getRowNum());

                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    hoja.setColumnWidth(contCelda, (short) 6000);
                    celda = fila.createCell(contCelda++);
                    celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos"));
                    celda.setCellStyle(my_style);
                    fila = hoja.createRow(numFila++);
                    //Preparamos los estilos para las celdas de la notas de conceptos
                    CellRangeAddress rangoMismaFila3Columnas;

                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    // Como sabemos que estos son largos lo hacemos manual, mientras vemos la forma de automatizarlo
                    celda.getRow().setHeightInPoints(25);
                    celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.1"));

                    contCelda = contCelda + 3;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide83I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.2"));

                 
                } catch (Exception e) {
                    log.error("Error al recuperar tramites: " + e.getMessage());
                } finally {
                    try {
                        if (con != null) {

                            adapt.devolverConexion(con);
                            log.info(">>>> devuelve con");
                        }
                    } catch (Exception e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 6");
            }
            //AUTOAJUSTAR CONTENIDO
            //TODO: mejorar autosize porque deja textos en dos filas..
            for (int j = 0; j < contCelda; j++) {
                //hoja.autoSizeColumn(j);
            }

            /**
             * ***********
             * CREAR FICHERO
            ************
             */
            crearFichero(libro, response);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el informe interno ", ex);
        }
        if (log.isDebugEnabled()) {
            log.debug("descargarInformeInterno() : END");
        }
    }
    
    private void crearFichero(HSSFWorkbook libro, HttpServletResponse response) throws Exception {
          
        ByteArrayOutputStream excelOutput = new ByteArrayOutputStream();
        libro.write(excelOutput);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=informeInterno.xls");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setContentLength(excelOutput.size());
        response.getOutputStream().write(excelOutput.toByteArray(), 0, excelOutput.size());
        response.getOutputStream().flush();
        response.getOutputStream().close();

    }
    
    
}
