/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide38.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide38.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide38.util.ConstantesMeLanbide38;
import es.altia.flexia.integracion.moduloexterno.melanbide38.util.MeLanbide38MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.ContratoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.FilaContratoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.Tercero;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide38DAO 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide38DAO.class);
    
    //Instancia
    private static MeLanbide38DAO instance = null;
    
    private MeLanbide38DAO()
    {
        
    }
    
    public static MeLanbide38DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide38DAO.class)
            {
                instance = new MeLanbide38DAO();
            }
        }
        return instance;
    }      
    
    public List<FilaContratoVO> getContratosExpediente(String numExp, Connection con) throws Exception
    {
        List<FilaContratoVO> retList = new ArrayList<FilaContratoVO>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            FilaContratoVO fila = null;
            
            String query = "select SOXEX_NCON, SOXEX_DOC, SOXEX_NOM, SOXEX_AP1, SOXEX_AP2 from " + ConfigurationParameter.getParameter(ConstantesMeLanbide38.TABLA_CNEE_SOXEX, ConstantesMeLanbide38.FICHERO_PROPIEDADES)
                    + " where SOXEX_NUM like '" + numExp +"' order by SOXEX_NCON ASC";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                fila = (FilaContratoVO)MeLanbide38MappingUtils.getInstance().map(rs, FilaContratoVO.class);
                if(fila != null)
                {
                    retList.add(fila);
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando contratos para el expediente "+numExp, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return retList;
    }
    
    public List<Tercero> busquedaTerceros(Tercero ter, Connection con) throws Exception
    {
        if(log.isDebugEnabled())
            log.debug("BUSQUEDA TERCEROS");
        Statement st = null;
        ResultSet rs = null;
        List<Tercero> retList = new ArrayList<Tercero>();
        try
        {
            String sql = "select TER_COD, TER_DOC, TER_NOM, TER_AP1, TER_AP2 from " + ConfigurationParameter.getParameter(ConstantesMeLanbide38.TABLA_TERCEROS, ConstantesMeLanbide38.FICHERO_PROPIEDADES)
                    +" where 1=1";
            if(ter != null)
            {
                if(ter.getDni() != null && !ter.getDni().equalsIgnoreCase(""))
                {
                    sql += " and upper(TER_DOC) like '%"+ter.getDni().toUpperCase()+"%'";
                }
                if(ter.getNombre() != null && !ter.getNombre().equalsIgnoreCase(""))
                {
                    sql += " and upper(TER_NOM) like '%"+ter.getNombre().toUpperCase()+"%'";
                }
                if(ter.getApellido1() != null && !ter.getApellido1().equalsIgnoreCase(""))
                {
                    sql += " and upper(TER_AP1) like '%"+ter.getApellido1().toUpperCase()+"%'";
                }
                if(ter.getApellido2() != null && !ter.getApellido2().equalsIgnoreCase(""))
                {
                    sql += " and upper(TER_AP2) like '%"+ter.getApellido2().toUpperCase()+"%'";
                }
            }
            sql +=" order by TER_NOM ASC, TER_AP1 ASC, TER_AP2 ASC, TER_DOC ASC";
            if(log.isDebugEnabled())
                log.debug("SQL BUSQUEDA TERCEROS = "+sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            Tercero t = null;
            while(rs.next())
            {
                t = (Tercero)MeLanbide38MappingUtils.getInstance().map(rs, Tercero.class);
                retList.add(t);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        if(log.isDebugEnabled())
            log.debug("FIN BUSQUEDA TERCEROS: "+retList.size());
        return retList;
    }
    
    public int getNuevoNumContrato(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String query = "select max(SOXEX_NCON) + 1 as num "
                     + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide38.TABLA_CNEE_SOXEX, ConstantesMeLanbide38.FICHERO_PROPIEDADES)
                     + " where SOXEX_NUM = '"+numExpediente+"'";
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                return rs.getInt("NUM");
            else
                return -1;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error generando el nuevo número de contrato", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
    }
    
    public boolean crearContrato(ContratoVO contrato, Connection con) throws BDException, Exception
    {
        Statement st = null;
        int rows = 0;
        try
        {
            String query = "";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            
            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide38.TABLA_CNEE_SOXEX, ConstantesMeLanbide38.FICHERO_PROPIEDADES)
                    +"(SOXEX_MUN, SOXEX_EJE, SOXEX_PRO, SOXEX_NUM, SOXEX_NCON, SOXEX_DOC, SOXEX_TER_COD, SOXEX_NOM, SOXEX_AP1, SOXEX_AP2, SOXEX_FNA, SOXEX_SEX, SOXEX_MIN, SOXEX_INM, SOXEX_PLD, SOXEX_RML, SOXEX_OTR, SOXEX_COL, SOXEX_MDE, SOXEX_NES, SOXEX_TCO, SOXEX_TIC, SOXEX_FAC, SOXEX_TJO, SOXEX_PJT, SOXEX_FFC, SOXEX_DCO, SOXEX_CNOE, SOXEX_SAL, SOXEX_CSS, SOXEX_IMP, SOXEX_IMR, SOXEX_IRE, SOXEX_CPRO)"
                    +" values("
                    +contrato.getMun()+", "
                    +(contrato.getEje() != null ? contrato.getEje().toString() : "null")+", "
                    +(contrato.getPro() != null && !contrato.getPro().equals("") ? "'"+contrato.getPro().toUpperCase()+"'" : "null")+", "
                    +(contrato.getNum() != null && !contrato.getNum().equals("") ? "'"+contrato.getNum().toUpperCase()+"'" : "null")+", "
                    +(contrato.getnCon() != null ? contrato.getnCon().toString() : "null")+", "
                    +(contrato.getDoc() != null && !contrato.getDoc().equals("") ? "'"+contrato.getDoc().toUpperCase()+"'" : "null")+", "
                    +(contrato.getTerCod() != null ? contrato.getTerCod().toString() : "null")+", "
                    +(contrato.getNom() != null && !contrato.getNom().equals("") ? "'"+contrato.getNom().toUpperCase()+"'" : "null")+", "
                    +(contrato.getAp1() != null && !contrato.getAp1().equals("") ? "'"+contrato.getAp1().toUpperCase()+"'" : "null")+", "
                    +(contrato.getAp2() != null && !contrato.getAp2().equals("") ? "'"+contrato.getAp2().toUpperCase()+"'" : "null")+", "
                    +(contrato.getFna() != null ? " TO_DATE('"+format.format(contrato.getFna())+"', 'dd/mm/yyyy')" : " null")+", "
                    +(contrato.getSex() != null ? contrato.getSex().toString() : "null")+", "
                    +(contrato.getMin() != null && !contrato.getMin().equals("") ? "'"+(contrato.getMin().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                    +(contrato.getInm() != null && !contrato.getInm().equals("") ? "'"+(contrato.getInm().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                    +(contrato.getPld() != null && !contrato.getPld().equals("") ? "'"+(contrato.getPld().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                    +(contrato.getRml() != null && !contrato.getRml().equals("") ? "'"+(contrato.getRml().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                    +(contrato.getOtr() != null && !contrato.getOtr().equals("") ? "'"+(contrato.getOtr().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                    +(contrato.getCol() != null ? contrato.getCol().toString() : "null")+", "
                    +(contrato.getMde() != null ? contrato.getMde().toString() : "null")+", "
                    +(contrato.getNes() != null ? contrato.getNes().toString() : "null")+", "
                    +(contrato.getTco() != null ? contrato.getTco().toString() : "null")+", "
                    +(contrato.getTic() != null ? contrato.getTic().toString() : "null")+", "
                    +(contrato.getFac() != null ? " TO_DATE('"+format.format(contrato.getFac())+"', 'dd/mm/yyyy')" : " null")+", "
                    +(contrato.getTjo() != null && !contrato.getTjo().equals("") ? "'"+contrato.getTjo().toUpperCase()+"'" : "null")+", "
                    +(contrato.getPjt() != null ? contrato.getPjt().toPlainString() : "null")+", "
                    +"null, "
                    +(contrato.getDco() != null ? contrato.getDco().toString() : "null")+", "
                    +(contrato.getCnoe() != null && !contrato.getCnoe().equals("") ? "'"+contrato.getCnoe().toUpperCase()+"'" : "null")+", "
                    +(contrato.getSal() != null ? contrato.getSal().toPlainString() : "null")+", "
                    +(contrato.getCss() != null ? contrato.getCss().toPlainString() : "null")+", "
                    +(contrato.getImp() != null ? contrato.getImp().toPlainString() : "null")+", "
                    +(contrato.getImr() != null ? contrato.getImr().toPlainString() : "null")+", "
                    +(contrato.getIre() != null ? contrato.getIre().toPlainString() : "null")+", "
                    +(contrato.getcPro() != null ? contrato.getcPro().toString() : "null")
                    +")";
            if(log.isDebugEnabled()) 
                log.debug("query = "+query);
            st = con.createStatement();
            rows = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
        return rows > 0;
    }
    
    public ContratoVO getContratoExpedientePorNumContrato(String numExpediente, Integer numContrato, Connection con) throws BDException, Exception
    {
        ContratoVO contrato = null;
        Statement st = null;
        if(numContrato != null)
        {
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide38.TABLA_CNEE_SOXEX, ConstantesMeLanbide38.FICHERO_PROPIEDADES)
                               +" where SOXEX_NUM = '"+numExpediente+"' and SOXEX_NCON = "+numContrato.toString();
                st = con.createStatement();
                ResultSet rs = st.executeQuery(query);
                if(rs.next())
                {
                    contrato = (ContratoVO)MeLanbide38MappingUtils.getInstance().map(rs, ContratoVO.class);
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando el contrato numero "+numContrato+" para el expediente "+numExpediente, ex);
                throw new Exception(ex);
            }
            finally
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
        }
        return contrato;
    }
    
    public boolean modificarContrato(ContratoVO contrato, Connection con) throws BDException, Exception
    {
        Statement st = null;
        int rows = 0;
        if(contrato != null && contrato.getnCon() != null)
        {
            try
            {
                String query = "";
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide38.TABLA_CNEE_SOXEX, ConstantesMeLanbide38.FICHERO_PROPIEDADES)
                        +" set"
                        + " SOXEX_DOC = "+(contrato.getDoc() != null && !contrato.getDoc().equals("") ? "'"+contrato.getDoc().toUpperCase()+"'" : "null")+", "
                        + " SOXEX_NOM = "+(contrato.getNom() != null && !contrato.getNom().equals("") ? "'"+contrato.getNom().toUpperCase()+"'" : "null")+", "
                        + " SOXEX_AP1 = "+(contrato.getAp1() != null && !contrato.getAp1().equals("") ? "'"+contrato.getAp1().toUpperCase()+"'" : "null")+", "
                        + " SOXEX_AP2 = "+(contrato.getAp2() != null && !contrato.getAp2().equals("") ? "'"+contrato.getAp2().toUpperCase()+"'" : "null")+", "
                        + " SOXEX_FNA = "+(contrato.getFna() != null ? " TO_DATE('"+format.format(contrato.getFna())+"', 'dd/mm/yyyy')" : " null")+", "
                        + " SOXEX_SEX = "+(contrato.getSex() != null ? contrato.getSex().toString() : "null")+", "
                        + " SOXEX_MIN = "+(contrato.getMin() != null && !contrato.getMin().equals("") ? "'"+(contrato.getMin().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                        + " SOXEX_INM = "+(contrato.getInm() != null && !contrato.getInm().equals("") ? "'"+(contrato.getInm().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                        + " SOXEX_PLD = "+(contrato.getPld() != null && !contrato.getPld().equals("") ? "'"+(contrato.getPld().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                        + " SOXEX_RML = "+(contrato.getRml() != null && !contrato.getRml().equals("") ? "'"+(contrato.getRml().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                        + " SOXEX_OTR = "+(contrato.getOtr() != null && !contrato.getOtr().equals("") ? "'"+(contrato.getOtr().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "1" : "0")+"'" : "null")+", "
                        + " SOXEX_COL = "+(contrato.getCol() != null ? contrato.getCol().toString() : "null")+", "
                        + " SOXEX_MDE = "+(contrato.getMde() != null ? contrato.getMde().toString() : "null")+", "
                        + " SOXEX_NES = "+(contrato.getNes() != null ? contrato.getNes().toString() : "null")+", "
                        + " SOXEX_TCO = "+(contrato.getTco() != null ? contrato.getTco().toString() : "null")+", "
                        + " SOXEX_TIC = "+(contrato.getTic() != null ? contrato.getTic().toString() : "null")+", "
                        + " SOXEX_FAC = "+(contrato.getFac() != null ? " TO_DATE('"+format.format(contrato.getFac())+"', 'dd/mm/yyyy')" : " null")+", "
                        + " SOXEX_TJO = "+(contrato.getTjo() != null && !contrato.getTjo().equals("") ? "'"+contrato.getTjo().toUpperCase()+"'" : "null")+", "
                        + " SOXEX_PJT = "+(contrato.getPjt() != null ? contrato.getPjt().toPlainString() : "null")+", "
                        + " SOXEX_FFC = "+"null, "
                        + " SOXEX_DCO = "+(contrato.getDco() != null ? contrato.getDco().toString() : "null")+", "
                        + " SOXEX_CNOE = "+(contrato.getCnoe() != null && !contrato.getCnoe().equals("") ? "'"+contrato.getCnoe().toUpperCase()+"'" : "null")+", "
                        + " SOXEX_SAL = "+(contrato.getSal() != null ? contrato.getSal().toPlainString() : "null")+", "
                        + " SOXEX_CSS = "+(contrato.getCss() != null ? contrato.getCss().toPlainString() : "null")+", "
                        + " SOXEX_IMP = "+(contrato.getImp() != null ? contrato.getImp().toPlainString() : "null")+", "
                        + " SOXEX_IMR = "+(contrato.getImr() != null ? contrato.getImr().toPlainString() : "null")+", "
                        + " SOXEX_IRE = "+(contrato.getIre() != null ? contrato.getIre().toPlainString() : "null")+", "
                        + " SOXEX_CPRO = "+(contrato.getcPro() != null ? contrato.getcPro().toString() : "null")
                        + " where SOXEX_NCON = "+contrato.getnCon().toString()
                        + " and SOXEX_NUM = '"+contrato.getNum()+"'";
                if(log.isDebugEnabled()) 
                    log.debug("query = "+query);
                st = con.createStatement();
                rows = st.executeUpdate(query);
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando las áreas", ex);
                throw new Exception(ex);
            }
            finally
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
        }
        return rows > 0;
    }
    
    public boolean eliminarContrato(String numExp, Integer numContrato, Connection con) throws Exception
    {
        String sql = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide38.TABLA_CNEE_SOXEX, ConstantesMeLanbide38.FICHERO_PROPIEDADES)
                +" where SOXEX_NUM = '"+numExp+"' and SOXEX_NCON = "+numContrato;
        if(log.isDebugEnabled())
            log.debug("SQL ELIMINAR CONTRATO = "+sql);
        int rows = 0;
        Statement st = null;
        try
        {
            st = con.createStatement();
            rows = st.executeUpdate(sql);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al eliminar", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
        return rows > 0;
    }
}
