<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide33.i18n.MeLanbide33I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide33.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide33.util.ConstantesMeLanbide33" %>

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
    MeLanbide33I18n meLanbide33I18n = MeLanbide33I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String urlConceptos = (String)request.getAttribute("urlConceptos");
    String urlETecnico = (String)request.getAttribute("urlETecnico");
    String urlPagos = (String)request.getAttribute("urlPagos");
        
    boolean btnConceptosVisible = false;
    boolean btnETecnicoVisible = false;
    boolean btnPagosVisible = false;
    
    try
    {
        if(request.getAttribute("tramConceptos") != null)
        {
            btnConceptosVisible = (Boolean)request.getAttribute("tramConceptos");
        }
        if(request.getAttribute("tramETecnico") != null)
        {
            btnETecnicoVisible = (Boolean)request.getAttribute("tramETecnico");
        }
        if(request.getAttribute("tramPagos") != null)
        {
            btnPagosVisible = (Boolean)request.getAttribute("tramPagos");
        }
    }
    catch(Exception ex)
    {
        
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide33/melanbide33.css"/>



<script type="text/javascript">
    var child;
    var timer;
    
    function pulsarAbrirConceptos(){
        barraProgresoAbrirURL('on', 'barraProgresoLanzarAplicacion');
        var width = getWindowWidth();
        var height = getWindowHeight();

        var control = new Date();
        var result = null;
        barraProgresoAbrirURL('off', 'barraProgresoLanzarAplicacion');
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=urlConceptos+"?num="+numExpediente+"&entorno="+codOrganizacion%>&control='+control.getTime(),height,width,'si','si');
        }else{
            result = lanzarPopUpModal('<%=urlConceptos+"?num="+numExpediente+"&entorno="+codOrganizacion%>&control='+control.getTime(),height,width,'si','si');
        }
        /*if (result != undefined){
            if(result[0] == '0'){
                recargarTablaUbicacionesCE(result);
            }
        }*/
    }
    
    function pulsarAbrirETecnico(){
        barraProgresoAbrirURL('on', 'barraProgresoLanzarAplicacion');
        var width = getWindowWidth();
        var height = getWindowHeight();

        var control = new Date();
        var result = null;
        barraProgresoAbrirURL('off', 'barraProgresoLanzarAplicacion');
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=urlETecnico+"?num="+numExpediente+"&entorno="+codOrganizacion%>&control='+control.getTime(),height,width,'si','si');
        }else{
            result = lanzarPopUpModal('<%=urlETecnico+"?num="+numExpediente+"&entorno="+codOrganizacion%>&control='+control.getTime(),height,width,'si','si');
        }
    }
    
    function pulsarAbrirPagos(){
        barraProgresoAbrirURL('on', 'barraProgresoLanzarAplicacion');
        var width = getWindowWidth();
        var height = getWindowHeight();

        var control = new Date();
        var result = null;
        barraProgresoAbrirURL('off', 'barraProgresoLanzarAplicacion');
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=urlPagos+"?num="+numExpediente+"&entorno="+codOrganizacion%>&control='+control.getTime(),height,width,'si','si');
        }else{
            result = lanzarPopUpModal('<%=urlPagos+"?num="+numExpediente+"&entorno="+codOrganizacion%>&control='+control.getTime(),height,width,'si','si');
        }
    }
    
    function getWindowWidth(){
        var myWidth = 900;
        try{
            if( typeof( window.innerWidth ) == 'number' ) {
                //Non-IE
                myWidth = window.innerWidth;
            } else if( document.documentElement && ( document.documentElement.clientWidth ) ) {
                //IE 6+ in 'standards compliant mode'
                myWidth = document.documentElement.clientWidth;
            } else if( document.body && ( document.body.clientWidth ) ) {
                //IE 4 compatible
                myWidth = document.body.clientWidth;
            }
            //myHeight = window.screen.availHeight;
            //myWidth = window.screen.availWidth;
        }catch(err){
            myWidth = 900;
        }
        
        return myWidth;
    }
    
    function getWindowHeight(){
        var myHeight = 800;
        try{
            if( typeof( window.innerHeight ) == 'number' ) {
                //Non-IE
                myHeight = window.innerHeight;
            } else if( document.documentElement && ( document.documentElement.clientHeight ) ) {
                //IE 6+ in 'standards compliant mode'
                myHeight = document.documentElement.clientHeight;
            } else if( document.body && ( document.body.clientHeight ) ) {
                //IE 4 compatible
                myHeight = document.body.clientHeight;
            }
            //myHeight = window.screen.availHeight;
        }catch(err){
            myHeight = 800;
        }
        return myHeight;
    }
    
    

//function lanzarPopUpModal( url, theHeight, theWidth, scrollable, resizable) {
// if (url) {
//     var h=640;
//     var w=480;
//     var s="no";
//     var r="no";
//     if (theHeight) h = theHeight;
//     if (theWidth) w = theWidth;
//     if (scrollable!=null) s = scrollable;
//     if (resizable!=null) r = resizable;
//     var winName = 'popup';
//    var t=(screen.height) ? (screen.height-h)/2 : 0;
//    var l=(screen.width) ? (screen.width-w)/2 : 0;
//    var options = "top="+t+",left="+l+",width="+w+",height="+h+",scrollbars="+s+",status=no,menubar=no,resizable="+r;
//    popup = window.open(url,winName,options);
//    if (popup.opener == null) popup.opener = self;
//    popup.focus();
//    return null;
// }
//}

    function lanzarPopUpModal( url, theHeight, theWidth, scrollable, resizable) {
     if (url) {
         var h=640;
         var w=480;
         var s="no";
         var r="no";
         if (theHeight) h = theHeight;
         if (theWidth) w = theWidth;
         if (scrollable!=null) s = scrollable;
         if (resizable!=null) r = resizable;
         var winName = 'popup';
         if (window.showModalDialog) {
            var mr = window.showModalDialog(url, null,'center:yes;dialogHeight:'+h+'px;dialogWidth:'+w+'px;status:no;help:no;unadorned:yes;edge:raisen;resizable:'+r+';scroll:'+s);
            return mr;
         } else {
            var t=(screen.height) ? (screen.height-h)/2 : 0;
            var l=(screen.width) ? (screen.width-w)/2 : 0;
            var options = "top="+t+",left="+l+",width="+w+",height="+h+",scrollbars="+s+",status=no,resizable="+r;
            popup = window.open(url,winName,options);
            if (popup.opener == null) popup.opener = self;
            popup.focus();
            return null;
         }
     }
    }
        
    function barraProgresoAbrirURL(valor, idBarra) {
        if(valor=='on'){
            document.getElementById(idBarra).style.visibility = 'inherit';
        }
        else if(valor=='off'){
            document.getElementById(idBarra).style.visibility = 'hidden';
        }
    }
</script>
<body>
    <div class="tab-page" id="tabPage331" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana331"><%=meLanbide33I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p331 = tp1.addTabPage( document.getElementById( "tabPage331" ) );</script>
          
        
        <div id="barraProgresoLanzarAplicacion" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span>
                                                    <%=meLanbide33I18n.getMensaje(idiomaUsuario, "msg.cargarDatos")%>
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
        
        <div class="botonera" style="padding-top: 20px;">
            <!--<input type="button" id="btnConceptos" name="btnConceptos" class="botonGeneral" value="<%=meLanbide33I18n.getMensaje(idiomaUsuario, "btn.conceptos")%>" onclick="window.open('<%=urlConceptos+"?num="+numExpediente+"&entorno="+codOrganizacion%>')">-->
            <input type="button" id="btnConceptos" name="btnConceptos" class="botonGeneral" value="<%=meLanbide33I18n.getMensaje(idiomaUsuario, "btn.conceptos")%>" onclick="pulsarAbrirConceptos();">
            
            <!--<input type="button" id="btnETecnico" name="btnETecnico" class="botonGeneral" value="<%=meLanbide33I18n.getMensaje(idiomaUsuario, "btn.eTecnico")%>" onclick="window.open('<%=urlETecnico+"?num="+numExpediente+"&entorno="+codOrganizacion%>')">-->
            <input type="button" id="btnETecnico" name="btnETecnico" class="botonGeneral" value="<%=meLanbide33I18n.getMensaje(idiomaUsuario, "btn.eTecnico")%>" onclick="pulsarAbrirETecnico();">
            
            <!--<input type="button" id="btnPagos" name="btnPagos" class="botonGeneral" value="<%=meLanbide33I18n.getMensaje(idiomaUsuario, "btn.pagos")%>" onclick="window.open('<%=urlPagos+"?num="+numExpediente+"&entorno="+codOrganizacion%>')">-->
            <input type="button" id="btnPagos" name="btnPagos" class="botonGeneral" value="<%=meLanbide33I18n.getMensaje(idiomaUsuario, "btn.pagos")%>" onclick="pulsarAbrirPagos();">
        </div>
    </div>
</body>

<script type="text/javascript">
    document.getElementById('btnConceptos').style.display = <%=btnConceptosVisible == true ? "'inline'" : "'none'"%>;
    document.getElementById('btnETecnico').style.display = <%=btnETecnicoVisible == true ? "'inline'" : "'none'"%>;
    document.getElementById('btnPagos').style.display = <%=btnPagosVisible == true ? "'inline'" : "'none'"%>;
</script>
