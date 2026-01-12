/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide88.dao.Melanbide88DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide88.dao.Melanbide88InversionesDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide88.dao.Melanbide88SociosDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide88.dao.Melanbide88SubsolicDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Inversiones;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Socios;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Subsolic;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class Melanbide88Manager {
    
    private static final Logger LOG = LogManager.getLogger(Melanbide88Manager.class);
    private final SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Melanbide88DAO melanbide88DAO = new Melanbide88DAO();
    private final Melanbide88SociosDAO melanbide88SociosDAO = new Melanbide88SociosDAO();
    private final Melanbide88InversionesDAO melanbide88InversionesDAO = new Melanbide88InversionesDAO();
    private final Melanbide88SubsolicDAO melanbide88SubsolicDAO = new Melanbide88SubsolicDAO();
    
    public List<Melanbide88Socios> getMelanbide88SociosByNumExp(String numeroExpediente, Connection con) throws Exception {
        LOG.info(" getMelanbide88SociosByNumExp - Begin " + formatFechaLog.format(new Date()));
        return melanbide88SociosDAO.getMelanbide88SociosByNumExp(numeroExpediente, con);
    }

    public List<Melanbide88Socios> getMelanbide88SociosByNumExp(String numeroExpediente, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" getMelanbide88SociosByNumExp - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.getMelanbide88SociosByNumExp(numeroExpediente, con);
        } catch (BDException e) {
            LOG.error("BDException Recogiendo Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Recogiendo Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Melanbide88Socios getMelanbide88SociosByID(Long id, Connection con) throws Exception {
        LOG.info(" getMelanbide88SociosByNumExp - Begin " + formatFechaLog.format(new Date()));
        return melanbide88SociosDAO.getMelanbide88SociosByID(id, con);
    }

    public Melanbide88Socios getMelanbide88SociosByID(Long id, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" getMelanbide88SociosByID - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.getMelanbide88SociosByID(id, con);
        } catch (BDException e) {
            LOG.error("BDException Recogiendo Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Recogiendo Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<Melanbide88Inversiones> getMelanbide88InversionesByNumExp(String numeroExpediente, Connection con) throws Exception {
        LOG.info(" getMelanbide88InversionesByNumExp - Begin " + formatFechaLog.format(new Date()));
        return melanbide88InversionesDAO.getMelanbide88InversionesByNumExp(numeroExpediente, con);
    }

    public List<Melanbide88Inversiones> getMelanbide88InversionesByNumExp(String numeroExpediente, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" getMelanbide88InversionesByNumExp - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.getMelanbide88InversionesByNumExp(numeroExpediente, con);
        } catch (BDException e) {
            LOG.error("BDException Recogiendo Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Recogiendo Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Melanbide88Inversiones getMelanbide88InversionesByID(Long id, Connection con) throws Exception {
        LOG.info(" getMelanbide88InversionesByNumExp - Begin " + formatFechaLog.format(new Date()));
        return melanbide88InversionesDAO.getMelanbide88InversionesByID(id, con);
    }

    public Melanbide88Inversiones getMelanbide88InversionesByID(Long id, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" getMelanbide88InversionesByID - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.getMelanbide88InversionesByID(id, con);
        } catch (BDException e) {
            LOG.error("BDException Recogiendo Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Recogiendo Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<Melanbide88Subsolic> getMelanbide88SubsolicByNumExp(String numeroExpediente, Connection con) throws Exception {
        LOG.info(" getMelanbide88SubsolicByNumExp - Begin " + formatFechaLog.format(new Date()));
        return melanbide88SubsolicDAO.getMelanbide88SubsolicByNumExp(numeroExpediente, con);
    }

    public List<Melanbide88Subsolic> getMelanbide88SubsolicByNumExp(String numeroExpediente, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" getMelanbide88SubsolicByNumExp - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.getMelanbide88SubsolicByNumExp(numeroExpediente, con);
        } catch (BDException e) {
            LOG.error("BDException Recogiendo Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Recogiendo Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Melanbide88Subsolic getMelanbide88SubsolicByID(Long id, Connection con) throws Exception {
        LOG.info(" getMelanbide88SubsolicByNumExp - Begin " + formatFechaLog.format(new Date()));
        return melanbide88SubsolicDAO.getMelanbide88SubsolicByID(id, con);
    }

    public Melanbide88Subsolic getMelanbide88SubsolicByID(Long id, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" getMelanbide88SubsolicByID - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.getMelanbide88SubsolicByID(id, con);
        } catch (BDException e) {
            LOG.error("BDException Recogiendo Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Recogiendo Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean guardarMelanbide88Socios(int codIdioma, Melanbide88Socios datos, Connection con) throws Exception {
        LOG.info(" guardarMelanbide88Socios - Begin " + formatFechaLog.format(new Date()));
        return melanbide88SociosDAO.saveMelanbide88Socios(datos, con);
    }

    public boolean guardarMelanbide88Socios(int codIdioma, Melanbide88Socios datos, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" guardarMelanbide88Socios - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.guardarMelanbide88Socios(codIdioma,datos, con);
        } catch (BDException e) {
            LOG.error("BDException Guardando Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Guardando Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean eliminarMelanbide88Socios(Long id, Connection con) throws Exception {
        LOG.info(" eliminarMelanbide88Socios - Begin " + formatFechaLog.format(new Date()));
        return melanbide88SociosDAO.deleteMelanbide88SociosByID(id, con);
    }

    public boolean eliminarMelanbide88Socios(Long id, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" eliminarMelanbide88Socios - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.eliminarMelanbide88Socios(id, con);
        } catch (BDException e) {
            LOG.error("BDException Eliminando Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Eliminando Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean guardarMelanbide88Inversiones(int codIdioma, Melanbide88Inversiones datos, Connection con) throws Exception {
        LOG.info(" guardarMelanbide88Inversiones - Begin " + formatFechaLog.format(new Date()));
        return melanbide88InversionesDAO.saveMelanbide88Inversiones(datos, con);
    }

    public boolean guardarMelanbide88Inversiones(int codIdioma, Melanbide88Inversiones datos, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" guardarMelanbide88Inversiones - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.guardarMelanbide88Inversiones(codIdioma,datos, con);
        } catch (BDException e) {
            LOG.error("BDException Guardando Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Guardando Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean eliminarMelanbide88Inversiones(Long id, Connection con) throws Exception {
        LOG.info(" eliminarMelanbide88Inversiones - Begin " + formatFechaLog.format(new Date()));
        return melanbide88InversionesDAO.deleteMelanbide88InversionesByID(id, con);
    }

    public boolean eliminarMelanbide88Inversiones(Long id, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" eliminarMelanbide88Inversiones - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.eliminarMelanbide88Inversiones(id, con);
        } catch (BDException e) {
            LOG.error("BDException Eliminando Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Eliminando Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean guardarMelanbide88Subsolic(int codIdioma, Melanbide88Subsolic datos, Connection con) throws Exception {
        LOG.info(" guardarMelanbide88Subsolic - Begin " + formatFechaLog.format(new Date()));
        return melanbide88SubsolicDAO.saveMelanbide88Subsolic(datos, con);
    }

    public boolean guardarMelanbide88Subsolic(int codIdioma, Melanbide88Subsolic datos, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" guardarMelanbide88Subsolic - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.guardarMelanbide88Subsolic(codIdioma,datos, con);
        } catch (BDException e) {
            LOG.error("BDException Guardando Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Guardando Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean eliminarMelanbide88Subsolic(Long id, Connection con) throws Exception {
        LOG.info(" eliminarMelanbide88Subsolic - Begin " + formatFechaLog.format(new Date()));
        return melanbide88SubsolicDAO.deleteMelanbide88SubsolicByID(id, con);
    }

    public boolean eliminarMelanbide88Subsolic(Long id, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info(" eliminarMelanbide88Subsolic - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.eliminarMelanbide88Subsolic(id, con);
        } catch (BDException e) {
            LOG.error("BDException Eliminando Datos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Eliminando Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<SelectItem> getDesplegableE_DES(String codigo,int idioma, Connection con) throws Exception {
        LOG.info(" getDesplegableE_DES - Begin " + formatFechaLog.format(new Date()));
        return melanbide88DAO.getDesplegableE_DES(codigo,idioma, con);
    }
    public List<SelectItem> getDesplegableE_DES(String codigo,int idioma, AdaptadorSQLBD adaptador) throws Exception{
        LOG.info(" getDesplegableE_DES - Begin " + formatFechaLog.format(new Date()));
        Connection con = null;
        List<SelectItem> respuesta = new ArrayList<SelectItem>();
        try {
            con = adaptador.getConnection();
            return this.getDesplegableE_DES(codigo,idioma, con);
        } catch (BDException e) {
            LOG.error("BDException Recuperando Lista Desplegable " + codigo, e);
            throw new Exception(e);
        } catch (Exception ex) {
            LOG.error("Exception Eliminando Datos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
}
