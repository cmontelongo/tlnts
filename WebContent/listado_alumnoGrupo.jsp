<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.clienteGrupoAction" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Grupo / Alumnos</title>
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
<%= clienteGrupoAction.obtenerTablaClienteGrupo() %>
</body>
</html>
