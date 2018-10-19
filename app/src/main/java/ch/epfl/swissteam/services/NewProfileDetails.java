package ch.epfl.swissteam.services;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * This activity is used to set up the account details of a new user.
 *
 * @author Adrian Baudat
 */
public class NewProfileDetails extends AppCompatActivity {

    public static final String GOOGLE_ID_TAG = "GOOGLE_ID", USERNAME_TAG = "USERNAME", EMAIL_TAG = "EMAIL", DESCRIPTION_TAG = "DESCRIPTION", IMAGE_TAG = "IMAGE";
    public static final String DEFAULT_IMAGE_URL = "https://i.stack.imgur.com/34AD2.jpg";

    private String googleID_, username_, email_, description_, imageUrl_;
    private Uri pictureURL_;
    private boolean saveToDB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_details);
        findViewById(R.id.imageview_newprofiledetails_picture).setTag(R.string.all_next);

        setTitle(getResources().getString(R.string.newprofile_title));

        GoogleSignInAccount account = getIntent().getParcelableExtra(SignInActivity.ACCOUNT_TAG);
        if(account != null) {
            saveToDB = true;

            findAndSetName(account);
            findAndSetPicture(account);

            googleID_ = account.getId();
            pictureURL_ = account.getPhotoUrl();
            email_ = account.getEmail();
            description_ = "";
            if(account.getPhotoUrl() != null) {
                imageUrl_ = account.getPhotoUrl().toString();
            }
            else{
                imageUrl_ = DEFAULT_IMAGE_URL;
            }

            ((EditText)findViewById(R.id.plaintext_newprofiledetails_description)).addTextChangedListener(new TextWatcher() {
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
        }
    }

    /**
     * If the given Google account has a photo set, this will use Picasso to load it into the
     * {@link ImageView} for the picture.
     *
     * @param account Google account to use
     */
    private void findAndSetPicture(GoogleSignInAccount account) {
        if(account.getPhotoUrl() != null) {
            Picasso.get().load(account.getPhotoUrl()).into((ImageView)findViewById(R.id.imageview_newprofiledetails_picture));
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
        if(account.getDisplayName() != null && !account.getDisplayName().equals("")) {
            setName(account.getDisplayName());
        }
        else if(account.getGivenName() != null && account.getFamilyName() != null) {
            setName(account.getGivenName() + " " + account.getFamilyName());
        }
    }

    /**
     * Sets the text in the name {@link EditText}.
     *
     * @param name new name
     */
    private void setName(String name) {
        ((EditText)findViewById(R.id.plaintext_newprofiledetails_name)).setText(name);
        username_ = name;
    }

    /**
     * Sets the picture in the {@link ImageView}.
     *
     * @param img new image bitmap
     */
    private void setPicture(Bitmap img) {
        ((ImageView)findViewById(R.id.imageview_newprofiledetails_picture)).setImageBitmap(img);
    }

    /**
     * Starts the capabilities activity.
     *
     * @param view view
     */
    public void nextScreen(View view) {
        Intent intent = new Intent(this, NewProfileCapabilities.class);
        intent.putExtra(GOOGLE_ID_TAG, googleID_);
        intent.putExtra(USERNAME_TAG, username_);
        intent.putExtra(EMAIL_TAG, email_);
        intent.putExtra(DESCRIPTION_TAG, description_);
        intent.putExtra(IMAGE_TAG, imageUrl_);
        startActivity(intent);
    }

    /**
     * Changes the picture by allowing the user to fetch a picture from his device.
     *
     * @param view view
     */
    public void changePicture(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galleryIntent.putExtra("return-data", true);
        startActivityForResult(galleryIntent, 1);
    }

    /**
     * Sets the picture bitmap to a bitmap obtained by letting the user choose an image on his device.
     * This will rotate the bitmap so that it is upright.
     *
     * @param requestCode request code
     * @param resultCode result code
     * @param data data
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                bitmap = ImageUtils.modifyOrientation(bitmap, getContentResolver().openInputStream(data.getData()));
                setPicture(bitmap);
            } catch (IOException e) {
                Log.w("BITMAP_CREATING", "Problem creating bitmap from activity result.");
                e.printStackTrace();
            }
        }
    }


}
