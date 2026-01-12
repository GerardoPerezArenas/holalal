/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide51.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.ControlAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide51.dao.MeLanbide51DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide51.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide51.util.ConstantesMeLanbide51;
import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.CriteriosBusquedaAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.DesplegableAdmonLocalVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide51Manager {
    
     //Logger
    private static Logger log = LogManager.getLogger(MeLanbide51Manager.class);
    
    //Instancia
    private static MeLanbide51Manager instance = null;
    
    public static MeLanbide51Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide51Manager.class)
            {
                instance = new MeLanbide51Manager();
            }
        }
        return instance;
    }

    public List<ControlAccesoVO> getDatosControlAcceso(int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide51DAO meLanbide51DAO = MeLanbide51DAO.getInstance();
            lista = meLanbide51DAO.getDatosControlAcceso(codOrganizacion, con);
            //recueperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaMotivoVisita = MeLanbide51Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide51.COD_DESP_MOTIVO_VISITA,ConstantesMeLanbide51.FICHERO_PROPIEDADES),adapt);
            for(ControlAccesoVO acce : lista){
                for(DesplegableAdmonLocalVO valordesp : listaMotivoVisita){
                    if(valordesp.getDes_val_cod().equals(acce.getCod_mot_visita())){
                        acce.setDes_mot_visita(valordesp.getDes_nom());
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
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public ControlAccesoVO getControlAccesoPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide51DAO meLanbide51DAO = MeLanbide51DAO.getInstance();
            return meLanbide51DAO.getControlAccesoPorID(id,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de un control de Acceso:  " + id , e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un control de Acceso:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarAcceso(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide51DAO meLanbide51DAO = MeLanbide51DAO.getInstance();
            return meLanbide51DAO.eliminarAcceso(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una liena de control de Acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una liena de control de Acceso:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoAcceso(ControlAccesoVO nuevoAcceso, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide51DAO meLanbide51DAO = MeLanbide51DAO.getInstance();
            insertOK = meLanbide51DAO.crearNuevoAcceso(nuevoAcceso, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando  un Control de Acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando un Control de Acceso : " + ex.getMessage(), ex);
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

    public boolean modificarAcceso(ControlAccesoVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide51DAO meLanbide51DAO = MeLanbide51DAO.getInstance();
            insertOK = meLanbide51DAO.modificarAcceso(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  un Control de Acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un Control de Acceso : " + ex.getMessage(), ex);
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
            MeLanbide51DAO meLanbide51DAO = MeLanbide51DAO.getInstance();
            return meLanbide51DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
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

    public List<ControlAccesoVO> busquedaFiltrandoListaAcceso(CriteriosBusquedaAccesoVO _criteriosBusquedaAccesoVO, AdaptadorSQLBD adapt) throws Exception {
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide51DAO meLanbide51DAO = MeLanbide51DAO.getInstance();
            lista = meLanbide51DAO.busquedaFiltrandoListaAcceso(_criteriosBusquedaAccesoVO, con);
            //recueperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaMotivoVisita = MeLanbide51Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide51.COD_DESP_MOTIVO_VISITA, ConstantesMeLanbide51.FICHERO_PROPIEDADES), adapt);
            for (ControlAccesoVO acce : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaMotivoVisita) {
                    if (valordesp.getDes_val_cod().equals(acce.getCod_mot_visita())) {
                        acce.setDes_mot_visita(valordesp.getDes_nom());
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
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    
    public ControlAccesoVO getControlAccesoPorDNICIF(String cif_dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide51DAO meLanbide51DAO = MeLanbide51DAO.getInstance();
            return meLanbide51DAO.getControlAccesoPorDNICIF(cif_dni, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de un control de Acceso:  " + cif_dni , e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un control de Acceso:  " + cif_dni, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String actualizaVistasMaterializadas(String vistas, Connection con) throws Exception
    {
        try
        {
            return MeLanbide51DAO.getInstance().actualizaVistasMaterializadas(vistas, con);
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }
    
}
