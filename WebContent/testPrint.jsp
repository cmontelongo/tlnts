<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
		<title>jQuery Print Plugin</title>
<script src="./js/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
    <script src="./js/printPage/jquery.printPage.js" type="text/javascript"></script>

  <script>  
  $(document).ready(function() {
	    $(".btnPrint").printPage({
		    url : "/pagoServlet?id="+$("#id").val(),
	        message:"Generando Recibo..."
	      });
	  });
  </script>
	</head>
	<body>
		<h3>This is a print button example</h3>
		<p>When you hit the button it will load your print template in an iframe and print it!</p>
		<p><input type="text" id="id" value="" /></p>
		<p><a class="btnPrint" href="#">Imprimir Recibo</a></p>
    <p><a class="btnPrint" href='/pagoServlet?id=1'>Print second page!</a></p>
	</body>
</html>