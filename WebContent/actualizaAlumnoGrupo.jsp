<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.grupoAction" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Actualizar Alumnos Grupo</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<link href="./js/jtable.2.4.0/themes/metro/blue/jtable.min.css" rel="stylesheet" type="text/css" />
	<style>
	body{
		font-family: "Trebuchet MS", "Roboto", helvetica, arial, sans-serif;
		font-size: 25px;
		margin: 30px;
	}

	.ui-datepicker-calendar {
    	display: none;
    }
	</style>
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Actualizar Grupos" />
</jsp:include>
<br />
<table style="padding:10px; width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<thead>
	<tr>
		<td style="width:150px">
			Grupo
		</td>
		<td style="width:100px">
			Total alumnos
		</td>
	</tr>
	</thead>
	<tr>
		<td>
			<input type="text" id="grupo" size="40px" />
			<input type="hidden" id="grupoId"  />
		</td>
		<td>
			<input type="text" id="totalA" size="13px" disabled="disabled" />
		</td>
	</tr>
	<tr>
		<td colspan='2' align="right">
			<button id="reporte" type="submit">Buscar</button><br />
		</td>
	</tr>
</table>
<hr align="center" style="width:90%" />
<center>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<iframe id="listado" src="./actualizaAlumnoGrupo_listado.jsp" style="min-width:800px; width:100%;" frameborder="0"></iframe>
</center>
<script>
setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

var grupoDisponible = <%= grupoAction.obtenerGrupoSalonJson() %>;

$( "#grupo" ).autocomplete({
	source: grupoDisponible,
    select: function (event, ui) {
        $("#grupoId").val(ui.item.id);
        $("#totalA").val(ui.item.total);
        console.log(ui.item);
    }
});


$( "input:text" ).css({
	fontSize: "22px"
});

$( "#reporte" ).button();
$( "#reporte-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

$(document).ready(function() {
	$('#reporte').click(function(event) {
		var grupo = $("#grupoId").val();
		$("#listado").attr("src", "./actualizaAlumnoGrupo_listado.jsp?c=3&g="+grupo);
		event.preventDefault();
	});
});

</script>
</body>
</html>