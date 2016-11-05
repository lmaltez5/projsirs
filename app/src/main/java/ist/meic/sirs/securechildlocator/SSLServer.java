package ist.meic.sirs.securechildlocator;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * Created by GuilhermeM on 05/11/2016.
 */

public class SSLServer {
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");"password".    SSLContext sc = SSLContext.getInstance("SSL"););
    SSLServerSocketFactory ssf = sc.getServerSocketFactory();
    kmf.init(ks, "password".newFileInputStream("server"),);
toCharArray()
    sc.init(kmf.getKeyManagers(), null, null);
toCharArray()
    server = (SSLServerSocket) ssf.createServerSocket(this.port);
}
