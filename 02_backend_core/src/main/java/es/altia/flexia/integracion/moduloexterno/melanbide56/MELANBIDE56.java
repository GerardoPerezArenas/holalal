/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide56;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide56.i18n.MeLanbide56I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide56.manager.MeLanbide56Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide56.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide56.util.ConstantesMeLanbide56;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.CampoSuplementario;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Domicilio;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Tercero;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.webservice.registro.cliente.wto.DomicilioVO;
import es.altia.flexia.webservice.registro.cliente.wto.InfoConexionVO;
import es.altia.flexia.webservice.registro.cliente.wto.RegistroVO;
import es.altia.flexia.webservice.registro.cliente.wto.RemitenteVO;
import es.altia.flexia.webservice.registro.cliente.wto.SalidaRegistroESBean;
import es.altia.flexia.webservice.registro.cliente.wsimpl.WSRegistroESBindingStub;
import es.altia.flexia.webservice.registro.cliente.wsimpl.WSRegistroESServiceLocator;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE56 extends ModuloIntegracionExterno
{
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE56.class);
    
    public String cargarPestanaAltaRegistro(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {    
        return "/jsp/extension/melanbide56/melanbide56.jsp";
    }
    
    public String altaRegistro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente)
    {
        int codigoResultado = 0;
        log.error("altaRegistro");
        try
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Direccion URL a configurar        
            String wsUrl = ConfigurationParameter.getParameter(ConstantesMeLanbide56.URL_WS_REGISTRO, ConstantesMeLanbide56.FICHERO_PROPIEDADES);
            log.error("wsUrl: " + wsUrl);
            //java.net.URL url = new java.net.URL("http://localhost:8081/WSRegistroES/services/WSRegistroESPort?wsdl");
            //java.net.URL url = new java.net.URL("http://10.168.212.21:7081/WSRegistroES/services/WSRegistroESPort?wsdl");
            java.net.URL url = new java.net.URL(wsUrl);
            WSRegistroESBindingStub binding = null;
            try
            {
                binding = (WSRegistroESBindingStub)
                          new WSRegistroESServiceLocator().getWSRegistroESPort(url);
                // Time out after a minute
                binding.setTimeout(60000);
            }catch(Exception ex)
            {
                log.error("eee", ex);
            }

            
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide56.FORMATO_FECHA);
            
            String[] datosExpediente = numExpediente.split(ConstantesMeLanbide56.BARRA_SEPARADORA);

            //Informacion de la conexion
            InfoConexionVO inf = new InfoConexionVO();
            inf.setOrganizacion(String.valueOf(codOrganizacion));
            inf.setAplicacion("RGI");
            
            Expediente exp = MeLanbide56Manager.getInstance().getDatosExpediente(codOrganizacion, numExpediente, adaptador);

            //Registro
            RegistroVO reg = new RegistroVO();
//            if(exp.getExrDep() != null)
//                reg.setDepartamento(exp.getExrDep()); //--> E_EXR.EXR_DEP
            reg.setDepartamento(1);
            //reg.setCodTipoEntradaSalida("1");
            reg.setEjercicio(datosExpediente[0]); //--> Del expediente
            reg.setCodUorRegistro(0); //1
            reg.setCodUorOrigenDestino(exp.getUorRegistro().toString()); //9999
            log.error("setCodUorOrigenDestino: "+exp.getUorRegistro().toString());
            reg.setFechaPresentacion(format.format(new java.util.Date())); //9999
            log.error("FechaPresentacion: "+format.format(new java.util.Date()));
            reg.setCodTipoDocumento(ConfigurationParameter.getParameter(ConstantesMeLanbide56.TIPO_DOC, ConstantesMeLanbide56.FICHERO_PROPIEDADES)); //--> R_RES.RES_TDO
            log.error("tipoDocumento: "+reg.getCodTipoDocumento());
            reg.setTipo("S");
            reg.setAsunto(ConfigurationParameter.getParameter(ConstantesMeLanbide56.ASUNTO, ConstantesMeLanbide56.FICHERO_PROPIEDADES) + " " + numExpediente); //--> Del expediente             
            log.error("Asunto: "+reg.getAsunto());
            reg.setObservaciones(ConfigurationParameter.getParameter(ConstantesMeLanbide56.OBSERVACIONES, ConstantesMeLanbide56.FICHERO_PROPIEDADES));//--> Del expediente    
            log.error("Observaciones: "+reg.getObservaciones());
//            reg.setNumTransporte(exp.getResNtr()); //-->R_RES.RES_NTR     
//            log.error("numTransporte: "+reg.getNumTransporte());
//            reg.setAutoridad(exp.getResAut()); //--> R_RES.RES_AUT    
//            log.error("Autoridad: "+reg.getAutoridad());
            /*???*/  //reg.setCodAsuntoCodificado("QWW");
            reg.setProcedimiento(datosExpediente[1]); //--> Del expediente
            
            
            //Interesados
            List<Tercero> interesados = MeLanbide56Manager.getInstance().getTercerosExpediente(codOrganizacion, Integer.parseInt(datosExpediente[0]), numExpediente, adaptador);
            
            RemitenteVO rem = null;
            DomicilioVO dom = null;
            Domicilio domTer = null;
            List<RemitenteVO> remitentes = new ArrayList<RemitenteVO>();
            
            for(Tercero ter : interesados)
            {
                //Interesado 1
                rem = new RemitenteVO();
                rem.setApe1(ter.getTerAp1());
                rem.setApe2(ter.getTerAp2());
                rem.setNombre(ter.getTerNom());
                rem.setTipoDoc(ter.getTerTid() != null ? ter.getTerTid().toString() : null);
                rem.setDocumento(ter.getTerDoc());
                rem.setRol(ter.getExtRol());
                domTer = ter.getDomicilio();
                if(domTer != null)
                {
                    dom = new DomicilioVO();         
                    dom.setCodMunicipio(domTer.getMun());
                    dom.setCodProvincia(domTer.getPrv());
                    dom.setCodPais(domTer.getPai());
                    dom.setTipoVia(domTer.getTvi());
                    dom.setVia(domTer.getNomVia());
                    dom.setEmplazamiento(domTer.getDmc());
                    rem.setDomicilio(dom);
                    /*???*/
                    /*CampoSuplementarioVO cs1 = new CampoSuplementarioVO();
                    CampoSuplementarioVO cs2 = new CampoSuplementarioVO();           
                    cs1.setCodCampo("TNUMSEGSOCIAL"); 
                    cs1.setValorCampo("2222222");
                    cs2.setCodCampo("TIKUSTERVER"); 
                    cs2.setValorCampo("14");
                    rem.setCamposSuplementarios(new CampoSuplementarioVO[]{cs1,cs2});*/
                }
                remitentes.add(rem);
            }
            if(remitentes.size() > 0)
                reg.setInteresados(remitentes.toArray(new RemitenteVO[]{}));
            reg.setExpRelacionado(numExpediente);
            SalidaRegistroESBean value = null;
            value = binding.setRegistroES(reg,inf);

            log.error("Estado: " + value.getStatus());
            log.error("Descripcion: " + value.getDescStatus());
            log.error("Numero:" + value.getNumero());
            log.error("FechaPresentacion: " + value.getFecha());
            log.error("Ejercicio: " + value.getEjercicio());   
           // value.
            if(value.getStatus() == 0)
            {

                //log.debug("Registro:" + value.getRegistros().length);
                if (value.getNumero() != null){
                    //RegistroVO registro = value.getRegistros()[0];
                    log.debug("Numero:" + value.getNumero());
                    log.debug("FechaPresentacion: " + value.getFecha());
                    log.debug("Ejercicio: " + value.getEjercicio()); 

                    String codCampoNumero = ConfigurationParameter.getParameter(ConstantesMeLanbide56.CAMPO_SUPL_NUMREGSALIDA, ConstantesMeLanbide56.FICHERO_PROPIEDADES);
                    String codCampoFecha = ConfigurationParameter.getParameter(ConstantesMeLanbide56.CAMPO_SUPL_FECREGSALIDA, ConstantesMeLanbide56.FICHERO_PROPIEDADES);



                    MeLanbide56Manager manager = MeLanbide56Manager.getInstance();

                    try
                    {
                        
                        boolean result = false;
                        Date fecha = format.parse(value.getFecha());
                        //String campoNumRegSalida = value.getNumero();
                        List<CampoSuplementario> camposSuplementarios = new ArrayList<CampoSuplementario>();
                        
                        CampoSuplementario campoFecRegSalida = new CampoSuplementario();
                        campoFecRegSalida.setCodCampo(codCampoFecha);
                        campoFecRegSalida.setTipoDato(ConstantesMeLanbide56.TIPOS_DATOS_SUPLEMENTARIOS.FECHA);
                        campoFecRegSalida.setValor(format.format(new java.util.Date()));
                        camposSuplementarios.add(campoFecRegSalida);                        
                        
                        CampoSuplementario campoNumRegSalida = new CampoSuplementario();
                        campoNumRegSalida.setCodCampo(codCampoNumero);
                        campoNumRegSalida.setTipoDato(ConstantesMeLanbide56.TIPOS_DATOS_SUPLEMENTARIOS.NUMERICO);
                        campoNumRegSalida.setValor(Integer.parseInt(value.getNumero()));
                        camposSuplementarios.add(campoNumRegSalida);
                        
                        
//                        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
//                    
//                        //grabamos el número de censo como dato suplementario
//                        CampoSuplementarioModuloIntegracionVO campo3 = new CampoSuplementarioModuloIntegracionVO();
//                        campo3.setCodOrganizacion(Integer.toString(codOrganizacion));
//                        campo3.setCodProcedimiento(datosExpediente[1]);
//                        campo3.setEjercicio(datosExpediente[0]);
//                        campo3.setCodigoCampo(codCampoFecha);
//                        campo3.setNumExpediente(numExpediente);
//                        campo3.setValorTexto(fecha.toString());
//                        campo3.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
//                    
//                        SalidaIntegracionVO salidaCampoFecha =el.grabarCampoSuplementario(campo3);
//                        
//                        log.error("fecha grabado: " + salidaCampoFecha.getStatus());
//                        if(salidaCampoFecha.getStatus()!=0){
//                            codigoResultado = 1;
//                        }
//                        CampoSuplementarioModuloIntegracionVO campo = new CampoSuplementarioModuloIntegracionVO();
//                        campo.setCodOrganizacion(Integer.toString(codOrganizacion));
//                        campo.setCodProcedimiento(datosExpediente[1]);
//                        campo.setEjercicio(datosExpediente[0]);
//                        campo.setCodigoCampo(codCampoNumero);
//                        campo.setNumExpediente(numExpediente);
//                        campo.setValorTexto(campoNumRegSalida);
//                        campo.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
//                    
//                        SalidaIntegracionVO salidaCampoNum =el.grabarCampoSuplementario(campo);
//                        log.error("num registro grabado: " + salidaCampoNum.getStatus());
//                        if(salidaCampoNum.getStatus()!=0){
//                            codigoResultado = 1;
//                        }
                        result = manager.grabarCamposSuplementariosTramite(codOrganizacion, datosExpediente[1], Integer.parseInt(datosExpediente[0]), numExpediente, codTramite, ocurrenciaTramite, camposSuplementarios, adaptador);

                        if(result)
                        {
                            codigoResultado = 0;
                        }
                        else
                        {
                            codigoResultado = 1;
                        }
                    }
                    catch(Exception ex)
                    {
                        log.error(ex);
                        codigoResultado = 2;
                    }
                }
            }
            else
            {
                log.error("value.getStatus(): " + value.getStatus());
                codigoResultado = 3;
            }
        }
        catch (javax.xml.rpc.ServiceException jre)
        {
            log.error("Error en altaRegistro1: " , jre);
            codigoResultado = 3;
        }
        catch(Exception ex)
        {
            log.error("Error en altaRegistro2: " , ex);
            codigoResultado = 4;
        }
        return String.valueOf(codigoResultado);
    }
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
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
}
