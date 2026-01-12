<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide13.i18n.MeLanbide13I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide13.vo.ListaExpedientesVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide13.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide13.util.ConstantesMeLanbide13"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuarioVO.getAppCod();
                    idiomaUsuario = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
            }

            MeLanbide13I18n meLanbide13I18n = MeLanbide13I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo"); 
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");

            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
            
            String costeTotal = (String)request.getAttribute("costeTotal");
//            costeTotal= (costeTotal!=null && costeTotal !="" ? (costeTotal):"0");
//            costeTotal = formateador.format(costeTotal);
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide13/melanbide13.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var total = <%=costeTotal%>;

            function cargarExpedienteRelacionado() {
                var indice = tabla.selectedIndex;
                if (indice != undefined && indice != null && indice >= 0) {
                    var numExp = listaExpedientesTabla[indice][0];
                    var datos = numExp.split("/");
                    var argumentos = new Array();
                    var source = "<c:url value='/sge/FichaExpediente.do'/>" + "?modoConsulta=si";
                    var nCS = document.forms[0].codMunicipio.value;
                    var codEjercicio = datos[0];//document.forms[0].ejercicio.value;
                    var codProc = datos[1];//document.forms[0].numExpediente.value;
                    var desdeConsulta = "no"; //document.forms[0].modoConsulta.value;
                    // desdeAltaRE la ponermos como si, para que al pulsar volver, solo cierre la ventana, no haga mas operaciones-
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source=' + source + "&codMunicipio=" + nCS
                            + "&ejercicio=" + codEjercicio + "&numero=" + numExp + "&codProcedimiento=" + codProc + "&desdeConsulta=" + desdeConsulta + "&desdeAltaRE=si"
                            + "&opcion=cargar&desdeListarExpM13=1",
                            argumentos, 'width=1400,height=650,status=' + '<%=statusBar%>' + '',
                            function (datos) {});

                } else {
                    jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                }
            }

            function crearTabla() {
                tabla = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaExpedientes'));

                tabla.addColumna('10', 'center', "<%=meLanbide13I18n.getMensaje(idiomaUsuario,"tabla.numeroExpediente")%>");
                tabla.addColumna('10', 'center', "<%=meLanbide13I18n.getMensaje(idiomaUsuario,"tabla.mes")%>");
                tabla.addColumna('20', 'center', "<%=meLanbide13I18n.getMensaje(idiomaUsuario,"tabla.territorio")%>");
                tabla.addColumna('10', 'right', "<%=meLanbide13I18n.getMensaje(idiomaUsuario,"tabla.importe")%>");

                tabla.displayCabecera = true;
                tabla.height = 400;
            }

            function pulsarExportarExcelListaExpedientes() {
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = '';
                parametros = '?tarea=preparar&modulo=MELANBIDE13&operacion=generarExcelListaExpedientes' + '&tipo=0&numExp=<%=numExpediente%>';
                window.open(url + parametros, "_blank");
            }

            function formatNumero(numero) {
                if (numero != null && numero != undefined && numero != "") {
                    if (isNaN(numero)) {
                        numero = numero.replace(/[^\d\.,]*/g, '');
                        numero = numero.replace(/\./g, '').replace(/\,/g, '.');
                    }
                    numero = parseFloat(numero).toFixed(2);

                    var parteEnteraFormat = (numero.toString().split('.')[0] != null && numero.toString().split('.')[0] != undefined ? numero.toString().split('.')[0].replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.') : "");
                    var parteDecimal = (numero.toString().split('.')[1] != null && numero.toString().split('.')[1] != undefined ? numero.toString().split('.')[1] : (parteEnteraFormat != null && parteEnteraFormat != undefined && parteEnteraFormat != "" ? "00" : ""));
                    numero = parteEnteraFormat.concat(",", parteDecimal);
                    return numero;
                }
                return "";
            }

        </script>        
    </head>    
    <body class="bandaBody">
        <div class="tab-page" id="tabPage131" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana131"><%=meLanbide13I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage131"));</script>
            <h2 class="legendTema" id="pestanaPrinc"><%=meLanbide13I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div style="text-align: center;">
                <div id="divGeneral"style="width: 80%; margin: 0 auto; display: flex; flex-direction: row; align-items: center; justify-content: center;">   
                    <div id="listaExpedientes" name="listaExpedientes" style="text-align: center; margin:0px;margin-top:0px; float: left;" align="center"></div>
                </div>              
                <div style="text-align: right; width: 90%; margin-top: 20px;">                      
                    <label for="IMPORTE_TOTAL"><%=meLanbide13I18n.getMensaje(idiomaUsuario,"label.importeTotal")%></label>
                    <input type="text" id="IMPORTE_TOTAL" name="IMPORTE_TOTAL" class="inputTexto" size="9" style="text-align:right" value="" readonly />
                </div>  
                <div class="botonera"  style="margin-top: 20px;">
                    <input type="button" id="btnCargarExpediente" name="btnCargarExpediente" class="botonMasLargo" value="<%=meLanbide13I18n.getMensaje(idiomaUsuario, "btn.cargar.ExpteRelacionado")%>" onclick="cargarExpedienteRelacionado();"/>
                    <input type="button" id="btnExportar" name="btnExportar" class="botonMasLargo" value="<%=meLanbide13I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarExcelListaExpedientes();">
                </div>
            </div>
        </div>
        <script  type="text/javascript">
            //Tabla
            var tabla;
            var listaExpedientes = new Array();
            var listaExpedientesTabla = new Array();
            var listaExpedientesTabla_titulos = new Array();

            <%                  
               ListaExpedientesVO objectVO = null;
               List<ListaExpedientesVO> List = null;
               if(request.getAttribute("listaExpedientes")!=null){
                   List = (List<ListaExpedientesVO>)request.getAttribute("listaExpedientes");
               }													
               if (List!= null && List.size() >0){
                   for (int indice=0;indice<List.size();indice++)
                   {
                        objectVO = List.get(indice);
                       
                        String numExp="-";
                        if(objectVO.getNumExp()!=null){
                           numExp=String.valueOf(objectVO.getNumExp());                
                        }
                        
                        String mes = "-";
                        if(objectVO.getMes()!=null){
                            mes=objectVO.getMes();                           
                            String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide13.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide13.FICHERO_PROPIEDADES);
                            String[] descripcionDobleIdioma = (mes != null ? mes.split(barraSeparadoraDobleIdiomaDesple) : null);
                            if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                if(idiomaUsuario==ConstantesMeLanbide13.CODIGO_IDIOMA_EUSKERA){
                                    mes=descripcionDobleIdioma[1];
                                }else{
                                    mes=descripcionDobleIdioma[0];
                                }
                            }
                        }
                          
                        String territorio="-";
                        if(objectVO.getTerritorio()!=null){
                           territorio=String.valueOf(objectVO.getTerritorio());                
                        }
                        
                        String importe="-";
                        if(objectVO.getImporte()!=null){
                           importe= formateador.format(objectVO.getImporte());
                        }
            %>
            listaExpedientes[<%=indice%>] = ['<%=numExp%>', '<%=mes%>', '<%=territorio%>', '<%=importe%>'];
            listaExpedientesTabla[<%=indice%>] = ['<%=numExp%>', '<%=mes%>', '<%=territorio%>', '<%=importe%>' + ' \u20ac'];
            listaExpedientesTabla_titulos[<%=indice%>] = ['<%=numExp%>', '<%=mes%>', '<%=territorio%>', '<%=importe%>' + ' \u20ac'];
            <%
                   }// for
               }// if
            %>
            crearTabla();
            tabla.lineas = listaExpedientesTabla;
            tabla.displayTabla();
            
            if (total != null) {
                document.getElementById('IMPORTE_TOTAL').value = formatNumero(total) + ' \u20ac';
            }

        </script>
    </body>
</html>