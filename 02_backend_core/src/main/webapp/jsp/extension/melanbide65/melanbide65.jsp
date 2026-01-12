<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<html>
    <head>
        <%
            String ejercicio = (String)request.getAttribute("ejercicio"); 
        %>
    </head>
    <body>
        <script type="text/javascript">
            // controlo que sean de 2023 o posteriores
            var eje =<%=ejercicio%>;
            //cuando se pincha en la pestana de datos suplementarios tengo recalcular subvención
            var recalculo = true;
            //ya que la carga de datos suplementarios se hace con una llamada ajax aprovecho esto para detectar cuï¿½ndo hacer el cambio de los literales (he hecho mï¿½ltiples pruebas de otras maneras como capturando el click de la pestaï¿½a pero se ejecuta antes que el cï¿½digo de fichaExpediente.jsp y no he conseguido que haga la espera)
            $(document).ajaxComplete(function () {
                if (estanCargadosCamposSuplementarios() && recalculo && eje >= 2023) {
                    recalculo = false;
                    //si los importes son diferentes pinto de rojo IMPTOTRECAL
                    if (document.getElementById("IMPCONINI").value != document.getElementById("IMPTOTSUBSOL").value) {
                        document.getElementById("IMPCONINI").style.color = 'red';
                    } else {
                        document.getElementById("IMPCONINI").style.color = '#8d9093';
                    }
                }
            });
        </script>
    </body>
</html>

