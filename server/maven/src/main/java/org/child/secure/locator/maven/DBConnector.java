package org.child.secure.locator.maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DBConnector {
	    
	    static String rhost;
        static Connection con;
        static Session session; 
	    
	    public static void connectSSH(){
	        String user = "ist176249";
	        String password = "Projsirs1";
	        String host = "sigma.ist.utl.pt";
	        try {
	            JSch jsch = new JSch();
	            Session session = jsch.getSession(user, host);
	            session.setPassword(password);
	            session.setConfig("StrictHostKeyChecking", "no");
	            rhost = "db.ist.utl.pt";
	        }
	        catch(Exception e){
	        	System.err.print("Can't connect to SSH");
	       }
	    }
	    
	    public static void connectDB(){
	    	try {
	          String driver = "com.mysql.jdbc.Driver";
	          String url = "jdbc:mysql://" + rhost +"/";
	          String db = "ist176249";
	          String dbUser = "ist176249";
	          String dbPasswd = "igxg0984";
	          Class.forName(driver);
	          con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
	    	} catch (Exception e){
	    		System.err.print("Can't connect to DB");
	    	}
	    }
	    
	    public static void connect(){
	    	connectSSH();
	    	connectDB();
	    }
	    
	    public static void disconnect(){
	    	disconnectSSH();
	    	disconnectDB();
	    }
	    
	    private static void disconnectDB() {
			try {
				 if (con != null && !con.isClosed()) {
					 con.close();
				 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

		private static void disconnectSSH() {
			if (session != null && session.isConnected()) {
				session.disconnect();			
			}
		}

		public static ResultSet query(String query){
			try {
				Statement st = con.createStatement();
				return st.executeQuery(query);
			} catch (SQLException e) {
				System.err.print("No Query");
				return null;
			}
	    }
	    
	    public static boolean update(String update){
			try {
				Statement st = con.createStatement();
				int i = st.executeUpdate(update);
				return i >= 0;
			} catch (SQLException e) {
				System.err.print("No Query");
				return false;
			}
	    }
}


