package org.child.secure.locator.maven;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClientHandlerThread extends Thread{
	private boolean running = true;
	private SSLSocket dataSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private String ip;
	private DBConnector dbconnector;

	public ClientHandlerThread(SSLSocket dataSocket){
	    this.dataSocket = dataSocket;
	    try
	    {
	        this.reader = new BufferedReader(new InputStreamReader(this.dataSocket.getInputStream()));
	        this.writer = new PrintWriter(this.dataSocket.getOutputStream());
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	
	    this.ip = this.dataSocket.getInetAddress().getHostAddress();
	}
	
	public void run()
	{
	    try
	    {
	        String fromClient,username,email,password,timeStamp,phoneID,phoneName,clientKey;
	        boolean temp;
	        dbconnector = new DBConnector();
			dbconnector.connect();
	        while (running && (fromClient = reader.readLine()) != null)
	        {
				 String delims = "[;]";
				 String[] tokens = fromClient.split(delims);
				 int option = Integer.parseInt(tokens[0]);
				 switch(option){
					 case 0:
					     email=tokens[1];
					     timeStamp=tokens[2];
					     if(verifyDate(timeStamp)){
					    	 verifyDbResult(dbconnector.uniqueEmail(email),"Email");
					 	}
				    break;
					case 1:
						 username = tokens[1];
					     email=tokens[2];
					     password = tokens[3];
					     timeStamp=tokens[4];
					     if(verifyDate(timeStamp)){
					    	 temp=dbconnector.insertSignup(username,email,password);
					    	 verifyDbResult(temp,"Signup");
					    	 if (temp){
						    		String serverKey=generateRandomSecret();
						    		writer.println(serverKey);
						 			writer.flush();
						    		/*INSERT IN BD
						    		dbconnector.uniqueEmail(email);
						 			writer.flush();*/
						 		}
					    }
			         break;
					 case 2:
					     email=tokens[1];
					     password = tokens[2];
					     timeStamp=tokens[3];
					     if(verifyDate(timeStamp)){
					    	 temp=dbconnector.login(email,password);
					    	 verifyDbResult(temp,"Login");
					    	 if (temp){
						    		String serverKey=generateRandomSecret();
						    		writer.println(serverKey);
						 			writer.flush();
						    		/*INSERT IN BD
						    		dbconnector.uniqueEmail(email);
						 			writer.flush();*/
						 		}
					     }
			         break;
					 case 3:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 phoneName=tokens[4];
						 timeStamp=tokens[5];
						 if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							 verifyDbResult(dbconnector.insertID(email, phoneID , phoneName,"child"),"Child ID");
					     }
					 break;
					 case 4:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 phoneName=tokens[4];
						 timeStamp=tokens[5];
						 if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							 verifyDbResult(dbconnector.insertID(email, phoneID , phoneName,"parent"),"Parent ID");
					     }
					 break;
					 case 5:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 timeStamp=tokens[4];
						 if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							 writer.println(dbconnector.queryPhoneId(email, phoneID));
							 writer.flush();
					     }
					 break;
					case 6:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 timeStamp=tokens[4];
						 if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							 writer.println(dbconnector.searchUser(email));
							 writer.flush();
					     }
					 break;
					case 7:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 timeStamp=tokens[4];
						 if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							writer.println(dbconnector.searchPhoneNames(email));
							writer.flush();
					     }
					 break;
				 }
	        }
	    }
	    catch (IOException e)
	    {
	        e.getCause();
	
	    }
	}
	
	public String getIP(){
	    return ip;
	}
	private void verifyDbResult(boolean result, String errorString){
		if(result){
			writer.println("Sucess");
			writer.flush();
		}
		else{
			writer.println("Error in "+errorString+", please try again");
			writer.flush();
		}
	}
	public boolean isRunning(){
	    return running;
	}
	
	public void setRunning(boolean running){
	    this.running = running;
	}
	private String generateRandomSecret(){
		try {
			 SecureRandom random = new SecureRandom();
			 byte seed[] = random.generateSeed(256);
			 MessageDigest md=MessageDigest.getInstance("SHA-256");
			 md.update(seed);
	         byte[] digest = md.digest();
	         return digest.toString().replace("\n", "");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
        
	}
	
	private boolean verifyDate(String date1){
		try {
			Calendar c = Calendar.getInstance();  
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	        String formattedDate = df.format(c.getTime());;
	        Date sDt = df.parse(date1);
			Date sDt2 = df.parse(formattedDate);
			long ld = sDt.getTime() /1000;  
	        long ld2 = sDt2.getTime() /1000;
	        if(ld2>ld){
	        	 writer.println("Error in TimeStamp,please try again");
		    	 writer.flush();
	        	return false;
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return true;
	}
}
