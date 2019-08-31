package com.talentos.action;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Evento;
import com.talentos.util.Constantes;

public class eventoAction {

	private static Logger log = Logger.getLogger(eventoAction.class);

	public static String obtenerEventoListadoJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select t1.id, t1.nombre, t1.descripcion ")
				.append("from evento t1 ")
				.append("where t1.estatus = 1 ")
				.append("ORDER BY t1.nombre DESC");
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

	public static int guardarEvento(Evento evento) throws SQLException{
		int resultado = existeRegistro(evento);
		if (resultado < 0){
		    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			StringBuilder query = new StringBuilder("insert into evento(nombre, descripcion, fecha, lugar, presupuesto, numero_dias, comentario) values('")
					.append(evento.getNombre()).append("','")
					.append(evento.getDescripcion()).append("',");
			if (evento.getFecha()!=null)
				query.append("STR_TO_DATE('").append(df.format(evento.getFecha())).append("', '"+Constantes.fechaNacimientoSQL+"'),'");
			else
				query.append("null,'");
			query.append(evento.getLugar()).append("',")
					.append(evento.getPresupuesto()).append(",")
					.append(evento.getNumero_dias()).append(",'")
					.append(evento.getComentario()).append("')");
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
		}

		return resultado;
	}

	public static int existeRegistro(Evento evento){
		int result = -1;
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder("select t1.id from evento t1 where t1.estatus = 1 and (id="+evento.getId()+" or nombre = '").append(evento.getNombre()).append("')");
		log.info("existeRegistro: "+query.toString());
		Connection con = null;
	    try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next() && rs.getInt("id")>0)
		    	result = rs.getInt("id");

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
}
