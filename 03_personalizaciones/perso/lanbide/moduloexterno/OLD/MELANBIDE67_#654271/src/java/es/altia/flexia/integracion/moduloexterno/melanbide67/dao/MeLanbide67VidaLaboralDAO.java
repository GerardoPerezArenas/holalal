/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.RegistroVidaLaboralVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author pablo.bugia
 */
public class MeLanbide67VidaLaboralDAO {
    //Logger

    private static final Logger log = Logger.getLogger(MeLanbide67VidaLaboralDAO.class);

    //Instancia
    private static MeLanbide67VidaLaboralDAO instance = null;

    private MeLanbide67VidaLaboralDAO() {
    }

    public static MeLanbide67VidaLaboralDAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide67VidaLaboralDAO.class) {
                instance = new MeLanbide67VidaLaboralDAO();
            }
        }
        return instance;
    }

    public List<RegistroVidaLaboralVO> getRegistrosVidaLaboral(final String DocIdentificacion,
            final Connection con) throws Exception {
        final List<RegistroVidaLaboralVO> result = new ArrayList<RegistroVidaLaboralVO>();
        Statement st = null;
        ResultSet rs = null;

        final String query = "SELECT * FROM "
                + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_INTEROP_VIDALABORAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                + " WHERE DOCUMENTACION = '" + DocIdentificacion + "'";

        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }

        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                result.add(new RegistroVidaLaboralVO(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6),
                        rs.getString(7), rs.getDate(8), rs.getString(9), rs.getInt(10),
                        rs.getInt(11), rs.getInt(12), rs.getInt(13), rs.getInt(14),
                        rs.getInt(15), rs.getInt(16), rs.getInt(17), rs.getInt(18),
                        rs.getString(19), rs.getString(20), rs.getString(21), rs.getString(22),
                        rs.getString(23), rs.getDate(24), rs.getDate(25), rs.getDate(26),
                        rs.getString(27), rs.getString(28), rs.getString(29), rs.getString(30),
                        rs.getInt(31), rs.getFloat(32), rs.getInt(33)));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el registro laboral con el document " + DocIdentificacion + " ", ex);
            throw new SQLException(ex);
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
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }

    public RegistroVidaLaboralVO getRegistroVidaLaboral(final String idPeticion,
            final String numExp, final Connection con) throws Exception {
        RegistroVidaLaboralVO result = null;
        Statement st = null;
        ResultSet rs = null;

        final String query = "SELECT * FROM "
                + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_INTEROP_VIDALABORAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                + " WHERE IDPETICION = '" + idPeticion + "'";

        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }

        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                result = new RegistroVidaLaboralVO(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6),
                        rs.getString(7), rs.getDate(8), rs.getString(9), rs.getInt(10),
                        rs.getInt(11), rs.getInt(12), rs.getInt(13), rs.getInt(14),
                        rs.getInt(15), rs.getInt(16), rs.getInt(17), rs.getInt(18),
                        rs.getString(19), rs.getString(20), rs.getString(21), rs.getString(22),
                        rs.getString(23), rs.getDate(24), rs.getDate(25), rs.getDate(26),
                        rs.getString(27), rs.getString(28), rs.getString(29), rs.getString(30),
                        rs.getInt(31), rs.getFloat(32), rs.getInt(33));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el registro laboral con la idPeticion " + idPeticion + " para el expediente " + numExp, ex);
            throw new SQLException(ex);
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
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }

    public int insertarRegistroVidaLaboral(final RegistroVidaLaboralVO registro, final String numExp,
            final Connection con) throws Exception {
        Statement st = null;
        final SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
        int result = 0;
        try {
            final String query = "INSERT INTO "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_INTEROP_VIDALABORAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " (IDPETICION, TIPODOCUMENTACION, DOCUMENTACION, FECHADESDE, FECHAHASTA, NUMEROSITUACIONES, "
                    + "NUMEROAFILIACION_L, FECHANACIMIENTO, TRANSFDERECHOSCEE, RESUMEN_CONPL_TOTALDIASALTA, "
                    + "RESUMEN_CONPL_DIASPLURIEMPLEO, RESUMEN_CONPL_ANIOSALTA, RESUMEN_CONPL_MESESALTA, "
                    + "RESUMEN_CONPL_DIASALTA, RESUMEN_TOTALDIASALTA, RESUMEN_ANIOSALTA, RESUMEN_MESESALTA, "
                    + "RESUMEN_DIASALTA, NUMEROAFILIACION, REGIMEN, EMPRESA, CODCUENTACOT, PROVINCIA, "
                    + "FECHAALTA, FECHAEFECTOS, FECHABAJA, CONTRATOTRABAJO, DESCCONTRATOTRABAJO, CONTRATOTPARCIAL, "
                    + "GRUPOCOTIZACION, DIASALTA, DIASALTA_CALCULADOS, ORDEN_SITLAB) VALUES ("
                    + "'" + registro.getIdPeticion() + "', '" + registro.getTipoDocumentacion() + "', "
                    + "'" + registro.getDocumentacion() + "', "
                    + registro.getFechaDesde() != null ? "TO_DATE('" + format.format(registro.getFechaDesde()) + "', 'dd/mm/yyyy'), " : "null, "
                    + registro.getFechaHasta() != null ? "TO_DATE('" + format.format(registro.getFechaHasta()) + "', 'dd/mm/yyyy'), " : "null, "
                    + registro.getNumeroSituaciones() + ", "
                    + "'" + registro.getNumeroAfiliacionL() + "', '" + registro.getFechaNacimiento() + "', "
                    + "'" + registro.getTransfDerechosCEE() + "', " + registro.getResumenConplTotalDiasAlta() + ", "
                    + registro.getResumenConplDiasPluriempleo() + ", " + registro.getResumenConplAniosAlta() + ", "
                    + registro.getResumenConplMesesAlta() + ", " + registro.getResumenConplDiasAlta() + ", "
                    + registro.getResumenTotalDiasAlta() + ", " + registro.getResumenAniosAlta() + ", "
                    + registro.getResumenMesesAlta() + ", " + registro.getResumenDiasAlta() + ", '"
                    + registro.getNumeroAfiliacion() + "', '" + registro.getRegimen() + "', '"
                    + registro.getEmpresa() + "', '" + registro.getCodCuentaCot() + "', '"
                    + registro.getProvincia() + "', '"
                    + registro.getFechaAlta() != null ? "TO_DATE('" + format.format(registro.getFechaAlta()) + "', 'dd/mm/yyyy'), " : "null, "
                    + registro.getFechaEfectos() != null ? "TO_DATE('" + format.format(registro.getFechaEfectos()) + "', 'dd/mm/yyyy'), " : "null, "
                    + registro.getFechaBaja() != null ? "TO_DATE('" + format.format(registro.getFechaBaja()) + "', 'dd/mm/yyyy'), " : "null, "
                    + "'" + registro.getContratoTrabajo() + "', '" + registro.getDescContratoTrabajo() + "', "
                    + "'" + registro.getContratoTParcial() + "', '" + registro.getGrupoCotizacion() + "', "
                    + registro.getDiasAlta() + ", " + registro.getDiasAltaCalculados() + ", " + registro.getOrdenSitLab()
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql insertarRegistroVidaLaboral= " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (final SQLException ex) {
            log.error(registro.getDocumentacion() + "Se ha producido un error grabando el registro laboral con el documento "
                    + registro.getDocumentacion() + " para el expediente " + numExp, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }

    public int actualizarRegistroVidaLaboral(final RegistroVidaLaboralVO registro, final String idPeticion,
            final String documentacion, final String numExp, final Connection con) throws Exception {
        Statement st = null;
        final SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
        int result = 0;
        try {
            final String query = "UPDATE  "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_INTEROP_VIDALABORAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " SET TIPODOCUMENTACION = " + registro.getTipoDocumentacion() + ", "
                    + "FECHADESDE = " + registro.getFechaDesde() != null ? "TO_DATE('" + format.format(registro.getFechaDesde()) + "', 'dd/mm/yyyy'), " : "null, "
                    + "FECHAHASTA = " + registro.getFechaHasta() != null ? "TO_DATE('" + format.format(registro.getFechaHasta()) + "', 'dd/mm/yyyy'), " : "null, "
                    + "NUMEROSITUACIONES = " + registro.getNumeroSituaciones() + ", "
                    + "NUMEROAFILIACION_L = '" + registro.getNumeroAfiliacionL() + "', "
                    + "FECHANACIMIENTO = " + registro.getFechaNacimiento() != null ? "TO_DATE('" + format.format(registro.getFechaNacimiento()) + "', 'dd/mm/yyyy'), " : "null, "
                    + "TRANSFDERECHOSCEE = '" + registro.getTransfDerechosCEE() + "', "
                    + "RESUMEN_CONPL_TOTALDIASALTA = " + registro.getResumenConplDiasAlta() + ", "
                    + "RESUMEN_CONPL_DIASPLURIEMPLEO = " + registro.getResumenConplDiasPluriempleo() + ", "
                    + "RESUMEN_CONPL_ANIOSALTA = " + registro.getResumenConplAniosAlta() + ", "
                    + "RESUMEN_CONPL_MESESALTA = " + registro.getResumenConplMesesAlta() + ", "
                    + "RESUMEN_CONPL_DIASALTA = " + registro.getResumenConplDiasAlta() + ", "
                    + "RESUMEN_TOTALDIASALTA = " + registro.getResumenTotalDiasAlta() + ", "
                    + "RESUMEN_ANIOSALTA = " + registro.getResumenAniosAlta() + ", "
                    + "RESUMEN_MESESALTA = " + registro.getResumenMesesAlta() + ", "
                    + "RESUMEN_DIASALTA = " + registro.getResumenDiasAlta() + ", "
                    + "NUMEROAFILIACION ='" + registro.getNumeroAfiliacion() + "', "
                    + "REGIMEN = '" + registro.getRegimen() + "', "
                    + "EMPRESA = '" + registro.getEmpresa() + "', "
                    + "CODCUENTACOT ='" + registro.getCodCuentaCot() + "', "
                    + "PROVINCIA = '" + registro.getProvincia() + "', "
                    + "FECHAALTA = " + registro.getFechaAlta() != null ? "TO_DATE('" + format.format(registro.getFechaAlta()) + "', 'dd/mm/yyyy'), " : "null, "
                    + "FECHAEFECTOS = " + registro.getFechaEfectos() != null ? "TO_DATE('" + format.format(registro.getFechaEfectos()) + "', 'dd/mm/yyyy'), " : "null, "
                    + "FECHABAJA = " + registro.getFechaBaja() != null ? "TO_DATE('" + format.format(registro.getFechaBaja()) + "', 'dd/mm/yyyy'), " : "null, "
                    + "CONTRATOTRABAJO = '" + registro.getContratoTrabajo() + "', "
                    + "DESCCONTRATOTRABAJO = '" + registro.getDescContratoTrabajo() + "', "
                    + "CONTRATOTPARCIAL = '" + registro.getContratoTParcial() + "', "
                    + "GRUPOCOTIZACION = '" + registro.getGrupoCotizacion() + "', "
                    + "DIASALTA = " + registro.getDiasAlta() + ", "
                    + "DIASALTA_CALCULADOS = " + registro.getDiasAltaCalculados() + ", "
                    + "ORDEN_SITLAB = " + registro.getOrdenSitLab() + " "
                    + "WHERE IDPETICION = '" + idPeticion + "' AND DOCUMENTACION = '" + registro.getDocumentacion() + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql actualizarRegistroVidaLaboral= " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (final SQLException ex) {
            log.error("Se ha producido un error actualizando el registro laboral de documentación " + documentacion + " para el expediente " + numExp, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new SQLException(e);
            }
        }
        return result;
    }

    public int eliminarRegistroVidaLaboral(final String idPeticion,
            final String documentacion, final String numExp, final Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            final String query = "DELETE FROM  "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_INTEROP_VIDALABORAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " WHERE IDPETICION = '" + idPeticion + "' AND DOCUMENTACION = '" + documentacion + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql eliminarRegistroVidaLaboral= " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (final SQLException ex) {
            log.error("Se ha producido un error eliminando el registro laboral de documentación " + documentacion + " para el expediente " + numExp, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new SQLException(e);
            }
        }
        return result;
    }

    public String getNombreProvincia(final String codigoProvincia,
            final Connection con) throws Exception {
        String nombreProvincia = null;
        Statement st = null;
        ResultSet rs = null;

        final String query = "SELECT DS_O FROM "
                + ConfigurationParameter.getParameter(ConstantesMeLanbide67.VISTA_PROVINCIA, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                + " WHERE ID = '" + codigoProvincia + "'";

        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }

        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                nombreProvincia = rs.getString(1);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el nombre de la provincia de " + codigoProvincia, ex);
            throw new SQLException(ex);
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
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return nombreProvincia;
    }
}
