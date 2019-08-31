<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.grupoAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.util.Date" %>
	<%@ page import="java.text.DateFormat" %>
	<%@ page import="java.text.ParseException" %>
	<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Listado Grupos</title>
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		font-size: 10px;
		margin: 0px;
	}
	</style>
</head>
<body>
<%
int[] params = {0, -1, 0, 0};

if ( (request.getParameter("c") != null) && (!request.getParameter("c").equals("")))
	params[0] = Integer.parseInt(request.getParameter("c"));

if ( (request.getParameter("g") != null) && (!request.getParameter("g").equals("")))
	params[1] = Integer.parseInt(request.getParameter("g"));
if ( (request.getParameter("m") != null) && (!request.getParameter("m").equals("")))
	params[2] = Integer.parseInt(request.getParameter("m"))-1;
if ( (request.getParameter("l") != null) && (!request.getParameter("l").equals("")))
	params[3] = Integer.parseInt(request.getParameter("l"));
System.out.println(params[0]+","+params[1]+","+params[2]+","+params[3]);
%>
<%= grupoAction.obtenerTablaGrupo(params) %>
</body>
</html>
