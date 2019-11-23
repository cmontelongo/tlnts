package com.talentos.entidad;

import java.io.Serializable;
import java.util.Date;

public class Producto_Inventario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id = 0;
	private int id_producto_almacen;
	private int cantidad;
	private Float precio_venta_ultima;
	private int id_usuario_alta;
	private Date fecha_alta;
	private int id_usuario_ultima_modificacion;
	private Date fecha_ultima_modificacion;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_producto_almacen() {
		return id_producto_almacen;
	}
	public void setId_producto_almacen(int id_producto_almacen) {
		this.id_producto_almacen = id_producto_almacen;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public Float getPrecio_venta_ultima() {
		if (precio_venta_ultima == null){
			precio_venta_ultima = 0f;
		}
		return precio_venta_ultima;
	}
	public void setPrecio_venta_ultima(Float precio_venta_ultima) {
		this.precio_venta_ultima = precio_venta_ultima;
	}
	public int getId_usuario_alta() {
		return id_usuario_alta;
	}
	public void setId_usuario_alta(int id_usuario_alta) {
		this.id_usuario_alta = id_usuario_alta;
	}
	public Date getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public int getId_usuario_ultima_modificacion() {
		return id_usuario_ultima_modificacion;
	}
	public void setId_usuario_ultima_modificacion(int id_usuario_ultima_modificacion) {
		this.id_usuario_ultima_modificacion = id_usuario_ultima_modificacion;
	}
	public Date getFecha_ultima_modificacion() {
		return fecha_ultima_modificacion;
	}
	public void setFecha_ultima_modificacion(Date fecha_ultima_modificacion) {
		this.fecha_ultima_modificacion = fecha_ultima_modificacion;
	}
	
}
