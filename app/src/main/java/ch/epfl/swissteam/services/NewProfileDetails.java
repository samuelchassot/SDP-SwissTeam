package ch.epfl.swissteam.services;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
     * Sets the picture in the picture <code>ImageView</code>
     *
     * @param img new image
     */
    private void setPicture(Drawable img) {
        ((ImageView)findViewById(R.id.imageview_newprofiledetails_picture)).setImageDrawable(img);
    }

    public void nextScreen(View view) {
        //TODO: Save changes made by the user here.
        Intent intent = new Intent(this, NewProfileCapabilites.class);
        startActivity(intent);
    }
}
