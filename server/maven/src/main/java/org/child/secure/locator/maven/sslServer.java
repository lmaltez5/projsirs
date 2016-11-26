import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Security;
import com.sun.net.ssl.internal.ssl.Provider;


public
class sslServer {
	static DBConnector dbconnector= new DBConnector();
	
    public static void  main(String[] arstring) {
        try {
			dbconnector = new DBConnector();
			dbconnector.connect();
			 Security.addProvider(new Provider());
			//Specifying the Keystore details
			 
			System.setProperty("javax.net.ssl.keyStore","serverKey.keystore");
			System.setProperty("javax.net.ssl.keyStorePassword","EpW5eE[bxwHu");
			
			SSLServerSocketFactory factory=(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			SSLServerSocket sslserversocket=(SSLServerSocket) factory.createServerSocket(9999);
			SSLSocket sslSocket=(SSLSocket) sslserversocket.accept();
			
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(
							sslSocket.getInputStream()));
			
			String string = null;
			/*0: verify email
			 * 1: register user
			 * 2: login
			 * */
			while ((string = bufferedreader.readLine()) != null) { 
				String username,email,password;
				 String delims = "[;]";
				 String[] tokens = string.split(delims);
				 int option = Integer.parseInt(tokens[0]);
				 switch(option){
				 case 0:
				     email=tokens[1];
				     dbconnector.uniqueEmail(email);
				     System.out.println(email);
			         System.out.flush();
					 break;
				 case 1:
					 username = tokens[1];
				     email=tokens[2];
				     password = tokens[3];
				     dbconnector.insertSignup(username,email,password);
				     System.out.println(username +" "+ email +" "+ password);
			         System.out.flush();
			         break;
				 case 2:
				     email=tokens[1];
				     password = tokens[2];
				     dbconnector.login(email,password);
				     System.out.println(email +" "+ password);
			         System.out.flush();
			         break;
				 }
            }
                    
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
      