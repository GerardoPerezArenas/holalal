package es.altia.flexia.integracion.moduloexterno.melanbide84.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide84.dao.MeLanbide84DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConstantesMeLanbide84;
import es.altia.flexia.integracion.moduloexterno.melanbide84.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide84.vo.PersonaVO;
import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide84Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide84Manager.class);

    //Instancia
    private static MeLanbide84Manager instance = null;

    public static MeLanbide84Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide84Manager.class) {
                instance = new MeLanbide84Manager();
            }
        }
        return instance;
    }

    public List<PersonaVO> getListaPersonas(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            lista = meLanbide84DAO.getListaPersonas(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaNAPE = MeLanbide84Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_DES_NAPE, ConstantesMeLanbide84.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide84Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_DES_SEXO, ConstantesMeLanbide84.FICHERO_PROPIEDADES), adapt);
            for (PersonaVO pers : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaNAPE) {
                    if (valordesp.getDes_val_cod().equals(pers.getNivelAcademico())) {
                        pers.setDescNivel(valordesp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableAdmonLocalVO valordesp : listaSexo) {
                    if (Integer.valueOf(valordesp.getDes_val_cod()).equals(pers.getSexo())) {
                        pers.setDescSexo(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las personas ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las personas ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public PersonaVO getPersonaPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getPersonaPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre una persona:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una persona:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaPersona(PersonaVO nuevaPersona, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            insertOK = meLanbide84DAO.crearNuevaPersona(nuevaPersona, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando una persona : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando una persona : " + ex.getMessage(), ex);
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

    public boolean modificarPersona(PersonaVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.modificarPersona(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando una persona : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando una persona : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarPersona(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.eliminarPersona(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una persona:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una persona:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean estaEnTramite110(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.estaEnTramite110(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD comprobando el trámite ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
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

    public ExpedienteVO getDatosExpediente(int codMunicipio, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getDatosExpediente(codMunicipio, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public IdExpedienteVO getDatosIdExpediente(int codOrganizacion, String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getDatosIdExpediente(numExpediente, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int getcodigoUOR(int codOrganizacion, String numExpediente, String procedimientoPadre, int codTramite, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getcodigoUOR(codOrganizacion, numExpediente, procedimientoPadre, codTramite, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el códogo de la UOR del expediente ");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int getUsuarioExpediente(int codOrganizacion, String numExpediente, String procedimientoPadre, int codTramite, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getUsuarioExpediente(codOrganizacion, numExpediente, procedimientoPadre, codTramite, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el usuario del expediente ");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<InteresadoExpedienteVO> getDatosInteresado(int codOrganizacion, Integer ejercicio, String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getDatosInteresado(codOrganizacion, ejercicio, numExpediente, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public TerceroVO getDatosTercero(int codOrganizacion, String dni, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getTercero(codOrganizacion, dni, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el tercero.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int existeTercero(TerceroVO ter, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.existeTercero(ter, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si existe el tercero.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int existeDomicilioTer(int codigoTerceroAPE, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.existeDomicilioTer(codigoTerceroAPE, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error comprobandi si el tercero tiene domicilio.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DireccionVO getDomicilioTercero(TerceroVO ter, int codTercero, int codDomicilio, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getDomicilioTercero(ter, codTercero, codDomicilio, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el domicilio del tercero.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int[] getCodigosMunicipio(String localidad, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getCodigosMunicipio(localidad, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el codigo Municipio ." + localidad);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int getTipoVia(int codPais, int codProvincia, int codMunicipio, String nombreVia, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getTipoVia(codPais, codProvincia, codMunicipio, nombreVia, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el tipo Vía.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int getCodTerceroExp(int codOrg, String numExp, int rol, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getCodTerceroExp(codOrg, numExp, rol, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el tercero.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean existeSuplementarioTercero(int codOrg, String tabla, String campo, int codTercero, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.existeSuplementarioTercero(codOrg, tabla, campo, codTercero, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error comprobando si existe " + campo);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabarSuplementarioTercero(int codOrg, String tabla, String campo, int codTercero, String valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.grabarSuplementarioTercero(codOrg, tabla, campo, codTercero, valor, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error grabando " + campo);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean actualizarSuplementarioTercero(int codOrg, String tabla, String campo, int codTercero, String valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.actualizarSuplementarioTercero(codOrg, tabla, campo, codTercero, valor, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error actualizando " + campo);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean existeSuplementarioDesplegable(int codOrg, String campo, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.existeSuplementarioDesplegable(codOrg, campo, numExp, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error comprobando si existe " + campo);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabarSuplementarioDesplegable(int codOrg, String campo, String numExp, String valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.grabarSuplementarioDesplegable(codOrg, campo, numExp, valor, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error grabando " + campo);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<String> getExpedientesEntidad(int codOrganizacion, int codEntidad, String programa, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getExpedientesEntidad(codOrganizacion, codEntidad, programa, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando expedientes de la entidad.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean getExpedientesInteresado(int codOrganizacion, int codigoTerceroAPE, String expediente, String programa, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getExpedientesInteresado(codOrganizacion, codigoTerceroAPE, expediente, programa, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error comprobando si el tercero existe en expediente de entidad.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<String> getExpedientesPersona(int codOrganizacion, int codigoTerceroAPE, String eje, String programa, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getExpedientesPersona(codOrganizacion, codigoTerceroAPE, eje, programa, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando los expedientes " + programa + " del tercero.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean existeSuplementarioFecha(int codOrg, String campo, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.existeSuplementarioFecha(codOrg, campo, numExp, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error comprobando si existe " + campo);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabarSuplementarioFecha(int codOrg, String campo, String numExp, Date valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.grabarSuplementarioFecha(codOrg, campo, numExp, valor, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error grabando " + campo);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getCodEntidad(int codOrg, String documento, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide84DAO meLanbide84DAO = MeLanbide84DAO.getInstance();
            return meLanbide84DAO.getCodEntidad(codOrg, documento, con);
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el cod  de la entidad.");
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

}
