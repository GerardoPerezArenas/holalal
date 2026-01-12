<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.DatosAnexosVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.EcaConfiguracionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaPreparadorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaProspectorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44Utils" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.*" %>
<%@page import="java.math.*" %>
<%@page import="java.text.*" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
 int apl = 5;
 String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    
    try
    {
        if (session != null) 
        {
            if (usuario != null) 
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
	 apl = usuario.getAppCod();
	 css = usuario.getCss();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    Integer anoExpediente = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
    EcaSolicitudVO ecaSolicitud = (EcaSolicitudVO)request.getAttribute("ecaSolicitud");
    DatosAnexosVO datosAnexos = (DatosAnexosVO)request.getAttribute("datosAnexos");
    //DatosAnexosVO datosCarga = (DatosAnexosVO)request.getAttribute("datosConcedido");   
    EcaConfiguracionVO ecaConfig = (EcaConfiguracionVO)session.getAttribute("ecaConfiguracion");    
    
    Integer numPreparadoresSinErrores = null;
    Integer numProspectoresSinErrores = null;
    
    BigDecimal importePreparadores = null;
    FilaPreparadorSolicitudVO prep = null;        
    List<FilaPreparadorSolicitudVO> solPrepList = (List<FilaPreparadorSolicitudVO>)request.getAttribute("listaPreparadoresSolicitud");
    if (solPrepList!= null && solPrepList.size() >0){
        importePreparadores = BigDecimal.ZERO;
        numPreparadoresSinErrores = 0;
        for (int i = 0; i <solPrepList.size(); i++)
        {
            prep = solPrepList.get(i);            
            //if (prep.getErrores().size()==0) {
                if(prep.getSolPreparadorOrigen() == null){
                    numPreparadoresSinErrores++;
                }
                if((prep.getCostesSalarialesSS()!=null) && (!prep.getCostesSalarialesSS().equals("-")))
                   // numero= new BigDecimal(numformateado.replace(".", "").replace(",", "."));
                  importePreparadores = importePreparadores.add(new BigDecimal(prep.getCostesSalarialesSS().replace(".", "").replace(",",".")));
            //}
        }
    }
    
    BigDecimal importeProspectores = null;   
    FilaProspectorSolicitudVO pros = null;        
    List<FilaProspectorSolicitudVO> solProsList = (List<FilaProspectorSolicitudVO>)request.getAttribute("listaProspectoresSolicitud");
    if (solProsList!= null && solProsList.size() >0){
        numProspectoresSinErrores = 0;
        importeProspectores = BigDecimal.ZERO;
        for (int i = 0; i <solProsList.size(); i++)
        {
            pros = solProsList.get(i);            
            //if ((pros.getErrores().size()==0)|| (pros.getErrores().size()==1  &&  pros.getErrorCampo(11).equals("S")) ) {
                if(pros.getSolProspectorOrigen() == null){
                    numProspectoresSinErrores++;
                }
                if ( (pros.getCoste()!=null) && (!pros.getCoste().equals("-")) ){
                  importeProspectores = importeProspectores.add(new BigDecimal(pros.getCoste().replace(".", "").replace(",",".")));
                }
            //}
        }
    }
    
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
    
    FichaExpedienteForm expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
    GeneralValueObject expedienteVO = null;
    String modoConsulta = "no";
    if(expForm != null)
    {
        expedienteVO = expForm.getExpedienteVO();
        if(expedienteVO != null)
        {
            modoConsulta = (String) expedienteVO.getAtributo("modoConsulta");
            if(modoConsulta != null)
            {
                modoConsulta = modoConsulta.toLowerCase();
            }
            else
            {
                modoConsulta = "no";
            }
        }
    }
%>


<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

<script type="text/javascript">        
    var mensajeValidacion = '';
    
    function calcularGasto(){
        var gasto = <%=ecaConfig.getPoGastos()%>;
        var pros = 0;
        var prep = 0;
        var solic = 0;
        
        var datoActualGastos= document.getElementById("gastosGeneralesSolic").value;
        if (datoActualGastos==""){//solo calcula si es la primera vez(gastos no guardados), sino mostrar dato guardado.
            if(document.getElementById("prospectoresSolicSol").value != "")
                pros = parseFloat(convertirANumero(document.getElementById("prospectoresSolicSol").value));
            if(document.getElementById("preparadoresSolicSol").value != "")
                prep = parseFloat(convertirANumero(document.getElementById("preparadoresSolicSol").value));

            solic = (pros + prep)*gasto;
            //alert("gasto" + solic);
            document.getElementById("gastosGeneralesSolic").value = solic;
            reemplazarPuntosEca(document.getElementById("gastosGeneralesSolic"));
        }
    }
    
  function cambioRadioOtrosOrganismos(){
        if(document.getElementById('radioOtrasAyudasS').checked){
            document.getElementById('filaOtrosOrganismos1').style.display = 'table-row';
            document.getElementById('filaOtrosOrganismos2').style.display = 'table-row';
            document.getElementById('filaOtrosOrganismos3').style.display = 'table-row';
        }else if(document.getElementById('radioOtrasAyudasN').checked){
            document.getElementById('filaOtrosOrganismos1').style.display = 'none';
            document.getElementById('filaOtrosOrganismos2').style.display = 'none';
            document.getElementById('filaOtrosOrganismos3').style.display = 'none';
        }
    }
    
    function calcularTotalSolicitud(idInput1, idInput2, idInputTot){
        var input1 = document.getElementById(idInput1);
        var input2 = document.getElementById(idInput2);
        var inputTot = document.getElementById(idInputTot);
        var n1;
        var n2;
        var tot;
        try{
            var valorinput1 = convertirANumero(input1.value);
            if(!isNaN(valorinput1)){
                n1 = parseFloat(valorinput1);
            }
        }catch(err){
            
        }
        
        try{
            var valorinput2 = convertirANumero(input2.value);
            if(!isNaN(valorinput2)){
                n2 = parseFloat(valorinput2);
            }
        }catch(err){
            
        }
        
        if(!isNaN(n1) && !isNaN(n2)){
            if(n1 < 0)
                n1 = 0;
            if(n2 < 0)
                n2 = 0;
            tot = n1 + n2;
        }else if(!isNaN(n1)){
            tot = n1;
        }else if(!isNaN(n2)){
            tot = n2;
        }else{
            tot = "";
        }
        if (tot !="") tot = tot.toFixed(2);
        inputTot.value = ""+tot;
        inputTot.value = inputTot.value.replace(/\./g,',');
        //FormatNumber(inputTot.value, 8, 0, inputTot.id);//nuevo
        //reemplazarPuntosEca(inputTot);        
        //ajustarDecimalesImportesSolicitud();
    }
    
    function calcularTotalSubvSolicitud(idInput1, idInput2, idInput3, idInputTot, idInputOrgPublicos, idInputOrgPrivados){
        var input1 = document.getElementById(idInput1);
        var input2 = document.getElementById(idInput2);
        var input3 = document.getElementById(idInput3);
        var inputTot = document.getElementById(idInputTot);
        var ayuPub = document.getElementById(idInputOrgPublicos);
        var ayuPriv = document.getElementById(idInputOrgPrivados);
        var n1;
        var n2;
        var n3;
        var tot;
        var nAyuPub;
        var nAyuPriv;
        var valor;
        try{
            valor = input1.value;
            if(valor != null && valor != ''){
                valor = convertirANumero(valor);
                
            } else valor=0;                 
            if(!isNaN(valor)){                
                n1 = parseFloat(valor);
            }
        }catch(err){
            
        }
        
        try{
            valor = input2.value;
            if(valor != null && valor != ''){
                valor = convertirANumero(valor);                
            }else valor=0;                  
            if(!isNaN(valor)){                
                n2 = parseFloat(valor);
            }
            
        }catch(err){
            
        }
        
         try{
            valor = input3.value;
            if(valor != null && valor != ''){
                valor = convertirANumero(valor);
                
            }else valor=0;              
            
            if(!isNaN(valor)){                
                n3 = parseFloat(valor);
            }
        }catch(err){
            
        }
        
        try{
            valor = ayuPub.value;
            if(valor != null && valor != ''){
                valor = convertirANumero(valor);                
            }else valor=0;                  
            if(!isNaN(valor)){                
                nAyuPub = parseFloat(valor);
            }
            
        }catch(err){
            
        }
        
        try{
            valor = ayuPriv.value;
            if(valor != null && valor != ''){
                valor = convertirANumero(valor);                
            }else valor=0;                  
            if(!isNaN(valor)){                
                nAyuPriv = parseFloat(valor);
            }
            
        }catch(err){
            
        }
        
        if(!isNaN(n1) && !isNaN(n2) && !isNaN(n3)){
            if(n1 < 0)
                n1 = 0;
            if(n2 < 0)
                n2 = 0;
            if(n3 < 0)
                n3 = 0;
            tot = n1 + n2 + n3;            
        }else if(!isNaN(n1)){
            tot = n1;
        }else if(!isNaN(n2)){
            tot = n2;
        }else if(!isNaN(n3)){
            tot = n3;
        }else{
            tot = '';
        }
        if (tot != ''){
            if(!isNaN(nAyuPub)){
                if(nAyuPub < 0)
                    nAyuPub = 0;
            }else{
                nAyuPub = 0;
            }
            if(!isNaN(nAyuPriv)){
                if(nAyuPriv < 0)
                    nAyuPriv = 0;
            }else{
                nAyuPriv = 0;
            }
            tot = tot - nAyuPub;
            tot = tot - nAyuPriv
            tot = tot.toFixed(2);
        }
        inputTot.value =tot;
        reemplazarPuntosEca(inputTot);        
        //ajustarDecimalesImportesSolicitud();
        
    }
    
     function guardarSolicitudEca(){
        if(validarSolicitudEca()){
            barraProgresoEca('on', 'barraProgresoSolicitudEca');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            
            var otrasAyudas;
            if(document.getElementById('radioOtrasAyudasS').checked)
                otrasAyudas = 'S';
            else if(document.getElementById('radioOtrasAyudasN').checked)
                otrasAyudas = 'N';
            
            parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=guardarDatosSolicitud&tipo=0&numero=<%=numExpediente%>'
                        +'&col1SolicH='+convertirANumero(document.getElementById('col1SolicH').value)
                        +'&col1SolicM='+convertirANumero(document.getElementById('col1SolicM').value)
                        +'&col2SolicH='+convertirANumero(document.getElementById('col2SolicH').value)
                        +'&col2SolicM='+convertirANumero(document.getElementById('col2SolicM').value)
                        +'&col3SolicH='+convertirANumero(document.getElementById('col3SolicH').value)
                        +'&col3SolicM='+convertirANumero(document.getElementById('col3SolicM').value)
                        +'&col4SolicH='+convertirANumero(document.getElementById('col4SolicH').value)
                        +'&col4SolicM='+convertirANumero(document.getElementById('col4SolicM').value)
                        +'&col5SolicH='+convertirANumero(document.getElementById('col5SolicH').value)
                        +'&col5SolicM='+convertirANumero(document.getElementById('col5SolicM').value)
                        +'&col6SolicH='+convertirANumero(document.getElementById('col6SolicH').value)
                        +'&col6SolicM='+convertirANumero(document.getElementById('col6SolicM').value)
                        +'&seg1SolicH='+convertirANumero(document.getElementById('seg1SolicH').value)
                        +'&seg1SolicM='+convertirANumero(document.getElementById('seg1SolicM').value)
                        +'&nActuacionesSolic='+convertirANumero(document.getElementById('nActuacionesSolic').value)
                        +'&prospectoresSolicNum='+convertirANumero(document.getElementById('prospectoresSolicNum').value)
                        +'&prospectoresSolicSol='+convertirANumero(document.getElementById('prospectoresSolicSol').value)
                        +'&preparadoresSolicNum='+convertirANumero(document.getElementById('preparadoresSolicNum').value)
                        +'&preparadoresSolicSol='+convertirANumero(document.getElementById('preparadoresSolicSol').value)
                        +'&gastosGeneralesSolic='+convertirANumero(document.getElementById('gastosGeneralesSolic').value)
                        +'&impOrgPublicos='+convertirANumero(document.getElementById('impOrgPublicos').value)
                        +'&impOrgPrivados='+convertirANumero(document.getElementById('impOrgPrivados').value)
                        +'&otrasAyudas='+otrasAyudas
                        +'&totSubSolicitada='+convertirANumero(document.getElementById('totSubSolicitada').value)
                        +'&totSubAprobada='+convertirANumero(document.getElementById('totSubAprobada').value)                        
                        +'&impOrgPublicosAnexSol='+convertirANumero(document.getElementById('impOrgPublicosAnexSol').value)
                        +'&impOrgPrivadosAnexSol='+convertirANumero(document.getElementById('impOrgPrivadosAnexSol').value)
                        +'&col1HConc='+convertirANumero(document.getElementById('col1HConc').value)
                        +'&col1MConc='+convertirANumero(document.getElementById('col1MConc').value)
                        +'&col2HConc='+convertirANumero(document.getElementById('col2HConc').value)
                        +'&col2MConc='+convertirANumero(document.getElementById('col2MConc').value)
                        +'&col3HConc='+convertirANumero(document.getElementById('col3HConc').value)
                        +'&col3MConc='+convertirANumero(document.getElementById('col3MConc').value)
                        +'&col4HConc='+convertirANumero(document.getElementById('col4HConc').value)
                        +'&col4MConc='+convertirANumero(document.getElementById('col4MConc').value)
                +'&col5HConc='+convertirANumero(document.getElementById('col5HConc').value)
                        +'&col5MConc='+convertirANumero(document.getElementById('col5MConc').value)
                +'&col6HConc='+convertirANumero(document.getElementById('col6HConc').value)
                        +'&col6MConc='+convertirANumero(document.getElementById('col6MConc').value)
                       // +'&segHConc='+convertirANumero(document.getElementById('segHConc').value)
                       // +'&segMConc='+convertirANumero(document.getElementById('segMConc').value)
                        +'&nActuacionesConc='+convertirANumero(document.getElementById('nActuacionesConc').value)
                        +'&prospectoresNumConc='+convertirANumero(document.getElementById('prospectoresConcNum').value)
                        +'&prospectoresSolConc='+convertirANumero(document.getElementById('prospectoresConcSol').value)
                        +'&preparadoresNumConc='+convertirANumero(document.getElementById('preparadoresConcNum').value)
                        +'&preparadoresSolConc='+convertirANumero(document.getElementById('preparadoresConcSol').value)
                        +'&gastosGeneralesConc='+convertirANumero(document.getElementById('gastosGeneralesConc').value)
                        +'&impOrgPublicosConcedidos='+convertirANumero(document.getElementById('impOrgPublicosConcedidos').value)
                        +'&impOrgPrivadosConcedidos='+convertirANumero(document.getElementById('impOrgPrivadosConcedidos').value)
                        +'&control='+control.getTime();


            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                //var formData = new FormData(document.getElementById('formContrato'));
                ajax.send(parametros);
                if (ajax.readyState==4 && ajax.status==200){
                    var xmlDoc = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;
                        xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async="false";
                        xmlDoc.loadXML(text);
                    }else{
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//if (ajax.readyState==4 && ajax.status==200)
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }  
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                barraProgresoEca('off', 'barraProgresoSolicitudEca');
                if(codigoOperacion=="0"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    mostrarPestanasSolicitudEca(1);
                    actualizarOtrasPestanasEca();
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//try-catch
        }else{
            jsp_alerta("A", mensajeValidacion);
        }
    }
    
    function validarSolicitudEca(){
        mensajeValidacion = '';
        var correcto = true;
        if(!validarInsercionesSolicitudEca()){
            correcto = false;
        }
        if(!validarSeguimientosSolicitudEca()){
            correcto = false;
        }
        if(!validarActuacionSolicitudEca()){
            correcto = false;
        }
        if(!validarSubvencionApro()){
            correcto = false;
        }
        if(!validarOtrasAyudasCargaManual()){
            correcto = false;
        }
        return correcto;
    }
    
    function validarSubvencionApro()
    {
        var correcto = true;
        var subSolic = convertirANumero(document.getElementById('totSubSolicitada').value);
        var subCarg = convertirANumero(document.getElementById('totSolCarga').value);
        var subMax = convertirANumero(document.getElementById('totMaxSub').value);
        if((subSolic != null && subSolic != '') && (subCarg != null && subCarg != '') && (subMax != null && subMax != '')){
            var subApro = convertirANumero(document.getElementById('totSubAprobada').value);
            if(subApro != null && subApro != ''){
                var subSolicN = parseFloat(subSolic);
                var subCargN = parseFloat(subCarg);
                var subMaxN = parseFloat(subMax);
                var subAproN = parseFloat(subApro);
                if(subAproN > subSolicN && subAproN > subCargN && subAproN > subMaxN){
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.totSubAprobadaIncorrecto2")%>';
                    correcto = false;  
                }
            }
        }
        return correcto;
    }
    
    function validarInsercionesSolicitudEca(){
        //Colectivo 1-H
        var correcto = true;
        if(document.getElementById('col1SolicH').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col1SolicH'),8,2)){
                document.getElementById('col1SolicH').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo1HIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col1SolicH').value);
                    if(desp < 0){
                        document.getElementById('col1SolicH').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo1HIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col1SolicH').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col1SolicH').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo1HIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 1-M
        if(document.getElementById('col1SolicM').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col1SolicM'),8,2)){
                document.getElementById('col1SolicM').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo1MIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col1SolicM').value);
                    if(desp < 0){
                        document.getElementById('col1SolicM').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo1MIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col1SolicM').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col1SolicM').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo1MIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 2-H
        if(document.getElementById('col2SolicH').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col2SolicH'),8,2)){
                document.getElementById('col2SolicH').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo2HIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col2SolicH').value);
                    if(desp < 0){
                        document.getElementById('col2SolicH').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo2HIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col2SolicH').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col2SolicH').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo2HIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 2-M
        if(document.getElementById('col2SolicM').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col2SolicM'),8,2)){
                document.getElementById('col2SolicM').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo2MIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col2SolicM').value);
                    if(desp < 0){
                        document.getElementById('col2SolicM').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo2MIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col2SolicM').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col2SolicM').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo2MIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 3-H
        if(document.getElementById('col3SolicH').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col3SolicH'),8,2)){
                document.getElementById('col3SolicH').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo3HIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col3SolicH').value);
                    if(desp < 0){
                        document.getElementById('col3SolicH').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo3HIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col3SolicH').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col3SolicH').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo3HIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 3-M
        if(document.getElementById('col3SolicM').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col3SolicM'),8,2)){
                document.getElementById('col3SolicM').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo3MIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col3SolicM').value);
                    if(desp < 0){
                        document.getElementById('col3SolicM').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo3MIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col3SolicM').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col3SolicM').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo3MIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 4-H
        if(document.getElementById('col4SolicH').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col4SolicH'),8,2)){
                document.getElementById('col4SolicH').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo4HIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col4SolicH').value);
                    if(desp < 0){
                        document.getElementById('col4SolicH').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo4HIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col4SolicH').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col4SolicH').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo4HIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 4-M
        if(document.getElementById('col4SolicM').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col4SolicM'),8,2)){
                document.getElementById('col4SolicM').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo4MIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col4SolicM').value);
                    if(desp < 0){
                        document.getElementById('col4SolicM').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo4MIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col4SolicM').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col4SolicM').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo4MIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 5-H
        if(document.getElementById('col5SolicH').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col5SolicH'),8,2)){
                document.getElementById('col5SolicH').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo5HIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col5SolicH').value);
                    if(desp < 0){
                        document.getElementById('col5SolicH').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo5HIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col5SolicH').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col5SolicH').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo5HIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 5-M
        if(document.getElementById('col5SolicM').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col5SolicM'),8,2)){
                document.getElementById('col5SolicM').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo5MIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col5SolicM').value);
                    if(desp < 0){
                        document.getElementById('col5SolicM').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo5MIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col5SolicM').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col5SolicM').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo5MIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 6-H
        if(document.getElementById('col6SolicH').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col6SolicH'),8,2)){
                document.getElementById('col6SolicH').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo6HIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col6SolicH').value);
                    if(desp < 0){
                        document.getElementById('col6SolicH').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo6HIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col6SolicH').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col6SolicH').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo6HIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Colectivo 6-M
        if(document.getElementById('col6SolicM').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('col6SolicM'),8,2)){
                document.getElementById('col6SolicM').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo6MIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('col6SolicM').value);
                    if(desp < 0){
                        document.getElementById('col6SolicM').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo6MIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('col6SolicM').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('col6SolicM').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.colectivo6MIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        return correcto;
    }
    
    function validarSeguimientosSolicitudEca(){
        var correcto = true;
        //Seguimientos 1-H
        if(document.getElementById('seg1SolicH').value != ''){
            if(!validarNumericoEca(document.getElementById('seg1SolicH'))){
                document.getElementById('seg1SolicH').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.seg1HIncorrecto"), anoExpediente)%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseInt(document.getElementById('seg1SolicH').value);
                    if(desp < 0){
                        document.getElementById('seg1SolicH').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.seg1HIncorrecto"), anoExpediente)%>';
                        correcto = false;
                    }else{
                        document.getElementById('seg1SolicH').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('seg1SolicH').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.seg1HIncorrecto"), anoExpediente)%>';
                    correcto = false;
                }
            }
        }
        
        //Seguimientos 1-M
        if(document.getElementById('seg1SolicM').value != ''){
            if(!validarNumericoEca(document.getElementById('seg1SolicM'))){
                document.getElementById('seg1SolicM').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.seg1MIncorrecto"), anoExpediente)%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseInt(document.getElementById('seg1SolicM').value);
                    if(desp < 0){
                        document.getElementById('seg1SolicM').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.seg1MIncorrecto"), anoExpediente)%>';
                        correcto = false;
                    }else{
                        document.getElementById('seg1SolicM').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('seg1SolicM').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.seg1MIncorrecto"), anoExpediente)%>';
                    correcto = false;
                }
            }
        }
        
        //Seguimientos 2
        if(document.getElementById('nActuacionesSolic').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('nActuacionesSolic'), 10, 2)){
                document.getElementById('nActuacionesSolic').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.nActuacionesIncorrecto"), anoExpediente)%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('nActuacionesSolic').value);
                    if(desp < 0){
                        document.getElementById('nActuacionesSolic').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.nActuacionesIncorrecto"), anoExpediente)%>';
                        correcto = false;
                    }else if(desp > 99999999.0){
                        document.getElementById('nActuacionesSolic').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.nActuacionesIncorrecto"), anoExpediente)%>';
                        correcto = false;
                    }else{
                        document.getElementById('nActuacionesSolic').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('nActuacionesSolic').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.nActuacionesIncorrecto"), anoExpediente)%>';
                    correcto = false;
                }
            }
        }
        return correcto;
    }
    
    function validarActuacionSolicitudEca(){
        var correcto = true;
        //Prospectores num
        if(document.getElementById('prospectoresSolicNum').value != ''){
            if(!validarNumericoEca(document.getElementById('prospectoresSolicNum'))){
                document.getElementById('prospectoresSolicNum').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.prospectoresNumIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseInt(document.getElementById('prospectoresSolicNum').value);
                    if(desp < 0){
                        document.getElementById('prospectoresSolicNum').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.prospectoresNumIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('prospectoresSolicNum').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('prospectoresSolicNum').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.prospectoresNumIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        //Prospectores imp
        if(document.getElementById('prospectoresSolicSol').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('prospectoresSolicSol'), 10, 2)){
                document.getElementById('prospectoresSolicSol').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.prospectoresImpIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('prospectoresSolicSol').value);
                    if(desp < 0){
                        document.getElementById('prospectoresSolicSol').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.prospectoresImpIncorrecto")%>';
                        correcto = false;
                    }else if(desp > 99999999.0){
                        document.getElementById('prospectoresSolicSol').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.prospectoresImpIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('prospectoresSolicSol').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('prospectoresSolicSol').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.prospectoresImpIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        //Preparadores num
        if(document.getElementById('preparadoresSolicNum').value != ''){
            if(!validarNumericoEca(document.getElementById('preparadoresSolicNum'))){
                document.getElementById('preparadoresSolicNum').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresNumIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseInt(document.getElementById('preparadoresSolicNum').value);
                    if(desp < 0){
                        document.getElementById('preparadoresSolicNum').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresNumIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('preparadoresSolicNum').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('preparadoresSolicNum').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresNumIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        //Preparadores imp
        if(document.getElementById('preparadoresSolicSol').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('preparadoresSolicSol'), 10, 2)){
                document.getElementById('preparadoresSolicSol').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('preparadoresSolicSol').value);
                    if(desp < 0){
                        document.getElementById('preparadoresSolicSol').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                        correcto = false;
                    }else if(desp > 99999999.0){
                        document.getElementById('preparadoresSolicSol').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('preparadoresSolicSol').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('preparadoresSolicSol').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        //Gastos
        if(document.getElementById('gastosGeneralesSolic').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('gastosGeneralesSolic'), 10, 2)){
                document.getElementById('gastosGeneralesSolic').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.gastosGeneralesIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('gastosGeneralesSolic').value);
                    if(desp < 0){
                        document.getElementById('gastosGeneralesSolic').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.gastosGeneralesIncorrecto")%>';
                        correcto = false;
                    }else if(desp > 99999999.0){
                        document.getElementById('gastosGeneralesSolic').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.gastosGeneralesIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('gastosGeneralesSolic').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('gastosGeneralesSolic').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.gastosGeneralesIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        if(document.getElementById('radioOtrasAyudasS').checked == true)
        {
            //Subv. Org. Publicos
            if(document.getElementById('impOrgPublicos').value != ''){
                if(!validarNumericoDecimalEca(document.getElementById('impOrgPublicos'), 10, 2)){
                    document.getElementById('impOrgPublicos').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPublicosIncorrecto")%>';
                    correcto = false;
                }
                else{
                    try{
                        var desp = parseFloat(document.getElementById('impOrgPublicos').value);
                        if(desp < 0){
                            document.getElementById('impOrgPublicos').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPublicosIncorrecto")%>';
                            correcto = false;
                        }else if(desp > 99999999.0){
                            document.getElementById('impOrgPublicos').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPublicosIncorrecto")%>';
                            correcto = false;
                        }else{
                            document.getElementById('impOrgPublicos').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('impOrgPublicos').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPublicosIncorrecto")%>';
                        correcto = false;
                    }
                }
            }
            //Subv. Org. Privados
            if(document.getElementById('impOrgPrivados').value != ''){
                if(!validarNumericoDecimalEca(document.getElementById('impOrgPrivados'), 10, 2)){
                    document.getElementById('impOrgPrivados').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPrivadosIncorrecto")%>';
                    correcto = false;
                }
                else{
                    try{
                        var desp = parseFloat(document.getElementById('impOrgPrivados').value);
                        if(desp < 0){
                            document.getElementById('impOrgPrivados').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPrivadosIncorrecto")%>';
                            correcto = false;
                        }else if(desp > 99999999.0){
                            document.getElementById('impOrgPrivados').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPrivadosIncorrecto")%>';
                            correcto = false;
                        }else{
                            document.getElementById('impOrgPrivados').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('impOrgPrivados').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPrivadosIncorrecto")%>';
                        correcto = false;
                    }
                }
            }
        }
        
        //Total subvención solicitada
        if(document.getElementById('totSubSolicitada').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('totSubSolicitada'), 10, 2)){
                document.getElementById('totSubSolicitada').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('totSubSolicitada').value);
                    if(desp < 0){
                        document.getElementById('totSubSolicitada').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                        correcto = false;
                    }else if(desp > 99999999.0){
                        document.getElementById('totSubSolicitada').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('totSubSolicitada').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('totSubSolicitada').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        //Total subvención aprobada
        if(document.getElementById('totSubAprobada').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('totSubAprobada'), 10, 2)){
                document.getElementById('totSubAprobada').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('totSubAprobada').value);
                    if(desp < 0){
                        document.getElementById('totSubAprobada').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                        correcto = false;
                    }else if(desp > 99999999.0){
                        document.getElementById('totSubAprobada').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('totSubAprobada').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('totSubAprobada').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.preparadoresImpIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        return correcto;
    }
    
    function validarOtrasAyudasCargaManual(){
        var correcto = true;
        if(document.getElementById('radioOtrasAyudasS').checked == true)
        {
            //Subv. Org. Publicos
            if(document.getElementById('impOrgPublicosConcedidos').value != ''){
                if(!validarNumericoDecimalEca(document.getElementById('impOrgPublicosConcedidos'), 10, 2)){
                    document.getElementById('impOrgPublicosConcedidos').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPublicosIncorrectoCargaManual")%>';
                    correcto = false;
                }
                else{
                    try{
                        var desp = parseFloat(document.getElementById('impOrgPublicosConcedidos').value);
                        if(desp < 0){
                            document.getElementById('impOrgPublicosConcedidos').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPublicosIncorrectoCargaManual")%>';
                            correcto = false;
                        }else if(desp > 99999999.0){
                            document.getElementById('impOrgPublicosConcedidos').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPublicosIncorrectoCargaManual")%>';
                            correcto = false;
                        }else{
                            document.getElementById('impOrgPublicosConcedidos').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('impOrgPublicosConcedidos').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPublicosIncorrectoCargaManual")%>';
                        correcto = false;
                    }
                }
            }
            //Subv. Org. Privados
            if(document.getElementById('impOrgPrivadosConcedidos').value != ''){
                if(!validarNumericoDecimalEca(document.getElementById('impOrgPrivadosConcedidos'), 10, 2)){
                    document.getElementById('impOrgPrivadosConcedidos').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPrivadosIncorrectoCargaManual")%>';
                    correcto = false;
                }
                else{
                    try{
                        var desp = parseFloat(document.getElementById('impOrgPrivadosConcedidos').value);
                        if(desp < 0){
                            document.getElementById('impOrgPrivadosConcedidos').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPrivadosIncorrectoCargaManual")%>';
                            correcto = false;
                        }else if(desp > 99999999.0){
                            document.getElementById('impOrgPrivadosConcedidos').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPrivadosIncorrectoCargaManual")%>';
                            correcto = false;
                        }else{
                            document.getElementById('impOrgPrivadosConcedidos').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('impOrgPrivadosConcedidos').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.subvOrgPrivadosIncorrectoCargaManual")%>';
                        correcto = false;
                    }
                }
            }
        }
        return correcto;
    }
    
    function validarTotales(){
        var correcto = true;
        return correcto;
    }
    
    function deshabilitarRadiosSolicitudEca(){
        if(document.forms[0].modoConsulta.value == "si"){
            document.getElementById('radioOtrasAyudasS').disabled = true;
            document.getElementById('radioOtrasAyudasN').disabled = true;
            
            document.getElementById('col1SolicH').readOnly = true;
        }
    }
            
   function ajustarDecimalesImportesSolicitud(){
        var campo;

        campo = document.getElementById('col1HConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col1MConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col1TConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2HConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2MConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2TConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3HConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3MConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3TConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4HConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4MConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4TConc');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('col5HConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col5MConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col5TConc');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('col6HConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col6MConc');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col6TConc');
        ajustarDecimalesCampo(campo, 2);
        
        campo = document.getElementById('col1AnexCargaH');
        ajustarDecimalesCampo(campo, 2); 
        campo = document.getElementById('col1AnexCargaM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col1AnexCargaT');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2AnexCargaH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2AnexCargaM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2AnexCargaT');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3AnexCargaH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3AnexCargaM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3AnexCargaT');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4AnexCargaH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4AnexCargaM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4AnexCargaT');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('col5AnexCargaH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col5AnexCargaM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col5AnexCargaT');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('col6AnexCargaH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col6AnexCargaM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col6AnexCargaT');
        ajustarDecimalesCampo(campo, 2);
        
        campo = document.getElementById('col1SolicH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col1SolicM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col1SolicT');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2SolicH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2SolicM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col2SolicT');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3SolicH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3SolicM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col3SolicT');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4SolicH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4SolicM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col4SolicT');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('col5SolicH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col5SolicM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col5SolicT');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('col6SolicH');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col6SolicM');
        ajustarDecimalesCampo(campo, 2);        
        campo = document.getElementById('col6SolicT');
        ajustarDecimalesCampo(campo, 2);
        
        //Nª Actuaciones solic
        campo = document.getElementById('nActuacionesSolic');
        ajustarDecimalesCampo(campo, 2);

        //Nº Actuaciones Anex
        campo = document.getElementById('nActuacionesConc');
        ajustarDecimalesCampo(campo, 2);
        
        //Nº Actuaciones Anex Carga
        campo = document.getElementById('nActuacionesAnexCarga');
        ajustarDecimalesCampo(campo, 2);

        //Prospectores solic sol
        campo = document.getElementById('prospectoresSolicSol');
        ajustarDecimalesCampo(campo, 2);

        //Prospectores anex sol
        campo = document.getElementById('prospectoresConcSol');
        ajustarDecimalesCampo(campo, 2);

        //Prospectores anex carga sol
        campo = document.getElementById('prospectoresAnexCargaSol');
        ajustarDecimalesCampo(campo, 2);

        //Preparadores solic sol
        campo = document.getElementById('preparadoresSolicSol');
        ajustarDecimalesCampo(campo, 2);

        //Preparadores anex sol
        campo = document.getElementById('preparadoresConcSol');
        ajustarDecimalesCampo(campo, 2);

        //Preparadores anex carga sol
        campo = document.getElementById('preparadoresAnexCargaSol');
        ajustarDecimalesCampo(campo, 2);

        //Gastos generales solic
        campo = document.getElementById('gastosGeneralesSolic');
        ajustarDecimalesCampo(campo, 2);

        //Gastos generales anex
        campo = document.getElementById('gastosGeneralesConc');
        ajustarDecimalesCampo(campo, 2);

        //Gastos generales anex carga
        campo = document.getElementById('gastosGeneralesAnexCarga');
        ajustarDecimalesCampo(campo, 2);

        //Imp org publicos columna solicitud
        campo = document.getElementById('impOrgPublicos');
        ajustarDecimalesCampo(campo, 2);

        //Imp org privados columna solicitud
        campo = document.getElementById('impOrgPrivados');
        ajustarDecimalesCampo(campo, 2);

        //Tot sub solicitada
        campo = document.getElementById('totSubSolicitada');
        ajustarDecimalesCampo(campo, 2);
        
        //Tot solicitud carga        
        campo = document.getElementById('totSolCarga');
        ajustarDecimalesCampo(campo, 2);

        //Tot max sub
        campo = document.getElementById('totMaxSub');
        ajustarDecimalesCampo(campo, 2);

        //Tot sub aprobada
        campo = document.getElementById('totSubAprobada');
        ajustarDecimalesCampo(campo, 2);

        //Imp org publicos columna carga manual
        campo = document.getElementById('impOrgPublicosConcedidos');
        ajustarDecimalesCampo(campo, 2);

        //Imp org privados columna carga manual
        campo = document.getElementById('impOrgPrivadosConcedidos');
        ajustarDecimalesCampo(campo, 2);
    }
    
    function actualizarDatosAnexosSolicitud(actualizarOtrasPestanas){
        try{
            var result = getDatosAnexosSolicitud();
            var resultprep = getListaPreparadoresSolicitudEca();
            var resultpros = getListaProspectoresSolicitudEca();
            recargarDatosAnexosSolicitud(result, resultprep, resultpros, actualizarOtrasPestanas);
        }catch(err){
        }
    }
    
    function getDatosAnexosSolicitud(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        var datosAnexos = new Array();
        parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=getDatosAnexosSolicitud&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
        try{
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
            //var formData = new FormData(document.getElementById('formContrato'));
            ajax.send(parametros);
            if (ajax.readyState==4 && ajax.status==200){
                var xmlDoc = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                    var text = ajax.responseText;
                    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                    xmlDoc.async="false";
                    xmlDoc.loadXML(text);
                }else{
                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                    xmlDoc = ajax.responseXML;
                }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
            }//if (ajax.readyState==4 && ajax.status==200)
            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
            datosAnexos = extraerDatosAnexosSolicitudEca(nodos);
        }
        catch(err){
            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return datosAnexos;
    }
    
      function extraerDatosAnexosSolicitudEca(nodos){
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaProspectores = new Array();
        var fila = new Array();
        var nodoFila;
        var hijosFila;
        var nodoCampo;
        var j;
        for(j=0;hijos!=null && j<hijos.length;j++){
            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                listaProspectores[j] = codigoOperacion;
            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
            else if(hijos[j].nodeName=="VALORES_CONCEDIDO"){
                nodoFila = hijos[j];
                hijosFila = nodoFila.childNodes;
                for(var cont = 0; cont < hijosFila.length; cont++){
                    if(hijosFila[cont].nodeName=="C1H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[0] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C1M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[1] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C1T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[2] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C2H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[3] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C2M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[4] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C2T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[5] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C3H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[6] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[6] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C3M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[7] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[7] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C3T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[8] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[8] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C4H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[9] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[9] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C4M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[10] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[10] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C4T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[11] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[11] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C5H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[12] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[12] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C5M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[13] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[13] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C5T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[14] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[14] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C6H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[15] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[15] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C6M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[16] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[16] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C6T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[17] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[17] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="TOT_ACTUACIONES"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[18] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[18] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="PROSPECTORES_NUM"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[19] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[19] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="PROSPECTORES_SOL"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[20] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[20] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="PREPARADORES_NUM"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[21] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[21] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="PREPARADORES_SOL"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[22] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[22] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="GASTOS"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[23] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[23] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="MAX_SUBV"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[24] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[24] = '';
                        }
                    }
                }
            }                     
            
            else if(hijos[j].nodeName=="VALORES_ANEXOS"){
                nodoFila = hijos[j];
                hijosFila = nodoFila.childNodes;
                for(var cont = 0; cont < hijosFila.length; cont++){
                    if(hijosFila[cont].nodeName=="C1H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[25] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[25] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C1M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[26] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[26] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C1T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[27] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[27] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C2H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[28] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[28] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C2M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[29] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[29] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C2T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[30] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[30] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C3H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[31] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[31] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C3M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[32] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[32] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C3T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[33] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[33] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C4H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[34] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[34] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C4M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[35] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[35] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C4T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[36] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[36] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C5H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[37] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[37] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C5M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[38] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[38] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C5T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[39] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[39] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C6H"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[40] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[40] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C6M"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[41] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[41] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="C6T"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[42] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[42] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="TOT_ACTUACIONES"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[43] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[43] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="PROSPECTORES_NUM"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[44] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[44] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="PROSPECTORES_SOL"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[45] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[45] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="PREPARADORES_NUM"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[46] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[46] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="PREPARADORES_SOL"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[47] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[47] = '';
                        }
                    }
                    else if(hijosFila[cont].nodeName=="GASTOS"){
                        nodoCampo = hijosFila[cont];
                        if(nodoCampo.childNodes.length > 0){
                            fila[48] = nodoCampo.childNodes[0].nodeValue;
                        }
                        else{
                            fila[48] = '';
                        }
                    }
                }
            }
        }
        return fila;
    }
    
      function recargarDatosAnexosSolicitud(result, resultprep, resultpros, actualizarOtrasPestanas){
        document.getElementById('col1HConc').value = result[0];
        document.getElementById('col1MConc').value = result[1];
        document.getElementById('col1TConc').value = result[2];
             
        document.getElementById('col2HConc').value = result[3];
        document.getElementById('col2MConc').value = result[4];
        document.getElementById('col2TConc').value = result[5];
                
        document.getElementById('col3HConc').value = result[6];
        document.getElementById('col3MConc').value = result[7];
        document.getElementById('col3TConc').value = result[8];
                
        document.getElementById('col4HConc').value = result[9];
        document.getElementById('col4MConc').value = result[10];
        document.getElementById('col4TConc').value = result[11];
        
         document.getElementById('col5HConc').value = result[12];
        document.getElementById('col5MConc').value = result[13];
        document.getElementById('col5TConc').value = result[14];
        
            document.getElementById('col6HConc').value = result[15];
        document.getElementById('col6MConc').value = result[16];
        document.getElementById('col6TConc').value = result[17];
        
        reemplazarPuntosEca(document.getElementById('col1HConc'));
        reemplazarPuntosEca(document.getElementById('col1MConc'));
        reemplazarPuntosEca(document.getElementById('col1TConc'));
        reemplazarPuntosEca(document.getElementById('col2HConc'));
        reemplazarPuntosEca(document.getElementById('col2MConc'));
        reemplazarPuntosEca(document.getElementById('col2TConc'));
        reemplazarPuntosEca(document.getElementById('col3HConc'));
        reemplazarPuntosEca(document.getElementById('col3MConc'));
        reemplazarPuntosEca(document.getElementById('col3TConc'));
        reemplazarPuntosEca(document.getElementById('col4HConc'));
        reemplazarPuntosEca(document.getElementById('col4MConc'));
        reemplazarPuntosEca(document.getElementById('col4TConc'));
        reemplazarPuntosEca(document.getElementById('col5HConc'));
        reemplazarPuntosEca(document.getElementById('col5MConc'));
        reemplazarPuntosEca(document.getElementById('col5TConc'));
                reemplazarPuntosEca(document.getElementById('col6HConc'));
        reemplazarPuntosEca(document.getElementById('col6MConc'));
        reemplazarPuntosEca(document.getElementById('col6TConc'));
                    
        document.getElementById('nActuacionesConc').value = result[18];
        document.getElementById('prospectoresConcNum').value = result[19];
        document.getElementById('prospectoresConcSol').value = result[20];       
        document.getElementById('preparadoresConcNum').value = result[21];
        document.getElementById('preparadoresConcSol').value = result[22];        
        document.getElementById('gastosGeneralesConc').value = result[23];
        
        document.getElementById('totMaxSub').value = result[24];
        
        
        //document.getElementById('preparadoresAnexNum').value = numPrepSinErr != undefined ? numPrepSinErr : '';
        //document.getElementById('prospectoresConcNum').value = numProsSinErr != undefined ? numProsSinErr : '';
        //document.getElementById('preparadoresConcSol').value = importeprep != undefined ? importeprep : '';
        //document.getElementById('prospectoresConcSol').value = importepros != undefined ? importepros : '';  
        
        
        
        /*var totMaxSubCalc;
        
        if(document.getElementById('prospectoresConcSol').value != ''){
            if(!totMaxSubCalc){
                totMaxSubCalc = 0.0;
            }
            totMaxSubCalc += parseFloat(document.getElementById('prospectoresConcSol').value);
        }
        
        if(document.getElementById('preparadoresConcSol').value != ''){
            if(!totMaxSubCalc){
                totMaxSubCalc = 0.0;
            }
            totMaxSubCalc += parseFloat(document.getElementById('preparadoresConcSol').value);
        }
        
        if(document.getElementById('gastosGeneralesConc').value != ''){
            if(!totMaxSubCalc){
                totMaxSubCalc = 0.0;
            }
            totMaxSubCalc += parseFloat(document.getElementById('gastosGeneralesConc').value);
        }
        
        if(document.getElementById('impOrgPublicosConcedidos').value != ''){
            if(!totMaxSubCalc){
                totMaxSubCalc = 0.0;
            }
            totMaxSubCalc -= parseFloat(document.getElementById('impOrgPublicosConcedidos').value);
        }
        
        if(document.getElementById('impOrgPrivadosConcedidos').value != ''){
            if(!totMaxSubCalc){
                totMaxSubCalc = 0.0;
            }
            totMaxSubCalc -= parseFloat(document.getElementById('impOrgPrivadosConcedidos').value);
        }
         
        if(totMaxSubCalc != undefined){
            document.getElementById('totMaxSub').value = totMaxSubCalc;
        }else{
            document.getElementById('totMaxSub').value = '';
        }*/
        
        
        
        reemplazarPuntosEca(document.getElementById('preparadoresConcSol')); 
        reemplazarPuntosEca(document.getElementById('prospectoresConcSol')); 
        reemplazarPuntosEca(document.getElementById('totMaxSub'));
        reemplazarPuntosEca(document.getElementById('totSolCarga')); 
        reemplazarPuntosEca(document.getElementById('gastosGeneralesConc')); 
        
        calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');
        
        
        /*REFRESO DE DATOS CARGA*/
        document.getElementById('col1AnexCargaH').value = result[25];
        document.getElementById('col1AnexCargaM').value = result[26];
        document.getElementById('col1AnexCargaT').value = result[27];        
        
        document.getElementById('col2AnexCargaH').value = result[28];
        document.getElementById('col2AnexCargaM').value = result[29];
        document.getElementById('col2AnexCargaT').value = result[30];        
        
        document.getElementById('col3AnexCargaH').value = result[31];
        document.getElementById('col3AnexCargaM').value = result[32];
        document.getElementById('col3AnexCargaT').value = result[33];        
        
        document.getElementById('col4AnexCargaH').value = result[34];
        document.getElementById('col4AnexCargaM').value = result[35];
        document.getElementById('col4AnexCargaT').value = result[36];
 
        document.getElementById('col5AnexCargaH').value = result[37];
        document.getElementById('col5AnexCargaM').value = result[38];
        document.getElementById('col5AnexCargaT').value = result[39];
        
        document.getElementById('col6AnexCargaH').value = result[40];
        document.getElementById('col6AnexCargaM').value = result[41];
        document.getElementById('col6AnexCargaT').value = result[42];
        
        reemplazarPuntosEca(document.getElementById('col1AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col1AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col1AnexCargaT'));
        reemplazarPuntosEca(document.getElementById('col2AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col2AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col2AnexCargaT'));
        reemplazarPuntosEca(document.getElementById('col3AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col3AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col3AnexCargaT'));
        reemplazarPuntosEca(document.getElementById('col4AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col4AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col4AnexCargaT'));
        reemplazarPuntosEca(document.getElementById('col5AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col5AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col5AnexCargaT'));
        reemplazarPuntosEca(document.getElementById('col6AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col6AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col6AnexCargaT'));
        
        document.getElementById('nActuacionesAnexCarga').value = result[43];
        
        document.getElementById('prospectoresAnexCargaNum').value = result[44];
        document.getElementById('prospectoresAnexCargaSol').value = result[45];
        
        document.getElementById('preparadoresAnexCargaNum').value = result[46];
        document.getElementById('preparadoresAnexCargaSol').value = result[47];  
        
        document.getElementById('gastosGeneralesAnexCarga').value = result[48];   
        
        
        
        //var v1 = document.getElementById('prospectoresAnexCargaSol').value!=""?parseFloat(document.getElementById('prospectoresAnexCargaSol').value):0.0;
        //var v2 = document.getElementById('preparadoresAnexCargaSol').value!=""?parseFloat(document.getElementById('preparadoresAnexCargaSol').value):0.0;
        //var v3 = document.getElementById('gastosGeneralesAnexCarga').value!=""?parseFloat(document.getElementById('gastosGeneralesAnexCarga').value):0.0;
        //var v4 = document.getElementById('impOrgPublicos').value!=""?parseFloat(document.getElementById('impOrgPublicos').value):0.0;
        //var v5 = document.getElementById('impOrgPrivados').value!=""?parseFloat(document.getElementById('impOrgPrivados').value):0.0;
        
        //var vT = v1 + v2 + v3 - v4 - v5
        
        var vT;
        var vAct = document.getElementById('prospectoresAnexCargaSol').value;
        if(vAct != ''){
            if(!vT){
                vT = 0.0;
            }
            vT += parseFloat(document.getElementById('prospectoresAnexCargaSol').value);
        }
        
        vAct = document.getElementById('preparadoresAnexCargaSol').value;
        if(vAct != ''){
            if(!vT){
                vT = 0.0;
            }
            vT += parseFloat(document.getElementById('preparadoresAnexCargaSol').value);
        }
        
        vAct = document.getElementById('gastosGeneralesAnexCarga').value;
        if(vAct != ''){
            if(!vT){
                vT = 0.0;
            }
            vT += parseFloat(document.getElementById('gastosGeneralesAnexCarga').value);
        }
       
        vAct = document.getElementById('impOrgPublicosAnexSol').value;
        if(vAct != ''){
            if(!vT){
                vT = 0.0;
            }
            vT -= parseFloat(convertirANumero(document.getElementById('impOrgPublicosAnexSol').value));
        }
        
        vAct = document.getElementById('impOrgPrivadosAnexSol').value;
        if(vAct != ''){
            if(!vT){
                vT = 0.0;
            }
            vT -= parseFloat(convertirANumero(document.getElementById('impOrgPrivadosAnexSol').value));
        }
        
        if(vT != undefined){
            document.getElementById('totSolCarga').value = ''+vT;
        }else{
            document.getElementById('totSolCarga').value = '';
        }
        
        reemplazarPuntosEca(document.getElementById('preparadoresAnexCargaSol')); 
        reemplazarPuntosEca(document.getElementById('prospectoresAnexCargaSol')); 
        reemplazarPuntosEca(document.getElementById('totSolCarga')); 
        reemplazarPuntosEca(document.getElementById('gastosGeneralesAnexCarga'));
        reemplazarPuntosEca(document.getElementById('totSolCarga')); 
        
        //si no coinciden lo solicitado con el detalle del anexo solicitado marcar en rojo       
        if (document.getElementById('totSubSolicitada').value != document.getElementById('totSolCarga').value){            
            document.getElementById('totSubSolicitada').style.border = '1px solid red';
            document.getElementById('totSolCarga').style.border = '1px solid red';
        }
        
        /*REFRESCO DE DATOS CARGA*/
                
         ajustarDecimalesImportesSolicitud();
         comprobarDiferencias();
        
        if(actualizarOtrasPestanas){
            actualizarOtrasPestanasEca(1);
        }
    }
    
    function comprobarDiferencias(){
     //si no coinciden lo solicitado con el detalle del anexo solicitado marcar en rojo    
     if (document.getElementById('totSubSolicitada').value != document.getElementById('totSolCarga').value){             
            document.getElementById('totSubSolicitada').style.border = '1px solid red';
            document.getElementById('totSolCarga').style.border = '1px solid red';
        }
    }
    
    function activarModoConsulta(){
        <%
            if(modoConsulta != null && modoConsulta.equalsIgnoreCase("si"))
            {
        %>
                document.getElementById('col1SolicH').disabled = true;
                document.getElementById('col1SolicH').readOnly = true;
                document.getElementById('col1SolicM').disabled = true;
                document.getElementById('col1SolicM').readOnly = true;
                document.getElementById('col2SolicH').disabled = true;
                document.getElementById('col2SolicH').readOnly = true;
                document.getElementById('col2SolicM').disabled = true;
                document.getElementById('col2SolicM').readOnly = true;
                document.getElementById('col3SolicH').disabled = true;
                document.getElementById('col3SolicH').readOnly = true;
                document.getElementById('col3SolicM').disabled = true;
                document.getElementById('col3SolicM').readOnly = true;
                document.getElementById('col4SolicH').disabled = true;
                document.getElementById('col4SolicH').readOnly = true;
                document.getElementById('col4SolicM').disabled = true;
                document.getElementById('col4SolicM').readOnly = true;
                document.getElementById('col5SolicH').disabled = true;
                document.getElementById('col5SolicH').readOnly = true;
                document.getElementById('col5SolicM').disabled = true;
                document.getElementById('col5SolicM').readOnly = true;
                document.getElementById('col6SolicH').disabled = true;
                document.getElementById('col6SolicH').readOnly = true;
                document.getElementById('col6SolicM').disabled = true;
                document.getElementById('col6SolicM').readOnly = true;
                document.getElementById('seg1SolicH').disabled = true;
                document.getElementById('seg1SolicH').readOnly = true;
                document.getElementById('seg1SolicM').disabled = true;
                document.getElementById('seg1SolicM').readOnly = true;
                document.getElementById('nActuacionesSolic').disabled = true;
                document.getElementById('nActuacionesSolic').readOnly = true;
                document.getElementById('prospectoresSolicNum').disabled = true;
                document.getElementById('prospectoresSolicNum').readOnly = true;
                document.getElementById('prospectoresSolicSol').disabled = true;
                document.getElementById('prospectoresSolicSol').readOnly = true;
                document.getElementById('preparadoresSolicNum').disabled = true;
                document.getElementById('preparadoresSolicNum').readOnly = true;
                document.getElementById('preparadoresSolicSol').disabled = true;
                document.getElementById('preparadoresSolicSol').readOnly = true;
                document.getElementById('gastosGeneralesSolic').disabled = true;
                document.getElementById('gastosGeneralesSolic').readOnly = true;
                document.getElementById('impOrgPublicos').disabled = true;
                document.getElementById('impOrgPublicos').readOnly = true;
                document.getElementById('impOrgPrivados').disabled = true;
                document.getElementById('impOrgPrivados').readOnly = true;
                document.getElementById('totSubAprobada').disabled = true;
                document.getElementById('totSubAprobada').readOnly = true;

				//campos concedido
                document.getElementById('preparadoresConcNum').disabled = true;
                document.getElementById('preparadoresConcNum').readOnly = true;

				document.getElementById('prospectoresConcNum').disabled = true;
                document.getElementById('prospectoresConcNum').readOnly = true;
                
        <%
            }
        %>
    }
</script>
<body>
    <div id="barraProgresoSolicitudEca" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgGuardandoDatosSolic">
                                                <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:5%;height:20%;"></td>
                                        <td class="imagenHide"></td>
                                        <td style="width:5%;height:20%;"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" style="height:10%" ></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <div style="width: 920px; float: left; text-align: left; clear: both; height: 414px; overflow-x: hidden; overflow-y: auto; border: 1px solid #999999" onscroll="deshabilitarRadiosSolicitudEca();">
        <table style="width: 910px; font-size: 12px; text-align: center; border-collapse: collapse;" cellpadding="1px">
            <tr>
                <td width="28%" ></td>
                <td width="8%" ></td>
                <td width="8%" ></td>
                <td width="7%" ></td>
                <td width="8%" ></td>
                <td width="8%" ></td>
                <td width="7%" ></td>
                <td width="8%" ></td>
                <td width="8%" ></td>
                <td width="7%" ></td>
                <td width="3%" ></td>
            </tr>
            <tr >
                <td colspan="10" style="text-align: left;" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.objetivosInsercion")%></td>
            </tr>
            <tr>
                <td colspan="10">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr class="negrita">
                <td rowspan="2" style="text-align: left;" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.inserciones")%></td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.solicitud")%></td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.anexos")%></td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.concedido")%></td>
            </tr>
            <tr class="negrita">
                <!--td></td-->
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.hombres")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.mujeres")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.total")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.hombres")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.mujeres")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.total")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.hombres")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.mujeres")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.total")%></td>
            </tr>
            <tr>
                <td colspan="4" style="border-right: 1px solid #1766A7;"></td>
                <td colspan="3" style="border-right: 1px solid #1766A7;">
                    <div class="separadorTabla"></div>
                </td>
                <td colspan="3">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.inserciones.colectivo1")%></td>
                <td><input id="col1SolicH" name="col1SolicH" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col1SolicH', 'col1SolicM', 'col1SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col1SolicH', 'col1SolicM', 'col1SolicT');"></td>
                <td><input id="col1SolicM" name="col1SolicM" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                          onkeyup="calcularTotalSolicitud('col1SolicH', 'col1SolicM', 'col1SolicT');FormatNumber(this.value, 8, 2, this.id);"
                          onblur="calcularTotalSolicitud('col1SolicH', 'col1SolicM', 'col1SolicT');"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col1SolicT" name="col1SolicT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col1AnexCargaH" name="col1AnexCargaH" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col1AnexCargaM" name="col1AnexCargaM" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col1AnexCargaT" name="col1AnexCargaT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col1HConc" name="col1HConc" type="text" size="8" class="inputTexto textoNumerico" onkeyup="calcularTotalSolicitud('col1HConc', 'col1MConc', 'col1TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col1MConc" name="col1MConc" type="text" size="8" class="inputTexto textoNumerico" onkeyup="calcularTotalSolicitud('col1HConc', 'col1MConc', 'col1TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col1TConc" name="col1TConc" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.inserciones.colectivo2")%></td>
                <td><input id="col2SolicH" name="col2SolicH" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col2SolicH', 'col2SolicM', 'col2SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col2SolicH', 'col2SolicM', 'col2SolicT');"></td>
                <td><input id="col2SolicM" name="col2SolicM" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col2SolicH', 'col2SolicM', 'col2SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col2SolicH', 'col2SolicM', 'col2SolicT');"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col2SolicT" name="col2SolicT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col2AnexCargaH" name="col2AnexCargaH" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col2AnexCargaM" name="col2AnexCargaM" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col2AnexCargaT" name="col2AnexCargaT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col2HConc" name="col2HConc" type="text" size="8" class="inputTexto textoNumerico" onkeyup="calcularTotalSolicitud('col2HConc', 'col2MConc', 'col2TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col2MConc" name="col2MConc" type="text" size="8" class="inputTexto textoNumerico" onkeyup="calcularTotalSolicitud('col2HConc', 'col2MConc', 'col2TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col2TConc" name="col2TConc" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
            </tr>
            <tr>
                <td style=" text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.inserciones.colectivo3")%></td>
                <td><input id="col3SolicH" name="col3SolicH" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                          onkeyup="calcularTotalSolicitud('col3SolicH', 'col3SolicM', 'col3SolicT');FormatNumber(this.value, 8, 2, this.id);"
                          onblur="calcularTotalSolicitud('col3SolicH', 'col3SolicM', 'col3SolicT');"></td>
                <td><input id="col3SolicM" name="col3SolicM" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                          onkeyup="calcularTotalSolicitud('col3SolicH', 'col3SolicM', 'col3SolicT');FormatNumber(this.value, 8, 2, this.id);"
                          onblur="calcularTotalSolicitud('col3SolicH', 'col3SolicM', 'col3SolicT');"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col3SolicT" name="col3SolicT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col3AnexCargaH" name="col3AnexCargaH" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col3AnexCargaM" name="col3AnexCargaM" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col3AnexCargaT" name="col3AnexCargaT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col3HConc" name="col3HConc" type="text" size="8" class="inputTexto textoNumerico " onkeyup="calcularTotalSolicitud('col3HConc', 'col3MConc', 'col3TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col3MConc" name="col3MConc" type="text" size="8" class="inputTexto textoNumerico " onkeyup="calcularTotalSolicitud('col3HConc', 'col3MConc', 'col3TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col3TConc" name="col3TConc" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.inserciones.colectivo4")%></td>
                <td><input id="col4SolicH" name="col4SolicH" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col4SolicH', 'col4SolicM', 'col4SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col4SolicH', 'col4SolicM', 'col4SolicT');"></td>
                <td><input id="col4SolicM" name="col4SolicM" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col4SolicH', 'col4SolicM', 'col4SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col4SolicH', 'col4SolicM', 'col4SolicT');"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col4SolicT" name="col4SolicT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col4AnexCargaH" name="col4AnexCargaH" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col4AnexCargaM" name="col4AnexCargaM" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col4AnexCargaT" name="col4AnexCargaT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col4HConc" name="col4HConc" type="text" size="8" class="inputTexto textoNumerico " onkeyup="calcularTotalSolicitud('col2HConc', 'col2MConc', 'col2TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col4MConc" name="col4MConc" type="text" size="8" class="inputTexto textoNumerico " onkeyup="calcularTotalSolicitud('col2HConc', 'col2MConc', 'col2TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col4TConc" name="col4TConc" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.inserciones.colectivo5")%></td>
                <td><input id="col5SolicH" name="col5SolicH" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col5SolicH', 'col5SolicM', 'col5SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col5SolicH', 'col5SolicM', 'col5SolicT');"></td>
                <td><input id="col5SolicM" name="col5SolicM" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col5SolicH', 'col5SolicM', 'col5SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col5SolicH', 'col5SolicM', 'col5SolicT');"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col5SolicT" name="col5SolicT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col5AnexCargaH" name="col5AnexCargaH" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col5AnexCargaM" name="col5AnexCargaM" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col5AnexCargaT" name="col5AnexCargaT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col5HConc" name="col5HConc" type="text" size="8" class="inputTexto textoNumerico " onkeyup="calcularTotalSolicitud('col5HConc', 'col5MConc', 'col5TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col5MConc" name="col5MConc" type="text" size="8" class="inputTexto textoNumerico " onkeyup="calcularTotalSolicitud('col5HConc', 'col5MConc', 'col5TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col5TConc" name="col5TConc" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.inserciones.colectivo6")%></td>
                <td><input id="col6SolicH" name="col6SolicH" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col6SolicH', 'col6SolicM', 'col6SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col6SolicH', 'col6SolicM', 'col6SolicT');"></td>
                <td><input id="col6SolicM" name="col6SolicM" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                           onkeyup="calcularTotalSolicitud('col6SolicH', 'col6SolicM', 'col6SolicT');FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularTotalSolicitud('col6SolicH', 'col6SolicM', 'col6SolicT');"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col6SolicT" name="col6SolicT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col6AnexCargaH" name="col6AnexCargaH" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col6AnexCargaM" name="col6AnexCargaM" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="col6AnexCargaT" name="col6AnexCargaT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="col6HConc" name="col6HConc" type="text" size="8" class="inputTexto textoNumerico " onkeyup="calcularTotalSolicitud('col6HConc', 'col6MConc', 'col6TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col6MConc" name="col6MConc" type="text" size="8" class="inputTexto textoNumerico " onkeyup="calcularTotalSolicitud('col6HConc', 'col6MConc', 'col6TConc');FormatNumber(this.value, 8, 2, this.id);"></td>
                <td><input id="col6TConc" name="col6TConc" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
            </tr>
            <tr>
                <td colspan="10">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr class="negrita">
                <td rowspan="2" style="text-align: left;" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.seguimientos")%></td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.solicitud")%></td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.anexos")%></td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.concedido")%></td>
            </tr>
            <!--tr class="negrita">
                <td style="text-align: left;" colspan="10" class="textoAzul"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.seguimientos")%></td>
            </tr-->
            <tr class="negrita">
                <!--td></td-->
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.hombres")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.mujeres")%></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.total")%></td>
                <td colspan="2" class="xCabecera"></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.total")%></td>
                <td colspan="2" class="xCabecera"></td>
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.total")%></td>
            </tr>
            <tr>
                <td colspan="4" style="border-right: 1px solid #1766A7;"></td>
                <td colspan="3" style="border-right: 1px solid #1766A7;">
                    <div class="separadorTabla"></div>
                </td>
                <td colspan="3">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.seguimientos.nPersContratadas"), anoExpediente)%></td>
                <td><input id="seg1SolicH" name="seg1SolicH" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                         onkeyup="calcularTotalSolicitud('seg1SolicH', 'seg1SolicM', 'seg1SolicT');"
                         onblur="calcularTotalSolicitud('seg1SolicH', 'seg1SolicM', 'seg1SolicT');"></td>
                <td><input id="seg1SolicM" name="seg1SolicM" type="text" size="8" class="inputTexto textoNumerico" maxlength="8" 
                         onkeyup="calcularTotalSolicitud('seg1SolicH', 'seg1SolicM', 'seg1SolicT');"
                         onblur="calcularTotalSolicitud('seg1SolicH', 'seg1SolicM', 'seg1SolicT');"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="seg1SolicT" name="seg1SolicT" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td colspan="3" style="border-right: 1px solid #1766A7;"></td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.seguimientos.nActuaciones"), anoExpediente)%></td>
                <td colspan="2"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="nActuacionesSolic" name="nActuacionesSolic" type="text" size="8" class="inputTexto textoNumerico" maxlength="11" onchange="FormatNumber(this.value, 8, 2,this.id);" onblur="FormatNumber(this.value, 8, 2,this.id); ajustarDecimalesImportesSolicitud();"></td>
                <!--td style="border-right: 1px solid #1766A7;"><input id="nActuacionesSolic" name="nActuacionesSolic" type="text" size="8" class="inputTexto textoNumerico" maxlength="11"></td-->
                <td colspan="2"></td>
                <td style="border-right: 1px solid #1766A7;"><input id="nActuacionesAnexCarga" name="nActuacionesAnexCarga" type="text" size="8" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td colspan="2"></td>
                <td><input id="nActuacionesConc" name="nActuacionesConc" type="text" size="8" class="inputTexto textoNumerico"></td>
            </tr>
            <tr>
                <td colspan="10">
                    <div class="separadorTabla"></div>
                </td>
            </tr>            
            <tr>
                <td colspan="10" style="text-align: left;" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.actuacionSubvencionable")%></td>
            </tr>
            <tr>
                <td colspan="10">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr class="negrita">
                <td rowspan="2" style="text-align: left;" >&nbsp;</td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.solicitud")%></td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.anexos")%></td>
                <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.concedido")%></td>
            </tr>
            <tr class="negrita">
                <!--td style="text-align: left;"></td-->
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.numero")%></td>
                <td colspan="2" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.solicitado")%></td>               
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.numero")%></td>
                <td colspan="2" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.solicitado")%></td>                
                <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.numero")%></td>
                <td colspan="2" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.solicitado")%></td>
                
            </tr>
           <tr>
                <td colspan="4" style="border-right: 1px solid #1766A7;"></td>
                <td colspan="3" style="border-right: 1px solid #1766A7;">
                    <div class="separadorTabla"></div>
                </td>
                <td colspan="3">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.nProspectores")%></td>
                <td><input id="prospectoresSolicNum" name="prospectoresSolicNum" type="text" size="3" class="inputTexto textoNumerico" maxlength="4"></td>
                <td  colspan="2" style="border-right: 1px solid #1766A7;">
                    <input id="prospectoresSolicSol" name="prospectoresSolicSol" type="text" size="10" class="inputTexto textoNumerico" maxlength="11" 
                    onkeyup="calcularGasto();calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');FormatNumber(this.value, 8, 2, this.id);comprobarDiferencias();" 
                    onblur="calcularGasto();calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');ajustarDecimalesImportesSolicitud();comprobarDiferencias();"
                    onChange="calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');ajustarDecimalesImportesSolicitud();">
                </td>
                <td><input id="prospectoresAnexCargaNum" name="prospectoresAnexCargaNum" type="text" size="3" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="prospectoresAnexCargaSol" name="prospectoresAnexCargaSol" type="text" size="10" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="prospectoresConcNum" name="prospectoresConcNum" type="text" size="3" class="inputTexto textoNumerico"></td>
                <td colspan="2"><input id="prospectoresConcSol" name="prospectoresConcSol" type="text" size="10" class="inputTexto textoNumerico " 
                                       onkeyup="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');FormatNumber(this.value, 8, 2,this.id);" 
                                        onblur="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');ajustarDecimalesImportesSolicitud();"></td>
                
            </tr>
            <tr>
                <td style=" text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.nPreparadores")%></td>
                <td><input id="preparadoresSolicNum" name="preparadoresSolicNum" type="text" size="3" class="inputTexto textoNumerico" maxlength="4"></td>
                <td colspan="2" style="border-right: 1px solid #1766A7;">
                    <input id="preparadoresSolicSol" name="preparadoresSolicSol" type="text" size="10" class="inputTexto textoNumerico" maxlength="11" 
                           onkeyup="calcularGasto();calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');FormatNumber(this.value, 8, 2, this.id);comprobarDiferencias();" 
                           onblur="calcularGasto();calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');ajustarDecimalesImportesSolicitud();comprobarDiferencias();">
                </td>
                <td><input id="preparadoresAnexCargaNum" name="preparadoresAnexCargaNum" type="text" size="3" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="preparadoresAnexCargaSol" name="preparadoresAnexCargaSol" type="text" size="10" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td><input id="preparadoresConcNum" name="preparadoresConcNum" type="text" size="3" class="inputTexto textoNumerico"></td>
                <td colspan="2"><input id="preparadoresConcSol" name="preparadoresConcSol" type="text" size="10" class="inputTexto textoNumerico"
                                       onkeyup="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');FormatNumber(this.value, 8, 2,this.id);" 
                                       onblur="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');ajustarDecimalesImportesSolicitud();"></td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.gastosGeneralesAdm")%></td>
                <td></td>
                <td colspan="2" style="border-right: 1px solid #1766A7;">
                    <input id="gastosGeneralesSolic" name="gastosGeneralesSolic" type="text" size="10" class="inputTexto textoNumerico" maxlength="11"                                        
                           onkeyup="calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');FormatNumber(this.value, 8, 2,this.id);comprobarDiferencias();"
                           onblur="calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');ajustarDecimalesImportesSolicitud();comprobarDiferencias();"></td>
                <td></td>
                <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="gastosGeneralesAnexCarga" name="gastosGeneralesAnexCarga" type="text" size="10" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td></td>
                <td colspan="2"><input id="gastosGeneralesConc" name="gastosGeneralesConc" type="text" size="10" class="inputTexto textoNumerico" 
                                       onkeyup="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');FormatNumber(this.value, 8, 2,this.id);" 
                                       onblur="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');ajustarDecimalesImportesSolicitud();"></td>
            </tr>
            <tr>
                <td colspan="4" style="border-right: 1px solid #1766A7;">
                    <div class="separadorTabla"></div>
                </td>
                <td colspan="3" style="border-right: 1px solid #1766A7;">
                    <div class="separadorTabla"></div>
                </td>
                <td colspan="3">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr>
                <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.otrasSubvenciones")%></td>
                <td colspan="3" style="text-align: left; border-right: 1px solid #1766A7;">
                    <table style="width: 100%; font-size: 12px;text-align: center;">
                        <tr>
                            <input type="radio" name="radioOtrasAyudas" id="radioOtrasAyudasS" value="S" onclick="cambioRadioOtrosOrganismos();"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.general.si").toUpperCase()%>
                            <input type="radio" name="radioOtrasAyudas" id="radioOtrasAyudasN" value="N" onclick="cambioRadioOtrosOrganismos();"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.general.no").toUpperCase()%>
                        </tr>
                    </table>
                </td>
                <td colspan="3" style="border-right: 1px solid #1766A7;"></td>
                <td colspan="3"></td>
            </tr>
            <tr id="filaOtrosOrganismos1" class="negrita">
                <td style=" text-align: left;"></td>
                <td></td>
               
                <td colspan="2" style="border-right: 1px solid #1766A7;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.general.importe").toUpperCase()%></td>
                <td colspan="3" style="border-right: 1px solid #1766A7;"></td>
                <td colspan="3"></td>
            </tr>
            <tr id="filaOtrosOrganismos2">
                <td style="text-align: left; padding-left: 45px;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.cantOrgPublicos")%></td>
                <td></td>                
                <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPublicos" name="impOrgPublicos" type="text" size="10" class="inputTexto textoNumerico" maxlength="11" 
                            onkeyup="calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');FormatNumber(this.value, 8, 2,this.id);comprobarDiferencias();" 
                            onblur="calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');ajustarDecimalesImportesSolicitud();comprobarDiferencias();"></td>
                <!--td colspan="3" style="border-right: 1px solid #1766A7;"></td-->
                <td></td>                
                <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPublicosAnexSol" name="impOrgPublicosAnexSol" type="text" size="10" class="inputTexto textoNumerico" maxlength="11" 
                            onkeyup="calcularTotalSubvSolicitud('prospectoresAnexCargaSol', 'preparadoresAnexCargaSol', 'gastosGeneralesAnexCarga', 'totSolCarga', 'impOrgPublicosAnexSol', 'impOrgPrivadosAnexSol');FormatNumber(this.value, 8, 2,this.id);comprobarDiferencias();"                            
                            onblur="calcularTotalSubvSolicitud('prospectoresAnexCargaSol', 'preparadoresAnexCargaSol', 'gastosGeneralesAnexCarga', 'totSolCarga', 'impOrgPublicosAnexSol', 'impOrgPrivadosAnexSol');ajustarDecimalesImportesSolicitud();comprobarDiferencias();"></td>
                <td></td>
                <td colspan="2"><input id="impOrgPublicosConcedidos" name="impOrgPublicosConcedidos" type="text" size="10" class="inputTexto textoNumerico" maxlength="11" 
                            onkeyup="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');FormatNumber(this.value, 8, 2,this.id);" 
                            onblur="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');ajustarDecimalesImportesSolicitud();"></td>
                <td></td>     
            </tr>
            <tr id="filaOtrosOrganismos3">
                <td style="text-align: left; padding-left: 45px;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.cantOrgPrivados")%></td>
                <td></td>                
                <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPrivados" name="impOrgPrivados" type="text" size="10" class="inputTexto textoNumerico" maxlength="11" 
                            onkeyup="calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');FormatNumber(this.value, 8, 2,this.id);comprobarDiferencias();"
                            onblur="calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic', 'totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');ajustarDecimalesImportesSolicitud();comprobarDiferencias();"></td>
                <!--td colspan="3" style="border-right: 1px solid #1766A7;"></td-->
                <td></td>     
                <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPrivadosAnexSol" name="impOrgPrivadosAnexSol" type="text" size="10" class="inputTexto textoNumerico" maxlength="11" 
                            onkeyup="calcularTotalSubvSolicitud('prospectoresAnexCargaSol', 'preparadoresAnexCargaSol', 'gastosGeneralesAnexCarga', 'totSolCarga', 'impOrgPublicosAnexSol', 'impOrgPrivadosAnexSol');FormatNumber(this.value, 8, 2,this.id);comprobarDiferencias();"                            
                            onblur="calcularTotalSubvSolicitud('prospectoresAnexCargaSol', 'preparadoresAnexCargaSol', 'gastosGeneralesAnexCarga', 'totSolCarga', 'impOrgPublicosAnexSol', 'impOrgPrivadosAnexSol');ajustarDecimalesImportesSolicitud();comprobarDiferencias();"></td>
                <td></td>
                <td colspan="2"><input id="impOrgPrivadosConcedidos" name="impOrgPrivadosConcedidos" type="text" size="10" class="inputTexto textoNumerico" maxlength="11" 
                            onkeyup="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');FormatNumber(this.value, 8, 2,this.id);"
                            onblur="calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');ajustarDecimalesImportesSolicitud();"></td>
            </tr>
            <tr>
                <td colspan="4" style="border-right: 1px solid #1766A7;">
                    <div class="separadorTabla"></div>
                </td>
                <td colspan="3" style="border-right: 1px solid #1766A7;">
                    <div class="separadorTabla"></div>
                </td>
                <td colspan="3">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr>
                <td style=" text-align: left;" class="negrita"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.totSubSolicitada")%></td>
                <td ></td>
                <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="totSubSolicitada" name="totSubSolicitada" type="text" size="10" class="inputTexto textoNumerico readOnly" maxlength="11" onchange="FormatNumber(this.value, 8, 2,this.id);" onblur="FormatNumber(this.value, 8, 2,this.id); ajustarDecimalesImportesSolicitud();" disabled="disabled"></td>
                <td></td>
                <td colspan="2"  style="border-right: 1px solid #1766A7;"><input id="totSolCarga" name="totSolCarga" type="text" size="10" class="inputTexto textoNumerico readOnly" disabled="true"></td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td style="text-align: left;" class="negrita"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.totMaxSub")%></td>
                <td colspan="3" style="border-right: 1px solid #1766A7;"></td>                
                <td colspan="3" style="border-right: 1px solid #1766A7;"></td>  
                <td></td>
                <td colspan="2" ><input id="totMaxSub" name="totMaxSub" type="text" size="10" class="inputTexto textoNumerico readOnly" disabled="true"></td>                
                <td></td>
            </tr>
             <tr>
                <td colspan="10">
                    <div class="separadorTabla"></div>
                </td>                
            </tr>
            <tr>
                <td style="text-align: left;" class="negrita"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.totSubAprobada")%></td>
                <td></td>
                <td colspan="2" ><input id="totSubAprobada" name="totSubAprobada" type="text" class="inputTexto textoNumerico" maxlength="11" style="width: 100%;" onkeyup="FormatNumber(this.value, 8, 2,this.id, event);" onchange="FormatNumber(this.value, 8, 2,this.id);" onblur="FormatNumber(this.value, 8, 2,this.id); ajustarDecimalesImportesSolicitud();"></td>
                <td colspan="3" ></td>
                <td colspan="3"></td>
            </tr>
            <tr>
                <td colspan="10">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
        </table> 
            
        <input type="hidden" id="nuevaSolicEca" name="nuevaSolicEca"/>
    </div>
    <div class="botonera" style="height: 50px; padding-top: 20px;">
        <input type="button" id="btnGuardarSolic" name="btnGuardarSolic" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.guardar")%>" onclick="guardarSolicitudEca();">
    </div> 
</body>

<script type="text/javascript">
    <%
        if(ecaSolicitud != null)
        {
    %>      
            document.getElementById('col1SolicH').value = '<%=ecaSolicitud.getInserC1H() != null ? ecaSolicitud.getInserC1H().toString() : ""%>';
            document.getElementById('col1SolicM').value = '<%=ecaSolicitud.getInserC1M() != null ? ecaSolicitud.getInserC1M().toString() : ""%>';
            
            reemplazarPuntosEca(document.getElementById('col1SolicH'));
            reemplazarPuntosEca(document.getElementById('col1SolicM'));
            calcularTotalSolicitud('col1SolicH', 'col1SolicM', 'col1SolicT');
            
            document.getElementById('col2SolicH').value = '<%=ecaSolicitud.getInserC2H() != null ? ecaSolicitud.getInserC2H().toString() : ""%>';
            document.getElementById('col2SolicM').value = '<%=ecaSolicitud.getInserC2M() != null ? ecaSolicitud.getInserC2M().toString() : ""%>';
            reemplazarPuntosEca(document.getElementById('col2SolicH'));
            reemplazarPuntosEca(document.getElementById('col2SolicM'));
            calcularTotalSolicitud('col2SolicH', 'col2SolicM', 'col2SolicT');
            
            document.getElementById('col3SolicH').value = '<%=ecaSolicitud.getInserC3H() != null ? ecaSolicitud.getInserC3H().toString() : ""%>';
            document.getElementById('col3SolicM').value = '<%=ecaSolicitud.getInserC3M() != null ? ecaSolicitud.getInserC3M().toString() : ""%>';
            reemplazarPuntosEca(document.getElementById('col3SolicH'));
            reemplazarPuntosEca(document.getElementById('col3SolicM'));
            calcularTotalSolicitud('col3SolicH', 'col3SolicM', 'col3SolicT');
            
            document.getElementById('col4SolicH').value = '<%=ecaSolicitud.getInserC4H() != null ? ecaSolicitud.getInserC4H().toString() : ""%>';
            document.getElementById('col4SolicM').value = '<%=ecaSolicitud.getInserC4M() != null ? ecaSolicitud.getInserC4M().toString() : ""%>';
            reemplazarPuntosEca(document.getElementById('col4SolicH'));
            reemplazarPuntosEca(document.getElementById('col4SolicM'));
            calcularTotalSolicitud('col4SolicH', 'col4SolicM', 'col4SolicT');
            
            document.getElementById('col5SolicH').value = '<%=ecaSolicitud.getInserC5H() != null ? ecaSolicitud.getInserC5H().toString() : ""%>';
            document.getElementById('col5SolicM').value = '<%=ecaSolicitud.getInserC5M() != null ? ecaSolicitud.getInserC5M().toString() : ""%>';
            reemplazarPuntosEca(document.getElementById('col5SolicH'));
            reemplazarPuntosEca(document.getElementById('col5SolicM'));
            calcularTotalSolicitud('col5SolicH', 'col5SolicM', 'col5SolicT');
            
            document.getElementById('col6SolicH').value = '<%=ecaSolicitud.getInserC6H() != null ? ecaSolicitud.getInserC6H().toString() : ""%>';
            document.getElementById('col6SolicM').value = '<%=ecaSolicitud.getInserC6M() != null ? ecaSolicitud.getInserC6M().toString() : ""%>';
            reemplazarPuntosEca(document.getElementById('col6SolicH'));
            reemplazarPuntosEca(document.getElementById('col6SolicM'));
            calcularTotalSolicitud('col6SolicH', 'col6SolicM', 'col6SolicT');
            
            document.getElementById('seg1SolicH').value = '<%=ecaSolicitud.getSegH() != null ? ecaSolicitud.getSegH().toString() : ""%>';
            document.getElementById('seg1SolicM').value = '<%=ecaSolicitud.getSegM() != null ? ecaSolicitud.getSegM().toString() : ""%>';
            
            calcularTotalSolicitud('seg1SolicH', 'seg1SolicM', 'seg1SolicT');
                     
            document.getElementById('nActuacionesSolic').value = '<%=ecaSolicitud.getSegActuaciones() != null ? formateador.format (ecaSolicitud.getSegActuaciones()):"" %>';
            document.getElementById('prospectoresSolicNum').value = '<%=ecaSolicitud.getProspectoresNum() != null ? ecaSolicitud.getProspectoresNum().toString() : ""%>';            
            document.getElementById('prospectoresSolicSol').value = '<%=ecaSolicitud.getProspectoresImp() != null ? formateador.format (ecaSolicitud.getProspectoresImp()):"" %>';
            document.getElementById('preparadoresSolicNum').value = '<%=ecaSolicitud.getPreparadoresNum() != null ? ecaSolicitud.getPreparadoresNum().toString() : ""%>';            
            document.getElementById('preparadoresSolicSol').value = '<%=ecaSolicitud.getPreparadoresImp() != null ? formateador.format (ecaSolicitud.getPreparadoresImp()):"" %>';            
            document.getElementById('gastosGeneralesSolic').value = '<%=ecaSolicitud.getGastos() != null ? formateador.format (ecaSolicitud.getGastos()):"" %>';
            <%
                if(ecaSolicitud.getOtrasSub() != null && ecaSolicitud.getOtrasSub() == true)
                {
            %>
                document.getElementById('radioOtrasAyudasS').checked = true;
            <%
                }
                else
                {
            %>
                document.getElementById('radioOtrasAyudasN').checked = true;
            <%
                }
            %>            
            document.getElementById('impOrgPublicos').value = '<%=ecaSolicitud.getSubPub() != null ? formateador.format (ecaSolicitud.getSubPub()):"" %>';
            document.getElementById('impOrgPrivados').value = '<%=ecaSolicitud.getSubPriv() != null ? formateador.format (ecaSolicitud.getSubPriv()):"" %>';                        
            document.getElementById('totSubSolicitada').value = '<%=ecaSolicitud.getTotalSubvencion() != null ? formateador.format (ecaSolicitud.getTotalSubvencion()):"" %>';
            calcularTotalSubvSolicitud('prospectoresSolicSol', 'preparadoresSolicSol', 'gastosGeneralesSolic','totSubSolicitada', 'impOrgPublicos', 'impOrgPrivados');            
            document.getElementById('totSubAprobada').value = '<%=ecaSolicitud.getTotalAprobado() != null ? formateador.format (ecaSolicitud.getTotalAprobado()):"" %>';
            
            //columna concedido
            document.getElementById('col1HConc').value = '<%=ecaSolicitud.getInserC1HConc() != null ? ecaSolicitud.getInserC1HConc().floatValue() : ""%>';
            document.getElementById('col1MConc').value = '<%=ecaSolicitud.getInserC1MConc() != null ? ecaSolicitud.getInserC1MConc().floatValue() : ""%>';
            reemplazarPuntosEca(document.getElementById('col1HConc'));
            reemplazarPuntosEca(document.getElementById('col1MConc'));
            calcularTotalSolicitud('col1HConc', 'col1MConc', 'col1TConc');
            
            document.getElementById('col2HConc').value = '<%=ecaSolicitud.getInserC2HConc() != null ? ecaSolicitud.getInserC2HConc().floatValue() : ""%>';
            document.getElementById('col2MConc').value = '<%=ecaSolicitud.getInserC2MConc() != null ? ecaSolicitud.getInserC2MConc().floatValue() : ""%>';
            reemplazarPuntosEca(document.getElementById('col2HConc'));
            reemplazarPuntosEca(document.getElementById('col2MConc'));
            calcularTotalSolicitud('col2HConc', 'col2MConc', 'col2TConc');
            
            document.getElementById('col3HConc').value = '<%=ecaSolicitud.getInserC3HConc() != null ? ecaSolicitud.getInserC3HConc().floatValue() : ""%>';
            document.getElementById('col3MConc').value = '<%=ecaSolicitud.getInserC3MConc() != null ? ecaSolicitud.getInserC3MConc().floatValue() : ""%>';
            reemplazarPuntosEca(document.getElementById('col3HConc'));
            reemplazarPuntosEca(document.getElementById('col3MConc'));
            calcularTotalSolicitud('col3HConc', 'col3MConc', 'col3TConc');
            
            document.getElementById('col4HConc').value = '<%=ecaSolicitud.getInserC4HConc() != null ? ecaSolicitud.getInserC4HConc().floatValue() : ""%>';
            document.getElementById('col4MConc').value = '<%=ecaSolicitud.getInserC4MConc() != null ? ecaSolicitud.getInserC4MConc().floatValue() : ""%>';
            reemplazarPuntosEca(document.getElementById('col4HConc'));
            reemplazarPuntosEca(document.getElementById('col4MConc'));
            calcularTotalSolicitud('col4HConc', 'col4MConc', 'col4TConc');
            
            
            document.getElementById('col5HConc').value = '<%=ecaSolicitud.getInserC5HConc() != null ? ecaSolicitud.getInserC5HConc().floatValue() : ""%>';
            document.getElementById('col5MConc').value = '<%=ecaSolicitud.getInserC5MConc() != null ? ecaSolicitud.getInserC5MConc().floatValue() : ""%>';
            reemplazarPuntosEca(document.getElementById('col5HConc'));
            reemplazarPuntosEca(document.getElementById('col5MConc'));
            calcularTotalSolicitud('col5HConc', 'col5MConc', 'col5TConc');
            
            
            document.getElementById('col6HConc').value = '<%=ecaSolicitud.getInserC6HConc() != null ? ecaSolicitud.getInserC6HConc().floatValue() : ""%>';
            document.getElementById('col6MConc').value = '<%=ecaSolicitud.getInserC6MConc() != null ? ecaSolicitud.getInserC6MConc().floatValue() : ""%>';
            reemplazarPuntosEca(document.getElementById('col6HConc'));
            reemplazarPuntosEca(document.getElementById('col6MConc'));
            calcularTotalSolicitud('col6HConc', 'col6MConc', 'col6TConc');
            
            document.getElementById('nActuacionesConc').value = '<%=ecaSolicitud.getSegActuacionesConc() != null ? ecaSolicitud.getSegActuacionesConc().toPlainString() : ""%>';
            document.getElementById('prospectoresConcNum').value = '<%=ecaSolicitud.getProspectoresNumConc() != null ? ecaSolicitud.getProspectoresNumConc().toString() : ""%>';
            document.getElementById('prospectoresConcSol').value = '<%=ecaSolicitud.getProspectoresImpConc() != null ? formateador.format (ecaSolicitud.getProspectoresImpConc()):"" %>';    
            document.getElementById('preparadoresConcNum').value = '<%=ecaSolicitud.getPreparadoresNumConc() != null ? ecaSolicitud.getPreparadoresNumConc().toString() : ""%>';
            document.getElementById('preparadoresConcSol').value = '<%=ecaSolicitud.getPreparadoresImpConc() != null ? formateador.format (ecaSolicitud.getPreparadoresImpConc()):"" %>';            
            
            document.getElementById('gastosGeneralesConc').value ='<%=ecaSolicitud.getGastosConc() != null ? formateador.format(ecaSolicitud.getGastosConc()):""%>';

            document.getElementById('impOrgPublicosAnexSol').value = '<%=ecaSolicitud.getSubPubCargaManual() != null ? formateador.format (ecaSolicitud.getSubPubCargaManual()):"" %>';
            document.getElementById('impOrgPrivadosAnexSol').value = '<%=ecaSolicitud.getSubPrivCargaManual() != null ? formateador.format (ecaSolicitud.getSubPrivCargaManual()):"" %>';                        

            document.getElementById('impOrgPublicosConcedidos').value = '<%=ecaSolicitud.getSubPubConcedidos() != null ? formateador.format (ecaSolicitud.getSubPubConcedidos()):"" %>';
            document.getElementById('impOrgPrivadosConcedidos').value = '<%=ecaSolicitud.getSubPrivConcedidos() != null ? formateador.format (ecaSolicitud.getSubPrivConcedidos()):"" %>';                        
            calcularTotalSubvSolicitud('prospectoresConcSol', 'preparadoresConcSol', 'gastosGeneralesConc', 'totMaxSub', 'impOrgPublicosConcedidos', 'impOrgPrivadosConcedidos');
            
    <%
        }
        else
        {
    %>
            document.getElementById('radioOtrasAyudasN').checked = true;
    <%
        } 
    
       
    //Columna detalle anexos
         if(datosAnexos != null)
        {
    %>
        document.getElementById('col1AnexCargaH').value = '<%=datosAnexos.getC1H() != null ? datosAnexos.getC1H().toPlainString() : ""%>';
        document.getElementById('col1AnexCargaM').value = '<%=datosAnexos.getC1M() != null ? datosAnexos.getC1M().toPlainString() : ""%>';
        document.getElementById('col1AnexCargaT').value = '<%=datosAnexos.getC1T() != null ? datosAnexos.getC1T().toPlainString() : ""%>';
                
        document.getElementById('col2AnexCargaH').value = '<%=datosAnexos.getC2H() != null ? datosAnexos.getC2H().toPlainString() : ""%>';
        document.getElementById('col2AnexCargaM').value = '<%=datosAnexos.getC2M() != null ? datosAnexos.getC2M().toPlainString() : ""%>';
        document.getElementById('col2AnexCargaT').value = '<%=datosAnexos.getC2T() != null ? datosAnexos.getC2T().toPlainString() : ""%>';
                
        document.getElementById('col3AnexCargaH').value = '<%=datosAnexos.getC3H() != null ? datosAnexos.getC3H().toPlainString() : ""%>';
        document.getElementById('col3AnexCargaM').value = '<%=datosAnexos.getC3M() != null ? datosAnexos.getC3M().toPlainString() : ""%>';
        document.getElementById('col3AnexCargaT').value = '<%=datosAnexos.getC3T() != null ? datosAnexos.getC3T().toPlainString() : ""%>';
         
        document.getElementById('col4AnexCargaH').value = '<%=datosAnexos.getC4H() != null ? datosAnexos.getC4H().toPlainString() : ""%>';
        document.getElementById('col4AnexCargaM').value = '<%=datosAnexos.getC4M() != null ? datosAnexos.getC4M().toPlainString() : ""%>';
        document.getElementById('col4AnexCargaT').value = '<%=datosAnexos.getC4T() != null ? datosAnexos.getC4T().toPlainString() : ""%>';
        
        document.getElementById('col5AnexCargaH').value = '<%=datosAnexos.getC5H() != null ? datosAnexos.getC5H().toPlainString() : ""%>';
        document.getElementById('col5AnexCargaM').value = '<%=datosAnexos.getC5M() != null ? datosAnexos.getC5M().toPlainString() : ""%>';
        document.getElementById('col5AnexCargaT').value = '<%=datosAnexos.getC5T() != null ? datosAnexos.getC5T().toPlainString() : ""%>';
        
        
        document.getElementById('col6AnexCargaH').value = '<%=datosAnexos.getC6H() != null ? datosAnexos.getC6H().toPlainString() : ""%>';
        document.getElementById('col6AnexCargaM').value = '<%=datosAnexos.getC6M() != null ? datosAnexos.getC6M().toPlainString() : ""%>';
        document.getElementById('col6AnexCargaT').value = '<%=datosAnexos.getC6T() != null ? datosAnexos.getC6T().toPlainString() : ""%>';
        
        
        
        reemplazarPuntosEca(document.getElementById('col1AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col1AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col1AnexCargaT'));
        reemplazarPuntosEca(document.getElementById('col2AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col2AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col2AnexCargaT'));
        reemplazarPuntosEca(document.getElementById('col3AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col3AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col3AnexCargaT'));
        reemplazarPuntosEca(document.getElementById('col4AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col4AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col4AnexCargaT'));
        
         reemplazarPuntosEca(document.getElementById('col5AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col5AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col5AnexCargaT'));
        
        
         reemplazarPuntosEca(document.getElementById('col6AnexCargaH'));
        reemplazarPuntosEca(document.getElementById('col6AnexCargaM'));
        reemplazarPuntosEca(document.getElementById('col6AnexCargaT'));
        
        
        document.getElementById('nActuacionesAnexCarga').value = '<%=datosAnexos.getSeguimientosAnt() != null ? datosAnexos.getSeguimientosAnt().toPlainString() : ""%>';
        document.getElementById('prospectoresAnexCargaNum').value = '<%=datosAnexos.getNumProspectores() != null ? datosAnexos.getNumProspectores().toPlainString() : ""%>';        
        document.getElementById('prospectoresAnexCargaSol').value = '<%=datosAnexos.getImporteProspectores() != null ? formateador.format (datosAnexos.getImporteProspectores()):"" %>';    
        
        document.getElementById('preparadoresAnexCargaNum').value = '<%=datosAnexos.getNumPreparadores() != null ? datosAnexos.getNumPreparadores().toPlainString() : ""%>';
        document.getElementById('preparadoresAnexCargaSol').value = '<%=datosAnexos.getImportePreparadores() != null ? formateador.format (datosAnexos.getImportePreparadores()):"" %>';            
        document.getElementById('gastosGeneralesAnexCarga').value = '<%=datosAnexos.getGastos() != null ? datosAnexos.getGastos().toPlainString() : ""%>';
        document.getElementById('gastosGeneralesAnexCarga').value = '<%=datosAnexos.getGastos() != null ? formateador.format (datosAnexos.getGastos()):"" %>';            
       
    <%
        } 
    %> 
            
                    
        var totSolCarga;
        <%
            if(datosAnexos != null && datosAnexos.getImporteProspectores() != null)
            {
        %>
                if(!totSolCarga){
                    totSolCarga = 0.0;
                }
                totSolCarga += parseFloat(convertirANumero(document.getElementById('prospectoresAnexCargaSol').value));
        <%
            }
        %>   
        <%
            if(datosAnexos != null && datosAnexos.getImportePreparadores() != null)
            {
        %>
                if(!totSolCarga){
                    totSolCarga = 0.0;
                }
                totSolCarga += parseFloat(convertirANumero(document.getElementById('preparadoresAnexCargaSol').value));
        <%
            }
        %>   
            
        <% 
            if(datosAnexos != null && datosAnexos.getGastos() != null)
            {
        %>
                if(!totSolCarga){
                    totSolCarga = 0.0;
                }
                totSolCarga += parseFloat(convertirANumero(document.getElementById('gastosGeneralesAnexCarga').value));
        <%
            }
        %>   
        <%
            if(ecaSolicitud != null && ecaSolicitud.getSubPub() != null)
            {
        %>
                if(!totSolCarga){
                    totSolCarga = 0.0;
                }
                //totSolCarga -= parseFloat(convertirANumero(document.getElementById('impOrgPublicos').value));
                if (document.getElementById('impOrgPrivadosAnexSol').value!='')
                    totSolCarga -= parseFloat(convertirANumero(document.getElementById('impOrgPublicosAnexSol').value));
        <%
            }
        %>   
        <%
            if(ecaSolicitud != null && ecaSolicitud.getSubPriv() != null )
            {
        %>
                if(!totSolCarga){
                    totSolCarga = 0.0;
                }
                //totSolCarga -= parseFloat(convertirANumero(document.getElementById('impOrgPrivados').value));
                if (document.getElementById('impOrgPrivadosAnexSol').value!='')
                    totSolCarga -= parseFloat(convertirANumero(document.getElementById('impOrgPrivadosAnexSol').value));
        <%
            }
        %>   
            
            if(totSolCarga!= undefined){
                document.getElementById('totSolCarga').value = ''+totSolCarga;
            }
            
        reemplazarPuntosEca(document.getElementById('totSolCarga'));
   
        cambioRadioOtrosOrganismos();
        ajustarDecimalesImportesSolicitud();
        comprobarDiferencias();
            
        activarModoConsulta();
</script>
