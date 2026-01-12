<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.CerCertificadoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConstantesMeLanbide50" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<div>
        <%
            EspecialidadesVO datModif = new EspecialidadesVO();
           
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            
            String codCertificado = "";
            String codMotivo = "";

            try
            {

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                numExpediente    = request.getParameter("numero");
                nuevo = (String)request.getAttribute("nuevo");

                if(request.getAttribute("datModif") != null)
                {
                    datModif = (EspecialidadesVO)request.getAttribute("datModif");
                }
 
            }
            catch(Exception ex)
            {
            }
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>

         <script type="text/javascript">

            var comboListaCertificados;
            var listaCodigosCertificados = new Array();
            var listaDescripcionesCertificados = new Array();
            
            var comboListaMotivos;
            var listaCodigosMotivos = new Array();
            var listaDescripcionesMotivos = new Array();
            
            function rellenardatEspModificar(){
                if('<%=datModif%>' != null){
                    buscaCodigoCertificado('<%=datModif.getCodCP() != null ? datModif.getCodCP() : ""%>');
                }
                else
                    alert('No hemos podido cargar los datos para modificar');
            }
            
            function rellenardatEspModificar2(){
                if('<%=datModif%>' != null){
                    buscaCodigoMotivo('<%=datModif.getCodCP() != null ? datModif.getCodCP() : ""%>');
                    
                   
                }
                else
                    alert('No hemos podido cargar los datos para modificar');
            }
            
            function getXMLHttpRequest(){
                var aVersions = [ "MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp","Microsoft.XMLHttp"
                    ];

                if (window.XMLHttpRequest){
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                }else if (window.ActiveXObject){
                    // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                    for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        }catch (error) {
                        //no necesitamos hacer nada especial
                        }
                    }
                }else{
                    return null;
                }
            }
            

            
            function cerrarVentana(){
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
        
            
            function buscaCodigoCertificado (codCertificado){
            comboListaCertificados.buscaCodigo(codCertificado);
            }
            
            function buscaCodigoMotivo (codMotivo){
            comboListaMotivos.buscaCodigo(codMotivo);
            }
            
 
            function cargarDatosCertificado(){ 
                var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;
                buscaCodigoCertificado(codCertificadoSeleccionado);
            }
            
            function cargarDatosMotivo(){ 
                var codMotivoSeleccionado = document.getElementById("codListaMotivos").value;
                buscaCodigoMotivo(codMotivoSeleccionado);
            }
            
    </script>

<div class="contenidoPantalla">
        <form>  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->
          
                <div class="lineaFormulario">
                    <div>
                       <div>
                            <input type="text" name="codListaMotivos" id="codListaMotivos" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaMotivos"  id="descListaMotivos" size="90" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaMotivos" name="anchorListaMotivos">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion1"
                                     name="botonAplicacion1" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                    </div>
                    </div>
                        
                                     
                                     
                                     
                    <br><br>                 
                         
                    
                    
                    
                    <div>
                       <div>
                            <input type="text" name="codListaCertificados" id="codListaCertificados" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaCertificados"  id="descListaCertificados" size="90" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaCertificados" name="anchorListaCertificados">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion2"
                                     name="botonAplicacion2" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                    </div>
                    </div>
                </div>
                 <br><br>
                 
                 
                 

        </form>
    </div>
    
    <script type="text/javascript">

        listaCodigosCertificados[0] = "";
        listaDescripcionesCertificados[0] = "";

        /* Lista con los certificados recuperados de la BBDD */
        var contador = 0;
        <logic:iterate id="certificados" name="listaCertificados" scope="request">
        listaCodigosCertificados[contador] = ['<bean:write name="certificados" property="codCertificado" />'];
        listaDescripcionesCertificados[contador] = ['<bean:write name="certificados" property="descCertificadoC" />'];
        contador++;
        </logic:iterate>
        
        var comboListaCertificados = new Combo("ListaCertificados");
        comboListaCertificados.addItems(listaCodigosCertificados, listaDescripcionesCertificados);
        comboListaCertificados.change = cargarDatosCertificado;

        listaCodigosMotivos[0] = "";
        listaDescripcionesMotivos[0] = "";

        /* Lista con los certificados recuperados de la BBDD */
        var contadorM = 0;
        <logic:iterate id="motivosDeneg" name="listaMotivos" scope="request">
        listaCodigosMotivos[contadorM] = ['<bean:write name="motivosDeneg" property="codigo" />'];
        listaDescripcionesMotivos[contadorM] = ['<bean:write name="motivosDeneg" property="descripcion" />'];
        contadorM++;
        </logic:iterate>
        
        var comboListaMotivos = new Combo("ListaMotivos");
        comboListaMotivos.addItems(listaCodigosMotivos, listaDescripcionesMotivos);
        comboListaMotivos.change = cargarDatosMotivo;
        
        var nuevo = "<%=nuevo%>";
            if(nuevo==0){
                rellenardatEspModificar();
            }

    </script>
    
</div>
   
    