package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.talentos.action.clientePagoClaseAction;
import com.talentos.action.pagoAction;
import com.talentos.entidad.Grupo_Cliente_Recibo;
import com.talentos.entidad.Grupo_Cliente_Recibo_Pago;
import com.talentos.util.Constantes;

/**
 * Servlet implementation class pagoServlet
 */
@WebServlet("/pagoServlet")
public class pagoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public pagoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id")!=null?request.getParameter("id"):"";
		String proceso = request.getParameter("p")!=null?request.getParameter("p"):"1";
		StringBuilder resultado = new StringBuilder();
		if (proceso.equals("1")){
			resultado = pagoAction.obtenerPagoRecibo(id,"",false);
		} else if (proceso.equals("3")) {
			resultado = pagoAction.obtenerPagoRecibo(id,"",true);
		} else if (proceso.equals("4")) {
			response.setContentType("text/plain");
			String consulta = request.getParameter("p")!=null?request.getParameter("c"):"1";
			if (consulta.equals("1")){
				resultado = pagoAction.obtenerEstadisticaPagoRecibo(id);
			} else if (consulta.equals("2")){
				resultado = pagoAction.obtenerEstadisticaPagoDia(id);
			}
		} else {
			try {
				resultado = pagoAction.service(id);
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out.println(resultado.toString());
		out.flush();
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String proceso = request.getParameter("proceso")!=null?request.getParameter("proceso"):"";
		SimpleDateFormat formatter = new SimpleDateFormat(Constantes.fechaFormato);
		if (proceso.equals("ALT")){
			Grupo_Cliente_Recibo grupoClienteRecibo = new Grupo_Cliente_Recibo();
			try{
				Float monto = Float.parseFloat(request.getParameter("monto"));
				Float parcial = Float.parseFloat(request.getParameter("parcial").replaceAll("[$,]", ""));
				Byte estatus = 0;
				if (parcial>=monto){
					estatus = 1;
				}
				grupoClienteRecibo.setId_grupo_cliente(Integer.parseInt(request.getParameter("pagoId")));
				grupoClienteRecibo.setNumero_mes(Byte.parseByte(request.getParameter("mes")));
				grupoClienteRecibo.setMonto(monto);
				grupoClienteRecibo.setEstatus(estatus);
				grupoClienteRecibo.setFecha_alta(formatter.parse(request.getParameter("fechaAlta")));
				grupoClienteRecibo.setComentario(request.getParameter("comentario"));

				int idGrupoClienteRecibo = clientePagoClaseAction.guardarGrupoClienteRecibo(grupoClienteRecibo);

				Grupo_Cliente_Recibo_Pago grupoClienteReciboPago = new Grupo_Cliente_Recibo_Pago();
				grupoClienteReciboPago.setId_grupo_cliente_recibo(idGrupoClienteRecibo);
				grupoClienteReciboPago.setMonto(parcial);
				grupoClienteReciboPago.setNumero_mes(Byte.parseByte(request.getParameter("mes")));
				grupoClienteReciboPago.setFecha_alta(formatter.parse(request.getParameter("fechaAlta")));
				
				int resultado = clientePagoClaseAction.guardarGrupoClienteReciboPago(grupoClienteReciboPago);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", resultado)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if (proceso.equals("ACT")){
			try{
				int id = Integer.parseInt(request.getParameter("id"));
				int formaPago = Integer.parseInt(request.getParameter("m"));
				int resultado = clientePagoClaseAction.actualizaFormaPago(formaPago, id);
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", resultado)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de actualizar.\", \"responseText\":\"\"}");
			}
		}

		out.flush();
	}

}
