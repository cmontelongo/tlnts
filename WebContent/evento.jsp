<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Talentos - Eventos</title>
	<link href="css/estilos1.css" rel="stylesheet" type="text/css"/>
</head>
<body>


<font size="+2" face="helvetica">
<br />&nbsp;

<table style="85%" align="center">
	<tr>
		<td>
			Evento:
		</td>
		<td colspan="2">
			<select id="soflow" style="font-size:25px; font-family: Times new Roman">
			  <option value="volvo">Evento Guadalajara</option>
			  <option value="volvo">Evento Ciudad de Mexico</option>
			</select>
		</td>
		<td>
			Alumno:
		</td>
		<td colspan="2">
			<select id="soflow" style="font-size:25px; font-family: Times new Roman">
			  <option value="volvo">Alumno 7</option>
			  <option value="volvo">Alumno 6</option>
			  <option value="volvo">Alumno 5</option>
			  <option value="volvo">Alumno 4</option>
			  <option value="volvo">Alumno 3</option>
			  <option value="volvo">Alumno 2</option>
			  <option value="volvo">Alumno 1</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			Monto:
		</td>
		<td>
			<input type="text" value="$10,000.00" style="font-size:25px; font-family: Times new Roman" />
		</td>
		<td>
			&Uacute;ltimo pago:
		</td>
		<td>
			<input type="text" value="$1,500.00" style="font-size:25px; font-family: Times new Roman" />
		</td>
		<td>
			Comentarios:
		</td>
		<td>
			<input type="text" value="." style="font-size:25px; font-family: Times new Roman" />
		</td>
	</tr>
	<tr>
		<td colspan="6" align="center">
			<button>Guardar</button>
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
			Evento
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
		<td style="width:10%">
			Fecha pago
		</td>
	</tr>
	<tr style="font-family: Times new roman">
		<td>
			Evento Ciudad de Mexico
		</td>
		<td>
			Alumno 2
		</td>
		<td>
			$15,000.00
		</td>
		<td>
		</td>
		<td>
			03/Enero/2017
		</td>
	</tr>
	<tr style="font-family: Times new roman">
		<td>
			Evento Ciudad de Mexico
		</td>
		<td>
			Alumno 10
		</td>
		<td>
			$12,000.00
		</td>
		<td>
		</td>
		<td>
			03/Enero/2017
		</td>
	</tr>
	<tr style="font-family: Times new roman">
		<td>
			Evento Guadalajara
		</td>
		<td>
			Alumno 20
		</td>
		<td>
			$10,000.00
		</td>
		<td>
		</td>
		<td>
			02/Enero/2017
		</td>
	</tr>
</table>

</font>

</body>
</html>