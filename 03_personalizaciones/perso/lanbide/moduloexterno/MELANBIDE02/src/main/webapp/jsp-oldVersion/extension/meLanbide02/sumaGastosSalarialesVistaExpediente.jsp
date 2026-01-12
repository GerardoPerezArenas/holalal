<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<jsp:useBean id="configuracion" scope="request" class="es.altia.flexia.integracion.moduloexterno.lanbide02.util.ConfigurationParameter"
                                                type="es.altia.flexia.integracion.moduloexterno.lanbide02.util.ConfigurationParameter" />
<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);

    String codProcedimiento = request.getParameter("codProcedimiento");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String nombreModulo     = request.getParameter("nombreModulo");

%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/meLanbide02/lanbide02.css'/>">

<script type="text/javascript">

    function callFromTableTo(rowID,tableName){
    }

</script>

<div class="tab-page" id="tabPage100" style="height:350px; width: 100%;">
      <h2 class="tab" id="pestana100"><%=configuracion.getParameter("TITULO_PESTANHA/" + codProcedimiento + "/ETIQUETA_TITULO_SUMA_GASTOS_" + idiomaUsuario,nombreModulo)%></h2>
      <script type="text/javascript">tp1_p100 = tp1.addTabPage( document.getElementById( "tabPage100" ) );</script>
      <TABLE  width="100%" border="0px">
       <TR>
           <TD ALIGN="CENTER">
               <TABLE BORDER="0">
               <TR>
                    <TD id="tabGastos" name="tabGastos"></TD>
               </TR>              
               <TR>
                    <TD id="tabTotalGastos" id="tabTotalGastos"></TD>
               </TR>
              
               </TABLE>
           </TD>
       </TR>
      </TABLE>
  </div>

 <script type="text/javascript">

        // TABLA CON LOS EXPEDIENTES
        var tabGastos;
        if(document.all) tabGastos = new Tabla(document.all.tabGastos);
        else tabGastos = new Tabla(document.getElementById('tabGastos'));
        tabGastos.addColumna('55','center','<%=configuracion.getParameter("ETIQUETA_COLUMNA_GRUPO_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COLUMNA_NUM_CONTRATOS_SOLICITADOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COLUMNA_SUM_MESES_SOLICITADOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COLUMNA_SUM_COSTES_SOLICITADOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COLUMNA_SUM_SUBVENCION_SOLICITADA_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COLUMNA_NUM_CONTRATOS_CONCEDIDOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COLUMNA_SUM_MESES_CONCEDIDOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COLUMNA_SUM_COSTES_CONCEDIDOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COLUMNA_SUM_SUBVENCION_CONCEDIDA_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.displayCabecera=true;
        tabGastos.height = 200;
        tabGastos.displayTabla();

        var contador = 0;
        var indice = 1;
        var registros = new Array();

        <logic:present name="DatosGruposCotizacion" property="gruposCotizacion" scope="request">
            <logic:iterate name="DatosGruposCotizacion" property="gruposCotizacion" id="expediente">
                registros[contador] =   [indice,
                                        '<bean:write name="expediente" property="sumaContratosSolicitadosConFormato" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaMesesSolicitadosConFormato" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaCostesSolicitadosConFormato" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaSubvencionSolicitadaConFormato" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaContratosConcedidosConFormato" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaMesesConcedidosConFormato" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaCostesConcedidosConFormato" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaSubvencionConcedidaConFormato" ignore="true"/>'];
                contador++;
                indice++;
            </logic:iterate>
        </logic:present>

        tabGastos.lineas = registros;
        tabGastos.displayTabla();

        
        // TABLA CON LOS TOTALES
        var tabTotalGastos;
        if(document.all) tabTotalGastos = new Tabla(document.all.tabTotalGastos);
        else tabTotalGastos = new Tabla(document.getElementById('tabTotalGastos'));

        tabTotalGastos.addColumna('55','center','');
        tabTotalGastos.addColumna('90','right','');
        tabTotalGastos.addColumna('90','right','');
        tabTotalGastos.addColumna('90','right','');
        tabTotalGastos.addColumna('90','right','');
        tabTotalGastos.addColumna('90','right','');
        tabTotalGastos.addColumna('90','right','');
        tabTotalGastos.addColumna('90','right','');
        tabTotalGastos.addColumna('90','right','');
        tabTotalGastos.displayCabecera=false;
        tabTotalGastos.height = 20;

        var totales =  new Array();
        totales[0] = ['<%=configuracion.getParameter("ETIQUETA_FILA_TOTALES_" + idiomaUsuario,nombreModulo)%>','<bean:write name="DatosGruposCotizacion" property="totalContratosSolicitadosConFormato" ignore="true"/>','<bean:write name="DatosGruposCotizacion" property="totalMesesSolicitadosConFormato" ignore="true"/>',
                      '<bean:write name="DatosGruposCotizacion" property="totalCostesSolicitadosConFormato" ignore="true"/>','<bean:write name="DatosGruposCotizacion" property="totalSubvencionSolicitadaConFormato" ignore="true"/>',
                      '<bean:write name="DatosGruposCotizacion" property="totalContratosConcedidosConFormato" ignore="true"/>','<bean:write name="DatosGruposCotizacion" property="totalMesesConcedidosConFormato" ignore="true"/>',
                      '<bean:write name="DatosGruposCotizacion" property="totalCostesConcedidosConFormato" ignore="true"/>','<bean:write name="DatosGruposCotizacion" property="totalSubvencionConcedidaConFormato" ignore="true"/>'];

        tabTotalGastos.lineas = totales;       
        tabTotalGastos.displayTabla();
         
 </script>