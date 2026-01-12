package es.altia.flexia.integracion.moduloexterno.melanbide75.manager;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.flexia.integracion.moduloexterno.melanbide75.dao.MeLanbide75DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConstantesMeLanbide75;
import es.altia.flexia.integracion.moduloexterno.melanbide75.vo.ControlAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide75.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide75Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide75Manager.class);
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    //Instancia
    private static MeLanbide75Manager instance = null;

    public static MeLanbide75Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide75Manager.class) {
                instance = new MeLanbide75Manager();
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

            String idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea("COCUR"); //convierteProcedimiento(codProcedimiento);

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

    public List<ControlAccesoVO> getDatosControlAcceso(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide75DAO meLanbide75DAO = MeLanbide75DAO.getInstance();
            lista = meLanbide75DAO.getDatosControlAcceso(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaTiposContrato = MeLanbide75Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide75.COD_DES_TCCR, ConstantesMeLanbide75.FICHERO_PROPIEDADES), adapt);
            for (ControlAccesoVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaTiposContrato) {
                    if (valordesp.getDes_val_cod().equals(acce.getTipoCon())) {
                        acce.setDesTipoCon(valordesp.getDes_nom());
                        break;
                    }
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre los puestos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre los puestos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public ControlAccesoVO getControlAccesoPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide75DAO meLanbide75DAO = MeLanbide75DAO.getInstance();
            return meLanbide75DAO.getControlAccesoPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre un puesto:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un puesto:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarAcceso(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide75DAO meLanbide75DAO = MeLanbide75DAO.getInstance();
            return meLanbide75DAO.eliminarAcceso(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar un puesto:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar un puesto:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<ControlAccesoVO> getRegistrosAcceso(String numExp, AdaptadorSQLBD adapt) throws Exception {
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide75DAO meLanbide75DAO = MeLanbide75DAO.getInstance();
            lista = meLanbide75DAO.getRegistroAcceso(numExp, con);

            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaTipoContrato = MeLanbide75Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide75.COD_DES_TCCR, ConstantesMeLanbide75.FICHERO_PROPIEDADES), adapt);

            for (ControlAccesoVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaTipoContrato) {
                    if (valordesp.getDes_val_cod().equals(acce.getTipoCon())) {
                        acce.setDesTipoCon(valordesp.getDes_nom());
                        break;
                    }
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los puestos:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando los puestos:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoAcceso(ControlAccesoVO nuevoAcceso, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide75DAO meLanbide75DAO = MeLanbide75DAO.getInstance();
            insertOK = meLanbide75DAO.crearNuevoAcceso(nuevoAcceso, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando un puesto : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando un puesto : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarAcceso(ControlAccesoVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide75DAO meLanbide75DAO = MeLanbide75DAO.getInstance();
            insertOK = meLanbide75DAO.modificarAcceso(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un puesto : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un puesto : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide75DAO meLanbide75DAO = MeLanbide75DAO.getInstance();
            return meLanbide75DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un Desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

}
