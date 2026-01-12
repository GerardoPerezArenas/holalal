<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide29.i18n.MeLanbide29I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide29.vo.ContratoRenovacionPlantillaVO"%>

<html>
    <head> 
        <%
            int idiomaUsuario = 1;
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
            MeLanbide29I18n meLanbide29I18n = MeLanbide29I18n.getInstance();
            
            String codOrganizacion  = request.getParameter("tipo");
            String mensajeProgreso = "";
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide29/melanbide29.css'/>">
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        
        <script type="text/javascript">
        
        
        
        
        function barraProgresoBusqueda(valor) {
            if(valor=='on'){
                document.getElementById('barraProgresoBusqueda').style.visibility = 'inherit';
            }
            else if(valor=='off'){
                document.getElementById('barraProgresoBusqueda').style.visibility = 'hidden';
            }
        }

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
           }
        }
        
        function validarCriteriosBusqueda(){
            var doc = document.getElementById('txtDniBusq').value;
            var nom = document.getElementById('txtNomBusq').value;
            var ape1 = document.getElementById('txtApe1Busq').value;
            var ape2 = document.getElementById('txtApe2Busq').value;
            
            if((doc == '') && 
                    ((nom == '' && ape1 == '' && ape2 == '')
                    ||
                    (nom == '' && ape1 == '')
                    ||
                    (nom == '' && ape2 == '')
                    ||
                    (ape1 = '' && ape2 == ''))
                ){
                        return false;
                }else{
                    return true;
                }
        }
        
        function buscar(){
            if(validarCriteriosBusqueda()){
                barraProgresoBusqueda('on');
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "tarea=preparar&modulo=MELANBIDE29&operacion=busquedaTerceros&tipo=0"
                + "&dni="+document.getElementById('txtDniBusq').value+"&nom="+escape(document.getElementById('txtNomBusq').value)+"&ape1="+escape(document.getElementById('txtApe1Busq').value)+"&ape2="+escape(document.getElementById('txtApe2Busq').value);
                try{
                    ajax.open("POST",url,false);
                    ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
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
                    var listaTerceros = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if(hijos[j].nodeName=="FILA"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="ID"){
                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else if(hijosFila[cont].nodeName=="DNI"){
                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else if(hijosFila[cont].nodeName=="NOMBREAPELLIDOS"){
                                    var nom = "";
                                    try{
                                        nom = hijosFila[cont].childNodes[0].childNodes[0].nodeValue;
                                        fila[2] = nom;
                                    }
                                    catch(err){

                                    }
                                    var ape1 = "";
                                    try{
                                        ape1 = hijosFila[cont].childNodes[1].childNodes[0].nodeValue;
                                        fila[3] = ape1;
                                    }
                                    catch(err){

                                    }
                                    var ape2 = "";
                                    try{
                                        ape2 = hijosFila[cont].childNodes[2].childNodes[0].nodeValue;
                                        fila[4] = ape2;
                                    }
                                    catch(err){

                                    }
                                    var nomApe = '';
                                    nomApe = nom != null ? nom : "";
                                    nomApe += ape1 != null && ape1 != "" ? (nomApe != null && nomApe != "" ? " " : "") : "";
                                    nomApe += ape1 != null ? ape1 : "";
                                    nomApe += ape2 != null && ape2 != "" ? (nomApe != null && nomApe != "" ? " " : "") : "";
                                    nomApe += ape2 != null ? ape2 : "";
                                    fila[5] = nomApe;
                                }
                            }
                            listaTerceros[j-1] = fila;
                            fila = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if(codigoOperacion=="0"){
                        listaResultados = new Array();
                        listaResultadosTabla = new Array();
                        for(var cont = 0; cont < listaTerceros.length; cont++){
                            listaResultados[cont] = [listaTerceros[cont][0], listaTerceros[cont][1], listaTerceros[cont][2], listaTerceros[cont][3], listaTerceros[cont][4]];
                            listaResultadosTabla[cont] = [listaTerceros[cont][1], listaTerceros[cont][5]];
                        }
                    }else if(codigoOperacion=="1"){
                        jsp_alerta("A",'<%=meLanbide29I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    }else if(codigoOperacion=="2"){
                        jsp_alerta("A",'<%=meLanbide29I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                }
                catch(Err){

                }//try-catch


                tabResultados = new Tabla(document.getElementById('resultados'), 530);
                tabResultados.addColumna('100','left',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col1")%>");
                tabResultados.addColumna('417','left',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col2")%>");
                tabResultados.displayCabecera=true;
                tabResultados.height = 220;
                tabResultados.lineas=listaResultadosTabla;
                tabResultados.displayTabla();

                barraProgresoBusqueda('off');
            }
            else{
                jsp_alerta('A', '<%=meLanbide29I18n.getMensaje(idiomaUsuario, "msg.critBusquedaInsuficientes")%>');
            }
        }
        
        function limpiar(){
            document.getElementById('txtDniBusq').value = '';
            document.getElementById('txtNomBusq').value = '';
            document.getElementById('txtApe1Busq').value = '';
            document.getElementById('txtApe2Busq').value = '';
        }
        
        function aceptar(){
            if(tabResultados.selectedIndex != -1) {
                window.returnValue =  listaResultados[tabResultados.selectedIndex];
                cerrarVentana();
            } 
            else{
                jsp_alerta('A', '<%=meLanbide29I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function cerrarVentana(){
          if(navigator.appName=="Microsoft Internet Explorer") { 
                window.parent.window.opener=null; 
                window.parent.window.close(); 
          } else if(navigator.appName=="Netscape") { 
                top.window.opener = top; 
                top.window.open('','_parent',''); 
                top.window.close(); 
           }else{
               window.close(); 
           } 
        }
        
        
        
        
        </script>
    </head>
    <body class="contenidoPantalla" style="width: 98%;">
        <form>
            <div id="barraProgresoBusqueda" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td align="center" valign="middle">
                            <table class="contenedorHidePage" cellpadding="0px" cellspacing="0px" border="0px">
                                <tr>
                                    <td>
                                        <table width="349px" height="100%">
                                            <tr>
                                                <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                    <span><%=meLanbide29I18n.getMensaje(idiomaUsuario, "msg.realizandoBusq")%></span>
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
            <div style="clear: both;">
                <fieldset style="text-align: left; margin: 10px;">
                    <legend><font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"legend.personaContratada")%></font></legend>
                    <div style="width: 100%; clear: both;">
                        <div style="width: 60px; float: left;">
                            <span> <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.dni")%></span>
                        </div>
                        <div style="width: 480px; float: left;">
                            <input type="text" size="25" maxlength="25" id="txtDniBusq" name="txtDniBusq"/>
                        </div>
                    </div>
                    <div style="width: 100%; clear: both;">
                        <div style="width: 60px; float: left;">
                            <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div style="width: 480px; float: left;">
                            <input type="text" size="75" maxlength="80" id="txtNomBusq" name="txtNomBusq"/>
                        </div>
                    </div>
                    <div style="width: 100%; clear: both;">
                        <div style="width: 60px; float: left;">
                            <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                        </div>
                        <div style="width: 480px; float: left;">
                            <input type="text" size="75" maxlength="100" id="txtApe1Busq" name="txtApe1Busq"/>
                        </div>
                    </div>
                    <div style="width: 100%; clear: both;">
                        <div style="width: 60px; float: left;">
                            <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                        </div>
                        <div style="width: 480px; float: left;">
                            <input type="text" size="75" maxlength="100" id="txtApe2Busq" name="txtApe2Busq"/>
                        </div>
                    </div> 
                    <div style="width: 100%; text-align: center; padding-top: 5px;">
                        <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral" value="Buscar" onclick="buscar();">
                        <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral" value="Limpiar" onclick="limpiar();">
                    </div>
                </fieldset>
                <div id="resultados" style="width:580px; height: 270px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px; padding: 10px;" align="center"></div>
                <div style="width: 100%; text-align: center; padding-top: 5px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="aceptar();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cerrarVentana();">
                </div>
            </div>
        </form>
    </body>

    <script type="text/javascript">    
        var tabResultados;
        var listaResultados = new Array();
        var listaResultadosTabla = new Array();
        
        tabResultados = new Tabla(document.getElementById('resultados'), 530);
        tabResultados.addColumna('100','left',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col1")%>");
        tabResultados.addColumna('417','left',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col2")%>");
        tabResultados.displayCabecera=true;
        tabResultados.height = 220;
        tabResultados.lineas=listaResultadosTabla;
        tabResultados.displayTabla();
    </script>
</html>