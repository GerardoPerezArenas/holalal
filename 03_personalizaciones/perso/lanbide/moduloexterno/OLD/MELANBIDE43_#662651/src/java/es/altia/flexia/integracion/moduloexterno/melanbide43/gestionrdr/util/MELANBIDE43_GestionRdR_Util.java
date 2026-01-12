/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.util;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class MELANBIDE43_GestionRdR_Util {
    
    //Logger
    private static Logger log = Logger.getLogger(MELANBIDE43_GestionRdR_Util.class);
    
    private static MELANBIDE43_GestionRdR_Util instance = null;
        
    public static MELANBIDE43_GestionRdR_Util getInstance(){
        if(log.isDebugEnabled()) log.info("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MELANBIDE43_GestionRdR_Util.class){
                if(instance == null){
                    instance = new MELANBIDE43_GestionRdR_Util();
                }//if(instance == null)
            }//synchronized(MeLanbide43Manager.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.error("getInstance() : END");
        return instance;
    }//getInstance

    public MELANBIDE43_GestionRdR_Util() {
    }
    
    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        log.info("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        
        if (log.isDebugEnabled()) {
            log.info("getJndi =========> ");
            log.info("parametro codOrganizacion: " + codOrganizacion);
            log.info("gestor: " + gestor);
            log.info("jndi: " + jndiGenerico);
        }

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.info("He cogido el jndi: " + jndiGenerico);
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
                log.error("*** AdaptadorSQLBD: " + te.getMessage(),te);
            } catch (SQLException e) {
                log.error("*** Error al recuperr el adaptadore de conexion : " + e.getMessage(), e);
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
                log.info("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection
    
    public String[] getParamsAdaptSQLBD(String codOrganizacion) throws SQLException {
        log.info("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        
        if (log.isDebugEnabled()) {
            log.info("getJndi =========> ");
            log.info("parametro codOrganizacion: " + codOrganizacion);
            log.info("gestor: " + gestor);
            log.info("jndi: " + jndiGenerico);
        }

        DataSource ds = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.info("He cogido el jndi: " + jndiGenerico);
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

                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            } catch (TechnicalException te) {
                log.error("*** AdaptadorSQLBD: " + te.getMessage(),te);
            } catch (SQLException e) {
                log.error("*** Error al recuperr el adaptadore de conexion : " + e.getMessage(), e);
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
                log.info("getConnection() : END");
            }
        }// synchronized
        return salida;
    }//

    public String getCodigoProcedimientoDsdNumExpediente(String numExp){
        String codProc = null;
        try {
            if(numExp!=null && !numExp.isEmpty()){
                String[] arregloDatos=numExp.split("/");
                if(arregloDatos!=null && arregloDatos.length==3){
                    codProc=arregloDatos[1];
                }
            }
        } catch (Exception e) {
            log.error("Error al recger el codigo de procedimiento desde el Numero de Expediente : " + e.getMessage(), e);
            codProc=null;
        }
        return codProc;
    }
    
    public int getEjercicioDsdNumExpediente(String numExp){
        int ejercicio = 0;
        try {
            if(numExp!=null && !numExp.isEmpty()){
                String[] arregloDatos=numExp.split("/");
                if(arregloDatos!=null && arregloDatos.length==3){
                    ejercicio=Integer.valueOf(arregloDatos[0]);
                }
            }
        } catch (Exception e) {
            log.error("Error al recger el codigo de procedimiento desde el Numero de Expediente : " + e.getMessage(), e);
            ejercicio=0;
        }
        return ejercicio;
    }

    public String getDescRespuestaPorCodigoIdioma(String codigo, int idioma) {
        try {
            String claveProperties=MELANBIDE43_GestionRdR_Constantes.PREFIJO_RESPUESTA + MELANBIDE43_GestionRdR_Constantes.BARRA_BAJA + codigo + MELANBIDE43_GestionRdR_Constantes.BARRA_BAJA + idioma;
            return ConfigurationParameter.getParameter(claveProperties,MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_RdR);
        } catch (Exception e) {
            log.error("Error al recuperar la descripcion de la respuesta/error desde el properties : " + e.getMessage(), e);
            return "";
        }
    }

    public Map<String, String> prepararMapRespuesta(String codigo, int idioma, String[] paramTextos) {
        Map<String,String> repreRespuestaValidacion = new HashMap<String, String>();
        try {
            if(codigo!=null && !codigo.isEmpty()){
                repreRespuestaValidacion.put("codigo", codigo);
                String descripcionFormateada = MessageFormat.format(this.getDescRespuestaPorCodigoIdioma(codigo, idioma), paramTextos);
                if(descripcionFormateada!=null && !descripcionFormateada.isEmpty()){
                    descripcionFormateada=descripcionFormateada.replaceAll("\r\n|\r|\n", " ");
                }
                repreRespuestaValidacion.put("descripcion",descripcionFormateada);
            }else{
                log.error("-- No se ha recibido el codigo de la respuesta a traducir ");
                repreRespuestaValidacion.put("codigo", "-1");
                repreRespuestaValidacion.put("descripcion", "No se ha recibido un codigo de la respuesta para traducir");
            }
        } catch (Exception e) {
            log.error("-- Error al traducir la respuesta a la llamada del metodo validarExistActualRolRepresContraRdR : " + e.getMessage() , e);
            repreRespuestaValidacion.put("codigo", "-2");
            repreRespuestaValidacion.put("descripcion", "Se ha presentado un error al tratar la respuesta del metodo validarExistActualRolRepresContraRdR : " + e.getMessage());
        }
        return repreRespuestaValidacion;
    }
    
    public String recogerNombreFicheroLog() {
        String nombreLog="";
        try {
            String logRuta = getParameterFromFileProperties(MELANBIDE43_GestionRdR_Constantes.NOMBRE_KEY_FICHEROLOG_LOG4J, MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_LOG4J);
            if(logRuta!=null && !logRuta.isEmpty()){
                nombreLog=logRuta.substring(logRuta.lastIndexOf("/"));
            }
        } catch (Exception e) {
            log.error("Error a leer el nombre del fichero log : " + e.getMessage(), e);
            nombreLog="";
        }
        return nombreLog;
    }
    
    public static String getParameterFromFileProperties(String property,String FICHERO_CONFIGURACION){
        String valor = "";
        try{
            ResourceBundle bundle = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
            valor = bundle.getString(property);
        }catch(Exception e){
            log.error("Error al recoger una propiedad desde Fichero Propertie : " + property+"/"+FICHERO_CONFIGURACION,e);
            e.printStackTrace();
        }//try-catch
        return valor;
    }//getParameter
    
    /**
     * Calcula el tipo de documento en flexia segun el documento. Desde platea solo pasan : DNI, NIE o CIF
     * 0	SIN DOCUMENTO
     * 1	N.I.F.
     * 2	PASAPORTE
     * 3	TARJETA RESIDENCIA
     * 4	C.I.F.
     * 5	C.I.F. ENT. PUBLICA
     * 6	NEMOT¿CNICO
     * 7	UOR
     * 8	W
     * 9	U
     * Claves sobre la forma jurídica de entidades españolas
     * Para las entidades españolas, el número de identificación fiscal comenzará con una letra, que incluirá información sobre su forma jurídica
     * de acuerdo con las siguientes claves:
     * A. Sociedades anónimas
     * B. Sociedades de responsabilidad limitada
     * C. Sociedades colectivas
     * D. Sociedades comanditarias
     * E. Comunidades de bienes, herencias yacentes y demás entidades carentes de personalidad jurídica no incluidas expresamente en otras claves
     * F. Sociedades cooperativas
     * G. Asociaciones
     * H. Comunidades de propietarios en régimen de propiedad horizontal
     * J. Sociedades civiles
     * P. Corporaciones Locales
     * Q. Organismos públicos
     * R. Congregaciones e instituciones religiosas
     * S. Órganos de la Administración del Estado y de las Comunidades Autónomas
     * U. Uniones Temporales de Empresas
     * V. Otros tipos no definidos en el resto de claves
     * Clave de entidades extranjeras
     * N. Entidades extranjeras
     * Clave de establecimiento permanente de entidad no residente
     * W. Establecimientos permanentes de entidades no residentes en territorio español.
     * Letras finales DNI
     * RESTO	0	1	2	3	4	5	6	7	8	9	10	11
     * LETRA	T	R	W	A	G	M	Y	F	P	D	X	B
     *
     * RESTO	12	13	14	15	16	17	18	19	20	21	22 
     * LETRA	N	J	Z	S	Q	V	H	L	C	K	E
     * @param documento
     * @return 
     */
    public Integer getTipoDocumentoFlexiaFromDocumento(String documento){
        Integer resultado=null;
        try {
            List<String> listaLetrasReservadasInicioExtranjeros = new ArrayList<String>(asList("X","Y","Z"));
            List<String> listaLetrasReservadasInicioCIF = new ArrayList<String>(asList("A", "B", "C", "D", "E", "F", "G", "H", "J", "P", "Q", "R", "S", "U", "V", "N", "W"));
            List<String> listaLetrasReservadasFinalesDNI = new ArrayList<String>(asList("T","R","W","A","G","M","Y","F","P","D","X","B","N","J","Z","S","Q","V","H","L","C","K","E"));
            log.info("Documento:"+documento);
            if(documento!=null && !documento.isEmpty()){
                String letraInicial = documento.substring(0, 1);
                String letraFinal = documento.substring(documento.length()-1);
                if(documento.length()!=9){
                    log.info("Longitud diferente de 9, formato oficial de DNI,NIE, CIF. Asignamos 6 Nemotecnico");
                    resultado=6;
                }else{
                    if(listaLetrasReservadasInicioExtranjeros.contains(letraInicial)){
                        resultado=3;
                    }else if(listaLetrasReservadasInicioCIF.contains(letraInicial)){
                        resultado=4;
                    }else if(listaLetrasReservadasFinalesDNI.contains(letraFinal)){
                        resultado=1;
                    }else{
                        log.info("No cumplio niguna de la condciones para : NIE, DNI, CIF. Asignamos 6 Nemotecnico");
                        resultado=6;
                    }                        
                }
            }else
                resultado=0;
        } catch (Exception e) {
            log.error("Exception calculando tipo de documento. ", e);
        }
        log.info("resultado: "+resultado);
        return resultado;
    }
}
