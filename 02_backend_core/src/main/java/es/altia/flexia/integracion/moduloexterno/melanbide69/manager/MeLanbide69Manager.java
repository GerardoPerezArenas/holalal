package es.altia.flexia.integracion.moduloexterno.melanbide69.manager;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide69.dao.MeLanbide69DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.util.ConstantesMeLanbide69;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.ElementoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InfoCampoSuplementarioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InfoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.SocioVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoDesplegableModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide69Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide69Manager.class);

    //Instancia
    private static MeLanbide69Manager instance = null;

    private MeLanbide69Manager() {
    }

    //Devolvemos una única instancia de la clase a través de este método ya que el constructor es privado
    public static MeLanbide69Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide69Manager.class) {
                instance = new MeLanbide69Manager();
            }
        }
        return instance;
    }

    /**
     * Método que recupera el Idioma de la request para la gestion de
     * Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
    public int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide69.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            idioma = ConstantesMeLanbide69.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;

    }

    public InfoDesplegableVO obtenerInfoDesplegablePorCodigo(String codigo, String codCpto, AdaptadorSQLBD adapt) {
        Connection con = null;
        InfoDesplegableVO datos = null;
        try {
            con = adapt.getConnection();
            datos = MeLanbide69DAO.getInstance().obtenerInfoDesplegablePorCodigo(codigo, codCpto, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al obtener información del desplegable de código: " + codigo);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        return datos;
    }

    public ArrayList<FacturaVO> recuperarListaFacturas(String numExpediente, String tabla, String codOrg, String codProc, AdaptadorSQLBD adapt) {
        ArrayList<FacturaVO> lista = null;
        HashMap<String, String> desplegables = new HashMap<String, String>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            desplegables.put(ConstantesMeLanbide69.getCOD_DESPL_CONCEPTO(), ConstantesMeLanbide69.getPropVal_COD_DESPL_CPTO(codOrg, codProc));
            desplegables.put(ConstantesMeLanbide69.getCOD_DESPL_DESGLOSE_CPTO(), ConstantesMeLanbide69.getPropVal_COD_DESPL_DESGLOSE_CPTO(codOrg, codProc));
            lista = MeLanbide69DAO.getInstance().obtenerFacturas(numExpediente, tabla, desplegables, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al recuperar la lista de facturas");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        return lista;
    }

    public boolean guardarFactura(FacturaVO factura, String tabla, String secuencia, AdaptadorSQLBD adapt) throws BDException, SQLException {
        Connection con = null;
        boolean exito = false;
        try {
            con = adapt.getConnection();
            if (factura.getCodIdent() == -1) {
                exito = MeLanbide69DAO.getInstance().altaFactura(factura, tabla, secuencia, con);
            } else if (factura.getCodIdent() > 0) {
                exito = MeLanbide69DAO.getInstance().actualizarFactura(factura, tabla, con);
            }
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            throw bde;
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al aï¿½adir/modificar una factura a la BBDD");
            throw sqle;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        return exito;
    }

    public boolean borrarFactura(FacturaVO factura, String tabla, AdaptadorSQLBD adapt) throws BDException, SQLException {
        Connection con = null;
        boolean exito = false;
        try {
            con = adapt.getConnection();
            exito = MeLanbide69DAO.getInstance().borrarFactura(factura, tabla, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            throw bde;
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al eliminar una factura a la BBDD");
            throw sqle;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        return exito;
    }

    public Object getDatoSuplementarioExpediente(String codCampo, int tipoCampo, String codOrg, int ejerc, String codProc, String numExp, AdaptadorSQLBD adaptador) throws BDException, SQLException {
        Connection con = null;
        Object valorCampo = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            con = adaptador.getConnection();
            CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
            salida = el.getCampoSuplementarioExpediente(codOrg, String.valueOf(ejerc), numExp, codProc, codCampo, tipoCampo);
            if (salida.getStatus() == 0) { //salida 0, campo con valor y todo ok, salida 2, campo sin valor pero todo ok
                campoSuplementario = salida.getCampoSuplementario();
                switch (tipoCampo) {
                    case 1:
                        valorCampo = campoSuplementario.getValorNumero();
                        break;
                    case 3:
                        valorCampo = campoSuplementario.getValorFecha();
                        break;
                    case 6:
                        valorCampo = campoSuplementario.getValorDesplegable();
                        break;
                    case 2:
                    case 4:
                        valorCampo = campoSuplementario.getValorTexto();
                        break;
                    default:
                        break;
                }
                if (valorCampo != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Valor que tiene el campo " + codCampo + " = " + valorCampo);
                    }//if(log.isDebugEnabled())
                }
            } else if (salida.getStatus() != 2) {
                throw new SQLException(salida.getDescStatus());
            }
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            throw e;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar valores de datos suplementarios para el expediente  " + numExp, ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        return valorCampo;
    }

    public boolean setDatosSuplementariosExpediente(ArrayList<InfoCampoSuplementarioVO> datos, String codOrg, String codProc, int ejercicio, String numExp, AdaptadorSQLBD adaptador) throws BDException, SQLException {
        Connection con = null;
        int resultado = 0;
        boolean exito = false;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            for (InfoCampoSuplementarioVO csVO : datos) {
                resultado += MeLanbide69DAO.getInstance().grabarValorCampoSup(csVO.getCodigo(), csVO.getValor(), codOrg, codProc, ejercicio, numExp, con);
            }
            if (resultado == datos.size()) {
                adaptador.finTransaccion(con);
                exito = true;
            } else {
                adaptador.rollBack(con);
            }
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            adaptador.rollBack(con);
            throw e;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al grabar valores de datos suplementarios para el expediente  " + numExp, ex);
            adaptador.rollBack(con);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        return exito;
    }

    public InfoCampoSuplementarioVO recuperarCampoSupDes(String codCampo, int tipoCampo, String codOrg, int idioma, String codProc, int ejercicio, String numExp, AdaptadorSQLBD adaptador) throws BDException, SQLException {
        Connection con = null;
        InfoCampoSuplementarioVO csDesp = new InfoCampoSuplementarioVO();
        InfoDesplegableVO desp;
        String desCod = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = null;

        try {
            con = adaptador.getConnection();
            salida = el.getCampoSuplementarioExpediente(codOrg, String.valueOf(ejercicio), numExp, codProc, codCampo, tipoCampo);
            if (salida.getStatus() == 0 || salida.getStatus() == 2) {
                campoSuplementario = salida.getCampoSuplementario();
                csDesp.setCodigo(codCampo);
                if (campoSuplementario != null) {
                    csDesp.setValor(campoSuplementario.getValorDesplegable());
                    desCod = campoSuplementario.getCodigoDesplegable();
                } else {
                    desCod = MeLanbide69DAO.getInstance().getDesCodCampoSuplementario(codCampo, Integer.parseInt(codOrg), codProc, con);
                }
                desp = recuperarCampoDesplegable(codOrg, idioma, desCod);
                csDesp.setDesplegable(desp);
            } else {
                throw new SQLException(salida.getDescStatus());
            }
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            throw e;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar informaciï¿½n de un datos suplementarios tipo desplegable para el expediente  " + numExp, ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        return csDesp;
    }

    public InfoDesplegableVO recuperarCampoDesplegable(String codOrg, int idioma, String codCombo) throws SQLException {
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        CampoDesplegableModuloIntegracionVO campoDesplegable = null;
        ArrayList<ElementoDesplegableVO> elementosDesplegable = new ArrayList<ElementoDesplegableVO>();
        InfoDesplegableVO desp = null;

        salida = el.getCampoDesplegable(codOrg, codCombo);
        if (salida.getStatus() == 0) {
            campoDesplegable = salida.getCampoDesplegable();
            if (campoDesplegable != null) {
                ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores = campoDesplegable.getValores();

                desp = new InfoDesplegableVO();
                desp.setCodDesplegable(campoDesplegable.getCodigo());
                desp.setDescDesplegable(campoDesplegable.getDescripcion());
                for (ValorCampoDesplegableModuloIntegracionVO valor : valores) {
                    String cod = valor.getCodigo();
                    String val = getDescripcionDesplegableByIdioma(idioma, valor.getDescripcion());
                    ElementoDesplegableVO elem = new ElementoDesplegableVO(cod, val);
                    elementosDesplegable.add(elem);
                }
                desp.setParesCodVal(elementosDesplegable);
            }
        } else {
            throw new SQLException(salida.getDescStatus());
        }
        return desp;
    }

    public ExpedienteVO getDatosExpediente(int codOrg, String numExpediente, Connection con) throws Exception {
        return MeLanbide69DAO.getInstance().getDatosExpediente(codOrg, numExpediente, con);
    }

    public DatosEconomicosExpVO getImporteSubvencionYPorc(Integer edad, String sexo, String pro, String eje, Connection con) throws Exception {
        return MeLanbide69DAO.getInstance().getImporteSubvencion(edad, sexo, pro, eje, con);
    }

    public void actualizaImporteSubvencion(int codOrg, String numExpediente, Integer importeSubvencion, Connection con) throws Exception {
        actualizaSuplNumerico(codOrg, numExpediente, importeSubvencion, ConstantesMeLanbide69.getPropVal_CS_SUBV(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaImporteSubvencionCByS(int codOrg, String numExpediente, int importeSubvencion, Connection con) throws Exception {
        actualizaSuplNumerico(codOrg, numExpediente, importeSubvencion, ConstantesMeLanbide69.getPropVal_CS_SUBVCB(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaImportePrimerPagoCByS(int codOrg, String numExpediente, int importePrimerPago, Connection con) throws Exception {
        actualizaSuplNumerico(codOrg, numExpediente, importePrimerPago, ConstantesMeLanbide69.getPropVal_CS_PRIMERPAGOCB(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaImportePrimerPago(int codOrg, String numExpediente, Integer importePrimerPago, Connection con) throws Exception {
        actualizaSuplNumerico(codOrg, numExpediente, importePrimerPago, ConstantesMeLanbide69.getPropVal_CS_PRIMERPAGO(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaEdad(int codOrg, String numExpediente, Integer edad, Connection con) throws Exception {
        actualizaSuplNumerico(codOrg, numExpediente, edad, "EDAD", con);
    }

    private void actualizaSuplNumerico(int codOrg, String numExpediente, Integer valor, String nombreCampo, Connection con) throws Exception {
        MeLanbide69DAO.getInstance().deleteSuplNumerico(codOrg, numExpediente, nombreCampo, con);
        if (valor != null) {
            MeLanbide69DAO.getInstance().insertSuplNumerico(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }

    private void actualizaSuplDespl(int codOrg, String numExpediente, String valor, String nombreCampo, Connection con) throws Exception {
        MeLanbide69DAO.getInstance().deleteSuplDespl(codOrg, numExpediente, nombreCampo, con);
        if (valor != null) {
            MeLanbide69DAO.getInstance().insertSuplDespl(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }

    public void actualizaSexo(int codOrg, String numExpediente, String sexo, Connection con) throws Exception {
        actualizaSuplDespl(codOrg, numExpediente, sexo, "SEXO", con);
    }

    public List<SocioVO> getListaSocios(String numExpediente, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        log.debug("getListaSocios Manager ===>");
        List<SocioVO> listaSocios = new ArrayList<SocioVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide69DAO meLanbide69DAO = MeLanbide69DAO.getInstance();
            listaSocios = meLanbide69DAO.getListaSocios(numExpediente, codOrganizacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los SOCIOS ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return listaSocios;
    }

    public SocioVO getSocioPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide69DAO meLanbide69DAO = MeLanbide69DAO.getInstance();
            return meLanbide69DAO.getSocioPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una SOCIO:  " + id, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearSocio(SocioVO nuevoSocio, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide69DAO meLanbide69DAO = MeLanbide69DAO.getInstance();
            return meLanbide69DAO.crearSocio(nuevoSocio, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD grabando nuevo SOCIO:  ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarSocio(SocioVO socioModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide69DAO meLanbide69DAO = MeLanbide69DAO.getInstance();
            return meLanbide69DAO.modificarSocio(socioModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD grabando nuevo SOCIO:  ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarSocio(String id, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide69DAO meLanbide69DAO = MeLanbide69DAO.getInstance();
            return meLanbide69DAO.eliminarSocio(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD grabando nuevo SOCIO:  ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getDescripcionDesplegableByIdioma(int idioma, String descripcion) {
  //      log.info("getDescripcionDesplegable : descripcion " + descripcion + "idioma " + idioma);
        String barraIdioma = "\\|";
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = descripcion.split(barraIdioma);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (idioma == 4) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                } else {
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

    public Date[] getFechasNotificacion(int codOrg, String codProc, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        Date[] fechas = new Date[3];
        try {
            con = adapt.getConnection();
            MeLanbide69DAO meLanbide69DAO = MeLanbide69DAO.getInstance();
            fechas[0] = meLanbide69DAO.getFechaFirmaResolucion(codOrg, codProc, numExp, con);
            fechas[1] = meLanbide69DAO.getFechaEnvioNotificacion(codOrg, codProc, numExp, con);
            fechas[2] = meLanbide69DAO.getFechaAcuse(codOrg, codProc, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando las fechas de la notificación ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando las fechas de la notificación ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return fechas;
    }

}
