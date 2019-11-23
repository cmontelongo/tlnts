<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.clienteAction" %>
	<%@ page import="com.talentos.action.productoAction" %>
	<%@ page import="com.talentos.action.almacenAction" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Venta de Producto/Paquete</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<style>
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
	</style>
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Venta Productos" />
</jsp:include>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
String fecha2 = sdf2.format(new Date());
%>
<input type="hidden" id="fechaAlta" value="<%= fecha2 %>" />
<br />
<table style="width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<tr>
		<td width="50%">
			Cliente
		</td>
		<td colspan="2" width="50%">
			Almacen
		</td>
	</tr>
	<tr>
		<td>
			<input id="clienteId" type="hidden" />
			<input id="cliente" title="type &quot;a&quot;" style="width:350px" />
		</td>
		<td colspan="2">
			<input id="almacenId" type="hidden" />
			<input id="almacen" title="type &quot;a&quot;" style="width:350px" onblur="autocompleteSportsTeams()" />
		</td>
	</tr>
	<tr>
		<td colspan="3">
			Comentario:<br /><input id="comentario" style="width:750px" />
		</td>
	</tr>
	<tr>
		<td>
			<input id="productoId" type="hidden" />
			Producto :<br />
			<input id="producto" title="type &quot;a&quot;" style="width:250px" />
		</td>
		<td>
			Cantidad :<br /><input id="cantidad" style="width:100px" />
		</td>
		<td rowspan="2" align="right">
			<button id="agregaProducto" class="ui-button ui-corner-all ui-widget" onclick="agregar()">Agregar</button>
		</td>
	</tr>
	<tr>
		<td>
			<input id="paqueteId" type="hidden" />
			Paquete :<br />
			<input id="paquete" title="type &quot;a&quot;" style="width:250px" />
		</td>
		<td>
			<input id="venta2" type="hidden" />
			Precio Venta :<br /><input id="venta" style="width:100px" />
		</td>
	</tr>
	<tr>
		<td colspan=3 align="center">
			<table width="100%" border=1 cellpadding="5" cellspacing="7" style="border: 1px solid black;border-collapse: collapse;" id="productoTabla">
				<tr valign="top">
					<td style="border:1px solid black;" align="center" valign="middle" width="50%"><b><i>Producto</i></b></td>
					<td style="border:1px solid black;" align="center" valign="middle" width="10%"><b><i>Cantidad</i></b></td>
					<td style="border:1px solid black;" align="center" valign="middle" width="15%"><b><i>Precio<br/>Unitario</i></b></td>
					<td style="border:1px solid black;" align="center" valign="middle" width="20%"><b><i>Precio</i></b></td>
					<td style="border:1px solid black;" align="center" valign="middle" width="5%"><b><i>Acci&oacute;n</i></b></td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td width="568px" align="right">Total:</td>
					<td style="border:1px solid black;" align="right" width="152px" height="35px" id="totalMonto"></td>
					<td width="50px"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="left">
			<button id="verifica" type="submit">Inventario</button>
		</td>
		<td>&nbsp;</td>
		<td align="right">
			<button id="guardar" type="submit">Guardar Venta</button>
		</td>
	</tr>
</table>
<br />&nbsp;
<hr align="center" style="width:90%" />
<center><h1>&Uacute;ltimos registros</h1>
<iframe id="listado" src="./paqueteVenta_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script src="./js/printPage/jquery.printPage.js" type="text/javascript"></script>
<!-- ui-dialog -->
<div id="dialog" title="Detalle">
	<p id="templates"></p>
</div>
<script>
$( "#dialog" ).dialog({
	autoOpen: false,
	modal: true,
	width: 800,
	buttons: [
		{
			text: "Cerrar",
			click: function() {
				productosList = [];
				$( this ).dialog( "close" );
			}
		}
	]
});


var detalle = function(id){
	console.log("id: "+id);
	var contenido = $.get("/productoServlet?id="+id+"&p=1", function(data, status){
		$("#templates").html(data);
		$( "#dialog" ).dialog( "open" );
		$( "#dialog" ).dialog('option', 'title', 'Detalle Venta');
	});
}

var correo = function(id){
	$("#templates").html("Procesando...");
	$( "#dialog" ).dialog( "open" );
	var contenido = $.get("/productoServlet?id="+id+"&p=2", function(data, status){
		$("#templates").html(data);
	});
}

//Link to open the dialog
$( ".dialog-link" ).click(function( event ) {
	$( "#dialog" ).dialog( "open" );
	event.preventDefault();
});

//Hover states on the static widgets
$( ".dialog-link, #icons li" ).hover(
	function() {
		$( this ).addClass( "ui-state-hover" );
	},
	function() {
		$( this ).removeClass( "ui-state-hover" );
	}
);

var clienteDisponible = <%= clienteAction.obtenerClienteJson() %>;

$( "#cliente" ).autocomplete({
	source: clienteDisponible,
    select: function (event, ui) {
        $("#cliente").val(ui.item.label);
        $("#clienteId").val(ui.item.id);
    }
});

var almacenDisponible = <%= almacenAction.obtenerAlmacenJson() %>;

$( "#almacen" ).autocomplete({
	source: almacenDisponible,
    select: function (event, ui) {
        $("#almacen").val(ui.item.label);
        $("#almacenId").val(ui.item.id);
    }
});

function loadSportsTeams(){
    //Gets the name of the sport entered.
    var sportSelected= $("#almacenId").val();
    var teamList = "";
    $.ajax({
            url: '/productoServlet',
            type: "GET",
            async: false,
            data: { id: sportSelected, p: 4, c: 1, v: 1}
     }).done(function(teams){
        teamList = teams;
     });
  return teamList;
}

function autocompleteSportsTeams(){
  var teams = loadSportsTeams();
  $("#producto").autocomplete({
    source: teams,
    select: function (event, ui) {
        $("#producto").val(ui.item.label);
        $("#productoId").val(ui.item.id);
        $("#paqueteId").val("");
        $("#paquete").val("");
        $("#venta").val(ui.item.v);
        $("#venta2").val(ui.item.vM)
    }
  });
}

var productosDisponibles = [];

$( "#producto" ).autocomplete({
	source : productosDisponibles
});

var paqueteDisponible = <%= productoAction.obtenerPaqueteJson() %>;

$( "#paquete" ).autocomplete({
	source: paqueteDisponible,
    select: function (event, ui) {
        $("#paquete").val(ui.item.label);
        $("#paqueteId").val(ui.item.id);
        $("#productoId").val("");
        $("#producto").val("");
        $("#venta").val(ui.item.v);
        $("#venta2").val(ui.item.vM)
    }
});


var productosList = [];
var paquetesList = [];
var totalA = 0;
var totalM = 0;

var agregar = function(){
	var producto = $("#productoId").val();
	var prodNombre = $("#producto").val();
	var clase = "remCF";
	var monto = 0;
	var cantidad = parseFloat($("#cantidad").val());
	var currency = $("#venta").val();
	currency = currency.replace(/\$/g, '');
	currency = currency.replace(/,/g, '');
	var costo = parseFloat(currency);
	totalA += cantidad;
	if (producto != ""){
		productosList.push({id:producto,c:cantidad,v:costo});
	} else {
		producto = $("#paqueteId").val();
		paquetesList.push({id:producto,c:cantidad,v:costo});
		prodNombre = $("#paquete").val();
		var clase = "remCFP";
	}
	monto = cantidad * costo;
	totalM += monto;
	$("#productoTabla").append("<tr valign='top'><td>"+prodNombre+"</td>"
			+"<td align='right'>"+cantidad+"</td>"
			+"<td align='right'>"+format2(costo,"$")+"</td>"
			+"<td align='right'>"+format2(monto,"$")+"</td>"
			+"<td align='center'><a href='javascript:void(0)' class='"+clase+"' name='"+producto+"' ><img src='./img/minus-circle.png' /></a></td></tr>");
	$("#totalMonto").html(format2(totalM,"$"));
	$("#productoId").val("");
	$("#producto").val("");
	$("#paqueteId").val("");
	$("#paquete").val("");
	$("#cantidad").val("");
	$("#venta").val("");
	$("#venta2").val("");
}

$("#productoTabla").on('click','.remCF',function(){
	var id = $(this).attr('name');
	var tempList = [];
	for (i = 0; i < productosList.length; i++) { 
		if (productosList[i].id != id){
			tempList.push(productosList[i]);
		}
	}
    $(this).parent().parent().remove();
	productosList = tempList;
	actualizaMonto();
});

$("#productoTabla").on('click','.remCFP',function(){
	var id = $(this).attr('name');
	var tempList = [];
	for (i = 0; i < paquetesList.length; i++) { 
		if (paquetesList[i].id != id){
			tempList.push(paquetesList[i]);
		}
	}
    $(this).parent().parent().remove();
    paquetesList = tempList;
	actualizaMonto();
});

var actualizaMonto = function(){
	totalM = 0;
	for (i = 0; i < productosList.length; i++) {
		totalM += (parseFloat(productosList[i].c) * parseFloat(productosList[i].v));
	}
	for (i = 0; i < paquetesList.length; i++) { 
		totalM += (parseFloat(paquetesList[i].c) * parseFloat(paquetesList[i].v));
	}
	$("#totalMonto").html(format2(totalM,"$"));
	
}

setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

$(document).ready(function() {
	$('#verifica').click(function(event) {
		var contenido = $.get("/productoServlet?id=-1&p=3", function(data, status){
			$("#templates").html(data);
			$( "#dialog" ).dialog( "open" );
			$( "#dialog" ).dialog('option', 'title', 'Detalle Inventario');
		});
	});

	$('#guardar').click(function(event) {
		var fechaAlta = $("#fechaAlta").val();
		var cliente = $("#clienteId").val();
		var clienteN = $("#cliente").val();
		var comentario = $("#comentario").val();
		$.post('/productoServlet', {
			proceso    : "AL3",
			cliente    : cliente,
			clienteN   : clienteN,
			productos  : JSON.stringify(productosList),
			paquetes   : JSON.stringify(paquetesList),
			comentario : comentario,
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var respuesta = response.responseText;
				$("#pagoId").val("");
				$("#alumno").val("");
				$("#monto").val("");
				$("#monto2").val("");
				$("#cliente").val("");
				$("#clienteId").val("");
				$("#almacen").val("");
				$("#almacenId").val("");
				$("#comentario").val("");
				alert("El registro ha sido agregado. \n Registro # " + respuesta);
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

$( "#verifica" ).button();
$( "#verifica-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

function format2(n, currency) {
    return currency + " " + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
}

</script>
</body>
</html>