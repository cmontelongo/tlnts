package com.talentos.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.jboss.logging.Logger;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Clase;

public class claseAction {

	private static Logger log = Logger.getLogger(claseAction.class);

	public static String obtenerClaseGrupoJson(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t1.id,t1.nombre,t5.monto,t1.hora_inicio,t1.hora_fin,t1.id_maestro,t2.nombre maestro_nombre, t3.nombre salon_nombre, t4.nombre grupo_nombre, t1.dia_semana"
					+" from clase t1, maestro t2, salon t3, grupo t4, grupo_costo t5"
				    +" where t1.id_maestro=t2.id and t2.estatus=1 and t3.id=t1.id_salon and t4.id=t1.id_grupo and t5.id_grupo=t4.id";
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
				int iDiaSemana = rs.getInt("dia_semana");
				result += "{id:"+id+",label:\""+rs.getString("nombre")+" ("+rs.getString("grupo_nombre")+"-"+rs.getString("maestro_nombre")+")\",monto:"+rs.getString("monto")+",maestro:\""+rs.getString("maestro_nombre")+"\",hInicio:\""+rs.getString("hora_inicio")+"\",hFin:\""+rs.getString("hora_fin")+"\",diaS:"+iDiaSemana+"},";
			}
			con.commit();
			if (result.length()>2){
				result = result.substring(0, result.length()-1);
			}
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

	public static String obtenerClaseJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id,t1.nombre")
				.append(" from clase t1")
				.append(" where t1.estatus=1")
				.append(" ORDER BY t1.ID DESC");
		Connection con = null;
		System.out.println(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			result.append("[");
			while(rs.next()){
				String id = rs.getString("id");
				result.append("{id:").append(id).append(",label:\"").append(rs.getString("nombre")).append("\"},");
			}
			if (result.length()>2){
				result = new StringBuilder(result.substring(0, result.length()-1));
			}
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

	public static List<Clase> obtenerClase(){
		List<Clase> clase = new ArrayList<Clase>();
		ResultSet rs = null;
		try{
			rs = coneccion.ejecutar("select * from clase");
			while(rs.next()){
				Clase c = new Clase();
				c.setId(rs.getInt("id"));
				c.setNombre(rs.getString("nombre"));
				clase.add(c);
			}
		} catch (NamingException e) {
			log.error(e.getMessage());
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return clase;
	}

	public static int guardarClase(Clase clase) throws SQLException{
		int idclase = 1;

		StringBuilder query = new StringBuilder();
		query.append("insert into clase(nombre,comentario) values(")
				.append("'"+clase.getNombre()+"',")
				.append("'"+clase.getComentario()+"',")
				.append(clase.getEstatus())
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
			idclase = keys.getInt(1);
			log.info(idclase);
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			 try{
		           if(con != null){
		        	   con.commit();
		        	   con.close();
		           }
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return idclase;
	}

	public static String obtenerTablaClase(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id,t1.nombre,t1.comentario,t1.fecha_alta")
				.append(" from clase t1")
			    .append(" where t1.estatus=1")
			    .append(" ORDER BY t1.ID DESC");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		    if (rs.next()){
				result.append("<table style='width:90%' class='table-fill' border=0><thead><tr><th class='text-left'>Clase</th><th class='text-left'>Comentario</th><th class='text-left'>Fecha Registro</th></tr></thead>")
						.append("<tr><td>").append(rs.getString("nombre")).append("</td><td>")
				        .append(rs.getString("comentario")).append("</td><td>")
				        .append(df.format(rs.getDate("fecha_alta"))).append("</td></tr>");
				while(rs.next()){
					result.append("<tr><td>").append(rs.getString("nombre")).append("</td><td>")
				        .append(rs.getString("comentario")).append("</td><td>")
				        .append(df.format(rs.getDate("fecha_alta"))).append("</td></tr>");
				}
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

}
