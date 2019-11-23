package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.talentos.action.clienteAction;
import com.talentos.entidad.Cliente;
import com.talentos.util.Constantes;

/**
 * Servlet implementation class clienteServlet
 */
@WebServlet("/clienteServlet")
public class clienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public clienteServlet() {
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
		//String consulta = request.getParameter("c")!=null?request.getParameter("c"):"";
		if (proceso.equals("c")){
			String resultado = clienteAction.obtenerClienteJsonTable(null, 0);
			//out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+resultado+"\"}");
			out.println(resultado);
		} else if (proceso.equals("d")){
			String matricula = request.getParameter("m");
			String resultado = clienteAction.obtenerClienteTable(matricula);
			out.println(resultado);
		}
		out.flush();
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String proceso = request.getParameter("proceso")!=null?request.getParameter("proceso"):"";
		SimpleDateFormat formatter = new SimpleDateFormat(Constantes.fechaFormato);
		SimpleDateFormat formatoFechaNacimiento = new SimpleDateFormat(Constantes.fechaFormatoUsuario);
		if (proceso.equals("ALT")){
			Cliente cliente = new Cliente();
			try {
				cliente.setFecha_alta(formatter.parse(request.getParameter("fechaAlta")));
				cliente.setNombre(request.getParameter("nombre"));
				cliente.setFecha_nacimiento(formatoFechaNacimiento.parse(request.getParameter("fechaNacimiento")));
				cliente.setCalle(request.getParameter("calle"));
				cliente.setNumero(request.getParameter("numero"));
				cliente.setColonia(request.getParameter("colonia"));
				cliente.setMunicipio(request.getParameter("municipio"));
				cliente.setCodigo_postal(request.getParameter("codigoPostal"));
				cliente.setMadre_nombre(request.getParameter("madreNombre"));
				cliente.setMadre_ocupacion(request.getParameter("madreOcupacion"));
				cliente.setMadre_telefono_casa(request.getParameter("madreTelefonoCasa"));
				cliente.setMadre_telefono_celular(request.getParameter("madreTelefonoCelular"));
				cliente.setMadre_telefono_oficina(request.getParameter("madreTelefonoOficina"));
				cliente.setMadre_email(request.getParameter("madreEmail"));
				cliente.setMadre_telefono_recado(request.getParameter("madreTelefonoRecado"));
				cliente.setPadre_nombre(request.getParameter("padreNombre"));
				cliente.setPadre_ocupacion(request.getParameter("padreOcupacion"));
				cliente.setPadre_telefono_celular(request.getParameter("padreTelefonoCelular"));
				cliente.setPadre_telefono_oficina(request.getParameter("padreTelefonoOficina"));
				cliente.setPadre_email(request.getParameter("padreEmail"));
				cliente.setEstatus(Byte.parseByte("1"));
		
				int matricula = clienteAction.guardarCliente(cliente);
				
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", matricula)+"\"}");
			} catch (Exception e){
				e.printStackTrace();
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if (proceso.equals("AL1")){
			System.out.println("PROCESO SIN USAR");
			// Alta Cliente_Clase
/*			Cliente_Clase clienteClase = new Cliente_Clase();
			try {
				clienteClase.setId_cliente(Integer.parseInt(request.getParameter("idCliente")));
				clienteClase.setId_clase(Integer.parseInt(request.getParameter("idClase")));
				clienteClase.setBeca(Float.parseFloat(request.getParameter("beca")));
				clienteClase.setComentario(request.getParameter("comentario"));
				clienteClase.setFecha_alta(request.getParameter("fechaAlta"));

				int idClienteClase = clienteClaseAction.guardarClienteClase(clienteClase);
				
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", idClienteClase)+"\"}");
			} catch (Exception e){
				System.out.println(e.getMessage());
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}*/
		} else if (proceso.equals("ACT")){
			Cliente cliente = new Cliente();
			try {
				cliente.setId(Integer.parseInt(request.getParameter("id")));
				cliente.setMatricula(Integer.parseInt(request.getParameter("matricula")));
				cliente.setNombre(request.getParameter("nombre"));
				cliente.setFecha_nacimiento(formatoFechaNacimiento.parse(request.getParameter("fechaNacimiento")));
				cliente.setCalle(request.getParameter("calle"));
				cliente.setNumero(request.getParameter("numero"));
				cliente.setColonia(request.getParameter("colonia"));
				cliente.setMunicipio(request.getParameter("municipio"));
				cliente.setCodigo_postal(request.getParameter("codigoPostal"));
				cliente.setMadre_nombre(request.getParameter("madreNombre"));
				cliente.setMadre_ocupacion(request.getParameter("madreOcupacion"));
				cliente.setMadre_telefono_casa(request.getParameter("madreTelefonoCasa"));
				cliente.setMadre_telefono_celular(request.getParameter("madreTelefonoCelular"));
				cliente.setMadre_telefono_oficina(request.getParameter("madreTelefonoOficina"));
				cliente.setMadre_email(request.getParameter("madreEmail"));
				cliente.setMadre_telefono_recado(request.getParameter("madreTelefonoRecado"));
				cliente.setPadre_nombre(request.getParameter("padreNombre"));
				cliente.setPadre_ocupacion(request.getParameter("padreOcupacion"));
				cliente.setPadre_telefono_celular(request.getParameter("padreTelefonoCelular"));
				cliente.setPadre_telefono_oficina(request.getParameter("padreTelefonoOficina"));
				cliente.setPadre_email(request.getParameter("padreEmail"));
				cliente.setEstatus(Byte.parseByte(request.getParameter("estatus")));
		
				int matricula = clienteAction.actualizaCliente(cliente);
				
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", matricula)+"\"}");
			} catch (Exception e){
				e.printStackTrace();
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		}

		out.flush();
	}

}
