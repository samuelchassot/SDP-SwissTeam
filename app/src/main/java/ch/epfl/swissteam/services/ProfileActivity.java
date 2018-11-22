package ch.epfl.swissteam.services;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;
import static ch.epfl.swissteam.services.User.Vote.DOWNVOTE;
import static ch.epfl.swissteam.services.User.Vote.UPVOTE;

/**
 * An activity to display the profile of a user
 *
 * @author Ghali Chraïbi
 */
public class ProfileActivity extends NavigationDrawer {

    private RecyclerView mRecyclerView_;
    private LinearLayoutManager mLayoutManager_;
    private CapabilitiesAdapter mAdapter_;
    private List<Categories> mCapabilities_ = new ArrayList<Categories>();
    private Map<String, List<String>> mKeyWords_ = new HashMap<>();

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

        mRecyclerView_ = (RecyclerView) this.findViewById(R.id.recyclerview_profileactivity_capabilities);
        if (mRecyclerView_ != null) {
            mRecyclerView_.setHasFixedSize(true);

            mLayoutManager_ = new LinearLayoutManager(this);
            mRecyclerView_.setLayoutManager(mLayoutManager_);

            mAdapter_ = new CapabilitiesAdapter(mCapabilities_, mKeyWords_);
            mRecyclerView_.setAdapter(mAdapter_);
        }

        loadAndShowUser(clientUID);
    }

    private void voteStoreAndRefresh(User.Vote vote, String clientUID){
        DBUtility.get().getUser(clientUID, user ->{
            DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(), currentUser ->{
                user.vote(vote, currentUser);
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

            Picasso.get().load(user.getImageUrl_()).into((ImageView) findViewById(R.id.imageview_profile_picture));

            int[] attrs = new int[] { R.attr.thumbs_up_grey, R.attr.thumbs_down_grey, R.attr.star_grey};
            TypedArray ta = this.getTheme().obtainStyledAttributes(attrs);

            if (user.getUpvotes_().contains(GoogleSignInSingleton.get().getClientUniqueID())){
                findViewById(R.id.button_profile_upvote).setBackgroundResource(R.drawable.thumbs_up_blue);
            } else {
                findViewById(R.id.button_profile_upvote).setBackgroundResource(ta.getResourceId(0,0));
            }
            if (user.getDownvotes_().contains(GoogleSignInSingleton.get().getClientUniqueID())){
                findViewById(R.id.button_profile_downvote).setBackgroundResource(R.drawable.thumbs_down_red);
            } else {
                findViewById(R.id.button_profile_downvote).setBackgroundResource(ta.getResourceId(1,0));
            }

            mCapabilities_.clear();
            mCapabilities_.addAll(user.getCategories_());
            mKeyWords_.clear();
            for(Categories c : user.getCategories_()){
                mKeyWords_.put(c.toString(), user.getKeyWords(c));
            }
            if (mAdapter_ != null) {
                mAdapter_.notifyDataSetChanged();
                Log.i("PROFILEACTIVITY", "notify dataset changed");
                Log.i("PROFILEACTIVITY", "n of categories = " + mCapabilities_.size());
            }


            int rating = user.getRating_();
            ImageView starView[] = new ImageView[5];
            starView[0] = findViewById(R.id.imageview_usersearchlayout_star0);
            starView[1] = findViewById(R.id.imageview_usersearchlayout_star1);
            starView[2] = findViewById(R.id.imageview_usersearchlayout_star2);
            starView[3] = findViewById(R.id.imageview_usersearchlayout_star3);
            starView[4] = findViewById(R.id.imageview_usersearchlayout_star4);

            for (int i = 0; i < 5;i++){
                if (rating >= User.RATING_[i]){
                    starView[i].setBackgroundResource(R.drawable.star_yellow);
                } else {
                    starView[i].setBackgroundResource(ta.getResourceId(2,0));
                }
            }
            ta.recycle();






        });
    }
}
