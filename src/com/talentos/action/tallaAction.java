package com.talentos.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jboss.logging.Logger;

import com.talentos.coneccion.coneccion;

public class tallaAction {

	private static Logger log = Logger.getLogger(tallaAction.class);

	public static String obtenerTallaJson(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select id,nombre,descripcion from talla";
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
				result += "{id:"+id+",label:\""+rs.getString("descripcion")+" ("+rs.getString("nombre")+")\"},";
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

}
