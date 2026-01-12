<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.AltaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>

<%
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null) {
        try {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        }
        catch(Exception ex)
        {}
    }
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int apl = 5;
    String css = "";
    if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            apl = usuarioVO.getAppCod();
            idiomaUsuario = usuarioVO.getIdioma();
            css = usuarioVO.getCss();
    }

    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");
    //String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    //String nombreModulo     = request.getParameter("nombreModulo");
    
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript">
    function pulsarNuevoAlta() {
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarNuevoAlta&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 650, 1000, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaAltas(result);
                }
            }
        });
    }

    function pulsarModificarAlta() {
        if (tabaAltas.selectedIndex != -1) {
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarModificarAlta&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaAltas[tabaAltas.selectedIndex][0] + '&control=' + control.getTime(), 650, 1000, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaAltas(result);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarExportarAnexoAlta() {
        var apellidos = "";
        var control = new Date();
        if (document.getElementById('apellidos').value == null || document.getElementById('apellidos').value == '') {
            apellidos = "";
        } else {
            apellidos = document.getElementById('apellidos').value.replace(/\'/g, "''");
        }
        var parametros = "";
        parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=generarExcelAnexoAltas&tipo=0&numExp=<%=numExpediente%>&apellidos=' + escape(apellidos) + '&numLinea=' + document.getElementById('numLinea').value + '&nif=' + document.getElementById('nif').value + '&control=' + control.getTime();
        window.open(baseUrl + parametros, "_blank");
    }

    function pulsarEliminarAlta() {
        if (tabaAltas.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarAlta")%>');

            if (resultado == 1) {

                var ajax = getXMLHttpRequest();
                var nodos = null;

                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=eliminarAlta&tipo=0&numExp=<%=numExpediente%>&id=' + listaAltas[tabaAltas.selectedIndex][0] + '&control=' + control.getTime();
                try {
                    ajax.open("POST", baseUrl, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");

                    var listaAltasNueva = extraerListaAltas(nodos);
                    var codigoOperacion = listaAltasNueva[0];

                    if (codigoOperacion == "0") {
                        recargarTablaAltas(listaAltasNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarLimpiarAlta() {
        document.getElementById("numLinea").value = "";
        document.getElementById("nif").value = "";
        document.getElementById("apellidos").value = "";
        pulsarBuscarAlta();
    }

    function pulsarBuscarAlta() {
        mensajeValidacion = "";
        if (validarNumericoVacio(document.getElementById('numLinea').value)) {
            if (validarTresCaracteresApellido(document.getElementById('apellidos').value)) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var parametros = "";
                var control = new Date();
                var apellidos = "";
                var nif = "";

                if (document.getElementById('apellidos').value == null || document.getElementById('apellidos').value == '') {
                    apellidos = "";
                } else {
                    apellidos = document.getElementById('apellidos').value.replace(/\'/g, "''");
                }

                if (document.getElementById('nif').value == null || document.getElementById('nif').value == '') {
                    nif = "";
                } else {
                    nif = document.getElementById('nif').value.toUpperCase();
                }

                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=buscarAlta&tipo=0&numExp=<%=numExpediente%>&apellidos=' + escape(apellidos) + '&numLinea=' + document.getElementById('numLinea').value + '&nif=' + nif + '&control=' + control.getTime();
                try {
                    ajax.open("POST", baseUrl, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");

                    var listaAltasNueva = extraerListaAltas(nodos);
                    var codigoOperacion = listaAltasNueva[0];

                    if (codigoOperacion == "0") {
                        recargarTablaAltas(listaAltasNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else if (codigoOperacion == "4") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            } else {
                mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellidos.errLongitud")%>';
                jsp_alerta("A", mensajeValidacion);
            }
        } else {
            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
            jsp_alerta("A", mensajeValidacion);
        }
    }

    function extraerListaAltas(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaNueva = new Array();
        var fila = new Array();
        var nodoFila;
        var hijosFila;
        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                listaNueva[j] = codigoOperacion;
            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
            else if (hijos[j].nodeName == "FILA") {
                nodoFila = hijos[j];
                hijosFila = nodoFila.childNodes;
                for (var cont = 0; cont < hijosFila.length; cont++) {
                    if (hijosFila[cont].nodeName == "ID") {
                        if (hijosFila[cont].childNodes.length > 0) {
                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[0] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[1] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[2] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NOMBRE") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[3] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "FECHAALTA") {
                        if (hijosFila[cont].childNodes.length > 0) {
                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[4] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NIF") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[5] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NUMSS") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[6] = '-';
                        }
                    }
                    /*else if (hijosFila[cont].nodeName == "CAUSA") {
                     if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                     fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                     }
                     else {
                     fila[7] = '-';
                     }
                     }*/
                    else if (hijosFila[cont].nodeName == "DES_NOM") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[7] = '-';
                        }
                    }
                }
                listaNueva[j] = fila;
                fila = new Array();
            }
        }//for(j=0;hijos!=null && j<hijos.length;j++)
        return listaNueva;
    }

    function crearTablaAltas() {
        tabaAltas = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaAltas'));
        tabaAltas.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"altas.tablaAltas.col1")%>");
        tabaAltas.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"altas.tablaAltas.col6")%>");
        tabaAltas.addColumna('350', 'left', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"altas.tablaAltas.col2")%>");
        tabaAltas.addColumna('150', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"altas.tablaAltas.col3")%>");
        tabaAltas.addColumna('150', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"altas.tablaAltas.col4")%>");
        tabaAltas.addColumna('150', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"altas.tablaAltas.col5")%>");
        tabaAltas.addColumna('150', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"altas.tablaAltas.col7")%>");

        tabaAltas.displayCabecera = true;
        tabaAltas.height = 300;
    }

    function recargarTablaAltas(result) {
        var fila;
        listaAltas = new Array();
        listaAltasTabla = new Array();
        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            listaAltas[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7]];
            listaAltasTabla[i - 1] = [fila[0], fila[1], fila[2] + ", " + fila[3],
                fila[4], fila[5], fila[6], fila[7]];
        }

        crearTablaAltas();

        tabaAltas.lineas = listaAltasTabla;
        tabaAltas.displayTabla();
//        if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
//            try {
//                var div = document.getElementById('listaAltas');
//                if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
//                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
//                    div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
//                } else {
//                    div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
//                    div.children[0].children[1].children[0].children[0].style.width = '100%';
//                }
//            } catch (err) {
//
//            }
//        }
    }

</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<div class="tab-page" id="tabPage355" style="height:520px; width: 100%;">
    <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalAltas")%></h2>
    <div>
        <br/>
            <div>
            <div class="lineaFormulario">
                <div style="width: 120px; float: left;" class="etiqueta">
                    <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                </div>
                <div style="width: 120px; float: left;">
                    <div style="float: left;">
                        <input id="numLinea" name="numLinea" type="text" class="inputTexto" size="15" maxlength="5" 
                               value=""/>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 50px; float: left; padding-left: 15px;" class="etiqueta">
                    <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nif")%>
                </div>
                <div style="width: 120px; float: left;">
                    <div style="float: left;">
                        <input id="nif" name="nif" type="text" class="inputTexto" size="15" maxlength="15" 
                               onkeyup="xAMayusculas(this);" value=""/>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 70px; float: left; padding-left: 15px;" class="etiqueta">
                    <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                </div>
                <div style="width: 120px; float: left;">
                    <div style="float: left;">
                        <input id="apellidos" name="apellidos" type="text" class="inputTexto" size="30" maxlength="50" 
                               value=""/>
                    </div>
                </div>
                <div class="botonera" style="text-align: right; padding-left: 20px;">
                    <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.limpiar")%>" onclick="pulsarLimpiarAlta();">
                    <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBuscarAlta();">
                </div>
            </div>
        </div>
        <br/>
        <div id="divGeneral" >
            <div id="listaAltas"  align="center"></div>
        </div>
        <br/><br>
        <div class="botonera" style="text-align: center;">
            <input type="button" id="btnNuevoAlta" name="btnNuevoAlta" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoAlta();">
            <input type="button" id="btnModificarAlta" name="btnModificarAlta" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarAlta();">
            <input type="button" id="btnEliminarAlta" name="btnEliminarAlta"   class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarAlta();">
            <input type="button" id="btnExportar" name="btnExportar"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarAnexoAlta();">
        </div>
    </div>          
</div>
<script  type="text/javascript">
    //Tabla Altas
    var tabaAltas;
    var listaAltas = new Array();
    var listaAltasTabla = new Array();
    crearTablaAltas();
    <%  		
       AltaVO objectVO = null;
       List<AltaVO> List = null;
       if(request.getAttribute("listaAltas")!=null){
           List = (List<AltaVO>)request.getAttribute("listaAltas");
       }													
       if (List!= null && List.size() >0){
       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide58.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
        for (int indice=0;indice<List.size();indice++)
           {                        
               objectVO = List.get(indice);
               
               String fecAltaFormat="";
               if(objectVO.getFechaAlta()!=null){
                   fecAltaFormat=dateFormat.format(objectVO.getFechaAlta());
                
               }else{
                   fecAltaFormat="-";
               }

               String numLinea="";
               if(objectVO.getNumLinea()!=null && !"".equals(objectVO.getNumLinea())){
                   numLinea=Integer.toString(objectVO.getNumLinea());
                
               }else{
                   numLinea="-";
               }

               String apellidos="";
               if(objectVO.getApellidos()!=null){
                   apellidos=objectVO.getApellidos();
               }else{
                   apellidos="-";
               }

               String nombre="";
               if(objectVO.getNombre()!=null){
                   nombre=objectVO.getNombre();
               }else{
                   nombre="-";
               }

               String nif="";
               if(objectVO.getNif()!=null){
                   nif=objectVO.getNif();
               }else{
                   nif="-";
               }

               String nSS="";
               if(objectVO.getNumSS()!=null){
                   nSS=objectVO.getNumSS();
               }else{
                   nSS="-";
               }

               String causa="";
               if(objectVO.getCausa()!=null){
                   causa=objectVO.getCausa();
               }else{
                   causa="-";
               }

               String causaDesc="";
               if(objectVO.getCausaDesc()!=null){
                   causaDesc=objectVO.getCausaDesc();
                    String[] descripcionDobleIdioma = causaDesc.split(barraSeparadoraDobleIdiomaDesple);
                    if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                        if(idiomaUsuario==4){
                            causaDesc =descripcionDobleIdioma[1];
                        } else{
                            causaDesc =descripcionDobleIdioma[0];
                        }
                    }     
                }else{
                   causaDesc="-";
                }
                        

    %>
    listaAltas[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
        '<%=fecAltaFormat%>', '<%=nif%>', '<%=nSS%>', '<%=causaDesc%>'];
    listaAltasTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
        '<%=fecAltaFormat%>', '<%=nif%>', '<%=nSS%>', '<%=causaDesc%>'];
    <%
           }// for
       }// if
    %>

    tabaAltas.lineas = listaAltasTabla;
    tabaAltas.displayTabla();

    if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
        try {
            var div = document.getElementById('listaAltas');
            //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
            } else {
                div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                div.children[0].children[1].children[0].children[0].style.width = '100%';
            }
        } catch (err) {

        }
    }
</script>
<div id="popupcalendar" class="text"></div>               

