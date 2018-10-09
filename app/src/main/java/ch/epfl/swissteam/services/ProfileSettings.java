package ch.epfl.swissteam.services;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Button saveButton = (Button)findViewById(R.id.button_profilesettings_save);

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save();

            }
        });
    }


    /**
     * Save the modification done by the user
     */
    private void save(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
