package es.altia.flexia.integracion.moduloexterno.melanbide06;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailException;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.flexia.integracion.moduloexterno.melanbide06.manager.GestionAvisosManager;
import es.altia.flexia.integracion.moduloexterno.melanbide06.manager.MeLanbide06Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06Constantes;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.GestionAvisosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoParamAdicionales;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.*;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author david.caamano
 * @version 17/01/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 17/01/2013 * Edición inicial</li>
 * </ol> 
 */
public class MeLanbide06 extends ModuloIntegracionExterno {
    
    //Logger
    private static Logger log = Logger.getLogger(MeLanbide06.class);
    
    //Codigos de error devueltos por las operaciones
    private final static String TODO_CORRECTO = "0";
    private final static String ERROR = "1";
    private final static String ERROR_RECUPERANDO_COD_UOR = "2";
    private final static String ERROR_RECUPERANDO_UOR = "3";
    private final static String ERROR_RECUPERANDO_ASUNTO_EMAIL = "4";
    private final static String ERROR_RECUPERANDO_TEXTO_EMAIL = "5";
    private final static String ERROR_ENVIANDO_EMAIL = "6";
    private final static String ERROR_SERVICIO_MAIL_INACTIVO = "7";
    private final static String ERROR_RECUPERANDO_USUARIOS_UOR = "8";
    private final static String ERROR_RECUPERANDO_DATOS_EXPEDIENTE = "9";
    private final static String ERROR_RECUPERANDO_DATOS_USUARIO = "10";
    private final static String ERROR_RECUPERANDO_EMAIL_UOR = "11";
    private final static String ERROR_RECUPERANDO_EMAIL_USUARIO = "12";
    

    public String envioCorreoUsuarioIniciaExpediente (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, ModuloIntegracionExternoParamAdicionales parametros){
        log.error("envioCorreoUsuarioIniciaExpediente() : BEGIN - Expediente: "+numExpediente);
        String resultado = TODO_CORRECTO;
        log.info("origenLlamada: "+parametros.getOrigenLlamada());
        if ("SW".equals(parametros.getOrigenLlamada())){        
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            String ejercicio = new String();
            String codProcedimiento = new String();
            String numExp = new String();

            String asuntoEmail = new String();
            String textoEmail = new String();
            String emailUsuario = new String();
            String descripcionUorIniciaExpediente = new String();
            String interesado = new String();
            String descripcionTramite = "";
            HashMap<String, String> camposSustituir = new HashMap<String, String>();
            MeLanbide06Manager manager =null;
            try{
                String[] propsExpediente = numExpediente.split(MeLanbide06Constantes.BARRA);
                ejercicio = propsExpediente[0];
                codProcedimiento = propsExpediente[1];
                numExp = propsExpediente[2];

                Integer idUsuarioInicio = new Integer(0);
                String codUorVisIniciaExpediente = new String();
                if(log.isDebugEnabled()) log.debug("Recuperamos los datos del expediente");
                SalidaIntegracionVO salida = new SalidaIntegracionVO();
                salida = el.getExpediente(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
                if(salida.getStatus() == 0){
                    if(log.isDebugEnabled()) log.debug("Recuperamos los datos del usuario que inicia el expediente");
                    ExpedienteModuloIntegracionVO expediente = salida.getExpediente();
                    idUsuarioInicio = expediente.getCodUsuarioIniciaExpediente();
                    codUorVisIniciaExpediente = expediente.getCodigoUorVisibleInicioExpediente();
                    if(log.isDebugEnabled()) log.debug("idUsuarioInicio = " + idUsuarioInicio);
                    if(log.isDebugEnabled()) log.debug("codUorVisIniciaExpediente = " + codUorVisIniciaExpediente);
                    //se obtiene la descripcion del tramite
                    AdaptadorSQLBD adaptador = getAdaptSQLBD(String.valueOf(codOrganizacion));
                    log.debug("adaptador:" +adaptador.toString());
                    log.debug("codTramite:" +codTramite);
                    manager= MeLanbide06Manager.getInstance();
                    log.debug("manager:"+manager);
                    descripcionTramite = manager.getDescripcionTramite(codTramite, codProcedimiento, adaptador);
                    log.error("COD PROCEDIMIENTO: "+codProcedimiento+", COD TRAMITE: "+codTramite+" , DESCRIPCION: "+descripcionTramite);
                    

                }else{
                    log.error("Se ha producido un error recuperando los datos del expediente " + salida.getDescStatus());
                    resultado = ERROR_RECUPERANDO_DATOS_EXPEDIENTE;
                }//if(salida.getStatus() == 0)

                if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                    if(log.isDebugEnabled()) log.debug("Recuperamos los datos del usuario");
                    salida = new SalidaIntegracionVO();
                    salida = el.getUsuario(String.valueOf(codOrganizacion), String.valueOf(idUsuarioInicio));
                    if(salida.getStatus() == 0){                        
                        UsuarioModuloIntegracionVO usuario = salida.getUsuarioModuloIntegracionVO();
                        emailUsuario = usuario.getUsuEmail();
                        log.error("emailUsuario = " + emailUsuario);                        
                    }else{
                        log.error("Se ha producido un error recuperando los datos del usuario " + salida.getDescStatus());
                        resultado = ERROR_RECUPERANDO_DATOS_USUARIO;
                    }//if(salida.getStatus() == 0)
                }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))

                if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                    if(log.isDebugEnabled()) log.debug("Recuperamos el interesado");
                    interesado = MeLanbide06Manager.getInstance().getInteresado(numExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    log.error("Interesado" + interesado);
                    if(log.isDebugEnabled()) log.debug("interesado = " + interesado);
                }

                if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                    if(log.isDebugEnabled()) log.debug("Recuperamos la descripcion de la unidad de inicio");
                    descripcionUorIniciaExpediente = MeLanbide06Manager.getInstance().getDescripcionUor(codUorVisIniciaExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    log.error("DESCRIPCION UOR " + descripcionUorIniciaExpediente);
                    if(log.isDebugEnabled()) log.debug("descripcionUorIniciaExpediente = " + descripcionUorIniciaExpediente);
                }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))

                if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                    if(log.isDebugEnabled()) log.debug("Recuperamos el asunto del email");
                    try{
                        asuntoEmail = getAsuntoEmail(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_USUARIO_INICIA);
                        if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase("")){
                            log.error("La propiedad para el asunto del email es nula o esta vacia");
                            resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                        }else{
                            if(log.isDebugEnabled()) log.debug("asuntoEmail = " + asuntoEmail);
                        }//if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase(""))
                    }catch(MeLanbide06Exception ex){
                        log.error("Se ha producido un error recuperando el asunto del email " + ex.getMessage());
                        resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                    }//try-catch
                }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))

                if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                    if(log.isDebugEnabled()) log.debug("Recuperamos los codigos de los campos suplementarios que queremos sustituir en el texto");
                    String campos = new String();
                    try{
                        camposSustituir=getValoresCamposSuplementarios(codOrganizacion, codTramite,ocurrenciaTramite, codProcedimiento,ejercicio, numExpediente,MeLanbide06Constantes.OP_EMAIL_USUARIO_INICIA);

                    }catch(MeLanbide06Exception ex){
                        if(log.isDebugEnabled()) log.debug("No existen campos suplementarios para sustituir");
                    }//try-catch
                }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))

                if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                    if(log.isDebugEnabled()) log.debug("Insertamos el expediente y la descripcion del tramite en los parametros");
                    camposSustituir.put("idExpediente", numExpediente);
                    log.error("descripcionTramite : "+descripcionTramite);
                    camposSustituir.put("descTramite", descripcionTramite);
                    camposSustituir.put("interesado", interesado);
                    if(log.isDebugEnabled()) log.debug("Recuperamos el texto del email");
                    try{
                        textoEmail = getTextoEmail(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_USUARIO_INICIA
                                , camposSustituir);

                        if(textoEmail == null || textoEmail.equalsIgnoreCase("")){
                            log.error("La propiedad del texto del email es nula o esta vacia");
                            resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                        }else{
                            if(log.isDebugEnabled()) log.debug("textoEmail = " + textoEmail);
                        }//if(textoEmail == null || textoEmail.equalsIgnoreCase(""))

                        asuntoEmail = getAsuntoEmailSustCampos(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_USUARIO_INICIA, camposSustituir);

                        if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase("")){
                            log.error("La propiedad del asunto del email es nula o esta vacia");
                            resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                        }else{
                            if(log.isDebugEnabled()) log.debug("asuntoEmail = " + asuntoEmail);
                        }
                    }catch(MeLanbide06Exception ex){
                        log.error("Se ha producido un error recuperando el texto del email " + ex.getMessage());
                        resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                    }//try-catch
                }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))

                if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                    if(emailUsuario != null && !"".equalsIgnoreCase(emailUsuario)){
                        if(log.isDebugEnabled()) log.debug("Enviamos el correo");
                        MailHelper mailHelper = new MailHelper();
                        try{
                            mailHelper.sendMail(emailUsuario, asuntoEmail, textoEmail);
                        }catch(MailException ex){
                            log.error("Se ha producido un error enviando el email a la UOR " + ex.getMessage());
                            resultado = ERROR_ENVIANDO_EMAIL;
                        }catch(MailServiceNotActivedException ex){
                            log.error("El servicio de email no esta activo en la aplicacion " + ex.getMessage());
                            resultado = ERROR_SERVICIO_MAIL_INACTIVO;
                        }//try-catch
                    }else{
                        if(log.isDebugEnabled()) log.debug("El email del usuario que inicia el expediente es nulo, procedemos a comprobar"
                                + " si hay que lanzar un error");
                        Boolean lanzarErrorNoEmail = 
                            getLanzarErrorEmail(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_USUARIO_INICIA);
                        if(log.isDebugEnabled()) log.debug("lanzarErrorNoEmail = " + lanzarErrorNoEmail);
                        if(lanzarErrorNoEmail){
                            if(log.isDebugEnabled()) log.debug("Lanzamos el error");
                            resultado = ERROR_RECUPERANDO_EMAIL_USUARIO;
                        }else{
                            if(log.isDebugEnabled()) log.debug("La propiedad que indica si hay que lanzar la excepcion indica que no hay que lanzarla");
                        }//if(lanzarErrorNoEmail)
                    }//if(emailUsuario != null && !"".equalsIgnoreCase(emailUsuario))
                }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            }catch(Exception ex){
                log.error("Se ha producido un error en el proceso de envio del correo al usuario que inicia el expediente " + ex.getMessage());
                resultado = ERROR;
            }//try-catch
        }else {
            log.info("No se envía correo porque se le llama de forma presencial");
        }
        log.error("resultado = " + resultado);
        log.error("envioCorreoUsuarioIniciaExpediente() : END");
        return resultado;
    }//envioCorreoUsuarioIniciaExpediente
    
    /**
     * Envia un correo a la unidad que haya iniciado el expediente
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return 
     */
    public String envioCorreoUnidadIniciaExpediente (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente){
        if(log.isDebugEnabled()) log.debug("envioCorreoUnidadIniciaExpediente() : BEGIN");
        String resultado = TODO_CORRECTO;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        String ejercicio = new String();
        String codProcedimiento = new String();
        String numExp = new String();
        String emailUOR = new String();
        String asuntoEmail = new String();
        String textoEmail = new String();
        String descripcionUorIniciaExpediente = new String();
        HashMap<String, String> camposSustituir = new HashMap<String, String>();
        try{
            String[] propsExpediente = numExpediente.split(MeLanbide06Constantes.BARRA);
            ejercicio = propsExpediente[0];
            codProcedimiento = propsExpediente[1];
            numExp = propsExpediente[2];
            
            String uorCodVis = new String();
            if(log.isDebugEnabled()) log.debug("Recuperamos los datos del expediente");
            SalidaIntegracionVO salida = new SalidaIntegracionVO();
            salida = el.getExpediente(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
            if(salida.getStatus() == 0){
                if(log.isDebugEnabled()) log.debug("Recuperamos los datos del usuario que inicia el expediente");
                ExpedienteModuloIntegracionVO expediente = salida.getExpediente();
                uorCodVis = expediente.getCodigoUorVisibleInicioExpediente();
                if(log.isDebugEnabled()) log.debug("uorCodVis inicia expediente = " + uorCodVis);
            }else{
                log.error("Se ha producido un error recuperando los datos del expediente " + salida.getDescStatus());
                resultado = ERROR_RECUPERANDO_DATOS_EXPEDIENTE;
            }//if(salida.getStatus() == 0)
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos la unidad organica");
                salida = new SalidaIntegracionVO();
                salida = el.getUOR(String.valueOf(codOrganizacion), uorCodVis);
                if(salida.getStatus() == 0){
                    if(log.isDebugEnabled()) log.debug("Recuperamos el correo de la UOR");
                    UORModuloIntegracionVO uor = salida.getUorModuloIntegracionVO();
                    emailUOR = uor.getUorEmail();
                    if(log.isDebugEnabled()) log.debug("emailUOR = " + emailUOR);
                }else{
                    log.error("Se ha producido un error recuperando la unidad organica");
                    resultado = ERROR_RECUPERANDO_UOR;
                }//if(salida.getStatus() != 0)
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos la descripcion de la unidad de inicio");
                descripcionUorIniciaExpediente = MeLanbide06Manager.getInstance().
                        getDescripcionUor(uorCodVis, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(log.isDebugEnabled()) log.debug("descripcionUorIniciaExpediente = " + descripcionUorIniciaExpediente);
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos los codigos de los campos suplementarios que queremos sustituir en el texto");
                String campos = new String();
                try{                    
                    camposSustituir=getValoresCamposSuplementarios(codOrganizacion, codTramite,ocurrenciaTramite, codProcedimiento,ejercicio, numExpediente,MeLanbide06Constantes.OP_EMAIL_UOR_INICIA_EXPEDIENTE);
                }catch(MeLanbide06Exception ex){
                    if(log.isDebugEnabled()) log.debug("No existen campos suplementarios para sustituir");
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos el asunto del email");
                try{
                    asuntoEmail = getAsuntoEmail(codOrganizacion, codTramite, codProcedimiento, 
                            MeLanbide06Constantes.OP_EMAIL_UOR_INICIA_EXPEDIENTE);
                    if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase("")){
                        log.error("La propiedad para el asunto del email es nula o esta vacia");
                        resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                    }else{
                        if(log.isDebugEnabled()) log.debug("asuntoEmail = " + asuntoEmail);
                    }//if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase(""))
                }catch(MeLanbide06Exception ex){
                    log.error("Se ha producido un error recuperando el asunto del email " + ex.getMessage());
                    resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Insertamos el expediente y la unidad tramitadora en los parametros");
                camposSustituir.put("idExpediente", numExpediente);
                camposSustituir.put("unidadTramitadora", descripcionUorIniciaExpediente);
                if(log.isDebugEnabled()) log.debug("Recuperamos el texto del email");
                try{
                    textoEmail = getTextoEmail(codOrganizacion, codTramite, codProcedimiento, 
                            MeLanbide06Constantes.OP_EMAIL_UOR_INICIA_EXPEDIENTE, camposSustituir);
                    if(textoEmail == null || textoEmail.equalsIgnoreCase("")){
                        log.error("La propiedad del texto del email es nula o esta vacia");
                        resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                    }else{
                        if(log.isDebugEnabled()) log.debug("textoEmail = " + textoEmail);
                    }//if(textoEmail == null || textoEmail.equalsIgnoreCase(""))
                }catch(MeLanbide06Exception ex){
                    log.error("Se ha producido un error recuperando el texto del email " + ex.getMessage());
                    resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(emailUOR != null && !"".equals(emailUOR)){
                    if(log.isDebugEnabled()) log.debug("Enviamos el correo");
                    MailHelper mailHelper = new MailHelper();
                    try{
                        mailHelper.sendMail(emailUOR, asuntoEmail, textoEmail);
                    }catch(MailException ex){
                        log.error("Se ha producido un error enviando el email a la UOR " + ex.getMessage());
                        resultado = ERROR_ENVIANDO_EMAIL;
                    }catch(MailServiceNotActivedException ex){
                        log.error("El servicio de email no esta activo en la aplicacion " + ex.getMessage());
                        resultado = ERROR_SERVICIO_MAIL_INACTIVO;
                    }//try-catch
                }else{
                    if(log.isDebugEnabled()) log.debug("El email de la unidad organica es nulo, procedemos a comprobar"
                            + " si hay que lanzar un error");
                    Boolean lanzarError = getLanzarErrorEmail(codOrganizacion, codTramite, codProcedimiento, 
                            MeLanbide06Constantes.OP_EMAIL_UOR_INICIA_EXPEDIENTE);
                    if(log.isDebugEnabled()) log.debug("lanzarError = " + lanzarError);
                    if(lanzarError){
                        if(log.isDebugEnabled()) log.debug("Lanzamos el error");
                        resultado = ERROR_RECUPERANDO_EMAIL_UOR;
                    }else{
                        if(log.isDebugEnabled()) log.debug("La propiedad que indica si hay que lanzar el error es negativa");
                    }//if(lanzarError)
                }//if(emailUOR != null && !"".equals(emailUOR))
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
        }catch(Exception ex){
            log.error("Se ha producido un error en el proceso de envio del correo a la unidad tramitadora " + ex.getMessage());
            resultado = ERROR;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("envioCorreoUnidadIniciaExpediente() : END");
        return resultado;
    }//envioCorreoUnidadIniciaExpediente
    
    /**
     * Envia un correo a los usuarios que tengan permiso sobre la unidad que inicia el expediente
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return 
     */
    public String envioCorreoUsuariosUnidadIniciaExpediente (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente){
        if(log.isDebugEnabled()) log.debug("envioCorreoUsuariosUnidadIniciaExpediente() : BEGIN");
        String resultado = TODO_CORRECTO;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        ArrayList<UsuarioModuloIntegracionVO> listaUsuarios = new ArrayList<UsuarioModuloIntegracionVO>();
        String ejercicio = new String();
        String codProcedimiento = new String();
        String numExp = new String();
        
        String asuntoEmail = new String();
        String textoEmail = new String();
        String descripcionUorIniciaExpediente = new String();
        HashMap<String, String> camposSustituir = new HashMap<String, String>();
        try{
            String[] propsExpediente = numExpediente.split(MeLanbide06Constantes.BARRA);
            ejercicio = propsExpediente[0];
            codProcedimiento = propsExpediente[1];
            numExp = propsExpediente[2];
            
            if(log.isDebugEnabled()) log.debug("Recuperamos el codigo visible de la unidad organizativa");
            String uorCodVis = new String();
            if(log.isDebugEnabled()) log.debug("Recuperamos los datos del expediente");
            SalidaIntegracionVO salida = new SalidaIntegracionVO();
            salida = el.getExpediente(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
            if(salida.getStatus() == 0){
                if(log.isDebugEnabled()) log.debug("Recuperamos los datos del usuario que inicia el expediente");
                ExpedienteModuloIntegracionVO expediente = salida.getExpediente();
                uorCodVis = expediente.getCodigoUorVisibleInicioExpediente();
                if(log.isDebugEnabled()) log.debug("uorCodVis inicia expediente = " + uorCodVis);
            }else{
                log.error("Se ha producido un error recuperando los datos del expediente " + salida.getDescStatus());
                resultado = ERROR_RECUPERANDO_DATOS_EXPEDIENTE;
            }//if(salida.getStatus() == 0)
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos la lista de usuarios de la unidad organica");
                salida = new SalidaIntegracionVO();
                salida = el.getUsuariosPermisoUOR(String.valueOf(codOrganizacion), uorCodVis);
                if(salida.getStatus() == 0){
                    if(log.isDebugEnabled()) log.debug("Recuperamos los usuarios devueltos por la funcion");
                    listaUsuarios = salida.getListaUsuariosModuloIntegracionVO();
                }else{
                    log.error("Se ha producido un error recuperando los usuarios pertenecientes a una unidad organica");
                    resultado = ERROR_RECUPERANDO_USUARIOS_UOR;
                }//if(salida.getStatus() == 0)
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos la descripcion de la unidad de inicio");
                descripcionUorIniciaExpediente = MeLanbide06Manager.getInstance().
                        getDescripcionUor(uorCodVis, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(log.isDebugEnabled()) log.debug("descripcion uor inicia expediente = " + descripcionUorIniciaExpediente );
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos los codigos de los campos suplementarios que queremos sustituir en el texto");
                String campos = new String();
                try{
                    
                    camposSustituir=getValoresCamposSuplementarios(codOrganizacion, codTramite,ocurrenciaTramite, codProcedimiento,ejercicio, numExpediente,MeLanbide06Constantes.OP_EMAIL_USUARIOS_UOR_INICIA_EXPEDIENTE);
                }catch(MeLanbide06Exception ex){
                    if(log.isDebugEnabled()) log.debug("No existen campos suplementarios para sustituir");
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos el asunto del email");
                try{
                    asuntoEmail = getAsuntoEmail(codOrganizacion, codTramite, codProcedimiento, 
                            MeLanbide06Constantes.OP_EMAIL_USUARIOS_UOR_INICIA_EXPEDIENTE);
                    if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase("")){
                        log.error("La propiedad para el asunto del email es nula o esta vacia");
                        resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                    }else{
                        if(log.isDebugEnabled()) log.debug("asuntoEmail = " + asuntoEmail);
                    }//if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase(""))
                }catch(MeLanbide06Exception ex){
                    log.error("Se ha producido un error recuperando el asunto del email " + ex.getMessage());
                    resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Insertamos el expediente y la unidad tramitadora en los parametros");
                camposSustituir.put("idExpediente", numExpediente);
                camposSustituir.put("unidadTramitadora", descripcionUorIniciaExpediente);
                if(log.isDebugEnabled()) log.debug("Recuperamos el texto del email");
                try{
                    textoEmail = getTextoEmail(codOrganizacion, codTramite, codProcedimiento,
                            MeLanbide06Constantes.OP_EMAIL_USUARIOS_UOR_INICIA_EXPEDIENTE, camposSustituir);
                    if(textoEmail == null || textoEmail.equalsIgnoreCase("")){
                        log.error("La propiedad del texto del email es nula o esta vacia");
                        resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                    }else{
                        if(log.isDebugEnabled()) log.debug("textoEmail = " + textoEmail);
                    }//if(textoEmail == null || textoEmail.equalsIgnoreCase(""))
                }catch(MeLanbide06Exception ex){
                    log.error("Se ha producido un error recuperando el texto del email " + ex.getMessage());
                    resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Enviamos el correo");
                MailHelper mailHelper = new MailHelper();
                try{
                    for(UsuarioModuloIntegracionVO usuario : listaUsuarios){
                        if(usuario.getUsuEmail() != null && !"".equalsIgnoreCase(usuario.getUsuEmail())){
                            String email = usuario.getUsuEmail();
                            try{
                                mailHelper.sendMail(email, asuntoEmail, textoEmail);
                            }catch(MailException ex){
                                log.error("Se ha producido un error enviando el email al usuario " + ex.getMessage());
                            }//try-catch
                        }//if(usuario.getUsuEmail() != null && !"".equalsIgnoreCase(usuario.getUsuEmail()))
                    }//for(UsuarioModuloIntegracionVO usuario : listaUsuarios)
                }catch(MailServiceNotActivedException ex){
                    log.error("El servicio de email no esta activo en la aplicacion " + ex.getMessage());
                    resultado = ERROR_SERVICIO_MAIL_INACTIVO;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
        }catch(Exception ex){
            log.error("Se ha producido un error en el proceso de envio del correo a la unidad tramitadora " + ex.getMessage());
            resultado = ERROR;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("envioCorreoUsuariosUnidadIniciaExpediente() : END");
        return resultado;
    }//envioCorreoUsuariosUnidadIniciaExpediente
    
    /**
     * Envia un correo a una UOR definida como propiedad en el fichero de configuracion del modulo
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return 
     */
    public String envioCorreoUnidadTramitadora (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente){
        if(log.isDebugEnabled()) log.debug("envioCorreoUnidadTramitadora() : BEGIN");
        String resultado = TODO_CORRECTO;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        String ejercicio = new String();
        String codProcedimiento = new String();
        String numExp = new String();
        
        String emailUOR = new String();
        String asuntoEmail = new String();
        String textoEmail = new String();
        String descripcionUorTramitadora = new String();
        HashMap<String, String> camposSustituir = new HashMap<String, String>();
        try{
            String[] propsExpediente = numExpediente.split(MeLanbide06Constantes.BARRA);
            ejercicio = propsExpediente[0];
            codProcedimiento = propsExpediente[1];
            numExp = propsExpediente[2];
            
            if(log.isDebugEnabled()) log.debug("Recuperamos el codigo visible de la unidad organizativa");
            String codUorVisible = new String();
            try{
                codUorVisible = getCodigoUor(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_UOR);
                if(codUorVisible == null || "".equalsIgnoreCase(codUorVisible)){
                    log.error("El codigo de la UOR viene vacio o es nulo");
                    resultado = ERROR_RECUPERANDO_COD_UOR;
                }//if(codUorVisible == null || "".equalsIgnoreCase(codUorVisible))
                if(log.isDebugEnabled()) log.debug("uorCodVis = " + codUorVisible);
            }catch(MeLanbide06Exception ex){
                log.error("Se ha producido un error recuperando la propiedad que recupera el codigo de la UOR");
                resultado = ERROR_RECUPERANDO_COD_UOR;
            }//try-catch
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos la unidad organica");
                SalidaIntegracionVO salida = new SalidaIntegracionVO();
                salida = el.getUOR(String.valueOf(codOrganizacion), codUorVisible);
                if(salida.getStatus() == 0){
                    if(log.isDebugEnabled()) log.debug("Recuperamos el correo de la UOR");
                    UORModuloIntegracionVO uor = salida.getUorModuloIntegracionVO();
                    emailUOR = uor.getUorEmail();
                    if(log.isDebugEnabled()) log.debug("emailUOR = " + emailUOR);
                }else{
                    log.error("Se ha producido un error recuperando la unidad organica");
                    resultado = ERROR_RECUPERANDO_UOR;
                }//if(salida.getStatus() != 0)
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos los codigos de los campos suplementarios que queremos sustituir en el texto");
                String campos = new String();
                try{
                  
                    camposSustituir=getValoresCamposSuplementarios(codOrganizacion, codTramite,ocurrenciaTramite, codProcedimiento,ejercicio, numExpediente,MeLanbide06Constantes.OP_EMAIL_UOR);
                }catch(MeLanbide06Exception ex){
                    if(log.isDebugEnabled()) log.debug("No existen campos suplementarios para sustituir");
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos la unidad tramitadora");
                descripcionUorTramitadora = MeLanbide06Manager.getInstance().getDescripcionUorTramite(codOrganizacion, codTramite, 
                        numExpediente, codProcedimiento, ejercicio, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(log.isDebugEnabled()) log.debug("descripcionUorTramitadora = " + descripcionUorTramitadora );
                camposSustituir.put("unidadTramitadora", descripcionUorTramitadora);
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos el asunto del email");
                try{
                    asuntoEmail = getAsuntoEmail(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_UOR);
                    if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase("")){
                        log.error("La propiedad para el asunto del email es nula o esta vacia");
                        resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                    }//if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase(""))
                }catch(MeLanbide06Exception ex){
                    log.error("Se ha producido un error recuperando el asunto del email " + ex.getMessage());
                    resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Insertamos el expediente y la unidad tramitadora en los parametros");
                camposSustituir.put("idExpediente", numExpediente);
                if(log.isDebugEnabled()) log.debug("Recuperamos el texto del email");
                try{
                    textoEmail = getTextoEmail(codOrganizacion, codTramite, 
                            codProcedimiento, MeLanbide06Constantes.OP_EMAIL_UOR, camposSustituir);
                    if(textoEmail == null || textoEmail.equalsIgnoreCase("")){
                        log.error("La propiedad del texto del email es nula o esta vacia");
                        resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                    }//if(textoEmail == null || textoEmail.equalsIgnoreCase(""))
                }catch(MeLanbide06Exception ex){
                    log.error("Se ha producido un error recuperando el texto del email " + ex.getMessage());
                    resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(emailUOR != null && !"".equals(emailUOR)){
                    if(log.isDebugEnabled()) log.debug("Enviamos el correo");
                    MailHelper mailHelper = new MailHelper();
                    try{
                        mailHelper.sendMail(emailUOR, asuntoEmail, textoEmail);
                    }catch(MailException ex){
                        log.error("Se ha producido un error enviando el email a la UOR " + ex.getMessage());
                        resultado = ERROR_ENVIANDO_EMAIL;
                    }catch(MailServiceNotActivedException ex){
                        log.error("El servicio de email no esta activo en la aplicacion " + ex.getMessage());
                        resultado = ERROR_SERVICIO_MAIL_INACTIVO;
                    }//try-catch
                }else{
                    if(log.isDebugEnabled()) log.debug("El email de la unidad organica es nulo, procedemos a comprobar"
                            + " si hay que lanzar un error");
                    Boolean lanzarError = getLanzarErrorEmail(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_UOR);
                    if(lanzarError){
                        if(log.isDebugEnabled()) log.debug("Lanzamos el error");
                        resultado = ERROR_RECUPERANDO_EMAIL_UOR;
                    }//if(lanzarError)
                }//if(emailUOR != null && !"".equals(emailUOR))
                
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
        }catch(Exception ex){
            log.error("Se ha producido un error en el proceso de envio del correo a la unidad tramitadora " + ex.getMessage());
            resultado = ERROR;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("envioCorreoUnidadTramitadora() : END");
        return resultado;
    }//envioCorreoUnidadTramitadora
    
    /**
     * Envia un correo a los usuarios de una UOR definida como propiedad en el fichero de configuracion del modulo
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return 
     */
    public String envioCorreoUsuariosTramitadora (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente){
        if(log.isDebugEnabled()) log.debug("envioCorreoUsuariosTramitadora() : BEGIN");
        String resultado = TODO_CORRECTO;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        ArrayList<UsuarioModuloIntegracionVO> listaUsuarios = new ArrayList<UsuarioModuloIntegracionVO>();
        String ejercicio = new String();
        String codProcedimiento = new String();
        String numExp = new String();
        
        String asuntoEmail = new String();
        String textoEmail = new String();
        String descripcionUorTramitadora = new String();
        HashMap<String, String> camposSustituir = new HashMap<String, String>();
        try{
            String[] propsExpediente = numExpediente.split(MeLanbide06Constantes.BARRA);
            ejercicio = propsExpediente[0];
            codProcedimiento = propsExpediente[1];
            numExp = propsExpediente[2];
            
            if(log.isDebugEnabled()) log.debug("Recuperamos el codigo visible de la unidad organizativa");
            String codUorVisible = new String();
            try{
                codUorVisible = getCodigoUor(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_USUARIOS_UOR);
                if(codUorVisible == null || "".equalsIgnoreCase(codUorVisible)){
                    log.error("El codigo de la UOR viene vacio o es nulo");
                    resultado = ERROR_RECUPERANDO_COD_UOR;
                }else{
                    if(log.isDebugEnabled()) log.debug("codUorVisible = " + codUorVisible);
                }//if(codUorVisible == null || "".equalsIgnoreCase(codUorVisible))
                if(log.isDebugEnabled()) log.debug("uorCodVis = " + codUorVisible);
            }catch(MeLanbide06Exception ex){
                log.error("Se ha producido un error recuperando la propiedad que recupera el codigo de la UOR");
                resultado = ERROR_RECUPERANDO_COD_UOR;
            }//try-catch
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos la lista de usuarios de la unidad organica");
                SalidaIntegracionVO salida = el.getUsuariosPermisoUOR(String.valueOf(codOrganizacion), codUorVisible);
                if(salida.getStatus() == 0){
                    if(log.isDebugEnabled()) log.debug("Recuperamos los usuarios devueltos por la funcion");
                    listaUsuarios = salida.getListaUsuariosModuloIntegracionVO();
                }else{
                    log.error("Se ha producido un error recuperando los usuarios pertenecientes a una unidad organica");
                    resultado = ERROR_RECUPERANDO_USUARIOS_UOR;
                }//if(salida.getStatus() == 0)
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos los codigos de los campos suplementarios que queremos sustituir en el texto");
                String campos = new String();
                try{
                  
                    camposSustituir=getValoresCamposSuplementarios(codOrganizacion, codTramite,ocurrenciaTramite, codProcedimiento,ejercicio, numExpediente,MeLanbide06Constantes.OP_EMAIL_USUARIOS_UOR);
                }catch(MeLanbide06Exception ex){
                    if(log.isDebugEnabled()) log.debug("No existen campos suplementarios para sustituir");
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos la unidad tramitadora");
                descripcionUorTramitadora = MeLanbide06Manager.getInstance().getDescripcionUorTramite(codOrganizacion, codTramite, 
                        numExpediente, codProcedimiento, ejercicio, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(log.isDebugEnabled()) log.debug("descripcionUorTramitadora = " + descripcionUorTramitadora);
                camposSustituir.put("unidadTramitadora", descripcionUorTramitadora);
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Recuperamos el asunto del email");
                try{
                    asuntoEmail = getAsuntoEmail(codOrganizacion, codTramite, codProcedimiento, MeLanbide06Constantes.OP_EMAIL_USUARIOS_UOR);
                    if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase("")){
                        log.error("La propiedad para el asunto del email es nula o esta vacia");
                        resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                    }else{
                        if(log.isDebugEnabled()) log.debug("asuntoEmail = " + asuntoEmail);
                    }//if(asuntoEmail == null || asuntoEmail.equalsIgnoreCase(""))
                }catch(MeLanbide06Exception ex){
                    log.error("Se ha producido un error recuperando el asunto del email " + ex.getMessage());
                    resultado = ERROR_RECUPERANDO_ASUNTO_EMAIL;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Insertamos el expediente y la unidad tramitadora en los parametros");
                camposSustituir.put("idExpediente", numExpediente);
                if(log.isDebugEnabled()) log.debug("Recuperamos el texto del email");
                try{
                    textoEmail = getTextoEmail(codOrganizacion, codTramite, 
                            codProcedimiento, MeLanbide06Constantes.OP_EMAIL_USUARIOS_UOR, camposSustituir);
                    if(textoEmail == null || textoEmail.equalsIgnoreCase("")){
                        log.error("La propiedad del texto del email es nula o esta vacia");
                        resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                    }else{
                        if(log.isDebugEnabled()) log.debug("textoEmail = " + textoEmail);
                    }//if(textoEmail == null || textoEmail.equalsIgnoreCase(""))
                }catch(MeLanbide06Exception ex){
                    log.error("Se ha producido un error recuperando el texto del email " + ex.getMessage());
                    resultado = ERROR_RECUPERANDO_TEXTO_EMAIL;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
            if(TODO_CORRECTO.equalsIgnoreCase(resultado)){
                if(log.isDebugEnabled()) log.debug("Enviamos el correo");
                MailHelper mailHelper = new MailHelper();
                try{
                    for(UsuarioModuloIntegracionVO usuario : listaUsuarios){
                        if(usuario.getUsuEmail() != null && !"".equalsIgnoreCase(usuario.getUsuEmail())){
                            String email = usuario.getUsuEmail();
                            try{
                                mailHelper.sendMail(email, asuntoEmail, textoEmail);
                            }catch(MailException ex){
                                log.error("Se ha producido un error enviando el email al usuario " + ex.getMessage());
                            }//try-catch
                        }//if(usuario.getUsuEmail() != null && !"".equalsIgnoreCase(usuario.getUsuEmail()))
                    }//for(UsuarioModuloIntegracionVO usuario : listaUsuarios)
                }catch(MailServiceNotActivedException ex){
                    log.error("El servicio de email no esta activo en la aplicacion " + ex.getMessage());
                    resultado = ERROR_SERVICIO_MAIL_INACTIVO;
                }//try-catch
            }//if(TODO_CORRECTO.equalsIgnoreCase(resultado))
        }catch(Exception ex){
            log.error("Se ha producido un error en el proceso de envio del correo a la unidad tramitadora " + ex.getMessage());
            resultado = ERROR;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("envioCorreoUsuariosTramitadora() : END");
        return resultado;
    }//envioCorreoUsuariosTramitadora
    
    
    
    private  HashMap getValoresCamposSuplementarios(int codOrganizacion,int codTramite,int ocurrenciaTramite,String codProcedimiento,String ejercicio,String numExpediente, String constanteMelanbide06)
     throws MeLanbide06Exception{    
             HashMap<String, String> camposSustituir = new HashMap<String, String>();
               String campos = new String();
    
    try{
                    campos =
                        getCamposSuplementarios(codOrganizacion, codTramite, codProcedimiento, constanteMelanbide06);
                    if(campos != null && !"".equalsIgnoreCase(campos)){
                        if(log.isDebugEnabled()) log.debug("lista campos a recuperar = " + campos);
                        String[] listaCampos = campos.split(";");
                        for(String campo : listaCampos){
                            if(log.isDebugEnabled()) log.debug("Recuperamos el tipo de campo");
                            String tipoCampo = getTipoCampoSuplementario(codOrganizacion, codTramite, codProcedimiento, 
                                   constanteMelanbide06, campo);
                            if(log.isDebugEnabled()) log.debug("Recuperamos el valor del campo");
                            String valor = getValorCampoSuplementarioTramite(codOrganizacion, codTramite, codProcedimiento, 
                                    campo, Integer.valueOf(tipoCampo), ejercicio, numExpediente, ocurrenciaTramite);
                            if(log.isDebugEnabled()) log.debug("campo = " + campo);
                            if(log.isDebugEnabled()) log.debug("tipoCampo = " + tipoCampo);
                            if(log.isDebugEnabled()) log.debug("valor = " + valor);
                            camposSustituir.put(campo, valor);
                        }//for(String campo : listaCampos)
                    }//if(campos != null && !"".equalsIgnoreCase(campos))
                    
                   int codTramiteAnterior=-1;
                   try{
                   String tramiteAnterior=getTramiteAnterior(codOrganizacion, codTramite, codProcedimiento, constanteMelanbide06+"/TRAM_ANTERIOR");
                   codTramiteAnterior=Integer.parseInt(tramiteAnterior);
                   } catch (Exception e)
                   {
                       if(log.isDebugEnabled()) log.debug("No existen informacion de otro tramite para consultar para el procedimiento "+codProcedimiento+" y tramite "+codTramite);
                   }
                    
        
                   
                    if (codTramiteAnterior!=-1){

                    campos =
                        getCamposSuplementarios(codOrganizacion, codTramite, codProcedimiento, constanteMelanbide06+"/TRAM_ANTERIOR");
                    if(campos != null && !"".equalsIgnoreCase(campos)){
                        if(log.isDebugEnabled()) log.debug("lista campos a recuperar = " + campos);
                        String[] listaCampos = campos.split(";");
                        for(String campo : listaCampos){
                            if(log.isDebugEnabled()) log.debug("Recuperamos el tipo de campo");
                            String tipoCampo = getTipoCampoSuplementario(codOrganizacion, codTramite, codProcedimiento, 
                                   constanteMelanbide06+"/TRAM_ANTERIOR", campo);
                            if(log.isDebugEnabled()) log.debug("Recuperamos el valor del campo");
                            String valor = getValorCampoSuplementarioTramite(codOrganizacion, codTramiteAnterior, codProcedimiento, 
                                    campo, Integer.valueOf(tipoCampo), ejercicio, numExpediente, -1);
                            if(log.isDebugEnabled()) log.debug("campo = " + campo);
                            if(log.isDebugEnabled()) log.debug("tipoCampo = " + tipoCampo);
                            if(log.isDebugEnabled()) log.debug("valor = " + valor);
                            camposSustituir.put(campo, valor);
                        }//for(String campo : listaCampos)
                    }//if(campos != null && !"".equalsIgnoreCase(campos))
                    }
                     
                    
                    
                    
                    
                    
                }catch(MeLanbide06Exception ex){
                    if(log.isDebugEnabled()) log.debug("No existen campos suplementarios para sustituir");
                }//try-catch
          return camposSustituir;
    }
    
    /**
     * Recupera los campos suplementarios que deben de ser sustituidos en el texto del correo
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @param codOperacion
     * @return
     * @throws MeLanbide06Exception 
     */
    private String getCamposSuplementarios(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion)
             throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getCamposSuplementarios() : BEGIN");
        String camposSuplementarios = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos la propiedad ");
            String codPropiedad = codOrganizacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.MODULO_INTEGRACION + 
                    MeLanbide06Constantes.BARRA + MeLanbide06Constantes.NOMBRE_MODULO + MeLanbide06Constantes.BARRA +
                    codProcedimiento + MeLanbide06Constantes.BARRA + codTramite + MeLanbide06Constantes.BARRA + 
                    codOperacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.CAMPOS_SUPLEMENTARIOS;
            if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad)){
                if(log.isDebugEnabled()) log.debug("codPropiedad = " + codPropiedad);
                if(log.isDebugEnabled()) log.debug("Procedemos a buscar la propiedad en el fichero de propiedades");
                camposSuplementarios = 
                        MeLanbide06ConfigurationParameter.getParameter(codPropiedad, MeLanbide06Constantes.FICHERO_CONFIGURACION);
            }else{
                log.error("Se ha producido un error recuperando los campos suplementarios para el mensje,"
                        + " no hay ninguna propiedad con el codigo definido");
                throw new MeLanbide06Exception("Se ha producido un error recuperando los campos suplementarios para el mensaje,"
                        + " no hay ninguna propiedad con el codigo definido");
            }//if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando los campos suplementarios");
            throw new MeLanbide06Exception("Se ha producido un error recuperando los campos suplementarios ", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCamposSuplementarios() : END");
        return camposSuplementarios;
    }//getCamposSuplementarios
    
    
    
    
    
     private String getTramiteAnterior(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion)
             throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getCamposSuplementarios() : BEGIN");
        String camposSuplementarios = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos la propiedad ");
            String codPropiedad = codOrganizacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.MODULO_INTEGRACION + 
                    MeLanbide06Constantes.BARRA + MeLanbide06Constantes.NOMBRE_MODULO + MeLanbide06Constantes.BARRA +
                    codProcedimiento + MeLanbide06Constantes.BARRA + codTramite + MeLanbide06Constantes.BARRA + 
                    codOperacion;
            if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad)){
                if(log.isDebugEnabled()) log.debug("codPropiedad = " + codPropiedad);
                if(log.isDebugEnabled()) log.debug("Procedemos a buscar la propiedad en el fichero de propiedades");
                camposSuplementarios = 
                        MeLanbide06ConfigurationParameter.getParameter(codPropiedad, MeLanbide06Constantes.FICHERO_CONFIGURACION);
            }else{
                log.error("Se ha producido un error recuperando los campos suplementarios para el mensje,"
                        + " no hay ninguna propiedad con el codigo definido");
                throw new MeLanbide06Exception("Se ha producido un error recuperando los campos suplementarios para el mensaje,"
                        + " no hay ninguna propiedad con el codigo definido");
            }//if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando los campos suplementarios");
            throw new MeLanbide06Exception("Se ha producido un error recuperando los campos suplementarios ", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCamposSuplementarios() : END");
        return camposSuplementarios;
    }//getCamposSuplementarios
    
    /**
     * Recupera el tipo del campo suplementario
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @param codOperacion
     * @param codCampo
     * @return
     * @throws MeLanbide06Exception 
     */
    private String getTipoCampoSuplementario(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion,
            String codCampo)throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getTipoCampoSuplementario() : BEGIN");
        String tipoCampo = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos la propiedad ");
            String codPropiedad = codOrganizacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.MODULO_INTEGRACION + 
                    MeLanbide06Constantes.BARRA + MeLanbide06Constantes.NOMBRE_MODULO + MeLanbide06Constantes.BARRA +
                    codProcedimiento + MeLanbide06Constantes.BARRA + codTramite + MeLanbide06Constantes.BARRA + 
                    codOperacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.TIPO_CAMPO + MeLanbide06Constantes.BARRA + codCampo;
            if(log.isDebugEnabled()) log.debug("codPropiedad = " + codPropiedad);
            if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad)){
                if(log.isDebugEnabled()) log.debug("Procedemos a buscar la propiedad en el fichero de propiedades");
                tipoCampo = 
                        MeLanbide06ConfigurationParameter.getParameter(codPropiedad, MeLanbide06Constantes.FICHERO_CONFIGURACION);
            }else{
                log.error("Se ha producido un error recuperando el tipo del campo suplementario"
                        + " no hay ninguna propiedad con el codigo definido");
                throw new MeLanbide06Exception("Se ha producido un error recuperando el tipo del campo suplementario"
                        + " no hay ninguna propiedad con el codigo definido");
            }//if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el tipo del campo suplementario");
            throw new MeLanbide06Exception("Se ha producido un error recuperando el tipo del campo suplementario ", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getTipoCampoSuplementario() : END");
        return tipoCampo;
    }//getTipoCampoSuplementario
    
    /**
     * Devuelve el valor de un campo suplementario de tramite
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @param codCampo
     * @param tipoCampo
     * @param ejercicio
     * @param numExpediente
     * @param ocurrenciaTramite
     * @return
     * @throws MeLanbide06Exception 
     */
    public String getValorCampoSuplementarioTramite(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codCampo,
            Integer tipoCampo, String ejercicio, String numExpediente, Integer ocurrenciaTramite) throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getValorCampoSuplementario() : BEGIN");
        String valor = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos el valor ");
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            SalidaIntegracionVO salida = new SalidaIntegracionVO();
            salida = el.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExpediente, 
                    codProcedimiento, codTramite, ocurrenciaTramite, codCampo, tipoCampo);
            if(log.isDebugEnabled()) log.debug("estado de salida = " + salida.getStatus());
            if(salida.getStatus() == 0){
                CampoSuplementarioModuloIntegracionVO campo = salida.getCampoSuplementario();
                switch(tipoCampo){
                    case IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO:{
                        if(log.isDebugEnabled()) log.debug("Recuperamos el valor del campo numerico");
                        valor = campo.getValorNumero();
                        break;
                    }//case IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO:
                        
                    case IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO:{
                        if(log.isDebugEnabled()) log.debug("Recuperamos el valor del campo de texto");
                        valor = campo.getValorTexto();
                        break;
                    }//case IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO:
                        
                    case IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO:{
                        if(log.isDebugEnabled()) log.debug("Recuperamos el valor del campo de texto largo");
                        valor = campo.getValorTexto();
                        break;
                    }//case IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO:
                        
                    case IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA:{
                        if(log.isDebugEnabled()) log.debug("Recuperamos el valor del campo de fecha");
                        Calendar fecha = campo.getValorFecha();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        valor = sdf.format(fecha.getTime());
                        break;
                    }//case IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA:
                        
                    case IModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE:{
                        if(log.isDebugEnabled()) log.debug("Recuperamos el valor del campo desplegable");
                        valor = campo.getDescripcionValorDesplegable();
                        break;
                    }//case IModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE:
                }//switch(Integer.valueOf(tipoCampo))
            }else{
                log.error("Se ha producido un error recuperando el valor del campo suplementario ");
                throw new MeLanbide06Exception("Se ha producido un error recuperando el valor del campo suplementario ");
            }//if(salida.getStatus() == 0)
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el valor del campo suplementario");
            throw new MeLanbide06Exception("Se ha producido un error recuperando el valor del campo suplementario ", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorCampoSuplementario() : END");
        return valor;
    }//getValorCampoSuplementario
    
    
    
    
    
    /**
     * Recupera la UOR definida a la que hay que enviar el correo para las operaciones que lo precisen
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @return 
     */
    private String getCodigoUor(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion) 
            throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getCodigoUor() : BEGIN");
        String uorCodVis = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos la propiedad ");
            String codPropiedad = codOrganizacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.MODULO_INTEGRACION + 
                    MeLanbide06Constantes.BARRA + MeLanbide06Constantes.NOMBRE_MODULO + MeLanbide06Constantes.BARRA +
                    codProcedimiento + MeLanbide06Constantes.BARRA + codTramite + MeLanbide06Constantes.BARRA + 
                    codOperacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.UOR_COD_VIS;
            if(log.isDebugEnabled()) log.debug("codPropiedad = " + codPropiedad);
            if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad)){
                if(log.isDebugEnabled()) log.debug("Procedemos a buscar la propiedad en el fichero de propiedades");
                uorCodVis = MeLanbide06ConfigurationParameter.getParameter(codPropiedad, MeLanbide06Constantes.FICHERO_CONFIGURACION);
            }else{
                log.error("Se ha producido un error recuperando el codigo de la uor,"
                        + " no hay ninguna propiedad con el codigo definido");
                throw new MeLanbide06Exception("Se ha producido un error recuperando el codigo de la uor,"
                        + " no hay ninguna propiedad con el codigo definido");
            }//if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el codigo de la uor");
            throw new MeLanbide06Exception("Se ha producido un error recuperando el codigo de la uor ", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCodigoUor() : END");
        return uorCodVis;
    }//getCodigoUor
    
    /**
     * Recupera el asunto del email a enviar para una operacion
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @return 
     */
    private String getAsuntoEmail(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion) 
            throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getAsuntoEmail() : BEGIN");
        String asunto = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos la propiedad ");
            String codPropiedad = codOrganizacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.MODULO_INTEGRACION + 
                    MeLanbide06Constantes.BARRA + MeLanbide06Constantes.NOMBRE_MODULO + MeLanbide06Constantes.BARRA +
                    codProcedimiento + MeLanbide06Constantes.BARRA + codTramite + MeLanbide06Constantes.BARRA + 
                    codOperacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.ASUNTO_EMAIL;
            if(log.isDebugEnabled()) log.debug("codPropiedad = " + codPropiedad);
            if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad)){
                if(log.isDebugEnabled()) log.debug("Procedemos a buscar la propiedad en el fichero de propiedades");
                asunto = MeLanbide06ConfigurationParameter.getParameter(codPropiedad, MeLanbide06Constantes.FICHERO_CONFIGURACION);
            }else{
                log.error("Se ha producido un error recuperando el asunto para el email,"
                        + " no hay ninguna propiedad con el codigo definido");
                throw new MeLanbide06Exception("Se ha producido un error recuperando el asunto para el email,"
                        + " no hay ninguna propiedad con el codigo definido");
            }//if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el asunto para el email");
            throw new MeLanbide06Exception("Se ha producido un error recuperando el asunto para el email", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getAsuntoEmail() : END");
        return asunto;
    }//getAsuntoEmail
    
      /**
     * Recupera el asunto del email a enviar para una operacion
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @return 
     */
    private String getAsuntoEmailSustCampos(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion,HashMap<String,String> parametros) 
            throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getAsuntoEmailSustCampos() : BEGIN");
        String asunto = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos la propiedad ");
            String codPropiedad = codOrganizacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.MODULO_INTEGRACION + 
                    MeLanbide06Constantes.BARRA + MeLanbide06Constantes.NOMBRE_MODULO + MeLanbide06Constantes.BARRA +
                    codProcedimiento + MeLanbide06Constantes.BARRA + codTramite + MeLanbide06Constantes.BARRA + 
                    codOperacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.ASUNTO_EMAIL;
            if(log.isDebugEnabled()) log.debug("codPropiedad = " + codPropiedad);
            if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad)){
                if(log.isDebugEnabled()) log.debug("Procedemos a buscar la propiedad en el fichero de propiedades");
                asunto = MeLanbide06ConfigurationParameter.getParameter(codPropiedad, MeLanbide06Constantes.FICHERO_CONFIGURACION);
                if(parametros != null && parametros.size() > 0){
                    Iterator it = parametros.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry parametro = (Map.Entry)it.next();
                        asunto = asunto.replaceAll("@" + (String)parametro.getKey() + "@", (String)parametro.getValue());
                    }//while (it.hasNext()) 
                }//if(parametros != null && parametros.size() > 0)
            }else{
                log.error("Se ha producido un error recuperando el asunto para el email,"
                        + " no hay ninguna propiedad con el codigo definido");
                throw new MeLanbide06Exception("Se ha producido un error recuperando el asunto para el email,"
                        + " no hay ninguna propiedad con el codigo definido");
            }//if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el asunto para el email");
            throw new MeLanbide06Exception("Se ha producido un error recuperando el asunto para el email", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getAsuntoEmailSustCampos() : END");
        return asunto;
    }//getAsuntoEmailSustCampos
    
    /**
     * Recupera el texto del email a enviar para una operacion
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @return 
     */
    private String getTextoEmail(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion,
            HashMap<String,String> parametros)throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getTextoEmail() : BEGIN");
        String texto = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos la propiedad ");
            String codPropiedad = codOrganizacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.MODULO_INTEGRACION + 
                    MeLanbide06Constantes.BARRA + MeLanbide06Constantes.NOMBRE_MODULO + MeLanbide06Constantes.BARRA +
                    codProcedimiento + MeLanbide06Constantes.BARRA + codTramite + MeLanbide06Constantes.BARRA + 
                    codOperacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.TEXTO_EMAIL;
            if(log.isDebugEnabled()) log.debug("codPropiedad = " + codPropiedad);
            if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad)){
                if(log.isDebugEnabled()) log.debug("Procedemos a buscar la propiedad en el fichero de propiedades");
                texto = MeLanbide06ConfigurationParameter.getParameter(codPropiedad, MeLanbide06Constantes.FICHERO_CONFIGURACION);
                if(parametros != null && parametros.size() > 0){
                    Iterator it = parametros.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry parametro = (Map.Entry)it.next();
                        texto = texto.replaceAll("@" + (String)parametro.getKey() + "@", (String)parametro.getValue());
                    }//while (it.hasNext()) 
                }//if(parametros != null && parametros.size() > 0)
            }else{
                log.error("Se ha producido un error recuperando el texto para el email"
                        + " no hay ninguna propiedad con el codigo definido");
                throw new MeLanbide06Exception("Se ha producido un error recuperando el texto para el email"
                        + " no hay ninguna propiedad con el codigo definido");
            }//if(codPropiedad != null && !"".equalsIgnoreCase(codPropiedad))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando texto para el correo");
            throw new MeLanbide06Exception("Se ha producido un error recuperando texto para el correo", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getTextoEmail() : END");
        return texto.trim();
    }//getTextoEmail
    
    /**
     * Recupera para un expediente y codigo de tramite la descripcion de la UOR que tiene asignada
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @param codOperacion
     * @param numExpediente
     * @param ejercicio
     * @return
     * @throws MeLanbide06Exception 
     */
    private String getDescripcionUorTramite(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion, String numExpediente,
            String ejercicio) throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionUorTramite() : BEGIN");
        String descripcionUor = new String();
        try{    
            descripcionUor = MeLanbide06Manager.getInstance().getDescripcionUorTramite(codOrganizacion, codTramite, numExpediente, 
                            codProcedimiento, ejercicio, getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la descripcion de la uor para el expediente = " + numExpediente + 
                    " para el tramite = " + codTramite);
            throw new MeLanbide06Exception("Se ha producido un error recuperando la descripcion de la uor para el expediente = " + numExpediente + 
                    " para el tramite = " + codTramite, ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getDescripcionUorTramite() : END");
        return descripcionUor;
    }//getDescripcionUorTramite
    
    /**
     * Indica si la operacion debe de lanzar un error en caso de no encontrar un email al que enviar el mensaje
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @param codOperacion
     * @return 
     */
    private Boolean getLanzarErrorEmail(Integer codOrganizacion, Integer codTramite, String codProcedimiento, String codOperacion){
        if(log.isDebugEnabled()) log.debug("getLanzarErrorEmail() : BEGIN");
        Boolean lanzar = false;
        String propiedad = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos la propiedad si existe por codigo de procedimiento y tramite");
            String codProp = codOrganizacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.MODULO_INTEGRACION + 
                    MeLanbide06Constantes.BARRA + MeLanbide06Constantes.NOMBRE_MODULO + MeLanbide06Constantes.BARRA +
                    codProcedimiento + MeLanbide06Constantes.BARRA + codTramite + MeLanbide06Constantes.BARRA + 
                    codOperacion + MeLanbide06Constantes.BARRA + MeLanbide06Constantes.ERROR_NO_MAIL;
            if(log.isDebugEnabled()) log.debug("codProp = " + codProp);
            if(codProp != null && !"".equalsIgnoreCase(codProp)){
                propiedad =  MeLanbide06ConfigurationParameter.getParameter(codProp, MeLanbide06Constantes.FICHERO_CONFIGURACION);
                if(log.isDebugEnabled()) log.debug("El valor de la propiedad es = " + propiedad);
                if("S".equalsIgnoreCase(propiedad.trim())){
                    lanzar = true;
                }//if("S".equalsIgnoreCase(prop.trim()))
            }//if(prop != null && !"".equalsIgnoreCase(prop))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la propiedad que indica si hay que lanzar algun error si no existe el email " 
                    + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("lanzar = " + lanzar);
        if(log.isDebugEnabled()) log.debug("getLanzarErrorEmail() : END");
        return lanzar;
    }//getLanzarErrorEmail
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            }catch(TechnicalException te){
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    
    public String cargarGestionAvisos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarGestionAvisos - Begin()" + new Date().toString());
        String respuestaServicio = "/jsp/extension/melanbide06/gestionAvisos/formularioGestionAvisos.jsp";
        cargarCombosGestionAvisos(request,codOrganizacion);     
        return respuestaServicio;
    }
      
    public void cargarCombosGestionAvisos(HttpServletRequest request, int codOrganizacion){
        
        comboOrganizacionesGestionAvisos(request,codOrganizacion);
        comboEntidadesGestionAvisos(request,codOrganizacion);
        comboProcedimientoGestionAvisos(request,codOrganizacion);
        comboUORSGestionAvisos(request,codOrganizacion);
        comboAsuntosGestionAvisos(request,codOrganizacion);
        comboEventosGestionAvisos(request,codOrganizacion);
    }
    
    public void comboProcedimientoGestionAvisos(HttpServletRequest request, int codOrganizacion){
         GestionAvisosManager gestionAvisosManager = GestionAvisosManager.getInstance();
		 
	List<SelectItem> listaProcedimientos = new ArrayList<SelectItem>();
	try {
		listaProcedimientos = gestionAvisosManager.getListaProcedimientos( this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
	} catch (Exception ex) {
		log.debug("Error al obtener la Lista de procedimientos", ex);
	}
	request.setAttribute("listaProcedimientos", listaProcedimientos);
    }
    
    public void comboUORSGestionAvisos(HttpServletRequest request, int codOrganizacion){
         GestionAvisosManager gestionAvisosManager = GestionAvisosManager.getInstance();
		 
	List<SelectItem> listaUors = new ArrayList<SelectItem>();
	try {
		listaUors = gestionAvisosManager.getListaUors( this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
	} catch (Exception ex) {
		log.debug("Error al obtener la Lista de uors", ex);
	}
	request.setAttribute("listaUors", listaUors);
    }
    
    public void comboAsuntosGestionAvisos(HttpServletRequest request, int codOrganizacion){
         GestionAvisosManager gestionAvisosManager = GestionAvisosManager.getInstance();
		 
	List<SelectItem> listaAsuntos = new ArrayList<SelectItem>();
	try {
		listaAsuntos = gestionAvisosManager.getListaAsuntos( this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
	} catch (Exception ex) {
		log.debug("Error al obtener la Lista de asuntos", ex);
	}
	request.setAttribute("listaAsuntos", listaAsuntos);
    }
    
    public void comboEventosGestionAvisos(HttpServletRequest request, int codOrganizacion){
         GestionAvisosManager gestionAvisosManager = GestionAvisosManager.getInstance();
		 
	List<SelectItem> listaEventos = new ArrayList<SelectItem>();
	try {
		listaEventos = gestionAvisosManager.getListaEventos( this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
	} catch (Exception ex) {
		log.debug("Error al obtener la Lista de eventos", ex);
	}
	request.setAttribute("listaEventos", listaEventos);
    }
    
    public void comboEntidadesGestionAvisos(HttpServletRequest request, int codOrganizacion){
         GestionAvisosManager gestionAvisosManager = GestionAvisosManager.getInstance();
		 
	List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
	try {
		listaEntidades = gestionAvisosManager.getListaEntidades( this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
	} catch (Exception ex) {
		log.debug("Error al obtener la Lista de entidades", ex);
	}
	request.setAttribute("listaEntidades", listaEntidades);
    }
    
    public void comboOrganizacionesGestionAvisos(HttpServletRequest request, int codOrganizacion){
         GestionAvisosManager gestionAvisosManager = GestionAvisosManager.getInstance();
		 
	List<SelectItem> listaOrganizaciones = new ArrayList<SelectItem>();
	try {
		listaOrganizaciones = gestionAvisosManager.getListaOrganizaciones( this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
	} catch (Exception ex) {
		log.debug("Error al obtener la Lista de organizaciones", ex);
	}
	request.setAttribute("listaOrganizaciones", listaOrganizaciones);
    }  
    
            
    public String cargarEtiquetas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarEtiquetas - Begin()" + new Date().toString());
        String respuestaServicio = "/jsp/extension/melanbide06/gestionAvisos/modal/modalEtiquetas.jsp";
        cargarEtiquetas(request);
        return respuestaServicio;
    }
    
    public void cargarEtiquetas(HttpServletRequest request){      
        List<SelectItem> listaEtiquetas = new ArrayList<SelectItem>();
        SelectItem item = new SelectItem();
        item = new SelectItem();
        item.setCodigo("@nombreDocumento@");
        item.setDescripcion("Nombre del documento");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@numeroExpediente@");
        item.setDescripcion("Número del expediente");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@interesado@");
        item.setDescripcion("Interesado");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@usuario@");
        item.setDescripcion("Usuario");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@documento@");
        item.setDescripcion("Documento");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@tramite@");
        item.setDescripcion("Trámite");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@expediente@");
        item.setDescripcion("Expediente");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@unidadTramitadora@");
        item.setDescripcion("Unidad tramitadora");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@procedimiento@");
        item.setDescripcion("Procedimiento");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@ejercicio@");
        item.setDescripcion("Ejercicio");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@municipio@");
        item.setDescripcion("Municipio");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@ocurrencia@");
        item.setDescripcion("Ocurrencia");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@asunto@");
        item.setDescripcion("Asunto");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@cargo@");
        item.setDescripcion("Cargo");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@uor@");
        item.setDescripcion("Unidad Orgánica");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@plazo@");
        item.setDescripcion("Plazo");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@fecha@");
        item.setDescripcion("Fecha");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@fechaAnotacion@");
        item.setDescripcion("Fecha de la anotación");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@numeroAnotacion@");
        item.setDescripcion("Número de la anotación");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@fechaRechazo@");
        item.setDescripcion("Fecha rechazo");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@hora@");
        item.setDescripcion("Hora");
        listaEtiquetas.add(item);
        
        item = new SelectItem();
        item.setCodigo("@tiporegistro@");
        item.setDescripcion("Tipo de registro");
        listaEtiquetas.add(item);

        request.setAttribute("listaEtiquetas", listaEtiquetas);
    }
}
