package ist.meic.sirs.securechildlocator;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeKidActivity extends AppCompatActivity {
    @InjectView(R.id.button_logout) Button _button_logout;
    @InjectView(R.id.input_password) TextView _input_password;

    String sessionEmail;
    String sessionKey;
    String sessionPhoneID;
    GPSTracker gps;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_kid);
        ButterKnife.inject(this);
        sessionEmail = getIntent().getStringExtra("EMAIL").replace("\n", "");
        sessionKey = getIntent().getStringExtra("SESSIONKEY").replace("\n", "");
        sessionPhoneID = getIntent().getStringExtra("ID").replace("\n", "");
        _button_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // sendLocation();
    }

    private void sendLocation(){
        gps = new GPSTracker(this);
        while(true){
            waitForRequest();
        }
    }

    private void waitForRequest() {
        try {
            String read = null;
            SSLClient ssl =new SSLClient(getApplicationContext());
            InputStream inputstream = ssl.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedReader = new BufferedReader(inputstreamreader);
            while((read= bufferedReader.readLine()) == null){

            }
            if(gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }
            String result = "6;" + sessionKey +";"+sessionPhoneID+";"+sessionEmail+ ";" +Utils.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String input_password = _input_password.getText().toString().replace( "\n", "" );

        if (input_password.isEmpty() || input_password.length() < 4 || input_password.length() > 10) {
            _input_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _input_password.setError(null);
        }

        return valid;
    }

    public void logout(){
        try{

            String input_password = Utils.SHA256(_input_password.getText().toString()).replace("\n","");
            if (!validate()) {
                return;
            }
            String result="2;" + sessionEmail + ";" + sessionPhoneID + ";" + input_password+";"+Utils.getTime();
            SSLClient ssl =new SSLClient(getApplicationContext());
            Utils.connectSSL(getApplicationContext(),result,ssl,_button_logout);

            result="8;" + sessionKey +";"+sessionPhoneID+";"+sessionEmail+";"+Utils.getTime();
            Utils.connectSSL(getApplicationContext(),result,ssl,_button_logout);
            ssl.closeSocket();

            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
