package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;

/**
 * Activity which displays a {@link Post} and various informations about it
 *
 * @author Julie Giunta
 */
public class PostActivity extends NavigationDrawer{
    private Post post_;
    private User user_;
    private LinearLayout linearLayout_;
    private TextView username_, title_, body_, date_;
    private ImageView picture_;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        super.onCreateDrawer(BACK);

        //Retrieve the post from the intent which started this activity
        Intent callingIntent = getIntent();
        post_ = callingIntent.getParcelableExtra(PostAdapter.POST_TAG);

        //Connect the element of the layout with the corresponding attribute
        linearLayout_ = findViewById(R.id.linearlayout_postactivity_user);
        username_ = findViewById(R.id.textview_postactivity_username);
        title_ = findViewById(R.id.textview_postactivity_title);
        body_ = findViewById(R.id.textview_postactivity_body);
        date_ = findViewById(R.id.textview_postactivity_date);
        picture_ = findViewById(R.id.imageview_postactivity_picture);

        //Set the onClickListener of the linearLayout
        linearLayout_.setOnClickListener(view -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra(GOOGLE_ID_TAG, post_.getGoogleId_());
            startActivity(profileIntent);
        });

        //Set the different fields

        title_.setText(post_.getTitle_());
        body_.setText(post_.getBody_());
        date_.setText((new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)).format(new Date(post_.getTimestamp_()).getTime()));
        //Retrieve the user
        DBUtility.get().getUser(post_.getGoogleId_(), (user -> {
            user_ = user;
            username_.setText(user_.getName_());
            Picasso.get().load(user_.getImageUrl_()).into(picture_);
        }));
    }
}
