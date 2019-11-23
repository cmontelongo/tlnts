package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.action.grupoAction;
import com.talentos.entidad.Grupo;
import com.talentos.entidad.Grupo_Clase;
import com.talentos.entidad.Grupo_Cliente;
import com.talentos.entidad.Grupo_Costo;
import com.talentos.util.Constantes;

/**
 * Servlet implementation class grupoServlet
 */
@WebServlet("/grupoServlet")
public class grupoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public grupoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String proceso = request.getParameter("p")!=null?request.getParameter("p"):"";
		String consulta = request.getParameter("c")!=null?request.getParameter("c"):"";
		if (proceso.equals("c")){
			if (consulta.equals("1")){
				// Actualiza Grupos / Listado de Grupos
				response.setContentType("application/json");
				String resultado = grupoAction.obtenerGrupoJsonTable2();
				out.println(resultado);
			} else if (consulta.equals("2")){
				response.setContentType("text/html");
				String id = request.getParameter("m");
				String resultado = grupoAction.obtenerGrupoTable(id);
				out.println(resultado);
			} else if (consulta.equals("3")){
				// Actualiza Grupos / Detalle de Grupo
				response.setContentType("text/html");
				String id = request.getParameter("m");
				int detalle = request.getParameter("d")==null?0:Integer.parseInt(request.getParameter("d"));
				String resultado = grupoAction.obtenerGrupoDetalleTable(id, detalle);
				out.println(resultado);
			}
/*			String resultado = grupoAction.obtenerGrupoJsonTable(null, 0);
			out.println(resultado);*/
		} else if (proceso.equalsIgnoreCase("d")){
			if (consulta.equals("1")){
				String cliente_nombre = request.getParameter("m")!=null?request.getParameter("m"):"";
				String grupo_id = request.getParameter("idA")!=null?request.getParameter("idA"):"";
				String cliente_id = request.getParameter("id")!=null?request.getParameter("id"):"";
				String resultado = grupoAction.obtenerGrupoDisponibleTable(cliente_nombre, cliente_id, grupo_id);
				out.println(resultado);
			} else if (consulta.equals("2")){
				String grupo_id = request.getParameter("idA")!=null?request.getParameter("idA"):"";
				String resultado = grupoAction.obtenerGrupoDisponibleTable("", "", grupo_id);
				out.println(resultado);
			} else {
				String id = request.getParameter("m");
				String resultado = grupoAction.obtenerGrupoClienteTable(id);
				out.println(resultado);
			}
		}
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String proceso = request.getParameter("proceso")!=null?request.getParameter("proceso"):"";
		SimpleDateFormat formatter = new SimpleDateFormat(Constantes.fechaFormato);
		if (proceso.equals("ALT")){
			Grupo grupo = new Grupo();
			Grupo_Costo grupoCosto = new Grupo_Costo();
			try {
				grupo.setNombre(request.getParameter("nombre"));
				grupo.setComentario(request.getParameter("comentario"));
				grupo.setFecha_alta(formatter.parse(request.getParameter("fechaAlta")));
				grupo.setParticular(Integer.parseInt(request.getParameter("particular")));

				int id_grupo = grupoAction.guardarGrupo(grupo);
				
				grupoCosto.setId_grupo(id_grupo);
				Float monto = (float) 0;
				if (!request.getParameter("monto").equals("") || request.getParameter("monto")!=null){
					monto = Float.parseFloat(request.getParameter("monto"));
				}
				grupoCosto.setMonto(monto);
				
				int id_grupo_costo = grupoAction.guardarGrupoCosto(grupoCosto);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id_grupo)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if(proceso.equals("AL1")){
			Grupo_Clase grupoClase = new Grupo_Clase();
			int id_grupo_clase = 0;
			try{
				grupoClase.setId_clase(Integer.parseInt(request.getParameter("clase")));
				grupoClase.setId_grupo(Integer.parseInt(request.getParameter("grupo")));
				if (!request.getParameter("salon").equals("")){
					grupoClase.setId_salon(Integer.parseInt(request.getParameter("salon")));
				}
				grupoClase.setId_maestro(Integer.parseInt(request.getParameter("maestro")));
				grupoClase.setDia_semana(Integer.parseInt(request.getParameter("diaS")));
				Time thInicio = Time.valueOf(request.getParameter("hInicio")+":00");
				Time thFin = Time.valueOf(request.getParameter("hFin")+":00");
				grupoClase.setHora_inicio(thInicio);
				grupoClase.setHora_fin(thFin);
				grupoClase.setEstatus(1);

				id_grupo_clase = grupoAction.guardarGrupoClase(grupoClase);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id_grupo_clase)+"\"}");
			} catch(Exception ex){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if(proceso.equals("AL2")){
			Grupo_Cliente grupoCliente = new Grupo_Cliente();
			int id_grupo_cliente = 0;
			try{
				grupoCliente.setId_cliente(Integer.parseInt(request.getParameter("cliente")));
				grupoCliente.setId_grupo(Integer.parseInt(request.getParameter("grupo")));
				grupoCliente.setBeca(Float.parseFloat(request.getParameter("beca")));
				grupoCliente.setComentario(request.getParameter("comentario"));
				grupoCliente.setEstatus(1);

				id_grupo_cliente = grupoAction.guardarGrupoCliente(grupoCliente);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id_grupo_cliente)+"\"}");
			} catch(Exception ex){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if(proceso.equals("ACT")){
			Grupo_Cliente grupoCliente = new Grupo_Cliente();
			int id_grupo_cliente = 0;
			try{
				grupoCliente.setId(Integer.parseInt(request.getParameter("id")));
				if (request.getParameter("beca")!=null)
					grupoCliente.setBeca(Float.parseFloat(request.getParameter("beca")));
				grupoCliente.setEstatus(Integer.parseInt(request.getParameter("estatus")));

				id_grupo_cliente = grupoAction.actualizaGrupoClienteEstatus(grupoCliente, -1);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id_grupo_cliente)+"\"}");
			} catch(Exception ex){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de actualizar el registro.\", \"responseText\":\"\"}");
			}
		} else if(proceso.equals("AC2")){
			// Actualizar Estatus / Grupo del ALUMNO actual
			Grupo_Cliente grupoCliente = new Grupo_Cliente();
			int id_grupo_cliente = 0;
			int id_grupo = 0;
			try{
				if (request.getParameter("estatus")!=null)
					grupoCliente.setEstatus(Integer.parseInt(request.getParameter("estatus")));
				else
					grupoCliente.setEstatus(-1);
				if (request.getParameter("idN")!=null)
					id_grupo = Integer.parseInt(request.getParameter("idN"));
				grupoCliente.setId_grupo(Integer.parseInt(request.getParameter("idA")));
				grupoCliente.setId(Integer.parseInt(request.getParameter("id")));

				id_grupo_cliente = grupoAction.actualizaGrupoClienteEstatus(grupoCliente, id_grupo);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id_grupo_cliente)+"\"}");
			} catch(Exception ex){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de actualizar el registro.\", \"responseText\":\"\"}");
			}
		} else if(proceso.equals("AC3")){
			// Actualizar Estatus / Grupo del GRUPO actual
			Grupo_Cliente grupoCliente = new Grupo_Cliente();
			int id_grupo_cliente = 0;
			int id_grupo = 0;
			try{
				if (request.getParameter("estatus")!=null)
					grupoCliente.setEstatus(Integer.parseInt(request.getParameter("estatus")));
				if (request.getParameter("idN")!=null)
					id_grupo = Integer.parseInt(request.getParameter("idN"));
				grupoCliente.setId_grupo(Integer.parseInt(request.getParameter("idA")));

				id_grupo_cliente = grupoAction.actualizaGrupoClienteEstatus(grupoCliente, id_grupo);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id_grupo_cliente)+"\"}");
			} catch(Exception ex){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de actualizar el registro.\", \"responseText\":\"\"}");
			}			
		} else if(proceso.equals("AC4")){
			// Actualizar Estatus / Grupo del GRUPO actual
			int id_grupo_cliente = 0;
			try{
				String JSON_DATA = request.getParameter("info");
				System.out.println(JSON_DATA);
				JSONObject obj = new JSONObject(JSON_DATA);
				JSONArray geodata = obj.getJSONArray("info");
				int n = geodata.length();
				Grupo_Clase grupoClase = new Grupo_Clase();
				int id_grupo_clase = 0;
				JSONObject person;
				Time thInicio;
				Time thFin;
				for (int i = 0; i < n; ++i) {
					person = geodata.getJSONObject(i);
					grupoClase.setId(person.getInt("id"));
					grupoClase.setId_clase(person.getInt("c"));
					grupoClase.setId_grupo(person.getInt("g"));
					grupoClase.setId_salon(person.getInt("s"));
					grupoClase.setId_maestro(person.getInt("m"));
					grupoClase.setDia_semana(person.getInt("d"));
					thInicio = Time.valueOf(person.getString("i")+":00");
					thFin = Time.valueOf(person.getString("f")+":00");
					grupoClase.setHora_inicio(thInicio);
					grupoClase.setHora_fin(thFin);
					grupoClase.setEstatus(1);

					if (person.getInt("e")==1){
						// Actualizar registro
						id_grupo_clase = grupoAction.actualizaGrupoClase(grupoClase);
					} else {
						// Borrar registro
						grupoAction.bajaGrupoClase(grupoClase);
					}
				}
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id_grupo_cliente)+"\"}");
			} catch(Exception ex){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de actualizar el registro.\", \"responseText\":\"\"}");
			}			
		} else if(proceso.equals("BAJ")){
			Grupo_Cliente grupoCliente = new Grupo_Cliente();
			int id_grupo_cliente = 0;
			try{
				grupoCliente.setId(Integer.parseInt(request.getParameter("id")));

				id_grupo_cliente = grupoAction.bajaGrupoClienteEstatus(grupoCliente);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id_grupo_cliente)+"\"}");
			} catch(Exception ex){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de actualizar el registro.\", \"responseText\":\"\"}");
			}			
		}

		out.flush();
	}

}
