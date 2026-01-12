/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide54.util;

import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.RegistroAACCVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.RegistroBatchVO;
import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidl
 */
public class MeLanbide54MappingUtils {

    private static MeLanbide54MappingUtils instance = null;
    private static Logger log = LogManager.getLogger(MeLanbide54MappingUtils.class);
    
    private MeLanbide54MappingUtils()
    {
        
    }
    
    public static MeLanbide54MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide54MappingUtils.class)
            {
                instance = new MeLanbide54MappingUtils();
            }
        }
        return instance;
    }
        
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == RegistroAACCVO.class)
        {
            return mapearRegistroAACC(rs);
        }   
        else if(clazz == TerceroVO.class)
        {
            return mapearTercero(rs);
        }  
        else if(clazz == RegistroBatchVO.class)
        {
            return mapearRegistroBatch(rs);
        }         
         
        return null;
    }
    
    public RegistroAACCVO mapearRegistroAACC(ResultSet rs) throws SQLException
    {
        RegistroAACCVO fila = new RegistroAACCVO();        
        fila.setResDep(rs.getInt("RES_DEP"));
        fila.setResUor(rs.getInt("RES_UOR"));
        fila.setResTip(rs.getString("RES_TIP"));
        fila.setResEje(rs.getInt("RES_EJE"));                
        fila.setResNum(rs.getInt("RES_NUM"));        
        fila.setResUsu(rs.getInt("RES_USU")); 
        fila.setResTer(rs.getInt("RES_TER")); 
        fila.setResAsu(rs.getString("RES_ASU"));                
        
        return fila;
    }   

     private TerceroVO mapearTercero(ResultSet rs) throws Exception
    {
        TerceroVO ter = new TerceroVO();
        ter.setAp1(rs.getString("AP1"));
        ter.setAp2(rs.getString("AP2"));
        ter.setNombre(rs.getString("NOM"));
        ter.setDoc(rs.getString("DOC"));
        ter.setEmail(rs.getString("DCE"));
        ter.setTelefono(rs.getString("TLF"));
        ter.setTipoDoc(rs.getString("TID"));

        DireccionVO dom = new DireccionVO();
        dom.setBloque(rs.getString("DNN_BLQ"));
        dom.setCodMunicipio(rs.getInt("DNN_MUN"));
        dom.setCodPais(rs.getInt("DNN_PAI"));
        dom.setCodPostal(rs.getString("DNN_CPO"));
        dom.setCodProvincia(rs.getInt("DNN_PRV"));
        dom.setEmplazamiento(rs.getString("DNN_DMC"));
        dom.setEsPrincipal(true);
        dom.setEscalera(rs.getString("DNN_ESC"));
        dom.setNombreVia(rs.getString("VIA_NOM"));
        dom.setPlanta(rs.getString("DNN_PLT"));
        dom.setPortal(rs.getString("DNN_POR"));
        dom.setPrimerNumero(rs.getInt("DNN_NUD"));
        dom.setPrimeraLetra(rs.getString("DNN_LED"));
        dom.setPuerta(rs.getString("DNN_PTA"));
        dom.setTipoVia(rs.getInt("DNN_TVI"));
        dom.setUltimaLetra(rs.getString("DNN_LEH"));
        dom.setUltimoNumero(rs.getInt("DNN_NUH"));
        
        ter.setDomicilio(dom);
          
        return ter;

    }

    public RegistroBatchVO mapearRegistroBatch(ResultSet rs) throws SQLException
    {
        RegistroBatchVO fila = new RegistroBatchVO();        
        fila.setId(rs.getInt("ID"));
        fila.setFecha(rs.getString("FEC_REGISTRO"));
        fila.setEjerRegistro(rs.getInt("EJER_REGISTRO"));
        fila.setNumRegistro(rs.getInt("NUM_REGISTRO"));
        fila.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
        fila.setCodTramite(rs.getInt("COD_TRA"));     
        fila.setOperacion(rs.getString("OPERACION"));
        fila.setResultado(rs.getString("RESULTADO")); 
        fila.setCodOperacion(rs.getInt("COD_OPERACION"));  
        fila.setRelanzar(rs.getInt("RELANZAR"));    
        fila.setObservaciones(rs.getString("OBSERVACIONES"));         
        
        return fila;
    }       
       
}
