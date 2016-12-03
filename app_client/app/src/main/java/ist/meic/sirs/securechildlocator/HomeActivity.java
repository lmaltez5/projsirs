package ist.meic.sirs.securechildlocator;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends AppCompatActivity {

    @InjectView(R.id.greeting_text) TextView _greetingText;
    @InjectView(R.id.button_find) Button _buttonFind;
    @InjectView(R.id.button_manage) Button _buttonManage;
    @InjectView(R.id.button_logout) Button _buttonLogout;

    String sessionEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        sessionEmail = getIntent().getStringExtra("EMAIL").replace( "\n", "" );;

        String result="6;" + sessionEmail + ";" +Utils.getTime();
        SSLClient ssl =new SSLClient(getApplicationContext());
        String read= Utils.connectSSL(getApplicationContext(), result, ssl, null);
        ssl.closeSocket();
        byte[] decodedBytes = Base64.decode(read.getBytes(),Base64.DEFAULT);
        _greetingText.setText("Hello" + decodedBytes.toString() ); //add username


        _buttonFind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(getBaseContext(), SelectFind.class);
                //send session varables
                intent.putExtra("EMAIL",sessionEmail);
                startActivity(intent);
                finish();

            }
        });

        _buttonManage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        _buttonLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
