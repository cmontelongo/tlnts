package com.talentos.entidad;

import java.io.Serializable;
import java.util.Date;

public class Paquete_Detalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int id_paquete;
	private int id_producto_almacen;
	private int cantidad;
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
	public int getId_paquete() {
		return id_paquete;
	}
	public void setId_paquete(int id_paquete) {
		this.id_paquete = id_paquete;
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
