package es.altia.flexia.integracion.moduloexterno.lanbide01;

import es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.exception.MeLanbide01Exception;
import es.altia.flexia.integracion.moduloexterno.lanbide01.manager.MeLanbide01Manager;
import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.MeLanbide01DatosCalculoDao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.MeLanbide01Constantes;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Utilities;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.AlarmaVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosCalculoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosPeriodoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01DepenPerSut;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01HistoSubv;
import es.altia.flexia.integracion.moduloexterno.plugin.*;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.lanbide01.i18n.MeLanbide01I18n;
import es.altia.flexia.integracion.moduloexterno.lanbide01.manager.MeLanbide01OtherServices;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConnectionUtils;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Melanbide01DecretoExpedienteEnum;
import es.altia.flexia.integracion.moduloexterno.lanbide01.validator.MeLanbide01Validator;
import es.altia.flexia.integracion.moduloexterno.lanbide01.validator.MeLanbide01ValidatorUtils;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.MeLanbide01ValidatorResult;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01Decreto;
import java.io.IOException;
/**
 * Módulo de subvenciones de Lanbide
 */
public class ModuloSubvencionLanbide extends ModuloIntegracionExterno{

    //Logger 
    private Logger log = LogManager.getLogger(ModuloSubvencionLanbide.class);
    
    //Cantidad de dias de los meses de un año normal
    private static final Map<Integer, Integer> MONTHS = Collections.synchronizedMap(new HashMap<Integer, Integer>());
    static {
        MONTHS.put(0, 31);
        MONTHS.put(1, 28);
        MONTHS.put(2, 31);
        MONTHS.put(3, 30);
        MONTHS.put(4, 31);
        MONTHS.put(5, 30);
        MONTHS.put(6, 31);
        MONTHS.put(7, 31);
        MONTHS.put(8, 30);
        MONTHS.put(9, 31);
        MONTHS.put(10,30);
        MONTHS.put(11, 31);
    }//MONTHS
    //Cantidad de dias de los meses de un año bisiesto
    private static final Map<Integer, Integer> MONTHS_LEAP = Collections.synchronizedMap(new HashMap<Integer, Integer>());
    static {
        MONTHS_LEAP.put(0, 31);
        MONTHS_LEAP.put(1, 29);
        MONTHS_LEAP.put(2, 31);
        MONTHS_LEAP.put(3, 30);
        MONTHS_LEAP.put(4, 31);
        MONTHS_LEAP.put(5, 30);
        MONTHS_LEAP.put(6, 31);
        MONTHS_LEAP.put(7, 31);
        MONTHS_LEAP.put(8, 30);
        MONTHS_LEAP.put(9, 31);
        MONTHS_LEAP.put(10,30);
        MONTHS_LEAP.put(11, 31);
    }//MONTHS_LEAP
    
    // Formateador de Fecha RegistroLog
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFecha_dd_MM_yyyy = new SimpleDateFormat("dd/MM/yyyy");
    private MeLanbide01ValidatorUtils meLanbide01ValidatorUtils = new MeLanbide01ValidatorUtils();
    private MeLanbide01OtherServices meLanbide01OtherServices = new MeLanbide01OtherServices();
    
     // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public void eliminarPeriodo (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
        if(log.isDebugEnabled()) log.debug("eliminarPeriodo() : BEGIN");
        ArrayList<DatosPeriodoVO> periodos = new ArrayList<DatosPeriodoVO>();
        ArrayList<DatosPeriodoVO> nuevoPeriodos = new ArrayList<DatosPeriodoVO>();
        String error = MeLanbide01Constantes.TODO_CORRECTO;
        
        if(log.isDebugEnabled()) log.debug("Recuperamos los periodos");
        String stringPeriodos = request.getParameter("periodos");
        //Separamos los periodos
        String[] arrayPeriodos = stringPeriodos.split(MeLanbide01Constantes.SEPARADOR_REGISTRO);
        for(int x=0; x<arrayPeriodos.length; x++){
            DatosPeriodoVO periodo = new DatosPeriodoVO();
            //Separamos los elementos
            String[] elementosPeriodo = arrayPeriodos[x].split(MeLanbide01Constantes.SEPARADOR_ELEMENTO);
            try {
                //Elemento[0]
                Calendar fechaInicio  = Calendar.getInstance();
                    fechaInicio.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[0]));
                periodo.setFechaInicio(fechaInicio);
                //Elemento[1]
                Calendar fechaFin = Calendar.getInstance();
                    fechaFin.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[1]));
                periodo.setFechaFin(fechaFin);
                //Elemento[2]
                String porcSubvenc = elementosPeriodo[2];
                periodo.setPorcSubven(porcSubvenc);
                //Elemento[3]
                String numDias = elementosPeriodo[3];
                periodo.setNumDias(Integer.valueOf(numDias));
                //Elemento[4]
                String baseCotizacion = elementosPeriodo[4];
                periodo.setBaseCotizacion(baseCotizacion);
                //Elemento[5]
                String bonificacion = elementosPeriodo[5];
                periodo.setBonificacion(bonificacion);
                //Elemento[6]
                String gasto = elementosPeriodo[6];
                periodo.setGasto(gasto);
                
                String porcJornRealizada = elementosPeriodo[7];
                porcJornRealizada=(porcJornRealizada!= null && !porcJornRealizada.equalsIgnoreCase("") && porcJornRealizada!="null" ? porcJornRealizada :"0");
                periodo.setPorcJornRealizada(Double.parseDouble(porcJornRealizada));
                
                String porcJornSutitucion = elementosPeriodo[8];
                porcJornSutitucion=(porcJornSutitucion!= null && !porcJornSutitucion.equalsIgnoreCase("") && porcJornSutitucion!="null" ? porcJornSutitucion :"0");
                periodo.setPorcJornSustitucion(Double.parseDouble(porcJornSutitucion));
                
                periodos.add(periodo);
            } catch (ParseException ex) {
                log.debug("Se ha producido un error recuperando los periodos");
            }//try-catch
        }//for(int x=0; x<arrayPeriodos.length; x++)
        //Fin periodos
        
        //Comprobamos que el numero de periodos sea superior a uno. si no es asi no permitiremos borrar, para que quede al menos
        //un periodo
        if(periodos.size() > 1){
            //Recogemos el indice seleccionado para eliminar
            String indiceSeleccionado = (String) request.getParameter("indiceSeleccionado");
            Integer indice = Integer.valueOf(indiceSeleccionado);
            //Comprobamos que se haya seleccionado la primera o la ultima fila para eliminar
            if(indice.equals(0) || (indice.equals(periodos.size()-1))){
                //ELiminamos la fila del indice seleccionado
                for(Integer x=0; x<periodos.size(); x++){
                    if(!x.equals(indice)){
                        nuevoPeriodos.add(periodos.get(x));
                    }//if(!x.equals(indice))
                }//for(int x=0; x<periodos.size(); x++)
            }else{
                error = MeLanbide01Constantes.ERROR_BORRAR_FILA_PERIODO_INTERMEDIO;
            }//if(indice.equals(1) || (indice.equals(periodos.size())))
        }else{
            error = MeLanbide01Constantes.ERROR_BORRAR_ULTIMA_FILA;
        }//if(periodos.size() > 1)
        
        /*********************************************************************************************/
        /*********************************************************************************************/
        /*** Calculamos los dias subvencionables restantes para el interesado contratado           ***/
        /*********************************************************************************************/
        /*********************************************************************************************/
        Integer diasRestantes = new Integer(0);
        if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
            try{
                String[] datos          = numExpediente.split(MeLanbide01Constantes.BARRA);
                String ejercicio        = datos[0];
                String codProcedimiento = datos[1];
                
                Integer numeroTotalDias = Integer.valueOf(calcularNumeroTotalDias(nuevoPeriodos));
                Melanbide01Decreto decretoAplicable = meLanbide01ValidatorUtils.getDecretoAplicableExptePorFecInicioPeriodosOrFecIniAcciSubvConcedida(codOrganizacion, numExpediente);
                // POr defecto Asignamos el Decreto inicia 177/2010
                String decretoAplicableCod=(decretoAplicable!=null ? decretoAplicable.getDecretoCodigo() : Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto()); 
                diasRestantes = meLanbide01ValidatorUtils.diasRestantesSubvencionables2
                        (String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio,numeroTotalDias,decretoAplicableCod);
            }catch(MeLanbide01Exception meEx){
                log.error("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol de persona"
                        + " contratada " + meEx.getMessage());
                error = MeLanbide01Constantes.ERROR_CALCULANDO_DIAS_RESTANTES;
            }//try-catch
        }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        
        log.info("Validacion fuera de plazo pasada a validator ..");
        // Comprobaciones si el expedientes esta fuera de plazo
//        if (error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)) {
//            String[] datos = numExpediente.split(MeLanbide01Constantes.BARRA);
//            String ejercicio = datos[0];
//            String codProcedimiento = datos[1];
//            
//            String campoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_SOLICITUD", this.getNombreModulo());
//            String tipoCampoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_SOLICITUD", this.getNombreModulo());
//            String campoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", this.getNombreModulo());
//            String tipoCampoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", this.getNombreModulo());
//            if (meLanbide01ValidatorUtils.compruebaSiExpedienteFueraPlazo(codOrganizacion, ejercicio, codProcedimiento, numExpediente,
//                    campoSuplementarioFechaSolicitud, tipoCampoSuplementarioFechaSolicitud,
//                    campoSuplementarioFechaFinActuacionConcedido, tipoCampoSuplementarioFechaFinActuacionConcedido)) {
//                error = MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO;
//            }
//        }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(error);
            xmlSalida.append("</CODIGO_OPERACION>");
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                if(nuevoPeriodos.size() > 0){
                    xmlSalida.append("<TOTAL_DIAS>");
                        xmlSalida.append(calcularNumeroTotalDias(nuevoPeriodos));
                    xmlSalida.append("</TOTAL_DIAS>");
                    xmlSalida.append("<DIAS_SUBVENCIONABLES_RESTANTES>");
                            xmlSalida.append(diasRestantes);
                    xmlSalida.append("</DIAS_SUBVENCIONABLES_RESTANTES>");
                    xmlSalida.append("<GASTO_TOTAL>");
                        xmlSalida.append(calcularGastoTotal(nuevoPeriodos));
                    xmlSalida.append("</GASTO_TOTAL>");
                    xmlSalida.append("<PERIODOS>");
                        for(DatosPeriodoVO periodo : nuevoPeriodos){
                            xmlSalida.append("<PERIODO>");
                                xmlSalida.append("<INICIO_PERIODO>");
                                    xmlSalida.append(periodo.getFechaInicioAsString());
                                xmlSalida.append("</INICIO_PERIODO>");
                                xmlSalida.append("<FIN_PERIODO>");
                                    xmlSalida.append(periodo.getFechaFinAsString());
                                xmlSalida.append("</FIN_PERIODO>");
                                xmlSalida.append("<NUM_DIAS>");
                                    xmlSalida.append(periodo.getNumDias());
                                xmlSalida.append("</NUM_DIAS>");
                                if(periodo.getBaseCotizacion() != null && !periodo.getBaseCotizacion().equalsIgnoreCase("")){
                                    xmlSalida.append("<BASE_COTIZACION>");
                                        xmlSalida.append(periodo.getBaseCotizacion());
                                    xmlSalida.append("</BASE_COTIZACION>");
                                }//if(periodo.getBaseCotizacion() != null && !periodo.getBaseCotizacion().equalsIgnoreCase(""))
                                if(periodo.getReducPerSust() != null && !periodo.getReducPerSust().equalsIgnoreCase("")){
                                    xmlSalida.append("<REDUCPERSUST>");
                                        xmlSalida.append(periodo.getReducPerSust());
                                    xmlSalida.append("</REDUCPERSUST>");
                                }//if(periodo.getReducPerSust() != null && !periodo.getReducPerSust().equalsIgnoreCase(""))
                                if(periodo.getJornPersSust() != null && !periodo.getJornPersSust().equalsIgnoreCase("")){
                                    xmlSalida.append("<JORNPERSSUST>");
                                        xmlSalida.append(periodo.getJornPersSust());
                                    xmlSalida.append("</JORNPERSSUST>");
                                }//if(periodo.getJornPersSust() != null && !periodo.getJornPersSust().equalsIgnoreCase(""))
                                if(periodo.getJornPersCont() != null && !periodo.getJornPersCont().equalsIgnoreCase("")){
                                    xmlSalida.append("<JORNPERSCONT>");
                                        xmlSalida.append(periodo.getJornPersCont());
                                    xmlSalida.append("</JORNPERSCONT>");
                                }//if(periodo.getJornPersCont() != null && !periodo.getJornPersCont().equalsIgnoreCase(""))
                                if(periodo.getBonificacion() != null && !periodo.getBonificacion().equalsIgnoreCase("")){
                                    xmlSalida.append("<BONIFICACION>");
                                        xmlSalida.append(periodo.getBonificacion());
                                    xmlSalida.append("</BONIFICACION>");
                                }//if(periodo.getBonificacion() != null && !periodo.getBonificacion().equalsIgnoreCase(""))
                                if(periodo.getPorcSubven() != null && !periodo.getPorcSubven().equalsIgnoreCase("")){
                                    xmlSalida.append("<PORC_SUBVENC>");
                                        xmlSalida.append(periodo.getPorcSubven());
                                    xmlSalida.append("</PORC_SUBVENC>");
                                }//if(periodo.getPorcSubven() != null && !periodo.getPorcSubven().equalsIgnoreCase(""))
                                if(periodo.getGasto() != null && !periodo.getGasto().equalsIgnoreCase("")){
                                    xmlSalida.append("<GASTO>");
                                        xmlSalida.append(periodo.getGasto());
                                    xmlSalida.append("</GASTO>");
                                }//if(periodo.getGasto != null && !periodo.getGasto().equalsIgnoreCase(""))
                                if(periodo.getPorcJornRealizada()!= null){
                                    xmlSalida.append("<PORC_JORN_REALIZADA>");
                                        xmlSalida.append(periodo.getPorcJornRealizada());
                                    xmlSalida.append("</PORC_JORN_REALIZADA>");
                                }
                                if(periodo.getPorcJornSustitucion()!= null){
                                    xmlSalida.append("<PORC_JORN_SUSTITUCION>");
                                        xmlSalida.append(periodo.getPorcJornSustitucion());
                                    xmlSalida.append("</PORC_JORN_SUSTITUCION>");
                                }
                            xmlSalida.append("</PERIODO>");
                        }//for(DatosPeriodoVO periodo : periodos)
                    xmlSalida.append("</PERIODOS>");
                }//if(periodos.size() > 0)
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Se ha producido un error comprobando el nuevo tramo ", e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("eliminarPeriodo() : END");
    }//eliminarPeriodo
    
    
    private Calendar toCalendar(String dt, String fmt) {
        Calendar result = null;
        if (dt!=null) {
            result= toCalendar(toDate(dt,fmt));
        }//if
        return result;
    }//toCalendar
    
    private Calendar toCalendar(java.util.Date dt) {
        Calendar result = null;

        if (dt!=null) {
            result = Calendar.getInstance();
            result.clear();
            result.setTime(dt);
        }//if

        return result;
    }//toCalendar
    
    
    public static final java.util.Date toDate(String dt, String fmt) {
        java.util.Date result;

        try {
            final SimpleDateFormat formatter = new SimpleDateFormat(fmt);
            formatter.setLenient(false);
            result = formatter.parse(dt);
        } catch (Exception e) {
            result = null;
        }//try-catch

        return result;
    }//toDate
    
    
    private ArrayList<DatosPeriodoVO> convertirDatosPeriodo(String periodos){
        ArrayList<DatosPeriodoVO> salida = new ArrayList<DatosPeriodoVO>();       
        if(periodos!=null && !periodos.isEmpty()){
            log.info("periodos : " + periodos);
            log.info("MeLanbide01Constantes.SEPARADOR_LINEA_PERIODO : " + MeLanbide01Constantes.SEPARADOR_LINEA_PERIODO);
            log.info("MeLanbide01Constantes.ALMOHADILLA : " + MeLanbide01Constantes.ALMOHADILLA);

            String[] filas = periodos.split(MeLanbide01Constantes.SEPARADOR_LINEA_PERIODO);
            for(int i=0;filas!=null && i<filas.length;i++){                
                String[] fila = filas[i].split(MeLanbide01Constantes.ALMOHADILLA);
                DatosPeriodoVO vo = new DatosPeriodoVO();
                String fecInicio=null;
                String fecFin=null; 
                String porcentaje=null;
                String dias=null;
                String baseCotizacion=null;
                String bonificacion=null;
                String gasto=null;
                String porcJornRealizada=null;
                String porcJornSustitucion=null;
                if(fila!=null){
                    int idx=0;
                    fecInicio = (fila.length > idx ? fila[idx++] : "");
                    log.info("fecInicio " + fecInicio);
                    fecFin = (fila.length > idx ? fila[idx++] : "");
                    log.info("fecFin " + fecFin);
                    porcentaje = (fila.length > idx ? fila[idx++] : "");
                    log.info("porcentaje " + porcentaje);
                    dias = (fila.length > idx ? fila[idx++] : "");
                    log.info("dias " + dias);
                    baseCotizacion = (fila.length > idx ? fila[idx++] : "");
                    log.info("baseCotizacion " + baseCotizacion);
                    bonificacion = (fila.length > idx ? fila[idx++] : "");
                    log.info("bonificacion " + bonificacion);
                    gasto = (fila.length > idx ? fila[idx++] : "");
                    log.info("gasto " + gasto);
                    porcJornRealizada = (fila.length > idx ? fila[idx++] : "");
                    log.info("porcJornRealizada " + porcJornRealizada);
                    porcJornSustitucion = (fila.length > idx ? fila[idx++] : "");
                    log.info("porcJornSustitucion " + porcJornSustitucion);
                }else{
                    log.info("Datos de periodos no recibidos correctamente para convertir a VO ...  fila a null" );
                }

                vo.setFechaInicio((fecInicio!=null && !fecInicio.isEmpty() ? toCalendar(fecInicio, "dd/MM/yyyy") : null));
                vo.setFechaFin((fecFin!=null && !fecFin.isEmpty() ? toCalendar(fecFin, "dd/MM/yyyy") : null));
                vo.setPorcSubven(porcentaje);
                vo.setNumDias((dias != null && !dias.isEmpty() ? Integer.parseInt(dias) : null));
                vo.setBaseCotizacion(baseCotizacion);
                vo.setBonificacion(bonificacion);
                vo.setGasto(gasto);
                vo.setPorcJornRealizada((porcJornRealizada != null && !porcJornRealizada.isEmpty() ? Double.valueOf(porcJornRealizada) : Double.valueOf("0")));
                vo.setPorcJornSustitucion((porcJornSustitucion != null && !porcJornSustitucion.isEmpty() ? Double.valueOf(porcJornSustitucion) : Double.valueOf("0")));
                salida.add(vo);
            }// for
        }        
        return salida;
        
    }
    
    
    /**
     * Esta operacion se encarga de actualizar los datos de la pestaña del modulo cuando se actualiza algun dato del expediente
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void recargarDatosPagina(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,
            HttpServletRequest request,HttpServletResponse response) throws SQLException{
        if(log.isDebugEnabled()) log.debug("recargarDatosPagina() : BEGIN");
        DatosCalculoVO datosCalculo = new DatosCalculoVO();
        ArrayList<DatosPeriodoVO> intervaloFechas = new ArrayList<DatosPeriodoVO>();
        MeLanbide01Manager meLanbide01Manager = MeLanbide01Manager.getInstance();
        String error = MeLanbide01Constantes.TODO_CORRECTO;
        
        //Campos suplementarios.
        Calendar fechaInicio = null;
        Calendar fechaFin = null;
        String jornPersCont = "";
        String jornPersSust = "";
        String reducPersSust = "";
        String porcSubvenc = "";
        boolean EXISTEN_PERIODOS = false;
        String periodos = request.getParameter("periodos");
        ArrayList<DatosPeriodoVO> periodosOriginales = convertirDatosPeriodo(periodos);
        
        log.debug("periodos: " + periodos);
        log.debug("recargarDatosPagina ===========> ");
        if(numExpediente!=null && !"".equals(numExpediente)){
            String[] datos          = numExpediente.split(MeLanbide01Constantes.BARRA);
            String ejercicio        = datos[0];
            String codProcedimiento = datos[1];
            
             try {
                //Intentamos recuperar los datos de la BBDD.
                datosCalculo = meLanbide01Manager.getDatosCalculo(numExpediente, String.valueOf(codOrganizacion), 
			ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            } catch (MeLanbide01Exception ex) {
                log.error("Se ha producido un error recuperando los datos del cálculo del expediente de la BBDD " + ex.getMessage(), ex);
            }//try-catch
             
            //Recuperamos el valor de los campos suplementarios de INICONTRATO y FINCONTRATO para calcular los intervalos
            //y el numero de dias.
//            String campoSuplementarioInicioContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.CAMPO_INICIO_CONTRATO, this.getNombreModulo());
//            String tipoCampoSuplementarioInicioContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_CAMPO_INICIO_CONTRATO, this.getNombreModulo());
//            String campoSuplementarioFinContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.CAMPO_FIN_CONTRATO, this.getNombreModulo());
//            String tipoCampoSuplementarioFinContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_CAMPO_FIN_CONTRATO, this.getNombreModulo());
//            String campoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO",this.getNombreModulo());
//            String tipoCampoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO",this.getNombreModulo());
//            String campoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_SOLICITUD",this.getNombreModulo());
//            String tipoCampoSuplementarioFechaSolicitud  = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_SOLICITUD",this.getNombreModulo());
//            
//            if(log.isDebugEnabled()){
//                log.debug("Campo suplementario inicio de contrato = " + campoSuplementarioInicioContrato);
//                log.debug("Tipo campo suplementario inicio de contrato = " + tipoCampoSuplementarioInicioContrato);
//                log.debug("Campo suplementario fin de contrato = " + campoSuplementarioFinContrato);
//                log.debug("Tipo campo suplementario fin de contrato = " + tipoCampoSuplementarioFinContrato);
//                log.debug("Campo suplementario fecha solicitud = " + campoSuplementarioFechaSolicitud);
//                log.debug("Tipo suplementario fecha solicitud = " + tipoCampoSuplementarioFechaSolicitud);
//                log.debug("Campo fecha accion subv. concedida = " + campoSuplementarioFechaFinActuacionConcedido);
//                log.debug("Tipo suplementario fecha accion subv. concedida = " + tipoCampoSuplementarioFechaFinActuacionConcedido);
//            }//if(log.isDebugEnabled())
                        
            /**
             * Todas las validaciones en este caso, se ejecutan despues de llamar a recargarDatoPagina en JSP mediante jquery y Ajax
             */
            
            // Comprobamos si expedientes esta fuera de plazo. -- dgc 26/01/2015
            log.info("Validacion Fuera plazo movida a Validator");
//            if(meLanbide01ValidatorUtils.compruebaSiExpedienteFueraPlazo(codOrganizacion, ejercicio, codProcedimiento, numExpediente,
//                    campoSuplementarioFechaSolicitud, tipoCampoSuplementarioFechaSolicitud,
//                    campoSuplementarioFechaFinActuacionConcedido, tipoCampoSuplementarioFechaFinActuacionConcedido)){
//                error = MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO;
//            }    

            try {
                fechaInicio = meLanbide01ValidatorUtils.getCampoSuplementarioFecInicioAccionSubvConcedida(codOrganizacion, String.valueOf(ejercicio), numExpediente, codProcedimiento);
                fechaFin = meLanbide01ValidatorUtils.getCampoSuplementarioFecFinAccionSubvConcedida(codOrganizacion, String.valueOf(ejercicio), numExpediente, codProcedimiento);
                reducPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioReducPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                jornPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                jornPersCont = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersCont(codOrganizacion, ejercicio, numExpediente, codProcedimiento);                
            } catch (Exception e) {
                log.error("Error al recuperar campos suplementarios al recargar los datos del modulo : " + e.getMessage(), e);
            }
            if (fechaInicio == null || fechaFin == null) {
                error = (fechaInicio == null ? MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA : MeLanbide01Constantes.ERROR_FECHA_FIN_NULA);
            }
            log.info("FechaIniActSubvConcedida: " + (fechaInicio != null ? formatFecha_dd_MM_yyyy.format(fechaInicio.getTime()) : "-")
                    + " FechaFinActSubvConcedida: " + (fechaFin != null ? formatFecha_dd_MM_yyyy.format(fechaFin.getTime()) : "-")
                    + " reducPersSust: " + (reducPersSust != null ? reducPersSust : "-")
                    + " jornPersSust: " + (jornPersSust != null ? jornPersSust : "-")
                    + " jornPersCont: " + (jornPersCont != null ? jornPersCont : "-")
            );
            //Recuperamos el campo de fecha de inicio del contrato
           /* CampoSuplementarioModuloIntegracionVO fechaInicioContrato = new CampoSuplementarioModuloIntegracionVO();
            salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, 
                            codProcedimiento, campoSuplementarioInicioContrato, Integer.parseInt(tipoCampoSuplementarioInicioContrato));
            if(salida.getStatus() == 0){
                fechaInicioContrato = salida.getCampoSuplementario();
                fechaInicio = fechaInicioContrato.getValorFecha();
                if(fechaInicio != null){
                    if(log.isDebugEnabled()){
                        log.debug("Fecha de inicio = " + formatFecha_dd_MM_yyyy.format(fechaInicio.getTime()));
                    }//if(log.isDebugEnabled())
                }else{
                    //Si la fecha es nula lo marcamos como error.
                error = MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA; 
                }//if(fechaInicio != null)
            }else{
                error = MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA;
            }//if(salida.getStatus() == 0)

            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                //Recuperamos el campo de fecha de fin del contrato
                CampoSuplementarioModuloIntegracionVO fechaFinContrato = new CampoSuplementarioModuloIntegracionVO();
                salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, 
                                codProcedimiento, campoSuplementarioFinContrato, Integer.parseInt(tipoCampoSuplementarioFinContrato));
                if(salida.getStatus() == 0){
                    fechaFinContrato = salida.getCampoSuplementario();
                    fechaFin = fechaFinContrato.getValorFecha();
                    if(fechaFin != null){
                        if(log.isDebugEnabled()){
                            log.debug("Fecha de fin = " + formatFecha_dd_MM_yyyy.format(fechaFin.getTime()));
                        }//if(log.isDebugEnabled())
                    }else{
                        error = MeLanbide01Constantes.ERROR_FECHA_FIN_NULA;
                    }//if(fechaFin != null)
                }else{
                    error = MeLanbide01Constantes.ERROR_FECHA_FIN_NULA;
                }//if(salida.getStatus() == 0)
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo MeLanbide01Constantes.REDUCPERSSUST
                    reducPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioReducPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                    //Comprobamos que este campo sea mayor que el porcentaje indicado en el properties
                    Double minReducPersSust = Double.valueOf(meLanbide01ValidatorUtils.getMinReducPersSust(codOrganizacion, codProcedimiento));
                    Double suplReducPersSust = Double.valueOf(reducPersSust);
                    if(log.isDebugEnabled()) log.debug("minReducPersSust  = " + minReducPersSust);
                    if(log.isDebugEnabled()) log.debug("suplReducPersSust  = " + suplReducPersSust);
                    if(minReducPersSust > suplReducPersSust){
                        if(log.isDebugEnabled()) log.debug("minReducPersSust > suplReducPersSust");
                        error = MeLanbide01Constantes.ERROR_MIN_REDUCPERSSUST;
                    }//if(minReducPersSust > suplReducPersSust)
                    if(log.isDebugEnabled()) log.debug("Fin calculo reduccion persona sustituida");
                } catch (MeLanbide01Exception ex) {
                    log.error("Se ha producido un error cargando el campo suplementario de la reducción de la persona sustituida", ex);
                    error = MeLanbide01Constantes.ERROR_CAMPO_REDUC_PERS_SUST;
                }//try-catch
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo JORNPERSSUST2
                    if(log.isDebugEnabled()) log.debug("Recuperamos el campo JORNPERSSUST");
                    jornPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                    if(log.isDebugEnabled()) log.debug("jornPersSust = " + jornPersSust);
                } catch (MeLanbide01Exception ex) {
                    log.error("Se ha producido un error cargando el campo suplementario de la jornada de la persona sustuida", ex);
                    error = MeLanbide01Constantes.ERROR_CAMPO_JORN_PERS_SUST;
                }//try-catch
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
            
            
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo JORNPERSCONT
                    jornPersCont = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersCont(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                } catch (MeLanbide01Exception ex) {
                    log.debug("Se ha producido un error cargando el campo suplementario de la jornada de la persona contratada: " + ex.getMessage());
                    log.error("Se ha producido un error cargando el campo suplementario de la jornada de la persona contratada: " + ex.getMessage());
                    error = MeLanbide01Constantes.ERROR_CAMPO_JORN_PERS_CONT;
                }//try-catch
            }
            
            */

           
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                if(log.isDebugEnabled()) log.debug("Calculamos el intervalo de fechas (BEGIN) ");
                
                if(!MeLanbide01DatosCalculoDao.getInstance().existenPeriodosGrabados(codOrganizacion, numExpediente, ConnectionUtils.getAdaptSQLBD(Integer.toString(codOrganizacion)))){                
                    try {
                        intervaloFechas = calcularIntervaloFechas(fechaInicio, fechaFin);                                                
                        for(DatosPeriodoVO intervalo : intervaloFechas){
                            if(log.isDebugEnabled()){
                                String stringFechaInicio = formatFecha_dd_MM_yyyy.format(intervalo.getFechaInicio().getTime());
                                String stringFechaFin = formatFecha_dd_MM_yyyy.format(intervalo.getFechaFin().getTime());
                                log.debug("Intervalo = " + stringFechaInicio + " - " + stringFechaFin);
                            }//if(log.isDebugEnabled())

                        }//for(ConciliacionVO intervalo : intervaloFechas)

                        //Calculamos el numero de horas entre los intervalos
                        calcularNumeroDias(intervaloFechas);
                        datosCalculo.setPeriodos(intervaloFechas);
                    }catch (MeLanbide01Exception ex){
                        log.error("Se ha producido un error calculando el intervalo de fechas", ex);
                        Integer codigoError = ex.getCodError();
                        if(codigoError == MeLanbide01Exception.ERROR_FECHA_INICIO_SUPERIOR_FIN){
                            error = MeLanbide01Constantes.ERROR_FECHA_INICIO_POSTERIOR_FIN;
                        }else{
                            error = MeLanbide01Constantes.OTRO_ERROR;
                        }//if(codigoError
                    }//try-catch
                    if(log.isDebugEnabled()) log.debug("Calculamos el intervalo de fechas (END) ");
                }else{
                    EXISTEN_PERIODOS = true;
                    intervaloFechas = datosCalculo.getPeriodos();
                }
                    
                        
            }// if
            
            

            //Calculamos el porcentaje y el gasto
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                rellenarCamposSuplementarios(intervaloFechas, reducPersSust, jornPersSust, jornPersCont);

                //Si los campos suplementarios tienen datos calculamos los valores de porcSubvenc y gasto
                if((reducPersSust != null && !reducPersSust.equalsIgnoreCase(""))
                        && (jornPersSust != null && !jornPersSust.equalsIgnoreCase("")) && 
                        (jornPersCont != null && !jornPersCont.equalsIgnoreCase(""))){
                    if(log.isDebugEnabled()) log.debug("Procedemos a calcular los valores");
                    try {
                        porcSubvenc = meLanbide01ValidatorUtils.calcularPorcSubvenc(reducPersSust, jornPersSust, jornPersCont);
                    } catch (MeLanbide01Exception ex) {
                        log.error("Se ha producido un error calculando el porcentaje de subvencion", ex);
                        error = MeLanbide01Constantes.ERROR_CALCULANDO_PORC_SUBVENC;
                    }//try-catch

                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        
                        rellenarPorcSubvenc(intervaloFechas, porcSubvenc);
                        
                        //rellenarPorcSubvenc(intervaloFechas, porcSubvenc);
                        datosCalculo.setPeriodos(intervaloFechas);
                        datosCalculo.setSumaTotalDiasPeriodos(calcularNumeroTotalDias(datosCalculo.getPeriodos()));
                    }
                }
            }
                
            /*********************************************************************************************/
            /*********************************************************************************************/
            /*** SE RELLENA LOS PERÍODOS OBTENIDOS CON LOS DATOS DE BASE DE COTIZACIÓN Y BONIFICACIÓN ****/
            /*********************************************************************************************/
            /*********************************************************************************************/
            ArrayList<DatosPeriodoVO> auxiliar = new ArrayList<DatosPeriodoVO>();
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                auxiliar = datosCalculo.getPeriodos();
                for(int i=0;periodosOriginales!=null && i<periodosOriginales.size();i++){

                    if(i<auxiliar.size() && periodosOriginales.get(i)!=null && periodosOriginales.get(i).getBaseCotizacion()!=null && !"".equals(periodosOriginales.get(i).getBaseCotizacion()))
                        auxiliar.get(i).setBaseCotizacion(periodosOriginales.get(i).getBaseCotizacion());
                    else
                    if(i<auxiliar.size())    
                        auxiliar.get(i).setBaseCotizacion("");

                    if(i<auxiliar.size() && periodosOriginales.get(i)!=null &&  periodosOriginales.get(i).getBonificacion()!=null && !"".equals(periodosOriginales.get(i).getBonificacion()))
                        auxiliar.get(i).setBonificacion(periodosOriginales.get(i).getBonificacion());
                    else
                    if(i<auxiliar.size())    
                        auxiliar.get(i).setBonificacion("");

                    try{
                        if(i<auxiliar.size() && periodosOriginales.get(i).getBaseCotizacion()!=null && !"".equals(periodosOriginales.get(i).getBaseCotizacion()) 
                                && periodosOriginales.get(i).getBonificacion()!=null && !"".equals(periodosOriginales.get(i).getBonificacion())){

                            String gasto = calcularGasto(periodosOriginales.get(i).getBaseCotizacion(),periodosOriginales.get(i).getBonificacion(),porcSubvenc);
                            auxiliar.get(i).setGasto(gasto);
                        }else
                        if(i<auxiliar.size())    
                            auxiliar.get(i).setGasto("");
                        
                        if (i < auxiliar.size() && periodosOriginales.get(i) != null && periodosOriginales.get(i).getPorcJornRealizada()!= null ) {
                            auxiliar.get(i).setPorcJornRealizada(periodosOriginales.get(i).getPorcJornRealizada());
                        } else if (i < auxiliar.size()) {
                            auxiliar.get(i).setPorcJornRealizada(null);
                        }
                        
                        if (i < auxiliar.size() && periodosOriginales.get(i) != null && periodosOriginales.get(i).getPorcJornSustitucion()!= null ) {
                            auxiliar.get(i).setPorcJornSustitucion(periodosOriginales.get(i).getPorcJornSustitucion());
                        } else if (i < auxiliar.size()) {
                            auxiliar.get(i).setPorcJornSustitucion(null);
                        }
                        
                    }catch(Exception e){
                        e.printStackTrace();
                    }                    
                }
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                
            /*********************************************************************************************/
            /*********************************************************************************************/
            /*** SE RELLENA LOS PERÍODOS OBTENIDOS CON LOS DATOS DE BASE DE COTIZACIÓN Y BONIFICACIÓN ****/
            /*********************************************************************************************/
            /*********************************************************************************************/

            String gastoTotal = new String();
            //if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                gastoTotal = calcularGastoTotal(auxiliar);
            //}//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
            /*********************************************************************************************/
            /*********************************************************************************************/
            /*** Calculamos los dias subvencionables restantes para el interesado contratado           ***/
            /*********************************************************************************************/
            /*********************************************************************************************/
            Integer diasRestantes = 0;
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try{            
                    Melanbide01Decreto decretoAplicable = meLanbide01ValidatorUtils.getDecretoAplicableExptePorFecInicioPeriodosOrFecIniAcciSubvConcedida(codOrganizacion, numExpediente);
                    // POr defecto Asignamos el Decreto inicia 177/2010
                    String decretoAplicableCod = (decretoAplicable != null ? decretoAplicable.getDecretoCodigo() : Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto());
                    diasRestantes = meLanbide01ValidatorUtils.diasRestantesSubvencionables(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio,decretoAplicableCod);
                }catch(MeLanbide01Exception meEx){
                    log.error("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol de persona"
                            + " contratada " + meEx.getMessage());
                    error = MeLanbide01Constantes.ERROR_CALCULANDO_DIAS_RESTANTES;
                }catch(Exception e){
                    log.error("Se ha producido un error Generico calculando los dias restantes subvencionables para el interesado con rol de persona"
                            + " contratada " + e.getMessage());
                    error = MeLanbide01Constantes.ERROR_CALCULANDO_DIAS_RESTANTES;
                }
            }
            
            //Comprobamos que el numero de intervalos no sea mayor que el maximo permitido
            log.info("Validacion maximo intervalos movida validator...");
//            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                if(datosCalculo.getPeriodos() != null && datosCalculo.getPeriodos().size()>0){
//                    Integer numMaximoIntervalos = Integer.valueOf(meLanbide01ValidatorUtils.getNumMaximoIntervalosPermitidos(codOrganizacion, codProcedimiento));
//                    if(datosCalculo.getPeriodos().size() > numMaximoIntervalos){
//                        error = MeLanbide01Constantes.ERROR_SUPERA_NUMERO_MAXIMO_PERIODOS;
//                    }
//                }
//            }
                
            //Comprobamos que el numero total de dias de todos los expedientes en los que coincide el interesado con un rol dado
            //no superen un maximo definido en un parametro de configuracion
            log.info("Validacion maximo dias movida validatos...");
//            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                try {
//                    codAlarmaMaxDias = meLanbide01ValidatorUtils.alarmaMaximoDias(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
//                    if(codAlarmaMaxDias != null && !"".equalsIgnoreCase(codAlarmaMaxDias)){
//                        error = MeLanbide01Constantes.MOSTRAR_ALARMA_MAX_DIAS;
//                    }
//                } catch (MeLanbide01Exception ex) {
//                    log.error("Se ha producido un error comprobando si hay que mostrar la advertencia del numero total de dias " + ex.getMessage());
//                    error = MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_DIAS;
//                }
//            }

            //Comprobamos que los anhos de la persona dependiente no supere al maximo fijado para el tipo de actividad del expediente
            //definido en un parametro de configuracion.
            log.info("Validacion Alarma Maximo Anios movida validatos...");
//            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                try{
//                    codAlarmaMaxAnhos = alarmaMaxAnosDependencia(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
//                    
//                     if(codAlarmaMaxAnhos != null && !"".equalsIgnoreCase(codAlarmaMaxAnhos)){                                
//                        if("1".equals(codAlarmaMaxAnhos)){
//                            // ERROR => La fecha de actuación concedida es anterior a la fecha de nacimiento de la 
//                            // persona dependiente
//                            error = "29";
//                        }else
//                        if("2".equals(codAlarmaMaxAnhos)){
//                            // ERROR => Hay alarma porque la resta entre la fecha de actuación concedida y
//                            // la fecha de nacimiento de la persona dependiente, excede el límite establecido
//                            // para la actuación subvencionable del expediente
//                            error = MeLanbide01Constantes.MOSTRAR_ALARMA_MAX_ANHOS;
//                        }                                
//                    }
//                   
//                } catch(MeLanbide01Exception ex){
//                    log.error("Se ha producido un error comprobando si hay que mostrar la advertencia del numero maximo de anhos " + ex.getMessage());
//                    error = MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_ANHOS;
//                }//try-catch
//            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
            //Comprobamos que la fecha de nacimiento de la persona dependiente este cubierta
            log.info("Validacion fecha nacimiento persona dependiente movida validatos...");
//            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                try{
//                    Calendar fecha = getCampoSuplementarioFecNacPersonaDependiente
//                            (codOrganizacion, ejercicio, numExpediente, codProcedimiento);
//
//                    if(fecha == null){
//                        error = MeLanbide01Constantes.ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA;
//                    }//if(fecha == null)
//                }catch(MeLanbide01Exception meEx){
//                    log.error("Se ha producido un error comprobando si la fecha de nacimiento de la persona dependiente está cubierta"
//                            + " " + meEx.getMessage());
//                    error = MeLanbide01Constantes.ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA;
//                }
//            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
        
            StringBuilder xmlSalida = new StringBuilder();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(error);
                xmlSalida.append("</CODIGO_OPERACION>");
                //if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                    xmlSalida.append("<DIAS_SUBVENCIONABLES_RESTANTES>");
                            xmlSalida.append(diasRestantes);
                    xmlSalida.append("</DIAS_SUBVENCIONABLES_RESTANTES>");
                    if(datosCalculo.getPeriodos() != null && datosCalculo.getPeriodos().size() > 0){
                        xmlSalida.append("<GASTO_TOTAL>");
                            xmlSalida.append(gastoTotal);
                        xmlSalida.append("</GASTO_TOTAL>");
                        xmlSalida.append("<TOTAL_DIAS>");
                            xmlSalida.append(calcularNumeroTotalDias(datosCalculo.getPeriodos()));
                        xmlSalida.append("</TOTAL_DIAS>");
                        xmlSalida.append("<PERIODOS>");
                            for(DatosPeriodoVO periodo : datosCalculo.getPeriodos()){
                                xmlSalida.append("<PERIODO>");
                                    xmlSalida.append("<INICIO_PERIODO>");
                                        xmlSalida.append(periodo.getFechaInicioAsString());
                                    xmlSalida.append("</INICIO_PERIODO>");
                                    xmlSalida.append("<FIN_PERIODO>");
                                        xmlSalida.append(periodo.getFechaFinAsString());
                                    xmlSalida.append("</FIN_PERIODO>");
                                    xmlSalida.append("<NUM_DIAS>");
                                        xmlSalida.append(periodo.getNumDias());
                                    xmlSalida.append("</NUM_DIAS>");
                                    if(periodo.getBaseCotizacion() != null && !periodo.getBaseCotizacion().equalsIgnoreCase("")){
                                        xmlSalida.append("<BASE_COTIZACION>");
                                            xmlSalida.append(periodo.getBaseCotizacion());
                                        xmlSalida.append("</BASE_COTIZACION>");
                                    }//if(periodo.getBaseCotizacion() != null && !periodo.getBaseCotizacion().equalsIgnoreCase(""))
                                    if(periodo.getReducPerSust() != null && !periodo.getReducPerSust().equalsIgnoreCase("")){
                                        xmlSalida.append("<REDUCPERSUST>");
                                            xmlSalida.append(periodo.getReducPerSust());
                                        xmlSalida.append("</REDUCPERSUST>");
                                    }//if(periodo.getReducPerSust() != null && !periodo.getReducPerSust().equalsIgnoreCase(""))
                                    if(periodo.getJornPersSust() != null && !periodo.getJornPersSust().equalsIgnoreCase("")){
                                        xmlSalida.append("<JORNPERSSUST>");
                                            xmlSalida.append(periodo.getJornPersSust());
                                        xmlSalida.append("</JORNPERSSUST>");
                                    }//if(periodo.getJornPersSust() != null && !periodo.getJornPersSust().equalsIgnoreCase(""))
                                    if(periodo.getJornPersCont() != null && !periodo.getJornPersCont().equalsIgnoreCase("")){
                                        xmlSalida.append("<JORNPERSCONT>");
                                            xmlSalida.append(periodo.getJornPersCont());
                                        xmlSalida.append("</JORNPERSCONT>");
                                    }//if(periodo.getJornPersCont() != null && !periodo.getJornPersCont().equalsIgnoreCase(""))
                                    if(periodo.getBonificacion() != null && !periodo.getBonificacion().equalsIgnoreCase("")){
                                        xmlSalida.append("<BONIFICACION>");
                                            xmlSalida.append(periodo.getBonificacion());
                                        xmlSalida.append("</BONIFICACION>");
                                    }//if(periodo.getBonificacion() != null && !periodo.getBonificacion().equalsIgnoreCase(""))
                                    if(periodo.getPorcSubven() != null && !periodo.getPorcSubven().equalsIgnoreCase("")){
                                        xmlSalida.append("<PORC_SUBVENC>");
                                            xmlSalida.append(periodo.getPorcSubven());
                                        xmlSalida.append("</PORC_SUBVENC>");
                                        if(periodo.getGasto()!=null && !"".equals(periodo.getGasto())){
                                            xmlSalida.append("<GASTO>");
                                                xmlSalida.append(periodo.getGasto());
                                            xmlSalida.append("</GASTO>");                                        
                                        }//if(periodo.getGasto()!=null && !"".equals(periodo.getGasto()))
                                        if(periodo.getPorcJornRealizada()!=null){
                                            xmlSalida.append("<PORC_JORN_REALIZADA>");
                                                xmlSalida.append(periodo.getPorcJornRealizada());
                                            xmlSalida.append("</PORC_JORN_REALIZADA>");                                        
                                        }
                                        if(periodo.getPorcJornSustitucion()!=null){
                                            xmlSalida.append("<PORC_JORN_SUSTITUCION>");
                                                xmlSalida.append(periodo.getPorcJornSustitucion());
                                            xmlSalida.append("</PORC_JORN_SUSTITUCION>");                                        
                                        }
                                    }//if(periodo.getPorcSubven() != null && !periodo.getPorcSubven().equalsIgnoreCase(""))
                                xmlSalida.append("</PERIODO>");
                            }//for(DatosPeriodoVO periodo : datosCalculo.getPeriodos())
                        xmlSalida.append("</PERIODOS>");
                    }//if(datosCalculo.getPeriodos() != null && datosCalculo.getPeriodos().size() > 0)
                //}//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error("Se ha producido un error actualizando la tabla de unidades competenciales ", e);
            }//try-catch
        }
        if(log.isDebugEnabled()) log.debug("recargarDatosPagina() : END");
        
    }//recargarDatosPagina
    
    /**
     * Esta operación graba los datos en la BBDD
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return 
     */
    public void grabarDatos (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        if(log.isDebugEnabled()) log.debug("anhadirDatos() : BEGIN");
        ArrayList<DatosPeriodoVO> periodos = new ArrayList<DatosPeriodoVO>();
        MeLanbide01Manager meLanbide01Manager = MeLanbide01Manager.getInstance();
        String error = MeLanbide01Constantes.TODO_CORRECTO;
        
        //Campos suplementarios.
        String baseCotizacion = "";
        String jornPersCont = "";
        String jornPersSust = "";
        String reducPersSust = "";
        String bonificacion = "";
        String codAlarmaMaxDias = "";
        String codAlarmaMaxAnhos = "";
        int diasTotalesExcedidos = 0;
        Integer diasRestantes = new Integer(0);
        
        if(numExpediente!=null && !"".equals(numExpediente)){
            String[] datos          = numExpediente.split(MeLanbide01Constantes.BARRA);
            String ejercicio        = datos[0];
            String codProcedimiento = datos[1];

            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo MeLanbide01Constantes.REDUCPERSSUST
                    reducPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioReducPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                } catch (MeLanbide01Exception ex) {
                    log.debug(" =============> grabarDatos() ERROR: Se ha producido un error cargando el campo suplementario de la reducción de la persona sustituida: " + ex.getMessage());
                    error = MeLanbide01Constantes.ERROR_RECUPERANDO_CAMPOS_SUPLEMENTARIOS;
                }//try-catch
            }
            

            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo JORNPERSCONT
                    jornPersCont = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersCont(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                } catch (MeLanbide01Exception ex) {
                    log.debug("  =============> grabarDatos() ERROR: Se ha producido un error cargando el campo suplementario de la jornada de la persona contratada: " +  ex.getMessage());
                    error = MeLanbide01Constantes.ERROR_RECUPERANDO_CAMPOS_SUPLEMENTARIOS;
                }
            }
            
            
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo jornPersSust
                    jornPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                } catch (MeLanbide01Exception ex) {
                    log.debug("  =============> grabarDatos() ERROR: Se ha producido un error cargando el campo suplementario de la jornada de la persona sustituida: " +  ex.getMessage());
                    error = MeLanbide01Constantes.ERROR_RECUPERANDO_CAMPOS_SUPLEMENTARIOS;
                }
            }
            
            
            
            log.debug(" =========================> ModuloSubvencionLanbide.grabarDatos periodos: " + periodos);
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                if(log.isDebugEnabled()) log.debug(" =======> Recuperamos los periodos");
                String stringPeriodos = request.getParameter("periodos");
                
                String descuento = request.getParameter("descuento");
                String totalConDescuento = request.getParameter("totalConDescuento");

                totalConDescuento = totalConDescuento.replace(".","");
                totalConDescuento = totalConDescuento.replace(",",".");
                log.debug("============> totalConDescuento: " + totalConDescuento + ",descuento: " + descuento);
                                
                log.debug("============> ModuloSubvencionLanbide.grabarDatos() periodos: " + stringPeriodos);
                log.debug("============> ModuloSubvencionLanbide.grabarDatos() MeLanbide01Constantes.SEPARADOR_REGISTRO: " + MeLanbide01Constantes.SEPARADOR_REGISTRO);
                //Separamos los periodos
                String[] arrayPeriodos = stringPeriodos.split(MeLanbide01Constantes.SEPARADOR_REGISTRO);
                for(int x=0; x<arrayPeriodos.length; x++){
                    DatosPeriodoVO periodo = new DatosPeriodoVO();
                    //Separamos los elementos
                    String[] elementosPeriodo = arrayPeriodos[x].split(MeLanbide01Constantes.SEPARADOR_ELEMENTO);
                    try {
                        //Elemento[0]
                        Calendar fechaInicio  = Calendar.getInstance();
                            fechaInicio.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[0]));
                        periodo.setFechaInicio(fechaInicio);
                        //Elemento[1]
                        Calendar fechaFin = Calendar.getInstance();
                            fechaFin.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[1]));
                        periodo.setFechaFin(fechaFin);
                        //Elemento[2]
                        String ausPorcSubvenc = elementosPeriodo[2];
                        periodo.setPorcSubven(ausPorcSubvenc);
                        //Elemento[3]
                        String auxNumDias = elementosPeriodo[3];
                        periodo.setNumDias(Integer.valueOf(auxNumDias));
                        //Elemento[4]
                        String auxBaseCotizacion = elementosPeriodo[4];
                        periodo.setBaseCotizacion(auxBaseCotizacion);
                        //Elemento[5]
                        String auxBonificacion = elementosPeriodo[5];
                        periodo.setBonificacion(auxBonificacion);
                        //Elemento[6]
                        String auxGasto = elementosPeriodo[6].replace(",",".");                        
                        periodo.setGasto(auxGasto);
                        
                        String porcJornRealizada = elementosPeriodo[7];
                        porcJornRealizada = (porcJornRealizada != null && !porcJornRealizada.equalsIgnoreCase("") && porcJornRealizada != "null" ? porcJornRealizada : "0");
                        periodo.setPorcJornRealizada(Double.parseDouble(porcJornRealizada));

                        String porcJornSutitucion = elementosPeriodo[8];
                        porcJornSutitucion = (porcJornSutitucion != null && !porcJornSutitucion.equalsIgnoreCase("") && porcJornSutitucion != "null" ? porcJornSutitucion : "0");
                        periodo.setPorcJornSustitucion(Double.parseDouble(porcJornSutitucion));

                        periodo.setJornPersCont(jornPersCont);
                        periodo.setJornPersSust(jornPersSust);
                        periodo.setReducPerSust(reducPersSust);

                        periodos.add(periodo);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        log.error("================> grabarDatos ERROR: " + ex.getMessage(),ex);
                    }//try-catch
                }//for(int x=0; x<arrayPeriodos.length; x++)
                //Fin periodos
                
                log.debug("============> ModuloSubvencionLanbide.grabarDatos() despues de recuperar periodos");
                
                
                //Comprobamos que el numero total de dias de todos los expedientes en los que coincide el interesado con un rol dado
                //no superen un maximo definido en un parametro de configuracion
                Integer numeroTotalDias = Integer.valueOf(calcularNumeroTotalDias(periodos));
                log.debug(" ================> ModuloSubvencionLanbide.grabarDatos() numeroTotalDias: " + numeroTotalDias);
                
                /*********************************************************************************************/
                /*********************************************************************************************/
                /*** Calculamos los dias subvencionables restantes para el interesado contratado           ***/
                /*********************************************************************************************/
                /*********************************************************************************************/
                
                /***
                if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                    try{                    
                        
                        diasRestantes = diasRestantesSubvencionables(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
                    }catch(MeLanbide01Exception meEx){
                        log.error("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol de persona"
                            + " contratada " + meEx.getMessage());
                        error = MeLanbide01Constantes.ERROR_CALCULANDO_DIAS_RESTANTES;
                    }//try-catch
                }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                **/
                
                /**
                 * *
                 * Esta validacion no se lleva a Validator de alarmas Se debe
                 * hacer con los datos Dinamicos que pone el usario no lo
                 * guardado Yas e valida con el metodo alarmaMaximoDias en el
                 * validador
                 */
                AlarmaVO alarma = null;
                try {
                    Melanbide01Decreto decretoAplicable = meLanbide01ValidatorUtils.getDecretoAplicableExptePorFecInicioPeriodosOrFecIniAcciSubvConcedida(codOrganizacion, numExpediente);
                    // POr defecto Asignamos el Decreto inicia 177/2010
                    String decretoAplicableCod = (decretoAplicable != null ? decretoAplicable.getDecretoCodigo() : Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto());
                    alarma = meLanbide01ValidatorUtils.alarmaMaximoDias2(String.valueOf(codOrganizacion), 
                            numExpediente, codProcedimiento, ejercicio, numeroTotalDias,decretoAplicableCod);
                    
                    if(alarma != null && alarma.getCodigoAlarma()!=null && !"".equalsIgnoreCase(alarma.getCodigoAlarma()) 
                            && !"null".equalsIgnoreCase(alarma.getCodigoAlarma())){
                        error = alarma.getCodigoAlarma();
                        diasTotalesExcedidos = alarma.getDiasExcedidos();
                    }/*if(alarma != null && alarma.getCodigoAlarma()!=null && !"".equalsIgnoreCase(alarma.getCodigoAlarma()) 
                            && !"null".equalsIgnoreCase(alarma.getCodigoAlarma()))*/
                } catch (MeLanbide01Exception ex) {
                    log.error("Se ha producido un error comprobando si hay que mostrar la alarma de numero de dias " + ex.getMessage(), ex);
                    error = MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_DIAS;
                }catch(Exception e){
                    log.error("Se ha producido un error comprobando si hay que mostrar la alarma de numero de dias " + e.getMessage(),e);
                    error = MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_DIAS;
                }//try-catch

                //Creamos el objeto DatosCalculoVO
                DatosCalculoVO datosCalculo = new DatosCalculoVO();
                    datosCalculo.setNumExpediente(numExpediente);
                    datosCalculo.setCodMunicipio(String.valueOf(codOrganizacion));
                    datosCalculo.setCodProcedimiento(codProcedimiento);
                    datosCalculo.setEjercicio(ejercicio);
                    datosCalculo.setNombreModulo(MeLanbide01Constantes.MODULO_INTEGRACION);
                    datosCalculo.setImporteSubvencionado(calcularGastoTotal(periodos));
                    datosCalculo.setPeriodos(periodos);
                    // prueba
                    datosCalculo.setDescuento(Integer.parseInt(descuento));
                    datosCalculo.setTotalConDescuento(Double.parseDouble(totalConDescuento));
                    // prueba
                try {
                    String campoSuplementarioFinContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.CAMPO_FIN_CONTRATO, this.getNombreModulo());
                    String campoSuplementarioInicioContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.CAMPO_INICIO_CONTRATO, this.getNombreModulo());
                    String campoSuplementarioTotalSubvencionado = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.IMPORTE_SUBVENCION, this.getNombreModulo());
                    meLanbide01Manager.insertarDatosCalculo(datosCalculo, campoSuplementarioFinContrato, 
                            campoSuplementarioTotalSubvencionado, campoSuplementarioInicioContrato, ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                } catch (MeLanbide01Exception ex) {
                    log.debug(" =============> grabarDatos() ERROR : Se ha producido un error grabando los datos del calculo " + ex.getMessage());
                    error = MeLanbide01Constantes.ERROR_GRABANDO_CAMPOS_SUPLEMENTARIOS;
                }//try-catch
                
                // Guardamos campos suplementario numero de dias de suvbencion
                try {
                    log.error("NoError - Inicio try Guardar campo suplementario NroDiasConcedidos. Al Guardar Datos de Calculo. Exp " + numExpediente );
                    String numTotalDiasConcedidos = request.getParameter("numTotalDiasConcedidos");
                    meLanbide01Manager.insertarDatosCampoSuplementarioNroTotalDiasConcedidos(codOrganizacion, Integer.valueOf(ejercicio), numExpediente, numTotalDiasConcedidos, ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    log.error("NoError - Guardar OK - Fin Try -  campo suplementario NroDiasConcedidos. Al Guardar Datos de Calculo. Exp " + numExpediente );
                } catch (MeLanbide01Exception ex) {
                    log.debug(" =============> insertarDatosCampoSuplementarioNroTotalDiasConcedidos() ERROR : Se ha producido un error GUARDANDO  el valor del campo suplementario total dias concedidos al grabar los datos del calculo " + ex.getMessage());
                    error = MeLanbide01Constantes.ERROR_GRABANDO_CAMPOS_SUPLEMENTARIOS;
                }//try-catch
                
                
                //Calculamos el numero de dias para saber si tenemos que mostrar el aviso
                if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                    Integer numMinimoDias = Integer.valueOf(meLanbide01ValidatorUtils.getNumMinimoDias(codOrganizacion, codProcedimiento));
                    Integer numTotalDias = Integer.valueOf(calcularNumeroTotalDias(periodos));                    
                    log.debug(" ================> grabarDatos numMinimoDias: " + numMinimoDias + ", numTotalDias: " + numTotalDias);
                    /* Quitamos  validaciones 08/09/2015 - habilitar coger picos y meter un periodo de una dias
                    if(numMinimoDias > numTotalDias){
                        error = MeLanbide01Constantes.ERROR_NUM_MINIMO_DIAS;
                    }
                    */
                }
                
                // Comprobaciones si el expedientes esta fuera de plazo
                if (error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)) {
                    String campoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_SOLICITUD", this.getNombreModulo());
                    String tipoCampoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_SOLICITUD", this.getNombreModulo());
                    String campoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", this.getNombreModulo());
                    String tipoCampoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", this.getNombreModulo());
                    if (meLanbide01ValidatorUtils.compruebaSiExpedienteFueraPlazo(codOrganizacion, ejercicio, codProcedimiento, numExpediente,
                            campoSuplementarioFechaSolicitud, tipoCampoSuplementarioFechaSolicitud,
                            campoSuplementarioFechaFinActuacionConcedido, tipoCampoSuplementarioFechaFinActuacionConcedido)) {
                        error = MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO;
                    }
                }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            }
        }
        
        log.debug(" =================> grabarDatos()  código de operación de respuesta: " + error);
        StringBuilder xmlSalida = new StringBuilder();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(error);                    
                xmlSalida.append("</CODIGO_OPERACION>");
                /**
                xmlSalida.append("<DIAS_SUBVENCIONABLES_RESTANTES>");
                    xmlSalida.append(diasRestantes);
                xmlSalida.append("</DIAS_SUBVENCIONABLES_RESTANTES>");
                * **/
                xmlSalida.append("<INFORMACION_ADICIONAL>");                    
                    if(error.equals("25") && diasTotalesExcedidos>0){                    
                        xmlSalida.append(diasTotalesExcedidos);                        
                    }
                xmlSalida.append("</INFORMACION_ADICIONAL>");    
            xmlSalida.append("</RESPUESTA>");
       log.debug(" =================> grabarDatos() xmlSalida respuesta para jsp: " + xmlSalida);     
            
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Se ha producido un error actualizando la tabla de unidades competenciales ", e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("anhadirDatos() : END");
    }//comprobarDatos
    
    
    
    
    /** Recalcula el gasto para un período determinado y devuelve el mismo objeto modificado 
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param periodo: Objeto con los datos del período
     * @return DatosPeriodoVO con el gasto recalculado si proxcede
     **/
    private DatosPeriodoVO recalcularDatosPeriodo(String codOrganizacion,String numExpediente,DatosPeriodoVO periodo){        
        if(log.isDebugEnabled()) log.debug("recalcularDatosPeriodo() : BEGIN");
        if(periodo!=null){
            // Se procede a calcular el porcentaje de subvención para calcular el nuevo gasto.
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            try {
                String[] datos = numExpediente.split("/");
                String ejercicio = datos[0];
                String codProcedimiento = datos[1];
                
                String COD_REDUCCION_PERSONA_SUSTITUIDA  = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento +  "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/REDUCPERSSUST",this.getNombreModulo());            
                String TIPO_REDUCCION_PERSONA_SUSTITUIDA = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/REDUCPERSSUST",this.getNombreModulo());                                                   
                String COD_JORNADA_PERSONA_SUSTITUIDA    = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/JORNPERSSUST",this.getNombreModulo());
                String TIPO_JORNADA_PERSONA_SUSTITUIDA   = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/JORNPERSSUST",this.getNombreModulo());
                String COD_JORNADA_PERSONA_CONTRATADA    = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/JORNPERSCONT",this.getNombreModulo());
                String TIPO_JORNADA_PERSONA_CONTRATADA   = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/JORNPERSCONT",this.getNombreModulo());

                SalidaIntegracionVO campoReduccionPersonaSustituida = el.getCampoSuplementarioExpediente(codOrganizacion,ejercicio,numExpediente,codProcedimiento, COD_REDUCCION_PERSONA_SUSTITUIDA,Integer.parseInt(TIPO_REDUCCION_PERSONA_SUSTITUIDA));
                SalidaIntegracionVO campoJornadaPersonaSustituida   = el.getCampoSuplementarioExpediente(codOrganizacion,ejercicio,numExpediente,codProcedimiento, COD_JORNADA_PERSONA_SUSTITUIDA,Integer.parseInt(TIPO_JORNADA_PERSONA_SUSTITUIDA));
                SalidaIntegracionVO campoJornadaPersonaContratada   = el.getCampoSuplementarioExpediente(codOrganizacion,ejercicio,numExpediente,codProcedimiento, COD_JORNADA_PERSONA_CONTRATADA,Integer.parseInt(TIPO_JORNADA_PERSONA_CONTRATADA));

                String valorReduccionPersonaSustituida = "";
                String valorJornadaPersonaSustituida   = "";
                String valorJornadaPersonaContratada   = "";

                if(campoReduccionPersonaSustituida.getStatus()==0 && campoJornadaPersonaSustituida.getStatus()==0 && campoJornadaPersonaContratada.getStatus()==0){
                    valorReduccionPersonaSustituida = campoReduccionPersonaSustituida.getCampoSuplementario().getValorNumero();
                    valorJornadaPersonaSustituida   = campoJornadaPersonaSustituida.getCampoSuplementario().getValorNumero();
                    valorJornadaPersonaContratada   = campoJornadaPersonaContratada.getCampoSuplementario().getValorNumero();

                    if(valorReduccionPersonaSustituida!=null && !"".equals(valorReduccionPersonaSustituida) && valorJornadaPersonaSustituida!=null && !"".equals(valorJornadaPersonaSustituida) && valorJornadaPersonaContratada!=null && !"".equals(valorJornadaPersonaContratada)){
                        
                        /******/
                        valorReduccionPersonaSustituida  =valorReduccionPersonaSustituida.replace(",",".");
                        valorJornadaPersonaSustituida = valorJornadaPersonaSustituida.replace(",",".");
                        valorJornadaPersonaContratada = valorJornadaPersonaContratada.replace(",",".");
                        
                        /*******/
                        
                        
                        Double dValorReduccionPersonaSustituida = Double.parseDouble(valorReduccionPersonaSustituida);
                        Double dValorJornadaPersonaSustituida   = Double.parseDouble(valorJornadaPersonaSustituida);
                        Double dValorJornadaPersonaContratada   = Double.parseDouble(valorJornadaPersonaContratada);
                        log.debug("recalcularDatosPeriodo dValorReduccionPersonaSustituida: " + dValorReduccionPersonaSustituida);
                        log.debug("recalcularDatosPeriodo dValorJornadaPersonaSustituida: " + dValorJornadaPersonaSustituida);
                        log.debug("recalcularDatosPeriodo dValorJornadaPersonaContratada: " + dValorJornadaPersonaContratada);
                        
                        Double porcentajeSubvencion = null;
                        if(periodo.getPorcSubven() != null && !"".equals(periodo.getPorcSubven())){
                            porcentajeSubvencion = Double.valueOf(periodo.getPorcSubven());
                        }else{
                            porcentajeSubvencion =
                                    (dValorReduccionPersonaSustituida * dValorJornadaPersonaSustituida)/dValorJornadaPersonaContratada;
                        }//if(periodo.getPorcSubven() != null && !"".equals(periodo.getPorcSubven()))
                        porcentajeSubvencion = porcentajeSubvencion/100;
                        log.debug("recalcularDatosPeriodo porcentajeSubvencion: " + porcentajeSubvencion);
                        Double baseCotizacion = new Double(periodo.getBaseCotizacion().replace(",","."));
                        Double bonificacion   = new Double(periodo.getBonificacion().replace(",","."));
                       Double gasto = (Utilities.redondearDosDecimales(baseCotizacion) - Utilities.redondearDosDecimales(bonificacion)) * 0.2360 * porcentajeSubvencion;
                        log.debug("recalcularDatosPeriodo gasto: " + gasto);
                        String dGasto = Double.toString(gasto).replace(".",",");
                        log.debug("recalcularDatosPeriodo dGasto: " + dGasto);
                        periodo.setGasto(dGasto);
                    }// if               
                }// if
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Se ha producido un error al recalcular los cálculos de un período: " + ex.getMessage());
            }//try-catch
        }//if(periodo!=null)
        if(log.isDebugEnabled()) log.debug("recalcularDatosPeriodo() : END");         
        return periodo;
    }//recalcularDatosPeriodo
    
    /**
     * Esta operación comprueba que el intervalo de fechas que se quiere añadir este al principio o al final del que ya existe
     * y no se solape con alguno de los intervalos existentes.
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return 
     */
    
    public void comprobarDatosModificar (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        if(log.isDebugEnabled()) log.debug("comprobarDatosModificar() : BEGIN");
        ArrayList<DatosPeriodoVO> periodos = new ArrayList<DatosPeriodoVO>();
        String error = MeLanbide01Constantes.TODO_CORRECTO;
        
        if(log.isDebugEnabled()) log.debug("Recuperamos los periodos");
        String stringPeriodos = request.getParameter("periodos");
        //Separamos los periodos
        String[] arrayPeriodos = stringPeriodos.split(MeLanbide01Constantes.SEPARADOR_REGISTRO);
        for(int x=0; x<arrayPeriodos.length; x++){
            DatosPeriodoVO periodo = new DatosPeriodoVO();
            //Separamos los elementos
            String[] elementosPeriodo = arrayPeriodos[x].split(MeLanbide01Constantes.SEPARADOR_ELEMENTO);
            try {
                //Elemento[0]
                Calendar fechaInicio  = Calendar.getInstance();
                    fechaInicio.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[0]));
                periodo.setFechaInicio(fechaInicio);
                //Elemento[1]
                Calendar fechaFin = Calendar.getInstance();
                    fechaFin.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[1]));
                periodo.setFechaFin(fechaFin);
                //Elemento[2]
                String porcSubvenc = elementosPeriodo[2];
                porcSubvenc = porcSubvenc.replace(",",".");
                periodo.setPorcSubven(porcSubvenc);
                //Elemento[3]
                String numDias = elementosPeriodo[3];
                periodo.setNumDias(Integer.valueOf(numDias));
                //Elemento[4]
                if(elementosPeriodo.length > 4){
                    String baseCotizacion = elementosPeriodo[4];
                    periodo.setBaseCotizacion(baseCotizacion);
                }//if(elementosPeriodo.length > 3)
                //Elemento[5]
                if(elementosPeriodo.length > 5){
                    String bonificacion = elementosPeriodo[5];
                    periodo.setBonificacion(bonificacion);
                }//if(elementosPeriodo.length > 3)
                //Elemento[6]
                if(elementosPeriodo.length > 6){
                    String gasto = elementosPeriodo[6];
                    periodo.setGasto(gasto);
                }//if(elementosPeriodo.length > 3)
                
                if(elementosPeriodo.length > 7){
                    String porcJornRealizada = elementosPeriodo[7];
                    porcJornRealizada = (porcJornRealizada != null && !porcJornRealizada.isEmpty() && !porcJornRealizada.equalsIgnoreCase("null") ? porcJornRealizada : "0");
                    periodo.setPorcJornRealizada(Double.parseDouble(porcJornRealizada));
                }
                if(elementosPeriodo.length > 8){
                    String porcJornSutitucion = elementosPeriodo[8];
                    porcJornSutitucion = (porcJornSutitucion != null && !porcJornSutitucion.isEmpty() && !porcJornSutitucion.equalsIgnoreCase("null") ? porcJornSutitucion : "0");
                    periodo.setPorcJornSustitucion(Double.parseDouble(porcJornSutitucion));
                }
                periodos.add(periodo);
            } catch (ParseException ex) {
                log.error("Se ha producido un error recuperando los datos de los periodos");
            }//try-catch
        }//for(int x=0; x<arrayPeriodos.length; x++)
        //Fin periodos
        
        if(log.isDebugEnabled()) log.debug("Recuperamos el nuevo periodo");
        String stringNuevoPeriodo = (String) request.getParameter("nuevoPeriodo");
        DatosPeriodoVO nuevoPeriodo = new DatosPeriodoVO();
        String[] elementosPeriodo = stringNuevoPeriodo.split(MeLanbide01Constantes.SEPARADOR_ELEMENTO);
        try {
            //Elemento[0]
            Calendar fechaInicio  = Calendar.getInstance();
                fechaInicio.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[0]));
            nuevoPeriodo.setFechaInicio(fechaInicio);
            //Elemento[1]
            Calendar fechaFin = Calendar.getInstance();
                fechaFin.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[1]));
            nuevoPeriodo.setFechaFin(fechaFin);
            //Elemento[2]
            String porcSubvenc = elementosPeriodo[2];
            porcSubvenc = porcSubvenc.replace(",",".");
            nuevoPeriodo.setPorcSubven(porcSubvenc);
            //Elemento[3]
            String numDias = elementosPeriodo[3];
            nuevoPeriodo.setNumDias(Integer.valueOf(numDias));
            //Elemento[4]
            String baseCotizacion = elementosPeriodo[4];
            nuevoPeriodo.setBaseCotizacion(baseCotizacion);
            //Elemento[5]
            String bonificacion = elementosPeriodo[5];
            nuevoPeriodo.setBonificacion(bonificacion);
            //Elemento[6]
            String gasto = calcularGasto(baseCotizacion, bonificacion, porcSubvenc);
            nuevoPeriodo.setGasto(gasto);
            
            String porcJornRealizada = elementosPeriodo[7];
            porcJornRealizada = (porcJornRealizada != null && !porcJornRealizada.isEmpty() && !porcJornRealizada.equalsIgnoreCase("null") ? porcJornRealizada : "0");
            nuevoPeriodo.setPorcJornRealizada(Double.parseDouble(porcJornRealizada));

            String porcJornSutitucion = elementosPeriodo[8];
            porcJornSutitucion = (porcJornSutitucion != null && !porcJornSutitucion.isEmpty() && !porcJornSutitucion.equalsIgnoreCase("null") ? porcJornSutitucion : "0");
            nuevoPeriodo.setPorcJornSustitucion(Double.parseDouble(porcJornSutitucion));
            
        } catch (MeLanbide01Exception ex){
            log.error("Se ha producido un error calculando el gasto ");
            error = MeLanbide01Constantes.ERROR_CALCULANDO_GASTO;
        } catch (ParseException ex) {
            log.error("Se ha producido un error recuperando los datos del nuevo periodo");
        }//try-catch
        //Fin nuevo periodo
        
        Integer indiceSeleccionado = null;
        if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
            if(log.isDebugEnabled()) log.debug("Recuperamos el indice seleccionado");
            indiceSeleccionado = Integer.valueOf((String) request.getParameter("indiceSeleccionado"));
            
            //Primero comprobamos que el intervalo modificado comprenda un solo mes
            Boolean mesIntervaloCorrecto = nuevoIntervaloMesCorrecto(nuevoPeriodo);
            if(!mesIntervaloCorrecto){
                error = MeLanbide01Constantes.ERROR_NUEVO_PERIODO_INICIO_FIN_MES_DISTINTO;
            }//if(!mesIntervaloCorrecto)
        }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        
        if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
           periodos.set(indiceSeleccionado, recalcularDatosPeriodo(Integer.toString(codOrganizacion),numExpediente,nuevoPeriodo));
        }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        
        /**
         * Todas las validaciones en este caso, se ejecutan despues de llamar a
         * comprobarDatosModificar en JSP mediante jquery y Ajax
         */
        // Comprobar si expediente fuera de plazo
//        if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//            String[] datos = numExpediente.split(MeLanbide01Constantes.BARRA);
//            String ejercicio = "";
//            String codProcedimiento = "";
//            String campoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_SOLICITUD",this.getNombreModulo());
//            String tipoCampoSuplementarioFechaSolicitud  = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_SOLICITUD",this.getNombreModulo());
//            String campoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO",this.getNombreModulo());
//            String tipoCampoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO",this.getNombreModulo());
//            if(datos.length>0){
//                ejercicio = datos[0];
//                codProcedimiento = datos[1];
//            }
//            if(meLanbide01ValidatorUtils.compruebaSiExpedienteFueraPlazo(Integer.valueOf(codOrganizacion), ejercicio, codProcedimiento, numExpediente,
//                    campoSuplementarioFechaSolicitud, tipoCampoSuplementarioFechaSolicitud, 
//                    campoSuplementarioFechaFinActuacionConcedido,tipoCampoSuplementarioFechaFinActuacionConcedido)){
//                error = MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO;
//            }
//        }

        
        
        /*********************************************************************************************/
        /*********************************************************************************************/
        /*** Calculamos los dias subvencionables restantes para el interesado contratado           ***/
        /*********************************************************************************************/
        /*********************************************************************************************
        Integer diasRestantes = new Integer(0);
        if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
            try{
                String[] datos = numExpediente.split("/");
                String ejercicio = datos[0];
                String codProcedimiento = datos[1];
                Integer numeroTotalDias = Integer.valueOf(calcularNumeroTotalDias(periodos));
                
                //diasRestantes = diasRestantesSubvencionables2
                //        (String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio,numeroTotalDias);
                
                // Se comprueba todos los días que ya lleva imputados la persona contratada en todos los expedientes del CONCM.
                // Después al límite de la actividad subvencionable, se le resta el número de días ya imputados a la persona contratada, y esos
                // son, los que quedan todavía disponibles.
                diasRestantes = diasRestantesSubvencionables2(Integer.toString(codOrganizacion), numExpediente, codProcedimiento, ejercicio,numeroTotalDias);
            }catch(MeLanbide01Exception meEx){
                log.error("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol de persona"
                        + " contratada " + meEx.getMessage());
                error = MeLanbide01Constantes.ERROR_CALCULANDO_DIAS_RESTANTES;
            }//try-catch
        }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        ***/
        
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(error);
            xmlSalida.append("</CODIGO_OPERACION>");
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                /**
                xmlSalida.append("<DIAS_SUBVENCIONABLES_RESTANTES>");
                    xmlSalida.append(diasRestantes);
                xmlSalida.append("</DIAS_SUBVENCIONABLES_RESTANTES>");
                **/
                if(periodos.size() > 0){
                    xmlSalida.append("<TOTAL_DIAS>");
                        xmlSalida.append(calcularNumeroTotalDias(periodos));
                    xmlSalida.append("</TOTAL_DIAS>");
                    xmlSalida.append("<GASTO_TOTAL>");
                        xmlSalida.append(calcularGastoTotal(periodos));
                    xmlSalida.append("</GASTO_TOTAL>");
                    xmlSalida.append("<PERIODOS>");
                        for(DatosPeriodoVO periodo : periodos){
                            xmlSalida.append("<PERIODO>");
                                xmlSalida.append("<INICIO_PERIODO>");
                                    xmlSalida.append(periodo.getFechaInicioAsString());
                                xmlSalida.append("</INICIO_PERIODO>");
                                xmlSalida.append("<FIN_PERIODO>");
                                    xmlSalida.append(periodo.getFechaFinAsString());
                                xmlSalida.append("</FIN_PERIODO>");
                                xmlSalida.append("<NUM_DIAS>");
                                    xmlSalida.append(periodo.getNumDias());
                                xmlSalida.append("</NUM_DIAS>");
                                if(periodo.getBaseCotizacion() != null && !periodo.getBaseCotizacion().equalsIgnoreCase("")){
                                    xmlSalida.append("<BASE_COTIZACION>");
                                        /*****/                
                                        //String aux = periodo.getBaseCotizacion().replace(",",".");                                    
                                        String aux = periodo.getBaseCotizacion();
                                        Double dAux = new Double(aux);                                        
                                        xmlSalida.append(Utilities.redondearDosDecimales(dAux));
                
                                        /******/
                                    xmlSalida.append("</BASE_COTIZACION>");
                                }//if(periodo.getBaseCotizacion() != null && !periodo.getBaseCotizacion().equalsIgnoreCase(""))
                                if(periodo.getReducPerSust() != null && !periodo.getReducPerSust().equalsIgnoreCase("")){
                                    xmlSalida.append("<REDUCPERSUST>");
                                        xmlSalida.append(periodo.getReducPerSust());
                                    xmlSalida.append("</REDUCPERSUST>");
                                }//if(periodo.getReducPerSust() != null && !periodo.getReducPerSust().equalsIgnoreCase(""))
                                if(periodo.getJornPersSust() != null && !periodo.getJornPersSust().equalsIgnoreCase("")){
                                    xmlSalida.append("<JORNPERSSUST>");
                                        xmlSalida.append(periodo.getJornPersSust());
                                    xmlSalida.append("</JORNPERSSUST>");
                                }//if(periodo.getJornPersSust() != null && !periodo.getJornPersSust().equalsIgnoreCase(""))
                                if(periodo.getJornPersCont() != null && !periodo.getJornPersCont().equalsIgnoreCase("")){
                                    xmlSalida.append("<JORNPERSCONT>");
                                        xmlSalida.append(periodo.getJornPersCont());
                                    xmlSalida.append("</JORNPERSCONT>");
                                }//if(periodo.getJornPersCont() != null && !periodo.getJornPersCont().equalsIgnoreCase(""))
                                if(periodo.getBonificacion() != null && !periodo.getBonificacion().equalsIgnoreCase("")){
                                    xmlSalida.append("<BONIFICACION>");
                                        /*****/
                                        //String aux = periodo.getBonificacion().replace(".","");                                        
                                        String aux = periodo.getBonificacion();                                        
                                        Double dAux = new Double(aux);                                        
                                        xmlSalida.append(Utilities.redondearDosDecimales(dAux));                                        
                                        /******/
                                        //xmlSalida.append(periodo.getBonificacion());
                                    xmlSalida.append("</BONIFICACION>");
                                }//if(periodo.getBonificacion() != null && !periodo.getBonificacion().equalsIgnoreCase(""))
                                if(periodo.getPorcSubven() != null && !periodo.getPorcSubven().equalsIgnoreCase("")){
                                    xmlSalida.append("<PORC_SUBVENC>");
                                        xmlSalida.append(periodo.getPorcSubven());
                                    xmlSalida.append("</PORC_SUBVENC>");
                                }//if(periodo.getPorcSubven() != null && !periodo.getPorcSubven().equalsIgnoreCase(""))
                                if(periodo.getGasto() != null && !periodo.getGasto().equalsIgnoreCase("")){
                                    xmlSalida.append("<GASTO>");                                    
                                        /*****/
                                        //String aux = periodo.getGasto().replace(".","");                                                                                
                                        String aux = periodo.getGasto();                                              
                                        if(aux.equals("0,0")) aux = "0";
                                        
                                        if(aux.indexOf(",")!=-1)
                                            aux = aux.replace(",",".");
                                                    
                                        Double dAux = new Double(aux);                                        
                                        xmlSalida.append(Utilities.redondearDosDecimales(dAux));
                                        /******/
                                        
                                    xmlSalida.append("</GASTO>");
                                    
                                }//if(periodo.getGasto != null && !periodo.getGasto().equalsIgnoreCase(""))
                                if(periodo.getPorcJornRealizada()!= null){
                                    xmlSalida.append("<PORC_JORN_REALIZADA>");                                    
                                        xmlSalida.append(Utilities.redondearDosDecimales(periodo.getPorcJornRealizada()));
                                    xmlSalida.append("</PORC_JORN_REALIZADA>");
                                }
                                if(periodo.getPorcJornSustitucion()!= null){
                                    xmlSalida.append("<PORC_JORN_SUSTITUCION>");                                    
                                        xmlSalida.append(Utilities.redondearDosDecimales(periodo.getPorcJornSustitucion()));
                                    xmlSalida.append("</PORC_JORN_SUSTITUCION>");
                                }
                            xmlSalida.append("</PERIODO>");
                        }//for(DatosPeriodoVO periodo : periodos)
                    xmlSalida.append("</PERIODOS>");
                }//if(periodos.size() > 0)
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Se ha producido un error comprobando el nuevo tramo ", e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("comprobarDatosModificar() : END");
    }//comprobarDatos
    
    /**
     * Esta operación comprueba que el intervalo de fechas que se quiere añadir este al principio o al final del que ya existe
     * y no se solape con alguno de los intervalos existentes.
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return 
     */
    public void comprobarDatosAnhadir (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        if(log.isDebugEnabled()) log.debug("comprobarDatosAnhadir() : BEGIN");
        ArrayList<DatosPeriodoVO> periodos = new ArrayList<DatosPeriodoVO>();
        String error = MeLanbide01Constantes.TODO_CORRECTO;
        String porcSubvenc = new String();
        int numDiasExcedidos = 0;
        Integer diasRestantes = new Integer(0);
        
        if(numExpediente!=null && !"".equals(numExpediente)){
            String[] datos          = numExpediente.split(MeLanbide01Constantes.BARRA);
            String ejercicio        = datos[0];
            String codProcedimiento = datos[1];
            
            if(log.isDebugEnabled()) log.debug("Recuperamos los campos suplementarios para realizar las operaciones de calculo");
            //Campos suplementarios.
            String suplJornPersCont = "";
            String suplJornPersSust = "";
            String suplReducPersSust = "";

            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo MeLanbide01Constantes.REDUCPERSSUST
                    suplReducPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioReducPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                } catch (MeLanbide01Exception ex) {
                    log.error("Se ha producido un error cargando el campo suplementario de la reducción de la persona sustituida", ex);
                    error = MeLanbide01Constantes.ERROR_RECUPERANDO_CAMPOS_SUPLEMENTARIOS;
                }//try-catch
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo JORNPERSSUST
                    suplJornPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                } catch (MeLanbide01Exception ex) {
                    log.error("Se ha producido un error cargando el campo suplementario de la jornada de la persona sustuida", ex);
                    error = MeLanbide01Constantes.ERROR_RECUPERANDO_CAMPOS_SUPLEMENTARIOS;
                }//try-catch
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    //Recuperamos el campo JORNPERSCONT
                    suplJornPersCont = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersCont(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                } catch (MeLanbide01Exception ex) {
                    log.error("Se ha producido un error cargando el campo suplementario de la jornada de la persona contratada", ex);
                    error = MeLanbide01Constantes.ERROR_RECUPERANDO_CAMPOS_SUPLEMENTARIOS;
                }
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

            //Calculamos el porcentaje subvencionado
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try {
                    porcSubvenc = meLanbide01ValidatorUtils.calcularPorcSubvenc(suplReducPersSust, suplJornPersSust, suplJornPersCont);
                } catch (MeLanbide01Exception ex) {
                    log.error("Se ha producido un error calculando el porcentaje subvencionado ", ex);
                    error = MeLanbide01Constantes.ERROR_CALCULANDO_PORC_SUBVENC;
                }//try-catch
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        
            if(log.isDebugEnabled()) log.debug("Recuperamos los periodos");
            String stringPeriodos = request.getParameter("periodos");
            //Separamos los periodos
            String[] arrayPeriodos = stringPeriodos.split(MeLanbide01Constantes.SEPARADOR_REGISTRO);
            for(int x=0; x<arrayPeriodos.length; x++){
                DatosPeriodoVO periodo = new DatosPeriodoVO();
                    //Separamos los elementos
                    String[] elementosPeriodo = arrayPeriodos[x].split(MeLanbide01Constantes.SEPARADOR_ELEMENTO);
                    try {
                       //Elemento[0]
                        Calendar fechaInicio  = Calendar.getInstance();
                            fechaInicio.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[0]));
                        periodo.setFechaInicio(fechaInicio);
                        //Elemento[1]
                        Calendar fechaFin = Calendar.getInstance();
                            fechaFin.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[1]));
                        periodo.setFechaFin(fechaFin);
                        //Elemento[2]
                        String porcSub = elementosPeriodo[2];
                        periodo.setPorcSubven(porcSub);
                        //Elemento[3]
                        String numDias = elementosPeriodo[3];
                        periodo.setNumDias(Integer.valueOf(numDias));
                        //Elemento[4]
                        if(elementosPeriodo.length > 4){
                            String baseCotizacion = elementosPeriodo[4];
                            periodo.setBaseCotizacion(baseCotizacion);
                        }//if(elementosPeriodo.length > 3)
                        //Elemento[5]
                        if(elementosPeriodo.length > 5){
                            String bonificacion = elementosPeriodo[5];
                            periodo.setBonificacion(bonificacion);
                        }//if(elementosPeriodo.length > 3)
                        //Elemento[6]
                        if(elementosPeriodo.length > 6){
                            String gasto = elementosPeriodo[6];
                            periodo.setGasto(gasto);
                        }//if(elementosPeriodo.length > 3)
                        
                        if(elementosPeriodo.length > 7){
                            String porcJornRealizada = elementosPeriodo[7];
                            porcJornRealizada = (porcJornRealizada != null && !porcJornRealizada.isEmpty() && !porcJornRealizada.equalsIgnoreCase("null") ? porcJornRealizada : "0");
                            periodo.setPorcJornRealizada(Double.parseDouble(porcJornRealizada));
                        }
                        if (elementosPeriodo.length > 8) {
                            String porcJornSutitucion = elementosPeriodo[8];
                            porcJornSutitucion = (porcJornSutitucion != null && !porcJornSutitucion.isEmpty() && !porcJornSutitucion.equalsIgnoreCase("null") ? porcJornSutitucion : "0");
                            periodo.setPorcJornSustitucion(Double.parseDouble(porcJornSutitucion));
                        }
                        periodos.add(periodo);
                    } catch (ParseException ex) {
                        log.error("Se ha producido un error recuperando los datos de los periodos");
                    }//try-catch
            }//for(int x=0; x<arrayPeriodos.length; x++)
            //Fin periodos

            if(log.isDebugEnabled()) log.debug("Recuperamos el nuevo periodo");
            String stringNuevoPeriodo = (String) request.getParameter("nuevoPeriodo");
            DatosPeriodoVO nuevoPeriodo = new DatosPeriodoVO();
            String[] elementosPeriodo = stringNuevoPeriodo.split(MeLanbide01Constantes.SEPARADOR_ELEMENTO);
            try {
                //Elemento[0]
                Calendar fechaInicio  = Calendar.getInstance();
                    fechaInicio.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[0]));
                nuevoPeriodo.setFechaInicio(fechaInicio);
                //Elemento[1]
                Calendar fechaFin = Calendar.getInstance();
                    fechaFin.setTime(formatFecha_dd_MM_yyyy.parse(elementosPeriodo[1]));
                nuevoPeriodo.setFechaFin(fechaFin);
                //Elemento[2]
                if(elementosPeriodo[2] != null && !"".equalsIgnoreCase(elementosPeriodo[2])){
                    porcSubvenc = elementosPeriodo[2];
                    nuevoPeriodo.setPorcSubven(elementosPeriodo[2]);
                }else{
                    nuevoPeriodo.setPorcSubven(porcSubvenc);
                }//if(elementosPeriodo[2] != null && !"".equalsIgnoreCase(elementosPeriodo[2]))
                //Elemento[3]
                String numDias = elementosPeriodo[3];
                nuevoPeriodo.setNumDias(Integer.valueOf(numDias));
                //Elemento[4]
                String baseCotizacion = elementosPeriodo[4];
                nuevoPeriodo.setBaseCotizacion(baseCotizacion);
                //Elemento[5]
                String bonificacion = elementosPeriodo[5];
                nuevoPeriodo.setBonificacion(bonificacion);
                //Elemento[6]
                String gasto = calcularGasto(baseCotizacion, bonificacion, porcSubvenc);
                nuevoPeriodo.setGasto(gasto);
                
                String porcJornRealizada = elementosPeriodo[7];
                porcJornRealizada = (porcJornRealizada != null && !porcJornRealizada.isEmpty() && !porcJornRealizada.equalsIgnoreCase("null") ? porcJornRealizada : "0");
                nuevoPeriodo.setPorcJornRealizada(Double.parseDouble(porcJornRealizada));

                String porcJornSutitucion = elementosPeriodo[8];
                porcJornSutitucion = (porcJornSutitucion != null && !porcJornSutitucion.isEmpty() && !porcJornSutitucion.equalsIgnoreCase("null") ? porcJornSutitucion : "0");
                nuevoPeriodo.setPorcJornSustitucion(Double.parseDouble(porcJornSutitucion));
                
            } catch (MeLanbide01Exception ex) {
                log.error("Se ha producido un error realizando los calculos del gasto del nuevo periodo", ex);
                error = MeLanbide01Constantes.ERROR_CALCULANDO_GASTO;
            } catch (ParseException ex) {
                log.error("Se ha producido un error ", ex);
            }//try-catch
            //Fin nuevo periodo

            //Recuperamos el ultimo periodo de la lista de periodos para realizar las comparaciones con el nuevo periodo.
            DatosPeriodoVO ultimoPeriodo = new DatosPeriodoVO();
                ultimoPeriodo = periodos.get(periodos.size()-1);

            //Primero comprobamos que el intervalo de fechas del nuevo intervalo se encuentre dentro del mismo mes.
            Boolean mesIntervaloCorrecto = nuevoIntervaloMesCorrecto(nuevoPeriodo);
            if(!mesIntervaloCorrecto){
                error = MeLanbide01Constantes.ERROR_NUEVO_PERIODO_INICIO_FIN_MES_DISTINTO;
            }//if(!mesIntervaloCorrecto)
            
            //Comprobamos que no se dejen periodos con meses vacios por el medio.
            Boolean intervalosConsecutivos = intervalosConsecutivos(nuevoPeriodo, ultimoPeriodo);
            if(!intervalosConsecutivos){
                error = MeLanbide01Constantes.ERROR_NUEVO_PERIODO_NO_CONSECUTIVO;
            }//if(!intervalosConsecutivos)

            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                Boolean nuevoIntervaloMayor = nuevoIntervaloMayor(nuevoPeriodo, ultimoPeriodo);
                if(!nuevoIntervaloMayor){
                    error = MeLanbide01Constantes.ERROR_NUEVO_PERIODO_FECHA_INICIO_INCORRECTA;
                }//if(!nuevoIntervaloMayor)

                if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                    Boolean fusionar = fusionarIntervalos(nuevoPeriodo, ultimoPeriodo);
                    if(fusionar){
                        //Fusionamos el ultimo intervalo con el nuevo ya que pertenecen al mismo mes.
                        DatosPeriodoVO nuevoIntervalo = nuevoIntervalo(nuevoPeriodo, ultimoPeriodo);
                        //Eliminamos el ultimo periodo de la lista.
                        periodos.remove(periodos.size()-1);
                        //Añadimos el nuevo periodo
                        periodos.add(nuevoIntervalo);
                    }else{
                        //Añadimos el nuevo intervalo a la lista ya que es un añadido nuevo.
                        periodos.add(nuevoPeriodo);
                        //Avanzamos el ultimo tramite hasta final de mes para mantener la coherencia con el mes siguiente.
                        avanzarIntervaloFinDeMes(ultimoPeriodo);
                    }//if(fusionar)                
                }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

            //Comprobamos que el numero de intervalos no sea mayor que el maximo permitido
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                if(periodos != null && periodos.size()>0){
                    Integer numMaximoIntervalos = Integer.valueOf(meLanbide01ValidatorUtils.getNumMaximoIntervalosPermitidos(codOrganizacion, codProcedimiento));
                    if(periodos.size() > numMaximoIntervalos){
                        error = MeLanbide01Constantes.ERROR_SUPERA_NUMERO_MAXIMO_PERIODOS;
                    }//if(datosCalculo.getPeriodos().size() > numMaximoIntervalos)
                }//if(datosCalculo.getPeriodos() != null && datosCalculo.getPeriodos().size()>0)
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
            Integer numeroTotalDias = Integer.valueOf(calcularNumeroTotalDias(periodos));
            Melanbide01Decreto decretoAplicable = meLanbide01ValidatorUtils.getDecretoAplicableExptePorFecInicioPeriodosOrFecIniAcciSubvConcedida(codOrganizacion, numExpediente);
            // POr defecto Asignamos el Decreto inicia 177/2010
            String decretoAplicableCod = (decretoAplicable != null ? decretoAplicable.getDecretoCodigo() : Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto());
            /*********************************************************************************************/
            /*********************************************************************************************/
            /*** Calculamos los dias subvencionables restantes para el interesado contratado           ***/
            /*********************************************************************************************/
            /*********************************************************************************************/
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                try{
                    diasRestantes = meLanbide01ValidatorUtils.diasRestantesSubvencionables2
                            (String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio,numeroTotalDias,decretoAplicableCod);
                }catch(MeLanbide01Exception meEx){
                    log.error("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol de persona"
                            + " contratada " + meEx.getMessage());
                    error = MeLanbide01Constantes.ERROR_CALCULANDO_DIAS_RESTANTES;
                }//try-catch
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
            /***
             * Esta validacion no se lleva a Validator de alarmas
             * Se debe hacer con los datos Dinamicos que pone el usario no lo guardado
             * Yas e valida con el metodo alarmaMaximoDias en el validador
             */
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                AlarmaVO alarma = null;
                try {
                    //String codAlarma = alarmaMaximoDias2(String.valueOf(codOrganizacion), 
                    alarma = meLanbide01ValidatorUtils.alarmaMaximoDias2(String.valueOf(codOrganizacion), 
                            numExpediente, codProcedimiento, ejercicio, numeroTotalDias,decretoAplicableCod);
                    if(alarma != null &&(alarma.getCodigoAlarma() != null && !"".equalsIgnoreCase(alarma.getCodigoAlarma()))){
                        //error = MeLanbide01Constantes.MOSTRAR_ALARMA_MAX_DIAS;
                        error= alarma.getCodigoAlarma();
                    }//if(alarma != null &&(alarma.getCodigoAlarma() != null && !"".equalsIgnoreCase(alarma.getCodigoAlarma())))
                } catch (MeLanbide01Exception ex) {
                    log.error("Se ha producido un error comprobando si hay que mostrar la alarma de numero de dias " + ex.getMessage());
                    error = MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_DIAS;
                }//try-catch
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
            log.info("Validacion fuera de plazo pasada a validator ..");
            // Comprobaciones si el expedientes esta fuera de plazo
//            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                String campoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_SOLICITUD", this.getNombreModulo());
//                String tipoCampoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_SOLICITUD", this.getNombreModulo());
//                String campoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", this.getNombreModulo());
//                String tipoCampoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", this.getNombreModulo());
//                if(meLanbide01ValidatorUtils.compruebaSiExpedienteFueraPlazo(codOrganizacion, ejercicio, codProcedimiento, numExpediente, 
//                            campoSuplementarioFechaSolicitud, tipoCampoSuplementarioFechaSolicitud, 
//                            campoSuplementarioFechaFinActuacionConcedido, tipoCampoSuplementarioFechaFinActuacionConcedido))
//                    {
//                        error = MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO;
//                    }
//            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
        }else{
            error = MeLanbide01Constantes.OTRO_ERROR;
        }//if(numExpediente!=null && !"".equals(numExpediente))
        
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(error);
            xmlSalida.append("</CODIGO_OPERACION>");
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                if(periodos.size() > 0){
                    xmlSalida.append("<TOTAL_DIAS>");
                        xmlSalida.append(calcularNumeroTotalDias(periodos));
                    xmlSalida.append("</TOTAL_DIAS>");
                    xmlSalida.append("<DIAS_SUBVENCIONABLES_RESTANTES>");
                            xmlSalida.append(diasRestantes);
                    xmlSalida.append("</DIAS_SUBVENCIONABLES_RESTANTES>");
                    xmlSalida.append("<GASTO_TOTAL>");
                        xmlSalida.append(calcularGastoTotal(periodos));
                    xmlSalida.append("</GASTO_TOTAL>");
                    xmlSalida.append("<PERIODOS>");
                        for(DatosPeriodoVO periodo : periodos){
                            xmlSalida.append("<PERIODO>");
                                xmlSalida.append("<INICIO_PERIODO>");
                                    xmlSalida.append(periodo.getFechaInicioAsString());
                                xmlSalida.append("</INICIO_PERIODO>");
                                xmlSalida.append("<FIN_PERIODO>");
                                    xmlSalida.append(periodo.getFechaFinAsString());
                                xmlSalida.append("</FIN_PERIODO>");
                                xmlSalida.append("<NUM_DIAS>");
                                    xmlSalida.append(periodo.getNumDias());
                                xmlSalida.append("</NUM_DIAS>");
                                if(periodo.getBaseCotizacion() != null && !periodo.getBaseCotizacion().equalsIgnoreCase("")){
                                    xmlSalida.append("<BASE_COTIZACION>");
                                    
                                    /*****/
                                    String aux = periodo.getBaseCotizacion().replace(",",".");
                                    Double dAux = new Double(aux);                                        
                                    xmlSalida.append(Utilities.redondearDosDecimales(dAux));
                                    /******/                                    
                                    
                                    //xmlSalida.append(periodo.getBaseCotizacion());
                                    xmlSalida.append("</BASE_COTIZACION>");
                                }//if(periodo.getBaseCotizacion() != null && !periodo.getBaseCotizacion().equalsIgnoreCase(""))
                                if(periodo.getReducPerSust() != null && !periodo.getReducPerSust().equalsIgnoreCase("")){
                                    xmlSalida.append("<REDUCPERSUST>");
                                        xmlSalida.append(periodo.getReducPerSust());
                                    xmlSalida.append("</REDUCPERSUST>");
                                }//if(periodo.getReducPerSust() != null && !periodo.getReducPerSust().equalsIgnoreCase(""))
                                if(periodo.getJornPersSust() != null && !periodo.getJornPersSust().equalsIgnoreCase("")){
                                    xmlSalida.append("<JORNPERSSUST>");
                                        xmlSalida.append(periodo.getJornPersSust());
                                    xmlSalida.append("</JORNPERSSUST>");
                                }//if(periodo.getJornPersSust() != null && !periodo.getJornPersSust().equalsIgnoreCase(""))
                                if(periodo.getJornPersCont() != null && !periodo.getJornPersCont().equalsIgnoreCase("")){
                                    xmlSalida.append("<JORNPERSCONT>");
                                        xmlSalida.append(periodo.getJornPersCont());
                                    xmlSalida.append("</JORNPERSCONT>");
                                }//if(periodo.getJornPersCont() != null && !periodo.getJornPersCont().equalsIgnoreCase(""))
                                if(periodo.getBonificacion() != null && !periodo.getBonificacion().equalsIgnoreCase("")){
                                    xmlSalida.append("<BONIFICACION>");
                                    
                                        /*****/
                                        String aux = periodo.getBonificacion().replace(",",".");
                                        Double dAux = new Double(aux);                                        
                                        xmlSalida.append(Utilities.redondearDosDecimales(dAux));
                                        /******/
                                        
                                        //xmlSalida.append(periodo.getBonificacion());
                                    xmlSalida.append("</BONIFICACION>");
                                }//if(periodo.getBonificacion() != null && !periodo.getBonificacion().equalsIgnoreCase(""))
                                if(periodo.getPorcSubven() != null && !periodo.getPorcSubven().equalsIgnoreCase("")){
                                    xmlSalida.append("<PORC_SUBVENC>");
                                        xmlSalida.append(periodo.getPorcSubven());
                                    xmlSalida.append("</PORC_SUBVENC>");
                                }//if(periodo.getPorcSubven() != null && !periodo.getPorcSubven().equalsIgnoreCase(""))
                                if(periodo.getGasto() != null && !periodo.getGasto().equalsIgnoreCase("")){
                                    xmlSalida.append("<GASTO>");
                                    
                                        /*****/
                                        String aux = periodo.getGasto().replace(",",".");
                                        Double dAux = new Double(aux);                                        
                                        xmlSalida.append(Utilities.redondearDosDecimales(dAux));
                                        /******/    
                                    
                                        //xmlSalida.append(periodo.getGasto());
                                    xmlSalida.append("</GASTO>");
                                }//if(periodo.getGasto != null && !periodo.getGasto().equalsIgnoreCase(""))
                                
                                if(periodo.getPorcJornRealizada()!= null){
                                    xmlSalida.append("<PORC_JORN_REALIZADA>");                                    
                                        xmlSalida.append(Utilities.redondearDosDecimales(periodo.getPorcJornRealizada()));
                                    xmlSalida.append("</PORC_JORN_REALIZADA>");
                                }
                                if(periodo.getPorcJornSustitucion()!= null){
                                    xmlSalida.append("<PORC_JORN_SUSTITUCION>");                                    
                                        xmlSalida.append(Utilities.redondearDosDecimales(periodo.getPorcJornSustitucion()));
                                    xmlSalida.append("</PORC_JORN_SUSTITUCION>");
                                }
                                
                            xmlSalida.append("</PERIODO>");
                        }//for(DatosPeriodoVO periodo : periodos)
                    xmlSalida.append("</PERIODOS>");
                }//if(periodos.size() > 0)
            }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Se ha producido un error comprobando el nuevo tramo ", e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("comprobarDatosAnhadir() : END");
    }//comprobarDatos
    
    
    private void limpiarPeriodos(DatosCalculoVO obj){
        
        if(obj!=null){
            ArrayList<DatosPeriodoVO> periodos = obj.getPeriodos();
            ArrayList<DatosPeriodoVO> auxiliar = new ArrayList<DatosPeriodoVO>();
            
            for(int i=0;periodos!=null && i<periodos.size();i++){
                
                DatosPeriodoVO periodo = periodos.get(i);
                if(periodo.getPorcSubven()==null || "null".equalsIgnoreCase(periodo.getPorcSubven())){
                    periodo.setPorcSubven("");
                }
                
                if(periodo.getBaseCotizacion()==null || "null".equalsIgnoreCase(periodo.getBaseCotizacion())){
                    periodo.setBaseCotizacion("");
                }
                
                if(periodo.getBonificacion()==null || "null".equalsIgnoreCase(periodo.getBonificacion())){
                    periodo.setBonificacion("");
                }
                
                if(periodo.getGasto()==null || "null".equalsIgnoreCase(periodo.getGasto())){
                    periodo.setGasto("");
                }
                auxiliar.add(periodo);
           }// for
            
           obj.setPeriodos(auxiliar);
        }
    }
    
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
    public String prepararSubvencionConciliacion(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        if(log.isDebugEnabled()) log.debug("prepararSubvencionConciliacion() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        DatosCalculoVO datosCalculo = new DatosCalculoVO();
        ArrayList<DatosPeriodoVO> intervaloFechas = new ArrayList<DatosPeriodoVO>();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide01Manager meLanbide01Manager = MeLanbide01Manager.getInstance();
        String redireccion = null;
        String error = MeLanbide01Constantes.TODO_CORRECTO;
        
        //Campos suplementarios.
        Calendar fechaInicio = null;
        Calendar fechaFin = null;
        String jornPersCont = "";
        String jornPersSust = "";
        String reducPersSust = "";
        String porcSubvenc = "";
        String codAlarmaMaxDias = "";
        String codAlarmaMaxAnhos = "";
        
        String operacion = request.getParameter("operacion");
        String CODIGO_CAMPO_SUPLEMENTARIO_IMPORTE_SUBVENCION = null;
        String CODIGO_CAMPO_SUPLEMENTARIO_TOTAL_DIAS_CONCEDIDOS = null;
        log.debug("prepararSubvencionConciliacion ===========> ");
        
        
        try{
        
        if(numExpediente!=null && !"".equals(numExpediente)){
            String[] datos          = numExpediente.split(MeLanbide01Constantes.BARRA);
            String ejercicio        = datos[0];
            String codProcedimiento = datos[1];
            
            //JSP a la que se produce la redireccion
            redireccion    = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.PANTALLA_EXPEDIENTE + "SALIDA",this.getNombreModulo());
            log.debug("JSP de salida: " + redireccion);
            

            CODIGO_CAMPO_SUPLEMENTARIO_IMPORTE_SUBVENCION = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/NOMBRECAMPO/IMPORTE_SUBVENCION",this.getNombreModulo());
            CODIGO_CAMPO_SUPLEMENTARIO_TOTAL_DIAS_CONCEDIDOS = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/CAMPO_TOTAL_DIAS_CONCEDIDOS_SUBVENCION",this.getNombreModulo());

            AdaptadorSQLBD adapt = ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try {
                //Intentamos recuperar los datos de la BBDD.
                datosCalculo = meLanbide01Manager.getDatosCalculo(numExpediente, String.valueOf(codOrganizacion), 
                        adapt);
            } catch (MeLanbide01Exception ex) {
                log.debug("Se ha producido un error recuperando los datos del cálculo del expediente de la BBDD:: " + ex.getMessage());
                log.error("Se ha producido un error recuperando los datos del cálculo del expediente de la BBDD:: " + ex.getMessage());
            }//try-catch

         
            if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                
                Melanbide01Decreto decretoAplicable = meLanbide01ValidatorUtils.getDecretoAplicableExptePorFecInicioPeriodosOrFecIniAcciSubvConcedida(codOrganizacion, numExpediente);
                // POr defecto Asignamos el Decreto inicia 177/2010
                String decretoAplicableCod = (decretoAplicable != null ? decretoAplicable.getDecretoCodigo() : Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto());                
                //Si el numero de expediente de datosCalculo es distinto de null y tiene periodos usamos esa informacion
                if((datosCalculo.getNumExpediente() != null && !datosCalculo.getNumExpediente().equalsIgnoreCase("")) &&
                        (datosCalculo.getPeriodos() != null && datosCalculo.getPeriodos().size() > 0)){
                    
                    datosCalculo.setSumaTotalDiasPeriodos(calcularNumeroTotalDias(datosCalculo.getPeriodos()));
                    datosCalculo.setImporteSubvencionado(calcularGastoTotal(datosCalculo.getPeriodos()));
                    
                    //Calculamos los dias restantes subvencionables
                    Integer diasRestantes = 0;
                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        try{
                            diasRestantes = meLanbide01ValidatorUtils.diasRestantesSubvencionables(String.valueOf(codOrganizacion), numExpediente, 
                                    codProcedimiento, ejercicio,decretoAplicableCod);
                        }catch(MeLanbide01Exception meEx){
                            log.debug("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol "
                                    + "de persona contratada: " + meEx.getMessage());
                            log.error("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol "
                                    + "de persona contratada: " + meEx.getMessage());
                            error = MeLanbide01Constantes.ERROR_CALCULANDO_DIAS_RESTANTES;
                        }//try-catch
                    }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                    datosCalculo.setDiasRestantesSubvencionables(String.valueOf(diasRestantes));
            
                    //Comprobamos que los anhos de la persona dependiente no supere al maximo fijado para el tipo de actividad del expediente
                    //definido en un parametro de configuracion.
                    //if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        log.info("Validacion Alarma Anio Movida Validator ...");
                    //}//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                    
                    //Comprobamos que la fecha de nacimiento de la persona dependiente este cubierta
                    //if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        log.info("Validacion fecha nacimiento Movida Validator ...");
                    //}//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                    
                    if(datosCalculo!=null && datosCalculo.getDescuento()>0)
                        request.setAttribute("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS",Double.toString(datosCalculo.getDescuento()));                    
                    else
                        request.setAttribute("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS",ConfigurationParameter.getParameter("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS",this.getNombreModulo()));                    
                    
                    request.setAttribute("CODIGO_CAMPO_SUPLEMENTARIO_TOTAL_DIAS_CONCEDIDOS",CODIGO_CAMPO_SUPLEMENTARIO_TOTAL_DIAS_CONCEDIDOS);
                    request.setAttribute("CODIGO_CAMPO_SUPLEMENTARIO_IMPORTE_SUBVENCION",CODIGO_CAMPO_SUPLEMENTARIO_IMPORTE_SUBVENCION);
                    
                    // Comprobación de si expedientes esta fuera de plazo o no-- dgc 26/01/2015
                    log.info("Validacion Fuera de Plazo Movida Validator ...");
                    
                    
                    request.setAttribute("error", error);
                    request.setAttribute("datos_calculo", datosCalculo);
                }else{
                    //Recuperamos el valor de los campos suplementarios de INICONTRATO y FINCONTRATO para calcular los intervalos
                    //y el numero de dias.
//                    String campoSuplementarioInicioContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.CAMPO_INICIO_CONTRATO, this.getNombreModulo());
//                    String tipoCampoSuplementarioInicioContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_CAMPO_INICIO_CONTRATO, this.getNombreModulo());
//                    String campoSuplementarioFinContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.CAMPO_FIN_CONTRATO, this.getNombreModulo());
//                    String tipoCampoSuplementarioFinContrato = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_CAMPO_FIN_CONTRATO, this.getNombreModulo());
                    
                    fechaInicio = meLanbide01ValidatorUtils.getCampoSuplementarioFecInicioAccionSubvConcedida(codOrganizacion, String.valueOf(ejercicio), numExpediente, codProcedimiento);
                    fechaFin = meLanbide01ValidatorUtils.getCampoSuplementarioFecFinAccionSubvConcedida(codOrganizacion, String.valueOf(ejercicio), numExpediente, codProcedimiento);
                    reducPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioReducPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                    jornPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                    jornPersCont =  meLanbide01ValidatorUtils.getCampoSuplementarioJornPersCont(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
                    if(fechaInicio==null || fechaFin==null){
                        error = (fechaInicio == null ? MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA : MeLanbide01Constantes.ERROR_FECHA_FIN_NULA );
                    }
                    log.info("FechaIniActSubvConcedida: " + (fechaInicio!=null ? formatFecha_dd_MM_yyyy.format(fechaInicio.getTime()) : "-" )
                            + " FechaFinActSubvConcedida: " + (fechaFin!=null ? formatFecha_dd_MM_yyyy.format(fechaFin.getTime()) : "-" )
                            + " reducPersSust: " + (reducPersSust!=null ? reducPersSust : "-" )
                            + " jornPersSust: " + (jornPersSust!=null ? jornPersSust : "-" )
                            + " jornPersCont: " + (jornPersCont!=null ? jornPersCont : "-" )
                    );
                    
                    
//                    if(log.isDebugEnabled()){
//                        log.debug("Campo suplementario inicio de contrato = " + campoSuplementarioInicioContrato);
//                        log.debug("Tipo campo suplementario inicio de contrato = " + tipoCampoSuplementarioInicioContrato);
//                        log.debug("Campo suplementario fin de contrato = " + campoSuplementarioFinContrato);
//                        log.debug("Tipo campo suplementario fin de contrato = " + tipoCampoSuplementarioFinContrato);
//                    }//if(log.isDebugEnabled())
//
//                    //Recuperamos el campo de fecha de inicio del contrato
//                    CampoSuplementarioModuloIntegracionVO fechaInicioContrato = new CampoSuplementarioModuloIntegracionVO();
//                    salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, 
//                                    codProcedimiento, campoSuplementarioInicioContrato, Integer.parseInt(tipoCampoSuplementarioInicioContrato));
//                    if(salida.getStatus() == 0){
//                        fechaInicioContrato = salida.getCampoSuplementario();
//                        fechaInicio = fechaInicioContrato.getValorFecha();
//                        if(fechaInicio != null){
//                            if(log.isDebugEnabled()){
//                                log.debug("Fecha de inicio = " + formatFecha_dd_MM_yyyy.format(fechaInicio.getTime()));
//                            }//if(log.isDebugEnabled())
//                        }else{
//                            //Si la fecha es nula lo marcamos como error.
//                        error = MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA; 
//                        }//if(fechaInicio != null)
//                    }else{
//                        error = MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA;
//                    }//if(salida.getStatus() == 0)
//
//                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                        //Recuperamos el campo de fecha de fin del contrato
//                        CampoSuplementarioModuloIntegracionVO fechaFinContrato = new CampoSuplementarioModuloIntegracionVO();
//                        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, 
//                                        codProcedimiento, campoSuplementarioFinContrato, Integer.parseInt(tipoCampoSuplementarioFinContrato));
//                        if(salida.getStatus() == 0){
//                            fechaFinContrato = salida.getCampoSuplementario();
//                            fechaFin = fechaFinContrato.getValorFecha();
//                            if(fechaFin != null){
//                                if(log.isDebugEnabled()){
//                                    log.debug("Fecha de fin = " + formatFecha_dd_MM_yyyy.format(fechaFin.getTime()));
//                                }//if(log.isDebugEnabled())
//                            }else{
//                                error = MeLanbide01Constantes.ERROR_FECHA_FIN_NULA;
//                            }//if(fechaFin != null)
//                        }else{
//                            error = MeLanbide01Constantes.ERROR_FECHA_FIN_NULA;
//                        }//if(salida.getStatus() == 0)
//                    }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

//                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                        try {
//                            //Recuperamos el campo MeLanbide01Constantes.REDUCPERSSUST
//                            reducPersSust = getCampoSuplementarioReducPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
//                            //Comprobamos que este campo sea mayor que el porcentaje indicado en el properties
//                            Double minReducPersSust = Double.valueOf(getMinReducPersSust(codOrganizacion, codProcedimiento));
//                            Double suplReducPersSust = Double.valueOf(reducPersSust);
//                            if(minReducPersSust > suplReducPersSust){
//                                error = MeLanbide01Constantes.ERROR_MIN_REDUCPERSSUST;
//                            }//if(minReducPersSust > suplReducPersSust)
//                        } catch (MeLanbide01Exception ex) {
//                            log.error("Se ha producido un error cargando el campo suplementario de la reducción de la persona sustituida: "  + ex.getMessage());
//                            log.error("Se ha producido un error cargando el campo suplementario de la reducción de la persona sustituida", ex);
//                            error = MeLanbide01Constantes.ERROR_CAMPO_REDUC_PERS_SUST;
//                        }//try-catch
//                    }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

//                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                        try {
//                            //Recuperamos el campo JORNPERSSUST
//                            jornPersSust = getCampoSuplementarioJornPersSust(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
//                        } catch (MeLanbide01Exception ex) {
//                            log.debug("Se ha producido un error cargando el campo suplementario de la jornada de la persona sustuida: " + ex.getMessage());
//                            log.error("Se ha producido un error cargando el campo suplementario de la jornada de la persona sustuida", ex);
//                            error = MeLanbide01Constantes.ERROR_CAMPO_JORN_PERS_SUST;
//                        }//try-catch
//                    }
                    
                    
//                     if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                        try {
//                            //Recuperamos el campo JORNPERSCONT
//                            jornPersCont = getCampoSuplementarioJornPersCont(codOrganizacion, ejercicio, numExpediente, codProcedimiento);
//                        } catch (MeLanbide01Exception ex) {
//                            log.debug("Se ha producido un error cargando el campo suplementario de la jornada de la persona contratada: " + ex.getMessage());
//                            log.error("Se ha producido un error cargando el campo suplementario de la jornada de la persona contratada: " + ex.getMessage());
//                            error = MeLanbide01Constantes.ERROR_CAMPO_JORN_PERS_SUST;
//                        }//try-catch
//                    }
                                     
                    
                    
                    
                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        try {
                            intervaloFechas = calcularIntervaloFechas(fechaInicio, fechaFin);
                            for(DatosPeriodoVO intervalo : intervaloFechas){
                                if(log.isDebugEnabled()){
                                    String stringFechaInicio = formatFecha_dd_MM_yyyy.format(intervalo.getFechaInicio().getTime());
                                    String stringFechaFin = formatFecha_dd_MM_yyyy.format(intervalo.getFechaFin().getTime());
                                    log.debug("Intervalo = " + stringFechaInicio + " - " + stringFechaFin);
                                }//if(log.isDebugEnabled())

                            }//for(ConciliacionVO intervalo : intervaloFechas)
                            //Calculamos el numero de horas entre los intervalos
                            calcularNumeroDias(intervaloFechas);

                            //Si los campos suplementarios estan todos cubiertos calculamos los datos.
                            datosCalculo.setPeriodos(intervaloFechas);
                        }catch (MeLanbide01Exception ex){
                            log.debug("Se ha producido un error calculando el intervalo de fechas: " +  ex.getMessage());
                            log.error("Se ha producido un error calculando el intervalo de fechas", ex);
                            Integer codigoError = ex.getCodError();
                            if(codigoError == MeLanbide01Exception.ERROR_FECHA_INICIO_SUPERIOR_FIN){
                                error = MeLanbide01Constantes.ERROR_FECHA_INICIO_POSTERIOR_FIN;
                            }else{
                                error = MeLanbide01Constantes.OTRO_ERROR;
                            }//if(codigoError
                        }//try-catch
                    }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        if(log.isDebugEnabled()) log.debug("Rellenamos los campos suplementarios");
                        rellenarCamposSuplementarios(intervaloFechas, reducPersSust, jornPersSust, jornPersCont);
                        
                        //Si los campos suplementarios tienen datos calculamos los valores de porcSubvenc y gasto
                        if((reducPersSust != null && !reducPersSust.equalsIgnoreCase(""))
                                && (jornPersSust != null && !jornPersSust.equalsIgnoreCase("")) && (jornPersCont != null && !jornPersCont.equalsIgnoreCase(""))){
                            if(log.isDebugEnabled()) log.debug("Procedemos a calcular los valores");
                            try {
                                porcSubvenc = meLanbide01ValidatorUtils.calcularPorcSubvenc(reducPersSust, jornPersSust, jornPersCont);
                            } catch (MeLanbide01Exception ex) {
                                log.debug("Se ha producido un error calculando el porcentaje de subvencion: " +  ex.getMessage());
                                log.error("Se ha producido un error calculando el porcentaje de subvencion", ex);
                                error = MeLanbide01Constantes.ERROR_CALCULANDO_PORC_SUBVENC;
                            }//try-catch
                        }/*if((reducPersSust != null && !reducPersSust.equalsIgnoreCase(""))
                                && (jornPersSust != null && !jornPersSust.equalsIgnoreCase("")) && (jornPersCont != null && !jornPersCont.equalsIgnoreCase("")))*/
                        
                        if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                            rellenarPorcSubvenc(intervaloFechas, porcSubvenc);
                            datosCalculo.setPeriodos(intervaloFechas);
                            datosCalculo.setSumaTotalDiasPeriodos(calcularNumeroTotalDias(datosCalculo.getPeriodos()));
                        }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                    }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                    
                    //Calculamos los dias restantes subvencionables
                    Integer diasRestantes = new Integer(0);
                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        try{
                            diasRestantes = meLanbide01ValidatorUtils.diasRestantesSubvencionables(String.valueOf(codOrganizacion), numExpediente, 
                                    codProcedimiento, ejercicio,decretoAplicableCod);
                        }catch(MeLanbide01Exception meEx){
                            log.error("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol "
                                    + "de persona contratada " + meEx.getMessage());
                            log.error("Se ha producido un error calculando los dias restantes subvencionables para el interesado con rol "
                                    + "de persona contratada " + meEx.getMessage());
                            error = MeLanbide01Constantes.ERROR_CALCULANDO_DIAS_RESTANTES;
                        }//try-catch
                    }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                    datosCalculo.setDiasRestantesSubvencionables(String.valueOf(diasRestantes));
                    
                    //Comprobamos que el numero de intervalos no sea mayor que el maximo permitido
                    //if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        log.info("Validacion numero intervalos pasada a Validator ...");
                    //}//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
                    
                    //Comprobamos que el numero total de dias de todos los expedientes en los que coincide el interesado con un rol dado
                    //no superen un maximo definido en un parametro de configuracion
                    //if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
                        log.info("Validacion MaximoDias pasado a valdator");
                    //}//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))
            
                    //Comprobamos que los anhos de la persona dependiente no supere al maximo fijado para el tipo de actividad del expediente
                    //definido en un parametro de configuracion.
                   
                    //Comprobamos que la fecha de nacimiento de la persona dependiente este cubierta
                    log.info("Validacion Fecha Nacimiento pasado a valdator");
//                    if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO)){
//                        try{
//                            Calendar fecha = getCampoSuplementarioFecNacPersonaDependiente
//                                    (codOrganizacion, ejercicio, numExpediente, codProcedimiento);
//                            
//                            if(fecha == null){
//                                error = MeLanbide01Constantes.ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA;
//                            }//if(fecha == null)
//                        }catch(MeLanbide01Exception meEx){
//                            log.debug("Se ha producido un error comprobando si la fecha de nacimiento de la persona dependiente está cubierta"
//                                    + " " + meEx.getMessage());
//                            
//                            log.error("Se ha producido un error comprobando si la fecha de nacimiento de la persona dependiente está cubierta"
//                                    + " " + meEx.getMessage());
//                            error = MeLanbide01Constantes.ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA;
//                        }
//                    }//if(error.equalsIgnoreCase(MeLanbide01Constantes.TODO_CORRECTO))

                    limpiarPeriodos(datosCalculo);
                    if(log.isDebugEnabled()) log.debug("error = " + error);
                                        
                    String nombreModulo = this.getNombreModulo();
                    String valorPropiedad = ConfigurationParameter.getParameter("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS",this.getNombreModulo());                    
                    if(datosCalculo!=null && datosCalculo.getDescuento()>0)
                        request.setAttribute("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS",Double.toString(datosCalculo.getDescuento()));                                            
                    else{
                        request.setAttribute("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS",ConfigurationParameter.getParameter("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS",this.getNombreModulo()));                    
                        // Como no hay períodos, entonces no se han grabado en base de datos, por tanto, tampoco se ha almacenado
                        // el descuento, entonces el descuento será el valor
                        datosCalculo.setDescuento(Integer.parseInt(ConfigurationParameter.getParameter("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS",this.getNombreModulo())));
                    }                        
                    
                    // Comprobación de si expedientes esta fuera de plazo o no-- dgc 26/01/2015
                    log.info("Validacion Expediente Fuera Plazo pasado a valdator");
//                    String campoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_SOLICITUD",this.getNombreModulo());
//                    String tipoCampoSuplementarioFechaSolicitud  = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_SOLICITUD",this.getNombreModulo());
//                    String campoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO",this.getNombreModulo());
//                    String tipoCampoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO",this.getNombreModulo());
//                    if(meLanbide01ValidatorUtils.compruebaSiExpedienteFueraPlazo(codOrganizacion, ejercicio, codProcedimiento, numExpediente, 
//                            campoSuplementarioFechaSolicitud, tipoCampoSuplementarioFechaSolicitud, 
//                            campoSuplementarioFechaFinActuacionConcedido, tipoCampoSuplementarioFechaFinActuacionConcedido))
//                    {
//                        error = MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO;
//                    }
                    
                    request.setAttribute("CODIGO_CAMPO_SUPLEMENTARIO_TOTAL_DIAS_CONCEDIDOS",CODIGO_CAMPO_SUPLEMENTARIO_TOTAL_DIAS_CONCEDIDOS);
                    request.setAttribute("CODIGO_CAMPO_SUPLEMENTARIO_IMPORTE_SUBVENCION",CODIGO_CAMPO_SUPLEMENTARIO_IMPORTE_SUBVENCION);
                    // En la carga de la pagina, trasladamos todo a Validator con Modal
                    //request.setAttribute("error", error);
                    request.setAttribute("datos_calculo", datosCalculo);
                }//if(numExpediente!=null && !"".equals(numExpediente))
                
                // Inforacion regla Minimis reglamento Europeo
                double impTotalRecibidoEmpreReglaMinimis=0;
                boolean minimisValidado=true;
                try {
                    impTotalRecibidoEmpreReglaMinimis=meLanbide01Manager.getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios(codOrganizacion, codProcedimiento, numExpediente);
                    String impTotalRecibidoEmpreReglaMinimistr=Utilities.formatearNumeroDecimalSeparadores(impTotalRecibidoEmpreReglaMinimis);
                    log.info("impTotalRecibidoEmpreReglaMinimis: " + impTotalRecibidoEmpreReglaMinimistr);
                    String limiteReglaMinimis=ConfigurationParameter.getParameter(codOrganizacion+"/REGLA_MINIMIS_MAXIMO_IMPORTE",this.getNombreModulo());
                    limiteReglaMinimis = (limiteReglaMinimis != null && !limiteReglaMinimis.isEmpty() ? limiteReglaMinimis : "0");
                    limiteReglaMinimis=Utilities.formatearNumeroDecimalSeparadores(Double.valueOf(limiteReglaMinimis));
                    log.info("limiteReglaMinimis: " + limiteReglaMinimis);
                    request.setAttribute("impTotalRecibidoEmpreReglaMinimis", impTotalRecibidoEmpreReglaMinimistr);
                    request.setAttribute("limiteReglaMinimis", limiteReglaMinimis);
                } catch (Exception e) {
                    log.error("Error recuperando los datos para regla de minimos aplicada a la empresa del expediente " + e.getMessage(), e);
                    impTotalRecibidoEmpreReglaMinimis=-1;
                }
                
                Melanbide01Decreto melanbide01Decreto = new Melanbide01Decreto();
                List<MeLanbide01ValidatorResult> validacionesAlarmasCONCM = new ArrayList<MeLanbide01ValidatorResult>();
                int idioma = 1;
                try {
                    // Lo hacemos aqui asi, porque yas e ha leido de base de dato o calculado con la fecha de inicio accion subv. concedida.
                    if(datosCalculo!=null && datosCalculo.getPeriodos()!=null && datosCalculo.getPeriodos().size()>0){
                        String fechaInicialPeriodoSubvencio = datosCalculo.getPeriodos().get(0).getFechaInicioAsString();
                        melanbide01Decreto=meLanbide01Manager.getDecretoAplicableExpediente(formatFecha_dd_MM_yyyy.parse(fechaInicialPeriodoSubvencio), adapt);
                    }
                    request.setAttribute("melanbide01Decreto", melanbide01Decreto);
                    MeLanbide01Validator meLanbide01Validator = new MeLanbide01Validator();
                    if (request.getSession()!=null &&  request.getSession().getAttribute("usuario") != null) {
                        idioma = ((UsuarioValueObject) request.getSession().getAttribute("usuario")).getIdioma();
                    }
                    if (melanbide01Decreto != null
                            && melanbide01Decreto.getDecretoCodigo() != null && !melanbide01Decreto.getDecretoCodigo().isEmpty()) {
                        validacionesAlarmasCONCM = meLanbide01Validator.validacionesAlarmasCONCM(codOrganizacion, melanbide01Decreto.getDecretoCodigo(), numExpediente, idioma);
                    } else {
                        MeLanbide01ValidatorResult meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                        meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS));
                        meLanbide01ValidatorResult.setDescripcion(MeLanbide01I18n.getInstance().getMensaje(idioma, MeLanbide01Constantes.COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                        validacionesAlarmasCONCM.add(meLanbide01ValidatorResult);
                    }
                    // Agregamos la validacion de intervalos
                    if(MeLanbide01Constantes.ERROR_FECHA_INICIO_POSTERIOR_FIN.equalsIgnoreCase(error)){
                        MeLanbide01ValidatorResult meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                        meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_FECHA_INICIO_POSTERIOR_FIN));
                        meLanbide01ValidatorResult.setDescripcion(MeLanbide01I18n.getInstance().getMensaje(idioma, MeLanbide01Constantes.ERROR_FECHA_INICIO_POSTERIOR_FIN + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                        validacionesAlarmasCONCM.add(meLanbide01ValidatorResult);
                    }
                } catch (Exception e) {
                    log.error("Error leyendo los datos del Decreto que aplica al expediente", e);
                    MeLanbide01ValidatorResult meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS));
                    meLanbide01ValidatorResult.setDescripcion(MeLanbide01I18n.getInstance().getMensaje(idioma, MeLanbide01Constantes.COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                    validacionesAlarmasCONCM.add(meLanbide01ValidatorResult);
                }
                request.setAttribute("validacionesAlarmasCONCM", validacionesAlarmasCONCM);
                
                // Cargamos URL Y datos de las Subpestana de Hisotiral y Causantes
                prepararSubvencionConciliacionPestanaHistorialSubv(codOrganizacion,numExpediente,request,adapt);
                prepararSubvencionConciliacionPestanaCausantesSubv(codOrganizacion,numExpediente,request,adapt);
            }
            // Cargamos automaticamente los datos de Feccha Nacimiento Personas Sustituida/Contratada
            try {
                meLanbide01OtherServices.validarActualizarFechaNacimientoPersSustYContr(numExpediente, adapt);
            } catch (Exception e) {
                log.error("Se rpesento un error al cargar/actualizar las fechas de nacimiento de persona sustituida/contrada .. "+e.getMessage(),e);
            }
            
        }
        }catch(Exception tgz){
            tgz.printStackTrace();
            log.debug("Error en prepararSubvencionConciliacion() :: " + tgz.getMessage());
            log.error("Error en prepararSubvencionConciliacion() :: " + tgz.getMessage());
        }
  
        if(log.isDebugEnabled()) log.debug("prepararSubvencionConciliacion() : END");
        return redireccion;       
    }//prepararSubvencionConciliacion
    
    /**
     * Avanza la fecha de fin de un intervalo hasta el ultimo dia del mes
     * @param ultimoIntervalo
     * @return 
     */
    private DatosPeriodoVO avanzarIntervaloFinDeMes (DatosPeriodoVO ultimoIntervalo){
        if(log.isDebugEnabled()) log.debug("avanzarIntervaloFinDeMes() : BEGIN");
        if(ultimoIntervalo != null){
            GregorianCalendar cal = new GregorianCalendar();
            Integer diaFin = new Integer(0);
            Integer mesFin = ultimoIntervalo.getFechaFin().get(Calendar.MONTH);
            if(cal.isLeapYear(ultimoIntervalo.getFechaFin().get(Calendar.YEAR))){
                diaFin = MONTHS_LEAP.get(mesFin);
            }else{
                diaFin = MONTHS.get(mesFin);
            }//if(cal.isLeapYear(ultimoIntervalo.getFechaFin().get(Calendar.YEAR)))
            ultimoIntervalo.getFechaFin().set(Calendar.DAY_OF_MONTH, diaFin);
        }//if(ultimoIntervalo != null)
        if(log.isDebugEnabled()) log.debug("avanzarIntervaloFinDeMes() : END");
        return ultimoIntervalo;
    }//avanzarIntervaloFinDeMes
    
    /**
     * Comprueba si un intervalo esta dentro del mismo mes
     * @param nuevoIntervalo
     * @return 
     */
    private Boolean nuevoIntervaloMesCorrecto(DatosPeriodoVO nuevoIntervalo){
        if(log.isDebugEnabled()) log.debug("nuevoIntervaloMesCorrecto() : BEGIN");
        Boolean correcto = false;
        Calendar fechaInicio = Calendar.getInstance();
            fechaInicio.setTime(nuevoIntervalo.getFechaInicio().getTime());  
        Calendar fechaFin = Calendar.getInstance();
            fechaFin.setTime(nuevoIntervalo.getFechaFin().getTime());
            
        //Meses de inicio y de fin
        Integer mesInicio = fechaInicio.get(Calendar.MONTH);
        Integer mesFin = fechaFin.get(Calendar.MONTH);
        if(mesInicio.equals(mesFin)){
            correcto = true;
        }//if(mesInicio != mesFin)
        
        if(log.isDebugEnabled()) log.debug("correcto = " + correcto);
        if(log.isDebugEnabled()) log.debug("nuevoIntervaloMesCorrecto() : END");
        return correcto;
    }//nuevoIntervaloMesCorrecto
    
    /**
     * Comprueba si un intervalo nuevo es posterior a otro dado
     * @param nuevoIntervalo
     * @param ultimoIntervalo
     * @return 
     */
    private Boolean nuevoIntervaloMayor (DatosPeriodoVO nuevoIntervalo, DatosPeriodoVO ultimoIntervalo){
        if(log.isDebugEnabled()) log.debug("nuevoIntervaloMayor() : BEGIN");
        Boolean correcto = false;
        
        Calendar fechaFinUltimoIntervalo = Calendar.getInstance();
            fechaFinUltimoIntervalo.setTime(ultimoIntervalo.getFechaFin().getTime());
            
        Calendar fechaInicioNuevoIntervalo = Calendar.getInstance();
            fechaInicioNuevoIntervalo.setTime(nuevoIntervalo.getFechaInicio().getTime());
            
        if(fechaInicioNuevoIntervalo.after(fechaFinUltimoIntervalo)){
            correcto = true;
        }//if(fechaInicioNuevoIntervalo.after(fechaFinUltimoIntervalo))
        
        if(log.isDebugEnabled()) log.debug("correcto = " + correcto);
        if(log.isDebugEnabled()) log.debug("nuevoIntervaloMayor() : END");
        return correcto;
    }//nuevoIntervaloMayor
    
    /**
     * Comprueba si dos intervalos están en el mismo mes y pueden fusionarse
     * @param nuevoIntervalo
     * @param ultimoIntervalo
     * @return 
     */
    private Boolean fusionarIntervalos (DatosPeriodoVO nuevoIntervalo, DatosPeriodoVO ultimoIntervalo){
        if(log.isDebugEnabled()) log.debug("fusionarIntervalos() : BEGIN");
        Boolean correcto = false;
        
        Calendar fechaFinUltimoIntervalo = Calendar.getInstance();
            fechaFinUltimoIntervalo.setTime(ultimoIntervalo.getFechaFin().getTime());
            
        Calendar fechaInicioNuevoIntervalo = Calendar.getInstance();
            fechaInicioNuevoIntervalo.setTime(nuevoIntervalo.getFechaInicio().getTime());
            
        //Meses de inicio y de fin
        Integer mesInicio = fechaFinUltimoIntervalo.get(Calendar.MONTH);
        Integer mesFin = fechaInicioNuevoIntervalo.get(Calendar.MONTH);
        
        if(mesInicio.equals(mesFin)){
            correcto = true;
        }//if(mesInicio.equals(mesFin))
        
        if(log.isDebugEnabled()) log.debug("correcto = " + correcto);
        if(log.isDebugEnabled()) log.debug("fusionarIntervalos() : END");
        return correcto;
    }//fusionarIntervalos
    
    private Boolean intervalosConsecutivos(DatosPeriodoVO nuevoIntervalo, DatosPeriodoVO ultimoIntervalo){
        if(log.isDebugEnabled()) log.debug("intervalosConsecutivos() : BEGIN");
        Boolean correcto = false;
        Calendar fechaFinUltimoIntervalo = Calendar.getInstance();
            fechaFinUltimoIntervalo.setTime(ultimoIntervalo.getFechaFin().getTime());
            
        Calendar fechaInicioNuevoIntervalo = Calendar.getInstance();
            fechaInicioNuevoIntervalo.setTime(nuevoIntervalo.getFechaInicio().getTime());
            
        //Meses de inicio y de fin
        Integer mesInicio = fechaFinUltimoIntervalo.get(Calendar.MONTH);
        Integer mesFin = fechaInicioNuevoIntervalo.get(Calendar.MONTH);
        
        Integer anhoInicio = fechaFinUltimoIntervalo.get(Calendar.YEAR);
        Integer anhoFin = fechaInicioNuevoIntervalo.get(Calendar.YEAR);
        
        if(anhoInicio.equals(anhoFin)){
            //El mes de fin tiene que se el anterior al mes de inicio.
            if(mesFin.equals(mesInicio + 1)){
                correcto = true;
            }//if(mesFin.equals(mesInicio + 1))
        }else if(anhoFin.equals(anhoInicio +1)){
            if(mesInicio.equals(11) && mesFin.equals(0)){
                correcto = true;
            }//if(mesInicio.equals(11) && mesFin.equals(0))
        }//if
        if(log.isDebugEnabled()) log.debug("intervalosConsecutivos() : END");
        return correcto;
    }//intervalosConsecutivos
    
    /**
     * Crea un nuevo intervalo con la fecha de inicio de un intervalo y la fecha de fin de otro
     * @param nuevoIntervalo
     * @param ultimoIntervalo
     * @return 
     */
    private DatosPeriodoVO nuevoIntervalo(DatosPeriodoVO nuevoIntervalo, DatosPeriodoVO ultimoIntervalo){
        if(log.isDebugEnabled()) log.debug("nuevoIntervalo() : BEGIN");
        DatosPeriodoVO intervaloFusionado = new DatosPeriodoVO();
        
        //Fecha de inicio del ultimo intervalo
        intervaloFusionado.setFechaInicio(ultimoIntervalo.getFechaInicio());
        
        //Fecha de fin del nuevo intervalo
        intervaloFusionado.setFechaFin(nuevoIntervalo.getFechaFin());
        
        //Tenemos que realizar de nuevo los calculos.
        
        if(log.isDebugEnabled()) log.debug("nuevoIntervalo() : END");
        return intervaloFusionado;
    }//nuevoIntervalo
    
   /**
     * Calcula los intervalos de un expediente por la fecha de inicio y la de fin dadas como parametros
     * @param fechaInicio
     * @param fechaFin
     * @return
     * @throws MeLanbide01Exception 
     */
    private ArrayList<DatosPeriodoVO> calcularIntervaloFechas (Calendar fechaInicio, Calendar fechaFin) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("calcularIntervaloFechas() : BEGIN");
        ArrayList<DatosPeriodoVO> intervaloFechas = new ArrayList();
        GregorianCalendar cal = new GregorianCalendar();
        /****** oscar ***********/
        log.debug(" =================== calcularIntervaloFechas fechaInicio: " + formatFecha_dd_MM_yyyy.format(fechaInicio.getTime()));
        log.debug(" ================== calcularIntervaloFechas fechaFin: " + formatFecha_dd_MM_yyyy.format(fechaFin.getTime()));
        /****** oscar ***********/
        
        //Primero comprobamos si la fecha de inicio es superior a la fecha de fin de contrato
        // Añadimos   || equals que permita un periodo de un dia 08/09/2015 - habilitar coger picos y meter un periodo de una dias
        if(fechaInicio.before(fechaFin) || fechaInicio.equals(fechaFin) ){
            Integer anhoInicio = Integer.valueOf(fechaInicio.get(1));
            Integer anhoFin = Integer.valueOf(fechaFin.get(1));
            Integer mesInicio = Integer.valueOf(fechaInicio.get(2));
            Integer mesFin = Integer.valueOf(fechaFin.get(2));
            Integer diaInicio = Integer.valueOf(fechaInicio.get(5));
            Integer diaFin = Integer.valueOf(fechaFin.get(5));
            
            ArrayList<Integer> arrayAnhos = new ArrayList();
            ArrayList<Integer> meses = new ArrayList();
            
            //Añadimos el primer año
            arrayAnhos.add(anhoInicio);
            //Si el año de fin es distinto que el año de inicio lo añadimos tambien
            if(!anhoInicio.equals(anhoFin)){
               arrayAnhos.add(anhoFin);
            }//if(anhoInicio != anhoFin)
            
            if(arrayAnhos.size() > 1){            
                for(Integer anho : arrayAnhos){
                    Integer mes = null;
                    if(anho == anhoInicio){
                        //Si es el primer año calculamos lo que queda desde el primer mes hasta el final.
                        mes = mesInicio;
                        Integer dia = diaInicio;
                        for(int x=mesInicio; x<12; x++){
                            DatosPeriodoVO intervalo = new DatosPeriodoVO();
                            Calendar inicioIntervalo = Calendar.getInstance();
                            Calendar finIntervalo= Calendar.getInstance();
                            
                            inicioIntervalo.set(anho, x, dia);
                            if(cal.isLeapYear(anho)){
                                finIntervalo.set(anho, x, MONTHS_LEAP.get(x));
                            }else{
                                finIntervalo.set(anho, x, MONTHS.get(x));
                            }//if(cal.isLeapYear(anho))
                            
                            intervalo.setFechaInicio(inicioIntervalo);
                            intervalo.setFechaFin(finIntervalo);
                            intervaloFechas.add(intervalo);
                            if(x== mesInicio){
                                dia = 1;
                            }//if(x== mesInicio)
                        }//for(int x=mesInicio; x<11; x++)
                    }else if(anho == anhoFin){
                        //Si es el ultimo año calculamos lo que hay desde el mes 0 hasta el mes de fin
                        mes = 0;
                        Integer dia = 1;
                        for(int x=0; x<mesFin+1; x++){
                            if(x == mesFin){
                                dia = diaFin;
                            }else{
                                if(cal.isLeapYear(anho)){
                                    dia = MONTHS_LEAP.get(x);
                                }else{
                                    dia = MONTHS.get(x);
                                }//if(cal.isLeapYear(anho))
                            }//if(x == mesFin)
                            DatosPeriodoVO intervalo = new DatosPeriodoVO();
                            Calendar inicioIntervalo = Calendar.getInstance();
                            Calendar finIntervalo= Calendar.getInstance();
                            
                            inicioIntervalo.set(anho, x, 1);
                            finIntervalo.set(anho, x, dia);
                            
                            if(x == mesFin){
                                //Si es el ultimo intervalo le restamos un dia.
                                finIntervalo.add(Calendar.DAY_OF_MONTH, 0); // mod dgc 11/04/2014
                            }//if(x == mesFin)
                            // si omitimos  el igual cuando tenga un periodo de un dia y este sea el ultimo no lo suma dgc 11/04/2014
                            //Si fin intervalo es menor o igual que inicioIntervalo omitimos todo el intervalo		              
                            if(finIntervalo.after(inicioIntervalo) || finIntervalo.equals(inicioIntervalo)){
                                intervalo.setFechaInicio(inicioIntervalo);
                                intervalo.setFechaFin(finIntervalo);
                                intervaloFechas.add(intervalo);
                            }//if(finIntervalo.after(inicioIntervalo))
                        }//for(int x=0; x<diferencia; x++)
                    }else{
                        //Si no es el primer ni el ultimo año sumamos un año completo
                    }//if
                }//for(Integer anho : arrayAnhos)
            }else{
                Integer mes = mesInicio;
                Integer dia = diaInicio;
                for(int x=mesInicio; x<mesFin+1; x++){
                    if(x==mesInicio){
                        DatosPeriodoVO intervalo = new DatosPeriodoVO();
                        Calendar inicioIntervalo = Calendar.getInstance();
                        Calendar finIntervalo= Calendar.getInstance();
                        inicioIntervalo.set(anhoInicio, x, dia);
                        if(mesInicio==mesFin){
                        	if (x == mesFin) {
                        	dia = diaFin;
                        } else if (cal.isLeapYear(anhoInicio)) {
                        	dia = MONTHS_LEAP.get(x);
                        } else {
                        	dia = MONTHS.get(x);
                        }
                        }else{
                        if (cal.isLeapYear(anhoInicio)) {
                        		dia = MONTHS_LEAP.get(x);
                        	}else{
                        			dia = MONTHS.get(x);
                        	}
                        }
                        finIntervalo.set(anhoInicio, x,dia);
                        //if (cal.isLeapYear(anhoInicio)) {
                        //  finIntervalo.set(anhoInicio, x, (MONTHS_LEAP.get(x)));
                        //} else {
                        //  finIntervalo.set(anhoInicio, x, (MONTHS.get(x)));
                        //}
                        intervalo.setFechaInicio(inicioIntervalo);
                        intervalo.setFechaFin(finIntervalo);
                        intervaloFechas.add(intervalo);
                        dia = Integer.valueOf(1);
                        }
                        else
                        {
                        if (x == mesFin) {
                        	dia = diaFin;
                        } else if (cal.isLeapYear(anhoInicio)) {
                        	dia = MONTHS_LEAP.get(x);
                        } else {
                        	dia = MONTHS.get(x);
                        }
                        DatosPeriodoVO intervalo = new DatosPeriodoVO();
                        Calendar inicioIntervalo = Calendar.getInstance();
                        Calendar finIntervalo = Calendar.getInstance();
                        inicioIntervalo.set(anhoInicio, x, 1);
                        finIntervalo.set(anhoInicio, x, dia);
                        if (x == mesFin) {
                        //finIntervalo.add(5, 0);
                        finIntervalo.add(Calendar.DAY_OF_MONTH, 0);
                        }
                        
                        // si omitimos  el igual cuando tenga un periodo de un dia y este sea el ultimo no lo suma dgc 11/04/2014
                        if(finIntervalo.after(inicioIntervalo) || finIntervalo.equals(inicioIntervalo))
                        {
			              intervalo.setFechaInicio(inicioIntervalo);
			              intervalo.setFechaFin(finIntervalo);
			              intervaloFechas.add(intervalo);
                        }
		          }
		        }
		      }
		    }
		    else
		    {
		      this.log.debug("La fecha de incio es posterior a la fecha de fin");
		      MeLanbide01Exception excepcion = new MeLanbide01Exception("La fecha de incio es posterior a la fecha de fin");
		      excepcion.setCodError(MeLanbide01Exception.ERROR_FECHA_INICIO_SUPERIOR_FIN);
		      throw excepcion;
		    }
		    if (this.log.isDebugEnabled()) { this.log.debug("calcularIntervaloFechas() : END");}
		    return intervaloFechas;
  	}
  
    private ArrayList<DatosPeriodoVO> calcularIntervaloFechas (Calendar fechaInicio, Calendar fechaFin, ArrayList<DatosPeriodoVO> periodosOriginales) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("calcularIntervaloFechas() : BEGIN");
        ArrayList<DatosPeriodoVO> intervaloFechas = new ArrayList<DatosPeriodoVO>();
        GregorianCalendar cal = new GregorianCalendar();
        //Primero comprobamos si la fecha de inicio es superior a la fecha de fin de contrato
        if(fechaInicio.before(fechaFin)){
            Integer anhoInicio = fechaInicio.get(Calendar.YEAR);
            Integer anhoFin = fechaFin.get(Calendar.YEAR);
            Integer mesInicio = fechaInicio.get(Calendar.MONTH);
            Integer mesFin = fechaFin.get(Calendar.MONTH);
            Integer diaInicio = fechaInicio.get(Calendar.DAY_OF_MONTH);
            Integer diaFin = fechaFin.get(Calendar.DAY_OF_MONTH);
            
            ArrayList<Integer> arrayAnhos = new ArrayList<Integer>();
            ArrayList<Integer> meses = new ArrayList<Integer>();
            
            //Añadimos el primer año
            arrayAnhos.add(anhoInicio);
            //Si el año de fin es distinto que el año de inicio lo añadimos tambien
            if(!anhoInicio.equals(anhoFin)){
               arrayAnhos.add(anhoFin); 
            }//if(anhoInicio != anhoFin)
            
            if(arrayAnhos.size() > 1){
                for(Integer anho : arrayAnhos){
                    Integer mes = null;
                    if(anho == anhoInicio){
                        //Si es el primer año calculamos lo que queda desde el primer mes hasta el final.
                        mes = mesInicio;
                        Integer dia = diaInicio;
                        for(int x=mesInicio; x<12; x++){
                            DatosPeriodoVO intervalo = new DatosPeriodoVO();
                            Calendar inicioIntervalo = Calendar.getInstance();
                            Calendar finIntervalo= Calendar.getInstance();
                            
                            inicioIntervalo.set(anho, x, dia);
                            if(cal.isLeapYear(anho)){
                                finIntervalo.set(anho, x, MONTHS_LEAP.get(x));
                            }else{
                                finIntervalo.set(anho, x, MONTHS.get(x));
                            }//if(cal.isLeapYear(anho))
                            
                            intervalo.setFechaInicio(inicioIntervalo);
                            intervalo.setFechaFin(finIntervalo);
                            intervaloFechas.add(intervalo);
                            if(x== mesInicio){
                                dia = 1;
                            }//if(x== mesInicio)
                        }//for(int x=mesInicio; x<11; x++)
                    }else if(anho == anhoFin){
                        //Si es el ultimo año calculamos lo que hay desde el mes 0 hasta el mes de fin
                        mes = 0;
                        Integer dia = 1;
                        for(int x=0; x<mesFin+1; x++){
                            if(x == mesFin){
                                dia = diaFin;
                            }else{
                                if(cal.isLeapYear(anho)){
                                    dia = MONTHS_LEAP.get(x);
                                }else{
                                    dia = MONTHS.get(x);
                                }//if(cal.isLeapYear(anho))
                            }//if(x == mesFin)
                            DatosPeriodoVO intervalo = new DatosPeriodoVO();
                            Calendar inicioIntervalo = Calendar.getInstance();
                            Calendar finIntervalo= Calendar.getInstance();
                            
                            inicioIntervalo.set(anho, x, 1);
                            finIntervalo.set(anho, x, dia);
                            
                            if(x == mesFin){
                                //Si es el ultimo intervalo le restamos un dia.
                                finIntervalo.add(Calendar.DAY_OF_MONTH, 0); // mod dgc 11/04/2014
                            }//if(x == mesFin)
                            // si omitimos  el igual cuando tenga un periodo de un dia y este sea el ultimo no lo suma dgc 11/04/2014
                            //Si fin intervalo es menor o igual que inicioIntervalo omitimos todo el intervalo
                            if(finIntervalo.after(inicioIntervalo) || finIntervalo.equals(inicioIntervalo)){
                                intervalo.setFechaInicio(inicioIntervalo);
                                intervalo.setFechaFin(finIntervalo);
                                intervaloFechas.add(intervalo);
                            }//if(finIntervalo.after(inicioIntervalo))
                        }//for(int x=0; x<diferencia; x++)
                    }else{
                        //Si no es el primer ni el ultimo año sumamos un año completo
                    }//if
                }//for(Integer anho : arrayAnhos)
            }else{
                Integer mes = mesInicio;
                Integer dia = diaInicio;
                for(int x=mesInicio; x<mesFin+1; x++){
                    if(x==mesInicio){
                        DatosPeriodoVO intervalo = new DatosPeriodoVO();
                        Calendar inicioIntervalo = Calendar.getInstance();
                        Calendar finIntervalo= Calendar.getInstance();
                        inicioIntervalo.set(anhoInicio, x, dia);
                        if(cal.isLeapYear(anhoInicio)){
                            finIntervalo.set(anhoInicio, x, MONTHS_LEAP.get(x));
                        }else{
                            finIntervalo.set(anhoInicio, x, MONTHS.get(x));
                        }//if(cal.isLeapYear(anhoInicio))
                        intervalo.setFechaInicio(inicioIntervalo);
                        intervalo.setFechaFin(finIntervalo);
                        intervaloFechas.add(intervalo);
                        dia = 1;
                    }else{
                        if(x == mesFin){
                                dia = diaFin;
                        }else{
                            if(cal.isLeapYear(anhoInicio)){
                                dia = MONTHS_LEAP.get(x);
                            }else{
                                dia = MONTHS.get(x);
                            }//if(cal.isLeapYear(anhoInicio))
                        }//if(x == mesFin)
                        DatosPeriodoVO intervalo = new DatosPeriodoVO();
                        Calendar inicioIntervalo = Calendar.getInstance();
                        Calendar finIntervalo= Calendar.getInstance();
                        inicioIntervalo.set(anhoInicio, x, 1);
                        finIntervalo.set(anhoInicio, x, dia);
                        
                        if(x == mesFin){
                            //Si es el ultimo intervalo le restamos un dia.
                            finIntervalo.add(Calendar.DAY_OF_MONTH, 0); // mod dgc 11/04/2014
                        }//if(x == mesFin)
                        // si omitimos  el igual cuando tenga un periodo de un dia y este sea el ultimo no lo suma dgc 11/04/2014
                        //Si fin intervalo es menor o igual que inicioIntervalo omitimos todo el intervalo
                        if(finIntervalo.after(inicioIntervalo) || finIntervalo.equals(inicioIntervalo)){
                            intervalo.setFechaInicio(inicioIntervalo);
                            intervalo.setFechaFin(finIntervalo);
                            intervaloFechas.add(intervalo);
                        }//if(finIntervalo.after(inicioIntervalo))
                    }//if(x==mesInicio)
                }//for(int x=mesInicio; x<12; x++)
            }//if(arrayAnhos.size() > 0)
        }else{
            log.debug("La fecha de incio es posterior a la fecha de fin");
            MeLanbide01Exception excepcion = new MeLanbide01Exception("La fecha de incio es posterior a la fecha de fin");
            excepcion.setCodError(MeLanbide01Exception.ERROR_FECHA_INICIO_SUPERIOR_FIN);
            throw excepcion;
        }//if(fechaInicio.before(fechaFin))
        if(log.isDebugEnabled()) log.debug("calcularIntervaloFechas() : END");
        
        
        /***** oscar ****/
        // SE RELLENA LOS PERIODOS GENERADOS CON LOS DATOS DE BASE COTIZACIÓN, BONIFICACIÓN Y GASTO QUE SE CORRESPONDAN
        for(int i=0;i<intervaloFechas.size();i++){                        
            intervaloFechas.get(i).setBaseCotizacion(periodosOriginales.get(i).getBaseCotizacion());
            intervaloFechas.get(i).setBonificacion(periodosOriginales.get(i).getBonificacion());           
            
        }// for
        
        /***** oscar ****/
        
        
        return intervaloFechas;
    }//calcularIntervaloFechas
    

    /**
     * Calcula el numero de dias de un intervalo
     * @param intervaloFechas 
     */
    private void calcularNumeroDias (ArrayList<DatosPeriodoVO> intervaloFechas){
        if(log.isDebugEnabled()) log.debug("calcularNumeroDias() : BEGIN");
        for(DatosPeriodoVO intervalo : intervaloFechas){
            Integer numDias = 0;
            //DGC 12/02/2018
            // Calculamos en funcion del numero de dia en el año, evitar error intervalos de tiempos por ejemplo 29/03/xxx - 31/03/xxxx devuelve 4, y debe ser 3
            /*
            numDias = (intervalo.getFechaFin().getTimeInMillis() - intervalo.getFechaInicio().getTimeInMillis()) / MILLSECS_PER_DAY;
            // para corregir el calculo numeor dias en mes de marzo de todos los años -- si period 01-31 o 01/30 lo calcula mal
            if(intervalo.getFechaFin().get(5)>29 && intervalo.getFechaFin().get(2)==2)
                numDias++;
            */
            GregorianCalendar dateIni = new GregorianCalendar();
            dateIni.setTime(intervalo.getFechaInicio().getTime());
            GregorianCalendar dateFin = new GregorianCalendar();
            dateFin.setTime(intervalo.getFechaFin().getTime());

            numDias = (dateFin.get(GregorianCalendar.DAY_OF_YEAR) - dateIni.get(GregorianCalendar.DAY_OF_YEAR));
            intervalo.setNumDias(numDias.intValue()+1);
        }//for(ConciliacionVO intervalo : intervaloFechas)
        if(log.isDebugEnabled()) log.debug("calcularNumeroDias() : END");
    }//calcularNumeroDias
    
    /**
     * Calcula el total de la suma de los dias de los intervalos
     * @param intervaloFechas
     * @return 
     */
    private String calcularNumeroTotalDias (ArrayList<DatosPeriodoVO> intervaloFechas){
        if(log.isDebugEnabled()) log.debug("calcularNumeroTotalDias() : BEGIN");
        Long numDias = new Long(0);
        for(DatosPeriodoVO intervalo : intervaloFechas){
            //numDias += (intervalo.getFechaFin().getTimeInMillis() - intervalo.getFechaInicio().getTimeInMillis()) / MILLSECS_PER_DAY;
            //numDias +=1;
            numDias += intervalo.getNumDias();
            
        }//for(DatosPeriodoVO intervalo : intervaloFechas)
        if(log.isDebugEnabled()) log.debug("calcularNumeroTotalDias() : END");
        return numDias.toString();
    }//calcularNumeroTotalDias
    
    /**
     * Rellena los campos suplementarios de los periodos
     * @param intervaloFechas
     * @param baseCotizacion
     * @param reducPersSust
     * @param jornPersSust
     * @param jornPersCont
     * @param bonificacion 
     */
    private void rellenarCamposSuplementarios(ArrayList<DatosPeriodoVO> intervaloFechas, String reducPersSust,
           String jornPersSust, String jornPersCont){
        if(log.isDebugEnabled()) log.debug("rellenarCamposSuplementarios() : BEGIN");
        for(DatosPeriodoVO intervalo : intervaloFechas){
            if(reducPersSust != null && !reducPersSust.equalsIgnoreCase("")){
                intervalo.setReducPerSust(reducPersSust);
            }//if(reducPersSust != null && !reducPersSust.equalsIgnoreCase(""))
            if(jornPersSust != null && !jornPersSust.equalsIgnoreCase("")){
                intervalo.setJornPersSust(jornPersSust);
            }//if(jornPersSust != null && !jornPersSust.equalsIgnoreCase(""))
            if(jornPersCont != null && !jornPersCont.equalsIgnoreCase("")){
                intervalo.setJornPersCont(jornPersCont);
            }//if(jornPersCont != null && !jornPersCont.equalsIgnoreCase(""))
        }//for(DatosPeriodoVO intervalo : intervaloFechas)
        if(log.isDebugEnabled()) log.debug("rellenarCamposSuplementarios() : END");
    }//rellenarCamposSuplementarios
    
    
    private HashMap<String, String> getMaxAnhosDependenciaActividadSubvencionada(Integer codOrganizacion, String codProcedimiento){
        if(log.isDebugEnabled()) log.debug("getMaxAnhosDependenciaActividadSubvencionada() : BEGIN");
        HashMap<String, String> maxAnhosDependenciaActividadSubvencionada = new HashMap<String, String>();
        String tiposActividad = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + 
                this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPOS_ACTIVIDAD_SUBVENCIONADA, this.getNombreModulo());
        String maxAnhos = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + 
                this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.MAX_ANHOS_DEPENDENCIA_ACTIVIDAD_SUBVENCIONADA, this.getNombreModulo());
        String[] splitTiposActividad = tiposActividad.split(";");
        String[] splitMaxAnhos = maxAnhos.split(";");  
        for(int i=0; i<splitTiposActividad.length; i++){
            maxAnhosDependenciaActividadSubvencionada.put(splitTiposActividad[i], splitMaxAnhos[i]);
        }//for(int i=0; i<splitTiposActividad.length; i++)
        if(log.isDebugEnabled()) log.debug("getMaxAnhosDependenciaActividadSubvencionada() : END");
        return maxAnhosDependenciaActividadSubvencionada;
    }//getMaxAñosDependenciaActividadSubvencionada
    
    /**
     * Recupera el campo que indica la relacion con la persona dependiente
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param codProcedimiento
     * @return
     * @throws MeLanbide01Exception 
     */
    private String getCampoSuplementarioRelancionPersonaDependiente(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("getCampoSuplementarioRelancionPersonaDependiente() : BEGIN");
        String valor = new String();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        // 0/MODULO_INTEGRACION/MELANBIDE01/FAMIL/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/RELACPERSDEPEN
        //String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + RELACPERSDEPEN, this.getNombreModulo());                
        String campo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/RELACPERSDEPEN", this.getNombreModulo());
        //0/MODULO_INTEGRACION/MELANBIDE01/FAMIL/PANTALLA_EXPEDIENTE/TIPO/RELACPERSDEPEN
        //String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_RELACPERSDEPEN, this.getNombreModulo());
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/RELACPERSDEPEN", this.getNombreModulo());
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, 
                            codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if(salida.getStatus() == 0){
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorDesplegable();
        }//if(salida.getStatus() == 0)
        if(log.isDebugEnabled()) log.debug("getCampoSuplementarioRelancionPersonaDependiente() : END");
        return valor;
    }//getCampoSuplementarioRelancionPersonaDependiente
    
    /**
     * Recupera la fecha de nacimiento de la persona dependiente
     * 
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param codProcedimiento
     * @return
     * @throws MeLanbide01Exception 
     */
    private Calendar getCampoSuplementarioFecNacPersonaDependiente(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("getCampoSuplementarioFecNacPersonaDependiente() : BEGIN");
        Calendar valor = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.FECNAPDEPEN, this.getNombreModulo());
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_FECNAPDEPEN, this.getNombreModulo());
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, 
                            codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if(salida.getStatus() == 0){
            valor = Calendar.getInstance();
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorFecha();
        }//if(salida.getStatus() == 0)
        if(log.isDebugEnabled()) log.debug("getCampoSuplementarioFecNacPersonaDependiente() : END");
        return valor;
    }//getCampoSuplementarioFecNacPersonaDependiente
    
    
    private String getCampoSuplementarioGradoMinusvalia(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioGradoMinusvalia() : BEGIN");
        }
        String valor = new String();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + "/" + codProcedimiento + MeLanbide01Constantes.GRADMINUSDEPEN, this.getNombreModulo());
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/GRADMINUSDEPEN", this.getNombreModulo());
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorNumero();
        }//if(salida.getStatus() == 0)
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioGradoMinusvalia() : END");
        }
        return valor;
    }//getCampoSuplementarioGradoMinusvalia

    /**
     * Calcula el gasto
     * @param baseCotizacion
     * @param bonificacion
     * @param porcSubvenc
     * @return
     * @throws MeLanbide01Exception 
     */
    private String calcularGasto(String baseCotizacion, String bonificacion, String porcSubvenc) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("calcularGasto() : BEGIN");
        Double resultado = null;
        try{
            baseCotizacion = baseCotizacion.replace(",",".");
            bonificacion   = bonificacion.replace(",",".");
            porcSubvenc    = porcSubvenc.replace(",",".");
            
            Double doubleBaseCotizacion = new Double(baseCotizacion);
            Double doubleBonificacion = new Double(bonificacion);
            Double doublePorcSubvenc = new Double(porcSubvenc);
            
            doublePorcSubvenc = doublePorcSubvenc/100;
            //formula = (BASE COTIZACIÓN - BONIFICACIÓN) X 23,60 X % SUBVEN
            resultado = (Utilities.redondearDosDecimales(doubleBaseCotizacion) - Utilities.redondearDosDecimales(doubleBonificacion)) * 0.2360 * doublePorcSubvenc;            
            resultado = Utilities.redondearDosDecimales(resultado);
            
        }catch(Exception ex){
            log.error("Se ha producido un error calculando el gasto", ex);
            throw new MeLanbide01Exception("Se ha producido un error calculando el gasto", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("calcularGasto() : END");
        return resultado.toString();
    }//calcularGasto

        /**
     * Rellena el porcentaje de subvencion y un array de periodos
     * @param intervaloFechas
     * @param porcSubvenc
     * @param gasto 
     */
    private void rellenarPorcSubvenc (ArrayList<DatosPeriodoVO> intervaloFechas, String porcSubvenc){
        if(log.isDebugEnabled()) log.debug("rellenarPorcSubvencYGasto() : BEGIN");
        for(DatosPeriodoVO intervalo : intervaloFechas){
            intervalo.setPorcSubven(porcSubvenc);
        }//for(ConciliacionVO intervalo : intervaloFechas)
        if(log.isDebugEnabled()) log.debug("rellenarPorcSubvencYGasto() : END");
    }//calcularNumeroDias
    
    /**
     * Rellena el porcentaje de subvencion y el gasto para un array de periodos
     * @param intervaloFechas
     * @param porcSubvenc
     * @param gasto 
     */
    private void rellenarPorcSubvencYGasto (ArrayList<DatosPeriodoVO> intervaloFechas, String porcSubvenc, String gasto){
        if(log.isDebugEnabled()) log.debug("rellenarPorcSubvencYGasto() : BEGIN");
        for(DatosPeriodoVO intervalo : intervaloFechas){
            intervalo.setPorcSubven(porcSubvenc);
            intervalo.setGasto(gasto);
        }//for(ConciliacionVO intervalo : intervaloFechas)
        if(log.isDebugEnabled()) log.debug("rellenarPorcSubvencYGasto() : END");
    }//calcularNumeroDias
    
    /**
     * Calcula el gasto total de todos los intervalos
     * @param periodos
     * @return 
     */
    private String calcularGastoTotal(ArrayList<DatosPeriodoVO> periodos){
        if(log.isDebugEnabled()) log.debug("calcularGastoTotal() : BEGIN");
        Double gasto = new Double(0);
        for(DatosPeriodoVO periodo : periodos){
            if(periodo.getGasto() != null && !periodo.getGasto().equalsIgnoreCase("")){
                String gastoPeriodo = periodo.getGasto().replaceAll(",",".");
                gasto += Double.valueOf(gastoPeriodo);
            }//if(periodo.getGasto() != null && !periodo.getGasto().equalsIgnoreCase(""))
        }//for(DatosPeriodoVO periodo : periodos)
        
        gasto = Utilities.redondearDosDecimales(gasto);
        if(log.isDebugEnabled()) log.debug("calcularGastoTotal() : END");
        return gasto.toString();
    }//calcularGastoTotal
    
    /**
     * 
     * 
     * @param codigoOrganizacion
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @return Integer
     * @throws MeLanbide01Exception 
     *
    private Integer diasRestantesSubvencionInteresado(String codigoOrganizacion, String numExpediente, String codProcedimiento, 
            String ejercicio) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("diasRestantesSubvencionInteresado() : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        SalidaIntegracionVO salidaExpedientesInteresadoTercero = new SalidaIntegracionVO();
        ExpedienteModuloIntegracionVO expediente = new ExpedienteModuloIntegracionVO();
        ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = new ArrayList<InteresadoExpedienteModuloIntegracionVO>();
        ArrayList<ExpedienteModuloIntegracionVO> listaExpedientesInteresadoTercero = new ArrayList<ExpedienteModuloIntegracionVO>();
        MeLanbide01Manager meLanbide01Manager = MeLanbide01Manager.getInstance();
        Integer numeroTotalDias = new Integer(0);
        
        //Recuperamos la actividad de los campos suplementarios del expediente
        String actividadSubvencionada = getCampoSuplementarioActividadSubvencionada
            (Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);
        
        //Recuperamos la fecha de nacimiendo de la persona dependiente
        Calendar fechaNacimientoPersonaDependiente = 
                getCampoSuplementarioFecNacPersonaDependiente(new Integer(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

        log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() actividadSubvencionada : " + actividadSubvencionada);
        log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() fechaNacimientoPersonaDependiente : " + fechaNacimientoPersonaDependiente);
        
        if(!"".equalsIgnoreCase(actividadSubvencionada)){
            String rolPersonaContratada = getRolPersonaContratada(Integer.valueOf(codigoOrganizacion), codProcedimiento);
            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() rolPersonaContratada: " + rolPersonaContratada);
            salida = el.getExpediente(codigoOrganizacion, numExpediente, codProcedimiento, ejercicio);
            expediente = salida.getExpediente();
            interesados = expediente.getInteresados();
            
            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() número de interesados del expediente: " + interesados.size());
            if(interesados.size() > 0){
            for(InteresadoExpedienteModuloIntegracionVO interesado : interesados){
                //Buscamos al interesado con el rol de persona contratada
                log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() rol interesado expediente: " + interesado.getCodigoRol() + ", codTercero: " + interesado.getCodigoTercero());
                log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() rolPersonaContratada: " + rolPersonaContratada);
                if(interesado.getCodigoRol() == Integer.valueOf(rolPersonaContratada)){
                    String documentoInteresado = interesado.getDocumento();
                    String tipoDocumentoInteresado = String.valueOf(interesado.getTipoDocumento());
                                     
                    log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() se buscan expedientes en los que el interesado está, documentoInteresado: " + documentoInteresado + ",tipoDocumentoInteresado: " + tipoDocumentoInteresado);
                    ArrayList<ExpedienteMeLanbide01VO> expTerceros = MeLanbide01DatosCalculoDao.getInstance().getExpedientesTercero(codigoOrganizacion, codProcedimiento, tipoDocumentoInteresado, documentoInteresado, ConnectionUtils.getAdaptSQLBD(codigoOrganizacion));
                            
                    log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado(): " + expTerceros.size());
                    
                    //if(listaExpedientesInteresadoTercero!=null && listaExpedientesInteresadoTercero.size()>0){
                    if(expTerceros!=null && expTerceros.size()>0){
                        //for(ExpedienteModuloIntegracionVO expedienteInteresado : listaExpedientesInteresadoTercero){
                        for(ExpedienteMeLanbide01VO expedienteInteresado : expTerceros){
                            //Comprobamos que para el expediente del que somos interesados, la actividad subvencionada es la misma que par el 
                            //expediente en el que nos encontramos.
                            String actividadSubvencionadaInteresado = getCampoSuplementarioActividadSubvencionada
                                (Integer.valueOf(codigoOrganizacion), String.valueOf(expedienteInteresado.getEjercicio()), 
                                expedienteInteresado.getNumExpediente(), expedienteInteresado.getCodProcedimiento());
                            
                            //Recuperamos el valor del estudio tecnico del expediente
                            String resultadoEstudioTecnico = getCampoSuplementarioResultadoEstudioTecnico
                                (Integer.valueOf(codigoOrganizacion), String.valueOf(expedienteInteresado.getEjercicio()), 
                                expedienteInteresado.getNumExpediente(), expedienteInteresado.getCodProcedimiento());
                            
                            //Recuperamos el valor del fichero de propiedades que nos indica cuando un estudio tecnico esta rechazado
                            String valorNegativoEstudioTecnico = getResultadoEstudioTecnicoNegativo
                                (Integer.valueOf(codigoOrganizacion), codProcedimiento);
                            
                            //Recuperamos la fecha de nacimiento de la persona dependiente
                            Calendar fechaNacimientoPersonaDependienteExpRel = getCampoSuplementarioFecNacPersonaDependiente
                                    (Integer.valueOf(codigoOrganizacion), String.valueOf(expedienteInteresado.getEjercicio()), 
                                expedienteInteresado.getNumExpediente(), expedienteInteresado.getCodProcedimiento());
                            
                            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() actividadSubvencionadaInteresado : " + actividadSubvencionadaInteresado);
                            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() resultadoEstudioTecnico : " + resultadoEstudioTecnico);
                            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() valorNegativoEstudioTecnico : " + valorNegativoEstudioTecnico);
                            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() fechaNacimientoPersonaDependienteExpRel : " + fechaNacimientoPersonaDependienteExpRel);
                            
                            
                            if(!"".equalsIgnoreCase(actividadSubvencionadaInteresado)){
                                if(actividadSubvencionada.equalsIgnoreCase(actividadSubvencionadaInteresado)){
                                    //Si el estudio tecnico esta denegado no sumamos los dias de ese expediente
                                    if(!valorNegativoEstudioTecnico.equalsIgnoreCase(resultadoEstudioTecnico)){
                                        //Si el dia de nacimiento de la persona dependiente coincide con el del expediente relacionado
                                        //ya que la subvencion es para cada persona dependiente.
                                        if((fechaNacimientoPersonaDependienteExpRel != null && fechaNacimientoPersonaDependiente != null)
                                                && (fechaNacimientoPersonaDependienteExpRel.equals(fechaNacimientoPersonaDependiente))){
                                            //Buscamos para cada expediente el numero de dias
                                            
                                            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() a calcular el número de dias");
                                            numeroTotalDias += meLanbide01Manager.numDiasTotalExpediente(expedienteInteresado.getNumExpediente(), 
                                                codigoOrganizacion, ConnectionUtils.getAdaptSQLBD(codigoOrganizacion));
                                        }
                                    }
                                }
                            }else{
                                if(log.isDebugEnabled()) log.debug("El expediente relacionado con el interesado " 
                                        + expedienteInteresado.getNumExpediente() + " no tiene el campo actividad subvencionada");
                            }//if(!"".equalsIgnoreCase(actividadSubvencionadaInteresado))
                        }//for
                    }
                }
            }
            }
        }else{
            throw new 
                MeLanbide01Exception("No se ha podido recuperar el campo de actividad subvencionada para el expediente = " + numExpediente);
        }//if(!"".equalsIgnoreCase(actividadSubvencionada))
        if(log.isDebugEnabled()) log.debug("diasRestantesSubvencionInteresado() : END");
        
        log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() numeroTotalDias : " + numeroTotalDias);
        return numeroTotalDias;
    }//diasRestantesSubvencionInteresado
    
    */
       
    /**
     * Comprueba que la edad de un posible hijo dependiente no supere las marcadas como propiedades en el fichero de configuracion.
     * 
     * @param codigoOrganizacion: Código de organización/municipio
     * @param numExpediente_ Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param ejercicio: Ejercicio
     * @return Valores posibles:
     *      "0" --> Si no hay alarma
     *      "1" --> El valor del campo "fecha de actuación concedida" no puede ser inferior al valor de "Fecha de nacimiento de persona dependiente"
     *      "2" --> Se ha superado el límite máximo de días para la persona dependiente 
     
     * @throws MeLanbide01Exception 
     */
    private String alarmaMaxAnosDependencia (String codigoOrganizacion, String numExpediente, String codProcedimiento, 
            String ejercicio) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("alarmaMaxAnosDependencia() : BEGIN");
        String alarma = "0";
        
        try{
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

            //Recuperamos la actividad de los campos suplementarios del expediente
            String actividadSubvencionada = meLanbide01ValidatorUtils.getCampoSuplementarioActividadSubvencionada
                (Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            //Recuperamos la relacion con la persona dependiente
            String relacionPersonaDependiente = getCampoSuplementarioRelancionPersonaDependiente
                (Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            //Recuperamos el valor que indica si tiene un hijo dependiente
            String codRelacionPersonaDependiente = meLanbide01ValidatorUtils.getValorPersonaDependiente(Integer.valueOf(codigoOrganizacion), codProcedimiento);

            String CODIGO_CAMPO_FECHAFIN_ACTUACION = ConfigurationParameter.getParameter(codigoOrganizacion + "/MODULO_INTEGRACION/CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO",this.getNombreModulo());
            String TIPO_CODIGO_CAMPO_FECHAFIN_ACTUACION = ConfigurationParameter.getParameter(codigoOrganizacion + "/MODULO_INTEGRACION/TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO",this.getNombreModulo());

            
                  
            SalidaIntegracionVO salidaFechaFinActuacion = el.getCampoSuplementarioExpediente(codigoOrganizacion, ejercicio, numExpediente, 
                                codProcedimiento, CODIGO_CAMPO_FECHAFIN_ACTUACION, Integer.parseInt(TIPO_CODIGO_CAMPO_FECHAFIN_ACTUACION));


            if(salidaFechaFinActuacion!=null && salidaFechaFinActuacion.getStatus()==0){
                
                Calendar FECHA_FIN_ACTUACION = salidaFechaFinActuacion.getCampoSuplementario().getValorFecha();
           
                if(FECHA_FIN_ACTUACION!=null){                   
                log.debug("alarmaMaxAnosDependencia - FECHA_FIN_ACTUACION no nula ");
                    //Si el valor del campo "Al cuidado de" indica que es un hijo (valor de código 0 en el desplegable)
                   if(codRelacionPersonaDependiente.equalsIgnoreCase(relacionPersonaDependiente)){
                        log.debug("alarmaMaxAnosDependencia - codRelacionPersonaDependiente ");
                       //Recuperamos la fecha de nacimiento de la persona dependiente
                       Calendar FECHA_NACIMIENTO_PERSO_DEPENDIENTE = getCampoSuplementarioFecNacPersonaDependiente
                       (Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);


                       if(FECHA_NACIMIENTO_PERSO_DEPENDIENTE.after(FECHA_FIN_ACTUACION)){
                           // La fecha de nacimiento de la persona dependiente, no puede ser superior al a fecha de fin de actuación.
                           // En este caso se muestra un error en la pestaña "Datos de cálculo"                           
                           alarma = "1";
                           return alarma;
                       }else{
                           log.debug("alarmaMaxAnosDependencia - FECHA_NACIMIENTO_PERSO_DEPENDIENTE antes que fin de actuacion ");
                            //Recuperamos el grado de minusvalia de la persona dependiente
                            String gradoMinusvaliaPersonaDependiente = getCampoSuplementarioGradoMinusvalia(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

                           Integer valor = calcularDiferenciaAnyosEntreFecha(FECHA_NACIMIENTO_PERSO_DEPENDIENTE.getTime(),FECHA_FIN_ACTUACION.getTime());
                           
                           //Integer edad = calcularEdad(fecNacimientoPersonaDependiente);
                           HashMap<String,String> maximosAñosPersonaDependiente = 
                                     getMaxAnhosDependenciaActividadSubvencionada(Integer.valueOf(codigoOrganizacion), codProcedimiento);

                           Iterator it = maximosAñosPersonaDependiente.entrySet().iterator();
                           
                            while (it.hasNext()) {
                                Map.Entry e = (Map.Entry) it.next();
                                if (e.getKey().equals(actividadSubvencionada)) {
                                    if (!actividadSubvencionada.equals("REDUCC")) {
                                        Integer numAnhos = Integer.valueOf(String.valueOf(e.getValue()));
                                        if (valor >= numAnhos) {
                                            //alarma = (String) e.getKey();                                            
                                            alarma = "2";
                                        }
                                    } else {//es reduccion (menor 12 si minusvalia<=33%, menor 18 si minusvalia>33% )
                                        log.debug("alarmaMaxAnosDependencia - actividad subvencionada es REDUCCION ");
                                        String[] splitMaxAnhos = String.valueOf(e.getValue()).split("/");
                                        String gradosMinusvalia = ConfigurationParameter.getParameter(codigoOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                                                + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.GRADO_MINUSVALIA_ACTIVIDAD_SUBVENCIONADA, this.getNombreModulo());
                                        log.debug("alarmaMaxAnosDependencia - GRADO MINUSVALIA: "+gradosMinusvalia);
                                        log.debug("alarmaMaxAnosDependencia - DIFERENCIA AÑOS: "+valor);
                                        String[] splitGradosMinus = gradosMinusvalia.split(";");
                                        for (int i = 0; i < splitMaxAnhos.length; ++i) {
                                            String[] minmaxGrado = splitGradosMinus[i].split("-");
                                           log.debug("alarmaMaxAnosDependencia - COMPROBAR minmaxGrado: "+minmaxGrado[0]+" - "+minmaxGrado[1]);
                                            if (Integer.valueOf(gradoMinusvaliaPersonaDependiente) <= Integer.valueOf(minmaxGrado[1]) && Integer.valueOf(gradoMinusvaliaPersonaDependiente) > Integer.valueOf(minmaxGrado[0])) {
                                                 log.debug("alarmaMaxAnosDependencia - VALOR >="+splitMaxAnhos[i]+"?: "+(valor >= Integer.valueOf(splitMaxAnhos[i])));
                                                if (valor >= Integer.valueOf(splitMaxAnhos[i])) {
                                                    alarma ="2";
                                                    log.debug("alarmaMaxAnosDependencia - ALARMA ");
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }
                            }// while
                       }
                   }
                
                
                }// if
            }

           
        }catch(Exception e){
            e.printStackTrace();
            log.error("Error al verificar la alarma del máximo de años de la persona dependiente con respecto a la actividad subvencionable: " + e.getMessage());
        }
        
        if(log.isDebugEnabled()) log.debug("alarmaMaxAnosDependencia() : END");
        return alarma;
    }//alarmaMaxAnosDependencia
    
 
    public int calcularDiferenciaAnyosEntreFecha(Date fechaInicio, Date fechaFin){
        Calendar fechaAct = Calendar.getInstance();
        fechaAct.setTime(fechaFin);

        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(fechaInicio);

        int dif_anios = fechaAct.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int dif_meses = fechaAct.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int dif_dias = fechaAct.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

        //Si está en ese año pero todavía no los ha cumplido
        if(dif_meses<0 || (dif_meses==0 && dif_dias<0)){
            dif_anios--;
        }
        return dif_anios;
    }
    
    public int calcularEdad(Calendar fechaNac){
        Calendar today = Calendar.getInstance();

        int diff_year = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int diff_month = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int diff_day = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);
        
        //int diff_day = today.get(Calendar.DAY_OF_MONTH) ? fechaNac.get(Calendar.DAY_OF_MONTH);
        //Si está en ese año pero todavía no los ha cumplido
        if(diff_month<0 || (diff_month==0 && diff_day<0)){
            diff_year = diff_year-1;
        }
        return diff_year;
    }
    
    private String getFormatoNumeroDecimal(String numero){
        String salida = "";
        
        try{       
            
            Double num = new Double(numero);
            
            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();            
            DecimalFormat format = new DecimalFormat("###,###.00",simbolo);            
            salida = format.format(num);            
            
            if(salida.equals(",00")) salida = "0,00";
        }catch(Exception e){
            e.printStackTrace();
        }        
        return salida;        
    }

    public void prepararSubvencionConciliacionPestanaHistorialSubv(int codOrganizacion,String numExpediente,HttpServletRequest request, AdaptadorSQLBD adapt) throws MeLanbide01Exception{
        log.info("prepararSubvencionConciliacionPestanaHistorialSubv - " + formatFechaLog.format(new Date()));
         String redireccion = null;
         try {
             String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numExpediente);
             redireccion = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.PANTALLA_EXPEDIENTE_CAUSANTES_SUBV + "SALIDA", this.getNombreModulo());
             log.info("JSP de salida: " + redireccion);
            
        } catch (Exception e) {
            log.error("Error al preparar la pantalla de la pestana. " + e.getMessage(), e);
        }
         String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numExpediente);
         // Leer y meter en la request los objetos lista del expedinete a tratar e la jsp
         List<Melanbide01HistoSubv> listaHistorialSubvencionExpte = new ArrayList<Melanbide01HistoSubv>();
         listaHistorialSubvencionExpte=MeLanbide01Manager.getInstance().getTodoHitorialSubvencionExpediente(numExpediente, adapt);
         request.setAttribute("listaHistorialSubvencionExpte", listaHistorialSubvencionExpte);
         //JSP a la que se produce la redireccion
         redireccion    = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.PANTALLA_EXPEDIENTE_HISTORIAL_SUBV + "SALIDA",this.getNombreModulo());
         log.info("JSP de salida: " + redireccion);
         request.setAttribute("urlHistorialSubvencion", redireccion);
         log.info("prepararSubvencionConciliacionPestanaHistorialSubv - " + formatFechaLog.format(new Date()));
    }
    
    public void prepararSubvencionConciliacionPestanaCausantesSubv(int codOrganizacion,String numExpediente,HttpServletRequest request, AdaptadorSQLBD adapt){
        log.info("prepararSubvencionConciliacionPestanaCausantesSubv - " + formatFechaLog.format(new Date())); 
        String redireccion = null;
        try {
            String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numExpediente);
            // Leer y meter en la request los objetos lista del expedinete a tratar e la jsp
            List<Melanbide01DepenPerSut> listaCausanteSubvencionExpte = new ArrayList<Melanbide01DepenPerSut>();
            listaCausanteSubvencionExpte = MeLanbide01Manager.getInstance().getTodosCausantesSubvencion(numExpediente, adapt);
            
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            String codCampoDesplegable=ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento +MeLanbide01Constantes.BARRA+"CODIGO_DESPLEGABLE_TIPO_DEPENDIENTE", this.getNombreModulo());
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaTipoDependiente = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), codCampoDesplegable);
            listaTipoDependiente = salidaIntegracion.getCampoDesplegable().getValores();
            
            codCampoDesplegable=ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento +MeLanbide01Constantes.BARRA+"CODIGO_DESPLEGABLE_BOOLEAN", this.getNombreModulo());
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaSINO = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), codCampoDesplegable);
            listaSINO=salidaIntegracion.getCampoDesplegable().getValores();
            
            ArrayList<TipoDocumentoVO> listaTipoDocumento = MeLanbide01Manager.getInstance().getTiposDocumento(adapt);
            request.setAttribute("listaCausanteSubvencionExpte", listaCausanteSubvencionExpte);
            request.setAttribute("listaTipoDependiente", listaTipoDependiente);
            request.setAttribute("listaTipoDocumento", listaTipoDocumento);
            request.setAttribute("listaSINO", listaSINO);
            
            redireccion = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + this.getNombreModulo() + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.PANTALLA_EXPEDIENTE_CAUSANTES_SUBV + "SALIDA", this.getNombreModulo());
            log.info("JSP de salida: " + redireccion);
            request.setAttribute("urlCausantesSubvencion", redireccion);
        } catch (Exception e) {
            log.error("Error al preparar la pantalla de la pestana. " + e.getMessage(), e);
        }
        log.info("prepararSubvencionConciliacionPestanaCausantesSubv -" + formatFechaLog.format(new Date())); 
    }
    
    public void guardarDatosCausantesSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("guardarDatosCausantesSubvencion - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatosCausantes = "";
        List<Melanbide01DepenPerSut> respuestaServicio= new ArrayList<Melanbide01DepenPerSut>();
        try {
            //Recojo los parametros
            jsonDatosCausantes = (String) request.getParameter("jsonCausantesSubvencion");
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            log.info("tipoOperacion : " + tipoOperacion);
            log.info("jsonDatosCausantes : " + jsonDatosCausantes);
            if (jsonDatosCausantes != null && !jsonDatosCausantes.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                Melanbide01DepenPerSut datosDependientePS = (Melanbide01DepenPerSut) gson.fromJson(jsonDatosCausantes, Melanbide01DepenPerSut.class);
                if (datosDependientePS != null) {
                   if("0".equalsIgnoreCase(tipoOperacion))
                       MeLanbide01Manager.getInstance().agregarNuevoCausante(datosDependientePS, adapt);
                   else if("1".equalsIgnoreCase(tipoOperacion))
                       MeLanbide01Manager.getInstance().actualizarDatosCausante(datosDependientePS, adapt);
                   respuestaServicio=MeLanbide01Manager.getInstance().getTodosCausantesSubvencion(numExpediente, adapt);
                }
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al registrar Datos de Causante Sunvencion", e);
            respuestaServicio=null;
        }
        log.info("respuestaServicio : " + respuestaServicio);

        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(respuestaServicio);
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
            e.printStackTrace();
        }
        log.info("guardarDatosCausantesSubvencion - End()" + formatFechaLog.format(new Date()));
    }
    
    public void guardarDatosHistorialSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("guardarDatosHistorialSubvencion - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatos= "";
        String tipoOperacion="";
        String parametrosLLamadaM01="";
        String dataParameter="";
        List<Melanbide01HistoSubv> respuestaServicio= new ArrayList<Melanbide01HistoSubv>();
        try {
            //Recojo los parametros
            parametrosLLamadaM01 = (String) request.getParameter("parametrosLLamadaM01");
            dataParameter = (String) request.getParameter("dataParameter");
            jsonDatos = (String) request.getParameter("jsonHistorialSibvencion");
            tipoOperacion = (String) request.getParameter("tipoOperacion");
            log.info("tipoOperacion : " + tipoOperacion);
            log.info("jsonDatos: " + jsonDatos);
            log.info("dataParameter: " + dataParameter);
            log.info("parametrosLLamadaM01: " + parametrosLLamadaM01);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                Melanbide01HistoSubv datosGuardar = (Melanbide01HistoSubv) gson.fromJson(jsonDatos, Melanbide01HistoSubv.class);
                if (datosGuardar != null) {
                    if("0".equalsIgnoreCase(tipoOperacion))
                        MeLanbide01Manager.getInstance().agregarNuevaLineaHistorialSubvencion(datosGuardar, adapt);
                    else if("1".equalsIgnoreCase(tipoOperacion))
                        MeLanbide01Manager.getInstance().actualizarDatosLineaHistorialSubvencion(datosGuardar, adapt);
                    respuestaServicio=MeLanbide01Manager.getInstance().getTodoHitorialSubvencionExpediente(numExpediente, adapt);
                }
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al registrar Datos de historial Sunvencion", e);
            respuestaServicio=null;
        }
        log.info("respuestaServicio : " + respuestaServicio);

        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(respuestaServicio);
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
            e.printStackTrace();
        }
        log.info("guardarDatosHistorialSubvencion - End()" + formatFechaLog.format(new Date()));
    }
    
    public void eliminarDatosCausantesSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("eliminarDatosCausantesSubvencion - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatos= "";
        List<Melanbide01DepenPerSut> respuestaServicio= new ArrayList<Melanbide01DepenPerSut>();
        try {
            //Recojo los parametros
            jsonDatos = (String) request.getParameter("identificadorBDEliminar");
            log.info("jsonDatos: " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                MeLanbide01Manager.getInstance().eliminarCausante(Long.parseLong(jsonDatos), adapt);
                respuestaServicio=MeLanbide01Manager.getInstance().getTodosCausantesSubvencion(numExpediente, adapt);
            }
        } catch (Exception e) {
            log.error("Se ha presentado un error al eliminar Datos de causante Sunvencion", e);
            respuestaServicio=null;
        }
        log.info("respuestaServicio : " + respuestaServicio);

        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(respuestaServicio);
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
            e.printStackTrace();
        }
        log.info("eliminarDatosCausantesSubvencion - End()" + formatFechaLog.format(new Date()));
    }
    
    public void eliminarDatosHistorialSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("eliminarDatosHistorialSubvencion - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatos= "";
        List<Melanbide01HistoSubv> respuestaServicio= new ArrayList<Melanbide01HistoSubv>();
        try {
            //Recojo los parametros
            jsonDatos = (String) request.getParameter("identificadorBDEliminar");
            log.info("jsonDatos: " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                MeLanbide01Manager.getInstance().eliminarFilaHistorialSubvencion(Long.parseLong(jsonDatos), adapt);
                respuestaServicio=MeLanbide01Manager.getInstance().getTodoHitorialSubvencionExpediente(numExpediente, adapt);
            }
        } catch (Exception e) {
            log.error("Se ha presentado un error al eliminar Datos de hisotiral Sunvencion", e);
            respuestaServicio=null;
        }
        log.info("respuestaServicio : " + respuestaServicio);

        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(respuestaServicio);
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
            e.printStackTrace();
        }
        log.info("eliminarDatosHistorialSubvencion - End()" + formatFechaLog.format(new Date()));
    }
    
    public void validarControlAlarmasCONCMSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("validarControlAlarmasCONCMSubvencion - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = ConnectionUtils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<MeLanbide01ValidatorResult> validacionesAlarmasCONCM = new ArrayList<MeLanbide01ValidatorResult>();
        try {
            Melanbide01Decreto melanbide01Decreto;
            MeLanbide01Validator meLanbide01Validator = new MeLanbide01Validator();
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            if (request.getSession() != null && request.getSession().getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                //apl = usuarioVO.getAppCod();
                //css = usuarioVO.getCss();
                idioma = usuarioVO.getIdioma();
            }
            melanbide01Decreto = meLanbide01ValidatorUtils.getDecretoAplicableExptePorFecInicioPeriodosOrFecIniAcciSubvConcedida(codOrganizacion, numExpediente);
            if(melanbide01Decreto!=null 
                    && melanbide01Decreto.getDecretoCodigo()!=null && !melanbide01Decreto.getDecretoCodigo().isEmpty()){
                validacionesAlarmasCONCM = meLanbide01Validator.validacionesAlarmasCONCM(codOrganizacion, melanbide01Decreto.getDecretoCodigo(), numExpediente, idioma);
            }else{
                MeLanbide01ValidatorResult meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS));
                meLanbide01ValidatorResult.setDescripcion(MeLanbide01I18n.getInstance().getMensaje(idioma, MeLanbide01Constantes.COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                validacionesAlarmasCONCM.add(meLanbide01ValidatorResult);
            }
            request.setAttribute("melanbide01Decreto", melanbide01Decreto);
            request.setAttribute("validacionesAlarmasCONCM", validacionesAlarmasCONCM);

        } catch (Exception e) {
            log.error("Error leyendo los datos del Decreto que aplica al expediente", e);
        }
        log.info("respuesta Validaciones incorrectas : " + validacionesAlarmasCONCM!=null ? validacionesAlarmasCONCM.size() : 0);
        
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(validacionesAlarmasCONCM);
        try {
            PrintWriter out = response.getWriter();
            // Codificamos con UTF-8 Encodig de la request para los caracteres especiales o tildes
            if(respuestaJsonString!=null && !respuestaJsonString.isEmpty()){
                respuestaJsonString=Utilities.decodeText(respuestaJsonString,request.getCharacterEncoding());
            }
            response.setContentType("application/json");
            response.setCharacterEncoding(request.getCharacterEncoding());
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (IOException  e) {
            log.error("IOException preparando respuesta " + e.getMessage(), e);
        }catch (Exception e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
        }
        log.info("validarControlAlarmasCONCMSubvencion - End()" + formatFechaLog.format(new Date()));
    }
    
    public void getImporteTotalSubvCONCMUlt3AnioEmpresaExpte(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("validarControlAlarmasCONCMSubvencion - Begin() " + formatFechaLog.format(new Date()));
        String impTotalRecibidoEmpreReglaMinimistr="";
        try {
            double impTotalRecibidoEmpreReglaMinimis = 0;
            String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numExpediente);
            impTotalRecibidoEmpreReglaMinimis = MeLanbide01Manager.getInstance().getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios(codOrganizacion, codProcedimiento, numExpediente);
            impTotalRecibidoEmpreReglaMinimistr = Utilities.formatearNumeroDecimalSeparadores(impTotalRecibidoEmpreReglaMinimis);
            log.info("impTotalRecibidoEmpreReglaMinimis: " + impTotalRecibidoEmpreReglaMinimistr);
        } catch (Exception e) {
            log.error("Error recuperando los datos para regla de minimos aplicada a la empresa del expediente " + e.getMessage(), e);
        }     
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(impTotalRecibidoEmpreReglaMinimistr);
        try {
            PrintWriter out = response.getWriter();
            // Codificamos con UTF-8 Encodig de la request para los caracteres especiales o tildes
            if(respuestaJsonString!=null && !respuestaJsonString.isEmpty()){
                respuestaJsonString=Utilities.decodeText(respuestaJsonString,request.getCharacterEncoding());
            }
            response.setContentType("application/json");
            response.setCharacterEncoding(request.getCharacterEncoding());
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (IOException  e) {
            log.error("IOException preparando respuesta " + e.getMessage(), e);
        }catch (Exception e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
        }
        log.info("getImporteTotalSubvCONCMUlt3AnioEmpresaExpte - End() " + formatFechaLog.format(new Date()));
    }

    
}//class
