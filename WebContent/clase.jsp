<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.claseAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Talentos - Clases</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<style>
		#icons {
			margin: 0;
			padding: 0;
		}
		#icons li {
			margin: 2px;
			position: relative;
			padding: 4px 0;
			cursor: pointer;
			float: left;
			list-style: none;
		}
		#icons span.ui-icon {
			float: left;
			margin: 0 4px;
		}
		.fakewindowcontain .ui-widget-overlay {
			position: absolute;
		}
		select {
			width: 50px;
		}
	
		.checkbox {
		    position: relative;
		    z-index: 10;
		}
		
		span.checkbox {
		    z-index: 5;
		    display: inline-block;
		    width: 15px;
		    height: 15px;
		    background: #fff;
		    border: 1px solid #000;
		    margin: 1px 0 0 -14px;
		    top: 3px;
		    float: left;
		    &.on {
		        background: $blue;
		    }
		}
	</style>
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Alta Clase" />
</jsp:include>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat(Constantes.fechaFormato);
String fecha2 = sdf2.format(new Date());
%>
<br />
<table style="85%" align="center">
	<tr>
		<td>
			Clase
		</td>
		<td>
			Comentarios
		</td>
	</tr>
	<tr>
		<td>
			<input type="text" id="clase" value="" style="font-size:25px; font-family: Times new Roman" />
			<input type="hidden" id="fecha2" value="<%= fecha2 %>" />
		</td>
		<td>
			<input type="text" id="comentario" value="" style="font-size:25px; font-family: Times new Roman" />
		</td>
	</tr>
	<tr>
		<td colspan="3" align="center">
			<button id="guarda" type="submit">Guardar</button>
		</td>
	</tr>
</table>
<br />
<hr align="center" style="width:90%" />
<center><h1>&Uacute;ltimos registros</h1>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<iframe id="listado" src="./clase_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>
<script>
$('#guarda').button();
$('#guarda-icon').button({
	icon: "ui-icon-gear",
	showLabel: false
});

$(document).ready(function() {
	$('#guarda').click(function(event) {
		var fechaAlta = $('#fecha2').val();
		var nombre = $('#clase').val();
		var comentario = $('#comentario').val();
		$.post('/claseServlet', {
			proceso : "ALT",
			fechaAlta : fechaAlta,
			nombre : nombre,
			comentario : comentario
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				$('#clase').val("");
				$('#comentario').val("");
				var respuesta = response.responseText;
				alert("El registro ha sido agregado. \n Identificador # " + respuesta);
				document.location.reload();
			} else {
				alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
			}
		})
		.fail(function(res) {
               console.log("Server Error: " + res.status + " " + res.statusText);
           });
		event.preventDefault();
	});
});

$( "input:text" ).css({
	fontSize: "22px"
});

setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000)

var claseDisponible = <%= claseAction.obtenerClaseJson() %>;

$( "#clase" ).autocomplete({
	source: claseDisponible,
    select: function (event, ui) {
        $("#clase").val(ui.item.label); // display the selected text
        $("#claseId").val(ui.item.id); // save selected id to hidden input
        console.log(ui.item.id+" - "+ui.item.label+" - "+ui.item.monto);
    }
});

</script>
</body>
</html>