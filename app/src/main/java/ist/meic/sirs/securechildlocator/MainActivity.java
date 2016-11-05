package ist.meic.sirs.securechildlocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {


    public void login(){
        Crypto crypto=new Crypto();
        String email=crypto.SHA256(_emailText.getText().toString());
        String password= crypto.SHA256(_passwordText.getText().toString());
        String credentials = email + ":" + password;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
