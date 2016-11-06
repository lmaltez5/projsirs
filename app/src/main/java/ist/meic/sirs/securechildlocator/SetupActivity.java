package ist.meic.sirs.securechildlocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SetupActivity extends AppCompatActivity {

    @InjectView(R.id.button_legalguardian) Button _legalguardianbutton;
    @InjectView(R.id.button_child) Button _childbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        ButterKnife.inject(this);

        _legalguardianbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //SET LEGAL
            }
        });

        _childbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //SET CHILD
            }
        });
    }
}
