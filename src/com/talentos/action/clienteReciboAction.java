package com.talentos.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jboss.logging.Logger;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Cliente_Recibo;
import com.talentos.entidad.Cliente_Recibo_Detalle;
import com.talentos.entidad.Producto_Inventario;

public class clienteReciboAction {

	private static Logger log = Logger.getLogger(clienteReciboAction.class);

	public static int guardarClienteRecibo(Cliente_Recibo clienteRecibo) throws SQLException {
		int respuesta = 1;

		StringBuilder query = new StringBuilder()
				.append("insert into cliente_recibo(id_cliente,nombre_cliente,monto,comentario) values(")
				.append(clienteRecibo.getId_cliente()).append(",'")
				.append(clienteRecibo.getNombre_cliente()).append("',")
				.append("0.0,")
				.append("'").append(clienteRecibo.getComentario()).append("'")
				.append(")");
		log.info(query);
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

	public static void actualizaClienteRecibo(Cliente_Recibo clienteRecibo) throws SQLException{
		StringBuilder query = new StringBuilder()
				.append("update cliente_recibo")
				.append(" set monto=").append(clienteRecibo.getMonto())
				.append(", comentario='").append(clienteRecibo.getComentario()).append("'")
				.append(" where id=").append(clienteRecibo.getId())
				.append(" and id_cliente=").append(clienteRecibo.getId_cliente());
		log.info(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString());
			pstmt.executeUpdate();  
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
	}

	public static int guardarClienteReciboDetalle(Cliente_Recibo_Detalle clienteReciboDetalle) throws SQLException {
		int respuesta = 1;

		Producto_Inventario pi = new Producto_Inventario();
		if (clienteReciboDetalle.getId_producto_almacen()>0){
			pi.setId_producto_almacen(clienteReciboDetalle.getId_producto_almacen());
			pi.setCantidad(clienteReciboDetalle.getCantidad());
			if (productoAction.actualizarProductoInventario(pi, true)){
				StringBuilder query = new StringBuilder()
						.append("insert into cliente_recibo_detalle(id_cliente_recibo,id_producto_almacen,id_paquete,cantidad,precio_venta) values(")
						.append(clienteReciboDetalle.getId_cliente_recibo()).append(",")
						.append(clienteReciboDetalle.getId_producto_almacen()).append(",")
						.append(clienteReciboDetalle.getId_paquete()).append(",")
						.append(clienteReciboDetalle.getCantidad()).append(",")
						.append(clienteReciboDetalle.getPrecio_venta())
						.append(")");
				log.info(query);
				Connection con = null;
				try {
					con = coneccion.getConnection();
					con.setAutoCommit(true);
					PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
					pstmt.executeUpdate();  
					ResultSet keys = pstmt.getGeneratedKeys();    
					keys.next();  
					respuesta = keys.getInt(1);
					log.info(respuesta);
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
		}

		return respuesta;
	}
}
