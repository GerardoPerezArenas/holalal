package es.altia.flexia.integracion.moduloexterno.melanbide84.util;

import es.altia.flexia.integracion.moduloexterno.melanbide84.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide84.vo.PersonaVO;
import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide84MappingUtils {

    private static MeLanbide84MappingUtils instance = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide84MappingUtils() {
    }

    public static MeLanbide84MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide84MappingUtils.class) {
                instance = new MeLanbide84MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == ExpedienteVO.class) {
            return mapearExpediente(rs);
        } else if (clazz == IdExpedienteVO.class) {
            return mapearIdExpediente(rs);
        } else if (clazz == InteresadoExpedienteVO.class) {
            return mapearInteresado(rs);
        } else if (clazz == TerceroVO.class) {
            return mapearTercero(rs);
        } else if (clazz == DireccionVO.class) {
            return mapearDomicilio(rs);
        } else if (clazz == PersonaVO.class) {
            return mapearPersonaVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }
        return null;
    }

    private DireccionVO mapearDomicilio(ResultSet rs) throws Exception {
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

        return dom;
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
        //ter.setExtRol(rs.getString("ROL"));
        return ter;
    }

    private Object mapearIdExpediente(ResultSet rs) throws SQLException {
        IdExpedienteVO idexpediente = new IdExpedienteVO();
        idexpediente.setEjercicio(rs.getInt("EXP_EJE"));
        idexpediente.setProcedimiento(rs.getString("EXP_PRO"));
        idexpediente.setNumeroExpediente(rs.getString("EXP_NUM"));
        idexpediente.setNumero(rs.getInt("EXP_TRA"));
        return idexpediente;
    }

    private Object mapearInteresado(ResultSet rs) throws SQLException {
        InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();

        interesado.setRol(2);
        return interesado;
    }

    private ExpedienteVO mapearExpediente(ResultSet rs) throws SQLException {
        ExpedienteVO exp = new ExpedienteVO();

        exp.setAsunto(rs.getString("ASU"));
        exp.setUor(rs.getInt("UOR"));
        exp.setUorTramiteInicio(rs.getInt("UTR"));
        exp.setUsuario(rs.getInt("USU"));

        return exp;

    }

    private Object mapearPersonaVO(ResultSet rs) throws SQLException {
        PersonaVO persona = new PersonaVO();

        persona.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            persona.setId(null);
        }
        persona.setExpEntap(rs.getString("NUM_EXP"));
        persona.setPrograma(rs.getString("PROGRAMA"));
        persona.setDni(rs.getString("DNI"));
        persona.setNombre(rs.getString("NOMBRE"));
        persona.setApel1(rs.getString("APEL1"));
        if (rs.getString("APEL2") != null && !rs.getString("APEL2").isEmpty()) {
            persona.setApel2(rs.getString("APEL2"));
        } else {
            persona.setApel2(null);
        }
        persona.setNivelAcademico(rs.getString("NIVELACADEMICO"));
        persona.setSexo(rs.getInt("SEXO"));
        persona.setCalle(rs.getString("CALLE"));
        Integer aux = rs.getInt("NUMERO");
        if (aux != 0) {
            persona.setNumero(rs.getInt("NUMERO"));
        } else {
            persona.setNumero(null);
        }
        persona.setLetra((rs.getString("LETRA") != null && !rs.getString("LETRA").isEmpty() ? rs.getString("LETRA") : ""));
        if (rs.getString("PLANTA") != null && !rs.getString("PLANTA").isEmpty()) {
            persona.setPlanta(rs.getString("PLANTA"));
        } else {
            persona.setPlanta(null);
        }
        if (rs.getString("PUERTA") != null && !rs.getString("PUERTA").isEmpty()) {
            persona.setPuerta(rs.getString("PUERTA"));
        } else {
            persona.setPuerta(null);
        }
        persona.setCodPost(rs.getInt("CP"));
        persona.setLocalidad(rs.getString("LOCALIDAD"));
        if (rs.getDate("FECHANAC") != null) {
            persona.setFechaNacimiento(rs.getDate("FECHANAC"));
            persona.setFecNacStr(dateFormat.format(rs.getDate("FECHANAC")));
        } else {
            persona.setFechaNacimiento(null);
        }
        if (rs.getDate("FECHAINICIO") != null) {
            persona.setFechaInicio(rs.getDate("FECHAINICIO"));
             persona.setFecIniStr(dateFormat.format(rs.getDate("FECHAINICIO")));
       } else {
            persona.setFechaInicio(null);
        }
        if (rs.getDate("FECHASOLICITUD") != null) {
            persona.setFechaSolicitud(rs.getDate("FECHASOLICITUD"));
              persona.setFecSolStr(dateFormat.format(rs.getDate("FECHASOLICITUD")));
      } else {
            persona.setFechaSolicitud(null);
        }
        return persona;
    }

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
