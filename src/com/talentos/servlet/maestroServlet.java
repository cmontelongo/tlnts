package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.talentos.action.maestroAction;
import com.talentos.entidad.Maestro;
import com.talentos.util.Constantes;

/**
 * Servlet implementation class maestroServlet
 */
@WebServlet("/maestroServlet")
public class maestroServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public maestroServlet() {
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
		String resultado = "";
		if (proceso.equals("c")){
			if (consulta.equals("1")){
				resultado = maestroAction.obtenerMaestroJsonTable(null, 0);
			} else if (consulta.equals("2")){
				String id = request.getParameter("m");
				resultado = maestroAction.obtenerMaestroTable(id);
			} else {
				resultado = maestroAction.obtenerMaestroJsonTable(null, 0);
			}
			out.println(resultado);
		} else if (proceso.equals("d")){
			String id = request.getParameter("m");
			resultado = maestroAction.obtenerMaestroTable(id);
			out.println(resultado);
		}
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String proceso = request.getParameter("proceso")!=null?request.getParameter("proceso"):"";
		SimpleDateFormat formatter = new SimpleDateFormat(Constantes.fechaFormato);
		if (proceso.equals("ALT")){
			Maestro maestro = new Maestro();
			try {
				maestro.setNombre(request.getParameter("nombre"));
				maestro.setFecha_alta(formatter.parse(request.getParameter("fechaAlta")));

				int matricula = maestroAction.guardarMaestro(maestro);
				
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", matricula)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		}

		out.flush();
	}

}
