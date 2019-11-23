package com.talentos.action;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Cliente_Pago_Clase;
import com.talentos.entidad.Grupo_Cliente_Recibo;
import com.talentos.entidad.Grupo_Cliente_Recibo_Pago;
import com.talentos.util.Constantes;

public class clientePagoClaseAction {

	private static Logger log = Logger.getLogger(clientePagoClaseAction.class);

	public static String obtenerListado(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t1.id,t1.nombre, t3.id, t3.nombre, t3.monto, t4.fecha_pago from cliente t1, cliente_clase t2, clase t3, cliente_pago_clase t4 where t1.estatus = 1 and t1.id=t2.id_cliente and t2.fecha_baja is null and t2.id_clase=t3.id and t4.id_cliente_clase=t2.id";
		query += " ORDER BY t1.ID DESC";
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query);
		    rs = pstmt.executeQuery();
			result = "[";
			while(rs.next()){
				String id = rs.getString("id");
				result += "\""+id+" - "+rs.getString("nombre")+"\",";
			}
			con.commit();
			result = result.substring(0, result.length()-1);
			result += "]";
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return result;
	}
	

	public static String obtenerListadoCliente(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		//String query = "select t1.id,t1.nombre, t3.id, t3.nombre, t3.monto from cliente t1, cliente_clase t2, clase t3 where t1.estatus = 1 and t1.id=t2.id_cliente and t2.id_clase=t3.id and t2.id not in (select t4.id_cliente_clase from cliente_clase_recibo t4)";
		StringBuilder query = new StringBuilder()
				.append("select distinct t1.id, t1.matricula, t1.nombre cliente_nombre ")
				.append("from cliente t1 ")
				.append("where t1.estatus = 1 ")
				.append("order by t1.id DESC");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			JSONObject jsonFinal = new JSONObject();
		    JSONArray jsonArray = new JSONArray();
		    StringWriter out = new StringWriter();
			while(rs.next()){
				jsonFinal.put("id",rs.getString("id"));
				jsonFinal.put("label",rs.getString("matricula")+" - "+rs.getString("cliente_nombre"));
				jsonArray.put(jsonFinal);
			    jsonFinal = new JSONObject();
			}
			jsonArray.write(out);
			result = new StringBuilder(out.toString());

		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return result.toString();
	}

	public static String obtenerListadoClienteAdeudo(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		//String query = "select t1.id,t1.nombre, t3.id, t3.nombre, t3.monto from cliente t1, cliente_clase t2, clase t3 where t1.estatus = 1 and t1.id=t2.id_cliente and t2.id_clase=t3.id and t2.id not in (select t4.id_cliente_clase from cliente_clase_recibo t4)";
		StringBuilder query = new StringBuilder()
//				.append("select distinct t2.id, t1.matricula, t1.nombre cliente_nombre, CONCAT('Grupo ',t4.nombre) grupo_nombre, t3.dia_semana, (t5.monto*(1-t2.beca)) monto ")
				.append("select distinct t2.id, t1.matricula, t1.nombre cliente_nombre, CONCAT('Grupo ',t4.nombre) grupo_nombre, (t5.monto*(1-(t2.beca/100))) monto ")
				.append("from cliente t1 ")
				.append("inner join grupo_cliente t2 on t2.id_cliente=t1.id ")
				.append("inner join grupo t4 on t4.id=t2.id_grupo ")
				.append("inner join grupo_clase t3 on t4.id=t3.id_grupo ")
				.append("inner join grupo_costo t5 on t5.id_grupo=t2.id_grupo ")
				.append("where t1.estatus = 1 and t2.estatus in (0,1) ")
				.append("order by t2.id DESC");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			result = new StringBuilder("[");
			while(rs.next()){
				String id = rs.getString("id");
				String matricula = rs.getString("matricula");
				result.append("{id:").append(id).append(",label:\"").append(matricula).append(" - ").append(rs.getString("cliente_nombre")).append(" (").append(rs.getString("grupo_nombre")).append(")\",monto:").append(rs.getString("monto")).append("},");
			}
			con.commit();
			if (result.length()>2)
				result = new StringBuilder().append(result.substring(0, result.length()-1));
			result.append("]");
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return result.toString();
	}

	@SuppressWarnings("unused")
	public static String obtenerTablaPagos(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select distinct t1.id, t1.numero_mes, t2.monto, t5.nombre cliente_nombre,  CONCAT('Grupo ',t4.nombre) grupo_nombre, ")
				.append("DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaFormatoSQL2).append("') fecha_alta, t6.dia_semana, ")
				.append("t2.numero_mes, t2.id pago_recibo_id ")
				.append("from grupo_cliente_recibo t1 ")
				.append("inner join grupo_cliente_recibo_pago t2 on t1.id=t2.id_grupo_cliente_recibo ")
				.append("inner join grupo_cliente t3 on t3.id=t1.id_grupo_cliente ")
				.append("inner join grupo t4 on t4.id=t3.id_grupo ")
				.append("inner join cliente t5 on t5.id=t3.id_cliente ")
				.append("inner join grupo_clase t6 on t6.id_grupo=t4.id ")
				.append("order by t1.id desc, t6.dia_semana asc LIMIT 10");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			int id = 0;
			int idAnterior = -1;
			int diaActual = 0;
			String dias = "";
			String grupoNombre = "";
			String comentario = "";
			String fechaAlta = "";
			int numeroMes = 0;
			String clienteNombre = "";
			String idPagoRecibo = "";
			double pago = 0;
		    if (rs.next()){
				result.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Alumno</th><th class='text-left'>Grupo</th><th class='text-left'>Mes de Pago</th><th class='text-left'>Pago</th><th class='text-left'>Fecha pago</th><th class='text-left' style='width:120px'>Accion</th></tr></thead>");
				idAnterior = rs.getInt("id");
				pago = Double.parseDouble(rs.getString("monto"));
				grupoNombre = rs.getString("grupo_nombre");
				clienteNombre = rs.getString("cliente_nombre");
				//comentario = rs.getString("comentario");
				fechaAlta = rs.getString("fecha_alta");
				numeroMes = rs.getInt("numero_mes");
				diaActual = rs.getInt("dia_semana");
				dias = ""+Constantes.sDiaSemana[diaActual];
				idPagoRecibo = rs.getString("pago_recibo_id");
				while(rs.next()){
					id = rs.getInt("id");
					diaActual = rs.getInt("dia_semana");
					if (id==idAnterior){
						dias += ","+Constantes.sDiaSemana[diaActual];
					} else {
						if (idAnterior>0){
							result.append("<tr><td>").append(clienteNombre).append("</td><td>")
								.append(grupoNombre).append(" (").append(dias).append(")</td><td>")
								.append(Constantes.mes[numeroMes]).append("</td><td class='text-right'>")
								.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("</td><td>")
								.append(fechaAlta).append("</td>")
								.append("<td>")
								/*.append("<table style='width:80%; height:45px'><tr><td align='left'>")
								.append("<a href='javascript:void(0)' onClick='detalle(").append(idPagoRecibo).append(")'><span class='lnr lnr-book' title='Editar informacion del Alumno'></span></a>")
								.append("</td><td align='center'>")
								.append("<a href='javascript:void(0)' onClick='correo(").append(idPagoRecibo).append(")'><span class='lnr lnr-envelope' title='Dar de baja del Grupo'></span></a>")
								.append("</td><td align='right'>")
								.append("<a class='btnPrint' href='/pagoServlet?id=").append(idPagoRecibo).append("&p=3'><span class='lnr lnr-printer' title='Cambiar Alumno de Grupo'></span></a>")
								.append("</td></tr></table>")*/
								/*.append("<a href='javascript:void(0)' onClick='detalle(").append(idPagoRecibo).append(")'><img src='./img/text-documents.png' alt='Detalle' width='30px' class='dialog-link' /></a>&nbsp;&nbsp;&nbsp;")
								.append("<a href='javascript:void(0)' onClick='correo(").append(idPagoRecibo).append(")'><img src='./img/letter-with-stamp.png' alt='Enviar por correo' width='30px' class='dialog-link' /></a>&nbsp;&nbsp;&nbsp;")
								.append("<a class='btnPrint' href='/pagoServlet?id=").append(idPagoRecibo).append("&p=3'><img src='./img/print-button.png' alt='imprimir' width='30px' class='dialog-link' /></a>")*/
								.append("<a href='javascript:void(0)' onClick='detalle(").append(idPagoRecibo).append(")'><span class='lnr lnr-book' title='Detalle de pago'></span></a>&nbsp;&nbsp;&nbsp;")
								.append("<a href='javascript:void(0)' onClick='correo(").append(idPagoRecibo).append(")'><span class='lnr lnr-envelope' title='Enviar por correo'></span>&nbsp;&nbsp;&nbsp;")
								.append("<a class='btnPrint' href='/pagoServlet?id=").append(idPagoRecibo).append("&p=3'><span class='lnr lnr-printer' title='Imprimir'></span></a>")
								.append("</td></tr>");
						}
						pago = Double.parseDouble(rs.getString("monto"));
						grupoNombre = rs.getString("grupo_nombre");
						clienteNombre = rs.getString("cliente_nombre");
						//comentario = rs.getString("comentario");
						fechaAlta = rs.getString("fecha_alta");
						numeroMes = rs.getInt("numero_mes");
						dias = ""+Constantes.sDiaSemana[diaActual];
						idPagoRecibo = rs.getString("pago_recibo_id");
					}
					idAnterior = id;
				}
				if (id>0)
					result.append("<tr><td>").append(clienteNombre).append("</td><td>")
						.append(grupoNombre).append(" (").append(dias).append(")</td><td>")
						.append(Constantes.mes[numeroMes]).append("</td><td class='text-right'>")
						.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("</td><td>")
						.append(fechaAlta).append("</td>")
						.append("<td><a href='javascript:void(0)' onClick='detalle(").append(idPagoRecibo).append(")'><img src='./img/text-documents.png' alt='Detalle' width='30px' class='dialog-link' /></a>&nbsp;&nbsp;&nbsp;")
						.append("<a href='javascript:void(0)' onclick='correo(").append(idPagoRecibo).append(")'><img src='./img/letter-with-stamp.png' alt='Enviar por correo' width='30px' class='dialog-link' /></a>&nbsp;&nbsp;&nbsp;")
						.append("<a class='btnPrint' href='/pagoServlet?id=").append(idPagoRecibo).append("&p=1'><img src='./img/print-button.png' alt='imprimir' width='30px' class='dialog-link' /></a>")
						.append("</td></tr>");
		    } else {
		    	result.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
			con.commit();
			result.append("</table>");
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return result.toString();
	}

	public static int guardarGrupoClienteRecibo(Grupo_Cliente_Recibo grupoClienteRecibo) throws SQLException{
		int resultado = 0;

		String query = "insert into grupo_cliente_recibo(id_grupo_cliente, monto, estatus, numero_mes, comentario) values(";
		query += grupoClienteRecibo.getId_grupo_cliente()+","
				+grupoClienteRecibo.getMonto()+","
				+grupoClienteRecibo.getEstatus()+","
				+grupoClienteRecibo.getNumero_mes()+","
				+"'"+grupoClienteRecibo.getComentario()+"'";
		query += ")";
		System.out.println(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			ResultSet keys = pstmt.getGeneratedKeys();    
			keys.next();  
			resultado = keys.getInt(1);
			log.info(resultado);
			con.commit();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return resultado;
	}

	public static int guardarGrupoClienteReciboPago(Grupo_Cliente_Recibo_Pago grupoClienteReciboPago) throws SQLException{
		int resultado = 0;

		String query = "insert into grupo_cliente_recibo_pago(id_grupo_cliente_recibo, monto, numero_mes) values(";
		query += grupoClienteReciboPago.getId_grupo_cliente_recibo()+","
				+grupoClienteReciboPago.getMonto()+","
				+grupoClienteReciboPago.getNumero_mes();
		query += ")";
		System.out.println(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			ResultSet keys = pstmt.getGeneratedKeys();    
			keys.next();  
			resultado = keys.getInt(1);
			log.info(resultado);
			con.commit();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return resultado;
	}

	public static int guardarClientePagoClase(Cliente_Pago_Clase clientePagoClase) throws SQLException{
		int resultado = 0;

		String query = "insert into cliente_pago_clase(id_cliente_clase, fecha_pago, pago, comentario) values(";
		query += clientePagoClase.getId_cliente_clase()+","
				+"'"+clientePagoClase.getFecha_pago()+"',"
				+clientePagoClase.getPago()+","
				+"'"+clientePagoClase.getComentario()+"'";
		query += ")";
		System.out.println(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			ResultSet keys = pstmt.getGeneratedKeys();    
			keys.next();  
			resultado = keys.getInt(1);
			log.info(resultado);
			con.commit();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return resultado;
	}

	@SuppressWarnings("unused")
	public static String obtenerTablaPagosCorte(int opcion, String[] fecha) throws ParseException{
		Boolean buscaMes = true;
		String fechaInicial = "";
		if (!fecha[0].equals("")){
			buscaMes = false;
		};
		if (buscaMes) {
		    fechaInicial = fecha[1]+"/"+fecha[2];
		} else {
		    fechaInicial = String.format("%02d", Integer.parseInt(fecha[0]))+"/"+fecha[1]+"/"+fecha[2];
		}
		System.out.println("fInicial:"+fechaInicial);
		String result = "";
		String titulo = "CORTE DIARIO DE COBRANZA";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select distinct t2.id, t3.id, t4.id claseId, t1.numero_mes, t1.monto, t2.monto monto_pagado,")
				.append(" t4.id, t4.nombre cliente_nombre, t5.id, CONCAT('Grupo ',t5.nombre) grupo_nombre,")
				.append(" DATE_FORMAT(t2.fecha_alta,'%d/%m/%Y %H:%i:%S') fecha_alta,")
				.append(" DATE_FORMAT(t2.fecha_alta,'%d/%m/%Y %H:%i:%S') fecha_consulta, IFNULL(t2.id_usuario_alta,'') id_usuario_alta,")
				.append(" t1.forma_pago")
				.append(" from grupo_cliente_recibo t1")
				.append(" inner join grupo_cliente_recibo_pago t2 on t1.id=t2.id_grupo_cliente_recibo")
				.append(" inner join grupo_cliente t3 on t3.id=t1.id_grupo_cliente")
				.append(" inner join cliente t4 on t4.id=t3.id_cliente")
				.append(" inner join grupo t5 on t5.id=t3.id_grupo")
				.append(" where t5.estatus=1");
		if (opcion==2) {
			query.append(" and t1.numero_mes=").append(fecha[1]);
			titulo = "CORTE POR MENSUALIDAD COBRADA";
		} else if (opcion==3){
			query.append(" and DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
			titulo = "CORTE MENSUAL DE COBRANZA";
		} else {
			query.append(" and DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
		}
		query.append(" order by t2.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	int formaPago = rs.getInt("forma_pago");
		    	Date fechaReporte = new Date();
		    	String fechaConsulta = rs.getString("fecha_consulta");
		    	result = "<center><table style='width:100%;height:60px'><tr><td style='text-align:right'><h3>Fecha: "+fechaInicial+"</h3></td></tr>";
		    	result += "<tr style='height:60px'><td style='text-align:center'><h1>"+titulo+"</h1></td></tr>";
		    	result += "<tr><td><center>";
				result += "<table style='width:100%;border-collapse: collapse;' border='3'>";
				result += "<thead><tr><th style='text-align:center'><h3>Recibo</h3></th><th style='text-align:center'><h3>Nombre del Alumno</h3></th><th style='text-align:center'><h3>Pago</h3></th>";
				if (opcion==2 || opcion==1) {
					result +="<th style='text-align:center'><h3>Mensualidad</h3></th>";
				}
				result +="<th style='text-align:center'><h3>Fecha pago</h3></th><th style='text-align:center'><h3>Concepto</h3></th><th style='text-align:center'><h3>Metodo pago</h3></th><th style='text-align:center'><h3>Recibi&oacute;</h3></th></tr></thead>";
				double pago;
				double total = 0;
				double totalEfectivo = 0;
				pago = Double.parseDouble(rs.getString("monto_pagado"));
				total += pago;
				if (formaPago != 1 && formaPago != 4)
					totalEfectivo += pago;
				result += "<tr><td><h4>&nbsp;"+String.format("%06d", rs.getInt("id"))+"</h4></td><td><h4>&nbsp;"+
				        rs.getString("cliente_nombre")+"</h4></td><td align=right><h4>"+
				        NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)+"&nbsp;</h2></td>";
						if (opcion==2 || opcion==1) {
							result +="<td><h4>&nbsp;"+Constantes.mes[Integer.parseInt(rs.getString("numero_mes"))]+"</h4></td>";
						}
				result += "<td>&nbsp;"+rs.getString("fecha_alta")+"</td><td><h4>&nbsp;"+rs.getString("grupo_nombre")+"</h4></td>"
				        +"<td><h4>&nbsp;"+Constantes.sFormaPago[formaPago]+"</h4></td>"
				        +"<td><h4>&nbsp;"+rs.getString("id_usuario_alta")+"</h4></td>"
						+"</tr>";
				while(rs.next()){
					formaPago = rs.getInt("forma_pago");
					pago = Double.parseDouble(rs.getString("monto_pagado"));
					total += pago;
					if (formaPago != 1 && formaPago != 4)
						totalEfectivo += pago;
					result += "<tr><td><h4>&nbsp;"+String.format("%06d", rs.getInt("id"))+"</h4></td><td><h4>&nbsp;"+
					        rs.getString("cliente_nombre")+"</h4></td><td align=right><h4>"+
					        NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)+"&nbsp;</h4></td>";
							if (opcion==2 || opcion==1) {
								result +="<td><h4>&nbsp;"+Constantes.mes[Integer.parseInt(rs.getString("numero_mes"))]+"</h4></td>";
							}
					result += "<td>&nbsp;"+rs.getString("fecha_alta")+"</td><td><h4>&nbsp;"+rs.getString("grupo_nombre")+"</h4></td>";
					result += "<td><h4>&nbsp;"+Constantes.sFormaPago[formaPago]+"</h4></td>";
					result += "<td><h4>&nbsp;"+rs.getString("id_usuario_alta")+"</h4></td>"
							+"</tr>";
				}
				result += "<tr><td colspan='7' style='text-align:right'><h2>Total Efectivo "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalEfectivo)+"</h2></td></tr>";
				result += "<tr><td colspan='7' style='text-align:right'><h2>Total "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)+"</h2></td></tr>";
		    } else {
		    	result ="<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>";
		    }
			con.commit();
			result += "</center></table></td></tr></table></center>";
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return result;
	}


	public static String obtenerTablaPagosCorte2(int opcion, String[] fecha) throws ParseException{
		Boolean buscaMes = true;
		String fechaInicial = "";
		if (!fecha[0].equals("")){
			buscaMes = false;
		};
		if (buscaMes) {
		    fechaInicial = fecha[1]+"/"+fecha[2];
		} else {
		    fechaInicial = String.format("%02d", Integer.parseInt(fecha[0]))+"/"+fecha[1]+"/"+fecha[2];
		}
		System.out.println("fInicial:"+fechaInicial);
		StringBuilder result = new StringBuilder();
		String titulo = "CORTE DIARIO DE COBRANZA";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select distinct t1.id reciboId, t1.numero_mes, t1.monto, t2.monto monto_pagado,")
				.append(" t4.id, t4.nombre cliente_nombre, t5.id, CONCAT('Grupo ',t5.nombre) grupo_nombre,")
				.append(" DATE_FORMAT(t2.fecha_alta,'%d/%m/%Y %H:%i:%S') fecha_alta,")
				.append(" DATE_FORMAT(t2.fecha_alta,'%d/%m/%Y %H:%i:%S') fecha_consulta, IFNULL(t2.id_usuario_alta,'') id_usuario_alta,")
				.append(" t1.forma_pago")
				.append(" from grupo_cliente_recibo t1")
				.append(" inner join grupo_cliente_recibo_pago t2 on t1.id=t2.id_grupo_cliente_recibo")
				.append(" inner join grupo_cliente t3 on t3.id=t1.id_grupo_cliente")
				.append(" inner join cliente t4 on t4.id=t3.id_cliente")
				.append(" inner join grupo t5 on t5.id=t3.id_grupo")
				.append(" where t5.estatus=1");
		if (opcion==2) {
			query.append(" and t1.numero_mes=").append(fecha[1]);
			titulo = "CORTE POR MENSUALIDAD COBRADA";
		} else if (opcion==3){
			query.append(" and DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
			titulo = "CORTE MENSUAL DE COBRANZA";
		} else {
			query.append(" and DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
		}
		query.append(" order by t1.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result.append("<center><table style='width:100%;height:60px'>")
		    		.append("<tr style='height:60px'><td style='text-align:center'><h1>").append(titulo).append("</h1></td></tr>")
		    		.append("<tr><td><center>")
		    		.append("<table style='width:100%;border-collapse: collapse;' border='3'>")
		    		.append("<thead><tr style='background-color:lightgrey'><th style='text-align:center'><h4>Recibo</h4></th><th style='text-align:center'><h4>Nombre del Alumno</h4></th><th style='text-align:center'><h4>Pago</h4></th>");
				if (opcion==2 || opcion==1) {
					result.append("<th style='text-align:center'><h4>Mensualidad</h4></th>");
				}
				result.append("<th style='text-align:center'><h4>Fecha pago</h4></th><th style='text-align:center'><h4>Concepto</h4></th><th style='text-align:center'><h4>Forma de pago</h4></th><th style='text-align:center'><h4>Recibi&oacute;</h4></th></tr></thead>");
				double pago;
				pago = Double.parseDouble(rs.getString("monto_pagado"));
				result.append("<tr><td><h5>&nbsp;").append(String.format("%06d", rs.getInt("reciboId"))).append("</h5></td><td><h5>&nbsp;")
					.append(rs.getString("cliente_nombre")).append("</h5></td><td align=right><h5>")
					.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h5></td>");
				if (opcion==2 || opcion==1) {
					result.append("<td><h5>&nbsp;").append(Constantes.mes[Integer.parseInt(rs.getString("numero_mes"))]).append("</h5></td>");
				}
				result.append("<td><center><h5>").append(rs.getString("fecha_alta")).append("</h5></center></td><td><h5>&nbsp;").append(rs.getString("grupo_nombre")).append("</h5></td>")
					.append("<td>").append(obtenerSelectFormaPago(rs.getInt("reciboId"),rs.getInt("forma_pago")).toString()).append("</td>")
					.append("<td><h5>").append(rs.getString("id_usuario_alta")).append("</h5></td>")
					.append("</tr>");
				while(rs.next()){
					pago = Double.parseDouble(rs.getString("monto_pagado"));
					result.append("<tr><td><h5>&nbsp;").append(String.format("%06d", rs.getInt("reciboId"))).append("</h5></td><td><h5>&nbsp;")
						.append(rs.getString("cliente_nombre")).append("</h5></td><td align=right><h5>")
						.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h5></td>");
					if (opcion==2 || opcion==1) {
						result.append("<td><h5>&nbsp;").append(Constantes.mes[Integer.parseInt(rs.getString("numero_mes"))]).append("</h5></td>");
					}
					result.append("<td><center><h5>").append(rs.getString("fecha_alta")).append("</h5></center></td><td><h5>&nbsp;").append(rs.getString("grupo_nombre")).append("</h5></td>")
						.append("<td>").append(obtenerSelectFormaPago(rs.getInt("reciboId"),rs.getInt("forma_pago")).toString()).append("</td>")
						.append("<td><h5>").append(rs.getString("id_usuario_alta")).append("</h5></td>")
						.append("</tr>");
				}
		    } else {
		    	result.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
			con.commit();
			result.append("</center></table></td></tr></table></center>");
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return result.toString();
	}

	private static StringBuilder obtenerSelectFormaPago(int id, int formaPago){
		StringBuilder resultado = new StringBuilder();
		resultado.append("<center><select class='selectConcilia' onChange='cambia(").append(id).append(", this)'>")
			.append("<option value='0' ");
		if (formaPago == 0)
			resultado.append("selected");
		resultado.append(">Efectivo</option>")
			.append("<option value='1' ");
		if (formaPago == 1)
			resultado.append("selected");
		resultado.append(">Duplicado</option>")
			.append("<option value='2'  ");
		if (formaPago == 2)
			resultado.append("selected");
		resultado.append(">Deposito</option>")
		.append("<option value='3'  ");
		if (formaPago == 3)
			resultado.append("selected");
		resultado.append(">Transferencia</option>")
			.append("<option value='4'  ");
		if (formaPago == 4)
			resultado.append("selected");
		resultado.append(">Pago pendiente</option>")
			.append("</select></center>");
		return resultado;
	}

	public static int actualizaFormaPago(int formaPago, int id) throws SQLException{
		int resultado = 0;
		StringBuilder query = new StringBuilder()
				.append("update grupo_cliente_recibo set ")
				.append(" forma_pago=").append(formaPago)
				.append(" where id=").append(id);
		System.out.println("actualizaFormaPago: "+query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();
			con.commit();
			resultado = 1;
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			resultado = 0;
			throw e;
		} finally {
			try{
				if(con != null)
					con.close();
			}catch(SQLException ex){
				log.error(ex.getMessage());
			}
		}
		return resultado;
	}

	@SuppressWarnings("unused")
	public static JSONObject obtenerTablaPagosCorte3(int opcion, String[] fecha) throws ParseException{
		Boolean buscaMes = true;
		String fechaInicial = "";
		JSONObject jsonFinal = new JSONObject();
		double total = 0;
		double totalEfectivo = 0;
		if (!fecha[0].equals("")){
			buscaMes = false;
		};
		if (buscaMes) {
		    fechaInicial = fecha[1]+"/"+fecha[2];
		} else {
		    fechaInicial = String.format("%02d", Integer.parseInt(fecha[0]))+"/"+fecha[1]+"/"+fecha[2];
		}
		System.out.println("fInicial:"+fechaInicial);
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select distinct t2.id id_recibo, t3.id, t4.id claseId, t1.numero_mes, t1.monto, t2.monto monto_pagado,")
				.append(" t4.id, t4.nombre cliente_nombre, t5.id, CONCAT('Grupo ',t5.nombre) grupo_nombre,")
				.append(" DATE_FORMAT(t2.fecha_alta,'%d/%m/%Y %H:%i:%S') fecha_alta,")
				.append(" DATE_FORMAT(t2.fecha_alta,'%d/%m/%Y %H:%i:%S') fecha_consulta, IFNULL(t2.id_usuario_alta,'') id_usuario_alta, t2.fecha_alta, t1.comentario")
				.append(" ,t1.forma_pago")
				.append(" from grupo_cliente_recibo t1")
				.append(" inner join grupo_cliente_recibo_pago t2 on t1.id=t2.id_grupo_cliente_recibo")
				.append(" inner join grupo_cliente t3 on t3.id=t1.id_grupo_cliente")
				.append(" inner join cliente t4 on t4.id=t3.id_cliente")
				.append(" inner join grupo t5 on t5.id=t3.id_grupo")
				.append(" where t5.estatus=1");
		if (opcion==2) {
			query.append(" and t1.numero_mes=").append(fecha[1]);
		} else if (opcion==3){
			query.append(" and DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
		} else {
			query.append(" and DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
		}
		query.append(" order by t2.fecha_alta asc, t2.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	Date fechaReporte = new Date();
		    	String fechaConsulta = rs.getString("fecha_consulta");
		    	int formaPago = rs.getInt("forma_pago");
				double pago;
				pago = Double.parseDouble(rs.getString("monto_pagado"));
				total += pago;
				if (formaPago != 1 && formaPago != 4)
					totalEfectivo += pago;
				result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id_recibo"))).append("</h4></td><td><h4>&nbsp;")
					.append(rs.getString("cliente_nombre")).append("</h4></td>")
					.append("<td><h4>&nbsp;Mensualidad&nbsp;").append(Constantes.mes[Integer.parseInt(rs.getString("numero_mes"))]).append("</h4></td>")
					.append("<td><center>").append(rs.getString("fecha_alta")).append("</center></td>")
					.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h2></td>")
					.append("<td><h4>&nbsp;").append(rs.getString("comentario")).append("</h4></td>")
					.append("<td><h4><center>").append(Constantes.sFormaPago[formaPago]).append("</center></h4></td>")
					.append("<td><h4>&nbsp;").append(rs.getString("id_usuario_alta")).append("</h4></td>")
					.append("</tr>");
				while(rs.next()){
					pago = Double.parseDouble(rs.getString("monto_pagado"));
			    	formaPago = rs.getInt("forma_pago");
					total += pago;
					if (formaPago != 1 && formaPago != 4)
						totalEfectivo += pago;
					result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id_recibo"))).append("</h4></td><td><h4>&nbsp;")
						.append(rs.getString("cliente_nombre")).append("</h4></td>")
						.append("<td><h4>&nbsp;Mensualidad&nbsp;").append(Constantes.mes[Integer.parseInt(rs.getString("numero_mes"))]).append("</h4></td>")
						.append("<td><center>").append(rs.getString("fecha_alta")).append("</center></td>")
						.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h2></td>")
						.append("<td><h4>&nbsp;").append(rs.getString("comentario")).append("</h4></td>")
						.append("<td><h4><center>").append(Constantes.sFormaPago[formaPago]).append("</center></h4></td>")
						.append("<td><h4>&nbsp;").append(rs.getString("id_usuario_alta")).append("</h4></td>")
						.append("</tr>");
				}
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total Efectivo ").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalEfectivo)).append("</h2></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total ").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)).append("</h2></td></tr>");
		    } else {
		    	result.append("");
		    }
			jsonFinal.put("id", 1);
			jsonFinal.put("msg", result.toString());
			jsonFinal.put("totalE", totalEfectivo);
			jsonFinal.put("total", total);
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return jsonFinal;
	}

	private static int getInteger(String param) {
		int result = 0;
		if (param!=null && !"".equals(param)) {
			result = Integer.parseInt(param);
		}
		return result;
	}

	public static JSONObject obtenerTablaPagosCorte3(HashMap<String,String> params) throws ParseException{
		int opcion = 0;
		int alumno = 0;
		Boolean duplicado = false;
		String[] fecha = {};
		opcion = getInteger(params.get("o"));
		alumno = getInteger(params.get("a"));
		if (params.get("f")!=null) {
			fecha = params.get("f").split(",");
		}
		duplicado = getBoolean(params.get("d"));
		Boolean buscaMes = true;
		String fechaInicial = "";
		JSONObject jsonFinal = new JSONObject();
		double total = 0;
		double totalEfectivo = 0;
		if (!fecha[0].equals("")){
			buscaMes = false;
		};
		if (buscaMes) {
		    fechaInicial = fecha[1]+"/"+fecha[2];
		} else {
		    fechaInicial = String.format("%02d", Integer.parseInt(fecha[0]))+"/"+fecha[1]+"/"+fecha[2];
		}
		System.out.println("fInicial:"+fechaInicial);
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select distinct t2.id id_recibo, t3.id, t4.id claseId, t1.numero_mes, t1.monto, t2.monto monto_pagado,")
				.append(" t4.id, t4.nombre cliente_nombre, t5.id, CONCAT('Grupo ',t5.nombre) grupo_nombre,")
				.append(" DATE_FORMAT(t2.fecha_alta,'%d/%m/%Y %H:%i:%S') fecha_alta,")
				.append(" DATE_FORMAT(t2.fecha_alta,'%d/%m/%Y %H:%i:%S') fecha_consulta, IFNULL(t2.id_usuario_alta,'') id_usuario_alta, t2.fecha_alta, t1.comentario")
				.append(" ,t1.forma_pago")
				.append(" from grupo_cliente_recibo t1")
				.append(" inner join grupo_cliente_recibo_pago t2 on t1.id=t2.id_grupo_cliente_recibo")
				.append(" inner join grupo_cliente t3 on t3.id=t1.id_grupo_cliente")
				.append(" inner join cliente t4 on t4.id=t3.id_cliente")
				.append(" inner join grupo t5 on t5.id=t3.id_grupo")
				.append(" where t5.estatus=1");
		if (alumno>0) {
			query.append(" and t4.id = ").append(alumno).append(" ");
		}
		if (opcion==2) {
			query.append(" and t1.numero_mes=").append(fecha[1]);
		} else if (opcion==3){
			query.append(" and DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
		} else {
			query.append(" and DATE_FORMAT(t2.fecha_alta,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
		}
		if (!duplicado) {
			query.append(" and t1.forma_pago not in (1)");
		}
		query.append(" order by t2.fecha_alta asc, t2.id");
		System.out.println("q="+query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	Date fechaReporte = new Date();
		    	String fechaConsulta = rs.getString("fecha_consulta");
		    	int formaPago = rs.getInt("forma_pago");
				double pago;
				pago = Double.parseDouble(rs.getString("monto_pagado"));
				total += pago;
				if (formaPago != 1 && formaPago != 4)
					totalEfectivo += pago;
				result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id_recibo"))).append("</h4></td><td><h4>&nbsp;")
					.append(rs.getString("cliente_nombre")).append("</h4></td>")
					.append("<td><h4>&nbsp;Mensualidad&nbsp;").append(Constantes.mes[Integer.parseInt(rs.getString("numero_mes"))]).append("</h4></td>")
					.append("<td><center>").append(rs.getString("fecha_alta")).append("</center></td>")
					.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h2></td>")
					.append("<td><h4>&nbsp;").append(rs.getString("comentario")).append("</h4></td>")
					.append("<td><h4><center>").append(Constantes.sFormaPago[formaPago]).append("</center></h4></td>")
					.append("<td><h4>&nbsp;").append(rs.getString("id_usuario_alta")).append("</h4></td>")
					.append("</tr>");
				while(rs.next()){
					pago = Double.parseDouble(rs.getString("monto_pagado"));
			    	formaPago = rs.getInt("forma_pago");
					total += pago;
					if (formaPago != 1 && formaPago != 4)
						totalEfectivo += pago;
					result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id_recibo"))).append("</h4></td><td><h4>&nbsp;")
						.append(rs.getString("cliente_nombre")).append("</h4></td>")
						.append("<td><h4>&nbsp;Mensualidad&nbsp;").append(Constantes.mes[Integer.parseInt(rs.getString("numero_mes"))]).append("</h4></td>")
						.append("<td><center>").append(rs.getString("fecha_alta")).append("</center></td>")
						.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h2></td>")
						.append("<td><h4>&nbsp;").append(rs.getString("comentario")).append("</h4></td>")
						.append("<td><h4><center>").append(Constantes.sFormaPago[formaPago]).append("</center></h4></td>")
						.append("<td><h4>&nbsp;").append(rs.getString("id_usuario_alta")).append("</h4></td>")
						.append("</tr>");
				}
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total Efectivo ").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalEfectivo)).append("</h2></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total ").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)).append("</h2></td></tr>");
		    } else {
		    	result.append("");
		    }
			jsonFinal.put("id", 1);
			jsonFinal.put("msg", result.toString());
			jsonFinal.put("totalE", totalEfectivo);
			jsonFinal.put("total", total);
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return jsonFinal;
	}


	private static Boolean getBoolean(String strValor) {
		if (strValor != null && !strValor.equals("") && strValor.equalsIgnoreCase("1") ) {
			return true;
		}
		return false;
	}

}
