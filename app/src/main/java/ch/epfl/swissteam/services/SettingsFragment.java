package ch.epfl.swissteam.services;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * A fragment to set the different settings of the application
 *
 * @author Ghali Chraïbi
 */
public class SettingsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap_;
    private MapView mapView_;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment
     *
     * @return the settings fragment
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView_ = view.findViewById(R.id.mapview_settings);
        mapView_.onCreate(mapViewBundle);
        mapView_.getMapAsync(this);

        Button setHome = view.findViewById(R.id.button_settings_sethome);
        setHome.setOnClickListener(v -> {
            // TODO : Change the home location in the Database and call to update the map
            Location userLocation = LocationManager.get().getCurrentLocation_();
            LatLng currentLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            googleMap_.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        });

        Button deleteAccountButton = (Button) view.findViewById(R.id.button_settings_deleteaccount);
        deleteAccountButton.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), DeleteAccountActivity.class);
            v.getContext().startActivity(intent);
        });


        return view;
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
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        googleMap_.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
