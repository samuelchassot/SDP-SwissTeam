package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity is meant to fill the user profile with service capabilities at the first connection
 *
 * @author Sébastien Gachoud
 * @author Adrian Baudat
 */

public class NewProfileCapabilities extends AppCompatActivity {

    List<Categories> capabilitiesList_ = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_capabilites);

        RecyclerView recycler = findViewById(R.id.recyclerview_newprofilecapabilities_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new CategoriesAdapter(Categories.values()));
    }

    /**
     * Save the categories the user checked to the DB and go to the next screen
     *
     * @param view view
     */
    public void nextPage(View view) {
        for(Categories capability : capabilitiesList_) {
            DBUtility.get().getDb_().child(DBUtility.CATEGORIES).child(capability.toString()).child(GoogleSignInSingleton.get().getClientUniqueID()).setValue("true");
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Add a capability to the user.
     *
     * @param capability capability to add.
     */
    public void addCapability(Categories capability) {
        capabilitiesList_.add(capability);
    }
}