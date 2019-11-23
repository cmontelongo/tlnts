<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date"%>
	<%@ page import="com.talentos.action.maestroAction" %>
	<%@ page import="com.talentos.util.Constantes" %>
	<%@ page import="java.io.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Talentos - Catalogo Alumnos</title>
	<script type="text/javascript" src="/js/jquery-3.1.1.min.js"></script>
	<script src="/js/jquery.columns.min.js"></script>
	<link rel="stylesheet" href="/css/columns/clean.css">
</head>
<body>
<center><h2>Alumnos</h2>
</center>

<div id="columns1"></div>
<div id="columns2"></div>
<div id="columns3"></div>
<script>

var panelAnterior = 0;

$.ajax({
    url:'/clienteServlet?p=c&c='+2,
    dataType: 'json', 
    success: function(json) {
	    $('#columns1').columns({
	        data : json,
            showRows:[5, 10, 25, 50, 100, 200],
            size:10,
            searchableFields: ['nombre'],
	        schema: [
                {"header":"ID", "key":"matricula"},
                {"header":"Alumno", "key":"nombre"},
                {"header":"Madre", "key":"madre_nombre"},
                {"header":"Celular", "key":"madre_celular"},
                {"header":"Email", "key":"madre_email", "template":'<a href="mailto:{{madre_email}}">{{madre_email}}</a>'},
                {"header":"Padre", "key":"padre_nombre"},
                {"header":"Celular", "key":"padre_celular"},
                {"header":"Email", "key":"padre_email", "template":'<a href="mailto:{{padre_email}}">{{padre_email}}</a>'},
                {"header":"Accion", "template":'<a href="consulta()">Detalle</a>'}
            ]
        }); 
    }
});


var mostrar = function(busca){
	if (panelAnterior>0){
//		$('#columns'+panelAnterior).columns("destroy");
		$('#columns1').columns("destroy");
//		$('#columns2').columns("destroy");
//		$('#columns3').columns("destroy");
	}
	panelAnterior = busca;

	if (busca == 1){
		$.ajax({
		    url:'/clienteServlet?p=c&c='+busca,
		    dataType: 'json', 
		    success: function(json) {
		        $('#columns1').columns({
		            data:json,
		            showRows:[5, 10, 25, 50, 100, 200],
		            size:5,
		            searchableFields: ['matricula', 'nombre', 'madre_nombre', 'padre_nombre'],
		            schema: [
		                {"header":"ID", "key":"matricula"},
		                {"header":"Alumno", "key":"nombre"},
		                {"header":"Madre", "key":"madre_nombre"},
		                {"header":"Celular", "key":"madre_celular"},
		                {"header":"Email", "key":"madre_email", "template":'<a href="mailto:{{madre_email}}">{{madre_email}}</a>'},
		                {"header":"Padre", "key":"padre_nombre"},
		                {"header":"Celular", "key":"padre_celular"},
		                {"header":"Email", "key":"padre_email", "template":'<a href="mailto:{{padre_email}}">{{padre_email}}</a>'},
		                {"header":"Accion", "template":'<a href="consulta()">Detalle</a>'}
		            ]

		        }); 
		    }
		});
	} else {
		if (busca == 2){
			$.ajax({
			    url:'/clienteServlet?p=c&c='+busca,
			    dataType: 'json', 
			    success: function(json) {
				    $('#columns1').columns({
				        data : json,
			            showRows:[5, 10, 25, 50, 100, 200],
			            size:5,
			            searchableFields: ['nombre'],
				        schema: [
				            {"header":"ID", "key":"matricula", "template":"{{matricula}}"},
				            {"header":"Alumno", "key":"nombre"},
				            {"header":"Accion", "template":'<a href="consulta()">Detalle</a>'}
			            ]
			        }); 
			    }
			});
		} else {
			$.ajax({
			    url:'/clienteServlet?p=c&c='+busca,
			    dataType: 'json', 
			    success: function(json) {
				    $('#columns1').columns({
				        data : json,
			            showRows:[5, 10, 25, 50, 100, 200],
			            size:5,
			            searchableFields: ['nombre'],
				        schema: [
				            {"header":"ID", "key":"matricula", "template":"{{matricula}}"},
				            {"header":"Alumno", "key":"nombre"},
			                {"header":"Madre", "key":"madre_nombre"},
			                {"header":"Celular", "key":"madre_celular"},
			                {"header":"Email", "key":"madre_email", "template":'<a href="mailto:{{madre_email}}">{{madre_email}}</a>'},
				            {"header":"Accion", "template":'<a href="consulta()">Detalle</a>'}
			            ]
			        }); 
			    }
			});
		}
	}
}


</script>
</body>
</html>