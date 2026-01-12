
package es.altia.flexia.integracion.moduloexterno.melanbide72.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide72.dao.MeLanbide72DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConstantesMeLanbide72;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA1BCVO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA2VO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide72Manager 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide72Manager.class);
    
    //Instancia
    private static MeLanbide72Manager instance = null;
    
    private MeLanbide72Manager()
    {
        
    }
    
    public static MeLanbide72Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide72Manager.class)
            {
                instance = new MeLanbide72Manager();
            }
        }
        return instance;
    } 
    
    public List<MedidaA1BCVO> getDatosMedidasA1(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            lista = meLanbide72DAO.getDatosMedidasA1(numExp, codOrganizacion, con);
            
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las medidas alternativas A1 ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las medidas alternativas A1 ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<MedidaA2VO> getDatosMedidasA2(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<MedidaA2VO> lista = new ArrayList<MedidaA2VO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            lista = meLanbide72DAO.getDatosMedidasA2(numExp, codOrganizacion, con);
            
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las medidas alternativas A2 ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las medidas alternativas A2 ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<MedidaA1BCVO> getDatosMedidasB(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            lista = meLanbide72DAO.getDatosMedidasB(numExp, codOrganizacion, con);
            
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las medidas alternativas B ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las medidas alternativas B ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<MedidaA1BCVO> getDatosMedidasC(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            lista = meLanbide72DAO.getDatosMedidasC(numExp, codOrganizacion, con);
            
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las medidas alternativas C ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las medidas alternativas C ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
   
    public Double getCampoSuplementarioNumerico(String numExp, int codOrganizacion, String codCampSupl,AdaptadorSQLBD adapt) throws Exception {
        Double valorCampoSuplementario = 0.0;
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            valorCampoSuplementario = meLanbide72DAO.getCampoSuplementarioNumerico(numExp, codOrganizacion, codCampSupl, con);
            
            return valorCampoSuplementario;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el campo suplementario numérico ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el campo suplementario numérico ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public MedidaA1BCVO getMedidaA1PorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            return meLanbide72DAO.getMedidaA1PorID(id,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de una medida A1 :  " + id , e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una medida A1 :  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public MedidaA2VO getMedidaA2PorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            return meLanbide72DAO.getMedidaA2PorID(id,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de una medida A2 :  " + id , e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una medida A2 :  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public MedidaA1BCVO getMedidaBPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            return meLanbide72DAO.getMedidaBPorID(id,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de una medida B :  " + id , e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una medida B :  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public MedidaA1BCVO getMedidaCPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            return meLanbide72DAO.getMedidaCPorID(id,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre de una medida C :  " + id , e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una medida C :  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean crearNuevaMedidaA1(MedidaA1BCVO nuevaMedidaA1, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            insertOK = meLanbide72DAO.crearNuevaMedidaA1(nuevaMedidaA1, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una Medida A1 : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una Medida A1 : " + ex.getMessage(), ex);
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
    
    public boolean modificarMedidaA1(MedidaA1BCVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            insertOK = meLanbide72DAO.modificarMedidaA1(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD Actualizando una Medida A1 : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD Actualizando una Medida A1 : " + ex.getMessage(), ex);
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
    
    public int eliminarMedidaA1(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            return meLanbide72DAO.eliminarMedidaA1(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una Medida A1 :  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una Medida A1 :   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean crearNuevaMedidaA2(MedidaA2VO nuevaMedidaA2, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            insertOK = meLanbide72DAO.crearNuevaMedidaA2(nuevaMedidaA2, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una Medida A2 : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una Medida A2 : " + ex.getMessage(), ex);
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
    
    public boolean modificarMedidaA2(MedidaA2VO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            insertOK = meLanbide72DAO.modificarMedidaA2(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD Actualizando una Medida A2 : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD Actualizando una Medida A2 : " + ex.getMessage(), ex);
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
    
    public int eliminarMedidaA2(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            return meLanbide72DAO.eliminarMedidaA2(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una Medida A2 :  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una Medida A2 :   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean crearNuevaMedidaB(MedidaA1BCVO nuevaMedidaB, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            insertOK = meLanbide72DAO.crearNuevaMedidaB(nuevaMedidaB, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una Medida B : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una Medida B : " + ex.getMessage(), ex);
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
    
    public boolean modificarMedidaB(MedidaA1BCVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            insertOK = meLanbide72DAO.modificarMedidaB(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD Actualizando una Medida B : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD Actualizando una Medida B : " + ex.getMessage(), ex);
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
    
    public int eliminarMedidaB(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            return meLanbide72DAO.eliminarMedidaB(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una Medida B :  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una Medida B :   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean crearNuevaMedidaC(MedidaA1BCVO nuevaMedidaC, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            insertOK = meLanbide72DAO.crearNuevaMedidaC(nuevaMedidaC, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una Medida C : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una Medida C : " + ex.getMessage(), ex);
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
    
    public boolean modificarMedidaC(MedidaA1BCVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            insertOK = meLanbide72DAO.modificarMedidaC(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD Actualizando una Medida C : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD Actualizando una Medida C : " + ex.getMessage(), ex);
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
    
    public int eliminarMedidaC(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            return meLanbide72DAO.eliminarMedidaC(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una Medida C :  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una Medida C :   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean guardarSuplementarios(int codOrganizacion, String numExp, String num_trab_sust_A1,
                                                                             String periodo_contr_A1,
                                                                             String num_trab_sust_A2,
                                                                             String periodo_contr_A2,
                                                                             String num_trab_sust_B,
                                                                             String periodo_contr_B,
                                                                             String num_trab_sust_C,
                                                                             String periodo_contr_C,
                                                                             String pers_disc_ocup_C,
                                                                             String cuantificacion_econ,
                                                                             String imp_anual_total,
                                                                             String contratacion_total,
                                         AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean guardarOK;
        try {
            con = adapt.getConnection();
            MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
            guardarOK = meLanbide72DAO.guardarSuplementarios(codOrganizacion, numExp, num_trab_sust_A1,
                                                                                      periodo_contr_A1,
                                                                                      num_trab_sust_A2,
                                                                                      periodo_contr_A2,
                                                                                      num_trab_sust_B,
                                                                                      periodo_contr_B,
                                                                                      num_trab_sust_C,
                                                                                      periodo_contr_C,
                                                                                      pers_disc_ocup_C,
                                                                                      cuantificacion_econ,
                                                                                      imp_anual_total,
                                                                                      contratacion_total, 
                                                             con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD guardando los campos suplementarios : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD guardando los campos suplementarios : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return guardarOK;
    }
    
    
}


