package es.altia.flexia.integracion.moduloexterno.melanbide71;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide71.manager.MeLanbide71Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide71.util.ConstantesMeLanbide71;
import es.altia.flexia.integracion.moduloexterno.melanbide71.util.MeLanbide71Util;
import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class MELANBIDE71 extends ModuloIntegracionExterno {
    private static Logger log = LogManager.getLogger(MELANBIDE71.class);
    private static final ResourceBundle properties = ResourceBundle.getBundle(ConstantesMeLanbide71.getFICHERO_PROPIEDADES());
      
    public String actualizarDatosSuplementariosAPEA(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException{
        log.info("actualizarDatosSuplementariosAPEA. INICIO - Expediente: " + numExpediente + "  =================>");
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide71Manager meLanbide71Manager;
        ExpedienteVO datosExpediente;
        Integer edad;
        DatosEconomicosExpVO datosEcon;
        Integer importeSubvencion = null;
        Integer importePago = null;
        try{
            try{
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                con   = adapt.getConnection();
            } catch (BDException e) {
                log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                return "2";
            } catch (SQLException e) {
                log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                return "2";
            }
            meLanbide71Manager = MeLanbide71Manager.getInstance();
            // ini DATOS TERCERO
            try{
                datosExpediente = meLanbide71Manager.getDatosExpediente(codOrganizacion, numExpediente, con);
            }catch(Exception e){
                log.error("Error al obtener datos del tercero: " + e.getMessage());
                return "3";
            }
            //sean CB o no, se calcula la edad y el sexo
            if(datosExpediente!=null && datosExpediente.getFecPresentacion()!=null && datosExpediente.getTercero()!=null && datosExpediente.getTercero().getTFecNacimiento()!=null && datosExpediente.getTercero().getTSexoTercero()!=null){
                edad = MeLanbide71Util.calcularEdad(datosExpediente.getTercero().getTFecNacimiento(), datosExpediente.getFecPresentacion());
                adapt.inicioTransaccion(con);
                meLanbide71Manager.actualizaEdad(codOrganizacion, numExpediente, edad, con);
                meLanbide71Manager.actualizaSexo(codOrganizacion, numExpediente, datosExpediente.getTercero().getTSexoTercero(), con);
            // fin DATOS TERCERO
            // ini IMPORTE DE LA SUBVENCION
                if (datosExpediente.getCifCBSC() == null) {//si no es CB calculo los importes. Si es CB se eliminarï¿½n (por si hubiera por haber retrocedido expediente y cambiado el campo CIFCBSC...)
                //   ------------- Para coger el ejercicio de la fecha de presentacion en lugar de cogerlo del expediente ---------------- Rosa 2018/01/26 ------------------
                //  String [] arrNumExpediente = numExpediente.split("/");
                    //  datosEcon = meLanbide71Manager.getImporteSubvencion(edad, datosExpediente.getTercero().getTSexoTercero(), arrNumExpediente[1], arrNumExpediente[0], con);
                    String [] arrNumExpediente = numExpediente.split("/");
                    String formato = "yyyy";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
                    datosEcon = meLanbide71Manager.getImporteSubvencion(edad, datosExpediente.getTercero().getTSexoTercero(), arrNumExpediente[1], dateFormat.format(datosExpediente.getFecPresentacion()), con);
                    importeSubvencion = datosEcon.getImporteSubvencion();
                    importePago = datosEcon.getImportePago();
                }
                meLanbide71Manager.actualizaImporteSubvencion(codOrganizacion, numExpediente, importeSubvencion, con);
            // fin IMPORTE DE LA SUBVENCION
            //ini IMPORTE PRIMER PAGO
            //    meLanbide71Manager.actualizaImportePrimerPago(codOrganizacion, numExpediente, importePago, con);
            //fin IMPORTE PRIMER PAGO
                adapt.finTransaccion(con);
            } else {
                return "4";
            }
            
        }catch(Exception e){
            log.error("ERROR.- actualizarDatosSuplementariosAPEA(). Error: " + e.getMessage());
            if (con != null) {
                adapt.rollBack(con);
            }
            return "1";
        } finally {
            if (adapt != null) {
                adapt.devolverConexion(con);
        }
        }
        log.debug("actualizarDatosSuplementariosAPEA() End " + numExpediente + "  =================>");
        return "0";
    }

    public String actualizarDatosAPEAI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("actualizarDatosAPEAI. INICIO - Expediente: " + numExpediente + "  =================>");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide71Manager meLanbide71Manager;
        ExpedienteVO datosExpediente = null;
        Integer edad;
        DatosEconomicosExpVO datosEcon;
        Integer importeSubvencion = null;
        try {
            try {
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                con = adapt.getConnection();
            } catch (BDException e) {
                log.error("actualizarDatosAPEAI(). Error al obtener una conexión a la BBDD: " + e.getMessage());
                return "2";
            } catch (SQLException e) {
                log.error("actualizarDatosAPEAI(). Error al obtener una conexión a la BBDD: " + e.getMessage());
                return "2";
            }
            meLanbide71Manager = MeLanbide71Manager.getInstance();
            try {
                datosExpediente = meLanbide71Manager.getDatosExpAPEAI(codOrganizacion, numExpediente, con);
            } catch (Exception e) {
                log.error("actualizarDatosAPEAI(). Error al obtener datos del tercero: " + e.getMessage());
                return "3";
            }
            if (datosExpediente != null) {
                if (datosExpediente.getFecPresentacion() != null) {
                    if (datosExpediente.getTercero() != null) {
                        if (datosExpediente.getTercero().getTFecNacimiento() != null) {
                            if (datosExpediente.getTercero().getTSexoTercero() != null) {
                                edad = MeLanbide71Util.calcularEdad(datosExpediente.getTercero().getTFecNacimiento(), datosExpediente.getFecPresentacion());
                                adapt.inicioTransaccion(con);
                                meLanbide71Manager.actualizaEdad(codOrganizacion, numExpediente, edad, con);
                                meLanbide71Manager.actualizaSexo(codOrganizacion, numExpediente, datosExpediente.getTercero().getTSexoTercero(), con);
                                String[] arrNumExpediente = numExpediente.split("/");
                                String formato = "yyyy";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
                                datosEcon = meLanbide71Manager.getImporteSubvencion(edad, datosExpediente.getTercero().getTSexoTercero(), arrNumExpediente[1], dateFormat.format(datosExpediente.getFecPresentacion()), con);
                                importeSubvencion = datosEcon.getImporteSubvencion();
                                meLanbide71Manager.actualizaImporteSubvencion(codOrganizacion, numExpediente, importeSubvencion, con);
                                adapt.finTransaccion(con);
                            } else {
                                log.error("actualizarDatosAPEAI(). Falta el sexo del tercero.");
                                return "7";
                            }
                        } else {
                            log.error("actualizarDatosAPEAI(). Falta la fecha de nacimiento del tercero.");
                            return "6";
                        }
                    } else {
                        log.error("actualizarDatosAPEAI(). Faltan datos del tercero.");
                        return "4";
                    }
                } else {
                    log.error("actualizarDatosAPEAI(). Falta la Fecha de Presentación");
                    return "5";
                }
            } else {
                log.error("actualizarDatosAPEAI(). Faltan datos del expediente");
                return "4";
            }
        } catch (Exception e) {
            log.error("ERROR.- actualizarDatosAPEAI(). Error: " + e.getMessage());
            if (con != null) {
                adapt.rollBack(con);
            }
            return "1";
        } finally {
            if (adapt != null){
                adapt.devolverConexion(con);                        
            }
        }    
        log.debug("actualizarDatosAPEAI() End " + numExpediente + "  =================>");
        return "0";
    }    
    
        /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
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
            try{
                PortableContext pc = PortableContext.getInstance();
                log.debug("He cogido el jndi: " + jndiGenerico);
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
}
