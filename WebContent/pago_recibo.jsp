<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.clientePagoClaseAction" %>
	<%@ page import="com.talentos.action.clienteAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Pago/Recibo</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<link href="./js/jtable.2.4.0/themes/metro/blue/jtable.min.css" rel="stylesheet" type="text/css" />
	<style>
	body{
		font-family: "Trebuchet MS", "Roboto", helvetica, arial, sans-serif;
		font-size: 25px;
		margin: 30px;
	}
	</style>
</head>
<body>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat(Constantes.fechaFormato);
String fecha2 = sdf2.format(new Date());
%>
<input type="hidden" id="fechaAlta" value="<%= fecha2 %>" />
<table style="padding:10px; width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<thead>
	<tr>
		<td>
			Nombre
		</td>
		<td>
			Grupo
		</td>
		<td>
			Mensualidad
		</td>
		<td>
			Monto
		</td>
		<td>
			Fecha Pago
		</td>
	</tr>
	</thead>
	<tr>
		<td>
		AAA
		</td>
		<td>
		G1
		</td>
		<td>
		Abr
		</td>
		<td>
		$1,000.00
		</td>
		<td>
		13/Abr 15:01
		</td>
	</tr>
	<tr>
		<td colspan="2" align="left">
			<button id="imprimir" onclick="javascript:imprimirMarco()">Imprimir</button>
		</td>
	</tr>
</table>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>

$( "#imprimir" ).button();
$( "#imprimir-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

var imprimirMarco = function(){
	var iframe = $('#listado')[0];
	iframe.contentWindow.focus();
	iframe.contentWindow.print();
}
</script>
</body>
</html>