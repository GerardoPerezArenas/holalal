<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.FilaEcaResProspectoresVO" %>
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
            MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
            String numExpediente    = request.getParameter("numero");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;

            String tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.prospectores.tituloPagina");
            
        %>

        <title><%=tituloPagina%></title>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide44/melanbide44.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/FixedColumnsTable.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

        <script type="text/javascript">
            function dblClckTablaDetallePros(rowID,tableName){
                
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
        </script>
    </head>
    <body class="contenidoPantalla etiqueta">
        <form>
            <div style="width: 95%; padding: 0px; text-align: left;">
                <!--<div class="tituloAzul" style="clear: both; text-align: left;">-->
                <div class="sub3titulo" style="clear: both; text-align: left;height:18px;">
                    <span>
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.prospectores.detalleProspectores")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="clear: both;">
                        <div id="listaDetalle_pros" style="padding: 5px; width:960px; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                    </div>
                </div>
                <div class="lineaFormulario" style="height: 50px;">
                    <div style="width: 150px; display: inline; float: left;padding-left:30px;">
                        <label id="lblNumProspectores"></label>
                    </div>
                    <div style="width: auto; display: inline; float: right;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.prospectores.totalPagar")%>
                        <input id="totalPagar" name="totalPagar" type="text" size="10" class="inputTexto textoNumerico readOnly" disabled="true" maxlength="11">
                    </div>
                </div>
                <div class="botonera">
                    <input type="button" id="btnCerrarDetallePros" name="btnCerrarDetallePros" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();">
                </div>
            </div>
        </form>
    </body>
    
    <script type="text/javascript">
        var tabDetalle_pros;
        var listaDetalle_pros = new Array();
        var listaDetalle_prosTabla = new Array();
        var listaDetalle_prosTabla_titulos = new Array();
        
        var numProspectores = 0;
        var totalPagar = 0.0;
        var pagarError = false;
        
        tabDetalle_pros = new FixedColumnTable(document.getElementById('listaDetalle_pros'), 918, 941, 'listaDEtalle_pros');
        tabDetalle_pros.addColumna('115','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col0")%>','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col0.title")%>');
        var anchoColumnaNombre = '';
        if(navigator.appName=="Microsoft Internet Explorer"){
            anchoColumnaNombre = '215';
        }else{
            anchoColumnaNombre = '192';
        }
        tabDetalle_pros.addColumna(anchoColumnaNombre,'left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col1")%>','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col1.title")%>');
        
        var col2 = 
        '<table style="width: 100%;">'
        +   '<tr>'
        +       '<td colspan="3" style="text-align: center; border-top: none; border-right: none; border-left: none; border-bottom: 1px solid #ffffff;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col2")%>'
        +       '</td>'
        +   '</tr>'
        +   '<tr>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col2.subcol0")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col2.subcol1")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: none; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col2.subcol2")%>'
        +       '</td>'
        +   '</tr>'
        +'</table>';
        
        tabDetalle_pros.addColumna('210','right',col2,'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col2.title")%>');
        
        var col3 = 
        '<table style="width: 100%;">'
        +   '<tr>'
        +       '<td colspan="4" style="text-align: center; border-top: none; border-right: none; border-left: none; border-bottom: 1px solid #ffffff;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col3")%>'
        +       '</td>'
        +   '</tr>'
        +   '<tr>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col3.subcol0")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col3.subcol1")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: 1px solid #ffffff; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col3.subcol2")%>'
        +       '</td>'
        +       '<td style="text-align: center; border-top: none; border-right: none; border-left: none; border-bottom: none;" class="inner scrollCol xCabecera">'
        +           '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col3.subcol3")%>'
        +       '</td>'
        +   '</tr>'
        +'</table>';
        tabDetalle_pros.addColumna('250','right',col3,'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col3.title")%>');
        tabDetalle_pros.addColumna('127','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col4")%>','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.tabla.col4.title")%>');
        
        tabDetalle_pros.numColumnasFijas = 0;
        
        var titulosCol0 = '';
        var titulosCol1 = '';
        var titulosCol2 = '';
        var titulosCol3 = '';
        
        var col0 = '';
        var col1 = '';
    
        <%  		
            FilaEcaResProspectoresVO act = null;

            List<FilaEcaResProspectoresVO> listaProspectoresResumen = (List<FilaEcaResProspectoresVO>)request.getAttribute("listaDetalleProspectoresResumen");
            if (listaProspectoresResumen!= null && listaProspectoresResumen.size() >0)
            {
                for (int i = 0; i <listaProspectoresResumen.size(); i++)
                {
                    act = listaProspectoresResumen.get(i);

        %>
             listaDetalle_pros[<%=i%>] = ['<%=act.getEcaResProspectoresCod()%>', '<%=act.getNumExp()%>', '<%=act.getNif()%>', '<%=act.getNombre()%>',
                                          '<%=act.getGastosSalarialesSolicitados()%>', '<%=act.getGastosSalarialesConcedidos()%>', '<%=act.getGastosSalarialesJustificados()%>',
                                          '<%=act.getVisitasConcedidas()%>', '<%=act.getVisitasJustificadas()%>', '<%=act.getImporteVisitas()%>', '<%=act.getImpPagar()%>'];
            
            <%
                if(act.getTipoSust() != null && !act.getTipoSust().equals(""))
                {
            %>
                    col0 = 
                    '<table style="width: 100%; border-collapse:collapse; table-layout: fixed;" >'
                    +   '<tr>'
                    +       '<td style="text-align: left; border: none; padding-left: 15px;">'
                    +           '<%=act.getNif()%> (<%=act.getTipoSust()%>)'
                    +       '</td>'
                    +   '</tr>'
                    +'</table>';
                
                    col1 = 
                    '<table style="width: 100%; border-collapse:collapse; table-layout: fixed;" >'
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
            '<table style="width: 100%; border-collapse:collapse; table-layout: fixed;" >'
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
            
            col3 = 
            '<table style="width: 100%; border-collapse:collapse; table-layout: fixed;">'
            +   '<tr>'
            +       '<td style="text-align: right; border-top: none; border-right: #CCCCCC 1px solid; border-left: none; border-bottom: none;" width="19%">'
            +           '<%=act.getVisitasConcedidas()%>'
            +       '</td>'
            +       '<td style="text-align: right; border-top: none; border-right: #CCCCCC 1px solid; border-left: none; border-bottom: none;" width="32%">'
            +           '<%=act.getImporteVisitasConc()%>'
            +       '</td>'
            +       '<td style="text-align: right; border-top: none; border-right: #CCCCCC 1px solid; border-left: none; border-bottom: none;" width="19%">'
            +           '<%=act.getVisitasJustificadas()%>'
            +       '</td>'
            +       '<td style="text-align: right; border: none;" width="33%">'
            +           '<%=act.getImporteVisitas()%>'
            +       '</td>'
            +   '</tr>'
            +'</table>';
        
            listaDetalle_prosTabla[<%=i%>] = [col0, col1,
                                              col2,
                                              col3, '<%=act.getImpPagar()%>'];
                                          
            titulosCol2 = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.titulos.cSal")%>:\r\n\r\n'
                         +'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.titulos.cSal.solic")%>: <%=act.getGastosSalarialesSolicitados()%>\r\n'
                         +'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.titulos.cSal.conc")%>: <%=act.getGastosSalarialesConcedidos()%>\r\n'
                         +'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.titulos.cSal.justif")%>: <%=act.getGastosSalarialesJustificados()%>\r\n';
                                          
            titulosCol3 = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.titulos.visitas")%>:\r\n\r\n'
                         +'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.titulos.visitas.conc")%>: <%=act.getVisitasConcedidas()%>\r\n'
                         +'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.titulos.visitas.justif")%>: <%=act.getVisitasJustificadas()%>\r\n'
                         +'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.resumen.detalle.prospectores.titulos.visitas.coste")%>: <%=act.getImporteVisitas()%>\r\n';
        
            listaDetalle_prosTabla_titulos[<%=i%>] = [titulosCol0, titulosCol1,
                                              titulosCol2,
                                              titulosCol3, '<%=act.getImpPagar()%>'];
                                          
            numProspectores++;
            
            try{
                totalPagar += parseFloat(convertirANumero(listaDetalle_pros[<%=i%>][10]));
            }catch(err){
                pagarError = true;
            }
        <%
                    
                }// for
            }// if
        %>
        
        
        for(var cont = 0; cont < listaDetalle_prosTabla.length; cont++){
            tabDetalle_pros.addFila(listaDetalle_prosTabla[cont], listaDetalle_prosTabla_titulos[cont])
        }
        
        tabDetalle_pros.height = '300';

        tabDetalle_pros.altoCabecera = 70;

        tabDetalle_pros.dblClkFunction = 'dblClckTablaDetallePros';

        tabDetalle_pros.displayTabla();

        tabDetalle_pros.pack();
        
        document.getElementById('lblNumProspectores').innerText = numProspectores+' <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.prospectores.numProspectores")%>';
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
