package com.talentos.action;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Almacen;
import com.talentos.entidad.Producto_Almacen;

public class almacenAction {

	private static Logger log = Logger.getLogger(almacenAction.class);

	@SuppressWarnings("unused")
	public static String obtenerProductoAlmacenJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select t3.id, CONCAT(t1.descripcion, ' (',t2.nombre,')') producto_descripcion, t3.id id_almacen, t2.nombre talla_nombre, t2.descripcion talla_descripcion, t4.precio_venta_ultima precio_venta, t4.cantidad ")
				.append("from producto t1 ")
				.append("inner join producto_almacen t3 on t1.id=t3.id_producto ")
				.append("inner join talla t2 on t2.id=t3.id_talla ")
				.append("inner join producto_inventario t4 on t4.id_producto_almacen=t3.id ")
				.append("where t3.id_almacen=1 and t4.cantidad>0 ")
				.append("ORDER BY t1.id DESC");
		Connection con = null;
	    NumberFormat formatter = NumberFormat.getCurrencyInstance();
	    Float venta;
	    int cantidad = 0;
	    String id;
	    System.out.println("obtenerProductoAlmacenJson: "+query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			JSONObject jsonFinal = new JSONObject();
		    JSONArray jsonArray = new JSONArray();
		    StringWriter out = new StringWriter();
			while(rs.next()){
				id = rs.getString("id");
				venta = rs.getFloat("precio_venta");
				cantidad = rs.getInt("cantidad");
				jsonFinal.put("id", rs.getString("id"));
				jsonFinal.put("label", rs.getString("producto_descripcion"));
				jsonFinal.put("idA", rs.getString("id_almacen"));
				jsonFinal.put("v", NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(venta));
				jsonFinal.put("vM", venta);
				jsonFinal.put("c", cantidad);
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

	public static String obtenerAlmacenJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t1.id,t1.nombre"
				+" from almacen t1";
		query += " ORDER BY t1.ID DESC";
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query);
		    rs = pstmt.executeQuery();
			JSONObject jsonFinal = new JSONObject();
		    JSONArray jsonArray = new JSONArray();
		    StringWriter out = new StringWriter();
			while(rs.next()){
				jsonFinal.put("id", rs.getString("id"));
				jsonFinal.put("label", rs.getString("nombre"));
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

	public static String obtenerAlmacenProductoJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
		ResultSet rsP = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtP = null;
        StringBuilder queryP = new StringBuilder();
		String query = "select t1.id,t1.nombre"
				+" from almacen t1";
		query += " ORDER BY t1.ID DESC";
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query);
		    rs = pstmt.executeQuery();
			result.append("[");
			int id = 0;
			while(rs.next()){
				id = Integer.parseInt(rs.getString("id"));
				result.append("{id:").append(id).append(",label:\"").append(rs.getString("nombre")).append("\",productos:[");
				queryP = new StringBuilder()
						.append("select t1.id,t1.nombre ")
						.append("from producto t1 ")
						.append("inner join producto_almacen t2 on t1.id=t2.id_producto ")
						.append("where t2.id_almacen=").append(id);
			    pstmtP = con.prepareStatement(queryP.toString());
			    rsP = pstmtP.executeQuery();
				result.append("[");
				int idP = 0;
				while(rsP.next()){
					idP = Integer.parseInt(rsP.getString("id"));
					result.append("{id:").append(idP).append(",label:\"").append(rsP.getString("nombre")).append("\"},");
				}
				result.append(result.toString().substring(0, result.length()-1))
					.append("]},");
			}
			con.commit();
			result.append(result.toString().substring(0, result.length()-1))
				.append("]");
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

	public static String obtenerUltimosProductoAlmacen(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder()
        		.append("select t2.id, CONCAT(t1.descripcion,' (',t4.nombre,')') producto_descripcion, t3.nombre almacen_nombre, t4.descripcion ") //, t5.cantidad, t5.precio_compra, t5.precio_venta ")
        		.append("from producto t1 ")
        		.append("inner join producto_almacen t2 on t2.id_producto=t1.id  ")
        		.append("inner join almacen t3 on t2.id_almacen=t3.id ")
        		.append("inner join talla t4 on t4.id=t2.id_talla ")
        		//.append("inner join producto_almacen_inventario t5 on t5.id_producto_almacen=t2.id ")
        		.append("ORDER BY t2.ID DESC");
		Connection con = null;
		log.info(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
//		    Float precioCompra;
//		    Float precioVenta;
//		    NumberFormat formatter = NumberFormat.getCurrencyInstance();
		    if (rs.next()){
//		    	precioCompra = rs.getFloat("precio_compra");
//		    	precioVenta = rs.getFloat("precio_venta");
				result = new StringBuilder()
					.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Almacen</th><th class='text-left'>Producto</th></tr></thead>")
					.append("<tr><td>").append(rs.getString("almacen_nombre")).append("</td><td>").append(rs.getString("producto_descripcion")).append("</td></tr>");
				while(rs.next()){
//			    	precioCompra = rs.getFloat("precio_compra");
//			    	precioVenta = rs.getFloat("precio_venta");
					result.append("<tr><td>").append(rs.getString("almacen_nombre")).append("</td><td>").append(rs.getString("producto_descripcion")).append("</td></tr>");
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

	public static String obtenerUltimosAlmacen(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder()
        		.append("select t1.id,t1.clave,t1.nombre")
        		.append(" from almacen t1")
        		.append(" ORDER BY t1.ID DESC");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
				result = new StringBuilder()
					.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Clave</th><th class='text-left'>Nombre</th></tr></thead>")
					.append("<tr><td>").append(rs.getString("clave")).append("</td><td>").append(rs.getString("nombre")).append("</td></tr>");
				while(rs.next()){
					result.append("<tr><td>").append(rs.getString("clave")).append("</td><td>").append(rs.getString("nombre")).append("</td></tr>");
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

	public static int guardarAlmacen(Almacen almacen) throws SQLException{
		int respuesta = 1;

		String query = "insert into almacen(clave,descripcion,nombre,comentario) values(";
		query += "'"+almacen.getClave()+"',"
				+"'"+almacen.getDescripcion()+"',"
				+"'"+almacen.getNombre()+"',"
				+"'"+almacen.getComentario()+"'";
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

		String query = "";
		if (productoAlmacen.getId()>0){
			query = "update producto_almacen set"
					+" id_producto="+productoAlmacen.getId_producto()
					+" ,fecha_ultima_modificacion='"+productoAlmacen.getFecha_alta()+"'"
					+" where id="+productoAlmacen.getId();
		} else {
			query = "insert into producto_almacen(id_producto,id_almacen,comentario) values(";
			query += productoAlmacen.getId_producto()+","
					+productoAlmacen.getId_almacen()+","
					+"'"+productoAlmacen.getComentario()+"'";
			query += ")";
		};
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

	public static String obtenerUltimosProductoAlmacenInv(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder()
        		.append("select t2.id, CONCAT(t1.descripcion,' (',t4.nombre,')') producto_descripcion, t3.nombre almacen_nombre, t4.descripcion, t5.cantidad, t5.precio_compra, t5.precio_venta ")
        		.append("from producto t1 ")
        		.append("inner join producto_almacen t2 on t2.id_producto=t1.id  ")
        		.append("inner join almacen t3 on t2.id_almacen=t3.id ")
        		.append("inner join talla t4 on t4.id=t2.id_talla ")
        		.append("inner join producto_almacen_inventario t5 on t5.id_producto_almacen=t2.id ")
        		.append("ORDER BY t5.ID DESC");
		Connection con = null;
		log.info(query.toString());
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
					.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Almacen</th><th class='text-left'>Producto</th><th class='text-left'>Cantidad</th><th class='text-left'>Precio Compra</th><th class='text-left'>Precio Venta</th></tr></thead>")
					.append("<tr><td>").append(rs.getString("almacen_nombre")).append("</td><td>").append(rs.getString("producto_descripcion")).append("</td><td>").append(rs.getString("cantidad")).append("</td><td>").append(precioCompra).append("</td><td>").append(precioVenta).append("</td></tr>");
				while(rs.next()){
			    	precioCompra = rs.getFloat("precio_compra");
			    	precioVenta = rs.getFloat("precio_venta");
					result.append("<tr><td>").append(rs.getString("almacen_nombre")).append("</td><td>").append(rs.getString("producto_descripcion")).append("</td><td>").append(rs.getString("cantidad")).append("</td><td>").append(precioCompra).append("</td><td>").append(precioVenta).append("</td></tr>");
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

}
