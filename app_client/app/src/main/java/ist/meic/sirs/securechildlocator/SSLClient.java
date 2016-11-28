package ist.meic.sirs.securechildlocator;

import android.content.Context;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import java.io.*;
import java.security.KeyStore;

public class SSLClient {
    private SSLSocket sslsocket=null;
   public SSLClient(Context context) {
       try {
           KeyStore tsTrust = KeyStore.getInstance("BKS");
           InputStream instream = context.getResources().openRawResource(R.raw.truststore);
           tsTrust.load(instream, "EpW5eE[bxwHu".toCharArray());
           TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
           tmf.init(tsTrust);

           instream = context.getResources().openRawResource(R.raw.client);
           KeyStore ksStore = KeyStore.getInstance("BKS");
           ksStore.load(instream, "sirs".toCharArray());

           KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
           kmfactory.init(ksStore, "sirs".toCharArray());

           SSLContext sslContext = SSLContext.getInstance("TLS");
           sslContext.init(kmfactory.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());

           SSLSocketFactory sslsocketfactory = sslContext.getSocketFactory();

           sslsocket = (SSLSocket) sslsocketfactory.createSocket("46.101.3.38", 9999);
           sslsocket.setEnabledCipherSuites(new String[] {"TLS_DHE_RSA_WITH_AES_128_CBC_SHA256"});

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