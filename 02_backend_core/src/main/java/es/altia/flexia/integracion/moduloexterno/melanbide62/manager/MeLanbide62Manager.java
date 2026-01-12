package es.altia.flexia.integracion.moduloexterno.melanbide62.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide62.dao.MeLanbide62DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide62.util.ConstantesMeLanbide62;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.InfoExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.InfoTramiteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide62Manager {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide62Manager.class);    
    
    //Instancia
    private static MeLanbide62Manager instance = null;
    
    private MeLanbide62Manager() {
        
    }
    
    public static MeLanbide62Manager getInstance() {
        if(instance == null) {
            synchronized(MeLanbide62Manager.class) {
                instance = new MeLanbide62Manager();
            }
        }
        return instance;
    }
    
    public ArrayList<InfoTramiteVO> getDatosSuplementariosTramite(int codOrg, int ejerc, String numExp, int codTram, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide62DAO meLanbide62DAO = MeLanbide62DAO.getInstance();
            return meLanbide62DAO.getDatosSuplementariosTramite(codOrg, ejerc, numExp, codTram, con);
        } catch(BDException e) {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            throw new Exception(e);
        } catch(SQLException ex) {
            log.error("Se ha producido un error al recuperar valores de datos suplementarios para el expediente  "+numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);       
            } catch(Exception e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public InfoExpedienteVO getDatosSuplementariosExpediente(ArrayList<String> codsCampo, int codOrg, int ejerc, String codProc, String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        InfoExpedienteVO infoExp = null;
        
        try {
            con = adaptador.getConnection();
            MeLanbide62DAO meLanbide62DAO = MeLanbide62DAO.getInstance();
            
            infoExp =  meLanbide62DAO.getDatosSuplementariosExpediente(codsCampo, codOrg, ejerc, codProc, numExp, con);
        } catch(BDException e) {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            throw new Exception(e);
        } catch(SQLException ex) {
            log.error("Se ha producido un error al recuperar valores de datos suplementarios para el expediente  "+numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);       
            } catch(Exception e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        return infoExp;
    }
    
    public boolean grabarValorCampoSup(String codCampo, String valorCampo, String codOrg, int ejercicio, String numExp, AdaptadorSQLBD adaptador) throws BDException, SQLException {
        Connection con = null;
        int resultado = 0;
        
        try {
            con = adaptador.getConnection();
            MeLanbide62DAO meLanbide62DAO = MeLanbide62DAO.getInstance();
            adaptador.inicioTransaccion(con);
            resultado =  meLanbide62DAO.grabarValorCampoSupTTL(codCampo, valorCampo, codOrg, ejercicio, numExp, con);
            adaptador.finTransaccion(con);
        } catch(BDException e) {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            adaptador.rollBack(con);
            throw e;
        } catch(SQLException ex) {
            log.error("Se ha producido un error al grabar valores de datos suplementarios para el expediente  "+numExp, ex);
            adaptador.rollBack(con);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);       
            } catch(Exception e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        return resultado>0;
    }
    
    public Date setValorCampoFechaCalculada(int codOrg, int ejerc, String codProc, String numExp, AdaptadorSQLBD adapt) throws Exception{
        Connection con = null;
        InfoExpedienteVO expediente = null;
        ArrayList<String> codigos = new ArrayList<String>();
        HashMap<String,Object> valMap = null;
        int realizado = 0;
        Date fechaInicial = null;
        Date fechaFinal = null;
        int dias = 0;
        
        try{
            con = adapt.getConnection();
            
            codigos.add(ConstantesMeLanbide62.FECHAINI_SEPE);
            codigos.add(ConstantesMeLanbide62.DIAS_SEPE);
            expediente = getDatosSuplementariosExpediente(codigos, codOrg, ejerc, codProc, numExp, adapt);
            valMap = expediente.getCamposSuplementarios();
            
            if(valMap.containsKey(ConstantesMeLanbide62.DIAS_SEPE))
                dias = (Integer) valMap.get(ConstantesMeLanbide62.DIAS_SEPE);
            if(valMap.containsKey(ConstantesMeLanbide62.FECHAINI_SEPE))
                fechaInicial = (Date) valMap.get(ConstantesMeLanbide62.FECHAINI_SEPE);
            
            if(fechaInicial!=null){
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaInicial);
                //cal.add(Calendar.DAY_OF_MONTH, dias);
                cal.add(Calendar.DAY_OF_MONTH, dias - 1);
                fechaFinal = cal.getTime();
            }   
            adapt.inicioTransaccion(con);
            realizado = MeLanbide62DAO.getInstance().grabarValorCampoSup(ConstantesMeLanbide62.FECHAFIN_SEPE, fechaFinal, String.valueOf(codOrg), ejerc, numExp, con);
            adapt.finTransaccion(con);
        } catch (BDException bde) {
           log.error("Error al obtener una conexión a la bbdd");
           adapt.rollBack(con);
           throw new Exception(bde);
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de la bbdd");
                throw new Exception(ex);
            }
        }
        if(realizado>0) return fechaFinal;
        return null;
    }
    
    public boolean setValorDefectoCS(String codCampo, String valCampo, int codOrg, int ejerc, String codPro, String numExp, int codTram, int ocuTram, AdaptadorSQLBD adapt) throws BDException, SQLException{
        Connection con = null;
        int realizado = 0;
        
        try{
            con = adapt.getConnection();
            
            adapt.inicioTransaccion(con);
            realizado = MeLanbide62DAO.getInstance().setValorDefectoCS(codCampo, valCampo, String.valueOf(codOrg), ejerc, codPro, numExp, codTram, ocuTram, con);
            adapt.finTransaccion(con);
        } catch (BDException bde) {
           log.error("Error al obtener una conexión a la bbdd");
           adapt.rollBack(con);
           throw bde;
        } catch (SQLException sqle) {
           log.error("Error al grabar el valor de un dato suplementario");
           adapt.rollBack(con);
           throw sqle;
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de la bbdd");
                throw ex;
            }
        }
        return realizado>0;
    }
    
    public ExpedienteVO getDatosExpediente(int codOrg, String numExpediente, Connection con) throws Exception{
        return MeLanbide62DAO.getInstance().getDatosExpediente(codOrg, numExpediente, con);
    }
    
    public void actualizaEdad(int codOrg, String numExpediente, Integer edad, Connection con) throws Exception{
        actualizaSuplNumerico(codOrg, numExpediente, edad, "EDAD", con);
    }
    
    private void actualizaSuplNumerico(int codOrg, String numExpediente, Integer valor, String nombreCampo, Connection con) throws Exception{
        MeLanbide62DAO.getInstance().deleteSuplNumerico(codOrg, numExpediente, nombreCampo, con);
        if(valor!=null){
            MeLanbide62DAO.getInstance().insertSuplNumerico(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }
}
