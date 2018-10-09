package ch.epfl.swissteam.services;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileSettings extends AppCompatActivity {

    private ArrayList<String> currentCategories;

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


        String uniqueID = GoogleSignInSingleton.get().getClientUniqueID();
        loadAndShowUser(uniqueID);
    }


    /**
     * Save the modification done by the user
     */
    private void save(){
        String name = ((TextView) findViewById(R.id.edittext_profilesettings_name)).getText().toString();
        String uniqueID = GoogleSignInSingleton.get().getClientUniqueID();
        String surname =((TextView) findViewById(R.id.edittext_profilesettings_surname)).getText().toString();
        String email = ((TextView) findViewById(R.id.edittext_profilesettings_email)).getText().toString();
        String descr = ((TextView) findViewById(R.id.edittext_profilesettings_description)).getText().toString();
        User updatedUser = new User(uniqueID, name, surname, email, descr, currentCategories);

        DBUtility.get().setUser(updatedUser);
        finish();
    }

    private void loadAndShowUser(String clientUniqueID){
        DBUtility.get().getUser(clientUniqueID, (user)->{
            TextView nameView = (TextView) findViewById(R.id.edittext_profilesettings_name);
            nameView.setText(user.getName_());

            TextView surnameView =  (TextView) findViewById(R.id.edittext_profilesettings_surname);
            surnameView.setText(user.getSurname_());

            TextView emailView =  (TextView) findViewById(R.id.edittext_profilesettings_email);
            emailView.setText(user.getEmail_());

            TextView descrView =  (TextView) findViewById(R.id.edittext_profilesettings_description);
            descrView.setText(user.getDescription_());

            currentCategories = user.getCategories_();


        });
    }


}
