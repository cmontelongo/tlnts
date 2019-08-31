var tableChart = {};

tableChart.constructTableWidgetData = function(){
	//return ["Pago tipo "+Math.ceil(Math.random() * 10), ""+Math.ceil(Math.random() * 10)]

	var teamList1 = [];
    var teamList2 = [];
    var teamList3 = [];
    $.ajax({
            url: '/pagoServlet',
            type: "GET",
            async: false,
            data: { p: 4, c: 2}
     }).done(function(teams){
    	 var lista = teams.split(",");
    	 for (i=0; i < lista.length; i++){
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
    	 }
     });
    console.log([teamList1,teamList2,teamList3]);
    return [teamList1,teamList2,teamList3];
};

tableChart.tableWidgetData = {
	"aaData" : [
		tableChart.constructTableWidgetData()
	],

	"aoColumns" : [{
		"sTitle" : "Tipo pago"
	}, {
		"sTitle" : "# pagos"
	}]
};
