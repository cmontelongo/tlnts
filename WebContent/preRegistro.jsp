<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Pre - Registro</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
</head>
<body>

<button id="registro">Nuevo Alumno</button>

<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>
$( "#registro" ).button();
$( "#registro-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

$('#registro').click(function() {
	$(location).attr('href', './registro.jsp')
});
</script>
</body>
</html>