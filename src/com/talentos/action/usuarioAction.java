package com.talentos.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.jboss.logging.Logger;

import com.talentos.coneccion.coneccion;
import com.talentos.entidad.Usuario;

public class usuarioAction {

	private static Logger log = Logger.getLogger(usuarioAction.class);

	public static String obtenerUsuariosJson(){
		String result = "";
		ResultSet rs = null;
		try {
			rs = coneccion.ejecutar("select * from usuario");
			result = "{\"user\":[";
			while (rs.next()){
				result += "{\"id\":\""+rs.getString("id")+"\",\"nombre\":\""+rs.getString("nombre")+"\"},";
			}
			result = result.substring(0, result.length()-1);
			result += "]}";
		} catch (NamingException e) {
			log.error(e.getMessage());
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return result;
	}

	public static List<Usuario> obtenerUsuarios(){
		ResultSet rs = null;
		List<Usuario> usr = new ArrayList<Usuario>();
		try {
			rs = coneccion.ejecutar("select * from usuario");
			while (rs.next()){
				Usuario u = new Usuario();
				u.setId(Integer.parseInt(rs.getString("id")));
				u.setNombre(rs.getString("nombre"));
				usr.add(u);
			}
		} catch (NamingException e) {
			log.error(e.getMessage());
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return usr;
	}

}
