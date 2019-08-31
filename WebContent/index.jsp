<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Inicio</title>
		<link href="/js/jquery-ui-1.12.1.custom/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
		<link href="/css/sDashboard.css" rel="stylesheet" type="text/css"/>
		<!-- link href="/css/datatables.css" rel="stylesheet" type="text/css"/ -->
		<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.12.1.custom/jquery-ui.min.js"></script>
		<!-- script type="text/javascript" src="/js/datatables.js"></script -->
		<script type="text/javascript" src="/js/flotr2.min.js"></script>
		<script type="text/javascript" src="/js/jquery-sDashboard.js"></script>
		<script type="text/javascript" src="/js/barChart.js"></script>
		<!--script type="text/javascript" src="/js/tableChart.js"></script -->
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Principal" />
</jsp:include>
<br />&nbsp;

<script type="text/javascript">
	$(function() {

		var dashboardJSON = [
		{
			widgetTitle : "Resumen por semana",
			widgetId : "id002",
			widgetType : "chart",
			enableRefresh: true,
			getDataBySelection : true,
			refreshCallBack : function(widgetId){
				var refreshedData = {
					data : barChart.constructBarChartData(),
					options : barChart.barChartOptions
				};
				return refreshedData;
			},
			widgetContent : {
				data : barChart.barChartData,
				options : barChart.barChartOptions
			}
		} /*, {
			widgetTitle : "Resumen de Pagos recibidos en el dia",
			widgetId : "id3",
			widgetType : "table",
			enableRefresh : true,
			refreshCallBack : function(widgetData){
				return {
				"aaData" : [
					tableChart.constructTableWidgetData(),
					tableChart.constructTableWidgetData(), 
					tableChart.constructTableWidgetData()
				],
				"aoColumns" : [{
					"sTitle" : "Tipo pago"
					}, {
					"sTitle" : "# pagos"
					}]
				};
			},
			widgetContent : tableChart.tableWidgetData
		}*/];

		//basic initialization example
		$("#myDashboard").sDashboard({
			dashboardData : dashboardJSON
		});

	});

</script>
<table style="width:100%"><tr><td>&nbsp;</td><td align="center" style="width:90%;">
	<div align="center" style="padding: 20px">
		<button id="mensualidad" type="submit">Pago Mensualidad</button><span style="padding-left:75px"></span>
		<button id="evento" type="submit">Pago Eventos</button><span style="padding-left:75px"></span>
		<button id="venta" type="submit">Venta Producto</button>
	</div>
	<div id="myDashboard"></div>
</td><td>&nbsp;</td></tr></table>

<script>
	$( "#mensualidad" ).button();
	$( "#mensualidad-icon" ).button({
		icon: "ui-icon-gear",
		showLabel: false
	});
	$( "#mensualidad" ).click(function(){
		window.location = "/pago.jsp"
	})

	$( "#evento" ).button();
	$( "#evento-icon" ).button({
		icon: "ui-icon-gear",
		showLabel: false
	});
	$( "#evento" ).click(function(){
		window.location = "/pago_evento.jsp"
	})

	$( "#venta" ).button();
	$( "#venta-icon" ).button({
		icon: "ui-icon-gear",
		showLabel: false
	});
	$( "#venta" ).click(function(){
		window.location = "/paquete_venta.jsp"
	})
</script>
</body>
</html>