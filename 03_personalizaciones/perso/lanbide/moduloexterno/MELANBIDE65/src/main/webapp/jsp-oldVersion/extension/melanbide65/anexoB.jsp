<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.i18n.MeLanbide65I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.vo.EncargadoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>


<%
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null)
    {
        try
        {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        }
        catch(Exception ex)
        {}
    }

    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExpediente");
%>

<script type="text/javascript">
    function pulsarNuevoTecnico() {
        var urlParam = 'tarea=preparar&modulo=MELANBIDE65&operacion=cargarFormularioAltaTec&tipo=0&numExp=<%=numExpediente%>';
        var result = null;
        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?' + urlParam, 550, 1000, 'no', 'no');


        if (result != undefined) {
            if (result[0] == '0') {
                recargarTablaTecnico(result);
            }
        }
    
    }
    
    function pulsarModificarTecnico() {
        if (TablaAnexoB.selectedIndex != -1) {
            var result = null;
            var urlParam = 'tarea=preparar&modulo=MELANBIDE65&operacion=cargarFormularioModTec&tipo=0&numExp=<%=numExpediente%>&id=' + listaTecnicos[TablaAnexoB.selectedIndex][0];
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?' + urlParam, 650, 1000, 'no', 'no');
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaTecnico(result);
                }
            }
        }
        else {
            jsp_alerta('A', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarEliminarTecnico() {
        if (TablaAnexoB.selectedIndex != -1) {
            var eliminar = jsp_alerta('', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarFila")%>');
            if (eliminar == 1) {

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                parametros = 'tarea=preparar&modulo=MELANBIDE65&operacion=eliminarTecnico&tipo=0&expediente=<%=numExpediente%>&identificador=' + listaTecnicos[TablaAnexoB.selectedIndex][0];
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for (var j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaNueva[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].nodeName == "ID") {
                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "SEXO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DNI") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECHAALTA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "JORNADACOMPLETA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "JORNADAPARCIALPOR") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[8].toString();
                                        tex = tex.replace(".", ",");
                                        fila[8] = tex;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "JORNADAPARCIALPAG") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[9] = '-';
                                    }
                                } 
                            }// for elementos de la fila
                            listaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaTecnico(listaNueva);
                    } else  if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorParsearDatos")%>');
                    } else if (codigoOperacion == "3" || codigoOperacion == "5") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                    } else if (codigoOperacion == "4") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                    } else if (codigoOperacion == "6") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                    } else if (codigoOperacion == "7") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                    } else if (codigoOperacion == "-1") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                    }else {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        }
        else {
            jsp_alerta('A', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function crearTablaTecnico(){
        TablaAnexoB = new Tabla(document.getElementById('tablaAnexoB'), 880);
        TablaAnexoB.addColumna('25','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBId")%>");
        TablaAnexoB.addColumna('50','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBNumLinea")%>");
        TablaAnexoB.addColumna('250','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBNombreC")%>");
        TablaAnexoB.addColumna('50','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBSexo")%>");
        TablaAnexoB.addColumna('70','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBDNI")%>");
        TablaAnexoB.addColumna('95','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBFechaAlta")%>");
        TablaAnexoB.addColumna('95','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBJornadaLaboralCompletaA")%>");
        TablaAnexoB.addColumna('95','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBJornadaLaboralParcialPorcjB")%>");
        TablaAnexoB.addColumna('95','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBJornadaLaboralParcialPagC")%>");

        TablaAnexoB.displayCabecera=true;
        TablaAnexoB.height = 300;
    }
      
    function recargarTablaTecnico(result) {
            var fila; 
            listaTecnicos = new Array();
            listaTecnTabla = new Array();
            for (var i = 1; i < result.length; i++) {
                var sexo;
                fila = result[i];
                listaTecnicos[i - 1] = fila;
                if(fila[4]=="1") sexo="Varón";
                else if(fila[4]=="2") sexo="Mujer";
                else sexo=fila[4];
                listaTecnTabla[i - 1] = [fila[0], fila[1], fila[2] +", "+fila[3],
                    sexo,fila[5],fila[6],fila[7],fila[8],fila[9]];
            }
            
            crearTablaTecnico();
            
            TablaAnexoB.lineas = listaTecnTabla;
            TablaAnexoB.displayTabla();
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                try{
                    var div = document.getElementById('tablaAnexoB');
                    div.children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].style.width = '100%';
                }
                catch(err){

                }
            }
        }

</script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide65/melanbide65.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<body>
 <div class="tab-page" id="tabPage652" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana652"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAnexoB")%></h2>       
    <br/>
    <form>
        <h2 class="legendAzul" id="pestanaPrincAnexoB"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalAnexoB")%></h2>
        <div> 
            <div>
                <br/>
                <div id="divGeneralAnexoB"  style="overflow-y: auto; overflow-x: hidden; height: 290px;">     
                    <div id="tablaAnexoB" style="padding: 5px; width:990px; height: 280px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                </div>
                <br/><br>
                <div class="botonera" style="text-align: center; margin-top: 100px">
                    <input type="button" id="btnNuevoAnexoB" name="btnNuevoAnexoB" class="botonGeneral"  value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoTecnico();">
                    <input type="button" id="btnModificarAnexoB" name="btnModificarAnexoB" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTecnico();">
                    <input type="button" id="btnEliminarAnexoB" name="btnEliminarAnexoB"   class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTecnico();">
                </div>
            </div>
        </div>
    </form>    
 </div>
     <script  type="text/javascript">
        var TablaAnexoB;
        var listaTecnicos = new Array();
        var listaTecnTabla = new Array();
        
        crearTablaTecnico();

        <%  		
            EncargadoVO objectVO = null;
            ArrayList<EncargadoVO> relacion = null;
            if(request.getAttribute("relacionTecnicos")!=null){
                relacion = (ArrayList<EncargadoVO>)request.getAttribute("relacionTecnicos");
            }													
            if (relacion!= null && relacion.size() >0){
                for (int indice=0;indice<relacion.size();indice++)
                {
                    objectVO = relacion.get(indice);

        %>
            listaTecnicos[<%=indice%>] = ['<%=objectVO.getIdentificador()%>','<%=objectVO.getNumLinea()%>','<%=objectVO.getApellidos()%>','<%=objectVO.getNombre()%>' ,
                '<%=objectVO.getSexo()%>','<%=objectVO.getDni()%>','<%=objectVO.getFecAltaContrIndef()%>','<%=objectVO.getJornadaCompleta()%>'
                ,'<%=objectVO.getJornadaParcialPor()%>','<%=objectVO.getJornadaParcialPag()%>'];
            listaTecnTabla[<%=indice%>] = ['<%=objectVO.getIdentificador()%>','<%=objectVO.getNumLinea()%>','<%=objectVO.getApellidos()%>'+', '+ '<%=objectVO.getNombre()%>' ,
                '<%=(objectVO.getSexo())==1?"Varón":"Mujer"%>','<%=objectVO.getDni()%>','<%=objectVO.getFecAltaAsStr()%>','<%=objectVO.getJornadaCompleta()%>'
                ,'<%=String.valueOf(objectVO.getJornadaParcialPor()).replace(".",",")%>','<%=objectVO.getJornadaParcialPag()%>'];
            
        <%
                }// for
            }// if
        %>

        TablaAnexoB.lineas=listaTecnTabla;
        TablaAnexoB.displayTabla();
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            try{
                var div = document.getElementById('tablaAnexoB');
                div.children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].style.width = '100%';
            }
            catch(err){

            }
        }
        
    </script>
</body>

