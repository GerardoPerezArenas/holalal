/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43GestionTramitesExpedienteETraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43GestionTramitesExpedienteRespuesta;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43JobsTareaEje;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tml;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide43GestionTramitesExpedienteDAO {
    
    private static final Logger LOG = LogManager.getLogger(MeLanbide43GestionTramitesExpedienteDAO.class);
    private static final SimpleDateFormat dateLogFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat dateFormatddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    
    /***
     * Metodo que permite cerrar el tramite de un expediente. 
     * @param codOrganizacon
     * @param numExpediente
     * @param codigoTramite
     * @param ocurrenciaTramite : Si recibimos 0, se cierran todas las ocurrencias existentes que esten abiertas. 
     * @param observaciones
     * @param con
     * @return MeLanbide43GestionTramitesExpedienteRespuesta : CodigoRespuesta 0 Si ha sido correcto. != 0 y Descripcion en caso contrario.
     */
    public MeLanbide43GestionTramitesExpedienteRespuesta cerrarTramiteConRegistroHistoricoExpediente(int codOrganizacon, String numExpediente, MeLanbide43GestionTramitesExpedienteETraVO tramite, int ocurrenciaTramite, MeLanbide43JobsTareaEje meLanbide43JobsTareaEje,String observaciones, Connection con){
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        String[] datosExpediente = (numExpediente!=null && !numExpediente.isEmpty() ? numExpediente.split(ConstantesMeLanbide43.BARRA_SEPARADORA):null);
        PreparedStatement ps = null;
        try {
            int codigoTramite = (tramite!=null ? tramite.getTRA_COD() : 0);
            respuesta=validarCerrarTramiteExpediente(codOrganizacon,numExpediente,codigoTramite,ocurrenciaTramite,con);
            if(respuesta!=null && respuesta.isRespuestaOKBoolean()){
                String query = "update E_CRO "
                        + " SET CRO_FEF=? "
                        + " , CRO_USF = ?  "
                        + " , CRO_OBS=(CRO_OBS||' - '|| ? ) "
                        + " WHERE"
                        + " CRO_MUN=? "
                        + " AND CRO_EJE=? "
                        + " AND CRO_PRO=? "
                        + " AND CRO_NUM=? "
                        + " AND CRO_TRA=? "
                        + " AND CRO_OCU"+(ocurrenciaTramite>0?"=":">=")+"? " 
                        ;
                LOG.debug("sql = " + query);
                ps = con.prepareStatement(query);
                LOG.debug("param = "
                        + dateFormatddMMyyyy.format(new Date())
                        + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                        + "," + observaciones
                        + "," + codOrganizacon
                        + "," + (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0)
                        + "," + (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "")
                        + "," + numExpediente
                        + "," + codigoTramite
                        + "," + ocurrenciaTramite
                );
                int counter = 1;
                ps.setDate(counter++, new java.sql.Date(new Date().getTime()));
                ps.setInt(counter++, meLanbide43JobsTareaEje.getCodigoUsuario());
                ps.setString(counter++, (observaciones != null && !observaciones.isEmpty()) ? observaciones : "");
                ps.setInt(counter++, codOrganizacon);
                ps.setInt(counter++, (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0));
                ps.setString(counter++, (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : ""));
                ps.setString(counter++, numExpediente);
                ps.setInt(counter++, codigoTramite);
                ps.setInt(counter++, ocurrenciaTramite);
                if (ps.executeUpdate() > 0) {
                    respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.TRUE);
                } else {
                    respuesta.setCodigoRespuesta("2");
                    respuesta.setDescripcionRespuesta("No se ha realizado ninguna Actualizacion del Tramite, de acuerdo a los parametros recibidos : "
                            + dateFormatddMMyyyy.format(new Date())
                            + "," + observaciones
                            + "," + codOrganizacon
                            + "," + (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0)
                            + "," + (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "")
                            + "," + numExpediente
                            + "," + codigoTramite
                            + "," + ocurrenciaTramite
                    );
                    LOG.error(respuesta.getCodigoDescripcionErrorAsString());
                }
                if (respuesta.isRespuestaOKBoolean()) {
                    // Registramos en el Hisotirico la Operacion
                    TramitacionExpedientesValueObject infoTramiteGVO = new TramitacionExpedientesValueObject();
                    infoTramiteGVO.setCodMunicipio(String.valueOf(codOrganizacon));
                    infoTramiteGVO.setEjercicio(datosExpediente[0]);
                    infoTramiteGVO.setCodUsuario(String.valueOf(meLanbide43JobsTareaEje.getCodigoUsuario()));
                    infoTramiteGVO.setCodProcedimiento(datosExpediente[1]);
                    infoTramiteGVO.setNumero(numExpediente);
                    infoTramiteGVO.setNumeroExpediente(numExpediente);
                    infoTramiteGVO.setCodTramite(String.valueOf(codigoTramite));
                    String datosDescTramite = (tramite != null && tramite.getE_TML()!=null ? tramite.getE_TML().getTRA_COU() + " " + tramite.getE_TML().getTML_VALOR() : "");
                    infoTramiteGVO.setTramite(datosDescTramite);
                    infoTramiteGVO.setOcurrenciaTramite(String.valueOf(ocurrenciaTramite));
                    infoTramiteGVO.setNombreUsuario(meLanbide43JobsTareaEje.getNombreUsuario());
                    infoTramiteGVO.setUsuario(meLanbide43JobsTareaEje.getNombreUsuario());
                    infoTramiteGVO.setFechaFin(dateFormatddMMyyyy.format(new Date()));
                    try {
                        OperacionesExpedienteDAO.getInstance().registrarFinalizarTramite(infoTramiteGVO, null, con);
                    } catch (TechnicalException e) {
                        LOG.error("TechnicalException Al parsear alguno de los parametros recibidos - Abrir Tramite - Registrando Operacion Historico :  " + e.getMessage(), e);
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta(false, "-5", "NumberFormatException - Al parsear alguno de los parametros recibidos  - Cerrar Tramite : " + e.getMessage());
                    } catch (Exception e) {
                        LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - Abrir Tramite - Registrando Operacion Historico :  " + e.getMessage(), e);
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta(false, "-4", "NumberFormatException - Al parsear alguno de los parametros recibidos  - Abrir Tramite : " + e.getMessage());
                    }
                }
            }else{
                LOG.error("1 - Error al validar cierre del tramite: "
                        + (respuesta!=null?respuesta.getCodigoDescripcionErrorAsString():""));
                return respuesta;
            }
        }catch (NumberFormatException ne){
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - Cerrar Tramite :  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-3","NumberFormatException - Al parsear alguno de los parametros recibidos  - Cerra Tramite : " + ne.getMessage());
        } catch (SQLException e) {
            LOG.error("SQLException al Cerrar un Tramite" + e.getMessage() , e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2","SQLException: " + e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al Cerrar un Tramite" + e.getMessage() , e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1","Error Generico: " + e.getMessage());
        }finally{
            try {
                if(ps!=null && !ps.isClosed())
                    ps.close();
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    /***
     * 
     * @param codOrganizacon
     * @param numExpediente
     * @param codigoTramite
     * @param observaciones
     * @param codigoUsuario
     * @param permitirCrearSiExisteTramCerrado : Indica que si al validar se encuentra que ya existe un tramite igual cerrado (si esta abierto no lo creamos) se permita crear otro. 
     * @param con
     * @return 
     */
    public MeLanbide43GestionTramitesExpedienteRespuesta abrirTramiteConRegistroHistoricoExpediente(int codOrganizacon, String numExpediente, MeLanbide43GestionTramitesExpedienteETraVO tramite, String observaciones, MeLanbide43JobsTareaEje meLanbide43JobsTareaEje, boolean permitirCrearSiExisteTramCerrado, Connection con) {
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        PreparedStatement ps = null;
        //ha habido problemas java.sql.SQLException: ORA-01000: número máximo de cursores abiertos excedido para más de 1000, se crea otro PreparedStatement diferente
        PreparedStatement ps1 = null;
        try {
            /** Validamos si se puede crear el tramite.
             * Esta validacion devuelve en el map de datos adicionales la ocurrencia siguiente del tramite si existe uno y se ha permitido multiocurrencia en el tramite o 1 sino existe para crear la primera.
             * Y la unidad uor del expediente
             */
            int codigoTramite = tramite.getTRA_COD();
            respuesta = validarAbrirTramiteExpediente(codOrganizacon,numExpediente,codigoTramite,permitirCrearSiExisteTramCerrado,con);
            if (respuesta != null && respuesta.isRespuestaOKBoolean()) {
                // Procedemos a Abrir el tramite
                String[] datosExpediente = (numExpediente != null && !numExpediente.isEmpty() ? numExpediente.split(ConstantesMeLanbide43.BARRA_SEPARADORA) : null);
                int ocurrenciaTramite = (respuesta.getRespuestaDatosAdicionales()!=null && !respuesta.getRespuestaDatosAdicionales().get("ocurrenciaTramite").isEmpty() ? Integer.valueOf(respuesta.getRespuestaDatosAdicionales().get("ocurrenciaTramite")):0);
                int unidadOrganicaExpediente = (respuesta.getRespuestaDatosAdicionales()!=null && !respuesta.getRespuestaDatosAdicionales().get("unidadOrganicaExpediente").isEmpty() ? Integer.valueOf(respuesta.getRespuestaDatosAdicionales().get("unidadOrganicaExpediente")):0);
                String query = "INSERT INTO E_CRO "
                        + " (CRO_PRO, CRO_EJE, CRO_NUM, CRO_TRA, CRO_FEI, CRO_FEF, CRO_USU, CRO_UTR, CRO_MUN, CRO_OCU, CRO_FIP, CRO_FLI, CRO_FFP, CRO_RES, CRO_OBS, CRO_USF, CRO_AVISADOCFP, CRO_AVISADOFDP) "
                        + " values "
                        + "(?,?,?,?,?,NULL,?,?,?,?,NULL,NULL,NULL,NULL,?,?,0,0) "
                        ;
                LOG.debug("sql = " + query);
                ps = con.prepareStatement(query);
                LOG.debug("param = "
                        + (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "")
                        + "," + (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0)
                        + "," + numExpediente
                        + "," + codigoTramite
                        + "," + dateFormatddMMyyyy.format(new Date())
                        + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                        + "," + unidadOrganicaExpediente
                        + "," + codOrganizacon
                        + "," + (ocurrenciaTramite)
                        + "," + observaciones
                        + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                );
                int counter = 1;
                ps.setString(counter++,(datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : ""));
                ps.setInt(counter++, (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0));
                ps.setString(counter++, numExpediente);
                ps.setInt(counter++, codigoTramite);
                ps.setDate(counter++, new java.sql.Date(new Date().getTime()));
                ps.setInt(counter++, meLanbide43JobsTareaEje.getCodigoUsuario());
                ps.setInt(counter++, unidadOrganicaExpediente);
                ps.setInt(counter++, codOrganizacon);
                ps.setInt(counter++, ocurrenciaTramite);
                ps.setString(counter++, observaciones);
                ps.setInt(counter++, meLanbide43JobsTareaEje.getCodigoUsuario());
                if (ps.executeUpdate() > 0) {
                    // Insertamos en la tabla de flujo de Historial Tramites para retrocesos : -1 Origen : (Se abren sin flujo Retroceso)
                    query = "INSERT INTO LIST_TRAM_ORIG(EJERCICIO, COD_PRO, COD_MUN, NUM_EXP, COD_TRA_ORIGEN, OCU_TRA_ORIGEN, COD_TRA_DESTINO, OCU_TRA_DESTINO) "
                            + " VALUES (?,?,?,?,-1,-1,?,?)";
                    LOG.debug("sql = " + query);
                    //ps.clearParameters();
                    ps1 = con.prepareStatement(query);
                    LOG.debug("param = "
                            + (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0)
                            + "," + (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "")
                            + "," + codOrganizacon
                            + "," + numExpediente
                            + "," + codigoTramite
                            + "," + ocurrenciaTramite
                    );
                    counter = 1;
                    ps1.setInt(counter++, (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0));
                    ps1.setString(counter++, (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : ""));
                    ps1.setInt(counter++, codOrganizacon);
                    ps1.setString(counter++, numExpediente);
                    ps1.setInt(counter++, codigoTramite);
                    ps1.setInt(counter++, ocurrenciaTramite);
                    if (ps1.executeUpdate() > 0) {
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.TRUE);
                    }else{
                        respuesta.setCodigoRespuesta("3");
                        respuesta.setDescripcionRespuesta("No se ha podido realizar Insercion en la tabla de Historial de Tramites (LIST_TRAM_ORIG), de acuerdo a los parametros recibidos : "
                                + " " + codOrganizacon
                                + " " + numExpediente
                                + " " + codigoTramite
                                + " " + observaciones
                                + " " + meLanbide43JobsTareaEje.getCodigoUsuario()
                                + " " + permitirCrearSiExisteTramCerrado
                        );
                        LOG.error(respuesta.getCodigoDescripcionErrorAsString());
                    }
                } else {
                    respuesta.setCodigoRespuesta("2");
                    respuesta.setDescripcionRespuesta("No se ha realizado ninguna Insercion del Tramite, de acuerdo a los parametros recibidos : "
                            + (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "")
                            + "," + (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0)
                            + "," + numExpediente
                            + "," + codigoTramite
                            + "," + dateFormatddMMyyyy.format(new Date())
                            + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                            + "," + unidadOrganicaExpediente
                            + "," + codOrganizacon
                            + "," + (ocurrenciaTramite)
                            + "," + observaciones
                            + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                            + ", " + permitirCrearSiExisteTramCerrado
                    );
                    LOG.error(respuesta.getCodigoDescripcionErrorAsString());
                }
                if (respuesta.isRespuestaOKBoolean()) {
                    // Registramos en el Hisotirico la Operacion
                    GeneralValueObject infoTramiteGVO = new GeneralValueObject();
                    infoTramiteGVO.setAtributo("codMunicipio", String.valueOf(codOrganizacon));
                    infoTramiteGVO.setAtributo("ejercicio", datosExpediente[0]);
                    infoTramiteGVO.setAtributo("usuario", String.valueOf(meLanbide43JobsTareaEje.getCodigoUsuario()));
                    infoTramiteGVO.setAtributo("codProcedimiento", datosExpediente[1]);
                    infoTramiteGVO.setAtributo("numero", numExpediente);
                    infoTramiteGVO.setAtributo("codTramite", String.valueOf(codigoTramite));
                    String datosDescTramite = (tramite.getE_TML() != null ? tramite.getTRA_COU() + " " + tramite.getE_TML().getTML_VALOR() : "");
                    infoTramiteGVO.setAtributo("nomTramite", datosDescTramite);
                    infoTramiteGVO.setAtributo("ocurrTramite", String.valueOf(ocurrenciaTramite));
                    infoTramiteGVO.setAtributo("nombreUsuario", meLanbide43JobsTareaEje.getNombreUsuario());
                    infoTramiteGVO.setAtributo("fechaInicioTramite", dateFormatddMMyyyy.format(new Date()));
                    try {
                        OperacionesExpedienteDAO.getInstance().registrarIniciarTramite(infoTramiteGVO, false, con);
                    } catch (TechnicalException e) {
                        LOG.error("TechnicalException Al parsear alguno de los parametros recibidos - Cerrar Tramite - Registrando Operacion Historico :  " + e.getMessage(), e);
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-5", "NumberFormatException - Al parsear alguno de los parametros recibidos  - Cerra Tramite : " + e.getMessage());
                    } catch (Exception e) {
                        LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - Cerrar Tramite - Registrando Operacion Historico :  " + e.getMessage(), e);
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-4", "NumberFormatException - Al parsear alguno de los parametros recibidos  - Cerra Tramite : " + e.getMessage());
                    }
                }
            } else {
                LOG.error(" 1 - Error al validar el alta de un tramite. "
                    + (respuesta!=null ? respuesta.getCodigoDescripcionErrorAsString(): ""));
                return respuesta;
            }
        }catch (NumberFormatException ne){
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - Abrir Tramite :  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-3","NumberFormatException - Al parsear alguno de los parametros recibidos  - Abrir Tramite : " + ne.getMessage());
        } catch (SQLException sqle){
            LOG.error("SQLException al Abrir un Tramite" + sqle.getMessage(), sqle);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2","SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al Abrir un Tramite" + e.getMessage(), e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1","Error Generico: " + e.getMessage());
        }finally{
            try {
                if(ps!=null && !ps.isClosed())
                    ps.close();
                if(ps1!=null && !ps1.isClosed())
                    ps1.close();
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + + e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            }catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }

    public MeLanbide43GestionTramitesExpedienteRespuesta abrirTramiteOcuConRegistroHistoricoExpediente(int codTramOrigen,int ocuOrigen, int ocuDestino, int codOrganizacon, String numExpediente, MeLanbide43GestionTramitesExpedienteETraVO tramite, String observaciones, MeLanbide43JobsTareaEje meLanbide43JobsTareaEje, boolean permitirCrearSiExisteTramCerrado, Connection con) {
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        PreparedStatement ps = null;
        //ha habido problemas java.sql.SQLException: ORA-01000: número máximo de cursores abiertos excedido para más de 1000, se crea otro PreparedStatement diferente
        PreparedStatement ps1 = null;
        try {
            /** Validamos si se puede crear el tramite.
             * Esta validacion devuelve en el map de datos adicionales la ocurrencia siguiente del tramite si existe uno y se ha permitido multiocurrencia en el tramite o 1 sino existe para crear la primera.
             * Y la unidad uor del expediente
             */
            int codigoTramite = tramite.getTRA_COD();
            respuesta = validarAbrirTramiteExpediente(codOrganizacon,numExpediente,codigoTramite,permitirCrearSiExisteTramCerrado,con);
            if (respuesta != null && respuesta.isRespuestaOKBoolean()) {
                // Procedemos a Abrir el tramite
                String[] datosExpediente = (numExpediente != null && !numExpediente.isEmpty() ? numExpediente.split(ConstantesMeLanbide43.BARRA_SEPARADORA) : null);
                int ocurrenciaTramite = ocuDestino;
                int unidadOrganicaExpediente = (respuesta.getRespuestaDatosAdicionales()!=null && !respuesta.getRespuestaDatosAdicionales().get("unidadOrganicaExpediente").isEmpty() ? Integer.valueOf(respuesta.getRespuestaDatosAdicionales().get("unidadOrganicaExpediente")):0);
                String query = "INSERT INTO E_CRO "
                        + " (CRO_PRO, CRO_EJE, CRO_NUM, CRO_TRA, CRO_FEI, CRO_FEF, CRO_USU, CRO_UTR, CRO_MUN, CRO_OCU, CRO_FIP, CRO_FLI, CRO_FFP, CRO_RES, CRO_OBS, CRO_USF, CRO_AVISADOCFP, CRO_AVISADOFDP) "
                        + " values "
                        + "(?,?,?,?,?,NULL,?,?,?,?,NULL,NULL,NULL,NULL,?,?,0,0) "
                        ;
                LOG.debug("sql = " + query);
                ps = con.prepareStatement(query);
                LOG.debug("param = "
                        + (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "")
                        + "," + (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0)
                        + "," + numExpediente
                        + "," + codigoTramite
                        + "," + dateFormatddMMyyyy.format(new Date())
                        + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                        + "," + unidadOrganicaExpediente
                        + "," + codOrganizacon
                        + "," + (ocurrenciaTramite)
                        + "," + observaciones
                        + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                );
                int counter = 1;
                    ps.setString(counter++, (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : ""));
                ps.setInt(counter++, (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0));
                    ps.setString(counter++, numExpediente);
                    ps.setInt(counter++, codigoTramite);
                ps.setDate(counter++, new java.sql.Date(new Date().getTime()));
                ps.setInt(counter++, meLanbide43JobsTareaEje.getCodigoUsuario());
                ps.setInt(counter++, unidadOrganicaExpediente);
                ps.setInt(counter++, codOrganizacon);
                    ps.setInt(counter++, ocurrenciaTramite);
                ps.setString(counter++, observaciones);
                ps.setInt(counter++, meLanbide43JobsTareaEje.getCodigoUsuario());
                    if (ps.executeUpdate() > 0) {
                    query = "INSERT INTO LIST_TRAM_ORIG(EJERCICIO, COD_PRO, COD_MUN, NUM_EXP, COD_TRA_ORIGEN, OCU_TRA_ORIGEN, COD_TRA_DESTINO, OCU_TRA_DESTINO) "
                            + " VALUES (?,?,?,?,?,?,?,?)";
                    LOG.debug("sql = " + query);
                    //ps.clearParameters();
                    ps1 = con.prepareStatement(query);
                    LOG.debug("param = "
                            + (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0)
                            + "," + (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "")
                            + "," + codOrganizacon
                            + "," + numExpediente
                            + "," + codTramOrigen
                            + "," + ocuOrigen
                            + "," + codigoTramite
                            + "," + ocurrenciaTramite
                    );
                    counter = 1;
                    ps1.setInt(counter++, (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0));
                    ps1.setString(counter++, (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : ""));
                    ps1.setInt(counter++, codOrganizacon);
                    ps1.setString(counter++, numExpediente);
                    ps1.setInt(counter++, codTramOrigen);
                    ps1.setInt(counter++, ocuOrigen);
                    ps1.setInt(counter++, codigoTramite);
                    ps1.setInt(counter++, ocurrenciaTramite);
                    if (ps1.executeUpdate() > 0) {
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.TRUE);
                    }else{
                        respuesta.setCodigoRespuesta("3");
                        respuesta.setDescripcionRespuesta("No se ha podido realizar Insercion en la tabla de Historial de Tramites (LIST_TRAM_ORIG), de acuerdo a los parametros recibidos : "
                                + " " + codOrganizacon
                                + " " + numExpediente
                                + "," + codTramOrigen
                                + "," + ocuOrigen
                                + " " + codigoTramite
                                + " " + observaciones
                                + " " + meLanbide43JobsTareaEje.getCodigoUsuario()
                                + " " + permitirCrearSiExisteTramCerrado
                        );
                        LOG.error(respuesta.getCodigoDescripcionErrorAsString());
                    }
                } else {
                    respuesta.setCodigoRespuesta("2");
                    respuesta.setDescripcionRespuesta("No se ha realizado ninguna Insercion del Tramite, de acuerdo a los parametros recibidos : "
                            + (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "")
                            + "," + (datosExpediente != null && datosExpediente.length == 3 ? Integer.valueOf(datosExpediente[0]) : 0)
                            + "," + numExpediente
                            + "," + codTramOrigen
                            + "," + ocuOrigen
                            + "," + codigoTramite
                            + "," + dateFormatddMMyyyy.format(new Date())
                            + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                            + "," + unidadOrganicaExpediente
                            + "," + codOrganizacon
                            + "," + (ocurrenciaTramite)
                            + "," + observaciones
                            + "," + meLanbide43JobsTareaEje.getCodigoUsuario()
                            + ", " + permitirCrearSiExisteTramCerrado
                    );
                    LOG.error(respuesta.getCodigoDescripcionErrorAsString());
                }
                if (respuesta.isRespuestaOKBoolean()) {
                    // Registramos en el Hisotirico la Operacion
                    GeneralValueObject infoTramiteGVO = new GeneralValueObject();
                    infoTramiteGVO.setAtributo("codMunicipio", String.valueOf(codOrganizacon));
                    infoTramiteGVO.setAtributo("ejercicio", datosExpediente[0]);
                    infoTramiteGVO.setAtributo("usuario", String.valueOf(meLanbide43JobsTareaEje.getCodigoUsuario()));
                    infoTramiteGVO.setAtributo("codProcedimiento", datosExpediente[1]);
                    infoTramiteGVO.setAtributo("numero", numExpediente);
                    infoTramiteGVO.setAtributo("codTramite", String.valueOf(codigoTramite));
                    String datosDescTramite = (tramite.getE_TML() != null ? tramite.getTRA_COU() + " " + tramite.getE_TML().getTML_VALOR() : "");
                    infoTramiteGVO.setAtributo("nomTramite", datosDescTramite);
                    infoTramiteGVO.setAtributo("ocurrTramite", String.valueOf(ocurrenciaTramite));
                    infoTramiteGVO.setAtributo("nombreUsuario", meLanbide43JobsTareaEje.getNombreUsuario());
                    infoTramiteGVO.setAtributo("fechaInicioTramite", dateFormatddMMyyyy.format(new Date()));
                    try {
                        OperacionesExpedienteDAO.getInstance().registrarIniciarTramite(infoTramiteGVO, false, con);
                    } catch (TechnicalException e) {
                        LOG.error("TechnicalException Al parsear alguno de los parametros recibidos - Cerrar Tramite - Registrando Operacion Historico :  " + e.getMessage(), e);
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-5", "NumberFormatException - Al parsear alguno de los parametros recibidos  - Cerra Tramite : " + e.getMessage());
                    } catch (Exception e) {
                        LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - Cerrar Tramite - Registrando Operacion Historico :  " + e.getMessage(), e);
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-4", "NumberFormatException - Al parsear alguno de los parametros recibidos  - Cerra Tramite : " + e.getMessage());
                    }
                }
            } else {
                LOG.error(" 1 - Error al validar el alta de un tramite. "
                    + (respuesta!=null ? respuesta.getCodigoDescripcionErrorAsString(): ""));
                return respuesta;
            }
        }catch (NumberFormatException ne){
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - Abrir Tramite :  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-3","NumberFormatException - Al parsear alguno de los parametros recibidos  - Abrir Tramite : " + ne.getMessage());
        } catch (SQLException sqle){
            LOG.error("SQLException al Abrir un Tramite" + sqle.getMessage(), sqle);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2","SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al Abrir un Tramite" + e.getMessage(), e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1","Error Generico: " + e.getMessage());
        }finally{
            try {
                if(ps!=null && !ps.isClosed())
                    ps.close();
                if(ps1!=null && !ps1.isClosed())
                    ps1.close();
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + + e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            }catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }

    private MeLanbide43GestionTramitesExpedienteRespuesta existeTramiteEnExpediente(int codOrganizacon, String numExpediente, int codigoTramite, int ocurrenciaTramite, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        try {
            String query ="SELECT COUNT(1) EXISTE"
                    + " FROM E_CRO "
                    + " WHERE "
                    + " CRO_MUN=? "
                    + " AND CRO_NUM=? "
                    + " AND CRO_TRA=? "
                    + " AND CRO_OCU" + (ocurrenciaTramite > 0 ? "=" : ">=") + " ? ";
            LOG.debug("sql = " + query);
            ps = con.prepareStatement(query);
            LOG.debug("param = "
                    +  codOrganizacon
                    + "," + numExpediente
                    + "," + codigoTramite
                    + "," + ocurrenciaTramite
            );
            int counter = 1;
            ps.setInt(counter++, codOrganizacon);
            ps.setString(counter++, numExpediente);
            ps.setInt(counter++, codigoTramite);
            ps.setInt(counter++, ocurrenciaTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("EXISTE") > 0) {
                    respuesta.setRespuestaOKBoolean(Boolean.TRUE);
                } else {
                    respuesta.setRespuestaOKBoolean(Boolean.FALSE);
                    respuesta.setCodigoRespuesta("1");
                    respuesta.setDescripcionRespuesta("Tramite  " + codigoTramite + " no dado de alta para el expediente " + numExpediente);
                }
            }
        } catch (NumberFormatException ne) {
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - existeTramiteEnExpediente:  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-3", "NumberFormatException - Al parsear alguno de los parametros recibidos  - existeTramiteEnExpediente : " + ne.getMessage());
        } catch (SQLException sqle) {
            LOG.error("SQLException al existeTramiteEnExpediente " + sqle.getMessage(), sqle);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2", "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al existeTramiteEnExpediente: " + e.getMessage(), e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1", "Error Generico: " + e.getMessage());
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if(rs!=null){
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }

    private MeLanbide43GestionTramitesExpedienteRespuesta estaTramiteAbierto(int codOrganizacon, String numExpediente, int codigoTramite, int ocurrenciaTramite, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        try {
            String query = "SELECT COUNT(1) EXISTE"
                    + " FROM E_CRO "
                    + " WHERE "
                    + " CRO_MUN=? "
                    + " AND CRO_NUM=? "
                    + " AND CRO_TRA=? "
                    + " AND CRO_OCU" + (ocurrenciaTramite > 0 ? "=" : ">=") + " ? "
                    + " AND CRO_FEF IS NULL "
                    ;
            LOG.debug("sql = " + query);
            ps = con.prepareStatement(query);
            LOG.debug("param = "
                    + codOrganizacon
                    + "," + numExpediente
                    + "," + codigoTramite
                    + "," + ocurrenciaTramite
            );
            int counter = 1;
            ps.setInt(counter++, codOrganizacon);
            ps.setString(counter++, numExpediente);
            ps.setInt(counter++, codigoTramite);
            ps.setInt(counter++, ocurrenciaTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("EXISTE") > 0) {
                    respuesta.setRespuestaOKBoolean(Boolean.TRUE);
                } else {
                    respuesta.setRespuestaOKBoolean(Boolean.FALSE);
                    respuesta.setCodigoRespuesta("1");
                    respuesta.setDescripcionRespuesta("Tramite  " + codigoTramite + " no se encuentra Abierto en el expediente " + numExpediente);
                }
            }
        } catch (NumberFormatException ne) {
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - estaTramiteAbierto:  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-3", "NumberFormatException - Al parsear alguno de los parametros recibidos  - estaTramiteAbierto : " + ne.getMessage());
        } catch (SQLException sqle) {
            LOG.error("SQLException al estaTramiteAbierto " + sqle.getMessage(), sqle);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2", "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al estaTramiteAbierto: " + e.getMessage(), e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1", "Error Generico: " + e.getMessage());
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }

    private String getUnidadOrganicaExpediente(String numExpediente, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String respuesta = "";
        try {
            String query = "SELECT EXP_UOR "
                    + " FROM E_EXP "
                    + " WHERE "
                    + " EXP_NUM=? "
                    ;
            LOG.debug("sql = " + query);
            ps = con.prepareStatement(query);
            LOG.debug("param = "
                    + numExpediente
            );
            int counter = 1;
            ps.setString(counter++, numExpediente);
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta=String.valueOf(rs.getInt("EXP_UOR"));
            }
        } catch (NumberFormatException ne) {
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - getUnidadOrganicaExpediente:  " + ne.getMessage(), ne);
        } catch (SQLException sqle) {
            LOG.error("SQLException al getUnidadOrganicaExpediente " + sqle.getMessage(), sqle);
        } catch (Exception e) {
            LOG.error("Error Generico al getUnidadOrganicaExpediente: " + e.getMessage(), e);
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    private int getMaximaOcurrenciaTramiteEnExpediente(int codOrganizacon, String numExpediente, int codigoTramite, Connection con){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int respuesta = 0;
        try {
            String query = "SELECT MAX(NVL(CRO_OCU,0)) OCURRENCIA_MAX"
                    + " FROM E_CRO "
                    + " WHERE "
                    + " CRO_MUN=? "
                    + " AND CRO_NUM=? "
                    + " AND CRO_TRA=? "
                    ;
            LOG.debug("sql = " + query);
            ps = con.prepareStatement(query);
            LOG.debug("param = "
                    + codOrganizacon
                    + "," + numExpediente
                    + "," + codigoTramite
            );
            int counter = 1;
            ps.setInt(counter++, codOrganizacon);
            ps.setString(counter++, numExpediente);
            ps.setInt(counter++, codOrganizacon);
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta = rs.getInt("OCURRENCIA_MAX");
            }
        } catch (NumberFormatException ne) {
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - getMaximaOcurrenciaTramiteEnExpediente:  " + ne.getMessage(), ne);
        } catch (SQLException sqle) {
            LOG.error("SQLException al getMaximaOcurrenciaTramiteEnExpediente " + sqle.getMessage(), sqle);
        } catch (Exception e) {
            LOG.error("Error Generico al getMaximaOcurrenciaTramiteEnExpediente: " + e.getMessage(), e);
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta; 
    }
    
    private MeLanbide43GestionTramitesExpedienteRespuesta esCodigoTramiteValidoProcedimiento(int codOrganizacon, String codigoProcedimiento, int codigoTramite, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        try {
            String query = "SELECT COUNT(1) EXISTE"
                    + " FROM E_TRA "
                    + " WHERE "
                    + " TRA_MUN=? "
                    + " AND TRA_PRO=? "
                    + " AND TRA_COD=? "
                    ;
            LOG.debug("sql = " + query);
            ps = con.prepareStatement(query);
            LOG.debug("param = "
                    + codOrganizacon
                    + "," + codigoProcedimiento
                    + "," + codigoTramite
            );
            int counter = 1;
            ps.setInt(counter++, codOrganizacon);
            ps.setString(counter++, codigoProcedimiento);
            ps.setInt(counter++, codigoTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                if(rs.getInt("EXISTE")>0)
                    respuesta.setRespuestaOKBoolean(Boolean.TRUE);
                else{
                    respuesta.setRespuestaOKBoolean(Boolean.FALSE);
                    respuesta.setCodigoRespuesta("1");
                    respuesta.setDescripcionRespuesta("Tramite no valido. No existe el tramite "+codigoTramite+ " definido para el procedimiento " + codigoProcedimiento);
                }
            }
        } catch (NumberFormatException ne) {
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - esCodigoTramiteValidoProcedimiento:  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-3", "NumberFormatException - Al parsear alguno de los parametros recibidos  - esCodigoTramiteValidoProcedimiento : " + ne.getMessage());
        } catch (SQLException sqle) {
            LOG.error("SQLException al esCodigoTramiteValidoProcedimiento " + sqle.getMessage(), sqle);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2", "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al esCodigoTramiteValidoProcedimiento: " + e.getMessage(), e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1", "Error Generico: " + e.getMessage());
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    /***
     * Comprueba si un expediente existe en el sistema y si es valido para ejecutar operaciones sobre el : No este anulado.
     * @param codOrganizacon
     * @param codigoProcedimiento
     * @param codigoTramite
     * @param con
     * @return MeLanbide43GestionTramitesExpedienteRespuesta setRespuestaOKBoolean True si es valido. False y codigo y descripcion del error enc aso contrario
     */
    public MeLanbide43GestionTramitesExpedienteRespuesta esExpedienteValido(int codOrganizacon, String numeroExpediente, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        try {
            String query = "SELECT EXP_NUM,EXP_EST "
                    + " FROM E_EXP "
                    + " WHERE "
                    + " EXP_MUN=? "
                    + " AND EXP_NUM=? "
                    ;
            LOG.debug("sql = " + query);
            ps = con.prepareStatement(query);
            LOG.debug("param = "
                    + codOrganizacon
                    + "," + numeroExpediente
            );
            int counter = 1;
            ps.setInt(counter++, codOrganizacon);
            ps.setString(counter++, numeroExpediente);
            rs = ps.executeQuery();
            boolean existeExp = false;
            while (rs.next()) {
                existeExp=Boolean.TRUE;
                // Estado 9 Anulado
                if(rs.getInt("EXP_EST")!=9){
                    respuesta.setRespuestaOKBoolean(existeExp);
                }else{
                    respuesta.setRespuestaOKBoolean(Boolean.FALSE);
                    respuesta.setCodigoRespuesta("1");
                    respuesta.setDescripcionRespuesta("Expediente "+ numeroExpediente + " en estado " + rs.getInt("EXP_EST") +" (anulado). No se puede procesar.");
                }
            }
            if(!existeExp){
                respuesta.setRespuestaOKBoolean(Boolean.FALSE);
                respuesta.setCodigoRespuesta("2");
                respuesta.setDescripcionRespuesta("Expediente " + numeroExpediente + " no validado, no existe. No se puede procesar.");
            }
        } catch (NumberFormatException ne) {
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - esCodigoTramiteValidoProcedimiento:  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-3", "NumberFormatException - Al parsear alguno de los parametros recibidos  - esCodigoTramiteValidoProcedimiento : " + ne.getMessage());
        } catch (SQLException sqle) {
            LOG.error("SQLException al esCodigoTramiteValidoProcedimiento " + sqle.getMessage(), sqle);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2", "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al esCodigoTramiteValidoProcedimiento: " + e.getMessage(), e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1", "Error Generico: " + e.getMessage());
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }

    /***
     * Metodo que valida si se puede abrir un tramite en un expediente. Exista el tramite en el procedimiento,  No tenga un tramite abierto, Si existe y estan cerrados y se permita crear uno nuevo segun permitirCrearSiExisteCerrado
     * @param codOrganizacon
     * @param numExpediente
     * @param codigoTramite
     * @param permitirCrearSiExisteCerrado Valida que si existe el tramite para el expedinete y esta cerrado, se pueda crear una nueva ocurrencia.
     * @param con
     * @return MeLanbide43GestionTramitesExpedienteRespuesta setRespuestaOKBoolean True si es valido. False y codigo y descripcion del error enc aso contrario
     */
    private MeLanbide43GestionTramitesExpedienteRespuesta validarAbrirTramiteExpediente(int codOrganizacon, String numExpediente, int codigoTramite, boolean permitirCrearSiExisteCerrado, Connection con) {
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        try {
            String[] datosExpediente = (numExpediente != null && !numExpediente.isEmpty() ? numExpediente.split(ConstantesMeLanbide43.BARRA_SEPARADORA) : null);
            String codProcedimiento = (datosExpediente != null && datosExpediente.length == 3 ? datosExpediente[1] : "");
            respuesta=this.esCodigoTramiteValidoProcedimiento(codOrganizacon, codProcedimiento, codigoTramite,con);
            if(respuesta!=null && respuesta.isRespuestaOKBoolean()){
                respuesta=this.existeTramiteEnExpediente(codOrganizacon, numExpediente, codigoTramite, 0, con);
                if(respuesta!=null && respuesta.isRespuestaOKBoolean()){
                    // Existe el tramite, hay que comprobar que no este abierto y si esta cerrado que este habilitado la multiocurrencia
                    respuesta=this.estaTramiteAbierto(codOrganizacon, numExpediente, codigoTramite, 0, con);
                    if(respuesta!=null && !respuesta.isRespuestaOKBoolean() && permitirCrearSiExisteCerrado){
                        // Existe y non esta abierto y podemos tener multiocurrencia devolevmos OK de otra manera No validamos
                        respuesta=new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.TRUE);
                        respuesta.setCodigoRespuesta("1");
                        respuesta.setCodigoRespuesta("Tramite validado, existe, pero esta cerrado. Hemos recibido true para crear multiocurrencia.");
                    }else{
                        respuesta=new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.FALSE);
                        respuesta.setCodigoRespuesta("2");
                        respuesta.setCodigoRespuesta("Tramite No Validado. Esta abierto. O esta cerrado y false para crear multiocurrencias.");
                    }
                }else{
                    //Retornamos OK
                    respuesta=new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.TRUE);
                }
                // Devolvemos los datos faltantes del tramite en caso de ser validado
                if(respuesta.isRespuestaOKBoolean()){
                    int maximaOcurrenciaActualTramite=this.getMaximaOcurrenciaTramiteEnExpediente(codOrganizacon, numExpediente, codigoTramite, con);
                    String unidadOrganicaExpediente=this.getUnidadOrganicaExpediente(numExpediente, con);
                    HashMap<String,String> datoOcurrencia= new HashMap<String, String>();
                    datoOcurrencia.put("ocurrenciaTramite", String.valueOf((maximaOcurrenciaActualTramite+1)));
                    datoOcurrencia.put("unidadOrganicaExpediente", unidadOrganicaExpediente);
                    respuesta.setRespuestaDatosAdicionales(datoOcurrencia);
                }
            }else{
                LOG.error("Tramite no valido en el procedimiento." + (respuesta!=null?respuesta.getCodigoDescripcionErrorAsString():""));
            }
        } catch (NumberFormatException ne) {
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - Cerrar Tramite :  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2", "NumberFormatException - Al parsear alguno de los parametros recibidos  - Cerra Tramite : " + ne.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al Cerrar un Tramite" + e.getMessage(), e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1", "Error Generico: " + e.getMessage());
        }
        return respuesta;
    }

    /***
     * Metodo que valida si se puede cerrar un tramite en un expediente. Valida que codigo del tramite sea valido, Que exista el tramite en el expediente y que esta abierto. 
     * @param codOrganizacon
     * @param numExpediente
     * @param codigoTramite
     * @param ocurrenciaTramite
     * @return MeLanbide43GestionTramitesExpedienteRespuesta setRespuestaOKBoolean True si es valido. False y codigo y descripcion del error enc aso contrario
     */
    private MeLanbide43GestionTramitesExpedienteRespuesta validarCerrarTramiteExpediente(int codOrganizacon, String numExpediente, int codigoTramite, int ocurrenciaTramite, Connection con) {
        MeLanbide43GestionTramitesExpedienteRespuesta respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta();
        try {
            String[] datosExpediente = (numExpediente != null && !numExpediente.isEmpty() ? numExpediente.split(ConstantesMeLanbide43.BARRA_SEPARADORA) : null);
            String codProcedimiento = (datosExpediente!=null && datosExpediente.length==3 ? datosExpediente[1]:"");
            respuesta = this.esCodigoTramiteValidoProcedimiento(codOrganizacon, codProcedimiento, codigoTramite, con);
            if (respuesta != null && respuesta.isRespuestaOKBoolean()) {
                respuesta = this.existeTramiteEnExpediente(codOrganizacon, numExpediente, codigoTramite, ocurrenciaTramite, con);
                if (respuesta != null && respuesta.isRespuestaOKBoolean()) {
                    // Existe el tramite, hay que comprobar que si esta abierto.
                    respuesta = this.estaTramiteAbierto(codOrganizacon, numExpediente, codigoTramite, ocurrenciaTramite, con);
                    if (respuesta != null && respuesta.isRespuestaOKBoolean()) {
                        // Existe y esta abierto
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.TRUE);
                    } else {
                        respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.FALSE);
                        respuesta.setCodigoRespuesta("2");
                        respuesta.setCodigoRespuesta("Tramite No Validado. Esta cerrado.");
                    }
                } else {
                    respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta(Boolean.FALSE);
                    respuesta.setCodigoRespuesta("1");
                    respuesta.setCodigoRespuesta("Tramite No Validado. NO existe tramite en el Expediente.");
                    LOG.error(respuesta.getCodigoDescripcionErrorAsString());
                }
            } else {
                LOG.error("Tramite no valido en el procedimiento." + (respuesta != null ? respuesta.getCodigoDescripcionErrorAsString() : ""));
            }
        } catch (NumberFormatException ne) {
            LOG.error("NumberFormatException Al parsear alguno de los parametros recibidos - Cerrar Tramite :  " + ne.getMessage(), ne);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-2", "NumberFormatException - Al parsear alguno de los parametros recibidos  - Cerra Tramite : " + ne.getMessage());
        } catch (Exception e) {
            LOG.error("Error Generico al Cerrar un Tramite" + e.getMessage(), e);
            respuesta = new MeLanbide43GestionTramitesExpedienteRespuesta("-1", "Error Generico: " + e.getMessage());
        }
        return respuesta;
    }
    
    public int getCodInternoTramiteProcedimientoByCodVisible(int codOrganizacon, String codigoProcedimiento, String codigoVisibleTramite, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int respuesta = 0;
        String mensajeGestionError="";
        try {
            String query = "SELECT TRA_COD "
                    + " FROM E_TRA "
                    + " WHERE "
                    + " TRA_MUN=? "
                    + " AND TRA_PRO=? "
                    + " AND TRA_COU=? ";
            LOG.debug("sql = " + query);
            ps = con.prepareStatement(query);
            LOG.debug("param = "
                    + codOrganizacon
                    + "," + codigoProcedimiento
                    + "," + codigoVisibleTramite
            );
            int counter = 1;
            ps.setInt(counter++, codOrganizacon);
            ps.setString(counter++, codigoProcedimiento);
            ps.setInt(counter++, (codigoVisibleTramite!=null && !codigoVisibleTramite.isEmpty() ? Integer.valueOf(codigoVisibleTramite) : 0));
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta=rs.getInt("TRA_COD");
            }
        } catch (NumberFormatException ne) {
            mensajeGestionError="NumberFormatException Al parsear alguno de los parametros recibidos - getCodInternoTramiteProcedimientoByCodVisible:  " + ne.getMessage();
            LOG.error(mensajeGestionError, ne);
            throw new Exception(mensajeGestionError);
        } catch (SQLException sqle) {
            mensajeGestionError="SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage();
            LOG.error(mensajeGestionError, sqle);
            throw new Exception(mensajeGestionError);
        } catch (Exception e) {
            LOG.error("Error Generico al getCodInternoTramiteProcedimientoByCodVisible: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
    public MeLanbide43GestionTramitesExpedienteETraVO getTramiteExpedienteETraVOByCodVisible(int codOrganizacon, String codigoProcedimiento, String codigoVisibleTramite, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        MeLanbide43GestionTramitesExpedienteETraVO respuesta = new MeLanbide43GestionTramitesExpedienteETraVO();
        String mensajeGestionError = "";
        try {
            String query = "SELECT * "
                    + " FROM E_TRA "
                    + " INNER JOIN E_TML ON TML_PRO=TRA_PRO AND TML_TRA=TRA_COD "
                    + " WHERE "
                    + " TRA_MUN=? "
                    + " AND TRA_PRO=? "
                    + " AND TRA_COU=? ";
            LOG.debug("sql = " + query);
            ps = con.prepareStatement(query);
            LOG.debug("param = "
                    + codOrganizacon
                    + "," + codigoProcedimiento
                    + "," + codigoVisibleTramite
            );
            int counter = 1;
            ps.setInt(counter++, codOrganizacon);
            ps.setString(counter++, codigoProcedimiento);
            ps.setInt(counter++, (codigoVisibleTramite != null && !codigoVisibleTramite.isEmpty() ? Integer.valueOf(codigoVisibleTramite) : 0));
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta.setTRA_PRO(rs.getString("TRA_PRO"));
                respuesta.setTRA_COD(rs.getInt("TRA_COD"));
                respuesta.setTRA_PLZ(rs.getInt("TRA_PLZ"));
                respuesta.setTRA_UND(rs.getString("TRA_UND"));
                respuesta.setTRA_OCU(rs.getInt("TRA_OCU"));
                respuesta.setTRA_MUN(rs.getInt("TRA_MUN"));
                respuesta.setTRA_VIS(rs.getInt("TRA_VIS"));
                respuesta.setTRA_UIN(rs.getInt("TRA_UIN"));
                respuesta.setTRA_UTR(rs.getInt("TRA_UTR"));
                respuesta.setTRA_CLS(rs.getInt("TRA_CLS"));
                respuesta.setTRA_ARE(rs.getInt("TRA_ARE"));
                respuesta.setTRA_FBA(rs.getDate("TRA_FBA"));
                respuesta.setTRA_COU(rs.getInt("TRA_COU"));
                respuesta.setTRA_PRE(rs.getInt("TRA_PRE"));
                respuesta.setTRA_INS(rs.getString("TRA_INS"));
                respuesta.setTRA_WS_COD(rs.getString("TRA_WS_COD"));
                respuesta.setTRA_WS_OB(rs.getString("TRA_WS_OB"));
                respuesta.setTRA_UTI(rs.getString("TRA_UTI"));
                respuesta.setTRA_UTF(rs.getString("TRA_UTF"));
                respuesta.setTRA_USI(rs.getString("TRA_USI"));
                respuesta.setTRA_USF(rs.getString("TRA_USF"));
                respuesta.setTRA_INI(rs.getString("TRA_INI"));
                respuesta.setTRA_INF(rs.getString("TRA_INF"));
                respuesta.setTRA_WST_COD(rs.getString("TRA_WST_COD"));
                respuesta.setTRA_WST_OB(rs.getString("TRA_WST_OB"));
                respuesta.setTRA_WSR_COD(rs.getString("TRA_WSR_COD"));
                respuesta.setTRA_WSR_OB(rs.getString("TRA_WSR_OB"));
                respuesta.setTRA_PRR(rs.getString("TRA_PRR"));
                respuesta.setTRA_CAR(rs.getInt("TRA_CAR"));
                respuesta.setTRA_FIN(rs.getInt("TRA_FIN"));
                respuesta.setTRA_GENERARPLZ(rs.getInt("TRA_GENERARPLZ"));
                respuesta.setTRA_NOTCERCAFP(rs.getInt("TRA_NOTCERCAFP"));
                respuesta.setTRA_NOTFUERADP(rs.getInt("TRA_NOTFUERADP"));
                respuesta.setTRA_TIPNOTCFP(rs.getInt("TRA_TIPNOTCFP"));
                respuesta.setTRA_TIPNOTFDP(rs.getInt("TRA_TIPNOTFDP"));
                respuesta.setTRA_NOTIFICACION_ELECTRONICA(rs.getString("TRA_NOTIFICACION_ELECTRONICA"));
                respuesta.setTRA_COD_TIPO_NOTIFICACION(rs.getInt("TRA_COD_TIPO_NOTIFICACION"));
                respuesta.setTRA_TIPO_USUARIO_FIRMA(rs.getString("TRA_TIPO_USUARIO_FIRMA"));
                respuesta.setTRA_OTRO_COD_USUARIO_FIRMA(rs.getInt("TRA_OTRO_COD_USUARIO_FIRMA"));
                respuesta.setTRA_TRAM_EXT_PLUGIN(rs.getString("TRA_TRAM_EXT_PLUGIN"));
                respuesta.setTRA_TRAM_ID_ENLACE_PLUGIN(rs.getString("TRA_TRAM_ID_ENLACE_PLUGIN"));
                respuesta.setTRA_TRAM_EXT_URL(rs.getString("TRA_TRAM_EXT_URL"));
                respuesta.setTRA_TRAM_EXT_IMPLCLASS(rs.getString("TRA_TRAM_EXT_IMPLCLASS"));
                respuesta.setTRA_NOTIF_ELECT_OBLIG(rs.getInt("TRA_NOTIF_ELECT_OBLIG"));
                respuesta.setTRA_NOTIF_FIRMA_CERT_ORG(rs.getInt("TRA_NOTIF_FIRMA_CERT_ORG"));
                respuesta.setCOD_DEPTO_NOTIFICACION(rs.getString("COD_DEPTO_NOTIFICACION"));
                respuesta.setTRA_NOTIF_UITI(rs.getString("TRA_NOTIF_UITI"));
                respuesta.setTRA_NOTIF_UITF(rs.getString("TRA_NOTIF_UITF"));
                respuesta.setTRA_NOTIF_UIEI(rs.getString("TRA_NOTIF_UIEI"));
                respuesta.setTRA_NOTIF_UIEF(rs.getString("TRA_NOTIF_UIEF"));
                respuesta.setTRA_NOTIFICADO(rs.getInt("TRA_NOTIFICADO"));
                respuesta.setTRA_NOTIF_USUEXP_FINPLAZO(rs.getString("TRA_NOTIF_USUEXP_FINPLAZO"));
                respuesta.setTRA_NOTIF_USUTRA_FINPLAZO(rs.getString("TRA_NOTIF_USUTRA_FINPLAZO"));
                respuesta.setTRA_NOTIF_UOR_FINPLAZO(rs.getString("TRA_NOTIF_UOR_FINPLAZO"));
                //Completmaos Datos de TML Literal Nombre
                Tml tml = new Tml();
                tml.setTRA_COD(respuesta.getTRA_COD());
                tml.setTRA_COU(respuesta.getTRA_COU());
                tml.setTRA_PRO(respuesta.getTRA_PRO());
                tml.setTML_MUN(rs.getInt("TML_MUN"));
                tml.setTML_PRO(rs.getString("TML_PRO"));
                tml.setTML_TRA(rs.getInt("TML_TRA"));
                tml.setTML_CMP(rs.getString("TML_CMP"));
                tml.setTML_LENG(rs.getString("TML_LENG"));
                tml.setTML_VALOR(rs.getString("TML_VALOR"));
                respuesta.setE_TML(tml);
            }
        } catch (NumberFormatException ne) {
            mensajeGestionError = "NumberFormatException Al parsear alguno de los parametros recibidos - getTramiteExpedienteETraVOByCodVisible:  " + ne.getMessage();
            LOG.error(mensajeGestionError, ne);
            throw new Exception(mensajeGestionError);
        } catch (SQLException sqle) {
            mensajeGestionError = "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage();
            LOG.error(mensajeGestionError, sqle);
            throw new Exception(mensajeGestionError);
        } catch (Exception e) {
            LOG.error("Error Generico al getTramiteExpedienteETraVOByCodVisible: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
}
