/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide29.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide29.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide29.util.ConstantesMeLanbide29;
import es.altia.flexia.integracion.moduloexterno.melanbide29.util.MeLanbide29MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.ContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.FilaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.FilaDocumentoContableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.PersonaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.Tercero;
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
public class MeLanbide29DAO 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide29DAO.class);
    
    //Instancia
    private static MeLanbide29DAO instance = null;
    
    private MeLanbide29DAO()
    {
        
    }
    
    public static MeLanbide29DAO getInstance()
    {
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null)
        {
            synchronized(MeLanbide29DAO.class)
            {
                instance = new MeLanbide29DAO();
            }
        }
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }
    
    public List<FilaContratoRenovacionPlantillaVO> getContratosExpediente(String numExp, Connection con) throws Exception
    {
        List<FilaContratoRenovacionPlantillaVO> retList = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try
        {
            String sql = "select distinct (PEXCO_NCON) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES) + " where PEXCO_NUM like '"+numExp+"' order by PEXCO_NCON ASC";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            FilaContratoRenovacionPlantillaVO fila = null;
            String num = null;
            String query = null;
            boolean nuevoContrato = true;
            String nom = "";
            String ape1 = "";
            String ape2 = "";
            String dni = "";
            String nomApe = "";
            int tipoPers = -1;
            while(rs.next())
            {
                num = rs.getString("PEXCO_NCON");
                query = "select PEXCO_NCON, PEXCO_NUM, PEXCO_CTPE, PEXCO_AP1, PEXCO_AP2, PEXCO_NOM, PEXCO_DOC from " + ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)
                        + " where PEXCO_NCON like '" + num + "' and PEXCO_NUM like '" + numExp +"'";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st2 = con.createStatement();
                rs2 = st2.executeQuery(query);
                while(rs2.next())
                {
                    if(nuevoContrato)
                    {
                        fila = new FilaContratoRenovacionPlantillaVO();
                        fila.setNumContrato(rs2.getInt("PEXCO_NCON"));
                        if(rs2.wasNull())
                            fila.setNumContrato(null);
                        fila.setNumExpediente(rs2.getString("PEXCO_NUM"));
                        retList.add(fila);
                        nuevoContrato = false;
                    }
                    
                    ape1 = rs2.getString("PEXCO_AP1");
                    ape2 = rs2.getString("PEXCO_AP2");
                    nom = rs2.getString("PEXCO_NOM");
                    dni = rs2.getString("PEXCO_DOC");
                    String str = "";
                    nomApe = nom != null ? nom : "";
                    nomApe += ape1 != null && !ape1.equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                    nomApe += ape1 != null ? ape1 : "";
                    nomApe += ape2 != null && !ape2.equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                    nomApe += ape2 != null ? ape2 : "";
                    try
                    {
                        tipoPers = rs2.getInt("PEXCO_CTPE");
                        if(!rs2.wasNull())
                        {
                            switch(tipoPers)
                            {
                                case 1:
                                    fila.setDni1(dni);
                                    fila.setNomApe1(nomApe);
                                    break;
                                case 2:
                                    fila.setDni2(dni);
                                    fila.setNomApe2(nomApe);
                                    break;
                                case 3:
                                    fila.setDni3(dni);
                                    fila.setNomApe3(nomApe);
                                    break;
                            }
                        }
                    }
                    catch(Exception ex)
                    {
                        log.error("Se ha producido un error al procesar la persona del contrato con dni: "+dni, ex);
                    }
                }
                nuevoContrato = true;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
                if(st2!=null) 
                    st2.close();
                if(rs2!=null) 
                    rs2.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public ContratoRenovacionPlantillaVO getContratoExpedientePorNumContrato(String numExp, Integer numContrato,  Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        ContratoRenovacionPlantillaVO contrato = null;
        try
        {
            PersonaContratoRenovacionPlantillaVO persona = null;
            String num = null;
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)
                    + " where PEXCO_NUM like '" + numExp + "' and PEXCO_NCON = '" + numContrato + "'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            boolean nuevoContrato = true;
            while(rs.next())
            {
                if(nuevoContrato)
                {
                    contrato = new ContratoRenovacionPlantillaVO();
                    contrato.setEjercicio(rs.getInt("PEXCO_EJE"));
                    if(rs.wasNull())
                        contrato.setEjercicio(null);
                    contrato.setEntorno(rs.getInt("PEXCO_MUN"));
                    if(rs.wasNull())
                        contrato.setEntorno(null);
                    contrato.setNumContrato(rs.getInt("PEXCO_NCON"));
                    if(rs.wasNull())
                        contrato.setNumContrato(null);
                    contrato.setNumExpediente(rs.getString("PEXCO_NUM"));
                    contrato.setProcedimiento(rs.getString("PEXCO_PRO"));
                    nuevoContrato = false;
                }

                persona = new PersonaContratoRenovacionPlantillaVO();
                persona.setApellido1(rs.getString("PEXCO_AP1"));
                persona.setApellido2(rs.getString("PEXCO_AP2"));
                persona.setCnoe(rs.getString("PEXCO_CNOE"));
                persona.setCodTercero(rs.getLong("PEXCO_TER_COD"));
                persona.setCodTipoPersona(rs.getInt("PEXCO_CTPE"));
                if(rs.wasNull())
                    persona.setCodTipoPersona(null);    
                persona.setColectivo(rs.getInt("PEXCO_COL"));
                if(rs.wasNull())
                    persona.setColectivo(null);
                persona.setDuracionContrato(rs.getInt("PEXCO_DCO"));
                if(rs.wasNull())
                    persona.setDuracionContrato(null);
                persona.setFeAlta(rs.getDate("PEXCO_FAC"));
                persona.setFeBaja(rs.getDate("PEXCO_FBE"));
                persona.setFeFinContrato(rs.getDate("PEXCO_FFC"));
                persona.setFeNac(rs.getDate("PEXCO_FNA"));
                persona.setFlInmigrante(rs.getString("PEXCO_INM"));
                persona.setFlMinusvalido(rs.getString("PEXCO_MIN"));
                persona.setFlOtr(rs.getString("PEXCO_OTR"));
                persona.setFlPld(rs.getString("PEXCO_PLD"));
                persona.setFlRml(rs.getString("PEXCO_RML"));
                persona.setImpSubvencion(rs.getBigDecimal("PEXCO_IMP"));
                persona.setMesesDesempleo(rs.getInt("PEXCO_MDE"));
                if(rs.wasNull())
                    persona.setMesesDesempleo(null);
                persona.setNivelEstudios(rs.getInt("PEXCO_NES"));
                if(rs.wasNull())
                    persona.setNivelEstudios(null);
                persona.setNombre(rs.getString("PEXCO_NOM"));
                persona.setNumDoc(rs.getString("PEXCO_DOC"));
                persona.setPorReduJor(rs.getBigDecimal("PEXCO_PRJ"));
                persona.setRetSalarial(rs.getBigDecimal("PEXCO_SAL"));
                persona.setSexo(rs.getInt("PEXCO_SEX"));
                if(rs.wasNull())
                    persona.setSexo(null);    
                persona.setTipoContrato(rs.getInt("PEXCO_TCO"));
                if(rs.wasNull())
                    persona.setTipoContrato(null);
                persona.setTipoJornada(rs.getString("PEXCO_TJO"));
                contrato.addPersona(persona);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return contrato;
    }
    
    public boolean guardarContrato(ContratoRenovacionPlantillaVO contrato, Connection con) throws Exception
    {
        Statement st = null;
        int rows = 0;
        try
        {
            con.setAutoCommit(false);
            List<PersonaContratoRenovacionPlantillaVO> personas = contrato.getPersonas();
            PersonaContratoRenovacionPlantillaVO p = null;
            String query = "";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 0; i < personas.size(); i++)
            {
                p = personas.get(i);
                
                if(p.getCodTipoPersona() != null)
                {
                    switch(p.getCodTipoPersona())
                    {
                        case 1:
                            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)+" set PEXCO_NOM = '"+p.getNombre()+"', PEXCO_AP1 = '"+p.getApellido1()+"', PEXCO_AP2 = '"+p.getApellido2()+"',"
                                    + (p.getFeNac() != null ? " PEXCO_FNA = TO_DATE('"+format.format(p.getFeNac())+"', 'dd/mm/yyyy')," : " PEXCO_FNA = null,")
                                    + " PEXCO_SEX = "+p.getSexo()+","
                                    + (p.getFeBaja() != null ? " PEXCO_FBE = TO_DATE('"+format.format(p.getFeBaja())+"', 'dd/mm/yyyy')," : "PEXCO_FBE = null,")
                                    + " PEXCO_PRJ = "+(p.getPorReduJor() != null ? p.getPorReduJor().intValue() : "null")+","
                                    + " PEXCO_CNOE = '"+p.getCnoe()+"'"
                                    + " where PEXCO_NUM = '"+contrato.getNumExpediente()+"' and PEXCO_NCON = '"+contrato.getNumContrato()+"' and PEXCO_DOC = '"+p.getNumDoc()+"'";
                            break;
                        case 2:
                            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)+" set PEXCO_NOM = '"+p.getNombre()+"', PEXCO_AP1 = '"+p.getApellido1()+"', PEXCO_AP2 = '"+p.getApellido2()+"',"
                                    + (p.getFeNac() != null ? " PEXCO_FNA = TO_DATE('"+format.format(p.getFeNac())+"', 'dd/mm/yyyy')," : " PEXCO_FNA = null,")
                                    + " PEXCO_MIN = '"+p.getFlMinusvalido()+"', PEXCO_INM = '"+p.getFlInmigrante()+"', PEXCO_NES = "+p.getNivelEstudios()+","
                                    + " PEXCO_COL = "+p.getColectivo()+", PEXCO_MDE = "+p.getMesesDesempleo()+", PEXCO_PLD = '"+p.getFlPld()+"', PEXCO_RML = '"+p.getFlRml()+"', PEXCO_OTR = '"+p.getFlOtr()+"', PEXCO_TCO = "+p.getTipoContrato()+","
                                    + (p.getFeAlta() != null ? " PEXCO_FAC = TO_DATE('"+format.format(p.getFeAlta())+"', 'dd/mm/yyyy')," : " PEXCO_FAC = null,")
                                    + " PEXCO_TJO = '"+p.getTipoJornada()+"', PEXCO_CNOE = '"+p.getCnoe()+"', PEXCO_DCO = "+p.getDuracionContrato()
                                    + " where PEXCO_NUM = '"+contrato.getNumExpediente()+"' and PEXCO_NCON = '"+contrato.getNumContrato()+"' and PEXCO_DOC = '"+p.getNumDoc()+"'";
                            break;
                        case 3:
                            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)+" set PEXCO_NOM = '"+p.getNombre()+"', PEXCO_AP1 = '"+p.getApellido1()+"', PEXCO_AP2 = '"+p.getApellido2()+"',"
                                    + (p.getFeNac() != null ? " PEXCO_FNA = TO_DATE('"+format.format(p.getFeNac())+"', 'dd/mm/yyyy')," : " PEXCO_FNA = null,")
                                    + " PEXCO_MIN = '"+p.getFlMinusvalido()+"', PEXCO_INM = '"+p.getFlInmigrante()+"', PEXCO_NES = "+p.getNivelEstudios()+","
                                    + " PEXCO_COL = "+p.getColectivo()+", PEXCO_MDE = "+p.getMesesDesempleo()+", PEXCO_TCO = "+p.getTipoContrato()+","
                                    + (p.getFeAlta() != null ? " PEXCO_FAC = TO_DATE('"+format.format(p.getFeAlta())+"', 'dd/mm/yyyy')," : " PEXCO_FAC = null,")
                                    + " PEXCO_TJO = '"+p.getTipoJornada()+"', PEXCO_PLD = '"+p.getFlPld()+"', PEXCO_RML = '"+p.getFlRml()+"', PEXCO_OTR = '"+p.getFlOtr()+"',"
                                    + " PEXCO_CNOE = '"+p.getCnoe()+"'"
                                    + " where PEXCO_NUM = '"+contrato.getNumExpediente()+"' and PEXCO_NCON = '"+contrato.getNumContrato()+"' and PEXCO_DOC = '"+p.getNumDoc()+"'"
                                    + "";
                            break;
                            
                    }
                    if(log.isDebugEnabled()) 
                        log.debug("query = "+query);
                    st = con.createStatement();
                    rows = st.executeUpdate(query);
                }
            }
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return rows > 0;
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
            String sql = "select TER_COD, TER_DOC, TER_NOM, TER_AP1, TER_AP2 from T_TER where 1=1";
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
                t = new Tercero();
                t.setApellido1(rs.getString("TER_AP1"));
                t.setApellido2(rs.getString("TER_AP2"));
                t.setDni(rs.getString("TER_DOC"));
                t.setId(rs.getLong("TER_COD"));
                t.setNombre(rs.getString("TER_NOM"));
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
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        if(log.isDebugEnabled())
            log.debug("FIN BUSQUEDA TERCEROS: "+retList.size());
        return retList;
    }
    
    public int getNuevoNumContrato(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String query = "select count(distinct(pexco_ncon)) + 1 as num from "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES);
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
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean crearContrato(ContratoRenovacionPlantillaVO contrato, Connection con) throws Exception
    {
        Statement st = null;
        int rows = 0;
        try
        {
            con.setAutoCommit(false);
            List<PersonaContratoRenovacionPlantillaVO> personas = contrato.getPersonas();
            PersonaContratoRenovacionPlantillaVO p = null;
            String query = "";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 0; i < personas.size(); i++)
            {
                p = personas.get(i);
                
                if(p.getCodTipoPersona() != null)
                {
                    switch(p.getCodTipoPersona())
                    {
                        case 1:
                            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)+"(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, PEXCO_FNA, PEXCO_SEX, PEXCO_FBE, PEXCO_PRJ, PEXCO_CNOE)"
                                    +" values("
                                    +contrato.getEntorno()+", "+contrato.getEjercicio()+", '"+contrato.getProcedimiento()+"', '"+contrato.getNumExpediente()+"', "+contrato.getNumContrato()+","
                                    +" "+p.getCodTipoPersona()+", '"+p.getNumDoc()+"', "+p.getCodTercero()+", '"+p.getNombre()+"', '"+p.getApellido1()+"', '"+p.getApellido2()+"',"
                                    +(p.getFeNac() != null ? " TO_DATE('"+format.format(p.getFeNac())+"', 'dd/mm/yyyy')," : " null,")
                                    +" "+p.getSexo()+","
                                    +(p.getFeBaja() != null ? " TO_DATE('"+format.format(p.getFeBaja())+"', 'dd/mm/yyyy')," : "null,")
                                    +" "+(p.getPorReduJor() != null ? p.getPorReduJor().toString() : "null")+", '"+p.getCnoe()+"'"
                                    +")";
                            break;
                        case 2:
                            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)+"(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, PEXCO_FNA, PEXCO_SEX, PEXCO_MIN, PEXCO_INM, PEXCO_PLD, PEXCO_RML, PEXCO_OTR, PEXCO_COL, PEXCO_MDE, PEXCO_NES, PEXCO_TCO, PEXCO_FAC, PEXCO_TJO, PEXCO_FFC, PEXCO_DCO, PEXCO_CNOE, PEXCO_SAL, PEXCO_IMP)"
                                    +" values("
                                    +contrato.getEntorno()+", "+contrato.getEjercicio()+", '"+contrato.getProcedimiento()+"', '"+contrato.getNumExpediente()+"', "+contrato.getNumContrato()+","
                                    +" "+p.getCodTipoPersona()+", '"+p.getNumDoc()+"', "+p.getCodTercero()+", '"+p.getNombre()+"', '"+p.getApellido1()+"', '"+p.getApellido2()+"',"
                                    +(p.getFeNac() != null ? " TO_DATE('"+format.format(p.getFeNac())+"', 'dd/mm/yyyy')," : "null,")
                                    +" "+p.getSexo()+", '"+p.getFlMinusvalido()+"', '"+p.getFlInmigrante()+"', '"+p.getFlPld()+"', '"+p.getFlRml()+"', '"+p.getFlOtr()+"',"
                                    +" "+p.getColectivo()+", "+p.getMesesDesempleo()+", "+p.getNivelEstudios()+", "+p.getTipoContrato()+","
                                    +(p.getFeAlta() != null ? " TO_DATE('"+format.format(p.getFeAlta())+"', 'dd/mm/yyyy')," : "null,")
                                    +" '"+p.getTipoJornada()+"',"
                                    +(p.getFeFinContrato() != null ? " TO_DATE('"+format.format(p.getFeFinContrato())+"', 'dd/mm/yyyy')," : "null,")
                                    +" "+p.getDuracionContrato()+", '"+p.getCnoe()+"', "+(p.getRetSalarial() != null ? p.getRetSalarial().toString() : "null")+", "+(p.getImpSubvencion() != null ? p.getImpSubvencion().toString() : "null")
                                    +")";
                            break;
                        case 3:
                            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)+"(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, PEXCO_FNA, PEXCO_SEX, PEXCO_MIN, PEXCO_INM, PEXCO_PLD, PEXCO_RML, PEXCO_OTR, PEXCO_COL, PEXCO_MDE, PEXCO_NES, PEXCO_TCO, PEXCO_FAC, PEXCO_TJO, PEXCO_CNOE, PEXCO_SAL)"
                                    +" values("
                                    +contrato.getEntorno()+", "+contrato.getEjercicio()+", '"+contrato.getProcedimiento()+"', '"+contrato.getNumExpediente()+"', "+contrato.getNumContrato()+","
                                    +" "+p.getCodTipoPersona()+", '"+p.getNumDoc()+"', "+p.getCodTercero()+", '"+p.getNombre()+"', '"+p.getApellido1()+"', '"+p.getApellido2()+"',"
                                    +(p.getFeNac() != null ? " TO_DATE('"+format.format(p.getFeNac())+"', 'dd/mm/yyyy')," : "null,")
                                    +" "+p.getSexo()+", '"+p.getFlMinusvalido()+"', '"+p.getFlInmigrante()+"', '"+p.getFlPld()+"', '"+p.getFlRml()+"', '"+p.getFlOtr()+"',"
                                    +" "+p.getColectivo()+", "+p.getMesesDesempleo()+", "+p.getNivelEstudios()+", "+p.getTipoContrato()+","
                                    +(p.getFeAlta() != null ? " TO_DATE('"+format.format(p.getFeAlta())+"', 'dd/mm/yyyy')," : "null,")
                                    +" '"+p.getTipoJornada()+"', '"+p.getCnoe()+"', "+(p.getRetSalarial() != null ? p.getRetSalarial().toString() : "null")
                                    +")";
                            break;
                    }
                    if(log.isDebugEnabled()) 
                        log.debug("query = "+query);
                    st = con.createStatement();
                    rows = st.executeUpdate(query);
                }
            }
            con.commit();
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            con.rollback();
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return rows > 0;
    }
    
    public boolean eliminarContrato(String numExp, Integer numContrato, Connection con) throws Exception
    {
        String sql = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.RPLEM_PEXCO, ConstantesMeLanbide29.FICHERO_PROPIEDADES)
                +" where PEXCO_NUM = '"+numExp+"' and PEXCO_NCON = "+numContrato;
        if(log.isDebugEnabled())
            log.debug("SQL ELIMINAR CONTRATO = "+sql);
        int rows = 0;
        Statement st = null;
        try
        {
            con.setAutoCommit(false);
            st = con.createStatement();
            rows = st.executeUpdate(sql);
            con.commit();
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al eliminar", ex);
            con.rollback();
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return rows > 0;
    }
    
    public List<FilaDocumentoContableVO> getListaDocumentosContablesExpediente(String numExp, Connection con) throws Exception
    {
        String sql = "SELECT\r\n"
                   + " TO_CHAR(ANOEJ_V1,'0000')||'.'||TO_CHAR(ANORI_V1,'0000')||'.'||TO_CHAR(NUMA_V1,'000000')||'.'||TO_CHAR(NUMD_V1,'0000') AS REFERENCIA,\r\n"
                   + " TIPDOCU_V1 AS TIPO_DOC,\r\n"
                   + " TO_CHAR(EIMPORTE_V1,'9G999G999G990D00', 'NLS_NUMERIC_CHARACTERS = '',.''') AS IMPORTE,\r\n"
                   + " FDOCU_V1 AS FECHA_DOCUMENTO,\r\n"
                   + " FTRAMI_V1 AS FECHA_TRAMITACION,\r\n"
                   + " FECMAQ_V1 AS FECHA_MAQUETA,\r\n"
                   + " NUMMAQ_V1 AS NUMERO_MAQUETA,\r\n"
                   + " FCONTA_V1 AS FECHA_CONTAB,\r\n"
                   + " APUNTE_V1 AS NUMERO_APUNTE\r\n"
                   + " FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide29.TABLA_J1201V00, ConstantesMeLanbide29.FICHERO_PROPIEDADES)+"\r\n"
                   + " WHERE CODAPLIC_V1='"+ConstantesMeLanbide29.TIPO_AYUDA_RPLEM+"'\r\n"
                   + " --and REFEXP_V1 = (SELECT nvl(substr(txt_valor, 5), '') FROM E_TXT WHERE TXT_COD='"+ConstantesMeLanbide29.CAMPO_NUMEXP_P29+"' and txt_num = '"+numExp+"')\r\n"
                   + " and REFEXP_V1 = (SELECT nvl(txt_valor, '') FROM E_TXT WHERE TXT_COD='"+ConstantesMeLanbide29.CAMPO_NUMEXP_P29+"' and txt_num = '"+numExp+"')\r\n"
                   + " ORDER BY REFERENCIA, TIPDOCU_V1";
        if(log.isDebugEnabled())
            log.debug("sql = "+sql);
        List<FilaDocumentoContableVO> retList = new ArrayList<FilaDocumentoContableVO>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                retList.add((FilaDocumentoContableVO)MeLanbide29MappingUtils.getInstance().map(rs, FilaDocumentoContableVO.class));
            }
        }
        catch(Exception ex)
        {
            retList.clear();
            log.error("Se ha producido un error recuperando lista documentos contables para expediente "+numExp, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
}
