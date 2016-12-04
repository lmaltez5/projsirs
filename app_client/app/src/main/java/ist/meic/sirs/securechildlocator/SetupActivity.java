package ist.meic.sirs.securechildlocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SetupActivity extends AppCompatActivity {

    @InjectView(R.id.button_legalguardian) Button _legalguardianbutton;
    @InjectView(R.id.button_child) Button _childbutton;
    @InjectView(R.id.phone_name)  TextView _phone_name;

    private String sessionEmail;
    private String sessionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.inject(this);

        //get sessions variables
        sessionEmail = getIntent().getStringExtra("EMAIL");
        sessionKey=getIntent().getStringExtra("SESSIONKEY");

        //get android unique id
        final String deviceId = Utils.getPhoneID((TelephonyManager) getBaseContext().getSystemService(this.TELEPHONY_SERVICE),getContentResolver());

       _legalguardianbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    System.err.println(deviceId);
                    String phoneID = Utils.SHA256(deviceId);
                    String phone_name =  _phone_name.getText().toString().replace( "\n", "" );
                    //send to server
                    String result = "4;"+ sessionKey+";"+ phoneID +";" + sessionEmail + ";"+ phone_name +";"+ Utils.getTime();

                    SSLClient ssl = new SSLClient(getApplicationContext());
                    Utils.connectSSL(getApplicationContext(),result,ssl,_legalguardianbutton);
                    ssl.closeSocket();

                    // Goto Home
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    //send session varables
                    intent.putExtra("EMAIL", sessionEmail);
                    intent.putExtra("SESSIONKEY", sessionKey);
                    intent.putExtra("ID", phoneID);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        _childbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String phoneID = Utils.SHA256(deviceId);
                    String phone_name =  _phone_name.getText().toString().replace( "\n", "" );

                    //send to server
                    String result = "3;"+ sessionKey+";"+ phoneID +";" + sessionEmail + ";"+ phone_name +";"+ Utils.getTime();
                    SSLClient ssl = new SSLClient(getApplicationContext());
                    Utils.connectSSL(getApplicationContext(),result,ssl,_childbutton);
                    ssl.closeSocket();
                    // Goto Home
                    Intent intent = new Intent(getBaseContext(), HomeKidActivity.class);
                    //send session varables
                    intent.putExtra("EMAIL", sessionEmail);
                    intent.putExtra("SESSIONKEY", sessionKey);
                    intent.putExtra("ID", phoneID);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });
    }
}
