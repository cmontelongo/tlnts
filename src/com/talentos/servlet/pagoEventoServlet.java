package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.talentos.action.pagoEventoAction;
import com.talentos.entidad.Cliente_Pago_Evento;
import com.talentos.entidad.Concepto;
import com.talentos.entidad.Evento;

/**
 * Servlet implementation class pagoEventoServlet
 */
@WebServlet("/pagoEventoServlet")
public class pagoEventoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public pagoEventoServlet() {
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
		if (proceso.equals("ALT")){
			System.out.println("ALT");

			Evento evento = new Evento();
			String eventoId = request.getParameter("eventoId");
			if (!eventoId.equals(""))
				evento.setId(Integer.parseInt(eventoId));
			else
				evento.setNombre(request.getParameter("evento"));

			Concepto concepto = new Concepto();
			String conceptoId = request.getParameter("conceptoId");
			if (!conceptoId.equals(""))
				concepto.setId(Integer.parseInt(conceptoId));
			else
				concepto.setNombre(request.getParameter("concepto"));

			Cliente_Pago_Evento clientePagoEvento = new Cliente_Pago_Evento();
			String alumnoId = request.getParameter("alumno");
			clientePagoEvento.setId_cliente(Integer.parseInt(alumnoId));
			Calendar fechaPago = Calendar.getInstance();
			fechaPago.set(Integer.parseInt(request.getParameter("anio")), Integer.parseInt(request.getParameter("mes")), Integer.parseInt(request.getParameter("dia")));
			clientePagoEvento.setFecha_pago(fechaPago.getTime());
			clientePagoEvento.setComentario(request.getParameter("comentario"));
			clientePagoEvento.setMonto(Float.parseFloat(request.getParameter("monto")));

			int resultado = pagoEventoAction.guardarPagoEvento(evento, concepto, clientePagoEvento);
			out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+resultado+"\"}");
		} else if (proceso.equals("ACT")){
			try{
				int id = Integer.parseInt(request.getParameter("id"));
				int formaPago = Integer.parseInt(request.getParameter("m"));
				int resultado = pagoEventoAction.actualizaFormaPago(formaPago, id);
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", resultado)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de actualizar.\", \"responseText\":\"\"}");
			}
		}
		out.flush();
	}

}
