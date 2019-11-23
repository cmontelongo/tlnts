<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.talentos.entidad.*" %>
<%@ page import="com.talentos.coneccion.*" %>
<%@ page import="com.talentos.action.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%= usuarioAction.obtenerUsuariosJson() %>

<%
List<Clase> c = claseAction.obtenerClase();
if (!c.isEmpty())
	System.out.println(c.get(0).getId());
%>

<%
List<Usuario> u = usuarioAction.obtenerUsuarios();
if (!u.isEmpty())
	System.out.println(u.get(0).getId());
%>

</body>
</html>