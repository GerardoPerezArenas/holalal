package es.altia.flexia.integracion.moduloexterno.melanbide66.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide66.dao.MeLanbide66DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.util.ConstantesMeLanbide66;
import es.altia.flexia.integracion.moduloexterno.melanbide66.util.MeLanbide66Util;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.InfoCampoSuplementarioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.InfoDesplegableVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.ElementoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.SocioVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoDesplegableModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide66Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide66Manager.class);

    //Instancia
    private static MeLanbide66Manager instance = null;
    private static final MeLanbide66Util m66Util = new MeLanbide66Util();

    private MeLanbide66Manager() {
    }

    //Devolvemos una única instancia de la clase a través de este método ya que el constructor es privado
    public static MeLanbide66Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide66Manager.class) {
                instance = new MeLanbide66Manager();
            }
        }
        return instance;
    }

    public InfoDesplegableVO obtenerInfoDesplegablePorCodigo(String codigo, String codCpto, AdaptadorSQLBD adapt) {
        Connection con = null;
        InfoDesplegableVO datos = null;

        try {
            con = adapt.getConnection();

            datos = MeLanbide66DAO.getInstance().obtenerInfoDesplegablePorCodigo(codigo, codCpto, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al obtener información del desplegable de código: " + codigo);
            sqle.printStackTrace();
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

    public ArrayList<FacturaVO> recuperarListaFacturas(String numExpediente, String tabla, String codOrg,String codProc, HttpServletRequest request, AdaptadorSQLBD adapt) {
        ArrayList<FacturaVO> lista = null;
        HashMap<String, String> desplegables = new HashMap<String, String>();
        Connection con = null; 

        try {
            con = adapt.getConnection();
            String[] partes = numExpediente.split("/");
            Integer ejercicio = Integer.parseInt(partes[0]);
            desplegables.put(ConstantesMeLanbide66.getCOD_DESPL_CONCEPTO(), ConstantesMeLanbide66.getPropVal_COD_DESPL_CPTO(codOrg, codProc, ejercicio));
            desplegables.put(ConstantesMeLanbide66.getCOD_DESPL_DESGLOSE_CPTO(), ConstantesMeLanbide66.getPropVal_COD_DESPL_DESGLOSE_CPTO(codOrg, codProc, ejercicio));
            lista = MeLanbide66DAO.getInstance().obtenerFacturas(numExpediente, tabla, desplegables,request, con );
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al recuperar la lista de facturas");
            sqle.printStackTrace();
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
                exito = MeLanbide66DAO.getInstance().altaFactura(factura, tabla, secuencia, con);
            } else if (factura.getCodIdent() > 0) {
                exito = MeLanbide66DAO.getInstance().actualizarFactura(factura, tabla, con);
            }
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al añadir/modificar una factura a la BBDD");
            sqle.printStackTrace();
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

            exito = MeLanbide66DAO.getInstance().borrarFactura(factura, tabla, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al eliminar una factura a la BBDD");
            sqle.printStackTrace();
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
                if (tipoCampo == 1) {
                    valorCampo = campoSuplementario.getValorNumero();
                } else if (tipoCampo == 3) {
                    valorCampo = campoSuplementario.getValorFecha();
                } else if (tipoCampo == 6) {
                    valorCampo = campoSuplementario.getValorDesplegable();
                } else if (tipoCampo == 2 || tipoCampo == 4) {
                    valorCampo = campoSuplementario.getValorTexto();
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
                resultado += MeLanbide66DAO.getInstance().grabarValorCampoSup(csVO.getCodigo(), csVO.getValor(), codOrg, codProc, ejercicio, numExp, con);
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

    public InfoCampoSuplementarioVO recuperarCampoSupDes(String codCampo, int tipoCampo, String codOrg,String codProc, int ejercicio, String numExp, AdaptadorSQLBD adaptador) throws BDException, SQLException {
        Connection con = null;
        InfoCampoSuplementarioVO csDesp = new InfoCampoSuplementarioVO();
        InfoDesplegableVO desp;
        String desCod = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = null;
        int idioma=1;
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
                    desCod = MeLanbide66DAO.getInstance().getDesCodCampoSuplementario(codCampo, Integer.parseInt(codOrg), codProc, con);
                }
                desp = recuperarCampoDesplegable(codOrg,idioma,desCod);
                csDesp.setDesplegable(desp);
            } else {
                throw new SQLException(salida.getDescStatus());
            }
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            throw e;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar información de un datos suplementarios tipo desplegable para el expediente  " + numExp, ex);
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
                    String val = getDescripcionDesplegableByIdioma(idioma, valor.getDescripcion())  ;
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
        return MeLanbide66DAO.getInstance().getDatosExpediente(codOrg, numExpediente, con);
    }

    public DatosEconomicosExpVO getImporteSubvencionYPorc(Integer edad, String sexo, String pro, String eje, Connection con) throws Exception {
        return MeLanbide66DAO.getInstance().getImporteSubvencionYPorc(edad, sexo, pro, eje, con);
    }

    public void actualizaImporteSubvencion(int codOrg, String numExpediente, Double importeSubvencion, Connection con) throws Exception {
        actualizaSuplNumericoDecimal(codOrg, numExpediente, importeSubvencion, ConstantesMeLanbide66.getPropVal_CS_SUBV(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaImportePrimerPago(int codOrg, String numExpediente, Double importePrimerPago, Connection con) throws Exception {
        actualizaSuplNumericoDecimal(codOrg, numExpediente, importePrimerPago, ConstantesMeLanbide66.getPropVal_CS_PRIMERPAGO(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaImporteSubvencionCByS(int codOrg, String numExpediente, Double importeSubvencion, Connection con) throws Exception {
        actualizaSuplNumericoDecimal(codOrg, numExpediente, importeSubvencion, ConstantesMeLanbide66.getPropVal_CS_SUBVCB(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaImportePrimerPagoCByS(int codOrg, String numExpediente, Double importePrimerPago, Connection con) throws Exception {
        actualizaSuplNumericoDecimal(codOrg, numExpediente, importePrimerPago, ConstantesMeLanbide66.getPropVal_CS_PRIMERPAGOCB(String.valueOf(codOrg), numExpediente.split("/")[1]), con);
    }

    public void actualizaEdad(int codOrg, String numExpediente, Integer edad, Connection con) throws Exception {
        actualizaSuplNumericoEntero(codOrg, numExpediente, edad, "EDAD", con);
    }

    private void actualizaSuplNumericoDecimal(int codOrg, String numExpediente, Double valor, String nombreCampo, Connection con) throws Exception {
        MeLanbide66DAO.getInstance().deleteSuplNumerico(codOrg, numExpediente, nombreCampo, con);
        if (valor != null) {
            MeLanbide66DAO.getInstance().insertSuplNumericoDecimal(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }

    private void actualizaSuplNumericoEntero(int codOrg, String numExpediente, Integer valor, String nombreCampo, Connection con) throws Exception {
        MeLanbide66DAO.getInstance().deleteSuplNumerico(codOrg, numExpediente, nombreCampo, con);
        if (valor != null) {
            MeLanbide66DAO.getInstance().insertSuplNumericoEntero(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }

    private void actualizaSuplDespl(int codOrg, String numExpediente, String valor, String nombreCampo, Connection con) throws Exception {
        MeLanbide66DAO.getInstance().deleteSuplDespl(codOrg, numExpediente, nombreCampo, con);
        if (valor != null) {
            MeLanbide66DAO.getInstance().insertSuplDespl(codOrg, numExpediente, valor, nombreCampo, con);
        }
    }

    public void actualizaSexo(int codOrg, String numExpediente, String sexo, Connection con) throws Exception {
        actualizaSuplDespl(codOrg, numExpediente, sexo, "SEXO", con);
    }

    public String getFechaPublicaionBOE_TablaPantillas(Integer ejericio, Connection con) throws Exception {
        return MeLanbide66DAO.getInstance().getFechaPublicaionBOE_TablaPantillas(ejericio, con);
    }

    public TerceroVO getDatosTerceroXNroExpediente(String numeExpediente, Connection con) throws Exception {
        return MeLanbide66DAO.getInstance().getDatosTerceroXNroExpediente(numeExpediente, con);
    }

    public Date[] getFechasNotificacion(int codOrg, String codProc, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        Date[] fechas = new Date[3];

        try {
            con = adapt.getConnection();
            MeLanbide66DAO meLanbide66DAO = MeLanbide66DAO.getInstance();
            fechas[0] = meLanbide66DAO.getFechaFirmaResolucion(codOrg, codProc, numExp, con);
            fechas[1] = meLanbide66DAO.getFechaEnvioNotificacion(codOrg, codProc, numExp, con);
            fechas[2] = meLanbide66DAO.getFechaAcuse(codOrg, codProc, numExp, con);
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

    public Date getFechaFirmaResolucion(int codOrg, String codProc, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide66DAO meLanbide66DAO = MeLanbide66DAO.getInstance();
            return meLanbide66DAO.getFechaFirmaResolucion(codOrg, codProc, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la Fecha Firma Resolución ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<SocioVO> getListaSocios(String numExpediente, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        log.debug("getListaSocios Manager ===>");
        List<SocioVO> listaSocios = new ArrayList<SocioVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide66DAO meLanbide66DAO = MeLanbide66DAO.getInstance();
            listaSocios = meLanbide66DAO.getListaSocios(numExpediente, codOrganizacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las contrataciones ", e);
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
            MeLanbide66DAO meLanbide66DAO = MeLanbide66DAO.getInstance();
            return meLanbide66DAO.getSocioPorID(id, con);
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
            MeLanbide66DAO meLanbide66DAO = MeLanbide66DAO.getInstance();
            return meLanbide66DAO.crearSocio(nuevoSocio, con);
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
            MeLanbide66DAO meLanbide66DAO = MeLanbide66DAO.getInstance();
            return meLanbide66DAO.modificarSocio(socioModif, con);
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
            MeLanbide66DAO meLanbide66DAO = MeLanbide66DAO.getInstance();
            return meLanbide66DAO.eliminarSocio(id, con);
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
