package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This activity is meant to fill the user profile with service capabilities at the first connection
 *
 * @author Sébastien Gachoud
 * @author Adrian Baudat
 */
public class NewProfileCapabilities extends AppCompatActivity {

    private ArrayList<Categories> capabilitiesList_ = new ArrayList<>();
    private HashMap<String, ArrayList<String>> keyWords_ = new HashMap<>();
    private String googleID_, username_, email_, description_, imageUrl_;
    private boolean isShownLocation_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_capabilites);

        Intent intent = getIntent();

        googleID_ = intent.getStringExtra(NewProfileDetails.GOOGLE_ID_TAG);
        username_ = intent.getStringExtra(NewProfileDetails.USERNAME_TAG);
        email_ = intent.getStringExtra(NewProfileDetails.EMAIL_TAG);
        description_ = intent.getStringExtra(NewProfileDetails.DESCRIPTION_TAG);
        imageUrl_ = intent.getStringExtra(NewProfileDetails.IMAGE_TAG);
        isShownLocation_ = intent.getBooleanExtra(NewProfileDetails.SHOW_LOCATION_TAG, false);

        RecyclerView recycler = findViewById(R.id.recyclerview_newprofilecapabilities_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new CategoriesAdapterNewProfileCapabilities(Categories.values()));
    }

    /**
     * Save the categories the user checked to the DB and go to the next screen
     *
     * @param view view
     */
    public void nextPage(View view) {
        saveUserInDB();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Saves the newly created user in the database.
     */
    private void saveUserInDB() {

        User user = new User(googleID_, username_, email_, description_, capabilitiesList_, keyWords_,
                null, imageUrl_, 0, 0, 0, null, null, isShownLocation_);
        user.addToDB(DBUtility.get().getDb_());

    }

    /**
     * Add a capability to the user.
     *
     * @param capability capability to add.
     */
    public void addCapability(Categories capability) {
        capabilitiesList_.add(capability);
    }

    /**
     * Add a list of keyWords for the given categories
     *
     * @param capability capability for which add the keywords.
     * @param keyWords   String containing the keywords separated by ;
     */
    public void addKeyWords(Categories capability, String keyWords) {
        ArrayList<String> kW = new ArrayList<>(Arrays.asList(keyWords.split(";")));
        if (keyWords_.containsKey(capability.toString())) {
            keyWords_.remove(capability.toString());
        }
        Log.i("ADDKEYWORD", "key words for " + capability.toString() + " added");
        keyWords_.put(capability.toString(), kW);

    }

    /**
     * Remove a capability to the user.
     *
     * @param capability capability to remove.
     */
    public void removeCapability(Categories capability) {
        if (capabilitiesList_.contains(capability)) {
            capabilitiesList_.remove(capability);
        }
    }

    /**
     * Remove a list of keyWords for the given categories
     *
     * @param capability capability for which remove the keywords.
     */
    public void removeKeyWords(Categories capability) {
        if (keyWords_.containsKey(capability.toString())) {
            keyWords_.remove(capability.toString());
        }
    }
}