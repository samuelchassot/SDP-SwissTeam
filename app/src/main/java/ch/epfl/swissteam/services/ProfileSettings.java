package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Activity to modify the profile in the database
 * It shows the user infos and they can be edited
 *
 * @Author Samuel Chassot
 */
public class ProfileSettings extends NavigationDrawer {

    private String imageUrl_; //TODO: Allow user to change picture in his profile.
    private ArrayList<Categories> userCapabilities_ = new ArrayList<>();
    private HashMap<String, ArrayList<String>> keyWords_ = new HashMap<>();
    private RecyclerView recycler;
    private User oldUser_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_settings);
        super.onCreateDrawer(CANCEL);

        String uniqueID = GoogleSignInSingleton.get().getClientUniqueID();
        loadAndShowUser(uniqueID);

        recycler = findViewById(R.id.recyclerview_profilesettings_categories);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //don't want the keyboard automatically opens when activity starts
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    /**
     * Save the modification done by the user
     */
    private void save() {
        String name = ((TextView) findViewById(R.id.edittext_profilesettings_name)).getText().toString();
        String uniqueID = GoogleSignInSingleton.get().getClientUniqueID();
        String email = ((TextView) findViewById(R.id.textview_profilesettings_email)).getText().toString();
        String descr = ((TextView) findViewById(R.id.edittext_profilesettings_description)).getText().toString();

        ArrayList<Categories> categoriesThatHaveBeenRemoved = oldUser_.getCategories_();
        categoriesThatHaveBeenRemoved.removeAll(userCapabilities_);
        for(String cat : keyWords_.keySet()){
            if(!userCapabilities_.contains(Categories.fromString(cat))){
                keyWords_.remove(cat);
            }
        }

        User updatedUser = new User(uniqueID, name, email, descr, userCapabilities_, keyWords_, oldUser_.getChatRelations_(),
                imageUrl_, oldUser_.getRating_(), oldUser_.getLatitude_(), oldUser_.getLongitude_(), oldUser_.getUpvotes_(), oldUser_.getDownvotes_(), oldUser_.getIsShownLocation_());


        for (Categories c : categoriesThatHaveBeenRemoved){
            DBUtility.get().getCategory(c, (cat)->{
                cat.removeUser(uniqueID);
                cat.addToDB(DBUtility.get().getDb_());
            });
        }


        updatedUser.addToDB(DBUtility.get().getDb_());
        finish();
    }

    /**
     * Updates the status of a capability for a user.
     *
     * @param cat capability to update
     * @param checked whether to add or remove the capability
     */
    public void updateUserCapabilities(Categories cat, boolean checked){
        if(checked){
            //add category to the user's list
            if (!userCapabilities_.contains(cat)) {
                userCapabilities_.add(cat);
            }
        } else {
            //remove it from user's list
            if (userCapabilities_.contains(cat)) {
                userCapabilities_.remove(cat);
            }
        }
    }

    /**
     * add keywords in the map for the given category
     * @param cat the category for which add the keywords
     * @param keyWords the String containing the keywords separated by a ;
     */
    public void addKeyWords(Categories cat, String keyWords){
        ArrayList<String> kW = new ArrayList<>(Arrays.asList(keyWords.split(";")));
        if (keyWords_.containsKey(cat.toString())) {
            keyWords_.remove(cat.toString());
        }
        Log.i("PROFILESETTINGS","keywords added for " + cat.toString());
        keyWords_.put(cat.toString(), kW);
    }

    private void loadAndShowUser(String clientUniqueID) {
        DBUtility.get().getUser(clientUniqueID, (user) -> {
            TextView nameView = (TextView) findViewById(R.id.edittext_profilesettings_name);
            nameView.setText(user.getName_());

            TextView emailView = (TextView) findViewById(R.id.textview_profilesettings_email);
            emailView.setText(user.getEmail_());

            TextView descrView = (TextView) findViewById(R.id.edittext_profilesettings_description);
            descrView.setText(user.getDescription_());

            oldUser_ = user;

            Picasso.get().load(user.getImageUrl_()).into((ImageView)findViewById(R.id.imageview_profilesettings_picture));
            imageUrl_ = user.getImageUrl_();
            userCapabilities_.clear();
            userCapabilities_.addAll(user.getCategories_());
            keyWords_ = user.getKeyWords_();
            
            recycler.setAdapter(new CategoriesAdapterProfileSettings(Categories.realCategories(), userCapabilities_, keyWords_));

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_save){
            save();
            return true;
        }

        /*
         //noinspection SimplifiableIfStatement
         if (id == R.id.action_settings) {
         return true;
         }
         */

        return super.onOptionsItemSelected(item);
    }

}
