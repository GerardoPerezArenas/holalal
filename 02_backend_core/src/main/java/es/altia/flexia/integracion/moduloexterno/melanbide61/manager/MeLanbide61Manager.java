/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide61.manager;

import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.dao.MeLanbide61DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConstantesMeLanbide61;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaDocumentoContableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.SubSolicVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.Tercero;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.TrabajadorCAPVValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ValorCampoDesplegableEstadoVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide61Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide61Manager.class);

    //Instancia
    private static MeLanbide61Manager instance = null;

    private MeLanbide61Manager() {

    }

    public static MeLanbide61Manager getInstance() {
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : BEGIN");
        }
        if (instance == null) {
            synchronized (MeLanbide61Manager.class) {
                instance = new MeLanbide61Manager();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : END");
        }
        return instance;
    }

    public List<FilaContratoRenovacionPlantillaVO> getContratosExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception {
        if (log.isDebugEnabled()) {
            log.debug("getContratosExpediente( numExpediente = " + numExp + " ) : BEGIN");
        }
        List<FilaContratoRenovacionPlantillaVO> retList = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide29DAO = MeLanbide61DAO.getInstance();
            retList = meLanbide29DAO.getContratosExpediente(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("getContratosExpediente() : END");
        }
        return retList;
    }

    public ContratoRenovacionPlantillaVO getContratoExpedientePorNumContrato(String numExp, Integer numContrato, AdaptadorSQLBD adaptador) throws BDException, Exception {
        if (log.isDebugEnabled()) {
            log.debug("getContratoExpedientePorNumContrato( numExpediente = " + numExp + " numContrato = " + numContrato + ") : BEGIN");
        }
        ContratoRenovacionPlantillaVO contrato = null;
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide29DAO = MeLanbide61DAO.getInstance();
            contrato = meLanbide29DAO.getContratoExpedientePorNumContrato(numExp, numContrato, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("getContratoExpedientePorNumContrato() : END");
        }
        return contrato;
    }

    public String getMaxIdContrato(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception {
        if (log.isDebugEnabled()) {
            log.debug("getMaxIdContrato( numExpediente = " + numExp + ") : BEGIN");
        }
        String id = "0";
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide29DAO = MeLanbide61DAO.getInstance();
            id = meLanbide29DAO.getMaxIdContrato(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: ", e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("getContratoExpedientePorNumContrato() : END");
        }
        return id;
    }

    public boolean guardarContrato(ContratoRenovacionPlantillaVO contrato, AdaptadorSQLBD adaptador) throws BDException, Exception {
        Connection con = null;
        boolean ret = false;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            ret = meLanbide61DAO.guardarContrato(contrato, con);
        } catch (BDException e) {
            throw new Exception(e);
        } catch (Exception ex) {
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return ret;
    }

    public List<Tercero> busquedaTerceros(Tercero tercero, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<Tercero> retList = new ArrayList<Tercero>();
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide29DAO = MeLanbide61DAO.getInstance();
            //retList =  meLanbide29DAO.busquedaTerceros(tercero, con);
            retList = meLanbide29DAO.busquedaTercerosIntermediacion(tercero, con);
        } catch (BDException e) {
            throw new Exception(e);
        } catch (Exception ex) {
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }

    public int getNuevoNumContrato(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;
        int retValue = -1;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide29DAO = MeLanbide61DAO.getInstance();
            retValue = meLanbide29DAO.getNuevoNumContrato(numExpediente, con);
        } catch (BDException e) {
            throw new Exception(e);
        } catch (Exception ex) {
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return retValue;
    }

    public boolean crearContrato(ContratoRenovacionPlantillaVO contrato, AdaptadorSQLBD adaptador) throws BDException, Exception {
        Connection con = null;
        boolean ret = false;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide29DAO = MeLanbide61DAO.getInstance();

            ret = meLanbide29DAO.crearContrato(contrato, con);
        } catch (BDException e) {
            throw new Exception(e);
        } catch (Exception ex) {
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return ret;
    }

    public boolean eliminarContrato(String numExp, Integer numContrato, AdaptadorSQLBD adaptador) throws BDException, Exception {
        if (log.isDebugEnabled()) {
            log.debug("eliminarContrato( numExpediente = " + numExp + " numContrato = " + numContrato + ") : BEGIN");
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide29DAO = MeLanbide61DAO.getInstance();
            boolean retValue = meLanbide29DAO.eliminarContrato(numExp, numContrato, con);
            if (log.isDebugEnabled()) {
                log.debug("eliminarContrato() : END");
            }
            return retValue;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar el contrato " + numContrato + "para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar el contrato " + numContrato + "para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaDocumentoContableVO> getListaDocumentosContablesExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide29DAO = MeLanbide61DAO.getInstance();
            return meLanbide29DAO.getListaDocumentosContablesExpediente(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al recuperar el listado de documentos contables para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al recuperar el listado de documentos contables para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public ArrayList<ValorCampoDesplegableEstadoVO> getListaDesplegable(AdaptadorSQLBD adaptador, String codigo, Integer idioma) throws BDException, Exception {
        ArrayList<ValorCampoDesplegableEstadoVO> lista = new ArrayList<ValorCampoDesplegableEstadoVO>();
        Connection con = null;
        try {
            con = adaptador.getConnection();
            lista = MeLanbide61DAO.getInstance().getListaDesplegable(con, codigo, idioma);
        } catch (Exception ex) {
            log.error("Error al cerrar conexión a la BBDD: " + ex.getMessage());
        } finally {
            if (con != null) {
                adaptador.devolverConexion(con);
            }
        }
        return lista;
    }

    public String getValorFechaAcuse(String numExpediente, String codCampo, AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;

        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            String codigo = meLanbide61DAO.getValorFechaAcuseExpediente(numExpediente, codCampo, con);

            return codigo;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al recuperar el código del campo desplegable externo " + codCampo + " en el expediente " + numExpediente + ": " + e.getMessage());
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error al recuperar el código del campo desplegable externo " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean insertFechaDatoGeneral(String numExpediente, String fechaAcuse, AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;

        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            boolean insert = meLanbide61DAO.insertDatoGeneral(numExpediente, fechaAcuse, con);

            return insert;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al insertar el dato general " + fechaAcuse + " en el expediente " + numExpediente + ": " + e.getMessage());
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error al insertar el dato general " + fechaAcuse + " en el expediente " + numExpediente + ": " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String comprobarDniEnExpedientes(AdaptadorSQLBD adaptador, String numExp, String dni) {
        String mensaje = "";
        Connection con = null;
        boolean ret = false;
        try {
            con = adaptador.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            String expdts = "";
            List<ExpedienteVO> listaExp = meLanbide61DAO.getExpedientesPorDNI(numExp, dni, con);
            if (listaExp.size() > 0) {
                for (int i = 0; i < listaExp.size(); i++) {
                    expdts = listaExp.get(i).getNumero() + ", ";
                }
                mensaje = "Los siguientes expedientes: " + expdts + " tienen el dni " + dni;
            }
        } catch (Exception ex) {
            log.error("Error al cerrar conexión a la BBDD: " + ex.getMessage());
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return mensaje;
    }

    public List<TrabajadorCAPVValueObject> getTrabajadoresCAPV(String numExp, AdaptadorSQLBD adapt) throws BDException, SQLException {
        Connection con = null;
        List<TrabajadorCAPVValueObject> lista = null;

        try {
            con = adapt.getConnection();

            lista = MeLanbide61DAO.getInstance().getTrabajadoresCAPV(numExp, con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD");
            throw bde;
        } catch (SQLException e) {
            log.error("Error al recuperar la relación de trabajadores en la CAPV");
            throw e;
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException ex) {
                log.error("Error al devolver la conexión con la BBDD");
            }
        }

        return lista;
    }

    public boolean darAltaTrabajadorCAPV(TrabajadorCAPVValueObject fila, String numExp, AdaptadorSQLBD adapt) throws BDException, SQLException {
        Connection con = null;
        boolean exito = false;

        try {
            con = adapt.getConnection();

            exito = MeLanbide61DAO.getInstance().insertarTrabajadorCAPV(fila, numExp, con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD");
            throw bde;
        } catch (SQLException e) {
            log.error("Error al añadir datos a la CAPV");
            throw e;
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException ex) {
                log.error("Error al devolver la conexión con la BBDD");
            }
        }

        return exito;
    }

    public boolean modificarTrabajadorCAPV(long id, TrabajadorCAPVValueObject fila, String numExp, AdaptadorSQLBD adapt) throws BDException, SQLException {
        Connection con = null;
        boolean exito = false;

        try {
            con = adapt.getConnection();

            exito = MeLanbide61DAO.getInstance().actualizarTrabajadorCAPV(id, fila, numExp, con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD");
            throw bde;
        } catch (SQLException e) {
            log.error("Error al modificar datos de un registro de la CAPV");
            throw e;
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException ex) {
                log.error("Error al devolver la conexión con la BBDD");
            }
        }

        return exito;
    }

    public boolean eliminarTrabajadorCAPV(long id, AdaptadorSQLBD adapt) throws BDException, SQLException {
        Connection con = null;
        boolean exito = false;

        try {
            con = adapt.getConnection();

            exito = MeLanbide61DAO.getInstance().eliminarTrabajadorCAPV(id, con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD");
            throw bde;
        } catch (SQLException e) {
            log.error("Error al eliminar un registro de la CAPV");
            throw e;
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException ex) {
                log.error("Error al devolver la conexión con la BBDD");
            }
        }

        return exito;
    }

    public Date obtenerFechaIniRelevo(String numExpediente, Connection con) throws Exception {
        if (log.isDebugEnabled()) {
            log.error("obtenerFechaIniRelevo ( numExpediente = " + numExpediente + " ) : BEGIN");
        }
        MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();

        if (log.isDebugEnabled()) {
            log.error("comprobarExpGenerado() : END");
        }
        return meLanbide61DAO.getInstance().obtenerFechaIniRelevo(numExpediente, con);
    }

    public static boolean insertarFecha20Relevo(int codOrganizacion, String numExpediente, Date fecha20Relevo, Connection con) throws SQLException {
        if (log.isDebugEnabled()) {
            log.error("insertarFecha20Relevo ( numExpediente = " + numExpediente + " ) : BEGIN");
        }
        MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();

        if (log.isDebugEnabled()) {
            log.error("insertarFecha20Relevo() : END");
        }
        return meLanbide61DAO.getInstance().insertarFecha20Relevo(codOrganizacion, numExpediente, fecha20Relevo, con);

    }

    public static boolean eliminarFecha20Relevo(String numExpediente, Connection con) throws SQLException {
        if (log.isDebugEnabled()) {
            log.error("eliminarFecha20Relevo ( numExpediente = " + numExpediente + " ) : BEGIN");
        }
        MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();

        if (log.isDebugEnabled()) {
            log.error("eliminarFecha20Relevo() : END");
        }
        return meLanbide61DAO.getInstance().eliminarFecha20Relevo(numExpediente, con);
    }

    public DatosEconomicosExpVO getImporteSubvencionYPorc(String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        DatosEconomicosExpVO datos = new DatosEconomicosExpVO();
        try {
            con = adapt.getConnection();
            datos = MeLanbide61DAO.getInstance().getImporteSubvencionYPorc(numExpediente, con);
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD");
            throw bde;
        } catch (SQLException e) {
            log.error("Error al recuperar importe y porcentaje de la subvención");
            throw e;
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException ex) {
                log.error("Error al devolver la conexión con la BBDD");
            }
        }
        return datos;
    }

    public boolean guardarImportes(int codOrganizacion, String numExpediente, DatosEconomicosExpVO datosEcon, AdaptadorSQLBD adapt) throws BDException, SQLException {
        Connection con = null;
        boolean exito = false;
        int campos = 0;
        try {
            con = adapt.getConnection();
            if (MeLanbide61DAO.getInstance().existeValorImporte(numExpediente, con)) {
                campos = campos + MeLanbide61DAO.getInstance().actualizarImportes(codOrganizacion, numExpediente, datosEcon, con);
            } else {
                campos = campos + MeLanbide61DAO.getInstance().guardarImportes(codOrganizacion, numExpediente, datosEcon, con);
            }
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al guardar los importes de la subvención");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        return campos == 3;
    }

    public boolean haySustituto(String numExpediente, AdaptadorSQLBD adapt) throws BDException, SQLException {
        Connection con = null;
        boolean existe = false;
        try {
            con = adapt.getConnection();
            existe = MeLanbide61DAO.getInstance().haySustituto(numExpediente, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al consultar si hay sustituto");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        return existe;
    }

    
      public List<SubSolicVO> getDatosSubSolic(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            lista = meLanbide61DAO.getDatosSubSolic(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de deDatosSubSolicsplegables para traducir en la tabla principal
            List<DesplegableVO> listaEstado = MeLanbide61Manager.getInstance().getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_ESTADO, ConstantesMeLanbide61.FICHERO_PROPIEDADES), adapt);
            for (SubSolicVO subvencion : lista) {
                for (DesplegableVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(subvencion.getEstado())) {
                        subvencion.setDesEstado(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre SubSolic ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
   public SubSolicVO getSubSolicPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            return meLanbide61DAO.getSubSolicPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un SubSolic:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un SubSolic:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoSubSolic(SubSolicVO nuevaSubSolic, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            insertOK = meLanbide61DAO.crearNuevoSubSolic(nuevaSubSolic, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando un SubSolic : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando un SubSolic : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarSubSolic(SubSolicVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            insertOK = meLanbide61DAO.modificarSubSolic(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando un SubSolic : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando un SubSolic : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public int eliminarSubSolic(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            return meLanbide61DAO.eliminarSubSolic(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar un SubSolic:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar un SubSolic:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DesplegableVO> getValoresDesplegables(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            return meLanbide61DAO.getValoresDesplegables(des_cod, con);
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

    public boolean esTramiteAbierto (String numExpediente, String codTramite, String codProcedimiento, AdaptadorSQLBD adapt) throws Exception {
        boolean estaAbierto;
        Connection conn = null;

        try {
            conn = adapt.getConnection();
            MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
            estaAbierto = meLanbide61DAO.esTramiteAbierto(numExpediente, codTramite, codProcedimiento, conn);
        } catch (Exception e) {
            log.error("Se ha producido un error en esTramiteAbierto ", e);
            throw new Exception(e);
        } try {
            adapt.devolverConexion(conn);
        } catch (BDException e) {
            log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
        }

        return estaAbierto;
    }
}
