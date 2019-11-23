<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.pagoEventoAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.text.ParseException" %>
	<%@ page import="java.text.SimpleDateFormat" %>
	<%@ page import="java.util.HashMap" %>
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
String alumnoId="";
try{
	opcion = Integer.parseInt(request.getParameter("opc"));
	if (request.getParameter("sel") != null)
		sel = Integer.parseInt(request.getParameter("sel"));
	if (request.getParameter("e") != null)
		evento = Integer.parseInt(request.getParameter("e"));
	if (request.getParameter("c") != null)
		concepto = Integer.parseInt(request.getParameter("c"));
	if (request.getParameter("a") != null)
		alumnoId = request.getParameter("a");
	strDia = request.getParameter("dia");
	if (strDia==null && opcion==1) {
		strDia="01";
	}
	strMes = String.format("%02d", Integer.parseInt(request.getParameter("mes")));
	strAnio = request.getParameter("anio");
} catch(Exception e){
	System.out.println("Error:"+e.getLocalizedMessage());
}
System.out.println("---"+strDia+"/"+strMes+"/"+strAnio);
System.out.println("alumno="+alumnoId);
HashMap<String,String> parametros = new HashMap<String,String>();
parametros.put("o", ""+opcion);
parametros.put("f",strDia.concat(",").concat(strMes).concat(",").concat(strAnio));
parametros.put("s", ""+sel);
parametros.put("e", ""+evento);
parametros.put("c", ""+concepto);
parametros.put("a", ""+alumnoId);
%>
<%= pagoEventoAction.obtenerTablaPagosCorte(parametros) %>
</body>
</html>
