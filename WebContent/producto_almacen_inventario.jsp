<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.productoAction" %>
	<%@ page import="com.talentos.action.almacenAction" %>
	<%@ page import="com.talentos.action.tallaAction" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Producto / Almacen</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Alta Producto / Almacen" />
</jsp:include>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
String fecha2 = sdf2.format(new Date());
%>
<br />&nbsp;
<input type="hidden" id="fechaAlta" value="<%= fecha2 %>" />
<table style="width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<tr>
		<td>
			Almacen
		</td>
		<td>
			Producto
		</td>
	</tr>
	<tr>
		<td>
			<input id="almacenId" type="hidden" />
			<input id="almacen" title="type &quot;a&quot;" style="width:250px" />
		</td>
		<td>
			<input id="claveId" type="hidden" />
			<input id="clave" title="type &quot;a&quot;" style="width:250px" />
		</td>
	</tr>
	<tr>
		<td>
			Cantidad
		</td>
		<td>
			Precio de Compra
		</td>
		<td>
			Precio Venta
		</td>
	</tr>
	<tr>
		<td>
			<input id="cantidad" style="width:100px" />
		</td>
		<td>
			<input id="precio" style="width:150px" />
		</td>
		<td>
			<input id="costo" style="width:150px" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			Comentario:&nbsp;&nbsp;
			<input id="comentario" style="width:300px" />
		</td>
		<td colspan="3" align="right">
			<button id="guardar" type="submit">Guardar</button>
		</td>
	</tr>
</table>
<br />&nbsp;
<hr align="center" style="width:90%" />
<center><h1>&Uacute;ltimos registros</h1>
<iframe id="listado" src="./prodAlmacenInv_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>
setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

var almacenDisponible = <%= almacenAction.obtenerAlmacenJson() %>;

$( "#almacen" ).autocomplete({
	source: almacenDisponible,
    select: function (event, ui) {
        console.log(ui);
        $("#almacen").val(ui.item.label);
        $("#almacenId").val(ui.item.id);
    }
});

var productoDisponible = <%= productoAction.obtenerProductoAlmacenJson(-1,false) %>;

$( "#clave" ).autocomplete({
	source: productoDisponible,
    select: function (event, ui) {
        console.log(ui);
        $("#clave").val(ui.item.label);
        $("#claveId").val(ui.item.id);
    }
});

$("#precio").change(function() {
	calcularCosto();
});

$("#cantidad").change(function() {
	calcularCosto();
});

var calcularCosto = function(){
	var precio = $("#precio").val();
	var cantidad = $("#cantidad").val();
	if (precio>0 && cantidad>0){
		var costo = precio / cantidad;
	    $("#costo").val(costo);
	}
};

var tallaDisponible = <%= tallaAction.obtenerTallaJson() %>;

$( "#talla" ).autocomplete({
	source: tallaDisponible,
    select: function (event, ui) {
        $("#talla").val(ui.item.label);
        $("#tallaId").val(ui.item.id);
        console.log(ui);
    }
});

$(document).ready(function() {
	$('#guardar').click(function(event) {
		var fechaAlta = $("#fechaAlta").val();
		var clave = $("#claveId").val();
		var cantidad = $("#cantidad").val();
		var precio = $("#precio").val();
		var costo = $("#costo").val();
		var comentario = $("#comentario").val();
		var almacen = $("#almacenId").val();
		var talla = $("#tallaId").val();
		console.log(talla);
		$.post('/productoServlet', {
			proceso    : "AL4",
			fechaAlta  : fechaAlta,
			clave      : clave,
			talla      : talla,
			cantidad   : cantidad,
			precio     : precio,
			costo      : costo,
			comentario : comentario,
			almacen    : almacen
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var respuesta = response.responseText;
				$("#almacenId").val("");
				$("#almacen").val("");
				$("#tallaId").val("");
				$("#talla").val("");
				$("#claveId").val("");
				$("#clave").val("");
				$("#cantidad").val("");
				$("#precio").val("");
				$("#costo").val("");
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

</script>
</body>
</html>