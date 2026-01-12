package es.altia.flexia.integracion.moduloexterno.melanbide71.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide71.dao.MeLanbide71DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide71.util.ConstantesMeLanbide71;
import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.ExpedienteVO;
import java.sql.Connection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide71Manager {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide71Manager.class);    
    
    //Instancia
    private static MeLanbide71Manager instance = null;

    private MeLanbide71Manager() {
    }
    
    //Devolvemos una única instancia de la clase a través de este método ya que el constructor es privado
    public static MeLanbide71Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide71Manager.class) {
                instance = new MeLanbide71Manager();
            }
        }
        return instance;
    }
    
    public ExpedienteVO getDatosExpediente(int codOrg, String numExpediente, Connection con) throws Exception{
        return MeLanbide71DAO.getInstance().getDatosExpediente(codOrg, numExpediente, con);
    }
        
    public ExpedienteVO getDatosExpAPEAI(int codOrg, String numExpediente, Connection con) throws Exception {
        return MeLanbide71DAO.getInstance().getDatosExpAPEAI(codOrg, numExpediente, con);
    }

    public DatosEconomicosExpVO getImporteSubvencion(Integer edad, String sexo, String pro, String eje, Connection con) throws Exception {
        return MeLanbide71DAO.getInstance().getImporteSubvencion(edad, sexo, pro, eje, con);
    }
        
    public void actualizaImporteSubvencion(int codOrg, String numExpediente, Integer importeSubvencion, Connection con) throws Exception{
        actualizaSuplNumerico(codOrg, numExpediente, importeSubvencion, ConstantesMeLanbide71.getPropVal_CS_SUBV(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }
        
    public void actualizaImportePrimerPago(int codOrg, String numExpediente, Integer importePrimerPago, Connection con) throws Exception{
        actualizaSuplNumerico(codOrg, numExpediente, importePrimerPago, ConstantesMeLanbide71.getPropVal_CS_PRIMERPAGO(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }
        
    public void actualizaEdad(int codOrg, String numExpediente, Integer edad, Connection con) throws Exception{
        actualizaSuplNumerico(codOrg, numExpediente, edad, ConstantesMeLanbide71.getPropVal_CS_EDAD(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaSexo(int codOrg, String numExpediente, String sexo, Connection con) throws Exception {
        actualizaSuplDespl(codOrg, numExpediente, sexo, ConstantesMeLanbide71.getPropVal_CS_SEXO(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }
        
    private void actualizaSuplNumerico(int codOrg, String numExpediente, Integer valor, String nombreCampo, Connection con) throws Exception{
        MeLanbide71DAO.getInstance().deleteSuplNumerico(codOrg, numExpediente, nombreCampo, con);
        if(valor!=null){
            MeLanbide71DAO.getInstance().insertSuplNumerico(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }
        
    private void actualizaSuplDespl(int codOrg, String numExpediente, String valor, String nombreCampo, Connection con) throws Exception{
        MeLanbide71DAO.getInstance().deleteSuplDespl(codOrg, numExpediente, nombreCampo, con);
        if(valor!=null){
            MeLanbide71DAO.getInstance().insertSuplDespl(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }
}
