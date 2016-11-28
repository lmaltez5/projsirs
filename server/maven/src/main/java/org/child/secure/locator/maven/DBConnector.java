package org.child.secure.locator.maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DBConnector {
	
    private String rhost;
    private Connection con;
    private Session session; 
    
    public void connect(){
    	connectSSH();
    	connectDB();
    }
    
    private void connectSSH(){
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
    
    private void connectDB(){
    	try {
          String driver = "com.mysql.jdbc.Driver";
          String url = "jdbc:mysql://" + rhost +"/";
          String db = "ist176249";
          String dbUser = "ist176249";
          String dbPasswd = "igxg0984";
          Class.forName(driver);
          con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
    	} catch (Exception e){
    		System.err.print("Can't connect to DB\n"+ e+ "\n");
    	}
    }
    
   
    
    public void disconnect(){
    	disconnectSSH();
    	disconnectDB();
    }
    
    private void disconnectDB() {
		try {
			 if (con != null && !con.isClosed()) {
				 con.close();
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void disconnectSSH() {
		if (session != null && session.isConnected()) {
			session.disconnect();			
		}
	}

	public ResultSet query(String query){
		try {
			Statement st = con.createStatement();
			return st.executeQuery(query);
		} catch (SQLException e) {
			System.err.print("No Query");
			return null;
		}
    }
    
    public boolean update(String update){
		try {
			Statement st = con.createStatement();
			int i = st.executeUpdate(update);
			return i >= 0;
		} catch (SQLException e) {
			System.err.print("No Query");
			return false;
		}
    }
	    public boolean uniqueEmail(String email){
		try {
			ResultSet rs = query("SELECT email FROM userList where email='"+email+"';");
			//next false se nao tiver rows
			return !rs.next();
		} catch (SQLException e) {
			System.err.print("No Query");
			return false;
		}
    }

    public boolean insertSignup(String username, String email, String password){
		String updadeString= "INSERT INTO userList (name, email, password) VALUES ('" + username + "','" + email + "','" + password + "');";
		return update(updadeString);
    }

	public boolean login(String email, String password) {
		try {
			ResultSet rs = query("SELECT password FROM userList WHERE email='"+ email + "' AND password='"+password"';");
			return !rs.next();
		catch (SQLException e) {
			System.err.print("No Query");
			return false;
		}
	}
}
