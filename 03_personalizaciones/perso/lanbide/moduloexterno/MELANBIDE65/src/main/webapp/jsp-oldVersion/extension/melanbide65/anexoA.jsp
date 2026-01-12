<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.i18n.MeLanbide65I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.vo.TrabajadorVO" %>
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
    function pulsarNuevoTrabajador() {
        var urlParam = 'tarea=preparar&modulo=MELANBIDE65&operacion=cargarFormularioAltaTr&tipo=0&numExp=<%=numExpediente%>';
        var result = null;
        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?' + urlParam, 650, 1000, 'no', 'no');


        if (result != undefined) {
            if (result[0] == '0') {
                recargarTablaTrabajador(result);
            }
        }
    
    }
    
    function pulsarModificarTrabajador() {
        if (TablaAnexoA.selectedIndex != -1) {
            var result = null;
            var urlParam = 'tarea=preparar&modulo=MELANBIDE65&operacion=cargarFormularioModTr&tipo=0&numExp=<%=numExpediente%>&id=' + listaTrabajadores[TablaAnexoA.selectedIndex][0];
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?' + urlParam, 650, 1000, 'no', 'no');
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaTrabajador(result);
                }
            }
        }
        else {
            jsp_alerta('A', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarEliminarTrabajador() {
        if (TablaAnexoA.selectedIndex != -1) {
            var eliminar = jsp_alerta('', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarFila")%>');
            if (eliminar == 1) {

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                parametros = 'tarea=preparar&modulo=MELANBIDE65&operacion=eliminarTrabajador&tipo=0&expediente=<%=numExpediente%>&identificador=' + listaTrabajadores[TablaAnexoA.selectedIndex][0];
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
                                } else if (hijosFila[cont].nodeName == "DISCPSIQUICA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DISCFISICA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRINDEFCOMPL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRINDEFPARCIAL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[9].toString();
                                        tex = tex.replace(".", ",");
                                        fila[9] = tex;
                                    } else {
                                        fila[9] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRINDEFPARCIALPAG") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[10] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRTEMPCOMPL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[11] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRTEMPPARCIAL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[12].toString();
                                        tex = tex.replace(".", ",");
                                        fila[12] = tex;
                                    } else {
                                        fila[12] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRTEMPPARCIALPAG") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[13] = '-';
                                    }
                                }
                            }// for elementos de la fila
                            listaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaTrabajador(listaNueva);
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

    function crearTablaTrabajador(){
        TablaAnexoA = new Tabla(document.getElementById('tablaAnexoA'), 1070);
        TablaAnexoA.addColumna('25','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoAId")%>");
        TablaAnexoA.addColumna('50','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoANumLinea")%>");
        TablaAnexoA.addColumna('250','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoANombreC")%>");
        TablaAnexoA.addColumna('50','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoASexo")%>");
        TablaAnexoA.addColumna('70','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoADNI")%>");
        TablaAnexoA.addColumna('50','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoATipoYGradoDisPsiquicaA")%>");
        TablaAnexoA.addColumna('50','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoATipoYGradoDisFisicaB")%>");
        TablaAnexoA.addColumna('75','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoADurContratoIndefinidoC")%>");
        TablaAnexoA.addColumna('75','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoADurContratoIndefinidoJornadaD")%>");
        TablaAnexoA.addColumna('75','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoADurContratoIndefinidoJornadaNPagE")%>");
        TablaAnexoA.addColumna('75','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoADurContratoTemporalF")%>");
        TablaAnexoA.addColumna('75','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoADurContratoTemporalJornadaG")%>");
        TablaAnexoA.addColumna('75','center',"<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoADurContratoJornadaNPagH")%>");

        TablaAnexoA.displayCabecera=true;
        TablaAnexoA.height = 300;
    }
      
    function recargarTablaTrabajador(result) {
            var fila; 
            listaTrabajadores = new Array();
            listaTrabTabla = new Array();
            for (var i = 1; i < result.length; i++) {
                var sexo;
                fila = result[i];
                listaTrabajadores[i - 1] = fila;
                if(fila[4]=="1") sexo="Varón";
                else if(fila[4]=="2") sexo="Mujer";
                else sexo=fila[4];
                listaTrabTabla[i - 1] = [fila[0], fila[1], fila[2] +", "+fila[3],
                    sexo,fila[5],fila[6],fila[7],fila[8],fila[9],fila[10],fila[11],fila[12],fila[13]];
            }
            
            crearTablaTrabajador();
            
            TablaAnexoA.lineas = listaTrabTabla;
            TablaAnexoA.displayTabla();
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                try{
                    var div = document.getElementById('tablaAnexoA');
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
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

<body>
 <div class="tab-page" id="tabPage651" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana651"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAnexoA")%></h2>       
    <br/>
    <form>
        <h2 class="legendAzul" id="pestanaPrincAnexoA"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalAnexoA")%></h2>
        <div> 
            <div>
                <br/>
                <div id="divGeneralAnexoA"  style="overflow-y: auto; overflow-x: hidden; height: 290px;">     
                    <div id="tablaAnexoA" style="padding: 5px; width:990px; height: 280px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                </div>
                <br/><br>
                <div class="botonera" style="text-align: center; margin-top: 100px">
                        <input type="button" id="btnNuevoAnexoA" name="btnNuevoAnexoA" class="botonGeneral"  value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoTrabajador();">
                        <input type="button" id="btnModificarAnexoA" name="btnModificarAnexoA" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTrabajador();">
                        <input type="button" id="btnEliminarAnexoA" name="btnEliminarAnexoA"   class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTrabajador();">
                </div>
            </div>
        </div>
    </form>    
 </div>
     <script  type="text/javascript">
        var TablaAnexoA;
        var listaTrabajadores = new Array();
        var listaTrabTabla = new Array();
        
        crearTablaTrabajador();

        <%  		
            TrabajadorVO objectVO = null;
            ArrayList<TrabajadorVO> relacion = null;
            if(request.getAttribute("relacionTrabajadores")!=null){
                relacion = (ArrayList<TrabajadorVO>)request.getAttribute("relacionTrabajadores");
            }													
            if (relacion!= null && relacion.size() >0){
                for (int indice=0;indice<relacion.size();indice++)
                {
                    objectVO = relacion.get(indice);

        %>
            listaTrabajadores[<%=indice%>] = ['<%=objectVO.getIdentificador()%>','<%=objectVO.getNumLinea()%>','<%=objectVO.getApellidos()%>','<%=objectVO.getNombre()%>' ,
                '<%=objectVO.getSexo()%>','<%=objectVO.getDni()%>','<%=objectVO.getTipoDiscPsiquica()%>','<%=objectVO.getTipoDiscFisica()%>'
                ,'<%=objectVO.getDurContrIndefComp()%>','<%=objectVO.getDurContrIndefParcialJ()%>','<%=objectVO.getDurContrIndefParcialP()%>'
                ,'<%=objectVO.getDurContrTempComp()%>','<%=objectVO.getDurContrTempParcialJ()%>','<%=objectVO.getDurContrTempParcialP()%>'];
            listaTrabTabla[<%=indice%>] = ['<%=objectVO.getIdentificador()%>','<%=objectVO.getNumLinea()%>','<%=objectVO.getApellidos()%>'+', '+ '<%=objectVO.getNombre()%>' ,
                '<%=(objectVO.getSexo())==1?"Varón":"Mujer"%>','<%=objectVO.getDni()%>','<%=objectVO.getTipoDiscPsiquica()%>','<%=objectVO.getTipoDiscFisica()%>'
                ,'<%=objectVO.getDurContrIndefComp()%>','<%=String.valueOf(objectVO.getDurContrIndefParcialJ()).replace(".",",")%>','<%=objectVO.getDurContrIndefParcialP()%>'
                ,'<%=objectVO.getDurContrTempComp()%>','<%=String.valueOf(objectVO.getDurContrTempParcialJ()).replace(".",",")%>','<%=objectVO.getDurContrTempParcialP()%>'];
            
        <%
                }// for
            }// if
        %>

        TablaAnexoA.lineas=listaTrabTabla;
        TablaAnexoA.displayTabla();
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            try{
                var div = document.getElementById('tablaAnexoA');
                div.children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].style.width = '100%';
            }
            catch(err){

            }
        }
        
    </script>
    <div id="popupcalendar" class="text"></div>  
</body>

