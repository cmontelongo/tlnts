<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.clientePagoClaseAction" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Pagos recibidos</title>
	<link href="./css/estilos1.css" rel="stylesheet">
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<link rel="stylesheet" href="https://cdn.linearicons.com/free/1.0.0/icon-font.min.css">
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		font-size=+2;
		margin: 0px;
		padding: 0px;
	}
	a:link {
		text-decoration: none;
	}
	.lnr {
		font-size: 30px;
		text-decoration: none;
		color: #0F0F0F;
	}
	.lnr:hover {
		font-size: 35px;
		text-decoration: none;
		color: #aaa;
	}
	</style>
</head>
<body>
<%= clientePagoClaseAction.obtenerTablaPagos() %>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script src="./js/printPage/jquery.printPage.js" type="text/javascript"></script>
<!-- ui-dialog -->
<div id="dialog2" title="Detalle Pago">
	<p id="templates2">Test</p>
</div>
<script>
$( "#dialog2" ).dialog({
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

$(document).ready(function() {
    $(".btnPrint").printPage({
        message:"Generando Recibo..."
      });
});

var correo = function(id){
	console.log(id);
	$("#templates2").html("Procesando...");
	$( "#dialog2" ).dialog( "open" );
	//event.preventDefault();
	var contenido = $.get("/pagoServlet?id="+id+"&p=2", function(data, status){
		console.log(data);
		console.log(status);
		$("#templates2").html(data);
	});
}

var detalle = function(id){
	console.log(id);
	$("#templates2").html("Procesando...");
	$( "#dialog2" ).dialog( "open" );
	var contenido = $.get("/pagoServlet?id="+id+"&p=1", function(data, status){
		$("#templates2").html(data);
	});
}

</script>
</body>
</html>
