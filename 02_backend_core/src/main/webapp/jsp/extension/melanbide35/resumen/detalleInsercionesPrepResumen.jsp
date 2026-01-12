<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.EcaResDetalleInsercionesVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusPreparadoresVO" %>
<%@page import="java.text.DecimalFormatSymbols" %>
<%@page import="java.text.DecimalFormat" %>
<html>
    <head>
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

            String tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.tituloPagina");

            EcaJusPreparadoresVO preparador = (EcaJusPreparadoresVO)request.getAttribute("preparador");
            EcaResDetalleInsercionesVO detalleInserciones = (EcaResDetalleInsercionesVO)request.getAttribute("detalleInserciones");
    
            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador4Decimales = new DecimalFormat("###,##0.0000",simbolo);
            DecimalFormat formateador2Decimales = new DecimalFormat("###,##0.00",simbolo);
            
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
            <div style="width: 95%; padding: 10px; text-align: left;">
                <div class="lineaFormulario" style="padding-bottom: 10px;">
                    <span>
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.preparador")%>: 
                        <label id="nif" name="nif"><%=preparador != null && preparador.getNif() != null && !preparador.getNif().equals("") ? preparador.getNif().toUpperCase() + (preparador.getNombre() != null && !preparador.getNombre().equals("") ? " - " : "") : "" %></label><label id="nomApel" name="nomApel"><%=preparador != null && preparador.getNombre() != null ? preparador.getNombre().toUpperCase() : "" %></label>
                    </span>
                </div>
                <div style="text-align: center;">
                    <table style="border-collapse: collapse; width: 80%; margin: 0 auto;" cellpadding="2px">
                        <tr>
                            <td width="28%" ></td>
                            <td width="18%" ></td>
                            <td width="18%" ></td>
                            <td width="18%" ></td>
                            <td width="18%" ></td>
                        </tr>
                        <tr class="negrita">
                            <td rowspan="2" style="text-align: left; border-bottom: 2px solid #666;">&nbsp;</td>
                            <td colspan="2" class="sub3titulo" style="border: 2px solid #ffffff; border-left: 2px solid #666; border-top: 2px solid #666; text-align: center;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.hombres")%></td>
                            <td colspan="2" class="sub3titulo" style="border: 2px solid #ffffff; text-align: center; border-top: 2px solid #666; border-right: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.mujeres")%></td>
                        </tr>
                        <tr>
                            <td class="sub3titulo" style=" text-align: center; border-bottom: 2px solid #666; border-left: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.justificado")%></td>
                            <td class="sub3titulo" style=" text-align: center; border-bottom: 2px solid #666; border-right: 2px solid #ffffff;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.coste")%></td>
                            <td class="sub3titulo" style=" text-align: center; border-bottom: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.justificado")%></td>
                            <td class="sub3titulo" style=" text-align: center; border-bottom: 2px solid #666; border-right: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.coste")%></td>
                        </tr>
                        <tr>
                            <td style="border-left: 2px solid #666; border-bottom: 1px solid #666; border-right: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.c1")%></td>
                            <td id="c1H_jus" style="text-align: center; border-bottom: 1px solid #666;"></td>
                            <td id="c1H_cos" style="text-align: center; border-bottom: 1px solid #666; border-right: 2px solid #666;"></td>
                            <td id="c1M_jus" style="text-align: center; border-bottom: 1px solid #666;"></td>
                            <td id="c1M_cos" style="text-align: center; border-bottom: 1px solid #666; border-right: 3px solid #666;"></td>
                        </tr>
                        <tr>
                            <td style="border-left: 2px solid #666; border-bottom: 1px solid #666; border-right: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.c2")%></td>
                            <td id="c2H_jus" style="text-align: center; border-bottom: 1px solid #666;"></td>
                            <td id="c2H_cos" style="text-align: center; border-bottom: 1px solid #666; border-right: 2px solid #666;"></td>
                            <td id="c2M_jus" style="text-align: center; border-bottom: 1px solid #666;"></td>
                            <td id="c2M_cos" style="text-align: center; border-bottom: 1px solid #666; border-right: 3px solid #666;"><label id=""></label></td>
                        </tr>
                        <tr>
                            <td style="border-left: 2px solid #666; border-bottom: 1px solid #666; border-right: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.c3")%></td>
                            <td id="c3H_jus" style="text-align: center; border-bottom: 1px solid #666;"></td>
                            <td id="c3H_cos" style="text-align: center; border-bottom: 1px solid #666; border-right: 2px solid #666;"></td>
                            <td id="c3M_jus" style="text-align: center; border-bottom: 1px solid #666;"></td>
                            <td id="c3M_cos" style="text-align: center; border-bottom: 1px solid #666; border-right: 3px solid #666;"></td>
                        </tr>
                        <tr>
                            <td style="border-left: 2px solid #666; border-bottom: 1px solid #666; border-right: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.c4")%></td>
                            <td id="c4H_jus" style="text-align: center; border-bottom: 1px solid #666;"></td>
                            <td id="c4H_cos" style="text-align: center; border-bottom: 1px solid #666; border-right: 2px solid #666;"></td>
                            <td id="c4M_jus" style="text-align: center; border-bottom: 1px solid #666;"></td>
                            <td id="c4M_cos" style="text-align: center; border-bottom: 1px solid #666; border-right: 3px solid #666;"></td>
                        </tr>
                        <tr>
                            <td style="border-left: 2px solid #666; border-bottom: 2px solid #666; border-right: 2px solid #666;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.totalIns")%></td>
                            <td style="border-bottom: 2px solid #666;">&nbsp;</td>
                            <td id="totalH" style="text-align: center; border-right: 2px solid #666; border-bottom: 2px solid #666;"></td>
                            <td style="border-bottom: 2px solid #666;">&nbsp;</td>
                            <td id="totalM" style="text-align: center; border-right: 3px solid #666; border-bottom: 2px solid #666;"></td>
                        </tr>
                        <tr style="height: 100px;">
                            <td colspan="7" style="text-align: right;">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.totalFinal")%>:&nbsp;<label id="total"></label>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="botonera" style="margin-top: 10px;">
                    <input type="button" id="btnCerrarDetalleIns" name="btnCerrarDetalleIns" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();">
                </div>
            </div>
        </form>
    </body>

    <script type="text/javascript">
        <%
            if(detalleInserciones != null)
            {
        %>
                document.getElementById('c1H_jus').innerHTML = '<%=detalleInserciones.getC1h_numero() != null ? formateador4Decimales.format(detalleInserciones.getC1h_numero()) : "-"%>';
                document.getElementById('c1H_cos').innerHTML = '<%=detalleInserciones.getC1h_importe() != null ? formateador2Decimales.format(detalleInserciones.getC1h_importe()) : "-"%>';
                document.getElementById('c1M_jus').innerHTML = '<%=detalleInserciones.getC1m_numero() != null ? formateador4Decimales.format(detalleInserciones.getC1m_numero()) : "-"%>';
                document.getElementById('c1M_cos').innerHTML = '<%=detalleInserciones.getC1m_importe() != null ? formateador2Decimales.format(detalleInserciones.getC1m_importe()) : "-"%>';
                document.getElementById('c2H_jus').innerHTML = '<%=detalleInserciones.getC2h_numero() != null ? formateador4Decimales.format(detalleInserciones.getC2h_numero()) : "-"%>';
                document.getElementById('c2H_cos').innerHTML = '<%=detalleInserciones.getC2h_importe() != null ? formateador2Decimales.format(detalleInserciones.getC2h_importe()) : "-"%>';
                document.getElementById('c2M_jus').innerHTML = '<%=detalleInserciones.getC2m_numero() != null ? formateador4Decimales.format(detalleInserciones.getC2m_numero()) : "-"%>';
                document.getElementById('c2M_cos').innerHTML = '<%=detalleInserciones.getC2m_importe() != null ? formateador2Decimales.format(detalleInserciones.getC2m_importe()) : "-"%>';
                document.getElementById('c3H_jus').innerHTML = '<%=detalleInserciones.getC3h_numero() != null ? formateador4Decimales.format(detalleInserciones.getC3h_numero()) : "-"%>';
                document.getElementById('c3H_cos').innerHTML = '<%=detalleInserciones.getC3h_importe() != null ? formateador2Decimales.format(detalleInserciones.getC3h_importe()) : "-"%>';
                document.getElementById('c3M_jus').innerHTML = '<%=detalleInserciones.getC3m_numero() != null ? formateador4Decimales.format(detalleInserciones.getC3m_numero()) : "-"%>';
                document.getElementById('c3M_cos').innerHTML = '<%=detalleInserciones.getC3m_importe() != null ? formateador2Decimales.format(detalleInserciones.getC3m_importe()) : "-"%>';
                document.getElementById('c4H_jus').innerHTML = '<%=detalleInserciones.getC4h_numero() != null ? formateador4Decimales.format(detalleInserciones.getC4h_numero()) : "-"%>';
                document.getElementById('c4H_cos').innerHTML = '<%=detalleInserciones.getC4h_importe() != null ? formateador2Decimales.format(detalleInserciones.getC4h_importe()) : "-"%>';
                document.getElementById('c4M_jus').innerHTML = '<%=detalleInserciones.getC4m_numero() != null ? formateador4Decimales.format(detalleInserciones.getC4m_numero()) : "-"%>';
                document.getElementById('c4M_cos').innerHTML = '<%=detalleInserciones.getC4m_importe() != null ? formateador2Decimales.format(detalleInserciones.getC4m_importe()) : "-"%>';
                
                document.getElementById('totalH').innerHTML = '<%=detalleInserciones.getTotalH() != null ? formateador2Decimales.format(detalleInserciones.getTotalH()) : "-"%>';
                document.getElementById('totalM').innerHTML = '<%=detalleInserciones.getTotalM() != null ? formateador2Decimales.format(detalleInserciones.getTotalM()) : "-"%>';
                
                document.getElementById("total").innerText = '<%=detalleInserciones.getTotal() != null ? formateador2Decimales.format(detalleInserciones.getTotal()) : "-"%>';
        <%
            }
        %>
    </script>
</html>
