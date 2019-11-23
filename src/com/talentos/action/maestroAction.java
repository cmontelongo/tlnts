package com.talentos.action;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Maestro;
import com.talentos.util.Constantes;

public class maestroAction {

	private static Logger log = Logger.getLogger(maestroAction.class);

	public static String obtenerMaestroJson(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select id,nombre from maestro where estatus=1";
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

	public static int guardarMaestro(Maestro maestro) throws SQLException {
		int matricula = 1;

		String query = "insert into maestro(nombre,estatus) values(";
		query += "'"+maestro.getNombre()+"',"
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

	public static String obtenerTablaMaestro(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t1.id,t1.nombre, DATE_FORMAT(t1.fecha_alta,'%d/%m/%Y %H:%i') fecha_alta "
				+"from maestro t1 "
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

	public static String obtenerTablaGrupo(int[] parametros){
		StringBuilder result = new StringBuilder();

		if (parametros[0] == 1){
			result = obtenerTablaGrupoMaestro(parametros[1]);
		}

		return result.toString();
	}

	private static StringBuilder obtenerTablaGrupoMaestro(int maestro_id) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
			.append("select t1.id maestro_id, t1.nombre maestro_nombre, t3.id clase_id, t3.nombre clase_nombre, t4.id grupo_id, CONCAT('Grupo ',t4.nombre) grupo_nombre, t2.dia_semana, t2.id_salon, t2.hora_inicio, t2.hora_fin")
			.append(" from maestro t1")
			.append(" inner join grupo_clase t2 on t1.id=t2.id_maestro")
			.append(" inner join clase t3 on t2.id_clase=t3.id")
			.append(" inner join grupo t4 on t2.id_grupo=t4.id")
			.append(" where t1.id=").append(maestro_id);
		Connection con = null;
		System.out.println(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    result.append("<center>");
		    if (rs.next()){
				result.append("<table style='width:80%' class='table-fill' border=0 cellspacing=3 cellpadding=5><thead><tr><th class='text-left'>Grupo</th><th class='text-left'>Clase</th><th class='text-left'>Dia</th><th class='text-left'>Horario</th><th class='text-left'>Accion</th></tr></thead>")
					.append("<tr><td>"+rs.getString("grupo_nombre")+"</td><td align='center'>"+rs.getString("clase_nombre")+"</td><td align='center'>"+Constantes.sDiaSemana[rs.getInt("dia_semana")]+"</td><td align='center'>"+rs.getString("hora_inicio")+"-"+rs.getString("hora_fin")+"</td><td align='center'>Eliminar</td></tr>");
				while(rs.next()){
					result.append("<tr><td>"+rs.getString("grupo_nombre")+"</td><td align='center'>"+rs.getString("clase_nombre")+"</td><td align='center'>"+Constantes.sDiaSemana[rs.getInt("dia_semana")]+"</td><td align='center'>"+rs.getString("hora_inicio")+"-"+rs.getString("hora_fin")+"</td><td align='center'>Eliminar</td></tr>");
				}
		    } else {
		    	result.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
			result.append("</center></table>");
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

	public static String obtenerMaestroJsonTable(String maestroId, int numRegistros) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select m.id, m.nombre, m.estatus, count(gc.id) numero_clases ")
				.append("from maestro m ")
				.append("inner join grupo_clase gc on m.id=gc.id_maestro ");
		StringBuilder cliente = new StringBuilder(" where id = ").append(maestroId);
		StringBuilder registros = new StringBuilder(" LIMIT ").append(numRegistros).append(" OFFSET ").append(numRegistros);
		if (maestroId!= null && !maestroId.equals("")) {
			query.append(cliente.toString());
		}
		query.append("group by m.id, m.nombre ")
			.append("order by m.nombre");
		if (numRegistros > 0){
			query.append(registros.toString());
		}
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			JSONObject jsonFinal = new JSONObject();
		    JSONArray jsonArray = new JSONArray();
		    StringWriter out = new StringWriter();
			StringBuilder estatus = new StringBuilder("NO");
			while(rs.next()){
				jsonFinal.put("id",rs.getString("id"));
				jsonFinal.put("nombre",rs.getString("nombre"));
				if (rs.getBoolean("estatus")){
					estatus = new StringBuilder("SI");
				} else {
					estatus = new StringBuilder("NO");
				}
				jsonFinal.put("estatus",estatus.toString());
				jsonFinal.put("clases", rs.getInt("numero_clases"));
				jsonArray.put(jsonFinal);
			    jsonFinal = new JSONObject();
			}

			jsonArray.write(out);
			String jsonText = out.toString();
		    System.out.println(jsonText);
		    
		    result = new StringBuilder(jsonText);

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

	public static String obtenerMaestroTable(String id) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select id, matricula,nombre,DATE_FORMAT(fecha_nacimiento, '%d/%m/%Y') fecha_nacimiento,calle,numero,colonia,municipio,codigo_postal,")
				.append("madre_nombre,madre_ocupacion,madre_telefono_casa,madre_telefono_celular,madre_telefono_oficina,madre_email,madre_telefono_recado,")
				.append("padre_nombre,padre_ocupacion,padre_telefono_celular,padre_telefono_oficina,padre_email,estatus")
				.append(" FROM cliente WHERE matricula = ").append(id);
		query.append(" ORDER BY ID DESC");
		log.info(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	String checked = "";
		    	if (rs.getBoolean("estatus"))
		    		checked = "checked";
				result.append("<table style='width:100%' class='table-fill' border=0>")
					.append("<tr><td colspan='2'>Matricula:<br /><input type='text' id='matricula' size='60' value='"+String.format("%1$05d",Integer.parseInt(rs.getString("matricula")))+"' /><input type='hidden' id='id' value='").append(rs.getString("id")).append("' /><input type='hidden' id='matricula2' value='").append(rs.getString("matricula")).append("' /></td>")
					.append("<td>Activo: <input id='estatus' type='checkbox' ").append(checked).append(" /></td></tr>")
					.append("<tr><td colspan='2'>Nombre:<br /><input type='text' id='nombre' size='60' value='"+rs.getString("nombre")+"' /></td>")
					.append("<td>Fecha Nacimiento: <input id='fechaNacimiento' name='fechaNacimiento' class='date-picker' value='"+rs.getString("fecha_nacimiento")+"' /></td></tr>")
					.append("<tr><td colspan='3'><hr></td></tr>")
					.append("<tr><td colspan=2>Calle:<br/><input type='text' id='calle' size='60' value='"+rs.getString("calle")+"'/></td><td>Numero:<br /><input type='text' id='numero' size='10' value='"+rs.getString("numero")+"'/></td></tr>")
					.append("<tr><td colspan=3>Colonia:<br /><input type='text' id='colonia' size='80' value='"+rs.getString("colonia")+"' /></td></tr>")
					.append("<tr><td>Municipio:<br /><input type='text' id='municipio' value='"+rs.getString("municipio")+"' /></td><td>Codigo Postal:<br /><input type='text' id='codigoPostal' size='10' value='"+rs.getString("codigo_postal")+"' /></td></tr>")
					.append("<tr><td colspan='3'><hr></td></tr>")
					.append("<tr><td>Madre:<br /><input type='text' id='madreNombre' size='30' value='"+rs.getString("madre_nombre")+"' /></td>")
					.append("<td>Email:<br /><input type='text' id='madreEmail' size='30' value='"+rs.getString("madre_email")+"' /></td>")
					.append("<td>Celular:<br /><input type='text' id='madreTelefonoCelular' size='30' value='"+rs.getString("madre_telefono_celular")+"' /></td></tr><tr>")
					.append("<tr><td>Tel Casa:<br /><input type='text' id='madreTelefonoCasa' size='30' value='"+rs.getString("madre_telefono_casa")+"' /></td>")
					.append("<td>Tel oficina:<br /><input type='text' id='madreTelefonoOficina' size='30' value='"+rs.getString("madre_telefono_oficina")+"' /></td>")
					.append("<td>Tel recado:<br /><input type='text' id='madreTelefonoRecado' size='30' value='"+rs.getString("madre_telefono_recado")+"' /></td></tr>")
					.append("<tr><td>Ocupacion:<br /><input type='text' id='madreOcupacion' size='30' value='"+rs.getString("madre_ocupacion")+"' /></td></tr>")
					.append("<tr><td colspan='3'><hr></td></tr>")
					.append("<tr><td>Padre:<br /><input type='text' id='padreNombre' size='30' value='"+rs.getString("padre_nombre")+"' /></td>")
					.append("<td>Email:<br /><input type='text' id='padreEmail' size='30' value='"+rs.getString("padre_email")+"' /></td>")
					.append("<td>Ocupacion:<br /><input type='text' id='padreOcupacion' size='30' value='"+rs.getString("padre_ocupacion")+"' /></td></tr>")
					.append("<tr><td>Celular:<br /><input type='text' id='padreTelefonoCelular' size='30' value='"+rs.getString("padre_telefono_celular")+"' /></td>")
					.append("<td>Tel oficina:<br /><input type='text' id='padreTelefonoOficina' size='30' value='"+rs.getString("padre_telefono_oficina")+"' /></td></tr>");
		    } else {
		    	result.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
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
