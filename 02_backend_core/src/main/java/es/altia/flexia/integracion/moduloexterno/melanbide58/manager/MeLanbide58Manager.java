/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.manager;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.flexia.integracion.moduloexterno.melanbide58.dao.MeLanbide58DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.AltaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.BajaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.CausaAltaBajaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.PlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DiscapacitadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.SMIVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide58Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide58Manager.class);

    //Instancia
    private static MeLanbide58Manager instance = null;

    public static MeLanbide58Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide58Manager.class) {
                instance = new MeLanbide58Manager();
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

            String idProcedimiento = "CEESC";
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

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
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

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO A     ------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------   
    // ------------------  ALTAS  -------------------------------------------------------------------------------
    public List<AltaVO> getDatosAltas(String numExp, int codOrganizacion, Connection con) throws Exception {
        List<AltaVO> lista = new ArrayList<AltaVO>();
        try {
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getDatosAlta(numExp, codOrganizacion, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos Altas ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos Altas ", ex);
            throw new Exception(ex);
        }
    }

    public List<AltaVO> getRegistrosAlta(String numExp, String nif, String numLinea, String apellidos, int codOrganizacion, Connection con) throws Exception {
        List<AltaVO> lista = new ArrayList<AltaVO>();
        try {
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getRegistroAlta(numExp, nif, numLinea, apellidos, codOrganizacion, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los Registros del Anexo A - alta", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando los registros del Anexo A - alta", ex);
            throw new Exception(ex);
        }
    }

    public AltaVO getAltaPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getAltaPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de un alta:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un alta:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarAlta(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.eliminarAlta(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar un alta:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar un alta:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoAlta(AltaVO nuevoVO, Connection con) throws Exception {
        boolean insertOK;
        try {
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.crearNuevoAlta(nuevoVO, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando  un alta : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando un alta : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        return insertOK;
    }

    public boolean modificarAlta(AltaVO datModif, Connection con) throws Exception {
        boolean insertOK;
        try {
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.modificarAlta(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  un alta : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un alta : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        return insertOK;
    }

    public List<CausaAltaBajaVO> getListAltas(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            if (adaptador != null) {
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
                con = adaptador.getConnection();
            }
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getListAltas(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre la Lista de Altas " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Altas " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (adaptador != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    // ------------------  BAJAS  -------------------------------------------------------------------------------
    public List<BajaVO> getDatosBajas(String numExp, int codOrganizacion, Connection con) throws Exception {
        List<BajaVO> lista = new ArrayList<BajaVO>();
        try {
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getDatosBaja(numExp, codOrganizacion, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos Bajas ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos Bajas ", ex);
            throw new Exception(ex);
        }
    }

    public List<BajaVO> getRegistrosBaja(String numExp, String nif, String numLinea, String apellidos, int codOrganizacion, Connection con) throws Exception {
        List<BajaVO> lista = new ArrayList<BajaVO>();
        try {
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getRegistroBaja(numExp, nif, numLinea, apellidos, codOrganizacion, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los Registros del Anexo A - baja", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando los registros del Anexo A - baja", ex);
            throw new Exception(ex);
        }
    }

    public BajaVO getBajaPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getBajaPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de un baja:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un baja:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarBaja(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.eliminarBaja(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar un baja:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar un baja:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoBaja(BajaVO nuevoVO, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.crearNuevoBaja(nuevoVO, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando  un baja : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando un baja : " + ex.getMessage(), ex);
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

    public boolean modificarBaja(BajaVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.modificarBaja(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  un baja : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un baja : " + ex.getMessage(), ex);
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

    public List<CausaAltaBajaVO> getListBajas(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            if (adaptador != null) {
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
                con = adaptador.getConnection();
            }
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getListBajas(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre la Lista de Altas " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Altas " + numExp, ex);
            throw new Exception(ex);
        } finally {

            try {
                if (adaptador != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }

        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO B -  SMI    ------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
    public List<SMIVO> getDatosSMI(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<SMIVO> lista = new ArrayList<SMIVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getDatosAnexoB(numExp, codOrganizacion, con);
            //recueperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaCausas = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_CAUSA_INCIDENCIA, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (SMIVO reg : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaCausas) {
                    if (valordesp.getDes_val_cod().equals(reg.getCausaIncidencia())) {
                        reg.setDesCausaIncidencia(valordesp.getDes_nom());
                        break;
                    }
                }
            }
         
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos SMI ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos SMI ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public SMIVO getSMIPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getSMIPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de un smi:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un smi:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarSMI(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.eliminarSMI(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar un smi:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar un smi:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<SMIVO> getRegistrosAnexoB(String numExp, String numLinea, String apellidos, String nif,  AdaptadorSQLBD adapt) throws Exception {
        List<SMIVO> lista = new ArrayList<SMIVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getRegistroSMI(numExp, numLinea, apellidos, nif, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los Registros del SMI:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando los Registros del SMI:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<SMIVO> getRegistrosSubImpInc(String numExp, String numLinea, String apellidos, AdaptadorSQLBD adapt) throws Exception {
        List<SMIVO> lista = new ArrayList<SMIVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getRegistroSubImpInc(numExp, numLinea, apellidos, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los Registros de subvención de importe incorrecto:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando los Registros de subvención de importe incorrecto:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Integer crearNuevoSMI(SMIVO nuevoSMI, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        Integer idNuevoSMI;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            idNuevoSMI = meLanbide58DAO.crearNuevoSMI(nuevoSMI, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando  un smi : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando un smi : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return idNuevoSMI;
    }

    public String recalcularSMI(String numExp, String numLinea, AdaptadorSQLBD adapt) {
        if (log.isDebugEnabled()) {
            log.debug("recalcularSMI : BEGIN");
        }
        MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
        Connection con = null;
        String anio = "";
        String rdo = "";
        ErrorBean errorB = new ErrorBean();
        try {
            con = adapt.getConnection();
            // anio = meLanbide58DAO.obtenerAnio(numExp, con);
            int ani = meLanbide58DAO.getAnioAyuda(numExp, con);
            anio = String.valueOf(ani);
            rdo = meLanbide58DAO.recalcularSMI(numExp, numLinea, anio, con);

            if (rdo.equalsIgnoreCase(ConstantesMeLanbide58.OK)) {
                log.debug("El recálculo ha finalizado con éxito.");
            } else if (rdo.equalsIgnoreCase(ConstantesMeLanbide58.NOT_FOUND_SMI)) {
                String error = "Error al obtener SMI";
                errorB.setIdError(rdo);
                errorB.setMensajeError("La tabla MELANBIDE58_VALOR_SMI no contiene la información solicitada (No encuentra el SMI)");
                errorB.setSituacion("recalcularSMI");
                MeLanbide58Manager.grabarError(errorB, error, error, numExp);
            } else if (rdo.equalsIgnoreCase(ConstantesMeLanbide58.UPDATE_RECALCULO)) {
                String error = "Error al actualizar la tabla MELANBIDE58_SMI";
                errorB.setIdError(rdo);
                errorB.setMensajeError("Error al actualizar la tabla MELANBIDE58_SMI realizando el recálculo de la subvención para el expediente de Subvención de Centros Especiales de Empleo");
                errorB.setSituacion("recalcularSMI");
                MeLanbide58Manager.grabarError(errorB, error, error, numExp);
            }

            if (log.isDebugEnabled()) {
                log.debug("recalcularSMI : END");
            }
            //}

        } catch (SQLException e) {
            log.error("Error Oracle en recálculo de subvención: " + e);
            rdo = ConstantesMeLanbide58.ERROR_RECALCULO;
            String error = "Error Oracle en recálculo de subvención: " + e.toString();

            errorB.setIdError(rdo);
            errorB.setMensajeError("Error Oracle realizando el recálculo de la subvención para el expediente de Subvención de Centros Especiales de Empleo");
            errorB.setSituacion("recalcularSMI");
            MeLanbide58Manager.grabarError(errorB, error, error, numExp);

        } catch (Exception ex) {
            log.error("Error en la funcion recalcularSMI: " + ex);
            String error = "Error en la funcion recalcularSMI: " + ex.toString();

            errorB.setIdError(rdo);
            errorB.setMensajeError("Error en la funcion recalcularSMI");
            errorB.setSituacion("recalcularSMI");

            MeLanbide58Manager.grabarError(errorB, error, error, numExp);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
            return rdo;
        }
    }

    public ArrayList getSmiMesDia(String numExpediente, Integer codOrg, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getSmiMesDia(numExpediente, codOrg, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el Salario Mínimo Interprofesional(SMI):  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando el Salario Mínimo Interprofesional(SMI):   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public ArrayList getTotalSubvencion(String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getTotalSubvencion(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el total de la Subvención del SMI:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando el total de la Subvención del SMI:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarSMI(SMIVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.modificarSMI(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  un smi : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un smi : " + ex.getMessage(), ex);
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

    public SMIVO getDatosPadrePorID(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getDatosPadrePorId(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre el padre del hijo:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre el padre del hijo:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getNifSMIPorId(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entra en getNifSMIPorId - Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getNifSMIPorId(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre el padre del hijo:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre el padre del hijo:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Integer getLineaPadreBporDNI(String numExp, String dni_nif, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entra en getLineaPadreBporDNI - Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getLineaPadreBporDNI(numExp, dni_nif, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lineaPadre del hijo con DNI:  " + dni_nif, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lineaPadre del hijo con DNI:  " + dni_nif, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Integer getLineaPadreBValidadaDNI(String numExp, String dni_nif, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entra en getLineaPadreBValidadaDNI - Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getLineaPadreBValidadaDNI(numExp, dni_nif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre el padre del hijo:  " + dni_nif, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre el padre del hijo:  " + dni_nif, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Integer getLineaPadreValidadaID(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getLineaPadreValidadaID(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre el padre del hijo:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre el padre del hijo:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO C -  PLANTILLA    ------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
    public List<PlantillaVO> getPlantilla(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getPlantillaPorDni(numExp, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre los controles de Acceso ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre los controles de Acceso ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

    }

    public List<PlantillaVO> getDatosControlAcceso(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getDatosControlAcceso(numExp, codOrganizacion, con);
            //recueperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_SEXO, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getSexo())) {
                        acce.setDesSexo(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TIPODIS, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getTipoDis())) {
                        acce.setDesTipoDis(valordesp.getDes_nom());
                        break;
                    }
                }
            }
         listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_COD_CONTRATO, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getCodContrato())) {
                        acce.setDesCodContrato(valordesp.getDes_nom());
                        log.debug(acce.getDesCodContrato());
                        break;
                    }
                }
            }
            // Discapacidad severa
            listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getDiscSevera())) {
                        acce.setDesDiscSevera(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getDiscValidada())) {
                        acce.setDesDiscValidada(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre los controles de Acceso ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre los controles de Acceso ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public PlantillaVO getControlAccesoPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getControlAccesoPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de un control de Acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un control de Acceso:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public PlantillaVO getPersonaAnexoCPorDNI(String dni, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getControlAccesoPorDNI(dni, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de un control de Acceso:  " + dni, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un control de Acceso:  " + dni, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<PlantillaVO> getPlantillaSinProcesar(String numExp, AdaptadorSQLBD adapt) throws Exception {
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getPlantillaSinProcesar(numExp, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre los controles de Acceso ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre los controles de Acceso ", ex);
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
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.eliminarAcceso(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una liena de control de Acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una liena de control de Acceso:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<PlantillaVO> getRegistrosAcceso(String numExp, String nif, String numLinea, String apellidos, AdaptadorSQLBD adapt) throws Exception {
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getRegistroAcceso(numExp, nif, numLinea, apellidos, con);

            //recueperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_SEXO, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getSexo())) {
                        acce.setDesSexo(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TIPODIS, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getTipoDis())) {
                        acce.setDesTipoDis(valordesp.getDes_nom());
                        break;
                    }
                }
            }
      
            listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_COD_CONTRATO, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getCodContrato())) {
                        acce.setDesCodContrato(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            // Discapacidad severa
            listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getDiscSevera())) {
                        acce.setDesDiscSevera(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            listaValoresDesplegable = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            for (PlantillaVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaValoresDesplegable) {
                    if (valordesp.getDes_val_cod().equals(acce.getDiscValidada())) {
                        acce.setDesDiscValidada(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los Registros de Acceso:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando los Registros de Acceso:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoAcceso(PlantillaVO nuevoAcceso, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.crearNuevoAcceso(nuevoAcceso, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando  un Control de Acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando un Control de Acceso : " + ex.getMessage(), ex);
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

    public int modificarAccesosXpersona(PlantillaVO datModif, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        int insert = 0;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insert = meLanbide58DAO.modificarAccesosXpersona(datModif, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  un Control de Acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un Control de Acceso : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insert;
    }

    public boolean modificarAccesoSeleccionado(PlantillaVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.modificarAccesoSeleccionado(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  un Control de Acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un Control de Acceso : " + ex.getMessage(), ex);
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

    public void grabarImporteResolCEESC(int codOrganizacion, String numExpediente, AdaptadorSQLBD adaptador, int codTramite, String importeFinal) throws Exception {
        log.debug("grabarImporteResolCEESC BEGIN");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            meLanbide58DAO.grabarImporteResolCEESC(codOrganizacion, codTramite, numExpediente, con, importeFinal);

        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.debug("grabarImporteResolCEESC END");
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Integer getLineaPadrePorID(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getLineaPadrePorID(numExp, id, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lineaPadre del hijo con ID:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando d la lineaPadre del hijo con ID:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean copiaImportesSMI(String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.copiaImportesSMI(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD copiando Importes del expediente:  " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD copiando Importes del expediente:  " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    // -------------------------------------------------------------
    // ---------------------   PERSONAS DISCP   --------------------
    // -------------------------------------------------------------
    public Boolean actualizaDatoDiscSevera(String numExp, int id, String dni, String severa, Connection con) throws Exception {
        log.debug("Entra en actualizaDatoDiscSevera - Manager ");
        try {
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.actualizaDatoDiscSevera(numExp, id, dni, severa, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD  actualizando Discapacidad Severa Empresa:  " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion actualizando Discapacidad Severa Empresa:  " + numExp, ex);
            throw new Exception(ex);
        }
    }

    public Boolean actualizaDatosIncompletos(String numExp, String dni, String incompleto, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entra en actualizaDatosIncompletos - Manager ");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.actualizaDatosIncompletos(numExp, dni, incompleto, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD  actualizando datosIncompletos :  " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion actualizando datosIncompletos:  " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int existePersonaDisc(String dni, AdaptadorSQLBD adapt) throws Exception {
        int estado = 0;
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            estado = meLanbide58DAO.existePersonaDisc(dni, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD  comprobando existePersona en la tabla de personas discapacitadas:  " + dni, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion comprobando existePersona en la tabla de personas discapacitadas:  " + dni, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return estado;
    }

    public boolean tieneFechaBajaDisc(int idPersona, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.tieneFechaBajaDisc(idPersona, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD  comprobando tieneFechaBaja en la tabla de personas discapacitadas:  " + dni, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion comprobando tieneFechaBaja en la tabla de personas discapacitadas:  " + dni, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean nuevaPersonaDisc(DiscapacitadoVO persona, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entramos en nuevaPersonaDisc de " + this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.nuevaPersonaDisc(persona, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD  insertando en la tabla de personas discapacitadas:  " + persona.getDni() + " - ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion insertando en la tabla de personas discapacitadas:  " + persona.getDni() + " - ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean actualizarPersonaDisc(DiscapacitadoVO persona, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entramos en actualizarPersonaDisc de " + this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.actualizarPersonaDisc(persona, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD  actualizando en la tabla de personas discapacitadas:  " + persona.getDni() + " - ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion actualizando en la tabla de personas discapacitadas:  " + persona.getDni() + " - ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean actualizaJornadaB(String numExp, String apellidos, String dni_nif, Double porcJornada, String porcOriginal, AdaptadorSQLBD adapt) throws Exception {
        log.debug(">>>> ENTRA en actualizaJornadaB - ");
        boolean insertOK;
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.actualizaJornadaB(numExp, apellidos, dni_nif, porcJornada, porcOriginal, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD actualizando % jornada en Anexo B :  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD ractualizando % jornada en Anexo B:   " + ex);
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

    public boolean actualizaJornadaC(String numExp, String dni_nif, Double porcJornada, String porcOriginal, AdaptadorSQLBD adapt) throws Exception {
        log.debug(">>>> ENTRA en actualizaJornadaC - Manager");
        boolean insertOK;
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.actualizaJornadaC(numExp, dni_nif, porcJornada, porcOriginal, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD actualizando % jornada en Anexo C :  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD ractualizando % jornada en Anexo C:   " + ex);
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

    public List<SMIVO> getRegistrosSMIporNIF(String numExp, String dni_nif, AdaptadorSQLBD adapt) throws Exception {
        log.debug(">>>> ENTRA en getRegistrosSMIporNIF - ");
        List<SMIVO> lista = new ArrayList<SMIVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            lista = meLanbide58DAO.getRegistrosSMIporNIF(numExp, dni_nif, con);
            return lista;

        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los Registros de Acceso:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando los Registros de Acceso:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DiscapacitadoVO getPersonaDiscp(int codigo, String dni, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entramos en getPersonaDisc de " + this.getClass().getSimpleName());
        DiscapacitadoVO persona = new DiscapacitadoVO();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            persona = meLanbide58DAO.getPersonaDisc(codigo, dni, con);
            return persona;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando una persona discapacitada:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando una persona discapacitada:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DiscapacitadoVO> getDatosPersona(String dni, AdaptadorSQLBD adapt) throws Exception {
        log.debug(">>>> ENTRA en getDatosPersona - Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getDatosPersona(dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los datos de una persona discapacitada:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando los datos de una persona discapacitada:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean actualizaDiscValidada(String numExp, String dni, String validada, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entramos en actualizaDiscValidada de " + this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.actualizaDiscValidada(numExp, dni, validada, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD actualizando la discapacidad VALIDADA:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD actualizando la discapacidad VALIDADA:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int getMesAyuda(int codOrganizacion, String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entramos en getMesAyuda de " + this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getMesAyuda(codOrganizacion, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando recuperando el mes de la ayuda:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando recuperando el mes de la ayuda:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int getAnioAyuda(String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getAnioAyuda(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el año de la ayuda  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando el año de la ayuda:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public ArrayList getDniABajas(String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getDniABajas(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el DNI " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando el DNI:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Date getUltimaFechaAAltas(String numExp, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getUltimaFechaAAltas(numExp, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando ultima Fecha de Alta Anexo A   " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando ultima Fecha de Alta Anexo A :   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Date getUltimaFechaABajas(String numExp, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getUltimaFechaABajas(numExp, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando ultima Fecha de Baja Anexo A   " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando ultima Fecha de Baja Anexo A :   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabaFechaBaja(int codigo, String dni, Date fechaBaja, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.grabaFechaBajaDisc(codigo, dni, fechaBaja, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD grabando las fechas de Baja  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD grabando las fechas de Baja:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabaHoyFechaBaja(int codigo, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.grabarHoyFechaBajaDisc(codigo, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD grabando las fechas de Baja  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD grabando las fechas de Baja:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean borrarFechaBajaDisc(int codigo, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.borrarFechaBajaDisc(codigo, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD grabando las fechas de Baja  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD grabando las fechas de Baja:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public HashMap<String, Date> getFechasAltas(String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getFechasAltas(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando as fechas de Alta  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando as fechas de Alta:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public HashMap<String, Date> getFechasBajas(String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getFechasBajas(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando las fechas de Baja " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando as fechas de Baja:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DiscapacitadoVO> getPersonasBusqueda(String dni, String nombre, String apellidos, String tipo, String grado, String severa, String validez, String centro, String territorio, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getPersonasBusqueda(dni, nombre, apellidos, tipo, grado, severa, validez, centro, territorio, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de Personas Discapacitadas  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando datos de Personas Discapacitadas:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getNifAnexoCPorId(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getNifAnexoCPorId(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando DNI de Anexo C:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando DNI de Anexo C:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int modificarPersDiscp(DiscapacitadoVO persona, AdaptadorSQLBD adapt) throws Exception {
        log.debug(">>>> ENTRA en modificarPersDiscp - ");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.modificarPersDiscp(persona, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  Persona discapacitada : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando Persona discapacitada: " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int insertarPersDiscp(DiscapacitadoVO persona, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.insertarPersDiscp(persona, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  Persona discapacitada : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando Persona discapacitada: " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabarOid(String idRegistro, String idPersona, String idDocumento, String nombreFichero, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            if (meLanbide58DAO.tieneCertificado(idRegistro, idPersona, con)) {
                return meLanbide58DAO.actualizarOid(idRegistro, idPersona, idDocumento, nombreFichero, con);
            } else {
                return meLanbide58DAO.insertarOid(idRegistro, idPersona, idDocumento, nombreFichero, con);
            }
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD guardando el OID : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando el OID: " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String ultimaIdPorDni(String idPersona, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.ultimaIdPorDni(idPersona, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando ID de :  " + idPersona, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando ID de :  " + idPersona, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean borrarImportesSMI(String numExp, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.borrarImportesSMI(numExp, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD al borrar Importes SMI : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al borrar Importes SMI: " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean actualizaPersonaAnexoC(String expediente, String dni, String fecEmision, String tipoD, Double grado, String severo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.actualizaPersonaAnexoC(expediente, dni, fecEmision, tipoD, grado, severo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD actualizaPersonaAnexoC  :  " + dni, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD actualizaPersonaAnexoC :  " + dni, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean marcarNuevaAlta(String numExp, String dniAlta, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.marcarNuevaAlta(numExp, dniAlta, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD marcando Nueva Alta:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD marcando Nueva Alta:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean esNuevaAlta(String numExp, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.esNuevaAlta(numExp, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD  comprobando si esNuevaAlta:  " + dni, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion comprobando si esNuevaAlta  " + dni, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean faltanDatos(String numExp, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.faltanDatos(numExp, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD  comprobando si faltanDatos:  " + dni, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion comprobando si faltanDatos  " + dni, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int getUltimoNumLinea(String numExp, String tabla, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getUltimoNumLinea(numExp, tabla, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el último número de línea del expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando el último número de línea del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean existeAnexoC(String numExp, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.existeAnexoC(numExp, dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD comprobando si " + dni + " existe en el anexo C de " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion comprobando si " + dni + " existe en el anexo C de " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean coincideJornada(String numExp, String dni, Double jornada, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.coincideJornada(numExp, dni, jornada, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD comprobando si coincide el % de jornada en el anexo C de " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion comprobando si coincide el % de jornada en el anexo C de " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Map<String, String> getDocIncorrectosAnexos(String numExp, int tabla, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.getDocIncorrectosAnexo(numExp, tabla, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando getDocIncorrectosAnexos " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion general en la BBDD recuperando getDocIncorrectosAnexos:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabarDniCorrecto(String numExp, String id, String dni, int tabla, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            insertOK = meLanbide58DAO.grabarDniCorrecto(numExp, id, dni, tabla, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando una Contratacíon nueva : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando una Contratación : " + ex.getMessage(), ex);
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

    public boolean grabarAnexosProcesados(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entramos en grabarAnexosProcesados de " + this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            return meLanbide58DAO.grabarAnexosProcesados(codOrg, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al grabar Anexos Procesados:  ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion al grabar Anexos Procesados  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabarResultadoProcesar(int codOrg, String numExp, String resultado, AdaptadorSQLBD adapt) throws Exception {
        log.debug("Entramos en grabarResultadoProcesar de " + this.getClass().getSimpleName());
        Connection con = null;
        boolean graba = false;
        try {
            con = adapt.getConnection();
            MeLanbide58DAO meLanbide58DAO = MeLanbide58DAO.getInstance();
            adapt.inicioTransaccion(con);
            graba = meLanbide58DAO.grabarResultadoProcesar(codOrg, numExp, resultado, con);
            adapt.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al grabar Anexos Procesados:  ", e);
            adapt.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion al grabar Anexos Procesados  ", ex);
            adapt.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return graba;
    }
}
