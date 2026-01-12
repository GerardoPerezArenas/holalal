<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>

<html>
    <head> 
        <%
            MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();           
    int idiomaUsuario = 0;
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
        
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
        
       <script type="text/javascript">
            var msgValidacion = '';
            
            function aceptar(){
                if(esAnoValido(document.getElementById('txtAno').value)){
                    if(haSeleccionadoFormato()){
                        var ano = document.getElementById('txtAno').value;
                        var formato = document.getElementById('formatoSalida').value;
                        var retValue = new Array();
                        retValue[0] = ano;
                        retValue[1] = formato;
                        self.parent.opener.retornoXanelaAuxiliar(retValue);
                        cerrarVentana();
                    }else{
                        document.getElementById('msgLabel').innerHTML = msgValidacion;
                    }
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
                        msgValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.anoNoValido")%>';
                        return false;
                    }else{
                        var anoAct = new Date().getFullYear();
                        var nAno = parseInt(ano);
                        if(nAno > anoAct){
                          msgValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.anoPosterior")%>';
                          return false;
                        }
                        if(nAno < 0){
                          msgValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.anoNegativo")%>';
                          return false;  
                        }
                    }
                }catch(err){

                }
                return true;
            }
            
            function haSeleccionadoFormato(){
                var formatoSeleccionado = document.getElementById('formatoSalida').value;
                if(formatoSeleccionado != null && formatoSeleccionado != undefined && formatoSeleccionado != ''){
                    if(formatoSeleccionado == 'excel' || formatoSeleccionado == 'pdf' || formatoSeleccionado == 'word'){
                        return true;
                    }else{
                        msgValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.formatoSalidaIncorrecto")%>';
                        return false;
                    }
                }else{
                    msgValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.formatoSalidaVacio")%>';
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form>
            <div style="width: 100%; padding: 10px;">
                <div style="width: 100%; clear: both;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitarAno")%> <input id="txtAno" name="txtAno" type="text" class="inputTexto" size="4" maxlength="4">
                </div>
                <div style="width: 100%; clear: both;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.label.formatoInforme")%>
                    <select id="formatoSalida">
                        <option value="vacio"></option>
                        <option value="excel">Excel</option>
                        <!--<option value="pdf">PDF</option>-->
                    </select>
                </div>
                <div class="botonera" style="padding-top: 20px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.aceptar")%>" onclick="aceptar();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cancelar")%>" onclick="cancelar();">
                </div>
                <div style="width: 100%; padding: 10px;">
                    <label id="msgLabel" style="color:red;"></label>
                </div>
            </div>
        </form>    
    </body>
</html>

