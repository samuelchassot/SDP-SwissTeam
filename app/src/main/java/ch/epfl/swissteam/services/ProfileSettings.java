package ch.epfl.swissteam.services;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileSettings extends AppCompatActivity {

    private ArrayList<Categories> userCapabilities_ = new ArrayList<>();
    private RecyclerView recycler;

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

        recycler = findViewById(R.id.recyclerview_profilesettings_categories);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));




    }


    /**
     * Save the modification done by the user
     */
    private void save(){
        String name = ((TextView) findViewById(R.id.edittext_profilesettings_name)).getText().toString();
        String uniqueID = GoogleSignInSingleton.get().getClientUniqueID();
        String email = ((TextView) findViewById(R.id.edittext_profilesettings_email)).getText().toString();
        String descr = ((TextView) findViewById(R.id.edittext_profilesettings_description)).getText().toString();
        User updatedUser = new User(uniqueID, name, email, descr, userCapabilities_);

        DBUtility.get().setUser(updatedUser);
        finish();
    }

    public void updateUserCapabilities(Categories cat, boolean checked){
        if(checked){
            //add category to the user's list
            if(! userCapabilities_.contains(cat)){
                userCapabilities_.add(cat);
            }
        }
        else{
            //remove it from user's list
            if(userCapabilities_.contains(cat)){
                userCapabilities_.remove(cat);
            }
        }
    }


    private void loadAndShowUser(String clientUniqueID){
        DBUtility.get().getUser(clientUniqueID, (user)->{
            TextView nameView = (TextView) findViewById(R.id.edittext_profilesettings_name);
            nameView.setText(user.getName_());

            TextView emailView =  (TextView) findViewById(R.id.edittext_profilesettings_email);
            emailView.setText(user.getEmail_());

            TextView descrView =  (TextView) findViewById(R.id.edittext_profilesettings_description);
            descrView.setText(user.getDescription_());

            userCapabilities_.clear();
            userCapabilities_.addAll(user.getCategories_());
            
            recycler.setAdapter(new CategoriesAdapterProfileSettings(Categories.values(), userCapabilities_));



        });
    }


}
