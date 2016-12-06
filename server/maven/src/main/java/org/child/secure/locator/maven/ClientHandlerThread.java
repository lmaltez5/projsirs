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
import javax.crypto.*;
import java.util.Base64;

public class ClientHandlerThread extends Thread{
	private boolean running = true;
	private int vectorIndex;
	private SSLSocket dataSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private String ip;
	private DBConnector dbconnector;
	private sslServer server;

	public ClientHandlerThread(SSLSocket dataSocket, sslServer server,int index){
		this.dataSocket = dataSocket;
	  try {
			this.vectorIndex=index;
			this.server=server;
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
	        String fromClient,username,email,password,timeStamp,phoneID,phoneName,clientKey,phoneKidName;
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
					     phoneID=tokens[4];
					     timeStamp=tokens[5];
					     if(verifyDate(timeStamp)){
					    	 temp=dbconnector.insertSignup(username,email,password);
					    	 if (temp){
							String serverKey=generateRandomSecret();
							temp=dbconnector.insertKey(serverKey,email,phoneID,true);
							 if(temp)
								verifyDbResult(temp,serverKey);
							 else
								verifyDbResult(temp,"Creating key");
						 }
						 else
							verifyDbResult(temp,"Signup");
					    }
			         break;
					 case 2:
					     email=tokens[1];
					     password = tokens[2];
					     phoneID=tokens[3];
					     timeStamp=tokens[4];
					     if(verifyDate(timeStamp)){
					    	 temp=dbconnector.login(email,password);
					    	 if (temp){
						 	String serverKey=generateRandomSecret();
						        temp=dbconnector.insertKey(serverKey,email,phoneID,false);
							 if(temp)
								verifyDbResult(temp,serverKey);
							 else
								 verifyDbResult(temp,"Creating key");
						 }
						 else
							verifyDbResult(temp,"Login");
					     }
			         break;
					 case 3:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 phoneName=tokens[4];
						 timeStamp=tokens[5];
						 if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							 verifyDbResult(dbconnector.insertID(email, phoneID , phoneName,vectorIndex,"child"),"Child ID");
					     }
					 break;
					 case 4:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 phoneName=tokens[4];
						 timeStamp=tokens[5];
						 if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							 verifyDbResult(dbconnector.insertID(email, phoneID , phoneName,vectorIndex,"parent"),"Parent ID");
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
					 case 8:
						clientKey=tokens[1];
						phoneID=tokens[2];
						email=tokens[3];
						timeStamp=tokens[4];
						if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							verifyDbResult(dbconnector.insertKey(null,email,phoneID,false), "logout");

					        }
					 break;
					 case 9:
						clientKey=tokens[1];
						phoneID=tokens[2];
						email=tokens[3];
						phoneKidName=tokens[4];
						timeStamp=tokens[5];
						if(verifyDate(timeStamp)&& dbconnector.verifyKey(clientKey,email,phoneID)){
							//verifyDbResult(dbconnector.insertKey(null,email,phoneID,false), "logout");

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
		Sytem.err.println(result);
		if(result){
			writer.println("Sucess,"+errorString);
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
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    			keyGen.init(256); // for example
    			SecretKey secretKey = keyGen.generateKey();
    			return Base64.getEncoder().encodeToString(secretKey.getEncoded());
   		 }catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	public PrintWriter getWriter(){return this.writer;}

	public BufferedReader getReader(){return this.reader;}

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
