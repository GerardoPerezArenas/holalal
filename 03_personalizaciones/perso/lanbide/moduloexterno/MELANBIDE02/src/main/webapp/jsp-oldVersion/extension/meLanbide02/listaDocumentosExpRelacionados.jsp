<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:useBean id="configuracion" scope="request" class="es.altia.flexia.integracion.moduloexterno.lanbide02.util.ConfigurationParameter"
                                                type="es.altia.flexia.integracion.moduloexterno.lanbide02.util.ConfigurationParameter" />
<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);

    String codProcedimiento = request.getParameter("codProcedimiento");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codTramite       = request.getParameter("codTramite");

%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/meLanbide02/lanbide02.css'/>">
<script type="text/javascript">
    
    function callFromTableTo(rowID,tableName){
    }

    function pulsarVerDocumentoMeLanbide02(){        
        var indice = tabDocumentos.selectedIndex;        
        if(indice!=-1){
            var codMunicipio      = documentosOriginales[indice][0];
            var numExpediente     = documentosOriginales[indice][1];
            var codTramite        = documentosOriginales[indice][2];
            var ocurrenciaTramite = documentosOriginales[indice][3];
            var numeroDocumento   = documentosOriginales[indice][4];
            var nombreDocumento   = documentosOriginales[indice][5];

            if(codMunicipio!='' && numExpediente!='' && codTramite!='' && ocurrenciaTramite!='' && numeroDocumento!='' && nombreDocumento!=''){
                var url = "<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE02&operacion=visualizarDocumentoTramitacion&tipo=1" +
                         "&numeroDocumento=" + numeroDocumento + "&nombreDocumento=" + nombreDocumento + "&numero=" + numExpediente + "&codTramite=" + codTramite + "&ocuTramite=" + ocurrenciaTramite + "&codOrganizacion=" + codMunicipio + "&idioma=" + "<%=idiomaUsuario%>";
                window.open(url,'ventana','left=10, top=10, width=650, height=350, scrollbars=no, menubar=no, location=no, resizable=yes');
            }else
                jsp_alerta("A",'<%=configuracion.getParameter("MSG_ERROR_EXPEDIENTE_SIN_DOCUMENTO_" + idiomaUsuario,nombreModulo)%>');            
        }else
            jsp_alerta("A",'<%=configuracion.getParameter("MSG_ERROR_DOCUMENTO_NO_SELECCIONADO_" + idiomaUsuario,nombreModulo)%>');

    }

</script>

<div class="tab-page" id="tabPage101" style="height:350px; width: 100%;">
      <h2 class="tab" id="pestana101"><%=configuracion.getParameter("TITULO_PESTANHA/" + codProcedimiento + "/" + codTramite + "/ETIQUETA_TITULO_DOCUMENTOS_ACTIVIDAD_" + idiomaUsuario,nombreModulo)%></h2>
      <script type="text/javascript">tp1_p101 = tp1.addTabPage( document.getElementById( "tabPage101" ) );</script>
      <TABLE  width="100%" border="0px">
       <TR>
           <TD ALIGN="CENTER">
               <TABLE BORDER="0">
               <TR>
                    <TD id="tabDocumentos" name="tabDocumentos"></TD>
               </TR>
               <TR>
                   <TD ALIGN="CENTER">
                       <INPUT TYPE="BUTTON" class="botonLargoLanbide" onclick="javascript:pulsarVerDocumentoMeLanbide02();" NAME="btnVerDocumento" value="<%=configuracion.getParameter("ETIQUETA_BOTON_VER_DOCUMENTO_" + idiomaUsuario,nombreModulo)%>"/>

                   </TD>
               </TR>
               </TABLE>
           </TD>
       </TR>
      </TABLE>
  </div>

 <script type="text/javascript">

        // TABLA CON LOS EXPEDIENTES
        var tabDocumentos;
        if(document.all) tabDocumentos = new Tabla(document.all.tabDocumentos);
        else tabDocumentos = new Tabla(document.getElementById('tabDocumentos'));
        tabDocumentos.addColumna('350','center','<%=configuracion.getParameter("ETIQUETA_NUM_EXPEDIENTE_" + idiomaUsuario,nombreModulo)%>');
        tabDocumentos.addColumna('350','center','<%=configuracion.getParameter("ETIQUETA_NOMBRE_DOCUMENTO_" + idiomaUsuario,nombreModulo)%>');
        tabDocumentos.displayCabecera=true;
        tabDocumentos.height = 200;
        
        var contador = 0;
        var registros = new Array();
        var documentosOriginales = new Array();
       
        <c:if test="${requestScope.expedientes_documentos ne null}">
            <c:forEach var="expediente" items="${expedientes_documentos}">
                registros[contador]            = ['<c:out value="${expediente.numExpediente}"/>','<c:out value="${expediente.documentoTramitacion.nombreDocumento}"/>'];
                documentosOriginales[contador] = ['<c:out value="${expediente.documentoTramitacion.codMunicipio}"/>','<c:out value="${expediente.documentoTramitacion.numExpediente}"/>',
                                                  '<c:out value="${expediente.documentoTramitacion.codTramite}"/>','<c:out value="${expediente.documentoTramitacion.ocurrenciaTramite}"/>',
                                                  '<c:out value="${expediente.documentoTramitacion.numeroDocumento}"/>','<c:out value="${expediente.documentoTramitacion.nombreDocumento}"/>'];
                contador++;
            </c:forEach>        
        </c:if>
        
        tabDocumentos.lineas = registros;
        tabDocumentos.displayTabla();

 </script>
