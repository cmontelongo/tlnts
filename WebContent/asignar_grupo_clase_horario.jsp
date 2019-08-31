<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.claseAction" %>
	<%@ page import="com.talentos.action.grupoAction" %>
	<%@ page import="com.talentos.action.salonAction" %>
	<%@ page import="com.talentos.action.maestroAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Talentos - Asignar Grupos</title>
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
	<jsp:param name="titulo" value="Asignar Grupo - Horario" />
</jsp:include>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat(Constantes.fechaFormato);
String fecha2 = sdf2.format(new Date());
%>
<br />&nbsp;
<table style="85%" align="center">
	<tr>
		<td>
			Grupo
		</td>
		<td>
			Salón
		</td>
		<td>
			Clase
		</td>
		<td>
			Maestro
		</td>
	</tr>
	<tr>
		<td>
			<input type="text" id="grupo" value="" style="font-size:25px; font-family: Times new Roman" />
			<input type="hidden" id="grupoId" value="" />
		</td>
		<td>
			<input type="text" id="salon" value="" style="font-size:25px; font-family: Times new Roman" />
			<input type="hidden" id="salonId" value="" />
		</td>
		<td>
			<input type="text" id="clase" value="" style="font-size:25px; font-family: Times new Roman" />
			<input type="hidden" id="claseId" value="" />
			<input type="hidden" id="fecha2" value="<%= fecha2 %>" />
		</td>
		<td>
			<input type="text" id="maestro" value="" style="font-size:25px; font-family: Times new Roman" />
			<input type="hidden" id="maestroId" value="" />
		</td>
	</tr>
	<tr>
		<td>
			Horario
		</td>
		<td>
			Dia semana
		</td>
		<td>
			Monto
		</td>
		<td>
			Comentarios
		</td>
	</tr>
	<tr>
		<td>
			<input type="text" id="hInicio" value="" style="font-size:25px; font-family: Times new Roman" size="6" />
			<input type="hidden" id="hInicioId" value="" />&nbsp;a&nbsp;
			<input type="text" id="hFin" value="" style="font-size:25px; font-family: Times new Roman" size="6" />
			<input type="hidden" id="hFinId" value="" />
		</td>
		<td>
			<input type="text" id="diaSemana" value="" style="font-size:25px; font-family: Times new Roman" />
			<input type="hidden" id="diaSId" value="" />
		</td>
		<td>
			<input type="text" id="monto" value="" disabled="disabled" style="font-size:25px; font-family: Times new Roman" />
		</td>
		<td>
			<input type="text" id="comentario" value="" style="font-size:25px; font-family: Times new Roman" />
		</td>
		<td align="center">
			<button id="guarda" type="submit">Guardar</button>
		</td>
	</tr>
</table>
<br />&nbsp;
<hr align="center" style="width:90%" />
<center><h1>&Uacute;ltimos registros</h1>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<iframe id="listado" src="./asignarClaseGrupo_listadoUltimos.jsp" style="min-width:800px; width:100%;" frameborder="0"></iframe>
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
		var clase = $('#claseId').val();
		var grupo = $('#grupoId').val();
		var salon = $('#salonId').val();
		var maestro = $('#maestroId').val();
		var diaS = $('#diaSId').val();
		var horaInicio = $('#hInicio').val();
		var horaFin = $('#hFin').val();
		var comentario = $('#comentario').val();
		$.post('/grupoServlet', {
			proceso : "AL1",
			fechaAlta : fechaAlta,
			clase : clase,
			grupo : grupo,
			salon : salon,
			maestro : maestro,
			diaS : diaS,
			hInicio : horaInicio,
			hFin : horaFin,
			comentario : comentario
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				$('#clase').val("");
				$('#claseId').val("");
				$('#grupo').val("");
				$('#grupoId').val("");
				$('#salon').val("");
				$('#salonId').val("");
				$('#maestro').val("");
				$('#maestroId').val("");
				$('#hInicio').val("");
				$('#hInicioId').val("");
				$('#hFin').val("");
				$('#hFinId').val("");
				$('#diaSemana').val("");
				$('#diaSId').val("");
				$('#monto').val("");
				$('#comentario').val("");
				var respuesta = response.responseText;
				//alert("El registro ha sido agregado. \n Identificador # " + respuesta);
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
    }
});

var grupoDisponible = <%= grupoAction.obtenerGrupoJson() %>;
$( "#grupo" ).autocomplete({
	source: grupoDisponible,
    select: function (event, ui) {
        $("#grupo").val(ui.item.label);
        $("#grupoId").val(ui.item.id);
        $("#monto").val(ui.item.monto);
    }
});

var salonDisponible = <%= salonAction.obtenerSalonJson() %>;
$( "#salon" ).autocomplete({
	source: salonDisponible,
    select: function (event, ui) {
        $("#salon").val(ui.item.label);
        $("#salonId").val(ui.item.id);
    }
});

var maestroDisponible = <%= maestroAction.obtenerMaestroJson() %>;
$( "#maestro" ).autocomplete({
	source: maestroDisponible,
    select: function (event, ui) {
        $("#maestro").val(ui.item.label); // display the selected text
        $("#maestroId").val(ui.item.id); // save selected id to hidden input
        console.log(ui.item.id+" - "+ui.item.label);
    }
});

var horaDisponible = [
	{id:1, value:"08:00"},
	{id:2, value:"08:30"},
	{id:3, value:"09:00"},
	{id:4, value:"09:30"},
	{id:5, value:"10:00"},
	{id:6, value:"10:30"},
	{id:7, value:"11:00"},
	{id:8, value:"11:30"},
	{id:9, value:"12:00"},
	{id:10, value:"12:30"},
	{id:11, value:"13:00"},
	{id:12, value:"13:30"},
	{id:13, value:"14:00"},
	{id:14, value:"14:30"},
	{id:15, value:"15:00"},
	{id:16, value:"15:30"},
	{id:17, value:"16:00"},
	{id:18, value:"16:30"},
	{id:19, value:"17:00"},
	{id:20, value:"17:30"},
	{id:21, value:"18:00"},
	{id:22, value:"18:30"},
	{id:23, value:"19:00"},
	{id:24, value:"19:30"},
	{id:25, value:"20:00"}
];

$( "#hInicio" ).autocomplete({
	source: horaDisponible,
    select: function (event, ui) {
        $("#hInicio").val(ui.item.value); // display the selected text
        $("#hInicioId").val(ui.item.id); // save selected id to hidden input
    }
});

$( "#hFin" ).autocomplete({
	source: horaDisponible,
    select: function (event, ui) {
        $("#hFin").val(ui.item.value); // display the selected text
        $("#hFinId").val(ui.item.id); // save selected id to hidden input
    }
});

var diaDisponible = [
	{id:1, value:"Domingo"},
	{id:2, value:"Lunes"},
	{id:3, value:"Martes"},
	{id:4, value:"Miércoles"},
	{id:5, value:"Jueves"},
	{id:6, value:"Viernes"},
	{id:7, value:"Sábado"}
];

$( "#diaSemana" ).autocomplete({
	source: diaDisponible,
    select: function (event, ui) {
        $("#diaSemana").val(ui.item.value); // display the selected text
        $("#diaSId").val(ui.item.id); // save selected id to hidden input
    }
});

</script>
</body>
</html>