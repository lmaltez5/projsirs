package org.child.secure.locator.maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public boolean uniqueEmail(String email){
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT email FROM userList where email = ? ;");
			stmt.setString(1, email);
			System.err.println(stmt.toString());
			ResultSet rs = stmt.executeQuery();
			return !rs.next();
			
		} catch (SQLException e) {
			System.err.print("No Query uniqueEmail");
			return false;
		}
    }

    public boolean insertSignup(String username, String email, String password){
	    try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO userList (name, email, password) VALUES ( ? , ? , ? );");
			stmt.setString(1, username);
		    stmt.setString(2, email);
		    stmt.setString(3, password);
			System.err.println(stmt.toString());
			int i = stmt.executeUpdate();
			return i >= 0;
	    }  catch (SQLException e) {
			System.err.print("No Query insertSignup");
			return false;
	    }
    }
    
    public boolean verifyKey(String clientKey,String email,String phoneID){
	   try {
		PreparedStatement stmt =con.prepareStatement("SELECT * FROM sessionTable WHERE email = ? AND ckey = ? AND phone_id = ? ;");
		stmt.setString(1, email);
		stmt.setString(2, clientKey);
		stmt.setString(3, phoneID);
		System.err.println(stmt.toString());
		ResultSet rs = stmt.executeQuery();
		return rs.next();
	    }  catch (SQLException e) {
			System.err.print("No Query verifyKey");
			return false;
	    }
    }	

	public boolean insertKey(String clientKey,String email,String phoneID, boolean mode){
	   try {
		   PreparedStatement stmt=null;
		   if (mode){
			stmt =con.prepareStatement("INSERT INTO sessionTable (email, phone_id, ckey) VALUES (?,?,?);");
			stmt.setString(1, email);
			stmt.setString(2, clientKey);
			stmt.setString(3, phoneID);
		   }
		   else {
		   	stmt =con.prepareStatement("update sessionTable set ckey = ? where email = ? AND phone_id = ?;");
			stmt.setString(1,clientKey);
		   	stmt.setString(2,email);
			stmt.setString(3,phoneID);
		   }
		System.err.println(stmt.toString());
		int i = stmt.executeUpdate();
		return i >= 0;
	    }  catch (SQLException e) {
			System.err.print("No Query insertKey");
			return false;
	    }
    }
	
	
	public boolean login(String email, String password) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM userList WHERE email= ? AND password= ? ; ");
			stmt.setString(1, email);
			stmt.setString(2, password);
			System.err.println(stmt.toString());
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		}catch (SQLException e) {
			System.err.print("No Query login");
			return false;
		}
	}
	
	public String searchUser(String email) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT name FROM userList WHERE email= ? ;");
			stmt.setString(1, email);
			System.err.println(stmt.toString());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()){
				return rs.getString("name");
			}
			else
				return "Error, something went wrong";
		}catch (SQLException e) {
			System.err.print("No Query searchUser");
			return "Error, something went wrong";
		}
	}
	
	public String searchPhoneNames(String email) {
		try {
			String names = "";
			PreparedStatement stmt = con.prepareStatement("SELECT phone_name FROM phonesIDChild WHERE email= ? ;");
			stmt.setString(1, email);
			System.err.println(stmt.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				names =  rs.getString("phone_name")+","+names;
			}
			return names;
		}catch (SQLException e) {
			System.err.print("No Query searchPhoneNames");
			return "Error, something went wrong";
		}

	}
	
	
	
	public String queryPhoneId(String email, String phoneID) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM phonesIDParent WHERE email= ? AND phone_id= ? ;");
			stmt.setString(1, email);
			stmt.setString(2, phoneID);
			System.err.println(stmt.toString());

			ResultSet rs = stmt.executeQuery();
			if (rs.next()){
				return "legal";
			}
			stmt = con.prepareStatement("SELECT * FROM phonesIDChild WHERE email= ? AND phone_id= ? ;");
			stmt.setString(1, email);
			stmt.setString(2, phoneID);
			System.err.println(stmt.toString());
			rs = stmt.executeQuery();
			if (rs.next()){
				return "child";
			}
			return "new";
			
		} catch (SQLException e) {
			System.err.print("No Query queryPhoneId");
			return "Error, something went wrong";
		}
	}

	public boolean insertID(String email, String phoneID , String phone_name,int index,String parent){
		String table;
		PreparedStatement stmt=null;
		try {
			if (parent=="parent"){
				stmt = con.prepareStatement("INSERT INTO phonesIDParent (email, phone_id , phone_name ) VALUES (?,?,?);");
			}
			else{
				stmt = con.prepareStatement("INSERT INTO phonesIDChild (email, phone_id , phone_name,thread_index) VALUES (?,?,?,?);");
				stmt.setString(4, Integer.toString(index));
			}	
		
		 	stmt.setString(1, email);
		    	stmt.setString(2, phoneID);
		    	stmt.setString(3, phone_name);
			System.err.println(stmt.toString());
			int i = stmt.executeUpdate();
			return i >= 0;
		}
		catch (SQLException e) {
			System.err.print("No Query insertID");
			return false;
		}
	}

}
