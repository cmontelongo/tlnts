package com.talentos.action;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Paquete;
import com.talentos.entidad.Paquete_Detalle;
import com.talentos.entidad.Producto;
import com.talentos.entidad.Producto_Almacen;
import com.talentos.entidad.Producto_Almacen_Inventario;
import com.talentos.entidad.Producto_Inventario;
import com.talentos.util.Constantes;
import com.talentos.util.ContentIdGenerator;

public class productoAction {

	private static Logger log = Logger.getLogger(productoAction.class);
	private static NumberFormat formatter = NumberFormat.getCurrencyInstance();

	public static String obtenerProductoJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select t1.id, t1.descripcion producto_nombre ")
				.append("from producto t1 ")
				.append("ORDER BY t1.id DESC");
		System.out.println(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			result.append("[");
			while(rs.next()){
				result.append("{id:"+rs.getString("id")+",label:\""+rs.getString("producto_nombre")+"\"},");
			}
			con.commit();
			result = new StringBuilder().append(result.toString().substring(0, result.length()-1));
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

	public static String obtenerPaqueteJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select id, nombre, precio_venta ")
				.append("from paquete ")
				.append("ORDER BY ID DESC");
		System.out.println(query.toString());
		Connection con = null;
	    Float venta;
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
				jsonFinal.put("label",rs.getString("nombre"));
				venta = rs.getFloat("precio_venta");
				jsonFinal.put("v",NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(venta));
				jsonFinal.put("vM",venta);
				jsonArray.put(jsonFinal);
			    jsonFinal = new JSONObject();
			}
			if (jsonArray.length()>0){
				jsonArray.write(out);
				result = new StringBuilder(out.toString());
			} else {
				result = new StringBuilder("[{}]");
			}
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

	public static String obtenerProductoCostoJson(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select id,descripcion, 100 costo, ifnull((select distinct id from producto_almacen t2 where t2.id_producto=t1.id),-1) id_almacen from producto t1";
		query += " ORDER BY ID DESC";
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query);
		    rs = pstmt.executeQuery();
			result = "[";
			while(rs.next()){
				String id = rs.getString("id");
				result += "{id:"+id+",label:\""+rs.getString("descripcion")+"\",costo:"+rs.getString("costo")+",idAlmacen:"+rs.getString("id_almacen")+"},";
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

	public static String obtenerUltimosProductos(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
			.append("select t1.clave, t1.descripcion")
			.append(" from producto t1")
			.append(" ORDER BY t1.ID DESC");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
				result = new StringBuilder()
					.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Clave</th><th class='text-left'>Descripcion</th></tr></thead>")
					.append("<tr><td>").append(rs.getString("clave")).append("</td><td>").append(rs.getString("descripcion")).append("</td></tr>");
				while(rs.next()){
					result.append("<tr><td>").append(rs.getString("clave")).append("</td><td>").append(rs.getString("descripcion")).append("</td></tr>");
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

	public static int guardarProducto(Producto producto) throws SQLException{
		int respuesta = 1;

		String query = "insert into producto(clave,descripcion,comentario) values(";
		query += "'"+producto.getClave()+"',"
				+"'"+producto.getDescripcion()+"',"
				+"'"+producto.getComentario()+"'";
		query += ")";
		log.info(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			ResultSet keys = pstmt.getGeneratedKeys();    
			keys.next();  
			respuesta = keys.getInt(1);
			log.info(respuesta);
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

		return respuesta;
	}

	public static int guardarProductoAlmacen(Producto_Almacen productoAlmacen) throws SQLException{
		int respuesta = 1;

		StringBuilder query = new StringBuilder();
/*		if (productoAlmacen.getId()>0){
			query.append("update producto_almacen set")
				.append(",fecha_ultima_modificacion='").append(productoAlmacen.getFecha_alta()).append("'")
				.append(" where id=").append(productoAlmacen.getId());
		} else {*/
			query.append("insert into producto_almacen(id_producto,id_almacen,id_talla,comentario) values(")
				.append(productoAlmacen.getId_producto()).append(",")
				.append(productoAlmacen.getId_almacen()).append(",")
				.append(productoAlmacen.getId_talla()).append(",")
				.append("'").append(productoAlmacen.getComentario()).append("'")
				.append(")");
//		};
		log.info(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			ResultSet keys = pstmt.getGeneratedKeys();    
			keys.next();  
			respuesta = keys.getInt(1);
			log.info(respuesta);
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

		return respuesta;
	}

	public static int guardarPaquete(Paquete paquete) throws SQLException {
		int respuesta = 0;

		StringBuilder query = new StringBuilder()
				.append("insert into paquete(nombre,precio_compra,precio_venta,comentario) values(")
				.append("'").append(paquete.getNombre()).append("',")
				.append(paquete.getPrecio_compra()).append(",")
				.append(paquete.getPrecio_venta()).append(",")
				.append("'").append(paquete.getComentario()).append("'")
				.append(")");
		log.info(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			ResultSet keys = pstmt.getGeneratedKeys();    
			keys.next();  
			respuesta = keys.getInt(1);
			log.info(respuesta);
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

		return respuesta;
	}

	public static int guardarPaqueteDetalle(Paquete_Detalle pd) throws SQLException {
		int respuesta = 0;

		StringBuilder query = new StringBuilder()
				.append("insert into paquete_detalle(id_paquete,cantidad,id_producto_almacen) values(")
				.append(pd.getId_paquete()).append(",")
				.append(pd.getCantidad()).append(",")
				.append(pd.getId_producto_almacen())
				.append(")");
		log.info(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			ResultSet keys = pstmt.getGeneratedKeys();    
			keys.next();  
			respuesta = keys.getInt(1);
			log.info(respuesta);
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

		return respuesta;
	}

	public static String obtenerUltimosPaquete(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder()
        		.append("select t1.id,t1.nombre paquete_nombre, t5.id, t5.nombre almacen_nombre, t4.id, CONCAT(t4.descripcion,'(',t6.nombre,')') producto_descripcion, t6.id, t6.nombre, t6.descripcion, t2.cantidad, IFNULL(t1.precio_compra,0) precio_compra, t1.precio_venta ")
        		.append("from paquete t1 ")
        		.append("inner join paquete_detalle t2 on t1.id=t2.id_paquete ")
        		.append("inner join producto_almacen t3 on t3.id=t2.id_producto_almacen ")
        		.append("inner join almacen t5 on t5.id=t3.id_almacen ")
        		.append("inner join producto t4 on t4.id=t3.id_producto ")
        		.append("inner join talla t6 on t6.id=t3.id_talla ")
        		.append("ORDER BY t1.ID DESC");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    Float precioCompra;
		    Float precioVenta;
		    if (rs.next()){
		    	precioCompra = rs.getFloat("precio_compra");
		    	precioVenta = rs.getFloat("precio_venta");
				result = new StringBuilder()
					.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Paquete</th><th class='text-left'>Almacen</th><th class='text-left'>Producto</th><th class='text-left'>Cantidad</th><th class='text-left'>Precio Compra</th><th class='text-left'>Precio Venta</th></tr></thead>")
					.append("<tr><td>").append(rs.getString("paquete_nombre")).append("</td><td>").append(rs.getString("almacen_nombre")).append("</td><td>").append(rs.getString("producto_descripcion")).append("</td><td>").append(rs.getString("cantidad")).append("</td><td>").append(formatter.format(precioCompra)).append("</td><td>").append(formatter.format(precioVenta)).append("</td></tr>");
				while(rs.next()){
			    	precioCompra = rs.getFloat("precio_compra");
			    	precioVenta = rs.getFloat("precio_venta");
					result.append("<tr><td>").append(rs.getString("paquete_nombre")).append("</td><td>").append(rs.getString("almacen_nombre")).append("</td><td>").append(rs.getString("producto_descripcion")).append("</td><td>").append(rs.getString("cantidad")).append("</td><td>").append(formatter.format(precioCompra)).append("</td><td>").append(formatter.format(precioVenta)).append("</td></tr>");
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

	@SuppressWarnings("unused")
	public static String obtenerUltimosPaqueteVenta(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder()
        		.append("select t1.id, t2.id id_detalle, t1.id_cliente, DATE_FORMAT(t1.fecha_recibo, '%d/%b/%Y %H:%i') fecha_recibo, t1.nombre_cliente, t2.id_producto_almacen, t2.id_paquete, sum(t2.cantidad) cantidad, sum(t2.precio_venta) precio_venta ")
        		.append("from cliente_recibo t1 ")
        		.append("inner join cliente_recibo_detalle t2 on t1.id=t2.id_cliente_recibo ")
        		.append("group by t1.id, t2.id, t1.id_cliente, t1.fecha_recibo, t2.id_producto_almacen, t2.id_paquete ")
        		.append("order by t1.id desc, t2.id desc, t2.id_producto_almacen desc, t2.id_paquete desc ");
		Connection con = null;
		System.out.println("q="+query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    Float precioVenta = 0f;
		    Float totalVenta = 0f;
		    int cantidad = 0;
		    int idProducto = 0;
		    int idPaquete = 0;
		    int id2 = 0;
		    int idAnterior2 = -1;
		    int id = 0;
		    int idAnterior = -1;
		    int idCliente = 0;
		    String nombreCliente = "";
		    String fechaRecibo = "";
		    if (rs.next()){
		    	id = rs.getInt("id");
		    	id2 = rs.getInt("id_detalle");
		    	idCliente = rs.getInt("id_cliente");
		    	nombreCliente = rs.getString("nombre_cliente");
		    	fechaRecibo = rs.getString("fecha_recibo");
		    	precioVenta = rs.getFloat("precio_venta");
		    	cantidad = rs.getInt("cantidad");
		    	idProducto = rs.getInt("id_producto_almacen");
		    	idPaquete = rs.getInt("id_paquete");
		    	totalVenta = (cantidad * precioVenta);
				result = new StringBuilder()
					.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Cliente</th><th class='text-left'>Recibo #</th><th class='text-left'>Total Venta</th><th class='text-left'>Fecha Venta</th><th class='text-left' width='120px'>Acci&oacute;n</th></tr></thead>");
				while(rs.next()){
					idAnterior = id;
			    	id = rs.getInt("id");
			    	cantidad = rs.getInt("cantidad");
			    	precioVenta = rs.getFloat("precio_venta");
					if (id == idAnterior){
				    	totalVenta = totalVenta + (cantidad * precioVenta);
					} else {
						result.append("<tr><td>").append(nombreCliente).append("</td><td>")
							.append(idAnterior).append("</td><td>")
							.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("</td>")
							.append("<td>").append(fechaRecibo).append("</td>")
							.append("<td>")
								.append("<a href='javascript:void(0);' onclick='parent.parent.detalle(").append(idAnterior).append(")'><img src='./img/text-documents.png' alt='Detalle' width='30px' class='dialog-link' /></a>&nbsp;&nbsp;&nbsp;");
						if (idCliente > 0){
							result.append("<a href='javascript:void(0);' onclick='parent.parent.correo(").append(id).append(")'><img src='./img/letter-with-stamp.png' alt='Enviar por correo' width='30px' class='dialog-link' /></a>&nbsp;&nbsp;&nbsp;");
						} else {
							result.append("&nbsp;&nbsp;&nbsp;");
						}
						result.append("<a class='btnPrintR' href='/productoServlet?id=").append(idAnterior).append("&p=1&o=1'><img src='./img/print-button.png' alt='imprimir' width='30px' class='btnPrint' /></a>");
						result.append("</td></tr>");
				    	idCliente = rs.getInt("id_cliente");
				    	nombreCliente = rs.getString("nombre_cliente");
				    	fechaRecibo = rs.getString("fecha_recibo");
				    	idProducto = rs.getInt("id_producto_almacen");
				    	idPaquete = rs.getInt("id_paquete");
				    	totalVenta = (cantidad * precioVenta);
					}
			    	id2 = rs.getInt("id_detalle");
				}
		    } else {
		    	result = new StringBuilder()
				    	.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
		    if (id>0){
				result.append("<tr><td>").append(nombreCliente).append("</td><td>")
				.append(id).append("</td><td>")
				.append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("</td><td>")
				.append(fechaRecibo).append("</td><td><a href='javascript:void(0);' onclick='parent.parent.detalle(").append(id).append(")'><img src='./img/text-documents.png' alt='Detalle' width='30px' class='dialog-link' /></a>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' onclick='parent.parent.correo(").append(id).append(")'><img src='./img/letter-with-stamp.png' alt='Enviar por correo' width='30px' class='dialog-link' /></a>&nbsp;&nbsp;&nbsp;<a class='btnPrintR' href='/productoServlet?id=").append(id).append("&p=1'><img src='./img/print-button.png' alt='imprimir' width='30px' class='btnPrint' /></a></td></tr>");
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

	@SuppressWarnings("unused")
	public static String obtenerPaqueteVenta(int id, String cid, String opcion){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder()
        		.append("select t1.id, t1.id_cliente, DATE_FORMAT(t1.fecha_recibo,'%d/%b/%Y %H:%i') fecha_recibo, t1.nombre_cliente, t2.id_producto_almacen, t2.id_paquete, t2.cantidad, t2.precio_venta ")
        		.append("from cliente_recibo t1 ")
        		.append("inner join cliente_recibo_detalle t2 on t1.id=t2.id_cliente_recibo ")
        		.append("where t1.id=").append(id)
        		.append(" order by t1.id desc, t2.id desc, t2.id_producto_almacen desc, t2.id_paquete desc ");
		Connection con = null;
		System.out.println(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    int idRecibo = 0;
		    StringBuilder clienteNombre;
		    String fechaRecibo;
		    int productoAId = 0;
		    int paqueteId = 0;
		    Float montoVenta = 0f;
		    int cantidad = 0;
		    Float totalVenta = 0f;
		    Float total = 0f;
		    if (rs.next()){
		    	idRecibo = rs.getInt("id");
		    	clienteNombre = new StringBuilder(rs.getString("nombre_cliente"));
		    	fechaRecibo = rs.getString("fecha_recibo");
		    	productoAId = rs.getInt("id_producto_almacen");
		    	paqueteId = rs.getInt("id_paquete");
		    	montoVenta = Float.parseFloat(rs.getString("precio_venta"));
		    	cantidad = rs.getInt("cantidad");
		    	totalVenta = cantidad * montoVenta;
		    	total = totalVenta;
				result = new StringBuilder()
						.append("<table width='100%' border=0 cellpadding='5' cellspacing='5' style='font-family: \"Trebuchet MS\", sans-serif'><tr><td colspan='3'>");
				if (cid!=null && !cid.equals(""))
					result.append("<img src='cid:").append(cid).append("'>");
				else {
					if (opcion.equals("1")){
						result.append("<img src='./img/logo_1.png' />");
					}
				}
					
				result.append("</td><td align='right'><h3>Recibo #&nbsp;&nbsp;").append(id).append("</td></tr>")
						.append("<tr><td colspan='2'><h4><i>Cliente : </i></h4><h2>").append(clienteNombre).append("</h2></td><td align='right' colspan='2' width='140px'><h4><i>Fecha : </i></h4><h4>").append(fechaRecibo).append("</h4></td></tr>")
						.append("<tr><td colspan='4'>&nbsp;</td></tr><tr><td colspan='4'>");
				result.append("<table width='100%' border=0 cellpadding='8' cellspacing='8' style='border: 3px solid black;border-collapse: collapse;'><tr><td colspan='2' align='center' style='border: 3px solid black;border-collapse: collapse;'><b>Concepto</b></td><td align='center' width='90px' style='border: 3px solid black;border-collapse: collapse;'><b>Cantidad</b></td><td align='center' width='100px' style='border: 3px solid black;border-collapse: collapse;'><b>P.Unitario</b></td><td width='100px' align='center' style='border: 3px solid black;border-collapse: collapse;'><b>Total</b></td></tr>");
				if (productoAId>0){
					result.append(obtenerProductoDetalle(productoAId,montoVenta, cantidad));
				} else {
				    result.append(obtenerPaqueteDetalle(paqueteId, montoVenta, cantidad));
				}
				while(rs.next()){
					result.append("</td></tr>");
			    	montoVenta = Float.parseFloat(rs.getString("precio_venta"));
			    	cantidad = rs.getInt("cantidad");
			    	productoAId = rs.getInt("id_producto_almacen");
			    	paqueteId = rs.getInt("id_paquete");
			    	totalVenta = cantidad * montoVenta;
			    	total += totalVenta;
					if (productoAId>0){
						result.append(obtenerProductoDetalle(productoAId, montoVenta, cantidad));
					} else {
						result.append(obtenerPaqueteDetalle(paqueteId, montoVenta, cantidad));
					}
				}
				result.append("</table></td></tr><tr><td colspan='3' align='right'><h3>Total :</h3></td><td align='right' width='120px'><h3>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)).append("</h3></td>");
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

	private static String obtenerPaqueteDetalle(int paqueteId, Float montoVenta, int cantidad) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select t1.id, t1.nombre, t2.id_producto_almacen, t2.cantidad ")
				.append("from paquete t1 ")
				.append("inner join paquete_detalle t2 on t1.id=t2.id_paquete ")
				.append("where t1.id=").append(paqueteId)
				.append(" ORDER BY ID DESC");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    Float total = cantidad * montoVenta;
			if (rs.next()){
				result.append("<tr><td colspan='2' style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("nombre")).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(cantidad).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(formatter.format(montoVenta)).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(formatter.format(total)).append("</td></tr>")
					.append("<tr><td width='3%'>&nbsp;</td><td colspan='2'>")
					.append("<table width='100%' border=1 cellpadding='5' cellspacing='5' style='border: 5px solid black;border-collapse: collapse;'><thead><tr><td width='80%' style='border: 3px solid black;border-collapse: collapse;'><i>Producto</i></td>")
					.append("<td style='border: 3px solid black;border-collapse: collapse;'><i>Talla</i></td><td style='border: 3px solid black;border-collapse: collapse;'><i>Cantidad</i></td></tr></thead>")
//					.append("<tr><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("producto")).append("</td><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("talla"))
//					.append("<td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(cantidad).append("</td>")
					.append(obtenerProductoDetalle(rs.getInt("id_producto_almacen"), 0f, rs.getInt("cantidad")));
				while(rs.next()) {
//					result.append("</td></tr>")
					result.append(obtenerProductoDetalle(rs.getInt("id_producto_almacen"), 0f, rs.getInt("cantidad")));
				}
				result.append("</table></td><td colspan='3'>&nbsp;</td></tr>");
			}
			con.commit();
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

	private static String obtenerProductoDetalle(int productoAId, Float montoVenta, int cantidad) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select t1.id, t1.id_producto, t2.descripcion producto, t1.id_talla, t3.descripcion talla ")
				.append("from producto_almacen t1 ")
				.append("inner join producto t2 on t2.id=t1.id_producto ")
				.append("inner join talla t3 on t3.id=t1.id_talla ")
				.append("where t1.id=").append(productoAId)
				.append(" ORDER BY ID DESC");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    System.out.println(cantidad);
		    System.out.println(montoVenta);
		    Float total = cantidad * montoVenta;
		    if (rs.next()){
				if (montoVenta == 0){
					result.append("<tr><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("producto")).append("</td><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("talla"))
						.append("<td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(cantidad);
				} else {
					result.append("<tr><td colspan='2' style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("producto")).append(" (").append(rs.getString("talla")).append(")</td>")
						.append("<td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(cantidad).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(montoVenta)).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total));
				}
				while(rs.next()){
					result.append("</td></tr>");
					if (montoVenta == 0){
						result.append("<tr><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("producto")).append("</td><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("talla"))
							.append("<td style='border: 3px solid black;border-collapse: collapse;'>").append(cantidad);
					} else {
						result.append("<tr><td colspan='2' style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("producto")).append(" (").append(rs.getString("talla")).append(")</td>")
							.append("<td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(cantidad).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(montoVenta)).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total));
					}
				}
				result.append("</td></tr>");
		    }
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

    public static StringBuilder service(int id)
        throws ServletException, IOException, NamingException {
        Session mailSession;
        StringBuilder resultado = new StringBuilder();
        StringBuilder respuesta = new StringBuilder();
        try {        	
        	InitialContext ctx = new InitialContext();
        	String[] correos = obtenerCorreos(id);
        	if (correos.length>0){
        	    String cid = ContentIdGenerator.getContentId();
            	resultado = new StringBuilder(obtenerPaqueteVenta(id,cid,"1"));

            	mailSession = (Session) ctx.lookup("java:jboss/mail/Gmail");
	            MimeMessage m = new MimeMessage(mailSession);
	            Address from = new InternetAddress("noreplay@talentos.com.mx","Talentos");
	            Address[] to = new InternetAddress[] { new InternetAddress(
	            		correos[1],correos[0]) };
	            m.setFrom(from);
	            m.setRecipients(Message.RecipientType.TO, to);
	            m.setSubject("Talentos - Recibo de Compra # "+id);
	            //m.setContent(resultado, "text/plain");
	            MimeMultipart multipart = new MimeMultipart("related");
	            BodyPart messageBodyPart = new MimeBodyPart();
	            messageBodyPart.setContent(resultado.toString(), "text/html;charset=UTF-8");
	            multipart.addBodyPart(messageBodyPart);
	            // Image part
	            try{
		            MimeBodyPart imagePart = new MimeBodyPart();
		            File imagen = new File("../standalone/deployments/img/logo_1.png");
		            System.out.println(imagen.getAbsolutePath());
		            imagePart.attachFile(imagen.getAbsolutePath());
		            imagePart.setContentID("<" + cid + ">");
		            imagePart.setDisposition(MimeBodyPart.INLINE);
		            multipart.addBodyPart(imagePart);
	            } catch(Exception ex){
	            	log.error(ex.getLocalizedMessage());
	            }

	            m.setContent(multipart);
	            Transport.send(m);

	            respuesta = new StringBuilder ("Mensaje enviado correctamente.");
        	}

        } catch (javax.mail.MessagingException e) {
        	respuesta = new StringBuilder ("Error al intentar realizar el envio de correo (Mensaje: "+e.getLocalizedMessage()+")");
            e.printStackTrace();
        }
        return respuesta;
    }

	private static String[] obtenerCorreos(int id) {
		String[] result = {};
		StringBuilder query = new StringBuilder()
    		.append("select t3.id, t3.madre_nombre, t3.madre_email, t3.padre_nombre, t3.padre_email, t1.id ")
    		.append("from cliente_recibo t1 ")
    		.append("inner join cliente_recibo_detalle t2 on t1.id=t2.id_cliente_recibo ")
    		.append("inner join cliente t3 on t3.id=t1.id_cliente ")
    		.append("where t1.id=").append(id)
    		.append(" order by t1.id desc, t2.id desc, t2.id_producto_almacen desc, t2.id_paquete desc ");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    PreparedStatement pstmt = con.prepareStatement(query.toString());
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result = new String[5];
		    	result[0] = rs.getString("madre_nombre");
		    	result[1] = rs.getString("madre_email");
		    	result[2] = rs.getString("padre_nombre");
		    	result[3] = rs.getString("padre_email");
		    }
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

		log.debug(result.length);

		return result;
	}

    public static List<Producto_Inventario> obtenerListadoProductoInventario(){
    	List<Producto_Inventario> productoInventario = new ArrayList<Producto_Inventario>();

		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("SELECT id, id_producto_almacen, cantidad, precio_venta_ultima, id_usuario_alta, fecha_alta, id_usuario_ultima_modificacion, fecha_ultima_modificacion FROM producto_inventario ")
				.append(" ORDER BY ID DESC");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    Producto_Inventario pi = new Producto_Inventario();
		    if (rs.next()){
				pi.setId(rs.getInt("id"));
				pi.setId_producto_almacen(rs.getInt("id_producto_almacen"));
				pi.setCantidad(rs.getInt("cantidad"));
				pi.setPrecio_venta_ultima(rs.getFloat("precio_venta_ultima"));
				productoInventario.add(pi);
				pi = new Producto_Inventario();
				while(rs.next()){
					pi.setId(rs.getInt("id"));
					pi.setId_producto_almacen(rs.getInt("id_producto_almacen"));
					pi.setCantidad(rs.getInt("cantidad"));
					pi.setPrecio_venta_ultima(rs.getFloat("precio_venta_ultima"));
					productoInventario.add(pi);
					pi = new Producto_Inventario();
				}
		    }
			con.commit();
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

    	return productoInventario;
    }

    public static Producto_Inventario obtenerProductoInventario(int idProductoAlmacen){
    	Producto_Inventario productoInventario = new Producto_Inventario();

		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("SELECT id, id_producto_almacen, cantidad, precio_venta_ultima, id_usuario_alta, fecha_alta, id_usuario_ultima_modificacion, fecha_ultima_modificacion FROM producto_inventario ")
				.append(" where id_producto_almacen=").append(idProductoAlmacen)
				.append(" ORDER BY ID DESC");
		Connection con = null;
		log.info(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	productoInventario.setId(rs.getInt("id"));
		    	productoInventario.setId_producto_almacen(rs.getInt("id_producto_almacen"));
		    	productoInventario.setCantidad(rs.getInt("cantidad"));
		    	productoInventario.setPrecio_venta_ultima(rs.getFloat("precio_venta_ultima"));
		    }
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

    	return productoInventario;
    }

    public static boolean actualizarProductoInventario(Producto_Inventario productoInventario, boolean esVenta) throws SQLException{
    	boolean actualizar = false;
		boolean esNuevo = false;
    	Producto_Inventario productoTemp = obtenerProductoInventario(productoInventario.getId_producto_almacen());
    	System.out.println("Cantidad disponible: "+productoTemp.getCantidad()+" - Compra/Venta: "+productoInventario.getCantidad()+".");
    	if (esVenta){
    		System.out.println("VENTA de producto.");
    		if (productoTemp.getId()==0){
	    		System.out.println("ERROR. No hay productos disponibles.");
	    	} else if(productoTemp.getCantidad()<productoInventario.getCantidad()){
	    		System.out.println("Cantidad de producto insuficiente.");
	    	} else if(productoTemp.getCantidad()>=productoInventario.getCantidad()){
	    		System.out.println("Venta de producto ("+productoTemp.getId()+").");
	    		actualizar = true;
	    	}
    	} else {
    		actualizar = true;
    		System.out.println("ACTUALIZAR productos "+productoTemp.getId());
    		if (productoTemp.getId()==0){
    			esNuevo = true;
    		}
    	}

    	if (actualizar){
    		StringBuilder query = new StringBuilder();
			if (esVenta || !esNuevo ){
    			query.append("update producto_inventario set");
    			if (esVenta)
    				query.append(" cantidad=cantidad-").append(productoInventario.getCantidad());
    			else
    				query.append(" cantidad=cantidad+").append(productoInventario.getCantidad())
    					.append(" ,precio_venta_ultima=").append(productoInventario.getPrecio_venta_ultima());
    			query.append(",fecha_ultima_modificacion=CURRENT_TIMESTAMP")
    				.append(" where id=").append(productoTemp.getId());
    		} else {
    			query.append("insert into producto_inventario(id_producto_almacen,cantidad,precio_venta_ultima) values(")
    				.append(productoInventario.getId_producto_almacen()).append(",")
    				.append(productoInventario.getCantidad()).append(",")
    				.append(productoInventario.getPrecio_venta_ultima())
    				.append(")");
    		};
    		log.info(query.toString());
    		Connection con = null;
    		try {
    			con = coneccion.getConnection();
    			con.setAutoCommit(true);
    			PreparedStatement pstmt = con.prepareStatement(query.toString());  
    			pstmt.executeUpdate();  
    			log.info("producto_inventario ("+productoInventario.getId()+")");
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
    	}

    	return actualizar;
    }

	public static boolean validaInventario(Map<String, String[]> params) {
		boolean valida = true;
		JSONArray jsonA = new JSONArray(params.get("productos")[0]);
		if (jsonA!=null && jsonA.length()>0){
			for (int i=0; i<jsonA.length(); i++){
				JSONObject jsonO = jsonA.getJSONObject(i);
				System.out.println(jsonO.getInt("id")+":"+jsonO.getInt("c")+"x"+jsonO.getFloat("v"));
				if (!verificaInventario(1, jsonO.getInt("id"), jsonO.getInt("c"))){
					return false;
				}
			}
		}
		jsonA = new JSONArray(params.get("paquetes")[0]);
		if (jsonA!=null && jsonA.length()>0){
			for (int i=0; i<jsonA.length(); i++){
				JSONObject jsonO = jsonA.getJSONObject(i);
				System.out.println(jsonO.getInt("id")+":"+jsonO.getInt("c")+"x"+jsonO.getFloat("v"));
				if (!verificaInventario(2, jsonO.getInt("id"), jsonO.getInt("c"))){
					return false;
				}
			}
		}

		System.out.println(params.get("productos")[0]);
		System.out.println(params.get("paquetes")[0]);

		return valida;
	}

	private static boolean verificaInventario(int producto, int id, int cant) {
		if (producto == 1){
			Producto_Inventario pi = obtenerProductoInventario(id);
			if (pi.getCantidad()<cant){
				return false;
			}
		} else {
			List<Paquete_Detalle> listaPd = obtenerPaqueteDetalleD(id);
			for (Paquete_Detalle pd : listaPd){
				if (!verificaInventario(1, pd.getId_producto_almacen(),pd.getCantidad())){
					return false;
				}
			}
		}

		return true;
	}

	private static List<Paquete_Detalle> obtenerPaqueteDetalleD(int paqueteId) {
		List<Paquete_Detalle> listaPd = new ArrayList<Paquete_Detalle>();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select t1.id, t1.nombre, t2.id_producto_almacen, t2.cantidad ")
				.append("from paquete t1 ")
				.append("inner join paquete_detalle t2 on t1.id=t2.id_paquete ")
				.append("where t1.id=").append(paqueteId)
				.append(" ORDER BY ID DESC");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			while(rs.next()) {
			    Paquete_Detalle pd = new Paquete_Detalle();
			    pd.setId(rs.getInt("id"));
			    pd.setCantidad(rs.getInt("cantidad"));
			    pd.setId_producto_almacen(rs.getInt("id_producto_almacen"));
				listaPd.add(pd);
			}
			con.commit();
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
		return listaPd;
	}

	public static int guardarProductoAlmacenInventario(Producto_Almacen_Inventario pai) throws SQLException {
		int respuesta = 0;
		String query = "insert into producto_almacen_inventario(id_producto_almacen,cantidad,precio_compra,precio_venta,comentario) values(";
		query += pai.getId_producto_almacen()+","
				+pai.getCantidad()+","
				+pai.getPrecio_compra()+","
				+pai.getPrecio_venta()+","
				+"'"+pai.getComentario()+"'";
		query += ")";
		log.info(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();    
			keys.next();  
			respuesta = keys.getInt(1);
			log.info(respuesta);
			con.commit();

			Producto_Inventario pi = new Producto_Inventario();
			pi.setId_producto_almacen(pai.getId_producto_almacen());
			pi.setCantidad(pai.getCantidad());
			pi.setPrecio_venta_ultima(pai.getPrecio_venta());
			actualizarProductoInventario(pi, false);
		} catch (SQLException e) {
			log.error("Ocurrio un error al actualizar la informacion del inventario ("+e.getLocalizedMessage()+")");
			throw e;
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return respuesta;
	}

	public static String obtenerProductoAlmacenInvJson(){
		StringBuilder respuesta = new StringBuilder();

		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select distinct t1.id_producto_almacen id, CONCAT(t3.descripcion,' (',t4.nombre,')') descripcion, t1.cantidad, t1.precio_compra, t1.precio_venta costo ")
				.append("from producto_almacen_inventario t1 ")
				.append("inner join producto_almacen t2 on t2.id=t1.id_producto_almacen ")
				.append("inner join producto t3 on t3.id=t2.id_producto ")
				.append("inner join talla t4 on t4.id=t2.id_talla ")
				.append("order by t1.id desc");
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
				jsonFinal.put("id", rs.getString("id"));
				jsonFinal.put("label", rs.getString("descripcion"));
				jsonFinal.put("costo", rs.getString("costo"));
				jsonArray.put(jsonFinal);
			    jsonFinal = new JSONObject();
			}
			jsonArray.write(out);
			String jsonText = out.toString();
		    System.out.println(jsonText);
		    
		    respuesta = new StringBuilder(jsonText);
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
		
		return respuesta.toString();
	}

	public static StringBuilder obtenerProductoAlmacenJson(int id, boolean esParaVenta){
		StringBuilder respuesta = new StringBuilder();

		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				/*.append("select distinct t2.id, CONCAT(t3.descripcion,' (',t4.nombre,')') descripcion ")
				.append("from producto_almacen t2 ")
				.append("inner join producto t3 on t3.id=t2.id_producto ")
				.append("inner join talla t4 on t4.id=t2.id_talla ");*/
			.append("select t3.id, CONCAT(t1.descripcion, ' (',t2.nombre,')') descripcion, t3.id id_almacen, t2.nombre talla_nombre, t2.descripcion talla_descripcion, t4.precio_venta_ultima precio_venta, t4.cantidad ")
			.append("from producto t1 ")
			.append("inner join producto_almacen t3 on t1.id=t3.id_producto ")
			.append("inner join talla t2 on t2.id=t3.id_talla ")
			.append("inner join producto_inventario t4 on t4.id_producto_almacen=t3.id ");
		if (esParaVenta && id >=0) {
			query.append("where t4.cantidad>0 ")
				.append("and t3.id_almacen=").append(id);
		} else if (id >=0) {
			query.append("where t3.id_almacen=").append(id);
		}
		query.append(" order by t1.descripcion, t1.id desc");
		Connection con = null;
		log.info("obtenerProductoAlmacenJson: "+query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			JSONObject jsonFinal = new JSONObject();
		    JSONArray jsonArray = new JSONArray();
		    StringWriter out = new StringWriter();
			while(rs.next()){
			    jsonFinal.put("id", rs.getString("id"));
				jsonFinal.put("label", rs.getString("descripcion"));
				jsonFinal.put("idA", rs.getString("id_almacen"));
				jsonFinal.put("v", NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(rs.getFloat("precio_venta")));
				jsonFinal.put("vM", rs.getFloat("precio_venta"));
				jsonFinal.put("c", rs.getInt("cantidad"));
				jsonArray.put(jsonFinal);
			    jsonFinal = new JSONObject();
			}
			jsonArray.write(out);
			String jsonText = out.toString();
		    System.out.println(jsonText);
		    
		    respuesta = new StringBuilder(jsonText);
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
		
		return respuesta;
	}

	public static StringBuilder obtenerInventario() {
		StringBuilder respuesta = new StringBuilder();

		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select t1.id, t5.nombre almacen, CONCAT(t3.descripcion,' (',t4.nombre,')') producto, t1.cantidad, t1.precio_venta_ultima precio_venta ")
				.append("from producto_inventario t1 ")
				.append("inner join producto_almacen t2 on t1.id_producto_almacen=t2.id ")
				.append("inner join producto t3 on t2.id_producto=t3.id ")
				.append("inner join talla t4 on t2.id_talla=t4.id ")
				.append("inner join almacen t5 on t2.id_almacen=t5.id ")
				.append(" ORDER BY t3.descripcion ASC");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
				respuesta.append("<table width='100%' cellpadding='5' cellspacing='5' style='border: 3px solid black;border-collapse: collapse;'><thead><tr><td style='border: 3px solid black;border-collapse: collapse;'><b><i>Almacen</i></b></td><td width='50%' style='border: 3px solid black;border-collapse: collapse;'><b><i>Producto</i></b></td>")
					.append("<td width='15%' style='border: 3px solid black;border-collapse: collapse;'><b><i>Cantidad</i></b></td><td width='20%' style='border: 3px solid black;border-collapse: collapse;'><b><i>Precio Venta</i></b></td></tr></thead>")
					.append("<tr><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("almacen")).append("</td><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("producto"))
					.append("<td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("cantidad")).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(rs.getDouble("precio_venta")));
				while(rs.next()){
					respuesta.append("</td></tr>")
						.append("<tr><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("almacen")).append("</td><td style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("producto"))
						.append("<td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(rs.getString("cantidad")).append("</td><td align='right' style='border: 3px solid black;border-collapse: collapse;'>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(rs.getDouble("precio_venta")));
				}
				respuesta.append("</td></tr>");
		    }
			con.commit();
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

		return respuesta;
	}

	@SuppressWarnings("unused")
	public static String obtenerTablaPagosCorte(int opcion, String[] fecha, int sel, int evento, int concepto){
		Boolean buscaMes = true;
		String fechaInicial = "";
		String titulo = "CORTE DIARIO VENTA INVENTARIO";
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
        StringBuilder query = new StringBuilder()
        		.append("select t1.id, t2.id id_detalle, t1.id_cliente, DATE_FORMAT(t1.fecha_recibo, '%d/%b/%Y %H:%i') fecha_recibo, t1.nombre_cliente")
        		.append(", t2.id_producto_almacen, t2.id_paquete, sum(t2.cantidad) cantidad, sum(t2.precio_venta) precio_venta ")
        		.append(", (select CONCAT(t10.descripcion,'(',t12.nombre,')') from producto t10, producto_almacen t11, talla t12 where t10.id=t11.id_producto and t11.id=t2.id_producto_almacen and t12.id=t11.id_talla) producto_nombre ")
        		.append(", t1.forma_pago ")
        		.append("from cliente_recibo t1 ")
        		.append("inner join cliente_recibo_detalle t2 on t1.id=t2.id_cliente_recibo ")
        		//.append("inner join cliente t3 on t3.id=t1.id_cliente ")
        		.append("where ");
		if (buscaMes) {
			titulo = "CORTE MENSUAL VENTA INVENTARIO";
			query.append(" DATE_FORMAT(t1.fecha_recibo,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("' ");
		} else {
			query.append(" DATE_FORMAT(t1.fecha_recibo,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("' ");
		}
        query.append("group by t1.id, t2.id, t1.id_cliente, t1.fecha_recibo, t2.id_producto_almacen, t2.id_paquete ")
        		.append("order by t1.id desc, t2.id desc, t2.id_producto_almacen desc, t2.id_paquete desc ");
		Connection con = null;
		System.out.println("q="+query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    Float precioVenta = 0f;
		    Float totalVenta = 0f;
		    int formaPago = 0;
		    int cantidad = 0;
		    int idProducto = 0;
		    int idPaquete = 0;
		    int id2 = 0;
		    int idAnterior2 = -1;
		    int id = 0;
		    int idAnterior = -1;
		    int idCliente = 0;
		    String nombreCliente = "";
		    String fechaRecibo = "";
		    String nombreProducto = "";
		    if (rs.next()){
		    	id = rs.getInt("id");
		    	id2 = rs.getInt("id_detalle");
		    	idCliente = rs.getInt("id_cliente");
		    	formaPago = rs.getInt("forma_pago");
		    	nombreCliente = rs.getString("nombre_cliente");
		    	fechaRecibo = rs.getString("fecha_recibo");
		    	nombreProducto = rs.getString("producto_nombre");
		    	precioVenta = rs.getFloat("precio_venta");
		    	cantidad = rs.getInt("cantidad");
		    	idProducto = rs.getInt("id_producto_almacen");
		    	idPaquete = rs.getInt("id_paquete");
		    	totalVenta = (cantidad * precioVenta);
				result = new StringBuilder();
//					.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Cliente</th><th class='text-left'>Recibo #</th><th class='text-left'>Total Venta</th><th class='text-left'>Fecha Venta</th><th class='text-left' width='120px'>Acci&oacute;n</th></tr></thead>");
		    	result.append("<center><table style='width:100%;height:60px'><tr><td style='text-align:right'><h2>Fecha: "+fechaInicial+"</h2></td></tr>")
			    	.append("<tr style='height:60px'><td style='text-align:center'><h1>"+titulo+"</h1></td></tr>")
			    	.append("<tr><td><center>")
					.append("<table style='width:100%;border-collapse: collapse;' border='3'>")
					.append("<thead><tr><th style='text-align:center'><h3># Recibo</h3></th>")
					.append("<th style='text-align:center'><h3>Nombre del Alumno</h3></th>")
					.append("<th style='text-align:center'><h3>Pago</h3></th>")
					.append("<th style='text-align:center'><h3>Producto / Paquete</h3></th>")
					.append("<th style='text-align:center'><h3>Fecha pago</h3></th>")
					.append("<th style='text-align:center'><h3>Forma de Pago</h3></th>")
					.append("<th style='text-align:center'><h3>Recibi&oacute;</h3></th></tr></thead>");
				while(rs.next()){
					idAnterior = id;
			    	id = rs.getInt("id");
			    	cantidad = rs.getInt("cantidad");
			    	formaPago = rs.getInt("forma_pago");
			    	precioVenta = rs.getFloat("precio_venta");
					if (id == idAnterior){
				    	totalVenta = totalVenta + (cantidad * precioVenta);
					} else {
						result.append("<tr><td>").append(idAnterior).append("</td>")
							.append("<td>").append(nombreCliente).append("</td>")
							.append("<td>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("</td>")
							.append("<td>").append(nombreProducto).append("</td>")
							.append("<td style='text-align:center'>").append(fechaRecibo).append("</td>")
							.append("<td style='text-align:center'>").append(Constantes.sFormaPago[formaPago]).append("</td>")
							.append("<td>").append("").append("</td></tr>");
				    	idCliente = rs.getInt("id_cliente");
				    	nombreCliente = rs.getString("nombre_cliente");
				    	fechaRecibo = rs.getString("fecha_recibo");
				    	nombreProducto = rs.getString("producto_nombre");
				    	idProducto = rs.getInt("id_producto_almacen");
				    	idPaquete = rs.getInt("id_paquete");
				    	totalVenta = (cantidad * precioVenta);
					}
			    	id2 = rs.getInt("id_detalle");
				}
		    } else {
		    	result = new StringBuilder()
				    	.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
		    if (id>0){
				result.append("<tr><td>").append(idAnterior).append("</td>")
				.append("<td>").append(nombreCliente).append("</td>")
				.append("<td>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("</td>")
				.append("<td>").append(nombreProducto).append("</td>")
				.append("<td style='text-align:center'>").append(fechaRecibo).append("</td>")
				.append("<td style='text-align:center'>").append(Constantes.sFormaPago[formaPago]).append("</td>")
				.append("<td>").append("").append("</td></tr>");
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

	@SuppressWarnings("unused")
	public static String obtenerTablaPagosCorte2(int opcion, String[] fecha, int sel, int evento, int concepto){
		Boolean buscaMes = true;
		String fechaInicial = "";
		String titulo = "CORTE DIARIO VENTA INVENTARIO";
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
        StringBuilder query = new StringBuilder()
        		.append("select t1.id, t2.id id_detalle, t1.id_cliente, DATE_FORMAT(t1.fecha_recibo, '%d/%b/%Y %H:%i') fecha_recibo, t1.nombre_cliente")
        		.append(", t2.id_producto_almacen, t2.id_paquete, sum(t2.cantidad) cantidad, sum(t2.precio_venta) precio_venta ")
        		.append(", (select CONCAT(t10.descripcion,'(',t12.nombre,')') from producto t10, producto_almacen t11, talla t12 where t10.id=t11.id_producto and t11.id=t2.id_producto_almacen and t12.id=t11.id_talla) producto_nombre ")
        		.append(", t1.forma_pago ")
        		.append("from cliente_recibo t1 ")
        		.append("inner join cliente_recibo_detalle t2 on t1.id=t2.id_cliente_recibo ")
        		.append("where ");
		if (buscaMes) {
			titulo = "CORTE MENSUAL VENTA INVENTARIO";
			query.append(" DATE_FORMAT(t1.fecha_recibo,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("' ");
		} else {
			query.append(" DATE_FORMAT(t1.fecha_recibo,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("' ");
		}
        query.append("group by t1.id, t2.id, t1.id_cliente, t1.fecha_recibo, t2.id_producto_almacen, t2.id_paquete ")
        		.append("order by t1.id desc, t2.id desc, t2.id_producto_almacen desc, t2.id_paquete desc ");
		Connection con = null;
		System.out.println("q="+query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    Float precioVenta = 0f;
		    Float totalVenta = 0f;
		    int formaPago = 0;
		    int cantidad = 0;
		    int idProducto = 0;
		    int idPaquete = 0;
		    int id2 = 0;
		    int idAnterior2 = -1;
		    int id = 0;
		    int idAnterior = -1;
		    int idCliente = 0;
		    String nombreCliente = "";
		    String fechaRecibo = "";
		    String nombreProducto = "";
		    if (rs.next()){
		    	id = rs.getInt("id");
		    	id2 = rs.getInt("id_detalle");
		    	idCliente = rs.getInt("id_cliente");
		    	formaPago = rs.getInt("forma_pago");
		    	nombreCliente = rs.getString("nombre_cliente");
		    	fechaRecibo = rs.getString("fecha_recibo");
		    	nombreProducto = rs.getString("producto_nombre");
		    	precioVenta = rs.getFloat("precio_venta");
		    	cantidad = rs.getInt("cantidad");
		    	idProducto = rs.getInt("id_producto_almacen");
		    	idPaquete = rs.getInt("id_paquete");
		    	totalVenta = (cantidad * precioVenta);
				result = new StringBuilder();
		    	result.append("<center><table style='width:100%;height:60px'><tr><td style='text-align:right'><h2>Fecha: "+fechaInicial+"</h2></td></tr>")
			    	.append("<tr style='height:60px'><td style='text-align:center'><h1>"+titulo+"</h1></td></tr>")
			    	.append("<tr><td><center>")
					.append("<table style='width:100%;border-collapse: collapse;' border='3'>")
					.append("<thead><tr><th style='text-align:center'><h3># Recibo</h3></th>")
					.append("<th style='text-align:center'><h3>Nombre del Alumno</h3></th>")
					.append("<th style='text-align:center'><h3>Pago</h3></th>")
					.append("<th style='text-align:center'><h3>Producto / Paquete</h3></th>")
					.append("<th style='text-align:center'><h3>Fecha pago</h3></th>")
					.append("<th style='text-align:center'><h3>Forma de Pago</h3></th>")
					.append("<th style='text-align:center'><h3>Recibi&oacute;</h3></th></tr></thead>");
				while(rs.next()){
					idAnterior = id;
			    	id = rs.getInt("id");
			    	cantidad = rs.getInt("cantidad");
			    	formaPago = rs.getInt("forma_pago");
			    	precioVenta = rs.getFloat("precio_venta");
					if (id == idAnterior){
				    	totalVenta = totalVenta + (cantidad * precioVenta);
					} else {
						result.append("<tr><td>").append(String.format("%06d", idAnterior)).append("</td>")
							.append("<td>").append(nombreCliente).append("</td>")
							.append("<td>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("</td>")
							.append("<td>").append(nombreProducto).append("</td>")
							.append("<td style='text-align:center'>").append(fechaRecibo).append("</td>")
							.append("<td style='text-align:center'>").append(obtenerSelectFormaPago(rs.getInt("id"), formaPago)).append("</td>")
							.append("<td>").append("").append("</td></tr>");
				    	idCliente = rs.getInt("id_cliente");
				    	nombreCliente = rs.getString("nombre_cliente");
				    	fechaRecibo = rs.getString("fecha_recibo");
				    	nombreProducto = rs.getString("producto_nombre");
				    	idProducto = rs.getInt("id_producto_almacen");
				    	idPaquete = rs.getInt("id_paquete");
				    	totalVenta = (cantidad * precioVenta);
				    	formaPago = rs.getInt("forma_pago");
					}
			    	id2 = rs.getInt("id_detalle");
				}
		    } else {
		    	result = new StringBuilder()
				    	.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
		    if (id>0){
				result.append("<tr><td>").append(String.format("%06d", id)).append("</td>")
				.append("<td>").append(nombreCliente).append("</td>")
				.append("<td>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("</td>")
				.append("<td>").append(nombreProducto).append("</td>")
				.append("<td style='text-align:center'>").append(fechaRecibo).append("</td>")
				.append("<td style='text-align:center'>").append(obtenerSelectFormaPago(idAnterior, formaPago)).append("</td>")
				.append("<td>").append("").append("</td></tr>");
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

	@SuppressWarnings("unused")
	public static JSONObject obtenerTablaPagosCorte3(int opcion, String[] fecha, int sel, int evento, int concepto){
		Boolean buscaMes = true;
		String fechaInicial = "";
		JSONObject jsonFinal = new JSONObject();
		double total = 0;
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
        StringBuilder query = new StringBuilder()
        		.append("select t1.id, t2.id id_detalle, t1.id_cliente, DATE_FORMAT(t1.fecha_recibo, '%d/%b/%Y %H:%i') fecha_recibo, t1.nombre_cliente, t2.id_producto_almacen, t2.id_paquete, sum(t2.cantidad) cantidad, sum(t2.precio_venta) precio_venta ")
        		.append(", (select CONCAT(t10.descripcion,'(',t12.nombre,')') from producto t10, producto_almacen t11, talla t12 where t10.id=t11.id_producto and t11.id=t2.id_producto_almacen and t12.id=t11.id_talla) producto_nombre")
        		.append(", IFNULL(t1.id_usuario_alta,'') id_usuario_alta, t1.comentario ")
        		.append(", t1.forma_pago ")
        		.append("from cliente_recibo t1 ")
        		.append("inner join cliente_recibo_detalle t2 on t1.id=t2.id_cliente_recibo ")
        		//.append("inner join cliente t3 on t3.id=t1.id_cliente ")
        		.append("where ");
		if (buscaMes) {
			query.append(" DATE_FORMAT(t1.fecha_recibo,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("' ");
		} else {
			query.append(" DATE_FORMAT(t1.fecha_recibo,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("' ");
		}
        query.append("group by t1.id, t2.id, t1.id_cliente, t1.fecha_recibo, t2.id_producto_almacen, t2.id_paquete ")
        	.append("order by t1.fecha_recibo asc, t1.id asc, t2.id desc, t2.id_producto_almacen desc, t2.id_paquete desc ");
		Connection con = null;
		System.out.println("q="+query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    Float precioVenta = 0f;
		    Float totalVenta = 0f;
		    Float totalEfectivo = 0f;
		    int formaPago = 0;
		    int cantidad = 0;
		    int idProducto = 0;
		    int idPaquete = 0;
		    int id2 = 0;
		    int idAnterior2 = -1;
		    int id = 0;
		    int idAnterior = -1;
		    int idCliente = 0;
		    String nombreCliente = "";
		    String fechaRecibo = "";
		    String nombreProducto = "";
		    String usuarioAlta = "";
		    String comentario = "";
		    if (rs.next()){
		    	id = rs.getInt("id");
		    	id2 = rs.getInt("id_detalle");
		    	idCliente = rs.getInt("id_cliente");
		    	nombreCliente = rs.getString("nombre_cliente");
		    	fechaRecibo = rs.getString("fecha_recibo");
		    	nombreProducto = rs.getString("producto_nombre");
		    	usuarioAlta = rs.getString("id_usuario_alta");
		    	precioVenta = rs.getFloat("precio_venta");
		    	cantidad = rs.getInt("cantidad");
		    	idProducto = rs.getInt("id_producto_almacen");
		    	idPaquete = rs.getInt("id_paquete");
		    	comentario = rs.getString("comentario");
		    	formaPago = rs.getInt("forma_pago");
				result = new StringBuilder();
		    	totalVenta = (cantidad * precioVenta);
				//total += totalVenta;
				while(rs.next()){
					idAnterior = id;
			    	id = rs.getInt("id");
			    	cantidad = rs.getInt("cantidad");
			    	precioVenta = rs.getFloat("precio_venta");
					if (id == idAnterior){
				    	totalVenta = totalVenta + (cantidad * precioVenta);
					} else {
						result.append("<tr><td><h4>&nbsp;"+String.format("%06d", idAnterior)+"</h4></td>")
							.append("<td><h4>&nbsp;").append(nombreCliente).append("</h4></td>")
							.append("<td><h4>&nbsp;Venta producto</h4></td>")
							.append("<td><center>").append(fechaRecibo).append("</center></td>")
							.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("&nbsp;</h4></td>")
							.append("<td>&nbsp;").append(comentario).append("</td>")
							.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
							.append("<td><h4>&nbsp;").append(usuarioAlta).append("</h4></td></tr>");
				    	idCliente = rs.getInt("id_cliente");
				    	nombreCliente = rs.getString("nombre_cliente");
				    	fechaRecibo = rs.getString("fecha_recibo");
				    	nombreProducto = rs.getString("producto_nombre");
				    	usuarioAlta = rs.getString("id_usuario_alta");
				    	idProducto = rs.getInt("id_producto_almacen");
				    	idPaquete = rs.getInt("id_paquete");
				    	comentario = rs.getString("comentario");
						total += totalVenta;
				    	if (formaPago!= 1 && formaPago != 4){
				    		totalEfectivo += totalVenta;
				    	}
				    	totalVenta = (cantidad * precioVenta);
					}
			    	id2 = rs.getInt("id_detalle");
			    	formaPago = rs.getInt("forma_pago");
				}
		    }
		    if (id>0){
				total += totalVenta;
		    	if (formaPago!= 1 && formaPago != 4){
		    		totalEfectivo += totalVenta;
		    	}
				result.append("<tr><td><h4>&nbsp;"+String.format("%06d", id)+"</h4></td>")
					.append("<td><h4>&nbsp;").append(nombreCliente).append("</h4></td>")
					.append("<td><h4>&nbsp;Venta producto</h4></td>")
					.append("<td><center>").append(fechaRecibo).append("</center></td>")
					.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("&nbsp;</h4></td>")
					.append("<td>&nbsp;").append(comentario).append("</td>")
					.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
					.append("<td><h4>&nbsp;").append(usuarioAlta).append("</h4></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total Efectivo "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalEfectivo)+"</h2></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)+"</h2></td></tr>");
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

	public static JSONObject obtenerTablaPagosCorte3(HashMap<String,String> params){
		String[] fecha = {};
		int alumno = 0;
		if (params.get("f")!=null) {
			fecha = params.get("f").split(",");
		}
		alumno = getInteger(params.get("a"));
		Boolean buscaMes = true;
		String fechaInicial = "";
		JSONObject jsonFinal = new JSONObject();
		double total = 0;
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
        StringBuilder query = new StringBuilder()
        		.append("select t1.id, t2.id id_detalle, t1.id_cliente, DATE_FORMAT(t1.fecha_recibo, '%d/%b/%Y %H:%i') fecha_recibo, t1.nombre_cliente, t2.id_producto_almacen, t2.id_paquete, sum(t2.cantidad) cantidad, sum(t2.precio_venta) precio_venta ")
        		.append(", (select CONCAT(t10.descripcion,'(',t12.nombre,')') from producto t10, producto_almacen t11, talla t12 where t10.id=t11.id_producto and t11.id=t2.id_producto_almacen and t12.id=t11.id_talla) producto_nombre")
        		.append(", IFNULL(t1.id_usuario_alta,'') id_usuario_alta, t1.comentario ")
        		.append(", t1.forma_pago ")
        		.append("from cliente_recibo t1 ")
        		.append("inner join cliente_recibo_detalle t2 on t1.id=t2.id_cliente_recibo ");
		if (alumno > 0) {
			query.append("inner join cliente t3 on t3.id=t1.id_cliente ");
		}
		query.append("where ");
		if (alumno > 0) {
			query.append(" t3.id= ").append(alumno).append(" ");
		}
        if (fecha.length>0) {
        	if (alumno > 0) {
        		query.append(" and ");
        	}
			if (buscaMes) {
				query.append(" DATE_FORMAT(t1.fecha_recibo,'").append(Constantes.fechaFormato2SQL).append("') = '").append(fechaInicial).append("' ");
			} else {
				query.append(" DATE_FORMAT(t1.fecha_recibo,'").append(Constantes.fechaNacimientoSQL).append("') = '").append(fechaInicial).append("' ");
			}
        }
        query.append("group by t1.id, t2.id, t1.id_cliente, t1.fecha_recibo, t2.id_producto_almacen, t2.id_paquete ")
        	.append("order by t1.fecha_recibo asc, t1.id asc, t2.id desc, t2.id_producto_almacen desc, t2.id_paquete desc ");
		Connection con = null;
		System.out.println("q="+query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    Float precioVenta = 0f;
		    Float totalVenta = 0f;
		    Float totalEfectivo = 0f;
		    int formaPago = 0;
		    int cantidad = 0;
		    int idProducto = 0;
		    int idPaquete = 0;
		    int id2 = 0;
		    int idAnterior2 = -1;
		    int id = 0;
		    int idAnterior = -1;
		    int idCliente = 0;
		    String nombreCliente = "";
		    String fechaRecibo = "";
		    String nombreProducto = "";
		    String usuarioAlta = "";
		    String comentario = "";
		    if (rs.next()){
		    	id = rs.getInt("id");
		    	id2 = rs.getInt("id_detalle");
		    	idCliente = rs.getInt("id_cliente");
		    	nombreCliente = rs.getString("nombre_cliente");
		    	fechaRecibo = rs.getString("fecha_recibo");
		    	nombreProducto = rs.getString("producto_nombre");
		    	usuarioAlta = rs.getString("id_usuario_alta");
		    	precioVenta = rs.getFloat("precio_venta");
		    	cantidad = rs.getInt("cantidad");
		    	idProducto = rs.getInt("id_producto_almacen");
		    	idPaquete = rs.getInt("id_paquete");
		    	comentario = rs.getString("comentario");
		    	formaPago = rs.getInt("forma_pago");
				result = new StringBuilder();
		    	totalVenta = (cantidad * precioVenta);
				//total += totalVenta;
				while(rs.next()){
					idAnterior = id;
			    	id = rs.getInt("id");
			    	cantidad = rs.getInt("cantidad");
			    	precioVenta = rs.getFloat("precio_venta");
					if (id == idAnterior){
				    	totalVenta = totalVenta + (cantidad * precioVenta);
					} else {
						result.append("<tr><td><h4>&nbsp;"+String.format("%06d", idAnterior)+"</h4></td>")
							.append("<td><h4>&nbsp;").append(nombreCliente).append("</h4></td>")
							.append("<td><h4>&nbsp;Venta producto</h4></td>")
							.append("<td><center>").append(fechaRecibo).append("</center></td>")
							.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("&nbsp;</h4></td>")
							.append("<td>&nbsp;").append(comentario).append("</td>")
							.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
							.append("<td><h4>&nbsp;").append(usuarioAlta).append("</h4></td></tr>");
				    	idCliente = rs.getInt("id_cliente");
				    	nombreCliente = rs.getString("nombre_cliente");
				    	fechaRecibo = rs.getString("fecha_recibo");
				    	nombreProducto = rs.getString("producto_nombre");
				    	usuarioAlta = rs.getString("id_usuario_alta");
				    	idProducto = rs.getInt("id_producto_almacen");
				    	idPaquete = rs.getInt("id_paquete");
				    	comentario = rs.getString("comentario");
						total += totalVenta;
				    	if (formaPago!= 1 && formaPago != 4){
				    		totalEfectivo += totalVenta;
				    	}
				    	totalVenta = (cantidad * precioVenta);
					}
			    	id2 = rs.getInt("id_detalle");
			    	formaPago = rs.getInt("forma_pago");
				}
		    }
		    if (id>0){
				total += totalVenta;
		    	if (formaPago!= 1 && formaPago != 4){
		    		totalEfectivo += totalVenta;
		    	}
				result.append("<tr><td><h4>&nbsp;"+String.format("%06d", id)+"</h4></td>")
					.append("<td><h4>&nbsp;").append(nombreCliente).append("</h4></td>")
					.append("<td><h4>&nbsp;Venta producto</h4></td>")
					.append("<td><center>").append(fechaRecibo).append("</center></td>")
					.append("<td align=right><h4>").append(NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalVenta)).append("&nbsp;</h4></td>")
					.append("<td>&nbsp;").append(comentario).append("</td>")
					.append("<td><center>").append(Constantes.sFormaPago[formaPago]).append("</center></td>")
					.append("<td><h4>&nbsp;").append(usuarioAlta).append("</h4></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total Efectivo "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(totalEfectivo)+"</h2></td></tr>");
				result.append("<tr><td colspan='7' style='text-align:right'><h2>Total "+NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total)+"</h2></td></tr>");
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

	public static StringBuilder buscaProductoAlmacen(int id) {
		// TODO Auto-generated method stub
		StringBuilder result = new StringBuilder();
		return result;
	}

	public static int actualizaFormaPago(int formaPago, int id) throws SQLException {
		int resultado = 0;
		StringBuilder query = new StringBuilder()
				.append("update cliente_recibo set ")
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
