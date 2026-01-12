package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerUnidadeCompetencialVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.InteresadoExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 22-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class MeLanbide03CertCentroDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03CertCentroDAO.class);
    
    //Instace
    private static MeLanbide03CertCentroDAO instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide03CertCentroDAO, si no existe la crea.
     * @return MeLanbide03CertCentroDAO
     */
    public static MeLanbide03CertCentroDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide03CertCentroDAO.class){
                if(instance == null){
                    instance = new MeLanbide03CertCentroDAO();
                }//if(instance == null)
            }//synchronized(MeLanbide03CertCentroDAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    
    /****
    public void insertarCentros(ArrayList<CerUnidadeCompetencialVO> unidades, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarCentros() : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        if(unidades != null && unidades.size() > 0){
            for(CerUnidadeCompetencialVO unidad : unidades){
                String numExpediente = unidad.getNumExpediente();
                String codOrganizacion = unidad.getCodOrganizacion();
                if(unidad.getCentroAcreditado().equalsIgnoreCase("1")){
                    insertarCentro(unidad, con);
                }else{
                    //Si vamos a aceptar un centro para una unidad competencial tenemos que comprobar que no exista ya un centro.
                    ArrayList<CerUnidadeCompetencialVO> unid = new ArrayList<CerUnidadeCompetencialVO>();
                    unid.add(unidad);
                    ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadas = getCodigosUnidadesYaAcreditadas(unidades, 
                            unidad.getCodCertificado(),unidad.getCodOrganizacion(), unidad.getNumExpediente(), con);
                    
                    if(unidadesAcreditadas.size() > 0){
                        String numExpedienteAux = numExpediente;
                        String[] numExpedienteAuxSplit = numExpedienteAux.split("/");
                        String codProcedimientoAux  = numExpedienteAuxSplit[1];
                        String ejercicioAux = numExpedienteAuxSplit[0];

                        SalidaIntegracionVO salidaExpediente = el.getExpediente(codOrganizacion, numExpedienteAux, codProcedimientoAux, ejercicioAux);
                        ExpedienteModuloIntegracionVO expediente = salidaExpediente.getExpediente();
                        ArrayList<InteresadoExpedienteModuloIntegracionVO> interesadosExpediente = expediente.getInteresados();
                        
                        for(CerUnidadeCompetencialVO unidadAcreditada : unidadesAcreditadas){
                            String numExpedienteAcreditado = unidadAcreditada.getNumExpediente();
                            String[] numExpedienteSplit = numExpedienteAcreditado.split("/");
                            String codProcedimiento  = numExpedienteSplit[1];
                            String ejercicio = numExpedienteSplit[0];
                            
                            SalidaIntegracionVO salida = el.getExpediente(codOrganizacion, numExpedienteAcreditado, codProcedimiento, ejercicio);
                            ExpedienteModuloIntegracionVO expedienteAcreditado = salida.getExpediente();
                            ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = expedienteAcreditado.getInteresados();
                            
                            Boolean coincideInteresado = false;
                            for(InteresadoExpedienteModuloIntegracionVO interesadoExpediente : interesadosExpediente){
                                for(InteresadoExpedienteModuloIntegracionVO interesado : interesados){
                                    if(interesadoExpediente.getDocumento().equalsIgnoreCase(interesado.getDocumento())){
                                        coincideInteresado = true;
                                        if(log.isDebugEnabled()) log.debug("El interesado coincide");
                                        
                                        if(unidadAcreditada.getCodUnidad().equalsIgnoreCase(unidad.getCodUnidad())){
                                            eliminarCentro(unidad, con);
                                        }else{
                                            insertarCentro(unidad, con);
                                        }//if(unidadAcreditada.getCodUnidad().equalsIgnoreCase(unidad.getCodUnidad()))
                                        
                                    }//if(interesadoExpediente.getDocumento().equalsIgnoreCase(interesado.getDocumento()))
                                }//for(InteresadoExpedienteModuloIntegracionVO interesado : interesados)
                            }//for(InteresadoExpedienteModuloIntegracionVO interesadoExpediente : interesadosExpediente)
                            if(!coincideInteresado){
                                insertarCentro(unidad, con);
                            }//if(!coincideInteresado)
                        }//for(CerUnidadeCompetencialVO unidadAcreditada : unidadesAcreditadas)
                    }else{
                        insertarCentro(unidad, con);
                    }//if(unidadesAcreditadas.size() > 0)
                }//if(unidad.getCentroAcreditado() == "1")
            }//for(CerUnidadeCompetencialVO unidad : unidades)
        }//if(unidades != null && unidades.size() > 0)
        if(log.isDebugEnabled()) log.debug("insertarCentros() : END");
    }//insertarCentros
    **/
    
    
    private boolean estaUnidadYaAcreditada(ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadas,CerUnidadeCompetencialVO unidad){        
        boolean exito = false;
        
        for(int i=0;unidadesAcreditadas!=null && i<unidadesAcreditadas.size();i++){
            CerUnidadeCompetencialVO unidadAcreditada = unidadesAcreditadas.get(i);
            if(unidadAcreditada.getCodUnidad().equals(unidad.getCodUnidad()) && unidadAcreditada.getCodCertificado().equals(unidad.getCodCertificado())){
                exito = true;                            
            }//if            
        }// for
        
        return exito;
    }
    
    
     public void insertarCentros(ArrayList<CerUnidadeCompetencialVO> unidades, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarCentros() : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            
        for(CerUnidadeCompetencialVO unidad : unidades){
            // SE COMPRUEBA PARA CADA UNIDAD COMPETENCIAL SI YA ESTÁ ACREDITADA EN OTRO EXPEDIENTE PARA EL MISMO INTERESADO                                
            String numExpediente = unidad.getNumExpediente();
            String[] datos = numExpediente.split("/");
            String codOrganizacion = unidad.getCodOrganizacion();

            String ejercicio = datos[0];
            String codProcedimiento = datos[1];

            // Se recupera los datos del expediente actual
            if(log.isDebugEnabled()) log.debug("recupera los datos del expediente actual");
            SalidaIntegracionVO salidaExpediente = el.getExpediente(codOrganizacion, numExpediente, codProcedimiento, ejercicio);                
            if(salidaExpediente.getStatus()==0){

                ExpedienteModuloIntegracionVO exp = salidaExpediente.getExpediente();
                ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = exp.getInteresados();

                
                 if(log.isDebugEnabled()) log.debug("NO COMPROBAMOS UNIDADES ACREDITADAS YA.");
                // Se recupera todas las unidades competenciales del expediente que ya han sido acreditadas
               // ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadas = getCodigosUnidadesYaAcreditadas(unidades,unidad.getCodCertificado(),unidad.getCodOrganizacion(), unidad.getNumExpediente(),interesados,con);

               // if(!estaUnidadYaAcreditada(unidadesAcreditadas, unidad)){
                    insertarCentro(unidad, con);
                 if(log.isDebugEnabled()) log.debug("insertar centro:"+unidad);
                //}
            }// if

        }// for
        if(log.isDebugEnabled()) log.debug("insertarCentros() : END");
        
    }//insertarCentros
    
    
    
    
    /**
     * Inserta la lista de unidades de competencia 
     * @param unidades
     * @param con
     * @throws SQLException 
     */
    public void insertarCentro(CerUnidadeCompetencialVO unidad, Connection con) throws SQLException{
        if(log.isDebugEnabled()) log.debug("insertarCentros() : BEGIN");
            /////////////ANTES
        /*
            Statement st = null;
            try{
                String sql = "Insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    + " (CENTRO_ACREDITADO, COD_CENTRO, COD_CENTRO_LANF, COD_CERTIFICADO, COD_ORGANIZACION, COD_PROCEDIMIENTO, COD_UNIDAD, NUM_EXPEDIENTE, COD_MOTIVO_ACREDITADO, COD_ORIGEN_ACREDITACION, CLAVE_REGISTRAL)"
                    + " values ('" + unidad.getCentroAcreditado() + "', '" + unidad.getCodCentro() + "', '" + unidad.getCodCentroLanF() + "', '" + unidad.getCodCertificado() +"'"
                    + ", " + Integer.valueOf(unidad.getCodOrganizacion()) + " , '" + unidad.getCodProcedimiento() + "',"
                    + " '" + unidad.getCodUnidad() + "', '" + unidad.getNumExpediente() + "', '" + unidad.getCodMotNoAcreditado() + "' , '" + unidad.getCodOrigenAcred() + "'"
                    + " , '" + unidad.getClaveRegistral() + "')"; 

                if(log.isDebugEnabled()) log.debug("sql = " + sql);
                st = con.createStatement();
                int rowsInserted = st.executeUpdate(sql);
                log.debug("Número de filas insertadas " + rowsInserted);
        */
            /////////////////ANTES    
                
              

                
        PreparedStatement ps = null;
        try{
            String sql = "Insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    + " (CENTRO_ACREDITADO, COD_CENTRO, COD_CENTRO_LANF, COD_CERTIFICADO, COD_ORGANIZACION, COD_PROCEDIMIENTO, COD_UNIDAD, NUM_EXPEDIENTE, COD_MOTIVO_ACREDITADO, COD_ORIGEN_ACREDITACION, CLAVE_REGISTRAL)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)";
            
            ps = con.prepareStatement(sql);
            ps.setString(1,unidad.getCentroAcreditado());
            ps.setString(2,unidad.getCodCentro());
            ps.setString(3,unidad.getCodCentroLanF());
            ps.setString(4,unidad.getCodCertificado());
            ps.setInt(5,Integer.valueOf(unidad.getCodOrganizacion()));
            ps.setString(6,unidad.getCodProcedimiento());
            ps.setString(7,unidad.getCodUnidad());
            ps.setString(8,unidad.getNumExpediente());
            ps.setString(9,unidad.getCodMotNoAcreditado());
            ps.setString(10,unidad.getCodOrigenAcred());
            ps.setString(11,unidad.getClaveRegistral());          
           
            if (ps.execute()) log.debug("Fila insertada ");
            

                
            }catch (SQLException e) {
                log.error("Se ha producido un error insertando las unidades competenciales en la tabla MELANBIDE03_CERT_CENTROS", e);
                throw e;
            }finally{
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
                if(ps!=null) ps.close();
                
            }//try-catch-finally    
        if(log.isDebugEnabled()) log.debug("insertarCentros() : END");         
    }//insertarCentro
    
    public void eliminarCentro(CerUnidadeCompetencialVO unidad, Connection con) throws SQLException{
        if(log.isDebugEnabled()) log.debug("eliminarCentro() : BEGIN");
            Statement st = null;
            try{
                String sql = "Delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                       + " where NUM_EXPEDIENTE = '" + unidad.getNumExpediente() + "' and COD_UNIDAD = '" + unidad.getCodUnidad() + "'" ;
                if(log.isDebugEnabled()) log.debug("sql = " + sql);
                st = con.createStatement();
                int rowsDeleted = st.executeUpdate(sql);
                log.debug("Número de filas eliminadas " + rowsDeleted);
            }catch (SQLException e) {
                log.error("Se ha producido un error ELIMINANDO las unidades competenciales en la tabla MELANBIDE03_CERT_CENTROS", e);
                throw e;
            }finally{
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) st.close();
            }//try-catch-finally    
        if(log.isDebugEnabled()) log.debug("eliminarCentro() : END");
    }//eliminarCentro
    
    /**
     * Actualiza las unidades de competencia  de un expediente.
     * Primero elimina todas las que hay y luego crea las que hay en la lista.
     * @param unidades
     * @param con
     * @throws SQLException 
     */
    public void actualizarCentros (ArrayList<CerUnidadeCompetencialVO> unidades, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("actualizarCentros() : BEGIN");
        if(log.isDebugEnabled()) log.debug("Borramos todos las unidades para luego crearlas con los datos nuevos");
        try{
            if(unidades != null && unidades.size() > 0){
                String codCertificado = unidades.get(0).getCodCertificado();
                String numExpediente = unidades.get(0).getNumExpediente();
                String codOrganizacion = unidades.get(0).getCodOrganizacion();
                
                borrarCentros(codCertificado, numExpediente, codOrganizacion, con);
                insertarCentros(unidades, con);
                
            }//if(unidades != null && unidades.size() > 0)
        }catch(SQLException ex){
            log.error("Se ha producido un error actualizando las unidades competenciales", ex);
            throw ex;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("actualizarCentros() : END");
        
    }//actualizarCentros
    
    /**
     * Elimina todas las unidades de competencia de un expediente, certificado y organización en concreto
     * @param codCertificado
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @throws SQLException si ha ocurrido algún ejecutar la consulta contra la BBDD
     */
    public void borrarCentros (String codCertificado, String numExpediente, String codOrganizacion, Connection con) throws SQLException{
        if(log.isDebugEnabled()) log.debug("borrarCentros ( codCertificado = " + codCertificado + " numExpediente = " + numExpediente
                + " codOrganizacion = " + codOrganizacion + " ) : BEGIN");        
        Statement st = null;
        try{
            String sql = "Delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    + " where NUM_EXPEDIENTE='" + numExpediente + "'"
                    + " AND COD_ORGANIZACION =" + Integer.valueOf(codOrganizacion);
            
            log.debug(sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.debug("Número de filas eliminadas: " + rowsDeleted);
            
        }catch (SQLException e) {
            log.error("Se ha producido un error eliminando los centros ", e);
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("borrarCentros() : END");        
    }//borrarCentros
    
    
    /**
     * Recupera los centros seleccionados para una unidad competencial en un expediente.
     * @param numExpediente
     * @param codCertificado
     * @param codOrganizacion
     * @param con
     * @return ArrayList<CerUnidadeCompetencial>
     * @throws Exception 
     */
    public ArrayList<CerUnidadeCompetencialVO> getCentrosExpedienteYCertificado (String numExpediente, String codCertificado, String codOrganizacion, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCentrosExpedienteYCertificado() : BEGIN");
        ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String[] datosExpediente = numExpediente.split("/");
            
            
            String sql="Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                   + " meLanbide03CertCentro " 
                   + " where COD_CERTIFICADO='" + codCertificado + "' AND NUM_EXPEDIENTE='" + numExpediente + "'"
                   + " AND COD_ORGANIZACION=" + Integer.valueOf(codOrganizacion) + " AND COD_PROCEDIMIENTO='" + datosExpediente[1] + "'";
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                CerUnidadeCompetencialVO unidad = new CerUnidadeCompetencialVO();
                    unidad.setCodUnidad(rs.getString("COD_UNIDAD"));
                    unidad.setCentroAcreditado(rs.getString("CENTRO_ACREDITADO"));
                    if(rs.getString("COD_CENTRO") != null){
                        unidad.setCodCentro(rs.getString("COD_CENTRO"));
                        unidad.setDescCentro(getDescripcionCentroCampoDesplegable(codOrganizacion,unidad.getCodCentro(), con));
                    }//if(rs.getString("COD_CENTRO") != null)
                    if(rs.getString("COD_CENTRO_LANF") != null){
                        unidad.setCodCentroLanF(rs.getString("COD_CENTRO_LANF"));
                        unidad.setDescCentroLanF(getDescripcionCentroLanFCampoDesplegable(codOrganizacion,unidad.getCodCentroLanF(), con));
                    }
                    //unidad.setCodOficina(getCodOficina(unidad.getCodCentro(), con));

                    unidad.setCodMotNoAcreditado(rs.getString("COD_MOTIVO_ACREDITADO"));
                    unidad.setCodOrigenAcred(rs.getString("COD_ORIGEN_ACREDITACION"));
                    
                    String claveRegistral = rs.getString("CLAVE_REGISTRAL");
                    unidad.setClaveRegistral("");
                    if(claveRegistral!=null && !"null".equalsIgnoreCase(claveRegistral) && !"".equals(claveRegistral)) 
                        unidad.setClaveRegistral(claveRegistral);
                unidades.add(unidad);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los centros ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCentrosExpedienteYCertificado() : END");
        return unidades;
    }//getCentrosExpedienteYCertificado

    /**
     * Recupera los centrosLanF para el combo
     * @return ArrayList<ValorCampoDesplegableModuloIntegracionVO>
     * @throws Exception 
     */
    public ArrayList<ValorCampoDesplegableModuloIntegracionVO> getComboCentrosLanF (Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getComboCentrosLanF() : BEGIN");
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> unidades = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql="Select GEN_CEN_COD_CENTRO, GEN_CEN_NOM_CENTRO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.VW_CENTROS_REG_REGEX, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " ORDER BY GEN_CEN_NOM_CENTRO";
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                ValorCampoDesplegableModuloIntegracionVO opcion = new ValorCampoDesplegableModuloIntegracionVO();
                    opcion.setCodigo(rs.getString("GEN_CEN_COD_CENTRO"));
                    opcion.setDescripcion(rs.getString("GEN_CEN_NOM_CENTRO"));
                unidades.add(opcion);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los centrosLanF ", e);
            throw new Exception(e);
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getComboCentrosLanF() : END");
        return unidades;
    }//getCentrosExpedienteYCertificado
    
     public String getCodOficina (Integer codTercero, String numExpediente, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCodOficina() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        String codOficina = "";
        try{            
            
//            String sql = "SELECT * FROM CP_OFICINAS " +
//                        "INNER JOIN T_DNN ON COD_POSTAL = DNN_CPO " +
//                        "INNER JOIN T_DOT ON DOT_DOM = DNN_DOM " +
//                        "WHERE DOT_TER = " + codTercero;
            String sql ="SELECT * FROM E_Ext " +
                        "Inner Join T_Hte On Ext_Ter = Hte_Ter And Ext_Nvr = Hte_Nvr " +
                        "Inner Join T_Dot On Dot_Dom = Ext_Dot And Ext_Ter = Hte_Ter " +
                        "INNER JOIN T_DNN ON DOT_DOM = DNN_DOM AND EXT_DOT = DNN_DOM " +
                        "INNER Join Cp_Oficinas On Lpad(Cod_Postal,5,'0') = Dnn_Cpo " + 
                         "WHERE EXT_NUM = '"+numExpediente+"'";
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                codOficina  = rs.getString("COD_OFICINA");
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando el código del centro ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCodOficina() : END");
        return codOficina;
    }
    
    /**
    private String getDescripcionCentro (String codCentro, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionCentro () : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select TITULAR from "
                    + GlobalNames.ESQUEMA_GENERICO 
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_CENTROS, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " where COD_CENTRO = '" + codCentro + "'";
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                valor = rs.getString("TITULAR");
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la descripción de un centro ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionCentro () : END");
        return valor;
    }//getDescripcionCentro
    **/
    
    private String getDescripcionCentroCampoDesplegable(String codOrganizacion,String codCentro, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionCentroCampoDesplegable () : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        try{
            //select * from e_des_val where des_val_cod='SS/0001' and des_cod='CENT';
            
            ResourceBundle config = ResourceBundle.getBundle("MELANBIDE03");
            String codCampoDesplegableCentros = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_CENTROS");
            
            
            String sql = "SELECT DES_NOM FROM E_DES_VAL WHERE DES_COD='" + codCampoDesplegableCentros + "' AND DES_VAL_COD='" + codCentro + "'";                    
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                valor = rs.getString("DES_NOM");
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la descripción de un centro ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionCentroCampoDesplegable () : END");
        return valor;
    }//getDescripcionCentro
    
    private String getDescripcionCentroLanFCampoDesplegable(String codOrganizacion,String codCentro, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionCentroLanFCampoDesplegable () : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        try{
//            //select * from e_des_val where des_val_cod='SS/0001' and des_cod='CENT';
//            
//            ResourceBundle config = ResourceBundle.getBundle("MELANBIDE03");
//            String codCampoDesplegableCentros = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_CENTROS");

            //TODO: jess comprobar
            
            String sql = "SELECT GEN_CEN_NOM_CENTRO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.VW_CENTROS_REG_REGEX, ConstantesMeLanbide03.FICHERO_PROPIEDADES) + " WHERE GEN_CEN_COD_CENTRO='" + codCentro + "'";                    
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                valor = rs.getString("GEN_CEN_NOM_CENTRO");
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la descripción de un centro ", e);
            throw new Exception(e);
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionCentroCampoDesplegable () : END");
        return valor;
    }//getDescripcionCentroLanF
    
    /**
     * 
     * @param unidades
     * @param codCertificado
     * @param organizacion
     * @return ArrayList<String> códigos de las unidades ya acreditadas
     */
   /* public ArrayList<CerUnidadeCompetencialVO> getCodigosUnidadesYaAcreditadas (ArrayList<CerUnidadeCompetencialVO> unidades, String codCertificado, String organizacion, String numExp ,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCodigosUnidadesYaAcreditadas() : BEGIN");
        ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadas = new ArrayList<CerUnidadeCompetencialVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            
            String[] datosExpediente = numExp.split("/");
            
            String sql = "Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " meLanbide03CertCentro "
                    + " where centro_acreditado = '" + ConstantesMeLanbide03.CENTRO_ACREDITADO_SI + "'" 
                    + " and num_expediente <> '" + numExp + "' and cod_procedimiento='" + datosExpediente[1].trim() + "'" ;                    
                    
            if(unidades!=null && unidades.size()>0){        
                    sql += " and cod_unidad in ";
                    sql += "(";
                        for(int x=0; x<unidades.size(); x++){
                            CerUnidadeCompetencialVO unidadExpediente = unidades.get(x);
                            sql += "'" + unidadExpediente.getCodUnidad() + "'";
                            if(x < unidades.size()-1){
                                sql += ",";
                            }//if(x >= unidades.size()-1)
                        }//for(int x=0; x<unidades.size(); x++)
                    sql += ")";
            }
            
            sql=sql+ " and NUM_EXPEDIENTE in (select EXT_NUM from E_EXT where EXT_TER in (select ext_ter from e_ext where ext_num='"+numExp+"') and ext_num <> '"+numExp+"')";
            
            log.debug("MeLanbide03CertCentroDAO.getCodigosUnidadesYaAcreditadas() sql: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                CerUnidadeCompetencialVO unidad = new CerUnidadeCompetencialVO();
                    unidad.setCodUnidad(rs.getString("COD_UNIDAD"));
                    unidad.setCentroAcreditado(rs.getString("CENTRO_ACREDITADO"));
                    if(rs.getString("COD_CENTRO") != null){
                        unidad.setCodCentro(rs.getString("COD_CENTRO"));
                        unidad.setDescCentro(getDescripcionCentroCampoDesplegable(organizacion,unidad.getCodCentro(), con));
                    }//if(rs.getString("COD_CENTRO") != null)
                    unidad.setCodOrigenAcred(rs.getString("COD_ORIGEN_ACREDITACION"));
                    unidad.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                    unidad.setCodMotNoAcreditado(rs.getString("COD_MOTIVO_ACREDITADO"));
                    String claveRegistral = rs.getString("CLAVE_REGISTRAL");
                    unidad.setClaveRegistral("");
                    if(claveRegistral!=null && !"".equals(claveRegistral) && !"null".equalsIgnoreCase(claveRegistral))
                        unidad.setClaveRegistral(claveRegistral);
                    
                    
                unidadesAcreditadas.add(unidad);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error eliminando los centros ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getCodigosUnidadesYaAcreditadas() : END");
        return unidadesAcreditadas;
    }//getCodigosUnidadesYaAcreditadas*/
    
    
    
      /**
     * 
     * @param unidades
     * @param codCertificado
     * @param organizacion
     * @return ArrayList<String> códigos de las unidades ya acreditadas
     */
    public ArrayList<CerUnidadeCompetencialVO> getCodigosUnidadesYaAcreditadas (ArrayList<CerUnidadeCompetencialVO> unidades, String codCertificado, String organizacion, String numExp ,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCodigosUnidadesYaAcreditadas() : BEGIN");
        ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadas = new ArrayList<CerUnidadeCompetencialVO>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            
            String[] datosExpediente = numExp.split("/");
            String docTercero="(";
            String sql1="Select HTE_DOC from T_HTE,E_EXT where EXT_TER=HTE_TER and HTE_NVR=EXT_NVR and EXT_NUM=?";
             log.debug("MeLanbide03CertCentroDAO.getCodigosUnidadesYaAcreditadas() getDOC: " + sql1);
            ps = con.prepareStatement(sql1);
            ps.setString(1, numExp );
            
               rs = ps.executeQuery();
          
            if(rs.next()){ //Solo debería tener un interesado
                docTercero=rs.getString("HTE_DOC");
            }
            rs.close();
            ps.close();
                                   
               log.debug("MeLanbide03CertCentroDAOgetDOC: " + docTercero);
            
            String sql = "Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " meLanbide03CertCentro "
                    + " where centro_acreditado = '" + ConstantesMeLanbide03.CENTRO_ACREDITADO_SI + "'" 
                    +" and cod_procedimiento=? " ;                    
                    
            if(unidades!=null && unidades.size()>0){        
                    sql += " and cod_unidad in ";
                    sql += "(";
                        for(int x=0; x<unidades.size(); x++){                           
                            sql += "?";
                            if(x < unidades.size()-1){
                                sql += ",";
                            }//if(x >= unidades.size()-1)
                        }//for(int x=0; x<unidades.size(); x++)
                    sql += ")";
            }
            
            //sql=sql+ " and NUM_EXPEDIENTE in (select a.ext_num from e_ext a, e_ext b where a.ext_ter=b.ext_ter and a.ext_num<>b.ext_num and b.ext_num=?)";
            
            sql=sql+ " and NUM_EXPEDIENTE in (select EXT_NUM from T_HTE,E_EXT where EXT_TER=HTE_TER and HTE_NVR=EXT_NVR and HTE_DOC=? and EXT_NUM<>?)";
                       
            log.debug("MeLanbide03CertCentroDAO.getCodigosUnidadesYaAcreditadas() sql: " + sql);
        
            ps = con.prepareStatement(sql);
            ps.setString(1, datosExpediente[1].trim() );
            
            int indice = 2;
            if (unidades != null && unidades.size() > 0) {

                for (int x = 0; x < unidades.size(); x++) {
                    CerUnidadeCompetencialVO unidadExpediente = unidades.get(x);
                    log.debug("MeLanbide03CertCentroDAO unidades "+unidadExpediente.getCodUnidad());
                    ps.setString(indice, unidadExpediente.getCodUnidad());
                    indice++;
                }

            }

            ps.setString(indice, docTercero);
            ps.setString(indice+1, numExp);
            
            rs = ps.executeQuery();
          
            while(rs.next()){
                CerUnidadeCompetencialVO unidad = new CerUnidadeCompetencialVO();
                    unidad.setCodUnidad(rs.getString("COD_UNIDAD"));
                    unidad.setCentroAcreditado(rs.getString("CENTRO_ACREDITADO"));
                    if(rs.getString("COD_CENTRO") != null){
                        unidad.setCodCentro(rs.getString("COD_CENTRO"));
                        unidad.setDescCentro(getDescripcionCentroCampoDesplegable(organizacion,unidad.getCodCentro(), con));
                    }//if(rs.getString("COD_CENTRO") != null)
                    if(rs.getString("COD_CENTRO_LANF") != null){
                        unidad.setCodCentro(rs.getString("COD_CENTRO_LANF"));
                        unidad.setDescCentro(getDescripcionCentroLanFCampoDesplegable(organizacion,unidad.getCodCentro(), con));
                    }
                    unidad.setCodOrigenAcred(rs.getString("COD_ORIGEN_ACREDITACION"));
                    unidad.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                    unidad.setCodMotNoAcreditado(rs.getString("COD_MOTIVO_ACREDITADO"));
                    String claveRegistral = rs.getString("CLAVE_REGISTRAL");
                    unidad.setClaveRegistral("");
                    if(claveRegistral!=null && !"".equals(claveRegistral) && !"null".equalsIgnoreCase(claveRegistral))
                        unidad.setClaveRegistral(claveRegistral);
                    
                    
                unidadesAcreditadas.add(unidad);

            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error eliminando los centros ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getCodigosUnidadesYaAcreditadas() : END");
        return unidadesAcreditadas;
    }//getCodigosUnidadesYaAcreditadas
    
    
   /**
     * Busca las unidades competenciales ya acreditadas para un/os interesado/s determinado/s en un expediente distinto al indicado en 
     * el parámetro numExpediente
     * @param unidades: Lista con las unidades competenciales a dar de alta
     * @param codCertificado: Código del certificado
     * @param organizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param interesados: Colección con los interesados del expediente
     * @return ArrayList<CerInidadeCompetencialVO> códigos de las unidades ya acreditadas
     */
    public ArrayList<CerUnidadeCompetencialVO> getCodigosUnidadesYaAcreditadas (ArrayList<CerUnidadeCompetencialVO> unidades, String codCertificado, String organizacion, String numExp , ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCodigosUnidadesYaAcreditadas() : BEGIN");
        ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadas = new ArrayList<CerUnidadeCompetencialVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            
            /**
            String sql = "Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " meLanbide03CertCentro "
                    + " where cod_certificado = '" + codCertificado + "' and centro_acreditado = '" + ConstantesMeLanbide03.CENTRO_ACREDITADO_SI + "'" 
                    + " and num_expediente <> '" + numExp + "'" 
                    + " and cod_unidad in ";
            sql += "(";
                for(int x=0; x<unidades.size(); x++){
                    CerUnidadeCompetencialVO unidadExpediente = unidades.get(x);
                    sql += "'" + unidadExpediente.getCodUnidad() + "'";
                    if(x < unidades.size()-1){
                        sql += ",";
                    }//if(x >= unidades.size()-1)
                }//for(int x=0; x<unidades.size(); x++)
            sql += ")";
            **/
            String sql = "Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " meLanbide03CertCentro,E_EXP,E_EXT,T_TER "
                    + " where cod_certificado = '" + codCertificado + "' and centro_acreditado = '" + ConstantesMeLanbide03.CENTRO_ACREDITADO_SI + "'" 
                    + " and num_expediente not like '" + numExp + "'" 
                    + " and cod_unidad in ";
            sql += "(";
                for(int x=0; x<unidades.size(); x++){
                    CerUnidadeCompetencialVO unidadExpediente = unidades.get(x);
                    sql += "'" + unidadExpediente.getCodUnidad() + "'";
                    if(x < unidades.size()-1){
                        sql += ",";
                    }//if(x >= unidades.size()-1)
                }//for(int x=0; x<unidades.size(); x++)
            sql += ")";
            
            sql += " AND E_EXP.EXP_NUM=meLanbide03CertCentro.NUM_EXPEDIENTE AND E_EXP.EXP_MUN=meLanbide03CertCentro.COD_ORGANIZACION" + 
                   " AND E_EXP.EXP_MUN=meLanbide03CertCentro.COD_ORGANIZACION AND E_EXP.EXP_NUM=E_EXT.EXT_NUM AND E_EXP.EXP_MUN = E_EXT.EXT_MUN" + 
                   " AND E_EXP.EXP_PRO=E_EXT.EXT_PRO AND E_EXT.EXT_TER=T_TER.TER_COD ";
                        
            StringBuffer sb = new StringBuffer();
            sb.append("(");
            for(int i=0;interesados!=null && i<interesados.size();i++){                
                sb.append("'" + interesados.get(i).getDocumento() + "'");
                if(interesados.size()-i>1) sb.append(",");
            }// for
            sb.append(")");
            
            if(interesados!=null && interesados.size()>=1){                
                sql+=" AND TER_DOC IN " + sb.toString();
            }
                        
            log.debug("MeLanbide03CertCentroDAO.getCodigosUnidadesYaAcreditadas() sql: " + sql);
            st = con.createStatement();            
            rs = st.executeQuery(sql);

            while(rs.next()){
                CerUnidadeCompetencialVO unidad = new CerUnidadeCompetencialVO();
                    unidad.setCodCertificado(rs.getString("COD_CERTIFICADO"));
                    unidad.setCodUnidad(rs.getString("COD_UNIDAD"));
                    unidad.setCentroAcreditado(rs.getString("CENTRO_ACREDITADO"));
                    if(rs.getString("COD_CENTRO") != null){
                        unidad.setCodCentro(rs.getString("COD_CENTRO"));
                        //unidad.setDescCentro(getDescripcionCentro(unidad.getCodCentro(), con));
                    }//if(rs.getString("COD_CENTRO") != null)
                    unidad.setCodOrigenAcred(rs.getString("COD_ORIGEN_ACREDITACION"));
                    unidad.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                    unidad.setCodMotNoAcreditado(rs.getString("COD_MOTIVO_ACREDITADO"));
                    unidad.setClaveRegistral(rs.getString("CLAVE_REGISTRAL"));
                unidadesAcreditadas.add(unidad);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error eliminando los centros ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getCodigosUnidadesYaAcreditadas() : END");
        return unidadesAcreditadas;
    }//getCodigosUnidadesYaAcreditadas
    
    
    
    public ArrayList<CerUnidadeCompetencialVO> getUnidadesAcreditadasExpediente (String organizacion, String numExp , Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getUnidadesAcreditadasExpediente() : BEGIN");
        ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql= "Select "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) + ".*, TFE_VALOR "
                    +" FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    +" LEFT JOIN E_TFE ON TFE_NUM = NUM_EXPEDIENTE AND TFE_COD = 'FECHAPRESENTACION'"
                    + " where NUM_EXPEDIENTE = '" + numExp + "' and COD_ORGANIZACION = " + organizacion 
                    + " and CENTRO_ACREDITADO = " + ConstantesMeLanbide03.CENTRO_ACREDITADO_SI 
                    + " ORDER BY COD_UNIDAD";
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                CerUnidadeCompetencialVO unidad = new CerUnidadeCompetencialVO();
                unidad.setCodUnidad(rs.getString("COD_UNIDAD"));
                unidad.setClaveRegistral(rs.getString("CLAVE_REGISTRAL"));
                unidad.setFechaCreacion(rs.getString("TFE_VALOR"));
                //unidad.setCodOficina(getCodOficina(rs.getString("COD_CENTRO"), con));
                unidades.add(unidad);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando las unidades acreditadas para un expediente ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getUnidadesAcreditadasExpediente() : END");
        return unidades;
    }//getUnidadesAcreditadasExpediente
    
    public boolean tieneUnidadesAcreditadasExpediente (String organizacion, String numExp , Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("tieneUnidadesAcreditadasExpediente() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql= "Select "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) + ".COD_UNIDAD"
                    +" FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    +" where NUM_EXPEDIENTE = '" + numExp + "' and COD_ORGANIZACION = " + organizacion 
                    + " and CENTRO_ACREDITADO = " + ConstantesMeLanbide03.CENTRO_ACREDITADO_SI;
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            if(rs.next()){
                return true;
            }
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando las unidades acreditadas para un expediente ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("tieneUnidadesAcreditadasExpediente() : END");
        return false;
    }//tieneUnidadesAcreditadasExpediente
    
}//class
