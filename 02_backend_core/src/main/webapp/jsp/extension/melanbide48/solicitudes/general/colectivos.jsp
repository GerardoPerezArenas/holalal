<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.MeLanbideConvocatorias" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils" %>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
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
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    ColecSolicitudVO solicitud = (ColecSolicitudVO)request.getAttribute("solicitudVO");
    List<ColecSolicitudVO> listaSolicitudVO = (List<ColecSolicitudVO>)request.getAttribute("listaSolicitudVO");
    int ejercicioExpediente = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
    Gson gson = new Gson();
    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    gsonB.serializeNulls();  
    gson=gsonB.create();
    String listaResponseJsonString = gson.toJson(listaSolicitudVO);
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>


<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/TablaNuevaColec.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionColectivoTHSolicitado.js"></script>

<script type="text/javascript"> 
    function guardarSolicitud(){
        //document.getElementById('msgGuardandoDatos').style.display="inline";
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        var ajax = getXMLHttpRequest();
        var parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=guardarSolicitud&tipo=0'
            +'&idSolicitud='+document.getElementById('idSolicitud').value
            +'&numero=<%=numExpediente%>'
            +'&col1Araba='+(document.getElementById('chkArabaColec1').checked ? '1' : '0')
            +'&col1Gipuzkoa='+(document.getElementById('chkGipuzkoaColec1').checked ? '1' : '0')
            +'&col1Bizkaia='+(document.getElementById('chkBizkaiaColec1').checked ? '1' : '0')
            +'&col2Araba='+(document.getElementById('chkArabaColec2').checked ? '1' : '0')
            +'&col2Gipuzkoa='+(document.getElementById('chkGipuzkoaColec2').checked ? '1' : '0')
            +'&col2Bizkaia='+(document.getElementById('chkBizkaiaColec2').checked ? '1' : '0')
            +'&col3Araba='+(document.getElementById('chkArabaColec3').checked ? '1' : '0')
            +'&col3Gipuzkoa='+(document.getElementById('chkGipuzkoaColec3').checked ? '1' : '0')
            +'&col3Bizkaia='+(document.getElementById('chkBizkaiaColec3').checked ? '1' : '0')
            +'&col4Araba='+(document.getElementById('chkArabaColec4').checked ? '1' : '0')
            +'&col4Gipuzkoa='+(document.getElementById('chkGipuzkoaColec4').checked ? '1' : '0')
            +'&col4Bizkaia='+(document.getElementById('chkBizkaiaColec4').checked ? '1' : '0')
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
            var nodos = xmlDoc.getElementsByTagName("RESPUESTA");
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var nodoSolicitud;
            var hijosSolicitud;
            var fila = new Array();
            var nodoCampo;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                }else if(hijos[j].nodeName=="SOLICITUD"){
                    nodoSolicitud = hijos[j];
                    hijosSolicitud = nodoSolicitud.childNodes;
                    for(var cont = 0; cont < hijosSolicitud.length; cont++){
                        if(hijosSolicitud[cont].nodeName=="ID_SOLICITUD"){
                            nodoCampo = hijosSolicitud[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                    }
                    recargarPestanaGeneral(fila);
                }
            }
            if(codigoOperacion=="0"){
                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
            }else if(codigoOperacion=="1"){
                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
            }else if(codigoOperacion=="2"){
                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }else if(codigoOperacion=="3"){
                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
            }else if(codigoOperacion=="4"){
                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
            }else{
                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }
        }catch(err){
            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }
    }
    
    function recargarPestanaGeneral(fila){
        if(fila != null && fila.length > 0){
            document.getElementById('idSolicitud').value = fila[0];
        }
    }

</script>

<div>
        <div id="divColecTHSolicitadoConvAntes2021">
            <div align="center" style="padding-top: 100px;">
                <table class="tablaNormal" style="width: 1000px;">
                    <tr>
                        <th style="width: 55%; font-size: 14px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.general.tabla.colColectivos")%>
                        </th>
                        <th style="width: 15%; font-size: 14px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.general.tabla.colAraba")%>
                        </th>
                        <th style="width: 15%; font-size: 14px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.general.tabla.colGipuzkoa")%>
                        </th>
                        <th style="width: 15%; font-size: 14px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.general.tabla.colBizkaia")%>
                        </th>
                    </tr>
                    <tr>
                        <td style="text-align: left">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.general.tabla.colectivo1")%>
                        </td>
                        <td>
                            <input type="checkbox" id="chkArabaColec1" name="chkArabaColec1"/>
                        </td>
                        <td>
                            <input type="checkbox" id="chkGipuzkoaColec1" name="chkGipuzkoaColec1"/>
                        </td>
                        <td>
                            <input type="checkbox" id="chkBizkaiaColec1" name="chkBizkaiaColec1"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: left">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.general.tabla.colectivo2")%>
                        </td>
                        <td>
                            <input type="checkbox" id="chkArabaColec2" name="chkArabaColec2"/>
                        </td>
                        <td>
                            <input type="checkbox" id="chkGipuzkoaColec2" name="chkGipuzkoaColec2"/>
                        </td>
                        <td>
                            <input type="checkbox" id="chkBizkaiaColec2" name="chkBizkaiaColec2"/>
                        </td>
                    </tr>
                    <tr >
                        <td style="text-align: left">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.general.tabla.colectivo3")%>
                        </td>
                        <td>
                            <input type="checkbox" id="chkArabaColec3" name="chkArabaColec3"/>
                        </td>
                        <td>
                            <input type="checkbox" id="chkGipuzkoaColec3" name="chkGipuzkoaColec3"/>
                        </td>
                        <td>
                            <input type="checkbox" id="chkBizkaiaColec3" name="chkBizkaiaColec3"/>
                        </td>
                    </tr>
                    <tr >
                        <td style="text-align: left">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.general.tabla.colectivo4")%>
                        </td>
                        <td>
                            <input type="checkbox" id="chkArabaColec4" name="chkArabaColec4"/>
                        </td>
                        <td>
                            <input type="checkbox" id="chkGipuzkoaColec4" name="chkGipuzkoaColec4"/>
                        </td>
                        <td>
                            <input type="checkbox" id="chkBizkaiaColec4" name="chkBizkaiaColec4"/>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="botonera">
                <input type="button" id="btnGuardarGeneral" name="btnGuardarGeneral" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarSolicitud();">
            </div>
        </div>
        
        <div id="divColecTHSolicitado">
            <div style="clear: both;">
                <div id="listaColecTHSolicitado" name="listaColecTHSolicitado" style="font-size: 12px; overflow-y: scroll;height: 300px;"></div>
            </div>
            <div class="botonera">
                <input type="button" id="botonAnadirColecTHSolicitado" name="botonAnadirColecTHSolicitado" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirColecTHSolicitado();"/>
                <input type="button" id="botonModificarColecTHSolicitado" name="botonModificarColecTHSolicitado" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTHSolicitado();"/>
                <input type="button" id="botonEliminarColecTHSolicitado" name="botonEliminarColecTHSolicitado" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTHSolicitado();"/>
            </div>
        </div>
        
        <input type="hidden" id="idSolicitud" name="idSolicitud"/>
        <input type="hidden" id="listaColectivosSolicitadosTH" name="listaColectivosSolicitadosTH"/>
        <script>document.getElementById("listaColectivosSolicitadosTH").value=JSON.stringify(<%=listaResponseJsonString%>,function(key, value) { return value == null ? "" : value });</script>
        <!-- Literales paracabecera de tabla-->
        <input type="hidden" id="listaColecTHSolicitado_textoColumna2" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"lable.tabla.colectivo.solicitado.th.col2")%>"/>
        <input type="hidden" id="listaColecTHSolicitado_textoColumna3" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"lable.tabla.colectivo.solicitado.th.col3")%>"/>
        <input type="hidden" id="listaColecTHSolicitado_textoColumna4" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"lable.tabla.colectivo.solicitado.th.col4")%>"/>
        <input type="hidden" id="listaColecTHSolicitado_textoColumna5" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"lable.tabla.colectivo.solicitado.th.col5")%>"/>
</div>
