<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.pagoEventoAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.text.ParseException" %>
	<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Corte de Pagos</title>
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		margin: 0px;
	}
	.selectConcilia{
	   height: 30px;
	   overflow: hidden;
	   width: 200px;
	   font-size: 20px;
	}
	</style>
</head>
<body>
<%
int opcion = 1;
int sel = 0;
int evento = 0;
int concepto = 0;
String strDia = "";
String strMes = "";
String strAnio = "";
String[] fecha = {"","",""};
try{
	opcion = Integer.parseInt(request.getParameter("opc"));
	if (request.getParameter("sel") != null)
		sel = Integer.parseInt(request.getParameter("sel"));
	if (request.getParameter("e") != null)
		evento = Integer.parseInt(request.getParameter("e"));
	if (request.getParameter("c") != null)
		concepto = Integer.parseInt(request.getParameter("c"));
	strDia = request.getParameter("dia");
	if (strDia==null && opcion==1) {
		strDia="01";
	}
	strMes = String.format("%02d", Integer.parseInt(request.getParameter("mes")));
	strAnio = request.getParameter("anio");
} catch(Exception e){
	System.out.println("Error:"+e.getLocalizedMessage());
}
fecha[0] = strDia;
fecha[1] = strMes;
fecha[2] = strAnio;
System.out.println("---"+strDia+"/"+strMes+"/"+strAnio);
%>
<%= pagoEventoAction.obtenerTablaPagosCorte2(opcion, fecha, sel, evento, concepto) %>
</body>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script>
var cambia = function(id, e){
	var formaPago = e.selectedIndex;
	$.ajax({
		url: '/pagoEventoServlet',
		type: "POST",
		data: { proceso: "ACT", id: id, m: formaPago}
	});
};
</script>
</html>
