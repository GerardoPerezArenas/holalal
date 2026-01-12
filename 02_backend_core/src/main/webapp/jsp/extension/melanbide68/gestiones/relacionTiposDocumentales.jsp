<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocDokusiVO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
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
            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();
        %>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/FixedColumnsTable.js"></script>    

        <script type="text/javascript">

            function inicializar(){
                window.focus();
                APP_CONTEXT_PATH='<%=request.getContextPath()%>';
            }

        </script> 
    </head>

    <body class="bandaBody" onload="inicializar();">
        <form id="formMantenimientoTipDocDokusi">
            <div style="height:50px; width: 100%;">
                <table width="100%" style="height: 100%;" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td class="txttitblanco"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.tituloTablaTiposDocDokusi")%> </td>
                    </tr>
                </table>
            </div>          
            <div style="clear: both;">
                <div style="width: 90%;">
                    <!-- tabla de relación de tipos documentales Lanbide-Dokusi --->
                    <div 
                        id="divTablaTipDocDokusi" style="padding: 30px; width:100%; height: 600px; text-align: center">
                    </div>
                    <!-- botones debajo de la tabla --->
                    <div class="botonera" style="width:95%">
                          <input type="button" id="btnInsertarTipDoc" name="btnInsertarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.insertar")%>" onclick="pulsarInsertarTipDocDokusi(true);">
                          <input type="button" id="btnModificarTipDoc" name="btnModificarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTipDocDokusi();">
                          <input type="button" id="btnEliminarTipDoc" name="btnEliminarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTipDocDokusi(true);">
                    </div>
                </div>
            </div>                          
        </form>

        <script type="text/javascript">

            var tabTipDocDokusi;
            var listaTipDocDokusi = new Array();
            var listaTipDocDokusiTabla = new Array();
            var listaTipDocDokusiTabla_titulos = new Array();

            tabTipDocDokusi = new FixedColumnTable(document.getElementById('divTablaTipDocDokusi'), 1200, 1200, 'TipDocDokusi');
            tabTipDocDokusi.addColumna('100','center',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col1")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col1.title")%>");    
            tabTipDocDokusi.addColumna('600','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col2")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col2.title")%>");    
            tabTipDocDokusi.addColumna('500','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col3")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col3.title")%>");    
            tabTipDocDokusi.numColumnasFijas = 0;
            tabTipDocDokusi.displayCabecera=true;

            <%  
                FilaTipDocDokusiVO voP = null;
                List<FilaTipDocDokusiVO> listaTiposDocumentalesDokusi = (List<FilaTipDocDokusiVO>)request.getAttribute("tiposDocumentalesDokusi");													
                if (listaTiposDocumentalesDokusi != null && listaTiposDocumentalesDokusi.size() >0){
                    for(int i = 0;i < listaTiposDocumentalesDokusi.size();i++)
                    {
                        voP = listaTiposDocumentalesDokusi.get(i);
                    %>
                        listaTipDocDokusi[<%=i%>] = ['<%=voP.getCodTipDoc()%>','<%=voP.getDescTipDoc()%>','<%=voP.getTipDocDokusi()%>'];
                        listaTipDocDokusiTabla[<%=i%>] = ['<%=voP.getCodTipDoc()%>','<%=voP.getDescTipDoc()%>','<%=voP.getTipDocDokusi()%>'];
                        listaTipDocDokusiTabla_titulos[<%=i%>] = ['<%=voP.getCodTipDoc()%>','<%=voP.getDescTipDoc()%>','<%=voP.getTipDocDokusi()%>'];
                    <%
                    }// for
                }// if
            %>

            for(var cont = 0; cont < listaTipDocDokusiTabla.length; cont++){
                tabTipDocDokusi.addFila(listaTipDocDokusiTabla[cont], listaTipDocDokusiTabla_titulos[cont]);
            }

            tabTipDocDokusi.height = '600';
            tabTipDocDokusi.altoCabecera = 35;
            tabTipDocDokusi.scrollWidth = 1200;
            tabTipDocDokusi.dblClkFunction = 'dblClckTablaTipDocDokusi';
            tabTipDocDokusi.displayTabla();
            tabTipDocDokusi.pack();

            function dblClckTablaTipDocDokusi(rowID,tableName){
                //pulsarModificarTipDocDokusi();
            }

            function getXMLHttpRequest() {
                var aVersions = ["MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp", "Microsoft.XMLHttp"
                ];

                if (window.XMLHttpRequest) {
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                } else if (window.ActiveXObject) {
                    // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                    for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        } catch (error) {
                            //no necesitamos hacer nada especial
                        }
                    }
                } else {
                    return null;
                }
            }

        //Función para eliminar un registro
            function pulsarEliminarTipDocDokusi(){ 
                var fila;

                if(tabTipDocDokusi.selectedIndex != -1) {
                    fila = tabTipDocDokusi.selectedIndex;
                    var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1){
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        var listaTipDocDokusi = new Array();
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=cargarEliminarTipDocDokusi&tipo=0'
                                      +'&codTipDoc='+tabTipDocDokusi.lineas[fila][0]
                                      +'&control='+control.getTime(); 
                        try{
                            ajax.open("POST",url,false);
                            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
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
                            // que pasaría con el código de respuesta ?????
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                            listaTipDocDokusi = extraerListadoTipDocDokusi(nodos);
                            recargarTablalistaTipDocDokusi(listaTipDocDokusi);
                            var codigoOperacion = listaTipDocDokusi[0];
                            if(codigoOperacion=="0"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            }else if(codigoOperacion=="1"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                            }else if(codigoOperacion=="2"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                            }else if(codigoOperacion=="3"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                            }else{
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                            }//if(
                        }
                        catch(Err){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                        }//try-catch
                    }
                } 
                else {
                    jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            } 

            function extraerListadoTipDocDokusi(nodos){
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaTipDocDokusi = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;
                    var j;

                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaTipDocDokusi[j] = codigoOperacion;
                        }                   

                        else if(hijos[j].nodeName=="FILA"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="COD_TIPDOC"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[0] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[0] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="TIPDOC_LANBIDE"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[1] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[1] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="TIPDOC_DOKUSI"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[2] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[2] = '-';
                                    }
                                }
                            }
                            listaTipDocDokusi[j] = fila;
                            fila = new Array();   
                        }
                } 
                return listaTipDocDokusi;
            }

            function recargarTablalistaTipDocDokusi(result){
                var fila;
                listaTipDocDokusi = new Array();
                listaTipDocDokusiTabla = new Array();
                listaTipDocDokusiTabla_titulos = new Array();

                for(var i = 1;i< result.length; i++){
                    fila = result[i];

                    listaTipDocDokusi[i-1] = [fila[0],fila[1],fila[2]];
                    listaTipDocDokusiTabla[i-1] = [fila[0],fila[1],fila[2]];
                    listaTipDocDokusiTabla_titulos[i-1] = [fila[0],fila[1],fila[2]];
                }
                tabTipDocDokusi = new FixedColumnTable(document.getElementById('divTablaTipDocDokusi'), 1200, 1200, 'TipDocDokusi');
                tabTipDocDokusi.addColumna('100','center',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col1")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col1.title")%>");    
                tabTipDocDokusi.addColumna('600','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col2")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col2.title")%>");    
                tabTipDocDokusi.addColumna('500','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col3")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.tabla.col3.title")%>");    
                tabTipDocDokusi.numColumnasFijas = 0;
                tabTipDocDokusi.displayCabecera=true;
                
                for(var cont = 0; cont < listaTipDocDokusiTabla.length; cont++){
                    tabTipDocDokusi.addFila(listaTipDocDokusiTabla[cont], listaTipDocDokusiTabla_titulos[cont]);
                }

                tabTipDocDokusi.height = '600';
                tabTipDocDokusi.altoCabecera = 35;
                tabTipDocDokusi.scrollWidth = 1200;
                tabTipDocDokusi.dblClkFunction = 'dblClckTablaTipDocDokusi';
                tabTipDocDokusi.displayTabla();
                tabTipDocDokusi.pack();
            
            }
  
            function pulsarInsertarTipDocDokusi(){
                var control = new Date();
                var result = null;

                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoTipDocDokusi&tipo=0'
                                                                                                      +'&control='+control.getTime(),360,1030,'no','no', function(result){
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablalistaTipDocDokusi(result);								
                                                                        }
                                                                }
                                                        });
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoTipDocDokusi&tipo=0'
                                                                                                      +'&control='+control.getTime(),360,1030,'no','no', function(result){
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablalistaTipDocDokusi(result);								
                                                                        }
                                                                }
                                                        });
                }   
            }
            
            function pulsarModificarTipDocDokusi(){
                var fila;

                if(tabTipDocDokusi.selectedIndex != -1) {
                    fila = tabTipDocDokusi.selectedIndex;
                    var control = new Date();

                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarModificarTipDocDokusi&tipo=0'
                                                                        +'&codTipDoc='+tabTipDocDokusi.lineas[fila][0]
                                                                        +'&tipDocLanbide='+tabTipDocDokusi.lineas[fila][1]
                                                                        +'&tipDocDokusi='+tabTipDocDokusi.lineas[fila][2]                                                                
                                                                        +'&control='+control.getTime(),360,1030,'no','no', function(result){
                                                                if (result != undefined){
                                                                    // aquí también tengo que ver la forma de recuperar el CodigoOperacion porque si no me va a dar error
                                                                        if(result[0] == '0'){
                                                                                recargarTablalistaTipDocDokusi(result);								
                                                                        }
                                                                }
                                                        });
                    }else{
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarModificarTipDocDokusi&tipo=0'
                                                                        +'&codTipDoc='+tabTipDocDokusi.lineas[fila][0]
                                                                        +'&tipDocLanbide='+tabTipDocDokusi.lineas[fila][1]
                                                                        +'&tipDocDokusi='+tabTipDocDokusi.lineas[fila][2] 
                                                                        +'&control='+control.getTime(),360,1030,'no','no', function(result){
                                                                if (result != undefined){
                                                                    // aquí también tengo que ver la forma de recuperar el CodigoOperacion porque si no me va a dar error
                                                                        if(result[0] == '0'){
                                                                                recargarTablalistaTipDocDokusi(result);								
                                                                        }
                                                                }
                                                        });
                    }

                }else{
                        jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }    
            }

        </script>
    </body>
</html>