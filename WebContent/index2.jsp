<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Inicio</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<link href="css/botones.css" rel="stylesheet">
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="" />
</jsp:include>
<div class="header">Administrar</div>
<div class="row">
	<div class="col-3"><a href="./grupo.jsp" class="btnPrincipalv2" target="_blank">Grupos</a></div>
	<div class="col-3"><a href="./clase.jsp" class="btnPrincipalv2" target="_blank">Clases</a></div>
	<div class="col-3"><a href="./salon.jsp" class="btnPrincipalv2" target="_blank">Salones</a></div>
	<div class="col-3"><a href="./maestro.jsp" class="btnPrincipalv2" target="_blank">Maestros</a></div>
</div>
<div class="row">
	<div class="col-4"><a href="./asignar_grupo_clase_horario.jsp" class="btnPrincipalv2" target="_blank">Asignar Grupo - Clases</a></div>
	<div class="col-4"><a href="./registro.jsp" class="btnPrincipalv2" target="_blank">Alta alumno</a></div>
	<div class="col-4"><a href="./asignar_alumno_grupo.jsp" class="btnPrincipalv2" target="_blank">Asignar Alumno a Grupo</a></div>
</div>
<div class="row">
	<div class="col-4"><a href="./listado_alumnoGrupo.jsp" class="btnPrincipalv2" target="_blank">Listado Grupos/Alumnos</a></div>
	<div class="col-4"><a href="./cliente.jsp" class="btnPrincipalv2" target="_blank">Buscar</a></div>
	<div class="col-4"><a href="./asignar_alumno_grupo.jsp" class="btnPrincipalv2" target="_blank">Asignar Alumno a Grupo</a></div>
</div>
<hr />
<div class="header">Pagos</div>
<div class="row">
	<div class="col-6"><a href="./pago.jsp" class="btnPrincipalv3" target="_blank">Registro Pagos</a></div>
	<div class="col-6"><a href="./pago_reporte.jsp" class="btnPrincipalv3" target="_blank">Reporte Pagos</a></div>
</div>
<hr />
<div class="header">Inventario</div>
<div class="row">
	<div class="col-3"><a href="./almacen_alta.jsp" class="btnPrincipalv1" target="_blank">Almacen</a></div>
	<div class="col-3"><a href="./producto_alta.jsp" class="btnPrincipalv1" target="_blank">Producto</a></div>
	<div class="col-3"><a href="./paquete.jsp" class="btnPrincipalv1" target="_blank">Paquete</a></div>
	<div class="col-3"><a href="./paquete_venta.jsp" class="btnPrincipalv1" target="_blank">Venta Paquete</a></div>
</div>
<div class="row">
	<div class="col-6"><a href="./producto_almacen.jsp" class="btnPrincipalv1" target="_blank">Inventario (Almacen/Producto)</a></div>
</div>

</body>
</html>