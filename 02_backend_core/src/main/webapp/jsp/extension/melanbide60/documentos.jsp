<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.documentos.DocumentosVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
int idiomaUsuario = 1;
 int apl = 5;
 String css = "";
    
    String sIdioma = request.getParameter("idioma");
    idiomaUsuario = Integer.parseInt(sIdioma);
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
     //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();
   String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
   
    String numDoc = (String)request.getAttribute("numDoc");
    String codTram = request.getParameter("codigoTramite");

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>




<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>

</head>
<body>


    <div class="tab-page"id="tabPage331" style="height:420px; width: 98%;">

        <h2 class="tab" id="pestana331"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.documentos")%></h2>
        <script>  tp1_p331 = tp1.addTabPage(document.getElementById("tabPage331"));</script>

        <div style="clear: both;">
            <label class="legendAzul" style="text-align: center; position: relative; left: 5px;"><%=meLanbide50I18n.getMensaje(idiomaUsuario, "label.documentos")%></label>
            <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 440px;">     <!--onscroll="deshabilitarRadios();"-->
                <div id="listDocumentos" align="center"></div>
                <input type="button" name="btnAltaRegistro" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.documentos")%>" class="botonGeneral" onclick="pulsarBoton()" />       

                <div>

                    
                    <label>Nombre del documento:</label>
                    <input type="text" name="txtnombredocumentacion" value=""  size="120px"  id="txtnombredocumentacion"><br>
                    <label>Fecha:</label>
                    <input type="text" name="txtfechadocumento" value="" id="txtfechadocumento"><br>
                    <input type="button" name="btnBuscarDocumento" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.documentos")%>" class="botonGeneral" onclick="buscar()" />        


                </div> 
                </br>
                <div>
                    <div id="listDocumentos2" align="center"></div>
                </div>

            </div>


        </div>
    </div>
    <script type="text/javascript">
        //Tabla Especialidades
        var listDocumentos = new Array();
        var listDocumentosTabla = new Array();
        var tabEspecialidades = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listDocumentos'));
        //tabEspecialidades = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listDocumentos'), 870);
        tabEspecialidades.addColumna('150', 'center', "TIPO_DOCUMENTO");
        tabEspecialidades.addColumna('600', 'left', "NOMBRE_DOCUMENTO");
        tabEspecialidades.addColumna('80', 'center', "ORGANO");
        tabEspecialidades.addColumna('90', 'center', "FECHA");
        tabEspecialidades.addColumna('90', 'center', "TIPO DOCUMENTAL");
        tabEspecialidades.displayCabecera = true;
        tabEspecialidades.height = 150;
        <%  		
                DocumentosVO objectVO = null;
                List<DocumentosVO> List = (List<DocumentosVO>)request.getAttribute("listDocumentos");													
                if (List!= null && List.size() >0){
                    for (int indice=0;indice<List.size();indice++)
                    {
                        objectVO = List.get(indice);

        %>
        listDocumentosTabla[<%=indice%>] = ['<%=objectVO.getTIPO_DOCUMENTO()%>', '<%=objectVO.getNOMBRE_DOCUMENTO()%>', '<%=objectVO.getORGANO()%>', '<%=objectVO.getFECHA()%>', '<%=objectVO.getTIPO_DOCUMENTAL()%>'];
        listDocumentos[<%=indice%>] = ['<%=objectVO.getTIPO_DOCUMENTO()%>', '<%=objectVO.getNOMBRE_DOCUMENTO()%>', '<%=objectVO.getORGANO()%>', '<%=objectVO.getFECHA()%>', '<%=objectVO.getTIPO_DOCUMENTAL()%>'];
        <%
                    }// for
                }// if
        %>


        tabEspecialidades.lineas = listDocumentosTabla;
        tabEspecialidades.dblClkFunction = 'callFromTableTo';
        tabEspecialidades.displayTabla();</script>



    <script>

         var documentoenviado;
     //Tabla  dos
        var listDocumentos2 = new Array();
        var listDocumentosTabla2 = new Array();
        var tabEspecialidades2 = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listDocumentos2'));
        tabEspecialidades2.addColumna('150', 'center', "TIPO_DOCUMENTO");
        tabEspecialidades2.addColumna('600', 'left', "NOMBRE_DOCUMENTO");
        tabEspecialidades2.addColumna('80', 'center', "ORGANO");
        tabEspecialidades2.addColumna('90', 'center', "FECHA");
        tabEspecialidades2.addColumna('90', 'center', "TIPO DOCUMENTAL");
        tabEspecialidades2.displayCabecera = true;
        tabEspecialidades2.height = 150;

        function buscar() {
            alert("Pulsado Buscar bbbb");
            var numeroexpediente = "<%=numExpediente%>";
            alert("Esto es un numero de expediente:" + numeroexpediente);
            documentoenviado =  document.getElementById("txtnombredocumentacion").value + "," + document.getElementById("txtfechadocumento").value + "," + numeroexpediente;
            alert("Montamos el objeto para enviarlo a " + documentoenviado);
            //enviarDocumentoFormulario(documentoenviado);

            alert("Creado el objeto lo mandamos pro ajax al metodo BusquedaDocumentosAportadosDokusi()");
              
             //parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=BusquedaDocumentosAportadosDokusi&tipo=0&numero=" + documentoenviado;
             //alert(parametros);
            
             alert("Longitud de el array para listar la segunda tabla:"+listDocumentosTabla2.length);
var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=BusquedaDocumentosAportadosDokusi&tipo=0&numero=" + documentoenviado;
        alert(parametros);
		try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                //var formData = new FormData(document.getElementById('formContrato'));
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
                                var listaNueva = new Array();
                                var fila = new Array();
                                var nodoFila;
                                var hijosFila;
                                for(j=0;hijos!=null && j<hijos.length;j++){
                                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        listaNueva[j] = codigoOperacion;
                                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                    else if(hijos[j].nodeName=="FILA"){
                                        nodoFila = hijos[j];
                                        hijosFila = nodoFila.childNodes;
                                        for(var cont = 0; cont < hijosFila.length; cont++){
                                            if(hijosFila[cont].nodeName=="ID"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[0] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="SER_NUM"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                    listDocumentosTabla2.push(fila[1]);
                                                }
                                                else{
                                                    fila[1] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="SER_DESC"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                     listDocumentosTabla2.push(fila[2]);
                                                }
                                                else{
                                                    fila[2] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="SER_UBIC"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                     listDocumentosTabla2.push(fila[3]);
                                                }
                                                else{
                                                    fila[3] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="SER_SUPE"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                     listDocumentosTabla2.push(fila[4]);
                                                }
                                                else{
                                                    fila[4] = '-';
                                                }
                                            }
                                        }
                                        alert("listanueva"+listaNueva.length);
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                        
                                    }   
                                }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if(codigoOperacion=="0"){
                                    tabEspecialidades2.lineas = listaNueva;
                                    tabEspecialidades2.dblClkFunction = 'callFromTableTo';
                                    tabEspecialidades2.displayTabla();
                                }
                                
                                
          }catch(Err){
               // alert("Error de envio ajax post no es correcto");
            }             
                
    }
       

        function recargarTablaDisponibilidad(result){
                    var fila;
                     listDocumentos2 = new Array();
                     listDocumentosTabla2 = new Array();
                    for(var i = 1;i< result.length; i++){
                        fila = result[i];
                        //listaDisponibilidad[i-1] = fila;//no funciona ie9
			listDocumentos2[i-1]=[fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8]];
                        listDocumentosTabla2[i-1] =[fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8]];
                    }
                    tabEspecialidades2 = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listDocumentos2'));
                    tabEspecialidades2.addColumna('150', 'center', "TIPO_DOCUMENTO");
                    tabEspecialidades2.addColumna('510', 'left', "NOMBRE_DOCUMENTO");
                    tabEspecialidades2.addColumna('80', 'center', "ORGANO");
                    tabEspecialidades2.addColumna('90', 'center', "FECHA");
                    tabEspecialidades2.displayCabecera = true;
                    tabEspecialidades2.height = 150;
                    tabDisponibilidad.lineas=listDocumentosTabla2;
                    tabDisponibilidad.displayTabla();
                    
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        try{
                           var div = document.getElementById('listDocumentos2');
                    div.children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].style.width = '100%';
                        }
                        catch(err){
                        }
                    }
                }        

        function pulsarBoton() {
            //alert("Pulsado boton ");
            if (tabEspecialidades.selectedIndex != -1) {
                //var tipoDocAnterior = listDocumentos[tabEspecialidades.selectedIndex][0];
                var nombreDocAnterior = listDocumentos[tabEspecialidades.selectedIndex][0];
                //var organoDocAnterior = listDocumentos[tabEspecialidades.selectedIndex][2];
                var fechaDocAnterior = listDocumentos[tabEspecialidades.selectedIndex][1];
                var codigo = tabEspecialidades.selectedIndex;
                //alert("pulsado boton " + tipoDocAnterior + " " + nombreDocAnterior + " " + organoDocAnterior + " " + fechaDocAnterior);
                //alert("Enviamos datos al formulario.");
                //document.getElementById("txtipodocumentacion").value = tipoDocAnterior;
                document.getElementById("txtnombredocumentacion").value = nombreDocAnterior;
                //document.getElementById("txtorgano").value = organoDocAnterior;
                document.getElementById("txtfechadocumento").value = fechaDocAnterior;
            }
        }



        if (navigator.appName.indexOf("Internet Explorer") != -1) {
            try {
                var div = document.getElementById('listDocumentos');
                div.children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].style.width = '100%';
            }
            catch (err) {

            }
        }
        document.getElementById('listDocumentos').children[0].children[1].children[0].children[0].ondblclick = function (event) {
            //  pulsarListasxEspecialidad(event);

        }


        function recargarTablaEspecialidades(result) {
            var fila;
            listDocumentos = new Array();
            listDocumentosTabla = new Array();
            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                //listDocumentos[i-1] = fila;//NO FUNCIONA IE9
                listDocumentos[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6]];
                listDocumentosTabla[i - 1] = [fila[1], fila[2]]; //(fila[5]==0) ? 'S' : (fila[5]==1) ? 'N' : '-'
            }
            tabEspecialidades = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('divlistDocumentos'), 765);
            tabEspecialidades.addColumna('150', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"TIPO_DOCUMENTO")%>");
            tabEspecialidades.addColumna('600', 'left', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"NOMBRE_DOCUMENTO")%>");
            tabEspecialidades.addColumna('80', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"ORGANO")%>");
            tabEspecialidades.addColumna('90', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"FECHA")%>");
            tabEspecialidades.displayCabecera = true;
            tabEspecialidades.height = 100;
            tabEspecialidades.lineas = listDocumentosTabla;
            //tabEspecialidades.dblClkFunction = 'dblClckListasxEspecialidad';
            tabEspecialidades.displayTabla();
            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                try {
                    var div = document.getElementById('listDocumentos');
                    div.children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].style.width = '100%';
                    pulsarListasxEspecialidad(event, fila);
                }
                catch (err) {

                }
            }

            document.getElementById('listDocumentos').children[0].children[1].children[0].children[0].ondblclick = function (event) {

            }
        }

    </script>

    <script>
        function recargarTablaIdentificacionEsp(result) {
            var fila;
            var listDocumentos2 = new Array();
            var listDocumentosTabla2 = new Array();

            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                listDocumentos2[i - 1] = fila;
                listDocumentosTabla2[i - 1] = [fila[1], fila[2], fila[3], fila[4]];
            }
            tabEspecialidades2 = new Tabla(document.getElementById('listDocumentos2'), 2000);
            tabEspecialidades2.addColumna('150', 'center', "TIPO_DOCUMENTO");
            tabEspecialidades2.addColumna('510', 'left', "NOMBRE_DOCUMENTO");
            tabEspecialidades2.addColumna('80', 'center', "ORGANO");
            tabEspecialidades2.addColumna('90', 'center', "FECHA");
            tabEspecialidades2.addColumna('90', 'center', "AAAA");

            tabEspecialidades2.displayCabecera = true;
            tabEspecialidades2.height = 100;
            tabEspecialidades2.lineas = listDocumentos2;
            tabEspecialidades2.displayTabla();

            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                try {
                    var div = document.getElementById('listDocumentos2');
                         div.children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].style.width = '100%';
                }
                catch (err) {
                }
            }
        }

    </script>





</body>
</html>
