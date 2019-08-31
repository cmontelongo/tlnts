package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.talentos.action.claseAction;
import com.talentos.entidad.Clase;

/**
 * Servlet implementation class claseServlet
 */
@WebServlet("/claseServlet")
public class claseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public claseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String proceso = request.getParameter("p")!=null?request.getParameter("p"):"";
		if (proceso.equals("c")){
			String resultado = claseAction.obtenerClaseJson();
			//resultado = "[{'id':'1','label':'Ab'}]";
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
		if (proceso.equals("ALT")){
			Clase clase = new Clase();
			try {
				clase.setNombre(request.getParameter("nombre"));
				clase.setComentario(request.getParameter("comentario"));
				clase.setEstatus(1);

				int matricula = claseAction.guardarClase(clase);
				
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", matricula)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		}

		out.flush();
	}

}
