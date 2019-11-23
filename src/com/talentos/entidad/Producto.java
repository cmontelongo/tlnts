package com.talentos.entidad;

import java.io.Serializable;

public class Producto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String clave;
	private String descripcion;
	private Float costo_unitario;
	private String comentario;
	private String fecha_alta;
	private String id_usuario_alta;
	private String fecha_ultima_modificacion;
	private String id_usuario_ultima_modificacion;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Float getCosto_unitario() {
		return costo_unitario;
	}
	public void setCosto_unitario(Float costo_unitario) {
		this.costo_unitario = costo_unitario;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(String fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public String getId_usuario_alta() {
		return id_usuario_alta;
	}
	public void setId_usuario_alta(String id_usuario_alta) {
		this.id_usuario_alta = id_usuario_alta;
	}
	public String getFecha_ultima_modificacion() {
		return fecha_ultima_modificacion;
	}
	public void setFecha_ultima_modificacion(String fecha_ultima_modificacion) {
		this.fecha_ultima_modificacion = fecha_ultima_modificacion;
	}
	public String getId_usuario_ultima_modificacion() {
		return id_usuario_ultima_modificacion;
	}
	public void setId_usuario_ultima_modificacion(String id_usuario_ultima_modificacion) {
		this.id_usuario_ultima_modificacion = id_usuario_ultima_modificacion;
	}

}
