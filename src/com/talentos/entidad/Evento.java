package com.talentos.entidad;

import java.io.Serializable;
import java.util.Date;

public class Evento implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String nombre;
	private String descripcion = "";
	private Date fecha = new Date();
	private int numero_dias = 0;
	private String lugar = "";
	private Float presupuesto = 0f;
	private String comentario = "";
	private Date fecha_alta;
	private int id_usuario_alta;
	private Date fecha_ultima_modificacion;
	private int id_usuario_ultima_modificacion;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getNumero_dias() {
		return numero_dias;
	}
	public void setNumero_dias(int numero_dias) {
		this.numero_dias = numero_dias;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public Float getPresupuesto() {
		return presupuesto;
	}
	public void setPresupuesto(Float presupuesto) {
		this.presupuesto = presupuesto;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Date getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public int getId_usuario_alta() {
		return id_usuario_alta;
	}
	public void setId_usuario_alta(int id_usuario_alta) {
		this.id_usuario_alta = id_usuario_alta;
	}
	public Date getFecha_ultima_modificacion() {
		return fecha_ultima_modificacion;
	}
	public void setFecha_ultima_modificacion(Date fecha_ultima_modificacion) {
		this.fecha_ultima_modificacion = fecha_ultima_modificacion;
	}
	public int getId_usuario_ultima_modificacion() {
		return id_usuario_ultima_modificacion;
	}
	public void setId_usuario_ultima_modificacion(int id_usuario_ultima_modificacion) {
		this.id_usuario_ultima_modificacion = id_usuario_ultima_modificacion;
	}

}
