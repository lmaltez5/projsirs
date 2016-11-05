package ist.meic.sirs.securechildlocator;

/**
 * Created by GuilhermeM on 04/11/2016.
 */
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;

public class KerberosStuff {
    private static final byte[] SALT = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};
    public byte[] requestAuthentication(String userId, String password){

        Calendar c = Calendar.getInstance();
        final String XML = "<request>" +
                "<C>" +userId+ "</C>"+
                "<S>SD-STORE</S>" +
                "<Nonce>"+ c.toString()  +"</Nonce>" +
                "</request>";
        byte[] plainBytes = XML.getBytes();
        //ENVIAR OS PLAINBYTES
        //1ºRONDA DO KERBEROS

        // vERIFICAR SE O TEMPO E IGUAL??? INCREMENTAR ESSAS COISAS

        //RECEBER DO SERVIDOR O TICKET
        //Desencriptar a mensagem com o mesmo que no server e ver a primeira se puder a pass esta certa

        /*PASSWORD->recebemos uma mensagem encriptada com a password, temos de desencriptar com a pass feita numa key
            Temos o dCipher, depois tenho de comparar cenas
        */
        Key keyC = null;
        byte[] newPlainBytes = null;
        Cipher dCipher = null;
        try {
            keyC = Crypto.generatePBEKey(password.toCharArray(), SALT);
            dCipher = Crypto.getAESDecryptCipher(keyC,IV);
            //DESENCRIPTAR AQUI Crypto.decrypt(dCipher,TEXTODO-S)
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        /*VERIFICAR O TEMPO, SE NAO TIVER COMO DEVE DE SER DIZER QUE NAO*/

        final String xmlToBubble = "<message>" +
                "<Ticket>"+ticketNode.getTextContent()+"</Ticket>" +
                "<Kcs>"+ printBase64Binary(keyClientServer.getEncoded())+"</Kcs>" +
                "</message>";*/
        return null;
    }
}
