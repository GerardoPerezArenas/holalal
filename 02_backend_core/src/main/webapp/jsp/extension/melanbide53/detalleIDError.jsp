<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.i18n.MeLanbide53I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleErrorVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
</head>
<body>
	<div>
		<%
			DetalleErrorVO datDetaError = new DetalleErrorVO();
			String descripcionError = "";

			String nuevo = "";

			String tipo = "";
				   
			MeLanbide53I18n meLanbide53I18n = MeLanbide53I18n.getInstance();
			
			int idiomaUsuario = 1;
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
						}
					}
				}
				catch(Exception ex)
				{

				}

				nuevo = (String)request.getAttribute("nuevo");
				tipo = (String)request.getAttribute("tipo");

				if(request.getAttribute("datError") != null)
				{
					datDetaError = (DetalleErrorVO)request.getAttribute("datError");
					
				}
			}
			catch(Exception ex)
			{
				%>
				alert("Tenemos una excepción");
				<%
			}
		%>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide53/melanbide53.css"/>
		<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
		<script type="text/javascript">
				var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
			   
		</script>

	<!--    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
		<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide53/JavaScriptUtil.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide53/Parsers.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide53/InputMask.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide53/lanbide.js"></script>-->
		<!-- Eventos onKeyPress compatibilidad firefox  -->
		<!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->
	   
		<script type="text/javascript">

			var mensajeValidacion = '';
			var nuevo = '<%=nuevo%>';
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
			function rellenardatos(){
				if('<%=datDetaError%>' != null){
					buscaCodListaTipo('<%=datDetaError.getTipo() != null ? datDetaError.getTipo() : ""%>');  
					buscaCodListaCritico('<%=datDetaError.getCritico() != null ? datDetaError.getCritico() : ""%>'); 
				}
				else
					alert('No hemos podido cargar los datos para modificar');
			}
			  function compruebaTamanoCampo(elemento, maxTex){
				var texto = elemento.value;
				if(texto.length>maxTex){
					jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
					elemento.focus();
					return false;
				}
				return true;
			}
			
			function cerrarVentana() {
				if (navigator.appName == "Microsoft Internet Explorer") {
					window.parent.window.opener = null;
					window.parent.window.close();
				} else if (navigator.appName == "Netscape") {
					top.window.opener = top;
					top.window.open('', '_parent', '');
					top.window.close();
				} else {
					window.close();
				}
			}
			
			

			function cancelar() {
				var resultado = jsp_alerta('','<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
				if (resultado == 1) {
					cerrarVentana();
				}
			}
			
			function buscaCodListaTipo (codListaTipo){
				comboTipo.buscaCodigo(codListaTipo);
			}
			function buscaCodListaCritico (codListaCritico){
				comboCritico.buscaCodigo(codListaCritico);
			}
			
			function tipo(){
				//Recuperamos el valor seleccionado en el combo.
				if(document.getElementById("codListaTipo") != null){
					var codigo = document.getElementById("codListaTipo").value;
					buscaCodListaTipo(codigo);
				}
			}
			function critico(){
				//Recuperamos el valor seleccionado en el combo.
				if(document.getElementById("codListaCritico") != null){
					var codigo = document.getElementById("codListaCritico").value;
					buscaCodListaCritico(codigo);
				}
			}
			
			
			function nuevoError() {
				if (validarDatos()) {
					var ajax = getXMLHttpRequest();
					var nodos = null;
					var CONTEXT_PATH = '<%=request.getContextPath()%>';
					var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
					var parametros = "";
					var nuevo = "<%=nuevo%>";
					parametros = "tarea=preparar&modulo=MELANBIDE53&operacion=nuevoIdError&tipo=0"
								+ "&idError="+ document.getElementById('idDetalleError').value
								+ "&descC=" + escape(document.getElementById('descCortaDetaErr').value)
								+ "&descripcion=" + escape(document.getElementById('descriptDetaErr').value)
								+ "&codTipo=" + escape(document.getElementById('codListaTipo').value)
								+ "&critico=" + escape(document.getElementById('codListaCritico').value)
								+ "&aviso=" + document.getElementById('menAvisoDetaErr').value
								+ "&correos=" + document.getElementById('mailsAvisoDetaErr').value
								+ "&acciones=" +document.getElementById('accionesDetaErr').value
								+ "&mod=" + document.getElementById('moduloOrigen').value
						;
					try {
						ajax.open("POST", url, false);
						ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
						ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
						//var formData = new FormData(document.getElementById('formContrato'));
						
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
							}//if(navigator.appName.indexOf("Internet Explorer")!=-1)
						}//if (ajax.readyState==4 && ajax.status==200)
						nodos = xmlDoc.getElementsByTagName("RESPUESTA");
						var elemento = nodos[0];
						var hijos = elemento.childNodes;
						var codigoOperacion = null;
						var lista = new Array();
						var fila = new Array();
						var nodoFila; 
						var hijosFila;
						for (j = 0; hijos != null && j < hijos.length; j++) {
							if (hijos[j].nodeName == "CODIGO_OPERACION") {
								codigoOperacion = hijos[j].childNodes[0].nodeValue;
								lista[j] = codigoOperacion;
							}//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
							else if (hijos[j].nodeName == "FILA") {
								nodoFila = hijos[j];
								hijosFila = nodoFila.childNodes;
								for (var cont = 0; cont < hijosFila.length; cont++) {
									if (hijosFila[cont].nodeName == "IDEN_ERR_ID") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[0] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[0] = '-';
										}
									}
									else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC_C") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[1] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[1] = '-';
										}
									}
									else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[2] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[2] = '-';
										}
									}
									else if (hijosFila[cont].nodeName == "DES_TIPO") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[3] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[3] = '-';
										}
									}
									else if (hijosFila[cont].nodeName == "DES_CRITICO") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[4] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[4] = '-';
										}
									}
								}// for elementos de la fila
								lista[j] = fila;
								fila = new Array();
							}
						}//for(j=0;hijos!=null && j<hijos.length;j++)
						if (codigoOperacion == "0") {
							//jsp_alerta("A",'Correcto');                             
							//window.returnValue = lista;                       DavidL                                                          //                                                        
                                                        jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.registroCreadoOK")%>');  //DavidL                  
                                                        self.parent.opener.retornoXanelaAuxiliar(lista);  //DavidL                                                          
							cerrarVentana();
						} else if (codigoOperacion == "1") {
							jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
						} else if (codigoOperacion == "2") {
							jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
						} else if (codigoOperacion == "3") {
							jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
						}else {
							jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
						}
					} catch (Err) {
					}//try-catch
				} else {
					jsp_alerta("A", mensajeValidacion);
				}
			}
			
			function guardarDatos() {
				if (true) {
					var ajax = getXMLHttpRequest();
					var nodos = null;
					var CONTEXT_PATH = '<%=request.getContextPath()%>';
					var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
					var parametros = "";
					var nuevo = "<%=nuevo%>";

					parametros = "tarea=preparar&modulo=MELANBIDE53&operacion=modificarIdError&tipo=0"
								+ "&id=<%=datDetaError != null && datDetaError.getId() != null ? datDetaError.getId().toString() : ""%>"
								+ "&descC=" + escape(document.getElementById('descCortaDetaErr').value)
								+ "&descripcion=" + escape(document.getElementById('descriptDetaErr').value)
								+ "&codTipo=" + document.getElementById('codListaTipo').value
								+ "&critico=" + escape(document.getElementById('codListaCritico').value)
								+ "&aviso=" + escape(document.getElementById('menAvisoDetaErr').value)
								+ "&correos=" + escape(document.getElementById('mailsAvisoDetaErr').value)
								+ "&acciones=" + escape(document.getElementById('accionesDetaErr').value)
								+ "&mod=" + escape(document.getElementById('moduloOrigen').value)
						;
					try {
						ajax.open("POST", url, false);
						ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
						ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
						//var formData = new FormData(document.getElementById('formContrato'));
						
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
							}//if(navigator.appName.indexOf("Internet Explorer")!=-1)
						}//if (ajax.readyState==4 && ajax.status==200)
						nodos = xmlDoc.getElementsByTagName("RESPUESTA");
						var elemento = nodos[0];
						var hijos = elemento.childNodes;
						var codigoOperacion = null;
						var lista = new Array();
						var fila = new Array();
						var nodoFila; 
						var hijosFila;
						for (j = 0; hijos != null && j < hijos.length; j++) {
							if (hijos[j].nodeName == "CODIGO_OPERACION") {
								codigoOperacion = hijos[j].childNodes[0].nodeValue;
								lista[j] = codigoOperacion;
							}//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
							else if (hijos[j].nodeName == "FILA") {
								nodoFila = hijos[j];
								hijosFila = nodoFila.childNodes;
								for (var cont = 0; cont < hijosFila.length; cont++) {
									if (hijosFila[cont].nodeName == "IDEN_ERR_ID") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[0] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[0] = '-';
										}
									}
									else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC_C") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[1] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[1] = '-';
										}
									}
									else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[2] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[2] = '-';
										}
									}
									else if (hijosFila[cont].nodeName == "DES_TIPO") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[3] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[3] = '-';
										}
									}
									else if (hijosFila[cont].nodeName == "DES_CRITICO") {
										if (hijosFila[cont].childNodes.length > 0) {
											fila[4] = hijosFila[cont].childNodes[0].nodeValue;
										}
										else {
											fila[4] = '-';
										}
									}
								}// for elementos de la fila
								lista[j] = fila;
								fila = new Array();
							}
						}//for(j=0;hijos!=null && j<hijos.length;j++)
						if (codigoOperacion == "0") {
							//jsp_alerta("A",'Correcto');                              
							//window.returnValue = lista;                                                                                                                  
                                                        jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.registroModificadoOK")%>');  //DavidL                                                                                                                
                                                        self.parent.opener.retornoXanelaAuxiliar(lista);  //DavidL                                                         
							cerrarVentana();
						} else if (codigoOperacion == "1") {
							jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
						} else if (codigoOperacion == "2") {
							jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
						} else if (codigoOperacion == "3") {
							jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
						}else {
							jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
						}
					} catch (Err) {
					}//try-catch
				} else {
					jsp_alerta("A", mensajeValidacion);
				}
			}
			
			
					
			function validarDatos() {
				mensajeValidacion = "";
				var correcto = true;
				var observaciones = document.getElementById('idDetalleError').value;
				if (observaciones == null || observaciones == '') {
					mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.id")%>' + ' ';
					//jsp_alerta("A", mensajeValidacion);
					return false;

				}
			return correcto;
			}
			
			var listaCritico = new Array();
			var listaCodigosCritico = new Array();
			listaCodigosCritico[0] = 1;
			listaCodigosCritico[1] = 2;
			listaCodigosCritico[2] = 3;
			listaCodigosCritico[3] = 4;
			listaCritico[0] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.bloq")%>';
			listaCritico[1] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.grave")%>';
			listaCritico[2] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.leve")%>';
			listaCritico[3] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.inf")%>';
			//alert("critico cargado");
			var listaTipo = new Array();
			var listaCodigosTipo = new Array();
			listaCodigosTipo[0] = 1;
			listaCodigosTipo[1] = 2;
			listaTipo[0] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.tipo.sist")%>';
			listaTipo[1] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.tipo.func")%>';
			//alert("tipo cargado");
		</script>

		<div class="contenidoPantalla">
			<form><!--    action="/PeticionModuloIntegracion.do" method="POST" -->
				<div style="width: 100%; padding: 10px; text-align: left;">
					<div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
						<span>
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalleErorres")%>
						</span>
					</div>
					<div style="clear: both;"></div>
					<br>
					<div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.id.fk")%>
						</div>
						<div style="width: 450px; float: left;">
							<div style="float: left;">
								<input id="idDetalleError" name="idDetalleError" type="text" class="inputTextoM53" size="50" maxlength="25" 
								value="<%=datDetaError != null && datDetaError.getId() != null ? datDetaError.getId() : ""%>"/>
							</div>
						</div>
					</div>  
							  <div style="clear: both;"></div>
					<br>
				  
					<div style="clear: both;"></div>
					<br>
					<div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.descripcionErrorCorta")%>
						</div>
						<div style="width: 450px; float: left;">
							<div style="float: left;">
								<div style="width: 450px; float: left;">
									<div style="float: left;">
										<textarea class="inputTextoM53" rows="2" cols="70" id="descCortaDetaErr" name="descCortaDetaErr" maxlength="100"><%=datDetaError != null && datDetaError.getDescripcionCorta() != null ? datDetaError.getDescripcionCorta() : ""%></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div style="clear: both;"></div>
					<br>
					   <div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.descripcionError")%>
						</div>
						<div style="width: 450px; float: left;">
							<div style="float: left;">
								<div style="width: 450px; float: left;">
									<div style="float: left;">
										<textarea class="inputTextoM53" rows="5" cols="70" id="descriptDetaErr" name="descriptDetaErr" maxlength="300" ><%=datDetaError != null && datDetaError.getDescripcion() != null ? datDetaError.getDescripcion() : ""%></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div style="clear: both;"></div>
					<br>
					<div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.tipoError")%>
						</div>
						<div>
							<input type="text" name="codListaTipo" id="codListaTipo" size="2" class="inputTexto" value="<%=datDetaError != null && datDetaError.getTipo() != null ? datDetaError.getTipo() : ""%>" onkeyup="xAMayusculas(this);" />
							<input type="text" name="descListaTipo"  id="descListaTipo" size="45" class="inputTexto" readonly="true" value="<%=datDetaError != null && datDetaError.getDesTipo() != null ? datDetaError.getDesTipo() : ""%>" />
							<a href="" id="anchorListaTipo" name="anchorListaTipo">
								<img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
									 name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
							</a>
							
						</div>
					</div>
					<br><br>
						<div style="clear: both;"></div>
					<br>
					<div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.criticError")%>
						</div>
						<div>
							<input type="text" name="codListaCritico" id="codListaCritico" size="2" class="inputTexto" value="<%=datDetaError != null && datDetaError.getCritico() != null ? datDetaError.getCritico() : ""%>" onkeyup="xAMayusculas(this);" />
							<input type="text" name="descListaCritico"  id="descListaCritico" size="45" class="inputTexto" readonly="true" value="<%=datDetaError != null && datDetaError.getDesCritico() != null ? datDetaError.getDesCritico() : ""%>" />
							<a href="" id="anchorListaCritico" name="anchorListaCritico">
								<img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
									 name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
							</a>
						</div>
						
					</div>
					<br><br>
					<div style="clear: both;"></div>
					<br>
					<div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.avisoError")%>
						</div>
						<div style="width: 450px; float: left;">
							<div style="float: left;">
								<div style="width: 450px; float: left;">
									<div style="float: left;">
										<textarea class="inputTextoM53" rows="5" cols="70" id="menAvisoDetaErr" name="menAvisoDetaErr" maxlength="1000" ><%=datDetaError != null && datDetaError.getAviso() != null ? datDetaError.getAviso() : ""%></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div style="clear: both;"></div>
					<br>
					<div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.mailsError")%>
						</div>
						<div style="width: 450px; float: left;">
							<div style="float: left;">
								<div style="width: 450px; float: left;">
									<div style="float: left;">
										<textarea class="inputTextoM53" rows="5" cols="70" id="mailsAvisoDetaErr" name="mailsAvisoDetaErr" maxlength="1000" ><%=datDetaError != null && datDetaError.getMails() != null ? datDetaError.getMails() : ""%></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div style="clear: both;"></div>
					<br>
					<div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.accionesError")%>
						</div>
						<div style="width: 450px; float: left;">
							<div style="float: left;">
								<div style="width: 450px; float: left;">
									<div style="float: left;">
										<textarea class="inputTextoM53" rows="5" cols="70" id="accionesDetaErr" name="accionesDetaErr" maxlength="1000" ><%=datDetaError != null && datDetaError.getAcciones() != null ? datDetaError.getAcciones() : ""%></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div style="clear: both;"></div>
					<br>
					
					<div class="lineaFormulario">
						<div style="width: 190px; float: left;" class="etiqueta">
							<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.modulo")%>
						</div>
						<div style="width: 450px; float: left;">
							<div style="float: left;">
								<div style="width: 450px; float: left;">
									<div style="float: left;">
										<textarea class="inputTextoM53" rows="5" cols="70" id="moduloOrigen" name="moduloOrigen" maxlength="1000" ><%=datDetaError != null && datDetaError.getModulo() != null ? datDetaError.getModulo() : ""%></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<br><br>
						
						<br />
						<div style="clear: both;">
						<div>
							<div class="botonera" style="text-align: center;">
								<input type="button" id="btnNuevo" name="btnNuevo" class="botonGeneral"  value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.guadar")%>" onclick="nuevoError();">
								<input type="button" id="btnModif" name="btnModif" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="guardarDatos();">
								<input type="button" id="btnVolver" name="btnVolver" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cancelar();"/>   
							</div>
						</div>
					</div>
				</div>
				<script type="text/javascript">
					var comboCritico = new Combo("ListaCritico");
					comboCritico.addItems(listaCodigosCritico, listaCritico);
					comboCritico.change = critico;
					
					
					var comboTipo = new Combo("ListaTipo");
					comboTipo.addItems(listaCodigosTipo, listaTipo);
					comboTipo.change = tipo;
					
					var nuevo = "<%=nuevo%>";
					if(nuevo==0){
                                                document.getElementById("idDetalleError").disabled=true;        //DavidL
                                                
						document.getElementById("btnModif").style.display = "inline";
						document.getElementById("btnNuevo").style.display = "none";
					}else{
						document.getElementById("btnModif").style.display = "none";
						document.getElementById("btnNuevo").style.display = "inline";
					}
					
				</script>
			</form>
		</div>
	</div>
</body>