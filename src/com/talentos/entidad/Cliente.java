package com.talentos.entidad;

import java.io.Serializable;
import java.util.Date;

public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int matricula;
	private String nombre;
	private Date fecha_nacimiento;
	private String calle;
	private String numero;
	private String colonia;
	private String municipio;
	private String codigo_postal;
	private String madre_nombre;
	private String madre_ocupacion;
	private String madre_telefono_casa;
	private String madre_telefono_celular;
	private String madre_telefono_oficina;
	private String madre_email;
	private String madre_telefono_recado;
	private String padre_nombre;
	private String padre_ocupacion;
	private String padre_telefono_celular;
	private String padre_telefono_oficina;
	private String padre_email;
	private Byte estatus;
	private Date fecha_alta;
	private Date fecha_baja;
	private String comentario;
	private int id_usuario_alta;
	private Date fecha_ultima_modificacion;
	private int id_usuario_ultima_modificacion;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMatricula() {
		return matricula;
	}
	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFecha_nacimiento() {
		return fecha_nacimiento;
	}
	public void setFecha_nacimiento(Date fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getColonia() {
		return colonia;
	}
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getCodigo_postal() {
		return codigo_postal;
	}
	public void setCodigo_postal(String codigo_postal) {
		this.codigo_postal = codigo_postal;
	}
	public String getMadre_nombre() {
		return madre_nombre;
	}
	public void setMadre_nombre(String madre_nombre) {
		this.madre_nombre = madre_nombre;
	}
	public String getMadre_ocupacion() {
		return madre_ocupacion;
	}
	public void setMadre_ocupacion(String madre_ocupacion) {
		this.madre_ocupacion = madre_ocupacion;
	}
	public String getMadre_telefono_casa() {
		return madre_telefono_casa;
	}
	public void setMadre_telefono_casa(String madre_telefono_casa) {
		this.madre_telefono_casa = madre_telefono_casa;
	}
	public String getMadre_telefono_celular() {
		return madre_telefono_celular;
	}
	public void setMadre_telefono_celular(String madre_telefono_celular) {
		this.madre_telefono_celular = madre_telefono_celular;
	}
	public String getMadre_telefono_oficina() {
		return madre_telefono_oficina;
	}
	public void setMadre_telefono_oficina(String madre_telefono_oficina) {
		this.madre_telefono_oficina = madre_telefono_oficina;
	}
	public String getMadre_email() {
		return madre_email;
	}
	public void setMadre_email(String madre_email) {
		this.madre_email = madre_email;
	}
	public String getMadre_telefono_recado() {
		return madre_telefono_recado;
	}
	public void setMadre_telefono_recado(String madre_telefono_recado) {
		this.madre_telefono_recado = madre_telefono_recado;
	}
	public String getPadre_nombre() {
		return padre_nombre;
	}
	public void setPadre_nombre(String padre_nombre) {
		this.padre_nombre = padre_nombre;
	}
	public String getPadre_ocupacion() {
		return padre_ocupacion;
	}
	public void setPadre_ocupacion(String padre_ocupacion) {
		this.padre_ocupacion = padre_ocupacion;
	}
	public String getPadre_telefono_celular() {
		return padre_telefono_celular;
	}
	public void setPadre_telefono_celular(String padre_telefono_celular) {
		this.padre_telefono_celular = padre_telefono_celular;
	}
	public String getPadre_telefono_oficina() {
		return padre_telefono_oficina;
	}
	public void setPadre_telefono_oficina(String padre_telefono_oficina) {
		this.padre_telefono_oficina = padre_telefono_oficina;
	}
	public String getPadre_email() {
		return padre_email;
	}
	public void setPadre_email(String padre_email) {
		this.padre_email = padre_email;
	}
	public Byte getEstatus() {
		return estatus;
	}
	public void setEstatus(Byte estatus) {
		this.estatus = estatus;
	}
	public Date getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
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
	
/*	private int id;
	private String nombre;
	private int matricula;
	private String fecha_nacimiento;
	private String calle;
	private String numero;
	private String colonia;
	private String municipio;
	private String codigo_postal;
	private String madre_nombre;
	private String madre_ocupacion;
	private String madre_telefono_casa;
	private String madre_telefono_celular;
	private String madre_telefono_oficina;
	private String madre_email;
	private String madre_telefono_recado;
	private String padre_nombre;
	private String padre_ocupacion;
	private String padre_telefono_celular;
	private String padre_telefono_oficina;
	private String padre_email;
	private Byte estatus;
	private String fecha_alta;
	private String fecha_baja;
	private String comentario;
	private String id_usuario_alta;
	private String fecha_ultima_modificacion;
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
	public int getMatricula() {
		return matricula;
	}
	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}
	public String getFecha_nacimiento() {
		return fecha_nacimiento;
	}
	public void setFecha_nacimiento(String fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getColonia() {
		return colonia;
	}
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getCodigo_postal() {
		return codigo_postal;
	}
	public void setCodigo_postal(String codigo_postal) {
		this.codigo_postal = codigo_postal;
	}
	public String getMadre_nombre() {
		return madre_nombre;
	}
	public void setMadre_nombre(String madre_nombre) {
		this.madre_nombre = madre_nombre;
	}
	public String getMadre_ocupacion() {
		return madre_ocupacion;
	}
	public void setMadre_ocupacion(String madre_ocupacion) {
		this.madre_ocupacion = madre_ocupacion;
	}
	public String getMadre_telefono_casa() {
		return madre_telefono_casa;
	}
	public void setMadre_telefono_casa(String madre_telefono_casa) {
		this.madre_telefono_casa = madre_telefono_casa;
	}
	public String getMadre_telefono_celular() {
		return madre_telefono_celular;
	}
	public void setMadre_telefono_celular(String madre_telefono_celular) {
		this.madre_telefono_celular = madre_telefono_celular;
	}
	public String getMadre_telefono_oficina() {
		return madre_telefono_oficina;
	}
	public void setMadre_telefono_oficina(String madre_telefono_oficina) {
		this.madre_telefono_oficina = madre_telefono_oficina;
	}
	public String getMadre_email() {
		return madre_email;
	}
	public void setMadre_email(String madre_email) {
		this.madre_email = madre_email;
	}
	public String getMadre_telefono_recado() {
		return madre_telefono_recado;
	}
	public void setMadre_telefono_recado(String madre_telefono_recado) {
		this.madre_telefono_recado = madre_telefono_recado;
	}
	public String getPadre_nombre() {
		return padre_nombre;
	}
	public void setPadre_nombre(String padre_nombre) {
		this.padre_nombre = padre_nombre;
	}
	public String getPadre_ocupacion() {
		return padre_ocupacion;
	}
	public void setPadre_ocupacion(String padre_ocupacion) {
		this.padre_ocupacion = padre_ocupacion;
	}
	public String getPadre_telefono_celular() {
		return padre_telefono_celular;
	}
	public void setPadre_telefono_celular(String padre_telefono_celular) {
		this.padre_telefono_celular = padre_telefono_celular;
	}
	public String getPadre_telefono_oficina() {
		return padre_telefono_oficina;
	}
	public void setPadre_telefono_oficina(String padre_telefono_oficina) {
		this.padre_telefono_oficina = padre_telefono_oficina;
	}
	public String getPadre_email() {
		return padre_email;
	}
	public void setPadre_email(String padre_email) {
		this.padre_email = padre_email;
	}
	public Byte getEstatus() {
		return estatus;
	}
	public void setEstatus(Byte estatus) {
		this.estatus = estatus;
	}
	public String getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(String fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public String getFecha_baja() {
		return fecha_baja;
	}
	public void setFecha_baja(String fecha_baja) {
		this.fecha_baja = fecha_baja;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
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
	}*/

}
