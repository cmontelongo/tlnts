package com.talentos.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jboss.logging.Logger;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Salon;

public class salonAction {

	private static Logger log = Logger.getLogger(salonAction.class);

	public static String obtenerSalonJson(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t1.id, CONCAT('Salon ',t1.nombre) nombre"
					+" from salon t1";
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
				result += "{id:"+id+",label:\""+rs.getString("nombre")+"\"},";
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

	public static int guardarSalon(Salon salon) throws SQLException {
		int matricula = 1;

		String query = "insert into salon(nombre,estatus) values(";
		query += "'"+salon.getNombre()+"',"
				+"1";
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
			matricula = keys.getInt(1);
			log.info(matricula);
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

		return matricula;
	}

	public static String obtenerTablaSalon(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t1.id,t1.nombre, DATE_FORMAT(t1.fecha_alta,'%d/%m/%Y %H:%i') fecha_alta "
				+"from salon t1 "
				+"where t1.estatus = 1";
		query += " ORDER BY t1.ID DESC LIMIT 5";
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query);
		    rs = pstmt.executeQuery();
		    if (rs.next()){
				result = "<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Nombre</th><th class='text-left'>Fecha Alta</th></tr></thead>";
				result += "<tr><td>"+rs.getString("nombre")+"</td><td>"+
				        rs.getString("fecha_alta")+"</td></tr>";
				while(rs.next()){
					result += "<tr><td>"+rs.getString("nombre")+"</td><td>"+
					        rs.getString("fecha_alta")+"</td></tr>";
				}
		    } else {
		    	result ="<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>";
		    }
			con.commit();
			result += "</table>";
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
