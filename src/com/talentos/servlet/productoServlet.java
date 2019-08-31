package com.talentos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.talentos.action.clienteReciboAction;
import com.talentos.action.productoAction;
import com.talentos.entidad.Cliente_Recibo;
import com.talentos.entidad.Cliente_Recibo_Detalle;
import com.talentos.entidad.Paquete;
import com.talentos.entidad.Paquete_Detalle;
import com.talentos.entidad.Producto;
import com.talentos.entidad.Producto_Almacen;
import com.talentos.entidad.Producto_Almacen_Inventario;

/**
 * Servlet implementation class inventarioServlet
 */
@WebServlet("/productoServlet")
public class productoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public productoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		int id = Integer.parseInt(request.getParameter("id"));
		String proceso = request.getParameter("p")!=null?request.getParameter("p"):"";
		String opcion = request.getParameter("o")!=null?request.getParameter("o"):"";
		String resultado = "";
		if (proceso.equals("1")){
			String consulta = productoAction.obtenerPaqueteVenta(id,"", opcion);
			out.println(consulta);
		} else if(proceso.equals("2")){
			try {
				resultado = productoAction.service(id).toString();
				System.out.println("resultado= "+resultado);
				out.println(resultado);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		} else if(proceso.equals("3")){
			StringBuilder consulta = productoAction.obtenerInventario();
			out.println(consulta.toString());
		} else if(proceso.equals("4")){
			response.setContentType("application/json");
			String consulta = request.getParameter("c")!=null?request.getParameter("c"):"";
			boolean esParaVenta = request.getParameter("v")==null?false:true;
			if (consulta.equals("1")){
				StringBuilder result = productoAction.obtenerProductoAlmacenJson(id, esParaVenta);
				out.println(result.toString());
			}

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
			Producto producto = new Producto();
			try {
				producto.setFecha_alta(request.getParameter("fechaAlta"));
				producto.setClave(request.getParameter("clave"));
				producto.setDescripcion(request.getParameter("descripcion"));
				producto.setComentario(request.getParameter("comentario"));

				int matricula = productoAction.guardarProducto(producto);
				
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", matricula)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if (proceso.equals("AL1")){
			// Alta de ProductoAlmacen
			Producto_Almacen productoAlmacen = new Producto_Almacen();
			try {
				productoAlmacen.setId_producto(Integer.parseInt(request.getParameter("clave")));
				productoAlmacen.setId_talla(Integer.parseInt(request.getParameter("talla")));
//				productoAlmacen.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
//				Float costo = 0f;
//				if (!request.getParameter("costo").equals("")){
//					costo = Float.parseFloat(request.getParameter("costo"));
//				}
//				productoAlmacen.setPrecio_venta(costo);
//				Float precio = 0f;
//				if (!request.getParameter("precio").equals("")){
//					precio = Float.parseFloat(request.getParameter("precio"));
//				}
//				productoAlmacen.setPrecio_compra(precio);
				productoAlmacen.setComentario(request.getParameter("comentario"));

				int idAlmacen = -1;
				if (request.getParameter("almacen")!=null){
					idAlmacen = Integer.parseInt(request.getParameter("almacen"));
					productoAlmacen.setId_almacen(idAlmacen);
				}
				int matricula = productoAction.guardarProductoAlmacen(productoAlmacen);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", matricula)+"\"}");
			} catch (Exception e){
				e.printStackTrace();
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if (proceso.equals("AL2")){
			Paquete paquete = new Paquete();
			Paquete_Detalle paqueteDetalle = new Paquete_Detalle();
			try{
				paquete.setNombre(request.getParameter("paquete"));
				if (request.getParameter("compra")!=null && !request.getParameter("compra").equals("")){
					paquete.setPrecio_compra(Float.parseFloat(request.getParameter("compra")));
				}
				paquete.setPrecio_venta(Float.parseFloat(request.getParameter("venta")));
				paquete.setComentario(request.getParameter("comentario")!=null?request.getParameter("comentario"):"");

				int registro = productoAction.guardarPaquete(paquete);

				int registroDetalle = 0;
				if (registro>0){
					JSONArray jsonA = new JSONArray(request.getParameter("productos"));
					if (jsonA!=null && jsonA.length()>0){
						for (int i=0; i<jsonA.length(); i++){
							JSONObject jsonO = jsonA.getJSONObject(i);
							paqueteDetalle.setId_paquete(registro);
							paqueteDetalle.setId_producto_almacen(jsonO.getInt("id"));
							paqueteDetalle.setCantidad(Integer.parseInt(jsonO.getString("c")));

							registroDetalle = productoAction.guardarPaqueteDetalle(paqueteDetalle);
							System.out.println(registroDetalle);
						}
					}
				}
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", registro)+"\"}");
			} catch(Exception ex){
				ex.printStackTrace();
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if (proceso.equals("AL3")){
			// Venta Paquete/Producto
			Cliente_Recibo clienteRecibo = new Cliente_Recibo();
			Cliente_Recibo_Detalle clienteReciboDetalle = new Cliente_Recibo_Detalle();
			Float monto = 0.0f;
			Float venta = 0.0f;
			int registro = 0;
			int registroDetalle;
			int cantidad;
			try{
				Map<String,String[]> params = request.getParameterMap();
				boolean inventarioDisponible = productoAction.validaInventario(params);
				if (inventarioDisponible){
					// Si existe inventario disponible
					if (request.getParameter("cliente").equals("")){
						clienteRecibo.setId_cliente(0);
					} else {
						clienteRecibo.setId_cliente(Integer.parseInt(request.getParameter("cliente")));
					}
					clienteRecibo.setNombre_cliente(request.getParameter("clienteN"));
					clienteRecibo.setComentario(request.getParameter("comentario"));
					registro = clienteReciboAction.guardarClienteRecibo(clienteRecibo);
	
					JSONArray jsonA = new JSONArray(request.getParameter("productos"));
					if (jsonA!=null && jsonA.length()>0){
						for (int i=0; i<jsonA.length(); i++){
							clienteReciboDetalle = new Cliente_Recibo_Detalle();
							JSONObject jsonO = jsonA.getJSONObject(i);
							clienteReciboDetalle.setId_cliente_recibo(registro);
							clienteReciboDetalle.setId_producto_almacen(jsonO.getInt("id"));
							cantidad = jsonO.getInt("c");
							venta = jsonO.getFloat("v");
							monto = monto + (cantidad * venta);
							clienteReciboDetalle.setCantidad(cantidad);
							clienteReciboDetalle.setPrecio_venta(venta);
	
							registroDetalle = clienteReciboAction.guardarClienteReciboDetalle(clienteReciboDetalle);
							System.out.println(registroDetalle);
						}
					}
					jsonA = new JSONArray(request.getParameter("paquetes"));
					if (jsonA!=null && jsonA.length()>0){
						for (int i=0; i<jsonA.length(); i++){
							clienteReciboDetalle = new Cliente_Recibo_Detalle();
							JSONObject jsonO = jsonA.getJSONObject(i);
							clienteReciboDetalle.setId_cliente_recibo(registro);
							clienteReciboDetalle.setId_paquete(jsonO.getInt("id"));
							cantidad = jsonO.getInt("c");
							venta = jsonO.getFloat("v");
							monto = monto + (cantidad * venta);
							clienteReciboDetalle.setCantidad(cantidad);
							clienteReciboDetalle.setPrecio_venta(venta);
	
							registroDetalle = clienteReciboAction.guardarClienteReciboDetalle(clienteReciboDetalle);
							System.out.println(registroDetalle);
						}
					}
					clienteRecibo.setMonto(monto);
					clienteRecibo.setId(registro);
					clienteReciboAction.actualizaClienteRecibo(clienteRecibo);

					out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", registro)+"\"}");
				} else {
					out.println("{\"success\":false, \"error\":\"No hay existencia del producto solicitado.\", \"responseText\":\"\"}");
				}
				

			} catch(Exception ex){
				ex.printStackTrace();
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if (proceso.equals("AL4")){
			// Alta ProductoAlmacenInventario
			Producto_Almacen_Inventario pai = new Producto_Almacen_Inventario();
			try {
				pai.setId_producto_almacen(Integer.parseInt(request.getParameter("clave")));
				pai.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
				Float precioVenta = 0f;
				if (!request.getParameter("costo").equals("")){
					precioVenta = Float.parseFloat(request.getParameter("costo"));
				}
				pai.setPrecio_venta(precioVenta);
				Float precioCompra = 0f;
				if (!request.getParameter("precio").equals("")){
					precioCompra = Float.parseFloat(request.getParameter("precio"));
				}
				pai.setPrecio_compra(precioCompra);
				pai.setComentario(request.getParameter("comentario"));

				int idPAI = productoAction.guardarProductoAlmacenInventario(pai);

				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", idPAI)+"\"}");
			} catch (Exception e){
				e.printStackTrace();
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de crear el nuevo registro.\", \"responseText\":\"\"}");
			}
		} else if (proceso.equals("ACT")){
			try{
				int id = Integer.parseInt(request.getParameter("id"));
				int formaPago = Integer.parseInt(request.getParameter("m"));
				int resultado = productoAction.actualizaFormaPago(formaPago, id);
				out.println("{\"success\":true, \"error\":\"\", \"responseText\":\""+String.format("%06d", resultado)+"\"}");
			} catch (Exception e){
				out.println("{\"success\":false, \"error\":\"Ocurrio un error al momento de actualizar.\", \"responseText\":\"\"}");
			}
		}

		out.flush();
	}

}
