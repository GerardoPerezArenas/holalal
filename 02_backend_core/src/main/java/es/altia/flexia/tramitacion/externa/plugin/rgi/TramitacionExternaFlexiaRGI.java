package es.altia.flexia.tramitacion.externa.plugin.rgi;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaBase;
import es.altia.flexia.tramitacion.externa.plugin.config.Configuracion;
import es.altia.flexia.tramitacion.externa.plugin.util.ConstantesDatosTramitacionExterna;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TramitacionExternaFlexiaRGI extends TramitacionExternaBase{    
    private Logger log = LogManager.getLogger(TramitacionExternaFlexiaRGI.class);
    
    /**
     * Recupera la lista de trámites siguientes. Se supone que la condición es para trámites que tiene una lista de trámites que se abre a continuación
     * del actual.
     * @return Devuelve un String con los códigos de los trámites siguientes separados por una almohadilla. Sino hay trámites (condición de salida = Trámite)
     * se devuelve un espacio en blanco
     */
    public String getListaTramitesSiguientes(){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{       
            String sql = "SELECT FLS_CTS FROM E_FLS WHERE FLS_PRO='" + this.getCodProcedimiento() + "' AND FLS_TRA=" + this.getCodTramite() +
                         " AND FLS_MUN=" + this.getCodOrganizacion() + " AND FLS_NUC=0";
            log.debug(sql);
            
            con = this.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);
                ArrayList<String> lista = new ArrayList<String>();
                while(rs.next()){
                    lista.add(rs.getString("FLS_CTS"));
                }// while

                for(int i=0;lista!=null && i<lista.size();i++){
                    salida = salida + lista.get(i);
                    if(lista.size()-i>1) salida = salida + ";";
                }// for
                
            }else
                log.error(this.getClass().getName() + ".getListaTramitesSiguientes(): No se ha podido obtener una conexión a la BBDD");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getListaTramitesSiguientes(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }

        return salida;
    }



  /**
     * Recupera la lista de códigos de terceros alternativos de los interesados del expediente que tenga el rol por defecto
     * @return Devuelve un String con los códigos alternativos de los interesados del expediente, separados por un ; en el caso de que haya más de un
     * interesado con el rol por defecto
     */
    public String getCodigoTerceroAlternativo(){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{

            String sql = "SELECT EXT_TER,EXT_NVR,ROL_COD,ROL_DES,EXTERNAL_CODE " +
                         "FROM E_EXT,E_EXP,E_ROL,T_TER " +
                         "WHERE EXT_MUN=" + this.getCodOrganizacion() +
                         " AND EXT_EJE=" + this.getEjercicio() + " AND EXT_NUM='" + this.getNumExpediente() + "' " +
                         "AND EXT_MUN=EXP_MUN AND EXT_EJE=EXP_EJE AND EXT_NUM=EXP_NUM " +
                         "AND EXP_PRO=ROL_PRO AND EXT_ROL=ROL_COD AND ROL_PDE=1 " + 
                         "AND EXT_TER=TER_COD " +
                         "ORDER BY EXT_TER ASC";

            log.debug(sql);

            con = this.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);
                ArrayList<String> lista = new ArrayList<String>();
                while(rs.next()){
                    String externalCode = rs.getString("EXTERNAL_CODE");
                    if(externalCode!=null && !"".equals(externalCode) && !"null".equals(externalCode))
                        lista.add(externalCode); 

                }// while

                for(int i=0;lista!=null && i<lista.size();i++){
                    salida = salida + lista.get(i);
                    if(lista.size()-i>1) salida = salida + ";";
                }// for

            }else
                log.error(this.getClass().getName() + ".getCodigoTerceroAlternativo(): No se ha podido obtener una conexión a la BBDD");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getCodigoTerceroAlternativo(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }

        return salida;
    }



   /**
     * Recupera el número de una anotación de registro que ha iniciado un expediente
     * @return Devuelve un String con la anotación si existe, sino devuelve un espacio en blanco
     */
    public String getAsientoRegistroInicioExpediente(){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{

            String sql = "SELECT EXR_EJR,EXR_NRE " +
                         "FROM E_EXR " +
                         "WHERE EXR_NUM='" + this.getNumExpediente() + "' " +
                         "AND EXR_EJE=" + this.getEjercicio() + " AND EXR_MUN=" + this.getCodOrganizacion()+ " " +
                         "AND EXR_PRO='" + this.getCodProcedimiento() + "' " + 
                         "AND EXR_TOP=0";
            log.debug(sql);

            con = this.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);
                
                while(rs.next()){                    
                    String numero    = rs.getString("EXR_NRE");
                    if(numero!=null && !"".equals(numero) && !"null".equals(numero))
                        salida = numero;
                    
                }// while


            }else
                log.error(this.getClass().getName() + ".getAsientoRegistroAntiguo(): No se ha podido obtener una conexión a la BBDD");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getAsientoRegistroAntiguo(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }

        return salida;
    }


   /**
     * Recupera el ejercicio correspondiente a la anotación de registro que ha iniciado un expediente
     * @return Devuelve un String con la ejerciciosi existe, sino devuelve un espacio en blanco
     */
    public String getEjercicioAsientoRegistroInicioExpediente(){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{

            String sql = "SELECT EXR_EJR,EXR_NRE " +
                         "FROM E_EXR " +
                         "WHERE EXR_NUM='" + this.getNumExpediente() + "' " +
                         "AND EXR_EJE=" + this.getEjercicio() + " AND EXR_MUN=" + this.getCodOrganizacion()+ " " +
                         "AND EXR_PRO='" + this.getCodProcedimiento() + "' " +
                         "AND EXR_TOP=0";
            log.debug(sql);

            con = this.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    String ejercicio    = rs.getString("EXR_EJR");
                    if(ejercicio!=null && !"".equals(ejercicio) && !"null".equals(ejercicio))
                        salida = ejercicio;

                }// while


            }else
                log.error(this.getClass().getName() + ".getEjercicioAsientoRegistroInicioExpediente(): No se ha podido obtener una conexión a la BBDD");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getEjercicioAsientoRegistroInicioExpediente(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }
        return salida;
    }

    /**
     * Recupera la uor de la oficina de registro correspondiente a la anotación/asiento que ha iniciado el expediente
     * @return String con el código interna de la uor o espacio en blanco sino se ha podido recuperar
     */
    public String getUorRegistro (){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{
            String sql = "SELECT EXR_UOR " +
                         "FROM E_EXR " +
                         "WHERE EXR_NUM='" + this.getNumExpediente() + "' " +
                         "AND EXR_EJE=" + this.getEjercicio() + " AND EXR_MUN=" + this.getCodOrganizacion()+ " " +
                         "AND EXR_PRO='" + this.getCodProcedimiento() + "' " + 
                         "AND EXR_TOP=0";
            log.debug(sql);

            con = this.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);
                
                while(rs.next()){
                    String uor = rs.getString("EXR_UOR");
                    if(uor!=null && !"".equals(uor) && !"null".equals(uor)){
                        salida =uor.trim();
                    }                    

                }// while

            }else
                log.error(this.getClass().getName() + ".getUorRegistro(): No se ha podido obtener una conexión a la BBDD");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getUorRegistro(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }

        return salida;
    }


    /**
     * Recupera el código de uor visible de la oficina de registro correspondiente a la anotación/asiento que ha iniciado el expediente
     * @return String con el código interna de la uor o espacio en blanco sino se ha podido recuperar
     */
    public String getUorVisibleRegistro (){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{
            String sql = "SELECT EXR_UOR, UOR_COD_VIS " +
                         "FROM E_EXR,A_UOR " +
                         "WHERE EXR_NUM='" + this.getNumExpediente() + "' " +
                         "AND EXR_EJE=" + this.getEjercicio() + " AND EXR_MUN=" + this.getCodOrganizacion()+ " " +
                         "AND EXR_PRO='" + this.getCodProcedimiento() + "' " +
                         "AND EXR_TOP=0 AND EXR_UOR=UOR_COD";
            log.debug(sql);

            con = this.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    String uor = rs.getString("UOR_COD_VIS");
                    if(uor!=null && !"".equals(uor) && !"null".equals(uor)){
                        salida =uor.trim();
                    }

                }// while

            }else
                log.error(this.getClass().getName() + ".getUorVisibleRegistro(): No se ha podido obtener una conexión a la BBDD");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getUorVisibleRegistro(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }

        return salida;
    }


   /**
     * Recupera el código del departamento de la anotación/asiento de registro que ha iniciado el expediente, si existe
     * @return String con el código del departamento o un espacio en blanco sino se ha podido recuperar
     */
    public String getCodDepartamentoAsiento (){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{
            String sql = "SELECT EXR_DEP " +
                         "FROM E_EXR " +
                         "WHERE EXR_NUM='" + this.getNumExpediente() + "' " +
                         "AND EXR_EJE=" + this.getEjercicio() + " AND EXR_MUN=" + this.getCodOrganizacion()+ " " +
                         "AND EXR_PRO='" + this.getCodProcedimiento() + "' " +
                         "AND EXR_TOP=0";
            log.debug(sql);

            con = this.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    String dep = rs.getString("EXR_DEP");
                    if(dep!=null && !"".equals(dep) && !"null".equals(dep)){
                        salida =dep.trim();
                    }

                }// while

            }else
                log.error(this.getClass().getName() + ".getCodDepartamentoAsiento(): No se ha podido obtener una conexión a la BBDD");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getCodDepartamentoAsiento(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }

        return salida;
    }


   /**
     * Recupera el tipo de entrada de la anotación/asiento de registro que ha iniciado el expediente, si existe
     * @return String con el tipo de entrada (E=Entrada o S=salida) o espacio en blanco si no existe el asiento que ha iniciado el expediente
     */
    public String getTipoEntradaAsiento (){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{
            String sql = "SELECT EXR_TIP " +
                         "FROM E_EXR " +
                         "WHERE EXR_NUM='" + this.getNumExpediente() + "' " +
                         "AND EXR_EJE=" + this.getEjercicio() + " AND EXR_MUN=" + this.getCodOrganizacion()+ " " +
                         "AND EXR_PRO='" + this.getCodProcedimiento() + "' " +
                         "AND EXR_TOP=0";
            log.debug(sql);

            con = this.getConnection();
            if(con!=null){
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    String tip = rs.getString("EXR_TIP");
                    if(tip!=null && !"".equals(tip) && !"null".equals(tip)){
                        salida = tip.trim();
                    }

                }// while

            }else
                log.error(this.getClass().getName() + ".getTipoEntradaAsiento(): No se ha podido obtener una conexión a la BBDD");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getTipoEntradaAsiento(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }

        return salida;
    }



   /**
     * Recupera el login del usuario actual. En el parámetro codUsuario se encuentra el código de usuario
     * @return String con el login o espacio en blanco si el usuario no existe o no se ha podido recuperar
     */
    public String getLoginUsuario(){
        String salida = "";
        ResultSet rs = null;
        Statement st  =null;
        Connection con = null;

        try{

            String propiedad = getCodOrganizacion() + ConstantesDatosTramitacionExterna.TRAMITACION_EXTERNA + "GENERICO";
            String generico  = Configuracion.getValor(propiedad, getNombrePlugin());
            if(generico!=null && !"".equals(generico)){

                String sql = "SELECT USU_LOG " +
                             "FROM " + generico + ".A_USU " +
                             "WHERE USU_COD=" + getCodUsuario();
                log.debug(sql);

                con = this.getConnection();
                if(con!=null){
                    st = con.createStatement();
                    rs = st.executeQuery(sql);

                    while(rs.next()){
                        String login = rs.getString("USU_LOG");
                        if(login!=null && !"".equals(login) && !"null".equals(login)){
                            salida = login.trim();
                        }

                    }// while

                }else
                    log.error(this.getClass().getName() + ".getLoginUsuario(): No se ha podido obtener una conexión a la BBDD");
            }else
                log.error(this.getClass().getName() + ".getLoginUsuario(): No se ha podido recuperar el nombre del esquema genérico para realizar la consulta");

        }catch(SQLException e){
            log.error(this.getClass().getName() + ".getLoginUsuario(): Se ha producido un error: " + e.getMessage());
        }finally{
            try{
                if(rs!=null) rs.close();
                if(st!=null) st.close();
                if(con!=null) con.close();
            }catch(SQLException e){
                log.error("Error al cerrar recursos utilizados para realizar operaciones sobre la BBDD: " + e.getMessage());
            }
        }

        return salida;
    }



   /**
     * Devuelve los parámetros de conexión a base de datos para una determinada organización
     * @param codOrganizacion: Código de la organización
     * @return String[]
     */
    private Connection getConnection(){

        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        String codOrganizacion = this.getCodOrganizacion();
        log.debug(" getJndi =========> ");
        log.debug("     parametro codOrganizacion: " + codOrganizacion);
        log.debug("     gestor: " + gestor);
        log.debug("     jndi: " + jndiGenerico);

        DataSource ds = null;
        synchronized (this) {
           try{
                PortableContext pc = PortableContext.getInstance();
                log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql   = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }

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

                    AdaptadorSQLBD adapt = new AdaptadorSQLBD(salida);
                    try{
                        con = adapt.getConnection();
                    }catch(BDException e){
                        log.error("Error al obtener conexión a la BBDD " + e.getMessage());
                    }
                }

           }
           catch(TechnicalException te){
               te.printStackTrace();
               log.error("*** AdaptadorSQLBD: " + te.toString());
           }catch(SQLException e){
               e.printStackTrace();
           }finally{

               try{
                   if(st!=null) st.close();
                   if(rs!=null) rs.close();
                   if(conGenerico!=null && !conGenerico.isClosed()) conGenerico.close();

               }catch(SQLException e){
                   e.printStackTrace();
               }
           }// finally
        }// synchronized

       return con;
        
    }

    

    private boolean isInteger(String dato){
        boolean exito = false;
        try{
            Integer.parseInt(dato);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }
        return exito;
   }
    
}