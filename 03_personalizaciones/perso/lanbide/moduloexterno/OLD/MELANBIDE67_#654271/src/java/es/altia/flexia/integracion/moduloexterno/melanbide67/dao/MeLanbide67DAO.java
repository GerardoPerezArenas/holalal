/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.MeLanbide67MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.CentroColVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.EntidadColaboradoraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.FilaCentroColVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.FilaPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.LakModRecalculoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.LeaukPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.PersonaContratadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItemPuestos;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SubSolicVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author santiagoc
 */
public class MeLanbide67DAO {

    //Logger
    private static Logger log = Logger.getLogger(MeLanbide67DAO.class);

    //Instancia
    private static MeLanbide67DAO instance = null;

    private MeLanbide67DAO() {

    }

    public static MeLanbide67DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide67DAO.class) {
                instance = new MeLanbide67DAO();
            }
        }
        return instance;
    }

    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try {
            String query = null;
            query = "select TNU_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TNU_MUN = '" + codOrganizacion + "' and TNU_EJE = '" + ejercicio
                    + "' and TNU_NUM = '" + numExp + "' and TNU_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNU_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }

    public int guardarValorCampoNumerico(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, BigDecimal valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            boolean nuevo = false;
            if (this.getValorCampoNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR)"
                        + " values(" + codOrganizacion
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", '" + codigoCampo + "'"
                        + ", " + valor.toPlainString()
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " set TNU_VALOR = " + valor.toPlainString()
                        + " where TNU_MUN = '" + codOrganizacion + "'"
                        + " and TNU_EJE = " + ejercicio
                        + " and TNU_NUM = '" + numExp + "'"
                        + " and TNU_COD = '" + codigoCampo + "'";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql guardarValorCampoNumerico= " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }

    public String getValorCampoTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = '" + codOrganizacion + "' and TXT_EJE = '" + ejercicio
                    + "' and TXT_NUM = '" + numExp + "' and TXT_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TXT_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario texto " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }

    public int guardarValorCampoTexto(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, String valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            boolean nuevo = false;
            if (this.getValorCampoTexto(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " (TXT_MUN, TXT_EJE, TXT_NUM, TXT_COD, TXT_VALOR)"
                        + " values(" + codOrganizacion
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", '" + codigoCampo + "'"
                        + ", '" + valor + "'"
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " set TXT_VALOR = '" + valor + "'"
                        + " where TXT_MUN = '" + codOrganizacion + "'"
                        + " and TXT_EJE = " + ejercicio
                        + " and TXT_NUM = '" + numExp + "'"
                        + " and TXT_COD = '" + codigoCampo + "'";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario texto " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }

    public String getValorCampoDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TDE_MUN = '" + codOrganizacion + "' and TDE_EJE = '" + ejercicio
                    + "' and TDE_NUM = '" + numExp + "' and TDE_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDE_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario desplegable " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }

    public int guardarValorCampoDesplegable(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, String valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            boolean nuevo = false;
            if (this.getValorCampoDesplegable(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " (TDE_MUN, TDE_EJE, TDE_NUM, TDE_COD, TDE_VALOR)"
                        + " values(" + codOrganizacion
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", '" + codigoCampo + "'"
                        + ", '" + valor + "'"
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " set TDE_VALOR = '" + valor + "'"
                        + " where TDE_MUN = '" + codOrganizacion + "'"
                        + " and TDE_EJE = " + ejercicio
                        + " and TDE_NUM = '" + numExp + "'"
                        + " and TDE_COD = '" + codigoCampo + "'";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario desplegable " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }

    /*public Map<String, Integer> cargarCalculosPuestos(int codOrganizacion, Integer ejercicio, String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Map<String, Integer> calculos = new HashMap<String, Integer>();
        try
        {
            String query =null;
            Integer valor = null;
            
            //Puestos solicitados
            query = "select count(*) as pstOL from CPE_PUESTO where CPE_PTO_NUMEXP = '"+numExpediente+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("pstOL");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide67.CAMPO_SUPL_PUESTOS_SOLICITADOS, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide67.CAMPO_SUPL_PUESTOS_SOLICITADOS, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide67.CAMPO_SUPL_PUESTOS_SOLICITADOS, 0);
            }
            
            //Puestos denegados
            query = "select count(*) as PDEN from CPE_PUESTO where CPE_PTO_NUMEXP = '"+numExpediente+"'"
                    +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide67.CODIGO_RESULTADO_DENEGADO+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PDEN");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide67.CAMPO_SUPL_PUESTOS_DENEGADOS, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide67.CAMPO_SUPL_PUESTOS_DENEGADOS, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide67.CAMPO_SUPL_PUESTOS_DENEGADOS, 0);
            }
 
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error calculando puestos para el expediente "+numExpediente, ex);
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
        return calculos;
    }
     */
    public LeaukPuestoVO getPuestoPorCodigoYExpediente(LeaukPuestoVO p, Connection con) throws Exception {
        if (p != null && p.getIdPuesto() != null) {
            Statement st = null;
            ResultSet rs = null;
            LeaukPuestoVO puesto = null;
            try {
                /*String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_CPE_PUESTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                              +" where CPE_PTO_COD_PUESTO = '"+p.getCodPuesto()+"'"
                              +" and CPE_PTO_EXP_EJE = "+(p.getEjercicio() != null ? p.getEjercicio().toString() : "")
                              +" and CPE_PTO_NUMEXP = '"+p.getNumExp()+"'";*/
                String query = "select * from MELANBIDE67_EMPRESAS"
                        + " where ID = " + p.getIdPuesto() + ""
                        + " and NUM_EXP = '" + p.getNumExp() + "' ORDER BY ID ASC";
                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    puesto = (LeaukPuestoVO) MeLanbide67MappingUtils.getInstance().map(rs, LeaukPuestoVO.class);
                }
                return puesto;
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando puesto " + (p != null ? p.getCodPuesto() : "(puesto = null)") + " para expediente " + (p != null ? p.getNumExp() : "(puesto = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        } else {
            return null;
        }
    }

    public CentroColVO getCentroColPorCodigoYExpediente(CentroColVO cc, Connection con) throws Exception {
        if (cc != null) {
            Statement st = null;
            ResultSet rs = null;
            CentroColVO centroCol = null;
            try {

                String query = "select * from MELANBIDE67_CENTROS"
                        + " where ID = " + cc.getIdCentroCol() + ""
                        + " and NUM_EXP = '" + cc.getNumExp() + "' ORDER BY ID ASC";
                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    centroCol = (CentroColVO) MeLanbide67MappingUtils.getInstance().map(rs, CentroColVO.class);
                }
                return centroCol;
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando centro colaborador " + (cc != null ? cc.getIdCentroCol() : "(puesto = null)") + " para expediente " + (cc != null ? cc.getNumExp() : "(centro colaborador = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        } else {
            return null;
        }
    }

    public LeaukPuestoVO guardarLakPuestoVO(LeaukPuestoVO puesto, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
            if (puesto.getIdPuesto() == 0) {
                /*String codPuesto = this.getNuevoCodigoPuesto(puesto, con);
                if(codPuesto == null || codPuesto.equals(""))
                {
                    throw new Exception();
                }
                puesto.setCodPuesto(codPuesto);*/
                puesto.setIdPuesto(this.getNextId("MELANBIDE67_EMPRESAS_SQ", con));
                //Es un puesto nuevo
                query = "insert into MELANBIDE67_EMPRESAS (ID,NUM_EXP,COD_PUESTO,NOMBRE_PUESTO_SOL,APELLIDO1,APELLIDO2,NOMBRE,IMPORTE_SUBV_SOL,IMPORTE_SUBV_EST,IMPORTE_SUBV_OFE,"
                        + "REINTEGROS,IMPORTE_TOTAL,SALARIO_SOL,SALARIO_OFE,SEXO_SOL,SEXO_OFE,DPTO_SOL,DPTO_OFE,TITULACION_SOL,TITULACION_OFE,MODALIDAD_SOL,MODALIDAD_OFE,JORNADA_LAB_SOL,JORNADA_LAB_OFE,CENTRO_TRAB_SOL,CENTRO_TRAB_OFE,CTA_COTIZ_SOL,CTA_COTIZ_OFE,"
                        + "FEC_INI_CONTR_SOL,FEC_FIN_CONTR_SOL,FEC_INI_CONTR_OFE,FEC_FIN_CONTR_OFE,GRUPO_COTIZ_SOL,GRUPO_COTIZ_OFE,CONVENIO_COL_SOL,CONVENIO_COL_OFE,OBSERVACIONES,COD_TIPO_NIF,DNI_TRAB,FEC_NACIMIENTO,NUM_OFERTA,CENTRO_COL_GESTION,CENTRO_COL_CAPTACION,IMP_PAGO_1,IMP_PAGO_2"
                        + ")"
                        + " values(" + puesto.getIdPuesto() + ""
                        + ", " + (puesto.getNumExp() != null ? "'" + puesto.getNumExp() + "'" : "null")
                        + ", " + (puesto.getCodPuesto() != null ? "'" + puesto.getCodPuesto() + "'" : "null")
                        + ", " + (puesto.getDescPuesto() != null ? "'" + puesto.getDescPuesto() + "'" : "null")
                        + ", " + (puesto.getApellido1() != null ? "'" + puesto.getApellido1() + "'" : "null")
                        + ", " + (puesto.getApellido2() != null ? "'" + puesto.getApellido2() + "'" : "null")
                        + ", " + (puesto.getNombre() != null ? "'" + puesto.getNombre() + "'" : "null")
                        + ", " + (puesto.getImpSubvSol() != null ? puesto.getImpSubvSol().toPlainString() : "null")
                        + ", " + (puesto.getImpSubvEst() != null ? puesto.getImpSubvEst().toPlainString() : "null")
                        + ", " + (puesto.getImpSubvOfe() != null ? puesto.getImpSubvOfe().toPlainString() : "null")
                        + ", " + (puesto.getReintegros() != null ? puesto.getReintegros().toPlainString() : "null")
                        + ", " + (puesto.getImpTotal() != null ? puesto.getImpTotal().toPlainString() : "null")
                        + ", " + (puesto.getSalarioSol() != null ? puesto.getSalarioSol().toPlainString() : "null")
                        + ", " + (puesto.getSalarioOfe() != null ? puesto.getSalarioOfe().toPlainString() : "null")
                        + ", " + ("'" + puesto.getSexoSol() + "'")
                        + ", " + ("'" + puesto.getSexoOfe() + "'")
                        + ", " + (puesto.getDptoSol() != null ? "'" + puesto.getDptoSol() + "'" : "null")
                        + ", " + (puesto.getDptoOfe() != null ? "'" + puesto.getDptoOfe() + "'" : "null")
                        + ", " + (puesto.getCodTitulacionSol() != null ? "'" + puesto.getCodTitulacionSol() + "'" : "null")
                        + ", " + (puesto.getCodTitulacionOfe() != null ? "'" + puesto.getCodTitulacionOfe() + "'" : "null")
                        + ", " + (puesto.getCodModSol() != null ? "'" + puesto.getCodModSol() + "'" : "null")
                        + ", " + (puesto.getCodModOfe() != null ? "'" + puesto.getCodModOfe() + "'" : "null")
                        + ", " + (puesto.getJornadaLabSol() != null ? puesto.getJornadaLabSol().toPlainString() : "null")
                        + ", " + (puesto.getJornadaLabOfe() != null ? puesto.getJornadaLabOfe().toPlainString() : "null")
                        + ", " + (puesto.getCentroTrabSol() != null ? "'" + puesto.getCentroTrabSol() + "'" : "null")
                        + ", " + (puesto.getCentroTrabOfe() != null ? "'" + puesto.getCentroTrabOfe() + "'" : "null")
                        + ", " + (puesto.getCtaCotizSol() != null ? "'" + puesto.getCtaCotizSol() + "'" : "null")
                        + ", " + (puesto.getCtaCotizOfe() != null ? "'" + puesto.getCtaCotizOfe() + "'" : "null")
                        + ", " + (puesto.getFecIniSol() != null ? "TO_DATE('" + format.format(puesto.getFecIniSol()) + "', 'dd/mm/yyyy')" : "null")
                        + ", " + (puesto.getFecFinSol() != null ? "TO_DATE('" + format.format(puesto.getFecFinSol()) + "', 'dd/mm/yyyy')" : "null")
                        + ", " + (puesto.getFecIniOfe() != null ? "TO_DATE('" + format.format(puesto.getFecIniOfe()) + "', 'dd/mm/yyyy')" : "null")
                        + ", " + (puesto.getFecFinOfe() != null ? "TO_DATE('" + format.format(puesto.getFecFinOfe()) + "', 'dd/mm/yyyy')" : "null")
                        + ", " + (puesto.getGrupoCotizSol() != null ? "'" + puesto.getGrupoCotizSol() + "'" : "null")
                        + ", " + (puesto.getGrupoCotizOfe() != null ? "'" + puesto.getGrupoCotizOfe() + "'" : "null")
                        + ", " + (puesto.getConvenioColSol() != null ? "'" + puesto.getConvenioColSol() + "'" : "null")
                        + ", " + (puesto.getConvenioColOfe() != null ? "'" + puesto.getConvenioColOfe() + "'" : "null")
                        + ", " + (puesto.getObservaciones() != null ? "'" + puesto.getObservaciones() + "'" : "null")
                        + ", " + (puesto.getCodTipoNif() != null ? "'" + puesto.getCodTipoNif() + "'" : "null")
                        + ", " + (puesto.getNif() != null ? "'" + puesto.getNif() + "'" : "null")
                        + ", " + (puesto.getFecNacimiento() != null ? "TO_DATE('" + format.format(puesto.getFecNacimiento()) + "', 'dd/mm/yyyy')" : "null")
                        + ", " + (puesto.getNum_oferta() != null ? "'" + puesto.getNum_oferta() + "'" : "null")
                        + ", " + (puesto.getCentroColGestion() != null ? "'" + puesto.getCentroColGestion() + "'" : "null")
                        + ", " + (puesto.getCentroColCaptacion() != null ? "'" + puesto.getCentroColCaptacion() + "'" : "null")
                        + ", " + (puesto.getImpPago1() != null ? puesto.getImpPago1().toPlainString() : "null")
                        + ", " + (puesto.getImpPago2() != null ? puesto.getImpPago2().toPlainString() : "null")
                        + ")";

            } else {
                //Es un puesto que ya existe
                query = "update MELANBIDE67_EMPRESAS"
                        + " set"
                        + " COD_PUESTO = " + (puesto.getCodPuesto()) + ", "
                        + " APELLIDO1 = " + (puesto.getApellido1() != null ? "'" + puesto.getApellido1() + "'" : "null") + ", "
                        + " NOMBRE_PUESTO_SOL = " + (puesto.getDescPuesto() != null ? "'" + puesto.getDescPuesto() + "'" : "null") + ", "
                        + " APELLIDO2 = " + (puesto.getApellido2() != null ? "'" + puesto.getApellido2() + "'" : "null") + ", "
                        + " NOMBRE = " + (puesto.getNombre() != null ? "'" + puesto.getNombre() + "'" : "null") + ", "
                        + " IMPORTE_SUBV_SOL = " + (puesto.getImpSubvSol() != null ? puesto.getImpSubvSol().toPlainString() : "null") + ", "
                        + " IMPORTE_SUBV_EST = " + (puesto.getImpSubvEst() != null ? puesto.getImpSubvEst().toPlainString() : "null") + ", "
                        + " IMPORTE_SUBV_OFE = " + (puesto.getImpSubvOfe() != null ? puesto.getImpSubvOfe().toPlainString() : "null") + ", "
                        + " REINTEGROS = " + (puesto.getReintegros() != null ? puesto.getReintegros().toPlainString() : "null") + ", "
                        + " IMPORTE_TOTAL = " + (puesto.getImpTotal() != null ? puesto.getImpTotal().toPlainString() : "null") + ", "
                        + " SALARIO_SOL = " + (puesto.getSalarioSol() != null ? puesto.getSalarioSol().toPlainString() : "null") + ", "
                        + " SALARIO_OFE = " + (puesto.getSalarioOfe() != null ? puesto.getSalarioOfe().toPlainString() : "null") + ", "
                        + " SEXO_SOL = " + ("'" + puesto.getSexoSol() + "'") + ", "
                        + " SEXO_OFE = " + ("'" + puesto.getSexoOfe() + "'") + ", "
                        + " DPTO_SOL = " + (puesto.getDptoSol() != null ? "'" + puesto.getDptoSol() + "'" : "null") + ", "
                        + " DPTO_OFE = " + (puesto.getDptoOfe() != null ? "'" + puesto.getDptoOfe() + "'" : "null") + ", "
                        + " TITULACION_SOL = " + (puesto.getCodTitulacionSol() != null ? "'" + puesto.getCodTitulacionSol() + "'" : "null") + ", "
                        + " TITULACION_OFE = " + (puesto.getCodTitulacionOfe() != null ? "'" + puesto.getCodTitulacionOfe() + "'" : "null") + ", "
                        + " MODALIDAD_SOL = " + (puesto.getCodModSol() != null ? "'" + puesto.getCodModSol() + "'" : "null") + ", "
                        + " MODALIDAD_OFE = " + (puesto.getCodModOfe() != null ? "'" + puesto.getCodModOfe() + "'" : "null") + ", "
                        + " JORNADA_LAB_SOL = " + (puesto.getJornadaLabSol() != null ? puesto.getJornadaLabSol().toPlainString() : "null") + ", "
                        + " JORNADA_LAB_OFE = " + (puesto.getJornadaLabOfe() != null ? puesto.getJornadaLabOfe().toPlainString() : "null") + ", "
                        + " CENTRO_TRAB_SOL = " + (puesto.getCentroTrabSol() != null ? "'" + puesto.getCentroTrabSol() + "'" : "null") + ", "
                        + " CENTRO_TRAB_OFE = " + (puesto.getCentroTrabOfe() != null ? "'" + puesto.getCentroTrabOfe() + "'" : "null") + ", "
                        + " CTA_COTIZ_SOL = " + (puesto.getCtaCotizSol() != null ? "'" + puesto.getCtaCotizSol() + "'" : "null") + ", "
                        + " CTA_COTIZ_OFE = " + (puesto.getCtaCotizOfe() != null ? "'" + puesto.getCtaCotizOfe() + "'" : "null") + ", "
                        + " FEC_INI_CONTR_SOL = " + (puesto.getFecIniSol() != null ? "TO_DATE('" + format.format(puesto.getFecIniSol()) + "', 'dd/mm/yyyy')" : "null") + ", "
                        + " FEC_FIN_CONTR_SOL = " + (puesto.getFecFinSol() != null ? "TO_DATE('" + format.format(puesto.getFecFinSol()) + "', 'dd/mm/yyyy')" : "null") + ", "
                        + " FEC_INI_CONTR_OFE = " + (puesto.getFecIniOfe() != null ? "TO_DATE('" + format.format(puesto.getFecIniOfe()) + "', 'dd/mm/yyyy')" : "null") + ", "
                        + " FEC_FIN_CONTR_OFE = " + (puesto.getFecFinOfe() != null ? "TO_DATE('" + format.format(puesto.getFecFinOfe()) + "', 'dd/mm/yyyy')" : "null") + ", "
                        + " GRUPO_COTIZ_SOL = " + (puesto.getGrupoCotizSol() != null ? "'" + puesto.getGrupoCotizSol() + "'" : "null") + ", "
                        + " GRUPO_COTIZ_OFE = " + (puesto.getGrupoCotizOfe() != null ? "'" + puesto.getGrupoCotizOfe() + "'" : "null") + ", "
                        + " CONVENIO_COL_SOL = " + (puesto.getConvenioColSol() != null ? "'" + puesto.getConvenioColSol() + "'" : "null") + ", "
                        + " CONVENIO_COL_OFE = " + (puesto.getConvenioColOfe() != null ? "'" + puesto.getConvenioColOfe() + "'" : "null") + ", "
                        + " OBSERVACIONES = " + (puesto.getObservaciones() != null ? "'" + puesto.getObservaciones() + "'" : "null") + ", "
                        + " COD_TIPO_NIF = " + (puesto.getCodTipoNif() != null ? "'" + puesto.getCodTipoNif() + "'" : "null") + ", "
                        + " DNI_TRAB = " + (puesto.getNif() != null ? "'" + puesto.getNif() + "'" : "null") + ", "
                        + " FEC_NACIMIENTO = " + (puesto.getFecNacimiento() != null ? "TO_DATE('" + format.format(puesto.getFecNacimiento()) + "', 'dd/mm/yyyy')" : "null") + ", "
                        + " NUM_OFERTA = " + (puesto.getNum_oferta() != null ? "'" + puesto.getNum_oferta() + "'" : "null") + ", "
                        + " CENTRO_COL_GESTION = " + (puesto.getCentroColGestion() != null ? "'" + puesto.getCentroColGestion() + "'" : "null") + ", "
                        + " CENTRO_COL_CAPTACION = " + (puesto.getCentroColCaptacion() != null ? "'" + puesto.getCentroColCaptacion() + "'" : "null") + ", "
                        + " IMP_PAGO_1 = " + (puesto.getImpPago1() != null ? puesto.getImpPago1().toPlainString() : "null") + ", "
                        + " IMP_PAGO_2 = " + (puesto.getImpPago2() != null ? puesto.getImpPago2().toPlainString() : "null") + " "
                        + " where ID = '" + puesto.getIdPuesto() + "'"
                        //+ " and CPE_PTO_EXP_EJE = "+puesto.getEjercicio().toString()
                        + " and NUM_EXP = '" + puesto.getNumExp() + "'";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if (res > 0) {
                return puesto;
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos del puesto " + (puesto != null ? puesto.getCodPuesto() : "(puesto = null)") + " para el expediente " + (puesto != null ? puesto.getNumExp() : "(puesto = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public CentroColVO guardarCentroColaboradorVO(CentroColVO cc, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;

            //Es un centro colaborador que ya existe, siempre va a existir
            query = "UPDATE MELANBIDE67_CENTROS"
                    + " set"
                    + " OFERTA_EMPLEO = " + (cc.getOfertaEmpleo() != null ? "'" + cc.getOfertaEmpleo() + "'" : "null") + ", "
                    + " NUM_OFERTAS_SOL = " + (cc.getNumOfertaSol()) + ", "
                    + " NUM_OFERTAS_CON = " + (cc.getNumOfertaOfe()) + ", "
                    + " NUM_CONTR_SOL = " + (cc.getNumConSubvSol()) + ", "
                    + " NUM_CONTR_CON = " + (cc.getNumConSubvOfe()) + ", "
                    + " IMPORTE_SUBV = " + (cc.getSubvencion()) + ", "
                    + " IMPORTE_SUBV_SOL = " + (cc.getImpSubvSol() != null ? cc.getImpSubvSol().toPlainString() : "null") + ", "
                    + " IMPORTE_SUBV_CON = " + (cc.getImpSubvOfe() != null ? cc.getImpSubvOfe().toPlainString() : "null") + ", "
                    + " OBSERVACIONES = " + (cc.getObservaciones() != null ? "'" + cc.getObservaciones() + "'" : "null") + " "
                    + " where ID = '" + cc.getIdCentroCol() + "'"
                    + " and NUM_EXP = '" + cc.getNumExp() + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if (res > 0) {
                return cc;
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos del centro colaborador", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }

            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public List<FilaPuestoVO> getListaPuestosPorExpediente(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        try {
            String query = null;
            query = "SELECT ID,NUM_EXP,COD_PUESTO,NOMBRE_PUESTO_SOL,APELLIDO1,APELLIDO2,NOMBRE,IMPORTE_SUBV_SOL,IMPORTE_SUBV_EST,IMPORTE_SUBV_OFE,"
                    + "REINTEGROS,IMPORTE_TOTAL,SALARIO_SOL,SALARIO_OFE,SEXO_SOL,SEXO_OFE,DPTO_SOL,DPTO_OFE,TITULACION_SOL,TITULACION_OFE,MODALIDAD_SOL,MODALIDAD_OFE,JORNADA_LAB_SOL,JORNADA_LAB_OFE,CENTRO_TRAB_SOL,CENTRO_TRAB_OFE, "
                    + "CTA_COTIZ_SOL,CTA_COTIZ_OFE,FEC_INI_CONTR_SOL,FEC_FIN_CONTR_SOL,FEC_INI_CONTR_OFE,FEC_FIN_CONTR_OFE,GRUPO_COTIZ_SOL,GRUPO_COTIZ_OFE,CONVENIO_COL_SOL,CONVENIO_COL_OFE,OBSERVACIONES,COD_TIPO_NIF,DNI_TRAB,FEC_NACIMIENTO,NUM_OFERTA,CENTRO_COL_GESTION,CENTRO_COL_CAPTACION,IMP_PAGO_1,IMP_PAGO_2 "
                    + "FROM MELANBIDE67_EMPRESAS WHERE NUM_EXP = '" + numExpediente + "'" + " ORDER BY ID ASC";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                puestos.add((FilaPuestoVO) MeLanbide67MappingUtils.getInstance().map(rs, FilaPuestoVO.class));
            }
            return puestos;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando puestos para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public List<FilaCentroColVO> getListaCentrosColPorExpediente(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaCentroColVO> centrosCol = new ArrayList<FilaCentroColVO>();
        try {
            String query = null;
            query = "SELECT ID,NUM_EXP,OFERTA_EMPLEO,NUM_OFERTAS_SOL,NUM_OFERTAS_CON,NUM_CONTR_SOL,NUM_CONTR_CON,"
                    + "IMPORTE_SUBV,IMPORTE_SUBV_SOL,IMPORTE_SUBV_CON,OBSERVACIONES "
                    + "FROM MELANBIDE67_CENTROS WHERE NUM_EXP = '" + numExpediente + "'" + " ORDER BY ID ASC";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                centrosCol.add((FilaCentroColVO) MeLanbide67MappingUtils.getInstance().map(rs, FilaCentroColVO.class));
            }
            return centrosCol;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando centros colaboradores para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int getImporteSubv(int ejercicio, String ofertaEmpleo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaCentroColVO> centrosCol = new ArrayList<FilaCentroColVO>();
        int impstubv = 0;
        try {
            String query = null;
            if (ofertaEmpleo.startsWith("CAPTAC")) {
                query = "SELECT IMP_OFER_CAPTADAS_REG "
                        + "FROM MELANBIDE67_SUBV_CENTROS WHERE ANIO = " + ejercicio + "";
            } else {
                query = "SELECT IMP_OFER_GESTIONADAS "
                        + "FROM MELANBIDE67_SUBV_CENTROS WHERE ANIO = " + ejercicio + "";
            }

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                if (ofertaEmpleo.startsWith("CAPTAC")) {
                    impstubv = rs.getInt("IMP_OFER_CAPTADAS_REG");
                } else {
                    impstubv = rs.getInt("IMP_OFER_GESTIONADAS");
                }
            }
            return impstubv;

        } catch (Exception ex) {
            log.error("Se ha producido un error guardando el importe subvención ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int eliminarLakPuesto(LeaukPuestoVO p, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "delete from MELANBIDE67_EMPRESAS"
                    + " where ID = " + p.getIdPuesto() + ""
                    + " and NUM_EXP = '" + p.getNumExp() + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error eliminando puesto  " + (p != null ? p.getCodPuesto() : "puesto = null"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    /*public List<String> getNumerosExpedientes(Integer ejercicio, Connection con) throws Exception
    {
        List<String> retList = new ArrayList<String>();
        
        ResultSet rs = null;
        Statement st = null;
        try
        {
            String tablaPuesto = ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_CPE_PUESTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            
            String query = "select distinct CPE_PTO_NUMEXP from "+tablaPuesto+" where CPE_PTO_EXP_EJE = "+ejercicio+" order by CPE_PTO_NUMEXP";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retList.add(rs.getString("CPE_PTO_NUMEXP"));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
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
    }*/
    private Integer getNextId(String seqName, Connection con) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select " + seqName + ".nextval from dual";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numSec = rs.getInt(1);
                if (rs.wasNull()) {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error generando el siguiente valor de la secuencia " + seqName, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return numSec;
    }

    public List<SelectItemPuestos> getListaPuestos(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItemPuestos> puestos = new ArrayList<SelectItemPuestos>();
        try {
            String query = null;
            query = "select CODIGO, DESCRIPCION from MELANBIDE67_PUESTOS_TRAB ORDER BY DESCRIPCION";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            SelectItemPuestos si = null;
            int id = 0;
            String nombre = null;
            while (rs.next()) {
                id = new Integer(rs.getInt("CODIGO")).intValue();
                nombre = rs.getString("DESCRIPCION");
                si = new SelectItemPuestos();
                si.setId(id);
                si.setLabel(nombre);
                puestos.add(si);

            }
            return puestos;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando puestos para el expediente ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public String recalculoSubvLeaukCen(String numExp, Integer idLinea, String anio, Connection con) throws Exception {
        ResultSet rs = null;
        String result = "";
        CallableStatement call = null;
        try {
            if (idLinea != null) {
                call = con.prepareCall("{ ? = call Recalculosubvleaukcen(?,?,?) }");
            } else {
                call = con.prepareCall("{ ? = call Recalculosubvleaukcen(?,?,null) }");

            }
            call.registerOutParameter(1, java.sql.Types.VARCHAR);
            call.setString(2, numExp);
            call.setString(3, anio);
            if (idLinea != null) {
                call.setInt(4, idLinea);
            }

            call.execute();
            result = call.getString(1);

            return result;
        } catch (Exception ex) {
            log.error("Se ha producido un error en el recalculo de subvenciones de Leauk Centros", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (call != null) {
                    call.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public String recalculoSubvLeaukEmp(String numExp, Integer idLinea, String anio, Connection con) throws Exception {
        ResultSet rs = null;
        String result = "";
        CallableStatement call = null;
        try {
            if (idLinea != null) {
                call = con.prepareCall("{ ? = call Recalculosubvleaukemp(?,?,?) }");
            } else {
                call = con.prepareCall("{ ? = call Recalculosubvleaukemp(?,?,null) }");

            }
            call.registerOutParameter(1, java.sql.Types.VARCHAR);
            call.setString(2, numExp);
            call.setString(3, anio);
            if (idLinea != null) {
                call.setInt(4, idLinea);
            }

            call.execute();
            result = call.getString(1);

            return result;
        } catch (Exception ex) {
            log.error("Se ha producido un error en el recalculo de subvenciones de Leauk Empresas", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (call != null) {
                    call.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int crearRegistrosCentrosCol(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, Date valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        Statement st2 = null;

        try {
            //insert CAPTACIÓN Y REGISTRO DE OFERTA
            String query = null;
            int idCentroCap = this.getNextId("MELANBIDE67_CENTROS_SQ", con);
            int impstubvCap = getImporteSubv(Integer.parseInt(ejercicio), "CAPTACIÓN Y REGISTRO DE OFERTA", con);
            query = "insert into MELANBIDE67_CENTROS"
                    + " (ID,NUM_EXP, OFERTA_EMPLEO, NUM_OFERTAS_SOL, NUM_OFERTAS_CON, NUM_CONTR_SOL,NUM_CONTR_CON,IMPORTE_SUBV,IMPORTE_SUBV_SOL,IMPORTE_SUBV_CON,OBSERVACIONES)"
                    + " values(" + idCentroCap
                    + ", '" + numExp + "'"
                    + ", 'CAPTACIÓN Y REGISTRO DE OFERTA'"
                    + ", 0"
                    + ", 0"
                    + ", 0"
                    + ", 0"
                    + ", " + impstubvCap
                    + ", 0"
                    + ", 0"
                    + ", ''"
                    + ")";

            if (log.isDebugEnabled()) {
                log.debug("sql CAPTACIÓN Y REGISTRO DE OFERTA= " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);

            //insert GESTIÓN DE OFERTA
            String query2 = null;
            int idCentroGes = this.getNextId("MELANBIDE67_CENTROS_SQ", con);
            int impstubvGes = getImporteSubv(Integer.parseInt(ejercicio), "GESTIÓN DE OFERTA", con);

            query2 = "insert into MELANBIDE67_CENTROS"
                    + " (ID,NUM_EXP, OFERTA_EMPLEO, NUM_OFERTAS_SOL, NUM_OFERTAS_CON, NUM_CONTR_SOL,NUM_CONTR_CON,IMPORTE_SUBV,IMPORTE_SUBV_SOL,IMPORTE_SUBV_CON,OBSERVACIONES)"
                    + " values(" + idCentroGes
                    + ", '" + numExp + "'"
                    + ", 'GESTIÓN DE OFERTA'"
                    + ", 0"
                    + ", 0"
                    + ", 0"
                    + ", 0"
                    + ", " + impstubvGes
                    + ", 0"
                    + ", 0"
                    + ", ''"
                    + ")";

            if (log.isDebugEnabled()) {
                log.debug("sql gestion de oferta = " + query2);
            }
            st2 = con.createStatement();
            result = st2.executeUpdate(query2);
        } catch (Exception ex) {
            log.error("Se ha producido un error creando los dos registros iniciales de centros colaboradores para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (st2 != null) {
                    st2.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }

    public List<es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem> getTiposDocumento(Connection con) throws Exception {
        List<ValorCampoDesplegableModuloIntegracionVO> listaValores = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "SELECT TID_COD as CODIGO,TID_DES as DESCRIPCION FROM " + es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DOCUMENTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " ORDER BY TID_COD ASC";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                listaValores.add((ValorCampoDesplegableModuloIntegracionVO) MeLanbide67MappingUtils.getInstance().map(rs, ValorCampoDesplegableModuloIntegracionVO.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando lista tipos documento.", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return MeLanbide67MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(listaValores);
    }

    public String calculoSubvenLAK(String anio, String idPuesto, String titulacion, String modalidadContrato, String sexo, double jornada, Connection con) throws Exception {
        log.info("------- INICIO calculoSubvLak - DAO");
        ResultSet rs = null;
        String result = "";
        CallableStatement call = null;
        try {
            call = con.prepareCall("{? = call Recalculosubvlakemp(?,?,?,?,?,?)}");
            log.info("Llamo a la Procedure RecalculoSubvLak ");
            call.registerOutParameter(1, java.sql.Types.VARCHAR);
            call.setString(2, anio);
            call.setString(3, idPuesto);
            call.setString(4, titulacion);
            call.setString(5, modalidadContrato);
            call.setString(6, sexo);
            call.setDouble(7, jornada);

            call.execute();
            result = call.getString(1);
            log.info("Me devuelve: " + result);
            return result;
        } catch (Exception e) {
            log.error("Se ha producido un error en el calculo de subvenciones de Lak ", e);
            throw new Exception(e);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (call != null) {
                    call.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
            log.info("------- FIN calculoSubvLak - DAO");
        }

    }// calculoSubvLakEmp

    public String recalculoModsubvlakemp(LakModRecalculoVO datosCalculo, Connection con) throws Exception {
        log.info("------- INICIO RecalculoModsubvlakemp - DAO");
        ResultSet rs = null;
        String resultados = "";
        CallableStatement call = null;

        String anioSol = null;
        String codPuesto = null;
        String titulacion = null;
        String sexo = null;
        String jornada = null;
        String fecIni = null;
        String fecEsperada = null;
        String fecReal = null;
        String causa = "";
        String motivo = "";
        String concedidoSt = "0";
        double impConcedido = 0;
        String pagado1 = "0";
        double impPagadoPag1 = 0;
        String pagado2 = "0";
        double impPagadoPag2 = 0;

        //
        try {
            anioSol = datosCalculo.getAnioSol();
            codPuesto = datosCalculo.getCodPuesto();
            titulacion = datosCalculo.getTitulacion();
            sexo = datosCalculo.getSexo();
            jornada = datosCalculo.getJornada();
            fecIni = datosCalculo.getFecIni();
            fecEsperada = datosCalculo.getFecFinEspe();
            fecReal = datosCalculo.getFecFinReal();
            causa = datosCalculo.getCausa();
            motivo = datosCalculo.getMotivo();
            concedidoSt = datosCalculo.getImpConcedido();
            pagado1 = datosCalculo.getImpPagadoPag1();
            pagado2 = datosCalculo.getImpPagadoPag2();

        } catch (Exception e) {
            log.error("Se ha producido un error obteniendo los datos del VO ", e);
        }

        impConcedido = Double.parseDouble(concedidoSt);
        impPagadoPag1 = Double.parseDouble(pagado1);
        impPagadoPag2 = Double.parseDouble(pagado2);

        log.debug("PARAMETROS");
        log.debug("Año: " + anioSol);
        log.debug("Puesto: " + codPuesto);
        log.debug("Titulación: " + titulacion);
        log.debug("Sexo: " + sexo);
        log.debug("Jornada: " + jornada);
        log.debug("Fecha Inicio: " + fecIni);
        log.debug("Fecha Fin Esperada: " + fecEsperada);
        log.debug("Fecha Fin Real: " + fecReal);
        log.debug("Causa: " + causa);
        log.debug("Motivo: " + motivo);
        log.debug("Imp Concedido: " + impConcedido);
        log.debug("Pagado 1: " + impPagadoPag1);
        log.debug("Pagado 2: " + impPagadoPag2);
        try {

            call = con.prepareCall("{? = call RecalculoModsubvlakemp(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            call.registerOutParameter(1, java.sql.Types.VARCHAR);

            call.setString(2, anioSol);
            call.setString(3, codPuesto);
            call.setString(4, titulacion);
            call.setString(5, sexo);
            call.setString(6, jornada);
            call.setString(7, fecIni);
            call.setString(8, fecEsperada);
            call.setString(9, fecReal);
            call.setString(10, causa);
            call.setString(11, motivo);
            call.setDouble(12, impConcedido);
            call.setDouble(13, impPagadoPag1);
            call.setDouble(14, impPagadoPag2);
            // call = con.prepareCall("{? = call RecalculoModsubvlakemp(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            // call = con.prepareCall("{1 = call RecalculoModsubvlakemp(2,3,4,5,6,7,8,9,10,11,12,13,14)}");
            log.debug("Llamo a la Procedure RecalculoModSubvLakEmp ");

            call.execute();
            resultados = call.getString(1);
            log.debug("Me devuelve: " + resultados);
            return resultados;
        } catch (Exception e) {
            log.error("Se ha producido un error en la modificación del calculo de subvenciones de Lak ", e);
            throw new Exception(e);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (call != null) {
                    call.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
            log.debug("------- FIN calculoSubvLak - DAO");
        }
    }

    public String getValorCampoTextoTramite(int codOrganizacion, String procedimiento, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TXTT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_TEXTO_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TXTT_MUN = '" + codOrganizacion + "' and TXTT_EJE = '" + ejercicio
                    + "' and TXTT_NUM = '" + numExp + "' and TXTT_TRA ='" + tramite + "' and TXTT_COD = '" + codigoCampo + "'"
                    + " and txtt_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TXTT_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario texto " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }//get valor campo texto tramite

    public int guardarValorCampoTextoTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;

        try {
            boolean nuevo = false;
            if (this.getValorCampoTextoTramite(codOrganizacion, procedimiento, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_TEXTO_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " (TXTT_MUN, TXTT_PRO, TXTT_EJE, TXTT_NUM, TXTT_TRA, TXTT_COD, TXTT_VALOR,TXTT_ocu)"
                        + " values(" + codOrganizacion
                        + ", '" + procedimiento + "'"
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", " + tramite
                        + ", '" + codigoCampo + "'"
                        + ", '" + valor + "'"
                        + ", " + ocurrencia
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_TEXTO_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " set TXTT_VALOR = '" + valor + "'"
                        + " where TXTT_MUN = '" + codOrganizacion + "'"
                        + " and TXTT_EJE = " + ejercicio
                        + " and TXTT_NUM = '" + numExp + "'"
                        + " and TXTT_TRA = " + tramite
                        + " and TXTT_OCU = " + ocurrencia
                        + " and TXTT_COD = '" + codigoCampo + "'";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.debug("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo texto " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }// guardat valor txtt

    public BigDecimal getValorCampoNumericoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;

        try {
            String query = null;
            query = "select TNUT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_NUMERICO_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TNUT_MUN = '" + codOrganizacion + "' and TNUT_EJE = '" + ejercicio
                    + "' and TNUT_NUM = '" + numExp + "' and TNUT_TRA ='" + tramite + "' and TNUT_COD = '" + codigoCampo + "'"
                    + " and tnut_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNUT_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }// get numerico tramite

    public int guardarValorCampoNumericoTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;

        try {
            boolean nuevo = false;
            if (this.getValorCampoNumericoTramite(codOrganizacion, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            // VALOR DEBE VENIR FORMATEADO CON . DECIMALES COMO ORACLE
//            if(valor!=null && valor.contains(","))
//                valor = valor.replace(",",".");
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_NUMERICO_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " (TNUT_MUN, TNUT_PRO, TNUT_EJE, TNUT_NUM, TNUT_TRA,TNUT_OCU, TNUT_COD, TNUT_VALOR)"
                        + " values(" + codOrganizacion
                        + ", '" + procedimiento + "'"
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", " + tramite
                        + ", " + ocurrencia
                        + ", '" + codigoCampo + "'"
                        + ", '" + valor + "'"
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_NUMERICO_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " set TNUT_VALOR = '" + valor + "'"
                        + " where TNUT_MUN = '" + codOrganizacion + "'"
                        + " and TNUT_EJE = " + ejercicio
                        + " and TNUT_NUM = '" + numExp + "'"
                        + " and TNUT_TRA = " + tramite
                        + " and TNUT_OCU = " + ocurrencia
                        + " and TNUT_COD = '" + codigoCampo + "'";
            }
            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.info("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.info("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }// guardat valor numerico tramite

    public String getValorCampoDesplegableTramite(int codOrganizacion, String procedimiento, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDET_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TDET_MUN = '" + codOrganizacion + "' and TDET_EJE = '" + ejercicio
                    + "' and TDET_NUM = '" + numExp + "' and TDET_TRA ='" + tramite + "' and TDET_COD = '" + codigoCampo + "'"
                    + " and tdet_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDET_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario desplegable " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Valor " + codigoCampo + ": " + valor);
        return valor;
    }//get valor campo desplegable tramite

    public int guardarValorCampoDesplegableTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;

        try {
            boolean nuevo = false;
            if (this.getValorCampoDesplegableTramite(codOrganizacion, procedimiento, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " (TDET_MUN, TDET_PRO, TDET_EJE, TDET_NUM, TDET_TRA, TDET_OCU, TDET_COD, TDET_VALOR)"
                        + " values(" + codOrganizacion
                        + ", '" + procedimiento + "'"
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", " + tramite
                        + ", " + ocurrencia
                        + ", '" + codigoCampo + "'"
                        + ", '" + valor + "'"
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " set TDET_VALOR = '" + valor + "'"
                        + " where TDET_MUN = '" + codOrganizacion + "'"
                        + " and TDET_EJE = " + ejercicio
                        + " and TDET_NUM = '" + numExp + "'"
                        + " and TDET_TRA = " + tramite
                        + " and TDET_OCU = " + ocurrencia
                        + " and TDET_COD = '" + codigoCampo + "'";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.info("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo DESPLEGABLE " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }// guardar valor desplegable tramite

    public String getValorCampoFechaTramite(int codOrganizacion, String procedimiento, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TFET_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TFET_MUN = '" + codOrganizacion + "' and TFET_EJE = '" + ejercicio
                    + "' and TFET_NUM = '" + numExp + "' and TFET_COD = '" + codigoCampo + "'"
                    + " and tfet_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TFET_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FECHA " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Valor " + codigoCampo + ": " + valor);
        return valor;
    }

    public int guardarValorCampoFechaTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;

        try {
            boolean nuevo = false;
            if (this.getValorCampoFechaTramite(codOrganizacion, procedimiento, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " (TFET_MUN, TFET_PRO, TFET_EJE, TFET_NUM, TFET_TRA, TFET_OCU, TFET_COD, TFET_VALOR)"
                        + " values(" + codOrganizacion
                        + ", '" + procedimiento + "'"
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", " + tramite
                        + ", " + ocurrencia
                        + ", '" + codigoCampo + "'"
                        + ", TO_DATE('" + valor + "','dd/mm/yyyy')"
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " set TFET_VALOR = to_date('" + valor + "','dd/mm/yyyy')"
                        + " where TFET_MUN = '" + codOrganizacion + "'"
                        + " and TFET_EJE = " + ejercicio
                        + " and TFET_NUM = '" + numExp + "'"
                        + " and TFET_TRA = " + tramite
                        + " and TFET_OCU = " + ocurrencia
                        + " and TFET_COD = '" + codigoCampo + "'";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.info("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo FECHA " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return result;
    }// guardar valor fecha tramite

    public String getValorCampoFechaStr(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TFE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TFE_MUN = '" + codOrganizacion + "' and TFE_EJE = '" + ejercicio
                    + "' and TFE_NUM = '" + numExp + "' and TFE_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TFE_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario fecha " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Valor " + codigoCampo + ": " + valor);
        return valor;
    }

    public Date getValorFechaDate(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Date valor = null;
        try {
            String query = null;
            query = "select TFE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TFE_MUN = '" + codOrganizacion + "' and TFE_EJE = '" + ejercicio
                    + "' and TFE_NUM = '" + numExp + "' and TFE_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getDate("TFE_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el campo suplementario fecha " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Valor " + codigoCampo + ": " + valor);
        return valor;
    }

    public Long getTramiteActualExpediente(int codOrganizacion, int ejercicio, String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Long tram = null;
        try {
            String query = null;
            query = "select MAX(CRO_TRA) as CRO_TRA from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_E_CRO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where CRO_PRO = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide67.NOMBRE_PROCEDIMIENTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES) + "'"
                    + " and CRO_EJE = " + ejercicio + " and CRO_NUM = '" + numExp + "' and CRO_OCU = 1 and CRO_FEF is null";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                tram = rs.getLong("CRO_TRA");
                if (rs.wasNull()) {
                    tram = null;
                }
            }
            return tram;
        } catch (Exception ex) {
            con.rollback();
            log.error("Se ha producido un error recuperando Trámite actual", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }// getTramiteActual

    public List<SelectItem> getListaDesplegable(Connection con, String idLista) throws Exception {
        log.debug("Entro en listaDesplegable DAO");
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();
        String query = "select DES_VAL_COD, DES_NOM as NOMBRE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_E_DES_VAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                + "   where DES_COD='" + idLista + "' ";

        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }
        st = con.createStatement();
        rs = st.executeQuery(query);
        String id = null;
        String nombre = null;
        SelectItem si = null;/*new SelectItem();
            si.setId(-1);
            si.setLabel("");
            lista.add(si);*/
        while (rs.next()) {
            id = rs.getString("DES_VAL_COD");
            if (!rs.wasNull()) {
                nombre = rs.getString("NOMBRE");
                si = new SelectItem();
                si.setCodigo(id);
                si.setDescripcion(nombre);
                lista.add(si);
            }

        }
        return lista;
    }

    public String getValorDesplegable(Connection con, String codCampo, String codValor) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "select DES_NOM from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_E_DES_VAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                + "   where DES_COD='" + codCampo + "' "
                + " and DES_VAL_COD='" + codValor + "' ";
        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }
        st = con.createStatement();
        rs = st.executeQuery(query);
        String valor = null;
        while (rs.next()) {
            valor = rs.getString("DES_NOM");
        }
        log.info("valor Desplegable: " + valor);

        return valor;
    }

    public int guardarValorCalculoLAK(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, String valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            boolean nuevo = false;
            if (this.getValorCampoNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR)"
                        + " values(" + codOrganizacion
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", '" + codigoCampo + "'"
                        + ", " + valor
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                        + " set TNU_VALOR = " + valor
                        + " where TNU_MUN = '" + codOrganizacion + "'"
                        + " and TNU_EJE = " + ejercicio
                        + " and TNU_NUM = '" + numExp + "'"
                        + " and TNU_COD = '" + codigoCampo + "'";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.info("Se ha grabado el campo suplementario " + codigoCampo);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario LAK " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }

    public int eliminarValorCampoFechaTramite(int codOrganizacion, String ejercicio, String numExp, int codigoTramite, String codigoCampo, Connection con) throws Exception {

        log.debug("=========== ENTRO en eliminar campo Fecha Tramite");
        Statement st = null;
        try {
            String query = "delete " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TFET_MUN = '" + codOrganizacion + "'"
                    + " and TFET_EJE = " + ejercicio
                    + " and TFET_NUM = '" + numExp + "'"
                    + " and TFET_TRA = " + codigoTramite
                    + " and TFET_OCU = 1"
                    + " and TFET_COD = '" + codigoCampo + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error eliminando el campo fecha ");
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    } // eliminavValorFecha Tramite

    public int eliminarValorCampoTextoTramite(int codOrganizacion, String ejercicio, String numExp, int codigoTramite, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = "delete " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_TEXTO_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TXTT_MUN = '" + codOrganizacion + "'"
                    + " and TXTT_EJE = " + ejercicio
                    + " and TXTT_NUM = '" + numExp + "'"
                    + " and TXTT_TRA = " + codigoTramite
                    + " and TXTT_OCU = 1"
                    + " and TXTT_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error eliminando el campo fecha ");
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

    }

    public int eliminarValorCampoDesplegableTramite(int codOrganizacion, String ejercicio, String numExp, int codigoTramite, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = "delete " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE_TRAMITE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TdeT_MUN = '" + codOrganizacion + "'"
                    + " and TdeT_EJE = " + ejercicio
                    + " and TdeT_NUM = '" + numExp + "'"
                    + " and TdeT_TRA = " + codigoTramite
                    + " and TdeT_OCU = 1"
                    + " and TdeT_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error eliminarValorCampoDesplegableTramite ");
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("eliminarValorCampoDesplegableTramite - Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error(" eliminarValorCampoDesplegableTramite - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

    }

    public int getOcurrenciaTramiteAbiertoxCodigoInterno(String numExp, int tram, Connection con) throws Exception {
        log.info("getOcurrenciaTramiteAbiertoxCodigoInterno - ini()");
        Statement st = null;
        ResultSet rs = null;
        int ocurrencia = 1;
        try {
            String query = "SELECT CRO_OCU ocurrencia  FROM E_CRO WHERE CRO_NUM='" + numExp + "' AND CRO_TRA=" + tram + " AND CRO_FEF IS NULL order by CRO_OCU";
            log.debug("getOcurrenciaTramiteAbiertoxCodigoInterno - sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                ocurrencia = rs.getInt("ocurrencia");
            }
        } catch (Exception ex) {
            log.error("Excepcion - getOcurrenciaTramiteAbiertoxCodigoInterno : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getOcurrenciaTramiteAbiertoxCodigoInterno - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getOcurrenciaTramiteAbiertoxCodigoInterno - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getOcurrenciaTramiteAbiertoxCodigoInterno - end()" + ocurrencia);
        return ocurrencia;
    }

    public int getMaxOcurrenciaTramitexCodigoInterno(String numExp, int tram, Connection con) throws Exception {
        log.info("getMaxOcurrenciaTramitexCodigoInterno - ini()");
        Statement st = null;
        ResultSet rs = null;
        int ocurrencia = 1;
        try {
            String query = "SELECT MAX(CRO_OCU) ocurrencia  FROM E_CRO WHERE CRO_NUM='" + numExp + "' AND CRO_TRA=" + tram;
            log.debug("getMaxOcurrenciaTramitexCodigoInterno - sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                ocurrencia = rs.getInt("ocurrencia");
            }
        } catch (Exception ex) {
            log.error("Excepcion - getMaxOcurrenciaTramitexCodigoInterno : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getMaxOcurrenciaTramitexCodigoInterno - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getMaxOcurrenciaTramitexCodigoInterno - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getMaxOcurrenciaTramitexCodigoInterno - end()" + ocurrencia);
        return ocurrencia;
    }

    public BigDecimal getCosteContratoLAK(String ejercicio, String titulacion, Connection con) throws Exception {
        log.info("getCosteContratoLAK DAO- ini()");
        BigDecimal coste = BigDecimal.ZERO;
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = "SELECT retribucion FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_SUBVENCIONES, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " WHERE anio=" + ejercicio
                    + " AND titulacion='" + titulacion + "'";
            log.debug("getCosteContratoLAK - sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                coste = rs.getBigDecimal("RETRIBUCION");
            }
        } catch (Exception ex) {
            log.error("Excepcion - getCosteContratoLAK : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getCosteContratoLAK - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getCosteContratoLAK - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getCosteContratoLAK DAO- fin()");
        return coste;
    }

    public String getNIFEntidadColaboradora(String numeroExpediente, Connection con) throws Exception {
        log.info("INICIO getNIFEntidadColaboradora DAO");
        String nifEmpresario = "";

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT  TXT_VALOR ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numeroExpediente);
            query.append("' ");
            query.append("AND TXT_COD = 'NIFCOLAB' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                nifEmpresario = rs.getString("TXT_VALOR");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getNIFEntidadColaboradora : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getNIFEntidadColaboradora - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getNIFEntidadColaboradora - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return nifEmpresario;
    }

    public String getNumeroOferta(String numeroExpediente, Connection con) throws Exception {
        log.info("INICIO getNumeroOferta DAO");
        String numeroOferta = "";

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT  TXT_VALOR ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numeroExpediente);
            query.append("' ");
            query.append("AND TXT_COD = 'NUMEROTIPO2' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                numeroOferta = rs.getString("TXT_VALOR");
            }
        } catch (Exception ex) {
            log.error("Excepcion - getNumeroOferta : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getNumeroOferta - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getNumeroOferta - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return numeroOferta;
    }

    public String getNIFPersonaContratada(String numeroExpediente, Connection con) throws Exception {
        log.info("INICIO getNIFPersonaContratada DAO");
        String nifPersonaContratada = "";

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT  TXT_VALOR ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numeroExpediente);
            query.append("' ");
            query.append("AND TXT_COD = 'DNIPERCON' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                nifPersonaContratada = rs.getString("TXT_VALOR");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getNIFPersonaContratada : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getNIFPersonaContratada - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getNIFPersonaContratada - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return nifPersonaContratada;
    }

    public PersonaContratadaVO getPersonaContratada(String nifPersonaContratada, String numeroOferta, Connection con) throws Exception {
        log.info("INICIO getPersonaContratada DAO");
        PersonaContratadaVO personaContratada = new PersonaContratadaVO();

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT pc.GEN_PER_TIPO_DOC, ");
            query.append("pc.GEN_PER_NUM_DOC, "); //dni
            query.append("pc.GEN_PER_NOMBRE, "); //nombre
            query.append("pc.GEN_PER_APELLIDO1, "); //apellido1
            query.append("pc.GEN_PER_APELLIDO2, "); //apellido2
            query.append("pc.GEN_PER_FECHA_NAC, "); //fechaNacimiento
            query.append("pc.EDAD, "); //edad
            query.append("pc.SEXO, "); //sexo
            query.append("pc.ID_OFERTA, ");
            query.append("pc.CONTRAT_INCENT_LEHENAUK, "); //contratoIncentivadoLAK
            query.append("pc.DEM_DEM_FEC_REV_DEM, "); //fechaGestionDemanda
            query.append("pc.DEM_TIT_COD_TITU, "); //formacionCV
            query.append("pc.DEM_TIT_FEC_TITU, "); //anioObtencionTitulacion
            query.append("pc.DEM_DIA_CONTRATACION, "); //desempleadoDiaContratacion
            query.append("pc.ALTA_SIST_NAC_GARANTIA_JUVENIL, "); //altaSistemaGJ
            query.append("pc.EDICION_ANTERIOR_LEHEN_AUKERA, "); //participadoEdicionesAnterioresLAK
            query.append("pc.GEN_PER_REMUNI, "); //municipioDeResidencia
            query.append("o.OFE_FEC_REG, "); //fechaRegistro
            query.append("o.OFERTA_ASOC_LEHEN_AUKERA_2020, "); //ofertaLimitadaLAK2020
            query.append("o.NOMBRE_CENTRO_REGISTRO, "); //centroCaptacionRegistro
            query.append("o.NOMBRE_CENTRO_GESTION "); //centroGestion
            query.append("FROM S_VW_DATOS_PERSONA_CONTRATADA pc, ");
            query.append("S_VW_DATOS_OFERTA o ");
            query.append("WHERE pc.GEN_PER_NUM_DOC = '" + nifPersonaContratada + "' ");
            query.append("AND pc.ID_OFERTA = '" + numeroOferta + "' ");
            query.append("AND pc.ID_OFERTA = o.OFE_ID ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {

                personaContratada.setDni(rs.getString("GEN_PER_NUM_DOC"));
                personaContratada.setNombre(rs.getString("GEN_PER_NOMBRE"));
                personaContratada.setApellido1(rs.getString("GEN_PER_APELLIDO1"));
                personaContratada.setApellido2(rs.getString("GEN_PER_APELLIDO2"));
                personaContratada.setFechaNacimiento(rs.getDate("GEN_PER_FECHA_NAC"));
                personaContratada.setEdad(rs.getBigDecimal("EDAD"));
                personaContratada.setSexo(rs.getString("SEXO"));
                personaContratada.setContratoIncentivadoLAK(rs.getString("CONTRAT_INCENT_LEHENAUK"));
                personaContratada.setFechaGestionDemanda(rs.getDate("DEM_DEM_FEC_REV_DEM"));
                personaContratada.setFormacionCV(rs.getString("DEM_TIT_COD_TITU"));
                if (null != rs.getString("DEM_TIT_FEC_TITU")) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(rs.getDate("DEM_TIT_FEC_TITU"));
                    String anio = new Integer(cal.get(Calendar.YEAR)).toString();
                    personaContratada.setAnioObtencionTitulacion(anio);
                }
                personaContratada.setDesempleadoDiaContratacion(rs.getString("DEM_DIA_CONTRATACION"));
                personaContratada.setAltaSistemaGJ(rs.getString("ALTA_SIST_NAC_GARANTIA_JUVENIL"));
                personaContratada.setParticipadoEdicionesAnterioresLAK(rs.getString("EDICION_ANTERIOR_LEHEN_AUKERA"));
                personaContratada.setFechaRegistro(rs.getDate("OFE_FEC_REG"));
                personaContratada.setOfertaLimitadaLAK2020(rs.getString("OFERTA_ASOC_LEHEN_AUKERA_2020"));
                personaContratada.setCentroCaptacionRegistro(rs.getString("NOMBRE_CENTRO_REGISTRO"));
                personaContratada.setCentroGestion(rs.getString("NOMBRE_CENTRO_GESTION"));
                personaContratada.setMunicipioDeResidencia(rs.getString("GEN_PER_REMUNI"));
            }
        } catch (Exception ex) {
            log.error("Excepcion - getPersonaContratada : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getPersonaContratada - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getPersonaContratada - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getPersonaContratada DAO");
        return personaContratada;
    }

    public EntidadColaboradoraVO getEntidadColaboradora(String numeroOferta, Connection con) throws Exception {
        log.info("INICIO getEntidadColaboradora DAO");
        EntidadColaboradoraVO entidadColaboradora = new EntidadColaboradoraVO();

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT ec.GEN_CEN_NOM_UBIC, ");
            query.append("ec.GEN_DIRECCION, ");
            query.append("ec.GEN_COD_CODPOSTAL, ");
            query.append("ec.GEN_PRO_COD_PROV, ");
            query.append("ec.GEN_MUN_COD_MUN, ");
            query.append("ec.GEN_PER_CORR_RESPONSABLE, ");
            query.append("ec.GEN_PER_TIPO_DOC_RESPONSABLE, ");
            query.append("ec.GEN_PER_NUM_DOC_RESPONSABLE, ");
            query.append("ec.NIF_EMPRESARIO, ");
            query.append("ec.GEN_CEN_EMAIL, ");
            query.append("ec.GEN_CEN_TELEFONO1 ");
            query.append("FROM S_VW_DATOS_ENTIDADES_COLABO ec, ");
            query.append("S_VW_DATOS_OFERTA o ");
            query.append("WHERE ec.CENTRO_UBIC = o.CENTRO_UBIC_GESTION ");
            query.append("AND o.OFE_ID = '" + numeroOferta + "'  ");
            /*
            query.append("WHERE ec.NIF_EMPRRESARIO = '" + nifEmpresario + "'  ");
            query.append("AND ec.GEN_CEN_NOM_UBIC = o.NOMBRE_CENTRO_REGISTRO ");
            query.append("AND o.OFE_ID = = '" + numeroOferta + "'  ");
             */
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                entidadColaboradora.setNif(rs.getString("NIF_EMPRESARIO"));
                entidadColaboradora.setNombre(rs.getString("GEN_CEN_NOM_UBIC"));
                entidadColaboradora.setDireccion(rs.getString("GEN_DIRECCION"));
                entidadColaboradora.setCodigoPostal(rs.getString("GEN_COD_CODPOSTAL"));
                entidadColaboradora.setProvincia(rs.getString("GEN_PRO_COD_PROV"));
                entidadColaboradora.setMunicipio(rs.getString("GEN_MUN_COD_MUN"));
                entidadColaboradora.setEmail(rs.getString("GEN_CEN_EMAIL"));
                entidadColaboradora.setTelefono(rs.getString("GEN_CEN_TELEFONO1"));
            }
        } catch (Exception ex) {
            log.error("Excepcion - getEntidadColaboradora : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getEntidadColaboradora - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getEntidadColaboradora - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getEntidadColaboradora DAO");
        return entidadColaboradora;
    }

    public void setTextoDatosIntermediacion(String numExpediente, String valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setTextoDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();
            query.append("SELECT TXT_VALOR ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numExpediente);
            query.append("' AND TXT_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            // Si YA existe se ACTUALIZA
            if (rs.next()) {
                actualizarTextoDatosIntermediacion(numExpediente, valor, campo, con);
            } // Si NO existe se INSERTA
            else {
                insertarTextoDatosIntermediacion(numExpediente, valor, campo, txt_mun, txt_eje, con);
            }

        } catch (Exception ex) {
            log.error("Excepcion - setTextoDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("setTextoDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("setTextoDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN setTextoDatosIntermediacion DAO");
    }

    private void insertarTextoDatosIntermediacion(String numExpediente, String valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarTextoDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("INSERT INTO E_TXT (TXT_MUN, TXT_EJE, TXT_NUM, TXT_COD, TXT_VALOR) VALUES (");
            query.append(txt_mun);
            query.append(", ");
            query.append(txt_eje);
            query.append(", '");
            query.append(numExpediente);
            query.append("', '");
            query.append(campo);
            query.append("', '");
            query.append(valor);
            query.append("' ");
            query.append(") ");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (Exception ex) {
            log.error("Excepcion - insertarTextoDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("insertarTextoDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" insertarTextoDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN insertarTextoDatosIntermediacion DAO");
    }

    private void actualizarTextoDatosIntermediacion(String numExpediente, String valor, String campo, Connection con) throws Exception {
        log.info("INICIO actualizarTextoDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("UPDATE E_TXT SET TXT_VALOR = '");
            query.append(valor);
            query.append("' ");
            query.append("WHERE TXT_NUM = '");
            query.append(numExpediente);
            query.append("' AND TXT_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (Exception ex) {
            log.error("Excepcion - actualizarTextoDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizarTextoDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("actualizarTextoDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN actualizarTextoDatosIntermediacion DAO");
    }

    public void setFechaDatosIntermediacion(String numExpediente, Date valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setFechaDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();
            query.append("SELECT TFE_VALOR ");
            query.append("FROM E_TFE ");
            query.append("WHERE TFE_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFE_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            // Si YA existe se ACTUALIZA
            if (rs.next()) {
                actualizarFechaDatosIntermediacion(numExpediente, valor, campo, con);
            } // Si NO existe se INSERTA
            else {
                insertarFechaDatosIntermediacion(numExpediente, valor, campo, txt_mun, txt_eje, con);
            }

        } catch (Exception ex) {
            log.error("Excepcion - setFechaDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("setFechaDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("setFechaDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN setFechaDatosIntermediacion DAO");
    }

    private void insertarFechaDatosIntermediacion(String numExpediente, Date valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarFechaDatosIntermediacion DAO");
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("INSERT INTO E_TFE (TFE_MUN, TFE_EJE, TFE_NUM, TFE_COD, TFE_VALOR, TFE_FEC_VENCIMIENTO, PLAZO_ACTIVADO) VALUES (");
            query.append(txt_mun);
            query.append(", ");
            query.append(txt_eje);
            query.append(", '");
            query.append(numExpediente);
            query.append("', '");
            query.append(campo);
            query.append("', ");
            query.append("TO_DATE('" + format.format(valor) + "', 'dd/mm/yyyy')");
            query.append(", ");
            query.append("null, ");
            query.append("null ");
            query.append(") ");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (Exception ex) {
            log.error("Excepcion - insertarFechaDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("insertarFechaDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("insertarFechaDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN insertarFechaDatosIntermediacion DAO");
    }

    private void actualizarFechaDatosIntermediacion(String numExpediente, Date valor, String campo, Connection con) throws Exception {
        log.info("INICIO actualizarFechaDatosIntermediacion DAO");
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("UPDATE E_TFE SET TFE_VALOR = ");
            query.append("TO_DATE('" + format.format(valor) + "', 'dd/mm/yyyy')");
            query.append(" ");
            query.append("WHERE TFE_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFE_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (Exception ex) {
            log.error("Excepcion - actualizarFechaDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizarFechaDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("actualizarFechaDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN actualizarFechaDatosIntermediacion DAO");
    }

    public void setNumericoDatosIntermediacion(String numExpediente, BigDecimal valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setNumericoDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();
            query.append("SELECT TNU_VALOR ");
            query.append("FROM E_TNU ");
            query.append("WHERE TNU_NUM = '");
            query.append(numExpediente);
            query.append("' AND TNU_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            // Si YA existe se ACTUALIZA
            if (rs.next()) {
                actualizarNumericoDatosIntermediacion(numExpediente, valor, campo, con);
            } // Si NO existe se INSERTA
            else {
                insertarNumericoDatosIntermediacion(numExpediente, valor, campo, txt_mun, txt_eje, con);
            }

        } catch (Exception ex) {
            log.error("Excepcion - setNumericoDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("setNumericoDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("setNumericoDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN setNumericoDatosIntermediacion DAO");
    }

    private void insertarNumericoDatosIntermediacion(String numExpediente, BigDecimal valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarNumericoDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("INSERT INTO E_TNU (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) VALUES (");
            query.append(txt_mun);
            query.append(", ");
            query.append(txt_eje);
            query.append(", '");
            query.append(numExpediente);
            query.append("', '");
            query.append(campo);
            query.append("', ");
            query.append(valor);
            query.append(") ");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (Exception ex) {
            log.error("Excepcion - insertarNumericoDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("insertarNumericoDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("insertarNumericoDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN insertarNumericoDatosIntermediacion DAO");
    }

    private void actualizarNumericoDatosIntermediacion(String numExpediente, BigDecimal valor, String campo, Connection con) throws Exception {
        log.info("INICIO actualizarNumericoDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("UPDATE E_TNU SET TNU_VALOR = ");
            query.append(valor);
            query.append(" ");
            query.append("WHERE TNU_NUM = '");
            query.append(numExpediente);
            query.append("' AND TNU_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (Exception ex) {
            log.error("Excepcion - actualizarNumericoDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizarNumericoDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("actualizarNumericoDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN actualizarNumericoDatosIntermediacion DAO");
    }

    public void setComboDatosIntermediacion(String numExpediente, String valor, String campo, String desc, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setComboDatosIntermediacion DAO");

        // En el caso concreto de SEXO desde intermediacion nos llega HOMBRE/GIZONEZKOA y MUJER/EMAKUMEZKOA mientras que en nuestro sistema tenemos HOMBRE|GIZONEZKOA y MUJER|EMAKUMEZKOA respectivamente por lo que cambiamos el simbolo para que lo pueda localizar
        if (desc.equals("SEXO")) {
            valor = valor.replace("/", "|");
        }

        Statement st = null;
        ResultSet rs = null;
        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();

            query.append("SELECT TDE_VALOR ");
            query.append("FROM E_TDE ");
            query.append("WHERE TDE_NUM = '");
            query.append(numExpediente);
            query.append("' AND TDE_COD = '");
            query.append(campo);
            query.append("'");

            log.debug("** IAR ** - qwery:  " + query.toString());
            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            // Si YA existe se ACTUALIZA
            if (rs.next()) {
                actualizarComboDatosIntermediacion(numExpediente, valor, campo, desc, con);
            } // Si NO existe se INSERTA
            else {
                insertarComboDatosIntermediacion(numExpediente, valor, campo, desc, txt_mun, txt_eje, con);
            }

        } catch (Exception ex) {
            log.error("Excepcion - setComboDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("setComboDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("setComboDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN setComboDatosIntermediacion DAO");
    }

    private void insertarComboDatosIntermediacion(String numExpediente, String valor, String campo, String desc, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarComboDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            String valorDesc = null;

            StringBuffer query = new StringBuffer();

            query.append("SELECT DES_VAL_COD ");
            query.append("FROM E_DES_VAL ");
            query.append("WHERE DES_COD = '");
            query.append(desc);
            query.append("' ");

            // En el caso concreto de CURE (combos de SI / NO) desde intermediacion nos llega S o N mientras que en nuestro sistema tenemos SI o NO respectivamente.
            // La S o N la tenemos en el campo DES_VAL_COD y no en DES_NOM ya que en este tenemos SI o NO (pero esto puede cambiar debido al cambio de idioma
            if (desc.equals("CURE") || desc.equals("TERH")) {
                query.append("AND DES_VAL_COD = '");
                query.append(valor);
                query.append("' ");
            } else {
                query.append("AND DES_NOM = '");
                query.append(valor);
                query.append("' ");
            }

            log.debug("** IAR ** - qwery:  " + query.toString());
            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                valorDesc = rs.getString("DES_VAL_COD");

                query = new StringBuffer();
                query.append("INSERT INTO E_TDE (TDE_MUN, TDE_EJE, TDE_NUM, TDE_COD, TDE_VALOR) VALUES (");
                query.append(txt_mun);
                query.append(", ");
                query.append(txt_eje);
                query.append(", '");
                query.append(numExpediente);
                query.append("', '");
                query.append(campo);
                query.append("', '");
                query.append(valorDesc);
                query.append("') ");

                st = con.createStatement();
                st.executeUpdate(query.toString());
            }
        } catch (Exception ex) {
            log.error("Excepcion - insertarComboDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("insertarComboDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("insertarComboDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN insertarComboDatosIntermediacion DAO");
    }

    private void actualizarComboDatosIntermediacion(String numExpediente, String valor, String campo, String desc, Connection con) throws Exception {
        log.info("INICIO actualizarComboDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            String valorDesc = null;

            StringBuffer query = new StringBuffer();

            query.append("SELECT DES_VAL_COD ");
            query.append("FROM E_DES_VAL ");
            query.append("WHERE DES_COD = '");
            query.append(desc);
            query.append("' ");
            // En el caso concreto de CURE (combos de SI / NO) desde intermediacion nos llega S o N mientras que en nuestro sistema tenemos SI o NO respectivamente.
            // La S o N la tenemos en el campo DES_VAL_COD y no en DES_NOM ya que en este tenemos SI o NO (pero esto puede cambiar debido al cambio de idioma
            if (desc.equals("CURE") || desc.equals("TERH")) {
                query.append("AND DES_VAL_COD = '");
                query.append(valor);
                query.append("' ");
            } else {
                query.append("AND DES_NOM = '");
                query.append(valor);
                query.append("' ");
            }

            log.debug("** IAR ** - qwery:  " + query.toString());
            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                valorDesc = rs.getString("DES_VAL_COD");

                query = new StringBuffer();
                query.append("UPDATE E_TDE SET TDE_VALOR = '");
                query.append(valorDesc);
                query.append("' ");
                query.append("WHERE TDE_NUM = '");
                query.append(numExpediente);
                query.append("' AND TDE_COD = '");
                query.append(campo);
                query.append("'");

                st = con.createStatement();
                st.executeUpdate(query.toString());
            }
        } catch (Exception ex) {
            log.error("Excepcion - actualizarComboDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizarComboDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("actualizarComboDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN actualizarComboDatosIntermediacion DAO");
    }

    public void setComboMunicipioIntermediacion(String numExpediente, String idMunicipio, String campo,
            BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setComboMunicipioIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            final String nombreMunicipio = getNombreMunicipio(idMunicipio, con);
            // Comprobamos si ya existe información del campo en BBDD
            StringBuilder query = new StringBuilder();
            query.append("SELECT TDEX_CODSEL ");
            query.append("FROM E_TDEX ");
            query.append("WHERE TDEX_NUM = '");
            query.append(numExpediente);
            query.append("' AND TDEX_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            // Si YA existe se ACTUALIZA
            if (rs.next()) {
                actualizarMunicipioIntermediacion(numExpediente, nombreMunicipio, idMunicipio, campo, con);
            } // Si NO existe se INSERTA
            else {
                insertarMunicipioIntermediacion(numExpediente, nombreMunicipio, idMunicipio, campo, txt_mun, txt_eje, con);
            }

        } catch (Exception ex) {
            log.error("Excepcion - setComboMunicipioIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("setComboMunicipioIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("setComboMunicipioIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("FIN setComboMunicipioIntermediacion DAO");
    }

    private String getNombreMunicipio(final String idMunicipio, final Connection con) throws Exception {
        log.info("INICIO getNombreMunicipio DAO");

        String nombreMunicipio = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // Buscamos el nombre del municipio en la vista V_MUNICIPIOS
            StringBuilder query = new StringBuilder();
            query.append("SELECT DESCMUN ");
            query.append("FROM V_MUNICIPIOS ");
            query.append("WHERE CODMUN = '");
            query.append(idMunicipio);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                nombreMunicipio = rs.getString("DESCMUN");
            }

        } catch (SQLException ex) {
            log.error("Excepcion - getNombreMunicipio : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getNombreMunicipio - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("getNombreMunicipio - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("FIN getNombreMunicipio DAO");
        return nombreMunicipio;
    }

    private void insertarMunicipioIntermediacion(String numExpediente, String nombreMunicipio, String idMunicipio,
            String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarMunicipioIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuilder query = new StringBuilder();

            query.append("INSERT INTO E_TDEX (TDEX_MUN, TDEX_EJE, TDEX_NUM, TDEX_COD, TDEX_VALOR, TDEX_CODSEL) VALUES (");
            query.append(txt_mun);
            query.append(", ");
            query.append(txt_eje);
            query.append(", '");
            query.append(numExpediente);
            query.append("', '");
            query.append(campo);
            query.append("', '");
            query.append(nombreMunicipio);
            query.append("', '");
            query.append(idMunicipio);
            query.append("') ");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (SQLException ex) {
            log.error("Excepcion - insertarMunicipioIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("insertarMunicipioIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(" insertarMunicipioIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN insertarTextoDatosIntermediacion DAO");
    }

    private void actualizarMunicipioIntermediacion(String numExpediente, String nombreMunicipio, String idMunicipio, String campo, Connection con) throws Exception {
        log.info("INICIO actualizarMunicipioIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("UPDATE E_TDEX SET TDEX_VALOR = '");
            query.append(nombreMunicipio);
            query.append("', ");
            query.append("TDEX_CODSEL = '");
            query.append(idMunicipio);
            query.append("' WHERE TDEX_NUM = '");
            query.append(numExpediente);
            query.append("' AND TDEX_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (SQLException ex) {
            log.error("Excepcion - actualizarMunicipioIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizarMunicipioIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("actualizarMunicipioIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("FIN actualizarMunicipioIntermediacion DAO");
    }

    public void setFicheroCVIntermediacion(String numExpediente, File valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setFicheroCVIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();
            query.append("SELECT TFI_VALOR ");
            query.append("FROM E_TFI ");
            query.append("WHERE TFI_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFI_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            // Si YA existe se ACTUALIZA
            if (rs.next()) {
                actualizarFicheroCVIntermediacion(numExpediente, valor, campo, con);
            } // Si NO existe se INSERTA
            else {
                insertarFicheroCVIntermediacion(numExpediente, valor, campo, txt_mun, txt_eje, con);
            }

        } catch (Exception ex) {
            log.error("Excepcion - setFicheroCVIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("setFicheroCVIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("setFicheroCVIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN setFicheroCVIntermediacion DAO");
    }

    private void insertarFicheroCVIntermediacion(String numExpediente, File valor, String campo,
            BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarFicheroCVIntermediacion DAO");

        PreparedStatement st = null;
        int rows = 0;
        try {
            String sql = "INSERT INTO E_TFI "
                    + "(TFI_MUN, TFI_EJE, TFI_NUM, TFI_COD, TFI_VALOR, TFI_MIME, TFI_NOMFICH,"
                    + " TFI_TAMANHO) VALUES (" //?, ?, ?, ?, ?, ?, ?, ?)";
                    + txt_mun + ", " + txt_eje + ", '" + numExpediente + "', '" + campo + "', ?"
                    + ", 'application/pdf', '" + valor.getName() + "'"
                    + ", " + valor.length() + ")";
            log.info("insertarFicheroCVIntermediacion sql = " + sql);
            st = con.prepareStatement(sql);
            
            final byte[] buffer = getInputStream(valor);
            st.setBinaryStream(1, new ByteArrayInputStream(buffer), buffer.length);
            
            rows = st.executeUpdate();
        } catch (Exception ex) {
            log.error("Excepcion - insertarFicheroCVIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("insertarFicheroCVIntermediacion - Procedemos a cerrar el statement y el resultset: " + rows);
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("insertarFicheroCVIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN insertarFicheroCVIntermediacion DAO");
    }

    private void actualizarFicheroCVIntermediacion(String numExpediente, File valor, String campo,
            Connection con) throws Exception {
        log.info("INICIO actualizarFicheroCVIntermediacion DAO");

        PreparedStatement st = null;

        try {
            String sql = "UPDATE E_TFI SET TFI_VALOR = ?"
                    + " WHERE TFI_NUM = '"
                    + numExpediente + "' AND TFI_COD = '"
                    + campo + "'";
            st = con.prepareStatement(sql);

            final byte[] buffer = getInputStream(valor);
            st.setBinaryStream(1, new ByteArrayInputStream(buffer), buffer.length);

            st.executeUpdate();

        } catch (Exception ex) {
            log.error("Excepcion - actualizarFicheroCVIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizarFicheroCVIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("actualizarFicheroCVIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("FIN actualizarFicheroCVIntermediacion DAO");
    }

    private byte[] getInputStream(final File valor) throws Exception {
        if (valor.length() > Integer.MAX_VALUE) {
            throw new Exception("Erro: El fichero es demasiado grande para tratarlo");
        }
        final InputStream inputStream = new FileInputStream(valor);
        byte dataBuffer[] = new byte[(int) valor.length()];
        inputStream.read(dataBuffer, 0, (int) valor.length());
        inputStream.close();
        
        return dataBuffer;
    }

    public BigDecimal getMUNDatosIntermediacion(String numExpediente, Connection con) throws Exception {
        log.info("INICIO getMUNDatosIntermediacion DAO");
        BigDecimal txt_mun = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT  TXT_MUN ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numExpediente);
            query.append("' ");
            query.append("AND TXT_COD = 'DNIPERCON' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                txt_mun = rs.getBigDecimal("TXT_MUN");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getMUNDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getMUNDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("getMUNDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getMUNDatosIntermediacion DAO");
        return txt_mun;
    }

    public BigDecimal getEJEDatosIntermediacion(String numExpediente, Connection con) throws Exception {
        log.info("INICIO getEJEDatosIntermediacion DAO");
        BigDecimal txt_eje = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT  TXT_EJE ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numExpediente);
            query.append("' ");
            query.append("AND TXT_COD = 'DNIPERCON' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                txt_eje = rs.getBigDecimal("TXT_EJE");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getEJEDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getEJEDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("getEJEDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getEJEDatosIntermediacion DAO");
        return txt_eje;
    }

    public String getMunicipio(Integer codProvincia, Integer codMunicipio, Connection con) throws Exception {
        log.info("INICIO getMuncipio DAO");
        String municipio = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT MUN_NOM ");
            query.append("FROM T_MUN ");
            query.append("WHERE MUN_PRV = '");
            query.append(codProvincia);
            query.append("' ");
            query.append("AND MUN_COD = '");
            query.append(codMunicipio);
            query.append("' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                municipio = rs.getString("MUN_NOM");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getMuncipio : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getMuncipio - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("getMuncipio - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getEJEPersonaContratada DAO");
        return municipio;
    }

    /**
     * Recupera los datos de SubSolic del expediente
     *
     * @param numExp
     * @param codOrganizacion
     * @param con
     * @return
     * @throws Exception
     */
    public List<SubSolicVO> getDatosSubSolic(String numExp, int codOrganizacion, Connection con) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        SubSolicVO subSolic = new SubSolicVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_SUBSOLIC, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP=? ORDER BY ID";
            log.debug("sql = " + query);
            pst = con.prepareStatement(query);
            pst.setString(1, numExp);
            log.debug("PARÁMETROS: " + numExp);
            rs = pst.executeQuery();

            while (rs.next()) {
                subSolic = (SubSolicVO) MeLanbide67MappingUtils.getInstance().map(rs, SubSolicVO.class
                );
                lista.add(subSolic);
                subSolic = new SubSolicVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando SubSolic ", ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     * Recupera una fila de SubSolic por ID
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public SubSolicVO getSubSolicPorID(String id, Connection con) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        SubSolicVO subSolic = new SubSolicVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_SUBSOLIC, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " WHERE ID=?";
            log.debug("sql = " + query);
            pst = con.prepareStatement(query);
            pst.setString(1, id);
            log.debug("PARÁMETROS: " + id);
            rs = pst.executeQuery();

            while (rs.next()) {
                subSolic = (SubSolicVO) MeLanbide67MappingUtils.getInstance().map(rs, SubSolicVO.class
                );
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una SubSolic : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return subSolic;
    }

    /**
     * Operacion que elimina un registro
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarSubSolic(String id, Connection con) throws Exception {
        PreparedStatement pst = null;
        String query = null;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_SUBSOLIC, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " WHERE ID=?";
            log.debug("sql = " + query);
            pst = con.prepareStatement(query);
            pst.setString(1, id);
            log.debug("PARÁMETROS: " + id);
            return pst.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando SubSolic ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    /**
     * Operacion que crea un nuevo registro
     *
     * @param nuevoSubSolic
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevoSubSolic(SubSolicVO nuevoSubSolic, Connection con) throws Exception {
        PreparedStatement pst = null;
        String query = "";
        try {
            int i = 1;
            int id = getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide67.SEQ_SUBSOLIC, ConstantesMeLanbide67.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_SUBSOLIC, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,ESTADO,ORGANISMO,OBJETO,IMPORTE,FECHA) "
                    + " VALUES (?,?,?,?,?,?,?)";
            pst = con.prepareStatement(query);
            pst.setInt(i++, id);
            pst.setString(i++, nuevoSubSolic.getNumExp());
            pst.setString(i++, nuevoSubSolic.getEstado());
            pst.setString(i++, nuevoSubSolic.getOrganismo());
            pst.setString(i++, nuevoSubSolic.getObjeto());
            pst.setDouble(i++, nuevoSubSolic.getImporte());
            pst.setDate(i++, nuevoSubSolic.getFecha());
            log.debug("sql = " + query);
            log.debug("PARÁMETROS: Id- " + nuevoSubSolic.getId() + " NumExp- " + nuevoSubSolic.getNumExp() + " Estado- " + nuevoSubSolic.getEstado() + " Organismo- " + nuevoSubSolic.getOrganismo() + " Objeto- " + nuevoSubSolic.getObjeto() + " Importe- " + nuevoSubSolic.getImporte() + " Fecha- " + nuevoSubSolic.getFecha());
            return pst.executeUpdate() > 0;
        } catch (Exception ex) {
            log.debug("Se ha producido un error al insertar una nueva SubSolic" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    /**
     * Operacion que modifica registro
     *
     * @param datModif
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarSubSolic(SubSolicVO datModif, Connection con) throws Exception {
        PreparedStatement pst = null;
        String query = "";
        try {
            int i = 1;
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_SUBSOLIC, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " SET ESTADO=?, ORGANISMO=?, OBJETO=?, IMPORTE=?, FECHA=? WHERE ID=? AND NUM_EXP=?";
            pst = con.prepareStatement(query);
            pst.setString(i++, datModif.getEstado());
            pst.setString(i++, datModif.getOrganismo());
            pst.setString(i++, datModif.getObjeto());
            pst.setDouble(i++, datModif.getImporte());
            pst.setDate(i++, datModif.getFecha());
            pst.setInt(i++, datModif.getId());
            pst.setString(i++, datModif.getNumExp());
            log.debug("sql = " + query);
            log.debug("PARÁMETROS: estado- " + datModif.getEstado() + " Organismo- " + datModif.getOrganismo() + " Objeto- " + datModif.getObjeto() + " Importe- " + datModif.getImporte() + " Fecha- " + datModif.getFecha() + " Id- " + datModif.getId() + " NumExp- " + datModif.getNumExp());
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.debug("Se ha producido un error al modificar una SubSolic - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    /**
     *
     * @param des_cod
     * @param con
     * @return
     * @throws Exception
     */
    public List<DesplegableVO> getValoresDesplegables(String des_cod, Connection con) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<DesplegableVO> lista = new ArrayList<DesplegableVO>();
        DesplegableVO valoresDesplegable = new DesplegableVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_E_DES_VAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD=? order by DES_NOM";
            pst = con.prepareStatement(query);
            log.debug("sql = " + query);
            pst.setString(1, des_cod);
            log.debug("PARÁMETROS: " + des_cod);
            rs = pst.executeQuery();

            while (rs.next()) {
                valoresDesplegable = (DesplegableVO) MeLanbide67MappingUtils.getInstance().map(rs, DesplegableVO.class
                );
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando valores de desplegable : " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }
}
