<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.i18n.MeLanbide91I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.vo.ContrGenVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConstantesMeLanbide91"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Date;" %>

<html>
    <head>
        <%
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch(Exception ex) {
                }
            }

            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuarioVO.getAppCod();
                    idioma = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
            }

            MeLanbide91I18n meLanbide91I18n = MeLanbide91I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide91/melanbide91.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide91/GoUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var pestaña;
            
            function pulsarNuevoContratacion() {               
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE91&operacion=cargarNuevoAcceso&tipo=0&nuevo=1&numExp=<%=numExpediente%>&numero=<%=numExpediente%>', 700, 800, 'no', 'no', function(result){
                    if (result != undefined){								
                        if (result[0] == '0') {
                            recargarTablaContrataciones(result[1]);
                        }
                    }
                });
            }
            
            function pulsarModificarContratacion() {
                var indice = tablaContGen.selectedIndex;
                if (indice != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE91&operacion=cargarModificarAcceso&tipo=0&nuevo=0&numExp=<%=numExpediente%>&numero=<%=numExpediente%>&id=' + listaContrataciones[indice][0], 700, 800, 'no', 'no', function(result){
                            if (result != undefined){								
                                if (result[0] == '0') {
                                    recargarTablaContrataciones(result[1]);
                                }
                            }
                    });
                }
                else {
                    jsp_alerta('A', '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }          

            function pulsarEliminarContratacion() {
                var indice = tablaContGen.selectedIndex;
                if (indice != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoGO');
                        console.log('<%=numExpediente%>');                        
                        console.log(listaContrataciones[indice][0]);
                        var parametros = 'tarea=preparar&modulo=MELANBIDE91&operacion=eliminarAcceso&tipo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[indice][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaActualizarPestanaContratacion,
                                error: mostrarErrorEliminar
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoGO');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function iniciarTablaContrataciones(){
                tablaContGen = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaContrataciones'));

                tablaContGen.addColumna('0', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.id")%>");
              
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.nombre")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.apellido1")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.apellido2")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.sexo")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.dni")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.psiquica")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.fisica")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.sensorial")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.fecIni")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.jornada")%>");
                tablaContGen.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.porcParcial")%>");
                 
                tablaContGen.displayCabecera = true;
                tablaContGen.height = 360;
                              
                tablaContGen.lineas = listaContrataciones;
                tablaContGen.displayTabla();
           
            }
   
            function recargarTablaContrataciones(accesos) {
                var fila;
                listaContrataciones = new Array();
                listaContratacionesTabla = new Array();
                
                for (var i = 0; i < accesos.length; i++) {
                    fila = accesos[i];
                    listaContrataciones[i] =              [fila.id,fila.nombre,fila.apellido1,fila.apellido2,fila.dessexo,fila.dni,fila.psiquica, fila.fisica, fila.sensorial,fila.fecIniStr,fila.desjorn,fila.porcParcial];                    
                    listaContratacionesTabla[i] =         [fila.id,fila.nombre,fila.apellido1,fila.apellido2,fila.dessexo,fila.dni,fila.psiquica, fila.fisica, fila.sensorial,fila.fecIni,fila.fecIniStr,fila.desjorn,fila.porcParcial];  
                }
                iniciarTablaContrataciones();
            }
            
            function actualizarPestana() {
                var parametros = 'tarea=preparar&modulo=MELANBIDE91&operacion=actualizarPestana&tipo=0&numExp=<%=numExpediente%>';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        success: procesarRespuestaActualizarPestanaContratacion,
                        error: mostrarErrorPeticion
                    });
                } catch (Err) {
                    mostrarErrorPeticion();
                }
            }

            function mostrarCalfecIniContratacion(event) { 
            if(window.event) event = window.event;
            if (document.getElementById("calfecIni").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fecIni',null,null,null,'','calfecIni','',null,null,null,null,null,null,null, '',event);   
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestanaContratacion(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var accesos = datos.tabla.lista;
                        elementoVisible('off', 'barraProgresoGO');
                        recargarTablaContrataciones(accesos);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminar() {
                mostrarErrorPeticion(6);
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                elementoVisible('off', 'barraProgresoGO');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }            

        </script>
        
    </head>
    
    <body class="bandaBody ">
        <div class="tab-page quitarMayusculas" id="tabPage911" style="height:520px; width: 100%;;" >
            <h2 class="tab" id="pestana911"><%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage911"));</script>
            <div id="barraProgresoGO" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <br/>
            <h2 class="legendAzul" id="pestanaPrincContrataciones"><%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div>    
                <br>
                <div id="divGeneralContrataciones">     
                    <div id="listaContrataciones"  align="center"></div>
                </div>
                <br><br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoContrataciones" name="btnNuevoContrataciones" class="botonGeneral"  value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoContratacion();">
                    <input type="button" id="btnModificarContrataciones" name="btnModificarContrataciones" class="botonGeneral" value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarContratacion();">
                    <input type="button" id="btnEliminarContrataciones" name="btnEliminarContrataciones"   class="botonGeneral" value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarContratacion();">
                </div>
            </div>  
        </div>
               
        <script  type="text/javascript">
            //Tabla
            var tablaContrataciones;
            var listaContrataciones = new Array();
            var listaContratacionesTabla = new Array();
           

            <%  		
               ContrGenVO objectVO = null;
               List<ContrGenVO> List = null;
               if(request.getAttribute("listaAccesos")!=null){
                   List = (List<ContrGenVO>)request.getAttribute("listaAccesos");
               }													
               if (List!= null && List.size() >0){
                   for (int indice=0;indice<List.size();indice++)
                   {
                        objectVO = List.get(indice);
               
                        String nombre = "";
                        if(objectVO.getNombre()!=null){
                           nombre=objectVO.getNombre();
                        }
                        
                        String apellido1 = "";
                        if(objectVO.getApellido1()!=null){
                           apellido1=objectVO.getApellido1();
                        }
                        
                        String apellido2 = "";
                        if(objectVO.getApellido2()!=null){
                           apellido2=objectVO.getApellido2();
                        }
                        
                        String sexo = "";
                        if(objectVO.getDesSexo()!=null){
                            String descripcion=objectVO.getDesSexo();
                           
                            String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide91.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide91.FICHERO_PROPIEDADES);
                            String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                            if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                if(idiomaUsuario==ConstantesMeLanbide91.CODIGO_IDIOMA_EUSKERA){
                                    descripcion=descripcionDobleIdioma[1];
                                }else{
                                    // Cogemos la primera posición que debería ser castellano
                                    descripcion=descripcionDobleIdioma[0];
                                }
                            }
                            sexo = descripcion;
                        }else{
                            sexo="-";
                        }
                        
                                             
                        String dni = "";
                        if(objectVO.getDni()!=null){
                           dni=objectVO.getDni();
                        }
                        
                        String psiquica = "";
                        if(objectVO.getPsiquica()!=null){
                           psiquica=String.valueOf((objectVO.getPsiquica().toString()).replace(".",","));
                        }
                        
                        String fisica = "";
                        if(objectVO.getFisica()!=null){
                           fisica=String.valueOf((objectVO.getFisica().toString()).replace(".",","));
                        }
                        
                        String sensorial = "";
                        if(objectVO.getSensorial()!=null){
                           sensorial=String.valueOf((objectVO.getSensorial().toString()).replace(".",","));
                        }
                        
                        String jornada = "jornada";
                        if(objectVO.getJornada()!=null){
                           jornada=objectVO.getJornada();
                        }
                        
                        String jornDes = "";
                        if(objectVO.getDesJorn()!=null){
                           jornDes=objectVO.getDesJorn();
                        }
                        
                        Date fecIni = null;
                        if(objectVO.getFecIni()!=null){
                            fecIni=objectVO.getFecIni();
                        }
                        
                        String fecIniStr = null;
                        if(objectVO.getFecIniStr()!=null){
                           fecIniStr=objectVO.getFecIniStr();
                        }
                                                
                        String porcParcial="";
                        if(objectVO.getPorcParcial()!=null){
                            porcParcial=String.valueOf((objectVO.getPorcParcial().toString()).replace(".",","));
                        }
                        
                        
            %>
                
            listaContrataciones[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=sexo%>', '<%=dni%>', '<%=psiquica%>', '<%=fisica%>','<%=sensorial%>','<%=fecIniStr%>','<%=jornDes%>','<%=porcParcial%>'];
            listaContratacionesTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=sexo%>', '<%=dni%>', '<%=psiquica%>', '<%=fisica%>','<%=sensorial%>','<%=fecIni%>','<%=jornDes%>','<%=porcParcial%>'];
            
            <%
                   }// for
               }// if
            %>
            iniciarTablaContrataciones();
           
        </script>
        <div id="popupcalendar" class="text"></div>                
    </body>
</html>