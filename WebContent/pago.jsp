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
<title>Talentos - Captura pagos</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<link href="./js/jtable.2.4.0/themes/metro/blue/jtable.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Recibo Pago Mensualidad" />
</jsp:include>
<%
SimpleDateFormat sdf2 = new SimpleDateFormat(Constantes.fechaFormato);
String fecha2 = sdf2.format(new Date());
%>
<input type="hidden" id="fechaAlta" value="<%= fecha2 %>" /><br />&nbsp;
<table style="padding:10px; width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<tr>
		<td>
			<label>Alumno</label><br />
			<input id="pagoId" type="hidden" />
			<input id="alumnoNombre" style="width:400px" type="text" />
		</td>
		<td>
			<label>Mes</label><br />
			<input id="mesId" type="hidden" />
			<input id="mesNumero" style="width:200px" />
		</td>
	</tr>
	<tr>
		<td>
			<label>Mensualidad</label><br />
			<input id="monto2" type="hidden" />
			<input id="monto" disabled="disabled" style="width:200px" />
		</td>
		<td>
			<label>Pago</label><br />
			<input id="parcial2" type="hidden" />
			<input id="parcial" style="width:150px" />
		</td>
	</tr>
	<tr>
		<td>
			<label>Comentario</label><br />
			<input id="comentario" style="width:250px" />
		</td>
		<td>
			<button id="guardar" type="submit">Guardar</button>
		</td>
	</tr>
</table>
<hr align="center" style="width:90%" />
<center>
<div class="table-title">
<h3>&Uacute;ltimos pagos recibidos</h3>
</div>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script src="./js/printPage/jquery.printPage.js" type="text/javascript"></script>
<iframe id="listado" src="./pago_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>
<script>
setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000)

var numeroMes = [
	{id:1, label:"Enero"},
	{id:2, label:"Febrero"},
	{id:3, label:"Marzo"},
	{id:4, label:"Abril"},
	{id:5, label:"Mayo"},
	{id:6, label:"Junio"},
	{id:7, label:"Julio"},
	{id:8, label:"Agosto"},
	{id:9, label:"Septiembre"},
	{id:10, label:"Octubre"},
	{id:11, label:"Noviembre"},
	{id:12, label:"Diciembre"}
];

$( "#mesNumero" ).autocomplete({
	source: numeroMes,
    select: function (event, ui) {
        $("#mesId").val(ui.item.id);
    }
});

var alumnoDisponible = <%= clientePagoClaseAction.obtenerListadoClienteAdeudo() %>;

$( "#alumnoNombre" ).autocomplete({
	source: alumnoDisponible,
    select: function (event, ui) {
        $("#alumnoNombre").val(ui.item.label);
        $("#pagoId").val(ui.item.id);
        $("#monto2").val(ui.item.monto);
        $("#monto").val("$"+parseFloat($("#monto2").val(), 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString());
        $("#parcial2").val(ui.item.monto);
        $("#parcial").val("$"+parseFloat($("#parcial2").val(), 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString());
    }
});

$(document).ready(function() {
	$('#guardar').click(function(event) {
		var fechaAlta = $('#fechaAlta').val();
		var pagoId = $("#pagoId").val();
		var monto = $("#monto2").val();
		var mes = $("#mesId").val();
		var parcial = $("#parcial").val();
		var comentario = $("#comentario").val();
		if (monto == null || monto == ""){
			monto = 0.0
		}
		$.post('/pagoServlet', {
			proceso : "ALT",
			fechaAlta : fechaAlta,
			mes : mes,
			pagoId : pagoId,
			comentario : comentario,
			monto : monto,
			parcial : parcial,
			comentario : comentario
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var respuesta = response.responseText;
				$("#pagoId").val("");
				$("#alumnoNombre").val("");
				$("#monto").val("");
				$("#mesNumero").val("");
				$("#mesId").val("");
				$("#monto2").val("");
				$("#parcial").val("");
				$("#parcial2").val("");
				$("#comentario").val("");
				//alert("El registro ha sido agregado. \n Recibo # " + respuesta);
				document.location.reload();
			} else {
				alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
			}
		})
		.fail(function(res) {
               console.log("Server Error: " + res.status + " " + res.statusText);
        });
		event.preventDefault();
	});
});

$( "input:text" ).css({
	fontSize: "22px"
});

$( "#guardar" ).button();
$( "#guardar-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

</script>
</body>
</html>