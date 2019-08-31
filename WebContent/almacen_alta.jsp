<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.almacenAction" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Alta de Almacen</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Alta Almacen" />
</jsp:include>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
String fecha2 = sdf2.format(new Date());
%>
<br />&nbsp;
<input type="hidden" id="fechaAlta" value="<%= fecha2 %>" />
<table style="width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<thead>
		<td>
			Clave Almacen
		</td>
		<td>
			Nombre
		</td>
		<td>
			Comentario
		</td>
	</thead>
	<tr>
		<td>
			<input id="clave" style="width:100px" />
		</td>
		<td>
			<input id="nombre" style="width:200px" />
		</td>
		<td>
			<input id="comentario" style="width:200px" />
		</td>
		<td>
			<button id="guardar" type="submit">Guardar</button>
		</td>
	</tr>
</table>
<br />&nbsp;
<hr align="center" style="width:90%" />
<center><h1>&Uacute;ltimos registros</h1>
<iframe id="listado" src="./almacen_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>
setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

$(document).ready(function() {
	$('#guardar').click(function(event) {
		var fechaAlta = $("#fechaAlta").val();
		var clave = $("#clave").val();
		var nombre = $("#nombre").val();
		var comentario = $("#comentario").val();
		console.log("001");
		$.post('/almacenServlet', {
			proceso    : "ALT",
			fechaAlta  : fechaAlta,
			clave      : clave,
			nombre     : nombre,
			comentario : comentario
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var respuesta = response.responseText;
				$("#clave").val("");
				$("#nombre").val("");
				$("#comentario").val("");
				alert("El registro ha sido agregado. \n Almacen # " + respuesta);
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

$( "#guardar" ).button();
$( "#guardar-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

</script>
</body>
</html>