/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide67.dao.MeLanbide67DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.MeLanbide67Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.CentroColVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DatosPestanaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.EntidadColaboradoraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.LeaukPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.FilaPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.FilaCentroColVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.LakModRecalculoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.PersonaContratadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItemPuestos;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SubSolicVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.agora.business.sge.ValorCampoSuplementarioVO;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide67.dao.PersonaContratadaPuestoTrabajoDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DetallesCampoSuplementarioVO;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DesplegableExternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.PersonaContratadaPuestoTrabajoVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author santiagoc
 */
public class MeLanbide67Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide67Manager.class);

    // Variables para campos suplementarios
    private static final List<String> LISTA_CAMPOS_SUP_PEST = Arrays.asList(ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_DNI_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_NOM_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_APE1_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_APE2_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FECNAC_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_EDAD_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_SEXO_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_MUN_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_EXP6M_PER_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_DESEMP_ANT, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_SIST_GARANT_JUVE, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_DOC_CV_INTER, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_DOC_DEMANDA_INTER, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FEC_DEMANDA_INTER, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FEC_CV_INTER, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_DESEMPLEADO, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FEC_INI_CONTR, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_MUJ_SUBREPRE, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_NUM_CTA_COTSS, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_DIR_CENT_TRAB, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_GRUP_COTSS, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_JOR_PUESTA, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_MOD_CONTRATO, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_TIT_PUESTRA, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_MOD_CODOCU, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_MON_PUESTO_TRA, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FEC_REAL_FIN_CONTR, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FEC_FIN_CONTR, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_SALARIO_MIN, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_TIT_ESP, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_SAL_BRUT_CONT, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_PORC_JORN, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_DUR_CONTRA, ConstantesMeLanbide67.FICHERO_PROPIEDADES));

    private static final Map<String, DesplegableExternoVO> DESPLEGABLES_EXTERNOS_VO = new HashMap<String, DesplegableExternoVO>();

    private static final String PROCEDIMIENTO = "LAK";
    //Instancia
    private static MeLanbide67Manager instance = null;

    private MeLanbide67Manager() {
        DESPLEGABLES_EXTERNOS_VO.put(
                ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_MUNIPERCON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
                new DesplegableExternoVO(
                        ConfigurationParameter.getParameter(ConstantesMeLanbide67.VISTA_MUNIPERSON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
                        ConfigurationParameter.getParameter(ConstantesMeLanbide67.CODIGO_MUNIPERCON, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
                        ConfigurationParameter.getParameter(ConstantesMeLanbide67.DESCRIPCION_MUNIPERCON, ConstantesMeLanbide67.FICHERO_PROPIEDADES))
        );
    }

    public static MeLanbide67Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide67Manager.class) {
                instance = new MeLanbide67Manager();
            }
        }
        return instance;
    }

    /* public int guardarDatosCpe(int codOrganizacion, String ejercicio, String numExpediente, String gestor, String empresa, BigDecimal impPagado, BigDecimal impPagado2, BigDecimal otrasAyudasSolic, BigDecimal otrasAyudasConce, BigDecimal minimisSolic, BigDecimal minimisConce, BigDecimal importeConcedido,BigDecimal importeRenunciaRes, BigDecimal importeReintegrar, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        String campoActual = "";
        BigDecimal cero = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            int res = 0;
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(gestor != null && !gestor.equals(""))
            {
                res += meLanbide67DAO.guardarValorCampoDesplegable(codOrganizacion, String.valueOf(ejercicio), numExpediente, campoActual, gestor, con);
                if(res <= 0)
                {
                    throw new Exception();
                }
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(empresa != null && !empresa.equals(""))
            {
                res += meLanbide67DAO.guardarValorCampoTexto(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, empresa, con);
                if(res <= 0)
                {
                    throw new Exception();
                }
            }

            //En funcion del tramite en que se encuentre el expediente, habra que guardar en un campo o en otro

            //Miro a ver si tiene iniciado el tramite de "Resolucion de concesion o denegacion"
            //Long codTramResModif = MeLanbide67Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConstantesMeLanbide67.CODIGO_TRAMITE_RESOLUCION_MODIF, adaptador);

            if(impPagado == null)
            {
                impPagado = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                impPagado = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, impPagado, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_IMP_PAG_2, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(impPagado2 == null)
            {
                impPagado2 = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                impPagado2 = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, impPagado2, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_OTRAS_AYUDAS_CONCE, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(otrasAyudasConce == null)
            {
                otrasAyudasConce = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                otrasAyudasConce = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, otrasAyudasConce, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(otrasAyudasSolic == null)
            {
                otrasAyudasSolic = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                otrasAyudasSolic = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, otrasAyudasSolic, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_MINIMIS_SOLIC, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(minimisSolic == null)
            {
                minimisSolic = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                minimisSolic = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, minimisSolic, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_MINIMIS_CONCE, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(minimisConce == null)
            {
                minimisConce = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                minimisConce = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, minimisConce, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_IMP_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(importeConcedido == null)
            {
                importeConcedido = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                importeConcedido = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, importeConcedido, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_IMP_REN_RES, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(importeRenunciaRes == null)
            {
                importeRenunciaRes = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                importeRenunciaRes = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, importeRenunciaRes, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_IMP_REI, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            if(importeReintegrar == null)
            {
                importeReintegrar = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                importeReintegrar = new BigDecimal(ConstantesMeLanbide67.CERO_CON_DECIMALES);
            }
            res += meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, importeReintegrar, con);
            if(res <= 0)
            {
                throw new Exception();
            }
            adaptador.finTransaccion(con);
            return res;
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido un error grabando el campo suplementario desplegable "+campoActual+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido un error grabando el campo suplementario desplegable "+campoActual+" para el expediente "+numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
     */
    public LeaukPuestoVO getPuestoPorCodigoYExpediente(LeaukPuestoVO p, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getPuestoPorCodigoYExpediente(p, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando puesto " + (p != null ? p.getCodPuesto() : "(puesto = null)") + " para expediente " + (p != null ? p.getNumExp() : "(puesto = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando puesto " + (p != null ? p.getCodPuesto() : "(puesto = null)") + " para expediente " + (p != null ? p.getNumExp() : "(puesto = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public CentroColVO getCentroColPorCodigoYExpediente(CentroColVO cc, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getCentroColPorCodigoYExpediente(cc, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando centro colaborador " + (cc != null ? cc.getIdCentroCol() : "(puesto = null)") + " para expediente " + (cc != null ? cc.getNumExp() : "(puesto = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando centro colaborador " + (cc != null ? cc.getIdCentroCol() : "(puesto = null)") + " para expediente " + (cc != null ? cc.getNumExp() : "(puesto = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public LeaukPuestoVO guardarLakPuestoVO(int codOrganizacion, LeaukPuestoVO puesto, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            //boolean nuevo = puesto != null && puesto.getCodPuesto() != null ? false : true;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            puesto = meLanbide67DAO.guardarLakPuestoVO(puesto, con);
            if (puesto != null) {
                adaptador.finTransaccion(con);
                return puesto;
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando puesto " + (puesto != null ? puesto.getCodPuesto() : "(puesto = null)") + " para el expediente " + (puesto != null ? puesto.getNumExp() : "(puesto = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando puesto " + (puesto != null ? puesto.getCodPuesto() : "(puesto = null)") + " para el expediente " + (puesto != null ? puesto.getNumExp() : "(puesto = null)"), ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    }

    public int getImpSubv(int ejercicio, String ofertaEmpleo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getImporteSubv(ejercicio, ofertaEmpleo, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando importe subvenci?n", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando el importe subvenci?n", ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    }

    public CentroColVO guardarCentroColVO(int codOrganizacion, CentroColVO centroCol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            centroCol = meLanbide67DAO.guardarCentroColaboradorVO(centroCol, con);
            if (centroCol != null) {

                adaptador.finTransaccion(con);
                return centroCol;
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando centro colaborador " + (centroCol != null ? centroCol.getIdCentroCol() : "(centroCol = null)") + " para el expediente " + (centroCol != null ? centroCol.getNumExp() : "(centroCol = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando centro colaborador " + (centroCol != null ? centroCol.getIdCentroCol() : "(centroCol = null)") + " para el expediente " + (centroCol != null ? centroCol.getNumExp() : "(centroCol = null)"), ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    }

    public List<FilaPuestoVO> getListaPuestosPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getListaPuestosPorExpediente(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando puestos para expediente " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando puestos para expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaCentroColVO> getListaCentrosColPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getListaCentrosColPorExpediente(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando centros colaboradores para expediente " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando puestos para expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarLakPuestoVO(int codOrganizacion, LeaukPuestoVO puesto, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            if (puesto != null) {
                //meLanbide67DAO.eliminarJustificacionesPorPuestoYExpediente(puesto, con);
                //meLanbide67DAO.eliminarOfertasPorPuestoYExpediente(puesto, con);
                int eliminados = meLanbide67DAO.eliminarLakPuesto(puesto, con);
                if (eliminados > 0) {
                    //guardarCalculosCpe(codOrganizacion, puesto, null, true, false, adaptador, con);
                    //guardarCalculosPuestos(codOrganizacion, puesto.getEjercicio(), puesto.getNumExp(), adaptador, con);
                    adaptador.finTransaccion(con);
                    return eliminados;
                } else {
                    throw new BDException();
                }
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando puesto " + (puesto != null ? puesto.getCodPuesto() : "(puesto = null)") + " para el expediente " + (puesto != null ? puesto.getNumExp() : "(puesto = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando puesto " + (puesto != null ? puesto.getCodPuesto() : "(puesto = null)") + " para el expediente " + (puesto != null ? puesto.getNumExp() : "(puesto = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /*public List<String> getNumerosExpedientes(Integer ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getNumerosExpedientes(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista numeros exepediente", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista numeros exepediente", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    }
    * */
    public List<SelectItemPuestos> getListaPuestos(AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getListaPuestos(con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando puestos para expediente ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando puestos para expediente ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String recalculoSubvLeaukCen(String numExpediente, Integer idLinea, String anio, AdaptadorSQLBD adaptador) throws SQLException {
        Connection con = null;
        String res = "0";
        try {
            con = adaptador.getConnection();
            res = MeLanbide67DAO.getInstance().recalculoSubvLeaukCen(numExpediente, idLinea, anio, con);
        } catch (Exception ex) {
            log.error("Error en recalculoSubvLeaukCen: " + ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return res;
    }

    public String recalculoSubvLeaukEmp(String numExpediente, Integer idLinea, String anio, AdaptadorSQLBD adaptador) throws SQLException {
        Connection con = null;
        String res = "0";
        try {
            con = adaptador.getConnection();
            res = MeLanbide67DAO.getInstance().recalculoSubvLeaukEmp(numExpediente, idLinea, anio, con);
        } catch (Exception ex) {
            log.error("Error en recalculoSubvLeaukEmp: " + ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return res;
    }

    public void crearRegisCentrosCol(int codOrganizacion, Integer ejercicio, String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);

            BigDecimal bd = null;
            Integer i = null;

            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

            //Se guardan los dos registros en bbdd
            meLanbide67DAO.crearRegistrosCentrosCol(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FEC_RES, ConstantesMeLanbide67.FICHERO_PROPIEDADES), new Date(), con);

            adaptador.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD guardando importes resolucion I para el expediente " + numExpediente, e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepción en la BBDD guardando importes resolucion I para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem> getTiposDocumento(AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getTiposDocumento(con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista tipos de documento", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista tipos de documento", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String calculoSubvenLAK(String anio, String idPuesto, String titulacion, String modalidadContrato, String sexo, double jornada, AdaptadorSQLBD adaptador) throws SQLException {
        log.info("------- INICIO calculoSubvLak - manager");
        Connection con = null;
        String res = "0";
        try {

            con = adaptador.getConnection();
            log.info("calculoSubvenLAK - llama al DAO");
            res = MeLanbide67DAO.getInstance().calculoSubvenLAK(anio, idPuesto, titulacion, modalidadContrato, sexo, jornada, con);
        } catch (Exception ex) {
            log.error("Error en calculoSubvLak: " + ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        log.info("------- FIN calculoSubvLak - manager. Respuesta: " + res);
        return res;
    }// calculoSubvenLAK

    public String recalculoModsubvlakemp(LakModRecalculoVO datosCalculo, AdaptadorSQLBD adaptador) throws SQLException {
        log.info("------- INICIO RecalculoModsubvlakemp - manager");
        Connection con = null;
        String res = "0";

        log.debug("=========  MANAGER");
        log.info("AÑO " + datosCalculo.getAnioSol());
        try {
            con = adaptador.getConnection();
            res = MeLanbide67DAO.getInstance().recalculoModsubvlakemp(datosCalculo, con);
        } catch (Exception ex) {
            log.error("Error en RecalculoModsubvlakemp: " + ex.getMessage());
            res = ex.getMessage();
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("error al cerrar la conexion ", e);
            }
        }
        log.info("------- FIN RecalculoModsubvlakemp - manager. Respuesta: " + res);
        return res;
    }//RecalculoModsubvlakempPROV

    public boolean guardarDatosTramite260(DatosPestanaVO datosGrabar, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        log.info("=========== ENTRO en guardarDatosTramite260");
        boolean grabaOK = false;
        Connection con = null;
        String numExp = datosGrabar.getNumExp();
        String[] partes = numExp.split("/");
        String ejer = (partes[0]);
        String codProcedimiento = partes[1];
        String codCampo = "";
        String valor = "";
        int ocurrencia = 1;
        int tramite = 22;
        int result = 0;
        String tram = ConfigurationParameter.getParameter(ConstantesMeLanbide67.TRAMITE_260, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
        try {
            tramite = Integer.parseInt(tram);
            log.info("Tramite a insertar suplementarios " + tramite);
        } catch (Exception e) {
            log.error("No parsea a int");
        }
        try {
            con = adapt.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            ocurrencia = meLanbide67DAO.getOcurrenciaTramiteAbiertoxCodigoInterno(numExp, Integer.parseInt(tram), con);

            //causa
            codCampo = "CAUSA";

            valor = datosGrabar.getCodCausa();
            if (!valor.equals("") && !valor.equals("null")) {
                result = meLanbide67DAO.guardarValorCampoDesplegableTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            }
            //motivo
            codCampo = "MOTIMODRESOL";
            valor = datosGrabar.getCodMotivo();
            if (!valor.equals("") && !valor.equals("null")) {
                result = meLanbide67DAO.guardarValorCampoDesplegableTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            } else {
                result = meLanbide67DAO.eliminarValorCampoDesplegableTramite(codOrganizacion, ejer, numExp, tramite, codCampo, con);
            }
            //fecha fin
            codCampo = "FECFINMODRESOL";
            if (datosGrabar.getFechaFinReal() != null && !datosGrabar.getFechaFinReal().equals("")) {
                valor = datosGrabar.getFechaFinReal();
                if (!valor.equals("") && !valor.equals("null")) {
                    result = meLanbide67DAO.guardarValorCampoFechaTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
                } else {
                    result = meLanbide67DAO.eliminarValorCampoFechaTramite(codOrganizacion, ejer, numExp, tramite, codCampo, con);
                }
            }

            //importe inicial
            codCampo = "IMPRESOLINI";
            if (datosGrabar.getImporteTotalIni() != null) {
                valor = datosGrabar.getImporteTotalIni();
                if (!valor.equals("") && !valor.equals("null")) {
                    result = meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
                }
            }
            // importe modificado
            codCampo = "IMPREMODSOL";
            valor = datosGrabar.getTotalNuevo();
            if (!valor.equals("") && !valor.equals("null")) {
                result = meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            }
            //pagado
            codCampo = "IMPPAGPAGO1";
            valor = datosGrabar.getPago1();
            if (!valor.equals("") && !valor.equals("null")) {
                result = meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            }
            //pendiente
            codCampo = "IMPPTEPAGO2";
            valor = datosGrabar.getPago2();
            if (!valor.equals("") && !valor.equals("null")) {
                result = meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            }
            // a devolver
            codCampo = "IMPDEVOL";
            valor = datosGrabar.getDevolverTotal();
            if (!valor.equals("") && !valor.equals("null")) {
                result = meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            }

            // a devolver
            codCampo = "IMPTOT";
            //BigDecimal valorDecimal  = new BigDecimal(datosGrabar.getImporteTotalActualizado());
            valor = datosGrabar.getImporteTotalActualizado();
            if (!valor.equals("") && !valor.equals("null")) {
                result = meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            }
            codCampo = "IMPPAGO1";
            //valorDecimal  = new BigDecimal(datosGrabar.getImportePrimerPagoActualizado());
            valor = datosGrabar.getImportePrimerPagoActualizado();
            if (!valor.equals("") && !valor.equals("null")) {
                //result = meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion, ejer, numExp, codCampo, valorDecimal, con);
                result = meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            }
            codCampo = "IMPPAGO2";
            //valorDecimal  = new BigDecimal(datosGrabar.getImporteSegundoPagoActualizado());
            valor = datosGrabar.getImporteSegundoPagoActualizado();
            if (!valor.equals("") && !valor.equals("null")) {
                //result = meLanbide67DAO.guardarValorCampoNumerico(codOrganizacion, ejer, numExp, codCampo, valorDecimal, con);
                result = meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejer, numExp, tramite, ocurrencia, codCampo, valor, con);
            }

            grabaOK = true;
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD grabando suplementarios : " + e.getMessage(), e);

        } catch (Exception ex) {

            log.error("Se ha producido una Excepcion  grabando suplementarios  : " + ex.getMessage(), ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return grabaOK;
    }// guardarDatosTramite260

    public DatosPestanaVO cargarDatosLak(int codOrganizacion, String numExp, AdaptadorSQLBD adapt) throws Exception {
        DatosPestanaVO datos = null;
        log.info("ENTRA EN cargarDatosLak");
        Connection con = null;
        String[] partes = numExp.split("/");
        String ejer = (partes[0]);
        String codProcedimiento = (partes[1]);

        log.debug("Ejercicio: " + ejer);
        log.debug("codProcedimiento: " + codProcedimiento);
        String codCausa;
        String desCausa = null;

        String codMotivo;
        String desMotivo = null;

        String fechaInicio;
        String duracion;
        Date fechaFinEsperada = null;
        String fechaFinReal;

        // fila IMPORTE CONCEDIDO
        String importeTotalIni = null;
        String pago1 = null; // del expediente
        String pago2 = null;

        // fila PAGADO
        String pagado1 = null; // tramite 310
        String pagado2 = null; // tramite 320
        String totalPagado = null;

        // fila NUEVO IMPORTE
        String pagoNuevo1 = "0.00";
        String pagoNuevo2 = "0.00";
        String totalNuevo = "0.00";

        // fila A DEVOLVER
        String devolver1;
        String devolver2;
        String devolverTotal;

        BigDecimal importeSubvInicialConcTramResolEstTec;

        con = adapt.getConnection();
        int tramiteResolucionEstTec = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TRAMITE_200, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
        int tramitePago1 = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TRAMITE_310, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
        int tramitePago2 = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TRAMITE_320, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
        int tramiteModificacionResolucion = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TRAMITE_260, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
        int OcurrenciatramiteModificacionResolucion = MeLanbide67DAO.getInstance().getOcurrenciaTramiteAbiertoxCodigoInterno(numExp, tramiteModificacionResolucion, con);
        log.info("VARIABLES DEFINIDAS");

        datos = new DatosPestanaVO();
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        try {
            log.info("Cargo las variables con los suplementarios");
            codCausa = meLanbide67DAO.getValorCampoDesplegableTramite(codOrganizacion, codProcedimiento, numExp, ejer, tramiteModificacionResolucion, OcurrenciatramiteModificacionResolucion, "CAUSA", con);
            if (codCausa != null) {
                desCausa = meLanbide67DAO.getValorDesplegable(con, "CAUS", codCausa);
                datos.setCodCausa(codCausa);
                if (desCausa != null) {
                    datos.setDescCausa(desCausa);
                }
            }

            codMotivo = meLanbide67DAO.getValorCampoDesplegableTramite(codOrganizacion, codProcedimiento, numExp, ejer, tramiteModificacionResolucion, OcurrenciatramiteModificacionResolucion, "MOTIMODRESOL", con);
            if (codMotivo != null) {
                datos.setCodMotivo(codMotivo);
                desMotivo = meLanbide67DAO.getValorDesplegable(con, "MOLA", codMotivo);
                if (desMotivo != null) {
                    datos.setDescMotivo(desMotivo);
                }
            }

            duracion = meLanbide67DAO.getValorCampoDesplegable(codOrganizacion, numExp, ejer, "DURACONTRA", con);
            if (duracion != null) {
                datos.setDuracion(duracion);
            } else {
                log.error("No se ha cargado Duracion");
            }
            log.info("Cargo las variables con los suplementarios - fechas");
            fechaInicio = meLanbide67DAO.getValorCampoFechaStr(codOrganizacion, numExp, ejer, "FECINICONTR", con);
            if (fechaInicio != null) {

                String fechaCortada = fechaInicio.substring(0, 10);
                log.info("Fecha inicio contrato: " + fechaCortada);
                String[] componente = fechaCortada.split("-");
                String dia = componente[2];
                String mes = componente[1];
                String anio = componente[0];
                String fechaVolteada = dia + "/" + mes + "/" + anio;
                datos.setFechaInicio(fechaVolteada);
                // aqui calcular fecha

                try {
                    DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaInicioDATE = formatoFecha.parse(fechaVolteada);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaInicioDATE);
                    if (duracion.equals("1")) {
                        log.info("Duracion = 1");
                        //fechaFinEsperada = sumaRestaDias(fechaVolteada, 180);
                        cal.add(Calendar.MONTH, 6);

                    } else {
                        log.info("Duracion <>1");
                        //fechaFinEsperada = sumaRestaDias(fechaVolteada, 364);
                        cal.add(Calendar.MONTH, 12);
                    }
                    fechaFinEsperada = cal.getTime();
                    //Date nuevaF = new Date();
                    cal.setTime(fechaFinEsperada);
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    fechaFinEsperada = cal.getTime();

                } catch (ParseException e) {
                    log.error("Fallo al sumar dias");
                }
                log.info("Fecha Fin ESPERADA: " + fechaFinEsperada);
            } else {
                log.error("No se ha cargado Fecha Inicio");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                String finStr = sdf.format(fechaFinEsperada);
                log.info("ESPERADA: " + finStr);
                datos.setFechaFinEsperada(finStr);
            } catch (Exception e) {
                log.error("No existe fecha esperada " + e);
            }

            fechaFinReal = meLanbide67DAO.getValorCampoFechaStr(codOrganizacion, numExp, ejer, "FECFINMODRESOL", con);
            if (fechaFinReal != null) {
                String fechaCortada = fechaFinReal.substring(0, 10);
                String[] componente = fechaCortada.split("-");
                String dia = componente[2];
                String mes = componente[1];
                String anio = componente[0];
                String fechaVolteada = dia + "/" + mes + "/" + anio;
                datos.setFechaFinReal(fechaVolteada);
            } else {
                datos.setFechaFinReal("");
            }
            log.info("Cargo las variables con los suplementarios - importes");

            // CONCEDIDO
            BigDecimal importeTot = meLanbide67DAO.getValorCampoNumerico(codOrganizacion, numExp, ejer, "IMPTOT", con);
            if (importeTot != null) {
                try {
                    importeTotalIni = importeTot.toPlainString();
                    if (!importeTotalIni.contains(".")) {
                        //   importeTotalIni = importeTotalIni + ".00";
                    }
                } catch (Exception e) {
                    log.error("Fallo al convertir BigDecimal: " + e);
                }
            } else {
                log.error("No se ha cargado Importe Inicial");
                importeTotalIni = "0.00";
            }
            if (importeTotalIni != null) {
                datos.setImporteTotalIni(importeTotalIni);
            }

            BigDecimal impPago1 = meLanbide67DAO.getValorCampoNumerico(codOrganizacion, numExp, ejer, "IMPPAGO1", con);
            if (impPago1 != null) {
                try {
                    pago1 = impPago1.toPlainString();
                    if (!pago1.contains(".")) {
                        //      pago1 = pago1 + ".00";
                    }
                } catch (Exception e) {
                    log.error("Fallo al convertir BigDecimal: " + e);
                }
            } else {
                log.error("No se ha cargado Importe Pago 1");
                pago1 = "0.00";
            }
            if (pago1 != null) {
                datos.setPago1(pago1);
            }

            BigDecimal impPago2 = meLanbide67DAO.getValorCampoNumerico(codOrganizacion, numExp, ejer, "IMPPAGO2", con);
            if (impPago2 != null) {
                try {
                    pago2 = impPago2.toPlainString();
                    if (!pago2.contains(".")) {
                        //      pago2 = pago2 + ".00";
                    }
                } catch (Exception e) {
                    log.error("Fallo al convertir BigDecimal: " + e);
                }
            } else {
                log.error("No se ha cargado Importe Pago 2º");
            }
            if (pago2 != null) {
                datos.setPago2(pago2);
            }
            // PAGADO
            // Cogemos la maxima ocurrencia, no debe haber mas de 1, en este fujo principal , pero por si acaso cogeremos la mas alta
            int ocurrencia = meLanbide67DAO.getMaxOcurrenciaTramitexCodigoInterno(numExp, tramitePago1, con);
            BigDecimal importeTramitePago1 = meLanbide67DAO.getValorCampoNumericoTramite(codOrganizacion, numExp, ejer, tramitePago1, ocurrencia, "IMPPAGO1", con);
//            BigDecimal importeTramitePago1 = meLanbide67DAO.getValorCampoNumerico(codOrganizacion, numExp, ejer, "IMPPAGPAGO1", con);
            if (importeTramitePago1 != null) {
                try {
                    pagado1 = importeTramitePago1.toPlainString();
                    if (!pagado1.contains(".")) {
                        pagado1 = pagado1 + ".00";
                    }
                } catch (Exception e) {
                    log.error("Fallo al convertir BigDecimal: " + e);
                }
            } else {
                log.error("No se ha cargado Importe Pago 1 del tramite");

            }
            if (pagado1 != null) {
                datos.setPagado1(pagado1);
            } else {
                datos.setPagado1("0.00");
            }

            ocurrencia = meLanbide67DAO.getMaxOcurrenciaTramitexCodigoInterno(numExp, tramitePago2, con);
            BigDecimal importeTramitePago2 = meLanbide67DAO.getValorCampoNumericoTramite(codOrganizacion, numExp, ejer, tramitePago2, ocurrencia, "IMPPAGO2", con);
//            BigDecimal importeTramitePago2 = meLanbide67DAO.getValorCampoNumerico(codOrganizacion, numExp, ejer, "IMPPTEPAGO2", con);
            if (importeTramitePago2 != null) {
                try {
                    pagado2 = importeTramitePago2.toPlainString();
                    if (!pagado2.contains(".")) {
                        pagado2 = pagado2 + ".00";
                    }
                } catch (Exception e) {
                    log.error("Fallo al convertir BigDecimal: " + e);
                }
            } else {
                log.error("No se ha cargado Importe Pago 2 del tramite");
                pagado2 = "0";
            }
            if (pagado2 != null) {
                datos.setPagado2(pagado2);
            } else {
                datos.setPagado2("0.00");
            }

            ocurrencia = meLanbide67DAO.getMaxOcurrenciaTramitexCodigoInterno(numExp, tramiteResolucionEstTec, con);
            importeSubvInicialConcTramResolEstTec = meLanbide67DAO.getValorCampoNumericoTramite(codOrganizacion, numExp, ejer, tramiteResolucionEstTec, ocurrencia, "IMPTOT", con);
            if (importeSubvInicialConcTramResolEstTec != null) {
                datos.setImpSuvencionIniTramResolEstTec(importeSubvInicialConcTramResolEstTec);
                log.error("Importe Subvencion Inicial del tramite Resolucion Estudio Tecnico 200 :  " + importeSubvInicialConcTramResolEstTec);
            } else {
                log.error("No se ha cargado Importe Subvencion Inicial del tramite Resolucion Estudio Tecnico 200 ");
                datos.setImpSuvencionIniTramResolEstTec(BigDecimal.ZERO);
            }

            BigDecimal totalPagadoTramites = null;
            if (importeTramitePago1 != null && importeTramitePago2 != null) {
                totalPagadoTramites = importeTramitePago1.add(importeTramitePago2);
            } else {
                totalPagadoTramites = new BigDecimal(0.00);
            }
            try {
                totalPagado = totalPagadoTramites.toPlainString();
                if (!totalPagado.contains(".")) {
                    totalPagado = totalPagado + ".00";
                }
            } catch (Exception e) {
                log.error("Fallo al convertir BigDecimal: " + e);
            }
            if (totalPagado != null) {
                datos.setTotalPagado(totalPagado);
            } else {
                datos.setTotalPagado("0.00");
            }
        } catch (Exception e) {
            log.error("Fallo al llenar el objeto: " + e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return datos;
    }

    public boolean grabarDatosCalculoLAK(String total, String pago1, String pago2, int codOrganizacion, String numExp, AdaptadorSQLBD adapt) throws BDException, SQLException, Exception {
        log.info("---------------  ENTRA EN grabarDatosCalculo");
        boolean grabaOK = false;
        Connection con = null;
        String ejercicio = numExp.substring(0, 4);
        int ejerInt = Integer.parseInt(ejercicio);
        log.debug("EJER int: " + ejerInt);

        String codCampo = null;
        String valor = null;

        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        // graba los importes en los suplementarios del Expediente
        try {
            con = adapt.getConnection();
            // IMPTOTAL
            codCampo = "IMPTOT";
            if (total != null && total != "") {
                if (total.contains(",")) {
                    total = total.replace(".", "");
                    total = total.replace(",", ".");
                    log.debug("Sustituyo TOTAL");
                }
                meLanbide67DAO.guardarValorCalculoLAK(codOrganizacion, ejercicio, numExp, codCampo, total, con);
            }
            // IMPPAGO1
            codCampo = "IMPPAGO1";
            if (pago1 != null && pago1 != "") {
                if (pago1.contains(",")) {
                    pago1 = pago1.replace(".", "");
                    pago1 = pago1.replace(",", ".");
                    log.debug("Sustituyo pago1");
                }
                meLanbide67DAO.guardarValorCalculoLAK(codOrganizacion, ejercicio, numExp, codCampo, pago1, con);
            }
            //IMPPAGO2
            codCampo = "IMPPAGO2";
            if (pago2 != null && pago2 != null) {
                if (pago2.contains(",")) {
                    pago2 = pago2.replace(".", "");
                    pago2 = pago2.replace(",", ".");
                    log.debug("Sustituyo pago2");
                }
                meLanbide67DAO.guardarValorCalculoLAK(codOrganizacion, ejercicio, numExp, codCampo, pago2, con);
            }
            grabaOK = true;
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexion a la BBDD", e);
            throw e;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar informacion de ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar la conexion a la BBDD: " + e.getMessage());
            }
        }//try-catch

        /*

        Long tramiteLong = this.getTramiteActualExpediente(codOrganizacion, ejerInt, numExp, adapt);
        log.debug("Long tramite= " + tramiteLong);

        int tramiteActual = tramiteLong.intValue();
        log.debug("Tramite int - " + tramiteActual);
        int tramite = 3;
        String tram = null;
        if (tramiteActual == 1) {// si es Inicio de expediente se graban en los suplementarios del tramite Resolucion-Estudio Tecnico
            tram = ConfigurationParameter.getParameter(ConstantesMeLanbide67.TRAMITE_200, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
            try {
                tramite = Integer.parseInt(tram);
                log.info("Tramite a insertar suplementarios " + tramite);
            } catch (Exception e) {
                log.error("No parsea a int");
            }
            try {
                con = adapt.getConnection();
                // IMPTOTAL
                codCampo = "IMPTOT";
                if (total != null && total != "") {
                    meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, "LAK", ejercicio, numExp, tramite, 1, codCampo, total, con);
                }
                // IMPPAGO1
                codCampo = "IMPPAGO1";
                if (pago1 != null && pago1 != "") {
                    meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, "LAK", ejercicio, numExp, tramite, 1, codCampo, pago1, con);
                }
                //IMPPAGO2
                codCampo = "IMPPAGO2";
                if (pago2 != null && pago2 != null) {
                    meLanbide67DAO.guardarValorCampoNumericoTramite(codOrganizacion, "LAK", ejercicio, numExp, tramite, 1, codCampo, pago2, con);
                }
            } catch (BDException e) {
                log.error("Se ha producido un error al obtener la conexion a la BBDD", e);
                throw e;
            } catch (SQLException ex) {
                log.error("Se ha producido un error al recuperar informacion de ", ex);
                throw ex;
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (Exception e) {
                    log.error("Error al cerrar la conexion a la BBDD: " + e.getMessage());
                }
            }//try-catch

        } else {
// para otros tramites en los que se haga el calculo y haya que grabar los datos en un tramite
        }*/
        //
        return grabaOK;
    }

    public Long getTramiteActualExpediente(int codOrganizacion, int ejercicio, String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getTramiteActualExpediente(codOrganizacion, ejercicio, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando tramite actual del expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando tramite actual del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }// getTramiteActual

    public List<SelectItem> getListaDesplegable(AdaptadorSQLBD adaptador, String idLista) throws Exception {
        log.info("Entro en listaDesplegable MANAGER");
        Connection con = null;
        List<SelectItem> lista = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        try {
            con = adaptador.getConnection();
            lista = meLanbide67DAO.getListaDesplegable(con, idLista);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando lista resultado Final", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener lista resultado Final." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }

    public String getSuplementarioDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        String valor = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        try {
            con = adaptador.getConnection();
            valor = meLanbide67DAO.getValorCampoDesplegable(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo desplegable", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("Error al obtener Campo Desplegable." + e.getMessage());
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return valor;
    }

    public String getSuplementarioDesplegableExterno(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        String valor = null;
        PersonaContratadaPuestoTrabajoDAO personaContratadaPuestoTrabajoDAO = PersonaContratadaPuestoTrabajoDAO.getInstance();
        try {
            con = adaptador.getConnection();
            valor = personaContratadaPuestoTrabajoDAO.getValorCampoDesplegableExterno(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo desplegable", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("Error al obtener Campo Desplegable." + e.getMessage());
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return valor;
    }

    public CamposFormulario getFicheros(int codOrganizacion, final String numExp, final String ejercicio, final String codigoCampo,
                                        final AdaptadorSQLBD adaptador
    ) throws Exception {
        log.info("Inicio getFicheros");
        Connection con = null;
        CamposFormulario camposFormulario = null;
        PersonaContratadaPuestoTrabajoDAO personaContratadaPuestoTrabajoDAO = PersonaContratadaPuestoTrabajoDAO.getInstance();

        try {
            con = adaptador.getConnection();
            camposFormulario = personaContratadaPuestoTrabajoDAO.getValoresFichero(codOrganizacion, numExp, ejercicio, codigoCampo, con);
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

    public String getSuplementarioNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        String valor = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        BigDecimal numeroB = null;
        try {
            con = adaptador.getConnection();
            numeroB = meLanbide67DAO.getValorCampoNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo Numerico", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener Campo Numerico." + e.getMessage());
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (numeroB != null) {
            try {
                valor = numeroB.toPlainString();
            } catch (Exception e) {
                log.error("Fallo al convertir BigDecimal: " + e);
            }
        }
        return valor;
    }

    public String getSuplementarioFecha(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        String valor = null;
        Date fecha = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();
            fecha = meLanbide67DAO.getValorFechaDate(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo Numerico", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener Campo Numerico." + e.getMessage());
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (fecha != null) {
            valor = fecha.toString();
        }
        return valor;
    }

    private String getSuplementarioFichero(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        CamposFormulario valor = null;
        String resultado = null;

        PersonaContratadaPuestoTrabajoDAO personaContratadaPuestoTrabajoDAO = PersonaContratadaPuestoTrabajoDAO.getInstance();
        try {
            con = adaptador.getConnection();
            valor = personaContratadaPuestoTrabajoDAO.getValoresFichero(codOrganizacion, numExp, ejercicio, codigoCampo, con);
            resultado = (String) valor.get(codigoCampo + "_NOMBRE");
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo Numerico", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener Campo Numerico." + e.getMessage());
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return resultado;
    }

    public BigDecimal getSuplemNumericoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception {
        BigDecimal valor = null;
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        try {
            con = adaptador.getConnection();
            valor = meLanbide67DAO.getValorCampoNumericoTramite(codOrganizacion, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo Numerico", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener Campo Numerico." + e.getMessage());
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return valor;
    }

    public Date sumaMeses(String fechaString, int meses) throws ParseException {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date fechaDate = sdf.parse(fechaString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaDate);
        cal.add(Calendar.MONTH, meses);
        Date fechaSumada = cal.getTime();
        return fechaSumada;
    }

    public static Date sumaRestaDias(String fechaString, Integer days) throws ParseException {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date fechaDate = sdf.parse(fechaString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaDate);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public boolean validaImportesPago(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, Connection con) throws Exception {
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        BigDecimal impTotal = null;
        BigDecimal impPago1 = null;
        BigDecimal impPago2 = null;
        int ejercicio = 0;
        try {
            ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
        String ejerStr = String.valueOf(ejercicio);
        impTotal = meLanbide67DAO.getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejerStr, codTramite, ocurrenciaTramite, "IMPTOT", con);
        impPago1 = meLanbide67DAO.getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejerStr, codTramite, ocurrenciaTramite, "IMPPAGO1", con);
        impPago2 = meLanbide67DAO.getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejerStr, codTramite, ocurrenciaTramite, "IMPPAGO2", con);
        log.debug("Suma de importes: " + impPago1 + " + " + impPago2 + " = " + impPago1.add(impPago2));
        log.debug("Valor del suplementario" + impTotal);
        return impTotal.compareTo(impPago1.add(impPago2)) == 0;
    }//validaImportesPago

    public Date getValorFechaDate(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) {
        Date valor = null;
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        try {
            con = adaptador.getConnection();
            valor = meLanbide67DAO.getValorFechaDate(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (Exception ex) {
            log.error("Error en getValorFechaDate() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return valor;
    }

    public BigDecimal getCosteContratoLAK(String ejercicio, String titulacion, AdaptadorSQLBD adaptador) throws Exception {
        log.info("getCosteContratoLAK - ini()");
        BigDecimal coste = BigDecimal.ZERO;
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        try {
            con = adaptador.getConnection();
            coste = meLanbide67DAO.getCosteContratoLAK(ejercicio, titulacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo Numerico", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getCosteContratoLAK() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("getCosteContratoLAK - fin()");
        return coste;
    }

    public String getDatosSuplementariosNumeroOferta(String numeroExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosSuplementariosNumeroOferta");
        String numeroOferta = "";

        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();
            numeroOferta = meLanbide67DAO.getNumeroOferta(numeroExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getNumeroOferta() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getDatosSuplementariosNumeroOferta");

        return numeroOferta;
    }

    public String getDatosSuplementariosNIFPersonaContratada(String numeroExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosSuplementariosNIFPersonaContratada");
        String nifPersonaContratada = "";

        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();
            nifPersonaContratada = meLanbide67DAO.getNIFPersonaContratada(numeroExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getDatosSuplementariosNIFPersonaContratada() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getDatosSuplementariosNIFPersonaContratada");

        return nifPersonaContratada;
    }

    public String getDatosSuplementariosNIFEntidadColaboradora(String numeroExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosSuplementariosNIFPersonaResponsableEntidadColaboradora");
        String nifEmpresario = "";

        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();
            nifEmpresario = meLanbide67DAO.getNIFEntidadColaboradora(numeroExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getDatosSuplementariosNIFPersonaResponsableEntidadColaboradora() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getDatosSuplementariosNIFPersonaResponsableEntidadColaboradora");

        return nifEmpresario;
    }

    public PersonaContratadaVO getDatosPersonaContratada(String nifPersonaContratada, String numeroOferta, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosPersonaContratada");
        PersonaContratadaVO personaContratada = new PersonaContratadaVO();

        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();
            personaContratada = meLanbide67DAO.getPersonaContratada(nifPersonaContratada, numeroOferta, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getPersonaContratada() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.info("FIN getDatosPersonaContratada");
        return personaContratada;
    }

    public void setDatosPersonaContratada(String numExpediente, PersonaContratadaVO personaContratada, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosPersonaContratada");
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide67DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide67DAO.getEJEDatosIntermediacion(numExpediente, con);

            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, personaContratada.getNombre(), "NOMPERCON", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, personaContratada.getApellido1(), "APE1PERCON", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, personaContratada.getApellido2(), "APE2PERCON", txt_mun, txt_eje, con);
            meLanbide67DAO.setFechaDatosIntermediacion(numExpediente, personaContratada.getFechaNacimiento(), "FECNACPERCON", txt_mun, txt_eje, con);
            meLanbide67DAO.setNumericoDatosIntermediacion(numExpediente, personaContratada.getEdad(), "EDADPERCON", txt_mun, txt_eje, con);
            meLanbide67DAO.setComboDatosIntermediacion(numExpediente, personaContratada.getSexo(), "SEXOPERCON", "SEXO", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, personaContratada.getFormacionCV(), "FORMACV", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, personaContratada.getAnioObtencionTitulacion(), "ANOOBTITU", txt_mun, txt_eje, con);
            meLanbide67DAO.setComboDatosIntermediacion(numExpediente, personaContratada.getAltaSistemaGJ(), "SISTGRANTIAJUVE", "CURE", txt_mun, txt_eje, con);
            meLanbide67DAO.setComboDatosIntermediacion(numExpediente, personaContratada.getParticipadoEdicionesAnterioresLAK(), "PARTEDICANTLAK", "CURE", txt_mun, txt_eje, con);
            meLanbide67DAO.setComboDatosIntermediacion(numExpediente, personaContratada.getDesempleadoDiaContratacion(), "DESEMPLEADO", "CURE", txt_mun, txt_eje, con);
            meLanbide67DAO.setComboDatosIntermediacion(numExpediente, personaContratada.getContratoIncentivadoLAK(), "INCENTIVADO", "CURE", txt_mun, txt_eje, con);
            meLanbide67DAO.setFechaDatosIntermediacion(numExpediente, personaContratada.getFechaGestionDemanda(), "FECHADEMANDA", txt_mun, txt_eje, con);
            meLanbide67DAO.setFechaDatosIntermediacion(numExpediente, personaContratada.getFechaRegistro(), "FECHATIPO2", txt_mun, txt_eje, con);
            meLanbide67DAO.setComboDatosIntermediacion(numExpediente, personaContratada.getOfertaLimitadaLAK2020(), "LINKADO", "CURE", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, personaContratada.getCentroCaptacionRegistro(), "CENTRO", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, personaContratada.getCentroGestion(), "CENTROGESTION", txt_mun, txt_eje, con);
            meLanbide67DAO.setComboMunicipioIntermediacion(numExpediente, personaContratada.getMunicipioDeResidencia(), "MUNIPERCON", txt_mun, txt_eje, con);

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

        log.info("FIN setDatosPersonaContratada");
    }

    public void setDatosCVIntermediacion(String numExpediente, final Date fechaCVInter, final File cv, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosCVIntermediacion");
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide67DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide67DAO.getEJEDatosIntermediacion(numExpediente, con);

            meLanbide67DAO.setFechaDatosIntermediacion(numExpediente, fechaCVInter, "FECHACVINTER", txt_mun, txt_eje, con);
            meLanbide67DAO.setFicheroCVIntermediacion(numExpediente, cv, "DOCCVINTER", txt_mun, txt_eje, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en setDatosCVIntermediacion() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN setDatosCVIntermediacion");
    }

    public void setDatosCVIntermediacionProvisional(String numExpediente, final Date fechaCVInter, String url, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosCVIntermediacionProvisional");
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide67DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide67DAO.getEJEDatosIntermediacion(numExpediente, con);

            meLanbide67DAO.setFechaDatosIntermediacion(numExpediente, fechaCVInter, "FECHACVINTER", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, url, "URLCV", txt_mun, txt_eje, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en setDatosCVIntermediacionProvisional() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN setDatosCVIntermediacionProvisional");
    }

    public void setDatosDemandaIntermediacion(String numExpediente, final Date fechaDemandaInter, final File demanda,
                                              AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosDemandaIntermediacion");
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide67DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide67DAO.getEJEDatosIntermediacion(numExpediente, con);

            meLanbide67DAO.setFechaDatosIntermediacion(numExpediente, fechaDemandaInter, "FECHADEMINTER", txt_mun, txt_eje, con);
            meLanbide67DAO.setFicheroCVIntermediacion(numExpediente, demanda, "DOCDEMINTER", txt_mun, txt_eje, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en setDatosDemandaIntermediacion() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN setDatosDemandaIntermediacion");
    }
    public void setDatosDemandaIntermediacionProvisional(String numExpediente, final Date fechaDemandaInter, String url, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosDemandaIntermediacionProvisional");
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide67DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide67DAO.getEJEDatosIntermediacion(numExpediente, con);

            meLanbide67DAO.setFechaDatosIntermediacion(numExpediente, fechaDemandaInter, "FECHADEMINTER", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, url, "URLDEMANDA", txt_mun, txt_eje, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en setDatosDemandaIntermediacionProvisional() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN setDatosDemandaIntermediacionProvisional");
    }

    public DetallesCampoSuplementarioVO getDetallesCampo(String procedimiento, String codigoCampo,
                                                         AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        String valor = null;
        DetallesCampoSuplementarioVO resultado = null;
        PersonaContratadaPuestoTrabajoDAO personaContratadaPuestoTrabajoDAO = PersonaContratadaPuestoTrabajoDAO.getInstance();
        try {

            con = adapt.getConnection();
            resultado = personaContratadaPuestoTrabajoDAO.getDetallesCampo(procedimiento, codigoCampo, con);
        } catch (BDException e) {
            log.error("getDetallesCampo: Se ha producido una excepcion recuperando el campo " + codigoCampo, e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("getDetallesCampo: Error al obtener el campo " + codigoCampo + ", " + e.getLocalizedMessage());
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("getDetallesCampo: Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return resultado;
    }

    public String getSuplementarioTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo,
                                        AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        String valor = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
        try {

            con = adapt.getConnection();
            valor = meLanbide67DAO.getValorCampoTexto(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo de texto ", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener Campo Texto " + e.getMessage());
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return valor;
    }

    public PersonaContratadaPuestoTrabajoVO getDatosPersonaContratada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                                                      final AdaptadorSQLBD adapt) throws Exception {

        final List<DetallesCampoSuplementarioVO> detallesCampoSuplementarioVOs = new ArrayList<DetallesCampoSuplementarioVO>();

        for (final String nombreCampo : LISTA_CAMPOS_SUP_PEST) {
            final DetallesCampoSuplementarioVO detallesCampoSuplementarioVO = getDetallesCampo(PROCEDIMIENTO,
                    nombreCampo, adapt);
            if (detallesCampoSuplementarioVO != null) {
                detallesCampoSuplementarioVO.setValor(getDatoSuplementario(codOrganizacion, numExpediente, numExpediente.split("/")[0],
                        nombreCampo, detallesCampoSuplementarioVO.getPcaTda(), adapt));
                if (detallesCampoSuplementarioVO.getPcaDesplegable() != null) {
                    if (detallesCampoSuplementarioVO.getPcaTda().equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_DESPLEGABLE, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                        detallesCampoSuplementarioVO.setDesplegable(getValoresDesplegable(detallesCampoSuplementarioVO.getPcaDesplegable(), adapt));
                    } else if (detallesCampoSuplementarioVO.getPcaTda().equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_DESPLEGABLE_EXTERNO, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                        final DesplegableExternoVO desplegableExternoVO = DESPLEGABLES_EXTERNOS_VO.get(nombreCampo);
                        if (desplegableExternoVO != null) {
                            detallesCampoSuplementarioVO.setDesplegable(getValoresDesplegableExterno(desplegableExternoVO.getVista(),
                                    desplegableExternoVO.getNombreCod(), desplegableExternoVO.getNombreDesc(), adapt));
                        }
                    }
                }
            }
            detallesCampoSuplementarioVOs.add(detallesCampoSuplementarioVO);
        }

        final PersonaContratadaPuestoTrabajoVO personaContratadaPuestoTrabajoVO
                = new PersonaContratadaPuestoTrabajoVO(detallesCampoSuplementarioVOs.get(0), detallesCampoSuplementarioVOs.get(1), detallesCampoSuplementarioVOs.get(2),
                detallesCampoSuplementarioVOs.get(3), detallesCampoSuplementarioVOs.get(4), detallesCampoSuplementarioVOs.get(5), detallesCampoSuplementarioVOs.get(6),
                detallesCampoSuplementarioVOs.get(7), detallesCampoSuplementarioVOs.get(8), detallesCampoSuplementarioVOs.get(9), detallesCampoSuplementarioVOs.get(10),
                detallesCampoSuplementarioVOs.get(11), detallesCampoSuplementarioVOs.get(12), detallesCampoSuplementarioVOs.get(13), detallesCampoSuplementarioVOs.get(14),
                detallesCampoSuplementarioVOs.get(15), detallesCampoSuplementarioVOs.get(16), detallesCampoSuplementarioVOs.get(17), detallesCampoSuplementarioVOs.get(18),
                detallesCampoSuplementarioVOs.get(19), detallesCampoSuplementarioVOs.get(20), detallesCampoSuplementarioVOs.get(21), detallesCampoSuplementarioVOs.get(22),
                detallesCampoSuplementarioVOs.get(23), detallesCampoSuplementarioVOs.get(24), detallesCampoSuplementarioVOs.get(25), detallesCampoSuplementarioVOs.get(26),
                detallesCampoSuplementarioVOs.get(27), detallesCampoSuplementarioVOs.get(28), detallesCampoSuplementarioVOs.get(29), detallesCampoSuplementarioVOs.get(30),
                detallesCampoSuplementarioVOs.get(31), detallesCampoSuplementarioVOs.get(32));

        return personaContratadaPuestoTrabajoVO;
    }

    private String getDatoSuplementario(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, String tipoCampo, AdaptadorSQLBD adaptador) {
        Connection con = null;
        String valor = null;
        MeLanbide67Manager instance = MeLanbide67Manager.getInstance();
        try {
            con = adaptador.getConnection();
            if (tipoCampo.equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_TEXTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                valor = instance.getSuplementarioTexto(codOrganizacion, numExp, ejercicio, codigoCampo, adaptador);
            } else if (tipoCampo.equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_NUMERICO, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                valor = instance.getSuplementarioNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, adaptador);
            } else if (tipoCampo.equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_FECHA, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                valor = instance.getSuplementarioFecha(codOrganizacion, numExp, ejercicio, codigoCampo, adaptador);
            } else if (tipoCampo.equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_FICHERO, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                valor = instance.getSuplementarioFichero(codOrganizacion, numExp, ejercicio, codigoCampo, adaptador);
            } else if (tipoCampo.equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_DESPLEGABLE, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                valor = instance.getSuplementarioDesplegable(codOrganizacion, numExp, ejercicio, codigoCampo, adaptador);
            } else if (tipoCampo.equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_DESPLEGABLE_EXTERNO, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                valor = instance.getSuplementarioDesplegableExterno(codOrganizacion, numExp, ejercicio, codigoCampo, adaptador);
            }
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo desplegable", e);
        } catch (Exception e) {
            log.error("Error al obtener Campo Desplegable." + e.getMessage());
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return valor;
    }

    private List<GeneralComboVO> getValoresDesplegable(String codigo,
                                                       final AdaptadorSQLBD adapt) throws Exception {
        log.info("Inicio getValoresDesplegable");

        Connection con = null;
        PersonaContratadaPuestoTrabajoDAO personaContratadaPuestoTrabajoDAO = PersonaContratadaPuestoTrabajoDAO.getInstance();
        List<GeneralComboVO> desplegable = null;

        try {
            con = adapt.getConnection();
            desplegable = personaContratadaPuestoTrabajoDAO.cargarDesplegable(con, codigo);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getDatosEntidadColaboradora() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getValoresDesplegable");
        return desplegable;
    }

    private List<GeneralComboVO> getValoresDesplegableExterno(String vista, String nombreCod,
                                                              String nombreDesc, final AdaptadorSQLBD adapt) throws Exception {
        log.info("Inicio getValoresDesplegable");

        Connection con = null;
        PersonaContratadaPuestoTrabajoDAO personaContratadaPuestoTrabajoDAO = PersonaContratadaPuestoTrabajoDAO.getInstance();
        List<GeneralComboVO> desplegable = null;

        try {
            con = adapt.getConnection();
            desplegable = personaContratadaPuestoTrabajoDAO.cargarDesplegableExterno(con, vista, nombreCod, nombreDesc);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getDatosEntidadColaboradora() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getValoresDesplegable");
        return desplegable;
    }

    public int guardarDatosPersonaContradata(int codOrganizacion, final DatosExpedienteVO datosExpediente,
                                             final String codMunicipio, final List<DetallesCampoSuplementarioVO> detallesCampoSuplementarioVOs,
                                             final AdaptadorSQLBD adapt) throws Exception {

        final MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        int[] result = new int[LISTA_CAMPOS_SUP_PEST.size()];
        int i = 0;
        try {
            for (final String nombreCampo : LISTA_CAMPOS_SUP_PEST) {
                final DetallesCampoSuplementarioVO detallesCampoSuplementarioVO = getDetallesCampo(PROCEDIMIENTO,
                        nombreCampo, adapt);
                if (detallesCampoSuplementarioVO != null) {
                    final EstructuraCampo eC = new EstructuraCampo();

                    eC.setCodCampo(nombreCampo);
                    eC.setCodTipoDato(detallesCampoSuplementarioVO.getPcaTda());

                    final GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", codMunicipio);
                    gVO.setAtributo("ejercicio", Integer.valueOf(datosExpediente.getEjercicio()).toString());
                    gVO.setAtributo("numero", datosExpediente.getNumExpediente());
                    gVO.setAtributo(eC.getCodCampo(), nombreCampo);

                    if (detallesCampoSuplementarioVO.getPcaTda().equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_TEXTO, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                        gVO.setAtributo(eC.getCodCampo(), detallesCampoSuplementarioVOs.get(i).getValor());
                        result[i] = setDatoTexto(adapt, eC, gVO);
                    } else if (detallesCampoSuplementarioVO.getPcaTda().equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_NUMERICO, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                        if (detallesCampoSuplementarioVOs.get(i).getValor() != null && !detallesCampoSuplementarioVOs.get(i).getValor().equals("")) {
                            gVO.setAtributo(eC.getCodCampo(), detallesCampoSuplementarioVOs.get(i).getValor());
                            result[i] = setDatoNumerico(adapt, eC, gVO);
                        } else {
                            gVO.setAtributo(eC.getCodCampo(), "");
                            result[i] = setDatoNumerico(adapt, eC, gVO);
                        }
                    } else if (detallesCampoSuplementarioVO.getPcaTda().equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_FECHA, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                        if (detallesCampoSuplementarioVOs.get(i).getValor() != null && !detallesCampoSuplementarioVOs.get(i).getValor().equals("")) {
                            final String tmpAux1 = detallesCampoSuplementarioVOs.get(i).getValor().replace('-', '/');
                            final String formatedDate = tmpAux1.substring(8, 10) + "/" + tmpAux1.substring(5, 7) + "/" + tmpAux1.substring(0, 4);
                            gVO.setAtributo(eC.getCodCampo(), formatedDate);
                            result[i] = setDatoFecha(adapt, eC, gVO);
                        } else {
                            gVO.setAtributo(eC.getCodCampo(), "");
                            result[i] = setDatoFecha(adapt, eC, gVO);
                        }
                    } else if (detallesCampoSuplementarioVO.getPcaTda().equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_DESPLEGABLE, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                        gVO.setAtributo(eC.getCodCampo(), detallesCampoSuplementarioVOs.get(i).getValor());
                        result[i] = setDatoDesplegable(adapt, eC, gVO);
                    } else if (detallesCampoSuplementarioVO.getPcaTda().equals(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_SUPL_DESPLEGABLE_EXTERNO, ConstantesMeLanbide67.FICHERO_PROPIEDADES))) {
                        try { // Chapuza con pedigrí por problemas con las versiones del core de flexia
                            meLanbide67DAO.setComboMunicipioIntermediacion(String.valueOf(gVO.getAtributo("numero")),
                                    detallesCampoSuplementarioVOs.get(i).getValor(), nombreCampo,
                                    new BigDecimal(String.valueOf(gVO.getAtributo("codMunicipio"))),
                                    new BigDecimal(String.valueOf(gVO.getAtributo("ejercicio"))),
                                    adapt.getConnection());
                            result[i] = 1;
                        } catch (final Exception ex) {
                            result[i] = 0;
                        }
                    } else { // Campos suplementarios que no se actualizan.
                        result[i] = 1;
                    }
                } else {
                    result[i] = 1;
                }
                i++;
            }

            for (i = 0; i < result.length; i++) {
                if (result[i] == 1) {
                    result[i] = 0;
                } else {
                    result[i] = 1;
                }
            }

            for (i = 0; i < result.length; i++) {
                if (result[i] == 1) {
                    return 1;
                }
            }
            return 0;
        } catch (BDException e) {
            log.error("Se ha producido un error grabando el campo suplementario valor " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario valor : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
        }
    }

    private int setDatoDesplegableExterno(AdaptadorSQLBD adaptador, EstructuraCampo eC, GeneralValueObject gVO) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

            return datosSuplementariosDAO.setDatoDesplegableExt(adaptador, con, eC, gVO);
        } catch (BDException e) {
            log.error("Se ha producido un error de base de datos para el expediente " + (String) gVO.getAtributo("numero"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n para el expediente " + (String) gVO.getAtributo("numero"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    private int setDatoDesplegable(AdaptadorSQLBD adaptador, EstructuraCampo eC, GeneralValueObject gVO) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

            return datosSuplementariosDAO.setDatoDesplegable(adaptador, con, eC, gVO);
        } catch (BDException e) {
            log.error("Se ha producido un error de base de datos para el expediente " + (String) gVO.getAtributo("numero"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n para el expediente " + (String) gVO.getAtributo("numero"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    private int setDatoNumerico(AdaptadorSQLBD adaptador, EstructuraCampo eC, GeneralValueObject gVO) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

            return datosSuplementariosDAO.setDatoNumerico(adaptador, con, eC, gVO);
        } catch (BDException e) {
            log.error("Se ha producido un error de base de datos para el expediente " + (String) gVO.getAtributo("numero"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n para el expediente " + (String) gVO.getAtributo("numero"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    private int setDatoTexto(AdaptadorSQLBD adaptador, EstructuraCampo eC, GeneralValueObject gVO) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

            return datosSuplementariosDAO.setDatoTexto(adaptador, con, eC, gVO);
        } catch (BDException e) {
            log.error("Se ha producido un error de base de datos para el expediente " + (String) gVO.getAtributo("numero"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n para el expediente " + (String) gVO.getAtributo("numero"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    private int setDatoFecha(AdaptadorSQLBD adaptador, EstructuraCampo eC, GeneralValueObject gVO) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

            return datosSuplementariosDAO.setDatoFecha(adaptador, con, eC, gVO);
        } catch (BDException e) {
            log.error("Se ha producido un error de base de datos para el expediente " + (String) gVO.getAtributo("numero"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n para el expediente " + (String) gVO.getAtributo("numero"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EntidadColaboradoraVO getDatosEntidadColaboradora(String numeroOferta, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosEntidadColaboradora");
        EntidadColaboradoraVO entidadColaboradora = new EntidadColaboradoraVO();

        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();
            entidadColaboradora = meLanbide67DAO.getEntidadColaboradora(numeroOferta, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getDatosEntidadColaboradora() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getDatosEntidadColaboradora");
        return entidadColaboradora;
    }

    public void setDatosEntidadColaboradora(String numExpediente, EntidadColaboradoraVO entidadColaboradora, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosEntidadColaboradora");
        Connection con = null;
        MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide67DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide67DAO.getEJEDatosIntermediacion(numExpediente, con);

            Integer codMunicipio = new Integer(entidadColaboradora.getMunicipio().substring(2));
            Integer codProvincia = new Integer(entidadColaboradora.getProvincia());
            String txt_municipio = meLanbide67DAO.getMunicipio(codProvincia, codMunicipio, con);

            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, entidadColaboradora.getNombre(), "NOMCOLAB", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, entidadColaboradora.getNif(), "NIFCOLAB", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, entidadColaboradora.getDireccion(), "DIRCOLAB", txt_mun, txt_eje, con);
            meLanbide67DAO.setComboDatosIntermediacion(numExpediente, entidadColaboradora.getProvincia(), "PROVCOLAB", "TERH", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, entidadColaboradora.getCodigoPostal(), "CPCOLAB", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, txt_municipio, "MUNCOLAB", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, entidadColaboradora.getEmail(), "EMAILCOLAB", txt_mun, txt_eje, con);
            meLanbide67DAO.setTextoDatosIntermediacion(numExpediente, entidadColaboradora.getTelefono(), "TLFENTCOL", txt_mun, txt_eje, con);

        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en setDatosEntidadColaboradora() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN setDatosEntidadColaboradora");
    }

    public List<SubSolicVO> getDatosSubSolic(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            lista = meLanbide67DAO.getDatosSubSolic(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableVO> listaEstado = MeLanbide67Manager.getInstance().getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_ESTADO, ConstantesMeLanbide67.FICHERO_PROPIEDADES), adapt);
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
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getSubSolicPorID(id, con);
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
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            insertOK = meLanbide67DAO.crearNuevoSubSolic(nuevaSubSolic, con);
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
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            insertOK = meLanbide67DAO.modificarSubSolic(datModif, con);
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
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.eliminarSubSolic(id, con);
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
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getValoresDesplegables(des_cod, con);
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

    public String getValorCAmpoIAEELAK(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, String valorCampoIAEELAK, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getValorCAmpoIAEELAK(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, valorCampoIAEELAK, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando el valor  " + valorCampoIAEELAK, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el valor " + valorCampoIAEELAK, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (con != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getSeccionIAEELAK(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, String valor, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.getSeccionIAEELAK(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, valor, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando el valor  " + valor, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando el valor  " + valor, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (con != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public String guardarDatosDeSeccionEnElExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                                      String valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
            return meLanbide67DAO.guardarDatosDeSeccionEnElExpediente(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, valor, con);
        } catch (BDException e) {
            log.error("Se ha producido un error grabando el campo suplementario valor " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario valor : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        //return valor;
    }
//    public String guardarDatosDeSeccionEnElExpediente(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, Connection con) throws Exception {
//        try {
//            log.debug("==> entra en guardarDatosDeSeccionEnElExpediente Manager");
//            MeLanbide67DAO meLanbide67DAO = MeLanbide67DAO.getInstance();
//            return meLanbide67DAO.guardarDatosDeSeccionEnElExpediente(codOrganizacion, procedimiento, ejercicio, numExp, tramite, ocurrencia, codigoCampo, valor, con);
//        } catch (Exception e) {
//            log.error("Se ha producido una excepcion en la BBDD guardando en la tabla E_TDEX ", e);
//            throw new Exception(e);
//        }
//
//}
}
