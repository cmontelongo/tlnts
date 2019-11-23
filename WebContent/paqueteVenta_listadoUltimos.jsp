<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.productoAction" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Listado Productos/Almacen</title>
	<link href="./css/estilos1.css" rel="stylesheet">
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		font-size=+2;
		margin: 0px;
	}
	</style>
</head>
<body>
<%= productoAction.obtenerUltimosPaqueteVenta()%>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script src="./js/printPage/jquery.printPage.js" type="text/javascript"></script>
<script>
$(document).ready(function() {
    $(".btnPrintR").printPage({
        message:"Generando Recibo..."
      });
});
</script>
</body>
</html>
