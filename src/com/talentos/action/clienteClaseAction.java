package com.talentos.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.jboss.logging.Logger;

import com.talentos.coneccion.coneccion;

public class clienteClaseAction {

	private static Logger log = Logger.getLogger(clienteClaseAction.class);

	public static String obtenerListado(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select id, nombre from clase where estatus = 1";
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

	public static String obtenerTablaClientes(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t1.id, t1.nombre, t2.nombre maestro, t1.hora_inicio, t1.hora_fin, t1.monto, t1.fecha_alta"
						+" from clase t1, maestro t2"
						+" where t1.id_maestro=t2.id";
		query += " ORDER BY t1.ID DESC LIMIT 5";
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query);
		    rs = pstmt.executeQuery();
		    float monto = 0;
		    NumberFormat formatter = NumberFormat.getCurrencyInstance();
		    DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		    if (rs.next()){
		    	monto = rs.getFloat("monto");
				result = "<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Registro</th><th class='text-left'>Clase</th><th class='text-left'>Instructor</th><th class='text-left'>Horario</th><th class='text-left'>Monto</th><th class='text-left'>Fecha Registro</th></tr></thead>";
				result += "<tr><td>"+rs.getString("id")+"</td><td>"+
				        rs.getString("nombre")+"</td><td>"+
				        rs.getString("maestro")+"</td><td>"+
				        rs.getString("hora_inicio")+"-"+rs.getString("hora_fin")+"</td><td>"+
				        formatter.format(monto)+"</td><td>"+
				        df.format(rs.getDate("fecha_alta"))+"</td></tr>";
				while(rs.next()){
			    	monto = rs.getFloat("monto");
					result += "<tr><td>"+rs.getString("id")+"</td><td>"+
					        rs.getString("nombre")+"</td><td>"+
					        rs.getString("maestro")+"</td><td>"+
					        rs.getString("hora_inicio")+"-"+rs.getString("hora_fin")+"</td><td>"+
					        formatter.format(monto)+"</td><td>"+
					        df.format(rs.getDate("fecha_alta"))+"</td></tr>";
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

	public static String obtenerTablaClienteClase(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t1.id, t2.nombre, t3.nombre nombreClase, t3.monto, t1.beca, t1.fecha_alta"
				+" from cliente_clase t1, cliente t2, clase t3"
				+" where t1.id_cliente=t2.id and t1.id_clase=t3.id and t1.estatus=1";
		query += " ORDER BY ID DESC LIMIT 5";
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query);
		    rs = pstmt.executeQuery();
		    float monto = 0;
		    float beca = 0;
		    NumberFormat formatter = NumberFormat.getCurrencyInstance();
		    NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();
		    DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		    if (rs.next()){
		    	monto = rs.getFloat("monto");
		    	beca = rs.getFloat("beca");
				result = "<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Registro</th><th class='text-left'>Alumno</th><th class='text-left'>Clase</th><th class='text-left'>Monto</th><th class='text-left'>Beca</th><th class='text-left'>Fecha Registro</th></tr></thead>";
				result += "<tr><td>"+rs.getString("id")+"</td><td>"+
				        rs.getString("nombre")+"</td><td>"+
				        rs.getString("nombreClase")+"</td><td>"+
				        formatter.format(monto)+"</td><td>"+
				        formatoPorcentaje.format(beca)+"</td><td>"+
				        df.format(rs.getDate("fecha_alta"))+"</td></tr>";
				while(rs.next()){
			    	monto = rs.getFloat("monto");
			    	beca = rs.getFloat("beca");
					result += "<tr><td>"+rs.getString("id")+"</td><td>"+
					        rs.getString("nombre")+"</td><td>"+
					        rs.getString("nombreClase")+"</td><td>"+
					        formatter.format(monto)+"</td><td>"+
					        formatoPorcentaje.format(beca)+"</td><td>"+
					        df.format(rs.getDate("fecha_alta"))+"</td></tr>";
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
/*
	public static int guardarClienteClase(Cliente_Clase clienteClase) throws SQLException{
		int matricula = 1;

		String query = "insert into cliente_clase(id_cliente, id_clase, beca, estatus, comentario) values(";
		query += clienteClase.getId_cliente()+","
				+clienteClase.getId_clase()+","
				+clienteClase.getBeca()+","
				+"1,"
				+"'"+clienteClase.getComentario()+"'";
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
*/
}
