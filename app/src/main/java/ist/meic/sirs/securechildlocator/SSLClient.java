package ist.meic.sirs.securechildlocator;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class SSLClient {
    private SSLSocket sslsocket=null;
   public SSLClient() {
       try {
           SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
           sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 9999);

       } catch (Exception exception) {
           exception.printStackTrace();
       }
   }
    public void writeToServer(String write){
        try {
            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter  bufferedwriter = new BufferedWriter(outputstreamwriter);
            bufferedwriter.write(write + '\n');
            bufferedwriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public BufferedReader readFromServer () {
        BufferedReader bufferedreader=null;
        try {
            InputStream inputstream = sslsocket.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            bufferedreader = new BufferedReader(inputstreamreader);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return bufferedreader;
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