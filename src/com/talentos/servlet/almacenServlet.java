package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.talentos.action.almacenAction;
import com.talentos.entidad.Almacen;
import com.talentos.entidad.Producto_Almacen;

/**
 * Servlet implementation class inventarioServlet
 */
@WebServlet("/productoServlet")
public class almacenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public almacenServlet() {
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
		System.out.println(proceso);
		if (proceso.equals("ALT")){
			Almacen almacen = new Almacen();
			try {
				almacen.setClave(request.getParameter("clave"));
				almacen.setNombre(request.getParameter("nombre"));
				almacen.setComentario(request.getParameter("comentario"));

				int id = almacenAction.guardarAlmacen(almacen);
				
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", id)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if(proceso.equals("AL1")){
			Producto_Almacen productoAlmacen = new Producto_Almacen();
			try {
				productoAlmacen.setId_producto(Integer.parseInt(request.getParameter("clave")));
				productoAlmacen.setId_almacen(Integer.parseInt(request.getParameter("almacen")));

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", 0)+"\"}");
			} catch (Exception ex){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		}

		out.flush();
	}

}
