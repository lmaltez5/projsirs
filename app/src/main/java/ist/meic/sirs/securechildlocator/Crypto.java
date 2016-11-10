package ist.meic.sirs.securechildlocator;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

import java.security.MessageDigest;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * Created by GuilhermeM on 05/11/2016.
 */

public class Crypto {
    public static String SHA256 (String text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    public static String encode (String text) throws NoSuchAlgorithmException {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    public static byte[] encrypt(String text, String alias) {
        byte[] cipherText = null;
        try {
            PublicKey publicKey = getPrivateKeyEntry(alias).getCertificate().getPublicKey();
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static String getSigningKey(String alias) {
        String certificate=null;
        try {
           Certificate cert = getPrivateKeyEntry(alias).getCertificate();
            if (cert == null) {
                return null;
            }

            certificate= Base64.encodeToString(cert.getEncoded(), Base64.NO_WRAP);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return certificate;
    }

    private static KeyStore.PrivateKeyEntry getPrivateKeyEntry(String alias) {
        KeyStore.Entry entry=null;
        try {
            KeyStore ks = KeyStore
                    .getInstance("AndroidKeyStore");
            ks.load(null);
            entry = ks.getEntry(alias, null);

            if (entry == null) {
                return null;
            }

            if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                return null;
            }
            return (KeyStore.PrivateKeyEntry) entry;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return (KeyStore.PrivateKeyEntry)entry;
    }

    public static String decrypt(byte[] text, String alias) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            PrivateKey privateKey = getPrivateKeyEntry(alias).getPrivateKey();

            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String(dectyptedText);
    }

    public void createNewKeys(String alias,Context context) throws InvalidAlgorithmParameterException {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        // expires 1 year from today
        end.add(Calendar.YEAR, 1);

        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(new X500Principal("CN=" + alias))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();
        KeyPairGenerator gen = null;
        try {
            gen = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        gen.initialize(spec);

        // generates the keypair
        gen.generateKeyPair();
    }
}
