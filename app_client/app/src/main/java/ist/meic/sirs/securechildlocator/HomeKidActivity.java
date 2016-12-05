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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeKidActivity extends AppCompatActivity {
    @InjectView(R.id.button_logout) Button _button_logout;
    @InjectView(R.id.input_password) TextView _input_password;

    String sessionEmail;
    String sessionKey;
    String sessionPhoneID;

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


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


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
            String result="2;" + sessionEmail + ";" + input_password+";"+Utils.getTime();
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
