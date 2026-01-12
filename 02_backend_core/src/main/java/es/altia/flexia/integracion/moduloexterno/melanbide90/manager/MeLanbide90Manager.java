package es.altia.flexia.integracion.moduloexterno.melanbide90.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide90.dao.MeLanbide90DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConstantesMeLanbide90;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FamiliaSolicitadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FilaMinimisVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide90Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide90Manager.class);

    //Instancia
    private static MeLanbide90Manager instance = null;

    public static MeLanbide90Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide90Manager.class) {
                instance = new MeLanbide90Manager();
            }
        }
        return instance;
    }

    public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide90DAO meLanbide90DAO = MeLanbide90DAO.getInstance();
            lista = meLanbide90DAO.getDatosMinimis(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide90Manager.getInstance().getValoresDesplegablesXdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_ESTADO_AYUDA, ConstantesMeLanbide90.FICHERO_PROPIEDADES), adapt);

            for (FilaMinimisVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(cont.getEstado())) {
                        cont.setDesEstado(valordesp.getDes_nom());
//                        log.debug("Estado: " + cont.getDesEstado());
                        break;
                    }
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las minimis ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las minimis ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaMinimisVO getMinimisPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide90DAO meLanbide90DAO = MeLanbide90DAO.getInstance();
            return meLanbide90DAO.getMinimisPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarMinimis(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide90DAO meLanbide90DAO = MeLanbide90DAO.getInstance();
            return meLanbide90DAO.eliminarMinimis(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una minimis:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide90DAO meLanbide90DAO = MeLanbide90DAO.getInstance();
            insertOK = meLanbide90DAO.crearNuevoMinimis(nuevaMinimis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una minimis : " + ex.getMessage(), ex);
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

    public boolean modificarMinimis(FilaMinimisVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide90DAO meLanbide90DAO = MeLanbide90DAO.getInstance();
            insertOK = meLanbide90DAO.modificarMinimis(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una minimis : " + ex.getMessage(), ex);
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

    public List<FacturaVO> getDatosFacturas(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getDatosFacturas Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getDatosFacturas(codOrg, numExp, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos de las facturas ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public List<FacturaVO> getDatosFacturasFamilia(int codOrg, String numExp, String familia, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getDatosFacturasFamilia Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getDatosFacturasFamilia(codOrg, numExp, familia, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos de las facturas ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public FacturaVO getFacturaPorId(String id, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getFacturaPorId Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getFacturaPorId(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando una factura ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean nuevaFactura(FacturaVO factura, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> nuevaFactura Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().nuevaFactura(factura, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al dar de alta una FACTURA en el expediente  " + factura.getNumExp(), ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean modificarFactura(FacturaVO factura, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> modificarFactura Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().modificarFactura(factura, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al modificar una FACTURA en el expediente  " + factura.getNumExp(), ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean eliminarFactura(String id, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> eliminarFactura Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().eliminarFactura(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una FACTURA ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int getNumeroFamilias(String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getNumeroFamilias Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getNumeroFamilias(numExp, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el número de familias del expediente  " + numExp, ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public List<FamiliaSolicitadaVO> getFamiliasSolicitadas(String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getFamiliasSolicitadas Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getFamiliasSolicitadas(numExp, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el número de familias del expediente  " + numExp, ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public FamiliaSolicitadaVO getFamiliaSolicitadaExpedienteCodigo(String numExp, String codFamilia, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getFamiliaSolicitadaExpedienteCodigo Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getFamiliaSolicitadaExpedienteCodigo(numExp, codFamilia, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el número de familias del expediente  " + numExp, ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getDocumentoInteresado(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getDocumentoInteresado Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getDocumentoInteresado(codOrg, numExp, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el documento del interesado del expediente  " + numExp, ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int getEspecialidadesFamiliaEjer(String familia, String numDocumento, int ejercicio, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getEspecialidadesFamiliaEjer Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getEspecialidadesFamiliaEjer(familia, numDocumento, ejercicio, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el número de especialidades x familia y ejercicio  ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public Map<Integer, Integer> getAFsFamiliaPeriodo(String familia, String numDocumento, int ejercicio, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getAFsFamiliaPeriodo Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getAFsFamiliaPeriodo(familia, numDocumento, ejercicio, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el número de especialidades x familia y ejercicio  ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public Double[] getImportesMaximosEjer(int ejercicio, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getImportesMaximosEjer Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getImportesMaximosEjer(ejercicio, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el número de especialidades x familia y ejercicio  ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesXdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide90DAO meLanbide90DAO = MeLanbide90DAO.getInstance();
            return meLanbide90DAO.getValoresDesplegablesXdes_cod(des_cod, con);
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

    public String getValorDesplegableExpediente(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getValorDesplegableExpediente Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getValorDesplegableExpediente(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando un desplegable de expediente ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getCodSelDesplegableExterno(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getValorDesplegableExpediente Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide90DAO.getInstance().getCodSelDesplegableExterno(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando un desplegable externo de expediente ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

}
