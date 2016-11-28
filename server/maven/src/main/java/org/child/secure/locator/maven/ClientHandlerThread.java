package org.child.secure.locator.maven;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	        writer.println("SERVER_HANDSHAKE_INIT");
	        writer.flush();
	        String fromClient,username,email,password,timeStamp;
	        dbconnector = new DBConnector();
			dbconnector.connect();
	        while (running && (fromClient = reader.readLine()) != null)
	        {
				 String delims = "[;]";
				 String[] tokens = fromClient.split(delims);
				 int option = Integer.parseInt(tokens[0]);
				 switch(option){
				 case 0:
				     System.out.println(tokens);
				     email=tokens[1];
				     timeStamp=tokens[2];
				     if(verifyDate(timeStamp)){
					     dbconnector.uniqueEmail(email);
					     System.out.println(email);
				         System.out.flush();
				     }
				     else{
				    	 //TODO
				     }
					 break;
				 case 1:
					 username = tokens[1];
				     email=tokens[2];
				     password = tokens[3];
				     timeStamp=tokens[4];
				     if(verifyDate(timeStamp)){
					     dbconnector.insertSignup(username,email,password);
					     System.out.println(username +" "+ email +" "+ password+timeStamp);
				         System.out.flush();
				     }
				     else{
				    	 
				     }
			         break;
				 case 2:
				     email=tokens[1];
				     password = tokens[2];
				     timeStamp=tokens[3];
				     if(verifyDate(timeStamp)){
					     dbconnector.login(email,password);
					     System.out.println(email +" "+ password);
				         System.out.flush();
				     }
				     else{
				    	 
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
	
	public boolean isRunning(){
	    return running;
	}
	
	public void setRunning(boolean running){
	    this.running = running;
	}
	private static boolean verifyDate(String date1){
		try {
			Calendar c = Calendar.getInstance();  
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	        String formattedDate = df.format(c.getTime());
	        Date sDt;
	        Date sDt2;
			sDt = df.parse(date1);
			sDt2 = df.parse(formattedDate);
			long ld = sDt.getTime() /1000;  
	        long ld2 = sDt2.getTime() /1000;
	        if(ld2>ld){
	        	return false;
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return true;
	}
}
