package ch.epfl.swissteam.services;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;

/**
 * Activity which displays a {@link Post} and various informations about it
 *
 * @author Julie Giunta
 */
public class PostActivity extends NavigationDrawer implements OnMapReadyCallback {

    private static final String POST_MAPVIEW_BUNDLE_KEY = "PostMapViewBundleKey";

    private Post post_;
    private User user_;
    private View headerLayout_;
    private TextView username_, title_, body_, date_;
    private ImageView picture_;
    private double postLng_, postLat_;

    private GoogleMap googleMap_;
    private MapView mapView_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        super.onCreateDrawer(BACK);

        //Retrieve the post from the intent which started this activity
        Intent callingIntent = getIntent();
        post_ = callingIntent.getParcelableExtra(PostAdapter.POST_TAG);

        //Retrieve the location of the post
        postLng_ = post_.getLongitude_();
        postLat_ = post_.getLatitude_();

        //Connect Google Maps view
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(POST_MAPVIEW_BUNDLE_KEY);
        }

        mapView_ = findViewById(R.id.mapview_postactivity);
        mapView_.onCreate(mapViewBundle);
        mapView_.getMapAsync(this);

        //Connect the element of the layout with the corresponding attribute
        headerLayout_ = findViewById(R.id.framelayout_postactivity_header);
        username_ = findViewById(R.id.textview_postactivity_username);
        title_ = findViewById(R.id.textview_postactivity_title);
        body_ = findViewById(R.id.textview_postactivity_body);
        date_ = findViewById(R.id.textview_postactivity_date);
        picture_ = findViewById(R.id.imageview_postactivity_picture);

        //Set the onClickListener of the linearLayout
        headerLayout_.setOnClickListener(view -> {
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

        //Location of the post
        Location postLocation = new Location("");
        postLocation.setLongitude(postLng_);
        postLocation.setLatitude(postLat_);

        Location userLocation = LocationManager.get().getCurrentLocation_();

        if(userLocation != null) {
            float distance = postLocation.distanceTo(userLocation) / 1000;
            ((TextView)findViewById(R.id.textview_postactivity_distance)).setText(this.getResources().getString(R.string.homefragment_postdistance, distance));
        }
        else {
            ((TextView)findViewById(R.id.textview_postactivity_distance)).setText(this.getResources().getString(R.string.homefragment_postdistance, LocationManager.MAX_POST_DISTANCE / LocationManager.M_IN_ONE_KM));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap_ = googleMap;
        googleMap_.setMinZoomPreference(12);

        // Add a marker where the post is.
        LatLng postLatLng = new LatLng(postLat_, postLng_);
        googleMap_.addMarker(new MarkerOptions().position(postLatLng).title(post_.getTitle_()));
        googleMap_.moveCamera(CameraUpdateFactory.newLatLng(postLatLng));
    }
}
