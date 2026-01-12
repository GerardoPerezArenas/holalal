package es.altia.flexia.integracion.moduloexterno.melanbide62.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide62.util.ConstantesMeLanbide62;
import es.altia.flexia.integracion.moduloexterno.melanbide62.util.MeLanbide62MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.InfoExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.InfoTramiteVO;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide62DAO {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide62DAO.class);
    
    //Instancia
    private static MeLanbide62DAO instance = null;
    
    private MeLanbide62DAO()
    {
        
    }
    
    public static MeLanbide62DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide62DAO.class)
            {
                instance = new MeLanbide62DAO();
            }
        }
        return instance;
    }
    
    public ArrayList<InfoTramiteVO> getDatosSuplementariosTramite(int codOrg, int ejerc, String numExp, int codTram, Connection con) throws SQLException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query = null;
        ArrayList<InfoTramiteVO> datosFicha = null;
        String codProc = ConstantesMeLanbide62.COD_PROCEDIMIENTO;
        
        try {
            String org = String.valueOf(codOrg);
            query = new StringBuilder("SELECT CRO_OCU,TXTT_VALOR AS REFER,TNUT_VALOR AS IMPOR")
                    .append(getSubselectValorFecha(ConstantesMeLanbide62.FECHA_SOLICITUD))
                    .append(getSubselectValorFecha(ConstantesMeLanbide62.FECHA_DESDE))
                    .append(getSubselectValorFecha(ConstantesMeLanbide62.FECHA_HASTA))
                    .append(" FROM E_CRO LEFT JOIN E_TXTT ON")
                    .append(getParteOnJoin("TXTT")).append("LEFT JOIN E_TNUT ON").append(getParteOnJoin("TNUT"))
                    .append("WHERE CRO_MUN=? AND CRO_PRO=? AND CRO_EJE=? AND CRO_NUM=? AND CRO_TRA=? ")
                    .append("AND (TXTT_COD=? OR TXTT_COD IS NULL) AND (TNUT_COD=? OR TNUT_COD IS NULL) ")
                    .append("ORDER BY CRO_OCU");
        
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Organizacion: " + org);
                log.debug("Procedimiento: " + codProc);
                log.debug("Ejercicio: " + ejerc);
                log.debug("NumExpediente: " + numExp);
                log.debug("CodTramite:" + codTram);
                log.debug("CodCampoSup FECHA_SOLICITUD: " + ConstantesMeLanbide62.FECHA_SOLICITUD);
                log.debug("CodCampoSup FECHA_DESDE: " + ConstantesMeLanbide62.FECHA_DESDE);
                log.debug("CodCampoSup FECHA_HASTA: " + ConstantesMeLanbide62.FECHA_HASTA);
                log.debug("CodCampoSup REFERENCIA_IKUS: " + ConstantesMeLanbide62.REFERENCIA_IKUS);
                log.debug("CodCampoSup IMPORTE: " + ConstantesMeLanbide62.IMPORTE);
            }
            int contbd = 1;

            ps = con.prepareStatement(query.toString());
            ps.setString(contbd++, org);
            ps.setString(contbd++, codProc);
            ps.setInt(contbd++, ejerc);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, codTram);
            ps.setString(contbd++, ConstantesMeLanbide62.FECHA_SOLICITUD);
            ps.setString(contbd++, org);
            ps.setString(contbd++, codProc);
            ps.setInt(contbd++, ejerc);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, codTram);
            ps.setString(contbd++, ConstantesMeLanbide62.FECHA_DESDE);
            ps.setString(contbd++, org);
            ps.setString(contbd++, codProc);
            ps.setInt(contbd++, ejerc);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, codTram);
            ps.setString(contbd++, ConstantesMeLanbide62.FECHA_HASTA);
            ps.setString(contbd++, org);
            ps.setString(contbd++, codProc);
            ps.setInt(contbd++, ejerc);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, codTram);
            ps.setString(contbd++, ConstantesMeLanbide62.REFERENCIA_IKUS);
            ps.setString(contbd++, ConstantesMeLanbide62.IMPORTE);

            rs = ps.executeQuery();
            datosFicha = new ArrayList<InfoTramiteVO>();
            while(rs.next()){
                Date fecSol = rs.getDate(ConstantesMeLanbide62.FECHA_SOLICITUD);
                Date fecDesde = rs.getDate(ConstantesMeLanbide62.FECHA_DESDE);
                Date fecHasta = rs.getDate(ConstantesMeLanbide62.FECHA_HASTA);
                double impor = rs.getDouble("IMPOR");
                String refer = rs.getString("REFER");
                if(fecSol!=null || fecDesde!=null || fecHasta!=null || refer!=null || impor>0){
                    InfoTramiteVO infoTr = new InfoTramiteVO();
                    infoTr.setCodTramite(codTram);
                    infoTr.setOcurrencia(rs.getInt("CRO_OCU"));
                    infoTr.setfSolPago(rs.getDate(ConstantesMeLanbide62.FECHA_SOLICITUD));
                    infoTr.setfDesdePago(rs.getDate(ConstantesMeLanbide62.FECHA_DESDE));
                    infoTr.setfHastaPago(rs.getDate(ConstantesMeLanbide62.FECHA_HASTA));
                    infoTr.setImportePago(rs.getDouble("IMPOR"));
                    infoTr.setRefIkusPago(rs.getString("REFER"));
                    // Seteamos las propiedades que son resultado de operar con y/o formatear las anteriores
                    String formato = ConstantesMeLanbide62.FORMATO_FECHA;
                    infoTr.setfSolPagoStr(formato);
                    infoTr.setfDesdePagoStr(formato);
                    infoTr.setfHastaPagoStr(formato);
                    infoTr.setDiasSolPago();
                    infoTr.setImportePagoFormat();

                    datosFicha.add(infoTr);
                }
            }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        return datosFicha;
    }
    
    public InfoExpedienteVO getDatosSuplementariosExpediente(ArrayList<String> codsCampo, int codOrg, int ejerc, String codProc, String numExp, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        InfoExpedienteVO infoExp = new InfoExpedienteVO();
        
        try {
            infoExp.setNumExpediente(numExp);
            infoExp.setCodProcedimiento(codProc);
            
            for(String codCampo : codsCampo){
                query = "SELECT PCA_TDA FROM E_EXP JOIN E_PCA ON EXP_MUN=PCA_MUN AND EXP_PRO=PCA_PRO "
                    + "WHERE EXP_MUN=? AND EXP_EJE=? AND EXP_PRO=? AND EXP_NUM=? AND PCA_COD=?";

                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                    log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejerc + "-" + codProc + "-" +numExp + "-" + codCampo);
                }

                int contbd = 1;
                ps = con.prepareStatement(query);
                ps.setString(contbd++, String.valueOf(codOrg));
                ps.setInt(contbd++, ejerc);
                ps.setString(contbd++, codProc);
                ps.setString(contbd++, numExp);
                ps.setString(contbd++, codCampo);

                rs = ps.executeQuery();
                while(rs.next()){
                    String tipoDato = rs.getString("PCA_TDA");
                    Object valorAux = getValorCampoSup(codCampo, tipoDato, codOrg, ejerc, numExp, con);
                    if(valorAux!=null) {
                        if(valorAux instanceof String){
                            String valor = (String) valorAux;
                            if(tipoDato.equals("1")) infoExp.setDiasSepe(Integer.parseInt(valor));
                            else infoExp.setFichaTecObserv(valor);
                        } else if(valorAux instanceof Date){
                            infoExp.setCampoSuplementario(codCampo,valorAux);
                        }
                    }
                }
            }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        return infoExp;        
    }
    
    public int grabarValorCampoSupTTL(String codCampo, String valorCS, String codOrg, int ejer, String numExp, Connection con) throws SQLException{
        PreparedStatement ps = null;
        String query;
        int contbd = 1;
        int actualizado = 0;
        
        try {
            query = "DELETE FROM E_TTL WHERE TTL_MUN=? AND TTL_EJE=? AND TTL_NUM=? AND TTL_COD=?";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejer + "-" + numExp + "-" + codCampo);
            }
            ps = con.prepareStatement(query);
            ps.setInt(contbd++, Integer.parseInt(codOrg));
            ps.setInt(contbd++, ejer);
            ps.setString(contbd++, numExp);
            ps.setString(contbd++, codCampo);
            ps.executeUpdate();
            
            if(valorCS != null && !"".equals(valorCS)){
                query = "INSERT INTO E_TTL (TTL_MUN,TTL_EJE,TTL_NUM,TTL_COD,TTL_VALOR) VALUES (?,?,?,?,?)";

                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                    log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejer + "-" + numExp + "-" + codCampo + "-" + valorCS);
                }

                ps = con.prepareStatement(query);
                contbd = 1;
                ps.setInt(contbd++, Integer.parseInt(codOrg));
                ps.setInt(contbd++, ejer);
                ps.setString(contbd++, numExp);
                ps.setString(contbd++, codCampo);
                java.io.StringReader cr = new java.io.StringReader(valorCS);
                ps.setCharacterStream(contbd++, cr, valorCS.length());             

                actualizado = ps.executeUpdate();
            } else actualizado = 1;
        } catch(Exception ex){
            log.error("Se ha producido un error al modificar el valor de campo suplementario", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
                if(ps!=null) ps.close();
            } catch(Exception e) {
                log.error("Se ha producido un error cerrando los recursos de bbdd", e);
                throw new SQLException(e);
            }
        }
        return actualizado;  
    }
    
    public int grabarValorCampoSup(String codCampo, Object valorCS, String codOrg, int ejer, String numExp, Connection con) throws SQLException{
        PreparedStatement ps = null;
        StringBuilder query = new StringBuilder("");
        String tipoDato;
        String parteTabla;
        int contbd = 1;
        int actualizado = 0;
        
        try {
            String[] partes = numExp.split("/");
            tipoDato = recuperarTipoCS(codCampo, Integer.parseInt(codOrg), partes[1], -1, con);
            parteTabla = recuperarTablaTipo(tipoDato, false, con);
            if(parteTabla.equals("")) parteTabla = "TXT";
            
            query = query.append("DELETE FROM E_").append(parteTabla).append(" WHERE ").append(parteTabla).append("_MUN=? AND ")
                    .append(parteTabla).append("_EJE=? AND ").append(parteTabla).append("_NUM=? AND ").append(parteTabla).append("_COD=?");
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejer + "-" + numExp + "-" + codCampo);
            }
            ps = con.prepareStatement(query.toString());
            ps.setInt(contbd++, Integer.parseInt(codOrg));
            ps.setInt(contbd++, ejer);
            ps.setString(contbd++, numExp);
            ps.setString(contbd++, codCampo);
            ps.executeUpdate();
            
            if(valorCS != null){
                query.delete(0, query.length());
                query.insert(0, "INSERT INTO E_").append(parteTabla).append(" (").append(parteTabla).append("_MUN,").append(parteTabla)
                        .append("_EJE,").append(parteTabla).append("_NUM,").append(parteTabla).append("_COD,").append(parteTabla)
                        .append("_VALOR) VALUES (?,?,?,?,?)");

                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                    log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejer + "-" + numExp + "-" + codCampo + "-" + valorCS);
                }

                ps = con.prepareStatement(query.toString());
                contbd = 1;
                ps.setInt(contbd++, Integer.parseInt(codOrg));
                ps.setInt(contbd++, ejer);
                ps.setString(contbd++, numExp);
                ps.setString(contbd++, codCampo);
                if(valorCS instanceof String){
                    String valor = (String) valorCS;
                    ps.setString(contbd++, valor);             
                } else if(valorCS instanceof Date){
                    Date valor = (Date) valorCS;
                    ps.setDate(contbd++, new java.sql.Date(valor.getTime()));             
                }

                actualizado = ps.executeUpdate();
            } else actualizado = 1;
        } catch(Exception ex){
            log.error("Se ha producido un error al modificar el valor de campo suplementario", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
                if(ps!=null) ps.close();
            } catch(Exception e) {
                log.error("Se ha producido un error cerrando los recursos de bbdd", e);
                throw new SQLException(e);
            }
        }
        return actualizado;  
    }
    
    public int setValorDefectoCS(String codCampo, Object valorCS, String codOrg, int ejer, String codProc, String numExp, int codTramite, int ocuTramite, Connection con) throws SQLException{
        PreparedStatement ps = null;
        StringBuilder query = new StringBuilder("");
        String tipoDato;
        String parteTabla;
        int contbd = 1;
        int actualizado = 0;
        
        try {
            tipoDato = recuperarTipoCS(codCampo, Integer.parseInt(codOrg), codProc, codTramite, con);
            parteTabla = recuperarTablaTipo(tipoDato, true, con);
            if(parteTabla.equals("")) parteTabla = "TXTT";
            
            String valorActual = (String) getValorCampoSupTram(parteTabla, codCampo, tipoDato, Integer.parseInt(codOrg), ejer, codProc, numExp, codTramite, ocuTramite, con);
            if(valorActual==null || valorActual.equals("")){
                if(valorActual!=null){
                    query = query.append("DELETE FROM E_").append(parteTabla).append(" WHERE ").append(parteTabla).append("_MUN=? AND ")
                            .append(parteTabla).append("_EJE=? AND ").append(parteTabla).append("_NUM=? AND ").append(parteTabla).append("_COD=? AND ")
                            .append(parteTabla).append("_TRA=? AND ").append(parteTabla).append("_OCU=?");
                    if(log.isDebugEnabled()) {
                        log.debug("sql = " + query);
                        log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejer + "-" + numExp + "-" + codCampo + "-" + codTramite + "-" + ocuTramite);
                    }
                    ps = con.prepareStatement(query.toString());
                    ps.setInt(contbd++, Integer.parseInt(codOrg));
                    ps.setInt(contbd++, ejer);
                    ps.setString(contbd++, numExp);
                    ps.setString(contbd++, codCampo);
                    ps.setInt(contbd++, codTramite);
                    ps.setInt(contbd++, ocuTramite);
                    ps.executeUpdate();
                }

                if(valorCS != null){
                    if(query.length()>0){
                        query.delete(0, query.length());
                        query.insert(0, "INSERT INTO E_");
                    } else 
                        query.append("INSERT INTO E_");
                    query.append(parteTabla).append(" (").append(parteTabla).append("_MUN,").append(parteTabla)
                            .append("_EJE,").append(parteTabla).append("_NUM,").append(parteTabla).append("_COD,").append(parteTabla)
                            .append("_VALOR,").append(parteTabla).append("_PRO,").append(parteTabla).append("_TRA,")
                            .append(parteTabla).append("_OCU) VALUES (?,?,?,?,?,?,?,?)");

                    if(log.isDebugEnabled()) {
                        log.debug("sql = " + query);
                        log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejer + "-" + numExp + "-" + codCampo + "-" + valorCS
                         + "-" + codProc + "-" + codTramite + "-" + ocuTramite);
                    }

                    ps = con.prepareStatement(query.toString());
                    contbd = 1;
                    ps.setInt(contbd++, Integer.parseInt(codOrg));
                    ps.setInt(contbd++, ejer);
                    ps.setString(contbd++, numExp);
                    ps.setString(contbd++, codCampo);
                    if(valorCS instanceof String){
                        String valor = (String) valorCS;
                        ps.setString(contbd++, valor);             
                    } else if(valorCS instanceof Date){
                        Date valor = (Date) valorCS;
                        ps.setDate(contbd++, new java.sql.Date(valor.getTime()));             
                    }
                    ps.setString(contbd++, codProc);
                    ps.setInt(contbd++, codTramite);
                    ps.setInt(contbd++, ocuTramite);

                    actualizado = ps.executeUpdate();
                } else actualizado = 1;
            }  else actualizado = 1;
        } catch(Exception ex){
            log.error("Se ha producido un error al grabar el valor de campo suplementario", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
                if(ps!=null) ps.close();
            } catch(Exception e) {
                log.error("Se ha producido un error cerrando los recursos de bbdd", e);
                throw new SQLException(e);
            }
        }
        return actualizado;  
    }
    
    private Object getValorCampoSup(String codCampo, String tipoDato, int codOrg, int ejerc, String numExp, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder("");
        String partTabla;
        Object valor = null;
        
        try {
            partTabla = recuperarTablaTipo(tipoDato, false, con);
            if(partTabla.equals("")) partTabla = "TXT";
            
            query = query.append("SELECT ").append(partTabla).append("_VALOR FROM E_").append(partTabla).append(" WHERE ")
                    .append(partTabla).append("_MUN=? AND ").append(partTabla).append("_EJE=? AND ").append(partTabla).append("_NUM=? AND ")
                    .append(partTabla).append("_COD=?");

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejerc + "-" +numExp + "-" + codCampo);
            }

            int contbd = 1;
            ps = con.prepareStatement(query.toString());
            ps.setString(contbd++, String.valueOf(codOrg));
            ps.setInt(contbd++, ejerc);
            ps.setString(contbd++, numExp);
            ps.setString(contbd++, codCampo);

            rs = ps.executeQuery();
            while(rs.next()){
                if(tipoDato.equals("1") || tipoDato.equals("8")) //Numérico o numérico calculado
                    valor = String.valueOf(rs.getInt(partTabla+"_VALOR"));
                if(tipoDato.equals("3") || tipoDato.equals("9")) //Fecha o fecha calculada
                    valor = rs.getDate(partTabla+"_VALOR");
                else valor = rs.getString(partTabla+"_VALOR");
            }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch(Exception e) {
                log.error("Se ha producido un error cerrando los recursos de bbdd", e);
                throw new SQLException(e);
            }
        }
        return valor;
    }
    
    private Object getValorCampoSupTram(String particula, String codCampo, String tipoDato, int codOrg, int ejerc, String codPro, String numExp, int codTramite, int ocuTramite, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder("");
        Object valor = null;
        
        try {
            query = query.append("SELECT ").append(particula).append("_VALOR AS VALOR FROM E_").append(particula).append(" WHERE ")
                    .append(particula).append("_MUN=? AND ").append(particula).append("_EJE=? AND ").append(particula).append("_NUM=? AND ")
                    .append(particula).append("_COD=? AND ").append(particula).append("_PRO=? AND ").append(particula).append("_TRA=? AND ")
                    .append(particula).append("_OCU=?");

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetors pasados a la query: " + codOrg + "-" + ejerc + "-" +numExp + "-" + codCampo + "-" + codPro + "-" + codTramite + "-" + ocuTramite);
            }

            int contbd = 1;
            ps = con.prepareStatement(query.toString());
            ps.setString(contbd++, String.valueOf(codOrg));
            ps.setInt(contbd++, ejerc);
            ps.setString(contbd++, numExp);
            ps.setString(contbd++, codCampo);
            ps.setString(contbd++, codPro);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, ocuTramite);

            rs = ps.executeQuery();
            while(rs.next()){
                if(tipoDato.equals("1") || tipoDato.equals("8")) //Numérico o numérico calculado
                    valor = String.valueOf(rs.getInt("VALOR"));
                if(tipoDato.equals("3") || tipoDato.equals("9")) //Fecha o fecha calculada
                    valor = rs.getDate("VALOR");
                else valor = rs.getString("VALOR");
            }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch(Exception e) {
                log.error("Se ha producido un error cerrando los recursos de bbdd", e);
                throw new SQLException(e);
            }
        }
        return valor;
    }
    
    private String recuperarTipoCS(String codCampo, int codOrg, String codProc, int codTramite, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String tipoDato = null;
        
        try {
            if(codTramite==-1)
                query = "SELECT PCA_TDA AS TIPO FROM E_PCA WHERE PCA_MUN=? AND PCA_PRO=? AND PCA_COD=?";
            else
                query = "SELECT TCA_TDA AS TIPO FROM E_TCA WHERE TCA_MUN=? AND TCA_PRO=? AND TCA_COD=? AND TCA_TRA=?";

                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                    log.debug("Parámetors pasados a la query: " + codOrg + "-" + codProc + "-" + codCampo);
                }

                int contbd = 1;
                ps = con.prepareStatement(query);
                ps.setString(contbd++, String.valueOf(codOrg));
                ps.setString(contbd++, codProc);
                ps.setString(contbd++, codCampo);
                if(codTramite!=-1)
                    ps.setInt(contbd++, codTramite);

                rs = ps.executeQuery();
                if(rs.next()){
                    tipoDato = rs.getString("TIPO");
                }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch(Exception e) {
                log.error("Se ha producido un error cerrando los recursos de bbdd", e);
                throw new SQLException(e);
            }
        }
        return tipoDato;
    }
    
    private String recuperarTablaTipo(String tipoDato, boolean deTramite, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String idTabla = "";
        
        try {
            query = "SELECT TDA_TAB FROM E_TDA WHERE TDA_COD=?";

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetors pasados a la query: " + tipoDato);
            }

            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(tipoDato));

            rs = ps.executeQuery();
            if(rs.next()){
                idTabla = rs.getString("TDA_TAB").substring(2);
            }
        } catch(Exception ex){
            log.error("Se ha producido un error al recuperar el nombre de tabla en la que buscar el valor de cs según el tipo de dato", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar los recursos de bbdd");
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch(Exception e) {
                log.error("Se ha producido un error cerrando los recursos de bbdd", e);
                throw new SQLException(e);
            }
        }
        if(deTramite) return idTabla + "T";
        return idTabla;
    }
            
    private String getSubselectValorFecha(String codCampo){
        StringBuilder query = new StringBuilder(",(SELECT TFET_VALOR FROM E_TFET ")
            .append("WHERE TFET_MUN=? AND TFET_PRO=? AND TFET_EJE=? AND TFET_NUM=? AND TFET_TRA=? AND TFET_COD=? ")
            .append("AND TFET_OCU=CRO_OCU").append(") AS ").append(codCampo);
        
        if(log.isDebugEnabled()){
            log.debug("Subquery para obtener el valor de " + codCampo + " = " + query);
        }
        
        return query.toString();
    }
    
    private String getParteOnJoin(String pref){
        StringBuilder parteOn = new StringBuilder(" (CRO_MUN=");
        parteOn.append(pref).append("_MUN AND CRO_PRO=").append(pref).append("_PRO AND CRO_EJE=")
                .append(pref).append("_EJE AND CRO_NUM=").append(pref).append("_NUM AND CRO_TRA=").append(pref).append("_TRA AND CRO_OCU=")
                .append(pref).append("_OCU) ");
        return parteOn.toString();
    }
    
    public ExpedienteVO getDatosExpediente(int codOrg, String numExpediente, Connection con) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query;
        try{
            //CIFCBSC EN E_TXT
            query = new StringBuilder(" SELECT T_CAMPOS_FECHA.VALOR AS fecnacimiento, TFE_VALOR AS fecPresentacion\n " +
                                      " FROM e_exp\n " +
                                      " INNER JOIN e_ext ON e_exp.EXP_MUN  =e_ext.EXT_MUN AND e_exp.EXP_EJE=e_ext.EXT_EJE " + 
                                                "AND e_exp.EXP_NUM=e_ext.EXT_NUM AND e_exp.EXP_PRO=e_ext.EXT_PRO\n " +
                                      " LEFT JOIN T_CAMPOS_FECHA ON e_ext.EXT_TER  =T_CAMPOS_FECHA.COD_TERCERO AND e_ext.EXT_MUN=T_CAMPOS_FECHA.COD_MUNICIPIO\n " +
                                      " LEFT JOIN ( SELECT TFE_EJE,TFE_NUM,TFE_MUN,TFE_VALOR FROM e_tfe\n " +
                                                    " WHERE TFE_COD='FECHAPRESENTACION') e_tfe\n " +
                                        " ON e_exp.EXP_EJE  =e_tfe.TFE_EJE AND e_exp.EXP_NUM=e_tfe.TFE_NUM AND e_exp.EXP_MUN=e_tfe.TFE_MUN\n " +
                                      "WHERE e_exp.EXP_NUM =? AND e_exp.EXP_MUN =? AND e_ext.EXT_ROL =? AND T_CAMPOS_FECHA.COD_CAMPO ='TFECNACIMIENTO'");
            log.debug("query = "+query);
            log.debug("parametros de la query - EXP_NUM: "+numExpediente);
            log.debug("parametros de la query - EXT_ROL: "+ConstantesMeLanbide62.ROLES.INTERESADO);
            
            ps = con.prepareStatement(query.toString());
            ps.setString(1, numExpediente);
            ps.setString(2, String.valueOf(codOrg));
            ps.setString(3, String.valueOf(ConstantesMeLanbide62.ROLES.INTERESADO));
            rs = ps.executeQuery();
            if(rs.next())
            {
                log.debug("rs.next()");
                return (ExpedienteVO) MeLanbide62MappingUtils.getInstance().map(rs, ExpedienteVO.class);
            }
        } catch(Exception ex){
            log.error("Ha ocurrido un error al obtener los datos del tercero del expediente: "+numExpediente);
            ex.printStackTrace();
            throw ex;
        } finally{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
        }
        
        return null;
    }
    
    public void deleteSuplNumerico(int codOrg, String numExpediente, String nombreCampo, Connection con) throws Exception{
        PreparedStatement ps = null;
        String query;
        try{
            query = "DELETE FROM E_TNU where TNU_NUM=? and TNU_MUN=? and TNU_COD=?";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query TNU_COD= "+nombreCampo);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);
            
            ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al borrar datos suplementarios");
            throw sqle;
        } finally {
            if(ps!=null) ps.close();
        }
    }
    
    public void insertSuplNumerico(int codOrg, String numExpediente, Integer valor, String nombreCampo, Connection con) throws Exception{
        PreparedStatement ps = null;
        String query;
        try{
            query = "INSERT INTO E_TNU (TNU_VALOR,TNU_NUM,TNU_MUN,TNU_COD,TNU_EJE) VALUES(?,?,?,?,?)";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query TNU_VALOR= "+valor);
            log.debug("parametros pasados a la query TNU_COD= "+nombreCampo);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, valor);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);
            ps.setString(contbd++, numExpediente.substring(0,4));
            
            ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al guardar datos suplementarios");
            throw sqle;
        } finally {
            if(ps!=null) ps.close();
        }
    }
}
