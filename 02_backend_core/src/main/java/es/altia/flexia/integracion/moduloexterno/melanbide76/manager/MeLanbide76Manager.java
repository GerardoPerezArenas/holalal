package es.altia.flexia.integracion.moduloexterno.melanbide76.manager;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.flexia.integracion.moduloexterno.melanbide76.dao.MeLanbide76DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide76.util.ConstantesMeLanbide76;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.DatosTablaDesplegableExtVO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.DesplegableExternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.MinimisVO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide76Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide76Manager.class);
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    //Instancia
    private static MeLanbide76Manager instance = null;

    public static MeLanbide76Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide76Manager.class) {
                instance = new MeLanbide76Manager();
            }
        }
        return instance;
    }

    public static void grabarError(ErrorBean error, String excepError, String traza, String numExp) {
        try {
            log.error("grabando el error");
            error.setMensajeExcepError(excepError);
            error.setTraza(excepError);
            error.setCausa(traza);
            log.error("causa: " + traza);
            log.error("numExp: " + numExp);
            if ("".equals(numExp)) {
                numExp = "0000/ERRMISGEST/000000";
            }
            String idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea("DLDUR"); //convierteProcedimiento(codProcedimiento);

            log.error("procedimiento: " + idProcedimiento);
            error.setIdProcedimiento(idProcedimiento);
            error.setIdClave("");
            error.setSistemaOrigen("REGEXLAN");
            error.setErrorLog("flexia_debug");
            error.setIdFlexia(numExp);
            log.error("Vamos a registrar el error");

            RegistroErrores.registroError(error);
        } catch (Exception ex) {
            log.error("Error al grabarError" + ex);
        }
    }

    public List<ContratacionVO> getDatosContratacion(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            lista = meLanbide76DAO.getDatosContratacion(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_SEXO, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaMayor45 = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_BOOL, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaFinFormativa = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_BOOL, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_JORN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaGrupoCotizacion = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_GCOT, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaTipRetribucion = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_DTRT, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);

            //desplegables externos
            DatosTablaDesplegableExtVO datosTablaDesplegableOcupaciones = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_OCIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            String tablaOcupaciones = datosTablaDesplegableOcupaciones.getTabla();
            String campoCodigoOcupaciones = datosTablaDesplegableOcupaciones.getCampoCodigo();
            String campoValorOcupaciones = datosTablaDesplegableOcupaciones.getCampoValor();
            List<DesplegableExternoVO> listaOcupacion = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaOcupaciones, campoCodigoOcupaciones, campoValorOcupaciones, adapt);

            DatosTablaDesplegableExtVO datosTablaDesplegableTitulaciones = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_TIIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            String tablaTitulaciones = datosTablaDesplegableTitulaciones.getTabla();
            String campoCodigoTitulaciones = datosTablaDesplegableTitulaciones.getCampoCodigo();
            String campoValorTitulaciones = datosTablaDesplegableTitulaciones.getCampoValor();
            List<DesplegableExternoVO> listaTitulacion = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaTitulaciones, campoCodigoTitulaciones, campoValorTitulaciones, adapt);

            DatosTablaDesplegableExtVO datosTablaDesplegableCProfesionales = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_CPIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            String tablaCProfesionales = datosTablaDesplegableCProfesionales.getTabla();
            String campoCodigoCProfesionales = datosTablaDesplegableCProfesionales.getCampoCodigo();
            String campoValorCProfesionales = datosTablaDesplegableCProfesionales.getCampoValor();
            List<DesplegableExternoVO> listaCProfesionalidad = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaCProfesionales, campoCodigoCProfesionales, campoValorCProfesionales, adapt);

            for (ContratacionVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaSexo) {
                    if (valordesp.getDes_val_cod().equals(cont.getSexo())) {
                        cont.setDesSexo(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaMayor45) {
                    if (valordesp.getDes_val_cod().equals(cont.getMayor45())) {
                        cont.setMayor45(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaFinFormativa) {
                    if (valordesp.getDes_val_cod().equals(cont.getFinFormativa())) {
                        cont.setFinFormativa(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaJornada) {
                    if (valordesp.getDes_val_cod().equals(cont.getJornada())) {
                        cont.setDesJornada(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaGrupoCotizacion) {
                    if (valordesp.getDes_val_cod().equals(cont.getGrupoCotizacion())) {
                        cont.setDesGrupoCotizacion(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaTipRetribucion) {
                    if (valordesp.getDes_val_cod().equals(cont.getTipRetribucion())) {
                        cont.setDesTipRetribucion(valordesp.getDes_nom());
                        break;
                    }
                }

                //desplegables externos
                for (DesplegableExternoVO valordesp : listaOcupacion) {
                    if (valordesp.getCampoCodigo().equals(cont.getOcupacion())) {
                        cont.setDesOcupacion(valordesp.getCampoValor());
                        break;
                    }
                }
                for (DesplegableExternoVO valordesp : listaTitulacion) {
                    if (valordesp.getCampoCodigo().equals(cont.getTitulacion())) {
                        cont.setDesTitulacion(valordesp.getCampoValor());
                        break;
                    }
                }
                for (DesplegableExternoVO valordesp : listaCProfesionalidad) {
                    if (valordesp.getCampoCodigo().equals(cont.getcProfesionalidad())) {
                        cont.setDesCProfesionalidad(valordesp.getCampoValor());
                        break;
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
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public ContratacionVO getContratacionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            return meLanbide76DAO.getContratacionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una contratación:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una contratación:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarContratacion(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            return meLanbide76DAO.eliminarContratacion(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una contratación:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una contratación:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<ContratacionVO> getContrataciones(String numExp, AdaptadorSQLBD adapt) throws Exception {
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            lista = meLanbide76DAO.getContratacion(numExp, con);

            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_SEXO, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaMayor45 = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_BOOL, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaFinFormativa = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_BOOL, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_JORN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaGrupoCotizacion = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_GCOT, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaTipRetribucion = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_DTRT, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);

            //desplegables externos
            DatosTablaDesplegableExtVO datosTablaDesplegableOcupaciones = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_OCIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            String tablaOcupaciones = datosTablaDesplegableOcupaciones.getTabla();
            String campoCodigoOcupaciones = datosTablaDesplegableOcupaciones.getCampoCodigo();
            String campoValorOcupaciones = datosTablaDesplegableOcupaciones.getCampoValor();
            List<DesplegableExternoVO> listaOcupacion = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaOcupaciones, campoCodigoOcupaciones, campoValorOcupaciones, adapt);

            DatosTablaDesplegableExtVO datosTablaDesplegableTitulaciones = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_TIIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            String tablaTitulaciones = datosTablaDesplegableTitulaciones.getTabla();
            String campoCodigoTitulaciones = datosTablaDesplegableTitulaciones.getCampoCodigo();
            String campoValorTitulaciones = datosTablaDesplegableTitulaciones.getCampoValor();
            List<DesplegableExternoVO> listaTitulacion = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaTitulaciones, campoCodigoTitulaciones, campoValorTitulaciones, adapt);

            DatosTablaDesplegableExtVO datosTablaDesplegableCProfesionales = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_CPIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);
            String tablaCProfesionales = datosTablaDesplegableCProfesionales.getTabla();
            String campoCodigoCProfesionales = datosTablaDesplegableCProfesionales.getCampoCodigo();
            String campoValorCProfesionales = datosTablaDesplegableCProfesionales.getCampoValor();
            List<DesplegableExternoVO> listaCProfesionalidad = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaCProfesionales, campoCodigoCProfesionales, campoValorCProfesionales, adapt);

            for (ContratacionVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaSexo) {
                    if (valordesp.getDes_val_cod().equals(cont.getSexo())) {
                        cont.setDesSexo(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaMayor45) {
                    if (valordesp.getDes_val_cod().equals(cont.getMayor45())) {
                        cont.setMayor45(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaFinFormativa) {
                    if (valordesp.getDes_val_cod().equals(cont.getFinFormativa())) {
                        cont.setFinFormativa(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaJornada) {
                    if (valordesp.getDes_val_cod().equals(cont.getJornada())) {
                        cont.setDesJornada(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaGrupoCotizacion) {
                    if (valordesp.getDes_val_cod().equals(cont.getGrupoCotizacion())) {
                        cont.setDesGrupoCotizacion(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaTipRetribucion) {
                    if (valordesp.getDes_val_cod().equals(cont.getTipRetribucion())) {
                        cont.setDesTipRetribucion(valordesp.getDes_nom());
                        break;
                    }
                }

                //desplegables externos
                for (DesplegableExternoVO valordesp : listaOcupacion) {
                    if (valordesp.getCampoCodigo().equals(cont.getOcupacion())) {
                        cont.setDesOcupacion(valordesp.getCampoValor());
                        break;
                    }
                }
                for (DesplegableExternoVO valordesp : listaTitulacion) {
                    if (valordesp.getCampoCodigo().equals(cont.getTitulacion())) {
                        cont.setDesTitulacion(valordesp.getCampoValor());
                        break;
                    }
                }
                for (DesplegableExternoVO valordesp : listaCProfesionalidad) {
                    if (valordesp.getCampoCodigo().equals(cont.getcProfesionalidad())) {
                        cont.setDesCProfesionalidad(valordesp.getCampoValor());
                        break;
                    }
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando las contrataciones:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general en la BBDD recuperando las contrataciones:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaContratacion(ContratacionVO nuevaContratacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            insertOK = meLanbide76DAO.crearNuevaContratacion(nuevaContratacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una contratación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una contratación : " + ex.getMessage(), ex);
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

    public boolean modificarContratacion(ContratacionVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            insertOK = meLanbide76DAO.modificarContratacion(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una contratación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una contratación : " + ex.getMessage(), ex);
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

    public List<MinimisVO> getDatosMinimis(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<MinimisVO> lista = new ArrayList<MinimisVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            lista = meLanbide76DAO.getDatosMinimis(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_DTSV, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);

            for (MinimisVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(cont.getEstado())) {
                        cont.setDesEstado(valordesp.getDes_nom());
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
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public MinimisVO getMinimisPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            return meLanbide76DAO.getMinimisPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarMinimis(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            return meLanbide76DAO.eliminarMinimis(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una minimis:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<MinimisVO> getMinimis(String numExp, AdaptadorSQLBD adapt) throws Exception {
        List<MinimisVO> lista = new ArrayList<MinimisVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            lista = meLanbide76DAO.getMinimis(numExp, con);

            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_DTSV, ConstantesMeLanbide76.FICHERO_PROPIEDADES), adapt);

            for (MinimisVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(cont.getEstado())) {
                        cont.setDesEstado(valordesp.getDes_nom());
                        break;
                    }
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando las minimis:  " + e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general en la BBDD recuperando las minimis:   " + ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaMinimis(MinimisVO nuevaMinimis, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            insertOK = meLanbide76DAO.crearNuevaMinimis(nuevaMinimis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una minimis : " + ex.getMessage(), ex);
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

    public boolean modificarMinimis(MinimisVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            insertOK = meLanbide76DAO.modificarMinimis(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una minimis : " + ex.getMessage(), ex);
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

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            return meLanbide76DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
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

    public DatosTablaDesplegableExtVO getDatosMapeoDesplegableExterno(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            return meLanbide76DAO.getDatosMapeoDesplegableExterno(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de datos de tabla de desplegable externo : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de datos de tabla de desplegable externo :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DesplegableExternoVO> getValoresDesplegablesExternos(String tablaDesplegable, String campoCodigo, String campoValor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide76DAO meLanbide76DAO = MeLanbide76DAO.getInstance();
            return meLanbide76DAO.getValoresDesplegablesExternos(tablaDesplegable, campoCodigo, campoValor, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable externo de tabla : " + tablaDesplegable, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable externo de tabla :  " + tablaDesplegable, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

}
