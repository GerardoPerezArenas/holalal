<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<script type="text/javascript">
           
    //cuando se pincha en la pestana de datos suplementarios tengo recalcular fecha20relevo
    var recalculo = true;
    //ya que la carga de datos suplementarios se hace con una llamada ajax aprovecho esto para detectar cu�ndo hacer el cambio de los literales (he hecho m�ltiples pruebas de otras maneras como capturando el click de la pesta�a pero se ejecuta antes que el c�digo de fichaExpediente.jsp y no he conseguido que haga la espera)
    $( document ).ajaxComplete(function() {
        if(estanCargadosCamposSuplementarios() && recalculo){
            recalculo = false; 
        }
    });
</script>
