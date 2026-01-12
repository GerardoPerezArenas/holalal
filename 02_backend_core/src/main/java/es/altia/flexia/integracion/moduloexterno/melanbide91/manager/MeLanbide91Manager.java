
package es.altia.flexia.integracion.moduloexterno.melanbide91.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.ContrGenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.dao.MeLanbide91DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.SubvenSolicVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide91Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide91Manager.class);

    //Instancia
    private static MeLanbide91Manager instance = null;

    public static MeLanbide91Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide91Manager.class) {
                instance = new MeLanbide91Manager();
            }
        }
        return instance;
    }

    public List<ContrGenVO> getListaAccesos(String numExp, int codOrganizacion,int idioma, AdaptadorSQLBD adapt) throws Exception {
        List<ContrGenVO> lista = new ArrayList<ContrGenVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            lista = meLanbide91DAO.getListaContrGen(numExp, codOrganizacion,idioma, con);
            log.info("devuelve getListaAccesos");

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los accesos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los accesos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                 log.info("CERRANDO CONEXION listaAccesos");
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
     public List<SubvenSolicVO> getListaSubvenciones(String numExp, int codOrganizacion,int idioma, AdaptadorSQLBD adapt) throws Exception {
        List<SubvenSolicVO> lista = new ArrayList<SubvenSolicVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            lista = meLanbide91DAO.getListaSubvencion(numExp, codOrganizacion,idioma, con);
           log.debug("devuelve listaSubvenciones");
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los accesos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los accesos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                   log.info("CERRANDO CONEXION getListaSubvenciones");
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public ContrGenVO getAccesoPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            return meLanbide91DAO.getContrGenPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un acceso:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
     public SubvenSolicVO getSubvencionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            return meLanbide91DAO.getSubvencionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un acceso:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearAcceso(ContrGenVO nuevoAcceso, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            insertOK = meLanbide91DAO.crearContrGen(nuevoAcceso, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando un acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando un acceso : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarAcceso(ContrGenVO accesoModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            return meLanbide91DAO.modificarContrGen(accesoModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando un acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando un acceso : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int eliminarAcceso(String id, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            return meLanbide91DAO.eliminarContrGen(id, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar un acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar un acceso:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
       public boolean crearSubvencion(SubvenSolicVO nuevoAcceso, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            insertOK = meLanbide91DAO.crearSubvenSolic(nuevoAcceso, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando un acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando un acceso : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarSubvencion(SubvenSolicVO accesoModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            return meLanbide91DAO.modificarSubvencion(accesoModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando un acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando un acceso : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int eliminarSubvencion(String id, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            return meLanbide91DAO.eliminarSubvencion(id, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar un acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar un acceso:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
             
    public List<DesplegableVO> getValoresDesplegables(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            return meLanbide91DAO.getValoresDesplegables(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }    
    
    public String getDescripcionDesplegableByIdioma(int idioma, String descripcion ) {
       log.info("getDescripcionDesplegable : descripcion " + descripcion + "idioma " + idioma);
        String barraIdioma = "\\|" ;
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = descripcion.split(barraIdioma);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length >1) {
                    if (idioma == 4) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                } else{
                    log.info("El tamano del no es valido " + descripcion);
                }
            } else {
                descripcion = "-";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
    }

  
}
