package es.altia.flexia.integracion.moduloexterno.melanbide82.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide82.dao.MeLanbide82DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConstantesMeLanbide82;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.FilaContratacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.PersonaContratadaVO;
import java.io.File;
import java.sql.Date;

/**
 *
 * @author kepa
 */
public class MeLanbide82Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide82Manager.class);

    //Instancia
    private static MeLanbide82Manager instance = null;

    /**
     *
     * @return
     */
    public static MeLanbide82Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide82Manager.class) {
                instance = new MeLanbide82Manager();
            }
        }
        return instance;
    }

    /**
     *
     * @param numExp
     * @param codOrganizacion
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<FilaContratacionVO> getListaContrataciones(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        log.debug("getListaContrataciones Manager ===>");
        List<FilaContratacionVO> lista = new ArrayList<FilaContratacionVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
            lista = meLanbide82DAO.getListaContrataciones(numExp, codOrganizacion, con);

            List<DesplegableAdmonLocalVO> listaGrupoCotizacion = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_GCON, ConstantesMeLanbide82.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_SEXO, ConstantesMeLanbide82.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaNivel = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_NIVEL, ConstantesMeLanbide82.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaDuracion = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_DURA, ConstantesMeLanbide82.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaSistemaGrantiaJuve = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_SISTGRANTIAJUVE, ConstantesMeLanbide82.FICHERO_PROPIEDADES), adapt);

            for (FilaContratacionVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaGrupoCotizacion) {
                    if (valordesp.getDes_val_cod().equals(cont.getGrupoCotiz())) {
                        cont.setDescGrupoCotiz(valordesp.getDes_nom());
                    }
                    if (cont.getGrupocotizInicio() != null && valordesp.getDes_val_cod().equals(cont.getGrupocotizInicio())) {
                        cont.setDescGrupocotizInicio(valordesp.getDes_nom());
                    }
                    if (cont.getGrupocotizFin() != null && valordesp.getDes_val_cod().equals(cont.getGrupocotizFin())) {
                        cont.setDescGrupocotizFin(valordesp.getDes_nom());
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaSexo) {
                    if (cont.getSexoInicio() != null && valordesp.getDes_val_cod().equals(cont.getSexoInicio())) {
                        cont.setDescSexoInicio(valordesp.getDes_nom());
                    }
                    if (cont.getSexoFin() != null && valordesp.getDes_val_cod().equals(cont.getSexoFin())) {
                        cont.setDescSexoFin(valordesp.getDes_nom());
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaNivel) {
                    if (valordesp.getDes_val_cod().equals(cont.getNivelCualificacion())) {
                        cont.setDescNivelcualificacion(valordesp.getDes_nom());
                    }
                    if (cont.getNivelcualificacionInicio() != null && valordesp.getDes_val_cod().equals(cont.getNivelcualificacionInicio())) {
                        cont.setDescNivelcualificacionInicio(valordesp.getDes_nom());
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaDuracion) {
                    if (cont.getDurContrato() != null && valordesp.getDes_val_cod().equals(cont.getDurContrato().toString())) {
                        cont.setDescDurContrato(valordesp.getDes_nom());
                    }
                    if (cont.getDurcontratoInicio() != null && cont.getDurcontratoInicio() != null && valordesp.getDes_val_cod().equals(cont.getDurcontratoInicio().toString())) {
                        cont.setDescDurContratoInicio(valordesp.getDes_nom());
                    }
                    if (cont.getDurcontratoFin() != null && cont.getDurcontratoFin() != null && valordesp.getDes_val_cod().equals(cont.getDurcontratoFin().toString())) {
                        cont.setDescDurContratoFin(valordesp.getDes_nom());
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaSistemaGrantiaJuve) {
                    log.debug("getListaContrataciones Manager ===>" + valordesp.getDes_cod() + ", " + valordesp.getDes_nom());
                    if (cont.getSistGrantiaJuveIni() != null && !cont.getSistGrantiaJuveIni().equals("") && valordesp.getDes_val_cod().equals(cont.getSistGrantiaJuveIni().toString())) {
                        cont.setDescSistGrantiaJuveIni(valordesp.getDes_nom());
                    }
                    if (cont.getSistGrantiaJuveFin() != null && !cont.getSistGrantiaJuveFin().equals("") && valordesp.getDes_val_cod().equals(cont.getSistGrantiaJuveFin().toString())) {
                        cont.setDescSistGrantiaJuveFin(valordesp.getDes_nom());
                    }                    
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las contrataciones ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción recuperando datos sobre las contrataciones ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public FilaContratacionVO getContratacionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
            return meLanbide82DAO.getContratacionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una contratación:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando datos sobre una contratación:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param nuevaContratacion
     * @param tablas
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean crearContratacion(FilaContratacionVO nuevaContratacion, int tablas, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
            return meLanbide82DAO.crearContratacion(nuevaContratacion, tablas, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una contratación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción creando una contratación : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param contrataModif
     * @param tablas
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean modificarContratacion(FilaContratacionVO contrataModif, int tablas, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
            return meLanbide82DAO.modificarContratacion(contrataModif, tablas, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una contratación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción actualizando una contratación : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param id
     * @param numExp
     * @param adapt
     * @return
     * @throws Exception
     */
    public int eliminarContratacion(String id, String numExp,
            String docCV, String docDemanda, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        int result = 0, indice = -1;
        String nameFechaCV, nameFechaDemanda;
        try {
            con = adapt.getConnection();
            MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
            adapt.inicioTransaccion(con);
            result = meLanbide82DAO.eliminarContratacion(id, numExp, con);
            if (result > 0) {
                if (docCV != null && !docCV.isEmpty() && !docCV.equals("undefined")) {
                    nameFechaCV = "FECHA" + docCV.substring(3, docCV.length());
                    indice = Integer.valueOf(docCV.substring(6, docCV.length() - 5));
                    meLanbide82DAO.deleteFechaDatosIntermediacion(numExp, nameFechaCV, con);
                    meLanbide82DAO.deleteFicheroCVoDemandaIntermediacion(numExp, docCV, con);
                }
                if (docDemanda != null && !docDemanda.isEmpty() && !docDemanda.equals("undefined")) {
                    nameFechaDemanda = "FECHA" + docDemanda.substring(3, docDemanda.length());
                    indice = Integer.valueOf(docDemanda.substring(7, docDemanda.length() - 5));
                    meLanbide82DAO.deleteFechaDatosIntermediacion(numExp, nameFechaDemanda, con);
                    meLanbide82DAO.deleteFicheroCVoDemandaIntermediacion(numExp, docDemanda, con);
                }
                final List<FilaContratacionVO> contrataciones = getListaContrataciones(numExp, codOrganizacion, adapt);

                if (contrataciones != null && indice >= 0) {
                    int i = indice;
                    for (final FilaContratacionVO contratacion : contrataciones) {
                        if (meLanbide82DAO.existeCodigoFichero(numExp, "DOC" + "CV" + "2" + (i + 1) + "INTER", con)) {
                            meLanbide82DAO.actualizaCodigoFecha(numExp, "FECHA" + "CV" + "2" + (i + 1) + "INTER", "FECHA" + "CV" + "2" + i + "INTER", con);
                            meLanbide82DAO.actualizaCodigoFichero(numExp, "DOC" + "CV" + "2" + (i + 1) + "INTER", "DOC" + "CV" + "2" + i + "INTER", con);
                        }
                        if (meLanbide82DAO.existeCodigoFichero(numExp, "DOC" + "DEM" + "2" + (i + 1) + "INTER", con)) {
                            meLanbide82DAO.actualizaCodigoFecha(numExp, "FECHA" + "DEM" + "2" + (i + 1) + "INTER", "FECHA" + "DEM" + "2" + i + "INTER", con);
                            meLanbide82DAO.actualizaCodigoFichero(numExp, "DOC" + "DEM" + "2" + (i + 1) + "INTER", "DOC" + "DEM" + "2" + i + "INTER", con);
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

    /**
     *
     * @param des_cod
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
            return meLanbide82DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param prioridad
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean existePrioridad(String numExp, int prioridad, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
            return meLanbide82DAO.existePrioridad(numExp, prioridad, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD comprobando si existe Prioridad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción comprobando si existe Prioridad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public void setDatosCVoDemandaIntermediacion(String numExpediente,
            final Date fechaDocInter, final String nameFieldFecha, final File doc, final String nameFieldDoc,
            AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosCVoDemandaIntermediacion");
        Connection con = null;
        MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide82DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide82DAO.getEJEDatosIntermediacion(numExpediente, con);

            meLanbide82DAO.setFechaDatosIntermediacion(numExpediente, fechaDocInter, nameFieldFecha, txt_mun, txt_eje, con);
            meLanbide82DAO.setFicheroCVoDemandaIntermediacion(numExpediente, doc, nameFieldDoc, txt_mun, txt_eje, con);

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
        MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
        DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide82DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide82DAO.getEJEDatosIntermediacion(numExpediente, con);

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

    public CamposFormulario getSistGrantiaJuve(final String numExpediente, final AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getSistGrantiaJuve");
        Connection con = null;
        CamposFormulario camposFormulario = null;
        MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();
        DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide82DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide82DAO.getEJEDatosIntermediacion(numExpediente, con);

            final GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", txt_mun.toString());
            gVO.setAtributo("ejercicio", txt_eje.toString());
            gVO.setAtributo("numero", numExpediente);

            camposFormulario = datosSuplementariosDAO.getValoresDesplegable(adaptador, con, gVO);
            log.debug("@@@@@ getSistGrantiaJuve camposFormulario=" + camposFormulario);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getSistGrantiaJuve() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getSistGrantiaJuve");
        return camposFormulario;
    }

    public CamposFormulario getFicheros(final String numExpediente,
            final AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getFicheros");
        Connection con = null;
        CamposFormulario camposFormulario = null;
        MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide82DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide82DAO.getEJEDatosIntermediacion(numExpediente, con);

            final GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", txt_mun.toString());
            gVO.setAtributo("ejercicio", txt_eje.toString());
            gVO.setAtributo("numero", numExpediente);
            camposFormulario = meLanbide82DAO.getValoresFichero(gVO, con);

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
    
    public PersonaContratadaVO getDatosPersonaContratadaExterno(String nifPersonaContratada, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosPersonaContratadaExterno");
        PersonaContratadaVO personaContratada = new PersonaContratadaVO();

        Connection con = null;
        MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();

        try {
            con = adaptador.getConnection();
            personaContratada = meLanbide82DAO.getPersonaContratadaExterno(nifPersonaContratada, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getDatosPersonaContratadaExterno() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.info("FIN getDatosPersonaContratadaExterno");
        return personaContratada;
    }

    public void setDatosPersonaContratadaExterno(PersonaContratadaVO personaContratada,
            final String tabla, final Integer id, final String nombreCampo,
            AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosPersonaContratadaExterno");
        Connection con = null;
        MeLanbide82DAO meLanbide82DAO = MeLanbide82DAO.getInstance();

        try {
            con = adaptador.getConnection();
            meLanbide82DAO.setEstadoSistemaGJ(personaContratada, tabla, id, nombreCampo, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en setPersonaContratada() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN setDatosPersonaContratadaExterno");
    }    
}
