<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Talentos - Inicio</title>
		<link href="/js/jquery-ui-1.12.1.custom/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
		<link href="/css/sDashboard.css" rel="stylesheet" type="text/css"/>
		<link href="/css/jquery.gritter.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
		<script type="text/javascript" src="/js/jquery-ui-1.12.1.custom/jquery-ui.min.js"></script>
		<script type="text/javascript" src="/js/jquery.ui.touch-punch.js"></script>
		<script type="text/javascript" src="/js/jquery.gritter.js"></script>
		<script type="text/javascript" src="/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="/js/flotr2.min.js"></script>
		<script type="text/javascript" src="/js/jquery-sDashboard.js"></script>
		<script type="text/javascript" src="/js/exampleData.js"></script>
</head>
<body>
<jsp:include page="menuSide2.jsp">
	<jsp:param name="titulo" value="Principal" />
</jsp:include>
<br />&nbsp;

<script type="text/javascript">
	$(function() {

		var randomString = "Lorem ipsum dolor sit amet,consectetur adipiscing elit. Aenean lacinia mollis condimentum. Proin vitae ligula quis ipsum elementum tristique. Vestibulum ut sem erat.";
		
		//**********************************************//
		//dashboard json data
		//this is the data format that the dashboard framework expects
		//**********************************************//

		var dashboardJSON = [{
			widgetTitle : "Bubble Chart Widget",
			widgetId : "id009",
			widgetType : "chart",
			enableRefresh: true,
			refreshCallBack : function(widgetId){
				var refreshedData = {
					data : myExampleData.constructBubbleChartData(),
					options : myExampleData.bubbleChartOptions
				};
				return refreshedData;
			},
			widgetContent : {
				data : myExampleData.bubbleChartData,
				options : myExampleData.bubbleChartOptions
			}

		}, {
			widgetTitle : "Table Widget",
			widgetId : "id3",
			widgetType : "table",
			enableRefresh : true,
			refreshCallBack : function(widgetData){
				return {
				"aaData" : [myExampleData.constructTableWidgetData(), 
							myExampleData.constructTableWidgetData(), 
							myExampleData.constructTableWidgetData(), 
							myExampleData.constructTableWidgetData(),
							myExampleData.constructTableWidgetData(),
							myExampleData.constructTableWidgetData(), 
							myExampleData.constructTableWidgetData()
							],

							"aoColumns" : [{
							"sTitle" : "Engine"
							}, {
							"sTitle" : "Browser"
							}, {
							"sTitle" : "Platform"
							}]
						};
			},
			widgetContent : myExampleData.tableWidgetData
		}, {
			widgetTitle : "Text Widget",
			widgetId : "id2",
			enableRefresh : true,
			refreshCallBack : function(widgetId){
				return randomString + new Date();
			},
			widgetContent : randomString
		}, {
			widgetTitle : "Pie Chart Widget",
			widgetId : "id001",
			widgetType : "chart",
			widgetContent : {
				data : myExampleData.pieChartData,
				options : myExampleData.pieChartOptions
			}

		}, {
			widgetTitle : "bar Chart Widget",
			widgetId : "id002",
			widgetType : "chart",
			enableRefresh: true,
			refreshCallBack : function(widgetId){
				var refreshedData = {
					data : myExampleData.constructBarChartData(),
					options : myExampleData.barChartOptions
				};
				return refreshedData;
			},
			widgetContent : {
				data : myExampleData.barChartData,
				options : myExampleData.barChartOptions
			}

		}, {
			widgetTitle : "line Chart Widget",
			widgetId : "id003",
			widgetType : "chart",
			getDataBySelection : true,
			widgetContent : {
				data : myExampleData.lineChartData,
				options : myExampleData.lineChartOptions
			}

		}, {
			widgetTitle : "Adding an existing dom element",
			widgetId : "tweet123",
			widgetContent : $("#myTweets")
		}];

		//basic initialization example
		$("#myDashboard").sDashboard({
			dashboardData : dashboardJSON
		});

		//table row clicked event example
		$("#myDashboard").bind("sdashboardrowclicked", function(e, data) {
			$.gritter.add({
				position: 'bottom-left',
				title : 'Table row clicked',
				time : 1000,
				text : 'A table row within a table widget has been clicked, please check the console for additional event data'
			});

			if (console) {
				console.log("table row clicked, for widget: " + data.selectedWidgetId);
			}
		});

		//plot selected event example
		$("#myDashboard").bind("sdashboardplotselected", function(e, data) {
			$.gritter.add({
				position: 'bottom-left',
				title : 'Plot selected',
				time : 1000,
				text : 'A plot has been selected within a chart widget, please check the console for additional event data'
			});
			if (console) {
				console.log("chart range selected, for widget: " + data.selectedWidgetId);
			}
		});
		//plot click event example
		$("#myDashboard").bind("sdashboardplotclicked", function(e, data) {
			$.gritter.add({
				position: 'bottom-left',
				title : 'Plot Clicked',
				time : 1000,
				text : 'A plot has been clicked within a chart widget, please check the console for additional event data'
			});
			if (console) {
				console.log("chart clicked, for widget: " + data.selectedWidgetId);
			}
		});

		//widget order changes event example
		$("#myDashboard").bind("sdashboardorderchanged", function(e, data) {
			$.gritter.add({
				position: 'bottom-left',
				title : 'Order Changed',
				time : 4000,
				text : 'The widgets order has been changed,check the console for the sorted widget definitions array'
			});
			if (console) {
				console.log("Sorted Array");
				console.log("+++++++++++++++++++++++++");
				console.log(data.sortedDefinitions);
				console.log("+++++++++++++++++++++++++");
			}
			
		});
		//example for adding a text widget
		$("#btnAddWidget").click(function() {
			$("#myDashboard").sDashboard("addWidget", {
				widgetTitle : "Widget 7",
				widgetId : "id008",
				widgetContent : "Lorem ipsum dolor sit amet," + "consectetur adipiscing elit." + "Aenean lacinia mollis condimentum." + "Proin vitae ligula quis ipsum elementum tristique." + "Vestibulum ut sem erat."
			});
		});

		//example for adding a table widget
		$("#btnAddTableWidget").click(function() {
			$("#myDashboard").sDashboard("addWidget", {
				widgetTitle : "Table Widget 2",
				widgetId : "id007",
				widgetType : "table",
				widgetContent : myExampleData.tableWidgetData
			});

		});

		//example for  deleting a widget
		$("#btnDeleteWidget").click(function() {
			$("#myDashboard").sDashboard("removeWidget", "id007");
		});

		//example for adding a pie chart widget
		$("#btnAddPieChartWidget").click(function() {

			$("#myDashboard").sDashboard("addWidget", {
				widgetTitle : "Pie Chart 2",
				widgetId : "id006",
				widgetType : "chart",
				widgetContent : {
					data : myExampleData.pieChartData,
					options : myExampleData.pieChartOptions
				}
			});

		});

		//example for adding a bar chart widget
		$("#btnAddBarChartWidget").click(function() {

			$("#myDashboard").sDashboard("addWidget", {
				widgetTitle : "Bar Chart 2",
				widgetId : "id005",
				widgetType : "chart",
				enableRefresh: true,
				refreshCallBack : function(widgetId){
					var refreshedData = {
						data : myExampleData.constructBarChartData(),
						options : myExampleData.barChartOptions
					};
					return refreshedData;
				},
				widgetContent : {
					data : myExampleData.barChartData,
					options : myExampleData.barChartOptions
				}
			});
		});

		//example for adding an line chart widget
		$("#btnAddLineChartWidget").click(function() {
			$("#myDashboard").sDashboard("addWidget", {
				widgetTitle : "Line Chart 2",
				widgetId : "id004",
				widgetType : "chart",
				getDataBySelection : true,
				widgetContent : {
					data : myExampleData.lineChartData,
					options : myExampleData.lineChartOptions
				}

			});
		});

	});

</script>

	
	<div style="padding-top: 5px;">
	<label>Features :</label>
	<button class="btn" id="btnAddWidget">
		1) Add Widget
	</button>
	<button class="btn" id="btnAddTableWidget">
		2) Add Table widget
	</button>
	<button class="btn" id="btnDeleteWidget">
		3) Delete Table Widget
	</button>
	<button class="btn" id="btnAddPieChartWidget">
		4) Add Pie Chart widget
	</button>
	<button class="btn" id="btnAddBarChartWidget">
		5) Add Bar Chart widget
	</button>
	<button class="btn" id="btnAddLineChartWidget">
		6) Add Line Chart widget
	</button>
	
	</div>
	<hr/>
	
	<ul id="myDashboard">
	
	</ul>
	
	<div id="myTweets"> <h1>I was already on this page, but re-rendered as a widget</h1></div>
</body>
</html>