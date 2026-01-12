package es.altia.flexia.integracion.moduloexterno.melanbide86.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide86.dao.MeLanbide86DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConstantesMeLanbide86;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaContratacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaMinimisVO;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa
 */
public class MeLanbide86Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide86Manager.class);

    //Instancia
    private static MeLanbide86Manager instance = null;

    public static MeLanbide86Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide86Manager.class) {
                instance = new MeLanbide86Manager();
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
        List<FilaContratacionVO> listaContrataciones = new ArrayList<FilaContratacionVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            listaContrataciones = meLanbide86DAO.getListaContrataciones(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaPeia = MeLanbide86Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide86.COD_DES_PEIA, ConstantesMeLanbide86.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide86Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide86.COD_DES_SEXO, ConstantesMeLanbide86.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaGrupoCot = MeLanbide86Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide86.COD_DES_GCON, ConstantesMeLanbide86.FICHERO_PROPIEDADES), adapt);

            for (FilaContratacionVO contr : listaContrataciones) {
                for (DesplegableAdmonLocalVO valordesp : listaPeia) {
                    if (valordesp.getDes_val_cod().equals(contr.getActDes1())) {
                        contr.setDescActDes1(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(contr.getActDes2())) {
                        contr.setDescActDes2(valordesp.getDes_nom());
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaSexo) {
                    if (valordesp.getDes_val_cod().equals(contr.getSexo2())) {
                        contr.setDescSexo2(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaGrupoCot) {
                    if (valordesp.getDes_val_cod().equals(contr.getGrupoCotiz1())) {
                        contr.setDescGrupoCotiz1(valordesp.getDes_nom());
                    }
                    if (valordesp.getDes_val_cod().equals(contr.getGrupoCotiz2())) {
                        contr.setDescGrupoCotiz2(valordesp.getDes_nom());
                    }
                }
            }
            return listaContrataciones;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las contrataciónes ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las contrataciónes ", ex);
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
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public FilaContratacionVO getContratacionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            return meLanbide86DAO.getContratacionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre una contratación:  " + id, e);
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

    /**
     *
     * @param nuevaContr
     * @param tablas
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean crearContratacion(FilaContratacionVO nuevaContr, int tablas, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            insertOK = meLanbide86DAO.crearContratación(nuevaContr, tablas, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando una contratación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando una contratación : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    /**
     *
     * @param contratModif
     * @param tablas
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean modificarContratacion(FilaContratacionVO contratModif, int tablas, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            return meLanbide86DAO.modificarContratacion(contratModif, tablas, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando una contratación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando una contratación : " + ex.getMessage(), ex);
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
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            adapt.inicioTransaccion(con);
            result = meLanbide86DAO.eliminarContratacion(id, numExp, con);
            if (result > 0) {
                if (docCV != null && !docCV.isEmpty() && !docCV.equals("undefined")) {
                    nameFechaCV = "FECHA" + docCV.substring(3, docCV.length());
                    indice = Integer.valueOf(docCV.substring(6, docCV.length() - 5));
                    meLanbide86DAO.deleteFechaDatosIntermediacion(numExp, nameFechaCV, con);
                    meLanbide86DAO.deleteFicheroCVoDemandaIntermediacion(numExp, docCV, con);
                }
                if (docDemanda != null && !docDemanda.isEmpty() && !docDemanda.equals("undefined")) {
                    nameFechaDemanda = "FECHA" + docDemanda.substring(3, docDemanda.length());
                    indice = Integer.valueOf(docDemanda.substring(7, docDemanda.length() - 5));
                    meLanbide86DAO.deleteFechaDatosIntermediacion(numExp, nameFechaDemanda, con);
                    meLanbide86DAO.deleteFicheroCVoDemandaIntermediacion(numExp, docDemanda, con);
                }
                final List<FilaContratacionVO> contrataciones = getListaContrataciones(numExp, codOrganizacion, adapt);
                
                if (contrataciones != null && indice >= 0) {
                    int i = indice;
                    for (final FilaContratacionVO contratacion : contrataciones) {
                        if (meLanbide86DAO.existeCodigoFichero(numExp, "DOC" + "CV" + "2" + (i + 1) + "INTER", con)) {
                            meLanbide86DAO.actualizaCodigoFecha(numExp, "FECHA" + "CV" + "2" + (i + 1) + "INTER", "FECHA" + "CV" + "2" + i + "INTER", con);
                            meLanbide86DAO.actualizaCodigoFichero(numExp, "DOC" + "CV" + "2" + (i + 1) + "INTER", "DOC" + "CV" + "2" + i + "INTER", con);
                        }
                        if (meLanbide86DAO.existeCodigoFichero(numExp, "DOC" + "DEM" + "2" + (i + 1) + "INTER", con)) {
                            meLanbide86DAO.actualizaCodigoFecha(numExp, "FECHA" + "DEM" + "2" + (i + 1) + "INTER", "FECHA" + "DEM" + "2" + i + "INTER", con);
                            meLanbide86DAO.actualizaCodigoFichero(numExp, "DOC" + "DEM" + "2" + (i + 1) + "INTER", "DOC" + "DEM" + "2" + i + "INTER", con);
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
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            return meLanbide86DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un Desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public void setDatosCVoDemandaIntermediacion(String numExpediente,
            final Date fechaDocInter, final String nameFieldFecha, final File doc, final String nameFieldDoc,
            AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio setDatosCVoDemandaIntermediacion");
        Connection con = null;
        MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide86DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide86DAO.getEJEDatosIntermediacion(numExpediente, con);

            meLanbide86DAO.setFechaDatosIntermediacion(numExpediente, fechaDocInter, nameFieldFecha, txt_mun, txt_eje, con);
            meLanbide86DAO.setFicheroCVoDemandaIntermediacion(numExpediente, doc, nameFieldDoc, txt_mun, txt_eje, con);

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
        MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
        DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide86DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide86DAO.getEJEDatosIntermediacion(numExpediente, con);

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
        MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();

        try {
            con = adaptador.getConnection();

            BigDecimal txt_mun = meLanbide86DAO.getMUNDatosIntermediacion(numExpediente, con);
            BigDecimal txt_eje = meLanbide86DAO.getEJEDatosIntermediacion(numExpediente, con);

            final GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", txt_mun.toString());
            gVO.setAtributo("ejercicio", txt_eje.toString());
            gVO.setAtributo("numero", numExpediente);
            camposFormulario = meLanbide86DAO.getValoresFichero(gVO, con);

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


public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            lista = meLanbide86DAO.getDatosMinimis(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide86Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide86.COD_DES_DTSV, ConstantesMeLanbide86.FICHERO_PROPIEDADES), adapt);
       
            for (FilaMinimisVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(cont.getEstado())) {
                        cont.setDesEstado(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD recuperando datos sobre las minimis ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD recuperando datos sobre las minimis ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaMinimisVO getMinimisPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            return meLanbide86DAO.getMinimisPorID(id, con);
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
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            return meLanbide86DAO.eliminarMinimis(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD al eliminar una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD al eliminar una minimis:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }

  
    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            insertOK = meLanbide86DAO.crearNuevoMinimis(nuevaMinimis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD creando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD creando una minimis : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide86DAO meLanbide86DAO = MeLanbide86DAO.getInstance();
            insertOK = meLanbide86DAO.modificarMinimis(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD actualizando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD actualizando una minimis : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

  
    
}
