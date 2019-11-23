<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.clienteAction" %>
	<%@ page import="com.talentos.action.productoAction" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Productos</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		font-size=+2;
		margin: 30px;
	}
	</style>
</head>
<body>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
String fecha2 = sdf2.format(new Date());
%>
<br />&nbsp;
<input type="hidden" id="fechaAlta" value="<%= fecha2 %>" />
<table style="width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<thead>
		<td>
			Producto
		</td>
		<td>
			Cliente
		</td>
		<td>
			Cantidad
		</td>
		<td>
			Precio
		</td>
		<td>
			Comentario
		</td>
	</thead>
	<tr>
		<td>
			<input id="productoId" type="hidden" />
			<input id="costo" type="hidden" />
			<input id="producto" title="type &quot;a&quot;" style="width:250px" />
		</td>
		<td>
			<input id="clienteId" type="hidden" />
			<input id="cliente" title="type &quot;a&quot;" style="width:250px" />
		</td>
		<td>
			<input id="cantidad" style="width:100px" />
		</td>
		<td>
			<input id="precio2" type="hidden" />
			<input id="precio" disabled="disabled" style="width:150px" />
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
<center><h1>&Uacute;ltimos pagos recibidos</h1>
<iframe id="listado" src="./pago_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>
setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

var productoDisponible = <%= productoAction.obtenerProductoCostoJson() %>;

$( "#producto" ).autocomplete({
	source: productoDisponible,
    select: function (event, ui) {
        $("#producto").val(ui.item.label);
        $("#productoId").val(ui.item.id);
        $("#costo").val(ui.item.costo);
    }
});


var clienteDisponible = <%= clienteAction.obtenerClienteJson() %>;

$( "#cliente" ).autocomplete({
	source: clienteDisponible,
    select: function (event, ui) {
        $("#cliente").val(ui.item.label);
        $("#clienteId").val(ui.item.id);
    }
});


$("#cantidad").change(function() {
	calcularCosto();
});

var calcularCosto = function(){
	var costo = $("#costo").val();
	var cantidad = $("#cantidad").val();
	console.log(costo);
	console.log(cantidad);
	if (costo>0 && cantidad>0){
		var precio = costo * cantidad;
	    $("#precio2").val(precio);
        $("#precio").val("$"+parseFloat($("#precio2").val(), 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString());
	}
};

$(document).ready(function() {
	$('#guardar').click(function(event) {
		var fechaAlta = $("#fechaAlta").val();
		var clave = $("#claveId").val();
		var cantidad = $("#cantidad").val();
		var precio = $("#precio").val();
		var costo = $("#costo").val();
		var comentario = $("#comentario").val();
		$.post('/productoServlet', {
			proceso    : "AL1",
			fechaAlta  : fechaAlta,
			clave : clave,
			cantidad : cantidad,
			precio : precio,
			costo : costo,
			comentario : comentario
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
				$("#comentario").val("");
				alert("El registro ha sido agregado. \n Producto # " + respuesta);
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