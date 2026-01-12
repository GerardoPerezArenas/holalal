/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide38.util;

import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.ContratoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.FilaContratoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.Tercero;
import java.sql.ResultSet;

/**
 *
 * @author santiagoc
 */
public class MeLanbide38MappingUtils
{
    private static MeLanbide38MappingUtils instance = null;
    
    private MeLanbide38MappingUtils()
    {
        
    }
    
    public static MeLanbide38MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide38MappingUtils.class)
            {
                instance = new MeLanbide38MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == FilaContratoVO.class)
        {
            return mapearFilaContratoVO(rs);
        }
        else if(clazz == ContratoVO.class)
        {
            return mapearContratoVO(rs);
        }
        else if(clazz == Tercero.class)
        {
            return mapearTercero(rs);
        }
        return null;
    }
    
    private FilaContratoVO mapearFilaContratoVO(ResultSet rs) throws Exception
    {
        FilaContratoVO fila = new FilaContratoVO();
        fila.setDni(rs.getString("SOXEX_DOC") != null ? rs.getString("SOXEX_DOC").replaceAll("\r\n", "<br />").replaceAll("\n", "<br />").replaceAll("\r", "<br />").replaceAll(System.getProperty("line.separator"), "<br />").toUpperCase() : "-");
        String nomApe = "";
        String nom = rs.getString("SOXEX_NOM");
        String ape1 = rs.getString("SOXEX_AP1");
        String ape2 = rs.getString("SOXEX_AP2");
        nomApe = nom != null ? nom : "";
        nomApe += ape1 != null && !ape1.equals("") ? (!nomApe.equals("") ? " " : "") : "";
        nomApe += ape1 != null ? ape1 : "";
        nomApe += ape2 != null && !ape2.equals("") ? (!nomApe.equals("") ? " " : "") : "";
        nomApe += ape2 != null ? ape2 : "";
        fila.setNombreApellidos(nomApe.replaceAll("\r\n", "<br />").replaceAll("\n", "<br />").replaceAll("\r", "<br />").replaceAll(System.getProperty("line.separator"), "<br />").toUpperCase());
        fila.setNumContrato(rs.getInt("SOXEX_NCON"));
        if(rs.wasNull())
        {
            fila.setNumContrato(null);
        }
        return fila;
    }
    
    private ContratoVO mapearContratoVO(ResultSet rs) throws Exception
    {
        ContratoVO contrato = new ContratoVO();
        contrato.setAp1(rs.getString("SOXEX_AP1"));
        contrato.setAp2(rs.getString("SOXEX_AP2"));
        contrato.setCnoe(rs.getString("SOXEX_CNOE"));
        contrato.setCol(rs.getInt("SOXEX_COL"));
        if(rs.wasNull())
        {
            contrato.setCol(null);
        }
        contrato.setCss(rs.getBigDecimal("SOXEX_CSS"));
        contrato.setDco(rs.getInt("SOXEX_DCO"));
        if(rs.wasNull())
        {
            contrato.setDco(null);
        }
        contrato.setDoc(rs.getString("SOXEX_DOC"));
        contrato.setEje(rs.getInt("SOXEX_EJE"));
        if(rs.wasNull())
        {
            contrato.setEje(null);
        }
        contrato.setFac(rs.getDate("SOXEX_FAC"));
        contrato.setFfc(rs.getDate("SOXEX_FFC"));
        contrato.setFna(rs.getDate("SOXEX_FNA"));
        contrato.setImp(rs.getBigDecimal("SOXEX_IMP"));
        contrato.setImr(rs.getBigDecimal("SOXEX_IMR"));
        String inm = rs.getString("SOXEX_INM");
        if(inm != null)
        {
            if(inm.equalsIgnoreCase("1"))
            {
                contrato.setInm(ConstantesMeLanbide38.SI);
            }
            else
            {
                contrato.setInm(ConstantesMeLanbide38.NO);
            }
        }
        else
        {
            contrato.setInm(ConstantesMeLanbide38.NO);
        }
        contrato.setIre(rs.getBigDecimal("SOXEX_IRE"));
        contrato.setMde(rs.getInt("SOXEX_MDE"));
        if(rs.wasNull())
        {
            contrato.setMde(null);
        }
        String min = rs.getString("SOXEX_MIN");
        if(min != null)
        {
            if(min.equalsIgnoreCase("1"))
            {
                contrato.setMin(ConstantesMeLanbide38.SI);
            }
            else
            {
                contrato.setMin(ConstantesMeLanbide38.NO);
            }
        }
        else
        {
            contrato.setMin(ConstantesMeLanbide38.NO);
        }
        contrato.setMun(rs.getInt("SOXEX_MUN"));
        if(rs.wasNull())
        {
            contrato.setMun(null);
        }
        contrato.setNes(rs.getInt("SOXEX_NES"));
        if(rs.wasNull())
        {
            contrato.setNes(null);
        }
        contrato.setNom(rs.getString("SOXEX_NOM"));
        contrato.setNum(rs.getString("SOXEX_NUM"));
        String otr = rs.getString("SOXEX_OTR");
        if(otr != null)
        {
            if(otr.equalsIgnoreCase("1"))
            {
                contrato.setOtr(ConstantesMeLanbide38.SI);
            }
            else
            {
                contrato.setOtr(ConstantesMeLanbide38.NO);
            }
        }
        else
        {
            contrato.setOtr(ConstantesMeLanbide38.NO);
        }
        contrato.setPjt(rs.getBigDecimal("SOXEX_PJT"));
        String pld = rs.getString("SOXEX_PLD");
        if(pld != null)
        {
            if(pld.equalsIgnoreCase("1"))
            {
                contrato.setPld(ConstantesMeLanbide38.SI);
            }
            else
            {
                contrato.setPld(ConstantesMeLanbide38.NO);
            }
        }
        else
        {
            contrato.setPld(ConstantesMeLanbide38.NO);
        }
        contrato.setPro(rs.getString("SOXEX_PRO"));
        String rml = rs.getString("SOXEX_RML");
        if(rml != null)
        {
            if(rml.equalsIgnoreCase("1"))
            {
                contrato.setRml(ConstantesMeLanbide38.SI);
            }
            else
            {
                contrato.setRml(ConstantesMeLanbide38.NO);
            }
        }
        else
        {
            contrato.setRml(ConstantesMeLanbide38.NO);
        }
        contrato.setSal(rs.getBigDecimal("SOXEX_SAL"));
        contrato.setSex(rs.getInt("SOXEX_SEX"));
        if(rs.wasNull())
        {
            contrato.setSex(null);
        }
        contrato.setTco(rs.getInt("SOXEX_TCO"));
        if(rs.wasNull())
        {
            contrato.setTco(null);
        }
        contrato.setTerCod(rs.getLong("SOXEX_TER_COD"));
        contrato.setTic(rs.getInt("SOXEX_TIC"));
        if(rs.wasNull())
        {
            contrato.setTic(null);
        }
        contrato.setTjo(rs.getString("SOXEX_TJO"));
        contrato.setnCon(rs.getInt("SOXEX_NCON"));
        if(rs.wasNull())
        {
            contrato.setnCon(null);
        }
        contrato.setcPro(rs.getInt("SOXEX_CPRO"));
        if(rs.wasNull())
        {
            contrato.setcPro(null);
        }
        return contrato;
    }
    
    private Tercero mapearTercero(ResultSet rs) throws Exception
    {
        Tercero t = new Tercero();
        t.setApellido1(rs.getString("TER_AP1"));
        t.setApellido2(rs.getString("TER_AP2"));
        t.setDni(rs.getString("TER_DOC"));
        t.setId(rs.getLong("TER_COD"));
        if(rs.wasNull())
        {
            t.setId(null);
        }
        t.setNombre(rs.getString("TER_NOM"));
        return t;
    }
}
