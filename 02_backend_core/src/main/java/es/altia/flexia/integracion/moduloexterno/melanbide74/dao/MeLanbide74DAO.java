/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide74.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide74.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide74.util.ConstantesMeLanbide74;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Kepa
 */
public class MeLanbide74DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide74DAO.class);

    //Instancia
    private static MeLanbide74DAO instance = null;

    private MeLanbide74DAO() {
    }

    /**
     *
     * @return
     */
    public static MeLanbide74DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide74DAO.class) {
                instance = new MeLanbide74DAO();
            }
        }
        return instance;
    }

    /**
     *
     * @param codDomicilio
     * @param con
     * @return
     * @throws Exception
     */
    public String dameCodigoViaDomicilio(int codDomicilio, Connection con) throws Exception {
        log.info("---- ENTRA en dameCodigoViaDAO");
        String codigo = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        try {
            sql = "select TVI_ABR,TVI_COD from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_VIA, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + "	where TVI_COD in"
                    + "	(select DNN_TVI from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_DOMICILIO, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + "	where DNN_DOM = '" + codDomicilio + "')";
            log.debug("SQL: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("TVI_COD") != 0) {
                    codigo = rs.getString("TVI_ABR");
                }
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando el codigo del Tipo de Via. ", e);

        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return codigo;
    }

    /**
     *
     * @param codigoVia
     * @param con
     * @return
     * @throws Exception
     */
    public boolean coincideCodigo(String codigoVia, Connection con) throws Exception {
        log.info("---- ENTRA en coincideCodigo - DAO");
        Statement st = null;
        ResultSet rs = null;
        String sql = null;

        int resultado = -1;
        try {
            sql = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_COD_VIAS, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " where CODIGO='" + codigoVia + "'";
            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getInt(1);
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si existe el codigo " + codigoVia + " ", e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return resultado > 0;
    }

    /**
     * comprueba si existe un campo documento de tramite
     *
     * @param numExpediente
     * @param codTramite
     * @param ocuTramite
     * @param codCampo
     * @param con
     * @return
     * @throws SQLException
     */
    public boolean existeDocumentoTFIT(String numExpediente, int codTramite, int ocuTramite, String codCampo, Connection con) throws Exception {
        log.info("---- ENTRA en existeDocumentoTFIT");
        Statement st = null;
        ResultSet rs = null;
        int resultado = 0;
        String sql = "";
        try {
            sql = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TFIT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " where tfit_num = '" + numExpediente + "'"
                    + " and tfit_tra = " + codTramite
                    + " and tfit_ocu = " + ocuTramite
                    + " and tfit_cod = '" + codCampo + "'";
            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getInt(1);
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si existe eld documento " + codCampo + " de la tabla E_TFIT del expediente " + numExpediente, ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return resultado > 0;
    }

    /**
     * graba un campo documento de tramite
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codCampo
     * @param pdf
     * @param mime
     * @param nombreFichero
     * @param tamanhoFichero
     * @param origen
     * @param con
     * @throws Exception
     */
    public void insertarDocumentoTFIT(int codOrganizacion, String codProcedimiento, String ejercicio, String numExpediente, int codTramite, int ocurrenciaTramite, String codCampo, byte[] pdf, String mime, String nombreFichero, int tamanhoFichero, String origen, Connection con) throws Exception {
        log.info("---- ENTRA en insertarDocumentoTFIT");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "INSERT into " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TFIT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " (TFIT_MUN, TFIT_PRO, TFIT_EJE, TFIT_NUM, TFIT_TRA, TFIT_OCU, TFIT_COD, TFIT_VALOR, TFIT_MIME, "
                    + " TFIT_NOMFICH, TFIT_TAMANHO, TFIT_ORIGEN)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql insertarDocumentoTFIT = " + query);
            log.debug(numExpediente + " Trám= " + codTramite + " Ocu= " + ocurrenciaTramite);
            log.debug("Campo = " + codCampo + " FICHERO" + nombreFichero + " tamaño: " + tamanhoFichero);

            ps = con.prepareStatement(query);
            ps.setInt(1, codOrganizacion);
            ps.setString(2, codProcedimiento);
            ps.setString(3, ejercicio);
            ps.setString(4, numExpediente);
            ps.setInt(5, codTramite);
            ps.setInt(6, ocurrenciaTramite);
            ps.setString(7, codCampo);
            ps.setBytes(8, pdf);
            ps.setString(9, mime);
            ps.setString(10, nombreFichero);
            ps.setInt(11, tamanhoFichero);
            ps.setString(12, origen);
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            log.error("Se ha producido un error al insertar el documento ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codCampo
     * @param nombreFichero
     * @param oid
     * @param con
     * @return
     * @throws Exception
     */
    public boolean grabarCartaDokusi(int codOrg, String numExp, String codTramite, String ocurrenciaTramite, String codCampo, String nombreFichero, String oid, Connection con) throws Exception {
        log.info("---- ENTRA en grabarCartaDokusi");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        boolean grabada = false;
        int resultado = 0;
        try {
            String query = "INSERT into " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TFIT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " (TFIT_MUN, TFIT_PRO, TFIT_EJE, TFIT_NUM, TFIT_TRA, TFIT_OCU, TFIT_COD, TFIT_MIME, TFIT_NOMFICH, TFIT_ORIGEN)"
                    + " values (?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql grabarCartaDokusi = " + query);
            log.debug(numExp + " Trám= " + codTramite + " Ocu= " + ocurrenciaTramite);
            log.debug("Campo = " + codCampo + " FICHERO" + nombreFichero);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide74.BARRA)[1]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide74.BARRA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, codTramite);
            ps.setString(i++, ocurrenciaTramite);
            ps.setString(i++, codCampo);
            ps.setString(i++, "application/pdf");
            ps.setString(i++, nombreFichero);
            ps.setString(i++, ConstantesMeLanbide74.ORIGEN_FLEXIA);
            resultado = ps.executeUpdate();
            if (resultado > 0) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_RELDOC_CST, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                        + " (RELDOC_MUN, RELDOC_EJE, RELDOC_PRO, RELDOC_NUM, RELDOC_TRA, RELDOC_OCU, RELDOC_COD, RELDOC_OID) values (?, ?, ?, ?, ?, ?, ?, ?)";
                log.debug("sql = " + query);
                ps1 = con.prepareStatement(query);
                i = 1;
                ps1.setInt(i++, codOrg);
                ps1.setString(i++, numExp.split(ConstantesMeLanbide74.BARRA)[0]);
                ps1.setString(i++, numExp.split(ConstantesMeLanbide74.BARRA)[1]);
                ps1.setString(i++, numExp);
                ps1.setString(i++, codTramite);
                ps1.setString(i++, ocurrenciaTramite);
                ps1.setString(i++, codCampo);
                ps1.setString(i++, oid);
                resultado = ps1.executeUpdate();
                if (resultado > 0) {
                    grabada = true;
                } else {
                    log.error("Se ha producido un error al insertar la relación del documento en DOKUSI");
                }
            } else {
                log.error("Se ha producido un error al insertar el documento ");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al insertar el documento ", ex);
            throw new Exception(ex);
        } finally {
            if (ps1 != null) {
                ps1.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return grabada;
    }

    /**
     *
     * @param numExpediente
     * @param codTramite
     * @param ocuTramite
     * @param codCampoFichero
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public int borrarCartaPago(String numExpediente, int codTramite, int ocuTramite, String codCampoFichero, Connection con) throws Exception {
        log.info("---- ENTRA en borrarCartaPago");
        Statement st = null;
        String query = null;
        try {
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TFIT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " where TFIT_NUM='" + numExpediente + "'"
                    + " and TFIT_TRA ='" + codTramite + "'"
                    + " and TFIT_OCU ='" + ocuTramite + "'"
                    + " and TFIT_COD='" + codCampoFichero + "'";

            log.debug("sql borrarCartaPago = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando la carta de pago " + codCampoFichero + " del expediente : " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    } // borrarCartaPago

    /**
     * recupera el valor de un campo texto de tramite
     *
     * @param codOrganizacion
     * @param procedimiento
     * @param numExp
     * @param ejercicio
     * @param tramite
     * @param ocurrencia
     * @param codigoCampo
     * @param con
     * @return
     * @throws Exception
     */
    public String getValorCampoTextoTramite(int codOrganizacion, String procedimiento, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        log.info("---- ENTRA en getValorCampoTextoTramite");
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TXTT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TXTT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " where TXTT_MUN = '" + codOrganizacion + "' and TXTT_EJE = '" + ejercicio + "'"
                    + " and TXTT_NUM = '" + numExp + "' and TXTT_TRA ='" + tramite + "' and TXTT_COD = '" + codigoCampo + "'"
                    + " and txtt_ocu=" + ocurrencia;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valor = rs.getString("TXTT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario texto " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
//        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }//get valor campo texto tramite

    /**
     * graba el valor de un campo texto de tramite
     *
     * @param codOrganizacion
     * @param procedimiento
     * @param ejercicio
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codigoCampo
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
   /* public int guardarValorCampoTextoTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, Connection con) throws Exception {
        log.info("==> entra en guardarValorCampoTextoTramite DAO");
        Statement st = null;
        int result = 0;
        try {
            boolean nuevo = false;
            if (this.getValorCampoTextoTramite(codOrganizacion, procedimiento, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TXTT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
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
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TXTT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                        + " set TXTT_VALOR = '" + valor + "'"
                        + " where TXTT_MUN = '" + codOrganizacion + "'"
                        + " and TXTT_EJE = " + ejercicio
                        + " and TXTT_NUM = '" + numExp + "'"
                        + " and TXTT_TRA = " + tramite
                        + " and TXTT_OCU = " + ocurrencia
                        + " and TXTT_COD = '" + codigoCampo + "'";
            }
            log.debug("sql = " + query);

            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.debug("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception e) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo texto " + codigoCampo + " para el expediente " + numExp, e);
            throw new Exception(e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    } // guardat valor txtt
*/
    /**
     * recupera el valor de un campo numerico de tramite
     *
     * @param codOrganizacion
     * @param numExp
     * @param ejercicio
     * @param tramite
     * @param ocurrencia
     * @param codigoCampo
     * @param con
     * @return
     * @throws Exception
     */
    public BigDecimal getValorCampoNumericoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try {
            String query = null;
            query = "select TNUT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TNUT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " where TNUT_MUN = '" + codOrganizacion + "' and TNUT_EJE = '" + ejercicio + "'"
                    + " and TNUT_NUM = '" + numExp + "' and TNUT_TRA ='" + tramite + "' and TNUT_COD = '" + codigoCampo + "'"
                    + " and tnut_ocu=" + ocurrencia;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNUT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
//        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }// get numerico tramite

    /**
     * graba el valor de un campo numerico de tramite
     *
     * @param codOrganizacion
     * @param procedimiento
     * @param ejercicio
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codigoCampo
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
    public int guardarValorCampoNumericoTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, BigDecimal valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            boolean nuevo = false;
            if (this.getValorCampoNumericoTramite(codOrganizacion, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TNUT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                        + " (TNUT_MUN, TNUT_PRO, TNUT_EJE, TNUT_NUM, TNUT_TRA,TNUT_OCU, TNUT_COD, TNUT_VALOR)"
                        + " values(" + codOrganizacion
                        + ", '" + procedimiento + "'"
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", " + tramite
                        + ", " + ocurrencia
                        + ", '" + codigoCampo + "'"
                        + ", " + valor
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TNUT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                        + " set TNUT_VALOR = " + valor
                        + " where TNUT_MUN = '" + codOrganizacion + "'"
                        + " and TNUT_EJE = " + ejercicio
                        + " and TNUT_NUM = '" + numExp + "'"
                        + " and TNUT_TRA = " + tramite
                        + " and TNUT_OCU = " + ocurrencia
                        + " and TNUT_COD = '" + codigoCampo + "'";
            }
            log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.debug("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }// guardat valor numerico tramite

    /**
     * recupera el valor de un campo numerico calculado de tramite
     *
     * @param codOrganizacion
     * @param numExp
     * @param ejercicio
     * @param tramite
     * @param ocurrencia
     * @param codigoCampo
     * @param con
     * @return
     * @throws Exception
     */
    public BigDecimal getValorCampoNumCalculadoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try {
            String query = null;
            query = "select TNUCT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TNUCT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " where TNUCT_MUN = '" + codOrganizacion + "' and TNUCT_EJE = '" + ejercicio + "'"
                    + " and TNUCT_NUM = '" + numExp + "' and TNUCT_TRA ='" + tramite + "' and TNUCT_COD = '" + codigoCampo + "'"
                    + " and TNUCT_ocu=" + ocurrencia;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNUCT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
//        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }// get numerico tramite

    /**
     * graba el valor de un campo numerico calculado de tramite
     *
     * @param codOrganizacion
     * @param procedimiento
     * @param ejercicio
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codigoCampo
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
    public int guardarValorCampoNumCalculadoTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, BigDecimal valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            boolean nuevo = false;
            if (this.getValorCampoNumCalculadoTramite(codOrganizacion, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TNUCT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                        + " (TNUCT_MUN, TNUCT_PRO, TNUCT_EJE, TNUCT_NUM, TNUCT_TRA,TNUCT_OCU, TNUCT_COD, TNUCT_VALOR)"
                        + " values(" + codOrganizacion
                        + ", '" + procedimiento + "'"
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", " + tramite
                        + ", " + ocurrencia
                        + ", '" + codigoCampo + "'"
                        + ", " + valor
                        + ")";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_E_TNUCT, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                        + " set TNUCT_VALOR = " + valor
                        + " where TNUCT_MUN = '" + codOrganizacion + "'"
                        + " and TNUCT_EJE = " + ejercicio
                        + " and TNUCT_NUM = '" + numExp + "'"
                        + " and TNUCT_TRA = " + tramite
                        + " and TNUCT_OCU = " + ocurrencia
                        + " and TNUCT_COD = '" + codigoCampo + "'";
            }
            log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.debug("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo numerico calculado " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }// guarda valor numerico tramite

    /**
     * 
     * @param codOrg
     * @param numExp
     * @param codTramite
     * @param ocuTramite
     * @param codCampoCartaPago
     * @param con
     * @return
     * @throws Exception 
     */
    public int borrarRelacionDokusiCST(int codOrg, String numExp, int codTramite, int ocuTramite, String codCampoCartaPago, Connection con) throws Exception {
        log.info("borrarRelacionDokusiCST BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            if (existeRelacionDokusiCST(codOrg, numExp, codTramite, ocuTramite, codCampoCartaPago, con)) {
                query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_RELDOC_CST, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                        + " where RELDOC_MUN = ? and RELDOC_EJE = ?  and RELDOC_PRO = ? and RELDOC_NUM = ? and RELDOC_TRA = ? and RELDOC_OCU = ?  and RELDOC_COD= ?";
                log.debug("sql = " + query);
                int contador = 1;
                ps = con.prepareStatement(query);
                ps.setInt(contador++, codOrg);
                ps.setString(contador++, numExp.split(ConstantesMeLanbide74.BARRA)[0]);
                ps.setString(contador++, numExp.split(ConstantesMeLanbide74.BARRA)[1]);
                ps.setString(contador++, numExp);
                ps.setInt(contador++, codTramite);
                ps.setInt(contador++, ocuTramite);
                ps.setString(contador++, codCampoCartaPago);
                return ps.executeUpdate();
            } else {
                return 1;
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando de RELDOC_DOCCST la carta de pago " + codCampoCartaPago + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean existeRelacionDokusiCST(int codOrg, String numExp, int codTramite, int ocuTramite, String codCampo, Connection con) throws Exception {
        log.info("existeRelacionDokusiCST BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = -1;
        String query = "";
        try {
            query = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide74.TABLA_RELDOC_CST, ConstantesMeLanbide74.FICHERO_PROPIEDADES)
                    + " where RELDOC_MUN = ? and RELDOC_EJE = ? and RELDOC_PRO = ? and RELDOC_NUM = ? and RELDOC_TRA = ? and RELDOC_OCU = ? and RELDOC_COD = ?";
            log.debug("sql = " + query);
            log.debug(codOrg + " - " + numExp + " - " + codCampo);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide74.BARRA )[0]);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide74.BARRA )[1]);
            ps.setString(contador++, numExp);
            ps.setInt(contador++, codTramite);
            ps.setInt(contador++, ocuTramite);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            //    return resultado > 0;
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si el documento " + codCampo + "  del expediente " + numExp + " está en DOKUSI", e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return resultado > 0;
    }//existe documento

    /**
     *
     * @param ejerSubvencion
     * @param codProcedimiento
     * @param con
     * @return
     * @throws Exception
     */
    public String getCodigoSubvencionEika(String ejerSubvencion, String codProcedimiento, Connection con) throws Exception {
        log.info("getCodigoSubvencionEika - " + codProcedimiento + " - " + ejerSubvencion);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String codigoSubv = null;
        try {
            query = "select REI_PRO_DEUDA from MELANBIDE64_PROC_REINT where REI_EJE = ? and REI_PRO = ?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, ejerSubvencion);
            ps.setString(i++, codProcedimiento);
            rs = ps.executeQuery();
            if (rs.next()) {
                codigoSubv = rs.getString("REI_PRO_DEUDA");
            }
        } catch (SQLException e) {
            log.error("Excepcion - getCodigoSubvencionEika : " + e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(" getCodigoSubvencionEika - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return codigoSubv;
    }
}
