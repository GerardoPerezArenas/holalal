package es.altia.flexia.integracion.moduloexterno.melanbide80.manager;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.flexia.integracion.moduloexterno.melanbide80.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide80.dao.MeLanbide80DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConstantesMeLanbide80;
import es.altia.flexia.integracion.moduloexterno.melanbide80.vo.DesplegableAdmonLocalVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
//import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide80Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide80Manager.class);

    //Instancia
    private static MeLanbide80Manager instance = null;

    public static MeLanbide80Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide80Manager.class) {
                instance = new MeLanbide80Manager();
            }
        }
        return instance;
    }

    public static void grabarError(ErrorBean error, String excepError, String traza, String numExp) {
        try {
            log.error("grabando el error");
            error.setMensajeExcepError(excepError);
            error.setTraza(excepError);
            error.setCausa(traza);
            log.error("causa: " + traza);
            log.error("numExp: " + numExp);
            if ("".equals(numExp)) {
                numExp = "0000/ERRMISGEST/000000";
            }

            String idProcedimiento = "AEXCE";
            log.error("procedimiento: " + idProcedimiento);
            error.setIdProcedimiento(idProcedimiento);
            error.setIdClave("");
            error.setSistemaOrigen("REGEXLAN");
            error.setErrorLog("flexia_debug");
            error.setIdFlexia(numExp);
            log.error("Vamos a registrar el error");

            RegistroErrores.registroError(error);
        } catch (Exception ex) {
            log.error("Error al grabarError" + ex);
        }
    }

    public List<PersonaVO> getDatosPersona(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide80DAO meLanbide80DAO = MeLanbide80DAO.getInstance();
            lista = meLanbide80DAO.getDatosPersona(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaTiposContA = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_CTRA, ConstantesMeLanbide80.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_JORN, ConstantesMeLanbide80.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaSituacion = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_ERTE, ConstantesMeLanbide80.FICHERO_PROPIEDADES), adapt);
            for (PersonaVO pers : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaTiposContA) {
                    if (valordesp.getDes_val_cod().equals(pers.getTipcontA())) {
                        pers.setDesTipcontA(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaJornada) {
                    if (valordesp.getDes_val_cod().equals(pers.getJornada())) {
                        pers.setDesJornada(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaSituacion) {
                    if (valordesp.getDes_val_cod().equals(pers.getSituacion())) {
                        pers.setDesSituacion(valordesp.getDes_nom());
                        break;
                    }
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las pèrsonas ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las personas ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public PersonaVO getPersonaPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide80DAO meLanbide80DAO = MeLanbide80DAO.getInstance();
            return meLanbide80DAO.getPersonaPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre una persona:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una persona:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarPersona(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide80DAO meLanbide80DAO = MeLanbide80DAO.getInstance();
            return meLanbide80DAO.eliminarPersona(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una persona:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una persona:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaPersona(PersonaVO nuevaPersona, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide80DAO meLanbide80DAO = MeLanbide80DAO.getInstance();
            insertOK = meLanbide80DAO.crearNuevaPersona(nuevaPersona, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando una persona : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando una persona : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarPersona(PersonaVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide80DAO meLanbide80DAO = MeLanbide80DAO.getInstance();
            insertOK = meLanbide80DAO.modificarPersona(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando una persona : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando una persona : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide80DAO meLanbide80DAO = MeLanbide80DAO.getInstance();
            return meLanbide80DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un Desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getOidDocumento(String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide80DAO meLanbide80DAO = MeLanbide80DAO.getInstance();
            return meLanbide80DAO.getOidDocumento(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el OID del XML:  " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el OID del XML:  " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

}
