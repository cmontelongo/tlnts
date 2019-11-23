<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.clienteAction" %>
	<%@ page import="com.talentos.action.grupoAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Asignar Alumnos a Grupo</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<style>
	.demoHeaders {
		margin-top: 2em;
	}
	#dialog-link {
		padding: .4em 1em .4em 20px;
		text-decoration: none;
		position: relative;
	}
	#dialog-link span.ui-icon {
		margin: 0 5px 0 0;
		position: absolute;
		left: .2em;
		top: 50%;
		margin-top: -8px;
	}
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
}	</style>
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Asignar Alumno a Grupo" />
</jsp:include>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat(Constantes.fechaFormato);
String fecha2 = sdf2.format(new Date());
%>
<br />
<input type="hidden" id="fechaAlta" value="<%= fecha2%>" />
<table style="85%" align="center" cellspacing="15px" >
	<tr>
		<td>
			<label for="c2"><span>Grupo</span></label><br />
			<input id="grupo" title="type &quot;a&quot;" style="width:400px" />
			<input id="grupoId" type="hidden" />
		</td>
		<td>
			<label for="c2"><span>Alumno</span></label><br />
			<input id="alumnoNombre" title="type &quot;a&quot;" type="text" style="width:350px" />
			<input id="alumnoId" type="hidden" />
		</td>
	</tr>
	<tr>
		<td>
			<label for="c2"><span>Monto</span></label><br />
			<input id="grupoMonto" title="type &quot;a&quot;" style="width:150px" disabled="disabled" />
			<input id="grupoMonto2" type="hidden" />
		</td>
		<td>
			<label for="c2"><span>Beca</span></label><br />
			<input id="beca" title="type &quot;a&quot;" style="width:50px" value="0%"/>
			<input id="becaMonto" type="hidden" value="0.0" />
		</td>
	</tr>
	<tr>
		<td>
			<label for="c2"><span>Comentarios</span></label><br />
			<input style="width:300px" id="comentario" />
		</td>
		<td align="right">
			<button id="guardar" type="submit">Guardar</button>
		</td>
	</tr>
</table>
<br />
<hr align="center" style="width:90%" />
<center><h1>&Uacute;ltimos registros</h1>
<iframe id="listado" src="./alumnoGrupo_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>

<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>

setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

var grupoDisponible = <%= grupoAction.obtenerGrupoHorarioJson() %>;

$( "#grupo" ).autocomplete({
	source: grupoDisponible,
    select: function (event, ui) {
        $("#grupo").val(ui.item.label); // display the selected text
        $("#grupoId").val(ui.item.id); // save selected id to hidden input
        $("#grupoMonto").val(ui.item.monto); // save selected id to hidden input

        $("#grupoMonto").val("$"+parseFloat($("#grupoMonto").val(), 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString());

        $("#grupoMonto2").val(ui.item.monto); // save selected id to hidden input
        console.log(ui.item.id+" - "+ui.item.label+" - "+ui.item.monto);
    }
});

var alumnoDisponible = <%= clienteAction.obtenerClienteJson() %>

$( "#alumnoNombre" ).autocomplete({
	source: alumnoDisponible,
    select: function (event, ui) {
        $("#alumnoNombre").val(ui.item.label); // display the selected text
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
		var grupo = $("#grupoId").val();
		var alumno = $("#alumnoId").val();
		var beca = $("#becaMonto").val();
		if (beca == null || beca == ""){
			beca = 0.0;
		}
		var comentario = $("#comentario").val();
		if (comentario == null || comentario == ""){
			comentario = "";
		}
		console.log("------------------");
		console.log(grupo);
		console.log(alumno);
		console.log(beca);
		console.log(comentario);
		console.log("------------------");
		$.post('/grupoServlet', {
			proceso    : "AL2",
			fechaAlta  : fechaAlta,
			grupo      : grupo,
			cliente    : alumno,
			comentario : comentario,
			beca       : beca
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var matricula = response.responseText;
				$("#grupo").val("");
				$("#grupoId").val("");
				$("#alumnoNombre").val("");
				$("#alumnoId").val("");
				$("#grupoMonto").val("");
				$("#grupoMonto2").val("");
				$("#beca").val("0%");
				$("#becaMonto").val("0.0");
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

$( "#button" ).button();
$( "#button-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});


$( "#dialog" ).dialog({
	autoOpen: false,
	width: 400,
	buttons: [
		{
			text: "Ok",
			click: function() {
				$( this ).dialog( "close" );
			}
		},
		{
			text: "Cancel",
			click: function() {
				$( this ).dialog( "close" );
			}
		}
	]
});

// Link to open the dialog
$( "#dialog-link" ).click(function( event ) {
	$( "#dialog" ).dialog( "open" );
	event.preventDefault();
});

// Hover states on the static widgets
$( "#dialog-link, #icons li" ).hover(
	function() {
		$( this ).addClass( "ui-state-hover" );
	},
	function() {
		$( this ).removeClass( "ui-state-hover" );
	}
);
</script>

</body>
</html>