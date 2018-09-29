package ch.epfl.swissteam.services;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class NewProfileCapabilites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_capabilites);
    }

    public void addNewActivity(View view) {
        Button testButton = new Button(this);
        testButton.setText("test");
        LinearLayout ll = findViewById(R.id.mainScrollViewLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                                                );
        ll.addView(testButton, lp);
    }
}
