<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.clientePagoClaseAction" %>
	<%@ page import="com.talentos.action.clienteAction" %>
	<%@ page import="com.talentos.action.eventoAction" %>
	<%@ page import="com.talentos.action.conceptoAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Captura pagos eventos</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<link href="./js/jtable.2.4.0/themes/metro/blue/jtable.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Recibo Pago Eventos" />
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
			<input id="alumnoId" type="hidden" />
			<input id="alumnoNombre" style="width:400px" type="text" />
		</td>
		<td>
			<label>Fecha pago</label><br />
			<input type="hidden" id="diaId"/>
			<input type="hidden" id="mesId"/>
			<input type="hidden" id="anioId"/>
			<input id="fecha" style="width:200px" type="text" class="date-picker" />
		</td>
	</tr>
	<tr>
		<td>
			<label>Evento</label><br />
			<input id="eventoId" type="hidden" />
			<input id="evento" style="width:250px" />
		</td>
		<td>
			<label>Concepto</label><br />
			<input id="conceptoId" type="hidden" />
			<input id="concepto" style="width:250px" />
		</td>
	</tr>
	<tr>
		<td>
			<label>Pago</label><br />
			<input id="monto" style="width:150px" />
		</td>
		<td>
			<label>Comentarios</label><br />
			<input id="comentario" style="width:250px" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
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
<iframe id="listado" src="./pagoEvento_listadoUltimos.jsp" style="min-width:800px; width:90%;" frameborder="0"></iframe>
</center>
<script>
setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000)

$('.date-picker').datepicker( {
	monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
	closeText: 'Aceptar',
	currentText: 'Actual',
	showButtonPanel: true,
	dateFormat: 'dd MM yy',
	onClose: function(dateText, inst) {
		$("#diaId").val(inst.selectedDay);
		$("#mesId").val(inst.selectedMonth+1);
		$("#anioId").val(inst.selectedYear);
		$(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, inst.selectedDay));
	}
});

var eventoDisponible = <%= eventoAction.obtenerEventoListadoJson() %>;

$( "#evento" ).autocomplete({
	source: eventoDisponible,
    select: function (event, ui) {
        $("#evento").val(ui.item.label);
        $("#eventoId").val(ui.item.id);
    }
});

var conceptoDisponible = <%= conceptoAction.obtenerConceptoListadoJson() %>;

$( "#concepto" ).autocomplete({
	source: conceptoDisponible,
    select: function (event, ui) {
        $("#concepto").val(ui.item.label);
        $("#conceptoId").val(ui.item.id);
    }
});

var alumnoDisponible = <%= clientePagoClaseAction.obtenerListadoCliente() %>;

$( "#alumnoNombre" ).autocomplete({
	source: alumnoDisponible,
    select: function (event, ui) {
        $("#alumnoNombre").val(ui.item.label);
        $("#alumnoId").val(ui.item.id);
    }
});

$(document).ready(function() {
	$('#guardar').click(function(event) {
		var evento = $('#evento').val();
		var eventoId = $("#eventoId").val();
		var concepto = $('#concepto').val();
		var conceptoId = $("#conceptoId").val();
		var alumno = $("#alumnoId").val();
		var dia = $("#diaId").val();
		var mes = $("#mesId").val();
		var anio = $("#anioId").val();
		var comentario = $("#comentario").val();
		var monto = $("#monto").val();
		if (monto == null || monto == ""){
			monto = 0.0
		}
		$.post('/pagoEventoServlet', {
			proceso : "ALT",
			evento : evento,
			eventoId : eventoId,
			concepto : concepto,
			conceptoId : conceptoId,
			alumno : alumno,
			dia : dia,
			mes : mes-1,
			anio : anio,
			comentario : comentario,
			monto : monto
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var respuesta = response.responseText;
				$("#evento").val("");
				$("#eventoId").val("");
				$("#concepto").val("");
				$("#conceptoId").val("");
				$("#alumnoNombre").val("");
				$("#alumnoId").val("");
				$("#monto").val("");
				$("#diaId").val("");
				$("#mesId").val("");
				$("#anioId").val("");
				$("#fecha").val("");
				$("#monto").val("");
				$("#comentario").val("");
				//alert("El registro ha sido agregado. \n Recibo # " + respuesta);
				console.log(respuesta);
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