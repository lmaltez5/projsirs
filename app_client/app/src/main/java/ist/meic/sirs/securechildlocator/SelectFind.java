package ist.meic.sirs.securechildlocator

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectFind extends Activity {

    // Array of strings...
    String[] phonesNames;
    String sessionEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        sessionEmail = getIntent().getStringExtra("EMAIL").replace( "\n", "" );
        String email = Utils.SHA256(sessionEmail).replace( "\n", "" );
        
        //FAZ GET DOS NOMES
        String result="7;" + email +";"+Utils.getTime();
        SSLClient ssl =new SSLClient(getApplicationContext());
        ssl.writeToServer(result);
        String read=ssl.readFromServer();
        if(read.contains("Error")){
            Utils.errorHandling(read,getApplicationContext(),_button_logout);
            ssl.closeSocket();
            return;
        }
        ssl.closeSocket();
        
       
        //CONSTROI ARRAY DE STRINGS
        



        if (phonesNames != null) {

                final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                        R.layout.activity_listview, phonesNames);

                ListView listView = (ListView) findViewById(R.id.mobile_list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        String item = adapter.getItem(position).toString();
                    }
                });
            }
        else {
            // got to Home
            Toast.makeText(getBaseContext(), "Don have any kid", Toast.LENGTH_LONG).show();
            final Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
