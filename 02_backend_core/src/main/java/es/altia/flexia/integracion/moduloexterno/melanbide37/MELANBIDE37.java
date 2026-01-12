package es.altia.flexia.integracion.moduloexterno.melanbide37;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide37.manager.MeLanbide37Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide37.i18n.MeLanbide37I18n;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.http.HttpServletRequest;
import es.altia.flexia.integracion.moduloexterno.melanbide37.util.*;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCompAPAVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCompCPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoExpedientesEMPNLVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.Melanbide37RelacionExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.ExpedienteCEPAPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.ExpCEPAPCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.JSONArray;

/**
 *
 * @author laura
 */
public class MELANBIDE37  extends ModuloIntegracionExterno{
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE37.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    
     //Constantes de la clase
    private final String BARRA                              = "/";
    
    private final String MODULO_INTEGRACION = "/MODULO_INTEGRACION/";
    private final String PANTALLA_EXPEDIENTE_SALIDA = "/PANTALLA_EXPEDIENTE/SALIDA";
 
    
    public String generarClaveCP (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente){
        if(log.isDebugEnabled()) log.error("generarClaveCP ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide37Manager meLanbide37Manager = MeLanbide37Manager.getInstance();
        //cargarParametrosBásicosEnRequest(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request);
        //String redireccion = null;
        String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        
        try{           
            if(numExpediente!=null && !"".equals(numExpediente)){
                String[] datos          = numExpediente.split(BARRA);
                String ejercicio        = datos[0];
                String codProcedimiento = datos[1];
                Integer ano = MeLanbide37Manager.getInstance().obtieneFechaPresentacion(codOrganizacion, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(ano != null)
                {
                    //String[] dat = fecha.toString().split(BARRA);
                    //ejercicio = ano.toString();
                    //redireccion = es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_SALIDA, this.getNombreModulo());
                    if (!meLanbide37Manager.existeClave(codOrganizacion, clave, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)))){
                        clave = MeLanbide37Manager.getInstance().generarClave(numExpediente, codOrganizacion, ano.toString(),
                                this.getAdaptSQLBD(String.valueOf(codOrganizacion)).getConnection());
                        //CONTADOR POR AÑO                

                        //FLBPRU.E_TXT  
                        //FLBPRU.MELANBIDE03_CERTIFICADO - COD_CERTIFICADO
                        boolean correcto = false;
                        if (clave !=null)//existe certificado
                            correcto = MeLanbide37Manager.getInstance().guardarClaveCP(codOrganizacion, clave, numExpediente, ejercicio, ano.toString(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                        else  {
                            codigoOperacion ="1";
                            mensaje = MeLanbide37I18n.getInstance().getMensaje(1, "error.certificadoNoExiste");
                        }
                    }
                }
            }
            
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            mensaje = MeLanbide37I18n.getInstance().getMensaje(1, "error.errorGen");
        }
        
        if(log.isDebugEnabled()) log.error("generarClaveCP() : END");
        return codigoOperacion;
    }
    
   public String generarClaveAPA (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente){
        if(log.isDebugEnabled()) log.error("generarClaveAPA ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide37Manager meLanbide37Manager = MeLanbide37Manager.getInstance();
        
        String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        
        try{           
            if(numExpediente!=null && !"".equals(numExpediente)){
                String[] datos          = numExpediente.split(BARRA);
                String ejercicio        = datos[0];
                String codProcedimiento = datos[1];
               
                 String resultado= null;
                 resultado = MeLanbide37Manager.getInstance().guardarClavesAPA(numExpediente, codOrganizacion, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                 //if (resultado!=0)
                 //    codigoOperacion ="1";
                 codigoOperacion = resultado;
                /*clave = MeLanbide37Manager.getInstance().obtenerClaveAPA(numExpediente, codOrganizacion,
                        this.getAdaptSQLBD(String.valueOf(codOrganizacion)).getConnection());
                //CONTADOR POR AÑO                
                //FLBPRU.MELANBIDE03_CERTIFICADO - COD_CERTIFICADO
                boolean correcto = false;
                if (clave !=null)//existe certificado
                    correcto = MeLanbide37Manager.getInstance().guardarClaveAPA(codOrganizacion, clave, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                else  {
                    codigoOperacion ="1";
                    //mensaje = MeLanbide37I18n.getInstance().getMensaje(1, "error.certificadoNoExiste");
                }*/
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            mensaje = MeLanbide37I18n.getInstance().getMensaje(1, "error.errorGen");
        }
        if(log.isDebugEnabled()) log.error("generarClaveAPA() : END");
        return codigoOperacion;
    }
    
    /**
     * Carga parámetros necesarios en la request.
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request 
     */
    /*private void cargarParametrosBásicosEnRequest(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request){
        if(log.isDebugEnabled()) log.error("cargarParametrosBásicosEnRequest() : BEGIN");
            if(log.isDebugEnabled()) log.error("Almacenamos el nombre del módulo en la request");
            request.setAttribute("nombreModulo", ConfigurationParameter.getParameter(ConstantesMeLanbide37.NOMBRE_MODULO, ConstantesMeLanbide37.FICHERO_PROPIEDADES));
        if(log.isDebugEnabled()) log.error("cargarParametrosBásicosEnRequest() : END");
    }//cargarParametrosBásicosEnRequest
    */
     /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
        if(log.isDebugEnabled()) log.error("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.error("getJndi =========> ");
            log.error("parametro codOrganizacion: " + codOrganizacion);
            log.error("gestor: " + gestor);
            log.error("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.error("He cogido el jndi: " + jndiGenerico);
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
                log.error("Error :  " + te.getMessage(),te);
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                log.error("Error :  " + e.getMessage(),e);
            }finally{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            }// finally
            if(log.isDebugEnabled()) log.error("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    
    public void generarListadoCP(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.error("GENERAR_LISTADO_CP");
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try
        {
            String tipoacreditacion = request.getParameter("tipoacred");
            String valoracion = request.getParameter("valoracion");
            String codigoCP = request.getParameter("codigoCP");
            String fecDesde = request.getParameter("fecSoliDesde");
            String fecHasta = request.getParameter("fecSoliHasta");
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) 
                    {
                        idioma = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            
            MeLanbide37I18n meLanbide39I18n = MeLanbide37I18n.getInstance();
            MeLanbide37Manager manager = MeLanbide37Manager.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide37.FORMATO_FECHA);
            
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
            
                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try 
                {
                    hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                    if (hssfColor == null )
                    {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                }
                catch (Exception e) 
                {
                    log.error("Error :  " + e.getMessage(),e);
                }
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                //List<String> numerosExp = manager.getNumerosExpedientes(ano, adapt);
               // List<FilaInformeResumenPuestosContratadosVO> listaPuestos = null;
                
                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)180);
            
                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)200);
            
                HSSFSheet hoja = null;
                int numFila = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                hoja = libro.createSheet();
                estiloCelda = libro.createCellStyle();
                hoja=generarEstructuraNuevaHojaExcelListadoCP(hoja, numFila, idioma, estiloCelda, hssfColor, negritaTitulo);

                
                //estilos celdas DATOS
                HSSFCellStyle estiloCeldaColumnaPrimeraFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaPrimera.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaPrimera.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaPrimeraFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                HSSFCellStyle estiloCeldaColumnaPrimeraFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaIntermedia.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaPrimeraFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                HSSFCellStyle estiloCeldaColumnaPrimeraFilaUltima = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaUltima.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaUltima.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                HSSFCellStyle estiloCeldaColumnaIntermediaFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaIntermediaFilaPrimera.setWrapText(true);
                estiloCeldaColumnaIntermediaFilaPrimera.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaIntermediaFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                HSSFCellStyle estiloCeldaColumnaIntermediaFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaIntermediaFilaIntermedia.setWrapText(true);     
                estiloCeldaColumnaIntermediaFilaIntermedia.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaIntermediaFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                HSSFCellStyle estiloCeldaColumnaIntermediaFilaUltima = libro.createCellStyle();
                estiloCeldaColumnaIntermediaFilaUltima.setWrapText(true);
                estiloCeldaColumnaIntermediaFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaIntermediaFilaUltima.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                HSSFCellStyle estiloCeldaColumnaUltimaFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaPrimera.setWrapText(true);
                estiloCeldaColumnaUltimaFilaPrimera.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaPrimera.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaUltimaFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                HSSFCellStyle estiloCeldaColumnaUltimaFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaUltimaFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                HSSFCellStyle estiloCeldaColumnaUltimaFilaUltima = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaUltima.setWrapText(true);
                estiloCeldaColumnaUltimaFilaUltima.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaUltima.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                numFila++;
                
                //
                
                //datos                
                List<FilaListadoCPVO> listaCP = null;                                
                int p = 0;
                
                Integer limiteFilasHojaExcel = 0;
                try {
                    limiteFilasHojaExcel = Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide37.NUMERO_LINEAS_HOJA_EXCEL, ConstantesMeLanbide37.FICHERO_PROPIEDADES));
                } catch (Exception ex) {
                    log.error("Error al recueprar el limite de Lineas Excel Listados CEPAP : " + ex.getMessage(), ex);
                    limiteFilasHojaExcel = 0;
                }

                if (limiteFilasHojaExcel == null || limiteFilasHojaExcel.equals("")) {
                    limiteFilasHojaExcel = 0;
                }
                log.error(" generarListadoComprobacionAPA - Limite fila por hoja marcado Properties : " + limiteFilasHojaExcel + " - Si es cero no se ha indicado o no se pudo recuperar la propiedad.");
                
                listaCP = manager.getDatosListadoCP(tipoacreditacion, valoracion, codigoCP, fecDesde, fecHasta,adapt);
                for(FilaListadoCPVO filaI : listaCP)
                {
                    if(limiteFilasHojaExcel>0 && numFila>limiteFilasHojaExcel){
                        log.error(" generarListadoComprobacionAPA - Creamos Nueva Hoja - Limite de Registros por hoja Superado, Linea actual : " + numFila + " - Limite : " + limiteFilasHojaExcel);
                        numFila = 0;
                        HSSFSheet nuevaHoja = libro.createSheet();
                        HSSFCellStyle nuevoEstiloCelda = libro.createCellStyle();
                        hoja = generarEstructuraNuevaHojaExcelListadoCP(nuevaHoja, numFila, idioma,nuevoEstiloCelda,hssfColor,negritaTitulo);
                        numFila++;
                        log.error(" generarListadoComprobacionAPA - Reiniciado contador de Filas y continuamos insertando datos filas");
                    }
                    
                    fila = hoja.createRow(numFila);

                    /*estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    if(p == listaCP.size() - 1 )
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(p == listaCP.size()-1)
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }*/
                    
                    celda = fila.createCell(0);
                    celda.setCellValue(filaI.getNumExp());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaPrimeraFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaPrimeraFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaPrimeraFilaIntermedia);
                    
                    celda = fila.createCell(1);                    
                    celda.setCellValue(filaI.getFecSolicitud() != null ? filaI.getFecSolicitud()+"":""); 
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getNifInteresado());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getNomInteresado());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getFecNacimiento() != null ? filaI.getFecNacimiento()+"":"");
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getTipoAcreditacion());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(6);
                    celda.setCellValue(filaI.getValoracionCPAPA());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(7);
                    celda.setCellValue(filaI.getCentroExp());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(8);
                    celda.setCellValue(filaI.getCodigoCP());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(9);
                    celda.setCellValue(filaI.getDenoCpCas());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(10);
                    celda.setCellValue(filaI.getDenoCpEus());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                                        
                    celda = fila.createCell(11);
                    celda.setCellValue(filaI.getClaveRegistralCP());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                                        
                    celda = fila.createCell(12);
                    celda.setCellValue(filaI.getRealDecreto());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                                        
                    celda = fila.createCell(13);
                    celda.setCellValue(filaI.getFecRealDecreto() != null?filaI.getFecRealDecreto().toString():"");
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                                        
                    celda = fila.createCell(14);
                    celda.setCellValue(filaI.getRealDecretoModif());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                                        
                    celda = fila.createCell(15);
                    celda.setCellValue(filaI.getFecRealDecretoModif() != null?filaI.getFecRealDecretoModif().toString():"");                    
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                                        
                    celda = fila.createCell(16);
                    celda.setCellValue(filaI.getTramiteActivo());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(17);//nuevo modulo practicas
                    celda.setCellValue(filaI.getCodModuloPrac());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(18);
                    celda.setCellValue(filaI.getDesModuloPracCas());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(19);
                    celda.setCellValue(filaI.getDesModuloPracEus());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(20);
                    celda.setCellValue(filaI.getAcreditadoModuloPrac());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(21);
                    celda.setCellValue(filaI.getMotivoAcredModuloPrac());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(22);
                    celda.setCellValue(filaI.getDesOrigenGaituz());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(23);
                    celda.setCellValue(filaI.getEnviadoSilcoi()!=null && filaI.getEnviadoSilcoi().equalsIgnoreCase("1") ? "Si/Bai" : "");
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    // datos provinvia y municipio 20160119
                    celda = fila.createCell(24);
                    celda.setCellValue(filaI.getCodProvicia());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(25);
                    celda.setCellValue(filaI.getProvincia());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(26);
                    celda.setCellValue(filaI.getCodMuncipio());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(27);
                    celda.setCellValue(filaI.getMunicipio());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaIntermediaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaIntermediaFilaIntermedia);
                    
                    celda = fila.createCell(28);
                    celda.setCellValue(filaI.getTitRecogido());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaUltimaFilaPrimera);
                    else if (p==listaCP.size()-1) celda.setCellStyle(estiloCeldaColumnaUltimaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaUltimaFilaIntermedia);

                     numFila++;
                     p++;
                }
                
                
                //fin datos
            
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("listadoCP", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();
                
                FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

                int size = (int) informe.length(); 
                byte[] data = new byte[size]; 
                bstr.read( data, 0, size ); 
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");  
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } 
            catch (Exception ioe) 
            {
                log.error("EXCEPCION listado CP");
                log.error("Error :  " + ioe.getMessage(),ioe);
                
            }
        }
        catch(Exception ex)
        {
            log.error("EXCEPCION listado CP");
            log.error("Error :  " + ex.getMessage(),ex);
            
        }
    }
    
    
    public String cargarCriteriosFiltroListadoCP(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
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
        try
        {
            String codDesplegable=ConstantesMeLanbide37.CODIGOS_DESPLEGABLE.TIPO_ACREDITACION;
            List<SelectItem> listaTipoAcreditacion =  MeLanbide37Manager.getInstance().getListaDesplegable(codDesplegable, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("lstTipoAcreditacion", listaTipoAcreditacion);
                
            codDesplegable=ConstantesMeLanbide37.CODIGOS_DESPLEGABLE.VALORACION;
            List<SelectItem> listaValoracion =  MeLanbide37Manager.getInstance().getListaDesplegable(codDesplegable, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("lstValoracion", listaValoracion);
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide37/procesos/criteriosListadoCP.jsp";
    }
    
    public String cargarCriteriosFiltroListadoCompCP(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
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
        return "/jsp/extension/melanbide37/procesos/criteriosListadoCompCP.jsp";
    }
    
    
    public void generarListadoComprobacionCP(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try
        {           
            log.error("generarListadoComprobacionCP ");
            String codigoCP = request.getParameter("codigoCP");
            String fecDesde = request.getParameter("fecSoliDesde");
            String fecHasta = request.getParameter("fecSoliHasta");
            log.error("codigoCP "+codigoCP);
            log.error("fecSoliDesde "+fecDesde);
            log.error("fecSoliHasta "+fecHasta);
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) 
                    {
                        idioma = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            
            MeLanbide37I18n meLanbide39I18n = MeLanbide37I18n.getInstance();
            MeLanbide37Manager manager = MeLanbide37Manager.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide37.FORMATO_FECHA);
            
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
            
                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try 
                {
                    hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                    if (hssfColor == null )
                    {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                }
                catch (Exception e) 
                {
                    log.error("Error :  " + e.getMessage(),e);
                }
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                //List<String> numerosExp = manager.getNumerosExpedientes(ano, adapt);
               // List<FilaInformeResumenPuestosContratadosVO> listaPuestos = null;
                
                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)180);
            
                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)200);
            
                HSSFSheet hoja = null;
                int numFila = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                hoja = libro.createSheet();

                hoja.setColumnWidth(0, 6000);
                hoja.setColumnWidth(1, 4000);
                hoja.setColumnWidth(2, 10000);
                hoja.setColumnWidth(3, 4000);
                hoja.setColumnWidth(4, 6000);
                hoja.setColumnWidth(5, 4000);
                hoja.setColumnWidth(6, 4000);
                hoja.setColumnWidth(7, 10000);
                hoja.setColumnWidth(8, 10000);
                hoja.setColumnWidth(9, 6000);
                hoja.setColumnWidth(10, 6000);
                hoja.setColumnWidth(11, 4000);
                hoja.setColumnWidth(12, 8000);
                hoja.setColumnWidth(13, 8000);
                hoja.setColumnWidth(14, 6000);
                                
                fila = hoja.createRow(numFila);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);
                                
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);
                                               
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompCP.col14").toUpperCase());
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                celda.setCellStyle(estiloCelda);
                
                numFila++;
                
                //datos                
                List<FilaListadoCompCPVO> listaCompCP = null;                                
                int p = 0;
                
                listaCompCP = manager.getDatosListadoCompCP(codigoCP, fecDesde, fecHasta,adapt);
                for(FilaListadoCompCPVO filaI : listaCompCP)
                {
                    fila = hoja.createRow(numFila);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    if(p == listaCompCP.size() - 1 )
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(p == listaCompCP.size()-1)
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    
                    celda = fila.createCell(0);
                    celda.setCellValue(filaI.getNumExp());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell(1);                    
                    celda.setCellValue(filaI.getFecSolicitud() != null ? filaI.getFecSolicitud()+"":"");
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getNomInteresado());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getFecNacimiento() != null ? filaI.getFecNacimiento()+"":"");
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getValoracionCPAPA());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getCentroExp());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell(6);
                    celda.setCellValue(filaI.getCodigoCP());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell(7);
                    celda.setCellValue(filaI.getDenoCpCas());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell(8);
                    celda.setCellValue(filaI.getDenoCpEus());
                    celda.setCellStyle(estiloCelda);
                                        
                    celda = fila.createCell(9);
                    celda.setCellValue(filaI.getClaveRegistralCP());
                    celda.setCellStyle(estiloCelda);
                                        
                    celda = fila.createCell(10);
                    celda.setCellValue(filaI.getRealDecreto());
                    celda.setCellStyle(estiloCelda);
                                        
                    celda = fila.createCell(11);
                    celda.setCellValue(filaI.getFecRealDecreto() != null?filaI.getFecRealDecreto().toString():"");
                    celda.setCellStyle(estiloCelda);
                                        
                    celda = fila.createCell(12);
                    celda.setCellValue(filaI.getRealDecretoModif());
                    celda.setCellStyle(estiloCelda);
                                        
                    celda = fila.createCell(13);
                    celda.setCellValue(filaI.getFecRealDecretoModif() != null?filaI.getFecRealDecretoModif().toString():"");
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    celda.setCellStyle(estiloCelda);
                                        
                     numFila++;
                     p++;
                }
                
                
                //fin datos
            
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("listadoComprobacionCP", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();
                
                FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

                int size = (int) informe.length(); 
                byte[] data = new byte[size]; 
                bstr.read( data, 0, size ); 
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");  
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } 
            catch (Exception ioe) 
            {
                log.error("EXCEPCION listado Comprobación CP");
                log.error("Error :  " + ioe.getMessage(),ioe);
                
            }
        }
        catch(Exception ex)
        {
            log.error("EXCEPCION listado Comprobación CP");
            log.error("Error :  " + ex.getMessage(),ex);
            
        }
    }
    
    public void generarListadoComprobacionAPA(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try
        {           
            String codigoCP = request.getParameter("codigoCP");
            String fecDesde = request.getParameter("fecSoliDesde");
            String fecHasta = request.getParameter("fecSoliHasta");
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) 
                    {
                        idioma = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            
            MeLanbide37I18n meLanbide39I18n = MeLanbide37I18n.getInstance();
            MeLanbide37Manager manager = MeLanbide37Manager.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide37.FORMATO_FECHA);
            
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
            
                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try 
                {
                    hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                    if (hssfColor == null )
                    {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                }
                catch (Exception e) 
                {
                    log.error("Error :  " + e.getMessage(),e);
                }
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                //List<String> numerosExp = manager.getNumerosExpedientes(ano, adapt);
               // List<FilaInformeResumenPuestosContratadosVO> listaPuestos = null;
                
                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)180);
            
                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)200);
            
                HSSFSheet hoja = null;
                int numFila = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                hoja = libro.createSheet();
                estiloCelda = libro.createCellStyle();
                hoja = generarEstructuraNuevaHojaExcelListadoTodasUC(hoja, numFila, idioma,estiloCelda,hssfColor,negritaTitulo);
                
                //estilos celdas DATOS
                HSSFCellStyle estiloCeldaColumnaPrimeraFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaPrimera.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaPrimera.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaPrimeraFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle estiloCeldaColumnaPrimeraFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaIntermedia.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaPrimeraFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle estiloCeldaColumnaPrimeraFilaUltima = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaUltima.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaUltima.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle estiloCeldaColumnaIntermediaFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaIntermediaFilaPrimera.setWrapText(true);
                estiloCeldaColumnaIntermediaFilaPrimera.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaIntermediaFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle estiloCeldaColumnaIntermediaFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaIntermediaFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaIntermediaFilaIntermedia.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaIntermediaFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle estiloCeldaColumnaIntermediaFilaUltima = libro.createCellStyle();
                estiloCeldaColumnaIntermediaFilaUltima.setWrapText(true);
                estiloCeldaColumnaIntermediaFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaIntermediaFilaUltima.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle estiloCeldaColumnaUltimaFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaPrimera.setWrapText(true);
                estiloCeldaColumnaUltimaFilaPrimera.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaPrimera.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaUltimaFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle estiloCeldaColumnaUltimaFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaUltimaFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                HSSFCellStyle estiloCeldaColumnaUltimaFilaUltima = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaUltima.setWrapText(true);
                estiloCeldaColumnaUltimaFilaUltima.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaUltima.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                numFila++;
                
                //datos                
                List<FilaListadoCompAPAVO> listaCompAPA = null;                                
                int p = 0;
                        
                Integer limiteFilasHojaExcel = 0;
                try {
                    limiteFilasHojaExcel=Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide37.NUMERO_LINEAS_HOJA_EXCEL, ConstantesMeLanbide37.FICHERO_PROPIEDADES));
                } catch (Exception ex) {
                    log.error("Error al recueprar el limite de Lineas Excel Listados CEPA : " + ex.getMessage(), ex);
                    limiteFilasHojaExcel=0;
                }
                        
                if(limiteFilasHojaExcel == null || limiteFilasHojaExcel.equals(""))
                    limiteFilasHojaExcel=0;
                log.error(" generarListadoComprobacionAPA - Limite fila por hoja marcado Properties : " + limiteFilasHojaExcel + " - Si es cero no se ha indicado o no se pudo recuperar la propiedad.");
                
                listaCompAPA = manager.getDatosListadoCompAPA(codigoCP, fecDesde, fecHasta,adapt);
                log.error(" generarListadoComprobacionAPA - Creado Libro y Hoja - Vamos a insertar Lineas recuperadas : " + listaCompAPA.size());
                for(FilaListadoCompAPAVO filaI : listaCompAPA)
                {
                    if(limiteFilasHojaExcel>0 && numFila>limiteFilasHojaExcel){
                        log.error(" generarListadoComprobacionAPA - Creamos Nueva Hoja - Limite de Registros por hoja Superado, Linea actual : " + numFila + " - Limite : " + limiteFilasHojaExcel);
                        numFila = 0;
                        HSSFSheet nuevaHoja = libro.createSheet();
                        HSSFCellStyle nuevoEstiloCelda = libro.createCellStyle();
                        hoja = generarEstructuraNuevaHojaExcelListadoTodasUC(nuevaHoja, numFila, idioma,nuevoEstiloCelda,hssfColor,negritaTitulo);
                        numFila++;
                        log.error(" generarListadoComprobacionAPA - Reiniciado contador de Filas y continuamos insertando datos filas");
                    }
                    fila = hoja.createRow(numFila);

                    /*estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    if(p == listaCompAPA.size() - 1 )
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(p == listaCompAPA.size()-1)
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    */
                    
                    celda = fila.createCell(0);
                    celda.setCellValue(filaI.getNumExp());
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaPrimeraFilaPrimera);
                    else if (p==listaCompAPA.size()-1) celda.setCellStyle(estiloCeldaColumnaPrimeraFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaPrimeraFilaIntermedia);
                    
                    celda = fila.createCell(1);                    
                    celda.setCellValue(filaI.getFecSolicitud() != null ? filaI.getFecSolicitud()+"":"");
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                                        
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getNifInteresado());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getNomInteresado());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getFecNacimiento() != null ? filaI.getFecNacimiento()+"":"");
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getValoracionCPAPA());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(6);
                    celda.setCellValue(filaI.getCentroExp());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(7);
                    celda.setCellValue(filaI.getCodigoCP());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(8);
                    celda.setCellValue(filaI.getDenoCpCas());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(9);
                    celda.setCellValue(filaI.getDenoCpEus());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                                        
                    celda = fila.createCell(10);
                    celda.setCellValue(filaI.getClaveRegistralCP());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                                        
                    celda = fila.createCell(11);
                    celda.setCellValue(filaI.getRealDecreto());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                                        
                    celda = fila.createCell(12);
                    celda.setCellValue(filaI.getFecRealDecreto() != null?filaI.getFecRealDecreto().toString():"");
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                                        
                    celda = fila.createCell(13);
                    celda.setCellValue(filaI.getRealDecretoModif());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                                        
                    celda = fila.createCell(14);
                    celda.setCellValue(filaI.getFecRealDecretoModif() != null?filaI.getFecRealDecretoModif().toString():"");
                    //estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(15);
                    celda.setCellValue(filaI.getCodigoUC() != null?filaI.getCodigoUC().toString():"");
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                                        
                    celda = fila.createCell(16);
                    celda.setCellValue(filaI.getDenoUcCas());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                                        
                    celda = fila.createCell(17);
                    celda.setCellValue(filaI.getDenoUcEus());
                    //estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(18);
                    celda.setCellValue(filaI.getCentroUc());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                      
                    celda = fila.createCell(19);
                    celda.setCellValue(filaI.getAcreditacion());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                      
                    celda = fila.createCell(20);
                    celda.setCellValue(filaI.getAcreditado());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));                     
                    
                    celda = fila.createCell(21);
                    celda.setCellValue(filaI.getOrigenAcreditacion());
                    //estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(22);
                    celda.setCellValue(filaI.getClaveRegistralUC()!=null&&!filaI.getClaveRegistralUC().equals("null")?filaI.getClaveRegistralUC():"");
                    //estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(23);
                    celda.setCellValue(filaI.getTramiteActivo());
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    /*//moduilo practicas
                    celda = fila.createCell((short)23);
                    celda.setCellValue(filaI.getCodModuloPrac());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell((short)24);
                    celda.setCellValue(filaI.getDesModuloPracCas());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell((short)25);
                    celda.setCellValue(filaI.getDesModuloPracEus());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell((short)26);
                    celda.setCellValue(filaI.getAcreditado());
                    celda.setCellStyle(estiloCelda);
                    
                    celda = fila.createCell((short)27);
                    celda.setCellValue(filaI.getMotivoAcredModuloPrac());
                    celda.setCellStyle(estiloCelda);
                    */
                    
                    // nuevos datos origen gaituz y enviado a silcoi
                    celda = fila.createCell(24);
                    celda.setCellValue(filaI.getDesOrigenGaituz());
                    //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    celda.setCellStyle(defineEstiloCeldasColumnasIntermedias(listaCompAPA.size(),p,estiloCeldaColumnaIntermediaFilaPrimera,estiloCeldaColumnaIntermediaFilaUltima,estiloCeldaColumnaIntermediaFilaIntermedia));
                    
                    celda = fila.createCell(25);
                    celda.setCellValue(filaI.getEnviadoSilcoi()!=null && filaI.getEnviadoSilcoi().equalsIgnoreCase("1")?"Si/Bai":"");
                    //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    if (p==0) celda.setCellStyle(estiloCeldaColumnaUltimaFilaPrimera);
                    else if (p==listaCompAPA.size()-1) celda.setCellStyle(estiloCeldaColumnaUltimaFilaUltima);
                    else celda.setCellStyle(estiloCeldaColumnaUltimaFilaIntermedia);
                    
                    
                    //estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                     numFila++;
                     p++;
                }
                log.error(" generarListadoComprobacionAPA - Fin Preparacion Excel con datos - ");        
                
                //fin datos
            
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("listadoUcs", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();
                
                FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

                int size = (int) informe.length(); 
                byte[] data = new byte[size]; 
                bstr.read( data, 0, size ); 
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");  
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } 
            catch (Exception ioe) 
            {
                log.error("EXCEPCION listado Comprobación APA");
                log.error("Error :  " + ioe.getMessage(),ioe);
                
            }
        }
        catch(Exception ex)
        {
            log.error("EXCEPCION listado Comprobación APA");
            log.error("Error :  " + ex.getMessage(),ex);
            
        }
    }
    
    // actualiza datos de UC en vista materializada
    public void actualizaDatosTodasUC_VistaMate(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error("noError-actualizaDatosTodasUC_VistaMate - BEGIN");
        String codigoOperacion = "0";
        //Recojo los parametros
        String salidaProcedure = "";
        Date fechaActualizacion = new Date();
        String fechaActualizacionFormat = "";
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide37.FORMATO_FECHA_HORA24);
        try {
            fechaActualizacionFormat = format.format(fechaActualizacion);                
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide37Manager manager = MeLanbide37Manager.getInstance();
            String nombreVistaMaterializada  = ConfigurationParameter.getParameter(ConstantesMeLanbide37.NOMBRE_VISTA_MATERIALIZADA_TODAS_UC, ConstantesMeLanbide37.FICHERO_PROPIEDADES);
            log.error("NoError - Nombre Vista Materializada para Listado todas UC : " + nombreVistaMaterializada);
            if (nombreVistaMaterializada==null || nombreVistaMaterializada.equals("")) {
                log.error("No se ha podido recuperar el nombre de la vista materializada desde el properties MELANBIDE37");
                codigoOperacion = "1";
                salidaProcedure="No se ha podido recuperar el nombre de la vista materializada desde el properties MELANBIDE37 al intentar actualizar los datos para el listado de todas UC";
            } else {
                salidaProcedure = manager.actualizaDatosTodasUC_VistaMate(nombreVistaMaterializada,adapt);
            }
        } catch (Exception ex) {
            log.error("actualizaDatosTodasUC_VistaMate- Error al generar el xml para enviar a jsp de actualizacion de datos de listado UC",ex);
            codigoOperacion = "2";
            salidaProcedure="Exception general presentada al intentar actualizar los datos para el listado de todas UC";
        }
        
        if(salidaProcedure.isEmpty()){
            codigoOperacion="3";
            salidaProcedure="No se ha podido realizar la llamada al Procedure que actualiza los datos para generar el listado de todas las UC";
        }else{
            if(salidaProcedure.equals("0")){
                codigoOperacion=salidaProcedure;
                salidaProcedure="Datos de la vista actualizados correctamente. Pude generar el listado de todas las UC";
            }else{
                codigoOperacion="4";
            }
                
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<DESCRIPCION_OPERACION>");
        xmlSalida.append(salidaProcedure);
        xmlSalida.append("</DESCRIPCION_OPERACION>");
        xmlSalida.append("<FECHA_OPERACION>");
        xmlSalida.append(fechaActualizacionFormat);
        xmlSalida.append("</FECHA_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("actualizaDatosTodasUC_VistaMate- Error al generar el xml para enviar a jsp de actualizacion de datos de listado UC", e);
            log.error("Error :  " + e.getMessage(),e);
        }//try-catch
        log.error("noError-actualizaDatosTodasUC_VistaMate - END");
    }
    public void listadoRelaciones(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){        

        try{            
            log.error("En funcion listadoRelaciones ");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            HSSFWorkbook libro = new HSSFWorkbook();
            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFSheet hoja = null;
            int numFila = 0;
            int totalesAdjudicadas = 0;
            boolean adjudicada = false;
            String rutaArchivoSalida = null;
            ResourceBundle m_Conf = ResourceBundle.getBundle("common");
            Date control = new Date();
            
            ArrayList<Melanbide37RelacionExpVO> datos = MeLanbide37Manager.getInstance().getRelacionExp(adapt);
            if(datos != null)
            {                
                hoja = libro.createSheet("RELACION_EXPEDIENTES");
                hoja.setColumnWidth(0, 5000);
                hoja.setColumnWidth(1, 5000);
                hoja.setColumnWidth(2, 5000);
                hoja.setColumnWidth(3, 3000);
                hoja.setColumnWidth(4, 7000);
                hoja.setColumnWidth(5, 3000);
                hoja.setColumnWidth(6, 7000);
                HSSFRow fila = hoja.createRow(numFila);
                
                //Cabeceras
                HSSFCellStyle estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                //HSSFFont negrita = libro.createFont();
                //negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                //estiloCelda.setFont(negrita);
                HSSFCell celda = fila.createCell(0);
                celda.setCellValue("NUM EXP");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(1);
                celda.setCellValue("NUM EXP_REL");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(2);
                celda.setCellValue("FECHA SOLICITUD");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(3);
                celda.setCellValue("NIF");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(4);
                celda.setCellValue("NOMBRE");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(5);
                celda.setCellValue("COD CP");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(6);
                celda.setCellValue("CP");
                celda.setCellStyle(estiloCelda);
                numFila ++;
                //HSSFRow fila = hoja.createRow(numFila);
                
                // 2017/12/21 Evitamos crear estilos en el for, Esta limitado a 4000 por hoja,
                HSSFCellStyle estiloCeldaDatosAligxDefault = libro.createCellStyle();
                estiloCeldaDatosAligxDefault.setWrapText(true);
                HSSFCellStyle estiloCeldaDatosAligCenter = libro.createCellStyle();
                estiloCeldaDatosAligCenter.setWrapText(true);
                estiloCeldaDatosAligCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                for(Melanbide37RelacionExpVO rel: datos){
                    fila = hoja.createRow(numFila);

                    celda = fila.createCell(0);
                    celda.setCellValue(rel.getCodExpediente() != null ? rel.getCodExpediente() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);

                    celda = fila.createCell(1);
                    celda.setCellValue(rel.getCodExpedienteRel()!= null ? rel.getCodExpedienteRel() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);

                    celda = fila.createCell(2);
                    celda.setCellValue(rel.getFechaSoli()!= null ? rel.getFechaSoli() : "");
                    celda.setCellStyle(estiloCeldaDatosAligCenter);
                    
                    celda = fila.createCell(3);
                    celda.setCellValue(rel.getNif()!= null ? rel.getNif() : "");
                    celda.setCellStyle(estiloCeldaDatosAligCenter);
                    
                    celda = fila.createCell(4);
                    celda.setCellValue(rel.getNombreApe()!= null ? rel.getNombreApe() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);
                    
                    celda = fila.createCell(5);
                    celda.setCellValue(rel.getCodigoCP()!= null ? rel.getCodigoCP() : "");
                    celda.setCellStyle(estiloCeldaDatosAligCenter);
                    
                    celda = fila.createCell(6);
                    celda.setCellValue(rel.getDescCP()!= null ? rel.getDescCP(): "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);
                    numFila ++;
                }
                    File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                    File informe = File.createTempFile("resolucionCentros", ".xls", directorioTemp);

                    FileOutputStream archivoSalida = new FileOutputStream(informe);
                    libro.write(archivoSalida);
                    archivoSalida.close();

                    rutaArchivoSalida = informe.getAbsolutePath();

                    FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                    BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

                    int size = (int) informe.length(); 
                    byte[] data = new byte[size]; 
                    bstr.read( data, 0, size ); 
                    bstr.close();

                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                    response.setHeader("Content-Transfer-Encoding", "binary");  
                    response.setContentLength(data.length);
                    response.getOutputStream().write(data, 0, data.length);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
            }            
        }
        catch(Exception ex){
            log.error("Error en listadoRelaciones: " + ex);
        }
    }
    /*public void  enviarCriteriosListadoCP (int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
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
        
        String codigoOperacion = "0";
        String tipoAcreditacion ="";
        String valoracion ="";
        String codigoCP ="";
        String fecDesde ="";
        String fecHasta ="";
        try
        {            
            //Recojo los parámetros
            tipoAcreditacion = (String)request.getParameter("tipoAcreditacion");
            valoracion = (String)request.getParameter("valoracion");
            codigoCP = (String)request.getParameter("codigoCP");
            fecDesde = (String)request.getParameter("fecSoliDesde");
            fecHasta = (String)request.getParameter("fecSoliHasta");
            
        }catch(Exception ex)
        {
            log.error("Error :  " + ex.getMessage(),ex);
            codigoOperacion = "2";
        } 
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
            xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<DATOS>");
            xmlSalida.append("<TIPO_ACREDITACION>");
                xmlSalida.append(tipoAcreditacion);
            xmlSalida.append("</TIPO_ACREDITACION>");
            xmlSalida.append("<VALORACION>");
                xmlSalida.append(valoracion);
            xmlSalida.append("</VALORACION>");
            xmlSalida.append("<CODIGOCP>");
                xmlSalida.append(codigoCP);
            xmlSalida.append("</CODIGOCP>");
            xmlSalida.append("<FECSOLIDESDE>");
                xmlSalida.append(fecDesde);
            xmlSalida.append("</FECSOLIDESDE>");
            xmlSalida.append("<FECSOLIHASTA>");
                xmlSalida.append(fecHasta);
            xmlSalida.append("</FECSOLIHASTA>");
        xmlSalida.append("</DATOS>");        
        xmlSalida.append("</RESPUESTA>");
        
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Error :  " + e.getMessage(),e);
        }//try-catch
    }*/
    
    public void listadoExpedientesEMPNL(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        try {
            log.error(" Control - Begin listadoExpedientesEMPNL - ");
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
                log.error(" Error al recuperar datos del idioma desde la session - listadoExpedientesEMPNL");
            }
            MeLanbide37I18n meLanbide37I18n = MeLanbide37I18n.getInstance();
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            HSSFWorkbook libro = new HSSFWorkbook();
            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFSheet hoja = null;
            int numFila = 0;
            //int totalesAdjudicadas = 0;
            //boolean adjudicada = false;
            String rutaArchivoSalida = null;
            ResourceBundle m_Conf = ResourceBundle.getBundle("common");
            //Date control = new Date();

            ArrayList<FilaListadoExpedientesEMPNLVO> datos = MeLanbide37Manager.getInstance().listadoExpedientesEMPNL(adapt);
            if (datos != null) {
                hoja = libro.createSheet("EXPEDIENTES_EXCENCION_EMPNL");
                hoja.setColumnWidth( 0,  5000);
                hoja.setColumnWidth( 1,  4000);
                hoja.setColumnWidth( 2,  12000);
                hoja.setColumnWidth( 3,  15000);
                hoja.setColumnWidth( 4,  7000);
                hoja.setColumnWidth( 5,  15000);
                hoja.setColumnWidth( 6,  15000);
                hoja.setColumnWidth( 7,  7000);
                hoja.setColumnWidth( 8,  7000);
                hoja.setColumnWidth( 9,  4000);
                hoja.setColumnWidth( 10,  15000);
                hoja.setColumnWidth( 11,  15000);
                HSSFRow fila = hoja.createRow(numFila);

                //Cabeceras
                HSSFCellStyle estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                HSSFCell celda = fila.createCell( 0);
                
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 1);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 2);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 3);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 4);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 5);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 6);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 7);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 8);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 9);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 10);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(negrita);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell( 11);
                celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "doc.ListadoEMPNL.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                // 2017/12/21 Evitamos crear estilos en el for, Esta limitado a 4000 por hoja,
                HSSFCellStyle estiloCeldaDatosAligxDefault = libro.createCellStyle();
                estiloCeldaDatosAligxDefault.setWrapText(true);
                HSSFCellStyle estiloCeldaDatosAligCenter = libro.createCellStyle();
                estiloCeldaDatosAligCenter.setWrapText(true);
                estiloCeldaDatosAligCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                numFila++;
                //HSSFRow fila = hoja.createRow(numFila);
                for (FilaListadoExpedientesEMPNLVO rel : datos) {
                    fila = hoja.createRow(numFila);
                    
                    celda = fila.createCell(0);
                    celda.setCellValue(rel.getNumExpediente()!= null ? rel.getNumExpediente() : "");
                    celda.setCellStyle(estiloCeldaDatosAligCenter);

                    celda = fila.createCell(1);
                    celda.setCellValue(rel.getDocumento()!= null ? rel.getDocumento() : "");
                    celda.setCellStyle(estiloCeldaDatosAligCenter);

                    celda = fila.createCell(2);
                    celda.setCellValue(rel.getNombreCompleto()!= null ? rel.getNombreCompleto() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);

                    celda = fila.createCell( 3);
                    celda.setCellValue(rel.getDescMotivoExencion()!= null ? rel.getDescMotivoExencion() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);
                    
                    celda = fila.createCell(4);
                    celda.setCellValue(rel.getDescOrigenSolicitud()!= null ? rel.getDescOrigenSolicitud() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);

                    celda = fila.createCell(5);
                    celda.setCellValue(rel.getDescCentro()!= null ? rel.getDescCentro() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);

                    celda = fila.createCell(6);
                    celda.setCellValue(rel.getDescOficinaLanbide()!= null ? rel.getDescOficinaLanbide() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);

                    celda = fila.createCell(7);
                    celda.setCellValue(rel.getFechaSolicitud()!= null ? rel.getFechaSolicitud() : "");
                    celda.setCellStyle(estiloCeldaDatosAligCenter);
                    
                    celda = fila.createCell(8);
                    celda.setCellValue(rel.getDescValoracionExencion()!= null ? rel.getDescValoracionExencion() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);
                    
                    celda = fila.createCell(9);
                    celda.setCellValue(rel.getCodCertificado()!= null ? rel.getCodCertificado() : "");
                    celda.setCellStyle(estiloCeldaDatosAligCenter);
                    
                    celda = fila.createCell(10);
                    celda.setCellValue(rel.getDesCertificadoCas()!= null ? rel.getDesCertificadoCas() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);
                    
                    celda = fila.createCell(11);
                    celda.setCellValue(rel.getDesCertificadoEus()!= null ? rel.getDesCertificadoEus() : "");
                    celda.setCellStyle(estiloCeldaDatosAligxDefault);
                    
                    numFila++;
                }
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile(ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_NOMBRE_FICHERO_GENERADO, ConstantesMeLanbide37.FICHERO_PROPIEDADES), 
                        ConstantesMeLanbide37.DOT_PUNTO + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_EXTENSION_FICHERO_GENERADO, ConstantesMeLanbide37.FICHERO_PROPIEDADES),
                        directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                FileInputStream istr = new FileInputStream(rutaArchivoSalida);

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
            }
        log.error(" Control - END -  listadoExpedientesEMPNL - ");    
        } catch (Exception ex) {
            log.error("Error en listadoExpedientesEMPNL : " + ex);
        }
    }
    
    public void generarExcelOficina(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        
        try {
            FileInputStream istr = null;
        
            try {
                log.info(" Begin generarExcelOficina ");


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
                    log.error(" Error al recuperar datos del idioma desde la session - generarExcelOficina");
                }

                MeLanbide37I18n meLanbide37I18n = MeLanbide37I18n.getInstance();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

                HSSFWorkbook libro = new HSSFWorkbook();
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFSheet hoja = null;
                int numFila = 0;
                String rutaArchivoSalida = null;
                ResourceBundle m_Conf = ResourceBundle.getBundle("common");

                String oficina = request.getParameter("oficina").toString();

                ArrayList<ExpedienteCEPAPVO> datos = MeLanbide37Manager.getInstance().getExpedientesOficina(oficina,adapt);



                if (datos != null) {
                    hoja = libro.createSheet("CEPAP_Oficina");

                    hoja.setColumnWidth((short) 0, (short) 7000);//NUM_EXPEDIENTE
                    hoja.setColumnWidth((short) 1, (short) 6000);//DOC_INTERESADO
                    hoja.setColumnWidth((short) 2, (short) 18000);//NOMBRE_APELLIDOS
                    hoja.setColumnWidth((short) 3, (short) 6000);//CP
                    hoja.setColumnWidth((short) 4, (short) 15000);//LUGAR_ENVIO
                    hoja.setColumnWidth((short) 5, (short) 5000);//LOTE
                    hoja.setColumnWidth((short) 6, (short) 6000);//TITULO_ENTREGADO
                    hoja.setColumnWidth((short) 7, (short) 6000);//TELEFONO

                    HSSFRow fila = hoja.createRow(numFila);

                    //Cabecera
                    HSSFCellStyle estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(negrita);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    HSSFCell celda = fila.createCell((short) 0);
                    celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "entregaTitulo.expedientesOficina.expediente").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(negrita);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    celda = fila.createCell((short) 1);
                    celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "entregaTitulo.expedientesOficina.docInteresado").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(negrita);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    celda = fila.createCell((short) 2);
                    celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "entregaTitulo.expedientesOficina.nombreInteresado").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(negrita);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    celda = fila.createCell((short) 3);
                    celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "entregaTitulo.expedientesOficina.CP").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(negrita);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    celda = fila.createCell((short) 4);
                    celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "entregaTitulo.expedientesOficina.oficina").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(negrita);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    celda = fila.createCell((short) 5);
                    celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "entregaTitulo.expedientesOficina.lote").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(negrita);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    celda = fila.createCell((short) 6);
                    celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "entregaTitulo.expedientesOficina.titEntregado").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(negrita);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    celda = fila.createCell((short) 7);
                    celda.setCellValue(meLanbide37I18n.getMensaje(idioma, "entregaTitulo.expedientesOficina.telefono").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    // Evitamos crear estilos en el for, Esta limitado a 4000 por hoja
                    HSSFCellStyle estiloCeldaDatosAligxDefault = libro.createCellStyle();
                    estiloCeldaDatosAligxDefault.setWrapText(true);
                    HSSFCellStyle estiloCeldaDatosAligCenter = libro.createCellStyle();
                    estiloCeldaDatosAligCenter.setWrapText(true);
                    estiloCeldaDatosAligCenter.setAlignment(HSSFCellStyle.ALIGN_LEFT);

                    numFila++;

                    for (ExpedienteCEPAPVO rel : datos) {
                        fila = hoja.createRow(numFila);

                        celda = fila.createCell(0);
                        celda.setCellValue(rel.getNumeroExpediente()!= null ? rel.getNumeroExpediente() : "");
                        celda.setCellStyle(estiloCeldaDatosAligxDefault);

                        celda = fila.createCell(1);
                        celda.setCellValue(rel.getDocInteresado()!= null ? rel.getDocInteresado() : "");
                        celda.setCellStyle(estiloCeldaDatosAligxDefault);

                        celda = fila.createCell(2);
                        celda.setCellValue(rel.getNombreInteresado()!= null ? rel.getNombreInteresado() : "");
                        celda.setCellStyle(estiloCeldaDatosAligxDefault);

                        celda = fila.createCell( 3);
                        celda.setCellValue(rel.getCP()!= null ? rel.getCP() : "");
                        celda.setCellStyle(estiloCeldaDatosAligxDefault);

                        celda = fila.createCell(4);
                        celda.setCellValue(rel.getLugarEnvio()!= null ? rel.getLugarEnvio() : "");
                        celda.setCellStyle(estiloCeldaDatosAligxDefault);

                        celda = fila.createCell(5);
                        celda.setCellValue(rel.getLote()!= null ? rel.getLote() : "");
                        celda.setCellStyle(estiloCeldaDatosAligxDefault);

                        celda = fila.createCell(6);
                        celda.setCellValue(rel.getTituloEntregado()!= null ? rel.getTituloEntregado() : "");
                        celda.setCellStyle(estiloCeldaDatosAligxDefault);

                        celda = fila.createCell(7);
                        celda.setCellValue(rel.getTelefono()!= null ? rel.getTelefono() : "");
                        celda.setCellStyle(estiloCeldaDatosAligxDefault);

                        numFila++;
                    }

                    File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                    File informe = File.createTempFile("CEPAPExpedientesOficina", ".xls", directorioTemp);

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

                }
                log.info(" END generarExcelOficina ");
            } catch (Exception ioe) {
                log.error("Error en generarExcelOficina : " + ioe);
                ioe.printStackTrace();
            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (Exception e) {
            log.error("Error en generarExcelOficina : " + e);
            e.printStackTrace();
        }
        
    }
    
    public void busquedaFiltrandoListaExpedientes(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        List<ExpedienteCEPAPVO> lista = new ArrayList<ExpedienteCEPAPVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            ExpCEPAPCriteriosFiltroVO _criterioBusqueda = new ExpCEPAPCriteriosFiltroVO();
            //_criterioBusqueda.setFechaExpInicio((String) request.getParameter("meLanbide37Fecha_busqE"));
            //_criterioBusqueda.setFechaExpFin((String) request.getParameter("meLanbide37Fecha_busqS"));
            _criterioBusqueda.setDniInteresado((String) request.getParameter("dniInteresado_busq"));
            _criterioBusqueda.setNumeroExpediente((String) request.getParameter("numeroExpediente_busq"));
            lista = MeLanbide37Manager.getInstance().busquedaFiltrandoListaExpedientes(_criterioBusqueda, adapt);
            
        } catch (Exception ex) {
            log.error("Error al consultar lista de expedientes - CEPAP - Entrega título - : " + ex);
            codigoOperacion = "2";
        }
        StringBuffer xmlSalida = new StringBuffer();
       
        
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ExpedienteCEPAPVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<NUM_EXPEDIENTE>");
            xmlSalida.append(fila.getNumeroExpediente());
            xmlSalida.append("</NUM_EXPEDIENTE>");
            xmlSalida.append("<NOMBRE_APELLIDOS>");
            xmlSalida.append(fila.getNombreInteresado());
            xmlSalida.append("</NOMBRE_APELLIDOS>");
            xmlSalida.append("<CP>");
            xmlSalida.append(fila.getCP());
            xmlSalida.append("</CP>");
            xmlSalida.append("<LUGAR_ENVIO>");
            xmlSalida.append(fila.getLugarEnvio());
            xmlSalida.append("</LUGAR_ENVIO>");
            xmlSalida.append("<LOTE>");
            xmlSalida.append(fila.getLote());
            xmlSalida.append("</LOTE>");
            xmlSalida.append("<TITULO_ENTREGADO>");
            xmlSalida.append(fila.getTituloEntregado());
            xmlSalida.append("</TITULO_ENTREGADO>");

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
        } catch (Exception e) {
            log.error(" Error Recuperando datos en CEPAP - Título entregado - ", e);
        }//try-catch
    }
    
    public void marcarTitulosEntregado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String listaExpedientesMarcados = request.getParameter("listaExpedientesMarcados");
        String meLanbide37Fecha_busqS = request.getParameter("meLanbide37Fecha_busqS");
        
        JSONArray listaExpedientesMarcadosArr = new JSONArray(listaExpedientesMarcados);
        String[] listaExpedientesMarcadosStr = getStringArray(listaExpedientesMarcadosArr);
        
        String codCampo="";
        int numExpedientesMarcados = 0;
        String numExpedientesMarcadosStr = String.valueOf(numExpedientesMarcados);
        try {
            numExpedientesMarcados = MeLanbide37Manager.getInstance().marcarTitulosEntregado(codOrganizacion,codTramite, listaExpedientesMarcadosStr,codCampo, meLanbide37Fecha_busqS, adapt);
        
        } catch (Exception ex) {
            log.error("Error al marcar título entregado - CEPAP - Entrega título - : " + ex);
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
        } catch (Exception e) {
            log.error(" Error Recuperando datos de marcado de título entregado en CEPAP - Título entregado - ", e);
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

    private HSSFSheet generarEstructuraNuevaHojaExcelListadoTodasUC(HSSFSheet hoja, int numFila, Integer idioma,HSSFCellStyle estiloCelda,HSSFColor hssfColor,HSSFFont negritaTitulo) {
        log.error(" NoError - generarEstructuraNuevaHojaExcelListadoTodasUC -  BEGIN - ");   
        
        MeLanbide37I18n meLanbide39I18n = MeLanbide37I18n.getInstance();

        HSSFRow fila = null;
        HSSFCell celda = null;
        //HSSFCellStyle estiloCelda = null;

        hoja.setColumnWidth( 0,  6000);
        hoja.setColumnWidth( 1,  4000);
        hoja.setColumnWidth( 2,  4000);
        hoja.setColumnWidth( 3,  10000);
        hoja.setColumnWidth( 4,  4000);
        hoja.setColumnWidth( 5,  6000);
        hoja.setColumnWidth( 6,  4000);
        hoja.setColumnWidth( 7,  4000);
        hoja.setColumnWidth( 8,  10000);
        hoja.setColumnWidth( 9,  10000);
        hoja.setColumnWidth( 10,  6000);
        hoja.setColumnWidth( 11,  6000);
        hoja.setColumnWidth( 12,  4000);
        hoja.setColumnWidth( 13,  8000);
        hoja.setColumnWidth( 14,  8000);
        hoja.setColumnWidth( 15,  4000);
        hoja.setColumnWidth( 16,  10000);
        hoja.setColumnWidth( 17,  10000);
        hoja.setColumnWidth( 18,  4000);
        hoja.setColumnWidth( 19,  5000);
        hoja.setColumnWidth( 20,  8000);
        hoja.setColumnWidth( 21,  8000);
        hoja.setColumnWidth( 22,  8000);
        hoja.setColumnWidth( 23,  10000);
        /*//modulo practicas
         hoja.setColumnWidth((short)23, (short)4000);
         hoja.setColumnWidth((short)24, (short)8000);
         hoja.setColumnWidth((short)25, (short)8000);
         hoja.setColumnWidth((short)26, (short)4000);
         hoja.setColumnWidth((short)27, (short)8000);*/

        // nuevos datos enviado a origen gaituz y enviado a silcoi
        hoja.setColumnWidth( 24,  4000);
        hoja.setColumnWidth( 25,  4000);

        fila = hoja.createRow(numFila);

        //estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(hssfColor.getIndex());
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setFont(negritaTitulo);
        celda = fila.createCell( 0);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col1").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 1);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col2").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 2);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col3").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 3);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col4").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 4);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col5").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 5);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col6").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 6);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col7").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 7);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col8").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 8);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col9").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 9);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col10").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 10);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col11").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 11);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col12").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 12);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col13").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 13);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col14").toUpperCase());
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 14);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col15").toUpperCase());
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 15);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col16").toUpperCase());
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 16);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col17").toUpperCase());
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 17);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col18").toUpperCase());
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 18);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col19").toUpperCase());
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 19);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col20").toUpperCase());
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 20);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col21").toUpperCase());
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 21);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col22").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 22);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col23").toUpperCase());
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 23);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col24").toUpperCase());
        celda.setCellStyle(estiloCelda);

        /*//modulos practicas                
         celda = fila.createCell((short)23);
         celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col24").toUpperCase());
         celda.setCellStyle(estiloCelda);
                
         celda = fila.createCell((short)24);
         celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col25").toUpperCase());
         celda.setCellStyle(estiloCelda);
                
         celda = fila.createCell((short)25);
         celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col26").toUpperCase());
         celda.setCellStyle(estiloCelda);
                
         celda = fila.createCell((short)26);
         celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col27").toUpperCase());
         celda.setCellStyle(estiloCelda);
                
         celda = fila.createCell((short)27);
         celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col28").toUpperCase());
         celda.setCellStyle(estiloCelda);
         */
        // nuevos datos origen gaituz y enviado a sicoi
        celda = fila.createCell(24);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col30").toUpperCase());
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell( 25);
        celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCompAPA.col31").toUpperCase());
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        celda.setCellStyle(estiloCelda);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        
        log.error(" NoError - generarEstructuraNuevaHojaExcelListadoTodasUC -  END - "); 
        
        return  hoja;
    }

    private HSSFSheet generarEstructuraNuevaHojaExcelListadoCP(HSSFSheet hoja, int numFila, int idioma, HSSFCellStyle estiloCelda, HSSFColor hssfColor, HSSFFont negritaTitulo) {
        log.error(" NoError - generarEstructuraNuevaHojaExcelListadoCP -  BEGIN - ");   
        try {
            MeLanbide37I18n meLanbide39I18n = MeLanbide37I18n.getInstance();
            HSSFRow fila = null;
            HSSFCell celda = null;
            
            hoja.setColumnWidth(0, 6000);
            hoja.setColumnWidth(1, 4000);
            hoja.setColumnWidth(2, 4000);
            hoja.setColumnWidth(3, 10000);
            hoja.setColumnWidth(4, 4000);
            hoja.setColumnWidth(5, 4000);
            hoja.setColumnWidth(6, 6000);
            hoja.setColumnWidth(7, 4000);
            hoja.setColumnWidth(8, 4000);
            hoja.setColumnWidth(9, 10000);
            hoja.setColumnWidth(10, 10000);
            hoja.setColumnWidth(11, 6000);
            hoja.setColumnWidth(12, 6000);
            hoja.setColumnWidth(13, 4000);
            hoja.setColumnWidth(14, 8000);
            hoja.setColumnWidth(15, 8000);
            hoja.setColumnWidth(16, 10000);

            //modulos practicas
            hoja.setColumnWidth(17, 4000);
            hoja.setColumnWidth(18, 8000);
            hoja.setColumnWidth(19, 8000);
            hoja.setColumnWidth(20, 4000);
            hoja.setColumnWidth(21, 8000);

            // nuevos datos origen gaituz y enviado silcoi
            hoja.setColumnWidth(22, 4000);
            hoja.setColumnWidth(23, 4000);

            // nuevos provincia y 2016-01-19
            hoja.setColumnWidth(24, 4000);
            hoja.setColumnWidth(25, 8000);
            hoja.setColumnWidth(26, 4000);
            hoja.setColumnWidth(27, 8000);
            hoja.setColumnWidth(28, 5000);

            fila = hoja.createRow(numFila);

            //estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(hssfColor.getIndex());
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col1").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(1);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col2").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(2);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col3").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(3);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col4").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(4);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col5").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(5);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col6").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(6);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col7").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(7);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col8").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(8);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col9").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(9);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col10").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(10);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col11").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(11);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col12").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(12);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col13").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(13);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col14").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(14);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col15").toUpperCase());
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(15);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col16").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(16);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col17").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(17);//nuevo
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col18").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(18);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col19").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(19);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col20").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(20);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col21").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(21);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col22").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            // nuevos datos origen gaituz y enviado a silcoi 24062015
            celda = fila.createCell(22);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col23").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(23);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col24").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            // nuevos datos provincia y muncipio 20160119
            celda = fila.createCell(24);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col25").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(25);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col26").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(26);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col27").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(27);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col28").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            celda = fila.createCell(28);
            celda.setCellValue(meLanbide39I18n.getMensaje(idioma, "doc.ListadoCP.col29").toUpperCase());
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda.setCellStyle(estiloCelda);

            
        } catch (Exception e) {
            log.error("Error al preparar la estructura de la hoja de Datos para listado cp : " + e.getMessage(), e);
        }
        log.error(" NoError - generarEstructuraNuevaHojaExcelListadoCP -  END - ");
        return hoja;
    }

    private CellStyle defineEstiloCeldasColumnasIntermedias(int size, int p, HSSFCellStyle estiloCeldaColumnaIntermediaFilaPrimera, HSSFCellStyle estiloCeldaColumnaIntermediaFilaUltima, HSSFCellStyle estiloCeldaColumnaIntermediaFilaIntermedia) {
        try {
            
            if (p == 0) {
                return estiloCeldaColumnaIntermediaFilaPrimera;
            } else if (p == size - 1) {
                return estiloCeldaColumnaIntermediaFilaUltima;
            } else {
                return estiloCeldaColumnaIntermediaFilaIntermedia;
            }
        } catch (Exception e) {
            log.error("Error al asignar el estilo de las celdas" + e.getMessage(),e);
            return null;
        }
    }
    
}
