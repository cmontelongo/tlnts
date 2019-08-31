package com.talentos.entidad;

import java.io.Serializable;

public class Cliente_Pago_Clase implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int id_cliente_clase;
	private String fecha_pago;
	private float pago;
	private float adeudo;
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
	public int getId_cliente_clase() {
		return id_cliente_clase;
	}
	public void setId_cliente_clase(int id_cliente_clase) {
		this.id_cliente_clase = id_cliente_clase;
	}
	public String getFecha_pago() {
		return fecha_pago;
	}
	public void setFecha_pago(String fecha_pago) {
		this.fecha_pago = fecha_pago;
	}
	public float getPago() {
		return pago;
	}
	public void setPago(float pago) {
		this.pago = pago;
	}
	public float getAdeudo() {
		return adeudo;
	}
	public void setAdeudo(float adeudo) {
		this.adeudo = adeudo;
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
