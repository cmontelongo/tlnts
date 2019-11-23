package com.talentos.entidad;

import java.io.Serializable;
import java.util.Date;

public class Cliente_Recibo_Pago implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int id_cliente_recibo;
	private Float monto_pagado;
	private int id_concepto;
	private int id_evento;
	private String comentario;
	private Date fecha_pago;
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
	public int getId_cliente_recibo() {
		return id_cliente_recibo;
	}
	public void setId_cliente_recibo(int id_cliente_recibo) {
		this.id_cliente_recibo = id_cliente_recibo;
	}
	public Float getMonto_pagado() {
		return monto_pagado;
	}
	public void setMonto_pagado(Float monto_pagado) {
		this.monto_pagado = monto_pagado;
	}
	public int getId_Concepto() {
		return id_concepto;
	}
	public void setId_Concepto(int id_concepto) {
		this.id_concepto = id_concepto;
	}
	public int getId_Evento() {
		return id_evento;
	}
	public void setId_Evento(int id_evento) {
		this.id_evento = id_evento;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Date getFecha_pago() {
		return fecha_pago;
	}
	public void setFecha_pago(Date fecha_pago) {
		this.fecha_pago = fecha_pago;
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
