<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Asignar Alumnos a Evento</title>
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
</head>
<body>

<br />&nbsp;

<table style="85%" align="center" cellspacing="15px" >
	<thead>
		<td>
			Clase
		</td>
		<td>
			Alumno
		</td>
		<td>
			Monto
		</td>
		<td>
			Beca
		</td>
	</thead>
	<tr>
		<td>
			<input id="clase" title="type &quot;a&quot;" style="width:100px" />
		</td>
		<td>
			<input id="alumno" title="type &quot;a&quot;" style="width:250px" />
		</td>
		<td>
			<input disabled="disabled" value="$1,500.00" style="width:150px" />
		</td>
		<td>
			<input id="descuento" title="type &quot;a&quot;" style="width:50px" />
		</td>
		<td>
			<button id="button">Guardar</button>
		</td>
	</tr>
	<tr>
		<td colspan="4">
			<label for="c2"><span>Comentarios</span></label>
			<input style="width:300px" />
		</td>
	</tr>
</table>

<br />&nbsp;
<hr />
<br />&nbsp;

<center><b><i>&Uacute;ltimos pagos recibidos</i></b></center>
<p />
<table style="width:100%" border="1" cellpadding="5px" cellspacing="5px">
	<tr style="font-style: italic; font-size: 25px">
		<td style="width:10%">
			Clase
		</td>
		<td style="width:33%">
			Nombre alumno
		</td>
		<td style="width:10%">
			Monto
		</td>
		<td style="width:33%">
			Comentario
		</td>
	</tr>
	<tr style="font-family: Times new roman">
		<td>
			Clase 1
		</td>
		<td>
			Alumno 2
		</td>
		<td>
			$15,000.00
		</td>
		<td>
		</td>
	</tr>
	<tr style="font-family: Times new roman">
		<td>
			Clase 1
		</td>
		<td>
			Alumno 101
		</td>
		<td>
			$15,000.00
		</td>
		<td>
		</td>
	</tr>
	<tr style="font-family: Times new roman">
		<td>
			Clase 5
		</td>
		<td>
			Alumno 102
		</td>
		<td>
			$15,000.00
		</td>
		<td>
		</td>
	</tr>
</table>

<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
<script src="./js/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script>

$( "#accordion" ).accordion();

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

var descuentoDisponible = [
	"25%",
	"20%",
	"5%"
];
$( "#descuento" ).autocomplete({
	source: descuentoDisponible
});

var claseDisponible = [
	"C1",
	"C2"
];
$( "#clase" ).autocomplete({
	source: claseDisponible
});

$( "input:text" ).css({
	fontSize: "22px"
});

$( "#button" ).button();
$( "#button-icon" ).button({
	icon: "ui-icon-gear",
	showLabel: false
});



$( "#radioset" ).buttonset();



$( "#controlgroup" ).controlgroup();



$( "#tabs" ).tabs();



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



$( "#datepicker" ).datepicker({
	inline: true
});



$( "#slider" ).slider({
	range: true,
	values: [ 17, 67 ]
});



$( "#progressbar" ).progressbar({
	value: 20
});



$( "#spinner" ).spinner();



$( "#menu" ).menu();



$( "#tooltip" ).tooltip();



$( "#selectmenu" ).selectmenu();


// Hover states on the static widgets
$( "#dialog-link, #icons li" ).hover(
	function() {
		$( this ).addClass( "ui-state-hover" );
	},
	function() {
		$( this ).removeClass( "ui-state-hover" );
	}
);
</script>

</body>
</html>