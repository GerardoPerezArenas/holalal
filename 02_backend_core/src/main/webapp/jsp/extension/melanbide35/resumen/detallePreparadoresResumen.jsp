<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.FilaEcaResPreparadoresVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.List" %>
<!doctype html>
<html>
    <head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <%
            int idiomaUsuario = 1;
 int apl = 5;
 String css = "";
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
            MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
            String numExpediente    = request.getParameter("numero");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;

            String tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.preparadores.tituloPagina");

        %>

        <title><%=tituloPagina%></title>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/FixedColumnsTable.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

        <script type="text/javascript">
            function dblClckTablaDetallePrep(rowID,tableName){
                
            }
            
            function cerrarVentana(){
                //actualizarDatosProspectores();
                if(navigator.appName=="Microsoft Internet Explorer") { 
                    window.parent.window.opener=null; 
                    window.parent.window.close(); 
                }else if(navigator.appName=="Netscape") { 
                    top.window.opener = top; 
                    top.window.open('','_parent',''); 
                    top.window.close(); 
                }else{
                    window.close(); 
                } 
            }
            
            function verDetalleInserciones(prepId){
                var control = new Date();   
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarDetalleInsercionesPrep&prepId='+prepId+'&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),370,820,'no','no');
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarDetalleInsercionesPrep&prepId='+prepId+'&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),390,850,'no','no');
                }
            }
        </script>
    </head>
    <body class="contenidoPantalla etiqueta">
        <form>
            <div style="width: 95%; padding: 10px; text-align: left;">
                <!--<div class="tituloAzul" style="clear: both; text-align: left;">-->
                <div class="sub3titulo" style="clear: both; text-align: left;height:18px;">
                    <span>
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.preparadores.detallePreparadores")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="clear: both;">
                        <div id="listaDetalle_prep" style="padding: 5px; width:100%; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                    </div>
                </div>
                <div class="lineaFormulario" style="height: 50px;">
                    <div style="width: 150px; display: inline; float:left; padding-left:30px;">
                        <label id="lblNumPreparadores"></label>
                    </div>
                    <div style="width: auto; display: inline; float: right; margin-right: 0px;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.preparadores.totalPagar")%>
                        <input id="totalPagar" name="totalPagar" type="text" size="10" class="inputTexto textoNumerico readOnly" disabled="true" maxlength="11">
                    </div>
                </div>
                <div class="botonera">
                    <input type="button" id="btnCerrarDetallePrep" name="btnCerrarDetallePrep" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();">
                </div>
            </div>
        </form>
    </body>
    
    <script type="text/javascript">
        var tabDetalle_prep;
        var listaDetalle_prep = new Array();
        var listaDetalle_prepTabla = new Array();
        var listaVisitas_prepTabla_titulos = new Array();
        
        var numPreparadores = 0;
        var totalPagar = 0.0;
        var pagarError = false;
        
        tabDetalle_prep = new FixedColumnTable(document.getElementById('listaDetalle_prep'), 1005, 1030, 'listaDetalle_prep');
        tabDetalle_prep.addColumna('115','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col0")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col0.title")%>');
        var anchoColumnaNombre = '';
        if(navigator.appName=="Microsoft Internet Explorer"){
            anchoColumnaNombre = '202';
        }else{
            anchoColumnaNombre = '179';
        }
        tabDetalle_prep.addColumna(anchoColumnaNombre,'left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col1")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col1.title")%>');
        
        var col2 = 
        '<table width="100%">'
        +   '<tr>'
        +       '<td colspan="3" style="text-align: center; border-top: none; border-right: none; border-left: none; border-bottom: 1px solid #ffffff;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col2")%>'
        +       '</td>'
        +   '</tr>'
        +   '<tr>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col2.subcol0")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col2.subcol1")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: none; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col2.subcol2")%>'
        +       '</td>'
        +   '</tr>'
        +'</table>';
        
        tabDetalle_prep.addColumna('210','right',col2,'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col2.title")%>');
        //tabDetalle_prep.addColumna('75','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col3")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col3.title")%>');
        //tabDetalle_prep.addColumna('75','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col4")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col4.title")%>');
        tabDetalle_prep.addColumna('95','right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col5")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col5.title")%>');
        tabDetalle_prep.addColumna('95','right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col9")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col9.title")%>');
        
        var col6 = 
        '<table width="100%">'
        +   '<tr>'
        +       '<td colspan="3" style="text-align: center; border-top: none; border-right: none; border-left: none; border-bottom: 1px solid #ffffff;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col6")%>'
        +       '</td>'
        +   '</tr>'
        +   '<tr>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col6.subcol0")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col6.subcol1")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: none; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col6.subcol2")%>'
        +       '</td>'
        +   '</tr>'
        +'</table>';
        
        tabDetalle_prep.addColumna('210','right',col6,'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col6.title")%>');
        tabDetalle_prep.addColumna('85','right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col7")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col7.title")%>');
        tabDetalle_prep.addColumna('85','right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col8")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.tabla.col8.title")%>');
        
        tabDetalle_prep.numColumnasFijas = 0;
        
        var titulosCol0 = '';
        var titulosCol1 = '';
        var titulosCol6 = '';
        var titulosCol2 = '';
        
        var col0 = '';
        var col1 = '';
    
        <%  		
            FilaEcaResPreparadoresVO act = null;

            List<FilaEcaResPreparadoresVO> listaPreparadoresResumen = (List<FilaEcaResPreparadoresVO>)request.getAttribute("listaDetallePreparadoresResumen");
            if (listaPreparadoresResumen!= null && listaPreparadoresResumen.size() >0)
            {
                for (int i = 0; i <listaPreparadoresResumen.size(); i++)
                {
                    act = listaPreparadoresResumen.get(i);

        %>
             listaDetalle_prep[<%=i%>] = ['<%=act.getEcaResPreparadoresCod()%>', '<%=act.getNumExp()%>', '<%=act.getEcaJusPreparadoresCod()%>', '<%=act.getNif()%>', '<%=act.getNombre()%>',
                                          '<%=act.getGastosSalarialesSolicitados()%>', '<%=act.getGastosSalarialesConcedidos()%>', '<%=act.getGastosSalarialesJustificados()%>',
                                          '<%=act.getImporteSeguimientos()%>', '<%=act.getImporteSeguimientosLim()%>', '<%=act.getImporteInsConcedido()%>', '<%=act.getImporteInsJustificadas()%>', '<%=act.getImporteInserciones()%>', 
                                          '<%=act.getImporteSegInserciones()%>', '<%=act.getImpPagar()%>'];
                                      
                                      <%
            if(act.getTipoSust() != null && !act.getTipoSust().equals(""))
            {
            %>
                    col0 = 
                    '<table width="100%" style="border-collapse:collapse; table-layout: fixed;" >'
                    +   '<tr>'
                    +       '<td style="text-align: left; border: none; padding-left: 15px;">'
                    +           '<%=act.getNif()%> (<%=act.getTipoSust()%>)'
                    +       '</td>'
                    +   '</tr>'
                    +'</table>';
                
                    col1 = 
                    '<table width="100%" style="border-collapse:collapse; table-layout: fixed;" >'
                    +   '<tr>'
                    +       '<td style="text-align: left; border: none; padding-left: 15px;">'
                    +           '<%=act.getNombre()%>'
                    +       '</td>'
                    +   '</tr>'
                    +'</table>';
                
                    titulosCol0 = '<%=act.getNif()%> (<%=act.getTipoSust()%>)';
                    titulosCol1 = '<%=act.getNombre()%>';
            <%
                }
                else
                {
            %>
                    col0 = '<%=act.getNif()%>';
                    col1 = '<%=act.getNombre()%>';
                
                    titulosCol0 = '<%=act.getNif()%>';
                    titulosCol1 = '<%=act.getNombre()%>';
            <%
                }
            %>
                                      
             col2 = 
            '<table width="100%" style="border-collapse:collapse; table-layout: fixed;" >'
            +   '<tr>'
            +       '<td style="text-align: right; border-top: none; border-right: #CCCCCC 1px solid; border-left: none; border-bottom: none;" width="33%">'
            +           '<%=act.getGastosSalarialesSolicitados()%>'
            +       '</td>'
            +       '<td style="text-align: right; border-top: none; border-right: #CCCCCC 1px solid; border-left: none; border-bottom: none;" width="33%">'
            +           '<%=act.getGastosSalarialesConcedidos()%>'
            +       '</td>'
            +       '<td style="text-align: right; border: none;" width="33%">'
            +           '<%=act.getGastosSalarialesJustificados()%>'
            +       '</td>'
            +   '</tr>'
            +'</table>';
                
             col6 = 
            '<table width="100%" style="border-collapse:collapse; table-layout: fixed;" >'
            +   '<tr>'
            +       '<td style="text-align: right; border-top: none; border-right: #CCCCCC 1px solid; border-left: none; border-bottom: none;" width="33%">'
            +           '<%=act.getImporteInsConcedido()%>'
            +       '</td>'
            +       '<td style="text-align: right; border-top: none; border-right: #CCCCCC 1px solid; border-left: none; border-bottom: none;" width="33%">'
            +           '<%=act.getImporteInsJustificadas()%>'
            +       '</td>'
            +       '<td style="text-align: right; border: none;" width="33%">'
            +           '<a href=\'javascript:verDetalleInserciones(<%=act.getEcaJusPreparadoresCod()%>)\'><%=act.getImporteInserciones()%></a>'
            +       '</td>'
            +   '</tr>'
            +'</table>';
                                      
             listaDetalle_prepTabla[<%=i%>] = [col0, col1, col2,
                                          '<%=act.getImporteSeguimientos()%>', '<%=act.getImporteSeguimientosLim()%>', col6, '<%=act.getImporteSegInserciones()%>', '<%=act.getImpPagar()%>'];
                                          
            titulosCol2 = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.titulos.gastos")%>:\r\n\r\n'
                         +'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.titulos.gastos.conc")%>: <%=act.getImporteInsConcedido()%>\r\n'
                         +'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.titulos.gastos.justif")%>: <%=act.getImporteInsJustificadas()%>\r\n'
                         +'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.titulos.gastos.coste")%>: <%=act.getImporteInserciones()%>\r\n';
                     
            titulosCol6 = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.titulos.ins")%>:\r\n\r\n'
                         +'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.titulos.ins.conc")%>: <%=act.getImporteInsConcedido()%>\r\n'
                         +'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.titulos.ins.justif")%>: <%=act.getImporteInsJustificadas()%>\r\n'
                         +'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.preparadores.titulos.ins.coste")%>: <%=act.getImporteInserciones()%>\r\n';
                                      
            listaVisitas_prepTabla_titulos[<%=i%>] = [titulosCol0, titulosCol1, titulosCol2,
                                          '<%=act.getImporteSeguimientos()%>', '<%=act.getImporteSeguimientosLim()%>', titulosCol6, '<%=act.getImporteSegInserciones()%>', '<%=act.getImpPagar()%>'];
             
             numPreparadores++;
             
             try{
                totalPagar += parseFloat(convertirANumero(listaDetalle_prep[<%=i%>][listaDetalle_prep[0].length - 1]));
             }catch(err){
                pagarError = true;
             }
        <%
                    
                }// for
            }// if
        %>
        
        //tabVisitas_pros.lineas=listaVisitas_prosTabla;
        for(var cont = 0; cont < listaDetalle_prepTabla.length; cont++){
            tabDetalle_prep.addFila(listaDetalle_prepTabla[cont], listaVisitas_prepTabla_titulos[cont])
        }
        
        tabDetalle_prep.height = '300';

        tabDetalle_prep.altoCabecera = 70;

        tabDetalle_prep.dblClkFunction = 'dblClckTablaDetallePrep';

        tabDetalle_prep.displayTabla();

        tabDetalle_prep.pack();
        
        document.getElementById('lblNumPreparadores').innerText = numPreparadores+' <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.preparadores.numPreparadores")%>';
        
        if(!pagarError){
            document.getElementById('totalPagar').value = totalPagar;
            reemplazarPuntosEca(document.getElementById('totalPagar'));
            FormatNumber(document.getElementById('totalPagar').value, 8, 2, document.getElementById('totalPagar').id);
            ajustarDecimalesCampo(document.getElementById('totalPagar'), 2);
        }else{
            document.getElementById('totalPagar').value = '';
        }
        
    </script>
</html>
