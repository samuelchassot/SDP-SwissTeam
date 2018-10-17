package ch.epfl.swissteam.services;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String clientUID = getIntent().getStringExtra(GOOGLE_ID_TAG);
        loadAndShowUser(clientUID);
    }

    // TODO : This is nearly a duplicate from the one in ProfileDisplayFragment
    private void loadAndShowUser(String clientUniqueID){
        //for now we use the username
        DBUtility.get().getUser(clientUniqueID, (user)->{
            TextView nameView = findViewById(R.id.textView_profile_nameTag);
            nameView.setText(user.getName_());

            TextView emailView =  findViewById(R.id.textView_profile_email);
            emailView.setText(user.getEmail_());

            TextView descrView =  findViewById(R.id.textView_profile_description);
            descrView.setText(user.getDescription_());

        });
    }
}
