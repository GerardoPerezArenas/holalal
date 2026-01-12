package es.altia.flexia.integracion.moduloexterno.melanbide70;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide70.dao.MeLanbide70DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide70.i18n.MeLanbide70I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide70.manager.MeLanbide70Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide70.util.ConstantesMeLanbide70;
import es.altia.flexia.integracion.moduloexterno.melanbide70.util.MeLanbide70Util;
import es.altia.flexia.integracion.moduloexterno.melanbide70.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide70.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;


public class MELANBIDE70 extends ModuloIntegracionExterno {
    private static Logger log = LogManager.getLogger(MELANBIDE70.class);
    private static final ResourceBundle properties = ResourceBundle.getBundle(ConstantesMeLanbide70.getFICHERO_PROPIEDADES());
      
    public String actualizarDatosSuplementariosAPEI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException{
        log.error("actualizarDatosSuplementariosAPEI. INICIO - Expediente: "+ numExpediente + "  =================>");
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide70Manager meLanbide70Manager;
        ExpedienteVO datosExpediente;
        Integer edad;
        DatosEconomicosExpVO datosEcon;
        Integer importeSubvencion = null;
        Integer importePago = null;
        try{
            try{
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                con   = adapt.getConnection();
            }catch(Exception e){
                log.error("Error al obtener una conexi�n a la BBDD: " + e.getMessage());
                return "2";
            }
            meLanbide70Manager = MeLanbide70Manager.getInstance();
            // ini DATOS TERCERO
            try{
                datosExpediente = meLanbide70Manager.getDatosExpediente(codOrganizacion, numExpediente, con);
            }catch(Exception e){
                log.error("Error al obtener datos del tercero: " + e.getMessage());
                return "3";
            }
            //sean CB o no, se calcula la edad y el sexo
            if(datosExpediente!=null && datosExpediente.getFecPresentacion()!=null && datosExpediente.getTercero()!=null && datosExpediente.getTercero().getTFecNacimiento()!=null && datosExpediente.getTercero().getTSexoTercero()!=null){
                edad = MeLanbide70Util.calcularEdad(datosExpediente.getTercero().getTFecNacimiento(), datosExpediente.getFecPresentacion());
                adapt.inicioTransaccion(con);
                meLanbide70Manager.actualizaEdad(codOrganizacion, numExpediente, edad, con);
                meLanbide70Manager.actualizaSexo(codOrganizacion, numExpediente, datosExpediente.getTercero().getTSexoTercero(), con);
            // fin DATOS TERCERO
            // ini IMPORTE DE LA SUBVENCION
                if(datosExpediente.getCifCBSC()==null){//si no es CB calculo los importes. Si es CB se eliminar�n (por si hubiera por haber retrocedido expediente y cambiado el campo CIFCBSC...)
                //   ------------- Para coger el ejercicio de la fecha de presentacion en lugar de cogerlo del expediente ---------------- Rosa 2018/01/26 ------------------
                //  String [] arrNumExpediente = numExpediente.split("/");
                //  datosEcon = meLanbide70Manager.getImporteSubvencionYPorc(edad, datosExpediente.getTercero().getTSexoTercero(), arrNumExpediente[1], arrNumExpediente[0], con);
                    String [] arrNumExpediente = numExpediente.split("/");
                    String formato = "yyyy";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
                    datosEcon = meLanbide70Manager.getImporteSubvencionYPorc(edad, datosExpediente.getTercero().getTSexoTercero(), arrNumExpediente[1], dateFormat.format(datosExpediente.getFecPresentacion()), con);
                    importeSubvencion = datosEcon.getImporteSubvencion();
                    importePago = datosEcon.getImportePago();
                }
                meLanbide70Manager.actualizaImporteSubvencion(codOrganizacion, numExpediente, importeSubvencion, con);
            // fin IMPORTE DE LA SUBVENCION
            //ini IMPORTE PRIMER PAGO
            //    meLanbide70Manager.actualizaImportePrimerPago(codOrganizacion, numExpediente, importePago, con);
            //fin IMPORTE PRIMER PAGO
                adapt.finTransaccion(con);
            }
            else{
                return "4";
            }
            
        }catch(Exception e){
            log.error("ERROR.- actualizarDatosSuplementariosAPEI(). Error: " + e.getMessage());
            if(con!=null) adapt.rollBack(con);
            return "1";
        }
        finally{
            if (adapt != null){
                adapt.devolverConexion(con);                        
            }
        }    
        log.error(""
                + " "+ numExpediente + "  =================>");
        return "0";
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
