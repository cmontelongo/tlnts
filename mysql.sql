CREATE DATABASE /*!32312 IF NOT EXISTS*/ "talentos" /*!40100 DEFAULT CHARACTER SET latin1 */;

USE "talentos";


DROP TABLE IF EXISTS "almacen";
CREATE TABLE "almacen" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "clave" varchar(64) DEFAULT NULL,
  "nombre" varchar(128) DEFAULT NULL,
  "descripcion" varchar(255) DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_almacen_id" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "clase";
CREATE TABLE "clase" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(64) DEFAULT NULL,
  "estatus" int(11) DEFAULT NULL,
  "comentario" varchar(128) DEFAULT NULL,
  "fecha_alta" datetime DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_grupo_id" ("id"),
  KEY "fk_clase_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_clase_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_clase_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_clase_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "cliente";
CREATE TABLE "cliente" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "matricula" int(11) DEFAULT NULL,
  "nombre" varchar(255) DEFAULT NULL,
  "fecha_nacimiento" datetime DEFAULT NULL,
  "calle" varchar(512) DEFAULT NULL,
  "numero" varchar(10) DEFAULT NULL,
  "colonia" varchar(512) DEFAULT NULL,
  "municipio" varchar(512) DEFAULT NULL,
  "codigo_postal" varchar(7) DEFAULT NULL,
  "madre_nombre" varchar(255) DEFAULT NULL,
  "madre_ocupacion" varchar(512) DEFAULT NULL,
  "madre_telefono_casa" varchar(15) DEFAULT NULL,
  "madre_telefono_celular" varchar(15) DEFAULT NULL,
  "madre_telefono_oficina" varchar(20) DEFAULT NULL,
  "madre_email" varchar(255) DEFAULT NULL,
  "madre_telefono_recado" varchar(15) DEFAULT NULL,
  "padre_nombre" varchar(255) DEFAULT NULL,
  "padre_ocupacion" varchar(512) DEFAULT NULL,
  "padre_telefono_celular" varchar(15) DEFAULT NULL,
  "padre_telefono_oficina" varchar(20) DEFAULT NULL,
  "padre_email" varchar(255) DEFAULT NULL,
  "estatus" bit(1) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "fecha_baja" datetime DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "cliente_id" ("id","estatus"),
  KEY "fk_cliente_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_cliente_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_cliente_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_cliente_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=380 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "cliente_pago_evento";
CREATE TABLE "cliente_pago_evento" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_cliente" int(11) NOT NULL,
  "id_evento" int(11) NOT NULL,
  "id_concepto" int(11) NOT NULL,
  "monto" float(12,4) DEFAULT NULL,
  "fecha_pago" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "forma_pago" int(11) DEFAULT '0',
  "comentario" varchar(128) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_cliente_pago_evento" ("id"),
  KEY "fk_cliente_pago_evento_id_cliente" ("id_cliente"),
  KEY "fk_cliente_pago_evento_id_evento" ("id_evento"),
  KEY "fk_cliente_pago_evento_id_concepto" ("id_concepto"),
  KEY "fk_cliente_pago_evento_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_cliente_pago_evento_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_cliente_pago_evento_id_cliente" FOREIGN KEY ("id_cliente") REFERENCES "cliente" ("id"),
  CONSTRAINT "fk_cliente_pago_evento_id_concepto" FOREIGN KEY ("id_concepto") REFERENCES "concepto" ("id"),
  CONSTRAINT "fk_cliente_pago_evento_id_evento" FOREIGN KEY ("id_evento") REFERENCES "evento" ("id"),
  CONSTRAINT "fk_cliente_pago_evento_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_cliente_pago_evento_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=982 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "cliente_recibo";
CREATE TABLE "cliente_recibo" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_cliente" int(11) DEFAULT NULL,
  "nombre_cliente" varchar(255) DEFAULT NULL,
  "monto" float(14,4) DEFAULT NULL,
  "comentario" varchar(255) DEFAULT NULL,
  "fecha_recibo" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "forma_pago" int(11) DEFAULT '0',
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_producto_almacen_cliente_recibo" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "cliente_recibo_detalle";
CREATE TABLE "cliente_recibo_detalle" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_cliente_recibo" int(11) DEFAULT NULL,
  "id_producto_almacen" int(11) DEFAULT NULL,
  "id_paquete" int(11) DEFAULT NULL,
  "cantidad" int(11) DEFAULT NULL,
  "precio_venta" float(14,4) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_cliente_recibo_detalle_id" ("id"),
  KEY "fk_cliente_recibo_detalle_id_cliente_recibo" ("id_cliente_recibo"),
  CONSTRAINT "fk_cliente_recibo_detalle_id_cliente_recibo" FOREIGN KEY ("id_cliente_recibo") REFERENCES "cliente_recibo" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "cliente_recibo_pago";
CREATE TABLE "cliente_recibo_pago" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_cliente_recibo" int(11) DEFAULT NULL,
  "monto_pagado" float(18,4) DEFAULT NULL,
  "fecha_pago" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_cliente_recibo_pago" ("id"),
  KEY "fk_cliente_recibo_pago_id_cliente_recibo" ("id_cliente_recibo"),
  CONSTRAINT "fk_cliente_recibo_pago_id_cliente_recibo" FOREIGN KEY ("id_cliente_recibo") REFERENCES "cliente_recibo" ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "concepto";
CREATE TABLE "concepto" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(255) DEFAULT NULL,
  "descripcion" varchar(512) DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "estatus" smallint(6) NOT NULL DEFAULT '1',
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_concepto_id" ("id"),
  KEY "fk_concepto_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_concepto_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_concepto_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_concepto_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "evento";
CREATE TABLE "evento" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(255) DEFAULT NULL,
  "descripcion" varchar(512) DEFAULT NULL,
  "fecha" datetime DEFAULT NULL,
  "numero_dias" smallint(6) DEFAULT NULL,
  "lugar" varchar(255) DEFAULT NULL,
  "presupuesto" float(14,4) DEFAULT NULL,
  "estatus" smallint(6) NOT NULL DEFAULT '1',
  "comentario" varchar(512) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_evento_id" ("id"),
  KEY "fk_evento_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_evento_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_evento_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_evento_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "grupo";
CREATE TABLE "grupo" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(64) DEFAULT NULL,
  "estatus" int(11) DEFAULT NULL,
  "particular" tinyint(4) DEFAULT '0',
  "comentario" varchar(128) DEFAULT NULL,
  "fecha_alta" datetime DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_grupo_id" ("id"),
  KEY "fk_grupo_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_grupo_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_grupo_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_grupo_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "grupo_clase";
CREATE TABLE "grupo_clase" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(512) DEFAULT NULL,
  "id_grupo" int(11) DEFAULT NULL,
  "id_clase" int(11) DEFAULT NULL,
  "id_salon" int(11) DEFAULT NULL,
  "dia" int(11) DEFAULT NULL,
  "dia_semana" int(11) DEFAULT NULL,
  "id_maestro" int(11) DEFAULT NULL,
  "hora_inicio" time DEFAULT NULL,
  "hora_fin" time DEFAULT NULL,
  "descripcion" varchar(512) DEFAULT NULL,
  "monto" float(10,4) DEFAULT NULL,
  "fecha_baja" datetime DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "estatus" int(11) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_clase_id" ("id"),
  KEY "fk_grupo_clase_usuario_alta" ("id_usuario_alta"),
  KEY "fk_grupo_clase_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  KEY "fk_grupo_clase_maestro" ("id_maestro"),
  KEY "fk_grupo_clase_clase" ("id_clase"),
  KEY "fk_grupo_clase_grupo" ("id_grupo"),
  CONSTRAINT "fk_grupo_clase_clase" FOREIGN KEY ("id_clase") REFERENCES "clase" ("id"),
  CONSTRAINT "fk_grupo_clase_grupo" FOREIGN KEY ("id_grupo") REFERENCES "grupo" ("id"),
  CONSTRAINT "fk_grupo_clase_maestro" FOREIGN KEY ("id_maestro") REFERENCES "maestro" ("id"),
  CONSTRAINT "fk_grupo_clase_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_grupo_clase_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "grupo_cliente";
CREATE TABLE "grupo_cliente" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_cliente" int(11) DEFAULT NULL,
  "id_grupo" int(11) DEFAULT NULL,
  "beca" float(7,4) DEFAULT NULL,
  "estatus" int(11) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "comentario" varchar(512) DEFAULT NULL,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_grupo_cliente_id" ("id"),
  KEY "fk_grupo_cliente_id_cliente" ("id_cliente"),
  KEY "fk_grupo_cliente_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_grupo_cliente_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_grupo_cliente_id_cliente" FOREIGN KEY ("id_cliente") REFERENCES "cliente" ("id"),
  CONSTRAINT "fk_grupo_cliente_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_grupo_cliente_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=395 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "grupo_cliente_recibo";
CREATE TABLE "grupo_cliente_recibo" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_grupo_cliente" int(11) DEFAULT NULL,
  "monto" float(12,4) DEFAULT NULL,
  "estatus" int(11) DEFAULT NULL,
  "forma_pago" int(11) DEFAULT '0',
  "numero_mes" int(11) DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_grupo_cliente_recibo_id" ("id"),
  KEY "fk_grupo_cliente_recibo_id_grupo_cliente" ("id_grupo_cliente"),
  KEY "fk_grupo_cliente_recibo_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_grupo_cliente_recibo_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_grupo_cliente_recibo_id_grupo_cliente" FOREIGN KEY ("id_grupo_cliente") REFERENCES "grupo_cliente" ("id"),
  CONSTRAINT "fk_grupo_cliente_recibo_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_grupo_cliente_recibo_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=1516 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "grupo_cliente_recibo_pago";
CREATE TABLE "grupo_cliente_recibo_pago" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_grupo_cliente_recibo" int(11) DEFAULT NULL,
  "monto" float(12,4) DEFAULT NULL,
  "numero_mes" int(11) DEFAULT NULL,
  "comentario" varchar(128) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_grupo_cliente_pago_recibo" ("id"),
  KEY "fk_grupo_cliente_recibo_pago_id_grupo_cliente_recibo" ("id_grupo_cliente_recibo"),
  KEY "fk_grupo_cliente_recibo_pago_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_grupo_cliente_recibo_pago_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_grupo_cliente_recibo_pago_id_grupo_cliente_recibo" FOREIGN KEY ("id_grupo_cliente_recibo") REFERENCES "grupo_cliente_recibo" ("id"),
  CONSTRAINT "fk_grupo_cliente_recibo_pago_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_grupo_cliente_recibo_pago_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=1516 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "grupo_costo";
CREATE TABLE "grupo_costo" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_grupo" int(11) DEFAULT NULL,
  "monto" float(10,4) DEFAULT NULL,
  "estatus" int(11) DEFAULT NULL,
  "comentario" varchar(128) DEFAULT NULL,
  "fecha_alta" datetime DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_clase_costo" ("id"),
  KEY "fk_grupo_costo_usuario_alta" ("id_usuario_alta"),
  KEY "fk_grupo_costo_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  KEY "fk_grupo_costo_grupo" ("id_grupo"),
  CONSTRAINT "fk_grupo_costo_grupo" FOREIGN KEY ("id_grupo") REFERENCES "grupo" ("id"),
  CONSTRAINT "fk_grupo_costo_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_grupo_costo_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "maestro";
CREATE TABLE "maestro" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(64) DEFAULT NULL,
  "estatus" bit(1) DEFAULT NULL,
  "comentario" varchar(128) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_maestro_id" ("id"),
  KEY "fk_maestro_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_maestro_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_maestro_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_maestro_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "modulo";
CREATE TABLE "modulo" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(255) DEFAULT NULL,
  "descripcion" varchar(512) DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_modulo_id" ("id"),
  KEY "fk_modulo_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_modulo_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_modulo_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_modulo_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "paquete";
CREATE TABLE "paquete" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(64) DEFAULT NULL,
  "precio_compra" float(18,4) DEFAULT NULL,
  "precio_venta" float(18,4) DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_alta" datetime DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_paquete_id" ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "paquete_detalle";
CREATE TABLE "paquete_detalle" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_paquete" int(11) DEFAULT NULL,
  "id_producto_almacen" int(11) DEFAULT NULL,
  "cantidad" int(11) DEFAULT '1',
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_alta" datetime DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_paquete_detalle_id" ("id"),
  KEY "fk_paquete_detalle_id_producto_almacen" ("id_producto_almacen"),
  KEY "fk_paquete_detalle_id_paquete" ("id_paquete"),
  CONSTRAINT "fk_paquete_detalle_id_paquete" FOREIGN KEY ("id_paquete") REFERENCES "paquete" ("id"),
  CONSTRAINT "fk_paquete_detalle_id_producto_almacen" FOREIGN KEY ("id_producto_almacen") REFERENCES "producto_almacen" ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "permiso";
CREATE TABLE "permiso" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(255) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_permiso_id" ("id"),
  KEY "fk_permiso_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_permiso_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_permiso_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_permiso_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "producto";
CREATE TABLE "producto" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "clave" varchar(64) DEFAULT NULL,
  "descripcion" varchar(128) DEFAULT NULL,
  "comentario" varchar(255) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_producto_id" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "producto_almacen";
CREATE TABLE "producto_almacen" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_producto" int(11) DEFAULT NULL,
  "id_almacen" int(11) DEFAULT NULL,
  "id_talla" int(11) DEFAULT NULL,
  "cantidad" int(11) DEFAULT NULL,
  "precio_compra" float(18,4) DEFAULT NULL,
  "precio_venta" float(18,4) DEFAULT NULL,
  "fecha_compra" datetime DEFAULT CURRENT_TIMESTAMP,
  "comentario" varchar(255) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_producto_almacen_id" ("id"),
  KEY "fk_producto_almacen_id_producto" ("id_producto"),
  KEY "fk_producto_almacen_id_almacen" ("id_almacen"),
  KEY "fk_producto_almacen_id_talla" ("id_talla"),
  CONSTRAINT "fk_producto_almacen_id_almacen" FOREIGN KEY ("id_almacen") REFERENCES "almacen" ("id"),
  CONSTRAINT "fk_producto_almacen_id_producto" FOREIGN KEY ("id_producto") REFERENCES "producto" ("id"),
  CONSTRAINT "fk_producto_almacen_id_talla" FOREIGN KEY ("id_talla") REFERENCES "talla" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "producto_almacen_inventario";
CREATE TABLE "producto_almacen_inventario" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_producto_almacen" int(11) DEFAULT NULL,
  "cantidad" int(11) DEFAULT NULL,
  "precio_compra" float(18,4) DEFAULT NULL,
  "precio_venta" float(18,4) DEFAULT NULL,
  "fecha_compra" datetime DEFAULT CURRENT_TIMESTAMP,
  "comentario" varchar(255) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_producto_almacen_inventario_id" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS "producto_inventario";
CREATE TABLE "producto_inventario" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_producto_almacen" int(11) DEFAULT NULL,
  "cantidad" int(11) DEFAULT NULL,
  "precio_venta_ultima" float(18,4) DEFAULT NULL,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_alta" datetime DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_producto_inventario_id" ("id"),
  KEY "fk_producto_inventario_producto_almacen" ("id_producto_almacen"),
  CONSTRAINT "fk_producto_inventario_producto_almacen" FOREIGN KEY ("id_producto_almacen") REFERENCES "producto_almacen" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "salon";
CREATE TABLE "salon" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(64) DEFAULT NULL,
  "estatus" int(11) DEFAULT NULL,
  "comentario" varchar(128) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_salon" ("id"),
  KEY "fk_salon_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_salon_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  CONSTRAINT "fk_salon_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_salon_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "semana";
CREATE TABLE "semana" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "semana" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "semana_id" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=513 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS "talla";
CREATE TABLE "talla" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "nombre" varchar(128) DEFAULT NULL,
  "descripcion" varchar(255) DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_talla_id" ("id")
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "usuario";
CREATE TABLE "usuario" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "usuario" varchar(128) DEFAULT NULL,
  "contrasena" varchar(512) DEFAULT NULL,
  "nombre" varchar(512) DEFAULT NULL,
  "ultimo_acceso" datetime DEFAULT NULL,
  "comentario" varchar(512) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_usuario_id" ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS "usuario_modulo";
CREATE TABLE "usuario_modulo" (
  "id" int(11) NOT NULL AUTO_INCREMENT,
  "id_usuario" int(11) DEFAULT NULL,
  "id_modulo" int(11) DEFAULT NULL,
  "id_permiso" int(11) DEFAULT NULL,
  "fecha_alta" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "id_usuario_alta" int(11) DEFAULT NULL,
  "fecha_ultima_modificacion" datetime DEFAULT NULL,
  "id_usuario_ultima_modificacion" int(11) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "idx_usuario_modulo_id" ("id"),
  KEY "fk_usuario_modulo_id_usuario" ("id_usuario"),
  KEY "fk_usuario_modulo_id_usuario_alta" ("id_usuario_alta"),
  KEY "fk_usuario_modulo_id_usuario_ultima_modificacion" ("id_usuario_ultima_modificacion"),
  KEY "fk_usuario_modulo_id_permiso" ("id_permiso"),
  CONSTRAINT "fk_usuario_modulo_id_permiso" FOREIGN KEY ("id_permiso") REFERENCES "permiso" ("id"),
  CONSTRAINT "fk_usuario_modulo_id_usuario" FOREIGN KEY ("id_usuario") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_usuario_modulo_id_usuario_alta" FOREIGN KEY ("id_usuario_alta") REFERENCES "usuario" ("id"),
  CONSTRAINT "fk_usuario_modulo_id_usuario_ultima_modificacion" FOREIGN KEY ("id_usuario_ultima_modificacion") REFERENCES "usuario" ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
