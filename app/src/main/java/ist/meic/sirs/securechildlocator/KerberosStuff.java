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
    public byte[] requestAuthentication(String userId, byte[] reserved){
       /* SymKey symKey =new SymKey();
        FirstRound firstRound = new FirstRound();
        SymCrypto symCrypto = new SymCrypto();


        Calendar c = Calendar.getInstance();

        final String XML = "<request>" +
                "<C>" +userId+ "</C>"+
                "<S>SD-STORE</S>" +
                "<Pass>"+ new String(reserved) +"</Pass>"+
                "<Nonce>"+ c.toString()  +"</Nonce>" +
                "</request>";

        byte[] plainBytes = XML.getBytes();
        //Sending first message
        byte[] returnBytes= port.requestAuthentication(userId,plainBytes);

        Document xmlDocument=firstRound.getDocument(returnBytes);

        //TicketNode
        Node ticketNode= firstRound.getNode(xmlDocument,"cipherTicket");
        if(ticketNode==null){
            authException("Authentication failed Ticket not in message",reserved);
        }

        Node sessionNode=firstRound.getNode(xmlDocument,"cipherSession");
        if(sessionNode==null){
            authException("Authentication failed Session not in message",reserved);
        }
        byte[] sessionNodeText = parseBase64Binary(sessionNode.getTextContent());

        byte[] hashPasswordBytes = null;
        try {
            hashPasswordBytes = symKey.generateDigest((hashMap.get(userId)).getBytes());
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        String hashPassword=new String(hashPasswordBytes);
        SecretKey keyC = null;
        byte[] newPlainBytes = null;
        Cipher dCipher = null;
        try {
            keyC = symKey.generatePBEKey(hashPassword.toCharArray(), SALT);
            dCipher = symCrypto.getAESDecryptCipherwithIV(keyC);
            newPlainBytes = symCrypto.decrypt(dCipher, sessionNodeText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }


        Document xmlSession=firstRound.getDocument(newPlainBytes);
        String keyClientServerText  = xmlSession.getDocumentElement().getFirstChild().getTextContent();
        byte[] encodedKey = parseBase64Binary(keyClientServerText);
        Key keyClientServer = new SecretKeySpec(encodedKey,0,encodedKey.length, "AES");
        Node nonceserver=xmlSession.getDocumentElement().getFirstChild().getNextSibling();
        if(!dateTime.toString().equals(nonceserver.getTextContent())){
            AuthReqFailed faultInfo = new AuthReqFailed();
            faultInfo.setReserved(reserved);
            String message = "Chave de Sessao invalida";
            throw new AuthReqFailed_Exception(message,faultInfo);
        }
        //sending to Bubbledocs
        final String xmlToBubble = "<message>" +
                "<Ticket>"+ticketNode.getTextContent()+"</Ticket>" +
                "<Kcs>"+ printBase64Binary(keyClientServer.getEncoded())+"</Kcs>" +
                "</message>";*/
        return null;
    }
}
