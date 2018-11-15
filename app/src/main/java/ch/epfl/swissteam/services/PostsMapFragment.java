package ch.epfl.swissteam.services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment containing a map showing all surrounding posts.
 *
 * @author Adrian Baudat
 */
public class PostsMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKeyPosts";

    private GoogleMap googleMap_;
    private MapView mapView_;

    /**
     * Create a new instance of this fragment
     *
     * @return new instance of the fragment
     */
    public static PostsMapFragment newInstance() {
        PostsMapFragment fragment = new PostsMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_postsmap, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView_ = view.findViewById(R.id.mapview_postsmap);
        mapView_.onCreate(mapViewBundle);
        mapView_.getMapAsync(this);

        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        menu.setGroupEnabled(R.id.group_switchtoposts, true);
        menu.setGroupVisible(R.id.group_switchtoposts, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_switchtoposts) {
            ((MainActivity)getActivity()).showHomeFragment();
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
        googleMap_.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        googleMap_.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            return false;
        });
        googleMap_.setOnInfoWindowClickListener(marker -> {
            Post post = (Post)marker.getTag();
            Intent intent = new Intent(getContext(), PostActivity.class);
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

            DBUtility.get().getPostsFeed(new DBCallBack<ArrayList<Post>>() {
                @Override
                public void onCallBack(ArrayList<Post> value) {
                    for (Post post : value) {
                        Marker newMarker = googleMap_.addMarker(new MarkerOptions().position(new LatLng(post.getLatitude_(), post.getLongitude_())));
                        newMarker.setTag(post);
                    }
                }
            }, currentLocation, new SettingsDbHelper(getContext()));
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
            DBUtility.get().getAllUsers(list -> {
                for(User u : list) {
                    users.put(u.getGoogleId_(), u);
                    Picasso.get().load(u.getImageUrl_()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            pictures.put(u.getGoogleId_(), bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                }
            });
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
