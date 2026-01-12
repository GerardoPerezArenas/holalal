/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide41.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.flexia.integracion.moduloexterno.melanbide41.manager.MeLanbide41Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConstantesMeLanbide41;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.MeLanbide41Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.MeLanbide41MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.MeLanbide41Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.capacidad.CapacidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.disponibilidad.DisponibilidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.dotacion.DotacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.identificacionesp.IdentificacionEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.interesadoexpediente.InteresadoExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.material.MaterialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.servicios.ServiciosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.ubicacion.UbicacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide41DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide41DAO.class);

    //Instancia
    private static MeLanbide41DAO instance = null;

    private MeLanbide41DAO() {

    }

    public static MeLanbide41DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide41DAO.class) {
                instance = new MeLanbide41DAO();
            }
        }
        return instance;
    }

    // Metodos de Recupracion de datos
    public List<EspecialidadesVO> getDatosEspecialidades(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EspecialidadesVO> listEsp = new ArrayList<EspecialidadesVO>();
        EspecialidadesVO Especialidad = new EspecialidadesVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where ESP_NUM = '" + numExpediente + "' AND ESP_ACRED = 0 ORDER BY ESP_DENOM";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Especialidad = (EspecialidadesVO) MeLanbide41MappingUtils.getInstance().map(rs, EspecialidadesVO.class);
                listEsp.add(Especialidad);
                Especialidad = new EspecialidadesVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
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
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                DesplegableMotDeneg.setCodigo(rs.getString("codigo"));
                DesplegableMotDeneg.setDescripcion(rs.getString("descripcion"));
                //DesplegableMotDeneg = (ValorCampoDesplegableModuloIntegracionVO)MeLanbide50MappingUtils.getInstance().map(rs, ValorCampoDesplegableModuloIntegracionVO.class);
                listMotDeneg.add(DesplegableMotDeneg);
                DesplegableMotDeneg = new ValorCampoDesplegableModuloIntegracionVO();
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los motivos de denegacion", ex);
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
        return listMotDeneg;
    }

    public List<EspecialidadesVO> getDatosEspecialidadesInterfaz(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EspecialidadesVO> listEsp = new ArrayList<EspecialidadesVO>();
        EspecialidadesVO Especialidad = new EspecialidadesVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where ESP_NUM = '" + numExpediente + "' " +/*AND ESP_ACRED = 0*/ "ORDER BY ESP_DENOM";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Especialidad = (EspecialidadesVO) MeLanbide41MappingUtils.getInstance().map(rs, EspecialidadesVO.class);
                listEsp.add(Especialidad);
                Especialidad = new EspecialidadesVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
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
        return listEsp;
    }

    public List<String> getDatosEspecialidadMotDeneg(String idEspecialidad, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<String> listMotDeneg = new ArrayList<String>();
        try {
            StringBuilder query = new StringBuilder("select * from ");
            query.append(ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES_MOTDENEG, ConstantesMeLanbide41.FICHERO_PROPIEDADES));
            query.append(" where ID = ");
            query.append(idEspecialidad);
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                listMotDeneg.add(rs.getString("ESP_MOT_DENEG"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
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
        return listMotDeneg;
    }

    public EspecialidadesVO getEspecialidadPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        EspecialidadesVO Especialidad = new EspecialidadesVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where ESP_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY ESP_DENOM";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Especialidad = (EspecialidadesVO) MeLanbide41MappingUtils.getInstance().map(rs, EspecialidadesVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando EspecialidaD Especifica por codigo" + numExpediente + " -" + id, ex);
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
        return Especialidad;
    }

    public List<ServiciosVO> getDatosServicios(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ServiciosVO> listsServ = new ArrayList<ServiciosVO>();
        ServiciosVO Servicio = new ServiciosVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_SERVICIOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where SER_NUM = '" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Servicio = (ServiciosVO) MeLanbide41MappingUtils.getInstance().map(rs, ServiciosVO.class);
                listsServ.add(Servicio);
                Servicio = new ServiciosVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Servicios Higienico-Sanitarios ", ex);
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
        return listsServ;
    }

    public ServiciosVO getServicioPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ServiciosVO _datoReturn = new ServiciosVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_SERVICIOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where SER_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY SER_DESC";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (ServiciosVO) MeLanbide41MappingUtils.getInstance().map(rs, ServiciosVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando aseo servicio higienic-sanitario por codigo" + numExpediente + " -" + id, ex);
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
        return _datoReturn;
    }

    public List<DisponibilidadVO> getDatosDisponibilidad(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DisponibilidadVO> lista = new ArrayList<DisponibilidadVO>();
        DisponibilidadVO Disponibilidad = new DisponibilidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DISPONRECURSOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where DRE_NUM = '" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Disponibilidad = (DisponibilidadVO) MeLanbide41MappingUtils.getInstance().map(rs, DisponibilidadVO.class);
                lista.add(Disponibilidad);
                Disponibilidad = new DisponibilidadVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Servicios Higienico-Sanitarios ", ex);
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
        return lista;
    }

    public DisponibilidadVO getDisponibilidadPorCodigo(String numExpediente, String id, String idespsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DisponibilidadVO Disponibilidad = new DisponibilidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DISPONRECURSOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where DRE_NUM = '" + numExpediente + "' AND ID=" + id + " AND ID_ESPSOL=" + idespsol + "  ORDER BY DRE_CODCP";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Disponibilidad = (DisponibilidadVO) MeLanbide41MappingUtils.getInstance().map(rs, DisponibilidadVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Disponibilidad de recursos por codigo ", ex);
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
        return Disponibilidad;
    }

    public DisponibilidadVO getDisponibilidadPorCodigoEspSol_NumExp(String numExpediente, Integer idespsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DisponibilidadVO Disponibilidad = new DisponibilidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DISPONRECURSOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where DRE_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idespsol != null ? idespsol : "null") + "  ORDER BY DRE_CODCP";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Disponibilidad = (DisponibilidadVO) MeLanbide41MappingUtils.getInstance().map(rs, DisponibilidadVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Disponibilidad de recursos por codigo de especialidad solicitada y Num Expediente ", ex);
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
        return Disponibilidad;
    }

    public List<CapacidadVO> getDatosCapacidad(String numExpediente, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<CapacidadVO> lista = new ArrayList<CapacidadVO>();
        CapacidadVO Capacidad = new CapacidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_CAPACIDADINSTALA, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where CAIN_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Capacidad = (CapacidadVO) MeLanbide41MappingUtils.getInstance().map(rs, CapacidadVO.class);
                lista.add(Capacidad);
                Capacidad = new CapacidadVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Capacidad de Instalaciones", ex);
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
        return lista;
    }

    public CapacidadVO getCapacidadPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        CapacidadVO _datoReturn = new CapacidadVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_CAPACIDADINSTALA, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where CAIN_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY CAIN_IDEF";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (CapacidadVO) MeLanbide41MappingUtils.getInstance().map(rs, CapacidadVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando capcidad de instalaciones por codigo" + numExpediente + " -" + id, ex);
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
        return _datoReturn;
    }

    public List<DotacionVO> getDatosDotacion(String numExpediente, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DotacionVO> lista = new ArrayList<DotacionVO>();
        DotacionVO Dotacion = new DotacionVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DOTACION, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where DOT_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Dotacion = (DotacionVO) MeLanbide41MappingUtils.getInstance().map(rs, DotacionVO.class);
                lista.add(Dotacion);
                Dotacion = new DotacionVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Dotacion", ex);
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
        return lista;
    }

    public DotacionVO getDotacionPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DotacionVO _datoReturn = new DotacionVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DOTACION, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where DOT_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY DOT_DET";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (DotacionVO) MeLanbide41MappingUtils.getInstance().map(rs, DotacionVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Dotacion de instalaciones por codigo" + numExpediente + " -" + id, ex);
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
        return _datoReturn;
    }

    public List<MaterialVO> getDatosMaterial(String numExpediente, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<MaterialVO> lista = new ArrayList<MaterialVO>();
        MaterialVO Material = new MaterialVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_MATERIALCONSUMO, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where MAC_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Material = (MaterialVO) MeLanbide41MappingUtils.getInstance().map(rs, MaterialVO.class);
                lista.add(Material);
                Material = new MaterialVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Material de Consumo", ex);
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
        return lista;
    }

    public MaterialVO getMaterialPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        MaterialVO _datoReturn = new MaterialVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_MATERIALCONSUMO, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where MAC_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY MAC_DET";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (MaterialVO) MeLanbide41MappingUtils.getInstance().map(rs, MaterialVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Material de consumo por codigo" + numExpediente + " -" + id, ex);
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
        return _datoReturn;
    }

    public List<IdentificacionEspVO> getDatosIdentificacionEsp(String numExpediente, Integer idEpsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<IdentificacionEspVO> lista = new ArrayList<IdentificacionEspVO>();
        IdentificacionEspVO identificacionEsp = new IdentificacionEspVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_IDENTIFICACIONESP, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where IDE_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idEpsol != null ? idEpsol : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                identificacionEsp = (IdentificacionEspVO) MeLanbide41MappingUtils.getInstance().map(rs, IdentificacionEspVO.class);
                lista.add(identificacionEsp);
                identificacionEsp = new IdentificacionEspVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Lista de Identificacion de Especialidad", ex);
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
        return lista;
    }

    public IdentificacionEspVO getIdentificacionEspPorCodigo(String numExpediente, String id, String idespsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        IdentificacionEspVO Identificacion = new IdentificacionEspVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_IDENTIFICACIONESP, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where IDE_NUM = '" + numExpediente + "' AND ID=" + id + " AND ID_ESPSOL=" + idespsol + "  ORDER BY IDE_DENESP";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Identificacion = (IdentificacionEspVO) MeLanbide41MappingUtils.getInstance().map(rs, IdentificacionEspVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Lista de Identificacion de Especialidad ", ex);
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
        return Identificacion;
    }

    public IdentificacionEspVO getIdentificacionEspPorCodigoEspSol_NumExp(String numExpediente, Integer idespsol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        IdentificacionEspVO Identificacion = new IdentificacionEspVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_IDENTIFICACIONESP, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " where IDE_NUM = '" + numExpediente + "' AND ID_ESPSOL=" + (idespsol != null ? idespsol : "null") + "  ORDER BY IDE_DENESP";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Identificacion = (IdentificacionEspVO) MeLanbide41MappingUtils.getInstance().map(rs, IdentificacionEspVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Lista de Identificacion de Especialidad ", ex);
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
        return Identificacion;
    }

    public String getTipoCentro(int codOrg, String proce, int ejercicio, String numExpediente, Integer ocurrencia, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String tipoCentro = "";
        try {
            String query = null;
            query = "SELECT TDEXT_VALOR FROM E_TDEXT WHERE TDEXT_MUN = " + codOrg + " AND TDEXT_PRO = '" + proce
                    + "' AND TDEXT_EJE=" + ejercicio + " AND TDEXT_NUM = '" + numExpediente + "' TDEXT_OCU = " + ocurrencia
                    + " AND TDEXT_TRA = 5";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                tipoCentro = rs.getString("TDEXT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el tipo de centro", ex);
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
        return tipoCentro;
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
            String sql = "Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_CER_CERTIFICADOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " ORDER BY DESC_ESPECIALIDAD_C ASC";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                certificado = (CerCertificadoVO) MeLanbide41MappingUtils.getInstance().map(rs, CerCertificadoVO.class);
                listaCertificados.add(certificado);
                certificado = new CerCertificadoVO();
            }//while(rs.next())
        } catch (Exception e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw new Exception(e);
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
        }//try-catch-finally
        if (log.isDebugEnabled()) {
            log.debug("getCertificados() : END");
        }
        return listaCertificados;
    }//getCertificados

    // Metodos de Insercion en BBDD
    public int crearNuevaEspecialidad(EspecialidadesVO nuevEsp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide41.SEQ_RGCF_ESPECIALIDADES, ConstantesMeLanbide41.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            //opeCorrecta = false;
            log.error("Se ha producido un error al Insertar un nueva Especialidad" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
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
    }

    public void crearNuevaEspecialidadMotDeneg(int idEspecialidad, String idMotDeneg, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder();
        try {
            query.append("INSERT INTO ");
            query.append(ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES_MOTDENEG, ConstantesMeLanbide41.FICHERO_PROPIEDADES));
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
            //return opeCorrecta;
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
    }

    public boolean crearNuevoServicio(ServiciosVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_SERVICIOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + "(ID, SER_NUM, SER_DESC, SER_UBIC, SER_SUPE) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide41.SEQ_RGCF_SERVICIOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES) + ".NextVal,'" + nuevDato.getNumExp() + "','" + nuevDato.getDescripcion() + "','"
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar un nuevo aseo sevicio higienico-sanitario" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevaCapacidad(CapacidadVO nuevDato, Connection con) throws Exception {
        log.info("Entramos en crearNuevaCapacidad de " + this.getClass().getSimpleName());
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_CAPACIDADINSTALA, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + "(ID, ID_ESPSOL, CAIN_NUM, CAIN_IDEF, CAIN_UBI, CAIN_SUP) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide41.SEQ_RGCF_CAPACIDADINSTALA, ConstantesMeLanbide41.FICHERO_PROPIEDADES) + ".NextVal"
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar una capacidad de instalacinoes " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevaDotacion(DotacionVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            /*SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fechaAdqui = formatoFecha.format(nuevDato.getFechaAdq());*/

            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DOTACION, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + "(ID, ID_ESPSOL, DOT_NUM, DOT_CANT, DOT_DET, DOT_FAD) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide41.SEQ_RGCF_DOTACION, ConstantesMeLanbide41.FICHERO_PROPIEDADES) + ".NextVal "
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar una Dotacion de instalacinoes " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoMaterial(MaterialVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_MATERIALCONSUMO, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + "(ID, ID_ESPSOL,  MAC_NUM, MAC_CANT, MAC_DET) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide41.SEQ_RGCF_MATERIALCONSUMO, ConstantesMeLanbide41.FICHERO_PROPIEDADES) + ".NextVal"
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar material de consumo  " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    // Metodos UPDATE  BBDD
    public boolean modificarEspecialidad(EspecialidadesVO modEsp, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modifiacar epecialidad Especialidad" + " - "
                    + modEsp.getNumExp() + " - " + modEsp.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarServicio(ServiciosVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_SERVICIOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Aseo Servicio Higienico Sanitario" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarDisponibilidad(DisponibilidadVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DISPONRECURSOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Una linea de Disponibilidad de recursos" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean actualizarDisponibilidadDesdeListEspSol(DisponibilidadVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DISPONRECURSOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " SET DRE_CODCP='" + nuevoDato.getCodCp() + "'"
                    + " WHERE DRE_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId() + " AND ID_ESPSOL=" + nuevoDato.getIdEspSol();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Una linea de Disponibilidad de recursos desde la lista de especialuidades solicitadas" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarIdentificacionEsp(IdentificacionEspVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_IDENTIFICACIONESP, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " SET IDE_HORAS=" + (nuevoDato.getHoras() != null ? "'" + nuevoDato.getHoras() + "'" : "null")
                    + ", IDE_ALUM=" + (nuevoDato.getAlumnos() != null ? "'" + nuevoDato.getAlumnos() + "'" : "null")
                    + ", IDE_CERTP=" + (nuevoDato.getCertPro() != null ? nuevoDato.getCertPro() : "null")
                    + ", IDE_RDER='" + nuevoDato.getRealDecRegu() + "'"
                    + ", IDE_BOEFP='" + nuevoDato.getBoeFecPub() + "'"
                    + ", IDE_DESADAP='" + nuevoDato.getDescripAdapt() + "'"
                    + ", IDE_OBSADAP='" + nuevoDato.getObservAdapt() + "'"
                    + ", IDE_ALUM_AUT=" + (nuevoDato.getAlumnosAut() != null ? nuevoDato.getAlumnosAut() : "null")
                    + " WHERE IDE_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId() + " AND ID_ESPSOL=" + nuevoDato.getIdEspSol();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Una linea de la lista Identificacion de  Especialidad solicitada" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean actualizarIdentificacionEspDesdeListEspSol(IdentificacionEspVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_IDENTIFICACIONESP, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Identificacion de  Especialidad solicitada desde la lista de especialdad solicitada" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarCapacidad(CapacidadVO nuevoDato, Connection con) throws Exception {
        log.debug("modificarCapacidad BEGIN");
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_CAPACIDADINSTALA, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Capacidad de Instalaciones" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarDotacion(DotacionVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            /*SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fechaAdqui = formatoFecha.format(nuevoDato.getFechaAdq());*/

            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DOTACION, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Dotacion de Instalaciones" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarMaterial(MaterialVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_MATERIALCONSUMO, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Material de consumo" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    // Metodos Delete de BBDD
    public int eliminarEspecialidad(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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

    public int eliminarEspecialidadMotDeneg(Integer idEspecialidad, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_ESPECIALIDADES_MOTDENEG, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " WHERE  ID=" + (idEspecialidad != null ? idEspecialidad : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando motDeneg de Especialiddad " + idEspecialidad, ex);
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

    public int eliminarServicio(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_SERVICIOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public int eliminarDisponibilidadDesdeListEspSol(String numExp, Integer id, Integer idEspSol, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DISPONRECURSOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public int eliminarIdentificacionEspDesdeListEspSol(String numExp, Integer id, Integer idEspSol, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_IDENTIFICACIONESP, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public int eliminarCapacidad(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_CAPACIDADINSTALA, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " WHERE CAIN_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Capacidad de instalacinoes" + numExp + " - " + id, ex);
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

    public int eliminarDotacion(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DOTACION, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " WHERE DOT_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Dotacion  de instalacinoes" + numExp + " - " + id, ex);
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

    public int eliminarMaterial(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_MATERIALCONSUMO, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
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
        return id;
    }

    public void crearNuevaDisponibilidad(int id, String numExp, String codCP, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            int idDisp = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide41.SEQ_RGCF_DISPONRECURSOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_DISPONRECURSOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + "(ID, ID_ESPSOL, DRE_NUM, DRE_CODCP) "
                    + " VALUES (" + id + "," + idDisp + ",'" + numExp + "','" + codCP + "'"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex) {
            log.debug("Se ha producido un error al Insertar una nueva disponibilidad de recursos" + " - " + ex.getMessage() + ex);
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
    }

    public boolean crearNuevaIdentificacioEsp(IdentificacionEspVO datoNuevo, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide41.SEQ_RGCF_IDENTIFICACIONESP, ConstantesMeLanbide41.FICHERO_PROPIEDADES), con);
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_RGCF_IDENTIFICACIONESP, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + "(ID, ID_ESPSOL, IDE_NUM, IDE_CODESP, IDE_DENESP, IDE_HORAS, IDE_ALUM, IDE_CERTP, IDE_RDER, IDE_BOEFP, IDE_DESADAP, IDE_OBSADAP, IDE_ALUM_AUT)"
                    + " VALUES (" + id + "," + datoNuevo.getIdEspSol() + ",'" + datoNuevo.getNumExp() + "','" + datoNuevo.getCodCp() + "','" + datoNuevo.getDenomEsp() + "'"
                    + "," + (datoNuevo.getHoras() != null ? "'" + datoNuevo.getHoras() + "'" : "null")
                    + "," + (datoNuevo.getAlumnos() != null ? "'" + datoNuevo.getAlumnos() + "'" : "null")
                    + "," + (datoNuevo.getCertPro() != null ? datoNuevo.getCertPro() : "null")
                    + ",'" + datoNuevo.getRealDecRegu() + "'"
                    + ",'" + datoNuevo.getBoeFecPub() + "'"
                    + ",'" + datoNuevo.getDescripAdapt() + "'"
                    + ",'" + datoNuevo.getObservAdapt() + "'"
                    + "," + (datoNuevo.getAlumnosAut() != null ? datoNuevo.getAlumnosAut() : "null")
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar nuevos detalles de la identificacon de la especialidade " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
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
        //boolean opeCorrecta = true;
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
     * corresponde en la tabla flbgen.FPE_CEN_CENTROSREG_TIPOENT columna ID_DCF.
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

            query = "SELECT ID_DCF FROM flbgen.FPE_TIPOORG_DCF_TTDTIPOR "
                    + "WHERE DENOMINACION LIKE '%" + valor + "%'";

            if (log.isInfoEnabled()) {
                log.info("sql = " + query);
            }
            int i = 1;
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                salida = rs.getString("ID_DCF");
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
     * ******************
     */
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
     * es.altia.flexia.integracion.moduloexterno.melanbide41.util.MeLanbide41Exception
     * @throws java.sql.SQLException
     */
    public String getDocumentoInteresadoRolDefecto(int codOrganizacion, String numExpediente, Connection con) throws MeLanbide41Exception, SQLException {
        String documento = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        String[] datos = numExpediente.split("/");
        String ejercicio = datos[0];
        String codProcedimiento = datos[1];

        String sql = "SELECT HTE_DOC,HTE_TID,HTE_DOC FROM E_EXT,T_HTE,E_ROL WHERE EXT_MUN=? AND EXT_NUM=? AND EXT_EJE=? AND EXT_PRO=? "
                + //"AND EXT_TER=T_HTE.HTE_TER AND (HTE_TID=4 OR HTE_TID=5) "+
                "AND EXT_TER=T_HTE.HTE_TER "
                + "AND EXT_NVR=T_HTE.HTE_NVR AND ROL_MUN=? AND ROL_MUN=EXT_MUN AND "
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
            throw new MeLanbide41Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
        } catch (SQLException e) {
            throw new MeLanbide41Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
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

    public String getDocumentoInteresadoRolDefectoAltaExpFormaLANF(int codOrganizacion, String numExpediente, Connection con) throws MeLanbide41Exception, SQLException {
        String documento = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        String[] datos = numExpediente.split("/");
        String ejercicio = datos[0];
        String codProcedimiento = datos[1];

        String sql = "SELECT HTE_DOC,HTE_TID,HTE_DOC FROM E_EXT,T_HTE,E_ROL WHERE EXT_MUN=? AND EXT_NUM=? AND EXT_EJE=? AND EXT_PRO=? "
                + "AND EXT_TER=T_HTE.HTE_TER AND EXT_NVR=T_HTE.HTE_NVR AND ROL_MUN=? AND ROL_MUN=EXT_MUN AND "
                + "ROL_PRO=EXT_PRO AND ROL_PRO=EXT_PRO AND ROL_PDE=1 AND ROL_COD=EXT_ROL";
        //AND (HTE_TID=4 OR HTE_TID=5)
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
            throw new MeLanbide41Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
        } catch (SQLException e) {
            throw new MeLanbide41Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
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
     * @param codCentro
     * @param con: Conexión a la BBDD
     * @return ArrayList de Objetos UbicacionVO con las ubicaciones y en caso
     * contrario, con el código de error producido
     * @throws MeLanbide41Exception
     * @throws java.sql.SQLException
     */
    public ArrayList<UbicacionVO> getUbicacionesCentro(String codCentro, Connection con) throws MeLanbide41Exception, SQLException {
        ArrayList<UbicacionVO> ubicaciones = new ArrayList<UbicacionVO>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = null;
        /*   String sql = "SELECT LAN_CENTROS_UBICACIONES.GEN_CEN_COD_UBIC AS CODUBICACION," +
                    " LAN_CENTROS_UBICACIONES.GEN_CEN_NOM_UBIC, LAN_CENTROS_UBICACIONES.GEN_GRAL_TIPO_VIA," +
                    " LAN_CENTROS_UBICACIONES.GEN_CEN_NOM_VIA,LAN_CENTROS_UBICACIONES.GEN_CEN_NUM_VIA," +
                    " LAN_CENTROS_UBICACIONES.GEN_GRAL_BISDUPLICADO,LAN_CENTROS_UBICACIONES.GEN_CEN_ESCALERA," +
                    " LAN_CENTROS_UBICACIONES.GEN_CEN_PISO,LAN_CENTROS_UBICACIONES.GEN_CEN_PUERTA," +
                    " LAN_CENTROS_UBICACIONES.GEN_PRO_COD_PROV, LAN_CENTROS_UBICACIONES.GEN_MUN_COD_MUN, LAN_CENTROS_UBICACIONES.GEN_COD_CODPOSTAL" +
                    " FROM LAN_CENTROS_UBICACIONES" +
                    " WHERE LAN_CENTROS_UBICACIONES.GEN_CEN_COD_CENTRO=? " +                    
                    " AND LAN_CENTROS_UBICACIONES.GEN_CEN_ESTADO ='HO'";
         */
        try {
            sql = "SELECT UBIC.GEN_CEN_COD_UBIC AS CODUBICACION, UBIC.GEN_CEN_NOM_UBIC,"
                    + " UBIC.GEN_GRAL_TIPO_VIA, UBIC.GEN_CEN_NOM_VIA, UBIC.GEN_CEN_NUM_VIA,"
                    + " UBIC.GEN_GRAL_BISDUPLICADO, UBIC.GEN_CEN_ESCALERA, UBIC.GEN_CEN_PISO,"
                    + " UBIC.GEN_CEN_PUERTA, UBIC.GEN_PRO_COD_PROV, UBIC.GEN_MUN_COD_MUN,"
                    + " UBIC.GEN_COD_CODPOSTAL"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_CENFOR_UBICACION, ConstantesMeLanbide41.FICHERO_PROPIEDADES) + " UBIC"
                    + " JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide41.FICHERO_PROPIEDADES) + " SUBSERV"
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

                if (codProvincia != null && codMunicipio != null && MeLanbide41Utils.isInteger(codProvincia) && MeLanbide41Utils.isInteger(codMunicipio)) {

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

        } catch (MeLanbide41Exception e) {
            throw new MeLanbide41Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        } catch (NumberFormatException e) {
            throw new MeLanbide41Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        } catch (SQLException e) {
            throw new MeLanbide41Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
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
     * @return ArrayList String: Colección con los centros homologados
     * @throws MeLanbide41Exception si ocurre algún error
     * @throws java.sql.SQLException
     */
    public ArrayList<String> getCodCentro(String documento, Connection con) throws MeLanbide41Exception, SQLException {
        ArrayList<String> centros = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;

        try {
            /*    String sql = "SELECT LAN_CENTROS_COLABORADORES.GEN_CEN_COD_CENTRO "
                    + "FROM GEN_EMPRESARIO,LAN_CENTROS_COLABORADORES "
                    + "WHERE GEN_USU_NUM_DOC=? AND GEN_EMPRESARIO.GEN_EMP_CORR = LAN_CENTROS_COLABORADORES.GEN_EMP_CORR AND LAN_CENTROS_COLABORADORES.GEN_CEN_ESTADO ='HO'";*/

            sql = "SELECT CENTROS.GEN_CEN_COD_CENTRO "
                    + " FROM GEN_EMPRESARIO, " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_CENFOR_CENTROS, ConstantesMeLanbide41.FICHERO_PROPIEDADES) + " CENTROS "
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
            throw new MeLanbide41Exception("Error al recuperar el código de centro: " + e.getMessage());
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
     * @throws MeLanbide41Exception si ocurre algún error
     * @throws java.sql.SQLException
     */
    public String getDescProvincia(int codPais, int codProvincia, Connection con) throws MeLanbide41Exception, SQLException {
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
            throw new MeLanbide41Exception("Error al recuperar la descripción de la provincia: " + e.getMessage());
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
     * @throws MeLanbide41Exception si ocurre algún error
     * @throws java.sql.SQLException
     */
    public String getDescMunicipio(int codPais, int codProvincia, int codMunicipio, Connection con) throws MeLanbide41Exception, SQLException {
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
            throw new MeLanbide41Exception("Error al recuperar la descripción del municipio: " + e.getMessage());
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
     * Recupera el valor de un campo desplegable externo grabado en un trámite
     * de un expediente
     *
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param codCampo: Código del campo desplegable exerno
     * @param con: Conexión a la BBDD
     * @return
     * @throws Exception
     */
    public String getValorCampoDesplegableExternoTramite(int codOrganizacion, String numExpediente, int codTramite, int ocurrenciaTramite, String codCampo, Connection con) throws Exception {
        String salida = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";

        try {
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];

            query = "SELECT TDEXT_VALOR FROM E_TDEXT "
                    + "WHERE TDEXT_NUM=? AND TDEXT_EJE=? AND TDEXT_MUN=? AND TDEXT_COD=? AND TDEXT_TRA=? AND TDEXT_OCU=?";

            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExpediente);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, codCampo);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocurrenciaTramite);

            rs = ps.executeQuery();
            while (rs.next()) {
                salida = rs.getString("TDEXT_VALOR");
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
     * Recupera la ocurrencia más reciente de un determinado trámite de un
     * expedinete
     *
     * @param codTramite: Código del trámite
     * @param numExpediente: Número del expediente
     * @param codOrganizacion: Cód. organización
     * @param con: Conexión a la BBDD
     * @return int 0 -1 sino se ha podido recuperar
     * @throws java.sql.SQLException
     */
    public int getOcurrenciaMasRecienteTramite(int codTramite, String numExpediente, String codOrganizacion, Connection con) throws SQLException {
        int salida = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];

            String sql = "SELECT MAX(CRO_OCU) AS NUM FROM E_CRO WHERE CRO_NUM=? AND CRO_PRO=? AND CRO_MUN=? AND CRO_EJE=? AND CRO_TRA=?";

            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setString(i++, numExpediente);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, Integer.parseInt(codOrganizacion));
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, codTramite);
            rs = ps.executeQuery();

            while (rs.next()) {
                salida = rs.getInt("NUM");
            }

        } catch (SQLException e) {

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

    public ArrayList<InteresadoExpedienteVO> getListaInteresadosExpediente(int codOrganizacion, String numExpediente, Connection con) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        ArrayList resultado = new ArrayList();

        try {

            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];

            sql = "SELECT DOM_COD,EXT_TER,ROL_COD,EXT_NVR"
                    + " FROM T_DOM,E_EXT,E_EXP,E_ROL"
                    + " WHERE EXT_MUN = ? AND EXT_EJE = ? AND EXT_NUM = ?"
                    + " AND EXT_DOT = DOM_COD AND EXT_MUN = EXP_MUN AND EXT_EJE = EXP_EJE "
                    + " AND EXT_NUM = EXP_NUM AND EXT_PRO = ROL_PRO AND EXT_ROL = ROL_COD "
                    + " AND EXT_MUN = ROL_MUN ORDER BY MOSTRAR DESC, ROL_COD";

            if (log.isDebugEnabled()) {
                log.debug(sql);
            }

            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, codOrganizacion);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setString(i++, numExpediente);
            rs = ps.executeQuery();

            ArrayList listaDomicilios = new ArrayList();
            ArrayList listaTerceros = new ArrayList();
            ArrayList listaVersionesTercero = new ArrayList();
            while (rs.next()) {
                String codDomicilio = rs.getString("DOM_COD");
                String codTercero = rs.getString("EXT_TER");
                String numeroVersion = rs.getString("EXT_NVR");
                listaDomicilios.add(codDomicilio);
                listaTerceros.add(codTercero);
                listaVersionesTercero.add(numeroVersion);
            }
            rs.close();

            for (int j = 0; j < listaDomicilios.size(); j++) {
                String codDomicilio = (String) listaDomicilios.get(j);
                String codTercero = (String) listaTerceros.get(j);
                String verTercero = (String) listaVersionesTercero.get(j);

                sql = "SELECT T_DNN.*,EXT_TER,EXT_NVR,HTE_AP1,HTE_AP2,HTE_NOM,HTE_TLF,HTE_DCE,HTE_TID,HTE_DOC,PAI_NOM,PRV_NOM,MUN_NOM,"
                        + " EXT_ROL,MOSTRAR, EXT_NOTIFICACION_ELECTRONICA,ROL_DES,ROL_PDE,EXT_DOT,TVI_DES,VIA_NOM"
                        + " FROM E_EXT LEFT JOIN T_HTE ON E_EXT.ext_ter=T_HTE.hte_ter AND E_EXT.ext_nvr=T_HTE.hte_nvr,"
                        + " T_DNN LEFT JOIN T_TVI ON T_DNN.dnn_tvi=T_TVI.tvi_cod"
                        + " LEFT JOIN T_VIA ON T_DNN.dnn_pai=T_VIA.via_pai AND T_DNN.dnn_prv=T_VIA.via_prv AND T_DNN.dnn_mun=T_VIA.via_mun AND T_DNN.dnn_via=T_VIA.via_cod,"
                        + " E_ROL, "
                        + GlobalNames.ESQUEMA_GENERICO + "T_PAI T_PAI,"
                        + GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN,"
                        + GlobalNames.ESQUEMA_GENERICO + "T_PRV T_PRV"
                        + " WHERE EXT_MUN = ? AND EXT_EJE = ? AND EXT_NUM = ? AND EXT_TER = ? AND EXT_NVR = ?"
                        + " AND EXT_MUN = ROL_MUN AND ROL_PRO = ? AND EXT_ROL = ROL_COD AND EXT_PRO = ROL_PRO"
                        + " AND PAI_COD = DNN_PAI AND PRV_PAI = DNN_PAI AND MUN_PAI = DNN_PAI"
                        + " AND PRV_COD = DNN_PRV AND MUN_PRV = DNN_PRV AND MUN_COD = DNN_MUN "
                        + " AND DNN_DOM = ?"
                        + " ORDER BY EXT_ROL";

                if (log.isDebugEnabled()) {
                    log.debug(sql);
                }

                i = 1;
                ps = con.prepareStatement(sql);
                ps.setInt(i++, codOrganizacion);
                ps.setInt(i++, Integer.parseInt(ejercicio));
                ps.setString(i++, numExpediente);
                ps.setInt(i++, Integer.parseInt(codTercero));
                ps.setInt(i++, Integer.parseInt(verTercero));
                ps.setString(i++, codProcedimiento);
                ps.setInt(i++, Integer.parseInt(codDomicilio));
                rs = ps.executeQuery();

                while (rs.next()) {
                    //para dar formato al interesado
                    String ap1 = rs.getString("HTE_AP1");
                    String ap2 = rs.getString("HTE_AP2");
                    String nombre = rs.getString("HTE_NOM");
                    String titular = FormateadorTercero.getDescTercero(nombre, ap1, ap2, false);

                    InteresadoExpedienteVO interesadoVO = new InteresadoExpedienteVO();
                    interesadoVO.setCodTercero(rs.getInt("EXT_TER"));
                    interesadoVO.setNumVersion(rs.getInt("EXT_NVR"));
                    interesadoVO.setNombreCompleto(titular);
                    interesadoVO.setCodigoRol(rs.getInt("EXT_ROL"));
                    interesadoVO.setDescRol(rs.getString("ROL_DES"));
                    interesadoVO.setPorDefecto(rs.getBoolean("ROL_PDE"));
                    interesadoVO.setCodDomicilio(rs.getInt("EXT_DOT"));
                    interesadoVO.setMostrar(rs.getBoolean("MOSTRAR"));

                    String notificacion = rs.getString("EXT_NOTIFICACION_ELECTRONICA");
                    interesadoVO.setAdmiteNotificacion("0");
                    if (notificacion != null && !"".equals(notificacion)) {
                        interesadoVO.setAdmiteNotificacion(notificacion);
                    }

                    interesadoVO.setTelf(rs.getString("HTE_TLF"));
                    interesadoVO.setEmail(rs.getString("HTE_DCE"));
                    interesadoVO.setTipoDoc(rs.getString("HTE_TID"));
                    interesadoVO.setTxtDoc(rs.getString("HTE_DOC"));
                    interesadoVO.setPais(rs.getString("PAI_NOM"));
                    interesadoVO.setProvincia(rs.getString("PRV_NOM"));
                    interesadoVO.setMunicipio(rs.getString("MUN_NOM"));

                    String descTipoVia = rs.getString("TVI_DES");
                    String numDesde = rs.getString("DNN_NUD");
                    String letraDesde = rs.getString("DNN_LED");
                    String numHasta = rs.getString("DNN_NUH");
                    String letraHasta = rs.getString("DNN_LEH");
                    String bloque = rs.getString("DNN_BLQ");
                    String portal = rs.getString("DNN_POR");
                    String escalera = rs.getString("DNN_ESC");
                    String planta = rs.getString("DNN_PLT");
                    String puerta = rs.getString("DNN_PTA");
                    String emplaz = rs.getString("DNN_DMC");
                    String domic = rs.getString("VIA_NOM");
                    String domicilio = "";
                    domicilio = (descTipoVia != null && !descTipoVia.isEmpty()) ? domicilio + descTipoVia + " " : domicilio;
                    domicilio = (domic != null && !domic.isEmpty()) ? domicilio + domic + " " : domicilio;
                    domicilio = (emplaz != null && !emplaz.isEmpty()) ? domicilio + " Empl.: " + emplaz : domicilio;
                    domicilio = (numDesde != null && !numDesde.isEmpty()) ? domicilio + " Nº desde: " + numDesde : domicilio;
                    domicilio = (numHasta != null && !numHasta.isEmpty()) ? domicilio + " Nº hasta: " + numHasta : domicilio;
                    domicilio = (letraDesde != null && !letraDesde.isEmpty()) ? domicilio + " Letra desde: " + letraDesde : domicilio;
                    domicilio = (letraHasta != null && !letraHasta.isEmpty()) ? domicilio + " Letra hasta: " + letraHasta : domicilio;
                    domicilio = (bloque != null && !bloque.isEmpty()) ? domicilio + " Bloque: " + bloque : domicilio;
                    domicilio = (portal != null && !portal.isEmpty()) ? domicilio + " Portal: " + portal : domicilio;
                    domicilio = (escalera != null && !escalera.isEmpty()) ? domicilio + " Esc.: " + escalera : domicilio;
                    domicilio = (planta != null && !planta.isEmpty()) ? domicilio + " Planta: " + planta : domicilio;
                    domicilio = (puerta != null && !puerta.isEmpty()) ? domicilio + " Puerta: " + puerta : domicilio;

                    interesadoVO.setDomicilio(domicilio);

                    interesadoVO.setCp(rs.getString("DNN_CPO"));

                    resultado.add(interesadoVO);
                }
            }

        } catch (NumberFormatException e) {
            log.error("Error en la ejecución de la consulta. " + e.getMessage());
            resultado = null;
        } catch (SQLException e) {
            log.error("Error en la ejecución de la consulta. " + e.getMessage());
            resultado = null;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return resultado;
    }

    /**
     * ******************
     */
    /**
     * Recupera los datos necesarios para pasar al WS de expedientes formacion
     *
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param con: Conexión a la BBDD
     * @return String con el numero de registro
     * @throws
     * es.altia.flexia.integracion.moduloexterno.melanbide41.util.MeLanbide41Exception
     * @throws java.sql.SQLException
     */
    public String[] getDatosAltaExpedienteIkasLan(int codOrganizacion, String numExpediente, Connection con) throws MeLanbide41Exception, SQLException {
        String[] res = new String[3];
        ResultSet rs = null;
        PreparedStatement ps = null;

        String sql = "select TO_CHAR(EXP_FEI, 'dd-MM-yyyy') fecha_inicio, EXR_NRE numero_registro, EXP_ASU info_adicional"
                + " from E_EXR,E_EXT,T_HTE,T_TID, E_EXP"
                + " where EXT_NUM = EXP_NUM AND EXT_NUM=EXR_NUM(+) and"
                + " HTE_TER=EXT_TER and HTE_NVR=EXT_NVR and"
                + " HTE_TID=TID_cod  and EXT_ROL=1 and"
                + " EXT_NUM='" + numExpediente + "'";
        log.debug("getNumeroRegistroExpediente sql: " + sql);

        try {
            ps = con.prepareStatement(sql);
            //ps.setString(1, numExpediente);
            rs = ps.executeQuery();
            if (rs.next()) {
                res[0] = rs.getString("fecha_inicio");
                res[1] = rs.getString("numero_registro");
                res[2] = rs.getString("info_adicional");
            }
            log.info(" getNumeroRegistroExpediente - OK ");
        } catch (SQLException e) {
            throw new MeLanbide41Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }

        }
        return res;
    }

    public String getNumeroCensoCentro(String valorCScodigoCentro, String valorCScodigoUbicacionCentro, Connection con) throws MeLanbide41Exception, SQLException {
        log.info("getNumeroCensoCentro - BEGIN - ");
        String documento = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String codigoFijoUbicacionServicio = ConfigurationParameter.getParameter(ConstantesMeLanbide41.VALORFIJO_CONSULTA_NROCENSO_LAN_UBIC_SERVICIO_LMV, ConstantesMeLanbide41.FICHERO_PROPIEDADES);

        String sql = "SELECT LAN_NUM_REG_AUT FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_LAN_CENTROS_SERVICIOS, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                + " WHERE GEN_CEN_COD_CENTRO=? " //'" + valorCScodigoCentro + "'" 
                + " AND GEN_CEN_COD_UBIC=? " //'" + valorCScodigoUbicacionCentro +"'" 
                + " AND LAN_UBIC_SERVICIO_LMV=? "; //'" + codigoFijoUbicacionServicio +  "'";
        log.info("getNumeroCensoCentro sql: " + sql);
        log.info("getNumeroCensoCentro param1: " + valorCScodigoCentro + ",param2: " + valorCScodigoUbicacionCentro + ",param3: " + codigoFijoUbicacionServicio);

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, valorCScodigoCentro);
            ps.setString(2, valorCScodigoUbicacionCentro);
            ps.setString(3, codigoFijoUbicacionServicio);
            rs = ps.executeQuery();
            int cont = 0;
            String nroCensos = "";
            while (rs.next()) {
                documento = rs.getString("LAN_NUM_REG_AUT");
                cont++;
                if (cont == 1) {
                    nroCensos = documento;
                } else {
                    nroCensos = "," + documento;
                }
            }
            if (cont > 1) {
                log.error("Se ha encontrado mas de un Numero de Censo - Nro. Lineas Encontradas " + cont + " Censos : " + nroCensos);
            }

        } catch (SQLException e) {
            log.error("Error al consultar el Numero de censo - ", e);
            throw new MeLanbide41Exception("Error al recuperar el documento del interesado - Numero de Censo - en Cod Centro / Ubicacion : " + valorCScodigoCentro + "/" + valorCScodigoUbicacionCentro);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }

        }
        log.error("getNumeroCensoCentro - END - ");
        return documento;
    }

    /**
     *
     * Función que obtiene el número de censo SILICOI de base de datos
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param corrSubservicio
     * @param con: Conexion
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
            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                censo = rs.getString("NCENSO_SILCOI_CONCAT");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
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
        return censo;
    }

    /**
     *
     * Función para obtener el número de censo de un centro
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
            if (log.isDebugEnabled()) {
                log.info("sql getNumeroCensoLanbide = " + query);
            }
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
     * Recupera los datos necesarios para pasar al WS de Registro Centros
     *
     * @param codCentro codigo del Centro
     * @param codUbic codigo de Ubicacion del centro
     * @param con
     * @return String[] con corrServicio y corrSubservicio
     * @throws Exception
     */
    public String[] getCodigosCorr(String codCentro, String codUbic, Connection con) throws Exception {
        log.debug("Entra en getCodigosCorr de " + this.getClass().getSimpleName() + " - Centro: " + codCentro + " - Ubicación: " + codUbic);
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        String[] codigosCorr = new String[2];
        try {
            query = "SELECT gen_cen_servicio_corr,gen_cen_subservicio_corr"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
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
            query = " select CEN_NUM_REG_AUT from " + ConfigurationParameter.getParameter(ConstantesMeLanbide41.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide41.FICHERO_PROPIEDADES)
                    + " WHERE GEN_CEN_COD_CENTRO = '" + codCentro + "'"
                    + " AND GEN_CEN_COD_UBIC = '" + codUbic + "'"
                    + " AND GEN_CEN_SUBSERVICIO_CORR   = '" + corrSubservicio + "'";
            log.debug("query: " + query);
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
