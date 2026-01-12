/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide57.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide57.dao.Melanbide57DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeGeneralVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeInternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeRGIVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.TramiteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide57Manager
{
    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide57Manager.class);
    
    //Instancia
    private static MeLanbide57Manager instance = null;
    
    private MeLanbide57Manager()
    {
        
    }
    
    public static MeLanbide57Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide57Manager.class)
            {
                instance = new MeLanbide57Manager();
            }
        }
        return instance;
    }
    
    public ArrayList<String> recogerExpedientesAFinalizar(AdaptadorSQLBD adapt) throws Exception
    {
        try
        {
            return Melanbide57DAO.getInstance().recogerExpedientesAFinalizar(adapt);
        }catch(Exception ex)
        {
            throw ex;
        }
    }
    
    public int actualizaTercero(String expediente, Connection con) throws Exception
    {
        try
        {
            return Melanbide57DAO.getInstance().actualizaTercero(expediente, con);
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }
    
    public int eliminaTercero(String expediente, Connection con) throws Exception
    {
        try
        {
            return Melanbide57DAO.getInstance().eliminaTercero(expediente, con);
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }
    
    public int insertaTercero(String expediente, int codOrg, String ano, Connection con) throws Exception
    {
        try
        {
            return Melanbide57DAO.getInstance().insertaTercero(expediente, codOrg, ano, con);
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    
    public List<TramiteVO> getListaTramitesAreas(int codOrganizacion, int ocurrenciaTramite, String expediente, Connection con) throws Exception
    {
        return Melanbide57DAO.getInstance().getListaTramitesAreas(codOrganizacion, ocurrenciaTramite, expediente, con);
    }
    
    public List<InformeGeneralVO> getListaDatosInformeGeneralDesplegable(String codigoCampo, int codOrganizacion/*, int ocurrenciaTramite*/, String strFechaDesde, String strFechaHasta, String codigoTramite, Connection con, boolean bloqueRespuesta) throws Exception
    {
        return Melanbide57DAO.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampo, codOrganizacion, strFechaDesde, strFechaHasta, codigoTramite, con, bloqueRespuesta);
    }
    
    public List<InformeInternoVO> getListaDatosInformeInternoDerivadas(int codOrganizacion, int ocurrenciaTramite, String strFechaDesde, String strFechaHasta, String codigoTramiteRevision, Connection con) throws Exception
    {
        return Melanbide57DAO.getInstance().getListaDatosInformeInternoDerivadas(codOrganizacion, ocurrenciaTramite, strFechaDesde, strFechaHasta, codigoTramiteRevision, con);
    }
    
    public List<InformeInternoVO> getListaDatosInformeInternoResueltas(int codOrganizacion, int ocurrenciaTramite, String strFechaDesde, String strFechaHasta, String codigoTramiteRevision, String codigoDesplegableRespuesta1, String codigoDesplegableRespuesta2, String codigoDesplegableRespuesta3, String codigoDesplegableRespuesta4, Connection con) throws Exception
    {
        return Melanbide57DAO.getInstance().getListaDatosInformeInternoResueltas(codOrganizacion, ocurrenciaTramite, strFechaDesde, strFechaHasta, codigoTramiteRevision, codigoDesplegableRespuesta1, codigoDesplegableRespuesta2, codigoDesplegableRespuesta3, codigoDesplegableRespuesta4, con);
    }
    
    public List<InformeRGIVO> getListaDatosInformeRGI(int codOrganizacion, String strFechaDesde, String strFechaHasta, int idUsuario, Connection con) throws Exception
    {
        return Melanbide57DAO.getInstance().getListaDatosInformeRGI(codOrganizacion, strFechaDesde, strFechaHasta, idUsuario, con);
    }
    
}
