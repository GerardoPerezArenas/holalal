package es.altia.flexia.integracion.moduloexterno.melanbide05;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.common.exception.TechnicalException;
import java.sql.Connection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.ResourceBundle;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.integracion.moduloexterno.melanbide05.exception.MeLanbide05Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide05.manager.MeLanbide05Manager;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.melanbide05.persistence.dao.FechaPresentacionDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide05.util.MeLanbide05ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide05.util.MeLanbide05Constantes;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import javax.sql.DataSource;

/**
 *
 * @author paz.rodriguez
 */
public class MELANBIDE05 extends ModuloIntegracionExterno {
    
    //Logger de la clase
    private Logger log = LogManager.getLogger(MELANBIDE05.class);

    //Codigos de error del modulo
    private final static String TODO_CORRECTO = "0";
    private final static String ERROR_RECUPERANDO_FECHA_PRESENTACION = "1";
    private final static String ERROR_GRABANDO_FECHA_CAMPO_SUPLEMENTARIO  = "2";
    private final static String ERROR = "3";
    
   /**
    * Se procede a dar de alta una convocatoria
    * @param codOrganizacion: Código de la organización
    * @param codProcedimiento:
    * @param request:
    * @param response:
    * @return devuelve un string igual a null
    */
    public String grabarFechaPresentacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws TechnicalException{
        if(log.isDebugEnabled()) log.debug("grabarFechaPresentacion() : BEGIN");
        String retorno = TODO_CORRECTO;
        try{
            if(log.isDebugEnabled()) log.debug("Primero comprobamos si existe una fecha de presentacion de una anotacion de registro para el expediente");
            MeLanbide05Manager meLanbide05Manager = MeLanbide05Manager.getInstance();
            Boolean existeFecha = false;
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];
            try{
                existeFecha = 
                    meLanbide05Manager.existeFechaPresentacion(codOrganizacion, numExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(log.isDebugEnabled()) log.debug("existeFecha = " + existeFecha);
            }catch(MeLanbide05Exception ex){
                log.error("Se ha producido un error recuperando la fecha de presentacion " + ex.getMessage());
                retorno = ERROR_RECUPERANDO_FECHA_PRESENTACION;
            }//try-catch
            if(TODO_CORRECTO.equalsIgnoreCase(retorno)){
                if(existeFecha){
                    if(log.isDebugEnabled()) log.debug("Existe fecha");
                    Calendar fecha = null;
                    try{
                        if(log.isDebugEnabled()) log.debug("Recuperamos la fecha de la anotacion");
                        fecha = meLanbide05Manager.
                                getFechaPresentacion(codOrganizacion, numExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    }catch(MeLanbide05Exception ex){
                        log.error("Se ha producido un error recuperando la fecha de presentacion " + ex.getMessage());
                        retorno = ERROR_RECUPERANDO_FECHA_PRESENTACION;
                    }//try-catch
                    if(log.isDebugEnabled()) log.debug("Grabamos el valor en el campo suplementario");
                    try{
                        actualizarCampoSuplementarioFechaPresentacion(fecha, numExpediente, codOrganizacion, codProcedimiento, ejercicio);
                    }catch(MeLanbide05Exception ex){
                        log.error("Se ha producido un error grabando el campo suplementario de la fecha de presentacion " + ex.getMessage());
                        retorno = ERROR_GRABANDO_FECHA_CAMPO_SUPLEMENTARIO;
                    }//try-catch
                }else{
                    if(log.isDebugEnabled()) log.debug("NO Existe fecha");
                }//if(existeFecha)
            }//if(TODO_CORRECTO.equalsIgnoreCase(retorno))
       }catch(Exception ex){
           log.error("Se ha producido un error en la operación de grabarFechaPresentacion " + ex.getMessage());
           retorno = ERROR;
       }//try-catch
       if(log.isDebugEnabled()) log.debug("grabarFechaPresentacion() : END");
       return retorno;
   }//grabarFechaPresentacion
   
    private void actualizarCampoSuplementarioFechaPresentacion(Calendar fechaPresentacion, String numExpediente, Integer codOrganizacion,
        String codProcedimiento, String ejercicio)throws MeLanbide05Exception{
        if(log.isDebugEnabled()) log.debug("actualizarCampoSuplementarioFechaPresentacion() : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos el nombre del campo suplementario de la fecha");
            String campoSuplementario = null;
            try{
                /**
                campoSuplementario = MeLanbide05ConfigurationParameter.getParameter(codOrganizacion + MeLanbide05Constantes.BARRA +
                    MeLanbide05Constantes.MODULO_INTEGRACION + MeLanbide05Constantes.BARRA + this.getNombreModulo() + MeLanbide05Constantes.BARRA 
                    + codProcedimiento + MeLanbide05Constantes.BARRA + MeLanbide05Constantes.NOMBRE_CAMPO + MeLanbide05Constantes.BARRA +
                    MeLanbide05Constantes.CAMPO_SUPLEMENTARIO_FECHA, this.getNombreModulo());
                    **/
                
                campoSuplementario = MeLanbide05ConfigurationParameter.getParameter(codOrganizacion + MeLanbide05Constantes.BARRA +
                    MeLanbide05Constantes.MODULO_INTEGRACION + MeLanbide05Constantes.BARRA + this.getNombreModulo() + MeLanbide05Constantes.BARRA 
                    + MeLanbide05Constantes.NOMBRE_CAMPO + MeLanbide05Constantes.BARRA +
                    MeLanbide05Constantes.CAMPO_SUPLEMENTARIO_FECHA, this.getNombreModulo());
                
            }catch(Exception ex){
                log.error("Se ha producido un error recuperando el nombre del campo suplementario " + ex.getMessage());
                throw new MeLanbide05Exception("Se ha producido un error recuperando el nombre del campo suplementario ", ex);
            }//try-catch
           
           SalidaIntegracionVO salida = new SalidaIntegracionVO();
           CampoSuplementarioModuloIntegracionVO campoSuplementarioInicioContrato = new CampoSuplementarioModuloIntegracionVO();
           campoSuplementarioInicioContrato.setCodOrganizacion(String.valueOf(codOrganizacion));
                campoSuplementarioInicioContrato.setCodProcedimiento(codProcedimiento);
                campoSuplementarioInicioContrato.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                campoSuplementarioInicioContrato.setTramite(false);
                campoSuplementarioInicioContrato.setNumExpediente(numExpediente);
                campoSuplementarioInicioContrato.setEjercicio(ejercicio);
                campoSuplementarioInicioContrato.setCodigoCampo(campoSuplementario);
                campoSuplementarioInicioContrato.setValorFecha(fechaPresentacion);
            salida = el.grabarCampoSuplementario(campoSuplementarioInicioContrato);
            if(salida.getStatus() != 0){
                log.error("Se ha producido una excepcion actualizando el campo suplementario de fecha de presentacion ");
                throw new MeLanbide05Exception("Se ha producido una excepcion actualizando el campo suplementario de fecha de presentacion ");
            }//if(salida.getStatus() != 0)
        }catch(Exception ex){
            log.error("Se ha producido una excepcion actualizando el campo suplementario de fecha de presentacion " + ex.getMessage());
            throw new MeLanbide05Exception("Se ha producido una excepcion actualizando el campo suplementario de fecha de presentacion ", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("actualizarCampoSuplementarioFechaPresentacion() : END");
    }//actualizarCampoSuplementarioFechaPresentacion
   
   /**
    * Recupera a partir del código de la organización, el objeto AdaptadorSQLBD a partir del cual recuperar la conexión a la BBDD
    * @param codOrganizacion: Código de la organización
    * @return  AdaptadorSQLBD
    */
   private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
                
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
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection

}//class
