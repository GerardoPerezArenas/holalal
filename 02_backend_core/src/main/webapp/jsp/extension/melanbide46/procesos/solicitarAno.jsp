<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n" %>

<html>
    <head> 
        <%
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
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
	 apl = usuario.getAppCod();
	 css = usuario.getCss();
                codOrganizacion  = usuario.getOrgCod();
            }
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide46/melanbide46.css"/>
        
        <script type="text/javascript">
            var msgValidacion = '';
            
            function aceptar(){
                if(esAnoValido(document.getElementById('txtAno').value)){
                    var ano = document.getElementById('txtAno').value;
                    self.parent.opener.retornoXanelaAuxiliar(  ano);
                    cerrarVentana();
                }else{
                    document.getElementById('msgLabel').innerHTML = msgValidacion;
                }
            }
            
            function cancelar(){
                cerrarVentana();
            }
            
            function cerrarVentana(){
                if(navigator.appName=="Microsoft Internet Explorer") { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                }else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                }else{
                     window.close(); 
                } 
            }
    
            function esAnoValido(ano){
                msgValidacion = '';
                try{
                    if(ano == '' || isNaN(ano)){
                        msgValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.anoNoValido")%>';
                        return false;
                    }else{
                        var anoAct = new Date().getFullYear();
                        var nAno = parseInt(ano);
                        if(nAno > anoAct){
                          msgValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.anoPosterior")%>';
                          return false;
                        }
                        if(nAno < 0){
                          msgValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.anoNegativo")%>';
                          return false;  
                        }
                    }
                }catch(err){

                }
                return true;
            }
        </script>
    </head>
    <body>
        <form>
            <div style="width: 100%; padding: 10px;">
                <%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.solicitarAno")%> <input id="txtAno" name="txtAno" type="text" class="inputTexto" size="4" maxlength="4">
                <div class="botonera" style="padding-top: 20px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="aceptar();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
                <div style="width: 100%; padding: 10px;">
                    <label id="msgLabel" style="color:red;"></label>
                </div>
            </div>
        </form>    
    </body>
</html>
