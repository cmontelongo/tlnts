package com.talentos.entidad;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Grupo_Clase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String nombre = "";
	private int id_grupo;
	private int id_clase;
	private int id_salon;
	private int dia;
	private int dia_semana;
	private int id_maestro;
	private Time hora_inicio;
	private Time hora_fin;
	private String descripcion = "";
	private float monto = 0;
	private Date fecha_baja;
	private String comentario ="";
	private int estatus = 1;
	private Date fecha_alta;
	private String id_usuario_alta;
	private Date fecha_ultima_modificacion;
	private String id_usuario_ultima_modificacion;

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
	public int getId_grupo() {
		return id_grupo;
	}
	public void setId_grupo(int id_grupo) {
		this.id_grupo = id_grupo;
	}
	public int getId_clase() {
		return id_clase;
	}
	public void setId_clase(int id_clase) {
		this.id_clase = id_clase;
	}
	public int getId_salon() {
		return id_salon;
	}
	public void setId_salon(int id_salon) {
		this.id_salon = id_salon;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getDia_semana() {
		return dia_semana;
	}
	public void setDia_semana(int dia_semana) {
		this.dia_semana = dia_semana;
	}
	public int getId_maestro() {
		return id_maestro;
	}
	public void setId_maestro(int id_maestro) {
		this.id_maestro = id_maestro;
	}
	public Time getHora_inicio() {
		return hora_inicio;
	}
	public void setHora_inicio(Time hora_inicio) {
		this.hora_inicio = hora_inicio;
	}
	public Time getHora_fin() {
		return hora_fin;
	}
	public void setHora_fin(Time hora_fin) {
		this.hora_fin = hora_fin;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public float getMonto() {
		return monto;
	}
	public void setMonto(float monto) {
		this.monto = monto;
	}
	public Date getFecha_baja() {
		return fecha_baja;
	}
	public void setFecha_baja(Date fecha_baja) {
		this.fecha_baja = fecha_baja;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public int getEstatus() {
		return estatus;
	}
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	public Date getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public String getId_usuario_alta() {
		return id_usuario_alta;
	}
	public void setId_usuario_alta(String id_usuario_alta) {
		this.id_usuario_alta = id_usuario_alta;
	}
	public Date getFecha_ultima_modificacion() {
		return fecha_ultima_modificacion;
	}
	public void setFecha_ultima_modificacion(Date fecha_ultima_modificacion) {
		this.fecha_ultima_modificacion = fecha_ultima_modificacion;
	}
	public String getId_usuario_ultima_modificacion() {
		return id_usuario_ultima_modificacion;
	}
	public void setId_usuario_ultima_modificacion(String id_usuario_ultima_modificacion) {
		this.id_usuario_ultima_modificacion = id_usuario_ultima_modificacion;
	}

}
