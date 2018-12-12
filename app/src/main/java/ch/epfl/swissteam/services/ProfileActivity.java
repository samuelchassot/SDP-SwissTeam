package ch.epfl.swissteam.services;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.epfl.swissteam.services.NewProfileDetailsActivity.GOOGLE_ID_TAG;
import static ch.epfl.swissteam.services.User.Vote.DOWNVOTE;
import static ch.epfl.swissteam.services.User.Vote.UPVOTE;

/**
 * An activity to display the profile of a user
 *
 * @author Ghali Chraïbi
 */
public class ProfileActivity extends NavigationDrawerActivity implements OnMapReadyCallback {


    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private GoogleMap googleMap_;
    private MapView mapView_;
    private Marker marker_;
    private User user_;
  
    private RecyclerView mRecyclerView_;
    private LinearLayoutManager mLayoutManager_;
    private CategoriesAdapterProfileActivity mAdapter_;
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
            Intent intent = new Intent(this, ChatRoomActivity.class);
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

            mAdapter_ = new CategoriesAdapterProfileActivity(mCapabilities_, mKeyWords_);
            mRecyclerView_.setAdapter(mAdapter_);
        }


        loadAndShowUser(clientUID);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView_ = findViewById(R.id.mapview_profileactivity);
        mapView_.onCreate(mapViewBundle);
        mapView_.getMapAsync(this);
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
            user_ = user;

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

            showMap(user.getIsShownLocation_());

        });
    }

    private void showMap(boolean isShownLocation){
        if(isShownLocation){
            MapView m = findViewById(R.id.mapview_profileactivity);
            m.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams p = m.getLayoutParams();
            p.height = 500;
            m.setLayoutParams(p);
            //findViewById(R.id.mapview_profileactivity).setMinimumHeight(160);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        googleMap_ = googleMap;
        googleMap_.setMinZoomPreference(12);

        // Add a marker where the user is.
        LatLng userLatLng = new LatLng(user_.getLatitude_(), user_.getLongitude_());
        marker_ = googleMap_.addMarker(new MarkerOptions().position(userLatLng).title(user_.getName_()));
        googleMap_.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView_.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView_.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView_.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView_.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView_.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView_.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView_.onSaveInstanceState(mapViewBundle);
    }

    /**
     * !Just for testing!
     * Methods to get the marker corresponding to the location of the user
     * in the Google Maps view.
     * @return the marker of the user
     */
    protected Marker getMarker(){
        return marker_;
    }
}

