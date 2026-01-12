/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide50.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide50.manager.MeLanbide50Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConstantesMeLanbide50;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.MeLanbide50Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.MeLanbide50MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.MeLanbide50Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.CampoSuplementario;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Domicilio;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Tercero;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.capacidad.CapacidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.direccion.DireccionInteresadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.disponibilidad.DisponibilidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.documentos.DocumentosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.dotacion.DotacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.espacios.EspacioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.identificacionesp.IdentificacionEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.material.MaterialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.servicios.ServiciosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.ubicacion.UbicacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide50DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide50DAO.class);

    //Instancia
    private static MeLanbide50DAO instance = null;

    private MeLanbide50DAO() {

    }

    /**
     *
     * @return instancia
     */
    public static MeLanbide50DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide50DAO.class) {
                instance = new MeLanbide50DAO();
            }
        }
        return instance;
    }

    // Metodos de RecupEracion de datos
    /**
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception
     */
    public List<EspecialidadesVO> getDatosEspecialidades(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EspecialidadesVO> listEsp = new ArrayList<EspecialidadesVO>();
        EspecialidadesVO Especialidad = new EspecialidadesVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where ESP_NUM = '" + numExpediente + "' ORDER BY ESP_DENOM";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Especialidad = (EspecialidadesVO) MeLanbide50MappingUtils.getInstance().map(rs, EspecialidadesVO.class);
                listEsp.add(Especialidad);
                Especialidad = new EspecialidadesVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listEsp;
    }

    /**
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception
     */
    public List<EspecialidadesVO> getDatosEspecialidadesAcreditadas(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EspecialidadesVO> listEsp = new ArrayList<EspecialidadesVO>();
        EspecialidadesVO Especialidad = new EspecialidadesVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where ESP_NUM = '" + numExpediente + "' and ESP_ACRED=0 ORDER BY ESP_DENOM";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Especialidad = (EspecialidadesVO) MeLanbide50MappingUtils.getInstance().map(rs, EspecialidadesVO.class);
                listEsp.add(Especialidad);
                Especialidad = new EspecialidadesVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listEsp;
    }

    /**
     *
     * Función que obtiene de la base de datos de la tabla E_DES_VAL los motivos
     * de denegacion para el campo desplegable de MDRF
     *
     * @param con: conexion
     * @return
     * @throws java.lang.Exception
     */
    public List<ValorCampoDesplegableModuloIntegracionVO> getDatosMotivosDenegacion(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ValorCampoDesplegableModuloIntegracionVO> listMotDeneg = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        ValorCampoDesplegableModuloIntegracionVO DesplegableMotDeneg = new ValorCampoDesplegableModuloIntegracionVO();
        try {
            String query = null;
            query = "select DES_VAL_COD as codigo, DES_NOM as descripcion from E_DES_VAL where DES_COD='MDRF' ORDER BY DES_VAL_COD";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {

                //              String texto = MeLanbide50Manager.getInstance().getDescripcionDesplegableByIdioma(4, query);
                DesplegableMotDeneg.setCodigo(rs.getString("codigo"));
                DesplegableMotDeneg.setDescripcion(rs.getString("descripcion"));
                listMotDeneg.add(DesplegableMotDeneg);
                DesplegableMotDeneg = new ValorCampoDesplegableModuloIntegracionVO();
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los motivos de denegacion", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listMotDeneg;
    }

    /**
     *
     * @param idEspecialidad
     * @param con
     * @return
     * @throws Exception
     */
    public List<String> getDatosEspecialidadMotDeneg(String idEspecialidad, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<String> listMotDeneg = new ArrayList<String>();
        try {
            StringBuilder query = new StringBuilder("select * from ");
            query.append(ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES_MOTDENEG, ConstantesMeLanbide50.FICHERO_PROPIEDADES));
            query.append(" where ID = ");
            query.append(idEspecialidad);
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                listMotDeneg.add(rs.getString("ESP_MOT_DENEG"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listMotDeneg;
    }

    /**
     *
     * Función que obtiene el número de censo SILICOI de base de datos
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param corrSubservicio
     * @param con
     * @return String con el numero de censo SILICOI
     * @throws java.lang.Exception
     */
    public String getNumeroCensoSILICOI(String codCentro, String codUbicacion, String corrSubservicio, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String censo = "";
        try {
            String query = null;
            query = "select NCENSO_SILCOI_CONCAT from FLBGEN.VW_CENTROS_REG "
                    + " where GEN_CEN_COD_CENTRO  = '" + codCentro + "'"
                    + " AND GEN_CEN_COD_UBIC = '" + codUbicacion + "'"
                    + " AND CEN_CENTRO_SUBSERVIC_CORR = '" + corrSubservicio + "'";
            log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                censo = rs.getString("NCENSO_SILCOI_CONCAT");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return censo;
    }

    /**
     *
     * Función que llama al dao para obtener el número de censo de un centro
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param con: conexión
     * @return
     * @throws java.lang.Exception
     */
    public String getNumeroCensoLanbide(String codCentro, String codUbicacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String censo = "";
        try {
            String query = null;
            query = "select LAN_NUM_REG_AUT from FLBGEN.LAN_CENTROS_SERVICIOS "
                    + " where GEN_CEN_COD_CENTRO  = '" + codCentro + "' AND GEN_CEN_COD_UBIC = '" + codUbicacion + "' AND LAN_UBIC_SERVICIO_LMV='314'";
            log.debug("sql getNumeroCensoLanbide = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                censo = rs.getString("LAN_NUM_REG_AUT");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return censo;
    }

    /**
     *
     * @param numExpediente
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public EspecialidadesVO getEspecialidadPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        EspecialidadesVO Especialidad = new EspecialidadesVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where ESP_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY ESP_DENOM";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Especialidad = (EspecialidadesVO) MeLanbide50MappingUtils.getInstance().map(rs, EspecialidadesVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando EspecialidaD Especifica por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return Especialidad;
    }

    /**
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception
     */
    public List<ServiciosVO> getDatosServicios(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ServiciosVO> listsServ = new ArrayList<ServiciosVO>();
        ServiciosVO Servicio = new ServiciosVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_SERVICIOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where SER_NUM = '" + numExpediente + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Servicio = (ServiciosVO) MeLanbide50MappingUtils.getInstance().map(rs, ServiciosVO.class);
                listsServ.add(Servicio);
                Servicio = new ServiciosVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Servicios Higienico-Sanitarios ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listsServ;
    }

    /**
     *
     * @param numExpediente
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public ServiciosVO getServicioPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ServiciosVO _datoReturn = new ServiciosVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_SERVICIOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where SER_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY SER_DESC";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (ServiciosVO) MeLanbide50MappingUtils.getInstance().map(rs, ServiciosVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando aseo servicio higienic-sanitario por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return _datoReturn;
    }

    /**
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception
     */
    public List<DisponibilidadVO> getDatosDisponibilidad(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DisponibilidadVO> lista = new ArrayList<DisponibilidadVO>();
        DisponibilidadVO Disponibilidad = new DisponibilidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DISPONRECURSOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where DRE_NUM = '" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Disponibilidad = (DisponibilidadVO) MeLanbide50MappingUtils.getInstance().map(rs, DisponibilidadVO.class);
                lista.add(Disponibilidad);
                Disponibilidad = new DisponibilidadVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Servicios Higienico-Sanitarios ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     *
     * @param numExpediente
     * @param id
     * @param idespsol
     * @param con
     * @return
     * @throws Exception
     */
    public DisponibilidadVO getDisponibilidadPorCodigo(String numExpediente, String id, String idespsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DisponibilidadVO Disponibilidad = new DisponibilidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DISPONRECURSOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where DRE_NUM = '" + numExpediente + "' AND ID=" + id + " AND ID_ESPSOL=" + idespsol + "  ORDER BY DRE_CODCP";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Disponibilidad = (DisponibilidadVO) MeLanbide50MappingUtils.getInstance().map(rs, DisponibilidadVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Disponibilidad de recursos por codigo ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return Disponibilidad;
    }

    /**
     *
     * @param numExpediente
     * @param idespsol
     * @param con
     * @return
     * @throws Exception
     */
    public DisponibilidadVO getDisponibilidadPorCodigoEspSol_NumExp(String numExpediente, Integer idespsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DisponibilidadVO Disponibilidad = new DisponibilidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DISPONRECURSOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where DRE_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idespsol != null ? idespsol : "null") + "  ORDER BY DRE_CODCP";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Disponibilidad = (DisponibilidadVO) MeLanbide50MappingUtils.getInstance().map(rs, DisponibilidadVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Disponibilidad de recursos por codigo de especialidad solicitada y Num Expediente ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return Disponibilidad;
    }

    /**
     *
     * @param numExpediente
     * @param idEpsol
     * @param con
     * @return
     * @throws Exception
     */
    public List<CapacidadVO> getDatosCapacidad(String numExpediente, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<CapacidadVO> lista = new ArrayList<CapacidadVO>();
        CapacidadVO Capacidad = new CapacidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_CAPACIDADINSTALA, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where CAIN_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Capacidad = (CapacidadVO) MeLanbide50MappingUtils.getInstance().map(rs, CapacidadVO.class);
                lista.add(Capacidad);
                Capacidad = new CapacidadVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Capacidad de Instalaciones", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     *
     * @param numExpediente
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public CapacidadVO getCapacidadPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        CapacidadVO _datoReturn = new CapacidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_CAPACIDADINSTALA, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where CAIN_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY CAIN_IDEF";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (CapacidadVO) MeLanbide50MappingUtils.getInstance().map(rs, CapacidadVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando capcidad de instalaciones por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return _datoReturn;
    }

    /**
     *
     * @param numExpediente
     * @param idEpsol
     * @param con
     * @return
     * @throws Exception
     */
    public List<DotacionVO> getDatosDotacion(String numExpediente, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DotacionVO> lista = new ArrayList<DotacionVO>();
        DotacionVO Dotacion = new DotacionVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DOTACION, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where DOT_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Dotacion = (DotacionVO) MeLanbide50MappingUtils.getInstance().map(rs, DotacionVO.class);
                lista.add(Dotacion);
                Dotacion = new DotacionVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Dotacion", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     *
     * @param numExpediente
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public DotacionVO getDotacionPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DotacionVO _datoReturn = new DotacionVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DOTACION, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where DOT_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY DOT_DET";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (DotacionVO) MeLanbide50MappingUtils.getInstance().map(rs, DotacionVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Dotacion de instalaciones por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return _datoReturn;
    }

    /**
     *
     * @param numExpediente
     * @param idEpsol
     * @param con
     * @return
     * @throws Exception
     */
    public List<MaterialVO> getDatosMaterial(String numExpediente, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<MaterialVO> lista = new ArrayList<MaterialVO>();
        MaterialVO Material = new MaterialVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_MATERIALCONSUMO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where MAC_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Material = (MaterialVO) MeLanbide50MappingUtils.getInstance().map(rs, MaterialVO.class);
                lista.add(Material);
                Material = new MaterialVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Material de Consumo", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     *
     * @param numExpediente
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public MaterialVO getMaterialPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        MaterialVO _datoReturn = new MaterialVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_MATERIALCONSUMO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where MAC_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY MAC_DET";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (MaterialVO) MeLanbide50MappingUtils.getInstance().map(rs, MaterialVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Material de consumo por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return _datoReturn;
    }

    /**
     *
     * @param numExpediente
     * @param idEpsol
     * @param con
     * @return
     * @throws Exception
     */
    public List<IdentificacionEspVO> getDatosIdentificacionEsp(String numExpediente, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<IdentificacionEspVO> lista = new ArrayList<IdentificacionEspVO>();
        IdentificacionEspVO identificacionEsp = new IdentificacionEspVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_IDENTIFICACIONESP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where IDE_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                identificacionEsp = (IdentificacionEspVO) MeLanbide50MappingUtils.getInstance().map(rs, IdentificacionEspVO.class);
                lista.add(identificacionEsp);
                identificacionEsp = new IdentificacionEspVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Lista de Identificacion de Especialidad", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     *
     * @param numExpediente
     * @param id
     * @param idespsol
     * @param con
     * @return
     * @throws Exception
     */
    public IdentificacionEspVO getIdentificacionEspPorCodigo(String numExpediente, String id, String idespsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        IdentificacionEspVO Identificacion = new IdentificacionEspVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_IDENTIFICACIONESP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where IDE_NUM = '" + numExpediente + "' AND ID=" + id + " AND ID_ESPSOL=" + idespsol + "  ORDER BY IDE_DENESP";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Identificacion = (IdentificacionEspVO) MeLanbide50MappingUtils.getInstance().map(rs, IdentificacionEspVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Lista de Identificacion de Especialidad ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return Identificacion;
    }

    /**
     *
     * @param numExpediente
     * @param idespsol
     * @param con
     * @return
     * @throws Exception
     */
    public IdentificacionEspVO getIdentificacionEspPorCodigoEspSol_NumExp(String numExpediente, Integer idespsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        IdentificacionEspVO Identificacion = new IdentificacionEspVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_IDENTIFICACIONESP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where IDE_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idespsol != null ? idespsol : "null") + "  ORDER BY IDE_DENESP";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Identificacion = (IdentificacionEspVO) MeLanbide50MappingUtils.getInstance().map(rs, IdentificacionEspVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Lista de Identificacion de Especialidad ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return Identificacion;
    }

    /**
     *
     * @param numExp
     * @param idEpsol
     * @param con
     * @return
     * @throws Exception
     */
    public List<EspacioVO> getDatosEspacio(String numExp, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EspacioVO> listaEspacios = new ArrayList<EspacioVO>();
        EspacioVO espacio = new EspacioVO();
        String query = null;
        try {
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPACIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where NUM_EXP='" + numExp + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null")
                    + " order by ID";

            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                espacio = (EspacioVO) MeLanbide50MappingUtils.getInstance().map(rs, EspacioVO.class);
                listaEspacios.add(espacio);
                espacio = new EspacioVO();
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Lista ESPACIOS", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return listaEspacios;
    }

    /**
     *
     * @param numExp
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public EspacioVO getEspacioPorCodigo(String numExp, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        EspacioVO espacio = new EspacioVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPACIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where NUM_EXP='" + numExp + "' AND ID =" + id;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                espacio = (EspacioVO) MeLanbide50MappingUtils.getInstance().map(rs, EspacioVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando ESPACIO de instalaciones por codigo" + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return espacio;
    }

    /**
     * Recupera una lista con el código y la descripción de los certificados de
     * la tabla CER_CERTIFICADOS
     *
     * @param con
     * @return ArrayList CerCertificadoVO (Solo incluye el código y la
     * descripción)
     * @throws Exception
     */
    public ArrayList<CerCertificadoVO> getCertificados(Connection con) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCertificados() : BEGIN");
        }
        ArrayList<CerCertificadoVO> listaCertificados = new ArrayList<CerCertificadoVO>();
        CerCertificadoVO certificado = new CerCertificadoVO();
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_CER_CERTIFICADOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " ORDER BY DESC_ESPECIALIDAD_C  ASC";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                certificado = (CerCertificadoVO) MeLanbide50MappingUtils.getInstance().map(rs, CerCertificadoVO.class);
                listaCertificados.add(certificado);
                certificado = new CerCertificadoVO();
            }//while(rs.next())
        } catch (Exception e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw new Exception(e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }//try-catch-finally
        log.debug("getCertificados() : END");
        return listaCertificados;
    }//getCertificados

    /**
     *
     * @param codMunicipio
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public Expediente getDatosExpediente(int codMunicipio, String numExp, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Expediente exp = null;

        try {
            String query = "select exp.EXP_PRO PRO, exp.EXP_EJE EJE, exp.EXP_NUM NUM, exp.EXP_MUN MUN, exp.EXP_UOR UOR, exp.EXP_OBS OBS, exp.EXP_ASU ASU,"
                    + " exr.EXR_DEP DEP,"
                    + " res.RES_TDO TDO, res.RES_NTR NTR, res.RES_AUT AUT"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_EXP, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " exp"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_EXR, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " exr"
                    + " on exp.exp_pro = exr.exr_pro and exp.exp_eje = exr.exr_eje and exp.exp_num = exr.exr_num  and exp.exp_mun = exr.exr_mun"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_R_RES, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " res"
                    + " on exr.exr_nre = res.res_num and exr.exr_eje = res.res_eje"
                    + " where exp.EXP_MUN = ? and exp.EXP_NUM = ?";
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setString(2, numExp);
            log.info("sql: " + query);
            rs = st.executeQuery();
            if (rs.next()) {
                exp = (Expediente) MeLanbide50MappingUtils.getInstance().map(rs, Expediente.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return exp;
    }

    // Metodos de Insercion en BBDD
    /**
     *
     * @param nuevEsp
     * @param con
     * @return
     * @throws Exception
     */
    public int crearNuevaEspecialidad(EspecialidadesVO nuevEsp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide50.SEQ_RGCFM_ESPECIALIDADES, ConstantesMeLanbide50.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + "(ID, ESP_NUM, ESP_CODCP, ESP_DENOM, ESP_PRESE, ESP_TELEF, ESP_ACRED) "
                    + " VALUES (" + id + ",'" + nuevEsp.getNumExp() + "','" + nuevEsp.getCodCP() + "','"
                    + nuevEsp.getDenominacion() + "',"
                    + (nuevEsp.getInscripcionPresencial() != null ? nuevEsp.getInscripcionPresencial() : "null")
                    + "," + (nuevEsp.getInscripcionTeleformacion() != null ? nuevEsp.getInscripcionTeleformacion() : "null")
                    + "," + (nuevEsp.getAcreditacion() != null ? nuevEsp.getAcreditacion() : "null") + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return id;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error al Insertar un nueva Especialidad" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     *
     * @param idEspecialidad
     * @param idMotDeneg
     * @param con
     * @throws Exception
     */
    public void crearNuevaEspecialidadMotDeneg(int idEspecialidad, String idMotDeneg, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder();
        try {
            query.append("INSERT INTO ");
            query.append(ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES_MOTDENEG, ConstantesMeLanbide50.FICHERO_PROPIEDADES));
            query.append("(ID, ESP_MOT_DENEG) VALUES (");
            query.append(idEspecialidad);
            query.append(",");
            query.append(idMotDeneg);
            query.append(")");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query.toString());
            }
            st = con.createStatement();
            st.executeUpdate(query.toString());
        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Insertar un nuevo motivo denegación Especialidad" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     *
     * @param nuevDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevoServicio(ServiciosVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_SERVICIOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + "(ID, SER_NUM, SER_DESC, SER_UBIC, SER_SUPE) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide50.SEQ_RGCFM_SERVICIOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + ".NextVal,'" + nuevDato.getNumExp() + "','" + nuevDato.getDescripcion() + "','"
                    + nuevDato.getUbicacion() + "',"
                    + (nuevDato.getSuperficie() != null ? nuevDato.getSuperficie() : "null")
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Insertar un nuevo aseo sevicio higienico-sanitario" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevaCapacidad(CapacidadVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_CAPACIDADINSTALA, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + "(ID, ID_ESPSOL, CAIN_NUM, CAIN_IDEF, CAIN_UBI, CAIN_SUP) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide50.SEQ_RGCFM_CAPACIDADINSTALA, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + ".NextVal"
                    + "," + (nuevDato.getIdEspSol() != null ? nuevDato.getIdEspSol() : "null")
                    + ",'" + nuevDato.getNumExp() + "'"
                    + ",'" + nuevDato.getIdetificacionEspFor() + "'"
                    + ",'" + nuevDato.getUbicacion() + "'"
                    + "," + (nuevDato.getSuperficie() != null ? "'" + nuevDato.getSuperficie() + "'" : "null")
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Insertar una capacidad de instalaciones " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevaDotacion(DotacionVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DOTACION, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + "(ID, ID_ESPSOL, DOT_NUM, DOT_CANT, DOT_DET, DOT_FAD) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide50.SEQ_RGCFM_DOTACION, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + ".NextVal "
                    + "," + (nuevDato.getIdEspSol() != null ? nuevDato.getIdEspSol() : "null")
                    + ",'" + nuevDato.getNumExp() + "'"
                    + "," + (nuevDato.getCantidad() != null ? "'" + nuevDato.getCantidad() + "'" : "null")
                    + ",'" + nuevDato.getDenominacionET() + "'"
                    + "," + (nuevDato.getFechaAdq() != null ? "'" + nuevDato.getFechaAdq() + "'" : "null")
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Insertar una Dotacion de instalaciones " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevoMaterial(MaterialVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_MATERIALCONSUMO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " (ID, ID_ESPSOL,  MAC_NUM, MAC_CANT, MAC_DET) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide50.SEQ_RGCFM_MATERIALCONSUMO, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + ".NextVal"
                    + "," + (nuevDato.getIdEspSol() != null ? nuevDato.getIdEspSol() : "null")
                    + ",'" + nuevDato.getNumExp() + "'"
                    + "," + (nuevDato.getCantidad() != null ? nuevDato.getCantidad() : "null")
                    + ",'" + nuevDato.getDenominacionET() + "'"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Insertar material de consumo  " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevoEspacio(EspacioVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPACIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " (ID, ID_ESPSOL, NUM_EXP, NESP_DENESP, NESP_ESPACRED, NESP_ESPAUT, NESP_ALUM, NESP_ALUMNUEV) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide50.SEQ_RGCFM_ESPACIOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + ".NextVal"
                    + "," + (nuevoDato.getIdEspSol() != null ? nuevoDato.getIdEspSol() : "null")
                    + "," + (nuevoDato.getNumExp() != null ? "'" + nuevoDato.getNumExp() + "'" : "null")
                    + "," + (nuevoDato.getDenominacion() != null ? "'" + nuevoDato.getDenominacion() + "'" : "null")
                    + "," + (nuevoDato.getEspAcred() != null ? "'" + nuevoDato.getEspAcred() + "'" : "null")
                    + "," + (nuevoDato.getEspAutor() != null ? "'" + nuevoDato.getEspAutor() + "'" : "null")
                    + "," + (nuevoDato.getNumAlumnos() != null ? nuevoDato.getNumAlumnos() : "null")
                    + "," + (nuevoDato.getAlumNuevos() != null ? nuevoDato.getAlumNuevos() : "null")
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al insertar Espacio " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // Metodos UPDATE  BBDD
    /**
     *
     * @param modEsp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarEspecialidad(EspecialidadesVO modEsp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET ESP_CODCP='" + modEsp.getCodCP() + "'"
                    + ", ESP_DENOM='" + modEsp.getDenominacion() + "'"
                    + ", ESP_PRESE=" + (modEsp.getInscripcionPresencial() != null ? modEsp.getInscripcionPresencial() : "null")
                    + ", ESP_TELEF=" + (modEsp.getInscripcionTeleformacion() != null ? modEsp.getInscripcionTeleformacion() : "null")
                    + ", ESP_ACRED=" + (modEsp.getAcreditacion() != null ? modEsp.getAcreditacion() : "null")
                    + " WHERE ESP_NUM='" + modEsp.getNumExp() + "' AND ID=" + modEsp.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                try {
                    DisponibilidadVO disponibilidad = new DisponibilidadVO();
                    IdentificacionEspVO identificacion = new IdentificacionEspVO();
                    disponibilidad = getDisponibilidadPorCodigoEspSol_NumExp(modEsp.getNumExp(), modEsp.getId(), con);
                    identificacion = getIdentificacionEspPorCodigoEspSol_NumExp(modEsp.getNumExp(), modEsp.getId(), con);
                    if (!modEsp.getCodCP().equals(disponibilidad.getCodCp())) {
                        try {
                            disponibilidad.setCodCp(modEsp.getCodCP());
                            actualizarDisponibilidadDesdeListEspSol(disponibilidad, con);
                        } catch (Exception ex11) {
                            log.debug("Se ha producido un error al Actualizar lienas de Disponibilidad Desde la lista de  : " + disponibilidad.getCodCp() + " - " + identificacion.getId() + " - " + identificacion.getIdEspSol() + ex11.getMessage() + ex11);
                            log.error(ex11);
                        }
                    }
                    if (!modEsp.getCodCP().equals(identificacion.getCodCp())) {
                        try {
                            identificacion.setCodCp(modEsp.getCodCP());
                            identificacion.setDenomEsp(modEsp.getDenominacion());
                            actualizarIdentificacionEspDesdeListEspSol(identificacion, con);
                        } catch (Exception ex12) {
                            log.debug("Se ha producido un error al Actualizar lienas de Identificacion Desde la lista de  Especilidades solicitadas: " + identificacion.getCodCp() + " - " + identificacion.getId() + " - " + identificacion.getIdEspSol() + ex12.getMessage() + ex12);
                            log.error(ex12);
                        }
                    }
                } catch (Exception ex1) {
                    log.debug("Se ha producido un error al Actualizar lienas de Disponibilidad e Identificacion de la Especilidad dada de alta : " + modEsp.getCodCP() + " - " + ex1.getMessage() + ex1);
                    log.error(ex1);
                }
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modifiacar epecialidad Especialidad" + " - "
                    + modEsp.getNumExp() + " - " + modEsp.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarServicio(ServiciosVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_SERVICIOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET SER_DESC='" + nuevoDato.getDescripcion() + "'"
                    + ", SER_UBIC='" + nuevoDato.getUbicacion() + "'"
                    + ", SER_SUPE=" + (nuevoDato.getSuperficie() != null ? nuevoDato.getSuperficie() : "null")
                    + " WHERE SER_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Aseo Servicio Higienico Sanitario" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarDisponibilidad(DisponibilidadVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DISPONRECURSOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET DRE_PRCE='" + nuevoDato.getPropiedadCedidos() + "'"
                    + ", DRE_SIT='" + nuevoDato.getSituados() + "'"
                    + ", DRE_AUL='" + (nuevoDato.getSupAulas() != null ? nuevoDato.getSupAulas() : "")
                    + "', DRE_TAL='" + (nuevoDato.getSupTaller() != null ? nuevoDato.getSupTaller() : "")
                    + "', DRE_AUTA='" + (nuevoDato.getSupAulaTaller() != null ? nuevoDato.getSupAulaTaller() : "")
                    + "', DRE_CAPR='" + (nuevoDato.getSupCampoPract() != null ? nuevoDato.getSupCampoPract() : "")
                    + "' WHERE DRE_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId() + " AND ID_ESPSOL=" + nuevoDato.getIdEspSol();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Una linea de Disponibilidad de recursos" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean actualizarDisponibilidadDesdeListEspSol(DisponibilidadVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DISPONRECURSOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET DRE_CODCP='" + nuevoDato.getCodCp() + "'"
                    + " WHERE DRE_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId() + " AND ID_ESPSOL=" + nuevoDato.getIdEspSol();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Una linea de Disponibilidad de recursos desde la lista de especialuidades solicitadas" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarIdentificacionEsp(IdentificacionEspVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_IDENTIFICACIONESP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET IDE_HORAS=" + (nuevoDato.getHoras() != null ? "'" + nuevoDato.getHoras() + "'" : "null")
                    + ", IDE_ALUM=" + (nuevoDato.getAlumnos() != null ? "'" + nuevoDato.getAlumnos() + "'" : "null")
                    + ", IDE_CERTP=" + (nuevoDato.getCertPro() != null ? nuevoDato.getCertPro() : "null")
                    + ", IDE_RDER='" + nuevoDato.getRealDecRegu() + "'"
                    + ", IDE_BOEFP='" + nuevoDato.getBoeFecPub() + "'"
                    + ", IDE_DESADAP='" + nuevoDato.getDescripAdapt() + "'"
                    + ", IDE_OBSADAP='" + nuevoDato.getObservAdapt() + "'"
                    + " WHERE IDE_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId() + " AND ID_ESPSOL=" + nuevoDato.getIdEspSol();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Una linea de la lista Identificacion de  Especialidad solicitada" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean actualizarIdentificacionEspDesdeListEspSol(IdentificacionEspVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_IDENTIFICACIONESP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET IDE_CODESP='" + nuevoDato.getCodCp() + "'"
                    + ", IDE_DENESP='" + nuevoDato.getDenomEsp() + "'"
                    + " WHERE IDE_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId() + " AND ID_ESPSOL=" + nuevoDato.getIdEspSol();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Identificacion de  Especialidad solicitada desde la lista de especialdad solicitada" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarCapacidad(CapacidadVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_CAPACIDADINSTALA, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET CAIN_IDEF='" + nuevoDato.getIdetificacionEspFor() + "'"
                    + ", CAIN_UBI='" + nuevoDato.getUbicacion() + "'"
                    + ", CAIN_SUP='" + (nuevoDato.getSuperficie()) + "'"
                    + " WHERE CAIN_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Capacidad de Instalaciones" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarDotacion(DotacionVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DOTACION, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET DOT_CANT=" + (nuevoDato.getCantidad() != null ? "'" + nuevoDato.getCantidad() + "'" : "null")
                    + ", DOT_DET='" + nuevoDato.getDenominacionET() + "'"
                    + ", DOT_FAD=" + (nuevoDato.getFechaAdq() != null ? "'" + nuevoDato.getFechaAdq() + "'" : "null")
                    + " WHERE DOT_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Dotacion de Instalaciones" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarMaterial(MaterialVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_MATERIALCONSUMO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET MAC_CANT=" + (nuevoDato.getCantidad() != null ? nuevoDato.getCantidad() : "null")
                    + ", MAC_DET='" + nuevoDato.getDenominacionET() + "'"
                    + " WHERE MAC_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Material de consumo" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param nuevoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarEspacio(EspacioVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPACIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " SET NESP_DENESP=" + (nuevoDato.getDenominacion() != null ? "'" + nuevoDato.getDenominacion() + "'" : "null")
                    + ", NESP_ESPACRED=" + (nuevoDato.getEspAcred() != null ? "'" + nuevoDato.getEspAcred() + "'" : "null")
                    + ", NESP_ESPAUT=" + (nuevoDato.getEspAutor() != null ? "'" + nuevoDato.getEspAutor() + "'" : "null")
                    + ", NESP_ALUM=" + (nuevoDato.getNumAlumnos() != null ? nuevoDato.getNumAlumnos() : "null")
                    + ", NESP_ALUMNUEV=" + (nuevoDato.getAlumNuevos() != null ? nuevoDato.getAlumNuevos() : "null")
                    + " WHERE NUM_EXP='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar Espacio " + " - " + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // Metodos Delete de BBDD
    /**
     *
     * @param numExp
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarEspecialidad(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE ESP_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Especialiddad " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param idEspecialidad
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarEspecialidadMotDeneg(Integer idEspecialidad, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPECIALIDADES_MOTDENEG, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE  ID=" + (idEspecialidad != null ? idEspecialidad : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando motDeneg de Especialidad " + idEspecialidad, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarServicio(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_SERVICIOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE SER_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Aseo Servicio Higienico-Sanitario " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param idEspSol
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarDisponibilidadDesdeListEspSol(String numExp, Integer id, Integer idEspSol, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DISPONRECURSOS, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE DRE_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null") + " AND ID_ESPSOL=" + (idEspSol != null ? idEspSol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Disponibilidad de Recursos desde Lista Especialidad Solicitada " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param idEspSol
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarIdentificacionEspDesdeListEspSol(String numExp, Integer id, Integer idEspSol, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_IDENTIFICACIONESP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE IDE_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null") + " AND ID_ESPSOL=" + (idEspSol != null ? idEspSol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Identificacion de Especialidad desde Lista Especialidad Solicitada " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarCapacidad(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_CAPACIDADINSTALA, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE CAIN_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Capacidad de instalaciones" + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarDotacion(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_DOTACION, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE DOT_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Dotacion  de instalaciones" + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarMaterial(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_MATERIALCONSUMO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE MAC_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Material de Consumo " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarEspacio(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_ESPACIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Espacio " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // Funciones Privadas
    private int recogerIDInsertar(String sequence, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String query = "SELECT " + sequence + ".NextVal AS PROXID from DUAL ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando El numero de ID para insercion al llamar la sequencia " + sequence + " : ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return id;
    }

    /**
     *
     * @param datoNuevo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevaIdentificacioEsp(IdentificacionEspVO datoNuevo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide50.SEQ_RGCFM_IDENTIFICACIONESP, ConstantesMeLanbide50.FICHERO_PROPIEDADES), con);
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_RGCFM_IDENTIFICACIONESP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + "(ID, ID_ESPSOL, IDE_NUM, IDE_CODESP, IDE_DENESP, IDE_HORAS, IDE_ALUM, IDE_CERTP, IDE_RDER, IDE_BOEFP, IDE_DESADAP, IDE_OBSADAP)"
                    + " VALUES (" + id + "," + datoNuevo.getIdEspSol() + ",'" + datoNuevo.getNumExp() + "','" + datoNuevo.getCodCp() + "','" + datoNuevo.getDenomEsp() + "'"
                    + "," + (datoNuevo.getHoras() != null ? "'" + datoNuevo.getHoras() + "'" : "null")
                    + "," + (datoNuevo.getAlumnos() != null ? "'" + datoNuevo.getAlumnos() + "'" : "null")
                    + "," + (datoNuevo.getCertPro() != null ? datoNuevo.getCertPro() : "null")
                    + ",'" + datoNuevo.getRealDecRegu() + "'"
                    + ",'" + datoNuevo.getBoeFecPub() + "'"
                    + ",'" + datoNuevo.getDescripAdapt() + "'"
                    + ",'" + datoNuevo.getObservAdapt() + "'"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Insertar nuevos detalles de la identificacon de la especialidade " + " - " + ex.getMessage() + ex);
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
    }

    /**
     * Recupera el valor de un campo suplementario desplegable externo para un
     * determinado expediente
     *
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param codCampo: Código del campo
     * @param con: Conexión a la BBDD
     * @return String con el valor o null sino se ha podido recuperar
     * @throws Exception
     */
    public String getValorCampoDesplegableExternoExpediente(int codOrganizacion, String numExpediente, String codCampo, Connection con) throws Exception {
        String salida = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";

        try {
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];

            query = "SELECT TDEX_VALOR FROM E_TDEX "
                    + "WHERE TDEX_NUM=? AND TDEX_EJE=? AND TDEX_MUN=? AND TDEX_COD=?";

            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExpediente);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, codCampo);

            rs = ps.executeQuery();
            while (rs.next()) {
                salida = rs.getString("TDEX_VALOR");
            }
        } catch (NumberFormatException ex) {
            log.error("Error al recuperar el valor del campo desplegable " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());
            throw ex;
        } catch (SQLException ex) {
            log.error("Error al recuperar el valor del campo desplegable " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return salida;
    }

    /**
     * Busca para una descripción concreta de un tipo de centro, que código le
     * corresponde en la tabla flbgen.FPE_CEN_CENTROSREG_TIPOENT
     *
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
    public String getCodigoValorTipoCentro(String valor, Connection con) throws Exception {
        String salida = null;
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {

            query = "SELECT CORR_CENTROSREG_TIPOENT FROM flbgen.FPE_CEN_CENTROSREG_TIPOENT "
                    + "WHERE TIPO_ENT_DES_C LIKE '%" + valor + "%'";

            if (log.isInfoEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                salida = rs.getString("CORR_CENTROSREG_TIPOENT");
                break;
            }
        } catch (SQLException ex) {
            log.error("Error al recuperar el código del tipo de centro correspondiente a un centro con descripción " + valor + ": " + ex.getMessage());
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return salida;
    }

    /**
     * Recupera el número de alumnos de una especialidad
     *
     * @param numExpediente: Número del expediente
     * @param codigoEspecialidad: Código de la especialidad
     * @param con: Conexión a la BBDD
     * @return Integer
     * @throws java.lang.Exception
     */
    public String getNumeroAlumnosEspecialidad(String numExpediente, String codigoEspecialidad, Connection con) throws Exception {
        String salida = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "SELECT IDE_ALUM FROM MELANBIDE50_IDENTIFICESP "
                    + "WHERE IDE_NUM=? AND IDE_CODESP=?";

            if (log.isInfoEnabled()) {
                log.info("sql = " + query);
            }
            int i = 1;

            ps = con.prepareStatement(query);
            ps.setString(i++, numExpediente);
            ps.setString(i++, codigoEspecialidad);

            rs = ps.executeQuery();

            while (rs.next()) {
                salida = rs.getString("IDE_ALUM");
                break;
            }
        } catch (SQLException ex) {
            log.error("Error al recuperar el número de alumnos de una especialidad: " + ex.getMessage());
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return salida;
    }

    /**
     * Recupera el número de censo a partir del código de centro y del código de
     * ubicación. La búsqueda se hace en la tabla LAN_CENTROS_SERVICIOS
     *
     * @param codigoCentro: Código del centro
     * @param codigoUbicacion: Código de la ubicación
     * @param con: Conexión a la BBDD
     * @return String con el número de censo o false en caso contrario
     * @throws Exception
     */
    public String getNumeroCenso(String codigoCentro, String codigoUbicacion, Connection con) throws Exception {
        String salida = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "SELECT LAN_NUM_REG_AUT FROM LAN_CENTROS_SERVICIOS "
                    + "WHERE GEN_CEN_COD_CENTRO=? AND GEN_CEN_COD_UBIC=? AND LAN_UBIC_SERVICIO_LMV='314'";

            log.info("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, codigoCentro);
            ps.setString(i++, codigoUbicacion);

            rs = ps.executeQuery();

            while (rs.next()) {
                salida = rs.getString("LAN_NUM_REG_AUT");
                break;
            }
        } catch (SQLException ex) {
            log.error("Error al recuperar el número de censo de una especialidad: " + ex.getMessage());
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return salida;
    }

    /**
     * Recupera el documento de un interesado que tenga el rol por defecto en el
     * expediente, y además se comprueba que su documento sea un CIF (tipo
     * documento 4 o 5 en Flexia) Si hay más de un interesado se recupera uno de
     * ellos, aunque esto no debería pasar.
     *
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param con: Conexión a la BBDD
     * @return String con el documento o null en caso contrario
     * @throws
     * es.altia.flexia.integracion.moduloexterno.melanbide50.util.MeLanbide50Exception
     * @throws java.sql.SQLException
     */
    public String getDocumentoInteresadoRolDefecto(int codOrganizacion, String numExpediente, Connection con) throws MeLanbide50Exception, SQLException {
        String documento = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        String[] datos = numExpediente.split("/");
        String ejercicio = datos[0];
        String codProcedimiento = datos[1];

        String sql = "SELECT HTE_DOC,HTE_TID,HTE_DOC FROM E_EXT,T_HTE,E_ROL WHERE EXT_MUN=? AND EXT_NUM=? AND EXT_EJE=? AND EXT_PRO=? "
                + "AND EXT_TER=T_HTE.HTE_TER "
                + // "AND (HTE_TID=4 OR HTE_TID=5) "+ 
                "AND EXT_NVR=T_HTE.HTE_NVR AND ROL_MUN=? AND ROL_MUN=EXT_MUN AND "
                + "ROL_PRO=EXT_PRO AND ROL_PRO=EXT_PRO AND ROL_PDE=1 AND ROL_COD=EXT_ROL";
        log.debug("getDocumentoInteresadoRolDefecto sql: " + sql);
        log.debug("getDocumentoInteresadoRolDefecto param1: " + codOrganizacion + ",param2: " + numExpediente + ",param3: " + ejercicio + ",codProcedimiento: " + codProcedimiento + ",codOrganizacion: " + codOrganizacion);

        try {
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, numExpediente);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codOrganizacion);
            rs = ps.executeQuery();
            while (rs.next()) {
                documento = rs.getString("HTE_DOC");
            }

        } catch (NumberFormatException e) {
            throw new MeLanbide50Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return documento;
    }

    /**
     * Recupera las ubicaciones de un centro
     *
     * @param codCentro: CIF correspondiente al centro
     * @param con: Conexión a la BBDD
     * @return RespuestaUbicacionesVO: Objeto instancia de la clase
     * RespuestaUbicacionesVO con las ubicaciones y en caso contrario, con el
     * código de error producido
     * @throws MeLanbide50Exception
     * @throws java.sql.SQLException
     */
    public ArrayList<UbicacionVO> getUbicacionesCentro(String codCentro, Connection con) throws MeLanbide50Exception, SQLException {
        ArrayList<UbicacionVO> ubicaciones = new ArrayList<UbicacionVO>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = null;

        try {
            sql = "SELECT UBIC.GEN_CEN_COD_UBIC AS CODUBICACION, UBIC.GEN_CEN_NOM_UBIC,"
                    + " UBIC.GEN_GRAL_TIPO_VIA, UBIC.GEN_CEN_NOM_VIA, UBIC.GEN_CEN_NUM_VIA,"
                    + " UBIC.GEN_GRAL_BISDUPLICADO, UBIC.GEN_CEN_ESCALERA, UBIC.GEN_CEN_PISO,"
                    + " UBIC.GEN_CEN_PUERTA, UBIC.GEN_PRO_COD_PROV, UBIC.GEN_MUN_COD_MUN,"
                    + " UBIC.GEN_COD_CODPOSTAL"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_CENFOR_UBICACION, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " UBIC"
                    + " JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " SUBSERV"
                    + " ON UBIC.GEN_CEN_COD_CENTRO = SUBSERV.GEN_CEN_COD_CENTRO"
                    + " AND UBIC.GEN_CEN_COD_UBIC = SUBSERV.GEN_CEN_COD_UBIC"
                    + " WHERE UBIC.GEN_CEN_COD_CENTRO=? "
                    + " AND UBIC.GEN_CEN_ESTADO ='HO'";
            log.debug("getUbicacionesCentro sql: " + sql);
            log.debug("getUbicacionesCentro param: " + codCentro);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, codCentro);
            rs = ps.executeQuery();

            Hashtable<String, String> provincias = new Hashtable<String, String>();
            Hashtable<String, String> municipios = new Hashtable<String, String>();

            while (rs.next()) {
                String codUbicacion = rs.getString("CODUBICACION");
                String nombreUbicacion = rs.getString("GEN_CEN_NOM_UBIC");
                String tipoVia = rs.getString("GEN_GRAL_TIPO_VIA");
                String nombreCalle = rs.getString("GEN_CEN_NOM_VIA");
                String numeroCalle = rs.getString("GEN_CEN_NUM_VIA");
                String bis = rs.getString("GEN_GRAL_BISDUPLICADO");
                String escalera = rs.getString("GEN_CEN_ESCALERA");
                String piso = rs.getString("GEN_CEN_PISO");
                String puerta = rs.getString("GEN_CEN_PUERTA");
                String codProvincia = rs.getString("GEN_PRO_COD_PROV");
                String codMunicipio = rs.getString("GEN_MUN_COD_MUN");
                String codPostal = rs.getString("GEN_COD_CODPOSTAL");

                UbicacionVO ubicacion = new UbicacionVO();
                ubicacion.setCodCentro(codCentro);
                ubicacion.setNombreUbicacion(nombreUbicacion);
                ubicacion.setCodUbicacion(codUbicacion);
                ubicacion.setNombreCalle(nombreCalle);
                ubicacion.setNumeroCalle(numeroCalle);
                ubicacion.setBis(bis);
                ubicacion.setEscalera(escalera);
                ubicacion.setPiso(piso);
                ubicacion.setPuerta(puerta);
                ubicacion.setTipoCalle(tipoVia);
                ubicacion.setCodProvincia(codProvincia);
                ubicacion.setCodMunicipio(codMunicipio);
                ubicacion.setCodPostal(codPostal);

                if (codProvincia != null && codMunicipio != null && MeLanbide50Utils.isInteger(codProvincia) && MeLanbide50Utils.isInteger(codMunicipio)) {

                    Integer iProvincia = Integer.valueOf(codProvincia);
                    //Integer iMunicipio = new Integer(codMunicipio);                    

                    if (provincias.contains(Integer.valueOf(codProvincia))) {
                        ubicacion.setDescProvincia(provincias.get(codProvincia));
                    } else {
                        String descProvincia = getDescProvincia(108, iProvincia, con);
                        if (descProvincia != null) {
                            ubicacion.setDescProvincia(descProvincia);
                            provincias.put(iProvincia.toString(), descProvincia);
                        } else {
                            ubicacion.setDescProvincia("-");
                        }
                    }

                    String aux = codMunicipio.substring(2, codMunicipio.length());
                    Integer iMunicipio = Integer.valueOf(aux);

                    if (municipios.contains(iProvincia + "-" + iMunicipio)) {
                        ubicacion.setDescMunicipio(municipios.get(iProvincia + "-" + iMunicipio));
                    } else {
                        String descMunicipio = getDescMunicipio(108, iProvincia, iMunicipio, con);
                        if (descMunicipio != null) {
                            ubicacion.setDescMunicipio(descMunicipio);
                            municipios.put(iProvincia + "-" + iMunicipio, descMunicipio);
                        } else {
                            ubicacion.setDescMunicipio("-");
                        }
                    }
                }
                ubicaciones.add(ubicacion);
            }

        } catch (MeLanbide50Exception e) {
            throw new MeLanbide50Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        } catch (NumberFormatException e) {
            throw new MeLanbide50Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return ubicaciones;
    }

    /**
     * Recupera el código de centro que se busca a través de un CIF
     *
     * @param documento: String que contiene el CIF
     * @param con: Conexión a la BBDD
     * @return ArrayList: Colección con los centros homologados
     * @throws MeLanbide50Exception si ocurre algún error
     * @throws java.sql.SQLException
     */
    public ArrayList<String> getCodCentro(String documento, Connection con) throws MeLanbide50Exception, SQLException {
        ArrayList<String> centros = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            sql = "SELECT CENTROS.GEN_CEN_COD_CENTRO "
                    + " FROM GEN_EMPRESARIO, " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_CENFOR_CENTROS, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " CENTROS "
                    + " WHERE GEN_USU_NUM_DOC=?"
                    + " AND GEN_EMPRESARIO.GEN_EMP_CORR = CENTROS.GEN_EMP_CORR"
                    + " AND CENTROS.GEN_CEN_ESTADO ='HO'";

            log.debug("getCodCentro sql = " + sql);
            log.debug("getCodCentro parametro = " + documento);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, documento);

            rs = ps.executeQuery();
            while (rs.next()) {
                centros.add(rs.getString("GEN_CEN_COD_CENTRO"));
            }

        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar el código de centro: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return centros;
    }

    /**
     * Recupera la descripción de la provincia
     *
     * @param codPais: Código del pais
     * @param codProvincia: Código de la provincia
     * @param con: Conexión a la BBDD
     * @return String con al descripción
     * @throws MeLanbide50Exception si ocurre algún error
     * @throws java.sql.SQLException
     */
    public String getDescProvincia(int codPais, int codProvincia, Connection con) throws MeLanbide50Exception, SQLException {
        String salida = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT PRV_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PRV "
                    + "WHERE PRV_PAI=" + codPais + " AND PRV_COD=" + codProvincia;

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                salida = rs.getString("PRV_NOM");
            }

        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar la descripción de la provincia: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return salida;
    }

    /**
     * Recupera la descripción de un municipio
     *
     * @param codPais: código del pais
     * @param codProvincia: Código de la provincia
     * @param codMunicipio: Códigio del municipio
     * @param con: Conexión a la BBDD
     * @return String con al descripción
     * @throws MeLanbide50Exception si ocurre algún error
     * @throws java.sql.SQLException
     */
    public String getDescMunicipio(int codPais, int codProvincia, int codMunicipio, Connection con) throws MeLanbide50Exception, SQLException {
        String salida = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT MUN_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN "
                    + "WHERE MUN_PAI=" + codPais + " AND MUN_PRV=" + codProvincia + " AND MUN_COD=" + codMunicipio;

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                salida = rs.getString("MUN_NOM");
            }

        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar la descripción del municipio: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return salida;
    }

    /**
     * Recupera el CIF del interesado de un expediente que tenga un determinado
     * rol en el mismo
     *
     * @param codOrganizacion: Código de la organización
     * @param codRol: Código del rol
     * @param numExpediente: Nú8mero del expediente
     * @param con: Conexión a la BBDD
     * @return String o null sino se ha podido recuperar
     * @throws MeLanbide50Exception: Si ocurre un error técnico
     * @throws java.sql.SQLException
     */
    public String getCIFInteresadoConRol(int codOrganizacion, int codRol, String numExpediente, Connection con) throws MeLanbide50Exception, SQLException {
        String salida = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];

            String sql = "SELECT HTE_DOC FROM E_EXT,T_HTE WHERE EXT_NUM=? AND EXT_ROL=? AND EXT_EJE=? AND EXT_MUN=? "
                    + "AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR AND (HTE_TID=4 OR HTE_TID=5)";

            int i = 1;
            ps = con.prepareStatement(sql);

            ps.setString(i++, numExpediente);
            ps.setInt(i++, codRol);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, codOrganizacion);
            rs = ps.executeQuery();

            while (rs.next()) {
                salida = rs.getString("HTE_DOC");
            }

        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar el CIF del interesado con rol " + codRol + " del expediente " + numExpediente + ": " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return salida;
    }

    /**
     * Se pasa un código de provincia y si está formado por un único dígito, se
     * concatena un cero a la izquierda del mismo
     *
     * @param codProvincia: Código de la provincia en Flexia
     * @return String con el código de provincia con 2 dígitos.
     */
    private String formatoProvinciaNLangai(String codProvincia) {

        if (codProvincia != null && !"".equals(codProvincia) && codProvincia.length() == 1) {
            codProvincia = ConstantesMeLanbide50.CERO + codProvincia;
        }
        return codProvincia;
    }

    /**
     * Recibe un código de provincia y un código de municipio y devuelve un
     * código de municipio válido para NLANGAI. Ejemplo: codProvincia = 1 y
     * codMunicipio = 31 --> tiene que devolver 01031 El código de provincia
     * tiene que estar formado por 2 dígitos y el de municipio por 3.
     * Concatenado tiene que tener una longitud de 5 dígitos.
     *
     * @param codProvincia: Código de provincia
     * @param codMunicipio: Código de municipio
     * @return String
     */
    private String formatoMunicipioNLangai(String codProvincia, String codMunicipio) {
        String salida = null;

        codProvincia = formatoProvinciaNLangai(codProvincia);
        if (codMunicipio != null && !"".equals(codMunicipio)) {
            int aux = ConstantesMeLanbide50.LONGITUD_MUNICIPIO - codMunicipio.length();
            for (int i = 0; i < aux; i++) {
                codMunicipio = ConstantesMeLanbide50.CERO + codMunicipio;
            }
        }

        salida = codProvincia + codMunicipio;
        return salida;
    }

    /**
     * Recupera el domicilio del interesado que tiene un determinado rol en el
     * expediente
     *
     * @param codOrganizacion: Código de la organización
     * @param codRol: Código del rol
     * @param numExpediente: Número del expediente
     * @param con: Conexión a la BBDD
     * @return
     * @throws MeLanbide50Exception : Si ocurre un error técnico no controlado
     * @throws java.sql.SQLException
     */
    public Hashtable<String, Object> getDomicilioInteresadoConRol(int codOrganizacion, int codRol, String numExpediente, Connection con) throws MeLanbide50Exception, SQLException {
        Hashtable<String, Object> salida = new Hashtable<String, Object>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];

            String sql = "SELECT DNN_TVI TIPOVIA,DNN_PAI CODPAIS,DNN_PRV CODPROVINCIA,DNN_MUN CODMUNICIPIO,DNN_PLT PLANTA,DNN_DMC EMPLAZAMIENTO,DNN_CPO CODPOSTAL,DNN_PTA PUERTA,DNN_ESC ESCALERA,DNN_NUD NUMERO_DESDE,DNN_PLT PLANTA,VIA_NOM NOMBREVIA,VIA_COD CODIGOVIA "
                    + "FROM E_EXT,T_DNN,T_VIA "
                    + "WHERE EXT_NUM=? AND EXT_ROL=? AND EXT_EJE=? AND EXT_MUN=? "
                    + "AND EXT_DOT=DNN_DOM AND DNN_VIA=VIA_COD (+)"
                    + "AND DNN_VIA=VIA_COD(+) AND DNN_TVI = VIA_TVI(+) AND DNN_MUN = VIA_MUN(+) AND DNN_PRV=VIA_PRV(+) AND DNN_PAI = VIA_PAI(+)";
            log.info(sql);
            int i = 1;
            ps = con.prepareStatement(sql);

            ps.setString(i++, numExpediente);
            ps.setInt(i++, codRol);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, codOrganizacion);
            rs = ps.executeQuery();

            int contador = 0;
            DireccionInteresadoVO dom = new DireccionInteresadoVO();
            while (rs.next()) {

                String tipoViaFlexia = rs.getString("TIPOVIA");
                String codProvincia = rs.getString("CODPROVINCIA");
                String codMunicipio = rs.getString("CODMUNICIPIO");

                String tipoViaPersonaFisica = ConfigurationParameter.getParameter("CodigoVia/Flexia/" + tipoViaFlexia, ConstantesMeLanbide50.FICHERO_PROPIEDADES);
                if (tipoViaPersonaFisica != null && !"".equals(tipoViaPersonaFisica)) {
                    dom.setTipoViaPersonaFisica(tipoViaFlexia);
                } else {
                    dom.setTipoViaPersonaFisica(ConstantesMeLanbide50.TIPO_VIA_DEFECTO_PERSONA_FISICA);
                }

                dom.setTipoViaPersonaFisica(tipoViaPersonaFisica);
                dom.setCodPais(rs.getString("CODPAIS"));
                dom.setCodProvincia(formatoProvinciaNLangai(codProvincia));
                dom.setCodMunicipio(formatoMunicipioNLangai(codProvincia, codMunicipio));
                dom.setEmplazamiento(rs.getString("EMPLAZAMIENTO"));
                dom.setCodPostal(rs.getString("CODPOSTAL"));
                dom.setPuerta(rs.getString("PUERTA"));
                dom.setEscalera(rs.getString("ESCALERA"));
                dom.setNombreVia(rs.getString("NOMBREVIA"));
                dom.setCodVia(rs.getString("CODIGOVIA"));
                dom.setNumeroDesde(rs.getString("NUMERO_DESDE"));
                dom.setPlanta(rs.getString("PLANTA"));
                contador++;
            }

            if (contador == 1) {
                // Hay un único interesado con dicho rol en el expediente                
                salida.put("status", "0");
                salida.put("direccion", dom);
            } else if (contador > 1) {
                salida.put("status", "1"); // Se indica que hay más de un interesado con el rol, por tanto, error
            } else if (contador == 0) {
                salida.put("status", "2"); // El expediente no tienen interesados con dicho rol
            }

        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar el CIF del interesado con rol " + codRol + " del expediente " + numExpediente + ": " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return salida;
    }

    /**
     *
     * @param codMunicipio
     * @param ejercicio
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public List<Tercero> getTercerosExpediente(int codMunicipio, Integer ejercicio, String numExp, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Tercero> terceros = new ArrayList<Tercero>();
        try {
            String query = null;
            int historico = 0;
            String codTercero = null;
            String version = null;
            Tercero tercero = null;
            Domicilio domicilio = null;
            /*query = "select ext_rol, ter_doc from t_ter inner join e_ext on ter_cod = ext_ter"
                    +" where ext_num = '"+numExp+"' and ext_rol in (";*/

            query = "select t.ter_cod, t.ter_doc, t.ter_nve, h.hte_ter, h.hte_doc, h.hte_nvr,"
                    + " case when t.ter_cod is not null and t.ter_doc is not null then 0 else 1 end as HISTORICO"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " rel"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_TERCEROS, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " t on t.ter_cod = rel.ext_ter and t.ter_nve = rel.ext_nvr"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_TERCEROS_HIST, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " h on h.hte_ter = rel.ext_ter and h.hte_nvr = rel.ext_nvr"
                    + " where ext_rol = 1 and ext_mun = ? and ext_eje = ? and ext_num = ?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setInt(2, ejercicio);
            st.setString(3, numExp);
            rs = st.executeQuery();
            while (rs.next()) {

                historico = rs.getInt("HISTORICO");
                switch (historico) {
                    case 0:
                        codTercero = rs.getString("TER_COD");
                        version = rs.getString("TER_NVE");
                        tercero = this.getTercero(codMunicipio, ejercicio, numExp, codTercero, version, con);
                        break;
                    case 1:
                        codTercero = rs.getString("HTE_TER");
                        version = rs.getString("HTE_NVR");
                        tercero = this.getTerceroHistorico(codMunicipio, ejercicio, numExp, codTercero, version, con);
                        break;
                    default:
                        tercero = null;
                }
                if (tercero != null) {
                    try {
                        domicilio = this.getDomicilioTercero(tercero, con);
                        tercero.setDomicilio(domicilio);
                    } catch (Exception ex) {
                        log.error("Se ha producido un error al recuperar el domicilio del tercero " + tercero.getTerDoc() + " para el expediente " + numExp, ex);
                    }
                    terceros.add(tercero);
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return terceros;
    }

    private Tercero getTercero(int codMunicipio, Integer ejercicio, String numExpediente, String codTercero, String version, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Tercero ter = null;
        try {
            String query = null;
            query = ""
                    + ""
                    + "select"
                    + " TER_COD COD, TER_TID TID, TER_DOC DOC, TER_NOM NOM, TER_AP1 AP1, TER_PA1 PA1, TER_AP2 AP2,"
                    + "TER_PA2 PA2, TER_NOC NOC, TER_NML NML, TER_TLF TLF, TER_DCE DCE, TER_SIT SIT, TER_NVE VER,"
                    + "TER_FAL FAL, TER_UAL UAL, TER_APL APL, TER_FBJ FBJ, TER_UBJ UBJ, TER_DOM DOM, EXTERNAL_CODE EXTERNAL_CODE, EXT_ROL ROL"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_TERCEROS, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " t"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " r"
                    + " on t.ter_cod = r.ext_ter and t.ter_nve = r.ext_nvr"
                    + " where r.ext_mun = ? and r.ext_eje = ? and r.ext_num = ? and r.ext_ter = ? and r.ext_nvr = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setInt(2, ejercicio);
            st.setString(3, numExpediente);
            st.setString(4, codTercero);
            st.setString(5, version);
            rs = st.executeQuery();
            if (rs.next()) {
                ter = (Tercero) MeLanbide50MappingUtils.getInstance().map(rs, Tercero.class);
            }
            return ter;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando tercero " + codTercero, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    private Tercero getTerceroHistorico(int codMunicipio, Integer ejercicio, String numExpediente, String codTercero, String version, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        Tercero ter = null;
        try {
            String query = null;
            query = "select "
                    + " HTE_TER COD, HTE_NVR VER, HTE_TID TID, HTE_DOC DOC, HTE_NOM NOM,"
                    + " HTE_AP1 AP1, HTE_PA1 PA1, HTE_AP2 AP2, HTE_PA2 PA2, HTE_NOC NOC, HTE_NML NML,"
                    + " HTE_TLF TLF, HTE_DCE DCE, HTE_APL APL, EXTERNAL_CODE EXTERNAL_CODE,"
                    + " null SIT, null FAL, null UAL, null FBJ, null UBJ, null DOM, EXT_ROL ROL"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_TERCEROS_HIST, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " h"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " r"
                    + " on h.hte_ter = r.ext_ter and h.hte_nvr = r.ext_nvr"
                    + " where r.ext_mun = ? and r.ext_eje = ? and r.ext_num = ? and r.ext_ter = ? and r.ext_nvr = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setInt(2, ejercicio);
            st.setString(3, numExpediente);
            st.setString(4, codTercero);
            log.info("codTercero: " + codTercero);
            st.setString(5, version);
            log.info("version: " + version);
            rs = st.executeQuery();
            if (rs.next()) {
                ter = (Tercero) MeLanbide50MappingUtils.getInstance().map(rs, Tercero.class);
            }
            return ter;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando tercero " + codTercero, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    private Domicilio getDomicilioTercero(Tercero ter, Connection con) throws Exception {
        if (ter != null && ter.getTerCod() != null) {
            PreparedStatement st = null;
            ResultSet rs = null;
            Domicilio dom = null;
            try {
                String query = null;
                query = "select dom.*, via.VIA_NOM from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_DOMICILIO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " dom inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_T_DOT, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " dot on dom.DNN_DOM = dot.DOT_DOM"
                        + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_T_VIA, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " via on dom.DNN_VIA = via.VIA_COD"
                        + " where dot.DOT_TER = ?";

                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st = con.prepareStatement(query);
                st.setLong(1, ter.getTerCod());
                log.debug("codigo tercero: " + ter.getTerCod());
                rs = st.executeQuery();
                if (rs.next()) {
                    dom = (Domicilio) MeLanbide50MappingUtils.getInstance().map(rs, Domicilio.class);
                }
                return dom;
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando domicilio tercero " + ter.getTerCod(), ex);
                throw new Exception(ex);
            } finally {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } else {
            return null;
        }
    }

    private boolean grabarCampoSuplementarioNumericoTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, Integer valor, Connection con) throws Exception {
        log.info("ULTIMO TRAMITE:" + codTramite);
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "";
        log.debug("sql = " + numExp);
        String[] parts = numExp.split("/");
        log.debug("procedimiento = " + parts[1]);

        String CodRGCFM = parts[1];
        try {
            String sql = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TNUT, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where TNUT_COD = '" + codCampo + "' AND TNUT_NUM = '" + numExp + "' AND TNUT_TRA=" + codTramite + " AND TNUT_OCU=" + ocurrenciaTramite;
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TNUT, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " SET TNUT_VALOR = " + valor + ""
                        + " WHERE TNUT_NUM = '" + numExp + "' AND TNUT_COD = '" + codCampo + "'  AND TNUT_TRA=" + codTramite + " AND TNUT_OCU=" + ocurrenciaTramite;
            } else {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TNUT, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " (TNUT_MUN, TNUT_EJE, TNUT_NUM, TNUT_TRA, TNUT_OCU, TNUT_COD, TNUT_VALOR,TNUT_PRO)"
                        + " values (" + codOrganizacion + ", " + ejercicio + ", '" + numExp + "', " + codTramite + ", " + ocurrenciaTramite + ", '" + codCampo + "'," + valor + ",'" + CodRGCFM + "')";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);

            int res = st.executeUpdate();
            return res > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error guardando campo suplementario " + codCampo + " para expediente " + numExp + " en trámite " + codTramite);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    private boolean grabarCampoSuplementarioFechaTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, String valor, Connection con) throws Exception {
        PreparedStatement st = null;
        String query = "";
        ResultSet rs = null;
        log.debug("sql = " + numExp);

        String[] parts = numExp.split("/");
        log.debug("procedimiento = " + parts[1]);

        String tipoExpediente = parts[1];
        try {
            String sql = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TFET, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where TFET_COD = '" + codCampo + "' AND TFET_NUM = '" + numExp + "'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TFET, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " SET TFET_VALOR = '" + valor + "'"
                        + " WHERE TFET_NUM = '" + numExp + "' AND TFET_COD = '" + codCampo + "'";
            } else {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TFET, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " (TFET_MUN, TFET_EJE, TFET_NUM, TFET_TRA, TFET_OCU, TFET_COD, TFET_VALOR, TFET_PRO)"
                        + " values (" + codOrganizacion + ", " + ejercicio + ", '" + numExp + "', " + codTramite + ", " + ocurrenciaTramite + ", '" + codCampo + "','" + valor + "','" + tipoExpediente + "')";

            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);

            int res = st.executeUpdate();
            return res > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error guardando campo suplementario " + codCampo + " para expediente " + numExp + " en trámite " + codTramite);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    private boolean grabarCampoSuplementarioNumericoExpediente(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, Integer valor, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            String sql = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TNU, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where TNU_COD = '" + codCampo + "' AND TNU_NUM = '" + numExp + "'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TNU, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " SET TNU_VALOR = " + valor + ""
                        + " WHERE TNU_NUM = '" + numExp + "' AND TNU_COD = '" + codCampo + "'";
            } else {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TNU, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR)"
                        + " values (" + codOrganizacion + ", " + ejercicio + ", '" + numExp + "', '" + codCampo + "'," + valor + ")";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);

//            st = con.prepareStatement(query);
//            st.setInt(1, codOrganizacion);
//            st.setString(2, codProcedimiento);
//            st.setInt(3, ejercicio);
//            st.setString(4, numExp);
//            st.setInt(5, codTramite);
//            st.setInt(6, ocurrenciaTramite);
//            st.setString(7, codCampo);
//            st.setInt(8, valor);
            int res = st.executeUpdate();
            return res > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error guardando campo suplementario " + codCampo + " para expediente " + numExp);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    private boolean grabarCampoSuplementarioFechaExpediente(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, String valor, Connection con) throws Exception {
        PreparedStatement st = null;
        String query = "";
        ResultSet rs = null;
        try {
            String sql = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TFE, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where TFE_COD = '" + codCampo + "' AND TFE_NUM = '" + numExp + "'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TFE, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " SET TFE_VALOR = '" + valor + "'"
                        + " WHERE TFE_NUM = '" + numExp + "' AND TFE_COD = '" + codCampo + "'";
            } else {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_TFE, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                        + " (TFE_MUN, TFE_EJE, TFE_NUM, TFE_COD, TFE_VALOR)"
                        + " values (" + codOrganizacion + ", " + ejercicio + ", '" + numExp + "', '" + codCampo + "','" + valor + "')";

            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);

            int res = st.executeUpdate();
            return res > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error guardando campo suplementario " + codCampo + " para expediente " + numExp);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExp
     * @param codTramite
     * @param ocurrenciaTramite
     * @param codCampo
     * @param valor
     * @param tipoDato
     * @param con
     * @return
     * @throws Exception
     */
    public boolean grabarCampoSuplementarioTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, Object valor, int tipoDato, Connection con) throws Exception {
        boolean result = false;
        switch (tipoDato) {
            case ConstantesMeLanbide50.TIPOS_DATOS_SUPLEMENTARIOS.NUMERICO:
                //result = grabarCampoSuplementarioNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, codCampo, (Integer)valor, con);
                //result = grabarCampoSuplementarioNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide50.TRAM_RESOLUCIONINSP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)), 1, codCampo, (Integer)valor, con);
                result = grabarCampoSuplementarioNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, 1, codCampo, (Integer) valor, con);

                break;
            case ConstantesMeLanbide50.TIPOS_DATOS_SUPLEMENTARIOS.FECHA:
                //result = grabarCampoSuplementarioFechaTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, codCampo, valor.toString(), con);
                //result = grabarCampoSuplementarioFechaTramite(codOrganizacion, codProcedimiento, ejercicio, numExp,Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide50.TRAM_RESOLUCIONINSP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)), 1, codCampo, valor.toString(), con);
                result = grabarCampoSuplementarioFechaTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, 1, codCampo, valor.toString(), con);
                break;
        }
        return result;
    }

    /**
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param ejercicio
     * @param numExp
     * @param codTramite
     * @param ocurrenciaTramite
     * @param camposSuplementarios
     * @param con
     * @return
     * @throws Exception
     */
    public boolean grabarCamposSuplementariosTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, List<CampoSuplementario> camposSuplementarios, Connection con) throws Exception {
        boolean result = false;
        if (camposSuplementarios.size() > 0) {
            int tipoDato;
            for (CampoSuplementario campo : camposSuplementarios) {
                tipoDato = campo.getTipoDato();
                switch (tipoDato) {
                    case ConstantesMeLanbide50.TIPOS_DATOS_SUPLEMENTARIOS.NUMERICO:
                        //result = grabarCampoSuplementarioNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, campo.getCodCampo(), (Integer)campo.getValor(), con);
                        //result = grabarCampoSuplementarioNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide50.TRAM_RESOLUCIONINSP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)),1, campo.getCodCampo(), (Integer)campo.getValor(), con);
                        result = grabarCampoSuplementarioNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, 1, campo.getCodCampo(), (Integer) campo.getValor(), con);
                        break;
                    case ConstantesMeLanbide50.TIPOS_DATOS_SUPLEMENTARIOS.FECHA:
                        //result = grabarCampoSuplementarioFechaTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, campo.getCodCampo(), campo.getValor().toString(), con);
                        result = grabarCampoSuplementarioFechaTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, 1, campo.getCodCampo(), campo.getValor().toString(), con);
                        break;
                }
            }
        }
        return result;
    }

    /**
     *
     * @param codOrganizacion
     * @param ejercicio
     * @param numExp
     * @param ocurrencia
     * @param con
     * @return
     * @throws Exception
     */
    public Long getCodigoUltimoTramiteAbierto(int codOrganizacion, String ejercicio, String numExp, Long ocurrencia, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Long cod = null;
        log.debug("sql = " + numExp);
        String[] parts = numExp.split("/");
        log.debug("procedimiento = " + parts[1]);
        try {
            String query = "select CRO_TRA from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_E_CRO, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " where CRO_MUN = " + codOrganizacion
                    + " and CRO_PRO = '" + parts[1] + "'"
                    + " and CRO_EJE = " + ejercicio
                    + " and CRO_NUM = '" + numExp + "'"
                    + " and CRO_OCU = " + ocurrencia
                    + " and CRO_FEF is null";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            List<Long> listaOcurrencias = new ArrayList<Long>();
            Long act = null;
            while (rs.next()) {
                act = rs.getLong("CRO_TRA");
                if (!rs.wasNull()) {
                    listaOcurrencias.add(act);
                }
            }
            cod = act;
            /*if(listaOcurrencias.size() > 0)
            {
                Statement st2 = null;
                ResultSet rs2 = null;
                try
                {

                    String query2 = "select MAX(TRA_COU) as TRA_COU from "+ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_TRAMITES, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                                    +" where TRA_MUN = "+codOrganizacion
                                    +" and TRA_PRO = '"+ConstantesMeLanbide50.CODIGO_PROCEDIMIENTO_MELANBIDE50+"'"
                                    +" and TRA_COD in(";
                    if(log.isDebugEnabled()) 
                        log.debug("sql = " + query);
                    for(int i = 0; i < listaOcurrencias.size(); i++)
                    {
                        if(i > 0)
                        {
                            query2 += ", ";
                        }
                        query2 += listaOcurrencias.get(i);
                    }
                    query2 += ") and tra_cou not like '90%' ";
                    st2 = con.createStatement();
                    rs2 = st2.executeQuery(query2);
                    if(rs2.next())
                    {
                        cod = rs2.getLong("TRA_COU");
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido un error recuperando el trámite abierto ("+numExp+")", ex);
                    throw new Exception(ex);
                }
                finally
                {
                    try
                    {
                        if(log.isDebugEnabled()) 
                            log.debug("Procedemos a cerrar el statement y el resultset");
                        if(st2!=null) 
                            st2.close();
                        if(rs2!=null)
                            rs2.close();
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
            }*/
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el trámite abierto (" + numExp + ")", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return cod;
    }

    /**
     *
     * @param codTramite
     * @param numExpediente
     * @param con
     * @return
     * @throws MeLanbide50Exception
     */
    public String getNumDocumento(int codTramite, String numExpediente, Connection con) throws MeLanbide50Exception {
        String salida = "";
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*)as num FROM E_CRD WHERE CRD_NUM='" + numExpediente + "' AND CRD_TRA=7 AND CRD_FINF IS NULL";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                salida = rs.getString("NUM");
            }
            return salida;
        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar la descripción del municipio: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    /**
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws MeLanbide50Exception
     */
    public String getErrorEspecialCargarExpedientes50(String numExpediente, Connection con) throws MeLanbide50Exception {
        String salida = "";
        Statement st = null;
        ResultSet rs = null;
        //Obtenemos la fecha del dia y hora minuto y milisegundo actual para filtrar los errores generados
        //en fecha obtenemos la fecha y hora del sistema
        Calendar fecha = new GregorianCalendar();
        //Obtenemos el valor del año, mes, día,
        //hora, minuto y segundo del sistema
        //usando el método get y el parámetro correspondiente
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);

        String fechaHoy = dia + mes + año + " " + hora + minuto + segundo;
        log.debug("fechaHoy" + fechaHoy);
        try {
            String sql = "select * from reg_err where reg_err_fec_error='23/11/15 10:10:07,000000000' and reg_err_men='Error en la funcion insertarExpedienteElectronico'";
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                salida = rs.getString("reg_err_excep_men");
            }
            return salida;
        } catch (SQLException e) {

            throw new MeLanbide50Exception("Error al recuperar la descripción del error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }

            } catch (SQLException e) {
            }
        }
    }

    /**
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws MeLanbide50Exception
     * @throws Exception
     */
    public List<DocumentosVO> getDatosDocumentos(String numExpediente, Connection con) throws MeLanbide50Exception, Exception {
        Statement st = null;
        ResultSet rs = null;
        DocumentosVO docmentos = new DocumentosVO();
        List<DocumentosVO> listDocumentos = new ArrayList<DocumentosVO>();
        try {
            String sql = "SELECT  "
                    + "R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_TIP_DOC AS TIPO_DOCUMENTO, "
                    + "R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_NOM_DOC  AS NOMBRE_DOCUMENTO, "
                    + "R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_ORGANO AS ORGANO, "
                    + "R_DOC_APORTADOS_ANTERIOR.R_DOC_APORTADOS_FEC_DOC AS FECHA, "
                    + "R_DOC_APORTADOS_TIP_DOCUMENTAL as TIPO_DOCUMENTAL "
                    + "FROM E_EXR "
                    + "inner JOIN R_DOC_APORTADOS_ANTERIOR ON  "
                    + "exr_dep=r_doc_aportados_dep and  "
                    + "exr_uor= r_doc_aportados_uor and  "
                    + "exr_tip=r_doc_aportados_tip and "
                    + "exr_eje=r_doc_aportados_eje and "
                    + "r_doc_aportados_num=EXR_NRE "
                    + "WHERE EXR_NUM='" + numExpediente + "'";
            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                docmentos = (DocumentosVO) MeLanbide50MappingUtils.getInstance().map(rs, DocumentosVO.class);
                listDocumentos.add(docmentos);
                docmentos = new DocumentosVO();
            }
            return listDocumentos;
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando Datos de documentos ", e);
            throw new MeLanbide50Exception("Error recuperando Datos de documentos : " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    /**
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception
     */
    public String getDniTercero(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String documento = null;
        try {
            String sql = "SELECT HTE_DOC FROM E_EXT "
                    + "INNER JOIN  E_ROL ON ROL_PRO=EXT_PRO AND ROL_PDE=1 and rol_cod=ext_rol "
                    + "INNER JOIN T_HTE ON HTE_TER=EXT_TER AND HTE_NVR=EXT_NVR "
                    + "WHERE EXT_NUM='" + numExpediente + "'";

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                documento = rs.getString("HTE_DOC");
            }
            return documento;
        } catch (SQLException e) {
            throw new MeLanbide50Exception("Error al recuperar la descripción del municipio: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Recupera los datos necesarios para pasar al WS de Registro Centros
     *
     * @param codCentro codigo del Centro
     * @param codUbic codigo de Ubicacion del centro
     * @param con
     * @return String[] con corrServicio y corrSubservicio
     * @throws Exception
     */
    public String[] getCodigosCorr(String codCentro, String codUbic, Connection con) throws Exception {
        log.info("Entra en getCodigosCorr de " + this.getClass().getSimpleName() + " - Centro: " + codCentro + " - Ubicación: " + codUbic);
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        String[] codigosCorr = new String[2];
        try {
            query = "SELECT gen_cen_servicio_corr,gen_cen_subservicio_corr"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE gen_cen_cod_centro = '" + codCentro + "'"
                    + " AND gen_cen_estado = 'HO'"
                    + " AND gen_cen_cod_ubic='" + codUbic + "'"
                    + " AND cen_subservic_cod='00051'";

            log.debug("query: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                codigosCorr[0] = rs.getString("gen_cen_servicio_corr");
                codigosCorr[1] = rs.getString("gen_cen_subservicio_corr");
                log.debug("corr_ser: " + codigosCorr[0]);
                log.debug("corr_SUB: " + codigosCorr[1]);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando codigos Servicio y SubServicio: ", e);
            throw new Exception(e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return codigosCorr;
    }

    /**
     *
     * Metodo que obtiene el numero de censo de un centro
     *
     * @param codCentro Código del centro
     * @param codUbic Código de la ubicación
     * @param corrSubservicio corrSubservicio
     * @param con Conexion
     * @return String con el numero Censo CENFOR
     * @throws Exception
     */
    public String getNumeroCensoCenFor(String codCentro, String codUbic, String corrSubservicio, Connection con) throws Exception {
        log.debug("Entra en getNumeroCensoCenFor de " + this.getClass().getSimpleName() + " - Centro: " + codCentro + " - Ubicación: " + codUbic + " - corrSubservicio: " + corrSubservicio);
        String numCenso = null;
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = " select CEN_NUM_REG_AUT from " + ConfigurationParameter.getParameter(ConstantesMeLanbide50.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide50.FICHERO_PROPIEDADES)
                    + " WHERE GEN_CEN_COD_CENTRO = '" + codCentro + "'"
                    + " AND GEN_CEN_COD_UBIC = '" + codUbic + "'"
                    + " AND GEN_CEN_SUBSERVICIO_CORR   = '" + corrSubservicio + "'";
            log.info("query: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                numCenso = rs.getString("CEN_NUM_REG_AUT");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando número de Censo : ", e);
            throw new Exception(e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numCenso;
    }

}
