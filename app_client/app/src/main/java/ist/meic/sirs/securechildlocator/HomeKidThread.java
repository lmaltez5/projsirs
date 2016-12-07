package ist.meic.sirs.securechildlocator;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HomeKidThread implements Runnable {

    private Context context;
    private String sessionEmail;
    private String sessionKey;
    private String sessionPhoneID;
    private GPSTracker gps;
    double latitude = 200.0; //doesnt exist (caps at 180)
    double longitude = 200.0; //doesnt exist (caps at 180)

    public HomeKidThread(Context context, String sessionKey, String sessionPhoneID, String sessionEmail, GPSTracker gps){
        this.sessionEmail = sessionEmail;
        this.sessionKey = sessionKey;
        this.sessionPhoneID = sessionPhoneID;
        this.gps = gps;
        this.context = context;
    }

    public void run() {
        SSLClient ssl = new SSLClient(context);
        ssl.setSoTimeout();
        InputStream inputstream = ssl.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedReader = new BufferedReader(inputstreamreader);
        while (true) {
            waitForRequest(bufferedReader);
        }
    }

    private void waitForRequest(BufferedReader bufferedReader) {
        try {
            String read = null;
            read = bufferedReader.readLine();

            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                String result = "10;" + sessionKey + ";" + sessionPhoneID + ";" + sessionEmail + ";" + latitude
                        + ";" + longitude + ";" + Utils.getTime();
            }
            if ((latitude != 200.0) && (longitude != 200.0)) {
                String result = "10;" + sessionKey + ";" + sessionPhoneID + ";" + sessionEmail + ";" + latitude
                        + ";" + longitude + ";" + Utils.getTime();
            }
            else {
                String result = "error";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
