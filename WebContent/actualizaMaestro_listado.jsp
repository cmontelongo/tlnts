<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.maestroAction" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Talentos - Actualiza Maestros</title>
	<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		margin: 0px;
	}
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
<!-- ui-dialog -->
<div id="dialog" title="Detalle">
	<p id="templates"></p>
</div>
<%
int[] params = {0, -1, 0, 0};

if ( (request.getParameter("c") != null) && (!request.getParameter("c").equals("")))
	params[0] = Integer.parseInt(request.getParameter("c"));

if ( (request.getParameter("g") != null) && (!request.getParameter("g").equals("")))
	params[1] = Integer.parseInt(request.getParameter("g"));
if ( (request.getParameter("m") != null) && (!request.getParameter("m").equals("")))
	params[2] = Integer.parseInt(request.getParameter("m"))-1;
if ( (request.getParameter("l") != null) && (!request.getParameter("l").equals("")))
	params[3] = Integer.parseInt(request.getParameter("l"));
System.out.println(params[0]+","+params[1]+","+params[2]+","+params[3]);
%>
<%= maestroAction.obtenerTablaGrupo(params) %>
</body>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>

var editar = function(id){
	console.log(id);
	var contenido = $.get("/grupoServlet?m="+id+"&p=d", function(data, status){
		$("#templates").html(data);
		$( "#dialog" ).dialog( "open" );
		$( "#dialog" ).dialog('option', 'title', 'Detalle Alumno');
	});
};

var actualiza = function(e){
	console.log(e);
	console.log(e.id);
	console.log(e.value);
	var beca = e.value;
	var monto = $("#monto").val();
	var total = 0;
	total = monto - ((beca*monto)/100);
	$("#total").val(format(total,"$"));
}

function format(n, currency) {
    return currency + "" + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
}



$( "#dialog" ).dialog({
	autoOpen: false,
	width: 500,
	buttons: [
		{
			text: "Ok",
			click: function() {
				var id = $("#id").val();
				var estatus = 0;
				var beca = 0;
				if($("#estatus").is(':checked'))
					estatus = 1;
				console.log(estatus);
				if (estatus==1){
					$("#tr_"+id).hide();
				}
				beca = $("#beca").val();
				$.post('/grupoServlet', {
					proceso 	: "ACT",
					id			: id,
					beca		: beca,
					estatus		: estatus
				})
				.done(function(data) {
					var response = JSON.parse(data);
					var success = response.success;
					if (success) {
						alert("El registro ha sido actualizado.");
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

</script>
</html>
