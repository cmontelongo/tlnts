<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.grupoAction" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Talentos - Actualiza Alumnos Grupo</title>
	<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<link rel="stylesheet" href="https://cdn.linearicons.com/free/1.0.0/icon-font.min.css">
	<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		margin: 0px;
	}
	#dialog-link #dialog-add {
		padding: .4em 1em .4em 20px;
		text-decoration: none;
		position: relative;
	}
	#dialog-link #dialog-add span.ui-icon {
		margin: 0 5px 0 0;
		position: absolute;
		left: .2em;
		top: 50%;
		margin-top: -8px;
	}
	a:link {
		text-decoration: none;
	}
	.lnr {
		font-size: 30px;
		text-decoration: none;
	}
	.lnr:hover {
		font-size: 35px;
		text-decoration: none;
	}
	.lnr-pencil {
		color: #0F0F0F;
	}
	.lnr-pencil:hover {
		color: #aaa;
	}
	.lnr-trash {
		color: #ba0000;
	}
	.lnr-trash:hover {
		color: #ffbaba;
	}
	.lnr-sync {
		color: #0002aa;
	}
	.lnr-sync:hover {
		color: #8385db;
	}
	</style>
</head>
<body>
<!-- ui-dialog -->
<div id="dialog" title="Detalle">
	<p id="templates"></p>
</div>
<!-- ui-dialog -->
<div id="dialogGrupo" title="Mover de Grupo">
	<p id="tmpGrupo"></p>
</div>
<!-- ui-dialog -->
<div id="dialogGrupo2" title="Mover Grupo">
	<p id="tmpGrupo2"></p>
</div>
<!-- ui-dialog -->
<div id="dialogGrupo3" title="Mover Grupo">
	<p id="tmpGrupo3"></p>
</div>
<%
int[] params = {0, -1, 0, 0};

if ( (request.getParameter("c") != null) && (!request.getParameter("c").equals("")))
	params[0] = Integer.parseInt(request.getParameter("c"));

if ( (request.getParameter("g") != null) && (!request.getParameter("g").equals("")))
	params[1] = Integer.parseInt(request.getParameter("g"));
if ( (request.getParameter("m") != null) && (!request.getParameter("m").equals("")))
	params[2] = Integer.parseInt(request.getParameter("m"))-1;
if ( (request.getParameter("l") != null) && (!request.getParameter("l").equals("")))
	params[3] = Integer.parseInt(request.getParameter("l"));
System.out.println(params[0]+","+params[1]+","+params[2]+","+params[3]);
%>
<%= grupoAction.obtenerTablaGrupo(params) %>
</body>
<script>

var editar = function(id){
	console.log(id);
	var contenido = $.get("/grupoServlet?m="+id+"&p=d", function(data, status){
		$("#templates").html(data);
		$( "#dialog" ).dialog( "open" );
		$( "#dialog" ).dialog('option', 'title', 'Detalle Alumno');
	});
};

var baja = function(grupoNombre, alumnoId, alumnoNombre, id){
	if (confirm("Dar de baja el alumno ("+alumnoId+")"+alumnoNombre+" del Grupo "+grupoNombre+" ?")){
		console.log($("#"+alumnoId));
		$("#"+alumnoId).hide();
		$.post('/grupoServlet', {
			proceso : "ACT",
			id		: id,
			estatus	: -1
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				alert("El Alumno ha sido dado de baja.");
				location.reload();
			} else {
				alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
			}
		})
		.fail(function(res) {
               console.log("Server Error: " + res.status + " " + res.statusText);
           });
	}
}

var actualiza = function(e){
	console.log(e);
	console.log(e.id);
	console.log(e.value);
	var beca = e.value;
	var monto = $("#monto").val();
	var total = 0;
	total = monto - ((beca*monto)/100);
	$("#total").val(format(total,"$"));
}

function format(n, currency) {
    return currency + "" + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
}


var grupoDisponible = <%= grupoAction.obtenerGrupoHorarioJson() %>;


var cambio = function(clienteNombre,clienteId){
	console.log(clienteId);
	console.log(clienteNombre);
	var contenido = $.get("/grupoServlet?p=d&c=1&id="+clienteId+"&m="+clienteNombre, function(data, status){
		$("#tmpGrupo3").html(data);
		$( "#dialogGrupo3" ).dialog( "open" );
		$( "#dialogGrupo3" ).dialog('option', 'title', 'Mover Alumno de Grupo');
		$('#grupo').autocomplete({
			source: grupoDisponible,
			select: function (event, ui) {
				$('#grupo').val(ui.item.label);
				$('#grupoId').val(ui.item.id);
			}
		});
	});
};

var cambioTodo = function(grupoId, grupoNombre){
	var seleccion = $("#selAccion");
	console.log(seleccion.prop('selectedIndex'));
	if (seleccion.prop('selectedIndex')==0){
		var contenido = $.get("/grupoServlet?p=d&c=2&idA="+grupoId, function(data, status){
			$("#tmpGrupo2").html(data);
			$( "#dialogGrupo2" ).dialog( "open" );
			$( "#dialogGrupo2" ).dialog('option', 'title', 'Mover Alumnos de Grupo');
			$('#grupo').autocomplete({
				source: grupoDisponible,
				select: function (event, ui) {
					$('#grupo').val(ui.item.label);
					$('#grupoId').val(ui.item.id);
				}
			});
		});
	} else {
		if (confirm("Dar de baja el grupo "+grupoNombre+" ?")){
			console.log($("#G_"+grupoId));
			console.log(grupoId);
			$("#G_"+grupoId).hide();
			$.post('/grupoServlet', {
				proceso : "AC3",
				idA		: grupoId,
				estatus	: -1
			})
			.done(function(data) {
				var response = JSON.parse(data);
				var success = response.success;
				if (success) {
					alert("El grupo ha sido actualizado.");
					location.reload();
				} else {
					alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
				}
			})
			.fail(function(res) {
	               console.log("Server Error: " + res.status + " " + res.statusText);
	           });
		}
	}
};


$( "#dialog" ).dialog({
	autoOpen: false,
	width: 500,
	buttons: [
		{
			text: "Ok",
			click: function() {
				var id = $("#id").val();
				var estatus = 0;
				var beca = 0;
				if($("#estatus").is(':checked'))
					estatus = 1;
				console.log(estatus);
				if (estatus==1){
					$("#tr_"+id).hide();
				}
				beca = $("#beca").val();
				$.post('/grupoServlet', {
					proceso 	: "ACT",
					id			: id,
					beca		: beca,
					estatus		: estatus
				})
				.done(function(data) {
					var response = JSON.parse(data);
					var success = response.success;
					if (success) {
						alert("El registro ha sido actualizado.");
						location.reload();
					} else {
						alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
					}
				})
				.fail(function(res) {
		               console.log("Server Error: " + res.status + " " + res.statusText);
		           });
				//event.preventDefault();
				$( this ).dialog( "close" );
			}
		},
		{
			text: "Cancelar",
			click: function() {
				$( this ).dialog( "close" );
			}
		}
	]
});

$( "#dialogGrupo" ).dialog({
	autoOpen: false,
	width: 700,
	buttons: [
		{
			text: "Ok",
			click: function() {
				var id = $("#id").val();
				var estatus = 0;
				var beca = 0;
				if($("#estatus").is(':checked'))
					estatus = 1;
				if (estatus==1){
					$("#tr_"+id).hide();
				}
				beca = $("#beca").val();
				$.post('/grupoServlet', {
					proceso 	: "ACT",
					id			: id,
					beca		: beca,
					estatus		: estatus
				})
				.done(function(data) {
					var response = JSON.parse(data);
					var success = response.success;
					if (success) {
						alert("El registro ha sido actualizado.");
						location.reload();
					} else {
						alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
					}
				})
				.fail(function(res) {
		               console.log("Server Error: " + res.status + " " + res.statusText);
		           });
				//event.preventDefault();
				$( this ).dialog( "close" );
			}
		},
		{
			text: "Cancelar",
			click: function() {
				$( this ).dialog( "close" );
			}
		}
	]
});

$( "#dialogGrupo2" ).dialog({
	autoOpen: false,
	width: 700,
	buttons: [
		{
			text: "Ok",
			click: function() {
				var idN = $("#grupoId").val();
				var id = $("#grupoActual").val();
				$.post('/grupoServlet', {
					proceso : "AC3",
					idN		: idN,
					idA		: id
				})
				.done(function(data) {
					var response = JSON.parse(data);
					var success = response.success;
					if (success) {
						alert("El registro ha sido actualizado.");
						location.reload();
					} else {
						alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
					}
				})
				.fail(function(res) {
		               console.log("Server Error: " + res.status + " " + res.statusText);
		           });
				//event.preventDefault();
				$( this ).dialog( "close" );
			}
		},
		{
			text: "Cancelar",
			click: function() {
				$( this ).dialog( "close" );
			}
		}
	]
});

$( "#dialogGrupo3" ).dialog({
	autoOpen: false,
	width: 700,
	buttons: [
		{
			text: "Ok",
			click: function() {
				var idC = $("#clienteId").val();
				var idA = $("#grupoActual").val();
				var idN = $("#grupoId").val();
				$("#tr_"+idC).hide();
				console.log(idC);
				console.log(idA);
				console.log(idN);
				$.post('/grupoServlet', {
					proceso : "AC2",
					id		: idC,
					idA		: idA,
					idN		: idN
				})
				.done(function(data) {
					var response = JSON.parse(data);
					var success = response.success;
					if (success) {
						alert("El registro ha sido actualizado.");
						location.reload();
					} else {
						alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
					}
				})
				.fail(function(res) {
		               console.log("Server Error: " + res.status + " " + res.statusText);
		           });
				//event.preventDefault();
				$( this ).dialog( "close" );
			}
		},
		{
			text: "Cancelar",
			click: function() {
				$( this ).dialog( "close" );
			}
		}
	]
});

</script>
</html>
