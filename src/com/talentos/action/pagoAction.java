package com.talentos.action;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.jboss.logging.Logger;

import com.talentos.coneccion.coneccion;
import com.talentos.util.Constantes;
import com.talentos.util.ContentIdGenerator;

public class pagoAction {

	private static Logger log = Logger.getLogger(pagoAction.class);

    public static StringBuilder service(String id)
        throws ServletException, IOException, NamingException {
        Session mailSession;
        StringBuilder resultado = new StringBuilder();
        StringBuilder respuesta = new StringBuilder();
        try {
        	
        	InitialContext ctx = new InitialContext();
        	String[] correos = obtenerCorreosPagoGrupo(id);
        	if (correos.length>0){
        	    String cid = ContentIdGenerator.getContentId();
            	resultado = obtenerPagoRecibo(id, cid, true);

            	StringBuilder[] correo1 = new StringBuilder[2];
        		correo1[0] = new StringBuilder(correos[0]);
        		correo1[1] = new StringBuilder(correos[1]);
        		StringBuilder[] correo2 = new StringBuilder[2];
        		correo2[0] = new StringBuilder(correos[2]);
        		correo2[1] = new StringBuilder(correos[3]);
	        	mailSession = (Session) ctx.lookup("java:jboss/mail/Gmail");
	            MimeMessage m = new MimeMessage(mailSession);
	            Address from = new InternetAddress("noreplay@talentos.com.mx","Talentos");
	            Address[] to = null;
	            if ( !correo1[1].toString().equals("") && !correo2[1].toString().equals("") ) {
	            	System.out.println("0");
		            to = new InternetAddress[] {
			            	new InternetAddress(correo1[1].toString(),correo1[0].toString()),
	            			new InternetAddress(correo2[1].toString(),correo2[0].toString()) };
	            } else if ( !correo1[1].toString().equals("") && correo2[1].toString().equals("") ) {
	            	System.out.println("1");
		            to = new InternetAddress[] { new InternetAddress(correo1[1].toString(),correo1[0].toString()) };
	            } else if ( correo1[1].toString().equals("") && !correo2[1].toString().equals("") ) {
	            	System.out.println("2");
		            to = new InternetAddress[] { new InternetAddress(correo2[1].toString(),correo2[0].toString()) };
	            }
	            m.setFrom(from);
	            m.setRecipients(Message.RecipientType.TO, to);
	            m.setSubject("Talentos - Recibo Mensualidad "+Constantes.mes[Integer.parseInt(correos[4])]);
	            //m.setContent(resultado, "text/plain");
	            MimeMultipart multipart = new MimeMultipart("related");
	            BodyPart messageBodyPart = new MimeBodyPart();
	            messageBodyPart.setContent(resultado.toString(), "text/html;charset=UTF-8");
	            multipart.addBodyPart(messageBodyPart);
	            // Image part
	            try{
		            MimeBodyPart imagePart = new MimeBodyPart();
		            File imagen = new File("../standalone/deployments/img/logo_1.png");
		            System.out.println(imagen.getAbsolutePath());
		            imagePart.attachFile(imagen.getAbsolutePath());
		            imagePart.setContentID("<" + cid + ">");
		            imagePart.setDisposition(MimeBodyPart.INLINE);
		            multipart.addBodyPart(imagePart);
	            } catch(Exception ex){
	            	log.error(ex.getLocalizedMessage());
	            }

	            m.setContent(multipart);
	            Transport.send(m);
	            System.out.println("Mensaje enviado: email1:"+correo1[1]+" email2:"+correo2[1]);
	            respuesta = new StringBuilder ("Mensaje enviado correctamente.");
        	}

        } catch (javax.mail.MessagingException e) {
        	respuesta = new StringBuilder ("Ocurrio un error al enviar el correo. (Error: ").append(e.getLocalizedMessage()).append(")");
            e.printStackTrace();
        }
        return respuesta;
    }

	private static String[] obtenerCorreosPagoGrupo(String id) {
		String[] result = {};
		StringBuilder query = new StringBuilder();
		query.append("select t1.id, t1.madre_nombre, t1.madre_email, t1.padre_nombre, t1.padre_email, t4.numero_mes")
			.append(" from cliente t1")
			.append(" inner join grupo_cliente t2 on t1.id=t2.id_cliente")
			.append(" inner join grupo_cliente_recibo t3 on t2.id=t3.id_grupo_cliente")
			.append(" inner join grupo_cliente_recibo_pago t4 on t3.id=t4.id_grupo_cliente_recibo")
			.append(" where t4.id=").append(id);
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    PreparedStatement pstmt = con.prepareStatement(query.toString());
		    ResultSet rs = pstmt.executeQuery();
		    if (rs.next()){
		    	result = new String[5];
		    	result[0] = rs.getString("madre_nombre");
		    	result[1] = rs.getString("madre_email");
		    	result[2] = rs.getString("padre_nombre");
		    	result[3] = rs.getString("padre_email");
		    	result[4] = rs.getString("numero_mes");
		    }
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		log.debug(result.length);

		return result;
	}

	public static StringBuilder obtenerPagoRecibo(String id, String cid, boolean muestraLogo){
		StringBuilder resultado = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select distinct t3.id,t4.id claseId,t1.numero_mes,t1.monto,t2.monto monto_pagado,t4.id,t4.nombre cliente_nombre,t5.id,")
			.append(" CONCAT('Grupo ',t5.nombre) grupo_nombre, t6.dia_semana, DATE_FORMAT(t2.fecha_alta,'%d/%b/%Y %H:%i') fecha_alta,")
			.append(" IFNULL(t2.id_usuario_alta,'') id_usuario_alta ")
			.append(" from grupo_cliente_recibo t1")
			.append(" inner join grupo_cliente_recibo_pago t2 on t1.id=t2.id_grupo_cliente_recibo")
			.append(" inner join grupo_cliente t3 on t3.id=t1.id_grupo_cliente")
			.append(" inner join cliente t4 on t4.id=t3.id_cliente")
			.append(" inner join grupo t5 on t5.id=t3.id_grupo")
			.append(" inner join grupo_clase t6 on t6.id_grupo=t5.id")
			.append(" where t5.estatus=1 and t2.id=").append(id)
			.append(" order by t6.dia_semana asc");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    float monto = 0;
			int diaActual = 0;
			String dias = "";
			String clienteNombre = "";
			String grupoNombre = "";
			String mensualidad = "";
			String fechaAlta = "";
	    	Locale locale = new Locale("en", "US");
	    	NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		    if (rs.next()){
				diaActual = rs.getInt("dia_semana");
				dias = ""+Constantes.sDiaSemana[diaActual];
		    	monto = rs.getFloat("monto_pagado");
		    	clienteNombre = rs.getString("cliente_nombre");
		    	grupoNombre = rs.getString("grupo_nombre");
		    	mensualidad = Constantes.mes[rs.getInt("numero_mes")];
		    	fechaAlta = rs.getString("fecha_alta");
		    	while(rs.next()){
					diaActual = rs.getInt("dia_semana");
					dias += ","+Constantes.sDiaSemana[diaActual];
		    	}
		    	if (cid!="" && cid.equals(""))
		    		resultado.append("<img src='cid:"+cid+"'>");
		    	else if (muestraLogo)
		    		resultado.append("<img src='./img/logo_1.png' />");
	    		resultado.append("<h2><i>Alumno:</i> ").append(clienteNombre).append("</h2><p />")
	    			.append("<h3><i>Grupo:</i> ").append(grupoNombre).append(" (").append(dias).append(")</h3><p />")
					.append("<table style='width:90%;border: 1px solid black;border-collapse: collapse;' border=0 cellpadding=10><thead><tr><th style='border: 1px solid black;'>Mensualidad</th><th style='border: 1px solid black;'>Monto</th><th style='border: 1px solid black;'>Fecha Pago</th></tr></thead>")
					.append("<tr><td style='border: 1px solid black;' align=left>").append(mensualidad).append("</td><td style='border: 1px solid black;' align=right>")
					.append(formatter.format(monto)).append("</td><td style='border: 1px solid black;' align=right cellpadding=2>")
					.append(fechaAlta).append("</td></tr>");
		    } else {
		    	resultado.append("<table style='width:100%'><tr><td><h1>No hay informacion</h1></td></tr>");
		    }
			resultado.append("</table>");
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return resultado;
	}

	public static StringBuilder obtenerEstadisticaPagoRecibo(String id) {
		StringBuilder resultado = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select distinct t4.semana, t1.total_mes mes_1, t2.total_mes mes_2, t3.total_mes mes_3 ")
			.append(" FROM")
			.append(" (select semana from talentos.semana) t4")
			.append(" right join ( select 1 recibo, sum(monto) total_mes, EXTRACT(WEEK from fecha_alta) semana")
			.append(" from grupo_cliente_recibo")
			.append(" where EXTRACT(MONTH from fecha_alta) = EXTRACT(MONTH from NOW())")
			.append(" group by EXTRACT(WEEK from fecha_alta) ) t1 on t4.semana=t1.semana")
			.append(" right join ( select 2 recibo, sum(monto) total_mes, EXTRACT(WEEK from fecha_recibo) semana")
			.append(" from cliente_recibo")
			.append(" where EXTRACT(MONTH from fecha_recibo) = EXTRACT(MONTH from NOW())")
			.append(" group by EXTRACT(WEEK from fecha_recibo) ) t2 on t4.semana=t2.semana")
			.append(" right join ( select 3 recibo, sum(monto) total_mes, EXTRACT(WEEK from fecha_pago) semana")
			.append(" from cliente_pago_evento")
			.append(" where EXTRACT(MONTH from fecha_pago) = EXTRACT(MONTH from NOW())")
			.append(" group by EXTRACT(WEEK from fecha_pago) ) t3 on t4.semana=t3.semana ");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    StringBuilder recibo1 = new StringBuilder("{");
		    StringBuilder recibo2 = new StringBuilder("{");
		    StringBuilder recibo3 = new StringBuilder("{");
		    if (rs.next()){
		    	recibo1.append(rs.getString("mes_1"));
		    	recibo2.append(rs.getString("mes_2"));
		    	recibo3.append(rs.getString("mes_3"));
				while(rs.next()){
			    	recibo1.append("@").append(rs.getString("mes_1")==null?0:rs.getString("mes_1"));
			    	recibo2.append("@").append(rs.getString("mes_2")==null?0:rs.getString("mes_2"));
			    	recibo3.append("@").append(rs.getString("mes_3")==null?0:rs.getString("mes_3"));
				}
		    } else {
				recibo1.append("0");
				recibo2.append("0");
				recibo3.append("0");
		    }
			recibo1.append("}");
			recibo2.append("}");
			recibo3.append("}");

			resultado.append(recibo1.toString()).append(",").append(recibo2.toString()).append(",").append(recibo3.toString());
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return resultado;
	}

	public static StringBuilder obtenerEstadisticaPagoDia(String id) {
		StringBuilder resultado = new StringBuilder();
		ResultSet rs = null;
        PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		query.append("select distinct t4.semana, t1.forma_pago pago_1, t1.total_mes mes_1, t2.forma_pago pago_2, t2.total_mes mes_2, t3.forma_pago pago_3, t3.total_mes mes_3 ")
			.append(" FROM")
			.append(" (select semana from talentos.semana) t4")
			.append(" right join ( select 1 recibo, forma_pago, count(id) total_mes, EXTRACT(WEEK from fecha_alta) semana")
			.append(" from grupo_cliente_recibo")
			.append(" where fecha_alta = NOW()")
			.append(" group by forma_pago, EXTRACT(WEEK from fecha_alta) ) t1 on t4.semana=t1.semana")
			.append(" right join ( select 2 recibo, forma_pago, count(id) total_mes, EXTRACT(WEEK from fecha_recibo) semana")
			.append(" from cliente_recibo")
			.append(" where fecha_recibo = NOW()")
			.append(" group by forma_pago, EXTRACT(WEEK from fecha_recibo) ) t2 on t4.semana=t2.semana")
			.append(" right join ( select 3 recibo, forma_pago, count(id) total_mes, EXTRACT(WEEK from fecha_pago) semana")
			.append(" from cliente_pago_evento")
			.append(" where fecha_pago = NOW()")
			.append(" group by forma_pago, EXTRACT(WEEK from fecha_pago) ) t3 on t4.semana=t3.semana ");
		System.out.println(query.toString());
		Connection con = null;
		try {
			con = coneccion.getConnection();
		    con.setAutoCommit(true);
		    pstmt = con.prepareStatement(query.toString());
		    rs = pstmt.executeQuery();
		    StringBuilder recibo1 = new StringBuilder("{");
		    StringBuilder recibo2 = new StringBuilder("{");
		    StringBuilder recibo3 = new StringBuilder("{");
		    if (rs.next()){
		    	recibo1.append(rs.getString("mes_1"));
		    	recibo2.append(rs.getString("mes_2"));
		    	recibo3.append(rs.getString("mes_3"));
				while(rs.next()){
			    	recibo1.append("@").append(rs.getString("mes_1")==null?0:rs.getString("mes_1"));
			    	recibo2.append("@").append(rs.getString("mes_2")==null?0:rs.getString("mes_2"));
			    	recibo3.append("@").append(rs.getString("mes_3")==null?0:rs.getString("mes_3"));
				}
		    } else {
				recibo1.append("0");
				recibo2.append("0");
				recibo3.append("0");
		    }
			recibo1.append("}");
			recibo2.append("}");
			recibo3.append("}");

			resultado.append(recibo1.toString()).append(",").append(recibo2.toString()).append(",").append(recibo3.toString());
		} catch(Exception e){
			log.error(e.getMessage());
		} finally {
			 try{
		           if(con != null)
		        	   con.close();
			 }catch(SQLException ex){
		        	log.error(ex.getMessage());
			 }
		}

		return resultado;
	}

}
