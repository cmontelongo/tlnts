package com.talentos.action;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Grupo;
import com.talentos.entidad.Grupo_Clase;
import com.talentos.entidad.Grupo_Cliente;
import com.talentos.entidad.Grupo_Costo;
import com.talentos.util.Constantes;
import com.talentos.util.Utilerias;

public class grupoAction {

	private static Logger log = Logger.getLogger(grupoAction.class);

	public static String obtenerGrupoJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id,CONCAT('Grupo ',t1.nombre) nombre, t1.comentario, t2.monto")
			.append(" from grupo t1, grupo_costo t2")
			.append(" where t1.id=t2.id_grupo")
		    .append(" ORDER BY t1.ID DESC");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			result.append("[");
			Float monto = (float) 0;
		    Locale locale = new Locale("en","US");
		    NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
			while(rs.next()){
				String id = rs.getString("id");
				monto = Float.parseFloat(rs.getString("monto"));
				result.append("{id:").append(id).append(",label:\"").append(rs.getString("nombre")).append("\",monto:\"").append(formatter.format(monto)).append("\",comentario:\"").append(rs.getString("comentario")).append("\"},");
			}
			con.commit();
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

	public static String obtenerGrupoHorarioJson(){
		StringBuilder sbResultado = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		Connection con = null;
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("select DISTINCT")
				.append(" t1.id,CONCAT('Grupo ',t1.nombre) grupo_nombre, t6.dia_semana, t5.monto, IFNULL(t6.comentario,'') comentario")
				.append(" from grupo t1")
				.append(" inner join grupo_clase t6 on t1.id=t6.id_grupo")
				.append(" inner join  grupo_costo t5 on t1.id=t5.id_grupo")
				.append(" where t1.estatus=1")
				.append(" order by t1.id asc, t6.dia_semana asc");
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(sbQuery.toString());
		    rs = pstmt.executeQuery();
			sbResultado.append("[");
			int id = 0;
			int idAnterior = -1;
			int diaActual = 0;
			String dias = "";
			String grupoNombre = "";
			String comentario = "";
			String monto = "";
			while(rs.next()){
				id = rs.getInt("id");
				diaActual = rs.getInt("dia_semana");
				if (id==idAnterior){
					dias += ","+Constantes.sDiaSemana[diaActual];
				} else {
					if (idAnterior>0){
						sbResultado.append("{id:").append(idAnterior).append(",label:\"").append(grupoNombre).append(" (").append(dias).append(")").append("\",comentario:\"").append(comentario).append("\",monto:").append(monto).append(",diaS:\"").append(dias).append("\"},");
					}
					grupoNombre = rs.getString("grupo_nombre");
					comentario = rs.getString("comentario");
					monto = rs.getString("monto");
					dias = ""+Constantes.sDiaSemana[diaActual];
				}
				idAnterior = id;
			}
			if (id>0)
				sbResultado.append("{id:").append(idAnterior).append(",label:\"").append(grupoNombre).append(" (").append(dias).append(")").append("\",comentario:\"").append(comentario).append("\",monto:").append(monto).append(",diaS:\"").append(dias).append("\"},");
			con.commit();
			if (sbResultado.length()>2){
				sbResultado = new StringBuilder(sbResultado.substring(0, sbResultado.length()-1));
			}
			sbResultado.append("]");
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

		return sbResultado.toString();
	}

	public static int guardarGrupo(Grupo grupo) throws SQLException {
		int resultado = 1;

		StringBuilder query = new StringBuilder()
				.append("insert into grupo(nombre,comentario,estatus,particular) values(")
				.append("'").append(grupo.getNombre()).append("',")
				.append("'").append(grupo.getComentario()).append("',")
				.append("1,")
				.append(grupo.getParticular())
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
			resultado = keys.getInt(1);
			log.info(resultado);
			con.commit();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			con.rollback();
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

	public static String obtenerTablaGrupo(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id,CONCAT('Grupo ',t1.nombre) nombre,t1.comentario, DATE_FORMAT(t1.fecha_alta,'%d/%m/%Y %H:%i') fecha_alta, t2.monto, t1.particular ")
			.append(" from grupo t1, grupo_costo t2 ")
			.append("where t1.estatus = 1 and t2.id_grupo=t1.id")
			.append(" ORDER BY t1.ID DESC LIMIT 5");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    String esParticular = "NO";
		    if (rs.next()){
		    	if (rs.getInt("particular")==1)
		    		esParticular = "SI";
				result.append("<table style='width:100%' class='table-fill'><thead><tr><th class='text-left'>Nombre</th><th class='text-left'>Particular</th><th class='text-left'>Monto</th><th class='text-left'>Comentario</th><th class='text-left'>Fecha Alta</th></tr></thead>")
						.append("<tr><td>").append(rs.getString("nombre")).append("</td><td>")
						.append(esParticular).append("</td><td>")
						.append(rs.getString("monto")).append("</td><td>")
						.append(rs.getString("comentario")).append("</td><td>")
				        .append(rs.getString("fecha_alta")).append("</td></tr>");
				while(rs.next()){
			    	if (rs.getInt("particular")==1)
			    		esParticular = "SI";
			    	else
			    		esParticular = "NO";
					result.append("<tr><td>").append(rs.getString("nombre")).append("</td><td>")
							.append(esParticular).append("</td><td>")
							.append(rs.getString("monto")).append("</td><td>")
							.append(rs.getString("comentario")).append("</td><td>")
					        .append(rs.getString("fecha_alta")).append("</td></tr>");
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

	public static int guardarGrupoCosto(Grupo_Costo grupoCosto) throws SQLException {
		int resultado = 1;

		StringBuilder query = new StringBuilder();
		query.append("insert into grupo_costo(id_grupo,monto) values(")
				.append(grupoCosto.getId_grupo()).append(",")
				.append(grupoCosto.getMonto())
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
			resultado = keys.getInt(1);
			log.info(resultado);
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

		return resultado;
	}

	public static String obtenerTablaClaseGrupo(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id,t1.nombre,(SELECT t5.monto FROM grupo_costo t5 WHERE t4.id=t5.id_grupo) monto,")
				.append(" TIME_FORMAT(t6.hora_inicio, '").append(Constantes.horaFormatoSQL2).append("')hora_inicio,") 
				.append(" TIME_FORMAT(t6.hora_fin, '").append(Constantes.horaFormatoSQL2).append("') hora_fin,")
				.append(" t2.id maestro_id,t2.nombre maestro_nombre, CONCAT('Salon ',t3.nombre) salon_nombre, CONCAT('Grupo ',t4.nombre) grupo_nombre, t6.dia_semana, t6.fecha_alta, t4.particular")
				.append(" from clase t1, maestro t2, salon t3, grupo t4, grupo_clase t6")
				.append(" where t6.id_maestro=t2.id and t2.estatus=1 and t3.id=t6.id_salon and t4.id=t6.id_grupo")
				.append(" and t6.id_clase=t1.id and t6.id_grupo=t4.id")
				.append(" ORDER BY t6.ID DESC");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    float monto = 0;
		    Locale locale = new Locale("en","US");
		    NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		    DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		    String esParticular = "NO";
		    if (rs.next()){
		    	if (rs.getInt("particular")==1)
		    		esParticular = "SI";
		    	monto = rs.getFloat("monto");
				result.append("<table style='width:90%' class='table-fill' border=0><thead><tr><th class='text-left'>Grupo</th><th class='text-left'>Salon</th><th class='text-left'>Clase</th><th class='text-left'>Particular</th><th class='text-left'>Instructor</th><th class='text-left'>Horario</th><th class='text-left'>Dia semana</th><th class='text-left'>Monto</th><th class='text-left'>Fecha Registro</th></tr></thead>")
						.append("<tr><td>").append(rs.getString("grupo_nombre")).append("</td><td>")
						.append(rs.getString("salon_nombre")).append("</td><td>")
						.append(rs.getString("nombre")).append("</td><td>")
						.append(esParticular).append("</td><td>")
						.append(rs.getString("maestro_nombre")).append("</td><td>")
						.append(rs.getString("hora_inicio")).append(" / ").append(rs.getString("hora_fin")).append("</td><td>")
						.append(Constantes.sDiaSemana[rs.getInt("dia_semana")]).append("</td><td>")
						.append(formatter.format(monto)).append("</td><td>")
						.append(df.format(rs.getDate("fecha_alta"))).append("</td></tr>");
				while(rs.next()){
			    	if (rs.getInt("particular")==1)
			    		esParticular = "SI";
			    	else
			    		esParticular = "NO";
			    	monto = rs.getFloat("monto");
					result.append("<tr><td>").append(rs.getString("grupo_nombre")).append("</td><td>")
							.append(rs.getString("salon_nombre")).append("</td><td>")
							.append(rs.getString("nombre")).append("</td><td>")
							.append(esParticular).append("</td><td>")
							.append(rs.getString("maestro_nombre")).append("</td><td>")
							.append(rs.getString("hora_inicio")).append(" / ").append(rs.getString("hora_fin")).append("</td><td>")
							.append(Constantes.sDiaSemana[rs.getInt("dia_semana")]).append("</td><td align='right'>")
							.append(formatter.format(monto)).append("</td><td>")
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

	public static int guardarGrupoClase(Grupo_Clase grupoClase) throws SQLException {
		int resultado = 1;
//		SimpleDateFormat formatoFecha = new SimpleDateFormat(Constantes.horaFormato);

		StringBuilder query = new StringBuilder();
		query.append("insert into grupo_clase(nombre,id_grupo,id_clase,id_salon,dia_semana,id_maestro,hora_inicio,hora_fin,descripcion,comentario,estatus) values(")
				.append("'").append(grupoClase.getNombre()).append("',")
				.append(grupoClase.getId_grupo()).append(",")
				.append(grupoClase.getId_clase()).append(",")
				.append(grupoClase.getId_salon()).append(",")
				.append(grupoClase.getDia_semana()).append(",")
				.append(grupoClase.getId_maestro()).append(",")
				.append("'").append(grupoClase.getHora_inicio()).append("',")
				.append("'").append(grupoClase.getHora_fin()).append("',")
				.append("'").append(grupoClase.getDescripcion()).append("',")
				.append("'").append(grupoClase.getComentario()).append("',")
				.append(grupoClase.getEstatus())
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
			resultado = keys.getInt(1);
			log.info(resultado);
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

		return resultado;
	}

	public static int guardarGrupoCliente(Grupo_Cliente grupoCliente) throws SQLException {
		int resultado = 1;

		StringBuilder query = new StringBuilder();
		query.append("insert into grupo_cliente(id_grupo,id_cliente,beca,comentario,estatus) values(")
				.append(grupoCliente.getId_grupo()).append(",")
				.append(grupoCliente.getId_cliente()).append(",")
				.append(grupoCliente.getBeca()).append(",")
				.append("'").append(grupoCliente.getComentario()).append("',")
				.append(grupoCliente.getEstatus())
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
			resultado = keys.getInt(1);
			log.info(resultado);
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

		return resultado;
	}

	public static String obtenerGrupoJsonTable(String grupoId, int numRegistros) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select * from grupo where estatus = 1";
		String cliente = " and id = " + grupoId;
		String registros = " LIMIT "+numRegistros+" OFFSET "+numRegistros;
		if (grupoId!= null && !grupoId.equals("")) {
			query += cliente;
		}
		query += " ORDER BY ID DESC";
		if (numRegistros > 0){
			query += registros;
		}
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
				jsonFinal.put("id",rs.getString("id"));
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

	public static String obtenerGrupoSalonJson() {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select distinct t1.id, CONCAT('Grupo ',t1.nombre) grupo_nombre, count(distinct(t4.id)) alumnos_total, t1.nombre")
				.append(" from grupo t1")
				.append(" inner join grupo_clase t2 on t1.id=t2.id_grupo")
				.append(" inner join grupo_cliente t3 on t1.id=t3.id_grupo")
				.append(" inner join cliente t4 on t4.id=t3.id_cliente")
				.append(" inner join salon t5 on t5.id=t2.id_salon")
				.append(" where t4.estatus in (0,1) and t3.estatus in (0,1)")
				.append(" group by t1.id, t1.nombre")
				.append(" order by t1.nombre");
		Connection con = null;
		System.out.println(query.toString());
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
				jsonFinal.put("label",rs.getString("grupo_nombre"));
				jsonFinal.put("total",rs.getString("alumnos_total"));
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

	public static String obtenerGrupoMaestroJson() {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select distinct t2.id, t2.nombre, count(t1.id) grupoTotal")
				.append(" from grupo_clase t1")
				.append(" inner join maestro t2 on t2.id=t1.id_maestro")
				.append(" where t2.estatus=1")
				.append(" group by t2.id, t2.nombre")
				.append(" order by t2.nombre asc");
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
			JSONObject jsonFinal = new JSONObject();
		    JSONArray jsonArray = new JSONArray();
		    StringWriter out = new StringWriter();
			while(rs.next()){
				jsonFinal.put("id",rs.getString("id"));
				jsonFinal.put("label",rs.getString("nombre"));
				jsonFinal.put("total",rs.getString("grupoTotal"));
				jsonArray.put(jsonFinal);
			    jsonFinal = new JSONObject();
			}
			con.commit();

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

	public static String obtenerTablaGrupo(int[] parametros){ //(int buscaId, int mesBuscado, int buscar){
		StringBuilder result = new StringBuilder();

		if (parametros[0] == 1){
			result = obtenerTablaGrupoAlumno(parametros[1], parametros[2]);
		} else if (parametros[0] == 2){
			result = obtenerTablaGrupoMaestro(parametros[1], parametros[2], parametros[3]);
		} else if (parametros[0] == 3){
			// Listado para actualizar informacion Alumnos en grupos
			result = obtenerTablaGrupoAlumnoAct(parametros[1]);
		}

/*		if (buscar == 1){
			result = obtenerTablaGrupoAlumno(buscaId, mesBuscado);
		} else if (buscar == 2){
			result = obtenerTablaGrupoMaestro(buscaId, mesBuscado);
		}*/
		
		return result.toString();
	}

	private static StringBuilder obtenerTablaGrupoAlumnoAct(int grupoId) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		StringBuilder matricula = new StringBuilder();
		query.append("select distinct t4.id, t3.id gcid, t4.matricula, t4.nombre, t3.id_grupo, t1.nombre grupoNombre, t3.estatus")
			.append(" from grupo t1")
			.append(" inner join grupo_clase t2 on t1.id=t2.id_grupo")
			.append(" inner join grupo_cliente t3 on t1.id=t3.id_grupo")
			.append(" inner join cliente t4 on t4.id=t3.id_cliente")
			.append(" inner join salon t5 on t5.id=t2.id_salon")
			.append(" inner join clase t6 on t6.id=t2.id_clase")
			.append(" where t1.id=").append(grupoId).append(" and t3.estatus in (0,1)")
			.append(" group by t4.id, t3.id, t4.matricula, t4.nombre, t3.id_grupo")
			.append(" ORDER BY t1.ID, t1.nombre, t3.id, t3.id_grupo");
		Connection con = null;
		System.out.println(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result.append("<center><table style='width:100%' id='G_").append(rs.getString("id_grupo")).append("'><tr><td><font size='+2'><table width='90%'><tr><td style='width:50px'></td><td align='left'>Grupo ").append(rs.getString("grupoNombre")).append("</td><td style='width:300px'>Accion: <select name='selAccion' id='selAccion'><option id='1' value='1' selected>Cambio grupo</option><option id='2' value='2'>Baja grupo</option></select>&nbsp;&nbsp;<button id='accion' type='submit' onClick=\"cambioTodo(").append(rs.getString("id_grupo")).append(",'").append(rs.getString("grupoNombre")).append("')\">Ir</button><input type='hidden' id='grupoActual' value='").append(rs.getString("id_grupo")).append("'</td></tr></table></font></center>");
				matricula = new StringBuilder(String.format("%05d", rs.getInt("matricula")));
				StringBuilder activo = new StringBuilder("NO");
				if (rs.getInt("estatus")==1){
					activo = new StringBuilder("SI");
				}
				result.append("<center><table style='width:90%;border-collapse: collapse;' border=0 cellpadding=10 class='table-fill'>")
					.append("<thead><tr><th class='text-left' style='border: 1px solid black; width:25px'><font size='+1'>Matricula</font></th>")
					.append("<th class='text-left' style='border: 1px solid black; width:400px'><font size='+1'>Nombre Alumno</font></th>")
					.append("<th class='text-left' style='border: 1px solid black; width:40px'><font size='+1'>Activo</font></th>")
					.append("<th class='text-left' style='border: 1px solid black; width:120px'><font size='+1'>Accion</font></th>")
					.append("</tr></thead>")
					.append("<tr id='#").append(rs.getInt("gcid")).append("'><td style='border: 1px solid black;' align='right'><font size='+1'>").append(matricula).append("</font></td><td style='border: 1px solid black;'><font size='+1'>")
				    .append(rs.getString("nombre")).append("</font></td>")
				    .append("<td style='border: 1px solid black;' align='center'>").append(activo.toString()).append("</td>")
					.append("<td style='border: 1px solid black;' align='center'>")
					.append("<table style='width:80%; height:45px'><tr><td align='left'>")
					.append("<a href='javascript:void(0)' onClick='editar(").append(rs.getInt("gcid")).append(")'><span class='lnr lnr-pencil' title='Editar informacion del Alumno'></span></a>")
					.append("</td><td align='center'>")
					.append("<a href='javascript:void(0)' onClick=\"baja('").append(rs.getString("grupoNombre")).append("','").append(matricula).append("','").append(rs.getString("nombre")).append("',").append(rs.getInt("gcid")).append(")\"><span class='lnr lnr-trash' title='Dar de baja del Grupo'></span></a>")
					.append("</td><td align='right'>")
					.append("<a href='javascript:void(0)' onClick=\"cambio('").append(rs.getString("nombre")).append("',").append(rs.getInt("gcid")).append(")\"><span class='lnr lnr-sync' title='Cambiar Alumno de Grupo'></span></a>")
					.append("</td></tr></table>")
				    .append("</td></tr>");
				while(rs.next()){
					matricula = new StringBuilder(String.format("%05d", rs.getInt("matricula")));
					if (rs.getInt("estatus")==1){
						activo = new StringBuilder("SI");
					} else {
						activo = new StringBuilder("NO");
					}
					result.append("<tr><td style='border: 1px solid black;' align='right'><font size='+1'>").append(matricula).append("</font></td><td style='border: 1px solid black;'><font size='+1'>")
			        	.append(rs.getString("nombre")).append("</font></td>")
					    .append("<td style='border: 1px solid black;' align='center'>").append(activo.toString()).append("</td>")
						.append("<td style='border: 1px solid black;' align='center'>")
						.append("<table style='width:80%; height:45px'><tr><td align='left'>")
						.append("<a href='javascript:void(0)' onClick='editar(").append(rs.getInt("gcid")).append(")'><span class='lnr lnr-pencil' title='Editar informacion del Alumno'></span></a>")
						.append("</td><td align='center'>")
						.append("<a href='javascript:void(0)' onClick=\"baja('").append(rs.getString("grupoNombre")).append("','").append(matricula).append("','").append(rs.getString("nombre")).append("',").append(rs.getInt("gcid")).append(")\"><span class='lnr lnr-trash' title='Dar de baja del Grupo'></span></a>")
						.append("</td><td align='right'>")
						.append("<a href='javascript:void(0)' onClick=\"cambio('").append(rs.getString("nombre")).append("',").append(rs.getInt("gcid")).append(")\"><span class='lnr lnr-sync' title='Cambiar Alumno de Grupo'></span></a>")
						.append("</td></tr></table>")
			        	.append("</td></tr>");
				}
				result.append("</table>");
		    } else {
		    	result.append("<center><table style='width:90%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
			result.append("</table></center>");
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

	private static StringBuilder obtenerTablaGrupoMaestro(int maestro, int mesBuscado, int clase) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select t1.id, CONCAT('Grupo ', t3.nombre) grupoNombre, t2.nombre maestroNombre, t4.nombre claseNombre, t1.dia_semana, t1.hora_inicio")
			.append(" from grupo_clase t1")
			.append(" inner join maestro t2 on t2.id=t1.id_maestro")
			.append(" inner join grupo t3 on t3.id=t1.id_grupo")
			.append(" inner join clase t4 on t4.id=t1.id_clase")
			.append(" where t2.id=").append(maestro).append(" and t1.id_clase=").append(clase)
			.append(" order by t1.dia_semana, t1.hora_inicio, t2.nombre asc");
		Connection con = null;
		System.out.println(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result.append("<center><font size='+2'><table width='100%'><tr><td>Maestro ").append(rs.getString("maestroNombre")).append("</td></tr><tr><td width='33%'>Clase que imparte: ").append(rs.getString("claseNombre")).append("</td><td width='33%'>Mes: ").append(Constantes.mes[mesBuscado+1]).append("</td></tr></table></font></center>");
				result.append("<center><table style='width:100%;border-collapse: collapse;' border=0 cellpadding=10><thead><tr><th class='text-left' style='border: 1px solid black; width:100px'>Grupo</th><th class='text-left' style='border: 1px solid black; width:25px'>Dia</th><th class='text-left' style='border: 1px solid black; width:25px'>Hora</th><th class='text-left' style='border: 1px solid black; width:25px'>Firma</th><th class='text-left' style='border: 1px solid black; width:25px'>Suplio a:</th><th class='text-left' style='border: 1px solid black; width:25px'>H. Trab.</th><th class='text-left' style='border: 1px solid black; width:25px'>Hora de llegada</th>");
				result.append("</tr></thead>")
					.append("<tr><td style='border: 1px solid black;' align='left'>").append(rs.getString("grupoNombre")).append("</td><td style='border: 1px solid black;' align='right'>").append(Constantes.sDiaSemana[rs.getInt("dia_semana")]).append("</td><td style='border: 1px solid black;' align='right'>").append(rs.getString("hora_inicio"))
					.append("</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td>");
				result.append("</tr>");
				while(rs.next()){
					result.append("<tr><td style='border: 1px solid black;' align='left'>").append(rs.getString("grupoNombre")).append("</td><td style='border: 1px solid black;' align='right'>").append(Constantes.sDiaSemana[rs.getInt("dia_semana")]).append("</td><td style='border: 1px solid black;' align='right'>").append(rs.getString("hora_inicio"))
					.append("</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td>")
					.append("</tr>");
				}
				for(int i=0; i<5; i++){
					result.append("<tr><td style='border: 1px solid black;' align='center'></td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td><td style='border: 1px solid black;' align='right'>&nbsp;</td></tr>");
				}
				result.append("</table>");
				result.append("<p />&nbsp;<p /><hr style='width:50%' /><table style='width:100%'><tr><td style='width:100%'><center><h2>Nombre y firma de recibido</h2></center></td></tr>");
		    } else {
		    	result.append("<center><table style='width:90%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
			result.append("</table></center>");
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

	private static StringBuilder obtenerTablaGrupoAlumno(int grupoId, int mesBuscado){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		StringBuilder matricula = new StringBuilder();
		query.append("select distinct t4.id, t4.matricula, t4.nombre, t1.nombre grupoNombre")
			.append(" from grupo t1")
			.append(" inner join grupo_clase t2 on t1.id=t2.id_grupo")
			.append(" inner join grupo_cliente t3 on t1.id=t3.id_grupo")
			.append(" inner join cliente t4 on t4.id=t3.id_cliente")
			.append(" inner join salon t5 on t5.id=t2.id_salon")
			.append(" inner join clase t6 on t6.id=t2.id_clase")
			.append(" where t1.estatus = 1 and t3.estatus=1 and t1.id=").append(grupoId)
			.append(" group by t4.id, t4.matricula, t4.nombre")
			.append(" ORDER BY t1.ID, t1.nombre");
		Connection con = null;
		System.out.println(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	int[] diasSemana = obtenerDiasJson(grupoId);
		    	Calendar fecha = Calendar.getInstance();
		    	if (mesBuscado < 0)
		    		mesBuscado = fecha.get(Calendar.MONTH);
		    	String[] dias = Utilerias.obtenerDiasMes(mesBuscado, diasSemana);
		    	int numeroDias = 0;
		    	result.append("<center><font size='+2'><table width='90%'><tr><td>Grupo ").append(rs.getString("grupoNombre")).append("</td><td width='33%'>").append(Constantes.mes[mesBuscado+1]).append("</td></tr></table></font></center>");
				result.append("<center><table style='width:100%;border-collapse: collapse;' border=0 cellpadding=10><thead><tr><th class='text-left' style='border: 1px solid black; width:25px'>Matricula</th><th class='text-left' style='border: 1px solid black; width:400px'>Nombre Alumno</th>");
				for (int i=0; i<dias.length; i++){
					if (dias[i] != null){
						result.append("<th style='border: 1px solid black; width:50px'>").append(dias[i]).append("</th>");
						numeroDias ++;
					}
				}
				matricula = new StringBuilder(String.format("%05d", rs.getInt("matricula")));
				result.append("</tr></thead>")
					.append("<tr><td style='border: 1px solid black;' align='right'>").append(matricula).append("</td><td style='border: 1px solid black;'>")
				    .append(rs.getString("nombre")).append("</td>");
				for (int i=0; i<numeroDias; i++){
					result.append("<td style='border: 1px solid black;'>&nbsp;</td>");
				}
				result.append("</tr>");
				while(rs.next()){
					matricula = new StringBuilder(String.format("%05d", rs.getInt("matricula")));
					result.append("<tr><td style='border: 1px solid black;' align='right'>").append(matricula).append("</td><td style='border: 1px solid black;'>")
			        	.append(rs.getString("nombre")).append("</td>");
					for (int i=0; i<numeroDias; i++){
						result.append("<td style='border: 1px solid black;'>&nbsp;</td>");
					}
					result.append("</tr>");
				}
		    } else {
		    	result.append("<center><table style='width:90%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
			result.append("</table></center>");
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

	private static int[] obtenerDiasJson(int grupoId) {
		int[] resultado = new int[31];
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select distinct dia_semana")
			.append(" from grupo_clase t2 ")
			.append(" where t2.id_grupo=").append(grupoId)
			.append(" ORDER BY t2.dia_semana");
		Connection con = null;
		System.out.println(query.toString());
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    int actual = 0;
			while(rs.next()){
				resultado[actual] = rs.getInt("dia_semana");
				actual ++;
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

		return resultado;
	}

	public static String obtenerGrupoJsonTable2() {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
			.append("select t1.id, CONCAT('Grupo ',t1.nombre) nombre,")
			.append(" (select count(0) from grupo_cliente t2 where t2.id_grupo=t1.id and t2.estatus=1) total,")
			.append(" (select count(0) from grupo_clase t3 where t3.id_grupo=t1.id) total_clases")
			.append(" from grupo t1")
			.append(" order by t1.nombre");
		Connection con = null;
		log.info(query.toString());
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
				jsonFinal.put("nombre",rs.getString("nombre"));
				jsonFinal.put("total", rs.getString("total"));
				jsonFinal.put("totalC", rs.getString("total_clases"));
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

	public static String obtenerGrupoTable(String id) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
			.append("select t1.id, t2.id cliente_id, CONCAT('Grupo ',t1.nombre) grupo_nombre, t3.nombre cliente_nombre, t4.monto, t2.beca, (t4.monto-((t2.beca/100)*t4.monto)) total")
			.append(" from grupo t1")
			.append(" inner join grupo_cliente t2 on t2.id_grupo=t1.id")
			.append(" inner join cliente t3 on t2.id_cliente=t3.id")
			.append(" inner join grupo_costo t4 on t4.id_grupo=t1.id")
			.append(" WHERE t2.estatus=1 and t1.id = ").append(id);
		query.append(" ORDER BY ID DESC");
		log.info(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
				result.append("<font size='+2'><b>").append(rs.getString("grupo_nombre")).append("</b></font>")
					.append("<table style='width:100%; border-collapse: collapse' class='table-fill' border='0' cellspacing=0 cellpadding=7>")
						.append("<tr><td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Alumno</b></font></td><td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Accion</b></font></td></tr>")
						.append("<tr id='tr_").append(rs.getInt("cliente_id")).append("'><td class='text-left' style='border: 1px solid black;'>&nbsp;").append(rs.getString("cliente_nombre")).append("</td><td style='border: 1px solid black;'>&nbsp;<a href='javascript:void(0)' onClick='baja(\"").append(rs.getString("grupo_nombre")).append("\",").append(rs.getInt("cliente_id")).append(",\"").append(rs.getString("cliente_nombre")).append("\")'>Baja</a></td></tr>");
				while (rs.next()){
					result.append("<tr id='").append(rs.getInt("cliente_id")).append("'><td class='text-left' style='border: 1px solid black;'>&nbsp;").append(rs.getString("cliente_nombre")).append("</td><td style='border: 1px solid black;'>&nbsp;<a href='javascript:void(0)' onClick='baja(\"").append(rs.getString("grupo_nombre")).append("\",").append(rs.getInt("cliente_id")).append(",\"").append(rs.getString("cliente_nombre")).append("\")'>Baja</a></td></tr>");
				}
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

	public static int actualizaGrupoClienteEstatus(Grupo_Cliente grupoCliente, int id_grupo) throws SQLException {
		int resultado = 1;

		StringBuilder query = new StringBuilder();
		query.append("update grupo_cliente ")
			.append(" set ");
		if (id_grupo<0)
			query.append(" estatus=").append(grupoCliente.getEstatus());
		if (grupoCliente.getId()>0){
			if (id_grupo>0)
				query.append(" id_grupo=").append(id_grupo);
			if (grupoCliente.getBeca()!=null)
				query.append(" ,beca=").append(grupoCliente.getBeca());
			query.append(" where id=").append(grupoCliente.getId());
		}else {
			if (id_grupo<0)
				query.append(",");
			query.append(" id_grupo=").append(id_grupo)
				.append(" where id_grupo=").append(grupoCliente.getId_grupo());
		}
		log.info(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			con.commit();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			if (con != null)
				con.rollback();
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

	public static String obtenerGrupoClienteTable(String id) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
			.append("select t1.id, t2.id cliente_id, CONCAT('Grupo ',t1.nombre) grupo_nombre, t3.nombre cliente_nombre, t4.monto, t2.beca, (t4.monto-((t2.beca/100)*t4.monto)) total, t2.estatus")
			.append(" from grupo t1")
			.append(" inner join grupo_cliente t2 on t2.id_grupo=t1.id")
			.append(" inner join cliente t3 on t2.id_cliente=t3.id")
			.append(" inner join grupo_costo t4 on t4.id_grupo=t1.id")
			.append(" WHERE t2.id = ").append(id);
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
		    	Locale locale = new Locale("en", "US");
		    	NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		    	String moneyString = formatter.format(rs.getFloat("total"));
		    	if (rs.getBoolean("estatus"))
		    		checked = "checked";
				result.append("<font size='+2'><b>").append(rs.getString("cliente_nombre")).append("</b><p /><center>")
					.append("<table style='width:80%'>")
					.append("<tr><td>Activo:</td><td align='right'><input id='estatus' type='checkbox' ").append(checked).append(" /></td><td style='width:66%'><input type='hidden' id='id' value='").append(rs.getString("cliente_id")).append("' /></td></tr>")
					.append("<tr><td>Beca:</td><td align='right'><input id='beca' type='text' size='3' style='text-align: right;' value='").append(String.format("%.0f", rs.getFloat("beca"))).append("' onBlur='actualiza(this)' /> </td><td>%</td></tr>")
					.append("<tr><td>Mensualidad:</td><td><input id='total' type='text' size='10' style='text-align: right;' disabled='disabled' value='").append(moneyString).append("' /><input type='hidden' id='monto' value='").append(rs.getFloat("monto")).append("' /></td><td></td></tr>")
					.append("</table></center></font>");
		    } else {
		    	result.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
			    result.append("</table>");
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

	public static int bajaGrupoClienteEstatus(Grupo_Cliente grupoCliente) throws SQLException {
		int resultado = 0;

		StringBuilder query = new StringBuilder()
				.append("delete from grupo_cliente ")
				.append(" where id=").append(grupoCliente.getId());
		log.info(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			con.commit();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			if (con != null)
				con.rollback();
			throw e;
		} finally {
			 try{
		           if(con != null) {
		        	   con.close();
		        	   resultado = 1;
		        	}
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return resultado;
	}

	public static String obtenerGrupoDisponibleTable(String cliente_nombre, String cliente_id, String grupo_id) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
			.append("select id, CONCAT('Grupo ',t1.nombre) grupo_nombre, (SELECT COUNT(0) FROM grupo_cliente t2 WHERE t2.id_grupo=t1.id) ")
			.append("from grupo t1 ")
			.append("where t1.estatus=1 ")
			.append("order by grupo_nombre");
		log.info(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
		    	if (!cliente_nombre.equals("")){
		    		result.append("<font size='+2'>Alumno: <b>").append(cliente_nombre).append("</b><p /><center>");
		    	}
				result.append("<table style='width:90%'>")
					.append("<tr><td>Grupo: </td><td><input id='grupo' title='type &quot;a&quot;' style='width:100%' /><input id='grupoId' type='hidden' />")
					.append("<input value='").append(cliente_id).append("' id='clienteId' type='hidden' /><input value='").append(grupo_id).append("' id='grupoActual' type='hidden' /></td></tr>")
					.append("</table></center></font>");
		    } else {
		    	result.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
			    result.append("</table>");
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

	public static String obtenerGrupoDetalleTable(String id, int detalle) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
			.append("select t1.id, t1.id_grupo, CONCAT('Grupo ',t4.nombre) grupo_nombre, t1.id_clase, t2.nombre clase_nombre, t1.id_maestro, t3.nombre maestro_nombre, t1.id_salon, CONCAT('Salon ',t5.nombre) salon_nombre, t1.dia_semana, TIME_FORMAT(t1.hora_inicio, '%H:%i') hora_inicio, TIME_FORMAT(t1.hora_fin, '%H:%i') hora_fin")
			.append(" from grupo_clase t1")
			.append(" inner join clase t2 on t2.id=t1.id_clase")
			.append(" inner join maestro t3 on t3.id=t1.id_maestro")
			.append(" inner join grupo t4 on t4.id=t1.id_grupo")
			.append(" inner join salon t5 on t5.id=t1.id_salon");
		if (detalle==1){
			query.append(" where t1.id_maestro= ").append(id);
		} else {
			query.append(" where t1.id_grupo= ").append(id);
		}
		query.append(" ORDER BY t1.dia_semana, t1.hora_inicio, t2.nombre DESC");
		log.info(query.toString());
		Connection con = null;
		int idR = 1;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    if (rs.next()){
				if (detalle==1){
					result.append("<font size='+2'><b>").append(rs.getString("maestro_nombre")).append("</b></font>");
				} else {
					result.append("<font size='+2'><b>").append(rs.getString("grupo_nombre")).append("</b></font>");
				}
				result.append("<table style='width:100%; border-collapse: collapse' class='table-fill' border='0' cellspacing=0 cellpadding=7>")
						.append("<tr><td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Clase</b></font></td>");
				if (detalle==1){
					result.append("<td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Grupo</b></font></td>");
				} else {
					result.append("<td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Maestro</b></font></td>");
				}
				result.append("<td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Salon</b></font></td>")
						.append("<td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Dia</b></font></td>")
						.append("<td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Horario</b></font></td>")
						.append("<td style='border: 3px solid black;'><font size='+1'><b>&nbsp;Accion</b></font></td></tr>");
				result.append("<tr id='tr_").append(rs.getInt("id")).append("'>")
					.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='c_").append(idR).append("' name='clase' value='").append(rs.getString("clase_nombre")).append("' /><input type='hidden' id='c_").append(idR).append("id' name='claseId' value='").append(rs.getString("id_clase")).append("' /></td>");
				if (detalle==1){
					result.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='g_").append(idR).append("' name='grupo' value='").append(rs.getString("grupo_nombre")).append("' /><input type='hidden' id='g_").append(idR).append("id' name='grupoId' value='").append(rs.getString("id_grupo")).append("' /></td>");
				} else {
					result.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='m_").append(idR).append("' name='maestro' value='").append(rs.getString("maestro_nombre")).append("' /><input type='hidden' id='m_").append(idR).append("id' name='maestroId' value='").append(rs.getString("id_maestro")).append("' /></td>");
				}
				result.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='s_").append(idR).append("' name='salon' value='").append(rs.getString("salon_nombre")).append("' size='10px' /><input type='hidden' id='s_").append(idR).append("id' name='salonId' value='").append(rs.getString("id_salon")).append("' /></td>")
					.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='d_").append(idR).append("' name='dia' value='").append(Constantes.sDiaSemana[rs.getInt("dia_semana")]).append("' size='7px' /><input type='hidden' id='d_").append(idR).append("id' name='diaSe' value='").append(rs.getInt("dia_semana")).append("' /></td>")
					.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='hi_").append(idR).append("' name='horaI' value='").append(rs.getString("hora_inicio")).append("' size='5px' /> / <input type='text' id='hf_").append(idR).append("' name='horaF' value='").append(rs.getString("hora_fin")).append("' size='5px' /></td>")
					.append("<td style='border: 1px solid black;'>&nbsp;<a href='javascript:void(0)' onClick='baja(\"").append(rs.getString("grupo_nombre")).append("\",\"").append(rs.getString("clase_nombre")).append("\",").append(rs.getInt("id")).append(",").append(idR).append(")'>Borrar</a>")
					.append("<input type='hidden' id='id_").append(idR).append("id' name='ids' value='").append(rs.getString("id")).append("' /><input type='hidden' id='g_").append(idR).append("id' name='grupoId' value='").append(rs.getString("id_grupo")).append("' /><input type='hidden' id='es_").append(rs.getInt("id")).append("id' name='estatus' value='1' /></td>")
					.append(getScripts(idR).toString()).append("</tr>");
				idR++;
				while (rs.next()){
					result.append("<tr id='tr_").append(rs.getInt("id")).append("'>")
					.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='c_").append(idR).append("' name='clase' value='").append(rs.getString("clase_nombre")).append("' /><input type='hidden' id='c_").append(idR).append("id' name='claseId' value='").append(rs.getString("id_clase")).append("' /></td>");
					if (detalle==1){
						result.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='g_").append(idR).append("' name='grupo' value='").append(rs.getString("grupo_nombre")).append("' /><input type='hidden' id='g_").append(idR).append("id' name='grupoId' value='").append(rs.getString("id_grupo")).append("' /></td>");
					} else {
						result.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='m_").append(idR).append("' name='maestro' value='").append(rs.getString("maestro_nombre")).append("' /><input type='hidden' id='m_").append(idR).append("id' name='maestroId' value='").append(rs.getString("id_maestro")).append("' /></td>");
					}
					result.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='s_").append(idR).append("' name='salon' value='").append(rs.getString("salon_nombre")).append("' size='10px' /><input type='hidden' id='s_").append(idR).append("id' name='salonId' value='").append(rs.getString("id_salon")).append("' /></td>")
					.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='d_").append(idR).append("' name='dia' value='").append(Constantes.sDiaSemana[rs.getInt("dia_semana")]).append("' size='7px' /><input type='hidden' id='d_").append(idR).append("id' name='diaSe' value='").append(rs.getInt("dia_semana")).append("' /></td>")
					.append("<td class='text-left' style='border: 1px solid black;'>&nbsp;<input type='text' id='hi_").append(idR).append("' name='horaI' value='").append(rs.getString("hora_inicio")).append("' size='5px' /> / <input type='text' id='hf_").append(idR).append("' name='horaF' value='").append(rs.getString("hora_fin")).append("' size='5px' /></td>")
					.append("<td style='border: 1px solid black;'>&nbsp;<a href='javascript:void(0)' onClick='baja(\"").append(rs.getString("grupo_nombre")).append("\",\"").append(rs.getString("clase_nombre")).append("\",").append(rs.getInt("id")).append(",").append(idR).append(")'>Borrar</a>")
					.append("<input type='hidden' id='id_").append(idR).append("id' name='ids' value='").append(rs.getString("id")).append("' /><input type='hidden' id='g_").append(idR).append("id' name='grupoId' value='").append(rs.getString("id_grupo")).append("' /><input type='hidden' id='es_").append(rs.getInt("id")).append("id' name='estatus' value='1' /></td>")
					.append(getScripts(idR).toString()).append("</tr>");
					idR++;
				}
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

	private static StringBuilder getScripts(int id){
		return new StringBuilder()
				.append("<script>")
				.append("$('#c_").append(id).append("').autocomplete({source: claseDisponible,select: function (event, ui) {	$('#c_").append(id).append("').val(ui.item.label);$('#c_").append(id).append("Id').val(ui.item.id);}});")
				.append("$('#m_").append(id).append("').autocomplete({source: maestroDisponible,select: function (event, ui) {	$('#m_").append(id).append("').val(ui.item.label);$('#m_").append(id).append("Id').val(ui.item.id);}});")
				.append("$('#s_").append(id).append("').autocomplete({source: salonDisponible,select: function (event, ui) {	$('#s_").append(id).append("').val(ui.item.label);$('#s_").append(id).append("Id').val(ui.item.id);}});")
				.append("$('#d_").append(id).append("').autocomplete({source: diaDisponible,select: function (event, ui) {	$('#d_").append(id).append("').val(ui.item.label);$('#d_").append(id).append("Id').val(ui.item.id);}});")
				.append("$('#hi_").append(id).append("').autocomplete({source: horaDisponible,select: function (event, ui) {	$('#hi_").append(id).append("').val(ui.item.label);$('#hi_").append(id).append("Id').val(ui.item.id);}});")
				.append("$('#hf_").append(id).append("').autocomplete({source: horaDisponible,select: function (event, ui) {	$('#hf_").append(id).append("').val(ui.item.label);$('#hf_").append(id).append("Id').val(ui.item.id);}});")
				.append("</script>");

	}

	public static int actualizaGrupoClase(Grupo_Clase grupoClase) throws SQLException {
		int resultado = 1;
//		SimpleDateFormat formatoFecha = new SimpleDateFormat(Constantes.horaFormato);

		StringBuilder query = new StringBuilder();
		if (grupoClase.getId()>0){
			query.append("update grupo_clase set")
				.append(" id_grupo=").append(grupoClase.getId_grupo())
				.append(", id_clase=").append(grupoClase.getId_clase())
				.append(", id_salon=").append(grupoClase.getId_salon())
				.append(", dia_semana=").append(grupoClase.getDia_semana())
				.append(", id_maestro=").append(grupoClase.getId_maestro())
				.append(", hora_inicio='").append(grupoClase.getHora_inicio()).append("'")
				.append(", hora_fin='").append(grupoClase.getHora_fin()).append("' ")
				.append("where id=").append(grupoClase.getId());
			log.info(query.toString());
		} else {
			query.append("insert into grupo_clase(nombre,id_grupo,id_clase,id_salon,dia_semana,id_maestro,hora_inicio,hora_fin,descripcion,comentario,estatus) values(")
				.append("'").append(grupoClase.getNombre()).append("',")
				.append(grupoClase.getId_grupo()).append(",")
				.append(grupoClase.getId_clase()).append(",")
				.append(grupoClase.getId_salon()).append(",")
				.append(grupoClase.getDia_semana()).append(",")
				.append(grupoClase.getId_maestro()).append(",")
				.append("'").append(grupoClase.getHora_inicio()).append("',")
				.append("'").append(grupoClase.getHora_fin()).append("',")
//				.append("STR_TO_DATE('"+formatoFecha.format(grupoClase.getHora_inicio())+"', '"+Constantes.horaFormatoSQL2+"'),")
//				.append("STR_TO_DATE('"+formatoFecha.format(grupoClase.getHora_fin())+"', '"+Constantes.horaFormatoSQL2+"'),")
				.append("'").append(grupoClase.getDescripcion()).append("',")
				.append("'").append(grupoClase.getComentario()).append("',")
				.append(grupoClase.getEstatus())
				.append(")");
			log.info(query.toString());
		}
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();
			if (grupoClase.getId()==0){
				ResultSet keys = pstmt.getGeneratedKeys();    
				keys.next();  
				resultado = keys.getInt(1);
				log.info(resultado);
			}
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

		return resultado;
	}

	public static int bajaGrupoClase(Grupo_Clase grupoClase) throws SQLException {
		int resultado = 0;

		StringBuilder query = new StringBuilder()
				.append("delete from grupo_clase ")
				.append(" where id=").append(grupoClase.getId());
		log.info(query);
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			con.commit();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			if (con != null)
				con.rollback();
			throw e;
		} finally {
			 try{
		           if(con != null) {
		        	   con.close();
		        	   resultado = 1;
		        	}
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return resultado;
	}

}
