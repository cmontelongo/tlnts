<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.productoAction" %>
	<%@ page import="com.talentos.action.almacenAction" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Paquete</title>
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
	<jsp:param name="titulo" value="Alta Paquete" />
</jsp:include>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
String fecha2 = sdf2.format(new Date());
%>
<br />&nbsp;
<input type="hidden" id="fechaAlta" value="<%= fecha2 %>" />
<table style="width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<thead>
		<tr>
			<td>
				Nombre
			</td>
			<td>
				Almacen
			</td>
			<td>
				Precio Compra
			</td>
			<td>
				Precio Venta
			</td>
			<td>
				Comentario
			</td>
		</tr>
	</thead>
	<tr>
		<td>
			<input id="paquete" style="width:150px" />
		</td>
		<td>
			<input id="almacenId" type="hidden" />
			<input id="almacen" title="type &quot;a&quot;" style="width:250px" />
		</td>
		<td>
			<input id="compra" style="width:100px" />
		</td>
		<td>
			<input id="venta" style="width:100px" />
		</td>
		<td>
			<input id="comentario" style="width:100%" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<button id="dialog-link" class="ui-button ui-corner-all ui-widget">
				<span class="ui-icon ui-icon-newwin"></span>Agregar Productos
			</button>
		</td>
		<td colspan="2" align="right">
			<button id="guardar" type="submit">Guardar</button>
		</td>
	</tr>
</table>
<!-- ui-dialog -->
<div id="dialog" title="Agregar Productos al Paquete">
	<table width="100%" border="0">
		<tr>
			<td width="15%" align="left">Producto : </td>
			<td width="70%" align="left"><input id="producto" title="type &quot;a&quot;" style="width:250px" /><input type="hidden" id="productoId"/></td>
			<th width="15%" rowspan="2" align="center">
				<button id="agregaProducto" class="ui-button ui-corner-all ui-widget" onclick="agregar()">
					Agregar
				</button>
			</th>
		</tr>
		<tr>
			<td align="left">Cantidad : </td>
			<td align="left"><input type="text" id="productoCantidad" size="10" /></td>
		</tr>
		<tr>
			<td colspan=3 align="center">
				<table width="90%" border=1 cellpadding="5" cellspacing="7" style="border: 1px solid black;border-collapse: collapse;" id="productoTabla">
					<tr valign="top">
						<td style="border: 1px solid black;" align="center" width="75%"><b><i>Producto</i></b></td>
						<td style="border: 1px solid black;" align="center" width="20%"><b><i>Cantidad</i></b></td>
						<td style="border: 1px solid black;" align="center" width="5%"><b><i>Acci&oacute;n</i></b></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
<br />&nbsp;
<hr align="center" style="width:90%" />
<center><h1>&Uacute;ltimos registros</h1>
<iframe id="listado" src="./paquete_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>
var almacenDisponible = <%= almacenAction.obtenerAlmacenJson() %>;

$( "#almacen" ).autocomplete({
	source: almacenDisponible,
    select: function (event, ui) {
        console.log(ui);
        $("#almacen").val(ui.item.label);
        $("#almacenId").val(ui.item.id);
    }
});

var productosList = [];

var agregar = function(){
	productosList.push({id:$("#productoId").val(),c:$("#productoCantidad").val()});
	$("#productoTabla").append("<tr valign='top'><td>"+$("#producto").val()+"</td><td>"+$("#productoCantidad").val()+"</td><td align='center'><a href='javascript:void(0)' class='remCF' name='"+$("#productoId").val()+"' ><img src='./img/minus-circle.png' /></a></td></tr>");
	$("#producto").val("");
	$("#productoId").val("");
	$("#productoCantidad").val("");
	console.log(productosList);
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
	console.log(productosList);
});


setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

var productoDisponible = <%= almacenAction.obtenerProductoAlmacenJson() %>;

$( "#producto" ).autocomplete({
	appendTo: "#dialog",
	source: productoDisponible,
    select: function (event, ui) {
        $("#producto").val(ui.item.label);
        $("#productoId").val(ui.item.id);
        console.log(ui);
    }
});


$(document).ready(function() {
	$('#guardar').click(function(event) {
		var fechaAlta = $("#fechaAlta").val();
		var paquete = $("#paquete").val();
		var compra = $("#compra").val();
		var venta = $("#venta").val();
		var productos = JSON.stringify(productosList);
		var comentario = $("#comentario").val();
		$.post('/productoServlet', {
			proceso    : "AL2",
			paquete    : paquete,
			compra     : compra,
			venta      : venta,
			productos  : productos,
			comentario : comentario,
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var respuesta = response.responseText;
				$("#paquete").val("");
				$("#almacen").val("");
				$("#almacenId").val("");
				$("#venta").val("");
				$("#compra").val("");
				$("#comentario").val("");
				alert("El registro ha sido agregado. \n Producto # " + respuesta);
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


$( "#dialog" ).dialog({
	autoOpen: false,
	modal: true,
	width: 800,
	buttons: [
		{
			text: "Ok",
			click: function() {
				console.log(JSON.stringify(productosList));
				$( this ).dialog( "close" );
			}
		},
		{
			text: "Cancelar",
			click: function() {
				productosList = [];
				$( this ).dialog( "close" );
			}
		}
	]
});

//Link to open the dialog
$( "#dialog-link" ).click(function( event ) {
	$( "#dialog" ).dialog( "open" );
	event.preventDefault();
});

//Hover states on the static widgets
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