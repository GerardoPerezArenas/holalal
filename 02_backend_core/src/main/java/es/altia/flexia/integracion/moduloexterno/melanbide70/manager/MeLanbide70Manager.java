package es.altia.flexia.integracion.moduloexterno.melanbide70.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide70.dao.MeLanbide70DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide70.util.ConstantesMeLanbide70;
import es.altia.flexia.integracion.moduloexterno.melanbide70.vo.DatosEconomicosExpVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.flexia.integracion.moduloexterno.melanbide70.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoDesplegableModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide70Manager {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide70Manager.class);
    
    //Instancia
    private static MeLanbide70Manager instance = null;

    private MeLanbide70Manager() {
    }
    
    //Devolvemos una �nica instancia de la clase a trav�s de este m�todo ya que el constructor es privado
    public static MeLanbide70Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide70Manager.class)
            {
                instance = new MeLanbide70Manager();
            }
        }
        return instance;
    }
    
    public ExpedienteVO getDatosExpediente(int codOrg, String numExpediente, Connection con) throws Exception{
        return MeLanbide70DAO.getInstance().getDatosExpediente(codOrg, numExpediente, con);
    }
        
    public DatosEconomicosExpVO getImporteSubvencionYPorc(Integer edad, String sexo, String pro, String eje, Connection con) throws Exception{
        return MeLanbide70DAO.getInstance().getImporteSubvencion(edad, sexo, pro, eje, con);
    }
        
    public void actualizaImporteSubvencion(int codOrg, String numExpediente, Integer importeSubvencion, Connection con) throws Exception{
        actualizaSuplNumerico(codOrg, numExpediente, importeSubvencion, ConstantesMeLanbide70.getPropVal_CS_SUBV(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }
        
    public void actualizaImportePrimerPago(int codOrg, String numExpediente, Integer importePrimerPago, Connection con) throws Exception{
        actualizaSuplNumerico(codOrg, numExpediente, importePrimerPago, ConstantesMeLanbide70.getPropVal_CS_PRIMERPAGO(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }
        
    public void actualizaEdad(int codOrg, String numExpediente, Integer edad, Connection con) throws Exception{
        actualizaSuplNumerico(codOrg, numExpediente, edad, "EDAD", con);
    }
        
    private void actualizaSuplNumerico(int codOrg, String numExpediente, Integer valor, String nombreCampo, Connection con) throws Exception{
        MeLanbide70DAO.getInstance().deleteSuplNumerico(codOrg, numExpediente, nombreCampo, con);
        if(valor!=null){
            MeLanbide70DAO.getInstance().insertSuplNumerico(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }
        
    private void actualizaSuplDespl(int codOrg, String numExpediente, String valor, String nombreCampo, Connection con) throws Exception{
        MeLanbide70DAO.getInstance().deleteSuplDespl(codOrg, numExpediente, nombreCampo, con);
        if(valor!=null){
            MeLanbide70DAO.getInstance().insertSuplDespl(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }
        
    public void actualizaSexo(int codOrg, String numExpediente, String sexo, Connection con) throws Exception{
        actualizaSuplDespl(codOrg, numExpediente, sexo, "SEXO", con);
    }
}
