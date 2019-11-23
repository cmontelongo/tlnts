package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.talentos.action.salonAction;
import com.talentos.entidad.Salon;
import com.talentos.util.Constantes;

/**
 * Servlet implementation class salonServlet
 */
@WebServlet("/salonServlet")
public class salonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public salonServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String proceso = request.getParameter("proceso")!=null?request.getParameter("proceso"):"";
		SimpleDateFormat formatter = new SimpleDateFormat(Constantes.fechaFormato);
		if (proceso.equals("ALT")){
			Salon salon = new Salon();
			try {
				salon.setNombre(request.getParameter("nombre"));
				salon.setFecha_alta(formatter.parse(request.getParameter("fechaAlta")));

				int matricula = salonAction.guardarSalon(salon);
				
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", matricula)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		}

		out.flush();
	}

}
