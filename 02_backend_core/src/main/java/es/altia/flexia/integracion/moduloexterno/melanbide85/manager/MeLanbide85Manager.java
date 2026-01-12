package es.altia.flexia.integracion.moduloexterno.melanbide85.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide85.vo.FilaContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide85.dao.MeLanbide85DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConstantesMeLanbide85;
import es.altia.flexia.integracion.moduloexterno.melanbide85.vo.DesplegableAdmonLocalVO;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide85Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide85Manager.class);

    //Instancia
    private static MeLanbide85Manager instance = null;

    public static MeLanbide85Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide85Manager.class) {
                instance = new MeLanbide85Manager();
            }
        }
        return instance;
    }

    public List<FilaContratacionVO> getListaContrataciones(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaContratacionVO> lista = new ArrayList<FilaContratacionVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();
            lista = meLanbide85DAO.getListaContrataciones(numExp, codOrganizacion, con);

            List<DesplegableAdmonLocalVO> listaMesContrato = MeLanbide85Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide85.COD_DES_MEPE, ConstantesMeLanbide85.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaGrupoCotizacion = MeLanbide85Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide85.COD_DES_GCON, ConstantesMeLanbide85.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide85Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide85.COD_DES_SEXO, ConstantesMeLanbide85.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaBool = MeLanbide85Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide85.COD_DES_BOOL, ConstantesMeLanbide85.FICHERO_PROPIEDADES), adapt);

            for (FilaContratacionVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaMesContrato) {
                    if (valordesp.getDes_val_cod().equals(cont.getDurcontrato1())) {
                        cont.setDesdurcontrato1(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(cont.getDurcontrato2())) {
                        cont.setDesdurcontrato2(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(cont.getDurcontrato3())) {
                        cont.setDesdurcontrato3(valordesp.getDes_nom());
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaGrupoCotizacion) {
                    if (valordesp.getDes_val_cod().equals(cont.getGrupocotiz1())) {
                        cont.setDesgrupocotiz1(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(cont.getGrupocotiz2())) {
                        cont.setDesgrupocotiz2(valordesp.getDes_nom());
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaSexo) {
                    if (valordesp.getDes_val_cod().equals(cont.getSexo2())) {
                        cont.setDessexo2(valordesp.getDes_nom());
                    }
                    break;
                }
                for (DesplegableAdmonLocalVO valordesp : listaBool) {
                    if (valordesp.getDes_val_cod().equals(cont.getEmpverde1())) {
                        cont.setDesempverde1(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(cont.getEmpverde2())) {
                        cont.setDesempverde2(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(cont.getEmpdigit1())) {
                        cont.setDesempdigit1(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(cont.getEmpdigit2())) {
                        cont.setDesempdigit2(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(cont.getEmpgen1())) {
                        cont.setDesempgen1(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(cont.getEmpgen2())) {
                        cont.setDesempgen2(valordesp.getDes_nom());
                    }
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las contrataciones ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las contrataciones ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaContratacionVO getContratacionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();
            return meLanbide85DAO.getContratacionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una contratación:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una contratación:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearContratacion(FilaContratacionVO nuevaContratacion, int tablas, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();
            insertOK = meLanbide85DAO.crearContratacion(nuevaContratacion, tablas, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una contratación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una contratación : " + ex.getMessage(), ex);
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

    public boolean modificarContratacion(FilaContratacionVO contratModif, int tablas, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();
            return meLanbide85DAO.modificarContratacion(contratModif, tablas, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una contratación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una contratación : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarContratacion(String id, String numExp,
            String docCV, String docDemanda, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        int result = 0, indice = -1;
        String nameFechaCV, nameFechaDemanda;
        try {
            con = adapt.getConnection();
            MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();
            adapt.inicioTransaccion(con);
            result = meLanbide85DAO.eliminarContratacion(id, numExp, con);
            if (result > 0) {
                if (docCV != null && !docCV.isEmpty() && !docCV.equals("undefined")) {
                    nameFechaCV = "FECHA" + docCV.substring(3, docCV.length());
                    indice = Integer.valueOf(docCV.substring(6, docCV.length() - 5));
                    meLanbide85DAO.deleteFechaDatosIntermediacion(numExp, nameFechaCV, con);
                    meLanbide85DAO.deleteFicheroCVoDemandaIntermediacion(numExp, docCV, con);
                }
                if (docDemanda != null && !docDemanda.isEmpty() && !docDemanda.equals("undefined")) {
                    nameFechaDemanda = "FECHA" + docDemanda.substring(3, docDemanda.length());
                    indice = Integer.valueOf(docDemanda.substring(7, docDemanda.length() - 5));
                    meLanbide85DAO.deleteFechaDatosIntermediacion(numExp, nameFechaDemanda, con);
                    meLanbide85DAO.deleteFicheroCVoDemandaIntermediacion(numExp, docDemanda, con);
                }
                final List<FilaContratacionVO> contrataciones = getListaContrataciones(numExp, codOrganizacion, adapt);
                
                if (contrataciones != null && indice >= 0) {
                    int i = indice;
                    for (final FilaContratacionVO contratacion : contrataciones) {
                        if (meLanbide85DAO.existeCodigoFichero(numExp, "DOC" + "CV" + "2" + (i + 1) + "INTER", con)) {
                            meLanbide85DAO.actualizaCodigoFecha(numExp, "FECHA" + "CV" + "2" + (i + 1) + "INTER", "FECHA" + "CV" + "2" + i + "INTER", con);
                            meLanbide85DAO.actualizaCodigoFichero(numExp, "DOC" + "CV" + "2" + (i + 1) + "INTER", "DOC" + "CV" + "2" + i + "INTER", con);
                        }
                        if (meLanbide85DAO.existeCodigoFichero(numExp, "DOC" + "DEM" + "2" + (i + 1) + "INTER", con)) {
                            meLanbide85DAO.actualizaCodigoFecha(numExp, "FECHA" + "DEM" + "2" + (i + 1) + "INTER", "FECHA" + "DEM" + "2" + i + "INTER", con);
                            meLanbide85DAO.actualizaCodigoFichero(numExp, "DOC" + "DEM" + "2" + (i + 1) + "INTER", "DOC" + "DEM" + "2" + i + "INTER", con);
                        }
                        i++;
                    }
                }
            }
            adapt.finTransaccion(con);
        } catch (BDException e) {
            adapt.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una contratación:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            adapt.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una contratación:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return result;
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();
            return meLanbide85DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public void setDatosCVoDemandaIntermediacion(String numExpediente,
            final Date fechaDocInter, final String nameFieldFecha, final File doc, final String nameFieldDoc,
            AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosCVoDemandaIntermediacion");
        Connection con = null;
        MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide85DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide85DAO.getEJEDatosIntermediacion(numExpediente, con);

            meLanbide85DAO.setFechaDatosIntermediacion(numExpediente, fechaDocInter, nameFieldFecha, txt_mun, txt_eje, con);
            meLanbide85DAO.setFicheroCVoDemandaIntermediacion(numExpediente, doc, nameFieldDoc, txt_mun, txt_eje, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en setDatosCVoDemandaIntermediacion() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN setDatosCVoDemandaIntermediacion");
    }

    public CamposFormulario getValoresFechas(final String numExpediente, final AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getValoresFecha");
        Connection con = null;
        CamposFormulario camposFormulario = null;
        MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();
        DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide85DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide85DAO.getEJEDatosIntermediacion(numExpediente, con);

            final GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", txt_mun.toString());
            gVO.setAtributo("ejercicio", txt_eje.toString());
            gVO.setAtributo("numero", numExpediente);
            camposFormulario = datosSuplementariosDAO.getValoresFecha(adaptador, con, gVO, null);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getValoresFecha() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getValoresFecha");
        return camposFormulario;
    }

    public CamposFormulario getFicheros(final String numExpediente,
            final AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getFicheros");
        Connection con = null;
        CamposFormulario camposFormulario = null;
        MeLanbide85DAO meLanbide85DAO = MeLanbide85DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide85DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide85DAO.getEJEDatosIntermediacion(numExpediente, con);

            final GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", txt_mun.toString());
            gVO.setAtributo("ejercicio", txt_eje.toString());
            gVO.setAtributo("numero", numExpediente);
            camposFormulario = meLanbide85DAO.getValoresFichero(gVO, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getFicheros() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getFicheros");
        return camposFormulario;
    }
}
