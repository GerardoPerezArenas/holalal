<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.i18n.MeLanbide34I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.vo.ValoresPago" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConstantesMeLanbide34" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.vo.SelectItem" %>
<%@page import="java.util.*" %>
<%@page import="java.math.*" %>
<%@page import="java.text.*"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null) 
        {
            if (usuario != null) 
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide34I18n meLanbide34I18n = MeLanbide34I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    ValoresPago valoresPago = (ValoresPago)request.getAttribute("valoresPago");
    String mensaje = (String)request.getAttribute("mensaje");
    boolean deshabilitado = (Boolean)request.getAttribute("deshabilitado");
    
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
      
    //LISTA ESTUDIO VIABLE
    List<SelectItem> listaViable = new ArrayList<SelectItem>();
    if(request.getAttribute("lstViable") != null)
        listaViable = (List<SelectItem>)request.getAttribute("lstViable");
    String lcodViable = "";
    String ldescViable = "";
    if (listaViable != null && listaViable.size() > 0) 
    {
        int i;
        SelectItem ts = null;
        
        for (i = 0; i < listaViable.size() - 1; i++) 
        {
            ts = (SelectItem) listaViable.get(i);
            lcodViable += "\"" + ts.getId().toString() + "\",";
            ldescViable += "\"" + ts.getLabel() + "\",";
        }
        ts = (SelectItem) listaViable.get(i);
        lcodViable += "\"" + ts.getId().toString() + "\"";
        ldescViable += "\"" + ts.getLabel() + "\"";        
    }
    
     String codTram = request.getParameter("codigoTramite");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide34/melanbide34.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide34/utils.js"/>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
    var msgValidacion = '';
    
    var comboViable;    
    var codViable = [<%=lcodViable%>];
    var descViable = [<%=ldescViable%>];
   
   function mostrarCalFechaPrimerPago(evento) {
        if(window.event) 
            evento = window.event;
        if (document.getElementById('calfecPago').src.indexOf('icono.gif') != -1 )
            showCalendar('forms[0]','fecPago',null,null,null,'','calfecPago','',null,null,null,null,null,null,null,null,evento);
    }
    
	function guardarDatosPago(){
            document.getElementById('lblMensaje').innerHTML ='';
        if(validarImportePago(false,false)){
            actualizarCamposSuplementarios();
            
            
            var control = new Date();
            var parametros = 'tarea=preparar&modulo=MELANBIDE34&operacion=guardarPago&tipo=0&numero=<%=numExpediente%>'
                                +'&impAPagar='+convertirANumero(document.getElementById('impAPagar').value)
                                +'&descuento='+convertirANumero(document.getElementById('descuento').value)
                                +'&fecPago='+document.getElementById('fecPago').value
                                +'&viable='+document.getElementById('codViable').value
                                +'&pago=2'
                                +'&control='+control.getTime();
            realizarLlamada(parametros);
        }else{
            jsp_alerta("A", msgValidacion);
            document.getElementById('lblMensaje').innerHTML =msgValidacion;
            document.getElementById('lblMensaje').className = "textoRojo";
        }
    }
	
	function validarImportePago(mostrarMensaje, recalcular){
        try{
            var correcto = true;
            if(mostrarMensaje){
                msgValidacion = '';
            }
            
            //var concedida = parseFloat(document.getElementById('concedida').value);
            //reemplazarPuntosEca(document.getElementById('concedida'));
            FormatNumber(document.getElementById('impAPagar').value, 8, 2, 'impAPagar');            
            var concedida = parseFloat(convertirANumero(document.getElementById('impPago').value));
            var pagar = parseFloat(convertirANumero(document.getElementById('impAPagar').value));
            if(pagar < 0){
                if(msgValidacion == '')
                    msgValidacion = '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.error.pagarPositivo")%>';
                document.getElementById('impAPagar').style.border = '1px solid red';
                correcto = false;
            }else if(pagar > concedida){
                if(msgValidacion == '')
                    msgValidacion = '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.error.pagarMenorConcedida")%>';
                document.getElementById('impAPagar').style.border = '1px solid red';
                correcto = false;
            }else{
                document.getElementById('impAPagar').removeAttribute("style");
                document.getElementById('impAPagar').style.textAlign = 'right';
                if(recalcular){
                    recalcularDescuento();
                }
            }
            if(mostrarMensaje){
                if(!correcto){
                    jsp_alerta("A", msgValidacion);
                    document.getElementById('lblMensaje').innerHTML =msgValidacion;
                    document.getElementById('lblMensaje').className = "textoRojo";
                }else {document.getElementById('lblMensaje').innerHTML ='';}
            }
            return correcto;
        }catch(err){
            return false;
        }
    }
	
	function recalcularDescuento(){
		 var concedida = parseFloat(convertirANumero(document.getElementById('impPago').value));
         var pagar = parseFloat(convertirANumero(document.getElementById('impAPagar').value));
		 var descuento = concedida - pagar;
         document.getElementById('descuento').value = descuento.toFixed(2).replace(/\./g,',');           
         FormatNumber(document.getElementById('descuento').value, 8, 2, 'descuento');
    }
	
    function actualizarCamposSuplementarios(){
          var campoFecPago= 'T_<%=codTram%>_1_FECHAPAGO2';
        document.getElementById(campoFecPago).value = document.getElementById('fecPago').value;             
    } 
	
	 function realizarLlamada(parametros){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        try{
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
            var nodoValores;
            var hijosValores;
            for(var j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="VALORES"){
                    nodoValores = hijos[j];
                    hijosValores = nodoValores.childNodes;
                    /*for(var cont = 0; cont < hijosValores.length; cont++){
                        if(hijosValores[cont].nodeName=="IMPAPAGAR"){
                            if(hijosValores[cont].childNodes.length > 0){
                                impPagar = (hijosValores[cont].childNodes[0].nodeValue).replace(/\./g,',');
                                document.getElementById('impAPagar').value = impPagar;
                                FormatNumber(document.getElementById('impAPagar').value, 8, 2, 'impAPagar');
                            }
                            else{
                                document.getElementById('impAPagar').value = '';
                            }
                        }                        
                        else if(hijosValores[cont].nodeName=="CONCEDIDOPAGO"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('impPago').value = hijosValores[cont].childNodes[0].nodeValue.replace(/\./g,',');
                                FormatNumber(document.getElementById('impPago').value, 8, 2, 'impPago');
                            }
                            else{
                                document.getElementById('impPago').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="DESCUENTO"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('descuento').value = hijosValores[cont].childNodes[0].nodeValue.replace(/\./g,',');
                                FormatNumber(document.getElementById('descuento').value, 8, 2, 'descuento');
                            }
                            else{
                                document.getElementById('descuento').value = '';
                            }
                        }                        
                        else if(hijosValores[cont].nodeName=="FPAGO"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('fecPago').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('fecPago').value = '';
                            }
                        }                        
                    }*/
                }  
                else if(hijos[j].nodeName=="MENSAJE"){
                    nodoValores = hijos[j];
                    hijosValores = nodoValores.childNodes;
                    document.getElementById('lblMensaje').innerHTML = hijosValores[0].nodeValue;
                }
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            if(codigoOperacion=="0"){
                document.getElementById('lblMensaje').className = "textoAzul";
            }else if(codigoOperacion=="1"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else if(codigoOperacion=="2"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else if(codigoOperacion=="3"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else{
                    
            }//if(
            
            deshabilitarCampos();
        }
        catch(Err){

        }//try-catch
    }
    
    function cargarCombos(){
        comboViable.addItems(codViable, descViable);       
    }
    
    function cargarDescripcionesCombos(){
        var desc = "";
        var codAct = "";
        var codigo = "";

        //ESTUDIO VIABLE
        codigo = '<%=valoresPago != null && valoresPago.getViable() != null ? valoresPago.getViable() : ""%>';
        desc = '';
        var encontrado = false;
        var i = 0; 
        if(codigo != null && codigo != '')
        {
            while(i<codViable.length && !encontrado)
            {
                codAct = codViable[i];
                if(codAct == codigo)
                {
                    encontrado = true;
                    desc = descViable[i];
                }else{
                    i++;
                }
            }
        }
        document.getElementById('descViable').value = desc;  
    }
    
    
	
</script>
<body >
    <div class="tab-page" id="tabPage341" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana341"><%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.tituloPestanaPago2")%></h2>
        <script type="text/javascript">tp1_p341 = tp1.addTabPage( document.getElementById( "tabPage341" ) );</script>
        <div style="clear: both;">
            
            <div id="divGeneral" style="overflow-y: auto; overflow-x: hidden; height: 380px; padding: 20px;">
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.fecSegundoPago")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="fecPago" name="fecPago" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="10" maxlength="2" value="<%=valoresPago != null && valoresPago.getFecPago() != null ? valoresPago.getFecPago() : ""%>" <%=deshabilitado ? "readOnly" : ""%>/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaPrimerPago(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfecPago" name="calfecPago" alt="<%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.fecPrimerPago")%>" src="<%=request.getContextPath()%>/images/calendario/icono.gif"/>
                            </A>
                        </div>
                    </div>
                </div>  
                <div style="clear:both"/>             
                <div class="lineaFormulario">
                    <div style="float: left">
                         <div style="width: 170px; float: left; text-align: left;">
                             <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.impPago2")%>
                         </div>
                         <div style="width: 150px; float: left;">
                             <div style="float: left;">
                                 <input id="impPago" name="impPago" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresPago != null && valoresPago.getImporteConcedidoPago() != null ? formateador.format(Float.valueOf(valoresPago.getImporteConcedidoPago())) : ""%>" readOnly/>
                             </div>
                         </div>
                     </div>
                </div>       
                <div style="clear:both"/>                          
                <div class="lineaFormulario" >
                  <div style="float: left;">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.impPagar2")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="impAPagar" name="impAPagar" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="20" maxlength="11" value="<%=valoresPago != null && valoresPago.getImporteAPagar() != null ? formateador.format(Float.valueOf(valoresPago.getImporteAPagar())): ""%>" 
                                   <%=deshabilitado ? "readOnly" : ""%> onchange="validarImportePago(true, true)"/>
                        </div>
                    </div>
                  </div>
                </div>            
                <div style="clear:both"/>                 
                <div class="lineaFormulario">
                    <div style="float: left">
                         <div style="width: 170px; float: left; text-align: left;">
                             <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.descuento")%>
                         </div>
                         <div style="width: 150px; float: left;">
                             <div style="float: left;">
                                 <input id="descuento" name="descuento" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresPago != null && valoresPago.getDescuento() != null ? formateador.format(Float.valueOf(valoresPago.getDescuento())) : ""%>" readOnly/>
                             </div>
                         </div>
                     </div>
                </div>  
                <div style="clear:both"/>                   
                <div class="lineaFormulario">
                <div style="float:left;"> 
                       <div style="width: 162px; float: left;text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.viable")%>
                       </div>
                       
                   <div style="width: 130px; float: left;">                    
                        <div style="width: 220px; float: left;">
                            <input id="codViable" name="codViable" type="text" class="inputTexto" size="2" maxlength="3" 
                                   value="">
                            <input id="descViable" name="descViable" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorViable" name="anchorViable" href="">
                             <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonSector" name="botonSector" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"
                                  style="<%=deshabilitado ? "display:none" : ""%>"></a>
                        </div>
                    </div>
                                             
                </div>
                </div>
                <div style="clear:both"/>              
               
                <div class="lineaFormulario" style="color: red; padding-top: 50px;">  
                    <b>
                        <label id="lblMensaje"><%=mensaje%></label>
                    </b>
                </div>
            </div>
            <div class="botonera" style="padding-top: 20px; text-align: center; padding-top: 50px;">
                <input type="button" id="btnGuardarPago" name="btnGuardarPago" class="botonGeneral" value="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "btn.guardar")%>" onclick="guardarDatosPago();">
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
        document.getElementById('codViable').value = '<%=valoresPago.getViable() != null ? valoresPago.getViable() : "" %>';        
        comboViable = new Combo('Viable');       
        cargarCombos();
        cargarDescripcionesCombos();
</script>

