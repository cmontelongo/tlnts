package com.talentos.coneccion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.jboss.logging.Logger;


public class coneccion {

	private static Context ctx = null;
	private static DataSource ds = null;
	private static Connection con = null;
	private static Logger log = Logger.getLogger(coneccion.class);

	static {
		try {
			ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:jboss/datasources/Talentos");
		} catch (NamingException e) {
			log.error(e.getMessage());
		}
		
	}

	public static Connection getConnection() throws SQLException{
        con = ds.getConnection();
		return con;
	}

	public static ResultSet ejecutar(String query) throws NamingException, SQLException{

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
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
        return rs;
	}

	public static int actualizar(String query) throws NamingException, SQLException{
        int rs = 0;
        try {
            con = ds.getConnection();
            con.setAutoCommit(true);
        	Statement stmt = con.createStatement();
        	System.out.println(query);
        	rs = stmt.executeUpdate(query);
        	System.out.println(rs);
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
        return rs;
	}

	public static void conectar(){
		Connection conn = null;
		try {
		    // db parameters
		    String url       = "jdbc:mysql://mysqlsrv:3306/talentos";
		    String user      = "talentos";
		    String password  = "12345678";
		 
		    // create a connection to the database
		    conn = DriverManager.getConnection(url, user, password);
		    // more processing here
		    // ... 
		    
		    log.info(String.format("Connected to database %s " + "successfully.", conn.getCatalog()));
		} catch(SQLException e) {
        	log.error(e.getMessage());
		} finally {
		 try{
	           if(conn != null)
		             conn.close();
		 }catch(SQLException ex){
	        	log.error(ex.getMessage());
		 }
		}
	}
}
