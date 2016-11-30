package ist.meic.sirs.securechildlocator;

import java.security.NoSuchAlgorithmException;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import android.content.ContentResolver;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.widget.Button;
import android.widget.Toast;

public class Utils {
    public static String SHA256 (String text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    public static String encode (String text) throws NoSuchAlgorithmException {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }
    public static String getTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, +2);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(c.getTime());
    }
    public static void errorHandling(String error, Context context, Button button) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, error, duration);
        toast.show();
        button.setEnabled(true);
    }

    public static String getPhoneID(TelephonyManager tm, ContentResolver contentResolver){
        //get android unique id

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }
}
