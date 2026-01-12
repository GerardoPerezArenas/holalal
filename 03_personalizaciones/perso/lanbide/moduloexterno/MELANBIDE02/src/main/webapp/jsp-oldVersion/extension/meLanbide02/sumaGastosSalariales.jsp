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
    String codTramite       = request.getParameter("codTramite");

%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/meLanbide02/lanbide02.css'/>">

<script type="text/javascript">
    
    function callFromTableTo(rowID,tableName){
    }

</script>
                    
<div class="tab-page" id="tabPage100" style="height:350px; width: 100%;">                                      
      <h2 class="tab" id="pestana100">      
            <%=configuracion.getParameter("TITULO_PESTANHA/" + codProcedimiento + "/" + codTramite + "/ETIQUETA_TITULO_SUMA_GASTOS_" +  idiomaUsuario,nombreModulo)%>
      </h2>
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
        tabGastos.addColumna('120','center','<%=configuracion.getParameter("ETIQUETA_NUM_EXPEDIENTE_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_CONTRATOS_SOLICITADOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_CONTRATOS_CONCEDIDOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_MESES_SOLICITADOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_MESES_CONCEDIDOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COSTES_SOLICITADOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_COSTES_CONCEDIDOS_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_SUBVENCION_SOLICITADA_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.addColumna('90','right','<%=configuracion.getParameter("ETIQUETA_SUBVENCION_CONCEDIDA_" + idiomaUsuario,nombreModulo)%>');
        tabGastos.displayCabecera=true;
        tabGastos.height = 200;
        tabGastos.displayTabla();
        
        
        var contador = 0;
        var registros = new Array();

        <logic:present name="datos_expedientes" property="expedientes" scope="request">
            <logic:iterate name="datos_expedientes" property="expedientes" id="expediente">
                registros[contador] = ['<bean:write name="expediente" property="numExpediente" ignore="true"/>','<bean:write name="expediente" property="sumaContratosSolicitados" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaContratosConcedidos" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaMesesSolicitados" ignore="true"/>','<bean:write name="expediente" property="sumaMesesConcedidos" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaCostesSolicitados" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaCostesConcedidos" ignore="true"/>',
                                        '<bean:write name="expediente" property="sumaSubvencionSolicitada" ignore="true"/>','<bean:write name="expediente" property="sumaSubvencionConcedida" ignore="true"/>'];
                contador++;
            </logic:iterate>
          
        </logic:present>

        tabGastos.lineas = registros;
        tabGastos.displayTabla();


        // TABLA CON LOS TOTALES
        var tabTotalGastos;
        if(document.all) tabTotalGastos = new Tabla(document.all.tabTotalGastos);
        else tabTotalGastos = new Tabla(document.getElementById('tabTotalGastos'));
        
        tabTotalGastos.addColumna('120','right','');
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
        totales[0] = ['<%=configuracion.getParameter("ETIQUETA_TOTALES_" + idiomaUsuario,nombreModulo)%>','<bean:write name="datos_expedientes" property="totalContratosSolicitados" ignore="true"/>','<bean:write name="datos_expedientes" property="totalContratosConcedidos" ignore="true"/>',
                      '<bean:write name="datos_expedientes" property="totalMesesSolicitados" ignore="true"/>','<bean:write name="datos_expedientes" property="totalMesesConcedidos" ignore="true"/>',
                      '<bean:write name="datos_expedientes" property="totalCostesSolicitados" ignore="true"/>','<bean:write name="datos_expedientes" property="totalCostesConcedidos" ignore="true"/>',
                      '<bean:write name="datos_expedientes" property="totalSubvencionSolicitada" ignore="true"/>','<bean:write name="datos_expedientes" property="totalSubvencionConcedida" ignore="true"/>'];

        tabTotalGastos.lineas = totales;
        tabTotalGastos.displayTabla();

        
 </script>