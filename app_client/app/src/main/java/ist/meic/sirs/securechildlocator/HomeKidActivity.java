package ist.meic.sirs.securechildlocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.InjectView;

public class HomeKidActivity extends AppCompatActivity {
    @InjectView(R.id.button_logout) Button _button_logout;
    @InjectView(R.id.input_password) TextView _input_password;

    String sessionEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_kid);
        sessionEmail = getIntent().getStringExtra("EMAIL").replace( "\n", "" );

        _button_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String input_password = _input_password.getText().toString().replace( "\n", "" );;

        if (input_password.isEmpty() || input_password.length() < 4 || input_password.length() > 10) {
            _input_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _input_password.setError(null);
        }

        return valid;
    }

    public void logout(){
        String input_password = _input_password.getText().toString().replace( "\n", "" );;
        if (!validate()) {
            return;
        }
        String result="2;" + sessionEmail + ";" + input_password+";"+Utils.getTime();
        SSLClient ssl =new SSLClient(getApplicationContext());
        ssl.writeToServer(result);
        String read=ssl.readFromServer();
        if(read.contains("Error")){
            Utils.errorHandling(read,getApplicationContext(),_button_logout);
            ssl.closeSocket();
            return;
        }
        ssl.closeSocket();
        finish();
    }
}
