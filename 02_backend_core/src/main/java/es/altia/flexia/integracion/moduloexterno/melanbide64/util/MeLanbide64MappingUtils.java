package es.altia.flexia.integracion.moduloexterno.melanbide64.util;


import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author altia
 */
public class MeLanbide64MappingUtils {

    private static MeLanbide64MappingUtils instance = null;

    private MeLanbide64MappingUtils() {
    }

    public static MeLanbide64MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide64MappingUtils.class) {
                instance = new MeLanbide64MappingUtils();
            }
        }
        return instance;
    }

  public Object map(ResultSet rs, Class clazz) throws Exception
    {
         if(clazz == TerceroVO.class)
        {
            return mapearTercero(rs);
        }
        else if(clazz == DireccionVO.class)
        {
            return mapearDomicilio(rs);
        }
        else if(clazz == ExpedienteVO.class)
        {
            return mapearExpediente(rs);
        }
        else if (clazz == InteresadoExpedienteVO.class)
        {
        return mapearInteresado(rs);
        }
        else if (clazz == IdExpedienteVO.class)
        {
        return mapearIdExpediente(rs);
        }    
                    
        return null;
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
        //ter.setExtRol(rs.getString("ROL"));
        return ter;
    }
    
    private DireccionVO mapearDomicilio(ResultSet rs) throws Exception
    {
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
          
        return dom;

    }
    
    private ExpedienteVO mapearExpediente(ResultSet rs) throws SQLException
    {
       // ExpedienteVO exp = new ExpedienteVO();
        
        //exp.setUor(rs.getInt("CRO_UTR"));
       // exp.setUorTramiteInicio(rs.getInt("CRO_UTR"));
       // exp.setUsuario(rs.getInt("CRO_USU"));
        
       // return exp;
        
            {
        ExpedienteVO exp = new ExpedienteVO();
        
        exp.setAsunto(rs.getString("ASU"));
        exp.setUor(rs.getInt("UOR"));
        exp.setUorTramiteInicio(rs.getInt("UTR"));
        exp.setUsuario(rs.getInt("USU"));
        
        return exp;
      
        
    }
      
        
    }

    private Object mapearInteresado(ResultSet rs) throws SQLException {
        InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();
        
        interesado.setRol(rs.getInt("EXT_ROL"));
       return interesado;
    }

    private Object mapearIdExpediente(ResultSet rs) throws SQLException {
       IdExpedienteVO idexpediente=new IdExpedienteVO();
       idexpediente.setEjercicio(rs.getInt("EXP_EJE"));
       idexpediente.setProcedimiento(rs.getString("EXP_PRO"));
       idexpediente.setNumeroExpediente(rs.getString("EXP_NUM"));
       idexpediente.setNumero(rs.getInt("EXP_TRA"));
       return idexpediente;
    }    
   

}
