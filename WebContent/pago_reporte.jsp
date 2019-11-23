<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.clientePagoClaseAction" %>
	<%@ page import="com.talentos.action.clienteAction" %>
	<%@ page import="com.talentos.action.eventoAction" %>
	<%@ page import="com.talentos.action.conceptoAction" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Reporte Pagos</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<link href="./js/jtable.2.4.0/themes/metro/blue/jtable.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Cortes de Cobranza" />
</jsp:include>
&nbsp;<br />
<table style="padding:10px; width:'85%'; min-width:800px" align="center" cellspacing="15px" border="0">
	<tr>
		<td>
			Tipo de reporte: <br />
			<select name="selConcepto" id="selConcepto">
				<option id="4" value="4" selected>Concentrado</option>
				<option id="1" value="1">Pago de Mensualidades</option>
				<option id="2" value="2">Pago Eventos</option>
				<option id="3" value="3">Venta de Inventario</option>
			</select>
		</td>
		<td>
			<fieldset>
				<legend>Tipo de reporte: </legend>
				<input type="radio" name="opcReporte" id="opcRpt1" value="1" checked onClick="verificaOpcion(this)">
				<label for="opcRpt1">Corte diario</label>
				<input type="radio" name="opcReporte" id="opcRpt3" value="3" onClick="verificaOpcion(this)">
				<label for="opcRpt3">Corte Mensual</label>
				<input type="radio" name="opcReporte" id="opcMensualidad" value="2" onClick="verificaOpcion(this)">
				<label for="opcMensualidad" id="lblMensualidad">Mensualidad</label>
				<input type="radio" name="opcReporte" id="opcEvento" value="4" onClick="verificaOpcion(this)">
				<label for="opcEvento" id="lblEvento">Por Evento</label>
				<input type="hidden" name="opcReporte" id="opcHistorico" value="5" onClick="verificaOpcion(this)">
				<!-- label for="opcHistorico" type="hidden" id="lblHistorico">Hist&oacute;rico</label -->
			</fieldset>
		</td>
	</tr>
	<tr>
		<td>
			Fecha<br />
			<input type="hidden" id="diaId"/>
			<input type="hidden" id="mesId"/>
			<input type="hidden" id="anioId"/>
			<input name="startDate" id="startDate" style="width:200px; display:none" class="date-picker" />
			<input name="dia" id="dia" style="width:250px" class="date-picker2" />
		</td>
		<td>
			<label for="alumnoNombre" id="lblAlumnoNombre"><span>Alumno</span></label><br />
			<input id="alumnoNombre" title="type &quot;a&quot;" type="text" style="width:350px" />
			<input id="alumnoId" type="hidden" />
		</td>
	</tr>
	<tr>
		<td>
			<fieldset>
				<legend>Incluir pagos duplicados: </legend>
				<input type="radio" name="opcDuplicado" id="opcDuplicado" value="1" >
				<label for="opcDuplicados" id="lblDuplicadosSi">Si</label>
				<input type="radio" name="opcDuplicado" id="opcDuplicado" value="0" checked>
				<label for="opcDuplicados" id="lblDuplicadosNo">No</label>
			</fieldset>
		</td>
		<td>
		</td>
	</tr>
	<tr id="trEvento">
		<td>
			<label for="c2"><span>Evento</span></label><br />
			<input id="evento" title="type &quot;a&quot;" type="text" style="width:350px" />
			<input id="eventoId" type="hidden" />
		</td>
		<td>
			<label for="c2"><span>Concepto</span></label><br />
			<input id="concepto" title="type &quot;a&quot;" type="text" style="width:350px" />
			<input id="conceptoId" type="hidden" />
		</td>
	
	</tr>
	<tr>
		<td align="right">
			<button id="reporte" type="submit">Reporte</button><br />
		</td>
		<td align="left">
			<button id="imprimir" onclick="javascript:imprimirMarco()">Imprimir</button>
		</td>
	</tr>
</table>
<hr align="center" style="width:90%" />
<center>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<iframe id="listado" src="./pago_reporteCorte.jsp" style="min-width:800px; width:100%;" frameborder="0"></iframe>
</center>
<script>
setInterval(function(){
    document.getElementById("listado").style.height = document.getElementById("listado").contentWindow.document.body.scrollHeight + 'px';
},1000);

var alumnoDisponible = <%= clienteAction.obtenerClienteJson() %>

$( "#alumnoNombre" ).autocomplete({
	source: alumnoDisponible,
    select: function (event, ui) {
        $("#alumnoNombre").val(ui.item.label); // display the selected text
        $("#alumnoId").val(ui.item.id); // save selected id to hidden input
        console.log(ui.item.id+" - "+ui.item.label);
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

var sel = function(e){
	console.log("//--//");
	console.log(e);
}

$("#selConcepto").on('selectmenuchange',function () {
	$("#opcMensualidad").hide();
	$("#lblMensualidad").hide();
	$("#opcEvento").hide();
	$("#lblEvento").hide();
	$("#opcHistorico").hide();
	$("#lblHistorico").hide();
	$("#trEvento").hide();
	$("#opcRpt1").prop( "checked", true );
	verificaOpcion(document.getElementById("opcRpt1"));
	$("#alumnoNombre").val("");
	$("#alumnoNombre").hide();
	$("#lblAlumnoNombre").hide();
	$("#alumnoId").val("");
	$("#diaId").val("");
	$("#mesId").val("");
	$("#anioId").val("");
	$("#startDate").val("");
	$("#dia").val("");
	$("#evento").val("");
	$("#eventoId").val("");
	$("#concepto").val("");
	$("#conceptoId").val("");
	var value = $('#selConcepto').val();
    if (value==1){
    	$("#opcMensualidad").show();
    	$("#lblMensualidad").show();
		$("#listado").attr("src", "./pago_reporteCorte.jsp");
    } else if (value==2){
    	$("#opcEvento").show();
    	$("#lblEvento").show();
    	$("#trEvento").show();
    	$("#alumnoNombre").show();
    	$("#lblAlumnoNombre").show();
		$("#listado").attr("src", "./pagoEvento_reporte.jsp");
    } else if (value==3){
		$("#listado").attr("src", "./paqueteVenta_reporte.jsp");
    } else if (value==4){
    	$("#alumnoNombre").show();
    	$("#lblAlumnoNombre").show();
    	//$("#opcHistorico").show();
    	//$("#lblHistorico").show();
		$("#listado").attr("src", "./consolidado_reporteCorte.jsp");
    }
});

$(document).ready(function() {
	$( "#selConcepto" ).selectmenu();
	$("#opcEvento").hide();
	$("#lblEvento").hide();
	$("#trEvento").hide();
	$("#opcMensualidad").hide();
	$("#lblMensualidad").hide();

	$('#reporte').click(function(event) {
		var dia = "";
		var mes = $("#mesId").val();
		var anio = $("#anioId").val();
		var opc = $('input:radio[name=opcReporte]:checked').val();
		if (opc=="1") {
			dia = $("#diaId").val();
		}
		console.log("------------");
		console.log(his);
		var his = $('input:radio[name=opcHistorico]:checked').val();
		if (his === undefined)
			his = 0;
		console.log(his);
		console.log("------------");
		var sel = $('#selConcepto').val();
		var eventoId = $('#eventoId').val();
		var evento = $('#evento').val();
		if (evento == "")
			eventoId = 0;
		var conceptoId = $('#conceptoId').val();
		var concepto = $('#concepto').val();
		if (concepto == "")
			conceptoId = 0;
		var alumnoId = $('#alumnoId').val();
		var alumno = $('#alumnoNombre').val();
		if (alumno == "")
			alumnoId = 0;
		
		var opcDuplicado = $('input:radio[name=opcDuplicado]:checked').val();
		console.log(opcDuplicado);
		if (opcDuplicado == "")
			opcDuplicado = 0;
		console.log(opcDuplicado);
		if (sel == 1){
			// Pagos de Mensualidades
			$("#listado").attr("src", "./pago_reporteCorte.jsp?opc="+opc+"&dia="+dia+"&mes="+mes+"&anio="+anio+"&a="+alumnoId+"&d="+opcDuplicado);
		} else if (sel == 2) {
			// Pago de Eventos
			$("#listado").attr("src", "./pagoEvento_reporte.jsp?opc="+opc+"&dia="+dia+"&mes="+mes+"&anio="+anio+"&sel="+sel+"&e="+eventoId+"&c="+conceptoId+"&a="+alumnoId+"&d="+opcDuplicado);
		} else if (sel == 3) {
			// Venta de Productos / Inventario
			$("#listado").attr("src", "./paqueteVenta_reporte.jsp?opc="+opc+"&dia="+dia+"&mes="+mes+"&anio="+anio+"&sel="+sel+"&e="+eventoId+"&c="+conceptoId+"&a="+alumnoId+"&d="+opcDuplicado);
		} else if (sel==4) {
			// Reporte Consolidado
			$("#listado").attr("src", "./consolidado_reporteCorte.jsp?opc="+opc+"&dia="+dia+"&mes="+mes+"&anio="+anio+"&sel="+sel+"&e="+eventoId+"&c="+conceptoId+"&a="+alumnoId+"&h="+his+"&d="+opcDuplicado);
		}
		event.preventDefault();
	});

	$('.date-picker').datepicker( {
		monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
		monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
		closeText: 'Ok',
		currentText: 'Actual',
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'MM yy',
        onClose: function(dateText, inst) {
        	$("#mesId").val(inst.selectedMonth+1);
        	$("#anioId").val(inst.selectedYear);
            console.log(inst.selectedYear);
            console.log(inst.selectedMonth);
            $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
        }
    });

	$('.date-picker2').datepicker( {
		monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
		monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
		closeText: 'Ok',
		currentText: 'Actual',
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'dd MM yy',
        onClose: function(dateText, inst) {
        	$("#diaId").val(inst.selectedDay);
        	$("#mesId").val(inst.selectedMonth+1);
        	$("#anioId").val(inst.selectedYear);
            console.log(inst.selectedYear);
            console.log(inst.selectedMonth);
            $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, inst.selectedDay));
        }
    });

    $(".date-picker").focus(function () {
        $(".ui-datepicker-calendar").hide();
        $("#ui-datepicker-div").position({
            my: "center top",
            at: "center bottom",
            of: $(this)
        });
    });

});

$( "input:text" ).css({
	fontSize: "22px"
});

$( "#reporte" ).button();
$( "#reporte-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

$( "#imprimir" ).button();
$( "#imprimir-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

$( "#dialog" ).dialog({
	autoOpen: false,
	width: 400,
	buttons: [
		{
			text: "Ok",
			click: function() {
				$( this ).dialog( "close" );
			}
		},
		{
			text: "Cancel",
			click: function() {
				$( this ).dialog( "close" );
			}
		}
	]
});


// Link to open the dialog
$( "#dialog-link" ).click(function( event ) {
	$( "#dialog" ).dialog( "open" );
	event.preventDefault();
});

var verificaOpcion = function(elem){
	console.log(elem);
	$("#dia").removeAttr("disabled"); 
	if (elem.value=="1"){
		$("#dia").css('display','');
		$("#startDate").css('display','none');
	} else if (elem.value=="4"){
		$("#diaId").val('');
		$("#mesId").val('');
		$("#anioId").val('');
		$("#eventoId").val('');
		$("#evento").val('');
		$("#startDate").val('');
		$("#dia").val('');
		$("#dia").css('display','');
		$("#dia").attr("disabled", "disabled"); 
		$("#startDate").css('display','none');
	} else {
		$("#dia").css('display','none');
		$("#startDate").css('display','');
	};
};

var imprimirMarco = function(){
	var iframe = $('#listado')[0];
	iframe.contentWindow.focus();
	iframe.contentWindow.print();
}
</script>
</body>
</html>