<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.vo.ProcedimientoVO" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
        <%
            int idiomaUsuario = 0;
            int codOrganizacion = 0;
            int apl = 5;
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
                        apl = usuario.getAppCod();
                    }
                }
            }
            catch(Exception ex)
            {

            }   
            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();
        %>
      
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script> 
        <script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>'</script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/tabUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/JavaScriptUtil.js"></script>

        
    </head>

   <body class="bandaBody">

        <div class="contenidoPantalla" style="clear: both; padding: 2%; height: 100vh; overflow-y: auto; display: flex; flex-direction: column;">
        
        <form id="formMantenimientoTiposDocProc" style="display: flex; flex-direction: column; flex-grow: 1;">
            
            <!-- Título de la tabla -->
            <div style="height:50px; width: 100%;">
                <table width="100%" style="height: 100%;" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td class="txttitblanco"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.tituloTablaTiposDocProc")%> </td>
                    </tr>
                </table>
            </div>  
                    
            <!-- Área de selección de procedimientos -->
            <div style="padding: 30px; text-align: left;">
                    <div>
                    <label class="etiqueta" style="text-align: center; position: relative; padding-left: 30px;">
                        <%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.Procedimientos")%>
                    </label>
                    </div>

                    <div style="padding-left: 30px;">
                    <input type="text" name="codListaProcedimiento" id="codListaProcedimiento" size="10" class="inputTexto" onkeyup="xAMayusculas(this);" />
                    <input type="text" name="descListaProcedimiento" id="descListaProcedimiento" size="150" class="inputTexto" readonly="true" />
                        <a href="" id="anchorListaProcedimiento" name="anchorListaProcedimiento">
                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonProcedimiento"
                             name="botonProcedimiento" height="14" width="14" border="0" style="cursor: pointer;">
                        </a>
                    </div>
                </div>                        
            
            <!-- Contenedor de la tabla -->
            <div id="divTablaBotones" style="flex-grow: 1; display: flex; flex-direction: column; padding: 30px; max-height: auto; overflow-y: auto;">
                </br>
                <div style="padding: 10px;">
                    </br>
                <!-- Tabla de tipos documentales Lanbide asociados a procedimientos -->
                 <!--<div id="divTablaTiposDoc" style="width: 100%; height: auto; text-align: center; height: auto; overflow-y: hidden;">-->
                    <!-- Aquí se generará dinámicamente la tabla de datos -->
                <!--/div-->
                    <div id="divGeneral" >     
                        <div id="divTablaTiposDoc" align="center"></div>
                        </div>
                    <!-- Botonera debajo de la tabla -->
                    <div class="botonera" style="width: 80%; text-align: center; margin-top: 20px;">
                              <input type="button" id="btnInsertarTipoDoc" name="btnInsertarTipoDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.insertar")%>" onclick="pulsarInsertarTipoDoc(true);">
                              <input type="button" id="btnEliminarTipoDoc" name="btnEliminarTipoDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTipoDoc(true);">                      
                        <input type="button" id="btnExportarExcelTipoDocProc" name="btnExportarExcelTipoDocProc" class="botonLargo" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.excel")%>" onclick="pulsarExportarExcelTipoDocProc();">
                        </div>
                    </div>
                </div>

        </form>
        </div>
        

<script type="text/javascript"> 
            var tabTiposDoc;
    var listaTiposDoc = [];
    var listaTiposDocTabla = [];
    var listaTiposDocTabla_titulos = [];

    // Inicialización de la tabla
    //tabTiposDoc = new FixedColumnTable(document.getElementById('divTablaTiposDoc'), 1000, 1000, 'tiposDoc');                
    tabTiposDoc = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('divTablaTiposDoc'));

    // Configurar columnas de la tabla (Sin agregar nuevos parámetros)
    tabTiposDoc.addColumna('100', 'left', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col1") %>', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col1.title") %>');
    tabTiposDoc.addColumna('400', 'left', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col3") %>', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col3.title") %>');
    tabTiposDoc.addColumna('400', 'left', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col2") %>', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col2.title") %>');
       tabTiposDoc.addColumna('200', 'left', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDokusi.tabla.col1") %>', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDokusi.tabla.col1.title") %>');
            
    //tabTiposDoc.addColumna('300', 'left', '< % = meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocDokusi.tabla.col3.title") %>', '< %= meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocDokusi.tabla.col3.title") %>');
            
    // Configuraciones adicionales de la tabla
    //tabTiposDoc.numColumnasFijas = 0;
            tabTiposDoc.displayCabecera=true;
    tabTiposDoc.height = '650';
    //tabTiposDoc.altoCabecera = 30;
    //tabTiposDoc.scrollWidth = 1000;
   // tabTiposDoc.dblClkFunction = 'dblClckTablaTiposDoc';
            tabTiposDoc.displayTabla();
   // tabTiposDoc.pack();
            
    // Añadir funcionalidad de ordenación a cada columna manualmente después de crear la tabla
    setTimeout(function () {
        // Esperamos a que las cabeceras se rendericen correctamente
        document.querySelectorAll('#divTablaTiposDoc .fctHeader td').forEach(function (headerElement, index) {
            headerElement.style.cursor = 'pointer'; // Cambiar el cursor para indicar que es ordenable
            headerElement.addEventListener('click', function () {
                ordenarTabla(index);
            });
        });
        }, 100);
        let ordenAscendente = true;

    // Función de ordenación
    function ordenarTabla(colIndex) {
    // Restablecer estilos previos
    document.querySelectorAll('#divTablaTiposDoc table.head tr td').forEach((el) => {
        el.classList.remove('ordenAsc', 'ordenDesc');
    });

    // Aplicar estilo actual
    let headerElement = document.querySelector(`#divTablaTiposDoc table.head tr td:nth-child(${colIndex + 1})`);
    headerElement.classList.add(ordenAscendente ? 'ordenAsc' : 'ordenDesc');

    // Ordenar
    listaTiposDoc.sort((a, b) => {
        if (!isNaN(a[colIndex]) && !isNaN(b[colIndex])) {
            return ordenAscendente ? a[colIndex] - b[colIndex] : b[colIndex] - a[colIndex];
        } else {
            return ordenAscendente ? a[colIndex].localeCompare(b[colIndex]) : b[colIndex].localeCompare(a[colIndex]);
            }
        });

    ordenAscendente = !ordenAscendente;
        recargarTablaTiposDocProcedimiento(listaTiposDoc);
    }

    // La función `recargarTablaTiposDocProcedimiento` ya existente para recargar la tabla
    function recargarTablaTiposDocProcedimiento(result) {
        var fila;
        listaTiposDoc = [];
        listaTiposDocTabla = [];
        listaTiposDocTabla_titulos = [];

        for (var i = 0; i < result.length; i++) {
            fila = result[i];

                if (fila[3] == "0")
                    fila[3] = "N";
                else if (fila[3] == "1")
                    fila[3] = "S";
          
            listaTiposDoc[i] = [fila[0], fila[1], fila[2], fila[3]];
            listaTiposDocTabla[i] = [fila[0], fila[1], fila[2], fila[3]];
            listaTiposDocTabla_titulos[i] = [fila[0], fila[1], fila[2], fila[3]];
        }

        tabTiposDoc.clearFilas(); // Limpia las filas actuales de la tabla antes de recargar

        for (var cont = 0; cont < listaTiposDocTabla.length; cont++) {
            tabTiposDoc.addFilaConFormato(listaTiposDocTabla[cont], listaTiposDocTabla_titulos[cont]);
        }

        tabTiposDoc.displayTabla();
        //tabTiposDoc.pack();
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
            
            var listaCodigosProcedimiento = new Array();
            var listaDescripcionesProcedimiento = new Array();

            listaCodigosProcedimiento[0] = "";
            listaDescripcionesProcedimiento[0] = "";
                        
            contador = 0;

            <logic:iterate id="numProc" name="listaProcedimiento" scope="request">
                listaCodigosProcedimiento[contador] = ['<bean:write name="numProc" property="codProc" />'];
                listaDescripcionesProcedimiento[contador] = '<bean:write name="numProc" property="descProc" />';
                contador++;
            </logic:iterate>

            var comboListaProcedimiento = new Combo("ListaProcedimiento");
            comboListaProcedimiento.addItems(listaCodigosProcedimiento, listaDescripcionesProcedimiento); 
            comboListaProcedimiento.change = cargarTablaTiposDocProcedimiento;

          // funcion que carga la tabla de Tipos documentales existentes para el procedimiento seleccionado
            function cargarTablaTiposDocProcedimiento()
            {                                
                var codProcSelec = document.getElementById("codListaProcedimiento").value;
                console.log("Ejecutando `cargarTablaTiposDocProcedimiento()`. Código Procedimiento:", codProcSelec);
                
                if (codProcSelec!=null & codProcSelec!="")
                { 
                    console.log("Ejecutando `cargarTablaTiposDocProcedimiento()`. Entra en if codProcSelec");
                //Hazce visible la tabla 
                    document.getElementById("divTablaBotones").style.display=""
                   
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";

                    var control = new Date();

                    parametros = "tarea=preparar&modulo=MELANBIDE68&operacion=cargarTablaTiposDocProcedimiento&tipo=0&codProc=" + codProcSelec                    
                               + "&control=" + control.getTime();

                    try {
                        ajax.open("POST", url, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                        ajax.send(parametros);
                        if (ajax.readyState == 4 && ajax.status == 200) {
                            var xmlDoc = null;
                            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                // En IE el XML viene en responseText y no en la propiedad responseXML
                                var text = ajax.responseText;
                                xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                xmlDoc.async = "false";
                                xmlDoc.loadXML(text);
                            } else {
                                // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                xmlDoc = ajax.responseXML;
                            }   //if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        }    //if (ajax.readyState==4 && ajax.status==200){
                        console.log("Ejecutando `cargarTablaTiposDocProcedimiento()`. Respuesta ajax:", xmlDoc);
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                        var listaTiposDoc = new Array();

                        if (codigoOperacion == 0) 
                        {   
                            var fila = new Array();
                            var elemento = nodos[0];
                            var hijos = elemento.childNodes;
                            var codigoOperacion = null;
                            var nodoFila;
                            var nodoFila;
                            var hijosFila;
                            var nodoCampo;     

                            var hijosFila;
                            for (j = 0; hijos != null && j < hijos.length; j++) {

                                if (hijos[j].nodeName == "FILA") {
                                    nodoFila = hijos[j];
                                    hijosFila = nodoFila.childNodes;
                                    for (var cont = 0; cont < hijosFila.length; cont++) {
                                        if (hijosFila[cont].nodeName == "COD_TIPDOC") {
                                          if (hijosFila[cont].childNodes.length > 0) {
                                              nodoCampo = hijosFila[cont];
                                              if(nodoCampo.childNodes.length > 0){
                                                  fila[0] = nodoCampo.childNodes[0].nodeValue;
                                              }
                                              else{
                                                  fila[0] = '-';
                                              }
                                          }
                                        }
                                        if (hijosFila[cont].nodeName == "TIPDOC_EU") {
                                          if (hijosFila[cont].childNodes.length > 0) {
                                              nodoCampo = hijosFila[cont];
                                              if(nodoCampo.childNodes.length > 0){
                                                  fila[1] = nodoCampo.childNodes[0].nodeValue;
                                              }
                                              else{
                                                  fila[1] = '-';
                                              }
                                          }
                                        }
                                        if (hijosFila[cont].nodeName == "TIPDOC_ES") {
                                           if (hijosFila[cont].childNodes.length > 0) {
                                               nodoCampo = hijosFila[cont];
                                               if(nodoCampo.childNodes.length > 0){
                                                   fila[2] = nodoCampo.childNodes[0].nodeValue;
                                               }
                                               else{
                                                   fila[2] = '-';
                                               }
                                           }
                                        }                                      
                                        if (hijosFila[cont].nodeName == "TIPDOC_DOKUSI") {
                                          if (hijosFila[cont].childNodes.length > 0) {
                                              nodoCampo = hijosFila[cont];
                                              if(nodoCampo.childNodes.length > 0){
                                                  fila[3] = nodoCampo.childNodes[0].nodeValue;
                                              }
                                              else{
                                                  fila[3] = 'Sin Dato';
                                              }
                                          }
                                        }
                                                                       
                                    }
                                    listaTiposDoc[j] = fila;
                                    fila = new Array();  
                                }
                            }
                        }   
                        recargarTablaTiposDocProcedimiento(listaTiposDoc);
                    } catch(Err){
                        jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>')
                    } 
                } 
                else
                {
                    document.getElementById("divTablaBotones").style.display="none";
                    jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.SeleccProc")%>');
                } 
            }    
            
            function recargarTablaTiposDocProcedimiento(result){
                var fila;
                listaTiposDoc = new Array();
                listaTiposDocTabla = new Array();
                listaTiposDocTabla_titulos = new Array();

                for(var i = 1;i< result.length; i++){
                    fila = result[i];
  
                    if (fila[2]=="0") fila[2]="N";
                    else if (fila[2]=="1") fila[2]="S";
            
                    listaTiposDoc[i-1] = [fila[0],fila[1],fila[2],fila[3]];
                    listaTiposDocTabla[i-1] = [fila[0],fila[1],fila[2],fila[3]];
                    listaTiposDocTabla_titulos[i-1] = [fila[0],fila[1],fila[2],fila[3]];
                }

                //tabTiposDoc = new FixedColumnTable(document.getElementById('divTablaTiposDoc'), 1000, 2000, 'tiposDoc');
                tabTiposDoc = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('divTablaTiposDoc'));
                // Configurar columnas de la tabla (Sin agregar nuevos parámetros)
                tabTiposDoc.addColumna('100', 'left', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col1") %>', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col1.title") %>');
                tabTiposDoc.addColumna('400', 'left', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col3") %>', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col3.title") %>');
                tabTiposDoc.addColumna('400', 'left', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col2") %>', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.tipdoc.tabla.col2.title") %>');
                tabTiposDoc.addColumna('200', 'left', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDokusi.tabla.col1") %>', '<%= meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDokusi.tabla.col1.title") %>');
                //tabTiposDoc.addColumna('300', 'left', '< % = meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocDokusi.tabla.col3.title") %>', '< %= meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocDokusi.tabla.col3.title") %>');

                //tabTiposDoc.numColumnasFijas = 0;
                tabTiposDoc.displayCabecera=true;
       

               // for(var cont = 0; cont < listaTiposDocTabla.length; cont++){
                 //   tabTiposDoc.addFilaConFormato(listaTiposDocTabla[cont], listaTiposDocTabla_titulos[cont])
                //}

                tabTiposDoc.height = '650';
                //tabTiposDoc.altoCabecera = 30;
                //tabTiposDoc.scrollWidth = 1500;
                //tabTiposDoc.dblClkFunction = 'dblClckTablaTiposDoc';
                tabTiposDoc.lineas = listaTiposDocTabla;
                tabTiposDoc.displayTabla();
                //tabTiposDoc.pack();
            }
            
            //function dblClckTablaTiposDoc(rowID,tableName){
            //}          
      
            function pulsarEliminarTipoDoc(){ 
                var codProc = document.getElementById("codListaProcedimiento").value;
                var fila;

                if(tabTiposDoc.selectedIndex != -1) {
                    fila = tabTiposDoc.selectedIndex;
                    var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1){
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        var listaTiposDoc = new Array();
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=cargarEliminarTipoDoc&tipo=0&codProc='+codProc
                                      +'&codTipDoc='+tabTiposDoc.lineas[fila][0]
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
                            cargarTablaTiposDocProcedimiento();
                            // vuelvo a colocarme al principio del XML para recuperar el código de operacion
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA"); 
                            var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
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
        
        function pulsarInsertarTipoDoc(){
            var control = new Date();
            var result = null;
            var codProc = document.getElementById("codListaProcedimiento").value;

    if (codProc != null && codProc != "") {
        // Calcular el tamaño dinámico (porcentaje del tamaño de la ventana)
        var ancho = Math.round(window.innerWidth * 0.8); // 80% del ancho de la ventana
        var alto = Math.round(window.innerHeight * 0.35); // 80% del alto de la ventana

        lanzarPopUpModal(
            '<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoTipoDoc&tipo=0&codProc=' 
            + codProc + '&control=' + control.getTime(),
            alto, // Altura calculada
            ancho, // Ancho calculado
            'no', 
            'no',
            function (result) {
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablaTiposDocProcedimiento(result);								
                                                                        }
                                                                }
                }
        );
    } else {
                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.SeleccProc")%>');
            }     
        }

        function pulsarExportarExcelTipoDocProc() {
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=getExcelTipDocuLanbideProcedimiento&tipo=0'; 
                    url = url + "?" + parametros
                    
                    window.location.href = url;
                }
                
        
       </script>
    </body>
</html>