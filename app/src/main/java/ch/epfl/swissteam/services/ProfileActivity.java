package ch.epfl.swissteam.services;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;

/**
 * An activity to display the profile of a user
 *
 * @author Ghali Chraïbi
 */
public class ProfileActivity extends NavigationDrawer {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        super.onCreateDrawer(BACK);

        String clientUID = getIntent().getStringExtra(GOOGLE_ID_TAG);

        Button chatButton = findViewById(R.id.button_profile_toChat);
        Button upvoteButton = findViewById(R.id.button_profile_upvote);
        Button downvoteButton = findViewById(R.id.button_profile_downvote);

        if (clientUID.equals(GoogleSignInSingleton.get().getClientUniqueID())) {
            chatButton.setVisibility(View.INVISIBLE);
            upvoteButton.setVisibility(View.INVISIBLE);
            downvoteButton.setVisibility(View.INVISIBLE);
        }

        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatRoom.class);
            intent.putExtra(GOOGLE_ID_TAG, clientUID);
            this.startActivity(intent);
        });

        upvoteButton.setOnClickListener(v -> voteStoreAndRefresh(UPVOTE, clientUID));

        downvoteButton.setOnClickListener(v -> voteStoreAndRefresh(DOWNVOTE, clientUID));


        loadAndShowUser(clientUID);
    }

    private final int UPVOTE = 1;
    private final int DOWNVOTE = 0;
    private void voteStoreAndRefresh(int vote, String clientUID){
        DBUtility.get().getUser(clientUID, user ->{
            DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(), currentUser ->{
                if (vote == DOWNVOTE){
                    user.downvote(currentUser);
                } else if (vote == UPVOTE){
                    user.upvote(currentUser);
                }
                user.addToDB(DBUtility.get().getDb_());
                loadAndShowUser(user.getGoogleId_());
            });
        });
    }


    private void loadAndShowUser(String clientUniqueID) {
        //for now we use the username
        DBUtility.get().getUser(clientUniqueID, (user) -> {
            TextView nameView = findViewById(R.id.textView_profile_nameTag);
            nameView.setText(user.getName_());

            TextView emailView = findViewById(R.id.textView_profile_email);
            emailView.setText(user.getEmail_());

            TextView descrView = findViewById(R.id.textView_profile_description);
            descrView.setText(user.getDescription_());

            TextView ratingView = findViewById(R.id.textView_profile_rating);
            ratingView.setText(Integer.toString(user.getRating_()));

            Picasso.get().load(user.getImageUrl_()).into((ImageView) findViewById(R.id.imageview_profile_picture));
            if (user.getUpvotes_().contains(GoogleSignInSingleton.get().getClientUniqueID())){
                findViewById(R.id.button_profile_upvote).setBackgroundResource(R.drawable.thumbs_up_blue);
            } else {
                findViewById(R.id.button_profile_upvote).setBackgroundResource(R.drawable.thumbs_up);
            }
            if (user.getDownvotes_().contains(GoogleSignInSingleton.get().getClientUniqueID())){
                findViewById(R.id.button_profile_downvote).setBackgroundResource(R.drawable.thumbs_down_red);
            } else {
                findViewById(R.id.button_profile_downvote).setBackgroundResource(R.drawable.thumbs_down);
            }




        });
    }
}
