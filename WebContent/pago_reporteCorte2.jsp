<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.clientePagoClaseAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.util.Date" %>
	<%@ page import="java.text.DateFormat" %>
	<%@ page import="java.text.ParseException" %>
	<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Pagos recibidos</title>
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
<%
int opcion = 1;
String strDia = "01";
String strMes = "01";
String strAnio = "2017";
Date fechaInicial = new Date();
String[] fecha = {"","",""};
try{
	opcion = Integer.parseInt(request.getParameter("opc"));
	strDia = request.getParameter("dia");
	if (strDia==null) strDia="01";
	strMes = request.getParameter("mes");
	strAnio = request.getParameter("anio");
} catch(Exception e){
	System.out.println(e.getLocalizedMessage());
}
fecha[0] = strDia;
fecha[1] = strMes;
fecha[2] = strAnio;
System.out.println(strDia+"/"+strMes+"/"+strAnio);
%>
<%= clientePagoClaseAction.obtenerTablaPagosCorte(opcion, fecha) %>
</body>
</html>
