package ist.meic.sirs.securechildlocator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        ssl.writeToServer(result);
        String read=ssl.readFromServer();
        if(read.contains("Error")){
            ssl.closeSocket();
            return;
        }
        ssl.closeSocket();

        /*
        String name =

        _greetingText.setText("Hello" + name ); //add username

        */
        _buttonFind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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
