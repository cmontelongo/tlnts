<!DOCTYPE html>
<style>
	body{
		font-family: "Trebuchet MS", "Roboto", helvetica, arial, sans-serif;
		font-size: 15px;
		margin: 10px;
	}
	.sidenav {
	    height: 100%;
	    width: 0;
	    position: fixed;
	    z-index: 1;
	    top: 0;
	    left: 0;
	    background-color: #BDBDBD;
	    overflow-x: hidden;
	    transition: 0.5s;
	    padding-top: 60px;
	}
	
	.sidenav a {
	    padding: 8px 8px 8px 32px;
	    text-decoration: none;
	    font-size: 15px;
	    color: black;
	    display: block;
	    transition: 0.3s;
	}
	
	.sidenav a:hover, .offcanvas a:focus{
	    color: #f1f1f1;
	}
	
	.sidenav .closebtn {
	    position: absolute;
	    top: 0;
	    right: 25px;
	    font-size: 36px;
	    margin-left: 50px;
	}
	
	@media screen and (max-height: 800px) {
	  .sidenav {padding-top: 15px;}
	  .sidenav a {font-size: 18px;}
	}

	ul {
		list-style-type: none;
		padding: 0 0 0 20px;
		margin: 0px;
	}

</style>

<div id="mySidenav" class="sidenav">
  <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
  <a href="index.jsp">Inicio</a>
  <hr width="75%" />
  <a href="javascript:void(0)" onClick="mostrar('alumno')">Alumno</a>
	  <div id="alumno" style="display:none" name="subMenu">
		  <ul>
		  	<li><a href="/asignar_alumno_grupo.jsp">Asignar a Grupo</a></li>
		  	<li><a href="/asignar_grupo_clase_horario.jsp">Grupo horario</a></li>
			<hr width="75%" />
		  	<li><a href="/registro.jsp" target="_blank">Alta</a></li>
		  	<li><a href="/grupo.jsp">Grupo</a></li>
		  	<li><a href="/salon.jsp">Salon</a></li>
		  	<li><a href="/maestro.jsp">Maestro</a></li>
		  	<li><a href="/clase.jsp">Clase</a></li>
		  </ul>
	  </div>
  <hr width="75%" />
  <a href="javascript:void(0)" onClick="mostrar('listado1')">Listados</a>
	  <div id="listado1" style="display:none" name="subMenu">
		  <ul>
		  	<li><a href="/consulta_grupoAlumno.jsp">Listado Grupo</a></li>
		  	<li><a href="/consulta_grupoMaestro.jsp">Asistencia Maestro</a></li>
		  </ul>
	  </div>
  <hr width="75%" />
  <a href="javascript:void(0)" onClick="mostrar('actualiza1')">Actualizar</a>
	  <div id="actualiza1" style="display:none" name="subMenu">
		  <ul>
		  	<li><a href="/actualizaCliente.jsp">Alumnos</a></li>
		  	<li><a href="/actualizaAlumnoGrupo.jsp">Alumnos en Grupo</a></li>
		  	<li><a href="/actualizaGrupo.jsp">Grupos</a></li>
		  	<li><a href="/actualizaMaestro.jsp">Maestros</a></li>
		  </ul>
	  </div>
  <hr width="75%" />
  <a href="javascript:void(0)" onClick="mostrar('pagos1')">Pagos</a>
	  <div id="pagos1" style="display:none" name="subMenu">
	  	<ul>
	  		<li><a href="/pago.jsp">Pago mensualidad</a></li>
		  	<li><a href="/pago_evento.jsp">Pago eventos</a></li>
		  	<li><a href="/pago_concilia.jsp">Conciliacion de Pagos</a></li>
		  	<li><a href="/pago_reporte.jsp">Reporte de pagos</a></li>
		</ul>
	  </div>
  <hr width="75%" />
  <a href="javascript:void(0)" onClick="mostrar('inventario')">Inventario</a>
		<div id="inventario" style="display:none" name="subMenu">
			<ul>
				<li><a href="/producto_alta.jsp">Producto</a></li>
				<li><a href="/almacen_alta.jsp">Almacen</a></li>
				<li><a href="/producto_almacen.jsp">Producto / Almacen</a></li>
				<li><a href="/paquete.jsp">Paquete</a></li>
				<li><a href="/producto_almacen_inventario.jsp">Compra</a></li>
			</ul>
		</div>
	<a href="/paquete_venta.jsp">Venta Producto</a></li>
</div>

<span style="font-size:30px;cursor:pointer; position:absolute; top:10px; left:25px;" onclick="openNav()">&#9776;</span>
<div style="text-align:center; position:absolute; right:50%; margin-right:-20px; top:0px">
  <h2>${param.titulo}</h2>
</div>
<script>

var mostrar = function(id){
	var subMenus = $("[name='subMenu']");
	console.log(subMenus);
	subMenus.each(function (index, value){
		console.log(index);
		console.log(value);
		subMenus[index].style="display:none";
	});
	if (document.getElementById(id).style.display==""){
		document.getElementById(id).style.display="none";
	} else {
		document.getElementById(id).style.display="";
	}
}

var openNav = function(){
	document.getElementById("mySidenav").style.width = "300px";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}
</script>