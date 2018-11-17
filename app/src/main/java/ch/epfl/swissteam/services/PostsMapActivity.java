package ch.epfl.swissteam.services;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity to show a map with markers corresponding to posts
 *
 * @author Adrian Baudat
 */
public class PostsMapActivity extends NavigationDrawer implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKeyPosts";

    private GoogleMap googleMap_;
    private MapView mapView_;
    private CustomInfoWindowAdapter infoWindow;
    private List<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postsmap);
        super.onCreateDrawer(NavigationDrawer.MAIN);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView_ = findViewById(R.id.mapview_postsmap_map);
        mapView_.onCreate(mapViewBundle);
        mapView_.getMapAsync(this);

        infoWindow = new PostsMapActivity.CustomInfoWindowAdapter();

        invalidateOptionsMenu();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean toReturn = super.onPrepareOptionsMenu(menu);
        menu.setGroupEnabled(R.id.group_switchtoposts, true);
        menu.setGroupVisible(R.id.group_switchtoposts, true);
        menu.setGroupEnabled(R.id.group_refresh, true);
        menu.setGroupVisible(R.id.group_refresh, true);
        return toReturn;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_switchtoposts) {
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(id == R.id.action_refresh) {
            updateMapView();
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap_ = googleMap;
        googleMap_.setMinZoomPreference(12);
        googleMap_.moveCamera(CameraUpdateFactory.zoomTo(12));
        googleMap_.setMinZoomPreference(6);
        googleMap_.setMaxZoomPreference(20);
        googleMap_.setInfoWindowAdapter(infoWindow);
        googleMap_.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            return false;
        });
        googleMap_.setOnInfoWindowClickListener(marker -> {
            Post post = (Post)marker.getTag();
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra(PostAdapter.POST_TAG, post);
            startActivity(intent);
        });
        updateMapView();
    }

    private void updateMapView() {
        Location currentLocation = LocationManager.get().getCurrentLocation_();

        if(currentLocation != null) {
            LatLng newLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            googleMap_.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));

            for(Marker marker : markers) {
                marker.remove();
            }

            DBUtility.get().getPostsFeed(new DBCallBack<ArrayList<Post>>() {
                @Override
                public void onCallBack(ArrayList<Post> value) {
                    for (Post post : value) {
                        MarkerOptions newMarkerOptions = new MarkerOptions().position(new LatLng(post.getLatitude_(), post.getLongitude_()));
                        DBUtility.get().getUser(post.getGoogleId_(), user -> {
                            Picasso.get().load(user.getImageUrl_()).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    infoWindow.addUser(post.getGoogleId_(), user, bitmap);
                                    Marker newMarker = googleMap_.addMarker(newMarkerOptions);
                                    newMarker.setTag(post);
                                    markers.add(newMarker);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                        });
                    }
                }
            }, currentLocation, new SettingsDbHelper(this));
        }
    }

    /**
     * Info window to show when a marker is clicked on the map
     *
     * @author Adrian Baudat
     */
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View mContents_;

        private Map<String, User> users = new HashMap<>();
        private Map<String, Bitmap> pictures = new HashMap<>();

        CustomInfoWindowAdapter() {
            mContents_ = getLayoutInflater().inflate(R.layout.mapinfo_window, null);
        }

        public void addUser(String googleId, User user, Bitmap picture){
            users.put(googleId, user);
            pictures.put(googleId, picture);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            Post post = (Post)marker.getTag();
            ((TextView)mContents_.findViewById(R.id.textview_mapinfo_post)).setText(post.getTitle_());
            if(users.containsKey(post.getGoogleId_())) {
                User user = users.get(post.getGoogleId_());
                ((TextView) mContents_.findViewById(R.id.textview_mapinfo_name)).setText(user.getName_());
            }
            else{
                ((TextView) mContents_.findViewById(R.id.textview_mapinfo_name)).setText(getResources().getString(R.string.mapinfo_unknownuser));
            }
            if(pictures.containsKey(post.getGoogleId_())) {
                Bitmap bitmap = pictures.get(post.getGoogleId_());
                ((ImageView) mContents_.findViewById(R.id.imageview_mapinfo_picture)).setImageBitmap(bitmap);
            }
            else{
                ((ImageView) mContents_.findViewById(R.id.imageview_mapinfo_picture)).setImageBitmap(null);
            }
            return mContents_;
        }
    }
}
