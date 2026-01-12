package es.altia.flexia.integracion.moduloexterno.melanbide06.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide06.MeLanbide06Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide06.dao.GestionAvisosDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.GestionAvisosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.Melanbide06GA;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.util.conexion.BDException;
import java.util.Date;


/**
 * @author jaime.hermoso
 * @version 15/12/2022 1.0
 * Historial de cambios:
 * <ol>
 *  <li>jaime.hermoso * 17/01/2013 * Edición inicial</li>
 * </ol> 
 */
public class GestionAvisosManager {
    
    //Logger
    private static Logger log = LogManager.getLogger(GestionAvisosManager.class);
    
    GestionAvisosDAO gestionAvisosDAO;
    
    //Instance
    private static GestionAvisosManager instance = null;
    
    /**
     * Devuelve una unica instancia (Singleton) de la clase GestionAvisosManager
     * 
     * @return GestionAvisosManager
     */
    public static GestionAvisosManager getInstance(){
       if(log.isDebugEnabled()) log.info("getInstance() : BEGIN"); 
       if(instance == null){
            synchronized(GestionAvisosManager.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.info("Creamos una nueva instancia");
                    instance = new GestionAvisosManager();
                }//if(instance == null)
            }//synchronized(GestionAvisosManager.class)
       }//if(instance == null)
       if(log.isDebugEnabled()) log.info("getInstance() : BEGIN"); 
       return instance;
    }//getInstance

    
    public List<SelectItem> getListaProcedimientos(AdaptadorSQLBD adaptador) throws Exception {
		Connection con = null;
        try {
			con = adaptador.getConnection();
			GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
			return gestionAvisosDAO.getListaProcedimientos(con);
		} catch(BDException e) {
			log.error("Se ha producido una excepcion en la BBDD recuperando Procedimientos.");
            throw new Exception(e);
		} catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando Procedimientos.");
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }		
}
    
    
public List<SelectItem> getListaUors(AdaptadorSQLBD adaptador) throws Exception {
		Connection con = null;
        try {
			con = adaptador.getConnection();
			GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
			return gestionAvisosDAO.getListaUors(con);
		} catch(BDException e) {
			log.error("Se ha producido una excepcion en la BBDD recuperando uors.");
            throw new Exception(e);
		} catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando uors.");
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }		
}    

public List<SelectItem> getListaAsuntos(AdaptadorSQLBD adaptador) throws Exception {
		Connection con = null;
        try {
			con = adaptador.getConnection();
			GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
			return gestionAvisosDAO.getListaAsuntos(con);
		} catch(BDException e) {
			log.error("Se ha producido una excepcion en la BBDD recuperando asuntos.");
            throw new Exception(e);
		} catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando asuntos.");
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }		
}   

public List<SelectItem> getListaEventos(AdaptadorSQLBD adaptador) throws Exception {
		Connection con = null;
        try {
			con = adaptador.getConnection();
			GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
			return gestionAvisosDAO.getListaEventos(con);
		} catch(BDException e) {
			log.error("Se ha producido una excepcion en la BBDD recuperando eventos.");
            throw new Exception(e);
		} catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando eventos.");
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }		
}

        public List<SelectItem> getListaOrganizaciones(AdaptadorSQLBD adaptador) throws Exception {
		Connection con = null;
                try {
			con = adaptador.getConnection();
			GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
			return gestionAvisosDAO.getListaOrganizaciones(con);
		} catch(BDException e) {
			log.error("Se ha producido una excepcion en la BBDD recuperando organizaciones.");
                throw new Exception(e);
                    } catch (Exception ex) {
                log.error("Se ha producido una excepcion recuperando organizaciones.");
                throw new Exception(ex);
                } finally {
                    try {
                        adaptador.devolverConexion(con);
                    } catch (BDException e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                        adaptador.rollBack(con);
                    }
                }		
        } 

        public List<SelectItem> getListaEntidades(AdaptadorSQLBD adaptador) throws Exception {
		Connection con = null;
                try {
                                con = adaptador.getConnection();
                                GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
                                return gestionAvisosDAO.getListaEntidades(con);
                        } catch(BDException e) {
                                log.error("Se ha producido una excepcion en la BBDD recuperando entidades.");
                    throw new Exception(e);
                        } catch (Exception ex) {
                    log.error("Se ha producido una excepcion recuperando entidades.");
                    throw new Exception(ex);
                } finally {
                    try {
                        adaptador.devolverConexion(con);
                    } catch (BDException e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                        adaptador.rollBack(con);
                    }
                }		
        } 

        public SelectItem getRescatarProcedimientoByCodAsunto(String codAsunto,AdaptadorSQLBD adaptador) throws MeLanbide06Exception {
            //log.info("getRescatarProcedimientoByCodAsunto() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + numExpediente );
            Connection con = null;
            GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
            try {
                con = adaptador.getConnection();
                return gestionAvisosDAO.getRescatarProcedimientoByCodAsunto(codAsunto, con);
            } catch (Exception ex) {
                log.error("Se ha producido una excepción general al recuperar el código del procedimiento " + ex.getMessage(), ex);
                throw new MeLanbide06Exception("Error al leer el código del procedimiento. " + ex.getMessage(), ex);
            } finally {
                try {
                    adaptador.devolverConexion(con);
                    //log.info("getRescatarProcedimientoByCodAsunto() - Manager - End  "+ formatFechaLog.format(new Date()));
                } catch (Exception e) {
                    log.error("Error al cerrar conexión a la BBDD: getRescatarProcedimientoByCodAsunto - " + e.getMessage());
                }
            }
        }
    
        public List<SelectItem> getRescatarUorByCodProcedimiento(String codPro,AdaptadorSQLBD adaptador) throws MeLanbide06Exception {
            //log.info("getRescatarUorByCodProcedimiento() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + numExpediente );
            Connection con = null;
            GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
            try {
                con = adaptador.getConnection();
                return gestionAvisosDAO.getRescatarUorByCodProcedimiento(codPro, con);
            } catch (Exception ex) {
                log.error("Se ha producido una excepción general al recuperar el código del uor " + ex.getMessage(), ex);
                throw new MeLanbide06Exception("Error al leer el código del uor. " + ex.getMessage(), ex);
            } finally {
                try {
                    adaptador.devolverConexion(con);
                    //log.info("getRescatarUorByCodProcedimiento() - Manager - End  "+ formatFechaLog.format(new Date()));
                } catch (Exception e) {
                    log.error("Error al cerrar conexión a la BBDD: getRescatarUorByCodProcedimiento - " + e.getMessage());
                }
            }
        }
    
        public void getAnadirGA(Melanbide06GA datosGA,AdaptadorSQLBD adaptador) throws MeLanbide06Exception {
            //log.info("getAnadirGA() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + numExpediente );
            Connection con = null;
            GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
            try {
                con = adaptador.getConnection();
                gestionAvisosDAO.getAnadirGA(datosGA, con);
            } catch (Exception ex) {
                log.error("Se ha producido una excepción general al anadir un GA " + ex.getMessage(), ex);
                throw new MeLanbide06Exception("Error al leer al anadir un GA. " + ex.getMessage(), ex);
            } finally {
                try {
                    adaptador.devolverConexion(con);
                    //log.info("getAnadirGA() - Manager - End  "+ formatFechaLog.format(new Date()));
                } catch (Exception e) {
                    log.error("Error al cerrar conexión a la BBDD: getAnadirGA - " + e.getMessage());
                }
            }
        }
        
        public void getEliminarGA(String id,AdaptadorSQLBD adaptador) throws MeLanbide06Exception {
            //log.info("getAnadirGA() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + numExpediente );
            Connection con = null;
            GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
            try {
                con = adaptador.getConnection();
                gestionAvisosDAO.getEliminarGA(id, con);
            } catch (Exception ex) {
                log.error("Se ha producido una excepción general al eliminar un GA " + ex.getMessage(), ex);
                throw new MeLanbide06Exception("Error al leer al eliminar un GA. " + ex.getMessage(), ex);
            } finally {
                try {
                    adaptador.devolverConexion(con);
                    //log.info("getEliminarGA() - Manager - End  "+ formatFechaLog.format(new Date()));
                } catch (Exception e) {
                    log.error("Error al cerrar conexión a la BBDD: getEliminarGA - " + e.getMessage());
                }
            }
        }
    
        public void getModificarGA(Melanbide06GA datosGA, String id, AdaptadorSQLBD adaptador) throws MeLanbide06Exception {
            //log.info("getModificarGA() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + numExpediente );
            Connection con = null;
            GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
            try {
                con = adaptador.getConnection();
                gestionAvisosDAO.getModificarGA(datosGA, id, con);
            } catch (Exception ex) {
                log.error("Se ha producido una excepción general al modificar un GA " + ex.getMessage(), ex);
                throw new MeLanbide06Exception("Error al leer al modificar un GA. " + ex.getMessage(), ex);
            } finally {
                try {
                    adaptador.devolverConexion(con);
                    //log.info("getModificarGA() - Manager - End  "+ formatFechaLog.format(new Date()));
                } catch (Exception e) {
                    log.error("Error al cerrar conexión a la BBDD: getModificarGA - " + e.getMessage());
                }
            }
        }
    
        public List<GestionAvisosVO> getDatosGA(AdaptadorSQLBD adaptador) throws MeLanbide06Exception {
           //log.info("getDatosGA() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + numExpediente );
           Connection con = null;
           GestionAvisosDAO gestionAvisosDAO = GestionAvisosDAO.getInstance();
           try {
               con = adaptador.getConnection();
               return gestionAvisosDAO.getDatosGA(con);
           } catch (Exception ex) {
               log.error("Se ha producido una excepción general al recuperar los datos de GA " + ex.getMessage(), ex);
               throw new MeLanbide06Exception("Error al leer los datos de GA. " + ex.getMessage(), ex);
           } finally {
               try {
                   adaptador.devolverConexion(con);
                   //log.info("getDatosGA() - Manager - End  "+ formatFechaLog.format(new Date()));
               } catch (Exception e) {
                   log.error("Error al cerrar conexión a la BBDD: getDatosGA - " + e.getMessage());
               }
           }
       }

    public GestionAvisosVO recuperarConfiguracionEmail(AdaptadorSQLBD adaptador, String codEvento, String codigoBusqueda, String opcion) {     
        if(log.isDebugEnabled()) log.info("recuperarConfiguracionEmail() : BEGIN");
        Connection con = null;
        GestionAvisosVO gestionAvisosVO = new GestionAvisosVO();
        try{
            con = adaptador.getConnection();
            GestionAvisosDAO gestionAvisosDao = GestionAvisosDAO.getInstance();
            if(log.isDebugEnabled()) log.info("Recuperamos la configuración del email");
            gestionAvisosVO = gestionAvisosDao.getConfiguracionEmail(con, codEvento, codigoBusqueda, opcion);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la configuración del email " + ex.getMessage());
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }//try-catch-finally
        if(log.isDebugEnabled()) log.info("recuperarConfiguracionEmail() : END");
        
        return gestionAvisosVO;
    }
    
    public String buscarEventoExtracto(AdaptadorSQLBD adaptador, String extracto) {     
        if(log.isDebugEnabled()) log.info("buscarEventoExtracto() : BEGIN");
        Connection con = null;
        String resultado = null;
        try{
            con = adaptador.getConnection();
            GestionAvisosDAO gestionAvisosDao = GestionAvisosDAO.getInstance();
            if(log.isDebugEnabled()) log.info("Recuperamos el evento");
            if(extracto != null && !extracto.equals("")){
                resultado = gestionAvisosDao.buscarEventoExtracto(con, extracto);
            }else{
               resultado = null; 
            }
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el evento " + ex.getMessage());
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }//try-catch-finally
        if(log.isDebugEnabled()) log.info("buscarEventoExtracto() : END");
        
        return resultado;
    }
     
}
