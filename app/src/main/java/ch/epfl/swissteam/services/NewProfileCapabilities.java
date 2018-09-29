package ch.epfl.swissteam.services;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class NewProfileCapabilities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_capabilites);
    }

    public void addNewActivity(View view) {
        Spinner newSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.domains_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newSpinner.setAdapter(adapter);

        LinearLayout ll = findViewById(R.id.mainScrollViewLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                                                );

        ll.addView(newSpinner, lp);
    }
}
