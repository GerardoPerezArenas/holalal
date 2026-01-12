/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide53.util;

import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleEstadisticaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EstadisticasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EstadisticasVO_EXCEL;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EventoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO_EXCEL;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO_EXCEL2;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author davidg
 */
public class MeLanbide53MappingUtils {

    private static MeLanbide53MappingUtils instance = null;

    private MeLanbide53MappingUtils() {
    }

    public static MeLanbide53MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide53MappingUtils.class) {
                instance = new MeLanbide53MappingUtils();
            }
        }
        return instance;
    }

    /*Mapeo para el detalle de errores*/
    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == RegistroErrorVO.class) {
            return mapearRegistroErrorVO(rs);
        }
        if (clazz == DetalleErrorVO.class) {
            return mapearDetalleErrorVO(rs);
        }
        if (clazz == RegistroErrorVO_EXCEL.class) {
            return mapearRegistroErrorVO_listadoDetalle(rs);
        }
        if (clazz == RegistroErrorVO_EXCEL2.class) {
            return mapearRegistroErrorVO_listado2(rs);
        }
        if (clazz == EstadisticasVO.class) {
            return mapearEstadisticasVO(rs);
        }
        if (clazz == EstadisticasVO_EXCEL.class) {
            return mapearEstadisticasVO_EXCEL(rs);
        }
        if (clazz == EventoVO.class) {
            return mapearEventoVO(rs);
        }
        if (clazz == DetalleEstadisticaVO.class) {
            return mapearDetalleEstadisticaVO(rs);
        }

        return null;
    }

    /*Mapeo para el listado de errores*/
    public Object map_listado(ResultSet rs, Class clazz) throws Exception {
        if (clazz == RegistroErrorVO.class) {
            return mapearRegistroErrorVO_listado(rs);
        }
        if (clazz == DetalleErrorVO.class) {
            return mapearDetalleErrorVO(rs);
        }

        if (clazz == RegistroErrorVO_EXCEL.class) {
            return mapearRegistroErrorVO_listado(rs);
        }

        return null;
    }

    private Object mapearRegistroErrorVO(ResultSet rs) throws SQLException {
        RegistroErrorVO registroError = new RegistroErrorVO();

        registroError.setId(rs.getInt("REG_ERR_ID"));
        registroError.setFechaError(rs.getString("REG_ERR_FEC_ERROR"));
        registroError.setMensajeError(rs.getString("REG_ERR_MEN"));
        registroError.setMensajeException(rs.getString("REG_ERR_EXCEP_MEN"));
        registroError.setCausaException(rs.getString("REG_ERR_CAUSA"));
        registroError.setTrazaError(rs.getString("REG_ERR_TRAZA"));
        registroError.setIdProcedimiento(rs.getString("REG_ERR_ID_PROC"));
        registroError.setIdErrorFK(rs.getString("REG_ERR_IDEN_ERR_ID"));
        registroError.setClave(rs.getString("REG_ERR_ID_CLAVE"));
        registroError.setErrorNotificado(rs.getString("REG_ERR_NOT"));
        registroError.setErrorRevisado(rs.getString("REG_ERR_REV"));
        registroError.setFechaRevisionError(rs.getString("REG_ERR_FEC_REV"));
        registroError.setObservacionesError(rs.getString("REG_ERR_OBS"));
        registroError.setSistemaOrigen(rs.getString("REG_ERR_SIS_ORIG"));
        registroError.setUbicacionError(rs.getString("REG_ERR_SITU"));
        registroError.setFicheroLog(rs.getString("REG_ERR_LOG"));
        registroError.setEvento(rs.getString("REG_ERR_EVEN"));
        registroError.setNumeroExpediente(rs.getString("REG_ERR_ID_FLEXIA"));
        String xmlSolicitud = StringEscapeUtils.escapeXml(rs.getString("REG_ERR_SOLICITUD"));
        //.replace("<", "&lt;").replace(">", "&gt;");
        registroError.setXmlReglasSolicitud(xmlSolicitud);
        registroError.setSolicitudRetramitada(rs.getString("REG_ERR_RETRAMIT_SN"));
        registroError.setFechaRetramitacion(rs.getString("REG_ERR_FEC_RETRAMIT"));
        registroError.setResultadoRetramitacion(rs.getString("REG_ERR_RESULT_WS"));
        registroError.setImpacto(rs.getString("REG_ERR_IMPACTO"));
        registroError.setTratado(rs.getString("REG_ERR_TRATADO"));

        return registroError;
    }

    /* Mapeo de cada uno de los objetos del listado*/
    private Object mapearRegistroErrorVO_listadoDetalle(ResultSet rs) throws SQLException {
        RegistroErrorVO_EXCEL registroError = new RegistroErrorVO_EXCEL();

        registroError.setId(rs.getInt("REG_ERR_ID"));
        registroError.setFechaError(rs.getString("REG_ERR_FEC_ERROR"));
        registroError.setMensajeError(rs.getString("REG_ERR_MEN"));
        registroError.setIdProcedimiento(rs.getString("REG_ERR_ID_PROC"));
        registroError.setIdErrorFK(rs.getString("REG_ERR_IDEN_ERR_ID"));
        registroError.setClave(rs.getString("REG_ERR_ID_CLAVE"));
        registroError.setErrorNotificado(rs.getString("REG_ERR_NOT"));
        registroError.setErrorRevisado(rs.getString("REG_ERR_REV"));
        registroError.setFechaRevisionError(rs.getString("REG_ERR_FEC_REV"));
        registroError.setObservacionesError(rs.getString("REG_ERR_OBS"));
        registroError.setSistemaOrigen(rs.getString("REG_ERR_SIS_ORIG"));
        registroError.setUbicacionError(rs.getString("REG_ERR_SITU"));
        registroError.setFicheroLog(rs.getString("REG_ERR_LOG"));
        registroError.setEvento(rs.getString("REG_ERR_EVEN"));
        registroError.setNumeroExpediente(rs.getString("REG_ERR_ID_FLEXIA"));
        registroError.setSolicitudRetramitada(rs.getString("REG_ERR_RETRAMIT_SN"));
        registroError.setFechaRetramitacion(rs.getString("REG_ERR_FEC_RETRAMIT"));
        registroError.setResultadoRetramitacion(rs.getString("REG_ERR_RESULT_WS"));
        registroError.setImpacto(rs.getString("REG_ERR_IMPACTO"));
        registroError.setTratado(rs.getString("REG_ERR_TRATADO"));

        return registroError;
    }

    /* Mapeo de cada uno de los objetos del listado*/
    private Object mapearRegistroErrorVO_listado(ResultSet rs) throws SQLException {
        RegistroErrorVO registroError = new RegistroErrorVO();

        registroError.setId(rs.getInt("REG_ERR_ID"));
        registroError.setFechaError(rs.getString("REG_ERR_FEC_ERROR"));
        registroError.setMensajeError(rs.getString("REG_ERR_MEN"));
        registroError.setMensajeException(rs.getString("REG_ERR_EXCEP_MEN"));
        registroError.setCausaException(rs.getString("REG_ERR_CAUSA"));
        registroError.setTrazaError(rs.getString("REG_ERR_TRAZA"));
        registroError.setIdProcedimiento(rs.getString("REG_ERR_ID_PROC"));
        registroError.setIdErrorFK(rs.getString("REG_ERR_IDEN_ERR_ID"));
        registroError.setClave(rs.getString("REG_ERR_ID_CLAVE"));
        registroError.setErrorNotificado(rs.getString("REG_ERR_NOT"));
        registroError.setErrorRevisado(rs.getString("REG_ERR_REV"));
        registroError.setFechaRevisionError(rs.getString("REG_ERR_FEC_REV"));
        registroError.setObservacionesError(rs.getString("REG_ERR_OBS"));
        registroError.setSistemaOrigen(rs.getString("REG_ERR_SIS_ORIG"));
        registroError.setUbicacionError(rs.getString("REG_ERR_SITU"));
        registroError.setFicheroLog(rs.getString("REG_ERR_LOG"));
        registroError.setEvento(rs.getString("REG_ERR_EVEN"));
        registroError.setNumeroExpediente(rs.getString("REG_ERR_ID_FLEXIA"));
        //String xmlSolicitud = StringEscapeUtils.escapeXml(rs.getString("REG_ERR_SOLICITUD"));
        //.replace("<", "&lt;").replace(">", "&gt;");
        //registroError.setXmlReglasSolicitud(xmlSolicitud);
        //registroError.setSolicitudRetramitada(rs.getString("REG_ERR_RETRAMIT_SN"));
        //registroError.setFechaRetramitacion(rs.getString("REG_ERR_FEC_RETRAMIT"));
        //registroError.setResultadoRetramitacion(rs.getString("REG_ERR_RESULT_WS"));*/
        registroError.setImpacto(rs.getString("REG_ERR_IMPACTO"));
        registroError.setTratado(rs.getString("REG_ERR_TRATADO"));

        return registroError;
    }

    private Object mapearDetalleErrorVO(ResultSet rs) throws SQLException {
        DetalleErrorVO registroDetalleError = new DetalleErrorVO();

        try {
            registroDetalleError.setId(rs.getString("IDEN_ERR_ID"));
            registroDetalleError.setDescripcionCorta(rs.getString("IDEN_ERR_DESC_C"));
            registroDetalleError.setDescripcion(rs.getString("IDEN_ERR_DESC"));
            registroDetalleError.setTipo(rs.getString("IDEN_ERR_TIPO"));
            registroDetalleError.setDesTipo(rs.getString("TIPO"));
            registroDetalleError.setDesCritico(rs.getString("CRITICO"));
            registroDetalleError.setCritico(rs.getString("IDEN_ERR_CRIT"));
            registroDetalleError.setAviso(rs.getString("IDEN_ERR_MEN_AVISO"));
            registroDetalleError.setMails(rs.getString("IDEN_ERR_MAILS"));
            registroDetalleError.setAcciones(rs.getString("IDEN_ERR_ACC"));
            registroDetalleError.setModulo(rs.getString("IDEN_ERR_MOD"));

            if (registroDetalleError.getDescripcionCorta() != null) {
                registroDetalleError.setDescripcionCorta(registroDetalleError.getDescripcionCorta().replace("\n", " ").replace("\r", " "));
            }

            if (registroDetalleError.getDescripcion() != null) {
                registroDetalleError.setDescripcion(registroDetalleError.getDescripcion().replace("\n", " ").replace("\r", " "));
            }

            if (registroDetalleError.getAviso() != null) {
                registroDetalleError.setAviso(registroDetalleError.getAviso().replace("\n", " ").replace("\r", " "));
            }
            if (registroDetalleError.getDesTipo() == null || registroDetalleError.getDesTipo() == "null") {
                registroDetalleError.setDesTipo(" - ");
            }
            if (registroDetalleError.getDesCritico() == null) {
                registroDetalleError.setDesCritico(" ");
            }
        } catch (Exception ex) {
        }
        return registroDetalleError;
    }

    private Object mapearRegistroErrorVO_listado2(ResultSet rs) throws SQLException {
        RegistroErrorVO_EXCEL2 numero = new RegistroErrorVO_EXCEL2();

        numero.setIdErrorFK(rs.getString("REG_ERR_IDEN_ERR_ID"));
        numero.setMensajeError(rs.getString("IDEN_ERR_DESC"));
        numero.setNum(rs.getString("NUM"));
        return numero;

    }

    private Object mapearEstadisticasVO(ResultSet rs) throws SQLException {
        EstadisticasVO estadisticas = new EstadisticasVO();

        estadisticas.setId(rs.getInt("ID"));
        estadisticas.setFechaRegistro(rs.getString("FECHA_REGISTRO"));
        estadisticas.setIdProcedimiento(rs.getString("ID_PROCEDIMIENTO"));
        estadisticas.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
        estadisticas.setClave(rs.getString("OID_SOLICITUD"));
        estadisticas.setResultado(rs.getString("RESULTADO"));
        estadisticas.setIdError(rs.getString("ID_ERROR"));
        estadisticas.setEvento(rs.getString("EVENTO"));
        estadisticas.setObservaciones(rs.getString("OBSERVACIONES"));

        return estadisticas;
    }

    private Object mapearEstadisticasVO_EXCEL(ResultSet rs) throws SQLException {
        EstadisticasVO_EXCEL estadisticas = new EstadisticasVO_EXCEL();

        estadisticas.setId(rs.getInt("ID"));
        estadisticas.setFechaRegistro(rs.getString("FECHA_REGISTRO"));
        estadisticas.setIdProcedimiento(rs.getString("ID_PROCEDIMIENTO"));
        estadisticas.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
        estadisticas.setClave(rs.getString("OID_SOLICITUD"));
        estadisticas.setResultado(rs.getString("RESULTADO"));
        estadisticas.setIdError(rs.getString("ID_ERROR"));
        estadisticas.setEvento(rs.getString("EVENTO"));
        estadisticas.setObservaciones(rs.getString("OBSERVACIONES"));

        return estadisticas;
    }

    public EventoVO mapearEventoVO(ResultSet rs) throws SQLException {
        EventoVO fila = new EventoVO();
        fila.setCodEvent(rs.getString("VACIO"));
        fila.setDescEvent(rs.getString("EVENTO"));

        return fila;
    }

    private Object mapearDetalleEstadisticaVO(ResultSet rs) throws SQLException {
        DetalleEstadisticaVO detEstadistica = new DetalleEstadisticaVO();

        detEstadistica.setId(rs.getInt("ID"));
        detEstadistica.setFechaRegistro(rs.getString("FECHA_REGISTRO"));
        detEstadistica.setIdProcedimiento(rs.getString("ID_PROCEDIMIENTO"));
        detEstadistica.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
        detEstadistica.setClave(rs.getString("OID_SOLICITUD"));
        detEstadistica.setResultado(rs.getString("RESULTADO"));
        detEstadistica.setIdError(rs.getString("ID_ERROR"));
        detEstadistica.setEvento(rs.getString("EVENTO"));
        detEstadistica.setObservaciones(rs.getString("OBSERVACIONES"));
        String xmlSuscripcion = StringEscapeUtils.escapeXml(rs.getString("REGLAS_SUSCRIPCION"));
        detEstadistica.setXmlReglasSuscripcion(xmlSuscripcion);

        return detEstadistica;
    }

}
