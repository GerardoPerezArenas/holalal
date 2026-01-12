<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>

<html>
    <head> 
        <%
            MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null)
            {
                try
                {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                }
                catch(Exception ex)
                {
                    
                }
            }
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide48/melanbide48.css"/>
        
        <script type="text/javascript">
            var msgValidacion = '';
            
            function aceptar(){
                if(esAnoValido(document.getElementById('txtAno').value)){
                    var ano = document.getElementById('txtAno').value;
                    window.returnValue =  ano;
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
                        msgValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.anoNoValido")%>';
                        return false;
                    }else{
                        var anoAct = new Date().getFullYear();
                        var nAno = parseInt(ano);
                        if(nAno > anoAct){
                          msgValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.anoPosterior")%>';
                          return false;
                        }
                        if(nAno < 0){
                          msgValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.anoNegativo")%>';
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
                <%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.solicitarAno")%> <input id="txtAno" name="txtAno" type="text" class="inputTexto" size="4" maxlength="4">
                <div class="botonera" style="padding-top: 20px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="aceptar();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
                <div style="width: 100%; padding: 10px;">
                    <label id="msgLabel" style="color:red;"></label>
                </div>
            </div>
        </form>    
    </body>
</html>