<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.grupoAction" %>
	<%@ page import="com.talentos.action.claseAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="com.talentos.util.Utilerias" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Reporte Pagos</title>
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
	<jsp:param name="titulo" value="Asistencia Maestro" />
</jsp:include>
<table style="padding:10px; width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<thead>
	<tr>
		<td>
			Maestro
		</td>
		<td>
			Clase
		</td>
		<td>
			Mes
		</td>
	</tr>
	</thead>
	<tr>
		<td>
			<input type="text" id="maestro" style="width:400px" />
			<input type="hidden" id="maestroId"  />
		</td>
		<td>
			<input type="hidden" id="claseId"  />
			<input type="text" id="clase" style="width:150px" />
		</td>
		<td>
			<input type="hidden" id="mesId"/>
			<input name="startDate" id="startDate" style="width:150px" class="date-picker" />
		</td>
	</tr>
	<tr>
		<td align="right">
			<button id="reporte" type="submit">Reporte</button><br />
		</td>
		<td align="left">
			<button id="imprimir" onclick="javascript:imprimirMarco()">Imprimir</button>
		</td>
	</tr>
</table>
<hr align="center" style="width:90%" />
<center>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<iframe id="listado" src="./consulta_grupoReporte.jsp" style="min-width:800px; width:100%;" frameborder="0"></iframe>
</center>
<script>
setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

var maestroDisponible = <%= grupoAction.obtenerGrupoMaestroJson() %>;

$( "#maestro" ).autocomplete({
	source: maestroDisponible,
    select: function (event, ui) {
        $("#maestroId").val(ui.item.id);
        $("#totalA").val(ui.item.total);
        console.log(ui.item);
        // Obtener Clases del Maestro seleccionado
        $.get("/claseServlet",{p:"c"},function(data) {
            	console.log(data);
            	claseDisponible = data;
            }, "json"
        );
    }
});

var claseDisponible = <%= claseAction.obtenerClaseJson() %>;

$( "#clase" ).autocomplete({
	source: claseDisponible,
    select: function (event, ui) {
        $("#claseId").val(ui.item.id);
        $("#clase").val(ui.item.label);
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

$( "#imprimir" ).button();
$( "#imprimir-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

var imprimirMarco = function(){
	var iframe = $('#listado')[0];
	iframe.contentWindow.focus();
	iframe.contentWindow.print();
}

$(document).ready(function() {
	$('#reporte').click(function(event) {
		var opcion = $("#maestroId").val();
		var mes = $("#mesId").val();
		var clase = $("#claseId").val();
		console.log(opcion);
		console.log(clase);
		console.log(mes);
		$("#listado").attr("src", "./consulta_grupoReporte.jsp?c=2&g="+opcion+"&m="+mes+"&l="+clase);
		event.preventDefault();
	});

	$('.date-picker').datepicker( {
		monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
		monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
		closeText: 'Cerrar',
		currentText: 'Actual',
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'MM yy',
        onClose: function(dateText, inst) {
        	$("#mesId").val(inst.selectedMonth+1);
            console.log(inst.selectedYear);
            console.log(inst.selectedMonth);
            $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
        }
    });
});

</script>
</body>
</html>