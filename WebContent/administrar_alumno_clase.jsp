<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.clienteAction" %>
	<%@ page import="com.talentos.action.claseAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Asignar Alumnos a Clases</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		font-size=+2;
		background-color:purple;
	}
</style>
</head>
<body>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat(Constantes.fechaFormato);
String fecha2 = sdf2.format(new Date());
%>
<br />&nbsp;
<input type="hidden" id="fechaAlta" value="<%= fecha2%>" />
<table style="width:90%;background-color:white" align="center" cellspacing="15px" >
	<tr>
		<td>
			Clase:
		</td>
		<td>
			<input id="clase" title="type &quot;a&quot;" style="width:250px" />
			<input id="claseId" type="hidden" />
		</td>
	</tr>
</table>
<table style="width:90%;background-color:white" align="center" cellspacing="15px" >
	<thead>
		<td>
			Alumno
		</td>
		<td>
			Monto
		</td>
		<td>
			Beca
		</td>
	</thead>
	<tr>
		<td>
			<input id="alumno" title="type &quot;a&quot;" style="width:250px" />
			<input id="alumnoId" type="hidden" />
		</td>
		<td>
			<input id="claseMonto" title="type &quot;a&quot;" style="width:150px" disabled="disabled" />
			<input id="claseMonto2" type="hidden" />
		</td>
		<td>
			<input id="beca" title="type &quot;a&quot;" style="width:50px" value="0%"/>
			<input id="becaMonto" type="hidden" value="0.0" />
		</td>
		<td>
			<button id="guardar" type="submit">Guardar</button>
		</td>
	</tr>
	<tr>
		<td colspan="4">
			<label for="c2"><span>Comentarios</span></label>
			<input style="width:300px" />
		</td>
	</tr>
</table>

<br />&nbsp;
<hr align="center" style="width:90%" />
<center><h1>&Uacute;ltimos alumnos inscritos</h1>
<iframe id="listado" src="./alumnoClase_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>

<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>

setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

var claseDisponible = <%= claseAction.obtenerClaseJson() %>;

$( "#clase" ).autocomplete({
	source: claseDisponible,
    select: function (event, ui) {
        $("#clase").val(ui.item.label); // display the selected text
        $("#claseId").val(ui.item.id); // save selected id to hidden input
        $("#claseMonto").val(ui.item.monto); // save selected id to hidden input

        $("#claseMonto").val("$"+parseFloat($("#claseMonto").val(), 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString());

        $("#claseMonto2").val(ui.item.monto); // save selected id to hidden input
        console.log(ui.item.id+" - "+ui.item.label+" - "+ui.item.monto);
    }
});

var alumnoDisponible = <%= clienteAction.obtenerClienteJson() %>

$( "#alumno" ).autocomplete({
	source: alumnoDisponible,
    select: function (event, ui) {
        $("#alumno").val(ui.item.label); // display the selected text
        $("#alumnoId").val(ui.item.id); // save selected id to hidden input
        console.log(ui.item.id+" - "+ui.item.label);
    }
});

var descuentoDisponible = [
	{id:1,label:"100%",monto:1.00},
	{id:2,label:"75%",monto:0.75},
	{id:3,label:"50%",monto:0.50},
	{id:4,label:"25%",monto:0.25},
	{id:5,label:"20%",monto:0.20},
	{id:6,label:"0%",monto:0.0},
];

$( "#beca" ).autocomplete({
	source: descuentoDisponible,
    select: function (event, ui) {
        $("#beca").val(ui.item.label); // display the selected text
        $("#becaMonto").val(ui.item.monto); // save selected id to hidden input
        console.log(ui.item.id+" - "+ui.item.label+" - "+ui.item.monto);
    }
});

$(document).ready(function() {
	$('#guardar').click(function(event) {
		var fechaAlta = $('#fechaAlta').val();
		var claseId = $("#claseId").val();
		var alumnoId = $("#alumnoId").val();
		console.log("000");
		var beca = $("#becaMonto").val();
		if (beca == null || beca == ""){
			beca = 0.0
		}
		console.log(beca);
		$.post('/clienteServlet', {
			proceso : "AL1",
			fechaAlta : fechaAlta,
			idClase : claseId,
			idCliente : alumnoId,
			beca : beca
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var matricula = response.responseText;
				alert("El registro ha sido agregado. \Registro # " + matricula);
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

$( "input:text" ).css({
	fontSize: "22px"
});

</script>

</body>
</html>