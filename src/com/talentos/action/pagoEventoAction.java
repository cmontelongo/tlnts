package com.talentos.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Cliente_Pago_Evento;
import com.talentos.entidad.Concepto;
import com.talentos.entidad.Evento;
import com.talentos.util.Constantes;

public class pagoEventoAction {

	private static Logger log = Logger.getLogger(pagoEventoAction.class);


	public static int guardarPagoEvento(Evento evento, Concepto concepto, Cliente_Pago_Evento clientePagoEvento){
		int resultado = 0;
		try {
			clientePagoEvento.setId_evento(eventoAction.guardarEvento(evento));
			clientePagoEvento.setId_concepto(conceptoAction.guardarConcepto(concepto));

		    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			StringBuilder query = new StringBuilder("insert into cliente_pago_evento(id_cliente, id_evento, id_concepto, monto, fecha_pago, comentario) values(")
					.append(clientePagoEvento.getId_cliente()).append(",")
					.append(clientePagoEvento.getId_evento()).append(",")
					.append(clientePagoEvento.getId_concepto()).append(",")
					.append(clientePagoEvento.getMonto()).append(",");
			if (clientePagoEvento.getFecha_pago()!=null)
				query.append("STR_TO_DATE('").append(df.format(clientePagoEvento.getFecha_pago())).append("', '"+Constantes.fechaNacimientoSQL+"'),'");
			else
				query.append("CURDATE(),'");
			query.append(clientePagoEvento.getComentario()).append("')");
			log.info(query.toString());
			Connection con = null;
			try {
				con = coneccion.getConnection();
				con.setAutoCommit(false);

				PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
				pstmt.executeUpdate();  
				ResultSet keys = pstmt.getGeneratedKeys();    
				keys.next();  
				resultado = keys.getInt(1);
				log.info(resultado);
				con.commit();
			
			} catch (SQLException e) {
				con.rollback();
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;
	}

	public static String obtenerListadoClientePagoEvento(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder()
        		.append("select t1.id reciboId, t1.id_cliente, t4.nombre clienteNombre, t1.id_evento, t2.nombre eventoNombre, t1.id_concepto, t3.nombre conceptoNombre, t1.monto, DATE_FORMAT(t1.fecha_pago,'%d/%m/%Y') fecha_pago, t1.comentario ")
				.append("from cliente_pago_evento t1 ")
				.append("inner join evento t2 on t2.id=t1.id_evento ")
				.append("inner join concepto t3 on t3.id = t1.id_concepto ")
				.append("inner join cliente t4 on t4.id=t1.id_cliente ")
        		.append("order by t1.id desc, t4.id desc, t1.fecha_pago desc ");
		Connection con = null;
		System.out.println("q="+query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	Double pago = Double.parseDouble(rs.getString("monto"));
				result = new StringBuilder()
						.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Recibo #</th><th class='text-left'>Cliente</th><th class='text-left'>Evento</th><th class='text-left'>Concepto</th><th class='text-left'>Monto</th><th class='text-left'>Fecha pago</th></tr></thead>");
				result.append("<tr><td>").append(rs.getString("reciboId")).append("</td><td>").append(rs.getString("clienteNombre")).append("</td><td>").append(rs.getString("eventoNombre")).append("</td><td>").append(rs.getString("conceptoNombre")).append("</td><td>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("</td><td>").append(rs.getString("fecha_pago")).append("</td></tr>");
				while(rs.next()){
					pago = Double.parseDouble(rs.getString("monto"));
					result.append("<tr><td>").append(rs.getString("reciboId")).append("</td><td>").append(rs.getString("clienteNombre")).append("</td><td>").append(rs.getString("eventoNombre")).append("</td><td>").append(rs.getString("conceptoNombre")).append("</td><td>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("</td><td>").append(rs.getString("fecha_pago")).append("</td></tr>");
				}
		    } else {
		    	result = new StringBuilder()
				    	.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
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

	public static String obtenerTablaPagosCorte(int opcion, String[] fecha, int sel, int evento, int concepto) throws ParseException{
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
		String titulo = "CORTE DIARIO PAGO DE EVENTOS";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id, t1.id_cliente, t4.nombre cliente_nombre, t1.id_evento, t2.nombre evento_nombre, t1.id_concepto")
			.append(", t3.nombre concepto_nombre, t1.monto, DATE_FORMAT(t1.fecha_pago,'%d/%m/%Y') fecha_pago, t1.comentario, IFNULL(t1.id_usuario_alta,'') id_usuario_alta ")
			.append(", t1.forma_pago ")
			.append("from cliente_pago_evento t1 ")
			.append("inner join evento t2 on t2.id=t1.id_evento ")
			.append("inner join concepto t3 on t3.id = t1.id_concepto ")
			.append("inner join cliente t4 on t4.id=t1.id_cliente ")
			.append("where ");
		if (fecha[1].length()>0)
			if (opcion==3) {
				titulo = "CORTE MENSUAL PAGO DE EVENTOS";
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
			} else {
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
			}
		if (evento > 0 || concepto > 0){
			if (fecha[1].length()>0)
				query.append(" and ");
			titulo = "CORTE POR EVENTO";
			if (evento > 0)
				query.append(" t1.id_evento=").append(evento);
			if (evento>0 && concepto>0)
				query.append(" and ");
			if (concepto > 0)
				query.append(" t1.id_concepto=").append(concepto);
		}
		query.append(" order by t1.fecha_pago, t1.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result.append("<center><table style='width:100%;height:60px'><tr><td style='text-align:right'><h2>Fecha: ").append(fechaInicial).append("</h2></td></tr>")
		    		.append("<tr style='height:60px'><td style='text-align:center'><h1>").append(titulo).append("</h1></td></tr>")
		    		.append("<tr><td><center>")
		    		.append("<table style='width:100%;border-collapse: collapse;' border='3'>")
		    		.append("<thead><tr><th style='text-align:center'><h2>Mat.</h2></th><th style='text-align:center'><h2>Nombre del Alumno</h2></th><th style='text-align:center'><h2>Pago</h2></th>")
		    		.append("<th style='text-align:center'><h2>Evento</h2></th><th style='text-align:center'><h2>Concepto</h2></th>")
		    		.append("<th style='text-align:center'><h2>Fecha pago</h2></th><th style='text-align:center'><h2>Forma de pago</h2></th><th style='text-align:center'><h2>Recibi&oacute;</h2></th></tr></thead>");
				double pago;
				double total = 0;
				int formaPago = rs.getInt("forma_pago");
				pago = Double.parseDouble(rs.getString("monto"));
				total += pago;
				result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id"))).append("</h4></td><td><h4>&nbsp;")
					.append(rs.getString("cliente_nombre")).append("</h4></td><td align=right><h4>")
					.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h4></td>")
					.append("<td><h4>&nbsp;").append(rs.getString("evento_nombre")).append("</h4></td><td><h4>&nbsp;").append(rs.getString("concepto_nombre")).append("</h4></td>")
					.append("<td><center>").append(rs.getString("fecha_pago")).append("</center></td>")
					.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
					.append("<td><h4>").append(rs.getString("id_usuario_alta")).append("</h4></td>")
					.append("</tr>");
				while(rs.next()){
					formaPago = rs.getInt("forma_pago");
					pago = Double.parseDouble(rs.getString("monto"));
					total += pago;
					result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id"))).append("</h4></td><td><h4>&nbsp;")
						.append(rs.getString("cliente_nombre")).append("</h4></td><td align=right><h4>")
						.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h4></td>")
						.append("<td><h4>&nbsp;").append(rs.getString("evento_nombre")+"</h4></td><td><h4>&nbsp;").append(rs.getString("concepto_nombre")).append("</h4></td>")
						.append("<td><center>").append(rs.getString("fecha_pago")).append("</center></td>")
						.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
						.append("<td><h4>").append(rs.getString("id_usuario_alta")).append("</h4></td>")
						.append("</tr>");
				}
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total ").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)).append("</h2></td></tr>");
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

	public static String obtenerTablaPagosCorte(HashMap<String,String> parametros) throws ParseException{
		int opcion = 0;
		String[] fecha = null;
		int sel = 0;
		int evento = 0;
		int concepto = 0;
		int alumno = 0;
		if (parametros.get("f")!=null) {
			fecha = parametros.get("f").split(",");
		}
		opcion = getInteger(parametros.get("o"));
		sel = getInteger(parametros.get("s"));
		evento = getInteger(parametros.get("e"));
		concepto = getInteger(parametros.get("c"));
		alumno = getInteger(parametros.get("a"));
		Boolean buscaMes = true;
		String fechaInicial = "";
		if (fecha.length>0) {
			if (!fecha[0].equals("")){
				buscaMes = false;
			};
			if (buscaMes) {
			    fechaInicial = fecha[1]+"/"+fecha[2];
			} else {
			    fechaInicial = String.format("%02d", Integer.parseInt(fecha[0]))+"/"+fecha[1]+"/"+fecha[2];
			}
		}
		System.out.println("fInicial:"+fechaInicial);
		StringBuilder result = new StringBuilder();
		String titulo = "CORTE DIARIO PAGO DE EVENTOS";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id, t1.id_cliente, t4.nombre cliente_nombre, t1.id_evento, t2.nombre evento_nombre, t1.id_concepto")
			.append(", t3.nombre concepto_nombre, t1.monto, DATE_FORMAT(t1.fecha_pago,'%d/%m/%Y') fecha_pago, t1.comentario, IFNULL(t1.id_usuario_alta,'') id_usuario_alta ")
			.append(", t1.forma_pago ")
			.append("from cliente_pago_evento t1 ")
			.append("inner join evento t2 on t2.id=t1.id_evento ")
			.append("inner join concepto t3 on t3.id = t1.id_concepto ")
			.append("inner join cliente t4 on t4.id=t1.id_cliente ")
			.append("where ");
		if (alumno > 0) {
			query.append(" t4.id = ").append(alumno).append(" ");
		}
		if (fecha.length>0 && fecha[1].length()>0) {
			if (opcion==3) {
				titulo = "CORTE MENSUAL PAGO DE EVENTOS";
				if (alumno > 0) {
					query.append(" and ");
				}
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
			} else {
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
			}
		}
		if (evento > 0 || concepto > 0){
			if (fecha[1].length()>0 || alumno > 0)
				query.append(" and ");
			titulo = "CORTE POR EVENTO";
			if (evento > 0)
				query.append(" t1.id_evento=").append(evento);
			if (evento>0 && concepto>0)
				query.append(" and ");
			if (concepto > 0)
				query.append(" t1.id_concepto=").append(concepto);
		}
		query.append(" order by t1.fecha_pago, t1.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result.append("<center><table style='width:100%;height:60px'><tr><td style='text-align:right'><h2>Fecha: ").append(fechaInicial).append("</h2></td></tr>")
		    		.append("<tr style='height:60px'><td style='text-align:center'><h1>").append(titulo).append("</h1></td></tr>")
		    		.append("<tr><td><center>")
		    		.append("<table style='width:100%;border-collapse: collapse;' border='3'>")
		    		.append("<thead><tr><th style='text-align:center'><h2>Mat.</h2></th><th style='text-align:center'><h2>Nombre del Alumno</h2></th><th style='text-align:center'><h2>Pago</h2></th>")
		    		.append("<th style='text-align:center'><h2>Evento</h2></th><th style='text-align:center'><h2>Concepto</h2></th>")
		    		.append("<th style='text-align:center'><h2>Fecha pago</h2></th><th style='text-align:center'><h2>Forma de pago</h2></th><th style='text-align:center'><h2>Recibi&oacute;</h2></th></tr></thead>");
				double pago;
				double total = 0;
				int formaPago = rs.getInt("forma_pago");
				pago = Double.parseDouble(rs.getString("monto"));
				total += pago;
				result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id"))).append("</h4></td><td><h4>&nbsp;")
					.append(rs.getString("cliente_nombre")).append("</h4></td><td align=right><h4>")
					.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h4></td>")
					.append("<td><h4>&nbsp;").append(rs.getString("evento_nombre")).append("</h4></td><td><h4>&nbsp;").append(rs.getString("concepto_nombre")).append("</h4></td>")
					.append("<td><center>").append(rs.getString("fecha_pago")).append("</center></td>")
					.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
					.append("<td><h4>").append(rs.getString("id_usuario_alta")).append("</h4></td>")
					.append("</tr>");
				while(rs.next()){
					formaPago = rs.getInt("forma_pago");
					pago = Double.parseDouble(rs.getString("monto"));
					total += pago;
					result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id"))).append("</h4></td><td><h4>&nbsp;")
						.append(rs.getString("cliente_nombre")).append("</h4></td><td align=right><h4>")
						.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h4></td>")
						.append("<td><h4>&nbsp;").append(rs.getString("evento_nombre")+"</h4></td><td><h4>&nbsp;").append(rs.getString("concepto_nombre")).append("</h4></td>")
						.append("<td><center>").append(rs.getString("fecha_pago")).append("</center></td>")
						.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
						.append("<td><h4>").append(rs.getString("id_usuario_alta")).append("</h4></td>")
						.append("</tr>");
				}
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total ").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)).append("</h2></td></tr>");
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

	private static int getInteger(String param) {
		int result = 0;
		if (param!=null && !"".equals(param)) {
			result = Integer.parseInt(param);
		}
		return result;
	}

	public static String obtenerTablaPagosCorte2(int opcion, String[] fecha, int sel, int evento, int concepto) throws ParseException{
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
		String titulo = "CORTE DIARIO PAGO DE EVENTOS";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id, t1.id_cliente, t4.nombre cliente_nombre, t1.id_evento, t2.nombre evento_nombre, t1.id_concepto,")
			.append(" t3.nombre concepto_nombre, t1.monto, DATE_FORMAT(t1.fecha_pago,'%d/%m/%Y') fecha_pago, t1.comentario, IFNULL(t1.id_usuario_alta,'') id_usuario_alta ")
			.append(", t1.forma_pago ")
			.append("from cliente_pago_evento t1 ")
			.append("inner join evento t2 on t2.id=t1.id_evento ")
			.append("inner join concepto t3 on t3.id = t1.id_concepto ")
			.append("inner join cliente t4 on t4.id=t1.id_cliente ")
			.append("where ");
		if (fecha[1].length()>0)
			if (opcion==3) {
				titulo = "CORTE MENSUAL PAGO DE EVENTOS";
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
			} else {
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
			}
		if (evento > 0 || concepto > 0){
			if (fecha[1].length()>0)
				query.append(" and ");
			titulo = "CORTE POR EVENTO";
			if (evento > 0)
				query.append(" t1.id_evento=").append(evento);
			if (evento>0 && concepto>0)
				query.append(" and ");
			if (concepto > 0)
				query.append(" t1.id_concepto=").append(concepto);
		}
		query.append(" order by t1.fecha_pago, t1.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result.append("<center><table style='width:100%;height:60px'><tr><td style='text-align:right'><h2>Fecha: ").append(fechaInicial).append("</h2></td></tr>")
		    		.append("<tr style='height:60px'><td style='text-align:center'><h1>").append(titulo).append("</h1></td></tr>")
		    		.append("<tr><td><center>")
		    		.append("<table style='width:100%;border-collapse: collapse;' border='3'>")
		    		.append("<thead><tr><th style='text-align:center'><h2>Mat.</h2></th><th style='text-align:center'><h2>Nombre del Alumno</h2></th><th style='text-align:center'><h2>Pago</h2></th>")
		    		.append("<th style='text-align:center'><h2>Evento</h2></th><th style='text-align:center'><h2>Concepto</h2></th>")
		    		.append("<th style='text-align:center'><h2>Fecha pago</h2></th><th style='text-align:center'><h2>Forma de Pago</h2></th><th style='text-align:center'><h2>Recibi&oacute;</h2></th></tr></thead>");
				double pago;
				double total = 0;
				int formaPago = rs.getInt("forma_pago");
				pago = Double.parseDouble(rs.getString("monto"));
				total += pago;
				result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id"))).append("</h4></td><td><h4>&nbsp;")
					.append(rs.getString("cliente_nombre")).append("</h4></td><td align=right><h4>")
					.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h4></td>")
					.append("<td><h4>&nbsp;").append(rs.getString("evento_nombre")).append("</h4></td><td><h4>&nbsp;").append(rs.getString("concepto_nombre")).append("</h4></td>")
					.append("<td><center>").append(rs.getString("fecha_pago")).append("</center></td>")
					.append("<td><center>").append(obtenerSelectFormaPago(rs.getInt("id"), formaPago)).append("</center></td>")
					.append("<td><h4>").append(rs.getString("id_usuario_alta")).append("</h4></td>")
					.append("</tr>");
				while(rs.next()){
					formaPago = rs.getInt("forma_pago");
					pago = Double.parseDouble(rs.getString("monto"));
					total += pago;
					result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id"))).append("</h4></td><td><h4>&nbsp;")
						.append(rs.getString("cliente_nombre")).append("</h4></td><td align=right><h4>")
						.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h4></td>")
						.append("<td><h4>&nbsp;").append(rs.getString("evento_nombre")).append("</h4></td><td><h4>&nbsp;").append(rs.getString("concepto_nombre")).append("</h4></td>")
						.append("<td><center>").append(rs.getString("fecha_pago")).append("</center></td>")
						.append("<td><center>").append(obtenerSelectFormaPago(rs.getInt("id"), formaPago)).append("</center></td>")
						.append("<td><h4>").append(rs.getString("id_usuario_alta")).append("</h4></td>")
						.append("</tr>");
				}
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total ").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)).append("</h2></td></tr>");
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

	/*
	 * Reporte Consolidado - Reporte de Eventos
	 * */
	public static JSONObject obtenerTablaPagosCorte3(int opcion, String[] fecha, int sel, int evento, int concepto) throws ParseException{
		Boolean buscaMes = true;
		String fechaInicial = "";
		JSONObject jsonFinal = new JSONObject();
		double total = 0;
		double totalEfectivo = 0;
	    double subTotal = 0;
	    int idEvento = 0;
	    StringBuilder eventoNombre = new StringBuilder();
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
		query.append("select t1.id, t4.nombre cliente_nombre, t1.id_evento, t2.nombre evento_nombre, t1.id_concepto, t3.nombre concepto_nombre, t1.monto")
			.append(", DATE_FORMAT(t1.fecha_pago,'%d/%m/%Y') fecha_pago, (case t1.comentario when 'null' then '' else t1.comentario end) comentario, IFNULL(t1.id_usuario_alta,'') id_usuario_alta ")
			.append(", t1.forma_pago ")
			.append("from cliente_pago_evento t1 ")
			.append("inner join evento t2 on t2.id=t1.id_evento ")
			.append("inner join concepto t3 on t3.id = t1.id_concepto ")
			.append("inner join cliente t4 on t4.id=t1.id_cliente ")
			.append("where ");
		if (fecha[1].length()>0)
			if (opcion==3) {
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
			} else {
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
			}
		if (evento > 0 || concepto > 0){
			if (fecha[1].length()>0)
				query.append(" and ");
			if (evento > 0)
				query.append(" t1.id_evento=").append(evento);
			if (evento>0 && concepto>0)
				query.append(" and ");
			if (concepto > 0)
				query.append(" t1.id_concepto=").append(concepto);
		}
		query.append(" group by t1.id_evento, t1.id, t4.nombre, t2.nombre, t1.id_concepto, t3.nombre, t1.monto ")
			.append("order by t1.id_evento ASC, t1.fecha_pago asc, t1.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
				double pago;
				int formaPago = rs.getInt("forma_pago");
				pago = Double.parseDouble(rs.getString("monto"));
				total += pago;
				if (formaPago != 1 && formaPago != 4)
					totalEfectivo += pago;
				idEvento = rs.getInt("id_evento");
				eventoNombre = new StringBuilder(rs.getString("evento_nombre"));
				subTotal += Double.parseDouble(rs.getString("monto"));
				result.append("<tr><td><h4>&nbsp;"+String.format("%06d", rs.getInt("id"))+"</h4></td><td><h4>&nbsp;")
					.append(rs.getString("cliente_nombre")+"</h4></td>")
					.append("<td><h4>&nbsp;"+rs.getString("evento_nombre")+"&nbsp;"+rs.getString("concepto_nombre")+"</h4></td>")
					.append("<td><center>"+rs.getString("fecha_pago")+"</center></td>")
					.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)+"&nbsp;</h4></td>")
					.append("<td>&nbsp;").append(rs.getString("comentario")).append("</td>")
					.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
					.append("<td><h4>&nbsp;"+rs.getString("id_usuario_alta")+"</h4></td>")
					.append("</tr>");
				while(rs.next()){
					formaPago = rs.getInt("forma_pago");
					pago = Double.parseDouble(rs.getString("monto"));
					total += pago;
					if (formaPago != 1 && formaPago != 4)
						totalEfectivo += pago;
					if (rs.getInt("id_evento") != idEvento){
						result.append("<tr><td colspan='6' style='text-align:right'><h2>").append(eventoNombre.toString()).append(" Total "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(subTotal)+"</h2></td></tr>");
						subTotal = 0;
						idEvento = rs.getInt("id_evento");
						eventoNombre = new StringBuilder(rs.getString("evento_nombre"));
					}
					subTotal += Double.parseDouble(rs.getString("monto"));
					result.append("<tr><td><h4>&nbsp;"+String.format("%06d", rs.getInt("id"))+"</h4></td><td><h4>&nbsp;")
						.append(rs.getString("cliente_nombre")+"</h4></td>")
						.append("<td><h4>&nbsp;"+rs.getString("evento_nombre")+"&nbsp;"+rs.getString("concepto_nombre")+"</h4></td>")
						.append("<td><center>"+rs.getString("fecha_pago")+"</center></td>")
						.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)+"&nbsp;</h4></td>")
						.append("<td>&nbsp;").append(rs.getString("comentario")).append("</td>")
						.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
						.append("<td><h4>&nbsp;"+rs.getString("id_usuario_alta")+"</h4></td>")
						.append("</tr>");
				}
				result.append("<tr><td colspan='6' style='text-align:right'><h2>").append(eventoNombre.toString()).append(" Total "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(subTotal)+"</h2></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total Efectivo Eventos "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalEfectivo)+"</h2></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total Eventos "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)+"</h2></td></tr>");
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

	public static JSONObject obtenerTablaPagosCorte3(HashMap<String,String> params) throws ParseException{
		int opcion = 0;
		String[] fecha = {};
		int sel = 0;
		int evento = 0;
		int concepto = 0;
		int alumno = 0;
		if (params.get("f")!=null) {
			fecha = params.get("f").split(",");
		}
		opcion = getInteger(params.get("o"));
		sel = getInteger(params.get("s"));
		evento = getInteger(params.get("e"));
		concepto = getInteger(params.get("c"));
		alumno = getInteger(params.get("a"));
		Boolean buscaMes = true;
		String fechaInicial = "";
		JSONObject jsonFinal = new JSONObject();
		double total = 0;
		double totalEfectivo = 0;
	    double subTotal = 0;
	    int idEvento = 0;
	    StringBuilder eventoNombre = new StringBuilder();
	    if (fecha.length>0) {
			if (!fecha[0].equals("")){
				buscaMes = false;
			};
			if (buscaMes) {
			    fechaInicial = fecha[1]+"/"+fecha[2];
			} else {
			    fechaInicial = String.format("%02d", Integer.parseInt(fecha[0]))+"/"+fecha[1]+"/"+fecha[2];
			}
	    }
		System.out.println("fInicial:"+fechaInicial);
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id, t4.nombre cliente_nombre, t1.id_evento, t2.nombre evento_nombre, t1.id_concepto, t3.nombre concepto_nombre, t1.monto")
			.append(", DATE_FORMAT(t1.fecha_pago,'%d/%m/%Y') fecha_pago, (case t1.comentario when 'null' then '' else t1.comentario end) comentario, IFNULL(t1.id_usuario_alta,'') id_usuario_alta ")
			.append(", t1.forma_pago ")
			.append("from cliente_pago_evento t1 ")
			.append("inner join evento t2 on t2.id=t1.id_evento ")
			.append("inner join concepto t3 on t3.id = t1.id_concepto ")
			.append("inner join cliente t4 on t4.id=t1.id_cliente ")
			.append("where ");
		if (fecha.length>0) {
			if (buscaMes) {
				if (opcion==3) {
					query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
				} else {
					query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
				}
			}
		}
		if (alumno > 0) {
			if (fecha.length>0) {
				query.append(" and ");
			}
			query.append(" t4.id= ").append(alumno).append(" ");
		}
		if (evento > 0 || concepto > 0){
			if (fecha.length>0 || alumno>0)
				query.append(" and ");
			if (evento > 0)
				query.append(" t1.id_evento=").append(evento);
			if (evento>0 && concepto>0)
				query.append(" and ");
			if (concepto > 0)
				query.append(" t1.id_concepto=").append(concepto);
		}
		query.append(" group by t1.id_evento, t1.id, t4.nombre, t2.nombre, t1.id_concepto, t3.nombre, t1.monto ")
			.append("order by t1.id_evento ASC, t1.fecha_pago asc, t1.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
				double pago;
				int formaPago = rs.getInt("forma_pago");
				pago = Double.parseDouble(rs.getString("monto"));
				total += pago;
				if (formaPago != 1 && formaPago != 4)
					totalEfectivo += pago;
				idEvento = rs.getInt("id_evento");
				eventoNombre = new StringBuilder(rs.getString("evento_nombre"));
				subTotal += Double.parseDouble(rs.getString("monto"));
				result.append("<tr><td><h4>&nbsp;"+String.format("%06d", rs.getInt("id"))+"</h4></td><td><h4>&nbsp;")
					.append(rs.getString("cliente_nombre")+"</h4></td>")
					.append("<td><h4>&nbsp;"+rs.getString("evento_nombre")+"&nbsp;"+rs.getString("concepto_nombre")+"</h4></td>")
					.append("<td><center>"+rs.getString("fecha_pago")+"</center></td>")
					.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)+"&nbsp;</h4></td>")
					.append("<td>&nbsp;").append(rs.getString("comentario")).append("</td>")
					.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
					.append("<td><h4>&nbsp;"+rs.getString("id_usuario_alta")+"</h4></td>")
					.append("</tr>");
				while(rs.next()){
					formaPago = rs.getInt("forma_pago");
					pago = Double.parseDouble(rs.getString("monto"));
					total += pago;
					if (formaPago != 1 && formaPago != 4)
						totalEfectivo += pago;
					if (rs.getInt("id_evento") != idEvento){
						result.append("<tr><td colspan='6' style='text-align:right'><h2>").append(eventoNombre.toString()).append(" Total "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(subTotal)+"</h2></td></tr>");
						subTotal = 0;
						idEvento = rs.getInt("id_evento");
						eventoNombre = new StringBuilder(rs.getString("evento_nombre"));
					}
					subTotal += Double.parseDouble(rs.getString("monto"));
					result.append("<tr><td><h4>&nbsp;"+String.format("%06d", rs.getInt("id"))+"</h4></td><td><h4>&nbsp;")
						.append(rs.getString("cliente_nombre")+"</h4></td>")
						.append("<td><h4>&nbsp;"+rs.getString("evento_nombre")+"&nbsp;"+rs.getString("concepto_nombre")+"</h4></td>")
						.append("<td><center>"+rs.getString("fecha_pago")+"</center></td>")
						.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)+"&nbsp;</h4></td>")
						.append("<td>&nbsp;").append(rs.getString("comentario")).append("</td>")
						.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
						.append("<td><h4>&nbsp;"+rs.getString("id_usuario_alta")+"</h4></td>")
						.append("</tr>");
				}
				result.append("<tr><td colspan='6' style='text-align:right'><h2>").append(eventoNombre.toString()).append(" Total "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(subTotal)+"</h2></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total Efectivo Eventos "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalEfectivo)+"</h2></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total Eventos "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)+"</h2></td></tr>");
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

	public static String obtenerTablaPagosCorte4(int opcion, String[] fecha, int sel, int evento, int concepto) throws ParseException{
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
		String titulo = "CORTE DIARIO PAGO DE EVENTOS";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id, t1.id_cliente, t4.nombre cliente_nombre, t1.id_evento, t2.nombre evento_nombre, t1.id_concepto, t3.nombre concepto_nombre, t1.monto, DATE_FORMAT(t1.fecha_pago,'%d/%m/%Y') fecha_pago, t1.comentario, t1.id_usuario_alta ")
			.append(" ,t1.forma_pago ")
			.append("from cliente_pago_evento t1 ")
			.append("inner join evento t2 on t2.id=t1.id_evento ")
			.append("inner join concepto t3 on t3.id = t1.id_concepto ")
			.append("inner join cliente t4 on t4.id=t1.id_cliente ")
			.append("where ");
		if (fecha[1].length()>0)
			if (opcion==3) {
				titulo = "CORTE MENSUAL PAGO DE EVENTOS";
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("'");
			} else {
				query.append(" DATE_FORMAT(t1.fecha_pago,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("'");
			}
		if (evento > 0 || concepto > 0){
			if (fecha[1].length()>0)
				query.append(" and ");
			titulo = "CORTE POR EVENTO";
			if (evento > 0)
				query.append(" t1.id_evento=").append(evento);
			if (evento>0 && concepto>0)
				query.append(" and ");
			if (concepto > 0)
				query.append(" t1.id_concepto=").append(concepto);
		}
		query.append(" order by t1.fecha_pago, t1.id");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result.append("<center><table style='width:100%;height:60px'><tr><td style='text-align:right'><h2>Fecha: ").append(fechaInicial).append("</h2></td></tr>")
		    		.append("<tr style='height:60px'><td style='text-align:center'><h1>"+titulo+"</h1></td></tr>")
		    		.append("<tr><td><center>")
		    		.append("<table style='width:100%;border-collapse: collapse;' border='3'>")
		    		.append("<thead><tr><th style='text-align:center'><h2>Mat.</h2></th><th style='text-align:center'><h2>Nombre del Alumno</h2></th><th style='text-align:center'><h2>Pago</h2></th>")
		    		.append("<th style='text-align:center'><h2>Evento</h2></th><th style='text-align:center'><h2>Concepto</h2></th><th style='text-align:center'><h2>Metodo de pago</h2></th>")
		    		.append("<th style='text-align:center'><h2>Fecha pago</h2></th><th style='text-align:center'><h2>Recibi&oacute;</h2></th></tr></thead>");
				double pago;
				double total = 0;
				pago = Double.parseDouble(rs.getString("monto"));
				total += pago;
				int formaPago = rs.getInt("forma_pago");
				result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id"))).append("</h4></td><td><h4>&nbsp;")
					.append(rs.getString("cliente_nombre")).append("</h4></td><td align=right><h4>")
					.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h4></td>")
					.append("<td><h4>&nbsp;").append(rs.getString("evento_nombre")).append("</h4></td><td><h4>&nbsp;").append(rs.getString("concepto_nombre")).append("</h4></td>")
					.append("<td><center>").append(rs.getString("fecha_pago")).append("</center></td>")
					.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
					.append("<td><h4>").append(rs.getString("id_usuario_alta")).append("</h4></td>")
					.append("</tr>");
				while(rs.next()){
					pago = Double.parseDouble(rs.getString("monto"));
					total += pago;
					result.append("<tr><td><h4>&nbsp;").append(String.format("%06d", rs.getInt("id"))).append("</h4></td><td><h4>&nbsp;")
						.append(rs.getString("cliente_nombre")).append("</h4></td><td align=right><h4>")
						.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(pago)).append("&nbsp;</h4></td>")
						.append("<td><h4>&nbsp;").append(rs.getString("evento_nombre")).append("</h4></td><td><h4>&nbsp;").append(rs.getString("concepto_nombre")).append("</h4></td>")
						.append("<td><center>").append(rs.getString("fecha_pago")).append("</center></td>")
						.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
						.append("<td><h4>").append(rs.getString("id_usuario_alta")).append("</h4></td>")
						.append("</tr>");
				}
				result.append("<tr><td colspan='5' style='text-align:right'><h2>Total ").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)).append("</h2></td></tr>");
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


	public static int actualizaFormaPago(int formaPago, int id) throws SQLException {
		int resultado = 0;
		StringBuilder query = new StringBuilder()
				.append("update cliente_pago_evento set ")
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

}
