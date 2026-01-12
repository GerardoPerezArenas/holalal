package es.altia.flexia.integracion.moduloexterno.melanbide18.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide18.dao.MeLanbide18DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.DeudaZorkuVO;
import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.FilaDeudaFraccVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide18Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide18Manager.class);

    //Instancia
    private static MeLanbide18Manager instance = null;

    private MeLanbide18Manager() {

    }

    public static MeLanbide18Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide18Manager.class) {
                instance = new MeLanbide18Manager();
            }
        }
        return instance;
    }

    public String getDocumentoInteresado(String numExp, int codOrg, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide18DAO.getInstance().getDocumentoInteresado(numExp, codOrg, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el nº documento del interesado ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }
        
    public List<DeudaZorkuVO> getDeudasZorku(String numExp, int codOrg, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide18DAO meLanbide18DAO = MeLanbide18DAO.getInstance();
            String numDocumento =  meLanbide18DAO.getDocumentoInteresado(numExp, codOrg, con);  
            return meLanbide18DAO.getDeudasZorku( numDocumento, codOrg, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las deudas sin fraccionar ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public List<DeudaZorkuVO> getDeudasZorkuSinGrabar(String numExp, int codOrg, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
            try {
            con = adapt.getConnection();
            MeLanbide18DAO meLanbide18DAO = MeLanbide18DAO.getInstance();
            String numDocumento =  meLanbide18DAO.getDocumentoInteresado(numExp, codOrg, con);  
//getDocumentoInteresado(numExp, codOrg, adapt);
            return meLanbide18DAO.getDeudasZorkuSinGrabar(numExp, numDocumento, codOrg, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las deudas sin fraccionar ", ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public List<FilaDeudaFraccVO> getDeudasFracc(String numExpediente, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide18DAO meLanbide18DAO = MeLanbide18DAO.getInstance();
            return meLanbide18DAO.getDeudasFracc(numExpediente, codOrganizacion, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las deudas freaccionadas ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                throw new Exception(e);
            }
        }
    }

    public FilaDeudaFraccVO getDeudaFraccPorID(Integer id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide18DAO meLanbide18DAO = MeLanbide18DAO.getInstance();
            return meLanbide18DAO.getDeudaFraccPorID(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre una deuda fraccionada:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new Exception(e);
            }
        }
    }

    public boolean crearDeudaFracc(final String numExp, final List<Long> listaDeudas, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK = false;
        try {
            con = adapt.getConnection();
            MeLanbide18DAO meLanbide18DAO = MeLanbide18DAO.getInstance();
            if (listaDeudas != null && !listaDeudas.isEmpty()) {
//log.info("@@@@@ crearDeudaFracc numLiquidacion = " + numLiquidacion);
                final List<FilaDeudaFraccVO> deudasFracc = getDeudasFracc(numExp, codOrganizacion, adapt);
//log.info("@@@@@ crearDeudaFracc deudas = " + deudasFracc);
                if (deudasFracc.isEmpty()) {
//log.info("@@@@@ crearDeudaFracc: No existen deudas");                    
                    for (final Long liquidacionId : listaDeudas) {
                        DeudaZorkuVO deudaZorku = meLanbide18DAO.getDeudaZorkuPorNumLiq(liquidacionId, codOrganizacion, con);
                        log.info("crearDeudaFracc deudaGeneral = " + deudaZorku);
                        FilaDeudaFraccVO deudaAInsertar = new FilaDeudaFraccVO(null, numExp, deudaZorku.getExpediente(), deudaZorku.getImporteDeuda(), liquidacionId);
                        if (meLanbide18DAO.crearDeudaFracc(deudaAInsertar, con)) {
                            insertOK = true;
                        }                        
                    }
                } else {
//log.info("@@@@@ crearDeudaFracc: Existen deudas");                       
                    for (final Long liquidacionId : listaDeudas) {
//log.info("@@@@@ crearDeudaFracc: liquidacionId = " + liquidacionId); 
//log.info("@@@@@ crearDeudaFracc: resultado = " + meLanbide18DAO.getDeudaFraccPorNumLiquidacion(numExp, Long.valueOf(liquidacionId), con)); 

                        if (meLanbide18DAO.getDeudaFraccPorNumLiquidacion(numExp, liquidacionId, con) == null) {
//log.info("@@@@@ crearDeudaFracc No existe la liquidación = " + liquidacionId +  " en deudas fraccionadas");                    
                            for (final FilaDeudaFraccVO deudaFracc : deudasFracc) {
//log.info("@@@@@ crearDeudaFracc deudaFracc = " + deudaFracc);   
                                if (!deudaFracc.getNumLiquidacion().equals(liquidacionId)) {
                                    DeudaZorkuVO deudaZorku = meLanbide18DAO.getDeudaZorkuPorNumLiq(liquidacionId, codOrganizacion, con);
//log.info("@@@@@ crearDeudaFracc deudaGeneral = " + deudaGeneral);
                                    FilaDeudaFraccVO deudaAInsertar = new FilaDeudaFraccVO(null, numExp, deudaZorku.getExpediente(), deudaZorku.getImporteDeuda(), liquidacionId);
                                    if (meLanbide18DAO.crearDeudaFracc(deudaAInsertar, con)) {
                                        insertOK = true;
                                    }
                                }
                            }
                        }                                        
                    }
                }
            }

        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una deuda fraccionada : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new Exception(e);
            }
        }
        return insertOK;
    }

    public int eliminarDeudaFracc(Integer id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        int eliminarOK;

        try {
            con = adapt.getConnection();
            MeLanbide18DAO meLanbide18DAO = MeLanbide18DAO.getInstance();
            eliminarOK = meLanbide18DAO.eliminarDeudaFracc(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una deuda fraccionada : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new Exception(e);
            }
        }
        return eliminarOK;
    }

    public List<String> getListaExpedientesFraccionar(String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide18DAO.getInstance().getListaExpedientesFraccionar(numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD getListaExpedientesFraccionar ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean tieneMarcaTelematica(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide18DAO.getInstance().tieneMarcaTelematica(codOrg, numExp, con);
            } catch (Exception e) {
            log.error("Se ha producido una excepción en BBDD comprobando el trámite ", e);
                throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
            }
        }

    public boolean estaEnTramiteNotificacion(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide18DAO.getInstance().estaEnTramiteNotificacion(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepción en BBDD comprobando el trámite ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }
}
