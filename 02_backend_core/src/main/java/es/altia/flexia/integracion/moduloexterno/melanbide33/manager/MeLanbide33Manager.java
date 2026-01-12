/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide33.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide33.dao.MeLanbide33DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide33.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide33.util.ConstantesMeLanbide33;
import es.altia.flexia.integracion.moduloexterno.melanbide33.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide33.vo.FilaMinimisVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide33Manager 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide33Manager.class);
    
    //Instancia
    private static MeLanbide33Manager instance = null;
    
    private MeLanbide33Manager()
    {
        
    }
    
    public static MeLanbide33Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide33Manager.class)
            {
                instance = new MeLanbide33Manager();
            }
        }
        return instance;
    }       
    
    public Long getTramiteActualExpediente(int codOrganizacion, int ejercicio, String numExp, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            return meLanbide33DAO.getTramiteActualExpediente(codOrganizacion, ejercicio, numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando tramite actual del expediente " + numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando tramite actual del expediente " + numExp, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Long obtenerCodigoInternoTramite(Integer codOrganizacion, String codTramite, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            return meLanbide33DAO.obtenerCodigoInternoTramite(codOrganizacion, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando codigo interno del tramite " + codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando codigo interno del tramite " + codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    public boolean tieneTramiteIniciado(int codOrganizacion, int ejercicio, String numExp, Long codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            return meLanbide33DAO.tieneTramiteIniciado(codOrganizacion, ejercicio, numExp, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD comprobando si tiene tramite iniciado "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD comprobando si tiene tramite iniciado "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    
    public boolean eliminarSubvencion(int codOrganizacion, int ejercicio, String numExp, Long codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            boolean resultEliminarSubv=meLanbide33DAO.eliminarValorSubvencion(codOrganizacion, ejercicio, numExp, codTramite, con);
            boolean resultEliminarValoresCalcuET=meLanbide33DAO.eliminarValoresCalculadosET(codOrganizacion, ejercicio, numExp, codTramite, con);
            // nuevos david 21/01/2016 - borra pagos, relacions tecnicos puestos y etiquetas de plantilla resolucion
            boolean resultadoBorrarPagos = meLanbide33DAO.eliminarValorSubvET_PagosS75(codOrganizacion, ejercicio, numExp, codTramite, con);
            boolean resultadoBorrarRelacionTecPues =meLanbide33DAO.eliminarValorSubvET_RelaTecPuestos(codOrganizacion, ejercicio, numExp, codTramite, con);
            boolean resultadoBorrarEtiquetas = meLanbide33DAO.eliminarValorSubvET_EtiquetasPlant(codOrganizacion, ejercicio, numExp, codTramite, con);
            //26/01/2016 DavidG NO eliminaba valores propuestos y concedidos de formacion
            boolean resultadoBorrarFormacon = meLanbide33DAO.eliminarValorSubvET_Conceptos(codOrganizacion, ejercicio, numExp, codTramite, con);
            return resultEliminarSubv && resultEliminarValoresCalcuET && resultadoBorrarPagos && resultadoBorrarRelacionTecPues && resultadoBorrarEtiquetas && resultadoBorrarFormacon;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD al eliminar valor de subvencion "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD al eliminar valor de subvencion "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int actualizarPagos(int codOrganizacion, int ejercicio, String numExp, Long codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int res=0;
        try
        {
            con = adaptador.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            //boolean resultEliminarSubv=meLanbide33DAO.eliminarValorSubvencion(codOrganizacion, ejercicio, numExp, codTramite, con);
            //boolean resultEliminarValoresCalcuET=meLanbide33DAO.eliminarValoresCalculadosET(codOrganizacion, ejercicio, numExp, codTramite, con);
            //return resultEliminarSubv && resultEliminarValoresCalcuET;
            String anyoResolucion = meLanbide33DAO.getAnyoResolucion(codOrganizacion, codTramite, numExp, con);
            if (!anyoResolucion.equals("")){
                Long primerAno = meLanbide33DAO.getPrimerAnoPago(codOrganizacion, "SEI", ejercicio, numExp, con);
                if(primerAno != null)
                {
                        int dif = Integer.parseInt(anyoResolucion) - primerAno.intValue(); 
                        if(dif > 0)
                        {
                                res = meLanbide33DAO.actualizarAnoPago(codOrganizacion, "SEI", ejercicio, numExp, (long)dif,con);
                                log.debug("@@@@@@@@@@ Años actualizados: "+res+" @@@@@@@@@@@@@@@");
                                
                        }
                }
            }
            return res;
           
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD al eliminar valor de subvencion "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD al eliminar valor de subvencion "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getDocumentoInteresado(String numExpediente, Connection con) {
        log.error("getDocumentoInteresado Manager - BEGIN()");
        String documento ="";
        try {
            documento = MeLanbide33DAO.getInstance().getDocumentoInteresado(numExpediente, con);
        } catch (Exception e) {
            log.error("Exception en manager al getDocumentoInteresado");
        }
        log.error("getDocumentoInteresado Manager - END()");
        return documento;
    }

    public int relacionarAutomDosConvocAnteriores(Integer codOrganizacion, String ejercicio, String documentoInteresado, String numExpediente, Connection con) throws Exception {
        log.error("relacionarAutomDosConvocAnteriores Manager - BEGIN()");
        int extpsRela =0;
        try {
            ArrayList<String> exptsDosConvAnte = MeLanbide33DAO.getInstance().getEptsDosConvoAnteriores(Integer.valueOf(ejercicio),documentoInteresado,numExpediente,con);
            if(exptsDosConvAnte!=null && exptsDosConvAnte.size()>0){
                for(String num_exp : exptsDosConvAnte){
                    extpsRela++;
                    String[] datosExp = num_exp.split("/");
                    String ejercicioExpteRelacion = datosExp[0];
                    MeLanbide33DAO.getInstance().guardarRelacionConvocatoria(codOrganizacion,ejercicio,numExpediente,Integer.valueOf(ejercicioExpteRelacion),num_exp,con);
                }
            }else{
                log.error("Control-relacionarAutomDosConvocAnteriores : No se han recuperado expedientes en las 2 ultimas convocatorias " + numExpediente + " Documento : " + documentoInteresado);
            }
        } catch (Exception e) {
            log.error("Exception en manager al relacionarAutomDosConvocAnteriores");
            throw e;
        }
        log.error("relacionarAutomDosConvocAnteriores Manager - END()");
        return extpsRela;
    }

    public boolean comprobarDatosConv_ConcepGuardadosManager(int codOrganizacion, String ejercicio, String codProcedimiento, Integer numeroExpInt, String numExpediente, Connection con) throws Exception {
        log.error("comprobarDatosConv_ConcepGuardadosManager  - BEGIN()");
        boolean resultado =false;
        try {
            resultado = MeLanbide33DAO.getInstance().comprobarDatosConv_ConcepGuardadosDAO(codOrganizacion, ejercicio, codProcedimiento, numeroExpInt, numExpediente, con);
            if(resultado){
                log.error("Control-comprobarDatosConv_ConcepGuardadosManager : Datos recuperados, permitimos avanzar. Expediente : " + numExpediente);
            }else{
                log.error("Control-comprobarDatosConv_ConcepGuardadosManager : No se han recuperado registros de Convocatorias,Puestos o Conceptos para el expedientes : " + numExpediente);
            }
        } catch (Exception e) {
            log.error("Exception en manager al comprobarDatosConv_ConcepGuardadosManager");
            throw e;
        }
        log.error("comprobarDatosConv_ConcepGuardadosManager  - Resultado : " + resultado);
        log.error("comprobarDatosConv_ConcepGuardadosManager  - END()");
        return resultado;
    }

    public boolean guardarTotalPago3Anualidad3PagoManager(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente,int codTramite,Connection con) throws Exception {
        log.error("guardarTotalPago3Anualidad3PagoManager Manager - BEGIN()");
        boolean result = true;
        try {
            result = MeLanbide33DAO.getInstance().guardarTotalPago3Anualidad3DAO(codOrganizacion, ejercicio, codProcedimiento, numExpediente, codTramite, con);
        } catch (Exception e) {
            log.error("Exception en manager al guardarTotalPago3Anualidad3PagoManager");
            throw e;
        }
        log.error("guardarTotalPago3Anualidad3PagoManager Manager - END()");
        return result;
    }

    public boolean validarPasoaET_SEI_Manager(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente, int codTramite, Connection con) throws Exception {
        log.error("validarPasoaET_SEI_Manager - BEGIN()");
        boolean result = true;
        try {
            result = MeLanbide33DAO.getInstance().validarPasoaET_SEI_DAO(codOrganizacion, ejercicio, codProcedimiento, numExpediente, codTramite, con);
        } catch (Exception e) {
            log.error("Exception en manager al validarPasoaET_SEI_Manager");
            throw e;
        }
        log.error("validarPasoaET_SEI_Manager - END()");
        return result;
    }
    public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            lista = meLanbide33DAO.getDatosMinimis(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide33Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide33.COD_DES_DTSV, ConstantesMeLanbide33.FICHERO_PROPIEDADES), adapt);
       
            for (FilaMinimisVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(cont.getEstado())) {
                        cont.setDesEstado(valordesp.getDes_nom());
                        break;
                    }
                }
            }         
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepci0n en la BBDD recuperando datos sobre las minimis ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las minimis ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
     public FilaMinimisVO getMinimisPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            return meLanbide33DAO.getMinimisPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarMinimis(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            return meLanbide33DAO.eliminarMinimis(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD al eliminar una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD al eliminar una minimis:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            insertOK = meLanbide33DAO.crearNuevoMinimis(nuevaMinimis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD creando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD creando una minimis : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            insertOK = meLanbide33DAO.modificarMinimis(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD actualizando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD actualizando una minimis : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }
     public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide33DAO meLanbide33DAO = MeLanbide33DAO.getInstance();
            return meLanbide33DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }    
}
