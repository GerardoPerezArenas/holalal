package es.altia.flexia.integracion.moduloexterno.lanbide02;

import es.altia.flexia.integracion.moduloexterno.lanbide02.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.lanbide02.util.Utilities;
import es.altia.flexia.integracion.moduloexterno.lanbide02.vo.DatosExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.lanbide02.vo.DatosGruposCotizacionVO;
import es.altia.flexia.integracion.moduloexterno.lanbide02.vo.DocumentoLanbideVO;
import es.altia.flexia.integracion.moduloexterno.lanbide02.vo.ExpedienteLanbideVO;
import es.altia.flexia.integracion.moduloexterno.lanbide02.vo.GrupoCotizacionSumaVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DocumentoTramitacionModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class ModuloSubvencionLanbide02 extends ModuloIntegracionExterno{
    private final String MODULO_INTEGRACION = "/MODULO_INTEGRACION/";    
    private final String COD_PROCEDIMIENTO_EXP_RELACIONADOS = "/PANTALLA_TRAMITE/COD_PROCEDIMIENTO_EXP_RELACIONADOS";
    private final String PANTALLA_TRAMITE_SALIDA            = "/PANTALLA_TRAMITE/SALIDA";
    private final String CAMPOS_CONTRATOS_SOLICITADOS       = "/PANTALLA_TRAMITE/CAMPOS_CONTRATOS_SOLICITADOS";
    private final String CAMPOS_MESES_SOLICITADOS           = "/PANTALLA_TRAMITE/CAMPOS_MESES_SOLICITADOS";
    private final String CAMPOS_COSTES_SOLICITADOS          = "/PANTALLA_TRAMITE/CAMPOS_COSTES_SOLICITADOS";
    private final String CAMPOS_SUBVENCION_SOLICITADA       = "/PANTALLA_TRAMITE/CAMPOS_SUBVENCION_SOLICITADA";
    private final String CAMPOS_CONTRATOS_CONCEDIDOS        = "/PANTALLA_TRAMITE/CAMPOS_CONTRATOS_CONCEDIDOS";
    private final String CAMPOS_MESES_CONCEDIDOS            = "/PANTALLA_TRAMITE/CAMPOS_MESES_CONCEDIDOS";
    private final String CAMPOS_COSTES_CONCEDIDOS           = "/PANTALLA_TRAMITE/CAMPOS_COSTES_CONCEDIDOS";
    private final String CAMPOS_SUBVENCION_CONCEDIDA        = "/PANTALLA_TRAMITE/CAMPOS_SUBVENCION_CONCEDIDA";
    private final String COD_TRAMITE_DOCUMENTOS             = "/PANTALLA_TRAMITE/COD_TRAMITE_DOCUMENTOS";
    private final String PANTALLA_ERROR_DESCARGA_DOCUMENTO  = "/PANTALLA_ERROR_DESCARGA_DOCUMENTO";


    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_1  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_1";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_2  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_2";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_3  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_3";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_4  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_4";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_5  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_5";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_6  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_6";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_7  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_7";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_8  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_8";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_9  = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_9";
    private final String PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_10 = "/PANTALLA_EXPEDIENTE/CAMPOS_GRUPO_COTIZACION_10";
    private final String COD_PROCEDIMIENTO_EXP_RELACIONADOS_VISTA_EXPEDIENTE = "/PANTALLA_EXPEDIENTE/COD_PROCEDIMIENTO_EXP_RELACIONADOS";
    private final String PANTALLA_EXPEDIENTE_SALIDA                          = "/PANTALLA_EXPEDIENTE/SALIDA";
    private final String PANTALLA_EXPEDIENTE_METODOS_LLAMADA                 = "/PANTALLA_EXPEDIENTE/METODOS_LLAMADA";
    
    private final String BARRA                              = "/";
    private final String DOT_COMMA                          = ";";
    private final String DOT                                = ".";
    private Logger log = LogManager.getLogger(ModuloSubvencionLanbide02.class);
    
    /**
     * Esta operacións  se encarga de recuperar toda la información necesaria para poder configurar
     * la página de expediente para gestionar la conciliación de vida familiar y laboral
     * @param codOrganizacion: Código de la organización
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente: Número del expediente
     * @param request: HttpServletRequest para obtener los campos necesarios de la jsp
     * @param response: HttpServletResponse para enviar la respuesta a la jsp
     * @return String con la url de la jsp a la que se le pasa el control
     */
    public String prepararSumaGastosSalarialSeguridadSocial(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        String redireccion = null;

        String operacion = request.getParameter("operacion");
        log.debug("prepararSumaGastosSalarialSeguridadSocial ===========> ");
        if(numExpediente!=null && !"".equals(numExpediente)){
           String[] datos          = numExpediente.split(BARRA);
           String ejercicio        = datos[0];
           String codProcedimiento = datos[1];
            
           String codProcedimientoExpRelacionados = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + COD_PROCEDIMIENTO_EXP_RELACIONADOS,this.getNombreModulo());
           redireccion = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + PANTALLA_TRAMITE_SALIDA, this.getNombreModulo());
           log.debug("Código del procedimiento de los expedientes relacionados a recuperar: " + codProcedimientoExpRelacionados);
           if(codProcedimientoExpRelacionados!=null && !"".equals(codProcedimientoExpRelacionados)){
               salida = el.getExpedientesRelacionados(Integer.toString(codOrganizacion), numExpediente, codProcedimientoExpRelacionados, ejercicio);
               log.debug("salida status: " + salida.getStatus() + ", descStatus: " + salida.getDescStatus());
               if(salida.getStatus()==0){
                   ArrayList<ExpedienteModuloIntegracionVO> expedientes = salida.getExpedientesRelacionados();
                   log.debug("Número de expedientes relacionados recuperados: " + expedientes.size());

                   ArrayList<ExpedienteLanbideVO> expLanbide = new ArrayList<ExpedienteLanbideVO>();

                   double totalContratosSolicitados = 0;
                   double totalContratosConcedidos  = 0;
                   double totalMesesSolicitados     = 0;
                   double totalMesesConcedidos      = 0;
                   double totalCostesConcedidos     = 0;
                   double totalCostesSolicitados    = 0;
                   double totalSubvencionSolicitada = 0;
                   double totalSubvencionConcedida  = 0;

                   for(int i=0;i<expedientes.size();i++){

                       String codigosContratosSolicitados = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + CAMPOS_CONTRATOS_SOLICITADOS,this.getNombreModulo());
                       String codigosMesesSolicitados     = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + CAMPOS_MESES_SOLICITADOS,this.getNombreModulo());
                       String codigosCostesSolicitados    = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + CAMPOS_COSTES_SOLICITADOS,this.getNombreModulo());
                       String codigosSubvencionSolicitada = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + CAMPOS_SUBVENCION_SOLICITADA,this.getNombreModulo());

                       String codigosContratosConcedidos  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + CAMPOS_CONTRATOS_CONCEDIDOS,this.getNombreModulo());
                       String codigosMesesConcedidos      = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + CAMPOS_MESES_CONCEDIDOS,this.getNombreModulo());
                       String codigosCostesConcedidos     = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + CAMPOS_COSTES_CONCEDIDOS,this.getNombreModulo());
                       String codigosSubvencionConcedida  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + CAMPOS_SUBVENCION_CONCEDIDA,this.getNombreModulo());

                       log.debug("codigosContratosSolicitados: " + codigosContratosSolicitados);
                       log.debug("codigosMesesSolicitados: " + codigosMesesSolicitados);
                       log.debug("codigosCostesSolicitados: " + codigosCostesSolicitados);
                       log.debug("codigosSubvencionSolicitada: " + codigosSubvencionSolicitada);

                       log.debug("codigosContratosConcedidos: " + codigosContratosConcedidos);
                       log.debug("codigosMesesConcedidos: " + codigosMesesConcedidos);
                       log.debug("codigosCostesConcedidos: " + codigosCostesConcedidos);
                       log.debug("codigosSubvencionConcedida: " + codigosSubvencionConcedida);
                        
                       ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(i));
                       double sumaContratosSolicitados = 0;
                       double sumaContratosConcedidos  = 0;
                       double sumaMesesSolicitados     = 0;
                       double sumaMesesConcedidos      = 0;
                       double sumaCostesSolicitados    = 0;
                       double sumaCostesConcedidos     = 0;
                       double sumaSubvencionConcedida  = 0;
                       double sumaSubvencionSolicitada = 0;

                       if(Utilities.cadenaNoVacia(codigosContratosSolicitados) && Utilities.cadenaNoVacia(codigosContratosConcedidos) && Utilities.cadenaNoVacia(codigosMesesSolicitados)
                               && Utilities.cadenaNoVacia(codigosMesesConcedidos) && Utilities.cadenaNoVacia(codigosCostesSolicitados) && Utilities.cadenaNoVacia(codigosCostesConcedidos)
                               && Utilities.cadenaNoVacia(codigosSubvencionSolicitada) && Utilities.cadenaNoVacia(codigosSubvencionConcedida)){

                           String[] listaCodigosContratosSolicitados = codigosContratosSolicitados.split(DOT_COMMA);
                           String[] listaCodigosContratosConcedidos  = codigosContratosConcedidos.split(DOT_COMMA);
                           String[] listaCodigosMesesSolicitados     = codigosMesesSolicitados.split(DOT_COMMA);
                           String[] listaCodigosMesesConcedidos      = codigosMesesConcedidos.split(DOT_COMMA);
                           String[] listaCodigosCostesSolicitados    = codigosCostesSolicitados.split(DOT_COMMA);
                           String[] listaCodigosCostesConcedidos     = codigosCostesConcedidos.split(DOT_COMMA);
                           String[] listaCodigosSubvencionSolicitada = codigosSubvencionSolicitada.split(DOT_COMMA);
                           String[] listaCodigosSubvencionConcedida  = codigosSubvencionConcedida.split(DOT_COMMA);

                           // Suma de los campos suplementarios que pertenecen al grupo de contratos solicitados
                           sumaContratosSolicitados = this.getSumaCamposGrupo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), listaCodigosContratosSolicitados);
                           // Suma de los campos suplementarios que pertenecen al grupo de contratos concedidos
                           sumaContratosConcedidos = this.getSumaCamposGrupo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), listaCodigosContratosConcedidos);

                           // Suma de los campos suplementarios que pertenecen al grupo de meses solicitados
                           sumaMesesSolicitados = this.getSumaCamposGrupo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), listaCodigosMesesSolicitados);
                           // Suma de los campos suplementarios que pertenecen al grupo de meses concedidos
                           sumaMesesConcedidos  = this.getSumaCamposGrupo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), listaCodigosMesesConcedidos);

                           // Suma de los campos suplementarios que pertenecen al grupo de costes solicitados
                           sumaCostesSolicitados  = this.getSumaCamposGrupo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), listaCodigosCostesSolicitados);
                           // Suma de los campos suplementarios que pertenecen al grupo de costes concedidos
                           sumaCostesConcedidos   = this.getSumaCamposGrupo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), listaCodigosCostesConcedidos);

                           // Suma de los campos suplementarios que pertenecen al grupo de subvención solicitada
                           sumaSubvencionSolicitada  = this.getSumaCamposGrupo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), listaCodigosSubvencionSolicitada);
                           // Suma de los campos suplementarios que pertenecen al grupo de subvención concedida
                           sumaSubvencionConcedida   = this.getSumaCamposGrupo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), listaCodigosSubvencionConcedida);

                           totalContratosSolicitados = totalContratosSolicitados + sumaContratosSolicitados;
                           totalContratosConcedidos  = totalContratosConcedidos + sumaContratosConcedidos;
                           totalMesesSolicitados     = totalMesesSolicitados + sumaMesesSolicitados;
                           totalMesesConcedidos      = totalMesesConcedidos + sumaMesesConcedidos;
                           totalCostesConcedidos     = totalCostesConcedidos + sumaCostesConcedidos;
                           totalCostesSolicitados    = totalCostesSolicitados + sumaCostesSolicitados;
                           totalSubvencionSolicitada = totalSubvencionSolicitada + sumaSubvencionSolicitada;
                           totalSubvencionConcedida  = totalSubvencionConcedida  + sumaSubvencionConcedida;

                          

                       }// if

                       // Se almacena en el expediente los totales de cada agrupación de campos suplementarios
                       exp.setSumaContratosSolicitados(Utilities.formatoNumeroDecimales(sumaContratosSolicitados));
                       exp.setSumaContratosConcedidos(Utilities.formatoNumeroDecimales(sumaContratosConcedidos));
                       exp.setSumaCostesConcedidos(Utilities.formatoNumeroDecimales(sumaCostesConcedidos));
                       exp.setSumaCostesSolicitados(Utilities.formatoNumeroDecimales(sumaCostesSolicitados));
                       exp.setSumaMesesConcedidos(Utilities.formatoNumeroDecimales(sumaMesesConcedidos));
                       exp.setSumaMesesSolicitados(Utilities.formatoNumeroDecimales(sumaMesesSolicitados));
                       exp.setSumaSubvencionConcedida(Utilities.formatoNumeroDecimales(sumaSubvencionConcedida));
                       exp.setSumaSubvencionSolicitada(Utilities.formatoNumeroDecimales(sumaSubvencionSolicitada));

                       expLanbide.add(exp);
                      
                   }// for

                   DatosExpedienteVO deVO = new DatosExpedienteVO();
                   deVO.setExpedientes(expLanbide);
                   deVO.setTotalContratosConcedidos(Utilities.formatoNumeroDecimales(totalContratosConcedidos));
                   deVO.setTotalContratosSolicitados(Utilities.formatoNumeroDecimales(totalContratosSolicitados));
                   deVO.setTotalCostesConcedidos(Utilities.formatoNumeroDecimales(totalCostesConcedidos));
                   deVO.setTotalCostesSolicitados(Utilities.formatoNumeroDecimales(totalCostesSolicitados));
                   deVO.setTotalMesesConcedidos(Utilities.formatoNumeroDecimales(totalMesesConcedidos));
                   deVO.setTotalMesesSolicitados(Utilities.formatoNumeroDecimales(totalMesesSolicitados));
                   deVO.setTotalSubvencionConcedida(Utilities.formatoNumeroDecimales(totalSubvencionConcedida));
                   deVO.setTotalSubvencionSolicitada(Utilities.formatoNumeroDecimales(totalSubvencionSolicitada));

                   request.setAttribute("datos_expedientes",deVO);

               }//if
           }//if
        }

        log.debug("prepararSumaGastosSalarialSeguridadSocial <=====================");
        return redireccion;
    }

    /**
     * Obtiene la suma de los valores de los campos numéricos a nivel de expediente
     * @param codOrganizacion: Código de la organización
     * @param ejercicio: Ejercicio
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param codigosCampos: String[] con los códigos de los campos de los que se recuperan sus valores
     * @return double
     */
    private double getSumaCamposGrupo(String codOrganizacion,String ejercicio,String numExpediente,String codProcedimiento,String[] codigosCampos){
        double suma = 0;
        IModuloIntegracionExternoCamposFlexia instance = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        if(instance!=null){
            for(int i=0;codigosCampos!=null && i<codigosCampos.length;i++){
                log.debug("tratando campo: " + codigosCampos[i]);

                SalidaIntegracionVO salida = instance.getCampoSuplementarioExpediente(codOrganizacion, ejercicio, numExpediente, codProcedimiento, codigosCampos[i], IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                log.debug("Status: " + salida.getStatus() + ", descStatus: " + salida.getDescStatus());
                if(salida!=null && salida.getStatus()==0){
                    CampoSuplementarioModuloIntegracionVO campo = salida.getCampoSuplementario();
                    if(Utilities.cadenaNoVacia(campo.getValorNumero())){
                        suma =  suma + Double.parseDouble(campo.getValorNumero());
                    }
                    
                }//if
            }//for
        }//if

        return suma;
    }


   /**
     * Obtiene la suma de los valores de los campos numéricos a nivel de expediente
     * @param codOrganizacion: Código de la organización
     * @param ejercicio: Ejercicio
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param codigosCampos: String[] con los códigos de los campos de los que se recuperan sus valores
     * @return double
     */
    private double getValorCampo(String codOrganizacion,String ejercicio,String numExpediente,String codProcedimiento,String codigoCampo){
        double valor = 0;
        IModuloIntegracionExternoCamposFlexia instance = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        if(instance!=null){

            log.debug("tratando campo: " + codigoCampo);

            SalidaIntegracionVO salida = instance.getCampoSuplementarioExpediente(codOrganizacion, ejercicio, numExpediente, codProcedimiento, codigoCampo, IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
            log.debug("Status: " + salida.getStatus() + ", descStatus: " + salida.getDescStatus());
            if(salida!=null && salida.getStatus()==0){
                CampoSuplementarioModuloIntegracionVO campo = salida.getCampoSuplementario();
                if(Utilities.cadenaNoVacia(campo.getValorNumero())){
                    valor = Double.parseDouble(campo.getValorNumero());
                }

            }//if
        }//if

        return valor;
    }


    /**
     * Recupera los expedintes relacionados con el expediente actual que pertenezcan a un determinado expedienteEsta operacións  se encarga de recuperar toda la información necesaria para poder configurar
     * la página de expediente para gestionar la conciliación de vida familiar y laboral
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @param request: HttpServletRequest para obtener los campos necesarios de la jsp
     * @param response: HttpServletResponse para enviar la respuesta a la jsp
     * @return String con la url de la jsp a la que se le pasa el control
     */
   public String prepararExpedientesRelacionadosDocTramitacion(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
   SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        String redireccion = null;
                
        log.debug("prepararExpedientesRelacionadosDocTramitacion ================> ");
        if(numExpediente!=null && !"".equals(numExpediente)){
           String[] datos          = numExpediente.split(BARRA);
           String ejercicio        = datos[0];
           String codProcedimiento = datos[1];

           String codProcedimientoExpRelacionados = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + COD_PROCEDIMIENTO_EXP_RELACIONADOS,this.getNombreModulo());
           redireccion = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + PANTALLA_TRAMITE_SALIDA, this.getNombreModulo());
           log.debug("Código del procedimiento de los expedientes relacionados a recuperar: " + codProcedimientoExpRelacionados);
           if(codProcedimientoExpRelacionados!=null && !"".equals(codProcedimientoExpRelacionados)){
               salida = el.getExpedientesRelacionados(Integer.toString(codOrganizacion), numExpediente, codProcedimientoExpRelacionados, ejercicio);
               log.debug("salida status: " + salida.getStatus() + ", descStatus: " + salida.getDescStatus());
               if(salida.getStatus()==0){
                   ArrayList<ExpedienteModuloIntegracionVO> expedientes = salida.getExpedientesRelacionados();
                   log.debug("Número de expedientes relacionados recuperados: " + expedientes.size());

                   ArrayList<ExpedienteLanbideVO> expLanbide = new ArrayList<ExpedienteLanbideVO>();

                   //0/MODULO_INTEGRACION/MELANBIDE02/CTDES/2/PANTALLA_TRAMITE/COD_TRAMITE_DOCUMENTOS
                   String codTramiteDoc = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + BARRA + codTramite + COD_TRAMITE_DOCUMENTOS, this.getNombreModulo());
                   for(int i=0;i<expedientes.size();i++){
                       ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(i));

                       if(Utilities.esNumeroEntero(codTramiteDoc)){
                           // Se recupera la lista de documentos de tramitación del expediente relacionados
                           SalidaIntegracionVO salidaDocs = el.getListaDocumentosTramitacion(Integer.toString(codOrganizacion), exp.getNumExpediente(), exp.getCodProcedimiento(), Integer.parseInt(codTramiteDoc), -1, Integer.toString(exp.getEjercicio()));
                           log.debug("Salida al obtener documentos de tramitación status: " + salidaDocs.getStatus() + ", descStatus: " + salidaDocs.getDescStatus());
                           if(salidaDocs.getStatus()==0 && salidaDocs.getListaDocumentosTramitacion()!=null && salidaDocs.getListaDocumentosTramitacion().size()>=1){
                               DocumentoLanbideVO dlVO = Utilities.documentoTramitacionModuloIntegraciontoDocumentoLanbideVO(salidaDocs.getListaDocumentosTramitacion().get(0));
                               exp.setDocumentoTramitacion(dlVO);
                           }
                       }

                       expLanbide.add(exp);
                   }// for

                   request.setAttribute("expedientes_documentos",expLanbide);

               }//if
           }//if
        }//if

        log.debug("prepararExpedientesRelacionadosDocTramitacion <=====================");
        return redireccion;
   }

   /**
    * Recupera el contenido de un determinado documento de tramitación
    * @param codOrganizacion: Código de la organización
    * @param codTramite: Código del trámite
    * @param ocurrenciaTramite: Ocurrencia del trámite
    * @param numExpediente: Número del expediente
    * @param request: HttpServletRequest
    * @param response: HttpServletResponse
    * @return null si se ha podido recuperar el documento de tramitación o la url de la jsp de error si no se ha podido recuperar el documento
    */
   public String visualizarDocumentoTramitacion(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        log.debug("visualizarDocumentoTramitacion ==================>");
        String nombreDocumento = request.getParameter("nombreDocumento");
        String numeroDocumento = request.getParameter("numeroDocumento");
        String idioma          = request.getParameter("idioma");
        SalidaIntegracionVO salida = null;
        boolean hayRedireccion  = true;
        String redireccion = null;

        try{
            log.debug("nombreDocumento: " + nombreDocumento + ", numeroDocumento: " + numeroDocumento);
            if(nombreDocumento!=null && !"".equals(nombreDocumento) && numeroDocumento!=null && !"".equals(numeroDocumento)){
                String[] datosExp = numExpediente.split(BARRA);
                String codProcedimiento = datosExp[1];
                String ejercicio        = datosExp[0];

                IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();                
                salida = el.getDocumentoTramitacion(Integer.toString(codOrganizacion), numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, Integer.parseInt(numeroDocumento), ejercicio, nombreDocumento);
                                                                    
                redireccion = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + PANTALLA_ERROR_DESCARGA_DOCUMENTO,this.getNombreModulo());
                if(salida!=null) log.debug("status: " + salida.getStatus() + ", descStatus: " + salida.getDescStatus());

                if(salida!=null && salida.getStatus()==0){
                    DocumentoTramitacionModuloIntegracionVO doc = salida.getDocumentoTramitacion();
                    byte[] file = doc.getContenido();
                    if(file != null){

                        response.setContentType("application/octet-stream");
                        response.setHeader("Content-Disposition", "inline; filename=" + doc.getNombreDocumento() + DOT + doc.getExtensionDocumento());
                        
                        log.debug("DOCUMENTO OK ::: " + file.length);
                        response.setHeader("Content-Transfer-Encoding", "binary");
                        response.setContentLength(file.length);
                        ServletOutputStream out = response.getOutputStream();
                        BufferedOutputStream bos = new BufferedOutputStream(out);
                        bos.write(file, 0, file.length);
                        bos.close();
                        hayRedireccion = false;
                    }

                } else {
                    hayRedireccion = true;
                    log.error("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
                }
            }//if
        }catch(IOException e){
            e.printStackTrace();
        }

        log.debug("visualizarDocumentoTramitacion hayRedireccion " + hayRedireccion + " <==================");
        if(hayRedireccion){            
            request.setAttribute("idioma_usuario",idioma);            
            request.setAttribute("nombre_modulo",this.getNombreModulo());
            return redireccion;
        }
        else
            return null;        
   }





   /**
     * Esta método obtiene los expedientes de un determinado procedimiento que están relacionados con un expediente del procedimiento CTDES (configurable).
     * Para cada expediente relacionado se recuperan los valores de determinado campos suplementarios numéricos definidos a nivel de expediente, que se
     * van sumando para mostrarlos en la vista agrupados por grupo de cotización.
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite. Toma el valor -1 porque se trata de una operación de una vista de expediente.
     * @param ocurrenciaTramite: Ocurrencia del trámite. Toma el valor -1 porque se trata de una operación de una vista de expediente.
     * @param numExpediente: Número del expediente
     * @param request: HttpServletRequest para obtener los campos necesarios de la jsp
     * @param response: HttpServletResponse para enviar la respuesta a la jsp
     * @return String con la url de la jsp a la que se le pasa el control
     */
    public String prepararSumaGastosSalarialSeguridadSocialVistaExpediente(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        String redireccion = null;
        
        log.debug("prepararSumaGastosSalarialSeguridadSocialVistaExpediente  ===========> ");
        if(numExpediente!=null && !"".equals(numExpediente)){
           String[] datos          = numExpediente.split(BARRA);
           String ejercicio        = datos[0];
           String codProcedimiento = datos[1];
           
           String codProcedimientoExpRelacionados = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + COD_PROCEDIMIENTO_EXP_RELACIONADOS_VISTA_EXPEDIENTE,this.getNombreModulo());                                                                        
           redireccion = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_SALIDA, this.getNombreModulo());
           log.debug("Código del procedimiento de los expedientes relacionados a recuperar: " + codProcedimientoExpRelacionados);
           if(codProcedimientoExpRelacionados!=null && !"".equals(codProcedimientoExpRelacionados)){
               salida = el.getExpedientesRelacionados(Integer.toString(codOrganizacion), numExpediente, codProcedimientoExpRelacionados, ejercicio);
               log.debug("salida status: " + salida.getStatus() + ", descStatus: " + salida.getDescStatus());
               if(salida.getStatus()==0){
                   ArrayList<ExpedienteModuloIntegracionVO> expedientes = salida.getExpedientesRelacionados();
                   log.debug("Número de expedientes relacionados recuperados: " + expedientes.size());

                   String codigosCamposGrupoCotizacion1  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_1,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion2  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_2,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion3  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_3,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion4  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_4,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion5  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_5,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion6  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_6,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion7  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_7,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion8  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_8,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion9  = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_9,this.getNombreModulo());
                   String codigosCamposGrupoCotizacion10 = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_CAMPOS_GRUPOS_COTIZACION_10,this.getNombreModulo());                                                                                                
                   String ordenMetodosLlamada            = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_METODOS_LLAMADA,this.getNombreModulo());

                   log.debug("codigosCamposGrupoCotizacion1: " + codigosCamposGrupoCotizacion1);
                   log.debug("codigosCamposGrupoCotizacion2: " + codigosCamposGrupoCotizacion2);
                   log.debug("codigosCamposGrupoCotizacion3: " + codigosCamposGrupoCotizacion3);
                   log.debug("codigosCamposGrupoCotizacion4: " + codigosCamposGrupoCotizacion4);
                   log.debug("codigosCamposGrupoCotizacion5: " + codigosCamposGrupoCotizacion5);
                   log.debug("codigosCamposGrupoCotizacion6: " + codigosCamposGrupoCotizacion6);
                   log.debug("codigosCamposGrupoCotizacion7: " + codigosCamposGrupoCotizacion7);
                   log.debug("codigosCamposGrupoCotizacion8: " + codigosCamposGrupoCotizacion8);
                   log.debug("codigosCamposGrupoCotizacion9: " + codigosCamposGrupoCotizacion9);
                   log.debug("codigosCamposGrupoCotizacion10: " + codigosCamposGrupoCotizacion10);
                   log.debug("ordenMetodosLlamada: " + ordenMetodosLlamada);
                   
                   String[] listaGrupo1 = codigosCamposGrupoCotizacion1.split(DOT_COMMA);
                   String[] listaGrupo2 = codigosCamposGrupoCotizacion2.split(DOT_COMMA);
                   String[] listaGrupo3 = codigosCamposGrupoCotizacion3.split(DOT_COMMA);
                   String[] listaGrupo4 = codigosCamposGrupoCotizacion4.split(DOT_COMMA);
                   String[] listaGrupo5 = codigosCamposGrupoCotizacion5.split(DOT_COMMA);
                   String[] listaGrupo6 = codigosCamposGrupoCotizacion6.split(DOT_COMMA);
                   String[] listaGrupo7 = codigosCamposGrupoCotizacion7.split(DOT_COMMA);
                   String[] listaGrupo8 = codigosCamposGrupoCotizacion8.split(DOT_COMMA);
                   String[] listaGrupo9 = codigosCamposGrupoCotizacion9.split(DOT_COMMA);
                   String[] listaGrupo10 = codigosCamposGrupoCotizacion10.split(DOT_COMMA);
                   String[] listaOrdenMetodos = ordenMetodosLlamada.split(DOT_COMMA);

                   
                   ArrayList<GrupoCotizacionSumaVO> grupos = new ArrayList<GrupoCotizacionSumaVO>();
                   int grupo = 1;
                   GrupoCotizacionSumaVO gcsVO = new GrupoCotizacionSumaVO();

                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 1" ******/
                   for(int i=0;listaGrupo1!=null && i<listaGrupo1.length;i++){
                       String codCampo = listaGrupo1[i];
                       log.debug("Tratando código de campo grupo 1 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            log.debug("Tratando el expediente: " + exp.getNumExpediente());
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);
                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};

                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);                           
                       }
                                          

                   }//for

                   grupo++;
                   grupos.add(gcsVO);   
                   gcsVO = new GrupoCotizacionSumaVO();

                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 2" ******/
                   for(int i=0;listaGrupo2!=null && i<listaGrupo2.length;i++){
                       String codCampo = listaGrupo2[i];
                       log.debug("Tratando código de campo grupo 2 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};

                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);                           
                       }
                       
                   }//for

                   grupo++;
                   grupos.add(gcsVO);
                   gcsVO = new GrupoCotizacionSumaVO();


                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 3" ******/
                   for(int i=0;listaGrupo3!=null && i<listaGrupo3.length;i++){
                       String codCampo = listaGrupo3[i];
                       log.debug("Tratando código de campo grupo 3 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};

                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);                           
                       }

                   }//for

                   grupo++;
                   grupos.add(gcsVO);
                   gcsVO = new GrupoCotizacionSumaVO();


                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 4" ******/
                   for(int i=0;listaGrupo4!=null && i<listaGrupo4.length;i++){
                       String codCampo = listaGrupo4[i];
                       log.debug("Tratando código de campo grupo 4 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};

                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);                        
                       }

                   }//for

                   grupo++;
                   grupos.add(gcsVO);
                   gcsVO = new GrupoCotizacionSumaVO();


                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 5" ******/                   
                   for(int i=0;listaGrupo5!=null && i<listaGrupo5.length;i++){
                       String codCampo = listaGrupo5[i];
                       log.debug("Tratando código de campo grupo 5 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};

                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);                           
                       }

                   }//for

                   grupo++;
                   grupos.add(gcsVO);
                   gcsVO = new GrupoCotizacionSumaVO();


                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 6" ******/
                   for(int i=0;listaGrupo6!=null && i<listaGrupo6.length;i++){
                       String codCampo = listaGrupo6[i];
                       log.debug("Tratando código de campo grupo 6 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};
                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);
                       }
                   }//for

                   grupo++;
                   grupos.add(gcsVO);
                   gcsVO = new GrupoCotizacionSumaVO();


                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 7" ******/                   
                   for(int i=0;listaGrupo7!=null && i<listaGrupo7.length && listaOrdenMetodos!=null && listaOrdenMetodos.length==8;i++){
                       String codCampo = listaGrupo7[i];
                       log.debug("Tratando código de campo grupo 7 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};

                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);
                       }

                   }//for

                   grupo++;
                   grupos.add(gcsVO);
                   gcsVO = new GrupoCotizacionSumaVO();

                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 8" ******/                   
                   for(int i=0;listaGrupo8!=null && i<listaGrupo8.length && listaOrdenMetodos!=null && listaOrdenMetodos.length==8;i++){
                       String codCampo = listaGrupo8[i];
                       log.debug("Tratando código de campo grupo 8 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};
                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);
                       }

                   }//for

                   grupo++;
                   grupos.add(gcsVO);
                   gcsVO = new GrupoCotizacionSumaVO();


                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 9" ******/                   
                   for(int i=0;listaGrupo9!=null && i<listaGrupo9.length && listaOrdenMetodos!=null && listaOrdenMetodos.length==8;i++){
                       String codCampo = listaGrupo9[i];
                       log.debug("Tratando código de campo grupo 9 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};
                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);
                       }

                   }//for

                   grupo++;
                   grupos.add(gcsVO);
                   gcsVO = new GrupoCotizacionSumaVO();


                   /***** Obteniendo la suma de los campos suplementarios del grupo de cotización "Grupo 10" ******/
                   for(int i=0;listaGrupo10!=null && i<listaGrupo10.length && listaOrdenMetodos!=null && listaOrdenMetodos.length==8;i++){
                       String codCampo = listaGrupo10[i];
                       log.debug("Tratando código de campo grupo 10 " + codCampo);
                       double aux = 0;
                       for(int j=0;j<expedientes.size();j++){
                            ExpedienteLanbideVO exp = Utilities.expedienteModuloIntegracionVOtoExpedienteLanbideVO(expedientes.get(j));
                            double valor = this.getValorCampo(Integer.toString(exp.getCodMunicipio()),Integer.toString(exp.getEjercicio()),exp.getNumExpediente(), exp.getCodProcedimiento(), codCampo);
                            aux = aux + valor;
                            log.debug("valor: " + valor);

                       }//for

                       gcsVO.setGrupo(grupo);
                       if(Utilities.existePosicion(listaOrdenMetodos, i)){
                           String metodo = listaOrdenMetodos[i];
                           log.debug("Se va a ejecutar el método " + metodo);

                           Class[] tipoParametros     = {double.class};
                           Object[] valoresParametros = {aux};

                           gcsVO = (GrupoCotizacionSumaVO)Utilities.ejecutarMetodo(gcsVO, metodo, tipoParametros, valoresParametros);
                       }

                   }//for

                   grupos.add(gcsVO);
                   
                   DatosGruposCotizacionVO datosGrupos = new DatosGruposCotizacionVO();
                   datosGrupos.setGruposCotizacion(grupos);
                   datosGrupos = this.calcularTotales(datosGrupos);
                   request.setAttribute("DatosGruposCotizacion",datosGrupos);

               }//if
           }//if
        }

        log.debug("prepararSumaGastosSalarialSeguridadSocialVistaExpediente  <=====================");
        return redireccion;
    }


    /**
     * Calcula los totales por cada columna (Contratos solciitados, meses solicitados, costes soclitados, subvencion solicitada,.....)
     * @param datos: Objeto que contiene los totales por cada grupo de cotización
     * @return DatosGruposCotizacionVO
     */
    private DatosGruposCotizacionVO calcularTotales(DatosGruposCotizacionVO datos){

        ArrayList<GrupoCotizacionSumaVO> grupos =  datos.getGruposCotizacion();
        double totalContratosSolicitados = 0;
        double totalContratosConcedidos = 0;
        double totalMesesSolicitados = 0;
        double totalMesesConcedidos = 0;
        double totalCostesSolicitados = 0;
        double totalCostesConcedidos = 0;
        double totalSubvencionSolicitada = 0;
        double totalSubvencionConcedida  = 0;

        for(int i=0;grupos!=null && i<grupos.size();i++){
            /*
            totalContratosSolicitados = totalContratosSolicitados + Utilities.convertToDouble(grupos.get(i).getSumaContratosSolicitados());
            totalContratosConcedidos  = totalContratosConcedidos  + Utilities.convertToDouble(grupos.get(i).getSumaContratosConcedidos());
            totalMesesSolicitados     = totalMesesSolicitados + Utilities.convertToDouble(grupos.get(i).getSumaMesesSolicitados());
            totalMesesConcedidos      = totalMesesConcedidos  + Utilities.convertToDouble(grupos.get(i).getSumaMesesConcedidos());
            totalCostesSolicitados    = totalCostesSolicitados + Utilities.convertToDouble(grupos.get(i).getSumaCostesSolicitados());
            totalCostesConcedidos     = totalCostesConcedidos + Utilities.convertToDouble(grupos.get(i).getSumaCostesConcedidos());
            totalSubvencionSolicitada = totalSubvencionSolicitada + Utilities.convertToDouble(grupos.get(i).getSumaSubvencionSolicitada());
            totalSubvencionConcedida  = totalSubvencionConcedida + Utilities.convertToDouble(grupos.get(i).getSumaSubvencionConcedida());
             */
            totalContratosSolicitados = totalContratosSolicitados + grupos.get(i).getSumaContratosSolicitados();
            totalContratosConcedidos  = totalContratosConcedidos  + grupos.get(i).getSumaContratosConcedidos();
            totalMesesSolicitados     = totalMesesSolicitados + grupos.get(i).getSumaMesesSolicitados();
            totalMesesConcedidos      = totalMesesConcedidos  + grupos.get(i).getSumaMesesConcedidos();
            totalCostesSolicitados    = totalCostesSolicitados + grupos.get(i).getSumaCostesSolicitados();
            totalCostesConcedidos     = totalCostesConcedidos + grupos.get(i).getSumaCostesConcedidos();
            totalSubvencionSolicitada = totalSubvencionSolicitada + grupos.get(i).getSumaSubvencionSolicitada();
            totalSubvencionConcedida  = totalSubvencionConcedida + grupos.get(i).getSumaSubvencionConcedida();
        }

        datos.setTotalContratosConcedidos(totalContratosConcedidos);
        datos.setTotalContratosSolicitados(totalContratosSolicitados);
        datos.setTotalCostesConcedidos(totalCostesConcedidos);
        datos.setTotalCostesSolicitados(totalCostesSolicitados);
        datos.setTotalMesesConcedidos(totalMesesConcedidos);
        datos.setTotalMesesSolicitados(totalMesesSolicitados);
        datos.setTotalSubvencionConcedida(totalSubvencionConcedida);
        datos.setTotalSubvencionSolicitada(totalSubvencionSolicitada);
        
        /*
        datos.setTotalContratosConcedidos(Utilities.formatoNumeroDecimales(totalContratosConcedidos));
        datos.setTotalContratosSolicitados(Utilities.formatoNumeroDecimales(totalContratosSolicitados));
        datos.setTotalCostesConcedidos(Utilities.formatoNumeroDecimales(totalCostesConcedidos));
        datos.setTotalCostesSolicitados(Utilities.formatoNumeroDecimales(totalCostesSolicitados));
        datos.setTotalMesesConcedidos(Utilities.formatoNumeroDecimales(totalMesesConcedidos));
        datos.setTotalMesesSolicitados(Utilities.formatoNumeroDecimales(totalMesesSolicitados));
        datos.setTotalSubvencionConcedida(Utilities.formatoNumeroDecimales(totalSubvencionConcedida));
        datos.setTotalSubvencionSolicitada(Utilities.formatoNumeroDecimales(totalSubvencionSolicitada));
         */
        
        return datos;
    }

}