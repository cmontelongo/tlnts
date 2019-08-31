<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.clienteAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Registro Alumno</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		font-size=+2;
		margin: 20px;
	}
	.demoHeaders {
		margin-top: 2em;
	}
	#dialog-link {
		padding: .4em 1em .4em 20px;
		text-decoration: none;
		position: relative;
	}
	#dialog-link span.ui-icon {
		margin: 0 5px 0 0;
		position: absolute;
		left: .2em;
		top: 50%;
		margin-top: -8px;
	}
	#icons {
		margin: 0;
		padding: 0;
	}
	#icons li {
		margin: 2px;
		position: relative;
		padding: 4px 0;
		cursor: pointer;
		float: left;
		list-style: none;
	}
	#icons span.ui-icon {
		float: left;
		margin: 0 4px;
	}
	.fakewindowcontain .ui-widget-overlay {
		position: absolute;
	}
	select {
		width: 50px;
	}

.checkbox {
    position: relative;
    z-index: 10;
}

span.checkbox {
    z-index: 5;
    display: inline-block;
    width: 15px;
    height: 15px;
    background: #fff;
    border: 1px solid #000;
    margin: 1px 0 0 -14px;
    top: 3px;
    float: left;
    &.on {
        background: $blue;
    }
}
	</style>
<%
SimpleDateFormat sdf = new SimpleDateFormat("dd / MMM / yyyy");
SimpleDateFormat sdf2 = new SimpleDateFormat(Constantes.fechaFormato);
String fecha = sdf.format(new Date());
String fecha2 = sdf2.format(new Date());
%>
</head>
<body>
<table style="85%" align="center" cellpadding="5px" border="0" >
	<tr>
		<td rowspan="2" colspan="4" align="center">
			<font size="+4"><b>REGISTRO DE INSCRIPCION</b></font>
		</td>
		<td align="right">
			<i>Matricula:</i>
		</td>
		<td>
			<input type="text" disabled="disabled" id="matricula" value="" />
		</td>
	</tr>
	<tr>
		<td align="right">
			<i>Fecha:</i>
		</td>
		<td>
			<input type="text" id="fechaAlta" value="<%= fecha %>" disabled="disabled" size="13" />
			<input type="hidden" id="fecha2" value="<%= fecha2 %>" />
		</td>
	</tr>
	<tr>
		<td colspan="6">
			Nombre:<br />
			<input style="width:100%" id="nombre" />
		</td>
	</tr>
	<tr>
		<td>
			Fecha de nacimiento:<br />
			<input type="text" id="fechaNacimiento" />
		</td>
		<td>
			Edad:<br />
			<input type="text" id="edad" disabled="disabled" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			Calle:<br />
			<input style="width:100%" id="calle" />
		</td>
		<td>
			Numero exterior:<br />
			<input style="width:100%" id="numero" />
		</td>
		<td colspan="3">
			Colonia
			<input style="width:100%" id="colonia" />
		</td>
	</tr>
	<tr>
		<td colspan="5">
			Municipio:<br />
			<input style="width:100%" id="municipio" />
		</td>
		<td>
			Codigo Postal:<br />
			<input style="width:100%" id="codigoPostal" />
		</td>
	</tr>
	<tr>
		<td colspan="3">
			Nombre Mam&aacute;:<br />
			<input style="width:100%" id="madreNombre" />
		</td>
		<td colspan="3">
			Ocupacion:<br />
			<input style="width:100%" id="madreOcupacion" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
			Telefono Casa:<br />
			<input style="width:100%" id="madreTelefonoCasa" />
		</td>
		<td colspan="2">
			Celular:<br />
			<input style="width:100%" id="madreTelefonoCelular" />
		</td>
		<td colspan="2">
			Telefono Oficina:<br />
			<input style="width:100%" id="madreTelefonoOficina" />
		</td>
	</tr>
	<tr>
		<td colspan="3">
			Correo electronico:<br />
			<input style="width:100%" id="madreEmail" />
		</td>
		<td colspan="3">
			Telefono para dejar recado:<br />
			<input style="width:100%" id="madreTelefonoRecado" />
		</td>
	</tr>
	<tr>
		<td colspan="4">
			Nombre Pap&aacute;:<br />
			<input style="width:100%" id="padreNombre" />
		</td>
		<td colspan="2">
			Ocupacion:<br />
			<input style="width:100%" id="padreOcupacion" />
		</td>
	</tr>
	<tr>
		<td colspan="3">
			Celular:<br />
			<input style="width:100%" id="padreTelefonoCelular" />
		</td>
		<td colspan="3">
			Telefono Oficina:<br />
			<input style="width:100%" id="padreTelefonoOficina" />
		</td>
	</tr>
	<tr>
		<td colspan="6">
			Correo electronico:<br />
			<input style="width:100%" id="padreEmail" />
		</td>
	</tr>
	<tr>
		<td colspan="6">
			<table style="width:100%" border=0 cellpadding="20px">
				<tr>
					<td align="right" style="width:50%;">
						<button id="back">Regresar</button>
					</td>
					<td align="left">
						<button id="guardar" type="submit">Guardar</button>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br />

<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>
$(document).ready(function() {
	$('#guardar').click(function(event) {
		var fechaAlta = $('#fecha2').val();
		var nombre = $("#nombre").val();
		var fechaNacimiento = $("#fechaNacimiento").val();
		var calle = $('#calle').val();
		var numero = $('#numero').val();
		var colonia = $("#colonia").val();
		var municipio = $("#municipio").val();
		var codigoPostal = $("#codigoPostal").val();
		var madreNombre = $('#madreNombre').val();
		var madreOcupacion = $('#madreOcupacion').val();
		var madreTelefonoCasa = $("#madreTelefonoCasa").val();
		var madreTelefonoCelular = $("#madreTelefonoCelular").val();
		var madreTelefonoOficina = $("#madreTelefonoOficina").val();
		var madreEmail = $('#madreEmail').val();
		var madreTelefonoRecado = $('#madreTelefonoRecado').val();
		var padreNombre = $("#padreNombre").val();
		var padreOcupacion = $("#padreOcupacion").val();
		var padreTelefonoCelular = $("#padreTelefonoCelular").val();
		var padreTelefonoOficina = $("#padreTelefonoOficina").val();
		var padreEmail = $("#padreEmail").val();
		$.post('/clienteServlet', {
			proceso : "ALT",
			fechaAlta : fechaAlta,
			nombre : nombre,
			fechaNacimiento : fechaNacimiento,
			calle : calle,
			numero : numero,
			colonia : colonia,
			municipio : municipio,
			codigoPostal : codigoPostal,
			madreNombre : madreNombre,
			madreOcupacion : madreOcupacion,
			madreTelefonoCasa : madreTelefonoCasa,
			madreTelefonoCelular : madreTelefonoCelular,
			madreTelefonoOficina : madreTelefonoOficina,
			madreEmail : madreEmail,
			madreTelefonoRecado : madreTelefonoRecado,
			padreNombre : padreNombre,
			padreOcupacion : padreOcupacion,
			padreTelefonoCelular : padreTelefonoCelular,
			padreTelefonoOficina : padreTelefonoOficina,
			padreEmail : padreEmail
		})
		.done(function(data) {
			var response = JSON.parse(data);
			var success = response.success;
			if (success) {
				var matricula = response.responseText;
				$("#guardar").prop('disabled',true);
				$("#back").prop('disabled',false);
				$("#matricula").val(matricula);
				$('#fecha2').prop('disabled',true);
				$("#nombre").prop('disabled',true);
				$("#fechaNacimiento").prop('disabled',true);
				$('#calle').prop('disabled',true);
				$('#numero').prop('disabled',true);
				$("#colonia").prop('disabled',true);
				$("#municipio").prop('disabled',true);
				$("#codigoPostal").prop('disabled',true);
				$('#madreNombre').prop('disabled',true);
				$('#madreOcupacion').prop('disabled',true);
				$("#madreTelefonoCasa").prop('disabled',true);
				$("#madreTelefonoCelular").prop('disabled',true);
				$("#madreTelefonoOficina").prop('disabled',true);
				$('#madreEmail').prop('disabled',true);
				$('#madreTelefonoRecado').prop('disabled',true);
				$("#padreNombre").prop('disabled',true);
				$("#padreOcupacion").prop('disabled',true);
				$("#padreTelefonoCelular").prop('disabled',true);
				$("#padreTelefonoOficina").prop('disabled',true);
				$("#padreEmail").prop('disabled',true);
				alert("El registro ha sido agregado. \nMatricula # " + matricula);
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

$( "#fechaNacimiento" ).datepicker({
  changeMonth: true,
  changeYear: true,
  minDate: "-100Y", maxDate: "+10D",
  currentText: 'Hoy',
  monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
  monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
  dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
  dayNamesShort: ['Dom','Lun','Mar','Mié','Juv','Vie','Sáb'],
  dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','Sá'],
  weekHeader: 'Sm',
  dateFormat: 'dd/mm/yy'
});

$("#fechaNacimiento").change(function() {
	var fecha = $("#fechaNacimiento").val();
    $("#edad").val(getAge($.datepicker.parseDate("dd/mm/yy",fecha)));
});

function getAge(birthDate) {
    var now = new Date();

    function isLeap(year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    // days since the birthdate    
    var days = Math.floor(((((now.getTime() - birthDate.getTime()) / 1000) / 60) / 60) / 24);
    var age = 0;
    // iterate the years
    for (var y = birthDate.getFullYear(); y <= now.getFullYear(); y++) {
        var daysInYear = isLeap(y) ? 366 : 365;
        if (days >= daysInYear) {
            days -= daysInYear;
            age++;
            // increment the age only if there are available enough days for the year.
        }
    }

    return age;
}

$( "input:text" ).css({
	fontSize: "22px"
});

$( "#back" ).button();
$( "#back-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});
$('#back').click(function() {
	$(location).attr("href", "/index.jsp");
});

$( "#guardar" ).button();
$( "#guardar-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});

</script>

</body>
</html>