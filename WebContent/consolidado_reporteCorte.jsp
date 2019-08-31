<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ page import="com.talentos.action.clientePagoClaseAction" %>
	<%@ page import="com.talentos.action.pagoEventoAction" %>
	<%@ page import="com.talentos.action.productoAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.util.Date" %>
	<%@ page import="java.text.DateFormat" %>
	<%@ page import="java.text.ParseException" %>
	<%@ page import="java.text.SimpleDateFormat" %>
	<%@ page import="org.json.JSONObject" %>
	<%@ page import="java.text.NumberFormat" %>
	<%@ page import="java.util.Locale" %>
	<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Corte de Pagos</title>
	<style>
	body{
		font-family: "Trebuchet MS", sans-serif;
		margin: 0px;
	}
	</style>
</head>
<body>
<%
int opcion = 1;
int sel = 0;
int evento = 0;
int concepto = 0;
String strDia = "";
String strMes = "01";
String strAnio = "2017";
Date fechaInicial = new Date();
String alumnoId="";
int historico = 0;
int duplicado = 0;
try{
	opcion = Integer.parseInt(request.getParameter("opc"));
	if (request.getParameter("sel") != null)
		sel = Integer.parseInt(request.getParameter("sel"));
	if (request.getParameter("e") != null)
		evento = Integer.parseInt(request.getParameter("e"));
	if (request.getParameter("c") != null)
		concepto = Integer.parseInt(request.getParameter("c"));
	if (request.getParameter("a") != null)
		alumnoId = request.getParameter("a");
	if (request.getParameter("h") != null)
		historico = Integer.parseInt(request.getParameter("h"));
	if (request.getParameter("d") != null)
		duplicado = Integer.parseInt(request.getParameter("d"));
	strDia = request.getParameter("dia");
	if (strDia==null && opcion==1) {
		strDia="01";
	}
	strMes = String.format("%02d", Integer.parseInt(request.getParameter("mes")));
	strAnio = request.getParameter("anio");
} catch(Exception e){
	System.out.println(e.getLocalizedMessage());
}
System.out.println(strDia+"/"+strMes+"/"+strAnio);
System.out.println("alumno="+alumnoId);

Float total = 0f;
Float totalE = 0f;
HashMap<String,String> params = new HashMap<String,String>();
params.put("o",""+opcion);
params.put("f",strDia.concat(",").concat(strMes).concat(",").concat(strAnio));
params.put("s",""+sel);
params.put("e",""+evento);
params.put("c",""+concepto);
params.put("a",""+alumnoId);
params.put("h",""+historico);
params.put("d",""+duplicado);
JSONObject tablaClientePagos = clientePagoClaseAction.obtenerTablaPagosCorte3(params);
totalE += tablaClientePagos.getFloat("totalE");
total += tablaClientePagos.getFloat("total");
JSONObject tablaEventoPagos = pagoEventoAction.obtenerTablaPagosCorte3(params);
//JSONObject tablaEventoPagos = pagoEventoAction.obtenerTablaPagosCorte3(opcion, fecha, sel, evento, concepto);
totalE += tablaEventoPagos.getFloat("totalE");
total += tablaEventoPagos.getFloat("total");
JSONObject tablaProductoVentas = productoAction.obtenerTablaPagosCorte3(params);
//JSONObject tablaProductoVentas = productoAction.obtenerTablaPagosCorte3(opcion, fecha, sel, evento, concepto);
totalE += tablaProductoVentas.getFloat("totalE");
total += tablaProductoVentas.getFloat("total");
%>
<center>
<table style='width:100%;height:60px'>
	<tr>
		<td style='text-align:right'>
			<h2>Fecha: <%= strDia+"/"+strMes+"/"+strAnio %></h2>
		</td>
	</tr>
	<tr style='height:60px'>
		<td style='text-align:center'>
			<h1>CONSOLIDADO DE PAGOS RECIBIDOS</h1>
		</td>
	</tr>
	<tr>
		<td>
			<center>
				<table style='width:100%;border-collapse: collapse;' border='3'>
					<thead>
						<tr>
							<th style='text-align:center'>
								<h2># Recibo</h2>
							</th>
							<th style='text-align:center'>
								<h2>Nombre del Alumno</h2>
							</th>
							<th style='text-align:center'>
								<h2>Concepto</h2>
							</th>
							<th style='text-align:center'>
								<h2>Fecha pago</h2>
							</th>
							<th style='text-align:center'>
								<h2>Pago recibido</h2>
							</th>
							<th style='text-align:center'>
								<h2>Comentario</h2>
							</th>
							<th style='text-align:center'>
								<h2>Forma de pago</h2>
							</th>
							<th style='text-align:center'>
								<h2>Recibi&oacute;</h2>
							</th>
						</tr>
					</thead>
<%= tablaClientePagos.get("msg") %>
<%= tablaEventoPagos.get("msg") %>
<%= tablaProductoVentas.get("msg") %>
					<tr>
						<td colspan="8" align="right">
							<h2>Total Efectivo: <%= NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalE) %></h2>
						</td>
					</tr>
					<tr>
						<td colspan="8" align="right">
							<h2>Gran total: <%= NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total) %></h2>
						</td>
					</tr>
				</table>
			</center>
		</td>
	</tr>
</table></center><p />
</body>
</html>
