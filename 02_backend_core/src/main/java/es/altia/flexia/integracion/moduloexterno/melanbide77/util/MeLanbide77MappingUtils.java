/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide77.util;

import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.RegistroAerteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.RegistroBatchVO;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.SolicitudAerteVO;
import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa
 */
public class MeLanbide77MappingUtils {

    private static MeLanbide77MappingUtils instance = null;
    private static Logger log = LogManager.getLogger(MeLanbide77MappingUtils.class);

    private MeLanbide77MappingUtils() {

    }

    public static MeLanbide77MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide77MappingUtils.class) {
                instance = new MeLanbide77MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == RegistroAerteVO.class) {
            return mapearRegistroAerteVO(rs);
        } else if (clazz == RegistroBatchVO.class) {
            return mapearRegistroBatch(rs);
        } else if (clazz == SolicitudAerteVO.class) {
            return mapearSolicitud(rs);
        } else if (clazz == TerceroVO.class) {
            return mapearTercero(rs);
        }
        return null;
    }

    private RegistroAerteVO mapearRegistroAerteVO(ResultSet rs) throws Exception {
        RegistroAerteVO registro = new RegistroAerteVO();
        registro.setResDep(rs.getInt("RES_DEP"));
        registro.setResUor(rs.getInt("RES_UOR"));
        registro.setResTip(rs.getString("RES_TIP"));
        registro.setResEje(rs.getInt("RES_EJE"));
        registro.setResNum(rs.getInt("RES_NUM"));
        registro.setResUsu(rs.getInt("RES_USU"));
        registro.setResTer(rs.getInt("RES_TER"));
        registro.setResAsu(rs.getString("RES_ASU"));
        registro.setNumSolicitud(Integer.parseInt(registro.getResAsu().substring(33, 38)));

        return registro;
    }

    private SolicitudAerteVO mapearSolicitud(ResultSet rs) throws Exception {
        //SELECT NUM_DOC,FVIG, NUMCUENTA, NOMEMP1, CIFEMP1, TPJOR1, PORPARC1, NOMEMP2, CIFEMP2, TPJOR2, PORPARC2, NOMEMP3, CIFEMP3, TPJOR3, PORPARC3 
        SolicitudAerteVO solicitud = new SolicitudAerteVO();
        solicitud.setNumSolicitud(rs.getInt("CORR_REG"));
        solicitud.setNumDoc(rs.getString("NUM_DOC"));
        solicitud.setFecCaducidad(rs.getDate("FVIG"));
        solicitud.setNumCuenta(rs.getString("NUMCUENTA"));
        solicitud.setNomEmp1(rs.getString("NOMEMP1"));
        solicitud.setCifEmp1(rs.getString("CIFEMP1"));
        solicitud.setTpJor1(rs.getString("TPJOR1"));
        if (rs.getString("PORPARC1") != null && !rs.getString("PORPARC1").isEmpty()) {
            solicitud.setPorParc1(rs.getString("PORPARC1"));
        } else {
            solicitud.setPorParc1("");
        }
        if (rs.getString("NOMEMP2") != null && !rs.getString("NOMEMP2").isEmpty()) {
            solicitud.setNomEmp2(rs.getString("NOMEMP2"));
        } else {
            solicitud.setNomEmp2("");
        }
        if (rs.getString("CIFEMP2") != null && !rs.getString("CIFEMP2").isEmpty()) {
            solicitud.setCifEmp2(rs.getString("CIFEMP2"));
        } else {
            solicitud.setCifEmp2("");
        }
        if (rs.getString("TPJOR2") != null && !rs.getString("TPJOR2").isEmpty()) {
            solicitud.setTpJor2(rs.getString("TPJOR2"));
        } else {
            solicitud.setTpJor2("");
        }
        if (rs.getString("PORPARC2") != null && !rs.getString("PORPARC2").isEmpty()) {
            solicitud.setPorParc2(rs.getString("PORPARC2"));
        } else {
            solicitud.setPorParc2("");
        }
        if (rs.getString("NOMEMP3") != null && !rs.getString("NOMEMP3").isEmpty()) {
            solicitud.setNomEmp3(rs.getString("NOMEMP3"));
        } else {
            solicitud.setNomEmp3("");
        }
        if (rs.getString("CIFEMP3") != null && !rs.getString("CIFEMP3").isEmpty()) {
            solicitud.setCifEmp3(rs.getString("CIFEMP3"));
        } else {
            solicitud.setCifEmp3("");
        }
        if (rs.getString("TPJOR3") != null && !rs.getString("TPJOR3").isEmpty()) {
            solicitud.setTpJor3(rs.getString("TPJOR3"));
        } else {
            solicitud.setTpJor3("");
        }
        if (rs.getString("PORPARC3") != null && !rs.getString("PORPARC3").isEmpty()) {
            solicitud.setPorParc3(rs.getString("PORPARC3"));
        } else {
            solicitud.setPorParc3("");
        }

        return solicitud;
    }

    private TerceroVO mapearTercero(ResultSet rs) throws Exception {
        TerceroVO ter = new TerceroVO();
        ter.setAp1(rs.getString("AP1"));
        ter.setAp2(rs.getString("AP2"));
        ter.setNombre(rs.getString("NOM"));
        ter.setDoc(rs.getString("DOC"));
        ter.setEmail(rs.getString("DCE"));
        ter.setTelefono(rs.getString("TLF"));
        ter.setTipoDoc(rs.getString("TID"));

        DireccionVO dom = new DireccionVO();
        dom.setBloque(rs.getString("DNN_BLQ"));
        dom.setCodMunicipio(rs.getInt("DNN_MUN"));
        dom.setCodPais(rs.getInt("DNN_PAI"));
        dom.setCodPostal(rs.getString("DNN_CPO"));
        dom.setCodProvincia(rs.getInt("DNN_PRV"));
        dom.setEmplazamiento(rs.getString("DNN_DMC"));
        dom.setEsPrincipal(true);
        dom.setEscalera(rs.getString("DNN_ESC"));
        dom.setNombreVia(rs.getString("VIA_NOM"));
        dom.setPlanta(rs.getString("DNN_PLT"));
        dom.setPortal(rs.getString("DNN_POR"));
        dom.setPrimerNumero(rs.getInt("DNN_NUD"));
        dom.setPrimeraLetra(rs.getString("DNN_LED"));
        dom.setPuerta(rs.getString("DNN_PTA"));
        dom.setTipoVia(rs.getInt("DNN_TVI"));
        dom.setUltimaLetra(rs.getString("DNN_LEH"));
        dom.setUltimoNumero(rs.getInt("DNN_NUH"));

        ter.setDomicilio(dom);

        return ter;
    }

    public RegistroBatchVO mapearRegistroBatch(ResultSet rs) throws SQLException {
        RegistroBatchVO fila = new RegistroBatchVO();
        fila.setId(rs.getInt("ID"));
        fila.setFecha(rs.getString("FEC_REGISTRO"));
        fila.setEjerRegistro(rs.getInt("EJER_REGISTRO"));
        fila.setNumRegistro(rs.getInt("NUM_REGISTRO"));
        fila.setNumSolicitud(rs.getInt("NUM_SOLICITUD"));
        fila.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
        fila.setCodTramite(rs.getInt("COD_TRA"));
        fila.setOperacion(rs.getString("OPERACION"));
        fila.setResultado(rs.getString("RESULTADO"));
        fila.setCodOperacion(rs.getInt("COD_OPERACION"));
        fila.setRelanzar(rs.getInt("RELANZAR"));
        fila.setObservaciones(rs.getString("OBSERVACIONES"));

        return fila;
    }

}
