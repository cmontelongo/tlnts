<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Captura pagos</title>
	<link href="./js/jquery-ui-1.12.1.custom/jquery-ui.css" rel="stylesheet">
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		font-size=+2;
		margin: 50px;
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
	iframe:focus { 
    outline: none;
	}
	
	iframe[seamless] { 
	    display: block;
	}
	</style>
</head>
<body>
<table style="85%" align="center" cellspacing="15px" border=1>
	<thead>
		<td>
			Alumno
		</td>
		<td>
			Monto
		</td>
		<td>
			Comentario
		</td>
	</thead>
	<tr>
		<td>
			<input id="alumno" title="type &quot;a&quot;" style="width:250px" />
		</td>
		<td>
			<input disabled="disabled" value="$1,500.00" style="width:150px" />
		</td>
		<td>
			<input style="width:300px" />
		</td>
		<td>
			<button id="button">Guardar</button>
		</td>
	</tr>
</table>

<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>

var alumnoDisponible = [
	"001001 - Ana",
	"001002 - Carmen",
	"001003 - Perla",
	"001004 - Rocio",
	"001005 - Margarita",
	"001006 - Carlos",
	"001007 - Adan",
	"001008 - Martha",
	"001009 - Jose",
	"001010 - Juan",
	"001011 - Jorge",
	"001012 - Maria",
	"000001 - Raul"
];
$( "#alumno" ).autocomplete({
	source: alumnoDisponible
});


$( "input:text" ).css({
	fontSize: "22px"
});

$( "#button" ).button();
$( "#button-icon" ).button({
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
</script>

</body>
</html>
