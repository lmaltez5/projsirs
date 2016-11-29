package ist.meic.sirs.securechildlocator;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SetupActivity extends AppCompatActivity {

    @InjectView(R.id.button_legalguardian) Button _legalguardianbutton;
    @InjectView(R.id.button_child) Button _childbutton;

    String sessionEmail;
    String sessionUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.inject(this);

        //get sessions variables
        sessionEmail = getIntent().getStringExtra("EMAIL");
        sessionUser = getIntent().getStringExtra("USER");

        //get android unique id
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(this.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        final String deviceId = deviceUuid.toString();

       _legalguardianbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String email = Crypto.SHA256(sessionEmail).replace("\n", "");
                    String phoneID = Crypto.SHA256(deviceId);

                    //send to server
                    String result = "4;"+ phoneID +";" + email + ";" + TimeStamp.getTime();

                    SSLClient ssl = new SSLClient(getApplicationContext());
                    ssl.writeToServer(result);

                    // FALTA RECEBER O OK DO SERVER


                    ssl.closeSocket();

                    // Goto Home
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    //send session varables
                    intent.putExtra("EMAIL", sessionEmail);
                    intent.putExtra("USER", sessionUser);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        _childbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String email = Crypto.SHA256(sessionEmail).replace("\n", "");
                    String phoneID = Crypto.SHA256(deviceId);

                    //send to server
                    String result = "3;"+ phoneID +";" + email + ";" + TimeStamp.getTime();

                    SSLClient ssl = new SSLClient(getApplicationContext());
                    ssl.writeToServer(result);

                    // FALTA RECEBER O OK DO SERVER

                    ssl.closeSocket();

                    finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });
    }
}
