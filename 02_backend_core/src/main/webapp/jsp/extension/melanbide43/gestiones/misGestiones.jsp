<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.i18n.MeLanbide43I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaFaseVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.TramiteVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <%
            MeLanbide43I18n meLanbide43I18n = MeLanbide43I18n.getInstance();

            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try
            {
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide43/melanbide43.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/FixedColumnsTable.js"></script>        

        <script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>'</script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide43/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide43/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide43/InputMask.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide43/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/ecaUtils.js"></script>
        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script-->

    </head>
    <body class="bandaBody">
        <div class="contenidoPantalla" style="clear: both;">
            <form>
                <div style="width: 100%; text-align: left;">
                    <div>
                        <h2 class="sub3titulo" id="pestana331"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Mantenimiento")%></h2>
                        <div style="padding: 30px;"> 
                            <!-- etiqueta de procedimientos -->
                            <div>
                                <label class="etiqueta" style="text-align: center; position: relative; padding-left: 30px;"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Procedimientos")%></label>
                            </div>
                             <!-- combo de procedimientos -->
                            <div style="padding-left: 30px;">
                                <input type="text" name="codListaProcedimiento" id="codListaProcedimiento" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaProcedimiento" id="descListaProcedimiento" size="150" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaProcedimiento" name="anchorListaProcedimiento">
                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonProcedimiento"
                                         name="botonProcedimiento" height="14" width="14" border="0" style="cursor:hand;">
                                </a>
                            </div>
                        </div>
                    </div>
                <div style="clear: both;">
                    <div>
                        <!-- tabla de Fases --->
                        <div 
                            id="divTablaFases" style="padding: 30px; width:100%; height: 400px; text-align: center">
                        </div>
                        <!-- botones debajo de la tabla --->
                        <div class="botonera" >
                              <input type="button" id="btnInsertarFase" name="btnInsertarFase" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "btn.insertar")%>" onclick="pulsarInsertarFase(true);">
                              <input type="button" id="btnModificarFase" name="btnModificarFase" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarFase();">
                              <input type="button" id="btnEliminarFase" name="btnEliminarFase" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarFase(true);">
                        </div>
                    </div>
                </div>
            </div>

            </form>
        </div>

        <script type="text/javascript">
            
            var tabFases;
            var listaFases = new Array();
            var listaFasesTabla = new Array();
            var listaFasesTabla_titulos = new Array();
            var listaFasesTabla_estilos = new Array();
            
            tabFases = new FixedColumnTable(document.getElementById('divTablaFases'), 1020, 1000, 'fases');
            tabFases.addColumna('80','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col1")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col1.title")%>");    
            tabFases.addColumna('80','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col2")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col2.title")%>");    
            tabFases.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col3")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col3.title")%>");    
            tabFases.addColumna('80','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col4")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col4.title")%>");    
            tabFases.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col5")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col5.title")%>");    
            tabFases.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col6")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col6.title")%>");  
            tabFases.numColumnasFijas = 0;
            tabFases.displayCabecera=true;

            
            tabFases.height = '400';
            tabFases.altoCabecera = 30;
            tabFases.scrollWidth = 1010;
            tabFases.dblClkFunction = 'dblClckTablaFases';
            tabFases.displayTabla();
            tabFases.pack();
            
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
            
            //var listaCodigosTramite = new Array();
            //var listaDescripcionesTramite = new Array();

            listaCodigosProcedimiento[0] = "";
            listaDescripcionesProcedimiento[0] = "";
            
            //listaCodigosTramite[0] = "";
            //listaDescripcionesTramite[0] = "";
            
            contador = 0;

            <logic:iterate id="numProc" name="listaProcedimiento" scope="request">
            listaCodigosProcedimiento[contador] = ['<bean:write name="numProc" property="codProc" />'];
            listaDescripcionesProcedimiento[contador] = ['<bean:write name="numProc" property="descProc" />'];
            contador++;
            </logic:iterate>
                
            /*contador = 0;

            <!--logic:iterate id="numTram" name="listaTramite" scope="request"-->
            listaCodigosTramite[contador] = ['<!--bean:write name="numTram" property="codTramExterno" /-->'];
            listaDescripcionesTramite[contador] = ['<!--bean:write name="numTram" property="descTramite" /-->'];
            contador++;
            <!--/logic:iterate-->
            */

            var comboListaProcedimiento = new Combo("ListaProcedimiento");
            comboListaProcedimiento.addItems(listaCodigosProcedimiento, listaDescripcionesProcedimiento);
            comboListaProcedimiento.change = cargarTablaFases;

            //Combo procedimientos
            function buscaCodigoProcedimiento(codProcedimiento) {
                comboListaProcedimiento.buscaCodigo(codProcedimiento);
            }

            function cargarDatosProcedimiento() {
                codProSel = document.getElementById("codListaProcedimiento").value;
                buscaCodigoProcedimiento(codProSel);           
            }
            
            // funcion que carga la tabla de las fases existentes para el procedimiento seleccionado
            function cargarTablaFases()
            {                   
                //var listaFases = new Array();
               
                var codProcSelec = document.getElementById("codListaProcedimiento").value;
                
                if (codProcSelec!=null & codProcSelec!="")
                { 
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";

                    var control = new Date();

                    parametros = "tarea=preparar&modulo=MELANBIDE43&operacion=cargarTablaFases&tipo=0&codProc=" + codProcSelec 
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
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        listaFases=convertirXML(nodos);
                        recargarTablaFases(listaFases);
                    } catch(Err){
                        //jsp_alerta("A",'<//%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>')
                    } //try-catch     
                }
                else //codProcSelec!=null & codProcSelec!=""
                {
                    jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.SeleccProcedimiento")%>');
                } 
            }
                        
            function convertirXML(nodos)
            {         
                var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                var listaFases = new Array();

                if (codigoOperacion == 0) {
                //try {
                    var fila = new Array();
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;     

                    for (j = 0; hijos != null && j < hijos.length; j++) {

                        if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].nodeName == "COD_TRAM_EXT") {
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
                                if (hijosFila[cont].nodeName == "COD_TRAMITE") {
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
                                else if (hijosFila[cont].nodeName == "TRAMITE") {
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
                                 else if (hijosFila[cont].nodeName == "COD_FASE") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                }
                                 else if (hijosFila[cont].nodeName == "DESC_FASE_C") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                }
                                 else if (hijosFila[cont].nodeName == "DESC_FASE_E") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                }
                            }
                            listaFases[j] = fila;
                            fila = new Array();  
                        }
                    }
                }
                return listaFases;
//                alert ("acaba try");
//                catch(Err){
//                    jsp_alerta("A",'<//%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>')
//                    } //try-catch                    
//                } 
            }    
            
            function recargarTablaFases(result){
                var fila;
                listaFases = new Array();
                listaFasesTabla = new Array();
                listaFasesTabla_titulos = new Array();
                listaFasesTabla_estilos = new Array();

                for(var i = 1;i< result.length; i++){
                    fila = result[i];
  
                    listaFases[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5]];
                    listaFasesTabla[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5]];
                    listaFasesTabla_titulos[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5]];
                }
                
                tabFases = new FixedColumnTable(document.getElementById('divTablaFases'), 1020, 1000, 'fases');  
                tabFases.addColumna('80','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col1")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col1.title")%>");    
                tabFases.addColumna('80','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col2")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col2.title")%>");    
                tabFases.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col3")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col3.title")%>");    
                tabFases.addColumna('80','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col4")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col4.title")%>");    
                tabFases.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col5")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col5.title")%>");    
                tabFases.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col6")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.tabla.col6.title")%>");  
                tabFases.numColumnasFijas = 0;
                tabFases.displayCabecera=true;

                for(var cont = 0; cont < listaFasesTabla.length; cont++){
                    tabFases.addFilaConFormato(listaFasesTabla[cont], listaFasesTabla_titulos[cont], listaFasesTabla_estilos[cont])
                }

                tabFases.height = '400';
                tabFases.altoCabecera = 30;
                tabFases.scrollWidth = 1010;
                tabFases.dblClkFunction = 'dblClckTablaFases';
                tabFases.displayTabla();
                tabFases.pack();
            }
            
            function dblClckTablaFases(rowID,tableName){
                //pulsarModificarFase();
            }
            
            function pulsarInsertarFase(){
                var control = new Date();
                var result = null;
                var codProc = document.getElementById("codListaProcedimiento").value;

                if (codProc!=null & codProc!="")
                { 
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE43&operacion=cargarNuevaFase&tipo=0&codProc='+codProc
                                                                                                          +'&control='+control.getTime(),350,1030,'no','no', function(result){
                                                                    if (result != undefined){
                                                                            if(result[0] == '0'){
                                                                                    recargarTablaFases(result);								
                                                                            }
                                                                    }
                                                            });
                    }else{
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE43&operacion=cargarNuevaFase&tipo=0&codProc='+codProc
                                                                                                          +'&control='+control.getTime(),350,1030,'no','no', function(result){
                                                                    if (result != undefined){
                                                                            if(result[0] == '0'){
                                                                                    recargarTablaFases(result);								
                                                                            }
                                                                    }
                                                            });
                    }
                }else
                {
                    jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.SeleccProcedimiento")%>');
                }     
            }
            
            function pulsarModificarFase(){
                var fila;

                if(tabFases.selectedIndex != -1) {
                    fila = tabFases.selectedIndex;
                    var control = new Date();
                    //var result = null;
                    var codProc = document.getElementById("codListaProcedimiento").value;

                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE43&operacion=cargarModificarFase&tipo=0&codProc='+codProc
                                                                        +'&codTram='+tabFases.lineas[fila][1]
                                                                        +'&descTram='+tabFases.lineas[fila][2]
                                                                        +'&codFase='+tabFases.lineas[fila][3]
                                                                        +'&descFaseCas='+tabFases.lineas[fila][4]
                                                                        +'&descFaseEus='+tabFases.lineas[fila][5]
                                                                        +'&control='+control.getTime(),350,1030,'no','no', function(result){
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablaFases(result);								
                                                                        }
                                                                }
                                                        });
                    }else{
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE43&operacion=cargarModificarFase&tipo=0&codProc='+codProc
                                                                        +'&codTram='+tabFases.lineas[fila][1]
                                                                        +'&descTram='+tabFases.lineas[fila][2]
                                                                        +'&codFase='+tabFases.lineas[fila][3]
                                                                        +'&descFaseCas='+tabFases.lineas[fila][4]
                                                                        +'&descFaseEus='+tabFases.lineas[fila][5]
                                                                        +'&control='+control.getTime(),350,1030,'no','no', function(result){
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablaFases(result);								
                                                                        }
                                                                }
                                                        });
                    }

                }else{
                        jsp_alerta('A', '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }    
            }
    
            function pulsarEliminarFase(){ 
                var codProc = document.getElementById("codListaProcedimiento").value;
                var fila;

                if(tabFases.selectedIndex != -1) {
                    fila = tabFases.selectedIndex;
                    var resultado = jsp_alerta('', '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1){
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        var listaFases = new Array();
                        parametros = 'tarea=preparar&modulo=MELANBIDE43&operacion=cargarEliminarFase&tipo=0&codProc='+codProc
                                      +'&codTram='+tabFases.lineas[fila][1]
                                      +'&codFase='+tabFases.lineas[fila][3]
                                      +'&control='+control.getTime(); 
                        try{
                            ajax.open("POST",url,false);
                            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                            ajax.send(parametros);
                            var eliminar = new Boolean(true);
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
                            listaFases=convertirXML(nodos);                            
                            recargarTablaFases(listaFases);
                            // vuelvo a colocarme al principio del XML para recuperar el código de operacion
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA"); 
                            var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                            if(codigoOperacion=="0"){
                                jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            }else if(codigoOperacion=="1"){
                                jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                            }else if(codigoOperacion=="2"){
                                jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                            }else if(codigoOperacion=="3"){
                                jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                            }else{
                                jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                            }//if(
                        }
                        catch(Err){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                        }//try-catch
                    }
                } 
                else {
                    jsp_alerta('A', '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
                //barraProgreso('off', 'barraProgresoFases');
        }            
        </script>
              
    </body>
</html>

