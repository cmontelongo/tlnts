var barChart = {};

barChart.constructBarChartData = function() {
    var teamList1 = [];
    var teamList2 = [];
    var teamList3 = [];
    $.ajax({
            url: '/pagoServlet',
            type: "GET",
            async: false,
            data: { p: 4, c: 1}
     }).done(function(teams){
    	 var lista = teams.split(",");
		 var list1 = lista[0].replace("{","").replace("}","").split("@");
		 for (j=0; j < list1.length; j++){
			 point = [j, parseInt(list1[j])];
			 teamList1.push( point );
		 }
		 list1 = lista[1].replace("{","").replace("}","").split("@");
		 for (j=0; j < list1.length; j++){
			 point = [j, parseInt(list1[j])];
			 teamList2.push( point );
		 }
		 list1 = lista[2].replace("{","").replace("}","").split("@");
		 for (j=0; j < list1.length; j++){
			 point = [j, parseInt(list1[j])];
			 teamList3.push( point );
		 }
     });
    console.log([teamList1,teamList2,teamList3]);
    return [{label:'Mensualidades',data:teamList1},{label:'Eventos',data:teamList2},{label:'Inventario',data:teamList3}];
};
barChart.barChartData = barChart.constructBarChartData();

barChart.barChartOptions = {
	colors : ['#0B0B61', '#FF8000', '#04B404', '#FF8000', '#04B404'],
	shadowSize : 0,
	parseFloat : false,
	legend : {
		backgroundColor : '#fff'
	},
	grid : {
		horizontalLines : true
	},
	markers : {
		show : false
	},
	bars : {
		show : true,
		horizontal : false,
		stacked : true,
		barWidth : 0.6,
		lineWidth : 2,
		fillOpacity : 0.8
	},
	xaxis : {
		min : 0,
		showLabels : false,
		margins : false
	},
	yaxis : {
		min : 0,
		showLabels : false,
		autoscaleMargin : 1,
		margins : false
	}
};
