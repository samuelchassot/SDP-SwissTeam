package ch.epfl.swissteam.services;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This activity is used to set up the account details of a new user.
 *
 * @author Adrian Baudat
 */
public class NewProfileDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_details);

        setTitle(getResources().getString(R.string.newprofile_title));

        GoogleSignInAccount account = getIntent().getParcelableExtra("account");
        if(account != null) {
            findAndSetName(account);
            findAndSetPicture(account);
        }
    }

    /**
     * If the given Google account has a photo set, this will use Picasso to load it into the
     * <code>ImageView</code> for the picture.
     *
     * @param account Google account to use
     */
    private void findAndSetPicture(GoogleSignInAccount account) {
        if(account.getPhotoUrl() != null) {
            Picasso.get().load(account.getPhotoUrl()).into((ImageView)findViewById(R.id.imageview_newprofiledetails_picture));
        }
    }

    /**
     * Tries to find the name used by the Google account and sets it to the name <code>PlainText</code>.
     * This will prioritize the display name of the account, and if non-existent will create a name
     * with the form "FirstName LastName". This will do nothing if both names don't exist.
     *
     * @param account Google account to use
     */
    private void findAndSetName(GoogleSignInAccount account) {
        if(account.getDisplayName() != null) {
            setName(account.getDisplayName());
        }
        else if(account.getGivenName() != null && account.getFamilyName() != null) {
            setName(account.getGivenName() + " " + account.getFamilyName());
        }
    }

    /**
     * Sets the text in the name <code>TextView</code>.
     *
     * @param name new name
     */
    private void setName(String name) {
        ((EditText)findViewById(R.id.plaintext_newprofiledetails_name)).setText(name);
    }

    /**
     * Sets the picture in the <code>ImageView</code>
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
        //TODO: Save changes made by the user here.
        Intent intent = new Intent(this, NewProfileCapabilities.class);
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
