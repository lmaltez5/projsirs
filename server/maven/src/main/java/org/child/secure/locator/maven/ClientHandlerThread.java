package org.child.secure.locator.maven;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
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
	private double longitude=200;
	private double latitude=200;
	

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
					     if(verifyDate(timeStamp,writer)){
					    	 verifyDbResult(dbconnector.uniqueEmail(email),"Email");
					 	}
				    break;
					case 1:
					     username = tokens[1];
					     email=tokens[2];
					     password = tokens[3];
					     phoneID=tokens[4];
					     timeStamp=tokens[5];
					     if(verifyDate(timeStamp,writer)){
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
					     if(verifyDate(timeStamp,writer)){
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
						 if(verifyDate(timeStamp,writer)&&verifyKey(clientKey,email,phoneID,writer)){
							 verifyDbResult(dbconnector.insertID(email, phoneID , phoneName,vectorIndex,"child"),"Child ID");
					     }
					 break;
					 case 4:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 phoneName=tokens[4];
						 timeStamp=tokens[5];
						 if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
							 verifyDbResult(dbconnector.insertID(email, phoneID , phoneName,vectorIndex,"parent"),"Parent ID");
					     }
					 break;
					 case 5:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 timeStamp=tokens[4];
						 if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
							 writer.println(dbconnector.queryPhoneId(email, phoneID,vectorIndex));
							 writer.flush();
					     }
					 break;
					case 6:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 timeStamp=tokens[4];
						 if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
							 writer.println(dbconnector.searchUser(email));
							 writer.flush();
					     }
					 break;
					case 7:
						 clientKey=tokens[1];
						 phoneID=tokens[2];
						 email=tokens[3];
						 timeStamp=tokens[4];
						 if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
							writer.println(dbconnector.searchPhoneNames(email));
							writer.flush();
					     }
					 break;
					 case 8:
						clientKey=tokens[1];
						phoneID=tokens[2];
						email=tokens[3];
						password=tokens[4];
						timeStamp=tokens[5];
						if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
							temp=dbconnector.login(email,password);
							if(temp){
								verifyDbResult(dbconnector.insertKey(null,email,phoneID,false), "logout");
							}
							else
								verifyDbResult(temp,"logout");

					        }
					 break;
					 case 9:
						clientKey=tokens[1];
						phoneID=tokens[2];
						email=tokens[3];
						phoneKidName=tokens[4];
						timeStamp=tokens[5];
						if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
							String test=dbconnector.searchKidName(email,phoneKidName);
							if (!test.contains("Error")){
								int i=Integer.parseInt(test);
								connectToChild(i);
							}
							else{
								writer.println("Error, Child not Found");
								writer.flush();
							}		
					    }
					 break;
					 case 10:
							clientKey=tokens[1];
							phoneID=tokens[2];
							email=tokens[3];
							timeStamp=tokens[4];
							if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
									verifyDbResult(dbconnector.insertKey(null,email,phoneID,false), "logout");
								}
						 break;
				 
		        	case 11:
						clientKey=tokens[1];
						phoneID=tokens[2];
						email=tokens[3];
						timeStamp=tokens[4];
						if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
								verifyDbResult(dbconnector.updateThread(email,phoneID,vectorIndex), "thread update");
							}
						break;
				 
		        	case 12:
						clientKey=tokens[1];
						phoneID=tokens[2];
						email=tokens[3];
						String latitude=tokens[4];
						String longitude=tokens[5];
						timeStamp=tokens[6];
						if(verifyDate(timeStamp,writer)&& verifyKey(clientKey,email,phoneID,writer)){
							this.latitude=Double.parseDouble(latitude);
							this.longitude=Double.parseDouble(longitude);
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
		System.err.println(result);
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
	
	private void connectToChild(int index){
		ClientHandlerThread child=server.getThread(index);
		PrintWriter writeToChild=child.getWriter();
		writeToChild.println("Send Location");
		writeToChild.flush();
		try {
			wait(1000);
			writer.println(child.getLatitude()+","+child.getLongitude()); 
			writer.flush();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private boolean verifyKey(String clientKey, String email,String phoneID,PrintWriter functionWriter){
		if(dbconnector.verifyKey(clientKey,email,phoneID)){
			return true;
		}
		else{
			functionWriter.println("Error in key");
			functionWriter.flush();
			return false;
			
		}
		
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

	public PrintWriter getWriter(){return writer;}

	public BufferedReader getReader(){return reader;}
	
	public double getLongitude(){return longitude;}
	
	public double getLatitude(){return latitude;}
	
	private boolean verifyDate(String date1,PrintWriter functionWriter ){
		try {
			Calendar c = Calendar.getInstance();
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        String formattedDate = df.format(c.getTime());;
	        Date sDt = df.parse(date1);
			Date sDt2 = df.parse(formattedDate);
			long ld = sDt.getTime() /1000;
	        long ld2 = sDt2.getTime() /1000;
	        if(ld2>ld){
	        	functionWriter.println("Error in TimeStamp,please try again");
	        	functionWriter.flush();
	        	return false;
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
