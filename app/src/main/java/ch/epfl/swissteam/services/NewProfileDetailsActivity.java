package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

/**
 * This activity is used to set up the account details of a new user.
 *
 * @author Adrian Baudat
 */
public class NewProfileDetailsActivity extends AppCompatActivity {

    public static final String GOOGLE_ID_TAG = "GOOGLE_ID", USERNAME_TAG = "USERNAME",
            EMAIL_TAG = "EMAIL", DESCRIPTION_TAG = "DESCRIPTION", IMAGE_TAG = "IMAGE", SHOW_LOCATION_TAG = "isShownLocation";
    public static final String DEFAULT_IMAGE_URL = "https://i.stack.imgur.com/34AD2.jpg";

    private String googleID_, username_, email_, description_, imageUrl_;
    private boolean isShownLocation_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_details);
        findViewById(R.id.imageview_newprofiledetails_picture).setTag(R.string.general_next);

        setTitle(getResources().getString(R.string.newprofile_title));

        GoogleSignInAccount account = getIntent().getParcelableExtra(SignInActivity.ACCOUNT_TAG);
        if (account != null) {

            findAndSetName(account);

            googleID_ = account.getId();
            email_ = account.getEmail();
            description_ = "";
            if (account.getPhotoUrl() != null) {
                imageUrl_ = account.getPhotoUrl().toString();
            } else {
                imageUrl_ = DEFAULT_IMAGE_URL;
            }

            Picasso.get().load(imageUrl_).into((ImageView) findViewById(R.id.imageview_newprofiledetails_picture));

            ((EditText) findViewById(R.id.plaintext_newprofiledetails_description)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    description_ = editable.toString();
                }
            });

            setUpLocationSwitch();
        }
    }

    /**
     * Tries to find the name used by the Google account and sets it to the name {@link EditText}.
     * This will prioritize the display name of the account, and if non-existent will create a name
     * with the form "FirstName LastName". This will do nothing if both names don't exist.
     *
     * @param account Google account to use
     */
    private void findAndSetName(GoogleSignInAccount account) {
        if (account.getDisplayName() != null && !account.getDisplayName().equals("")) {
            setName(account.getDisplayName());
        } else if (account.getGivenName() != null && account.getFamilyName() != null) {
            setName(account.getGivenName() + " " + account.getFamilyName());
        }
    }

    /**
     * Sets the text in the name {@link EditText}.
     *
     * @param name new name
     */
    private void setName(String name) {
        ((EditText) findViewById(R.id.plaintext_newprofiledetails_name)).setText(name);
        username_ = name;
    }

    /**
     * Starts the capabilities activity.
     *
     * @param view view
     */
    public void nextScreen(View view) {
        Intent intent = new Intent(this, NewProfileCapabilitiesActivity.class);
        intent.putExtra(GOOGLE_ID_TAG, googleID_);
        intent.putExtra(USERNAME_TAG, username_);
        intent.putExtra(EMAIL_TAG, email_);
        intent.putExtra(DESCRIPTION_TAG, description_);
        intent.putExtra(IMAGE_TAG, imageUrl_);
        intent.putExtra(SHOW_LOCATION_TAG, isShownLocation_);
        startActivity(intent);
    }

    /**
     * Set up the switch for the attribute isShownLocation_
     */
    private void setUpLocationSwitch(){
        ((Switch)findViewById(R.id.switch_newprofiledetails_showlocation)).setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if(isChecked){
                        isShownLocation_ = true;
                    }else{
                        isShownLocation_ = false;
                    }
                }
        );
    }
}
