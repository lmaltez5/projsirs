package org.child.secure.locator.maven;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
 
public class sslServer {
    public static void  main(String[] arstring) {
        try {
            SSLServerSocketFactory sslserversocketfactory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket =
                    (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);
            SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();
 
            InputStream inputstream = sslsocket.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
 
            String read = null;
            while ((read = bufferedreader.readLine()) != null) {
                    JSONObject jObj = new JSONObject(read);
                        String request = jObj.getString("request");
                       if(request.equals("VERIFYEMAIL")){
                           jObj.getString("email");
                       }
                       else if(request.equals("REGISTER")){
                           jObj.getString("username");
                           jObj.getString("email");
                           jObj.getString("password");
                           jObj.getString("publicKey");
                       }
                       else if(request.equals("LOGIN")){
 
                       }
 
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}