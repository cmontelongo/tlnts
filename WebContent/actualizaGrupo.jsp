<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.claseAction" %>
	<%@ page import="com.talentos.action.grupoAction" %>
	<%@ page import="com.talentos.action.salonAction" %>
	<%@ page import="com.talentos.action.maestroAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Talentos - Actualiza Grupos</title>
	<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<script src="/js/jquery.columns.min.js"></script>
	<link rel="stylesheet" href="/css/columns/clean.css">
	<!-- link href="./css/estilos1.css" rel="stylesheet" -->
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
	<jsp:param name="titulo" value="Listado de Grupos" />
</jsp:include>
<br />
<div id="columns1"></div>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<!-- ui-dialog -->
<div id="dialog" title="Detalle">
	<p id="templates"></p>
</div>
<script>

var panelAnterior = 0;

$.ajax({
    url:'/grupoServlet?p=c&c=1',
    dataType: 'json', 
    success: function(json) {
	    $('#columns1').columns({
	        data : json,
            showRows:[5, 10, 15],
            size:15,
            searchableFields: ['nombre'],
	        schema: [
                {"header":"ID", "key":"id"},
                {"header":"Grupo", "key":"nombre"},
                {"header":"Total alumnos", "key":"total"},
                {"header":"Total clases", "key":"totalC"},
                {"header":"Accion", "key":"id","template":"<a href='javascript:void(0)' onClick='consulta({{id}})'>Detalles</a>"}

            ]
        }); 
    }
});

$( "#dialog" ).dialog({
	autoOpen: false,
	width: 1100,
	buttons: [
		{
			text: "Guardar",
			click: function() {
				var ids = $("[name='ids']");
				var clases = $("[name='claseId']");
				var maestros = $("[name='maestroId']");
				var grupos = $("[name='grupoId']");
				var salones = $("[name='salonId']");
				var dias = $("[name='diaSe']");
				var horasI = $("[name='horaI']");
				var horasF = $("[name='horaF']");
				var estatus = $("[name='estatus']");
/*				$(clases).each(function() { 
				  console.log(this);
				  console.log(this.id);
				  console.log(this.value);
				});*/
			    var jsonObj = [];
				for (var i=0; i<clases.length; i++){
			        item = {}
			        item ["id"] = ids[i].value;
			        item ["c"] = clases[i].value;
			        item ["g"] = grupos[i].value;
			        item ["m"] = maestros[i].value;
			        item ["s"] = salones[i].value;
			        item ["d"] = dias[i].value;
			        item ["i"] = horasI[i].value;
			        item ["f"] = horasF[i].value;
			        item ["e"] = estatus[i].value;
			        jsonObj.push(item);
				}
			    console.log(jsonObj);
				$.post('/grupoServlet', {
					proceso : "AC4",
					info	: "{info:"+JSON.stringify(jsonObj)+"}"
				})
				.done(function(data) {
					var response = JSON.parse(data);
					var success = response.success;
					if (success) {
						alert("Grupo actualizado");
					} else {
						alert("Ha ocurrido un error. \nIntente mas tarde. \nCodigo: "+response.error);
					}
				})
				.fail(function(res) {
		               console.log("Server Error: " + res.status + " " + res.statusText);
		           });
				//event.preventDefault();
				location.reload(); 
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

var claseDisponible = <%= claseAction.obtenerClaseJson() %>;
var maestroDisponible = <%= maestroAction.obtenerMaestroJson() %>;
var grupoDisponible = <%= grupoAction.obtenerGrupoJson() %>;
var salonDisponible = <%= salonAction.obtenerSalonJson() %>;
var horaDisponible = [
	{id:1, value:"08:00"},
	{id:2, value:"08:30"},
	{id:3, value:"09:00"},
	{id:4, value:"09:30"},
	{id:5, value:"10:00"},
	{id:6, value:"10:30"},
	{id:7, value:"11:00"},
	{id:8, value:"11:30"},
	{id:9, value:"12:00"},
	{id:10, value:"12:30"},
	{id:11, value:"13:00"},
	{id:12, value:"13:30"},
	{id:13, value:"14:00"},
	{id:14, value:"14:30"},
	{id:15, value:"15:00"},
	{id:16, value:"15:30"},
	{id:17, value:"16:00"},
	{id:18, value:"16:30"},
	{id:19, value:"17:00"},
	{id:20, value:"17:30"},
	{id:21, value:"18:00"},
	{id:22, value:"18:30"},
	{id:23, value:"19:00"},
	{id:24, value:"19:30"},
	{id:25, value:"20:00"}
];
var diaDisponible = [
	{id:1, value:"Domingo"},
	{id:2, value:"Lunes"},
	{id:3, value:"Martes"},
	{id:4, value:"Miércoles"},
	{id:5, value:"Jueves"},
	{id:6, value:"Viernes"},
	{id:7, value:"Sábado"}
];

var consulta = function(id){
	var contenido = $.get("/grupoServlet?m="+id+"&p=c&c=3", function(data, status){
		$("#templates").html(data);
		$( "#dialog" ).dialog( "open" );
		$( "#dialog" ).dialog('option', 'title', 'Detalle Grupo');
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

var baja = function(grupoNombre, claseNombre, id, idR){
	if (confirm("Dar de baja la clase "+claseNombre+" de "+grupoNombre+" ?")){
		$("#tr_"+id).hide();
		console.log($("#es_"+id+"id"));
		$("#es_"+id+"id").val("0");
	}
}

</script>
</body>
</html>