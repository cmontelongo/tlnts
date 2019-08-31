<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Talentos - Actualiza Alumnos</title>
	<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<script src="/js/jquery.columns.min.js"></script>
	<link rel="stylesheet" href="/css/columns/clean.css">
	<style>
	#dialog-link #dialog-add {
		padding: .4em 1em .4em 20px;
		text-decoration: none;
		position: relative;
	}
	#dialog-link #dialog-add span.ui-icon {
		margin: 0 5px 0 0;
		position: absolute;
		left: .2em;
		top: 50%;
		margin-top: -8px;
	}
	</style>
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Actualizar Alumnos" />
</jsp:include>
<div id="columns1"></div>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<!-- ui-dialog -->
<div id="dialog" title="Detalle">
	<p id="templates"></p>
</div>
<script>

var panelAnterior = 0;

$.ajax({
    url:'/clienteServlet?p=c&c=2',
    dataType: 'json', 
    success: function(json) {
	    $('#columns1').columns({
	        data : json,
            showRows:[5, 10, 25, 50, 100, 200],
            size:10,
            searchableFields: ['nombre'],
	        schema: [
                {"header":"ID", "key":"mat"},
                {"header":"Alumno", "key":"nombre"},
                {"header":"Madre", "key":"madre_nombre"},
                {"header":"Celular", "key":"madre_celular"},
                {"header":"Email", "key":"madre_email"},
                {"header":"Padre", "key":"padre_nombre"},
                {"header":"Celular", "key":"padre_celular"},
                {"header":"Email", "key":"padre_email"},
                {"header":"Accion", "key":"matricula","template":"<a href='javascript:void(0)' onClick='consulta({{matricula}})'>Detalles</a>"}

            ]
        }); 
    }
});

$( "#dialog" ).dialog({
	autoOpen: false,
	width: 1000,
	buttons: [
		{
			text: "Ok",
			click: function() {
				var id = $("#id").val();
				var matricula = $("#matricula").val();
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
				var estatus = 0;
				if($("#estatus").is(':checked'))
					estatus = 1;
				console.log(estatus);
				$.post('/clienteServlet', {
					proceso 				: "ACT",
					id						: id,
					matricula				: matricula,
					nombre 					: nombre,
					fechaNacimiento 		: fechaNacimiento,
					calle 					: calle,
					numero 					: numero,
					colonia 				: colonia,
					municipio 				: municipio,
					codigoPostal 			: codigoPostal,
					madreNombre 			: madreNombre,
					madreOcupacion 			: madreOcupacion,
					madreTelefonoCasa 		: madreTelefonoCasa,
					madreTelefonoCelular 	: madreTelefonoCelular,
					madreTelefonoOficina 	: madreTelefonoOficina,
					madreEmail 				: madreEmail,
					madreTelefonoRecado 	: madreTelefonoRecado,
					padreNombre 			: padreNombre,
					padreOcupacion 			: padreOcupacion,
					padreTelefonoCelular 	: padreTelefonoCelular,
					padreTelefonoOficina 	: padreTelefonoOficina,
					padreEmail 				: padreEmail,
					estatus					: estatus
				})
				.done(function(data) {
					var response = JSON.parse(data);
					var success = response.success;
					if (success) {
						alert("El registro ha sido actualizado. \nMatricula # " + matricula);
						location.reload();
					} else {
						alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
					}
				})
				.fail(function(res) {
		               console.log("Server Error: " + res.status + " " + res.statusText);
		           });
				//event.preventDefault();
				$( this ).dialog( "close" );
			}
		},
		{
			text: "Cancelar",
			click: function() {
				$( this ).dialog( "close" );
			}
		}
	]
});

var consulta = function(id){
	console.log(id);
	var contenido = $.get("/clienteServlet?m="+id+"&p=d", function(data, status){
		$("#templates").html(data);
		$( "#dialog" ).dialog( "open" );
		$( "#dialog" ).dialog('option', 'title', 'Detalle Alumno');
	});
};

$('.date-picker').datepicker( {
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


</script>
</body>
</html>