<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide37.i18n.MeLanbide37I18n" %>
<%@page import="java.util.List" %>

<%
    int idiomaUsuario = 0;
    int codOrganizacion = 0;
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null) 
        {
            if (usuario != null) 
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    MeLanbide37I18n meLanbide37I18n = MeLanbide37I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide39/melanbide39.css"/>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide39/ecaUtils.js"></script>
<script type="text/javascript">
    var msgValidacion = '';
            
    function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp","Microsoft.XMLHttp"
            ];

        if (window.XMLHttpRequest){
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
        }else if (window.ActiveXObject){
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                }catch (error) {
                //no necesitamos hacer nada especial
                }
            }
        }else{
            return null;
        }
    }
    
    function inicializar(){
        window.focus();
    }
    
    function ejecutarListadoCP(){        
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE37&operacion=cargarCriteriosFiltroListadoCP&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),330,840,'no','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE37&operacion=cargarCriteriosFiltroListadoCP&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,870,'no','no');
        }
        if (result != undefined){
            if(result[0] == '0'){
                //recargarCriteriosListadoCP(result);
                var fila;
                for(var i = 1;i< result.length; i++){
                   fila = result[i];
                   var tipoacred =fila[0];
                   var valoracion =fila[1];
                   var codigoCP =fila[2];
                   var fecdesde =fila[3];
                   var fechasta =fila[4];
                   //alert(tipoacred);
                   
                }//for
                
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = '?tarea=preparar&modulo=MELANBIDE37&operacion=generarListadoCP&tipo=0&tipoacred='+tipoacred
                    +'&valoracion='+valoracion+'&codigoCP='+codigoCP
                    +'&fecSoliDesde='+fecdesde+'&fecSoliHasta='+fechasta+'&control='+control.getTime();
                window.open(url+parametros, "_blank");
            }
        }
                
    }
    
    function ejecutarListadoComprobacionCP(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE37&operacion=cargarCriteriosFiltroListadoCompCP&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),330,840,'no','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE37&operacion=cargarCriteriosFiltroListadoCompCP&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,870,'no','no');
        }
        if (result != undefined){
            for(var i = 0;i< result.length; i++){
               var codigoCP =result[0];
               var fecdesde =result[1];
               var fechasta =result[2];               
            }//for
                
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = '?tarea=preparar&modulo=MELANBIDE37&operacion=generarListadoComprobacionCP&tipo=0'
                +'&codigoCP='+codigoCP
                +'&fecSoliDesde='+fecdesde+'&fecSoliHasta='+fechasta+'&control='+control.getTime();
            window.open(url+parametros, "_blank");
           
        }
    }
    
    function ejecutarListadoComprobacionAPA(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE37&operacion=cargarCriteriosFiltroListadoCompCP&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),330,840,'no','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE37&operacion=cargarCriteriosFiltroListadoCompCP&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,870,'no','no');
        }
        if (result != undefined){
            for(var i = 0;i< result.length; i++){
               var codigoCP =result[0];
               var fecdesde =result[1];
               var fechasta =result[2];               
            }//for
                
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = '?tarea=preparar&modulo=MELANBIDE37&operacion=generarListadoComprobacionAPA&tipo=0'
                +'&codigoCP='+codigoCP
                +'&fecSoliDesde='+fecdesde+'&fecSoliHasta='+fechasta+'&control='+control.getTime();
            window.open(url+parametros, "_blank");
           
        }
    }
    // Actualzar vista materializada
    function ejecutarActulizacionDatosUC(){
        var ajax = getXMLHttpRequest();
        var nodos = "";
        if(ajax!=null){
            var control = new Date();    
            var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
            var parametros = '?tarea=preparar&modulo=MELANBIDE37&operacion=actualizaDatosTodasUC_VistaMate&tipo=0&control='+control.getTime();
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            
            try{
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
                var descripcionOperacion = null;
                var fechaOperacion = null;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                    if(hijos[j].nodeName=="DESCRIPCION_OPERACION"){                            
                        descripcionOperacion = hijos[j].childNodes[0].nodeValue;
                    }
                    if(hijos[j].nodeName=="FECHA_OPERACION"){                            
                        fechaOperacion = hijos[j].childNodes[0].nodeValue;
                    }
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                
                if(codigoOperacion=="0"){
                    jsp_alerta("A",descripcionOperacion + ". A " + fechaOperacion);
                    //alert(descripcionOperacion + ". A " + fechaOperacion);
                }else{
                    //alert("Error al Actualizar los datos. " + codigoOperacion + " - " + descripcionOperacion);
                    jsp_alerta("A","Error al Actualizar los datos. " 
                            + codigoOperacion 
                            + " : " + descripcionOperacion);
                }
                /*}else if(codigoOperacion == "2"){
                    alert(codigoOperacion + ". A " + fechaOperacion);
                }else if(codigoOperacion == "3"){
                    alert(codigoOperacion + ". A " + fechaOperacion);
                }else if(codigoOperacion == "4"){
                    alert(codigoOperacion + ". A " + fechaOperacion);
                }//if(codigoOperacion)*/
            }catch(Err){
                jsp_alerta("A","Error General en java script -  Descripcion: " + Err.description);
            }//try-catch
        }//if(ajax!=null)
    }
    
    function ejecutarListadoCEPAP(){
        var control = new Date();
        var result = null;
                
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = '?tarea=preparar&modulo=MELANBIDE37&operacion=listadoRelaciones&tipo=0'
                +'&control='+control.getTime();
            window.open(url+parametros, "_blank");
    }
    
    function ejecutarListadoExencionesEMPNL(){
        var control = new Date();
        var result = null;
                
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = '?tarea=preparar&modulo=MELANBIDE37&operacion=listadoExpedientesEMPNL&tipo=0'
                +'&control='+control.getTime();
            window.open(url+parametros, "_blank");
    }
    
    function solicitarTipoAcreditacion(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide37/procesos/solicitarTipoAcreditacion.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),120,200,'no','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide37/procesos/solicitarTipoAcreditacion.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),120,250,'no','no');
        }
        return result;
    }
</script>
<body class="bandaBody" onload="javascript:{if (window.top.principal.frames[0]&&window.top.principal.frames['menu'].Go)
        window.top.principal.frames['menu'].Go();inicializar();}">
    <form id="formProcesos">
        <div style="height:550px; width: 100%;">
            <table width="100%" style="height: 550px;" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td style="width:100%;  height:30px; padding-left: 15px" class="txttitblanco"><%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.label.tit_procesos")%></td>
                </tr>
                <tr>
                    <td class="separadorTituloPantalla"></td>
                </tr>
                <tr>
                    <td class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 10px;">
                        <div id="contenidoProc" class="cuadroFondoBlanco" style="width:970px; height: 550px; overflow-x: auto; overflow-y: auto; padding: 10px;margin:0px;margin-top:0px;" align="center">
                            <fieldset style="width: 47%; float: left; padding-left: 10px; padding-right: 10px;">
                                <legend class="legendAzul" align="center"><%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.legend.listados")%></legend>
                                <div class="lineaFormulario">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.label.ListadoCP")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnListadoCP" name="btnListadoCP" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoCP();">
                                        </div>
                                    </div>
                                </div>
                               <!--div class="lineaFormulario">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.label.ListadoComprobacionCP")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnListadoComprobacionCP" name="btnListadoComprobacionCP" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoComprobacionCP();">
                                        </div>
                                    </div>
                                </div-->
                                <div style="clear: both;"></div><br/>
                               <div class="lineaFormulario">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.label.ListadoUCs")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnListadoComprobacionAPA" name="btnListadoComprobacionAPA" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoComprobacionAPA();">
                                        </div>
                                    </div>
                                <!--                                        
                                    <div class="lineaFormulario">
                                        <div style="width: 63%; float: left; text-align: left;">
                                            <%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.label.ListadoUCs.actualizaDatos")%>
                                        </div>
                                        <div style="width: auto; float: left;">
                                            <div style="float: left; margin-top: 2px;">
                                                <input type="button" id="btnListadoActualizaDatosUC" name="btnListadoActualizaDatosUC" class="botonLargo" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.label.ListadoUCs.actualizaDatos")%>" onclick="ejecutarActulizacionDatosUC();">
                                            </div>
                                        </div>
                                    </div>    
                                -->
                                </div>
                               <div style="clear: both;"></div><br/>
                               <div class="lineaFormulario">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.label.ListadoCEPAP")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnListadoCEPAP" name="btnListadoCEPAP" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoCEPAP();">
                                        </div>
                                    </div>
                                </div>
                                <div style="clear: both;"></div><br/>
                                <div class="lineaFormulario">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.label.ListadoEMPNL")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnListadoExencionesEMPNL" name="btnListadoExencionesEMPNL" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoExencionesEMPNL();">
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</body>