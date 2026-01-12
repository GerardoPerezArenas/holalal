package es.altia.flexia.integracion.moduloexterno.melanbide52.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide52.exception.MeLanbide52Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide52.utils.MeLanbide51Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide52.vo.ubicacion.UbicacionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author oscar
 */
public class MeLanbide52DAO {

    private Logger log = LogManager.getLogger(MeLanbide52DAO.class);

    
    
    
     /**
     * Recupera el documento de un interesado que tenga el rol por defecto en el expediente, y además se comprueba
     * que su documento sea un CIF (tipo documento 4 o 5 en Flexia)
     * Si hay más de un interesado se recupera uno de ellos, aunque esto no debería pasar.
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente     
     * @param con: Conexión a la BBDD
     * @return String con el documento o null en caso contrario
     */
    public String getDocumentoInteresadoRolDefecto(int codOrganizacion,String numExpediente, Connection con) throws MeLanbide52Exception, SQLException{
        String documento = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        
        String[] datos = numExpediente.split("/");
        String ejercicio = datos[0];
        String codProcedimiento = datos[1];
        
        String sql = "SELECT HTE_DOC,HTE_TID,HTE_DOC FROM E_EXT,T_HTE,E_ROL WHERE EXT_MUN=? AND EXT_NUM=? AND EXT_EJE=? AND EXT_PRO=? " + 
                     "AND EXT_TER=T_HTE.HTE_TER AND (HTE_TID=4 OR HTE_TID=5) AND EXT_NVR=T_HTE.HTE_NVR AND ROL_MUN=? AND ROL_MUN=EXT_MUN AND " + 
                     "ROL_PRO=EXT_PRO AND ROL_PRO=EXT_PRO AND ROL_PDE=1 AND ROL_COD=EXT_ROL";
        log.debug("getDocumentoInteresadoRolDefecto sql: " + sql);
        log.debug("getDocumentoInteresadoRolDefecto param1: " + codOrganizacion + ",param2: " + numExpediente + ",param3: " + ejercicio + ",codProcedimiento: " + codProcedimiento + ",codOrganizacion: " + codOrganizacion);
        
        try{
            ps = con.prepareStatement(sql);
            int i=1;
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++,numExpediente);
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++, codOrganizacion);
            rs = ps.executeQuery();
            while(rs.next()){
                documento = rs.getString("HTE_DOC");                
            }            
            
        }catch(Exception e){
            e.printStackTrace();
            throw new MeLanbide52Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
        }finally{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();

        }
        
        return documento;
    }
    
    
    
    /**
     * Recupera el código de centro que se busca a través de un CIF
     * @param documento: String que contiene el CIF
     * @param con: Conexión a la BBDD
     * @return ArrayList<String>: Colección con los centros homologados
     * @throws MeLanbide51Exception si ocurre algún error
     */
    public ArrayList<String> getCodCentro(String documento,Connection con) throws MeLanbide52Exception, SQLException{
        ArrayList<String> centros = new ArrayList<String>();        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT LAN_CENTROS_COLABORADORES.GEN_CEN_COD_CENTRO " +
                         "FROM GEN_EMPRESARIO,LAN_CENTROS_COLABORADORES " +
                         "WHERE GEN_USU_NUM_DOC=? AND GEN_EMPRESARIO.GEN_EMP_CORR = LAN_CENTROS_COLABORADORES.GEN_EMP_CORR AND LAN_CENTROS_COLABORADORES.GEN_CEN_ESTADO ='HO'";

            log.debug("getCodCentro sql = " + sql);
            log.debug("getCodCentro parametro = " + documento);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,documento);
            
            rs = ps.executeQuery();
            while(rs.next()){
                centros.add(rs.getString("GEN_CEN_COD_CENTRO"));
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            throw new MeLanbide52Exception("Error al recuperar el código de centro: " + e.getMessage());
        }finally{
            
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();

        }        
        return centros;
    }
    
    
    /**
     * Recupera las ubicaciones de un centro
     * @param documento: CIF correspondiente al centro
     * @param con: Conexión a la BBDD
     * @return RespuestaUbicacionesVO: Objeto instancia de la clase RespuestaUbicacionesVO con las ubicaciones y en caso
     * contrario, con el código de error producido
     * @throws MeLanbide51Exception 
     */
    public ArrayList<UbicacionVO> getUbicacionesCentro(String codCentro, Connection con) throws MeLanbide52Exception, SQLException{        
        ArrayList<UbicacionVO> ubicaciones = new ArrayList<UbicacionVO>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        
        String sql = "SELECT LAN_CENTROS_UBICACIONES.GEN_CEN_COD_UBIC AS CODUBICACION," +
                    " LAN_CENTROS_UBICACIONES.GEN_CEN_NOM_UBIC, LAN_CENTROS_UBICACIONES.GEN_GRAL_TIPO_VIA," +
                    " LAN_CENTROS_UBICACIONES.GEN_CEN_NOM_VIA,LAN_CENTROS_UBICACIONES.GEN_CEN_NUM_VIA," +
                    " LAN_CENTROS_UBICACIONES.GEN_GRAL_BISDUPLICADO,LAN_CENTROS_UBICACIONES.GEN_CEN_ESCALERA," +
                    " LAN_CENTROS_UBICACIONES.GEN_CEN_PISO,LAN_CENTROS_UBICACIONES.GEN_CEN_PUERTA," +
                    " LAN_CENTROS_UBICACIONES.GEN_PRO_COD_PROV, LAN_CENTROS_UBICACIONES.GEN_MUN_COD_MUN, LAN_CENTROS_UBICACIONES.GEN_COD_CODPOSTAL" +
                    " FROM LAN_CENTROS_UBICACIONES" +
                    " WHERE LAN_CENTROS_UBICACIONES.GEN_CEN_COD_CENTRO=? " +                    
                    " AND LAN_CENTROS_UBICACIONES.GEN_CEN_ESTADO ='HO'";
        
        log.debug("getUbicacionesCentro sql: " + sql);
        log.debug("getUbicacionesCentro param: " + codCentro);
                 
        try{
            ps = con.prepareStatement(sql);
            int i=1;            
            ps.setString(i++,codCentro);
            rs = ps.executeQuery();
            
            Hashtable<String,String> provincias = new Hashtable<String,String>();
            Hashtable<String,String> municipios = new Hashtable<String,String>();
            
            while(rs.next()){
                String codUbicacion     = rs.getString("CODUBICACION");
                String nombreUbicacion  = rs.getString("GEN_CEN_NOM_UBIC");
                String tipoVia          = rs.getString("GEN_GRAL_TIPO_VIA");
                String nombreCalle      = rs.getString("GEN_CEN_NOM_VIA");
                String numeroCalle      = rs.getString("GEN_CEN_NUM_VIA");
                String bis              = rs.getString("GEN_GRAL_BISDUPLICADO");
                String escalera         = rs.getString("GEN_CEN_ESCALERA");
                String piso             = rs.getString("GEN_CEN_PISO");
                String puerta           = rs.getString("GEN_CEN_PUERTA");
                String codProvincia     = rs.getString("GEN_PRO_COD_PROV");
                String codMunicipio     = rs.getString("GEN_MUN_COD_MUN");
                String codPostal        = rs.getString("GEN_COD_CODPOSTAL");
                
                UbicacionVO ubicacion = new UbicacionVO();                
                ubicacion.setCodCentro(codCentro);
                ubicacion.setNombreUbicacion(nombreUbicacion);
                ubicacion.setCodUbicacion(codUbicacion);
                ubicacion.setNombreCalle(nombreCalle);
                ubicacion.setNumeroCalle(numeroCalle);
                ubicacion.setBis(bis);
                ubicacion.setEscalera(escalera);
                ubicacion.setPiso(piso);
                ubicacion.setPuerta(puerta);
                ubicacion.setTipoCalle(tipoVia);
                ubicacion.setCodProvincia(codProvincia);
                ubicacion.setCodMunicipio(codMunicipio);
                ubicacion.setCodPostal(codPostal);
                
                if(codProvincia!=null && codMunicipio!=null && MeLanbide51Utils.isInteger(codProvincia) && MeLanbide51Utils.isInteger(codMunicipio)){
                    
                    Integer iProvincia = new Integer(codProvincia);
                    //Integer iMunicipio = new Integer(codMunicipio);
                    
                    
                    if(provincias.contains(new Integer(codProvincia)))
                        ubicacion.setDescProvincia(provincias.get(codProvincia));
                    else{
                        String descProvincia = getDescProvincia(108,iProvincia, con);
                        if(descProvincia!=null){
                            ubicacion.setDescProvincia(descProvincia);
                            provincias.put(iProvincia.toString(),descProvincia);                        
                        }else
                            ubicacion.setDescProvincia("-");
                    }

                    String aux = codMunicipio.substring(2,codMunicipio.length());
                    Integer iMunicipio = new Integer(aux);
                    
                    if(municipios.contains(iProvincia + "-" + iMunicipio)){
                        ubicacion.setDescMunicipio(municipios.get(iProvincia + "-" + iMunicipio));                        
                    }else{                        
                        String descMunicipio = getDescMunicipio(108,iProvincia,iMunicipio, con);
                        if(descMunicipio!=null){
                            ubicacion.setDescMunicipio(descMunicipio);                        
                            municipios.put(iProvincia + "-" + iMunicipio,descMunicipio);
                        }else
                            ubicacion.setDescMunicipio("-");                        
                    }
                }
                ubicaciones.add(ubicacion);                
            }            
            
        }catch(Exception e){
            e.printStackTrace();
            throw new MeLanbide52Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        }finally{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();

        }
        
        return ubicaciones;        
    } 
    
    
    
     /**
     * Recupera la descripción de la provincia
     * @param codPais: Código del pais
     * @param codProvincia: Código de la provincia
     * @param con: Conexión a la BBDD
     * @return String con al descripción 
     * @throws MeLanbide51Exception si ocurre algún error
     */
    public String getDescProvincia(int codPais,int codProvincia,Connection con) throws MeLanbide52Exception, SQLException{
        String salida = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT PRV_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PRV " +
                         "WHERE PRV_PAI=" + codPais + " AND PRV_COD=" + codProvincia;
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                salida = rs.getString("PRV_NOM");
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            throw new MeLanbide52Exception("Error al recuperar la descripción de la provincia: " + e.getMessage());
        }finally{            
            if(rs!=null) rs.close();
            if(st!=null) st.close();

        }        
        return salida;
    }
    
    
    /**
     * Recupera la descripción de un municipio
     * @param codPais: código del pais
     * @param codProvincia: Código de la provincia
     * @param codMunicipio: Códigio del municipio
     * @param con: Conexión a la BBDD
     * @return String con al descripción 
     * @throws MeLanbide51Exception si ocurre algún error
     */
    public String getDescMunicipio(int codPais,int codProvincia,int codMunicipio,Connection con) throws MeLanbide52Exception, SQLException{
        String salida = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            String sql = "SELECT MUN_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN " +
                         "WHERE MUN_PAI=" + codPais + " AND MUN_PRV=" + codProvincia + " AND MUN_COD=" + codMunicipio;
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                salida = rs.getString("MUN_NOM");
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            throw new MeLanbide52Exception("Error al recuperar la descripción del municipio: " + e.getMessage());
        }finally{            
            if(rs!=null) rs.close();
            if(st!=null) st.close();

        }        
        return salida;
    }
    
    
     /**
      * Recupera la ocurrencia más reciente de un determinado trámite de un expedinete
      * @param codTramite: Código del trámite
      * @param numExpediente: Número del expediente
      * @param codOrganizacion: Cód. organización
      * @param con: Conexión a la BBDD
      * @return int 0 -1 sino se ha podido recuperar
      */
    public int getOcurrenciaMasRecienteTramite(int codTramite,String numExpediente,String codOrganizacion,Connection con) throws SQLException{
        int salida = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];
            
            String sql = "SELECT MAX(CRO_OCU) AS NUM FROM E_CRO WHERE CRO_NUM=? AND CRO_PRO=? AND CRO_MUN=? AND CRO_EJE=? AND CRO_TRA=?";
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setString(i++,numExpediente);
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,Integer.parseInt(codOrganizacion));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setInt(i++,codTramite);
            rs = ps.executeQuery();
            
            while(rs.next()){                
                salida = rs.getInt("NUM");
            }
            
        }catch(SQLException e){
            
        }finally{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
        }
        
        return salida;
        
    }
    
    
    /**
     * Recupera el número de censo a partir de un código de centro y de un código de ubicación
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param con: Conexión a la BBDD
     * @return  String o null sino se ha podido recuperar o no existe
     */
    public String getNumeroCenso(String codCentro,String codUbicacion,Connection con) throws MeLanbide52Exception, SQLException{
        String salida = null;
        ResultSet rs = null;
        PreparedStatement st = null;
        
        try{
            String sql ="SELECT LAN_NUM_REG_AUT FROM LAN_CENTROS_SERVICIOS " +
                        "WHERE GEN_CEN_COD_CENTRO=? AND GEN_CEN_COD_UBIC=? AND LAN_UBIC_SERVICIO_LMV='314'";
            
            int i=1;
            st = con.prepareStatement(sql);
            st.setString(i++,codCentro);
            st.setString(i++,codUbicacion);
            
            rs = st.executeQuery();
            while(rs.next()){
                salida = rs.getString("LAN_NUM_REG_AUT");
                
            }
            
            
        }catch(SQLException e){
            e.printStackTrace();
            throw new MeLanbide52Exception("Error al recuperar el número de censo: " + e.getMessage());
        }finally{
            if(rs!=null) rs.close();
            if(st!=null) st.close();

        }
        
        return salida;
    }
    
    
}
