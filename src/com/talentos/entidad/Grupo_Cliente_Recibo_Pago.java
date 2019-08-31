package com.talentos.entidad;

import java.io.Serializable;
import java.util.Date;

public class Grupo_Cliente_Recibo_Pago implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int id_grupo_cliente_recibo;
	private Float monto;
	private Byte numero_mes;
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
	public int getId_grupo_cliente_recibo() {
		return id_grupo_cliente_recibo;
	}
	public void setId_grupo_cliente_recibo(int id_grupo_cliente_recibo) {
		this.id_grupo_cliente_recibo = id_grupo_cliente_recibo;
	}
	public Float getMonto() {
		return monto;
	}
	public void setMonto(Float monto) {
		this.monto = monto;
	}
	public Byte getNumero_mes() {
		return numero_mes;
	}
	public void setNumero_mes(Byte numero_mes) {
		this.numero_mes = numero_mes;
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
