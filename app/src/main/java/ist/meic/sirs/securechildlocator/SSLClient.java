package ist.meic.sirs.securechildlocator;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class SSLClient {
    SSLSocket sslsocket=null;
   public BufferedWriter createSocket () {
       BufferedWriter bufferedwriter=null;
        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 9999);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            bufferedwriter = new BufferedWriter(outputstreamwriter);
            //  bufferedwriter.write(string + '\n');
               // bufferedwriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
       return bufferedwriter;
    }
    public void closeSocket(){
        if(sslsocket  != null){
            try {
                sslsocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}