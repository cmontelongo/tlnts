package com.talentos.action;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Cliente;
import com.talentos.util.Constantes;

public class clienteAction {

	private static Logger log = Logger.getLogger(clienteAction.class);

	public static String obtenerMatricula() throws NamingException{
		String matricula = "001";
		Connection con = null;
		ResultSet rs = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);
	        PreparedStatement pstmt = null;
            pstmt = con.prepareStatement("select ifnull(max(matricula),0) as mat from cliente");
            rs = pstmt.executeQuery();
			if (rs != null){
				rs.next();
				int mat = rs.getInt("mat");
				mat++;
				matricula = String.format("%05d", mat);
			}

		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
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

	public static String obtenerClienteJson(String clienteId, int numRegistros){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select * from cliente where estatus = 1";
		String cliente = " and id = " + clienteId;
		String registros = " LIMIT "+numRegistros+" OFFSET "+numRegistros;
		if (clienteId!= null && !clienteId.equals("")) {
			query += cliente;
		}
		query += " ORDER BY ID DESC";
		if (numRegistros > 0){
			query += registros;
		}
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(false);
		    pstmt = con.prepareStatement(query);
		    rs = pstmt.executeQuery();
			result = "{\"cliente\":[";
			while(rs.next()){
				result += "{\"id\":\""+rs.getString("id")+
						"\",\"nombre\":\""+rs.getString("nombre")+"\""+
						"\",\"matricula\":\""+rs.getString("matricula")+"\""+
						"\",\"fecha_nacimiento\":\""+rs.getString("fecha_nacimiento")+"\""+
						"\",\"calle\":\""+rs.getString("calle")+"\""+
						"\",\"colonia\":\""+rs.getString("colonia")+"\""+
						"\",\"municipio\":\""+rs.getString("municipio")+"\""+
						"\",\"codigo_postal\":\""+rs.getString("codigo_postal")+"\""+
						"\",\"madre_nombre\":\""+rs.getString("madre_nombre")+"\""+
						"\",\"madre_ocupacion\":\""+rs.getString("madre_ocupacion")+"\""+
						"\",\"madre_telefono_casa\":\""+rs.getString("madre_telefono_casa")+"\""+
						"\",\"madre_telefono_celular\":\""+rs.getString("madre_telefono_celular")+"\""+
						"\",\"madre_telefono_oficina\":\""+rs.getString("madre_telefono_oficina")+"\""+
						"\",\"madre_email\":\""+rs.getString("madre_email")+"\""+
						"\",\"madre_telefono_recado\":\""+rs.getString("madre_telefono_recado")+"\""+
						"\",\"padre_nombre\":\""+rs.getString("padre_nombre")+"\""+
						"\",\"padre_ocupacion\":\""+rs.getString("padre_ocupacion")+"\""+
						"\",\"padre_telefono_celular\":\""+rs.getString("padre_telefono_celular")+"\""+
						"\",\"padre_telefono_oficina\":\""+rs.getString("padre_telefono_oficina")+"\""+
						"\",\"padre_email\":\""+rs.getString("padre_email")+"\""+
						"\",\"estatus\":\""+rs.getString("estatus")+"\""+
						"\",\"fecha_alta\":\""+rs.getString("fecha_alta")+"\""+
						"\",\"fecha_baja\":\""+rs.getString("fecha_baja")+"\""+
						"\",\"comentario\":\""+rs.getString("comentario")+"\""+
						"\",\"id_usuario_alta\":\""+rs.getString("id_usuario_alta")+"\""+
						"\",\"fecha_ultima_modificacion\":\""+rs.getString("fecha_ultima_modificacion")+"\""+
						"\",\"id_usuario_ultima_modificacion\":\""+rs.getString("id_usuario_ultima_modificacion")+"\""+
				"},";
			}
			con.commit();
			result = result.substring(0, result.length()-1);
			result += "]}";
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

	public static String obtenerClienteJsonTable(String clienteId, int numRegistros){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select id,matricula,nombre,DATE_FORMAT(fecha_nacimiento, '%d/%m/%Y') fecha_nacimiento,calle,numero,colonia,municipio,codigo_postal,")
				.append("madre_nombre,madre_ocupacion,madre_telefono_casa,madre_telefono_celular,madre_telefono_oficina,madre_email,madre_telefono_recado,")
				.append("padre_nombre,padre_ocupacion,padre_telefono_celular,padre_telefono_oficina,padre_email,")
				.append("estatus,fecha_alta,fecha_baja,comentario")
				.append(" FROM cliente");
		StringBuilder cliente = new StringBuilder(" where id = ").append(clienteId);
		StringBuilder registros = new StringBuilder(" LIMIT ").append(numRegistros).append(" OFFSET ").append(numRegistros);
		if (clienteId!= null && !clienteId.equals("")) {
			query.append(cliente.toString());
		}
		query.append(" ORDER BY ID DESC");
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
			while(rs.next()){
				jsonFinal.put("id",rs.getString("id"));
				jsonFinal.put("matricula",rs.getString("matricula"));
				jsonFinal.put("mat",String.format("%1$06d",Integer.parseInt(rs.getString("matricula"))));
				jsonFinal.put("nombre",rs.getString("nombre"));
				jsonFinal.put("fecha_nacimiento",rs.getString("fecha_nacimiento"));
				jsonFinal.put("madre_nombre", rs.getString("madre_nombre"));
				jsonFinal.put("madre_email",rs.getString("madre_email"));
				jsonFinal.put("madre_celular", rs.getString("madre_telefono_celular"));
				jsonFinal.put("padre_nombre",rs.getString("padre_nombre"));
				jsonFinal.put("padre_email",rs.getString("padre_email"));
				jsonFinal.put("padre_celular", rs.getString("padre_telefono_celular"));
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

	public static String obtenerClienteJson(){
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select id,matricula,nombre from cliente where estatus = 1";
		query += " ORDER BY ID DESC";
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
				jsonFinal.put("label", rs.getString("matricula")+" - "+rs.getString("nombre"));
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

	public static String obtenerClienteClaseJson(){
		String result = "";
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		String query = "select t2.id,t1.nombre, (t3.monto*(1-t2.beca)) monto from cliente t1, cliente_clase t2, clase t3 where t1.estatus = 1 and t1.id=t2.id_cliente and t2.fecha_baja is null and t2.id_clase=t3.id";
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
				result += "{id:"+id+",label:\""+id+" - "+rs.getString("nombre")+"\",monto:"+rs.getString("monto")+"},";
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

	public static int guardarCliente(Cliente cliente) throws SQLException, NamingException{
		int matricula = 1;
		SimpleDateFormat formatoFechaNacimiento = new SimpleDateFormat(Constantes.fechaFormatoUsuario);

		int matriculaNuevo = Integer.parseInt(obtenerMatricula());
		
		String query = "insert into cliente(matricula,nombre,fecha_nacimiento,calle,numero,colonia,municipio,codigo_postal,"
				+"madre_nombre,madre_ocupacion,madre_telefono_casa,madre_telefono_celular,madre_telefono_oficina,madre_email,madre_telefono_recado,"
				+"padre_nombre,padre_ocupacion,padre_telefono_celular,padre_telefono_oficina,padre_email,estatus) values(";
		query += matriculaNuevo+","
				+"'"+cliente.getNombre()+"',"
				+"STR_TO_DATE('"+formatoFechaNacimiento.format(cliente.getFecha_nacimiento())+"', '"+Constantes.fechaNacimientoSQL+"'),"
				+"'"+cliente.getCalle()+"',"
				+"'"+cliente.getNumero()+"',"
				+"'"+cliente.getColonia()+"',"
				+"'"+cliente.getMunicipio()+"',"
				+"'"+cliente.getCodigo_postal()+"',"
				+"'"+cliente.getMadre_nombre()+"',"
				+"'"+cliente.getMadre_ocupacion()+"',"
				+"'"+cliente.getMadre_telefono_casa()+"',"
				+"'"+cliente.getMadre_telefono_celular()+"',"
				+"'"+cliente.getMadre_telefono_oficina()+"',"
				+"'"+cliente.getMadre_email()+"',"
				+"'"+cliente.getMadre_telefono_recado()+"',"
				+"'"+cliente.getPadre_nombre()+"',"
				+"'"+cliente.getPadre_ocupacion()+"',"
				+"'"+cliente.getPadre_telefono_celular()+"',"
				+"'"+cliente.getPadre_telefono_oficina()+"',"
				+"'"+cliente.getPadre_email()+"',"
				+"1";
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
			matricula = matriculaNuevo;
			log.info("id="+keys.getInt(1)+" - matricula="+matricula);
			con.commit();
			
		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
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

	public static String obtenerClienteTable(String matricula) {
		StringBuilder result = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder()
				.append("select id, matricula,nombre,DATE_FORMAT(fecha_nacimiento, '%d/%m/%Y') fecha_nacimiento,calle,numero,colonia,municipio,codigo_postal,")
				.append("madre_nombre,madre_ocupacion,madre_telefono_casa,madre_telefono_celular,madre_telefono_oficina,madre_email,madre_telefono_recado,")
				.append("padre_nombre,padre_ocupacion,padre_telefono_celular,padre_telefono_oficina,padre_email,estatus")
				.append(" FROM cliente WHERE matricula = ").append(matricula);
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

	public static int actualizaCliente(Cliente cliente) throws SQLException {
		int matricula = 1;
		SimpleDateFormat formatoFechaNacimiento = new SimpleDateFormat(Constantes.fechaFormatoUsuario);

		StringBuilder query = new StringBuilder();
		query.append("update cliente ")
			.append(" set matricula=").append(cliente.getMatricula()).append(",")
			.append(" nombre='").append(cliente.getNombre()).append("', ")
			.append(" fecha_nacimiento=STR_TO_DATE('").append(formatoFechaNacimiento.format(cliente.getFecha_nacimiento())).append("', '").append(Constantes.fechaNacimientoSQL+"'), ")
			.append(" calle='").append(cliente.getCalle()).append("', ")
			.append(" numero='").append(cliente.getNumero()).append("', ")
			.append(" colonia='").append(cliente.getColonia()).append("', ")
			.append(" municipio='").append(cliente.getMunicipio()).append("', ")
			.append(" codigo_postal='").append(cliente.getCodigo_postal()).append("', ")
			.append(" madre_nombre='").append(cliente.getMadre_nombre()).append("', ")
			.append(" madre_ocupacion='").append(cliente.getMadre_ocupacion()).append("', ")
			.append(" madre_telefono_casa='").append(cliente.getMadre_telefono_casa()).append("', ")
			.append(" madre_telefono_celular='").append(cliente.getMadre_telefono_celular()).append("', ")
			.append(" madre_telefono_oficina='").append(cliente.getMadre_telefono_oficina()).append("', ")
			.append(" madre_email='").append(cliente.getMadre_email()).append("', ")
			.append(" madre_telefono_recado='").append(cliente.getMadre_telefono_recado()).append("', ")
			.append(" padre_nombre='").append(cliente.getPadre_nombre()).append("', ")
			.append(" padre_ocupacion='").append(cliente.getPadre_ocupacion()).append("', ")
			.append(" padre_telefono_celular='").append(cliente.getPadre_telefono_celular()).append("', ")
			.append(" padre_telefono_oficina='").append(cliente.getPadre_telefono_oficina()).append("', ")
			.append(" padre_email='").append(cliente.getPadre_email()).append("', ")
			.append(" estatus=").append(cliente.getEstatus())
			.append(" where id=").append(cliente.getId());
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);  
			pstmt.executeUpdate();  
			con.commit();
			
		} catch (SQLException e) {
			con.rollback();
			e.printStackTrace();
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

}
